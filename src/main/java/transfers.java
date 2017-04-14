import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 14.04.2017.
 */
public class transfers {
    private ConcurrentHashMap<Integer,AcctTransfer> Transfers = new ConcurrentHashMap<>();
    //Iterator entries;
    transfers(int m, ConcurrentHashMap<String,Integer> Accounts,Random rnd){
        AcctTransfer transfer;
        String rndKey1,rndKey2;
        List<String> keys = new ArrayList<>(Accounts.keySet());
        for (int i = 0;i<m;i++) {
            rndKey1 = keys.get(rnd.nextInt(keys.size()));
            do{
                rndKey2 = keys.get(rnd.nextInt(keys.size()));
            } while (rndKey2!=rndKey1);
            transfer = new AcctTransfer(rndKey1,rndKey2,rnd.nextInt(500));
            Transfers.put(i,transfer);
            System.out.format("Client1 name (from): %s, client2 name (to): %s sum: %d %n", transfer.AccountFrom, transfer.AccountTo, transfer.TransferAmount);
        }
        //Iterator<Transfers.Entry<String, String>> entries = Transfers.entrySet().iterator();
        Iterator<Transfers.Entry<Integer,AcctTransfer>> entries;
    }
}
