import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by sbt-khruzin-mm on 19.05.2017.
 */
class TestRunner {
    private static ConcurrentHashMap<Integer,Thread> Threads = new ConcurrentHashMap<>();
    void RunTest(TaskGenerator taskGenerator,StatisticsThread statistics, Configuration cfg, int StatisticInterval){
        statistics.init();
        statistics.start();
        for (int i=0;i<cfg.FLOWNUMBER;i++){
            BenchmarkThread benchmarkThread = new BenchmarkThread(taskGenerator,20);
            Threads.put(i,benchmarkThread);
            benchmarkThread.start();
            try {
                Thread.sleep(Math.round(1/200*1e3));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        while (Threads.get(cfg.FLOWNUMBER-1).isAlive()) {
            try {
                Thread.sleep(StatisticInterval*1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            statistics.PrintCurrentStatistics();
        }
        for (int i=1;i<cfg.FLOWNUMBER;i++){
            if (!Threads.get(i).isAlive()){
                try{
                    Threads.get(i).join();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        statistics.TimeToStop = true;
    }
}
