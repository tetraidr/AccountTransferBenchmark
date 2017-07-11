import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public class AcctTransferTaskGenerator implements TaskGenerator{
    private CustomClient client;
    private Transfers transfers;
    private long NumTransfers;
    private long CurTransfer;
    private StatisticsThread statistics;
    AcctTransferTaskGenerator(CustomClient client, ConcurrentHashMap<String,Integer> Accounts, AcctTransferConfiguration cfg, StatisticsThread statistics){
        this.client = client;
        this.transfers = new Transfers(Accounts,cfg);
        this.NumTransfers = cfg.TRANSFERSNUMBER;
        this.CurTransfer = 0;
        this.statistics = statistics;
    }
    public AsyncQueryAcctTransfer GetTask() {
        if (CurTransfer<NumTransfers) {
            CurTransfer++;
            return new AsyncQueryAcctTransfer(client, transfers, statistics.CallDuration);
        }
        else {
            return null;
        }
    }
    public boolean hasNext(){
        return CurTransfer<NumTransfers;
    }
    public StatisticsThread getStatistics(){
        return statistics;
    }
}
