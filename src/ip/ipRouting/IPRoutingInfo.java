package ip.ipRouting;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
@TableName("ip_routing")
@ToString
public class IPRoutingInfo {
    @PrimaryKey
    @ColumnName("id")@ToString.Exclude
    long id;

    @ColumnName("creation_time")@ToString.Exclude
    long creationTime;

    @ColumnName("last_modification_time")@CurrentTime@ToString.Exclude
    long lastModificationTime;

    @ColumnName("active_from")@ToString.Exclude
    long activeFrom;

    @ColumnName("active_to")@Builder.Default@ToString.Exclude
    long activeTo = Long.MAX_VALUE;

    @ColumnName("is_deleted")@ToString.Exclude
    boolean isDeleted = false;

    @ColumnName("from_ip")
    String fromIP;

    @ColumnName("to_ip")
    String toIP;

    @ColumnName("network_ip")
    String networkIP;

    @ColumnName("broadcast_ip")
    String broadcastIP;

    @ColumnName("gateway_ip")
    String gatewayIP;

}
