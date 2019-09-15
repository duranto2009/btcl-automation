package lli.Application.LocalLoop;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import inventory.InventoryConstants;
import inventory.InventoryEssentialData;
import inventory.InventoryService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import requestMapping.Service;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalLoopService {

    @DAO
    LocalLoopDAO localLoopDAO;
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
    @Transactional
    public void insertApplication(LocalLoop localLoop) throws Exception {
        localLoopDAO.insertlocalLoop(localLoop);
    }
    @Transactional
    public void insertSnapshotLoop(long oldOfficeID,long newOfficeID,long appID) throws Exception {
        List<LocalLoop> localLoopList=getLocalLoopByOffice(oldOfficeID);
        for (LocalLoop localLoop:localLoopList) {
            localLoop.setOfficeID(newOfficeID);
            localLoop.setApplicationID(appID);
            localLoop.setId(localLoop.getHistoryID());
            //localLoop.setHistoryID(localLoop.getId());
            localLoopDAO.insertlocalLoop(localLoop);

        }
    }

    @Transactional
    public void updateApplicaton(LocalLoop localLoop) throws Exception {
        localLoopDAO.update(localLoop);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoop(long applicationID) throws Exception {

        List<LocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByCon(long connectionID) throws Exception {
        List<Office> offices = officeService.getOfficeByCON(connectionID);

        List<LocalLoop> localLoops = new ArrayList<>();
        List<LocalLoop> temp;
        for (Office office : offices) {
            temp = getLocalLoopByOfficeWithoutVolatileData(office.getId());
            if (temp != null) {
                localLoops.addAll(temp);
            }
        }

        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByOffice(long officeID) throws Exception {

        List<LocalLoop> localLoops = localLoopDAO.getLocalLoopByOffice(officeID);
        for (LocalLoop loop : localLoops) {

            InventoryEssentialData essentialData = InventoryEssentialData.builder()
                    .vlanId(loop.getVLANID())
                    .portId(loop.getPortID())
                    .rsId(loop.getRouter_switchID())
                    .popId(loop.getPopID())
                    .build();
            modifyLocalLoopData(essentialData, loop);
        }
        return localLoops;
    }

    private void modifyLocalLoopData(InventoryEssentialData essentialData, LocalLoop loop) throws Exception {
        Map<Integer, String> names = inventoryService.getInventoryItemsByInventoryEssentialData(essentialData);

        loop.setPopName(names.getOrDefault(InventoryConstants.CATEGORY_ID_POP, "N/A"));
        loop.setSwitchName(names.getOrDefault(InventoryConstants.CATEGORY_ID_ROUTER, "N/A"));
        loop.setPortName(names.getOrDefault(InventoryConstants.CATEGORY_ID_PORT, "N/A"));
        String vlanName = names.getOrDefault(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, "N/A");
        if(vlanName.equalsIgnoreCase("N/A")){
            vlanName = names.getOrDefault(InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN, "N/A");
        }
        loop.setVlanName(vlanName);
        loop.setLoopName(loop.getPopName()+'-'+loop.getSwitchName()+'-'+loop.getPortName());
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByOfficeWithoutVolatileData(long officeID) throws Exception {

        List<LocalLoop> localLoops = localLoopDAO.getLocalLoopByOffice(officeID);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopByOffice(long officeID, long popID) throws Exception {

        List<LocalLoop> localLoops = localLoopDAO.getLocalLoopByOfficeAndPop(officeID, popID);
        return localLoops;
    }


    @Transactional
    public List<LocalLoop> prepareLocalloopFromIFR(List<IFR> ifrlist) throws Exception {

        List<LocalLoop> localLoops = new ArrayList<>();

        for (IFR ifr : ifrlist
                ) {
//            List<EFR> efrArrayList=efrService.getCompletedEFRByPOP(id,applicationID);

            //todo: check if loop already and update

            List<LocalLoop> localLoopList = getLocalLoopByOffice(ifr.getOfficeID(), ifr.getPopID());
            if (localLoopList.size() > 0) {

                for (LocalLoop localLoop : localLoopList
                        ) {
                    localLoop.setBandwidth(ifr.getRequestedBW() + localLoop.getBandwidth());
                    localLoopDAO.update(localLoop);
                }
            } else {

                int clientDistance = 0;
                int BTCLDIstance = 0;
                int OCDistance = 0;
                LocalLoop localLoop = new LocalLoop();
                localLoop.setApplicationID(ifrlist.get(0).getApplicationID());
                localLoop.setPopID(ifr.getPopID());
                localLoop.setBTCLDistances(BTCLDIstance);
                localLoop.setOCDistances(OCDistance);
                localLoop.setClientDistances(clientDistance);
                localLoop.setOfficeID(ifr.getOfficeID());
                //need to implement multi office
                //todo:need to fix
//            localLoop.setBandwidth((long) lliApplication.getBandwidth());
                localLoop.setBandwidth(ifr.getRequestedBW());
                localLoops.add(localLoop);
            }
        }

        return localLoops;
    }

    @Transactional
    public List<LocalLoop> prepareLocalloop(long applicationID) throws Exception {

        List<IFR> ifrlist = new ArrayList<>();
//        LLINewConnectionApplication lliApplication= (LLINewConnectionApplication) lliApplicationDAO.getLLIApplicationByID(applicationID);


        List<LocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);

        for (LocalLoop localLoop : localLoops
                ) {
            List<EFR> efrArrayList = efrService.getCompletedEFRByPOP(localLoop.getPopID(), applicationID,localLoop.getOfficeID());
            int clientDistance = 0;
            int BTCLDIstance = 0;
            int OCDistance = 0;

            for (EFR efr : efrArrayList
                    ) {

                if (efr.getVendorType() == 1) {
                    BTCLDIstance += efr.getProposedLoopDistance();
                } else if (efr.getVendorType() == 2) {
                    OCDistance += efr.getProposedLoopDistance();
                    localLoop.setOCID(efr.getVendorID());

                }
                localLoop.setOfcType(efr.getOfcType());

            }

            localLoop.setBTCLDistances(BTCLDIstance);
            localLoop.setOCDistances(OCDistance);
            localLoop.setClientDistances(clientDistance);

//            todo:set bandwidth
//            localLoop.setBandwidth((long) lliApplication.getBandwidth());

        }

        return localLoops;


    }


    @Transactional
    public List<LocalLoop> updateLocalloopAdjustingLength(long applicationID) throws Exception {

//        LLINewConnectionApplication lliApplication= (LLINewConnectionApplication) lliApplicationDAO.getLLIApplicationByID(applicationID);


        List<LocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);

        for (LocalLoop localLoop : localLoops
        ) {
            List<EFR> efrArrayList = efrService.getWorkCompletedEFRByPOP(localLoop.getPopID(), applicationID,localLoop.getOfficeID());
            int clientDistance = 0;
            int newBTCLDIstance = 0;
            int newOCDistance = 0;



            for (EFR efr : efrArrayList
            ) {

                if(efr.getLoopDistanceIsApproved()==1) {
                    if (efr.getVendorType() == 1) {
                        newBTCLDIstance += efr.getActualLoopDistance();
                    } else if (efr.getVendorType() == 2) {
                        newOCDistance += efr.getActualLoopDistance();
                        localLoop.setOCID(efr.getVendorID());
                    }
                    localLoop.setOfcType(efr.getOfcType());
                }

            }

            long oldOCDistance=localLoop.getOCDistances();
            long oldBTCLDistance=localLoop.getBTCLDistances();

            if(efrArrayList.size()>0) {
                localLoop.setOCDistances(newOCDistance);
                localLoop.setAdjustedOCDistance(newOCDistance - oldOCDistance);
                localLoop.setBTCLDistances(newBTCLDIstance);
                localLoop.setAdjustedBTClDistance(newBTCLDIstance - oldBTCLDistance);
                localLoop.setClientDistances(clientDistance);
            }



//            todo:set bandwidth
//            localLoop.setBandwidth((long) lliApplication.getBandwidth());

        }

        return localLoops;


    }

    @Transactional
    public LocalLoop getLocalLoopById(long localLoopId)throws Exception {
        return localLoopDAO.getLocalLoopById(localLoopId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No Loop found "));
    }

    public static LocalLoopStringProjection getLocalLoopProjection(LocalLoop localLoop) {
        InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
        String popName = localLoop.getPopID() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName();
        String router_switch_name = localLoop.getRouter_switchID() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getRouter_switchID()).getName();
        String portName = localLoop.getPortID() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getPortID()).getName();
        String vlanName = localLoop.getVLANID() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getVLANID()).getName();
        String ocName = localLoop.getOCID() == 0 ? "N/A":UserRepository.getInstance().getUserDTOByUserID(localLoop.getOCID()).getUsername();
        long btclDistance = localLoop.getBTCLDistances();
        long ocDistance = localLoop.getOCDistances();
        long clientDistance = localLoop.getClientDistances();
        return LocalLoopStringProjection.builder()
                .popName(popName)
                .portName(portName)
                .switchOrRouterName(router_switch_name)
                .vlanName(vlanName)
                .ocName(ocName)
                .btclDistance(btclDistance)
                .clientDistance(clientDistance)
                .ocDistance(ocDistance)
                .build();

    }
}
