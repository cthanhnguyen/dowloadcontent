package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;
import com.until.Untils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/21/2015.
 */
public class VdeSanSP extends DoctrinaSocialDeLaIglesia {



    @Override
    protected String genContentFromLink(String resourceDataLink) {
        Document document = null;

        try {
            document = Jsoup.connect(resourceDataLink).timeout(0).get();
        } catch (IOException e) {
            try {
                document = Jsoup.connect(resourceDataLink).timeout(0).get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        if(document==null){
            System.out.println("Error");
        }
        Element contenele =  document.getElementById("saint");
        String content ="";
        if(contenele!=null){
            content = contenele.outerHtml();
        }else{
            System.out.println("Error Title !");
        }

        //content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        return content.replaceAll("(?s)<div class=\"archiveBanner\">(.+?)</div>","");

    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        Map<String,ResourceInfo> mapSearch = new HashMap<>();
        for(ResourceInfo item:resourceInfoList){
            mapSearch.put(item.getTitle(),item);
        }
        String Link = getLinkHolder();
        String htmlSource = Untils.getHtmlFromURL(Link);

        Pattern pattern = Pattern.compile("(?i)(<tr class=\"row\\d\">(.+?)</tr>)");
        Matcher matcher = pattern.matcher(htmlSource);
        Map<String,List<String>> result = new HashMap<String, List<String>>();
        while (matcher.find()){
            Pattern pattern2 = Pattern.compile("<a href=\"([^\"]+)\">(.+)</a>");
            Matcher matcher2 = pattern2.matcher(matcher.group(1));
            matcher2.find();
            String title = matcher2.group(2);
            String link = matcher2.group(1);
            ResourceInfo re = mapSearch.get(title.trim());
            if(re!=null){
                re.setResourceDataLink("http://www.sadlierreligion.com"+link);
            }else{
                System.out.println("Error Title: "+title+" not reconize in mapping files");
            }

            //break;
        }
    }

    protected String getLinkHolder() {
        return "http://www.sadlierreligion.com/webelieve/saints.cfm?language=sp&sp=teacher&section=activity&grade=1&isarchive=1";
    }

    @Override
    protected String WorkingOnCategory() {
        return "Vidas de santos";
    }
    protected String getResourceStyle() {
        return "liveOfSaintsMain";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        VdeSanSP task = new VdeSanSP();
        task.doWork();
    }
}
