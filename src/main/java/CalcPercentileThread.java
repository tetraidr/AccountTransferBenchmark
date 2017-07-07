import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
/**
 * Created by sbt-khruzin-mm on 14.06.2017.
 */
public class CalcPercentileThread implements Callable{
    volatile ConcurrentLinkedQueue<Double> CallDuration;
    boolean StartCalcPercentile;
    int PercentileCalcMethod;
    final int NIARESTRANK = 1;
    int[] PercentileRanks = {95,97,99};
    double[] Percentiles;
    private SortedArrayList<Double> InitValues;
    private int SortedArrayRange;
    int INITSIZE=2000;
    long CurrentAmount;
    public boolean TimeToStop;
    ArrayList<SortedArrayList<Double>> PercentileStatistic;
    ArrayList<UnsortedArrayList<Double>> PercentileStaticticCache;
    public CalcPercentileThread(ConcurrentLinkedQueue<Double> CallDuration){
        this.CallDuration = CallDuration;
        this.StartCalcPercentile = false;
        this.SortedArrayRange = INITSIZE/200-1;
        for (int i = 0; i < PercentileRanks.length; i++){
            this.Percentiles[i] = PercentileRanks[i];
        }
        this.CurrentAmount = 0;
        this.TimeToStop = false;
    }
    @Override
    public Integer call(){
        InitValues = new SortedArrayList<>();
        while(true){
            Double NewValue = null;
            NewValue = CallDuration.poll();
            if (NewValue==null){
                if (TimeToStop){
                    break;
                }
            }
            else {
                CurrentAmount++;
                if (StartCalcPercentile) {
                    InsertNewValue(NewValue);
                    CalcPercentile();
                } else {
                    InitValues.insertSorted(NewValue);
                    if (InitValues.size() >= INITSIZE) {
                        StartCalc();
                    }
                }
            }
        }
        return 1;
    }
    private void StartCalc(){
        for (int i=0;i<PercentileRanks.length;i++){
            int NearestRank = INITSIZE*PercentileRanks[i]/100;
            PercentileStatistic.add((SortedArrayList)InitValues.subList(NearestRank-SortedArrayRange,NearestRank+SortedArrayRange-2));
            PercentileStatistic.get(i).NeedRebalance = false;
        }
        for (int i=0;i<=PercentileRanks.length;i++) {
            PercentileStaticticCache.add(new UnsortedArrayList<>());
            PercentileStaticticCache.get(i).putAll(InitValues.subList(i == 0 ? 0 : INITSIZE * PercentileRanks[i - 1] / 100 + SortedArrayRange - 1, INITSIZE * PercentileRanks[i] / 100 - SortedArrayRange - 1));
        }
    }
    private void InsertNewValue(Double NewValue){
        int i=0;
        while (true){
            if (NewValue<PercentileStatistic.get(i).lower()){
                PercentileStaticticCache.get(i).put(NewValue);
            }
            else if (NewValue<PercentileStatistic.get(i).higher()){
                PercentileStatistic.get(i).insertSorted(NewValue);
                PercentileStatistic.get(i).NeedRebalance = true;
            }
            else if (i<PercentileStatistic.size()){
                i++;
            }
            else{
                break;
            }
        }
    }
    private void CalcPercentile(){
        long CurrentPercentileRank = Math.round(((double)CurrentAmount)*PercentileRanks[0]/100)-PercentileStaticticCache.get(0).size();;
        for (int i=0; i<PercentileRanks.length; i++){
            if (CurrentPercentileRank<0){
                relocate(CurrentPercentileRank,i);
            }
            else if (CurrentPercentileRank>PercentileStatistic.get(i).size()){

            }
            else{
                Percentiles[i] = PercentileStatistic.get(i).get((int)CurrentPercentileRank);
            }
        }
    }
    private void relocate(long CurrentPercentileRank, int i){
        int length = SortedArrayRange*2+1;
        if (CurrentPercentileRank<0){
            ArrayList<Double> tmp = new ArrayList<>(PercentileStaticticCache.get(i).getMaxValues((int)(length*(1+Math.abs(CurrentPercentileRank)/length))));
            PercentileStaticticCache.get(i+1).putAll(PercentileStatistic.get(i).subList(0,PercentileStatistic.get(0).size()-1));
            PercentileStatistic.get(i).clear();
            PercentileStatistic.get(i).addAll(tmp.subList(0,length-1));
            if (tmp.size()>length){
                PercentileStaticticCache.get(i+1).putAll(tmp.subList(length,tmp.size()-1));
            }
        }
        if (i>0){

        }
    }
}
