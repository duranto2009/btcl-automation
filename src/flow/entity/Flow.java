package flow.entity;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author maruf
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
@TableName("flow")
public class Flow {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("name")
    String name;
    @ColumnName("component")
    int component;
}
