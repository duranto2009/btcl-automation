package vpn.ownerchange;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("vpn_on_process_link")
public class VPNOnProcessLink {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("application")
    long application;

    @ColumnName("link")
    long link;
}
