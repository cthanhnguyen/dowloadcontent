package com.tool.dowloadresource;

/**
 * Created by vincent on 8/5/2015.
 */
public class Escogiendoelprogramaadecuad extends Demicasaalatuya{
    protected String getWorkingOnCategory() {
        return "Creemos Pepaso";
    }

    public static void main(String[] args) {
        Escogiendoelprogramaadecuad task = new Escogiendoelprogramaadecuad();
        task.doWork();
    }
}
