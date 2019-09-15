package lli.Application.NewLocalLoop;

import annotation.DAO;
import annotation.Transactional;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationDAO;
import lli.Application.LLIApplicationService;
import lli.Application.Office.OfficeService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewLocalLoopService {

    @DAO
    NewLocalLoopDAO localLoopDAO;
    @DAO
    LLIApplication lliApplication;
    @DAO
    LLIApplicationDAO lliApplicationDAO;

    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);

    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);


    @Transactional
    public void insertApplication(NewLocalLoop NewlocalLoop) throws Exception {

//        if(!loginDTO.getIsAdmin() && lliApplication.getClientID()!=loginDTO.getAccountID()){
//            throw new RequestFailureException("You can not submit other client's application.");
//        }


        localLoopDAO.insertlocalLoop(NewlocalLoop);
    }

    @Transactional
    public void updateApplicaton(NewLocalLoop NewlocalLoop) throws Exception {
        localLoopDAO.update(NewlocalLoop);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewLocalLoop> getLocalLoop(long applicationID) throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewLocalLoop> getLocalLoopByCon(long connectionID) throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoop(connectionID);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewLocalLoop> getLocalLoopByOffice(long officeID) throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoopByOffice(officeID);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewLocalLoop> getLocalLoopByOfficeAndApplication(long officeID, long applicationId) throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoopByOfficeAndApplication(officeID, applicationId);
        return localLoops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewLocalLoop> getLocalLoopByOffice(long officeID, long popID) throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoopByOfficeAndPop(officeID, popID);
        return localLoops;
    }


    @Transactional
    public List<NewLocalLoop> prepareLocalloopFromIFR(List<IFR> ifrlist) throws Exception {

        List<NewLocalLoop> localLoops = new ArrayList<>();
        ifrlist= ifrlist.stream().filter(t->t.getIsSelected()==1).collect(Collectors.toList());

        for (IFR ifr :ifrlist
        ) {
//            List<EFR> efrArrayList=efrService.getCompletedEFRByPOP(id,applicationID);

            //todo: check if loop already and update

            //for reuse and replace //if addloop goes to update part code we need to fix it
            List<NewLocalLoop> localLoopList = getLocalLoopByOfficeAndApplication(ifr.getOfficeID(),ifr.getApplicationID());


            if (localLoopList.size() >0) {

                for (NewLocalLoop NewlocalLoop :localLoopList
                ) {
                        NewlocalLoop.setBandwidth(ifr.getRequestedBW() + NewlocalLoop.getBandwidth());
                        NewlocalLoop.setPopID(ifr.getPopID());
                        localLoopDAO.update(NewlocalLoop);
                }
            } else {

                int clientDistance = 0;
                int BTCLDIstance = 0;
                int OCDistance = 0;
                NewLocalLoop NewlocalLoop = new NewLocalLoop();
                NewlocalLoop.setApplicationID(ifrlist.get(0).getApplicationID());
                NewlocalLoop.setPopID(ifr.getPopID());
                NewlocalLoop.setBTCLDistances(BTCLDIstance);
                NewlocalLoop.setOCDistances(OCDistance);
                NewlocalLoop.setClientDistances(clientDistance);
                NewlocalLoop.setOfficeID(ifr.getOfficeID());
                //need to implement multi office
                //todo:need to fix
//            NewlocalLoop.setBandwidth((long) lliApplication.getBandwidth());
                NewlocalLoop.setBandwidth(ifr.getRequestedBW());
                localLoops.add(NewlocalLoop);
            }
        }

        return localLoops;
}



    public List<EFR> getEFRForLoop(List<EFR>efrs){
        List<EFR> efrForLoop=new ArrayList<>();
        for (EFR efr:efrs
             ) {

            if(efr.getSourceType()==1&&efr.getDestinationType()==3){
                efrForLoop.add(efr);
                break;
            }else if(efr.getSourceType()==2&&efr.getDestinationType()==3){
                efrForLoop.add(efr);
                break;
            }
            else{
                efrForLoop.add(efr);

            }

        }
        // todo: check if a loop is completed properly
        return efrForLoop;
    }

    @Transactional
    public List<NewLocalLoop> prepareLocalloop(long applicationID) throws Exception {

        List<IFR> ifrlist = new ArrayList<>();
//        LLINewConnectionApplication lliApplication= (LLINewConnectionApplication) lliApplicationDAO.getLLIApplicationByID(applicationID);


        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoop(applicationID);

        for (NewLocalLoop NewlocalLoop : localLoops
        ) {
            List<EFR> efrArrayList = efrService.getCompletedEFRByPOP(NewlocalLoop.getPopID(), applicationID,NewlocalLoop.getOfficeID());

            //todo : check effect for different vendor with same pop
            List<EFR> efrForLoop=getEFRForLoop(efrArrayList);



            int clientDistance = 0;
            int BTCLDIstance = 0;
            int OCDistance = 0;

            for (EFR efr : efrForLoop
            ) {

                if (efr.getVendorType() == 1) {
                    BTCLDIstance += efr.getProposedLoopDistance();
                } else if (efr.getVendorType() == 2) {
                    OCDistance += efr.getProposedLoopDistance();
                    NewlocalLoop.setOCID(efr.getVendorID());

                }
                NewlocalLoop.setOfcType(efr.getOfcType());

            }

            NewlocalLoop.setBTCLDistances(BTCLDIstance);
            NewlocalLoop.setOCDistances(OCDistance);
            NewlocalLoop.setClientDistances(clientDistance);

//            todo:set bandwidth
//            NewlocalLoop.setBandwidth((long) lliApplication.getBandwidth());

        }

        return localLoops;


    }

    @Transactional
    public List<NewLocalLoop> updateLocalloopAdjustingLength(long appID)throws Exception {

        List<NewLocalLoop> localLoops = localLoopDAO.getLocalLoop(appID);

        for (NewLocalLoop localLoop : localLoops
        ) {
            List<EFR> efrArrayList = efrService.getWorkCompletedEFRByPOP(localLoop.getPopID(), appID,localLoop.getOfficeID());

            List<EFR> efrForLoop=getEFRForLoop(efrArrayList);


            int clientDistance = 0;
            int newBTCLDIstance = 0;
            int newOCDistance = 0;



            for (EFR efr : efrForLoop
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
                if(localLoop.getAdjustedOCDistance()==0){

                    localLoop.setAdjustedOCDistance(newOCDistance - oldOCDistance);
                }
                localLoop.setBTCLDistances(newBTCLDIstance);

                if(localLoop.getAdjustedBTClDistance()==0){
                    localLoop.setAdjustedBTClDistance(newBTCLDIstance - oldBTCLDistance);

                }
                localLoop.setClientDistances(clientDistance);
            }



//            todo:set bandwidth
//            localLoop.setBandwidth((long) lliApplication.getBandwidth());

        }

        return localLoops;

    }
}
