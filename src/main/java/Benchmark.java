/**
 * Created by SBT-Hruzin-MM on 09.03.2017.
 */
import java.lang.String;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {
    private static volatile ConcurrentHashMap<String,Integer> Accounts = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        Configuration cfg;
        String PathToCfg = args[0];
        String OutFileName = args[1];
        FileWriter OutFile;
        try {OutFile = new FileWriter(OutFileName);}
        catch (IOException ex){
            ex.printStackTrace();
            return;
        }
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
            case "SelfTest":
                client = new SelftTestCustomClient<String,Integer>();
                break;
            default:
                return;
        }
        System.out.format("Prepare data for loading to %s (%d records)%n",cfg.ClientName,cfg.ACCOUNTSNUMBER);
        StatisticsThread statisticsAddAccount = new StatisticsThread(OutFile);
        AddAccountTaskGenerator addAccountTaskGenerator = new AddAccountTaskGenerator(client, Accounts, cfg, statisticsAddAccount);
        System.out.println("Start loading accounts");
        testRunner.RunTest(addAccountTaskGenerator,statisticsAddAccount,cfg,cfg.STATISTICINTERVAL_ADDACCOUTTEST_InS);
        System.out.println("Start loading transfers");
        StatisticsThread statisticsAcctTransfer = new StatisticsThread(OutFile);
        AcctTransferTaskGenerator acctTransferTaskGenerator = new AcctTransferTaskGenerator(client,Accounts,cfg, statisticsAddAccount);
        testRunner.RunTest(acctTransferTaskGenerator,statisticsAcctTransfer,cfg,cfg.STATISTICINTERVAL_TRANSFERTEST_InS);
        System.out.format("Complete!");
        System.out.format("Checksums: initial %d, after test: %d",addAccountTaskGenerator.checksum, client.Checksum());
        try {OutFile.flush();}
        catch (IOException ex){
            ex.printStackTrace();
            return;
        }
        if (cfg.DROPBASEAFTERTEST.equals("Y")){
            client.drop();
        }
    }

}
