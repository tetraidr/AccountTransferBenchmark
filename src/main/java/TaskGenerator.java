import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public interface TaskGenerator {
    Thread GetTask();
    boolean hasNext();
    void assignStatisticQueue(ConcurrentLinkedQueue<BenchmarkTaskResponse> statisticsQueue);
}
