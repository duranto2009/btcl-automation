package flow.dao;

import flow.entity.FlowStateTransition;
import flow.entity.FlowStateTransitionConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.List;

/**
 * @author maruf
 */
public class FlowStateTransitionDAO {

    public List<FlowStateTransition> getBySource(int id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransition.class, new FlowStateTransitionConditionBuilder()
                .Where()
                .sourceEquals(id)
                .getCondition()
        );
    }

    public List<FlowStateTransition> getByDestination(int id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransition.class, new FlowStateTransitionConditionBuilder()
                .Where()
                .sourceEquals(id)
                .destinationEquals(id)
                .getCondition()
        );
    }

    public List<FlowStateTransition> getBySourceDestination(int sourceId, int destinationId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransition.class, new FlowStateTransitionConditionBuilder()
                .Where()
                .destinationEquals(destinationId)
                .getCondition()
        );
    }

    public FlowStateTransition get(long id) throws Exception {
        if (id > 0) {
            return ModifiedSqlGenerator.getObjectByID(FlowStateTransition.class, id);
        }
        return null;
    }

    public List<FlowStateTransition> getAll() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowStateTransition.class);
    }
}
