package nix.revise;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("nix_probable_td_client")
public class NIXProbableTDClient {

    @PrimaryKey
    @ColumnName("id")
    long ID;
    @ColumnName("clientID")
    long clientID;
    @ColumnName("tdDate")
    long tdDate;

    public NIXProbableTDClient() {
    }
    public NIXProbableTDClient(long clientID, long tdDate) {
        this.clientID = clientID;
        this.tdDate = tdDate;
    }
}
