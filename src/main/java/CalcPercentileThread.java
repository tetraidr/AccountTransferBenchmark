import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by sbt-khruzin-mm on 14.06.2017.
 */
public class CalcPercentileThread implements Callable{
    volatile LinkedBlockingQueue<Double> CallDuration;
    boolean StartCalcPercentile;
    int PercentileCalcMethod;
    final int NIARESTRANK = 1;
    int[] PercentileRanks = {95,97,99};
    private SortedArrayList<Double> InitValues;
    private int SortedArrayRange;
    int INITSIZE=2000;
    ArrayList<ArrayList<Double>> PercentileStatistic;
    ArrayList<HashMap<Integer,Double>> PercentileStaticticCache;
    public CalcPercentileThread(LinkedBlockingQueue<Double> CallDuration){
        this.CallDuration = CallDuration;
        this.StartCalcPercentile = false;
        this.SortedArrayRange = INITSIZE/200-1;
    }
    @Override
    public Integer call(){
        InitValues = new SortedArrayList<>();
        while(true){
            Double NewValue = null;
            try {
                NewValue = CallDuration.poll(10, TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if (NewValue==null){
                break;
            }
            if (StartCalcPercentile){

            }
            else
            {
                InitValues.insertSorted(NewValue);
                if (InitValues.size()>=INITSIZE){
                    StartCalc();
                }
            }
        }
        return 1;
    }
    public void StartCalc(){
        PercentileStatistic.add(InitValues);
        for (int i=0;i<PercentileRanks.length;i++){
            int NearestRank = INITSIZE*PercentileRanks[i]/100;
            PercentileStatistic.add((ArrayList)InitValues.subList(NearestRank-SortedArrayRange,NearestRank+SortedArrayRange-2));
        }
        for (int i=0;i<=PercentileRanks.length;i++) {
            InitValues.subList(i == 0 ? 0 : INITSIZE * PercentileRanks[i - 1] / 100 + SortedArrayRange - 1, INITSIZE * PercentileRanks[i] / 100 - SortedArrayRange - 1);
        }
    }
}
