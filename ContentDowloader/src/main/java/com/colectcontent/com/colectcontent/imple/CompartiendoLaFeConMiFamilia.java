package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/22/2015.
 */
public class CompartiendoLaFeConMiFamilia extends PDLCSP {
    @Override
    protected String WorkingOnCategory() {
        return "Compartiendo la fe con mi familia";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        for(ResourceInfo item:resourceInfoList){
            String linkTemplate = "http://www.sadlierreligion.com/webelieve/docs/sharingfaith_sp/sharing_{grade}_ch{chapter}.pdf";
            String Chapter =  item.getTitle().replaceAll("\\s*Capítulo\\s*","");
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{grade}"),grade);
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{chapter}"),Chapter);
            item.setResourceDataLink(linkTemplate);
        }
    }


    public static void main(String[] args) {
        CompartiendoLaFeConMiFamilia task = new CompartiendoLaFeConMiFamilia();
        task.doWork();
    }
}
