/**
 * Created by sbt-khruzin-mm on 13.04.2017.
 */
public class BenchmarkThread extends Thread{
    private double Delta;
    private double nextAct;
    private volatile TaskGenerator taskGenerator;
    private StatisticsThread statistics;
    BenchmarkThread(TaskGenerator taskGenerator, int tps, StatisticsThread statistics){
        this.taskGenerator = taskGenerator;
        this.nextAct = 0;
        this.Delta = 1e9/tps;
        this.statistics = statistics;
    }
    @Override
    public void run() {
        nextAct = System.nanoTime();
        double interval;
        while (taskGenerator.hasNext()){
            interval = nextAct-System.nanoTime();
            if (interval>0) {
                try {
                    Thread.sleep(Math.round(interval/1e6));
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            AddFutureToSubTasks();
        }
    }
    private void AddFutureToSubTasks(){
        Thread t = new Thread(taskGenerator.GetTask());
        t.start();
        nextAct += Delta;
        statistics.GAmount.increment();
    }
    public void ChangeTps(int tps){
        Delta += 1e9/tps;
    }
}
