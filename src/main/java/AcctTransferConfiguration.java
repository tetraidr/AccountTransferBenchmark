import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 10.07.2017.
 */
public class AcctTransferConfiguration implements TestConfiguration{
    int MAXTRANSFER = 500;
    TestSettingsConfiguration TestCfg;
    AcctTransferConfiguration(XMLConfiguration config){
        MAXTRANSFER = config.getInt("TransferTest.MaxCacheForTransfer");
        TestCfg = new TestSettingsConfiguration(config, "TransferTest");
    }
    public int getFlowNumber(){
        return  TestCfg.FLOWNUMBER;
    }
    public int getStatisticInterval(){
        return TestCfg.STATISTICINTERVAL_InS;
    }
    public int getGeneratorSpeed(){return TestCfg.GENERATORSPEED;}
}
