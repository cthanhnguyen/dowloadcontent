package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.GenerateTask;
import com.colectcontent.ResourceInfo;
import com.until.Untils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/17/2015.
 */
public class PraysEngGen extends GenerateTask {


    @Override
    protected void setUpWorkingGrade() {
        Grade = new String[]{"1","2","3","4","5","6","K"};
    }

    @Override
    protected void genarateFiles(ResourceInfo item) {
        String outPutdir= getOuputDirPre(item);

        StringWriter stringWriter = null;
        String audioTemplate ="";
        try{
            InputStream inputStream = new FileInputStream(new File("template\\audioTemplate.html"));
            stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter, "UTF-8");
        } catch (Exception e) {

        }
        audioTemplate =  stringWriter.toString();

        audioTemplate = audioTemplate.replace("#caption#", "");

        Pattern mp3L = Pattern.compile("load_audioPlayer\\(\"([^\"]+)\", \"prayer\"\\); ");

        Matcher mp3Matcher1 = mp3L.matcher(item.getMainContent());
        // download Mp3 under

        if(mp3Matcher1.find()){
            String outputDir =  outPutdir+"\\docs\\prayer\\"+mp3Matcher1.group(1);
            String dirFomLink = Untils.getDirfromLink2(outputDir);
            File imgfolder = new File(dirFomLink);
            if(!imgfolder.exists()){
                imgfolder.mkdirs();
            }
            String Link = "http://www.sadlierreligion.com/docs/prayer/"+mp3Matcher1.group(1);
            Untils.dowloadFile(Link, outputDir);

            audioTemplate = audioTemplate.replaceAll(Pattern.quote("#audiosrc#"),getAudioPrepix(item.getGrade())+item.getResourceCode()+"/"+"docs/prayer/"+mp3Matcher1.group(1));
        }
        item.setMainContent(item.getMainContent().replaceAll("(?s)<div class=\"audioPlayer\"> (.+?)</div>",audioTemplate));

        //new File(outPutdir).mkdirs();
        try {
            FileUtils.writeStringToFile(new File(outPutdir + "\\" + item.getResourceCode() + ".html"), item.getMainContent());
            Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(item.getMainContent());
            while(matcher.find()){
                String fileLink = matcher.group(1);
                final String dirFomLink = Untils.getDirfromLink(fileLink);
                File imgfolder = new File(outPutdir+"\\"+dirFomLink);
                if(!imgfolder.exists()){
                    imgfolder.mkdirs();
                }
                String fileName = Untils.getFileNameFromLink(fileLink);
                Untils.dowloadFile("http://www.sadlierreligion.com/" + fileLink, outPutdir + "\\" + dirFomLink + "\\" + fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getOuputDirPre(ResourceInfo item) {
        return "output\\"+item.getCategory()+"\\WB_EN_"+item.getGrade()+"\\"+item.getResourceCode();
    }

    protected String getAudioPrepix(String grade) {
        return "/content/WB_EN_"+grade+"/";
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


        fileTemplate = fileTemplate.replaceAll(Pattern.quote("#content#"), mainContent);
        fileTemplate = fileTemplate.replace("#additionscript#", "<script type=\"text/javascript\" src=\"//static.assets.sadlierconnect.com/sc-content/javascript/iword_vws15_v1.3.js\"> </script>\n");

        return fileTemplate;
    }

    @Override
    protected String genContentFromLink(String resourceDataLink) {
        String htmlItemSource = Untils.getHtmlFromURL(resourceDataLink);
        Pattern pattern = Pattern.compile("(?s)(<div class=\"prayer\">(.+?)</div>)<br clear=\"all\" />");
        Matcher matcher = pattern.matcher(htmlItemSource);
        matcher.find();
        String contentorigin = matcher.group(1);
        contentorigin = contentorigin.replaceAll("<div class=\"audioPlayer\"> (.+?)</div>","#audiotemplate#");
        return matcher.group(1);
    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        Map<String,ResourceInfo> mapSearch = new HashMap<>();
        for(ResourceInfo item:resourceInfoList){
            mapSearch.put(item.getTitle(),item);
        }

            String htmlSource = Untils.getHtmlFromURL("http://www.sadlierreligion.com/webelieve/prayers.cfm?sp=teacher&section=resource&grade=" + grade);
            Pattern pattern1 = Pattern.compile("(?s)</h3><br />[\\s,\\n]+(<ul>(.+?)</ul>)[\\s,\\n]+</div><br clear=\"all\" />[\\s,\\n]+</div><br clear=\"all\" />[\\s,\\n]+</div><br clear=\"all\" />");
            Matcher matcher1 = pattern1.matcher(htmlSource);

            matcher1.find();

            //Pattern pattern = Pattern.compile("(?i)(<tr class=\"row\\d\">(.+?)</tr>)");
            Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\">(.+?)</a>");
            final Matcher matcher = pattern.matcher(matcher1.group(1));
            Set<String> result = new HashSet<String>();
            while (matcher.find()){
                String Link = matcher.group(1);
                String title = matcher.group(2);
                ResourceInfo re = mapSearch.get(title);
                if(re!=null)
                {
                    re.setResourceDataLink("http://www.sadlierreligion.com"+Link);
                } else{
                    System.out.println("Error --- title : "+title+" not recornize !");
                }

            }


    }

    @Override
    protected String WorkingOnCategory() {
        return "Prayers";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_ENG\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        PraysEngGen task = new PraysEngGen();
        task.doWork();
    }
}
