package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.GenerateTask;
import com.colectcontent.ResourceInfo;
import com.until.Untils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.List;

/**
 * Created by vincent on 7/22/2015.
 */
public class AñoLitúrgico extends GenerateTask{
    @Override
    protected void readResourceCode(String grade, String tcategory, List<ResourceInfo> resourceInfoList) {
        resourceInfoList.add(new ResourceInfo("0","Resources","Año litúrgico"));
    }
    @Override
    protected void setUpWorkingGrade() {
        //Grade = new String[]{"1","2","3","4","5","6","K"};
        Grade = new String[]{"1"};
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
        fileTemplate = fileTemplate.replace("#content#",mainContent);
        fileTemplate = fileTemplate.replace("#additionscript#","");
        return fileTemplate;
    }

    @Override
    protected String genContentFromLink(String resourceDataLink) {
        Document document = null;
        try {
            document = Jsoup.connect(resourceDataLink).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = document.getElementById("contentBody").outerHtml();
        return content;
    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        String Link = "http://www.sadlierreligion.com/webelieve/liturgical_year.cfm?language=sp&sp=teacher&section=resource&grade=K";
        for(ResourceInfo item:resourceInfoList){
             item.setResourceDataLink(Link);
        }
    }

    @Override
    protected String WorkingOnCategory() {
        return "Año litúrgico";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        AñoLitúrgico task = new AñoLitúrgico();
        task.doWork();
    }
}
