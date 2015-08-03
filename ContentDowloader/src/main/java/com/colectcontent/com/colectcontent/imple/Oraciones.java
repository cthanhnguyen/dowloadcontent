package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import com.until.Untils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/22/2015.
 */
public class Oraciones extends PraysEngGen {

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        Map<String,ResourceInfo> mapSearch = new HashMap<>();
        for(ResourceInfo item:resourceInfoList){
            mapSearch.put(item.getTitle(),item);
        }

        String htmlSource = Untils.getHtmlFromURL("http://www.sadlierreligion.com/webelieve/prayers.cfm?language=sp&sp=teacher&section=resource&grade=" + grade);
        Pattern pattern1 = Pattern.compile("(?s)<a href=\"(/webelieve/prayers.cfm\\?language=sp\\&sp=teacher&section=resource\\&grade=.\\&view=\\&[^\"]+)\">([^<]+)");
        Matcher matcher1 = pattern1.matcher(htmlSource);


        while (matcher1.find()){
            String Link = matcher1.group(1);
            String title = matcher1.group(2);
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
        return "Oraciones";
    }
    protected String getAudioPrepix(String grade) {
        return "/content/WB_SP_"+grade+"/";
    }
    protected String getOuputDirPre(ResourceInfo item) {
        return "output\\"+item.getCategory()+"\\WB_SP_"+item.getGrade()+"\\"+item.getResourceCode();
    }
    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        Oraciones task = new Oraciones();
        task.doWork();
    }
}
