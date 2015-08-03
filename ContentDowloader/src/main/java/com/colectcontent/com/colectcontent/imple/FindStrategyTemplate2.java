package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.gather.FindStrategy;
import org.jsoup.nodes.Element;

/**
 * Created by vincent on 7/28/2015.
 */
public class FindStrategyTemplate2 extends FindStrategy{
    protected String getContentFromFullCode(String link) {

        Element ele =  getContentElement(link);
        if(ele.select("table").size()>0){
            ele = ele.select("table").get(0).attr("style","display: inherit;");

        }


        //content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        if(ele.getElementsByAttributeValue("class","chart")!=null){
            ele.getElementsByAttributeValue("class","chart").remove();
        }
        String content = ele.html();
        return content;
    }
}
