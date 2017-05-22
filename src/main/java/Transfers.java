import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 14.04.2017.
 */
public class Transfers{
    private List<String> keys;
    private Random lrnd;
    private int MaxTransfer;
    Transfers(ConcurrentHashMap<String,Integer> Accounts,Configuration cfg){
        keys = new ArrayList<>(Accounts.keySet());
        lrnd = cfg.rnd;
        this.MaxTransfer = cfg.MAXTRANSFER;
    }
    public AcctTransfer getNext(){
        String rndKey1 = keys.get(lrnd.nextInt(keys.size()));
        String rndKey2;
        do{
            rndKey2 = keys.get(lrnd.nextInt(keys.size()));
        } while (rndKey2.equals(rndKey1));
        return new AcctTransfer(rndKey1,rndKey2,lrnd.nextInt(MaxTransfer));
    }
}
