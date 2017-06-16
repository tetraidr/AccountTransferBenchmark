/**
 * Created by sbt-khruzin-mm on 13.04.2017.
 */

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.lang.Math;

public class BenchmarkThread implements Callable{
    public long ActsRq;
    public long ActsDone;
    public int CurThread;
    private double Delta;
    //public int QueueLength;
    private double nextAct;
    private int MaxThreads;
    private volatile TaskGenerator taskGenerator;
    private ConcurrentHashMap<Integer,FutureTask> SubTasks = new ConcurrentHashMap<>();
    BenchmarkThread(int maxThreads, TaskGenerator taskGenerator, int tps){
        this.taskGenerator = taskGenerator;
        this.ActsDone = 0;
        this.ActsRq = 0;
        //this.QueueLength = 0;
        this.nextAct = 0;
        this.Delta = 1/tps*1e9;
        this.MaxThreads = maxThreads;
    }
    @Override
    public Integer call() {
        CurThread=0;
        //int SleepDelta = (int)(Math.round(Delta));
        /*
        while ((taskGenerator.hasNext()) & (CurThread < MaxThreads)){
            AddFutureToSubTasks(CurThread);
            CurThread++;
            try {
                Thread.sleep(0, SleepDelta);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (!taskGenerator.hasNext()){
            MaxThreads = CurThread;
        }
        CurThread = 0;*/
        //nextAct = System.nanoTime() + Delta;

        while (taskGenerator.hasNext()) {
            if (SubTasks.get(CurThread) == null){
                if (System.nanoTime()>nextAct) {
                    AddFutureToSubTasks(CurThread);
                }
                CurThread++;
            }
            else if (SubTasks.get(CurThread).isDone()){
                SubTasks.put(CurThread,null);
                ActsDone++;
                if (System.nanoTime()>nextAct) {
                    AddFutureToSubTasks(CurThread);
                }
                CurThread++;
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
        ActsRq++;
        nextAct += Delta;
    }
}
