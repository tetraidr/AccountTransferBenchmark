/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public interface TaskGenerator {
    Thread GetTask();
    boolean hasNext();
    StatisticsThread getStatistics();
}
