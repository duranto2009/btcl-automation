package vpn.network;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("vpn_network")

public class VPNNetwork {
    @PrimaryKey @ColumnName("id") long id;
    @ColumnName("name") String name;
    @ColumnName("client_id") long clientId;
    @ColumnName("network_type") String networkType; // TODO change to enum
    @ColumnName("status") String status; // TODO change to enum
    @ColumnName("active_from") long activeFrom; // TODO Change to enum
    @ColumnName("active_to") long activeTo;
    @ColumnName("valid_from") long validFrom;
    @ColumnName("valid_to") long validTo;
    @ColumnName("discount_rate") double discountRate;
}
