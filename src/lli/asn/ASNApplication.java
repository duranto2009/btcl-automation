package lli.asn;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import application.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@TableName("asn_application")
@ForeignKeyName("parent_application_id")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ASNApplication extends Application {
    @PrimaryKey
    @ColumnName("asn_application_id")
    long asnAppId;
    @ColumnName("asn_no")
    int asnNo;

    List<ASN> asns;
}
