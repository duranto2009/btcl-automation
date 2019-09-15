package ip.ipUsage;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import ip.IPConstants;
import lombok.*;

@Data@ToString
@Builder@AllArgsConstructor@NoArgsConstructor
@TableName("ip_usage")
public class IPBlockUsage {

    @PrimaryKey@ToString.Exclude
    @ColumnName("id")
    long id;

    @ColumnName("creation_time")
    @ToString.Exclude
    long creationTime;

    @ColumnName("last_modification_time")@CurrentTime@ToString.Exclude
    long lastModificationTime;

    @ColumnName("active_from")@ToString.Exclude
    long activeFrom;

    @ColumnName("active_to")@ToString.Exclude@Builder.Default
    long activeTo = Long.MAX_VALUE;

    @ColumnName("is_deleted")@ToString.Exclude@Builder.Default
    boolean isDeleted = false;

    @ColumnName("from_ip")
    String fromIP;

    @ColumnName("to_ip")
    String toIP;

    @ColumnName("version")
    IPConstants.Version version;

    @ColumnName("region_id")
    long regionId;

    @ColumnName("sub_region_id")
    long subRegionId;

    @ColumnName("purpose")
    IPConstants.Purpose purpose;

//    @ColumnName("status")
//    IPConstants.Status status;

    @ColumnName("type")
    IPConstants.Type type;

}
