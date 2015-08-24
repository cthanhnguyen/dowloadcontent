package com.tool.dowloadresource;

/**
 * Created by vincent on 8/4/2015.
 */
public class Demicasaalatuya extends ReunidosEnMiNombre {
    protected String getWorkingOnCategory() {
        return "De mi casa a la tuya";
    }

    public static void main(String[] args) {
        Demicasaalatuya task = new Demicasaalatuya();
        task.doWork();
    }
}
