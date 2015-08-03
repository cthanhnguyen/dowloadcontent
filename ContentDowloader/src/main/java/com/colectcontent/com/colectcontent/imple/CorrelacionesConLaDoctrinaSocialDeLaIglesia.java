package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * Created by vincent on 7/22/2015.
 */
public class CorrelacionesConLaDoctrinaSocialDeLaIglesia extends VdeSanSP{
    @Override
    protected String WorkingOnCategory() {
        return "Correlaciones con la doctrina social de la Iglesia";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        for(ResourceInfo item: resourceInfoList){
            item.setResourceDataLink("http://www.sadlierreligion.com/webelieve/catholicsocialcorrelations.cfm?language=sp&sp=teacher&section=resource&grade=1");
        }
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
        content = content.replaceAll("(?s)<div class=\"pagingBanner\">(.+?)</div>","");
        return content;

    }

    public static void main(String[] args) {
        CorrelacionesConLaDoctrinaSocialDeLaIglesia task = new CorrelacionesConLaDoctrinaSocialDeLaIglesia();
        task.doWork();
    }

}
