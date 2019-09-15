package vpn.td;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("vpn_client_td")
public class VPNClientTD {
    public VPNClientTD() {
    }
    public VPNClientTD(long clientID, long tdFrom, long tdTo) {
        this.clientID = clientID;
        this.tdFrom = tdFrom;
        this.tdTo = tdTo;
    }

    @PrimaryKey
    @ColumnName("id")
    long ID;
    @ColumnName("clientID")
    long clientID;
    @ColumnName("tdFrom")
    long tdFrom;
    @ColumnName("tdTo")
    long tdTo;

    @ColumnName("advicedDate")
    long advicedDate;
    @ColumnName("appliedDate")
    long appliedDate;
    @ColumnName("state")
    long state;
}
