package ip.ipRegion;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("ip_region_vs_district")
public class IPRegionToDistrict {

    @PrimaryKey
    @ColumnName("id")
    Long id;
    @ColumnName("region_id")
    Long region_id;
    @ColumnName(("district_id"))
    Long district_id;
    @ColumnName("isDeleted")
    int isDeleted;

}
