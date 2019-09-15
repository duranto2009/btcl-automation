package nix.application.localloop;


import inventory.InventoryService;
import nix.application.office.NIXApplicationOfficeConditionBuilder;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.util.List;

public class NIXApplicationLocalLoopDAO {
    Class<NIXApplicationLocalLoop> classObject = NIXApplicationLocalLoop.class;


    public List<NIXApplicationLocalLoop> getLocalLoopByOffice(long id) throws Exception{

        List<NIXApplicationLocalLoop> list = ModifiedSqlGenerator.getAllObjectList(classObject,new NIXApplicationLocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(id)
                        .getCondition());

        for(NIXApplicationLocalLoop nixApplicationLocalLoop:list){
            if(nixApplicationLocalLoop.getPortId()>0)nixApplicationLocalLoop.setPortName(
                    ServiceDAOFactory.getService(InventoryService.class).
                            getInventoryItemByItemID(nixApplicationLocalLoop.getPortId()).getName()
            );
            if(nixApplicationLocalLoop.getRouterSwitchId()>0)nixApplicationLocalLoop.setSwitchName(
                    ServiceDAOFactory.getService(InventoryService.class).
                            getInventoryItemByItemID(nixApplicationLocalLoop.getRouterSwitchId()).getName()
            );
            if(nixApplicationLocalLoop.getVlanId()>0)nixApplicationLocalLoop.setVlanName(
                    ServiceDAOFactory.getService(InventoryService.class).
                            getInventoryItemByItemID(nixApplicationLocalLoop.getVlanId()).getName()
            );

        }
        return list;
    }

    public List<NIXApplicationLocalLoop> getLocalLoopByOfficeAndPop(long officeID, long popID) throws Exception{
        return
                ModifiedSqlGenerator.getAllObjectList(NIXApplicationLocalLoop.class,
                        new NIXApplicationLocalLoopConditionBuilder()
                                .Where()
                                .officeIdEquals(officeID)
                                .popIdEquals(popID)
                                .getCondition()
                );
    }

    public void update(NIXApplicationLocalLoop localLoop) throws Exception{
        ModifiedSqlGenerator.updateEntity(localLoop);
    }

    public void insertApplication(NIXApplicationLocalLoop localLoop) throws Exception{
        ModifiedSqlGenerator.insert(localLoop);
    }

    public List<NIXApplicationLocalLoop> getLocalLoop(long appID)throws Exception {
        return  ModifiedSqlGenerator.getAllObjectList(NIXApplicationLocalLoop.class,
                        new NIXApplicationLocalLoopConditionBuilder()
                                .Where()
                                .officeIdInSqlPair(
                                        new NIXApplicationOfficeConditionBuilder()
                                                .selectId()
                                                .fromNIXApplicationOffice()
                                                .Where()
                                                .applicationEquals(appID)
                                                .getNullableSqlPair()
                                )
                                .getCondition()
                );
    }

    public NIXApplicationLocalLoop getLocalLoopById(long applicationLocalLoop) throws Exception{

       return ModifiedSqlGenerator.getObjectByID(NIXApplicationLocalLoop.class,applicationLocalLoop);

    }

    public NIXApplicationLocalLoop getLocalLoopByPort(long oldPortId)throws Exception {
        List<NIXApplicationLocalLoop>list =  ModifiedSqlGenerator.getAllObjectList(NIXApplicationLocalLoop.class,new NIXApplicationLocalLoopConditionBuilder().
         Where()
         .portIdEquals(oldPortId)
         .getCondition());
        if(list!=null && list.size()>0)return list.get(0);
        return null;
    }
}
