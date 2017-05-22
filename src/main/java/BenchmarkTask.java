import java.util.concurrent.Callable;

/**
 * Created by sbt-khruzin-mm on 18.05.2017.
 */
public interface BenchmarkTask extends Callable {
    Integer call();
}
