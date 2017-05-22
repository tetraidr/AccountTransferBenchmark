import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public class AcctTransferTaskGenerator implements TaskGenerator{
    private CustomClient client;
    private Transfers transfers;
    private long NumTransfers;
    private long CurTransfer;
    AcctTransferTaskGenerator(CustomClient client, ConcurrentHashMap<String,Integer> Accounts, Configuration cfg){
        this.client = client;
        this.transfers = new Transfers(Accounts,cfg);
        this.NumTransfers = cfg.TRANSFERSNUMBER;
        this.CurTransfer = 0;
    }
    public AsyncQueryAcctTransfer GetTask() {
        if (CurTransfer<NumTransfers) {
            CurTransfer++;
            return new AsyncQueryAcctTransfer(client, transfers);
        }
        else {
            return null;
        }
    }
    public boolean hasNext(){
        return CurTransfer<NumTransfers;
    }
}
