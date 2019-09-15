package lli.asn;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("asn")
public class ASN {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("asn_no")
    int asnNo;
    @ColumnName("application_id")
    long applicationId;
    @ColumnName("status")
    int status;

    @ColumnName("client")
    long client;

    @ColumnName("state")
    int state;

    @ColumnName("created_date")
    long createdDate;

    @ColumnName("created_by")
    long created_by;

    @ColumnName("last_modification_time")
    long lastModifyTime;

    List<ASNmapToIP>asNmapToIPS;
}
