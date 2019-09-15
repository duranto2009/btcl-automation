package lli.Application.LocalLoop;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("at_lli_local_loop")
@Data
@NoArgsConstructor
public class LocalLoop {

    @PrimaryKey
    @ColumnName("historyID")
    long historyID;
    @ColumnName("ID")
    long id;
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("lliOfficeHistoryID")
    long officeID;
    @ColumnName("popID")
    long popID;
    @ColumnName("bandwidth")
    long bandwidth;
    @ColumnName("numOfCore")
    long ofcType;
    @ColumnName("router_switchID")
    long router_switchID=0;
    @ColumnName("clientDistance")
    long clientDistances=0;
    @ColumnName("btclDistance")
    long BTCLDistances;
    @ColumnName("OCDistance")
    long OCDistances;

    @ColumnName("adjustedBTClDistance")
    long adjustedBTClDistance;

    @ColumnName("adjustedOCDistance")
    long adjustedOCDistance;

    @ColumnName("OCID")
    long OCID;
    @ColumnName("portID")
    long portID=0;
    @ColumnName("vlanID")
    long VLANID=0;

    @ColumnName("is_deleted")
    boolean isDeleted;

    String popName;

    String portName;

    String switchName;

    String vlanName;
    String loopName;


}

