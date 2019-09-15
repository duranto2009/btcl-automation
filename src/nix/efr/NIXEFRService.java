package nix.efr;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;

import java.util.List;

public class NIXEFRService {
    @DAO
    NIXEFRDAO nixefrdao;
    @Transactional
    public List<NIXEFR> getIncomleteEFR(long applicationId, long vendorID)throws Exception {
        // TODO: 12/12/2018 create EFR class for nix and dao with unimplemented methods
        List<NIXEFR> efrs= nixefrdao.getIncompleteEFR(applicationId,vendorID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<NIXEFR> getCompletedEFR(long id, long userID)throws Exception {
        List<NIXEFR> efrs= nixefrdao.getCompleteEFRByAppID(id,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }
        return efrs;
    }

    @Transactional(transactionType= TransactionType.READONLY)
    public List<NIXEFR> getCompletedEFR(long applicationID) throws Exception {
        List<NIXEFR> efrs= nixefrdao.getCompleteEFRByAppID(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }
        return efrs;
    }

    @Transactional
    public void insertApplication(NIXEFR efr) throws Exception{
        nixefrdao.insertApplication(efr);
    }

    @Transactional
    public List<NIXEFR> getEFRByVendor(long userID)throws Exception {
        List<NIXEFR> efrs= nixefrdao.efrByVendor(userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<NIXEFR> getWorkCompletedEFRByPOP(long popID,long appID) throws Exception {

        List<NIXEFR> efrs= nixefrdao.getWorkCompleteEFRByPopID(popID,appID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<NIXEFR> getNotCompletedWOByVendor(long applicationID,long vendorID) throws Exception {

        List<NIXEFR> efrs= nixefrdao.getNotCompletedWOByVendor(applicationID,vendorID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<NIXEFR> getNotCompletedWO(long applicationID) throws Exception {

        List<NIXEFR> efrs= nixefrdao.getNotCompletedWO(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<NIXEFR> getEFRByVendorAndAppID(long appId, long userId)throws Exception {
        List<NIXEFR> efrs= nixefrdao.efrByVendorandAppId(appId,userId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public void updateApplicaton(NIXEFR efr)throws Exception {
        nixefrdao.updateApplication(efr);
    }

    @Transactional
    public List<NIXEFR> getIncomleteEFRByAppID(long applicationID) throws Exception{
        List<NIXEFR> efrs= nixefrdao.getIncomleteEFRByAppID(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    public List<NIXEFR> getCompletedEFRByPOP(long popId, long appID)throws Exception {
        List<NIXEFR> efrs= nixefrdao.getCompleteEFRByPopID(popId,appID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }
        return efrs;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<NIXEFR> getCompletedEFRByPOP(long popID,long appID,long officeId) throws Exception {

        List<NIXEFR> efrs= nixefrdao.getCompleteEFRByPopID(popID,appID,officeId);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    @Transactional
    public List<NIXEFR> getSelected(long applicationId) {
        try {
            return nixefrdao.getSelectedEFRsByApplicationId(applicationId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public List<NIXEFR> getAllEFR(long applicationID) throws Exception {
        List<NIXEFR> efrs= nixefrdao.getAllEFRByApp(applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }


    public List<NIXEFR> getVendorWiseEFR(long id, long userID) throws Exception{
        List<NIXEFR> efrs= nixefrdao.getAllEFRByAppAndVendor(id,userID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<NIXEFR> getHistoricalCompletedEFR(long id) throws Exception{
        List<NIXEFR> efrs= nixefrdao.getHistoricalEFRByAppID(id);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public List<NIXEFR> getCompletedEFRByLoop(long loopId, long appID) throws Exception{
        return nixefrdao.getCompletedEFRByLoop(loopId,appID);
    }

    @Transactional
    public List<NIXEFR> getWorkCompletedEFRByLocalLoop(long loopId, long applicationID)throws Exception {
        List<NIXEFR> efrs= nixefrdao.getWorkCompletedEFRByLocalLoop(loopId,applicationID);
        if(efrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return efrs;
    }

    @Transactional
    public boolean getCompletedEFRByLocalLoopID(long loopId)throws Exception {
        NIXEFR nixefr = nixefrdao.getCompletedEFRByLocalLoopID(loopId);
        if(nixefr!=null)return true;
        else return false;
    }
}
