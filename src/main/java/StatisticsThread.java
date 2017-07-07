import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.Formatter;

/**
 * Created by sbt-khruzin-mm on 17.05.2017.
 */
public class StatisticsThread extends Thread {
    private FileWriter OutFile;
    boolean TimeToStop;
    ConcurrentLinkedQueue<BenchmarkTaskResponse> CallDuration;
    private double StartTime, PreviousTime;
    private long CurrentAmount, FullAmount;
    volatile long GAmount;
    StatisticsThread(FileWriter OutFile){
        this.OutFile = OutFile;
        this.CallDuration = new ConcurrentLinkedQueue<>();
        this.CurrentAmount = 0;
    }
    @Override
    public void run(){
        //InitValues = new SortedArrayList<>();
        BenchmarkTaskResponse NewValue;
        while(true){
            NewValue = CallDuration.poll();
            if (NewValue==null){
                if (TimeToStop){
                    break;
                }
                else {
                    Thread.yield();
                }
            }
            else {
          /*      CurrentAmount++;
                if (StartCalcPercentile) {
                    InsertNewValue(NewValue);
                    CalcPercentile();
                } else {
                    InitValues.insertSorted(NewValue);
                    if (InitValues.size() >= INITSIZE) {
                        StartCalc();
                    }
                }*/
                FPrintOneCallStatistic(NewValue);
                CurrentAmount++;
                FullAmount++;
            }
        }
        PrintFullStatistics();
    }
    private void FPrintOneCallStatistic(BenchmarkTaskResponse NewValue){

            if (NewValue.IsSuccessfull){
                Formatter f = new Formatter();
                f.format("Successful call. %f %n", NewValue.Interval);
                try {OutFile.write(f.toString());}
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
            else{
                Formatter f = new Formatter();
                f.format("Unsuccessful call. %f %n", NewValue.Interval);
                try {OutFile.write(f.toString());}
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }

    }
    void init(){
        this.StartTime = System.nanoTime();
        this.PreviousTime = System.nanoTime();
        this.CurrentAmount = 0;
        this.FullAmount = 0;
        this.GAmount = 0;
    }
    /*public double GetIntervalInMs(double t1, double t2){
        return (t1 - t2) / 1e6;
    }*/
    private double GetIntervalInS(double t1, double t2){
        return (t1 - t2) / 1e9;
    }
    /*public double CalcSpeed(StatisticsElement statistics1,StatisticsElement statistics2){
        return 1.0;// * (statistics2.Progress-statistics1.Progress) / GetIntervalInS(statistics1,statistics2);
    }*/
    void PrintCurrentStatistics(){
        System.out.format("Last %f s. Queue: %d acts. Generator speed: %f act/s. Current speed: %f act/s. Average speed: %f act/s.%n",
                          GetIntervalInS(System.nanoTime(),StartTime),
                          CallDuration.size(),
                          (double)GAmount/GetIntervalInS(System.nanoTime(),PreviousTime),
                          (double)CurrentAmount/GetIntervalInS(System.nanoTime(),PreviousTime),
                          (double)FullAmount/GetIntervalInS(System.nanoTime(),StartTime));
        CurrentAmount = 0;
        GAmount = 0;
        PreviousTime = System.nanoTime();
        /*Formatter f = new Formatter();
        f.format("%f %f %f%n", GetIntervalInS(statistics.get(0),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(CurrentStage-2),statistics.get(CurrentStage-1)), CalcSpeed(statistics.get(0),statistics.get(CurrentStage-1)));
        try {OutFile.write(f.toString());}
        catch (IOException ex){
            ex.printStackTrace();
        }*/
    }
    private void PrintFullStatistics(){
        //System.out.format("Loading to complete in %f s. Speed: %f act/s. %n", );
    }
    /*public boolean verifyCheckSum(){
        return statistics.get(CurrentStage).CheckSum == statistics.get(0).CheckSum;
    }*/
    /*public boolean verifyCheckSum(long CheckSum){
        return this.CheckSum == CheckSum;
    }*/
    /*void clear(){
        FullAmount = 0;
    }*/
}
