package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.GenerateTask;
import com.colectcontent.ResourceInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/17/2015.
 */
public class WB_SPGen extends GenerateTask {


    @Override
    protected void setUpWorkingGrade() {

    }

    @Override
    protected void genarateFiles(ResourceInfo item) {




    }

    @Override
    protected String midResourceGetFullContent(String mainContent) {
        return null;
    }

    @Override
    protected String genContentFromLink(String resourceDataLink) {
          return null;
    }

    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList, String grade) {


    }



    @Override
    protected String WorkingOnCategory() {
        return null;
    }

    @Override
    protected String getInputTemplateWithGrade() {
        return "input\\RWB_SP\\G{grade}.xlsx";
    }
}
