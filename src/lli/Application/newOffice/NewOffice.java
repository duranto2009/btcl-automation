package lli.Application.newOffice;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

import lli.Application.NewLocalLoop.NewLocalLoop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@TableName("at_lli_application_office")
@Getter
@Setter
@NoArgsConstructor
public class NewOffice {


    @PrimaryKey
    @ColumnName("history_id")
    long id;
    @ColumnName("id")
    long historyId;
    @ColumnName("application_id")
    long applicationId;
    @ColumnName("office_name")
    String officeName;
    @ColumnName("office_address")
    String officeAddress;
    @ColumnName("connectionID")
    long connectionID;

    @ColumnName("oldOfficeId")
    long old_office_id;

    List<NewLocalLoop> loops;

    public List<NewLocalLoop> getLoops() {
        return loops;
    }

    public void setLoops(List<NewLocalLoop> loops) {
        this.loops = loops;
    }
}
