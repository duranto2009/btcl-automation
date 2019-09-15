package vpn.td;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("vpn_probable_td_client")
public class VPNProbableTDClient {

    @PrimaryKey
    @ColumnName("id")
    long ID;
    @ColumnName("clientID")
    long clientID;
    @ColumnName("tdDate")
    long tdDate;

    public VPNProbableTDClient() {
    }
    public VPNProbableTDClient(long clientID, long tdDate) {
        this.clientID = clientID;
        this.tdDate = tdDate;
    }
}
