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
@TableName("component")
public class Component {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("name")
    String name;
    @ColumnName("module")
    int module;
}
