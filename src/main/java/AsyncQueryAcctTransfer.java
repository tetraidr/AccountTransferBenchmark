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
    public Integer call(){
        acctTransfer.Result = client.AcctTransfer(acctTransfer);
        return acctTransfer.Result;
    }
}
