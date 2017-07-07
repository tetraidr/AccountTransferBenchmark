import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 22.05.2017.
 */
class Configuration {
    int ACCOUNTNAMELENGTH = 8;
    int MAXACCOUNTCACHE = 1000;
    int MAXTRANSFER = 500;
    int ACCOUNTSNUMBER = 5000;
    int TRANSFERSNUMBER = 100000;
    int FLOWNUMBER = 10;
    int ASYNCREQUESTPERFLOW = 150;
    Random rnd;
    int STATISTICINTERVAL_ADDACCOUTTEST_InS = 1;
    int STATISTICINTERVAL_TRANSFERTEST_InS = 10;
    String ClientName;
    String TarantoolHostName;
    int TarantoolPort;
    String TarantoolUserName;
    String TarantoolPassword;
    String IgniteCfgPath;
    String DROPBASEAFTERTEST;
    Configuration(XMLConfiguration config){
        ACCOUNTNAMELENGTH = config.getInt("AddAccountTest.AccountIdLength");
        MAXACCOUNTCACHE = config.getInt("AddAccountTest.AccountMaxStartCache");
        MAXTRANSFER = config.getInt("TransferTest.MaxCacheForTransfer");
        ACCOUNTSNUMBER = config.getInt("AddAccountTest.NumberOfAccounts");
        TRANSFERSNUMBER = config.getInt("TransferTest.NumberOfTransfers");
        FLOWNUMBER = config.getInt("FlowNumber");
        ASYNCREQUESTPERFLOW = config.getInt("AsyncRequestsPerFlow");
        STATISTICINTERVAL_ADDACCOUTTEST_InS = config.getInt("AddAccountTest.StatisticIntervalSec");
        STATISTICINTERVAL_TRANSFERTEST_InS = config.getInt("TransferTest.StatisticIntervalSec");
        if (config.getString("RandomSeed")==null){
            rnd = new Random(System.nanoTime());
        }
        else{
            rnd = new Random(config.getInt("RandomSeed"));
        }
        switch (config.getString("Client[@type]")){
            case "Tarantool":
                ClientName = "Tarantool";
                TarantoolHostName = config.getString("Client.HostName");
                TarantoolPort = config.getInt("Client.Port");
                TarantoolUserName = config.getString("Client.UserName");
                TarantoolPassword = config.getString("Client.Password");
                break;
            case "Ignite":
                ClientName = "Ignite";
                IgniteCfgPath = config.getString("Client.CfgPath");
                break;
            case "SelfTest":
                ClientName = "SelfTest";
        }
        DROPBASEAFTERTEST = config.getString("DropBaseAfterTest");
    }
}
