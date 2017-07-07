import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sbt-khruzin-mm on 18.04.2017.
 */
class AsyncQueryAcctTransfer extends Thread{
    private AcctTransfer acctTransfer;
    private CustomClient client;
    private ConcurrentLinkedQueue<BenchmarkTaskResponse> response;
    AsyncQueryAcctTransfer(CustomClient client,Transfers ListOfTransfers,ConcurrentLinkedQueue<BenchmarkTaskResponse> response){
        this.acctTransfer = ListOfTransfers.getNext();
        this.client = client;
        this.response = response;
    }
    public void run(){
        double Interval = System.nanoTime();
        try {
            int Result = client.AcctTransfer(acctTransfer);
            Interval = System.nanoTime() - Interval;
            response.add(new BenchmarkTaskResponse(Interval,true,Result));
        }
        catch (Exception e){
            Interval = System.nanoTime() - Interval;
            response.add(new BenchmarkTaskResponse(Interval,false,0));
        }
    }
}
