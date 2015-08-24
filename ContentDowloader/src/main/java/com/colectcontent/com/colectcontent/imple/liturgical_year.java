package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

import java.util.List;

/**
 * Created by vincent on 8/19/2015.
 */
public class liturgical_year extends  AñoLitúrgico{
    @Override
    protected void readResourceCode(String grade, String tcategory, List<ResourceInfo> resourceInfoList) {
        resourceInfoList.add(new ResourceInfo("Liturgical","Resources","Liturgical Year"));
    }
    @Override
    protected void setUpWorkingGrade() {
        Grade = new String[]{"K"};
        //Grade = new String[]{"1"};
    }
    @Override
    protected String WorkingOnCategory() {
        return "Liturgical Year";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {
        String Link = "http://www.sadlierreligion.com/webelieve/liturgical_year.cfm?sp=teacher&section=resource&grade=1";
        for(ResourceInfo item:resourceInfoList){
            item.setResourceDataLink(Link);
        }
    }

    public static void main(String[] args) {
        liturgical_year task = new liturgical_year();
        task.doWork();
    }
}
