package lli.Application.Office;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.Application.LocalLoop.LocalLoop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@TableName("at_lli_office")
@Getter
@Setter
@NoArgsConstructor
public class Office {


    @PrimaryKey
    @ColumnName("historyID")
    long id;
    @ColumnName("ID")
    long historyId;
    @ColumnName("application_id")
    long applicationId;
    @ColumnName("name")
    String officeName;
    @ColumnName("address")
    String officeAddress;

    @ColumnName("connectionHistoryID")
    long connectionID;

    List<LocalLoop> loops;

    public List<LocalLoop> getLoops() {
        return loops;
    }

    public void setLoops(List<LocalLoop> loops) {
        this.loops = loops;
    }
}
