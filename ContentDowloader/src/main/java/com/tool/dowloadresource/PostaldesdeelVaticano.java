package com.tool.dowloadresource;

/**
 * Created by vincent on 8/7/2015.
 */
public class PostaldesdeelVaticano extends ReunidosEnMiNombre{
    protected String getWorkingOnCategory() {
        return "Postal desde el Vaticano";
    }

    public static void main(String[] args) {
        PostaldesdeelVaticano task = new PostaldesdeelVaticano();
        task.doWork();
    }
}
