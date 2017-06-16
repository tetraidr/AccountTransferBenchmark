import java.util.concurrent.Callable;

/**
 * Created by sbt-khruzin-mm on 18.04.2017.
 */
class AsyncQueryAcctTransfer implements Callable,BenchmarkTask{
    private AcctTransfer acctTransfer;
    private CustomClient client;
    AsyncQueryAcctTransfer(CustomClient client,Transfers ListOfTransfers){
        this.acctTransfer = ListOfTransfers.getNext();
        this.client = client;
    }
    public BenchmarkTaskResponse call(){
        double Interval = System.nanoTime();
        try {
            int Result = client.AcctTransfer(acctTransfer);
            Interval = System.nanoTime() - Interval;
            return new BenchmarkTaskResponse(Interval,true,Result);
        }
        catch (Exception e){
            Interval = System.nanoTime() - Interval;
            return new BenchmarkTaskResponse(Interval,false,0);
        }
    }
}
