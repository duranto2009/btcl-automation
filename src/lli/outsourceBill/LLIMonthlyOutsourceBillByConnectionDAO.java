package lli.outsourceBill;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class LLIMonthlyOutsourceBillByConnectionDAO {

    Class<LLIMonthlyOutsourceBillByConnection> classObject = LLIMonthlyOutsourceBillByConnection.class;

    public void insertItem(LLIMonthlyOutsourceBillByConnection object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public LLIMonthlyOutsourceBillByConnection getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(LLIMonthlyOutsourceBillByConnection object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public List<LLIMonthlyOutsourceBillByConnection> getByOutsourceBillId(long outsourceBillId) throws Exception {
        return getAllObjectList(classObject, new LLIMonthlyOutsourceBillByConnectionConditionBuilder()
                .Where()
                .outsourceBillIdEquals(outsourceBillId)
                .getCondition());
    }


}
