package com.colectcontent;

/**
 * Created by vincent on 7/17/2015.
 */
public class ResourceInfo {
    String resourceCode;
    String title;
    String grade;
    String resourceDataLink;
    String mainContent;
    String tempalte;
    String category;
    private String assetPath;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }



    public ResourceInfo(String resourceCode, String title, String category) {
        this.resourceCode = resourceCode;
        this.title = title;
        this.category = category;
    }

    public String getResourceDataLink() {
        return resourceDataLink;
    }

    public void setResourceDataLink(String resourceDataLink) {
        this.resourceDataLink = resourceDataLink;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getTempalte() {
        return tempalte;
    }

    public void setTempalte(String tempalte) {
        this.tempalte = tempalte;
    }




    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }
}
