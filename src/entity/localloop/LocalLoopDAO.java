package entity.localloop;

import util.ModifiedSqlGenerator;

import java.util.List;

public class LocalLoopDAO {

    private Class<LocalLoop> classObject = LocalLoop.class;

    List<LocalLoop> getLocalLoopByOffice(long id) throws Exception{

        return  ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(id)
                        .getCondition());
//        if(localLoops.size()>0){
//            return localLoops;
//        }else {
//            return null;
//        }
    }

    List<LocalLoop> getLocalLoopByOfficeAndPop(long officeID, long popID) throws Exception{
        return
                ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .officeIdEquals(officeID)
                                .popIdEquals(popID)
                                .getCondition()
                );
    }

    void update(LocalLoop localLoop) throws Exception{
        ModifiedSqlGenerator.updateEntity(localLoop);
    }

    void insertApplication(LocalLoop localLoop) throws Exception{
        ModifiedSqlGenerator.insert(localLoop);
    }


    LocalLoop getLocalLoopById(long loopId) throws Exception{

       return ModifiedSqlGenerator.getObjectByID(LocalLoop.class,loopId);

    }

    LocalLoop getLocalLoopByPort(long oldPortId)throws Exception {
        List<LocalLoop>list =  ModifiedSqlGenerator.getAllObjectList(LocalLoop.class,new LocalLoopConditionBuilder()
                .Where()
                .portIdEquals(oldPortId)
                .getCondition()
        );
        if(list!=null && list.size()>0)return list.get(0);
        return null;
    }
}
