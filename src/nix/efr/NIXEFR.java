package nix.efr;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@TableName("nix_efr")
public class NIXEFR {
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
    @ColumnName("application")
    long application;
    @ColumnName("source")
    String source;
    @ColumnName("source_type")
    int sourceType;
    @ColumnName("destination")
    String destination;
    @ColumnName("destination_type")
    int destinationType;
    @ColumnName("vendor")
    long vendor;
    @ColumnName("vendor_type")
    int vendorType;
    @ColumnName("work_given")
    int workGiven;
    @ColumnName("work_completed")
    long workCompleted;
    @ColumnName("deadline")
    long deadline;
    @ColumnName("proposed_loop_distance")
    long proposedDistance;
    @ColumnName("actual_loop_distance")
    long actualDistance;
    @ColumnName("loop_distance_approved")
    long approvedDistance;
    @ColumnName("pop")
    long pop;
    @ColumnName("ofc_type")
    int ofcType;
    @ColumnName("created")
    long created;
    @ColumnName("modified")
    long lastModificationTime;
    @ColumnName("office")
    long office;
    @ColumnName("is_forwarded")
    int isForwarded;
    @ColumnName("is_Selected")
    int isSelected;
    @ColumnName("parent_efr")
    long parentEFR;

    @ColumnName("quotation_status")
    int quotationStatus;

    @ColumnName("loop_id")
    long loopId;
}
