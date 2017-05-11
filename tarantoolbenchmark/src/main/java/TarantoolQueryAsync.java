import java.util.concurrent.Callable;

/**
 * Created by sbt-khruzin-mm on 18.04.2017.
 */
class TarantoolQueryAsync implements Callable{
    private AcctTransfer acctTransfer;
    private CustomTarantoolClient client;
    TarantoolQueryAsync(CustomTarantoolClient client, AcctTransfer accttr){
        this.acctTransfer = accttr;
        this.client = client;
    }
    public AcctTransfer call(){
        acctTransfer.Result = client.AcctTransfer(acctTransfer);
        return acctTransfer;
    }
}
