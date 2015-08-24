package com.colectcontent.gather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 7/28/2015.
 */
public class TitleInfo {
    String name;
    boolean isoption;
    List<GatherPageModel> gradePages = new ArrayList<>();
    List<GatherPageModel> clusterPages = new ArrayList<>();
    String nvBarCode = "";
    private String NVBcode;
    private CharSequence imgContent;

    public TitleInfo(String name, boolean isoption,String languageParam,String NvabarCode) {
        nvBarCode = NvabarCode;
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade"+languageParam, "##", "grade-main"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_prek"+languageParam, "##", "pre-school"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_k"+languageParam, "##", "kindergarten"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_1"+languageParam, "##", "grade-1"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_2"+languageParam, "##", "grade-2"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_3"+languageParam, "##", "grade-3"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_4"+languageParam, "##", "grade-4"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_5"+languageParam, "##", "grade-5"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_6"+languageParam, "##", "grade-6"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_7"+languageParam, "##", "grade-7"));
        gradePages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=grade_8"+languageParam, "##", "grade-8"));


        clusterPages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=cluster"+languageParam, "##", "cluster-main"));
        clusterPages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=cluster_prek"+languageParam, "##", "cluster-pre-k"));
        clusterPages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=cluster_primary"+languageParam, "##", "cluster-primary"));
        clusterPages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=cluster_interm"+languageParam, "##", "cluster-intermediate"));
        clusterPages.add(new GatherPageModel("http://www.sadlierreligion.com/webelieve/gather.cfm?page={title}&sp=option2&tp=cluster_junior"+languageParam, "##", "cluster-junior-high"));



        this.name = name;
        this.isoption = isoption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isoption() {
        return isoption;
    }

    public void setIsoption(boolean isoption) {
        this.isoption = isoption;
    }

    public List<GatherPageModel> getGradePages() {
        return gradePages;
    }

    public void setGradePages(List<GatherPageModel> gradePages) {
        this.gradePages = gradePages;
    }

    public List<GatherPageModel> getClusterPages() {
        return clusterPages;
    }

    public void setClusterPages(List<GatherPageModel> clusterPages) {
        this.clusterPages = clusterPages;
    }

    public String getNVBcode() {
        if(!isoption){
            return "";
        }
        return nvBarCode;
    }

    public CharSequence getImgContent() {
        return imgContent;
    }

    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }
}
