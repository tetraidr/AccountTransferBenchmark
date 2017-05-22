import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sbt-hruzin-mm on 10.04.2017.
 */
public class CustomTarantoolClient implements CustomClient{
    private static volatile TarantoolClient client = null;
    public CustomTarantoolClient(final String hostname, final Integer port, String username, String password) {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = username;
        config.password = password;
        SocketChannelProvider socketChannelProvider = new SocketChannelProvider() {
            @Override
            public SocketChannel get(int retryNumber, Throwable lastError) {
                if (lastError != null) {
                    lastError.printStackTrace(System.out);
                }
                try {
                    return SocketChannel.open(new InetSocketAddress(hostname, port));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
        client = new TarantoolClientImpl(socketChannelProvider, config);
    };
    public int AcctTransfer(AcctTransfer transfer){
        List Result = client.syncOps().call("Benchmark.AcctTransfer", Arrays.asList(transfer.AccountFrom,transfer.AccountTo,transfer.TransferAmount));
        return (Integer)((List)Result.get(0)).get(0);
    }
    public int AddAccount(String AcctId, Integer sum){
        client.syncOps().call("Benchmark.AddAccount", Arrays.asList(AcctId, sum));
        return 1;
    }
    public Long Checksum(){
        List Result = client.syncOps().call("Benchmark.checksum");
        return new Long(((List)Result.get(0)).get(0).toString());
    }
}
