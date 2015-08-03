import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;

/**
 * Created by vincent on 7/16/2015.
 */
public class SimpleThread extends Thread {
    public static Phaser phaser = new Phaser();
    MyTask task;
    public SimpleThread(MyTask doinTask){
        task = doinTask;
        phaser.register();
    }
    public void run(){
       Object result = task.doInBackGround();
       task.onSuccess(result);
       phaser.arrive();
    }
    public void startThread(){
        start();
    }
}
