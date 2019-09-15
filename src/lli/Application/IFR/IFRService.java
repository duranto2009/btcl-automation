package lli.Application.IFR;

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

public class IFRService {

    @DAO
    IFRDAO ifrdao;
    LLIApplicationService lliApplicationService= ServiceDAOFactory.getService(LLIApplicationService.class);
    @Transactional(transactionType=TransactionType.READONLY)
    public List<IFR> getIFR(long id) throws Exception {
        List<IFR> ifrs= ifrdao.getIFR(id);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }
        return ifrs;
    }
    @Transactional
    public void insertOrUpdateIFR(ArrayList<IFR> newIFRs,long applicationID,long nextState) throws Exception {

        //todo : keep and update oldIFR With flag in future.
        LLIApplication lliApplication=lliApplicationService.getLLIApplicationByApplicationID(applicationID);
        List<IFR> oldIFRs= ifrdao.getIFRByAppID(applicationID,lliApplication.getState());
        for (IFR oldIfr:oldIFRs
             ) {
            oldIfr.setIsIgnored(1);
            ifrdao.updateIFRLLIApplication(oldIfr);

        }
        for (IFR newIfr:newIFRs) {
            newIfr.setIsReplied(1);
            if(newIfr.isSelected==0){
                newIfr.setIsReplied(1);
            }
            newIfr.setState(nextState);
            ifrdao.insertIFR(newIfr);

        }
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<IFR> getIFRByAppID(long appID,long state) throws Exception {
        List<IFR> ifrs= ifrdao.getIFRByAppID(appID,state);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<IFR> getIFRByAppID(long appID) throws Exception {
        List<IFR> ifrs= ifrdao.getIFRByAppID(appID);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<IFR> getSelectedIFRByAppID(long appID,long state) throws Exception {
        List<IFR> ifrs= ifrdao.getSelectedIFRByAppID(appID,state);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }
    @Transactional(transactionType=TransactionType.READONLY)
    public List<IFR> getSelectedIFRByAppID(long appID) throws Exception {
        List<IFR> ifrs= ifrdao.getSelectedIFRByAppID(appID);
        if(ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional
    public void insertApplication(IFR ifr,LoginDTO loginDTO) throws Exception {
        ifr.setSubmissionDate(System.currentTimeMillis());
        ifrdao.insertIFR(ifr);
    }
    @Transactional
    public void updateApplicaton(IFR ifr) throws Exception{
        ifrdao.updateIFRLLIApplication(ifr);
    }
}
