package flow.entity;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
@TableName("module")
public class Module {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("name")
    String name;

}
