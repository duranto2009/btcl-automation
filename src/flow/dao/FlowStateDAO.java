package flow.dao;

import flow.entity.FlowState;
import flow.entity.FlowStateConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.List;

/**
 * @author maruf
 */
public class FlowStateDAO {

    public List<FlowState> getByFlow(int id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowState.class, new FlowStateConditionBuilder()
                .Where()
                .flowEquals(id)
                .getCondition()
        );
    }

    public FlowState get(long id) throws Exception {
        if (id > 0) {
            return ModifiedSqlGenerator.getObjectByID(FlowState.class, id);
        }
        return null;
    }

    public List<FlowState> getAll() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowState.class);
    }

    public List<FlowState> getStatesByIdList(List<Integer> stateIds) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FlowState.class,
                new FlowStateConditionBuilder()
                .Where()
                .idIn(stateIds)
                .getCondition()
        );
    }
}
