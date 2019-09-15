package nix.ifr;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import nix.application.NIXApplication;
import requestMapping.Service;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class NIXIFRService {

    @DAO
    NIXIFRDAO nixifrdao;

    @Service
    nix.application.NIXApplicationService NIXApplicationService;

    @Transactional
    public List<NIXIFR> getIFRByAppID(long id)throws Exception {

        // TODO: 12/12/2018 create dao class and also implement other services as well  
        return nixifrdao.getIFRByAppID(id);
    }


    @Transactional(transactionType= TransactionType.READONLY)
    public List<NIXIFR> getSelectedIFRByAppID(long appID,long state) throws Exception {
        List<NIXIFR> ifrs= nixifrdao.getSelectedIFRByAppID(appID);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }
    @Transactional(transactionType=TransactionType.READONLY)
    public List<NIXIFR> getSelectedIFRByAppID(long appID) throws Exception {
        List<NIXIFR> ifrs= nixifrdao.getSelectedIFRByAppID(appID);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional
    public void insertApplication(NIXIFR ifr, LoginDTO loginDTO)throws Exception {
        ifr.setSubmissionDate(System.currentTimeMillis());
        nixifrdao.insertIFR(ifr);
    }

    @Transactional
    public void updateIFR(ArrayList<NIXIFR> ifrArrayList, long appID, int state)throws Exception {
        //todo : keep and update oldIFR With flag in future.
        //NIXApplication nixApplication= NIXApplicationService.getApplicationById(appID);
        for (NIXIFR newIfr:ifrArrayList ) {
            if(newIfr.getId()>0) {
                newIfr.setReplied(1);
                newIfr.setLastModificationTime(System.currentTimeMillis());
                nixifrdao.updateIFRApplication(newIfr);
            }
            else {
                newIfr.setReplied(1);
                newIfr.setLastModificationTime(System.currentTimeMillis());
                nixifrdao.insertIFR(newIfr);
            }
        }
    }


    @Transactional
    public void updateApplicaton(NIXIFR ifr)throws Exception {
        nixifrdao.updateIFRApplication(ifr);
    }
}
