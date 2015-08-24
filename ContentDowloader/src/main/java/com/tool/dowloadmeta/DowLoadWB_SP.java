package com.tool.dowloadmeta;

import com.tool.ProductMeta;

/**
 * Created by vincent on 7/31/2015.
 */
public class DowLoadWB_SP extends DowloadMetaData{
    protected ProductMeta[] getProdoctCodeList() {
        return new ProductMeta[]{new ProductMeta("WB_SP_K","745",""),
                new ProductMeta("WB_SP_1","769",""),
                new ProductMeta("WB_SP_2","743",""),
                new ProductMeta("WB_SP_3","742",""),
                new ProductMeta("WB_SP_4","741",""),
                new ProductMeta("WB_SP_5","740",""),
                new ProductMeta("WB_SP_6","739","")};
    }
    protected String getOutPutTemplate() {
        return "input/WB_SP/{code}.xls";
    }

    public static void main(String[] args) {
        DowLoadWB_SP task = new DowLoadWB_SP();
        task.DowloadAndConverExlFiles();
    }
}
