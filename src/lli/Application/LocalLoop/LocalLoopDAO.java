package lli.Application.LocalLoop;

import inventory.InventoryItem;
import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class LocalLoopDAO {

    Class<LocalLoop> classObject = LocalLoop.class;

    public void insertlocalLoop(LocalLoop localLoop) throws Exception{
        insert(localLoop);
    }

    public void update(LocalLoop localLoop) throws Exception{
        ModifiedSqlGenerator.updateEntity(localLoop);
    }


    public List<LocalLoop> getLocalLoop(long applicationID) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .applicationIDEquals(applicationID)
                                .getCondition()
                );
    }



    public List<LocalLoop> getLocalLoopByOffice(long officeID) throws Exception {
        List<LocalLoop> localLoopList=
                ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(officeID)
                                .isDeleted(false)
                                .getCondition()
                );
        return localLoopList;
    }

    public List<LocalLoop> getLocalLoopByOfficeAndPop(long officeID,long popId) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(officeID)
                                .popIDEquals(popId)
                                .getCondition()
                );
    }


    public List<LocalLoop> getLocalLoopById(long localLoopId) throws Exception{
        return
                ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .idEquals(localLoopId)
                                .orderByhistoryIDDesc()
                                .getCondition()
                );
    }
}
