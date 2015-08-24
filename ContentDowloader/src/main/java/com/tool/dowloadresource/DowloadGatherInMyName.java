package com.tool.dowloadresource;

import com.tool.ProductMeta;

/**
 * Created by vincent on 7/31/2015.
 */
public class DowloadGatherInMyName extends DowloadResourceByCategory{
    protected String getWorkingOnCategory() {
        return "Gather in My Name";
    }
    protected String getInputTemplateWithGrade() {

        return "input/WB_EN/WB_EN_{grade}.xls";
    }

    protected ProductMeta[] getProdoctCodeList() {
        return new ProductMeta[]{new ProductMeta("WB_EN_K","752","K"),
                new ProductMeta("WB_EN_1","751","1"),
                new ProductMeta("WB_EN_2","750","2"),
                new ProductMeta("WB_EN_3","749","3"),
                new ProductMeta("WB_EN_4","748","4"),
                new ProductMeta("WB_EN_5","747","5"),
                new ProductMeta("WB_EN_6","746","6")
        };
    }
    protected String getExtendSionList() {
        return "";
    }

    public static void main(String[] args) {
        DowloadResourceByCategory task  = new DowloadGatherInMyName();
        task.doWork();
    }
}
