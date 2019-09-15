package coLocation.ifr;

import annotation.DAO;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import login.LoginDTO;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class CoLocationApplicationIFRService {
    @DAO
    CoLocationApplicationIFRDAO ifrdao;
//    @DAO
//    CoLocationApplicationDTO lliApplication;

    CoLocationApplicationService coLocationApplicationService = ServiceDAOFactory.getService(CoLocationApplicationService.class);

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationApplicationIFRDTO getIFR(long id) throws Exception {
        CoLocationApplicationIFRDTO ifrs = ifrdao.getIFR(id);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }


    @Transactional
    public void insertOrUpdateIFR(ArrayList<CoLocationApplicationIFRDTO> newIFRs, long applicationID, long nextState) throws Exception {

        //todo : keep and update oldIFR With flag in future.
        CoLocationApplicationDTO lliApplication = coLocationApplicationService.getColocationApplication(applicationID);
        List<CoLocationApplicationIFRDTO> oldIFRs = ifrdao.getIFRByAppID(applicationID, lliApplication.getState());
        for (CoLocationApplicationIFRDTO oldIfr : oldIFRs
        ) {
//            oldIfr.setIsIgnored(1);
            ifrdao.updateIFRCoLocationApplication(oldIfr);

        }
        for (CoLocationApplicationIFRDTO newIfr : newIFRs
        ) {
//            newIfr.setIsReplied(1);
//            newIfr.setState(nextState);

            ifrdao.insertIFR(newIfr);

        }

    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getIFRByAppID(long appID, long state) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getIFRByAppID(appID, state);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getIFRByAppID(long appID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getIFRByAppID(appID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getIFRByAppIDAndRole(long appID,long roleID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getIFRByAppIDAndRole(appID,roleID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getSelectedIFRByAppID(long appID, long state) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getSelectedIFRByAppID(appID, state);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getSelectedIFRByAppID(long appID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getSelectedIFRByAppID(appID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getIncompleteIFRByAppID(long appID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getIncompleteIFRByAppID(appID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationIFRDTO> getNotRepliedIFRByAppID(long appID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getNotRepliedIFRByAppID(appID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }

        return ifrs;
    }

    @Transactional
    public void insertApplication(CoLocationApplicationIFRDTO ifr
//            , LoginDTO loginDTO
    ) throws Exception {

//        if(!loginDTO.getIsAdmin() && lliApplication.getClientID()!=loginDTO.getAccountID()){
//            throw new RequestFailureException("You can not submit other client's application.");
//        }

        ifr.setSubmissionDate(System.currentTimeMillis());
        //lliApplication.setStatus(LLIConnectionConstants.STATUS_APPLIED);
        ifrdao.insertIFR(ifr);
    }

    @Transactional
    public void updateApplicaton(CoLocationApplicationIFRDTO ifr) throws Exception {
        ifrdao.updateIFRCoLocationApplication(ifr);
    }

    @Transactional
    public void updateFeasibility(JsonObject jsonObject, long appID, int state, LoginDTO loginDTO) throws Exception {
        JsonArray jsonArray = jsonObject.getAsJsonArray("ifr");
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();
            long id = object.get("id").getAsLong();
            CoLocationApplicationIFRDTO ifrdto = ifrdao.getIFR(id);

//              ifrdto.setCompleted(object.get("isCompleted").getAsBoolean());
            ifrdto.setReplied(true);
            ifrdto.setSelected(object.get("isSelected").getAsBoolean());
            ifrdao.updateIFRCoLocationApplication(ifrdto);
        }

        List<CoLocationApplicationIFRDTO> notRepliedIFRByAppID=getNotRepliedIFRByAppID(appID);
        if(!(notRepliedIFRByAppID.size()>0)){
            CoLocationApplicationDTO coLocationApplicationDTO=coLocationApplicationService.getColocationApplication(appID);
            coLocationApplicationDTO.setState(state);
            coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
        }

    }


    @Transactional
    public void CheckIFRandUpdateState(JsonObject jsonObject,long appID,int state,LoginDTO loginDTO) throws Exception {
        JsonArray jsonArray = jsonObject.getAsJsonArray("ifr");
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();
            long id = object.get("id").getAsLong();
            markIFRAsCompleted(id);
        }
        List<CoLocationApplicationIFRDTO> incompleteIFRDTOS=getIncompleteIFRByAppID(appID);
        if(!(incompleteIFRDTOS.size()>0)){
            CoLocationApplicationDTO coLocationApplicationDTO=coLocationApplicationService.getColocationApplication(appID);
            coLocationApplicationDTO.setState(state);
            coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
        }

    }


    @Transactional
    public void markIFRAsCompleted(long ifrID) throws Exception {
        CoLocationApplicationIFRDTO coLocationApplicationIFRDTO = getIFR(ifrID);
        coLocationApplicationIFRDTO.setCompleted(true);
        ifrdao.updateIFRCoLocationApplication(coLocationApplicationIFRDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean hasIFRAccess(CoLocationApplicationDTO coLocationApplicationDTO,long roleID) throws Exception {
        List<CoLocationApplicationIFRDTO> ifrs = ifrdao.getIFRByAppIDAndRole(coLocationApplicationDTO.getApplicationID(),roleID);
        if (ifrs == null) {
            throw new RequestFailureException("No Data found ");
        }
        boolean hasAccess = false;
        for(int i=0;i<ifrs.size();i++){
            if(ifrs.get(i).getUserRoleId() == roleID){
                hasAccess = true;
            }
        }

        // if no ifr then always has access
        if(ifrs.size() == 0 ){
            if(!coLocationApplicationDTO.isPowerNeeded()&&roleID== CoLocationConstants.DGM_BROADBAND){

                hasAccess = false;
            }
            else {

                hasAccess = true;
            }
        }


        return hasAccess;
    }


}
