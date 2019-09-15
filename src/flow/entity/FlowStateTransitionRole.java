package flow.entity;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author maruf
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
@TableName("flow_state_transition_role")
public class FlowStateTransitionRole {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("flow_state_transition")
    int flowStateTransition;
    @ColumnName("role")
    long role;
}
