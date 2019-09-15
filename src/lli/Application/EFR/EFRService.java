package lli.Application.EFR;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import login.LoginDTO;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class EFRService {

    @DAO
    EFRDAO efrdao;


    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getEFR(long applicationId) throws Exception {
        List<EFR> efrs= efrdao.getEFR(applicationId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getIncomleteEFR(long applicationId,long vendorID) throws Exception {
        List<EFR> efrs= efrdao.getIncompleteEFR(applicationId,vendorID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getIncomleteEFRByAppID(long applicationId) throws Exception {
        List<EFR> efrs= efrdao.getIncompleteEFRByAppID(applicationId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional
    public void insertApplication(EFR efr, LoginDTO loginDTO) throws Exception {

//        if(!loginDTO.getIsAdmin() && lliApplication.getClientID()!=loginDTO.getAccountID()){
//            throw new RequestFailureException("You can not submit other client's application.");
//        }

//        efr.set(System.currentTimeMillis());
        //lliApplication.setStatus(LLIConnectionConstants.STATUS_APPLIED);
        efrdao.insertEFR(efr);
    }

    @Transactional
    public void updateApplicaton(EFR efr) throws Exception{
        efrdao.updateEFRLLIApplication(efr);
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getCompletedEFR(long applicationID, long userID) throws Exception {

        List<EFR> efrs= efrdao.getCompleteEFRByAppID(applicationID,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getCompletedEFR(long applicationID) throws Exception {

        List<EFR> efrs= efrdao.getCompleteEFRByAppID(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getNotCompletedWOByVendor(long applicationID,long vendorID) throws Exception {

        List<EFR> efrs= efrdao.getNotCompletedWOByVendor(applicationID,vendorID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getNotCompletedWO(long applicationID) throws Exception {

        List<EFR> efrs= efrdao.getNotCompletedWO(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getCompletedEFRByPOP(long popID,long appID,long officeId) throws Exception {

        List<EFR> efrs= efrdao.getCompleteEFRByPopID(popID,appID,officeId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<EFR> getWorkCompletedEFRByPOP(long popID,long appID,long officeId) throws Exception {

        List<EFR> efrs= efrdao.getWorkCompleteEFRByPopID(popID,appID,officeId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional
    public List<EFR> getSelected(long applicationId) {
        try {
            return efrdao.getSelectedEFRsByApplicationId(applicationId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public List<EFR> getEFRByVendor(long userID) throws Exception {
        List<EFR> efrs= efrdao.efrByVendor(userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<EFR> getEFRQuationNotGivenByVendor(long userID) throws Exception {
        List<EFR> efrs= efrdao.efrQuationNotGivenByVendor(userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }
    @Transactional
    public List<EFR> getWorkGivenByVendor(long userID) throws Exception {
        List<EFR> efrs= efrdao.efrWorkByVendor(userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    public List<EFR> getEFRByVendorAndAppID(long appid,long userID) throws Exception {
        List<EFR> efrs= efrdao.efrByVendorandAppId(appid,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    public List<EFR> efrByVendorQuationNotCompletedandAppId(long appid,long userID) throws Exception {
        List<EFR> efrs= efrdao.efrByVendorQuationNotCompletedandAppId(appid,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<EFR> getAllEFR(long applicationID) throws Exception {
        List<EFR> efrs= efrdao.getAllEFRByApp(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<EFR> getVendorWiseEFR(long applicationID, long userID) throws Exception {
        List<EFR> efrs= efrdao.getAllEFRByAppAndVendor(applicationID,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<EFR> getEFRsToClose(long applicationID) throws Exception {
        LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(applicationID);

        List<EFR> efrs = new ArrayList<>();
        List<LLIApplication> lliApplications =  lliApplicationService.getLLIApplicationByConnectionID(lliApplication.getConnectionId());
        for(LLIApplication application:lliApplications){
            efrs.addAll(getSelected(application.getApplicationID()));
        }

        return efrs;
    }

    @Transactional
    public List<EFR> getHistoricalCompletedEFR(long applicationID) throws Exception {
        List<EFR> efrs= efrdao.getHistoricalEFRByAppID(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }
}
