import java.util.concurrent.BlockingQueue;

/**
 * Created by vincent on 7/13/2015.
 */
public class PoolThread extends Thread{
    BlockingQueue<MyTask> taskQueue;
    private Thread t;
    ThearHost host;
    private boolean stopped = true;
    String Name;

    public PoolThread(BlockingQueue<MyTask> task,ThearHost thHost,String name){
        taskQueue = task;
        host = thHost;
        this.Name = name;
    }
    public void run(){
        stopped = false;
        while(!isStopped()){
            try{
                if (taskQueue.size()>0){
                    final MyTask run = taskQueue.take();
                    final Object result = run.doInBackGround();

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            run.onSuccess(result);
                        }

                }; r.run();
                }else {
                    stopped = true;
                    break;
                }
            } catch(Exception e){
                //log or otherwise report exception,
                //but keep pool thread alive.
            }
        }

        host.onComplete(Name);
    }
    public void startTask(){
        if (t == null)
        {
            t = new Thread (this);
            t.start ();
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
