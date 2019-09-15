package entity.efr;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@TableName("efr")
@Data
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
    @ColumnName("application_id")
    long applicationId;
    @ColumnName("module_id")
    int moduleId;
    @ColumnName("office_id")
    long officeId;
    @ColumnName("parent_efr_id")
    long parentEFRId;
    @ColumnName("pop_id")
    long popId;
    @ColumnName("bandwidth")
    long bandwidth;
    @ColumnName("source_type")
    long sourceType;
    @ColumnName("source")
    String source;
    @ColumnName("destination_type")
    long destinationType;
    @ColumnName("destination")
    String destination;
    @ColumnName("is_replied")
    boolean isReplied;

    @ColumnName("is_ignored")
    boolean isIgnored;
    @ColumnName("quotation_deadline")
    long quotationDeadline;
    @ColumnName("proposed_loop_distance")
    long proposedLoopDistance;

    @ColumnName("actual_loop_distance")
    long actualLoopDistance;

    @ColumnName("loop_distance_is_approved")
    boolean loopDistanceIsApproved;

    @ColumnName("vendor_id")
    long vendorID;
    @ColumnName("vendor_type")
    long vendorType;
    @ColumnName("contact")
    String contact;
    @ColumnName("is_selected")
    boolean isSelected;
    @ColumnName("is_completed")
    boolean isCompleted;
    @ColumnName("work_deadline")
    long workDeadline;
    @ColumnName("ofc_type")
    long ofcType;
    @ColumnName("is_forwarded")
    boolean isForwarded;
    @ColumnName("is_work_ordered")
    boolean isWorkOrdered;
    @ColumnName("wo_number")
    Long workOrderNumber;

    String vendorName;


    @ColumnName("is_collaborated_server_room")
    boolean isCollaborated;

    @ColumnName("is_collaboration_approved_server_room")
    boolean isCollaborationApproved;

}
