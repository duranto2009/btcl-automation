package entity.office;

import util.ModifiedSqlGenerator;

import java.util.List;

public class OfficeDAO {



    Class<Office> classObject = Office.class;

    public void insertOffice(Office vpnOffice) throws Exception{
        ModifiedSqlGenerator.insert(vpnOffice);
    }


//    List<Office> getOfficesByApplicationId(long vpnApplicationId) throws Exception{
//        return ModifiedSqlGenerator.getAllObjectList(classObject,
//                new OfficeConditionBuilder()
//                        .Where()
//                        .ap(vpnApplicationId)
//                        .getCondition());
//
//    }


    List<Office> getOfficesByClientId(long clientId) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(classObject,
                new OfficeConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .getCondition());

    }


    public Office getOfficeById(long office) throws Exception{
        return  ModifiedSqlGenerator.getObjectByID(Office.class,office);
    }

    public void update(Office vpnOffice)throws Exception {
        ModifiedSqlGenerator.updateEntity(vpnOffice);
    }
}
