import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public class AcctTransferTaskGenerator implements TaskGenerator{
    private CustomClient client;
    private Transfers transfers;
    private long NumTransfers;
    private long CurTransfer;
    private ConcurrentLinkedQueue<BenchmarkTaskResponse> statisticsQueue;
    AcctTransferTaskGenerator(CustomClient client, ConcurrentHashMap<String,Integer> Accounts, AcctTransferConfiguration cfg){
        this.client = client;
        this.transfers = new Transfers(Accounts,cfg);
        this.NumTransfers = cfg.TRANSFERSNUMBER;
        this.CurTransfer = 0;
    }
    public AsyncQueryAcctTransfer GetTask() {
        if (CurTransfer<NumTransfers) {
            CurTransfer++;
            return new AsyncQueryAcctTransfer(client, transfers, statisticsQueue);
        }
        else {
            return null;
        }
    }
    public boolean hasNext(){
        return CurTransfer<NumTransfers;
    }
    public void assignStatisticQueue(ConcurrentLinkedQueue<BenchmarkTaskResponse> statisticsQueue){
        this.statisticsQueue = statisticsQueue;
    }
}
