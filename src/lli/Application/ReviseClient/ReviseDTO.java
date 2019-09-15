package lli.Application.ReviseClient;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@TableName("at_lli_connection_revise_client")

public class ReviseDTO {
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

    @ColumnName("advice_note_id")
    long adviceNoteId=0;
}
