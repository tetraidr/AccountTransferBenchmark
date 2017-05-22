/**
 * Created by sbt-khruzin-mm on 13.04.2017.
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BenchmarkThread implements Callable{
    public long ActsDone;
    public int CurThread;
    private int MaxThreads;
    private TaskGenerator taskGenerator;
    private ConcurrentHashMap<Integer,FutureTask> SubTasks = new ConcurrentHashMap<>();
    BenchmarkThread(int maxThreads, TaskGenerator taskGenerator){
        this.taskGenerator = taskGenerator;
        this.ActsDone = 0;
        this.MaxThreads = maxThreads;
    }
    @Override
    public Integer call() {
        CurThread=0;
        while ((taskGenerator.hasNext()) & (CurThread < MaxThreads)){
            AddFutureToSubTasks(CurThread);
            CurThread++;
        }
        if (!taskGenerator.hasNext()){
            MaxThreads = CurThread;
        }
        CurThread = 0;
        while (taskGenerator.hasNext()) {
            if (SubTasks.get(CurThread).isDone()){
                AddFutureToSubTasks(CurThread);
                CurThread++;
                ActsDone++;
            }
            else {
                CurThread++;
            }
            if (CurThread>=MaxThreads){
                CurThread = 0;
            }
        }
        for (CurThread=0;CurThread<MaxThreads;CurThread++){
            if (!SubTasks.get(CurThread).isDone()){
                try{
                    SubTasks.get(CurThread).get();
                }
                catch (ExecutionException e){
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }
    private void AddFutureToSubTasks(int ProcessN){
        FutureTask task = new FutureTask(taskGenerator.GetTask());
        Thread t = new Thread(task);
        SubTasks.put(ProcessN,task);
        t.start();
    }
}
