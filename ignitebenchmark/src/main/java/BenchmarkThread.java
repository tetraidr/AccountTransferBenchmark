/**
 * Created by sbt-khruzin-mm on 13.04.2017.
 */

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BenchmarkThread implements Callable{
    private transfers Transfers;
    //CustomTarantoolClient client;
    IgniteClient client;
    private int MaxThreads;
    private ConcurrentHashMap<Integer,FutureTask> SubTasks = new ConcurrentHashMap<>();
    BenchmarkThread(ConcurrentHashMap<String,Integer> Accounts, int m, int maxThreads, Random rnd, IgniteClient client){
        this.Transfers = new transfers(m, Accounts,rnd);
        this.client = client;
        this.MaxThreads = maxThreads;
        if (this.MaxThreads > m){
            this.MaxThreads = m;
        }
    }
    @Override
    public Integer call() {
        AcctTransfer transfer = Transfers.getNext();
        int i=0;
        while ((transfer != null) & (i < MaxThreads)){
            AddFutureToSubTasks(transfer,i);
            transfer = Transfers.getNext();
            i++;
        }
        i = 0;
        while (transfer != null) {
            if (SubTasks.get(i).isDone()){
                AddFutureToSubTasks(transfer,i);
                transfer = Transfers.getNext();
                i++;
            }
            else {
                i++;
            }
            if (i>=MaxThreads){
                i = 0;
            }
        }
        for (i=0;i<MaxThreads;i++){
            if (!SubTasks.get(i).isDone()){
                try{
                    SubTasks.get(i).get();
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
    private void AddFutureToSubTasks(AcctTransfer transfer,int i){
        FutureTask task = new FutureTask(new TarantoolQueryAsync(client, transfer));
        Thread t = new Thread(task);
        SubTasks.put(i,task);
        t.start();
    }
}
