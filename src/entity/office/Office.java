package entity.office;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import entity.efr.EFR;
import entity.localloop.LocalLoop;
import lombok.Data;

import java.util.List;


@Data
@TableName("office")
public class Office {
    @PrimaryKey @ColumnName("id") long id;
    @ColumnName("name") String officeName;
    @ColumnName("address") String officeAddress;
    @ColumnName("created_date") long createdDate = System.currentTimeMillis();
    @ColumnName("client_id") long clientId;
    @ColumnName("module_id") int moduleId;

    List<LocalLoop> localLoops;
    List<EFR> efrs;
//    LocalLoop localLoop;
}
