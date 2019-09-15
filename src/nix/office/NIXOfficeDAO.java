package nix.office;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NIXOfficeDAO {
    public void insertOffice(NIXOffice nixOffice)throws Exception {
        ModifiedSqlGenerator.insert(nixOffice);
    }

    public List<NIXOffice> getOfficesByConnectionID(long connectionId) throws Exception{
        List<NIXOffice> offices =ModifiedSqlGenerator.getAllObjectList(NIXOffice.class,
                new NIXOfficeConditionBuilder()
                    .Where()
                    .connectionEquals(connectionId)
                    .getCondition());
        return offices;
    }

    public NIXOffice getOfficeById(long office) throws Exception{
        NIXOffice nixOffice = ModifiedSqlGenerator.getObjectByID(NIXOffice.class,office);
        return nixOffice;
    }

    public List<NIXOffice> getOfficesByConnectionHistoryId(long historyId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXOffice.class
        , new NIXOfficeConditionBuilder()
                        .Where()
                        .idEquals(historyId)
                        .getCondition());
    }
}
