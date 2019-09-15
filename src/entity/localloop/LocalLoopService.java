package entity.localloop;

import annotation.DAO;
import annotation.Transactional;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryEssentialData;
import inventory.InventoryItem;
import inventory.InventoryService;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.VPNConstants;

import java.util.List;
import java.util.Map;

public class LocalLoopService {
    

    @DAO
    LocalLoopDAO localLoopDAO;

    GlobalService globalService= ServiceDAOFactory.getService(GlobalService.class);
    InventoryService inventoryService= ServiceDAOFactory.getService(InventoryService.class);


    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByOffice(long id)throws Exception {
        return  localLoopDAO.getLocalLoopByOffice(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByOfficeAndPop(long officeID, long popID) throws Exception {

        List<LocalLoop> localLoops = localLoopDAO.getLocalLoopByOfficeAndPop(officeID, popID);
        return localLoops;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isBillApplicableForConsumer(long localLoopId) throws Exception {

        List<LocalLoopConsumerMap> localLoopConsumerMaps =globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
        new LocalLoopConsumerMapConditionBuilder()
                .Where()
                .localLoopIdEquals(localLoopId)
                .isBillApplicable(true)
                .isActive(true)
                .getCondition()
        );
        if(localLoopConsumerMaps.size()>0){

            return false;
        }else if(getLocalLoopById(localLoopId).getLoopProvider()== VPNConstants.LOOP_CLIENT){
            return false;
        }
        else {
            return true;
        }
    }

//    @Transactional
//    public List<LocalLoop> prepareLocalloopFromIFR(ArrayList<IFR> ifrlist) throws Exception{
//        List<LocalLoop> localLoops = new ArrayList<>();
//        for (IFR ifr : ifrlist) {
//            //todo: check if loop already and update
//            List<LocalLoop> localLoopList = getLocalLoopByOfficeAndPop(ifr.getOffice(), ifr.getPop());
//            if (localLoopList.size() > 0) {
//                for (LocalLoop localLoop : localLoopList) {
//                    localLoopDAO.update(localLoop);
//                }
//            } else {
//                int clientDistance = 0;
//                int BTCLDIstance = 0;
//                int OCDistance = 0;
//                LocalLoop localLoop = new LocalLoop();
//                localLoop.setPopId(ifr.getPop());
//                localLoop.setBtclDistance(BTCLDIstance);
//                localLoop.setOcdDistance(OCDistance);
//                localLoop.setClientDistance(clientDistance);
//                localLoop.setOfficeId(ifr.getOffice());
//                localLoops.add(localLoop);
//            }
//        }
//        return localLoops;
//    }
    @Transactional
    public void updateApplicaton(LocalLoop localLoop) throws Exception {
        localLoopDAO.update(localLoop);
    }

    @Transactional
    public void insertApplication(LocalLoop localLoop) throws Exception{
        localLoopDAO.insertApplication(localLoop);
    }

//    @Transactional
//    public List<LocalLoop> getLocalLoopByAppId(long appID)throws Exception {
//        List<IFR> ifrlist = new ArrayList<>();
//        List<LocalLoop> localLoops = localLoopDAO.getLocalLoop(appID);
//        for (LocalLoop localLoop : localLoops) {
//            List<EFR> efrArrayList = nixefrService.getCompletedEFRByPOP(localLoop.getPopId(), appID);
//            int clientDistance = 0;
//            int BTCLDIstance = 0;
//            int OCDistance = 0;
//            for (EFR efr : efrArrayList) {
//                if (efr.getVendorType() == 1) {
//                    BTCLDIstance += efr.getProposedDistance();
//                } else if (efr.getVendorType() == 2) {
//                    OCDistance += efr.getProposedDistance();
//                   // localLoop.set(efr.getVendor());
//                }
//                localLoop.setOfcType(efr.getOfcType());
//            }
//            localLoop.setBtclDistance(BTCLDIstance);
//            localLoop.setOcdDistance(OCDistance);
//            localLoop.setClientDistance(clientDistance);
//        }
//        return localLoops;
//    }
//
//    @Transactional
//    public List<LocalLoop> updateLocalloopAdjustingLength(long applicationID) throws Exception {
//        List<LocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);
//        for (LocalLoop localLoop : localLoops) {
//            List<EFR> efrArrayList = nixefrService.getWorkCompletedEFRByPOP(localLoop.getPopId(), applicationID);
//            int clientDistance = 0;
//            int newBTCLDIstance = 0;
//            int newOCDistance = 0;
//            for (EFR efr : efrArrayList){
//                if (efr.getVendorType() == 1) {
//                    newBTCLDIstance += efr.getActualDistance();
//                }
//                else if (efr.getVendorType() == 2) {
//                    newOCDistance += efr.getActualDistance();
//                    localLoop.setVendor(efr.getVendor());
//                }
//                localLoop.setOfcType(efr.getOfcType());
//            }
//            //long oldOCDistance=localLoop.getOcdDistance();
//            //long oldBTCLDistance=localLoop.getBtclDistance();
//            localLoop.setOcdDistance(newOCDistance);
//            localLoop.setBtclDistance(newBTCLDIstance);
//            localLoop.setClientDistance(clientDistance);
//        }
//        return localLoops;
//    }

    @Transactional
    public LocalLoop getLocalLoopById(long loopId)throws Exception {
        return  localLoopDAO.getLocalLoopById(loopId);
    }

    @Transactional
    public LocalLoop getLocalLoopByPort(long oldPortId)throws Exception {
        return  localLoopDAO.getLocalLoopByPort(oldPortId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public void setVolatileProperties(List<LocalLoop> localLoops) throws Exception {
        for (LocalLoop localLoop:localLoops) {

            InventoryEssentialData inventoryEssentialData = InventoryEssentialData.builder()
                    .districtId(localLoop.getDistrictId())
                    .popId(localLoop.getPopId())
                    .rsId(localLoop.getRouterOrSwitchId())
                    .portId(localLoop.getPortId())
                    .vlanId(localLoop.getVlanId())
                    .build();

               modifyLocalLoopData(inventoryEssentialData, localLoop);

//                if (localLoop.getPopId() > 0) {
//                    localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopId()).getName());
//                }
//                if (localLoop.getPortId() > 0) {
//                    localLoop.setPortName(inventoryService.getInventoryItemByItemID(localLoop.getPortId()).getName());
//                }
//                if (localLoop.getPopId() > 0) {
//                    localLoop.setRsName(inventoryService.getInventoryItemByItemID(localLoop.getRouterOrSwitchId()).getName());
//                }
//                if (localLoop.getPopId() > 0) {
//                    localLoop.setVlanName(inventoryService.getInventoryItemByItemID(localLoop.getVlanId()).getName());
//                }
        }

    }


    private void modifyLocalLoopData(InventoryEssentialData essentialData, LocalLoop loop) throws Exception {
        Map<Integer, String> names = inventoryService.getInventoryItemsByInventoryEssentialData(essentialData);

        loop.setPopName(names.getOrDefault(InventoryConstants.CATEGORY_ID_POP, "N/A"));
        loop.setRsName(names.getOrDefault(InventoryConstants.CATEGORY_ID_ROUTER, "N/A"));
        loop.setPortName(names.getOrDefault(InventoryConstants.CATEGORY_ID_PORT, "N/A"));
        String vlanName = names.getOrDefault(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, "N/A");
        if(vlanName.equalsIgnoreCase("N/A")){
            vlanName = names.getOrDefault(InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN, "N/A");
        }
        loop.setVlanName(vlanName);

    }

}
