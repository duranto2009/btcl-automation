package nix.outsourcebill;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyOutsourceBillByConnectionDAO {

    Class<NIXMonthlyOutsourceBillByConnection> classObject = NIXMonthlyOutsourceBillByConnection.class;

    public void insertItem(NIXMonthlyOutsourceBillByConnection object) throws Exception{
        ModifiedSqlGenerator.insert(object);
    }

    public NIXMonthlyOutsourceBillByConnection getItem(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    public void updateItem(NIXMonthlyOutsourceBillByConnection object) throws Exception{
        ModifiedSqlGenerator.updateEntity(object);
    }


    public List<NIXMonthlyOutsourceBillByConnection> getByOutsourceBillId(long outsourceBillId) throws Exception {
        return getAllObjectList(classObject, new NIXMonthlyOutsourceBillByConnectionConditionBuilder()
                .Where()
                .outsourceBillIdEquals(outsourceBillId)
                .getCondition());
    }


}
