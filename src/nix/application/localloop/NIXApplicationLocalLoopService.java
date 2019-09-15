package nix.application.localloop;

import annotation.DAO;
import annotation.Transactional;
import inventory.InventoryService;
import nix.efr.NIXEFR;
import nix.ifr.NIXIFR;
import nix.efr.NIXEFRService;
import requestMapping.Service;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class NIXApplicationLocalLoopService {


    @Service
    NIXEFRService nixefrService;

    @DAO
    NIXApplicationLocalLoopDAO nixApplicationLocalLoopDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXApplicationLocalLoop> getLocalLoopByOffice(long id)throws Exception {
        return  nixApplicationLocalLoopDAO.getLocalLoopByOffice(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXApplicationLocalLoop> getLocalLoopByOfficeAndPop(long officeID, long popID) throws Exception {

        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopDAO.getLocalLoopByOfficeAndPop(officeID, popID);
        return localLoops;
    }

    @Transactional
    public List<NIXApplicationLocalLoop> prepareLocalloopFromIFR(ArrayList<NIXIFR> ifrlist) throws Exception{
        List<NIXApplicationLocalLoop> localLoops = new ArrayList<>();
        for (NIXIFR ifr : ifrlist) {
            //todo: check if loop already and update
            List<NIXApplicationLocalLoop> localLoopList = getLocalLoopByOfficeAndPop(ifr.getOffice(), ifr.getPop());
            if (localLoopList.size() > 0) {
                for (NIXApplicationLocalLoop localLoop : localLoopList) {
                    nixApplicationLocalLoopDAO.update(localLoop);
                }
            } else {
                int clientDistance = 0;
                int BTCLDIstance = 0;
                int OCDistance = 0;
                NIXApplicationLocalLoop localLoop = new NIXApplicationLocalLoop();
                localLoop.setPopId(ifr.getPop());
                localLoop.setBtclDistance(BTCLDIstance);
                localLoop.setOcdDistance(OCDistance);
                localLoop.setClientDistance(clientDistance);
                localLoop.setOfficeId(ifr.getOffice());
                localLoops.add(localLoop);
            }
        }
        return localLoops;
    }
    @Transactional
    public void updateApplicaton(NIXApplicationLocalLoop localLoop) throws Exception {
        nixApplicationLocalLoopDAO.update(localLoop);
    }

    @Transactional
    public void insertApplication(NIXApplicationLocalLoop localLoop) throws Exception{
        nixApplicationLocalLoopDAO.insertApplication(localLoop);
    }

    @Transactional
    public List<NIXApplicationLocalLoop> getLocalLoopByAppId(long appID)throws Exception {
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopDAO.getLocalLoop(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            List<NIXEFR> efrArrayList = nixefrService.getCompletedEFRByLoop(localLoop.getId(), appID);
            int clientDistance = 0;
            int BTCLDIstance = 0;
            int OCDistance = 0;
            for (NIXEFR efr : efrArrayList) {
                if (efr.getVendorType() == 1) {
                    BTCLDIstance += efr.getProposedDistance();
                } else if (efr.getVendorType() == 2) {
                    OCDistance += efr.getProposedDistance();
                   // localLoop.set(efr.getVendor());
                }
                localLoop.setOfcType(efr.getOfcType());
            }
            localLoop.setBtclDistance(BTCLDIstance);
            localLoop.setOcdDistance(OCDistance);
            localLoop.setClientDistance(clientDistance);
        }
        return localLoops;
    }

    @Transactional
    public List<NIXApplicationLocalLoop> getLocalLoopsAdjustedWithActualDistanceByApplicationId(long applicationID) throws Exception {
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopDAO.getLocalLoop(applicationID);

        for (NIXApplicationLocalLoop localLoop : localLoops) {

            //edit for local loop
            List<NIXEFR> efrArrayList = nixefrService.getWorkCompletedEFRByLocalLoop(localLoop.getId(), applicationID);
            long clientDistance = 0;
            long newBTCLDIstance = 0;
            long newOCDistance = 0;
            for (NIXEFR efr : efrArrayList){
                if(efr.getApprovedDistance()==1) {
                    if (efr.getVendorType() == 1) {
                        newBTCLDIstance += efr.getActualDistance();
                    } else if (efr.getVendorType() == 2) {
                        newOCDistance += efr.getActualDistance();
                        localLoop.setVendor(efr.getVendor());
                    }
                    localLoop.setOfcType(efr.getOfcType());
                }
            }
            localLoop.setOcdDistance(newOCDistance);
            localLoop.setBtclDistance(newBTCLDIstance);
            localLoop.setClientDistance(clientDistance);
        }
        return localLoops;
    }

    @Transactional
    public NIXApplicationLocalLoop getLocalLoopById(long applicationLocalLoop)throws Exception {
        NIXApplicationLocalLoop nixApplicationLocalLoop =  nixApplicationLocalLoopDAO.getLocalLoopById(applicationLocalLoop);
        return  nixApplicationLocalLoop;
    }

    @Transactional
    public NIXApplicationLocalLoop getLocalLoopByPort(long oldPortId)throws Exception {
        return  nixApplicationLocalLoopDAO.getLocalLoopByPort(oldPortId);
    }

    @Transactional
    public List<NIXApplicationLocalLoop> prepareLocalloop(long applicationID) throws Exception {
        List<NIXIFR> ifrlist = new ArrayList<>();
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopDAO.getLocalLoop(applicationID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            List<NIXEFR> efrArrayList = nixefrService.getCompletedEFRByPOP(localLoop.getPopId(), applicationID,localLoop.getOfficeId());
            int clientDistance = 0;
            int BTCLDIstance = 0;
            int OCDistance = 0;
            for (NIXEFR efr : efrArrayList) {
                if (efr.getVendorType() == 1) {
                    BTCLDIstance += efr.getProposedDistance();
                }
                else if (efr.getVendorType() == 2) {
                    OCDistance += efr.getProposedDistance();
                    localLoop.setVendor(efr.getVendor());
                }
                localLoop.setOfcType(efr.getOfcType());
            }
            localLoop.setBtclDistance(BTCLDIstance);
            localLoop.setOcdDistance(OCDistance);
            localLoop.setClientDistance(clientDistance);
        }
        return localLoops;
    }

}
