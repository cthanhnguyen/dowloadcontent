package com.tool.dowloadresource;

import com.tool.ProductMeta;

/**
 * Created by vincent on 7/31/2015.
 */
public class ReunidosEnMiNombre extends DowloadResourceByCategory {
    protected String getWorkingOnCategory() {
        return "Reunidos en mi nombre";
    }
    protected String getInputTemplateWithGrade() {

        return "input/WB_SP/WB_SP_{grade}.xls";
    }

    protected ProductMeta[] getProdoctCodeList() {
        return new ProductMeta[]{new ProductMeta("WB_SP_K","745","K"),
                new ProductMeta("WB_SP_1","769","1"),
                new ProductMeta("WB_SP_2","743","2"),
                new ProductMeta("WB_SP_3","742","3"),
                new ProductMeta("WB_SP_4","741","4"),
                new ProductMeta("WB_SP_5","740","5"),
                new ProductMeta("WB_SP_6","739","6")};
    }
    protected String getGamePreTemplate() {
        return "assets/WB_SP_{grade}/";
    }
    protected String getExtendSionList() {
        return "";
    }

    public static void main(String[] args) {
        ReunidosEnMiNombre task = new ReunidosEnMiNombre();
        task.doWork();
    }
}
