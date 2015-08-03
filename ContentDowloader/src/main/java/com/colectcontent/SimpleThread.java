package com.colectcontent;

/**
 * Created by vincent on 7/16/2015.
 */
public class SimpleThread extends Thread {
    MyTask task;
    public SimpleThread(MyTask doinTask){
        task = doinTask;
    }
    public void run(){
       Object result = task.doInBackGround();
       task.onSuccess(result);
    }
    public void startThread(){
        start();
    }
}
