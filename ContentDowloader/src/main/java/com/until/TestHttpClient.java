package com.until;

import com.colectcontent.MyTask;
import com.colectcontent.SimpleThread;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 8/3/2015.
 */
public class TestHttpClient {
    static Semaphore semaphore = new Semaphore(1);
    static CookieStore httpCookieStore = new BasicCookieStore();
    static HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();
    private static boolean login = false;
    static private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.125 Safari/537.36";
    private static final String cmsLogin = "/cms/login.jsp?error=2&action=expired";

    static public void innit() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
        client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setConnectionManager(cm).setRedirectStrategy(new LaxRedirectStrategy()).build();
    }

    static public String callGetMethod(String url) {
        StringBuilder resultString = new StringBuilder();
        HttpGet httpGet = null;
        httpGet = new HttpGet(url);
        HttpResponse response = null;

        HttpClientContext context = HttpClientContext.create();
        try {
            response = client.execute(httpGet, context);
            List<URI> redirectURIs = context.getRedirectLocations();
            HttpEntity entity = response.getEntity();
            response.getEntity();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent()), "UTF-8"));
            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                resultString.append(output);
            }
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
        if (!login) {
            List<URI> redirectURIs = context.getRedirectLocations();
            if (redirectURIs != null) {
                try {
                    URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
                    String uri = finalURI.toString();
                    if (uri.contains(cmsLogin)) {
                        semaphore.acquire();
                        if (!login) {
                            autoLogIn(resultString.toString());
                        }
                        semaphore.release();
                        return callGetMethod(url);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            List<URI> redirectURIs = context.getRedirectLocations();
            if (redirectURIs != null) {
                try {
                    URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
                    String uri = finalURI.toString();
                    if (uri.contains(cmsLogin)) {
                        semaphore.acquire();
                        login = false;
                        semaphore.release();
                        return callGetMethod(url);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resultString.toString();
    }

    public static String callPostMethod(String Link, List<NameValuePair> formData) {
        StringBuffer result = new StringBuffer();
        HttpPost post = new HttpPost(Link);
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        post.setHeader("Accept-Language", "en-US,en;q=0.5");
        //post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Upgrade-Insecure-Requests", "1");
        post.setHeader("Host", "192.168.2.12:8090");
        post.setHeader("Referer", "http://192.168.2.12:8090/cms/home.html");

        HttpClientContext context = HttpClientContext.create();
        // add header
        try {
            post.setEntity(new UrlEncodedFormEntity(formData));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = client.execute(post, context);
            BufferedReader rd = null;
            try {
                rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            String line = "";
            try {
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }


        if (!login) {
            List<URI> redirectURIs = context.getRedirectLocations();
            if (redirectURIs != null) {
                try {
                    URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
                    String uri = finalURI.toString();
                    if (uri.contains(cmsLogin)) {
                        semaphore.acquire();
                        autoLogIn(result.toString());
                        semaphore.release();
                        return callPostMethod(Link, formData);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            List<URI> redirectURIs = context.getRedirectLocations();
            if (redirectURIs != null) {
                try {
                    URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
                    String uri = finalURI.toString();
                    if (uri.contains(cmsLogin)) {
                        semaphore.acquire();
                        login = false;
                        semaphore.release();
                        return callPostMethod(Link, formData);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result.toString();
    }

    public static void dowloadFile(String link, String dir) {
        String getdomain = link.replaceAll("/[^/]+$", "/");
        String getparam = link.replaceAll("^.+/", "");

        String safeUrl = null;
        try {
            safeUrl = getdomain + URLEncoder.encode(getparam, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = null;
        httpGet = new HttpGet(link);
        HttpResponse response = null;


        HttpClientContext context = HttpClientContext.create();
        client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();
        try {
            response = client.execute(httpGet, context);


            HttpEntity entity = response.getEntity();
            response.getEntity();
            File makeDir = new File(getDirfromLink(dir));
            if (!makeDir.exists()) {
                makeDir.mkdirs();
            }
            if (entity != null) {
                OutputStream outputStream = new FileOutputStream(dir);
                InputStream inputStream = entity.getContent();
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            } else {
                System.out.println("Error! cannot download file " + dir);
            }


        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Error Link: ------" + link);
        } finally {
            httpGet.releaseConnection();
        }


        List<URI> redirectURIs = context.getRedirectLocations();
        if (redirectURIs != null) {
            //if server rediarect you and the link not reconize as loginplace. it mush be the file you waint to dowload.
            URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
            String uri = finalURI.toString();

            try {
                semaphore.acquire();
                //System.out.println("Hit Critical!");
                if (uri.contains(cmsLogin)) {
                    if (!login) {
                        BufferedReader rd = null;
                        try {
                            rd = new BufferedReader(
                                    new InputStreamReader(response.getEntity().getContent()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        StringBuffer result = new StringBuffer();
                        String line = "";
                        try {
                            while ((line = rd.readLine()) != null) {
                                result.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        finalURI = redirectURIs.get(redirectURIs.size() - 1);
                        uri = finalURI.toString();
                        if (uri.contains(cmsLogin)) {
                            autoLogIn(result.toString());
                        }
                        semaphore.release();
                    } else {
                        semaphore.release();
                    }

                    dowloadFile(link, dir);


                } else {
                    System.out.println("Dowloading rediarect file");
                    dowloadFile(uri, dir);

                }
                semaphore.release();
                return;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }

    private static void autoLogIn(String result) {
        login = true;
        System.out.println("Log In to systems!");
        Pattern getLogF = Pattern.compile("<form name=\"loginForm\" action=\"([^\"]+)");
        Matcher matcher = getLogF.matcher(result);
        matcher.find();
        String loginForm = "http://192.168.2.12:8090" + matcher.group(1);
        List<NameValuePair> input = new ArrayList<>();
        input.add(new BasicNameValuePair("j_username", "thanh"));
        input.add(new BasicNameValuePair("j_password", "142536"));

        HttpPost post = new HttpPost(loginForm);
        HttpClientContext context = HttpClientContext.create();
        // add header
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        post.setHeader("Accept-Language", "en-US,en;q=0.5");
        //post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Upgrade-Insecure-Requests:", "1");
        post.setHeader("Host", "192.168.2.12:8090");
        post.setHeader("Referer", "http://192.168.2.12:8090/cms/home.html");


        try {
            post.setEntity(new UrlEncodedFormEntity(input));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();
        try {
            response = client.execute(post, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :
        //       response.getFirstHeader("Set-Cookie").toString());
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDirfromLink(String fileLink) {
        String outLink = fileLink.replaceAll("/[^/]+$", "");
        outLink = outLink.replaceAll("^\\.[^/]+/", "");
        return outLink;
    }

    public static void main(String[] args) {
        innit();

        for (int i = 0; i < 1000; i++) {
            final int finalI = i;
            SimpleThread task = new SimpleThread(new MyTask() {
                @Override
                public Object doInBackGround() {
                    String result = TestHttpClient.callGetMethod("http://192.168.2.12:8090/cms/product/list.html");
                    System.out.println(finalI);
                    return null;
                }

                @Override
                public void onSuccess(Object result) {

                }
            });
            task.startThread();

        }
    }

    public static String getFileExtFromLink(String fileLink) {
        Pattern ext = Pattern.compile("(\\.[^\\.]+$)");
        Matcher matcher = ext.matcher(fileLink);
        matcher.find();
        return matcher.group(1);
    }
}
