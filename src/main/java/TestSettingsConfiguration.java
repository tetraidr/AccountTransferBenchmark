import org.apache.commons.configuration2.XMLConfiguration;

import java.util.Random;

/**
 * Created by sbt-khruzin-mm on 13.07.2017.
 */
public class TestSettingsConfiguration {
    int GENERATORSPEED = 200;
    int GENERATORSTEPINTERVAL_InS = 60;
    int GENERATORSTEPVALUE = 100;
    int STATISTICINTERVAL_InS = 10;
    int SPEPSNUMBER = 10;
    int ACTSNUMBER = 1000;
    int TIME = 5000;
    String TESTTYPE = "Const";
    String STOPCRITERIA = "Time";
    int FLOWNUMBER = 10;
    Random rnd;
    String FILENAME = "";
    TestSettingsConfiguration(XMLConfiguration config, String testName) {
        STATISTICINTERVAL_InS = config.getInt(testName.concat(".StatisticIntervalSec"));
        FLOWNUMBER = config.getInt(testName.concat(".FlowNumber"));
        if (config.getString(testName.concat(".RandomSeed"))==null){
            rnd = new Random(System.nanoTime());
        }
        else{
            rnd = new Random(config.getInt(testName.concat(".RandomSeed")));
        }
        TESTTYPE = config.getString(testName.concat(".TestSettings[@type]"));
        switch (TESTTYPE){
            case "StepByStep":
                GENERATORSPEED = config.getInt(testName.concat(".TestSettings.GeneratorSpeed"));
                GENERATORSTEPINTERVAL_InS = config.getInt(testName.concat(".TestSettings.GeneratorStepInterval"));
                GENERATORSTEPVALUE = config.getInt(testName.concat(".TestSettings.GeneratorStepValue"));
            case "Const":
                GENERATORSPEED = config.getInt(testName.concat(".TestSettings.GeneratorSpeed"));
            case "1Step":
                GENERATORSPEED = config.getInt(testName.concat(".TestSettings.GeneratorSpeed"));
                GENERATORSTEPINTERVAL_InS = config.getInt(testName.concat(".TestSettings.GeneratorStepInterval"));
                GENERATORSTEPVALUE = config.getInt(testName.concat(".TestSettings.GeneratorStepValue"));
            case "WarmUp_Max":
                GENERATORSPEED = config.getInt(testName.concat(".TestSettings.GeneratorSpeed"));
                GENERATORSTEPINTERVAL_InS = config.getInt(testName.concat(".TestSettings.GeneratorStepInterval"));
            case "Max":
        }
        STOPCRITERIA = config.getString(testName.concat(".AddAccountTest.StopCriteria[@type]"));
        switch (STOPCRITERIA){
            case "ActsNumber":
                ACTSNUMBER = config.getInt(testName.concat(".TestSettings.NumberOfActs"));
            case "StepsNumber":
                SPEPSNUMBER = config.getInt(testName.concat(".TestSettings.NumberOfSteps"));
            case "Time":
                TIME = config.getInt(testName.concat(".TestSettings.Time"));
        }
        FILENAME = config.getString(testName.concat(".LogFileName"));
    }
}
