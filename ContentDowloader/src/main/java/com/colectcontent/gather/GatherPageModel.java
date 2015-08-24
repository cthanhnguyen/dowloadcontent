package com.colectcontent.gather;

/**
 * Created by vincent on 7/28/2015.
 */
public class GatherPageModel {
    String contentLink;
    String templateSign;
    String id;
    String midContent;

    public GatherPageModel(String contentLink, String templateSign, String id) {
        this.contentLink = contentLink;
        this.templateSign = templateSign;
        this.id = id;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public String getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(String templateSign) {
        this.templateSign = templateSign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMidContent() {
        return midContent;
    }

    public void setMidContent(String midContent) {
        this.midContent = midContent;
    }
}
