/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
import java.util.concurrent.Callable;
public class AsyncQueryAddAccount implements Callable,BenchmarkTask{
    private CustomClient client;
    private String AccountId;
    private int AccountCache;
    AsyncQueryAddAccount(CustomClient client,String AccountId,int AccountCache){
        this.AccountCache = AccountCache;
        this.AccountId = AccountId;
        this.client = client;
    }
    public Integer call(){
        return client.AddAccount(AccountId,AccountCache);
    }
}
