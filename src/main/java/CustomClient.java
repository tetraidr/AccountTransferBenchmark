/**
 * Created by sbt-khruzin-mm on 17.05.2017.
 */
public interface CustomClient {
    int AcctTransfer(AcctTransfer transfer);
    int AddAccount(String AcctId, Integer sum);
    Long Checksum();
    void drop();
}
