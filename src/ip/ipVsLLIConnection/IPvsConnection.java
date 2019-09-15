package ip.ipVsLLIConnection;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.connection.LLIConnectionConstants;
import lombok.*;

@Data@ToString@AllArgsConstructor@Builder@NoArgsConstructor
@TableName("ip_vs_connection")
public class IPvsConnection {
    @PrimaryKey@ColumnName("id")
    long id;

    @ColumnName("creation_time")
    long creationTime;

    @ColumnName("last_modification_time")@CurrentTime
    long lastModificationTime;

    @ColumnName("active_from")
    long activeFrom;

    @ColumnName("active_to")@Builder.Default
    long activeTo = Long.MAX_VALUE;

    @ColumnName("is_deleted")@Builder.Default
    boolean isDeleted = false;

    @ColumnName("connection_id")
    long connectionId;

    @ColumnName("ip_usage_id")
    long ipUsageId;

    @ColumnName("routing_info_id")
    long routingInfoId;

    @ColumnName("ip_usage_type")
    LLIConnectionConstants.IPUsageType usageType;


}
