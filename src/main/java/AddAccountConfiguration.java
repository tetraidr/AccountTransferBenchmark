import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 10.07.2017.
 */
public class AddAccountConfiguration implements TestConfiguration{
    int ACCOUNTNAMELENGTH = 8;
    int MAXACCOUNTCACHE = 1000;
    TestSettingsConfiguration TestCfg;
    AddAccountConfiguration(XMLConfiguration config){
        ACCOUNTNAMELENGTH = config.getInt("AddAccountTest.AccountIdLength");
        MAXACCOUNTCACHE = config.getInt("AddAccountTest.AccountMaxStartCache");
        TestCfg = new TestSettingsConfiguration(config, "AddAccountTest");
    }
    public int getFlowNumber(){ return  TestCfg.FLOWNUMBER; }
    public int getStatisticInterval(){
        return TestCfg.STATISTICINTERVAL_InS;
    }
    public int getGeneratorSpeed(){return TestCfg.GENERATORSPEED;}
}
