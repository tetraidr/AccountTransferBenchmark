import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 07.07.2017.
 */
public class SelftTestCustomClient<K,V> extends ConcurrentHashMap implements CustomClient{
    public int AcctTransfer(AcctTransfer transfer){
        if (this.containsKey(transfer.AccountFrom) & this.containsKey(transfer.AccountTo)){
            if ((Integer)this.get(transfer.AccountFrom)-transfer.TransferAmount>0){
                this.put(transfer.AccountFrom, (Integer)this.get(transfer.AccountFrom)-transfer.TransferAmount);
                this.put(transfer.AccountTo, (Integer)this.get(transfer.AccountTo)+transfer.TransferAmount);
            }
        }
        return 0;
    }
    public int AddAccount(String AcctId, Integer sum){
        this.put(AcctId,sum);
        return 1;
    }
    public Long Checksum(){
        return new Long(1);
    }
    public void drop(){
        //
    }
}
