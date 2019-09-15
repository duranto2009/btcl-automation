package coLocation.accounts.VariableCost;

import common.RequestFailureException;
import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

public class VariableCostDAO {

    public List<VariableCostDTO> getCostByType(int type) throws Exception {

        return  ModifiedSqlGenerator.getAllObjectList(VariableCostDTO.class,
            new VariableCostDTOConditionBuilder()
                    .Where()
                    .typeIDEquals(type)
                    .getCondition()
        );
    }

    public List<VariableCostDTO> getCostByTypeAndAmount(int type,int quantity) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(VariableCostDTO.class,
            new VariableCostDTOConditionBuilder()
                    .Where()
                    .typeIDEquals(type)
                    .quantityIDEquals(quantity)
                    .getCondition()
        );
    }

    public void insertCostConfig(VariableCostDTO cost) throws Exception {
        insert(cost);

    }

    public void updateCostConfig(VariableCostDTO cost) throws Exception {
        updateEntity(cost);

    }
}
