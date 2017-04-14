/**
 * Created by sbt-khruzin-mm on 13.04.2017.
 */

import java.util.concurrent.ConcurrentHashMap;
public class BenchmarkThread implements Runnable{
    ConcurrentHashMap<Integer,AcctTransfer> Transfers;
    BenchmarkThread(ConcurrentHashMap<Integer,AcctTransfer> transfers){
        this.Transfers = transfers;
    }
    @Override
    public void run() {
        SynchronizedMap
    }
}
