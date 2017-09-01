import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 22.05.2017.
 */
class Configuration {
    String ClientName;
    String TarantoolHostName;
    int TarantoolPort;
    String TarantoolUserName;
    String TarantoolPassword;
    String IgniteCfgPath;
    String DROPBASEAFTERTEST;
    String FILENAME;
    AddAccountConfiguration addaccountcfg;
    AcctTransferConfiguration accttransfercfg;
    Configuration(XMLConfiguration config){
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
        if (config.getString("CopyOutToFile[@copy]").equals("yes")){
            FILENAME = config.getString("CopyOutToFile.FileName");
        }
        DROPBASEAFTERTEST = config.getString("DropBaseAfterTest");
        addaccountcfg = new AddAccountConfiguration(config);
        accttransfercfg = new AcctTransferConfiguration(config);
    }
}
