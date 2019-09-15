package flow.dao;

import flow.entity.FlowStateTransitionRole;
import flow.entity.FlowStateTransitionRoleConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.List;

/**
 * @author maruf
 */
public class FlowStateTransitionRoleDAO {

    public List<FlowStateTransitionRole> getByRole(int id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransitionRole.class,
                new FlowStateTransitionRoleConditionBuilder().Where().roleEquals((long) id).getCondition());
    }

    public List<FlowStateTransitionRole> getByStateTransition(int id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransitionRole.class, new FlowStateTransitionRoleConditionBuilder()
                .Where()
                .flowStateTransitionEquals(id)
                .getCondition()
        );
    }

    public FlowStateTransitionRole get(long id) throws Exception {
        if (id > 0) {
            return ModifiedSqlGenerator.getObjectByID(FlowStateTransitionRole.class, id);
        }
        return null;
    }

    public List<FlowStateTransitionRole> getAll() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransitionRole.class);
    }
}
