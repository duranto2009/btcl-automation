package flow.dao;

import flow.entity.Flow;
import flow.entity.FlowConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.List;

/**
 * @author maruf
 */
public class FlowDAO {

//    public List<Flow> getByModule(int id) throws Exception {
//        return ModifiedSqlGenerator.getAllObjectList(Flow.class, new FlowConditionBuilder()
//                .Where()
//                .moduleEquals(id)
//                .getCondition()
//        );
//    }

    public Flow get(long id) throws Exception {
        if (id > 0) {
            return ModifiedSqlGenerator.getObjectByID(Flow.class, id);
        }
        return null;
    }
}
