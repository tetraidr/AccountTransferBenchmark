/**
 * Created by SBT-Hruzin-MM on 09.03.2017.
 */
import java.lang.String;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TarantoolBenchmark {
    static ConcurrentHashMap<Integer,AcctTransfer> Transfers = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String,Integer> Accounts = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer,FutureTask> Threads = new ConcurrentHashMap<>();
    static CustomTarantoolClient client;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Tarantool Test:");
        System.out.println("Connecting");
        client = new CustomTarantoolClient("10.36.8.68", 3301, "tester", "tester");
        int n = 5000;
        int m = 10000;
        int t = 10;
        int mt = 150;
        long checksum1 = 0;
        long checksum2 = 0;
        long checksum3 = 0;
        System.out.format("Prepare data for loading to Tarantool (%d records)%n",n);
        String AcctId = new String("");
        Integer sum;
        Random rnd = new Random(100);
        String characters = new String("qwertyuiopasdfghjklzxcvbnm1234567890");
        int num = 0;
        for (int i = 0;i<n;i++) {
            AcctId = "";
            for (int j = 0; j<8; j++) {
                num = rnd.nextInt(36);
                AcctId = AcctId.concat(characters.substring(num, num + 1));
            }
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
        /*AcctTransfer transfer;
        String rndKey1,rndKey2;
        List<String> keys = new ArrayList<>(Accounts.keySet());
        for (int i = 0;i<m;i++) {
            rndKey1 = keys.get(rnd.nextInt(keys.size()));
            rndKey2 = keys.get(rnd.nextInt(keys.size()));
            transfer = new AcctTransfer(rndKey1,rndKey2,rnd.nextInt(500));
            Transfers.put(i,transfer);
            System.out.format("Client1 name (from): %s, client2 name (to): %s sum: %d %n", transfer.AccountFrom, transfer.AccountTo, transfer.TransferAmount);
        }*/
        for (int i=0;i<t;i++){
            FutureTask task = new FutureTask(new BenchmarkThread(Accounts,m,mt,new Random(i),client));
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
        /*System.out.println("Start loading data to Tarantool shard");
        Integer result;

        for (int i = 0;i<m;i++) {
            result = client.AcctTransfer(Transfers.get(i));
            if (result == 1){
                System.out.format("Transfer from %s to %s complete (%d) %n", Transfers.get(i).AccountFrom, Transfers.get(i).AccountTo, Transfers.get(i).TransferAmount);
            }
            else {
                System.out.format("Transfer from %s to %s denied (%d) %n", Transfers.get(i).AccountFrom, Transfers.get(i).AccountTo,Transfers.get(i).TransferAmount);
            }
        }*/
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
    }
}