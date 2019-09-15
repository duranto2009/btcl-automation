package ip.IPInventory;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import ip.IPConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Raihan on 10/14/2018.
 */

@TableName("ip_inventory")
@Data@Builder@NoArgsConstructor@AllArgsConstructor
public class IPBlockInventory {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("creation_time")
    long creationTime;


    @ColumnName("last_modification_time")@CurrentTime
    long lastModificationTime;

    @ColumnName("active_from")
    long activeFrom;

    @ColumnName("active_to")@Builder.Default
    long activeTo = Long.MAX_VALUE;

    @ColumnName("is_deleted")
    boolean isDeleted = false;

    @ColumnName("region_id")
    long regionId;

    @ColumnName("from_ip")
    String fromIP;

    @ColumnName("to_ip")
    String toIP;

    @ColumnName("version")
    IPConstants.Version version;

    @ColumnName("type")
    IPConstants.Type type;

    @ColumnName("module_id")
    int moduleId;

    @ColumnName("remarks")
    String remarks;

}
