package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import com.until.Untils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/23/2015.
 */
public class ActividadesDaraImprimir extends DoctrinaSocialDeLaIglesia{
    @Override
    protected String WorkingOnCategory() {
        return "Actividades para imprimir";
    }
    protected String getResourceStyle() {
        return "";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        for(ResourceInfo item:resourceInfoList){
            String linkTemplate = "http://www.sadlierreligion.com/webelieve/pop_printableactivity.cfm?language=sp&sp=teacher&section=activity&grade={grade}&ch={chapter}";
            String Chapter =  item.getTitle().replaceAll("\\s*Capítulo\\s*","");
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{grade}"),grade);
            linkTemplate = linkTemplate.replaceAll(Pattern.quote("{chapter}"),Chapter);
            item.setResourceDataLink(linkTemplate);
        }

    }
    @Override
    protected String genContentFromLink(String resourceDataLink) {
        String htmlItemSource = Untils.getHtmlFromURL(resourceDataLink);
        Pattern patten = Pattern.compile("(?s)(<h1>Actividades para imprimir</h1>.+)<br[^>]+><br[^>]+>");
        Matcher macherContent = patten.matcher(htmlItemSource);
        macherContent.find();
        String content = macherContent.group(1);
        content = "<div id=\"print\">\n" +
                "            <div id=\"print-layout\">"+content+"</div></div>";
        //content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        return content.replaceAll("(?s)<div class=\"archiveBanner\">(.+?)</div>","");

    }

    public static void main(String[] args) {
        ActividadesDaraImprimir task = new ActividadesDaraImprimir();
        task.doWork();
    }

}
