/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncQueryAddAccount extends Thread{
    private CustomClient client;
    private String AccountId;
    private int AccountCache;
    private ConcurrentLinkedQueue<BenchmarkTaskResponse> response;
    AsyncQueryAddAccount(CustomClient client, String AccountId, int AccountCache, ConcurrentLinkedQueue<BenchmarkTaskResponse> response){
        this.AccountCache = AccountCache;
        this.AccountId = AccountId;
        this.client = client;
        this.response = response;
    }
    public void run(){
        double Interval = System.nanoTime();
        try {
            int Result = client.AddAccount(AccountId,AccountCache);
            Interval = System.nanoTime() - Interval;
            response.add(new BenchmarkTaskResponse(Interval,true,Result));
        }
        catch (Exception e){
            Interval = System.nanoTime() - Interval;
            response.add(new BenchmarkTaskResponse(Interval,false,0));
        }
    }
}
