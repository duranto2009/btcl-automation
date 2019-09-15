package ip.privateIP;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("ip_private")
public class PrivateIPBlock {
    @PrimaryKey
    @ToString.Exclude
    @ColumnName("id")
    long id;

    @ColumnName("creation_time")@ToString.Exclude
    long creationTime;

    @ColumnName("last_modification_time")@CurrentTime
    @ToString.Exclude
    long lastModificationTime;

    @ColumnName("active_from")@ToString.Exclude
    long activeFrom;

    @ColumnName("active_to")@ToString.Exclude@Builder.Default
    long activeTo = Long.MAX_VALUE;

    @ColumnName("is_deleted")@ToString.Exclude
    boolean isDeleted = false;

    @ColumnName("sub_region_id")
    long subRegionId;

    @ColumnName("from_ip")
    String fromIP;

    @ColumnName("to_ip")
    String toIP;

    @ColumnName("module_id")
    int moduleId;

    @ColumnName("remarks")
    String remarks;
}

