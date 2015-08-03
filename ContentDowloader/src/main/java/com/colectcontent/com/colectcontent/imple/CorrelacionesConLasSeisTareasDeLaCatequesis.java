package com.colectcontent.com.colectcontent.imple;

import com.colectcontent.ResourceInfo;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/22/2015.
 */
public class CorrelacionesConLasSeisTareasDeLaCatequesis extends PDLCSP {
    @Override
    protected String WorkingOnCategory() {
        return "Correlaciones con las seis tareas de la catequesis";
    }
    @Override
    protected void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade) {

        for(ResourceInfo item:resourceInfoList){
            String linkTemplate = "http://www.sadlierreligion.com/webelieve/docs/correlation_sp/Grade%20{grade}.pdf";
            item.setResourceDataLink(linkTemplate.replace("{grade}",grade));
        }
    }

    public static void main(String[] args) {
        CorrelacionesConLasSeisTareasDeLaCatequesis task = new CorrelacionesConLasSeisTareasDeLaCatequesis();
        task.doWork();
    }
}
