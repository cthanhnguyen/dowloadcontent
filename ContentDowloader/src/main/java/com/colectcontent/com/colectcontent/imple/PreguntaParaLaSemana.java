package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by vincent on 7/22/2015.
 */
public class PreguntaParaLaSemana extends DoctrinaSocialDeLaIglesia{
    @Override
    protected String WorkingOnCategory() {
        return "Pregunta para la semana";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        for(ResourceInfo item: resourceInfoList){
            item.setResourceDataLink("http://www.sadlierreligion.com/webelieve/gather.cfm?language=sp&sp=teacher&section=resource&grade=1");
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
        Element item =  document.getElementById("contentBody");
        item.getElementById("home").attr("style"," width: 540px; margin: auto;");
        String content = item.outerHtml();
        content = content.replaceAll("(?s)<div class=\"pagingBanner\">(.+?)</div>","");
        return content;

    }

    public static void main(String[] args) {
        PreguntaParaLaSemana task = new PreguntaParaLaSemana();
        task.doWork();
    }

}
