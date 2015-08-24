package com.tool;

/**
 * Created by vincent on 7/30/2015.
 */
public class ProductMeta {
    String code;
    String resourceID;
    String grateTOC;

    public ProductMeta(String code, String resourceID,String gradeToc) {
        this.code = code;
        this.resourceID = resourceID;
        this.grateTOC =gradeToc;
    }

    public String getGrateTOC() {
        return grateTOC;
    }

    public void setGrateTOC(String grateTOC) {
        this.grateTOC = grateTOC;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }
}
