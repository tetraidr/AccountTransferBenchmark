/**
 * Created by SBT-Hruzin-MM on 09.03.2017.
 */
import java.lang.String;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Benchmark {
    private static volatile ConcurrentHashMap<String,Integer> Accounts = new ConcurrentHashMap<>();
    private static Configuration cfg;
    public static void main(String[] args) throws InterruptedException {
        String PathToCfg = args[0];
        Configurations configs = new Configurations();
        try
        {
            XMLConfiguration config = configs.xml(PathToCfg);
            cfg = new Configuration(config);

        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
            return;
        }
        TestRunner testRunner = new TestRunner();
        System.out.println("Benchmark Test:");
        System.out.println("Connecting");
        CustomClient client;
        switch (cfg.ClientName){
            case "Tarantool":
                client = new CustomTarantoolClient(cfg.TarantoolHostName, cfg.TarantoolPort, cfg.TarantoolUserName, cfg.TarantoolPassword);
                break;
            case "Ignite":
                client = new IgniteCustomClient(cfg.IgniteCfgPath);
                break;
            default:
                return;
        }
        System.out.format("Prepare data for loading to %s (%d records)%n",cfg.ClientName,cfg.ACCOUNTSNUMBER);
        AddAccountTaskGenerator addAccountTaskGenerator = new AddAccountTaskGenerator(client, Accounts, cfg);
        Statistics statisticsAddAccount = new Statistics(addAccountTaskGenerator.checksum);
        System.out.format("Complete! Sum of accounts: %d%n",statisticsAddAccount.CheckSum);
        System.out.println("Start loading accounts");
        testRunner.RunTest(addAccountTaskGenerator,statisticsAddAccount,cfg,cfg.STATISTICINTERVAL_ADDACCOUTTEST_InS);
        System.out.println("Start loading transfers");
        AcctTransferTaskGenerator acctTransferTaskGenerator = new AcctTransferTaskGenerator(client,Accounts,cfg);
        Statistics statisticsAcctTransfer = new Statistics(statisticsAddAccount.CheckSum);
        testRunner.RunTest(acctTransferTaskGenerator,statisticsAcctTransfer,cfg,cfg.STATISTICINTERVAL_TRANSFERTEST_InS);
        System.out.format("Complete!");
        if (cfg.DROPBASEAFTERTEST.equals("Y")){
            client.drop();
        }
    }

}
