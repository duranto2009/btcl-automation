package upstream.vendor;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("upstream_vendor")
public class UpstreamVendor {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("vendor_name")
    String vendorName;

    @ColumnName("vendor_location_id")
    long vendorLocation;
}
