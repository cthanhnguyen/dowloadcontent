package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

/**
 * Created by vincent on 7/24/2015.
 */
public class CatechistDevelopment extends LosCatólicosYLaBiblia{
    @Override
    protected void setUpWorkingGrade() {
        Grade = new String[]{"2"};
        //Grade = new String[]{"2"};
    }
    protected String getHtmlLinkHolder() {
        return "http://www.sadlierreligion.com/webelieve/catechistdevelopment.cfm?sp=teacher&section=article&grade=1&isarchive=1";
    }
    protected String getOutputDirPre(ResourceInfo item) {
        return "output\\"+item.getCategory()+"\\WB_EN_"+item.getGrade()+"\\"+item.getResourceCode();
    }
    @Override
    protected String WorkingOnCategory() {
        return "Catechist Development";
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_ENG\\G{grade}.xlsx";
    }

    public static void main(String[] args) {
        CatechistDevelopment task = new CatechistDevelopment();
        task.doWork();
    }
}
