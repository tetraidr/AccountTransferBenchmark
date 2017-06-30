import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Formatter;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sbt-khruzin-mm on 17.05.2017.
 */
public class Statistics{
    private static ConcurrentHashMap<Integer,StatisticsElement> statistics;
    int CurrentStage;
    long CheckSum;
    FileWriter OutFile;
    //
    volatile LinkedBlockingQueue<Double> CallDuration;

    double StartTime;
    long FullAmount, CurrentAmount;
    TreeSet<Double> Pеrcentile, Pеrcentile90, Pеrcentile95, Pеrcentile97, Pеrcentile99;
    Statistics(long CheckSum, FileWriter OutFile){
        statistics = new ConcurrentHashMap<>();
        CurrentStage = 0;
        this.CheckSum = CheckSum;
        this.OutFile = OutFile;
        TreeSet<Double> list = new TreeSet<>();
        this.CallDuration = new LinkedBlockingQueue<>();
    }
    public void init(){
        this.StartTime = System.nanoTime();
        this.FullAmount = 0;
        this.CurrentAmount = 0;
    }
    public void AddStatisticsElement(long TimeStamp,long Progress,long CheckSum){
        StatisticsElement NewElement = new StatisticsElement(TimeStamp,Progress,CheckSum);
        statistics.put(CurrentStage,NewElement);
        CurrentStage++;
    }
    public void AddStatisticsElement(long TimeStamp,long Progress){
        StatisticsElement NewElement = new StatisticsElement(TimeStamp,Progress);
        statistics.put(CurrentStage,NewElement);
        CurrentStage++;
    }
    public void AddStatisticsElement(ConcurrentHashMap<Integer,BenchmarkThread> Threads, int FlowNumber){
        long CurrentProgress;
        CurrentProgress = 0;
        for (int i = 0; i < FlowNumber; i++) {
            CurrentProgress += Threads.get(i).ActsDone;
        }
        StatisticsElement NewElement = new StatisticsElement(System.nanoTime(),CurrentProgress);
        statistics.put(CurrentStage,NewElement);
        CurrentStage++;
    }
    public void AddLastStatisticsElement(ConcurrentHashMap<Integer,BenchmarkThread> Threads, int FlowNumber){
        long CurrentProgress;
        CurrentProgress = 0;
        for (int i = 0; i < FlowNumber; i++) {
            CurrentProgress += Threads.get(i).ActsDone;
        }
        if (CurrentProgress != statistics.get(CurrentStage-1).Progress) {
            StatisticsElement NewElement = new StatisticsElement(System.nanoTime(), CurrentProgress);
            statistics.put(CurrentStage, NewElement);
            CurrentStage++;
        }
    }
    public double GetIntervalInMs(StatisticsElement statistics1,StatisticsElement statistics2){
        return (statistics2.TimeStamp - statistics1.TimeStamp) / 1e6;
    }
    public double GetIntervalInS(StatisticsElement statistics1,StatisticsElement statistics2){
        return (statistics2.TimeStamp - statistics1.TimeStamp) / 1e9;
    }
    public double CalcSpeed(StatisticsElement statistics1,StatisticsElement statistics2){
        return 1.0 * (statistics2.Progress-statistics1.Progress) / GetIntervalInS(statistics1,statistics2);
    }
    public void PrintCurrentStatistics(){
        System.out.format("Last %f s. Current speed: %f act/s. Average speed: %f act/s.%n", GetIntervalInS(statistics.get(0),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(CurrentStage-2),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(0),statistics.get(CurrentStage-1)));
        Formatter f = new Formatter();
        f.format("%f %f %f%n", GetIntervalInS(statistics.get(0),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(CurrentStage-2),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(0),statistics.get(CurrentStage-1)));
        try {OutFile.write(f.toString());}
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void PrintFullStatistics(){
        System.out.format("Loading to complete in %f s. Speed: %f act/s. %n", GetIntervalInS(statistics.get(0),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(0),statistics.get(CurrentStage-1)));
    }
    public boolean verifyCheckSum(){
        return statistics.get(CurrentStage).CheckSum == statistics.get(0).CheckSum;
    }
    public boolean verifyCheckSum(long CheckSum){
        return this.CheckSum == CheckSum;
    }
    public void clear(){
        statistics.clear();
        CurrentStage = 0;
        CheckSum = 0;
    }
    public void AddStatisticsElement(BenchmarkTaskResponse response){
        CallDuration.offer(response.Interval);
        FullAmount++;
        CurrentAmount++;
    }
}
