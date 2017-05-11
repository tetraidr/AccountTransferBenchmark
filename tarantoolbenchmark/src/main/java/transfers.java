import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 14.04.2017.
 */
public class transfers{
    private ConcurrentHashMap<Integer,AcctTransfer> Transfers = new ConcurrentHashMap<>();
    private Iterator<Map.Entry<Integer,AcctTransfer>> entries;
    transfers(int m, ConcurrentHashMap<String,Integer> Accounts,Random rnd){
        AcctTransfer transfer;
        String rndKey1,rndKey2;
        List<String> keys = new ArrayList<>(Accounts.keySet());
        for (int i = 0;i<m;i++) {
            rndKey1 = keys.get(rnd.nextInt(keys.size()));
            do{
                rndKey2 = keys.get(rnd.nextInt(keys.size()));
            } while (rndKey2==rndKey1);
            transfer = new AcctTransfer(rndKey1,rndKey2,rnd.nextInt(500));
            Transfers.put(i,transfer);
            //System.out.format("Client1 name (from): %s, client2 name (to): %s sum: %d %n", transfer.AccountFrom, transfer.AccountTo, transfer.TransferAmount);
        }
        entries = Transfers.entrySet().iterator();
    }
    public AcctTransfer getNext(){
        if (entries.hasNext()) {
            Map.Entry<Integer,AcctTransfer> entry = entries.next();
            return entry.getValue();
        }
        else {
            return null;
        }
    }
}
