package lli.longTerm;

import util.ModifiedSqlGenerator;
import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;

public class LLILongTermBenefitDAO {

    Class<LLILongTermBenefit> classObject = LLILongTermBenefit.class;

    public void insertItem(LLILongTermBenefit object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }


    public LLILongTermBenefit getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(LLILongTermBenefit object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }

    public List<LLILongTermBenefit> getActiveListByClientId(long clientId) throws Exception{

        return getAllObjectList(classObject, new LLILongTermBenefitConditionBuilder()
                .Where()
                .clientIdEquals(clientId)
                .isDeleted(false)
                .getCondition());

    }

    public LLILongTermBenefit getItemByContractId(long contractId) throws Exception{
        List<LLILongTermBenefit> list = getAllObjectList(classObject, new LLILongTermBenefitConditionBuilder()
                .Where()
                .contractIdEquals(contractId)
                .getCondition());

        return list.size()>0? list.get(0) : null;
    }
}
