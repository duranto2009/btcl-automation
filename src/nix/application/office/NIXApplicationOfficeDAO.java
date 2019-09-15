package nix.application.office;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NIXApplicationOfficeDAO {



    Class<NIXApplicationOffice> classObject = NIXApplicationOffice.class;

    public void insertOffice(NIXApplicationOffice nixApplicationOffice) throws Exception{
        ModifiedSqlGenerator.insert(nixApplicationOffice);
    }


    List<NIXApplicationOffice> getOfficesByApplicationId(long id) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(classObject,
                new NIXApplicationOfficeConditionBuilder()
                        .Where()
                        .applicationEquals(id)
                        .getCondition());

    }

    public NIXApplicationOffice getOfficeById(long office) throws Exception{
        return  ModifiedSqlGenerator.getObjectByID(NIXApplicationOffice.class,office);
    }

    public void update(NIXApplicationOffice nixApplicationOffice)throws Exception {
        ModifiedSqlGenerator.updateEntity(nixApplicationOffice);
    }
}
