package coLocation.demandNote;

import util.ModifiedSqlGenerator;

import java.util.List;

public class CoLocationDemandNoteAdjustmentDAO {
    Class<CoLocationDemandNoteAdjustment> classObject = CoLocationDemandNoteAdjustment.class;

    void insert(CoLocationDemandNoteAdjustment adjustment) throws Exception {
        ModifiedSqlGenerator.insert(adjustment);
    }

    List<CoLocationDemandNoteAdjustment> getCoLocationAdjustmentByCoLocationHistoryId(long historyId ) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(classObject, new CoLocationDemandNoteAdjustmentConditionBuilder()
                .Where()
                .connection_history_idEquals(historyId)
                .getCondition()

        );
    }

    List<CoLocationDemandNoteAdjustment> getCoLocationAdjustmentByConnectionIds(List<Long> connectionIds ) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(classObject, new CoLocationDemandNoteAdjustmentConditionBuilder()
                .Where()
//                .connection_history_idEquals(historyId)
                .connection_idIn(connectionIds)
                .getCondition()

        );
    }
}
