package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.GenerateTask;
import com.colectcontent.ResourceInfo;
import com.until.Untils;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/21/2015.
 */
public class PDLCSP extends GenerateTask {
    @Override
    protected void setUpWorkingGrade() {
        Grade = new String[]{"1","2","3","4","5","6","K"};
    }

    @Override
    protected void genarateFiles(ResourceInfo item) {
        String outPutdir="output\\"+item.getCategory()+"\\"+item.getGrade()+"\\"+item.getResourceCode();
        new File(outPutdir).mkdirs();
        String fileNameDir = outPutdir+"\\"+ Untils.getFileNameFromLink(item.getResourceDataLink());

        Untils.dowloadFile(item.getResourceDataLink(), fileNameDir);
    }

    @Override
    protected String midResourceGetFullContent(String mainContent) {
        return null;
    }

    @Override
    protected String genContentFromLink(String resourceDataLink) {
       return null;
    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        for(ResourceInfo item:resourceInfoList){
            String linkTemplate = "http://www.sadlierreligion.com/webelieve/docs/reproducibles_sp/G{grade}_CH{chapter}.pdf";
            String Chapter =  item.getTitle().replaceAll("\\s*Capítulo\\s*","");
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{grade}"),grade);
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{chapter}"),Chapter);
            item.setResourceDataLink(linkTemplate);
        }
    }

    @Override
    protected String WorkingOnCategory() {
        return "Patrones de los capítulos";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        PDLCSP task = new PDLCSP();
        task.doWork();
    }
}
