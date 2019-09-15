package lli.changeIPPort;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("at_ip_port_activity_log")
public class IPPortChangeLog {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("updatedBy")
    long updatedBy;
    @ColumnName("connectionId")
    long connectionId;
    @ColumnName("clientId")
    long clientId;
    @ColumnName("previousInfo")
    String previousInfo;
    @ColumnName("newInfo")
    String newInfo;
    @ColumnName("lastModificationTime")
    long lastModificationTime;
}
