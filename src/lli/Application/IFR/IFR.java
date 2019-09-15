package lli.Application.IFR;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@TableName("at_lli_application_ifr")
@Getter
@Setter
@NoArgsConstructor
public class IFR {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("parentIFRID")
    long parentIFRID;
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("officeID")
    long officeID;
    @ColumnName("popID")
    long popID;
    @ColumnName("requestedBW")
    long requestedBW;
    @ColumnName("availableBW")
    long availableBW;
    @ColumnName("selectedBW")
    long selectedBW;
    @ColumnName("priority")
    long priority;
    @ColumnName("isReplied")
    long isReplied;
    @ColumnName("serverRoomLocationID")
    long serverRoomLocationID;
    @ColumnName("submissionDate")
    long submissionDate;
    @ColumnName("isSelected")
    int isSelected;
    @ColumnName("state")
    long state;
    @ColumnName("isForwarded")
    int isForwarded;
    @ColumnName("isIgnored")
    int isIgnored;

    String officeAddress;
}

