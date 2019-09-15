package lli.Application.EFR;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@TableName("at_lli_application_efr")
@Getter
@Setter
@NoArgsConstructor
public class EFR {


    public static final String POP_TERMINAL = "POP";
    public static final String LDP_TERMINAL = "LDP";
    public static final String CUSTOMER_TERMINAL = "Customer Address";
    public static final String MUX_TERMINAL = "MUX";

    public static final long TERMINAL_TYPE_POP = 1;
    public static final long TERMINAL_TYPE_LDP = 2;
    public static final long TERMINAL_TYPE_CUSTOMER = 3;
    public static final long TERMINAL_TYPE_MUX = 4;

    public static final Map<Long, String> TERMINAL = new HashMap<Long, String>(){{
        put(TERMINAL_TYPE_POP, POP_TERMINAL);
        put(TERMINAL_TYPE_LDP, LDP_TERMINAL);
        put(TERMINAL_TYPE_CUSTOMER, CUSTOMER_TERMINAL);
        put(TERMINAL_TYPE_MUX, MUX_TERMINAL);
    }};

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("officeID")
    long officeID;
    @ColumnName("parentEFRID")
    long parentEFRID;
    @ColumnName("popID")
    long popID;
    @ColumnName("bandwidth")
    long bandwidth;
    @ColumnName("sourceType")
    long sourceType;
    @ColumnName("source")
    String source;
    @ColumnName("destinationType")
    long destinationType;
    @ColumnName("destination")
    String destination;
    @ColumnName("quotationStatus")
    long quotationStatus;
    @ColumnName("quotationDeadline")
    long quotationDeadline;
    @ColumnName("proposedLoopDistance")
    long proposedLoopDistance;

    @ColumnName("actualLoopDistance")
    long actualLoopDistance;

    @ColumnName("loopDistanceIsApproved")
    long loopDistanceIsApproved;

    @ColumnName("vendorID")
    long vendorID;
    @ColumnName("vendorType")
    long vendorType;
    @ColumnName("workGiven")
    long workGiven;
    @ColumnName("workCompleted")
    long workCompleted;
    @ColumnName("workDeadline")
    long workDeadline;
    @ColumnName("ofcType")
    long ofcType;
    @ColumnName("state")
    long state;
    @ColumnName("isForwarded")
    int isForwarded;
    @ColumnName("woNumber")
    Long workOrderNumber;

}
