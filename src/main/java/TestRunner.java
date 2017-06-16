import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by sbt-khruzin-mm on 19.05.2017.
 */
public class TestRunner {
    private static ConcurrentHashMap<Integer,FutureTask> Futures = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer,BenchmarkThread> Threads = new ConcurrentHashMap<>();
    void RunTest(TaskGenerator taskGenerator,Statistics statistics, Configuration cfg, int StatisticInterval){
        statistics.AddStatisticsElement(System.nanoTime(),0);
        for (int i=0;i<cfg.FLOWNUMBER;i++){
            BenchmarkThread benchmarkThread = new BenchmarkThread(cfg.ASYNCREQUESTPERFLOW,taskGenerator,200);
            FutureTask task = new FutureTask(benchmarkThread);
            Futures.put(i,task);
            Threads.put(i,benchmarkThread);
            Thread thread = new Thread(task);
            thread.start();
        }
        while (!Futures.get(cfg.FLOWNUMBER-1).isDone()) {
            try {
                Thread.sleep(StatisticInterval*1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            statistics.AddStatisticsElement(Threads,cfg.FLOWNUMBER);
            statistics.PrintCurrentStatistics();
        }
        for (int i=1;i<cfg.FLOWNUMBER;i++){
            if (!Futures.get(i).isDone()){
                try{
                    Futures.get(i).get();
                }
                catch (ExecutionException e){
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        statistics.AddLastStatisticsElement(Threads,cfg.FLOWNUMBER);
        //statistics.PrintCurrentStatistics();
        statistics.PrintFullStatistics();
    }
}
