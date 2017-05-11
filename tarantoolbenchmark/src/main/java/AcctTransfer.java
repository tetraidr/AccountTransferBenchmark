/**
 * Created by sbt-hruzin-mm on 06.04.2017.
 */
import java.lang.String;

public class AcctTransfer {

    public String AccountFrom = "";
    public String AccountTo = "";
    public int TransferAmount = 0;
    public int Result = 0;

    public AcctTransfer(String Acct1, String Acct2, int Amount) {
        this.AccountFrom = Acct1;
        this.AccountTo = Acct2;
        this.TransferAmount = Amount;
    }
}
