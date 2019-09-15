package nix.revise;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;


@Data
@TableName("nix_connection_revise_client")
public class NIXReviseDTO {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("clientID")
    long clientID;

    @ColumnName("applicationType")
    int applicationType;

    @ColumnName("isDemandNoteNeeded")
    int isDemandNoteNeeded=1;

    @ColumnName("demandNoteID")
    long demandNoteID=0;

    @ColumnName("suggestedDate")
    long suggestedDate;
    @ColumnName("appliedDate")
    long appliedDate;
    @ColumnName("state")
    long state;
    @ColumnName("description")
    String description;

    String stateDescription;

    String color;

    @ColumnName("bandwidth")
    double bandwidth;

    @ColumnName("referenceContract")
    long referenceContract;

    boolean hasPermission = false;

    @ColumnName("isCompleted")
    boolean isCompleted=false;
}
