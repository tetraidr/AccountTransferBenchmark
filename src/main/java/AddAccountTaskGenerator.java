import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public class AddAccountTaskGenerator implements TaskGenerator{
    private CustomClient client;
    private ConcurrentHashMap<String,Integer> Accounts;
    private int AccountsNumber;
    private int MaxAccountCache;
    private int AccountNameLength;
    private static final String characters = "qwertyuiopasdfghjklzxcvbnm1234567890";
    private int charactersNum;
    long checksum;
    private Iterator<Map.Entry<String,Integer>> entries;
    private StatisticsThread statistics;
    AddAccountTaskGenerator(CustomClient client, ConcurrentHashMap<String,Integer> Accounts, Configuration cfg, StatisticsThread statistics){
        this.client = client;
        this.Accounts = Accounts;
        this.AccountsNumber = cfg.ACCOUNTSNUMBER;
        this.MaxAccountCache = cfg.MAXACCOUNTCACHE;
        this.AccountNameLength = cfg.ACCOUNTNAMELENGTH;
        this.charactersNum = characters.length();
        checksum = GenerateRndAccounts(cfg.rnd);
        entries = Accounts.entrySet().iterator();
        this.statistics = statistics;
    }
    public AsyncQueryAddAccount GetTask(){
        if (entries.hasNext()) {
            Map.Entry<String,Integer> entry = entries.next();
            return new AsyncQueryAddAccount(client, entry.getKey(), entry.getValue(), statistics.CallDuration);
        }
        else
        {
            return null;
        }
    }
    public boolean hasNext(){
        return entries.hasNext();
    }
    private long GenerateRndAccounts(Random rnd){
        int i = 0;
        String AcctId;
        int sum;
        long CheckSum = 0;
        while (i<AccountsNumber){
            AcctId = GenerateRndAccount(rnd);
            sum = rnd.nextInt(MaxAccountCache);
            if (Accounts.get(AcctId)==null){
                Accounts.put(AcctId,sum);
                i++;
                CheckSum += sum;
            }
        }
        return CheckSum;
    }

    private String GenerateRndAccount(Random rnd){
        String AcctId = "";
        int num;
        for (int i = 0; i<AccountNameLength; i++) {
            num = rnd.nextInt(charactersNum);
            AcctId = AcctId.concat(characters.substring(num, num + 1));
        }
        return AcctId;
    }
    public StatisticsThread getStatistics(){
        return statistics;
    }
}
