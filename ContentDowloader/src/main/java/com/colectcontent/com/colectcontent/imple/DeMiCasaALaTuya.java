package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.GenerateTask;
import com.colectcontent.ResourceInfo;
import com.until.Untils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/21/2015.
 */
public class DeMiCasaALaTuya extends GenerateTask {
    @Override
    protected void setUpWorkingGrade() {
        Grade = new String[]{"1","2","3","4","5","6","K"};
        //Grade = new String[]{"1"};
    }

    @Override
    protected void genarateFiles(ResourceInfo item) {
        String outPutdir="output\\"+item.getCategory()+"\\"+item.getGrade()+"\\"+item.getResourceCode();
        File outDir = new File(outPutdir);
        if(!outDir.exists()){
            outDir.mkdirs();
        }
        String workingLink = Untils.getWorkingLinkFromLink(item.getResourceDataLink());
        detectAndDowImg(item.getMainContent(),outPutdir,workingLink);
        String refacContent = item.getMainContent().replaceAll("\"\\.\\./","\"");
        refacContent = refacContent.replaceAll("\'\\.\\./", "'");
        try {
            FileUtils.writeStringToFile(new File(outPutdir + "\\" + item.getResourceCode() + ".html"), refacContent, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String midResourceGetFullContent(String mainContent) {
        StringWriter stringWriter = null;
        try{
            InputStream inputStream = new FileInputStream(new File("template\\default.html"));
            stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter, "UTF-8");
        } catch (Exception e) {

        }
        String fileTemplate = stringWriter.toString();
        fileTemplate = fileTemplate.replaceAll(Pattern.quote("#content#"),mainContent);
        fileTemplate = fileTemplate.replaceAll(Pattern.quote("#additionscript#"),"");
        return fileTemplate;
    }

    @Override
    protected String genContentFromLink(String resourceDataLink) {
        String htmlsource = Untils.getHtmlFromURL(resourceDataLink);
        Pattern getContent = Pattern.compile("(?s)<div id=\"article\">(.+?)</div>[\\s,\\n]+</div><br clear=\"all\" />[\\s,\\n]+</div><br clear=\"all\" />[\\s,\\n]+</div><br clear=\"all\" />");
        Matcher matcher = getContent.matcher(htmlsource);
        matcher.find();
        String content = matcher.group(1);
        content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        return content.replaceAll("<div class=\"archiveBanner\">(.+?)</div>","");
    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        Map<String,ResourceInfo> mapSearch = new HashMap<>();
        for(ResourceInfo item:resourceInfoList){
            mapSearch.put(item.getTitle(),item);
        }
        String Link = "http://www.sadlierreligion.com/webelieve/frommyhometoyours.cfm?language=sp&sp=teacher&section=article&grade=3&isarchive=1";
        String htmlSource = Untils.getHtmlFromURL(Link);

        Pattern pattern = Pattern.compile("(?i)(<tr class=\"row\\d\">(.+?)</tr>)");
        Matcher matcher = pattern.matcher(htmlSource);
        Map<String,List<String>> result = new HashMap<String, List<String>>();
        while (matcher.find()){
            Pattern pattern2 = Pattern.compile("<a href=\"([^\"]+)\">(.+)</a>");
            Matcher matcher2 = pattern2.matcher(matcher.group(1));
            matcher2.find();
            String title = matcher2.group(2);
            String link = matcher2.group(1);
            ResourceInfo re = mapSearch.get(title.trim());
            if(re!=null){
                re.setResourceDataLink("http://www.sadlierreligion.com"+link);
            }else{
                System.out.println("Error Title: "+title+" not reconize in mapping files");
            }

            //break;
        }
    }

    @Override
    protected String WorkingOnCategory() {
        return "De mi casa a la tuya";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        DeMiCasaALaTuya task = new DeMiCasaALaTuya();
        task.doWork();
    }
}
