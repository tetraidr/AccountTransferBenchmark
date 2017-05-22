import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.processors.cache.CacheEntryImpl;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionDeadlockException;
import org.apache.ignite.transactions.TransactionTimeoutException;

import java.util.Random;

import static org.apache.ignite.internal.util.typedef.X.hasCause;
import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.SERIALIZABLE;

/**
 * Created by sbt-khruzin-mm on 22.05.2017.
 */
public class IgniteCustomClient implements CustomClient {
    private  static String CACHE_NAME = "BenchmarkSpace";
    private static volatile Ignite ignite = null;
    private static volatile CacheConfiguration<String, Integer> cfg;
    private static volatile IgniteCache<String, Integer> cache;
    private Random rnd = new Random(System.currentTimeMillis());
    public IgniteCustomClient(){
        ignite = Ignition.start("D:\\work\\IdeaProjects\\tarantoolbenchmark_i\\config\\ignitecl.xml");
        cfg = new CacheConfiguration<>(CACHE_NAME);
        cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cache = ignite.getOrCreateCache(cfg);
    }
    public int AddAccount(String AcctId, Integer sum){
        cache.put(AcctId, sum);
        return 1;
    }
    public int AcctTransfer(AcctTransfer transfer){
        try (Transaction tx = ignite.transactions().txStart(PESSIMISTIC, SERIALIZABLE, 10000, 0)) {
            Integer acct1 = cache.get(transfer.AccountFrom);
            Integer acct2 = cache.get(transfer.AccountTo);
            if ((acct1!=null) & (acct2!=null)){
                if (acct1 - transfer.TransferAmount>=0){
                    cache.put(transfer.AccountFrom,acct1-transfer.TransferAmount);
                    cache.put(transfer.AccountTo,acct2+transfer.TransferAmount);
                }
            }
            tx.commit();
        }
        catch (Throwable e){
            if (hasCause(e, TransactionDeadlockException.class)|hasCause(e, TransactionTimeoutException.class)){
                System.out.format("Deadlock! %n");
                try {
                    Thread.sleep(100+rnd.nextInt(200));
                } catch (Exception e1) {
                    System.out.println(e1);
                }
                this.AcctTransfer(transfer);
            }
            else {
                e.printStackTrace();
            }
        }
        return 1;
    }
    public Long Checksum(){
        Long res = new Long(0);
        try (QueryCursor cursor = cache.query(new ScanQuery<String,Integer>((k, p) -> true))){
            for (Object p:cursor){

                res += new Long(((CacheEntryImpl)p).getValue().toString());
                //System.out.format("%d", tmp);
            }
        }
        return res;
    }
    public void drop(){
        ignite.destroyCache(CACHE_NAME);
        ignite.close();
    }
}
