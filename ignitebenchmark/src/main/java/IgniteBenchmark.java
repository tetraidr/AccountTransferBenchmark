/**
 * Created by SBT-Hruzin-MM on 09.03.2017.
 */
import org.apache.ignite.IgniteCache;

import java.lang.String;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class IgniteBenchmark {
    static ConcurrentHashMap<Integer,AcctTransfer> Transfers = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String,Integer> Accounts = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer,FutureTask> Threads = new ConcurrentHashMap<>();
    static IgniteClient client;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Ignite Test:");
        System.out.println("Connecting");
        client = new IgniteClient();
        int n = 5000;
        int m = 10000;
        int t = 10;
        int mt = 150;
        long checksum1 = 0;
        long checksum2;
        long checksum3;
        System.out.format("Prepare data for loading to Tarantool (%d records)%n",n);
        String AcctId = new String("");
        Integer sum;
        Random rnd = new Random(100);
        String characters = new String("qwertyuiopasdfghjklzxcvbnm1234567890");
        int num ;
        for (int i = 0;i<n;i++) {
            do{
                AcctId = "";
                for (int j = 0; j<8; j++) {
                    num = rnd.nextInt(36);
                    AcctId = AcctId.concat(characters.substring(num, num + 1));
                }
            } while (Accounts.get(AcctId) != null);
            sum = rnd.nextInt(1000);
            checksum1 += sum;
            Accounts.put(AcctId,sum);
            //System.out.format("Client name: %s, account: %d %n", AcctId, sum);
        }
        System.out.format("Complete! Sum of accounts: %d%n",checksum1);
        System.out.println("Start loading data to Tarantool shard");
        long LoadStartTime = System.nanoTime();
        for (Map.Entry<String,Integer> entry: Accounts.entrySet())
            client.AddAccount(entry.getKey(),entry.getValue());
        long LoadEndTime = System.nanoTime();
        System.out.format("Loading to Tarantool shard complete in %f ms. Speed: %f records/s %n", (LoadEndTime - LoadStartTime)/1e6,1.0*n/(LoadEndTime - LoadStartTime)*1e9);
        checksum2 = client.Checksum();
        if (checksum1 == checksum2){
            System.out.format("Verifying checksums: Tarantool return %d, correct!%n",checksum2);
        }
        else
        {
            System.out.format("Verifying checksums: Tarantool return %d, wrong!%n",checksum2);
        }
        System.out.format("Prepare data for account transfer test (%d transfers)%n",m*t);
        for (int i=0;i<t;i++){
            FutureTask task = new FutureTask(new BenchmarkThread(Accounts,m,mt,rnd,client));
            Threads.put(i,task);
        }
        System.out.format("Complete!");
        System.out.println("Start loading data to Tarantool shard");
        long TransferStartTime = System.nanoTime();
        for (int i=0;i<t;i++){
            Thread asd = new Thread(Threads.get(i));
            asd.start();
        }
        for (int i=0;i<t;i++){
            if (!Threads.get(i).isDone()){
                try{
                    Threads.get(i).get();
                }
                catch (ExecutionException e){
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        long TransferEndTime = System.nanoTime();
        System.out.format("Loading to Tarantool shard complete in %f ms. Speed: %f records/s %n", (TransferEndTime - TransferStartTime)/1e6, 1.0*m*t/(TransferEndTime - TransferStartTime)*1e9);
        checksum3 = client.Checksum();
        if ((checksum2 == checksum3)&(checksum2 == checksum1)){
            System.out.format("Verifying checksums: Tarantool return %d, correct!%n",checksum3);
        }
        else
        {
            System.out.format("Verifying checksums: Tarantool return %d, wrong!%n",checksum3);
        }
        client.drop();
    }
}
