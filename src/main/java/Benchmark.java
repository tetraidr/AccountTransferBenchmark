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
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Benchmark {
    private static volatile ConcurrentHashMap<String,Integer> Accounts = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        //Logger logger = LoggerFactory.getLogger("Benchmark");
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
        TestRunner testRunner = new TestRunner();
        testRunner.RunTest("AddAccount", new AddAccountTaskGenerator(client, Accounts, cfg.addaccountcfg), cfg.addaccountcfg, OutFile);
        testRunner.RunTest("AcctTransfer", new AcctTransferTaskGenerator(client, Accounts, cfg.accttransfercfg), cfg.accttransfercfg, OutFile);
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
