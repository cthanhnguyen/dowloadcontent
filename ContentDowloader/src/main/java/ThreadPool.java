import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by vincent on 7/13/2015.
 */
public class ThreadPool implements ThearHost{

    private BlockingQueue<MyTask> taskQueue = new LinkedBlockingQueue<>();
    private int maxThread = 10;
    private int completeThreads = 0;
    private PoolThread[] worker = new PoolThread[maxThread];
    private AllTaskDone allTaskDon;

    public ThreadPool(AllTaskDone don){
        allTaskDon = don;
         for(int i = 0 ; i < maxThread ;i++){
             worker[i] = new PoolThread(taskQueue,this,"Thread "+i);
         }
    }
    public void doTaskExcute(){
       for(PoolThread item :worker){
           if(item.isStopped()){
               item.startTask();
               return;
           }
       }
    }
    public void addTask(MyTask task){

        taskQueue.add(task);

    }

    @Override
    public void onComplete(String name) {
        System.out.println(name+" --complete sir!");
        completeThreads ++;
        if(completeThreads == maxThread){
            System.out.println("All Task Done Sir!");
            allTaskDon.onDone();
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(new AllTaskDone() {
            @Override
            public void onDone() {

            }
        });
        for(int i = 0 ; i < 60;i++){
            final int num = i;
            threadPool.addTask(new MyTask() {
                @Override
                public Object doInBackGround() {
                    return num;
                }

                @Override
                public void onSuccess(Object result) {
                    int r = (int)result;
                    System.out.println(num);
                }
            });

        }
    }
}
