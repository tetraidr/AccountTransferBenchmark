/**
 * Created by sbt-khruzin-mm on 17.05.2017.
 */
public class StatisticsElement {
    public long TimeStamp;
    public long Progress;
    public long CheckSum;
    StatisticsElement(){
        this.TimeStamp = 0;
        this.Progress = 0;
        this.CheckSum = 0;
    }
    StatisticsElement(long TimeStamp,long Progress){
        this.TimeStamp = TimeStamp;
        this.Progress = Progress;
        this.CheckSum = 0;
    }
    StatisticsElement(long TimeStamp,long Progress,long CheckSum){
        this.TimeStamp = TimeStamp;
        this.Progress = Progress;
        this.CheckSum = CheckSum;
    }
}