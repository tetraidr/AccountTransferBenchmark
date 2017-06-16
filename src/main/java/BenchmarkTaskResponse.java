/**
 * Created by sbt-khruzin-mm on 08.06.2017.
 */
class BenchmarkTaskResponse {
    double Interval;
    boolean IsSuccessfull;
    int AnswerCode;
    public BenchmarkTaskResponse(double Interval, boolean IsSuccessfull, int AnswerCode){
        this.Interval = Interval;
        this.IsSuccessfull = IsSuccessfull;
        this.AnswerCode = AnswerCode;
    }
}
