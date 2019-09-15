package flow.entity;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

/**
 * @author maruf
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@TableName("adrole")
public class Role {

    @PrimaryKey
    @ColumnName("rlRoleID")
    long id;
    @ColumnName("rlLevel")
    int level;
    @ColumnName("rlRoleName")
    String name;
    @ColumnName("rlRoleDesc")
    String description;
    @ColumnName("rlMaxClientPerDay")
    long maxClientPerDay;
    @ColumnName("rlRestrictedToOwn")
    boolean restrictedToOwn;
    @ColumnName("rlLastModificationTime")
    long lastModificationTime;
    @ColumnName("rlIsDeleted")
    boolean deleted;
    @ColumnName("rlParentRoleID")
    long parentRoleId;
}
