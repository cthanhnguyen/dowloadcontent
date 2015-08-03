package com.tool.dowloadmeta;

import com.tool.ProductMeta;
import com.until.Untils;

/**
 * Created by vincent on 7/24/2015.
 */
public class DowloadMetaData {
    String pemplateLink = "http://192.168.2.12:8090/cms/product/exportProductMetaData.html?pojo.productId={productId}";
    String outPutTemplate = "input/WB_/{code}.xls";
    public void DowloadAndConverExlFiles(){
        ProductMeta[] productCode =  getProdoctCodeList();
        for(final ProductMeta item:productCode){

                    System.out.println("Dowloading "+item.getCode());
                    String oPutdirTemplate = getOutPutTemplate();
                    String dowLink = pemplateLink.replace("{productId}",item.getResourceID());
                    String outPutlink = oPutdirTemplate.replace("{code}",item.getCode());
                    Untils.dowloadFile(dowLink,outPutlink);
                    System.out.println("Dowload "+item.getCode()+" Done!");



        }
        //String ln = "http://192.168.2.12:8090/cms/product/exportProductMetaData.html?pojo.productId=749";
        //ExcelUntils.ReadAndConvertMetafile("input/test/abc.xls");
    }

    protected String getOutPutTemplate() {
        return "input/WB_EN/{code}.xls";
    }

    protected ProductMeta[] getProdoctCodeList() {
        return new ProductMeta[]{new ProductMeta("WB_EN_K","752",""),
                new ProductMeta("WB_EN_1","751",""),
                new ProductMeta("WB_EN_2","750",""),
                new ProductMeta("WB_EN_3","749",""),
                new ProductMeta("WB_EN_4","748",""),
                new ProductMeta("WB_EN_5","747",""),
                new ProductMeta("WB_EN_6","746","")};
    }

    public static void main(String[] args) {
        DowloadMetaData n = new DowloadMetaData();
        n.DowloadAndConverExlFiles();
    }
}
