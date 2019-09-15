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
@TableName("flow_state_transition")
public class FlowStateTransition {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("source")
    int source;
    @ColumnName("destination")
    int destination;
    @ColumnName("comment")
    String comment;
}
