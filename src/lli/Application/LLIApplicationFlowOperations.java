package lli.Application;

import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.RequestFailureException;
import global.GlobalService;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalIP.LLIAdditionalIPService;
import lli.Application.AdditionalPort.AdditionalPort;
import lli.Application.AdditionalPort.AdditionalPortConditionBuilder;
import lli.Application.AdditionalPort.LLIAdditionalPortService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRDeserializer;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRDeserializer;
import lli.Application.IFR.IFRService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopConditionBuilder;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.newOffice.NewOffice;
import lli.Application.newOffice.NewOfficeService;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import requestMapping.Service;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LLIApplicationFlowOperations {


    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    @Service
    NewLocalLoopService newlocalLoopService;

    @Service
    LLIAdditionalIPService lliAdditionalIPService; //added by jami
    @Service
    LLIAdditionalPortService lliAdditionalPortService; //jami

    @Service
    private GlobalService globalService;

    private OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    private LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    private NewOfficeService newOfficeService = ServiceDAOFactory.getService(NewOfficeService.class);
    private EFRService efrService = ServiceDAOFactory.getService(EFRService.class);


    private boolean checkIfAnyIfrFeasible(List<IFR> ifrs) {

        boolean isAnyIFRFeasible = false;
        for (IFR ifr :
                ifrs) {
            if (ifr.getIsSelected() == 1) {
                isAnyIFRFeasible = true;
                break;
            }

        }


        return isAnyIFRFeasible;
    }

    @Transactional
    public void applicationForwardForEFRPurpose(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        ArrayList<IFR> ifrs = new IFRDeserializer().deserialize_custom_ifr_update(jsonElement);


        boolean isAnyIfrFeasible = checkIfAnyIfrFeasible(ifrs);

        if (!isAnyIfrFeasible) {
            throw new RequestFailureException("There is no feasible ifr to proceed this application");
        }


        ArrayList<IFR> selectedIFR = new ArrayList<>();
        for (IFR ifr : ifrs) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            } else {
                ifr.setIsSelected(LLIConnectionConstants.IFR_IGNORED);
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.updateApplicaton(ifr);
        }


        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {

//            List<NewLocalLoop> localLoops = newlocalLoopService.prepareLocalloopFromIFR(selectedIFR);
//            for (NewLocalLoop localLoop : localLoops
//            ) {
//                newlocalLoopService.insertApplication(localLoop);
//            }
        } else {
            List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
            for (LocalLoop localLoop : localLoops
            ) {
                localLoopService.insertApplication(localLoop);
            }
        }

        lliApplication.setIsForwarded(1);
        if (lliApplication.getSecondZoneID() == 0) {

            lliApplication.setSecondZoneID(lliApplication.getZoneId());
        }
        lliApplication.setZoneId(jsonObject.get("zoneID").getAsInt());
        lliApplication.setState(state);
        lliApplicationService.updateApplicaton(lliApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
    }


    @Transactional
    public void adviceNoteGenerateLogicOperations(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;

        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);//added by Jami for new local loop advice note

        int state = jsonObject.get("nextState").getAsInt();
        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();

        JsonArray userArray = jsonObject.getAsJsonArray("userList");

        //ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom(jsonElement);

        ArrayList<EFR> lists = new ArrayList<>();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH && lliApplication.isNewLoop())
        ) {
            lists = new EFRDeserializer().deserialize_custom_new_local_loop(jsonElement);
        } else {
            lists = new EFRDeserializer().deserialize_custom(jsonElement);
        }
//
        for (EFR efr : lists) {
            efrService.updateApplicaton(efr);
        }
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH && lliApplication.isNewLoop())

        ) {
            List<NewLocalLoop> localLoops = newlocalLoopService.updateLocalloopAdjustingLength(appID);
            for (NewLocalLoop localLoop : localLoops) {
                newlocalLoopService.updateApplicaton(localLoop);
            }
        } else {
            List<LocalLoop> localLoops = localLoopService.updateLocalloopAdjustingLength(appID);
            for (LocalLoop localLoop : localLoops) {
                localLoopService.updateApplicaton(localLoop);
            }
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.generateAdviceNoteDocument(appID, state, userArray, senderId);

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);

    }

    @Transactional
    public String ifrInsertBatchOperations(String JsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);


        ArrayList<IFR> lists = new IFRDeserializer().deserialize_custom(jelement);

        for (IFR ifr : lists) {

            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.insertApplication(ifr, loginDTO);
            //parent ifr id need to be incorporated
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);//this need to update state
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);


        return "";//need to go search page
    }


    @Transactional
    public void DNGenerateLogic(JsonElement jsonElement, LoginDTO loginDTO, int skipPay) throws Exception {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        lliApplication.setSkipPayment(skipPay);
        lliApplicationService.updateApplicaton(lliApplication);

        ArrayList<EFR> lists = new ArrayList<>();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {
            lists = new EFRDeserializer().deserialize_custom_new_local_loop(jsonElement);
        } else {
            lists = new EFRDeserializer().deserialize_custom(jsonElement);
        }

        for (EFR efr : lists) {
            efrService.updateApplicaton(efr);
        }
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || lliApplication.isNewLoop()
        ) {
            List<NewLocalLoop> localLoops = newlocalLoopService.prepareLocalloop(appID);
            for (NewLocalLoop localLoop : localLoops) {
                newlocalLoopService.updateApplicaton(localLoop);
            }
        } else {

            List<LocalLoop> localLoops = localLoopService.prepareLocalloop(appID);
            for (LocalLoop localLoop : localLoops) {
                localLoopService.updateApplicaton(localLoop);
            }
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

    }


    public List<IFR> checkForNewLoopFromIFR(List<IFR> selectedIFRList, Map<Long, List<LocalLoop>> popMappedLoop) throws Exception {


        boolean isNewLoop = false;

        List<IFR> selectedIFRListForLoop = new ArrayList<>();

        for (IFR ifr : selectedIFRList
        ) {
            if (!popMappedLoop.containsKey(ifr.getPopID())) {

                selectedIFRListForLoop.add(ifr);
                isNewLoop = true;
//                break;
            }

        }
        return selectedIFRListForLoop;
    }

    @Transactional
    public void IFRUpdateLogicOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception {

        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);


        //this need to update state


        ArrayList<IFR> ifrArrayList = new IFRDeserializer()
                .deserialize_custom(jelement);


        ifrService.insertOrUpdateIFR(ifrArrayList, appID, state);

        List<IFR> selectedIFRList = ifrArrayList
                .stream()
                .filter(t -> t.getIsSelected() == 1)
                .collect(Collectors.toList());

        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);
        List<Office> officeList = new ArrayList<>();
        List<LocalLoop> localLoopList = new ArrayList<>();
        if (lliApplication.getConnectionId() > 0) {
            officeList = officeService.getOfficeByCON(lliApplication.getConnectionId());
            for (Office office : officeList) {
                for (LocalLoop localLoop : office.getLoops()) {
                    localLoopList.add(localLoop);
                }
            }
        }

        Map<Long, List<LocalLoop>> popMappedLoop = localLoopList
                .stream()
                .collect(Collectors.groupingBy(LocalLoop::getPopID));


        if (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH
        ) {

            List<IFR> selectedIfrForLoop = checkForNewLoopFromIFR(selectedIFRList, popMappedLoop);

            boolean isLoopFromNewPop = false;
            if (selectedIfrForLoop != null && selectedIfrForLoop.size() > 0) {
                isLoopFromNewPop = true;
            }
            if (!isLoopFromNewPop) {
                //no new pop neded for upgrade

                state = LLIConnectionConstants.WITHOUT_LOOP_IFR_RESPONSE;
            } else {
                //new pop needed for upgrade
                lliApplication.setNewLoop(true);
                List<NewLocalLoop> localLoops = newlocalLoopService.prepareLocalloopFromIFR(selectedIfrForLoop);
                List<NewLocalLoop> modifiedLocalLoops = new ArrayList<>();
                List<NewOffice> newOffices = new ArrayList<>();
                if (officeList != null & officeList.size() > 0) {
                    for (Office office : officeList) {
                        NewOffice newOffice = new NewOffice();
                        newOffice.setOld_office_id(office.getId());
                        newOffice.setOfficeName(office.getOfficeName());
                        newOffice.setConnectionID(office.getConnectionID());
                        newOffice.setOfficeAddress(office.getOfficeAddress());
                        newOffice.setApplicationId(lliApplication.getApplicationID());
                        globalService.save(newOffice);
                        List<NewLocalLoop> localLoopList1 = localLoops
                                .stream()
                                .filter(t -> t.getOfficeID() == newOffice.getOld_office_id())
                                .collect(Collectors.toList());
                        if (localLoopList1 != null && localLoopList1.size() > 0) {
                            for (NewLocalLoop newLocalLoop : localLoopList1
                            ) {
                                newLocalLoop.setOfficeID(newOffice.getId());
                            }
                        }

                        modifiedLocalLoops.addAll(localLoopList1);

                    }
                }
                for (NewLocalLoop localLoop : modifiedLocalLoops) {
                    newlocalLoopService.insertApplication(localLoop);
                }

            }


        }

        if (lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION && lliApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_CLIENT) {
            state = LLIConnectionConstants.SHIFT_BW_NEW_DN_BYPASS;
            ArrayList<IFR> selectedIFR = new ArrayList<>();
            for (IFR ifr : ifrArrayList) {

                if (ifr.getIsSelected() == 1) {
                    selectedIFR.add(ifr);
                }
            }

            List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
            for (LocalLoop localLoop : localLoops
            ) {
                localLoopService.insertApplication(localLoop);
            }
        }

        lliApplication.setState(state);
        lliApplicationService.updateApplicaton(lliApplication);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
    }


    @Transactional
    public void IFRUpdateNewLoopLogicOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);
        int state = jsonObject.get("nextState").getAsInt();
        ArrayList<IFR> ifrArrayList = new IFRDeserializer().deserialize_custom_local_loop(jelement);

        checkIFIFRFromSamePOP(ifrArrayList,lliApplication);


        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        ifrService.insertOrUpdateIFR(ifrArrayList, appID, state);


        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP) {

            List<NewLocalLoop> localLoops = newlocalLoopService.prepareLocalloopFromIFR(ifrArrayList);
            for (NewLocalLoop localLoop : localLoops) {
                newlocalLoopService.insertApplication(localLoop);
            }
        }

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
    }


    @Transactional
    public long applicationEdit(JsonElement jelement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jelement.getAsJsonObject();

        int nextState = jsonObject.get("nextState").getAsInt();

        LLIApplication lliNewConnectionApplication = new LLIApplicationDeserializer().deserialize_custom(jelement);

        if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_CLIENT
                && lliNewConnectionApplication.getApplicationType() == LLIConnectionConstants.NEW_CONNECTION) {
//            lliNewConnectionApplication.setState(LLIConnectionConstants.NEW_CONNECTION_CORRECTION_CLIENT_LOOP_SUBMITTED);
            nextState = LLIConnectionConstants.NEW_CONNECTION_CORRECTION_CLIENT_LOOP_SUBMITTED;
        }
        if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL
                && lliNewConnectionApplication.getApplicationType() == LLIConnectionConstants.NEW_CONNECTION) {
//            lliNewConnectionApplication.setState(LLIConnectionConstants.NEW_CONNECTION_CORRECTION_BTCL_LOOP_SUBMITTED);
            nextState = LLIConnectionConstants.NEW_CONNECTION_CORRECTION_BTCL_LOOP_SUBMITTED;
        }

        lliApplicationService.editApplication(lliNewConnectionApplication);
        int officeListSize = lliNewConnectionApplication.getOfficeList().size();
        for (int i = 0; i < officeListSize; i++) {
            officeService.updateOffice(lliNewConnectionApplication.getOfficeList().get(i));
        }
//        lliApplicationService.sendNotification(lliNewConnectionApplication, lliNewConnectionApplication.getState(), loginDTO);


        if (lliNewConnectionApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP ||
                lliNewConnectionApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
        ) {
            if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_CLIENT) {
                lliNewConnectionApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_PORT);
            } else if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL) {
                lliNewConnectionApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);

            }

            int portCount = jsonObject.get("portCount").getAsInt();

            AdditionalPort lliAdditionalPortApplication = lliAdditionalPortService.getAdditionalPortByApplication(lliNewConnectionApplication.getApplicationID());

            if (lliAdditionalPortApplication.getLoopType() == LLIConnectionConstants.REUSE_ADDITIONAL_LOOP
                    || lliAdditionalPortApplication.getLoopType() == LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP
            ) {
                if (portCount > 1) {
                    throw new RequestFailureException("Port Count must be 1 while reuse or replacing existing loop");
                }
            }

            lliAdditionalPortApplication.setPortCount(portCount);
            // TODO: 5/29/2019 if want to change connection and office as well then a significant development work will be needed to address the corrections as it is critically maintained during submission. so for now I am ommiting the parts
            globalService.save(lliAdditionalPortApplication);
        }

        if (lliNewConnectionApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP) {
            int ipCount = jsonObject.get("ipCount").getAsInt();
            LLIAdditionalIP lliAdditionalIP = lliAdditionalIPService.getAdditionalIPByApplication(lliNewConnectionApplication.getApplicationID());
            lliAdditionalIP.setIpCount(ipCount);
            globalService.save(lliAdditionalIP);
        }
        lliApplicationService.setInsertStageComment(lliNewConnectionApplication, loginDTO);

        //        lliApplicationService.updateApplicatonState(lliNewConnectionApplication.getApplicationID(), nextState);

//        lliApplicationService.updateApplicatonState(lliNewConnectionApplication.getApplicationID(), nextState);
        lliNewConnectionApplication.setState(nextState);
        lliApplicationService.updateApplicaton(lliNewConnectionApplication);
        lliApplicationService.sendNotification(lliNewConnectionApplication, lliNewConnectionApplication.getState(), loginDTO);

        return lliNewConnectionApplication.getApplicationID();
    }


    @Transactional
    public void ldgmForwardAfterWO(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;

        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);//added by Jami for new local loop advice note

        int state = jsonObject.get("nextState").getAsInt();

        ArrayList<EFR> lists = new ArrayList<>();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH && lliApplication.isNewLoop())

        ) {
            lists = new EFRDeserializer().deserialize_custom_new_local_loop(jsonElement);
        } else {
            lists = new EFRDeserializer().deserialize_custom(jsonElement);
        }
//
        for (EFR efr : lists) {
            efrService.updateApplicaton(efr);
        }
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH && lliApplication.isNewLoop())


        ) {
            List<NewLocalLoop> localLoops = newlocalLoopService.updateLocalloopAdjustingLength(appID);
            for (NewLocalLoop localLoop : localLoops) {
                newlocalLoopService.updateApplicaton(localLoop);
            }
        } else {
            List<LocalLoop> localLoops = localLoopService.updateLocalloopAdjustingLength(appID);
            for (LocalLoop localLoop : localLoops) {
                localLoopService.updateApplicaton(localLoop);
            }
        }

        int oldZone = lliApplication.getSecondZoneID();
        int currentZone = lliApplication.getZoneId();
        lliApplication.setZoneId(oldZone);
        lliApplication.setSecondZoneID(currentZone);
        lliApplication.setIsForwarded(0);
        lliApplication.setState(state);
        lliApplication.setSkipPayment(lliApplication.getSkipPayment());

        lliApplicationService.updateApplicaton(lliApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
    }


    private void checkIFIFRFromSamePOP(List<IFR> ifrs,LLIApplication lliApplication) throws Exception {


        List<AdditionalPort> additionalPort=globalService.getAllObjectListByCondition(AdditionalPort.class,
                new AdditionalPortConditionBuilder()
                .Where()
                .applicationIdEquals(lliApplication.getApplicationID())
                .getCondition()
                );


        if(additionalPort!=null){
            if(additionalPort.get(0).getLoopType()==LLIConnectionConstants.REUSE_ADDITIONAL_LOOP
                    ||additionalPort.get(0).getLoopType()==LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP){
                List<NewLocalLoop> newLocalLoops=globalService.getAllObjectListByCondition(NewLocalLoop.class,
                        new NewLocalLoopConditionBuilder()
                                .Where()
                                .applicationIDEquals(lliApplication.getApplicationID())
                                .getCondition()
                );

                if(newLocalLoops!=null){
                    if(newLocalLoops.size()>1 ){

                        throw new RequestFailureException("More than one local loop found which is not allowed for replacing or reusing current loop");
                    }else{
                        if(additionalPort.get(0).getLoopType()==LLIConnectionConstants.REUSE_ADDITIONAL_LOOP) {
                            for (IFR ifr : ifrs) {


                                if (ifr.getPopID()==newLocalLoops.get(0).getPopID()){
                                    continue;
                                }else {
                                    throw new RequestFailureException("IFR should be from the same pop for reuse local loop");

                                }

                            }
                        }
                        if(additionalPort.get(0).getLoopType()==LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP) {
                            int possibleIFR=0;
                            for (IFR ifr : ifrs) {
                                if(ifr.getIsSelected()==1){
                                    possibleIFR++;
                                }
                            }

                            if(possibleIFR>1){
                                throw new RequestFailureException("IFR positive Feasibility should not be from more than one POP for replace local loop");

                            }


                            for (IFR ifr : ifrs) {
                                if (ifr.getPopID()==newLocalLoops.get(0).getPopID()){
                                    throw new RequestFailureException("IFR should not be from the same pop for replace local loop");

                                }else {

                                    continue;

                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public void IFRRequestNewLoop(JsonElement jelement, LoginDTO loginDTO) throws Exception {


        JsonObject jsonObject = jelement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);


        ArrayList<IFR> lists = new IFRDeserializer().deserialize_custom_local_loop(jelement);

//        checkIFIFRFromSamePOP(lists,lliApplication);

        for (IFR ifr : lists) {
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.insertApplication(ifr, loginDTO);
            //parent ifr id need to be incorporated
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);//this need to update state
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
    }


}
