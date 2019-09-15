package lli.asn;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("asn_to_ip")
public class ASNmapToIP {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("asn_id")
    long asnId;
    @ColumnName("ip")
    String ip;
    @ColumnName("ip_version")
    int ipVersion;

    @ColumnName("last_modification_time")
    long lastModifyTime;

    @ColumnName("application_id")
    long applicationId;

    @ColumnName("is_deleted")
    int isDeleted;
}
