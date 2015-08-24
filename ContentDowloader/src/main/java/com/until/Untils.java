package com.until;

import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/17/2015.
 */
public class Untils {
    static private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.125 Safari/537.36";
    private static final String cmsLogin = "/cms/login.jsp?error=2&action=expired";
    static Semaphore semaphore = new Semaphore(1);
    private static String cookies;
    static CookieStore httpCookieStore = new BasicCookieStore();
    //static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    static HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();


    public static void innit() {
        CookieHandler.setDefault(new CookieManager());
        // Increase max total connection to 200

    }

    public static String getHtmlFromURL(String url) {
        StringBuilder resultString = new StringBuilder();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpGet httpGet = new HttpGet(url);


        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent()), "UTF-8"));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                resultString.append(output);
            }

            httpClient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
        }
        return resultString.toString();
    }

    public static String getDirfromLink2(String fileLink) {
        String outLink = fileLink.replaceAll("\\\\[^\\\\]+$", "");

        return outLink;
    }

    public static String getDirfromLink(String fileLink) {
        String outLink = fileLink.replaceAll("/[^/]+$", "");
        outLink = outLink.replaceAll("^\\.[^/]+/", "");
        return outLink;
    }

    public static String getFileNameFromLink(String fileLink) {
        return fileLink.replaceAll("^.+/", "");
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
                                    autoLogIn(result);
                                }
                                semaphore.release();
                            }else{
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
        }

    }

    public static String getFullDowloadLink(String workingLink, String link) {
        Pattern pre = Pattern.compile("^\\.\\./");
        Matcher find = pre.matcher(link);
        String domain = workingLink;
        if (find.find()) {
            domain = domain.replaceAll("[^/]+/$", "");
        }

        return domain + link.replaceAll("\\.\\./", "");
    }

    public static String getWorkingLinkFromLink(String resourceDataLink) {
        return resourceDataLink.replaceAll("[^/]+$", "");
    }

    public static String getFileExtFromLink(String fileLink) {
        Pattern ext = Pattern.compile("(\\.[^\\.]+$)");
        Matcher matcher = ext.matcher(fileLink);
        matcher.find();
        return matcher.group(1);
    }

    public static String postHtmlFromURL(String Link, List<NameValuePair> formData) {


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
        client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();

        try {
            response = client.execute(post, context);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Response Code : "
        //        + response.getStatusLine().getStatusCode());
        //Header[] headers = response.getHeaders("jsessionid");
        //setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :
        //       response.getFirstHeader("Set-Cookie").toString());

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

        List<URI> redirectURIs = context.getRedirectLocations();
        if (redirectURIs != null) {
            try {
                semaphore.acquire();
                if (!login) {
                    URI finalURI = redirectURIs.get(redirectURIs.size() - 1);
                    String uri = finalURI.toString();
                    if (uri.contains(cmsLogin)) {
                        autoLogIn(result);
                    }
                }
                semaphore.release();
                return postHtmlFromURL(Link, formData);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }


        return result.toString();


    }

    static boolean login = false;

    private static void autoLogIn(StringBuffer result) {
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

        result = new StringBuffer();
        String line = "";
        try {
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void detectAndDowImg(String mainContent, String outPutdir, String workingLink) {
        Pattern imgPattern = Pattern.compile("<img src=\"([^\"]+)\"");
        Pattern pdfPattern = Pattern.compile("<a\\s+href=\"((?!http)([^\"]+).pdf)\"");
        List<String> Links = new ArrayList<>();
        Matcher matcher = imgPattern.matcher(mainContent);
        while (matcher.find()) {
            if (!Links.contains(matcher.group(1))) {
                Links.add(matcher.group(1));
            }
        }
        matcher = pdfPattern.matcher(mainContent);
        while (matcher.find()) {
            if (!Links.contains(matcher.group(1))) {
                Links.add(matcher.group(1));
            }
        }
        for (String link : Links) {
            String fullLink = Untils.getFullDowloadLink(workingLink, link);
            String dirFomLink = Untils.getDirfromLink(link);
            File imgfolder = new File(outPutdir + "\\" + dirFomLink);
            if (!imgfolder.exists()) {
                imgfolder.mkdirs();
            }
            String fileName = Untils.getFileNameFromLink(link);
            Untils.dowloadFile(fullLink, outPutdir + "\\" + dirFomLink + "\\" + fileName);
        }
    }

    public static void coppyDir(String inputAssets, String outputPre) {
        File targetFolder = new File(inputAssets);
        File desFolder = new File(outputPre);
        if (!desFolder.exists()) {
            desFolder.mkdirs();
        }
        try {
            FileUtils.copyDirectory(targetFolder, desFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}