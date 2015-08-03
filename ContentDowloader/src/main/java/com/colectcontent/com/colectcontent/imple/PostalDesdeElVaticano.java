package com.colectcontent.com.colectcontent.imple;

/**
 * Created by vincent on 7/22/2015.
 */
public class PostalDesdeElVaticano extends DoctrinaSocialDeLaIglesia{
    @Override
    protected String WorkingOnCategory() {
        return "Postal desde el Vaticano";
    }

    protected String getHtmlLinkHolder() {
        return "http://www.sadlierreligion.com/webelieve/postcardsfromvatican.cfm?language=sp&sp=teacher&section=article&grade=4&isarchive=1";
    }
    protected String getResourceStyle() {
        return "postcardFromVCMain";
    }
    public static void main(String[] args) {
        PostalDesdeElVaticano task = new PostalDesdeElVaticano();
        task.doWork();
    }
}
