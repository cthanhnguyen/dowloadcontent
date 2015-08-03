package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

/**
 * Created by vincent on 7/24/2015.
 */
public class LivesOfTheSaints extends VdeSanSP{
    @Override
    protected String WorkingOnCategory() {
        return "Lives of The Saints";
    }
    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_ENG\\G{grade}.xlsx";
    }
    protected String getLinkHolder() {
        return "http://www.sadlierreligion.com/webelieve/saints.cfm?sp=teacher&section=activity&grade=1&isarchive=1";
    }
    protected String getOutputPre(ResourceInfo item) {
        return "output\\"+item.getCategory()+"\\WB_EN_"+item.getGrade()+"\\"+item.getResourceCode();
    }
    public static void main(String[] args) {
        LivesOfTheSaints task = new LivesOfTheSaints();
        task.doWork();
    }
}
