/**
 * Created by sbt-khruzin-mm on 08.06.2017.
 */
class BenchmarkTaskResponse implements AutoCloseable{
    double Interval;
    boolean IsSuccessfull;
    int AnswerCode;
    BenchmarkTaskResponse(double Interval, boolean IsSuccessfull, int AnswerCode){
        this.Interval = Interval;
        this.IsSuccessfull = IsSuccessfull;
        this.AnswerCode = AnswerCode;
    }
    public void close(){
    }
}
