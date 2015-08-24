package com.tool.dowloadresource;

/**
 * Created by vincent on 8/4/2015.
 */
public class Guíadeestudio extends ReunidosEnMiNombre{
    protected String getWorkingOnCategory() {
        return "Guía de estudio";
    }

    public static void main(String[] args) {
        Guíadeestudio task = new Guíadeestudio();
        task.doWork();
    }
}
