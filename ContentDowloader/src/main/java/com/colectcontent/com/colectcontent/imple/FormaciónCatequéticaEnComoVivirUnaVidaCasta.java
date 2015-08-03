package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

import java.util.List;

/**
 * Created by vincent on 7/22/2015.
 */
public class FormaciónCatequéticaEnComoVivirUnaVidaCasta extends PDLCSP{
    @Override
    protected String WorkingOnCategory() {
        return "Formación catequética en como vivir una vida casta";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        for(ResourceInfo item:resourceInfoList){
            String linkTemplate = "http://www.sadlierreligion.com/webelieve/docs/ChasteLivingCorrelation01.pdf";
            item.setResourceDataLink(linkTemplate);
        }
    }

    public static void main(String[] args) {
        FormaciónCatequéticaEnComoVivirUnaVidaCasta task = new FormaciónCatequéticaEnComoVivirUnaVidaCasta();
        task.doWork();
    }
}
