import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 10.07.2017.
 */
public class AddAccountConfiguration implements TestConfiguration{
    int ACCOUNTNAMELENGTH = 8;
    int MAXACCOUNTCACHE = 1000;
    int ACCOUNTSNUMBER = 5000;
    int STATISTICINTERVAL_InS = 1;
    int FLOWNUMBER = 10;
    int GENERATORSPEED = 200;
    int GENERATORSTEPINTERVAL_InS = 60;
    int GENERATORSTEPVALUE = 100;
    Random rnd;
    AddAccountConfiguration(XMLConfiguration config){
        ACCOUNTNAMELENGTH = config.getInt("AddAccountTest.AccountIdLength");
        MAXACCOUNTCACHE = config.getInt("AddAccountTest.AccountMaxStartCache");
        ACCOUNTSNUMBER = config.getInt("AddAccountTest.NumberOfAccounts");
        STATISTICINTERVAL_InS = config.getInt("AddAccountTest.StatisticIntervalSec");
        FLOWNUMBER = config.getInt("AddAccountTest.FlowNumber");
        GENERATORSPEED = config.getInt("AddAccountTest.GeneratorSpeed");
        GENERATORSTEPINTERVAL_InS = config.getInt("AddAccountTest.GeneratorStepInterval");
        GENERATORSTEPVALUE = config.getInt("AddAccountTest.GeneratorStepValue");
        if (config.getString("RandomSeed")==null){
            rnd = new Random(System.nanoTime());
        }
        else{
            rnd = new Random(config.getInt("RandomSeed"));
        }
    }
    public int getFlowNumber(){
        return  FLOWNUMBER;
    }
    public int getStatisticInterval(){
        return STATISTICINTERVAL_InS;
    }
    public int getGeneratorSpeed(){return GENERATORSPEED;}
}
