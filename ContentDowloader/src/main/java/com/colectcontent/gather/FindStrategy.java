package com.colectcontent.gather;

import com.colectcontent.MyTask;
import com.colectcontent.SimpleThread;
import com.until.Untils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by vincent on 7/27/2015.
 */
public class FindStrategy {
    public String midAnounContent(String link, String outPutPre) {
        String content = getContentFromFullCode(link);
        detectAndDowImg(content, outPutPre, Untils.getWorkingLinkFromLink(link));
        return content;
    }
    protected String getContentFromFullCode(String link) {

        Element ele =  getContentElement(link);


        //content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        if(ele.getElementsByAttributeValue("class","chart")!=null){
            ele.getElementsByAttributeValue("class","chart").remove();
        }
        String content = ele.outerHtml();
        return content;
    }
    private void detectAndDowImg(String content, final String outputPre, final String workingLinkFromLink) {
        final String contentHolder = content;
        SimpleThread task = new SimpleThread(new MyTask() {
            @Override
            public Object doInBackGround() {
                Untils.detectAndDowImg(contentHolder,outputPre,workingLinkFromLink);
                return  null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
        task.startThread();
        content = content.replaceAll("\"\\.\\./", "\"");
        content = content.replaceAll("\'\\.\\./", "'");
    }

    public String midTemBackGround(String link, String outputPre) {
        String content = getContentFromFullCode(link);
        detectAndDowImg(content, outputPre, Untils.getWorkingLinkFromLink(link));
        return content;
    }

    public String midaftherGather(String link, String outputPre) {
        String content = getContentFromFullCode(link);
        detectAndDowImg(content, outputPre, Untils.getWorkingLinkFromLink(link));
        return content;
    }

    public String midweGather(String link, String outputPre) {
        String content ="";
        for(int i = 0 ; i < 6;i++){
            String contentSection = " <div class=\"section no-{index}\">#sectionContent#</div>\n";


            Element ele =  getContentElement(link.replace("{section}",Integer.toString(i)));

            ele.getElementsByAttributeValue("class","navPanel").remove();
            ele.getElementsByAttributeValue("class","chart").remove();
            for(Element item :ele.children()){
                item.attr("style","display:block;");
            }
            contentSection = contentSection.replace("#sectionContent#", ele.html());
            contentSection = contentSection.replace("{index}",Integer.toString(i));
            detectAndDowImg(content, outputPre, Untils.getWorkingLinkFromLink(link));
            content += contentSection;

        }
        return content;
    }

    protected Element getContentElement(String link) {
        Document document = null;

        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error Link :"+link);
        }
        Element ele =  document.getElementById("content");
        if(ele==null){
            if(document.getElementById("gather").children().get(1).children().size()==0){
                   System.out.println("Broken Link : " + link);

            }
            if(document.getElementById("gather").children().get(1).children().get(0).children().size()<3){
                ele = document.getElementById("gather").children().get(1).children().get(0).children().get(1);
                System.out.println("Broken Link : " + link);
            }else {
                ele = document.getElementById("gather").children().get(1).children().get(0).children().get(2).children().get(1);
            }
                ele.attr("display","block");
        }
        if(ele.select("td[align=RIGHT]")!=null){
            Elements temps = ele.select("td[align=RIGHT]");
            for(Element item:temps){
               item.parent().remove();
            }
        }
        ele.attr("display","block");
        return ele;
    }

    public String midbeforeGathe(String link, String outputPre) {
        String content = getContentFromFullCode(link);
        content = content.replaceAll("(?i)href\\=\"/webelieve/gather.cfm\\?page\\=[^\\&]+\\&amp;sp\\=[^\\&]*\\&amp;tp\\=bulletin\"","class=\"nav-gather\" target=\"announcement\"");
        content = content.replaceAll("(?i)href\\=\"/webelieve/gather.cfm\\?page\\=[^\\&]+\\&amp;sp\\=[^\\&]+\\&amp;tp\\=catechist\"","class=\"nav-gather\" target=\"team-background\"");
        detectAndDowImg(content, outputPre, Untils.getWorkingLinkFromLink(link));
        return content;
    }

    public String midPreChar(String link, String outputPre, StringBuilder  imgContent, String NavBar) {
        //String content = getContentFromFullCode(link);

        Element element =  getContentElement(link);
        String content = null;
        imgContent.append(getMainImg(link));
        Elements temp = element.select("a[href*=#chart]");
        if(temp.size()>0){
            Element ul = (Element)temp.get(0).parent().parent();
            ul.html(NavBar);
        }
        content = element.html();
        //content = content.replaceAll("(?s)<ul>((?!<ul>).)+</ul>[\\s,\\n]+<hr size=\"1\" width=\"100%\" align=\"center\"\\s*/>", NavBar+"<hr size=\"1\" width=\"100%\" align=\"center\"\\s*/>");
        //content = content.replaceAll("(?s)(</v:edit><br />)[\\n,\\s]+<ul>(.+?)</ul>","\1 "+NavBar+"<hr size=\"1\" width=\"100%\" align=\"center\"\\s*/>");

        detectAndDowImg(content, outputPre, Untils.getWorkingLinkFromLink(link));
        detectAndDowImg(imgContent.toString(), outputPre, Untils.getWorkingLinkFromLink(link));
        detectAndDowImg("<img src=\"img/gather/banner_orange.gif\" height=\"8\" width=\"100%\" alt=\"\">", outputPre, Untils.getWorkingLinkFromLink(link));

        return content;
    }

    private String getMainImg(String link) {
        Document document = null;
        String imgtag = "";
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element ele =  document.getElementById("content");
        if(ele==null){
            imgtag = document.getElementById("gather").children().get(1).children().get(0).children().get(0).select("img").outerHtml();
        }else{
            imgtag = document.getElementById("gather").select("img").get(0).outerHtml();
        }
        return imgtag;
    }

    public String mindNomalcontentFromFullCode(String link,String outPutPre) {
        String content = "";
        try {
            content = getContentFromFullCode(link);
            detectAndDowImg(content, outPutPre, Untils.getWorkingLinkFromLink(link));
        }catch (Exception ex){
            System.out.println("Error Broken Link" + link);
        }
        return content;
    }
}
