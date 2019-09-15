package coLocation.application;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@TableName("colocation_application")
public class CoLocationApplicationDTO {

    @PrimaryKey
    @ColumnName("application_id")
    long applicationID;
    @ColumnName("client_id")
    long clientID;
    @ColumnName("user_id")
    Long userID;
    @ColumnName("submission_date")
    long submissionDate;
    @ColumnName("application_type")
    int applicationType;
    @ColumnName("connection_id")
    long connectionId;
    @ColumnName("connection_type")
    int connectionType;
    @ColumnName("state")
    int state;
    @ColumnName("demand_note_id")
    Long demandNoteID;
    @ColumnName("is_demand_note_needed")@Builder.Default
    boolean isDemandNoteNeeded=true;
    @ColumnName("suggested_date")
    long suggestedDate;
    @ColumnName("description")
    String description;
    @ColumnName("is_service_started")
    boolean isServiceStarted;
    @ColumnName("comment")
    String comment;
    @ColumnName("skip_payment")
    int skipPayment;
    @ColumnName("rack_needed")
    boolean rackNeeded;
    @ColumnName("rack_type_id")
    int rackTypeID;
    @ColumnName("rack_space")
    int rackSpace;
    @ColumnName("power_needed")
    boolean powerNeeded;
    @ColumnName("power_amount")
    double powerAmount;
    @ColumnName("power_type")
    int powerType;
    @ColumnName("fiber_needed")
    boolean fiberNeeded;
    @ColumnName("fiber_core")
    Integer fiberCore;
    @ColumnName("fiber_type")
    Integer fiberType;

    @ColumnName("floor_space_needed")
    boolean floorSpaceNeeded;
    @ColumnName("floor_space_amount")
    double floorSpaceAmount;
    @ColumnName("floor_space_type")
    int floorSpaceType;

    String stateDescription;
    String color;
    boolean hasPermission;
}
