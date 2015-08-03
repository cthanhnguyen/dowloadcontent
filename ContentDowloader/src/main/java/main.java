

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/7/2015.
 */
public class main {
    static Map<String,InputTemplate> templateMap = new HashMap<String,InputTemplate>();
    static  InputTemplate currentTemplate ;
    public static void main(String[] args) {
        templateMap.put("Postcards from Vatican City",new InputTemplate("Postcards from Vatican City","http://www.sadlierreligion.com/webelieve/postcardsfromvatican.cfm?sp=teacher&section=article&grade=K&isarchive=1","img src=\"(img/postcard/[^\"]+)\""));
        templateMap.put("Letters from Africa",new InputTemplate("Letters from Africa","http://www.sadlierreligion.com/webelieve/lettersfromafrica.cfm?sp=teacher&section=article&grade=4&isarchive=1","src=\"(img/upload/[^\"]+)\""));
        templateMap.put("Catholic Social Teaching",new InputTemplate("Catholic Social Teaching","http://www.sadlierreligion.com/webelieve/catholicsocialteaching.cfm?sp=teacher&section=article&grade=4","img src=\"(img/postcard/[^\"]+)\""));




        currentTemplate = templateMap.get("Letters from Africa");
        long starttime = System.currentTimeMillis();
        int count = DowloadImage();
        //int count = dowloadResource();

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - starttime;
        System.out.println("**************progress make :"+totalTime*0.001+"*********************");
        System.out.println("**************total resource :" + count + "*********************");

    }

    private static int dowloadResource() {
        String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/main.cfm?sp=teacher&section=activity&grade=1&language=sp");
        Set<String> resourceLink = DLRgetLinks(htmlSource);
        return 0;
    }

    private static Set<String> DLRgetLinks(String htmlSource) {
        final Map<String,String> resources = getTitleResource();
        Pattern pattern1 = Pattern.compile("(?s)(<dd><h3 id=\"Actividades para imprimir\">(.+?)</dd>)");
        Matcher matcher1 = pattern1.matcher(htmlSource);
        matcher1.find();

        //Pattern pattern = Pattern.compile("(?i)(<tr class=\"row\\d\">(.+?)</tr>)");
        Pattern pattern = Pattern.compile("(<a href=[^>]+>(.+?)</a>)");
        final Matcher matcher = pattern.matcher(matcher1.group(1));
        Set<String> result = new HashSet<String>();
        while (matcher.find()){
            final String[] LinkvsTitle = getResourceLink(matcher.group(1));
            SimpleThread work = new SimpleThread(new MyTask() {
                @Override
                public Object doInBackGround() {

                    String title = LinkvsTitle[1];
                    String Link = LinkvsTitle[0];
                    String ResourceCode = resources.get(title);
                    String htmlItemSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/" + Link);
                    Pattern patten = Pattern.compile("(?s)(<h1>Actividades para imprimir</h1>.+)<br[^>]+><br[^>]+>");
                    Matcher macherContent = patten.matcher(htmlItemSource);
                    macherContent.find();
                    StringWriter stringWriter = null;
                    try{
                        InputStream inputStream = new FileInputStream(new File("default.html"));
                        stringWriter = new StringWriter();
                        IOUtils.copy(inputStream, stringWriter, "UTF-8");
                    } catch (Exception e) {

                    }
                    String fileTemplate = stringWriter.toString();

                    String Content = macherContent.group(1);
                    fileTemplate = fileTemplate.replaceAll(Pattern.quote("#content#"), Content);
                    System.out.println(Content);
                    Content = Content.replaceAll("(?s)<div class=\"archiveBanner\">(.+?)</div>","");
                    DLRgenResourceOutput(fileTemplate,ResourceCode);
                    return null;
                }

                @Override
                public void onSuccess(Object result) {

                }
            });
            work.startThread();


        }
        return result;
    }



    private static void DLRgenResourceOutput(String content, String resourceCode) {
        String outPutdir="output\\"+resourceCode;
        String imgDir = "assets";

        new File(outPutdir).mkdir();
        try {
            FileUtils.writeStringToFile(new File(outPutdir+"\\"+resourceCode+".html"),content);
            Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(content);
            while(matcher.find()){
                String fileLink = matcher.group(1);
                final String dirFomLink = getDirfromLink(fileLink);
                File imgfolder = new File(outPutdir+"\\"+dirFomLink);
                if(!imgfolder.exists()){
                    imgfolder.mkdirs();
                }
                String fileName = getFileNameFromLink(fileLink);
                dowloadFile("http://www.sadlierreligion.com/webelieve/"+fileLink,outPutdir+"\\"+dirFomLink+"\\"+fileName);
            }
            //FileUtils.copyDirectory(new File(imgDir),new File(outPutdir));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String[] getResourceLink(String input) {
        String[] result = new String[2];
        Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\"[^>]+>([^<]+)");
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        result[0] = matcher.group(1);
        result[1] = matcher.group(2);
        return result;
    }

    private static int DowloadImage() {
        //String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=2&isarchive=1");
        //String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=3&isarchive=1");
        //String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=4&isarchive=1");

        //String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=5&isarchive=1");

        //String htmlSource = getHtmlFromURL("http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=K&isarchive=1");
        String htmlSource = getHtmlFromURL(currentTemplate.getHtmlLink());
        //http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=6&isarchive=1
        //

        Map<String,String> titleVsResource = getTitleResource();
        Map<String,List<String>> titleVsLinkMap = getTitleLink(htmlSource);
        final String outPutdir="output";
        new File(outPutdir).mkdir();
        int count = 0;
        for(String title: titleVsLinkMap.keySet()){

            String resource = "";
            resource = titleVsResource.get(title);
            List<String> imgLinks = titleVsLinkMap.get(title);
            if(resource==null){
                System.out.println("Error! *************** Can't find Resource of title " +title);
                continue;
            }
            System.out.println("Create resource:" +resource);
            count ++;
            new File(outPutdir+"\\"+resource).mkdir();
            new File(outPutdir+"\\"+resource+"\\img").mkdir();
            //new File(outPutdir+"\\"+resource+"\\img"+"\\article").mkdir();
            //dowloadFile("http://www.sadlierreligion.com/" + "img/article/type/7.jpg", outPutdir + "\\" + resource + "\\img\\article" + "\\" + "7.jpg");
            if(imgLinks!=null){
                //new File(outPutdir+"\\"+resource+"\\img"+"\\postcard").mkdir();
                for(final String link:imgLinks){
                    final String dirFomLink = getDirfromLink(link);
                    File imgfolder = new File(outPutdir+"\\"+resource+"\\"+dirFomLink);
                    if(!imgfolder.exists()){
                        imgfolder.mkdir();
                    }
                    final String fileName = getFileNameFromLink(link);
                    final String finalResource = resource;
                    SimpleThread thread = new SimpleThread(new MyTask() {

                        public Object doInBackGround() {
                            dowloadFile("http://www.sadlierreligion.com/webelieve/"+link.replaceAll("\\.\\./",""),outPutdir+"\\"+ finalResource+"\\"+dirFomLink+"\\"+fileName);
                            return null;
                        }


                        public void onSuccess(Object result) {
                            System.out.println("Dowload Done a img for Resource: " + finalResource);
                        }
                    });
                    thread.start();
                }


            }else {
                System.out.println("Warning! " + title + ": don't reconize author image");
            }

        }
        return count;
    }

    private static String getDirfromLink(String link) {
        String outLink = link.replaceAll("/[^/]+$","");
        outLink = outLink.replaceAll("^/","");
        return  outLink;

    }

    private static void dowloadFile(String link, String dir) {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        String getdomain = link.replaceAll("/[^/]+$","/");
        String getparam =  link.replaceAll("^.+/","");


        String safeUrl = null;
        try {
            safeUrl = getdomain + URLEncoder.encode(getparam, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = null;
        httpGet = new HttpGet(safeUrl);
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                response.getEntity();
                if (entity != null) {
                    OutputStream outputStream = new FileOutputStream(dir);
                    InputStream inputStream = entity.getContent();
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                }else{
                    System.out.println("Error! cannot download file "+dir);
                }


            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }


    }

    private static String getFileNameFromLink(String imgLink) {
        return imgLink.replaceAll("^.+/","");
    }

    private static Map<String, String> getTitleResource() {
        Map<String,String> result = new HashMap<String, String>();
        try {
            String file="E:\\Data\\code.xlsx";
            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }

            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row.getCell(3).getStringCellValue().equals("Actividades para imprimir")){
                    String resource = row.getCell(0).getStringCellValue();
                    String title = row.getCell(2).getStringCellValue();
                    System.out.println(resource+"--------"+title);
                    result.put(title,resource);
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    private static Map<String, List<String>> getTitleLink(String htmlSource) {
        Pattern pattern = Pattern.compile("(?i)(<tr class=\"row\\d\">(.+?)</tr>)");
        Matcher matcher = pattern.matcher(htmlSource);
        Map<String,List<String>> result = new HashMap<String, List<String>>();
        while (matcher.find()){
            Object[] pairTitleAndLink = getPairTitleAndLink(matcher.group(1));
            result.put((String)pairTitleAndLink[0],(List<String>)pairTitleAndLink[1]);
            System.out.println(pairTitleAndLink[0] + "----" + pairTitleAndLink[1]);
            //break;
        }
        return result;
    }

    private static Object[] getPairTitleAndLink(String input) {

        Pattern pattern = Pattern.compile("<td><a href=\"([^\"]+)\">(.+)</a>");
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        Object[] result = new Object[2];
        result[0] = matcher.group(2);

        result[1] = matcher.group(1);
        String resourceLink = "http://www.sadlierreligion.com"+result[1];
        String resourceHtml = getHtmlFromURL(resourceLink);
        List<String> resourceImg = getResourceAuthorImg(resourceHtml);
        if(resourceImg !=null){

        }else{
          System.out.println("***************************** Error ******"+ result[0] +"---Not Found Image!");
        }
        result[1] = resourceImg;
        return result;
    }

    private static List<String> getResourceAuthorImg(String resourceHtml) {
        String regex2 = "(?s)img src=\"(img/postcard/[^\"]+)\"";
        //Pattern pattern = Pattern.compile("(?s)<div id=\"authorBio\">[\\n,\\s]+<img src=\"([^\"]+)\"(.+?)</div>");
        Pattern pattern = Pattern.compile(currentTemplate.getRegexFindImage());
        Matcher matcher = pattern.matcher(resourceHtml);
        List<String> result = new ArrayList();
        while (matcher.find()){
            result.add(matcher.group(1));
        }

        return result;
    }

    private static String getHtmlFromURL(String url) {
        StringBuilder resultString = new StringBuilder();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent()),"UTF-8"));

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
        }
        return resultString.toString();
    }
}
