package lli.Application.NewLocalLoop;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("at_lli_application_localloop")
@Getter
@Setter
@NoArgsConstructor
public class NewLocalLoop {

    @PrimaryKey
    @ColumnName("id")
    long id;
    /*@ColumnName("ID")
    long ID;*/
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("officeID")
    long officeID;
    @ColumnName("popID")
    long popID;
    @ColumnName("bandwidth")
    long bandwidth;
    @ColumnName("OFCType")
    long ofcType;
    @ColumnName("router_switchID")
    long router_switchID=0;
    @ColumnName("clientDistances")
    long clientDistances=0;
    @ColumnName("BtclDistances")
    long BTCLDistances;
    @ColumnName("OCDistances")
    long OCDistances;
    @ColumnName("OCID")
    Long OCID=0L;
    @ColumnName("portID")
    long portID=0;
    @ColumnName("VLANID")
    long VLANID=0;
    @ColumnName("is_removable")
    int isRemovable;

    @ColumnName("old_loop_id")
    long oldLoopId;

    @ColumnName("adjustedBTClDistance")
    long adjustedBTClDistance;

    @ColumnName("adjustedOCDistance")
    long adjustedOCDistance;

    String popName;


}

