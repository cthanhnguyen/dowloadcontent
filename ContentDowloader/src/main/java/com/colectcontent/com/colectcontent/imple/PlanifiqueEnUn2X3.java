package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import com.until.Untils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/22/2015.
 */
public class PlanifiqueEnUn2X3 extends PDLCSP{
    @Override
    protected void genarateFiles(ResourceInfo item) {
        String outPutdir="output\\"+item.getCategory()+"\\"+item.getGrade()+"\\"+item.getResourceCode();
        new File(outPutdir).mkdirs();
        String fileNameDir = outPutdir+"\\"+ item.getResourceCode()+ Untils.getFileExtFromLink(item.getResourceDataLink());

        Untils.dowloadFile(item.getResourceDataLink(), fileNameDir);
    }
    @Override
    protected String WorkingOnCategory() {
        return "Planifique en un 2X3";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {
        Map<String,String> typeLink = new HashMap<>();
        typeLink.put("Ojeada","http://www.sadlierreligion.com/webelieve/docs/catechist_quickPrep_sp/G{grade}_CH{chapter}_Overview.pdf");
        typeLink.put("Guía para planificar la lección","http://www.sadlierreligion.com/webelieve/docs/catechist_quickPrep_sp/G{grade}_CH{chapter}_Lesson%20Planning%20Guide.pdf");
        for(ResourceInfo item:resourceInfoList){
            Pattern typeChapter = Pattern.compile("(.+)( —| \\&\\#8212\\;)? Capítulo (\\d+)");
            Matcher matcher = typeChapter.matcher(item.getTitle());
            matcher.find();
            String type = matcher.group(1).replaceAll(" \\&#8212;","");
            type = type.replaceAll(" —", "");
            type = type.replaceAll(" -", "");
            String chapter = matcher.group(3);
            String templateLink = typeLink.get(type);
            if(templateLink!=null){
            templateLink = templateLink.replaceAll(Pattern.quote("{grade}"),grade);
            templateLink = templateLink.replaceAll(Pattern.quote("{chapter}"),chapter);
            item.setResourceDataLink(templateLink);}
            else{
                System.out.println("Error can not find Resource Link: "+item.getTitle());
            }
        }
    }

    public static void main(String[] args) {
        PlanifiqueEnUn2X3 task = new PlanifiqueEnUn2X3();
        task.doWork();
    }
}
