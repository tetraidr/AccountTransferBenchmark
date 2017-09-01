import java.io.FileWriter;
import java.util.concurrent.*;

/**
 * Created by sbt-khruzin-mm on 19.05.2017.
 */
class TestRunner {
    private static ConcurrentHashMap<Integer,Thread> Threads = new ConcurrentHashMap<>();
    void RunTest(String testName, TaskGenerator taskGenerator, TestConfiguration cfg, FileWriter OutFile){
        System.out.format("Preparing %s test %n",testName);
        StatisticsThread statistics = new StatisticsThread(OutFile);
        taskGenerator.assignStatisticQueue(statistics.getStatisticsQueue());
        statistics.init();
        statistics.start();
        for (int i=0;i<cfg.getFlowNumber();i++){
            BenchmarkThread benchmarkThread = new BenchmarkThread(taskGenerator,cfg.getGeneratorSpeed()/cfg.getFlowNumber(), statistics);
            Threads.put(i,benchmarkThread);
            benchmarkThread.start();
            try {
                Thread.sleep(Math.round(1e3/cfg.getGeneratorSpeed()));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        while (Threads.get(cfg.getFlowNumber()-1).isAlive()) {
            try {
                Thread.sleep(cfg.getStatisticInterval()*1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            statistics.PrintCurrentStatistics();
        }
        for (int i=1;i<cfg.getFlowNumber();i++){
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
        System.out.format("Test %s complete %n", testName);
    }
}
