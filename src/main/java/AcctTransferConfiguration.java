import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 10.07.2017.
 */
public class AcctTransferConfiguration implements TestConfiguration{
    int FLOWNUMBER = 10;
    int GENERATORSPEED = 200;
    int GENERATORSTEPINTERVAL_InS = 60;
    int GENERATORSTEPVALUE = 100;
    int MAXTRANSFER = 500;
    int TRANSFERSNUMBER = 100000;
    int STATISTICINTERVAL_InS = 10;
    Random rnd;
    AcctTransferConfiguration(XMLConfiguration config){
        STATISTICINTERVAL_InS = config.getInt("TransferTest.StatisticIntervalSec");
        TRANSFERSNUMBER = config.getInt("TransferTest.NumberOfTransfers");
        MAXTRANSFER = config.getInt("TransferTest.MaxCacheForTransfer");
        FLOWNUMBER = config.getInt("TransferTest.FlowNumber");
        GENERATORSPEED = config.getInt("TransferTest.GeneratorSpeed");
        GENERATORSTEPINTERVAL_InS = config.getInt("TransferTest.GeneratorStepInterval");
        GENERATORSTEPVALUE = config.getInt("TransferTest.GeneratorStepValue");
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
