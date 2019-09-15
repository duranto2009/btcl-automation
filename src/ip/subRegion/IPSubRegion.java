package ip.subRegion;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ip_sub_region")
public class IPSubRegion {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("region_id")
    long regionId;

    @ColumnName("sub_region_name")
    String subRegionName;

    @ColumnName("creation_time")
    long creationTime;

    @ColumnName("last_modification_time")
    @CurrentTime
    long lastModificationTime;

    @ColumnName("is_deleted")
    @Builder.Default
    boolean isDeleted = false;
}
