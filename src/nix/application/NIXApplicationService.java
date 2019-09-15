package nix.application;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.RoleConstants;
import common.client.ClientDTO;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import common.repository.AllClientRepository;
import exception.NoDataFoundException;
import flow.entity.FlowState;
import flow.entity.Role;
import flow.repository.FlowRepository;
import global.GlobalService;
import inventory.InventoryConstants;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import location.ZoneDAO;
import login.LoginDTO;
import login.LoginService;
import nix.application.close.NIXCloseApplication;
import nix.application.close.NIXCloseApplicationService;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.downgrade.NIXDowngradeApplicationService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.application.upgrade.NIXUpgradeApplicationService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.document.NIXAdviceNote;
import nix.document.NIXWorkOrder;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRConditionBuilder;
import nix.efr.NIXEFRDeserializer;
import nix.efr.NIXEFRService;
import nix.ifr.NIXIFR;
import nix.ifr.NIXIFRDeserializer;
import nix.ifr.NIXIFRService;
import nix.localloop.NIXLocalLoop;
import nix.localloop.NIXLocalLoopService;
import nix.nixflowconnectionmanger.NIXApplicationFlowConnectionManagerService;
import nix.office.NIXOffice;
import nix.office.NIXOfficeService;
import nix.revise.NIXClientTD;
import nix.revise.NIXClientTDConditionBuilder;
import notification.NotificationService;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import officialLetter.ReferType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NIXApplicationService implements NavigationService {

    @DAO
    NIXApplicationDAO nixApplicationDAO;

    @DAO
    ZoneDAO zoneDAO;

    NIXApplicationOfficeService nixApplicationOfficeService = ServiceDAOFactory.getService(NIXApplicationOfficeService.class);
    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    NIXApplicationLocalLoopService nixApplicationLocalLoopService = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);
    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    NIXUpgradeApplicationService nixUpgradeApplicationService = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class);
    NIXIFRService nixifrService = ServiceDAOFactory.getService(NIXIFRService.class);
    NIXDowngradeApplicationService nixDowngradeApplicationService = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class);
    NIXEFRService nixefrService = ServiceDAOFactory.getService(NIXEFRService.class);
    UserService userService =ServiceDAOFactory.getService(UserService.class);;
    NotificationService notificationService=ServiceDAOFactory.getService(NotificationService.class);
    GlobalService globalService=ServiceDAOFactory.getService(GlobalService.class);

    @Service
    NIXApplicationFlowConnectionManagerService nixApplicationFlowConnectionManagerService;

    private static final Logger logger = LoggerFactory.getLogger(NIXApplicationService.class);





    @Transactional
    public void sendNotification(NIXApplication application,int nextState, LoginDTO loginDTO) throws Exception {


        boolean isForClient = false;

        if (loginDTO.getRoleID() > 0) {
            //todo support parallel actions

            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), loginDTO.getRoleID(), false,loginDTO.getUserID());
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), RoleConstants.LDGM_ROLE, false,loginDTO.getUserID());

        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), loginDTO.getAccountID(), false,loginDTO.getUserID());

        }

        HashMap<Long, Role> uniqueRoles = notificationService.getNextStateRoleListForNotification(nextState);
        if (uniqueRoles.isEmpty()) {
            isForClient = true;
            notificationService.saveNotificationForNextStateUser(
                    isForClient,
                    nextState,
                    application.getClient(),
                    -1,
                    ModuleConstants.Module_ID_NIX,
                    application.getId(),

                    "NIX Application",
                    NIXConstants.NIX_DETAILS_PAGE_URL,
                    false,
                    0
            );

        } else {
            Iterator it = uniqueRoles.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Role role = (Role) pair.getValue();
                Set<UserDTO> userDTOS = UserRepository.getInstance().getUsersByRoleID(role.getId());
                List<Long> userIdsForNotification = new ArrayList<>();

                for (UserDTO userDTO : userDTOS
                ) {
                    if (userDTO.getRoleID() == RoleConstants.LDGM_ROLE) {
                        ArrayList<Integer> zones = zoneDAO.getUserZone((int) userDTO.getUserID());
                        if (application.getZone() > 0) {

                            if (zones.contains(application.getZone())) {
                                userIdsForNotification.add(userDTO.getUserID());
                            }
                        }

                    }else if( userDTO.getRoleID()==RoleConstants.VENDOR_ROLE){
                        List<NIXEFR> efrs=globalService.getAllObjectListByCondition(NIXEFR.class,
                                new NIXEFRConditionBuilder()
                                        .Where()
                                        .vendorEquals(userDTO.getUserID())
                                        .applicationEquals(application.getId())
                                        .getCondition()
                        );
                        if(efrs.size()>0){
                            userIdsForNotification.add(efrs.get(0).getVendor());
                        }

                    } else {

                        userIdsForNotification.add(userDTO.getUserID());


                    }
                }
                for (Long userId : userIdsForNotification)
                {
                    notificationService.saveNotificationForNextStateUser(
                            isForClient,
                            nextState,
                            application.getClient(),
                            userId,
                            ModuleConstants.Module_ID_NIX,
                            application.getId(),

                            "NIX Application",
                            NIXConstants.NIX_DETAILS_PAGE_URL,
                            false,
                            0
                    );

                }
            }

        }


    }


    @Transactional
    void generateAdviceNoteDocument(long appId, int state, JsonArray userArray, long senderId,LoginDTO loginDTO) throws Exception {

        // TODO: 12/19/2018 add appropriate class and function to generate advice note 
       /* ConnectionAdviceNote adviceNote = generateAN(userArray, state, appId, senderId);
        //create pdf
        AsyncPdfService.getInstance().accept(adviceNote);*/


        NIXApplication nixApplication = getApplicationById(appId);

        nixApplication.setState(state);
        updateApplicaton(nixApplication);
        sendNotification(nixApplication,state,loginDTO);

        List<RecipientElement> recipientElements = officialLetterService.getAllCCAndToList(userArray, state);
        NIXAdviceNote nixAdviceNote = new NIXAdviceNote(nixApplication.getId(), nixApplication.getClient());
        officialLetterService.saveOfficialLetterTransactionalIndividual(nixAdviceNote, recipientElements, senderId);
        AsyncPdfService.getInstance().accept(nixAdviceNote);
    }



    @Transactional
    private void workOrderTask(int appType, long appId, ClientDTO clientDTO, UserDTO loggedInUserDTO,
                               List<NIXEFR> efrs, UserDTO vendorUserDTO, List<RecipientElement> recipientElements)
            throws Exception{


        NIXWorkOrder nixWorkOrder = new NIXWorkOrder(appId, clientDTO.getClientId(), vendorUserDTO.getUserID(), efrs);
        officialLetterService.saveOfficialLetterTransactionalIndividual( nixWorkOrder, recipientElements, loggedInUserDTO.getUserID() );
        AsyncPdfService.getInstance().accept(nixWorkOrder);

    }

    @Transactional
    public boolean setDemandNote(long appId, long dnId) throws Exception{
        boolean status = false;
        try {
            // TODO: set demand note id in nix application
            NIXApplication nixApplication= getApplicationById(appId);
            nixApplication.setDemandNote(dnId);
            updateApplicaton(nixApplication);
            status = true;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
        return status;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXApplication getApplicationById(long appId)throws Exception {
        NIXApplication nixApplication = nixApplicationDAO.getApplicationById(appId);
        if(nixApplication == null){
            throw new RequestFailureException("No NIX Application Found with id " + appId);
        }
        List<NIXApplicationOffice> list = nixApplicationOfficeService.getOfficesByApplicationId(appId);
        nixApplication.setNixApplicationOffices(list);

        return nixApplication;
    }

    @Transactional
    public void insert(NIXApplication nixApplicationObject) throws Exception{
        nixApplicationDAO.insert(nixApplicationObject);
    }

    String viewNewApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getFlowApplicationByApplicationID(applicationID).getType();
        return "nix-application-new-connection-view";
    }

    @Transactional(transactionType = TransactionType.READONLY)
    NIXApplication getFlowApplicationByApplicationID(long applicationID) throws Exception {
        NIXApplication nixApplication = nixApplicationDAO.getFlowApplicationByID(applicationID);
        if (nixApplication == null) {
            throw new RequestFailureException("No Application found with ID " + applicationID);
        }
        return nixApplication;
    }

    @SuppressWarnings("rawtypes")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {

        return nixApplicationDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//		return lliApplicationDAO.getLLIApplicationListByIDList((List<Long>) recordIDs);
        List<NIXApplication> list = nixApplicationDAO.getApplicationListByIDList((List<Long>) recordIDs);
        LoginDTO loginDTO = (LoginDTO) objects[0];

        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];

        int roleID = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);
        Map<Integer, FlowState> flowState = FlowRepository.getInstance().getActionableFlowStatesByRoleId(roleID)
                .stream()
                .distinct()
                .collect(Collectors.toMap(FlowState::getId, Function.identity()));

        Comparator<NIXApplication> compareByHasPermissionAndApplicationId =
                Comparator.comparing(NIXApplication::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(NIXApplication:: getId, Comparator.reverseOrder());


        return list.stream()
                .peek(t-> {
                    FlowState flowState1 = flowState.getOrDefault(t.getState(), null);
                    t.setHasPermission(null != flowState1);
                    FlowState state = FlowRepository.getInstance().getFlowStateByFlowStateId(t.getState());
                    t.setStateDescription(state.getViewDescription());
                    t.setColor(state.getColor());
                }) .sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());


    }

    @Transactional
    public void updateApplicaton(NIXApplication nixApplication) throws Exception {
        nixApplicationDAO.updateApplicaton(nixApplication);
    }

    @Transactional
    public void updateApplicatonState(long applicatonID, int applicationState) throws Exception {
        nixApplicationDAO.updateApplicatonState(applicatonID, applicationState);
    }
    @Transactional
    int getApplicationTypeByApplicationID(long applicationID) throws Exception {
        NIXApplication nixApplication = getApplicationById(applicationID);
        return nixApplication.getType();
    }

    void generateWorkOrderDocuments(long applicationId, long loggedInUserId) throws Exception{
        try {
            ClientDTO clientDTO = clientService.getClientByNIXApplicationId(applicationId);
            UserDTO userDTO = userService.getUserDTOByUserID(loggedInUserId);
            int applicationType = getApplicationTypeByApplicationID(applicationId);

            List<NIXEFR> efrs;
//			if(applicationType == LLIConnectionConstants.CLOSE_CONNECTION){
//				efrs = efrService.getEFRsToClose(applicationId);
//			}else{
            efrs = nixefrService.getSelected(applicationId);
//			}

            Map<Long, List<NIXEFR>> efrMap = new HashMap<>();
            long tempVendorId;
            List<NIXEFR> tempEFRList;
            if(efrs != null){
                for(NIXEFR efr: efrs){
                    tempVendorId = efr.getVendor();
                    tempEFRList = efrMap.get(tempVendorId);
                    if(tempEFRList == null){
                        tempEFRList = new ArrayList<>();
                    }
                    tempEFRList.add(efr);
                    efrMap.put(tempVendorId, tempEFRList);
                }
            }

            UserDTO vendorUserDTO;
            Iterator<Long> itr = efrMap.keySet().iterator();
            while (itr.hasNext()){
                tempVendorId = itr.next();
                vendorUserDTO = userService.getUserDTOByUserID(tempVendorId);

                if(vendorUserDTO != null){
                    UserDTO cdgmUserDTO = userService.getCDGMUserDTO();
//					UserDTO clientUserDTO = userService.getUserDTOByUserID(clientDTO.getUserId());
//					 clientDTO = AllClientRepository.getInstance().getClientByClientID(clientUserId);
                    common.ClientDTO clientDTO2 = AllClientRepository.getInstance().getClientByClientID(clientDTO.getClientId());
                    List<RecipientElement> recipientElements = new ArrayList<>();
                    recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(vendorUserDTO, ReferType.TO));
                    if (userDTO != null)recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(userDTO, ReferType.CC));
                    if (clientDTO != null)recipientElements.add(RecipientElement.getRecipientElementFromClientAndReferType(clientDTO2, ReferType.CC));
                    if (cdgmUserDTO != null)recipientElements.add(RecipientElement.getRecipientElementFromUserAndReferType(cdgmUserDTO, ReferType.CC));

                    workOrderTask(applicationType, applicationId, clientDTO, userDTO, efrMap.get(tempVendorId), vendorUserDTO, recipientElements);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Transactional(transactionType = TransactionType.READONLY)
    public NIXApplication getNIXApplicationByDemandNoteId(long id) throws Exception {
        NIXApplication nixApplication =  nixApplicationDAO.getNIXApplicationsByDemandNoteId(id)
                .stream()
                .findFirst()
                .orElseThrow(()->new NoDataFoundException("No NIX Application Found with Demand Note id " + id));

        List<NIXApplicationOffice> nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixApplication.getId());
        nixApplication.setNixApplicationOffices(nixApplicationOffices);
        return  nixApplication;
    }

/*

    @Transactional
    public void handleCompleteConnection(
            NIXApplication nixApplication,
            JsonElement jsonElement,
            String connectionName)throws Exception {
        NIXConnection nixConnection = new NIXConnection();
        nixConnection.setApplication(nixApplication.getId());
        nixConnection.setStatus(1);
        nixConnection.setName(connectionName);
        nixConnection.setClient(nixApplication.getClient());
        nixConnection.setValidFrom(System.currentTimeMillis());
        nixConnection.setValidTo(Long.MAX_VALUE);
        nixConnection.setActiveFrom(System.currentTimeMillis());
        nixConnection.setActiveTo(Long.MAX_VALUE);
        nixConnection.setStartDate(System.currentTimeMillis());
        nixConnection.setIncidentId(NIXConstants.NEW_CONNECTION_APPLICATION);
        nixConnectionService.insertConnection(nixConnection);
        if(nixApplication.getType() ==NIXConstants.NEW_CONNECTION_APPLICATION){
            nixConnection.setConnectionId(nixConnection.getId());
            nixConnectionService.updateConnection(nixConnection);
        }
        //save port vlan informations
        List<NIXApplicationLocalLoop> localLoops = new NIXApplicationLocalLoop().deserialize_custom(jsonElement);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            nixApplicationLocalLoopService.updateApplicaton(localLoop);
        }

        List<NIXApplicationOffice>nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixApplication.getId());

        //insert new office
        for(NIXApplicationOffice nixApplicationOffice:nixApplicationOffices){
            List<NIXApplicationLocalLoop> nixLocalLoops = nixApplicationOffice.getLoops();
            NIXOffice nixOffice = new NIXOffice();
            nixOffice.setAppication(nixApplication.getId());
            nixOffice.setConnection(nixConnection.getId());
            nixOffice.setName(nixApplicationOffice.getName());
            nixOffice.setApplication_offfice(nixApplicationOffice.getId());
            nixOfficeService.insertOffice(nixOffice);
            //insert new local loop
            for(NIXApplicationLocalLoop nixApplicationLocalLoop:nixLocalLoops){
                NIXLocalLoop nixLocalLoop = new NIXLocalLoop();
                nixLocalLoop.setApplicationLocalLoop(nixApplication.getId());
                nixLocalLoop.setConnection(nixConnection.getId());
                nixLocalLoop.setOffice(nixOffice.getId());
                nixLocalLoopService.insertLocalLoop(nixLocalLoop);
            }
        }
    }
*/

    @Transactional
    void insertUpgradeApplication(NIXApplication nixApplicationObject,
                                         NIXUpgradeApplication nixUpgradeApplication,
                                         LoginDTO loginDTO)throws Exception {

        nixUpgradeApplication.setParent(nixApplicationObject.getId());
        nixUpgradeApplicationService.insert(nixUpgradeApplication);

        Comments comments = new Comments();
        comments.setSubmissionDate(nixApplicationObject.getSubmissionDate());
        comments.setStateID(nixApplicationObject.getState());
        comments.setComments(nixApplicationObject.getComment());
        comments.setApplicationID(nixApplicationObject.getId());
        commentsService.insertComments(comments, loginDTO);
    }

    @Transactional
    void upgradeBatchOperation(JsonElement jelement,int state,long appID,LoginDTO loginDTO) throws Exception{
        ArrayList<NIXIFR> ifrArrayList = new NIXIFRDeserializer().deserialize_custom(jelement);
        NIXApplication nixApplication = getApplicationById(appID);
        if (
                nixApplication.getLoopProvider() == NIXConstants.LOOP_PROVIDER_CLIENT||
                nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION||
                nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION
        ){
            state = NIXConstants.WITHOUT_LOOP_IFR_RESPONDED_STATE;
        }
        nixifrService.updateIFR(ifrArrayList, nixApplication.getId(), state);
        updateApplicatonState(appID, state);
        sendNotification(nixApplication,state,loginDTO);
    }

    @Transactional
     void insertDowngradeApplication(NIXApplication nixApplicationObject,
                                           NIXDowngradeApplication nixDowngradeApplication,
                                           LoginDTO loginDTO) throws Exception{

        nixDowngradeApplication.setParent(nixApplicationObject.getId());
        nixDowngradeApplicationService.insert(nixDowngradeApplication);

        Comments comments = new Comments();
        comments.setSubmissionDate(nixApplicationObject.getSubmissionDate());
        comments.setStateID(nixApplicationObject.getState());
        comments.setComments(nixApplicationObject.getComment());
        comments.setApplicationID(nixApplicationObject.getId());
        commentsService.insertComments(comments, loginDTO);
    }

    @Transactional
    public void editApplication(NIXApplication nixApplication,int nextState,JsonObject jsonObject,LoginDTO loginDTO) throws Exception {
        int officeListSize = nixApplication.getNixApplicationOffices().size();
        for (int i = 0; i < officeListSize; i++) {
            NIXApplicationOffice nixApplicationOffice = nixApplication.getNixApplicationOffices().get(i);
            globalService.update(nixApplicationOffice);
        }
        NIXApplication lastLLIApplicationInstance = getApplicationById(nixApplication.getId());
        if(lastLLIApplicationInstance.getType()==NIXConstants.NIX_UPGRADE_APPLICATION
                ||lastLLIApplicationInstance.getType()==NIXConstants.NIX_DOWNGRADE_APPLICATION){


            int newPort=0;
            if(jsonObject.has("selectedNewPort")) {
                 newPort = jsonObject.get("selectedNewPort").getAsJsonObject().get("ID").getAsInt();
                 if(newPort<=0){
                     throw new RequestFailureException("Unable to add new port. Try again!");

                 }
            }

            if(lastLLIApplicationInstance.getType() == NIXConstants.NIX_UPGRADE_APPLICATION){
                NIXUpgradeApplication nixUpgradeApplication = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class).getApplicationByParent(lastLLIApplicationInstance.getId());
                nixUpgradeApplication.setNewPortType(newPort);
                globalService.update(nixUpgradeApplication);
            }
            else{
                NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class).getApplicationByParent(lastLLIApplicationInstance.getId());
                nixDowngradeApplication.setNewPortType(newPort);
                globalService.update(nixDowngradeApplication);
            }
        }
        else {
            if(jsonObject.has("selectedPort")) {

                int portType=0;
                if(jsonObject.get("selectedPort").isJsonObject()) {
                    portType = jsonObject.get("selectedPort").getAsJsonObject().get("ID").getAsInt();
                }

                if( portType<1){
                    throw new RequestFailureException(" Invalid Port Type");
                }

                int portCount=0;
                if(jsonObject.has("portCount")) {
                     portCount = jsonObject.get("portCount").getAsInt();
                     if(portCount<=0){
                         throw new RequestFailureException("Port Count should pe positive");
                     }
                }
                lastLLIApplicationInstance.setPortType(portType);
                lastLLIApplicationInstance.setPortCount(portCount);
            }
        }
        setImmutablePropertiesForApplicationEdit(nixApplication, lastLLIApplicationInstance);
        nixApplication.setStatus(NIXConstants.STATUS_APPLIED);
        nixApplication.setState(nextState);
        updateApplicaton(nixApplication);
        sendNotification(nixApplication,nextState,loginDTO);
    }

    private void setImmutablePropertiesForApplicationEdit(NIXApplication nixApplication,
                                                          NIXApplication lastExistingApplication) {

        nixApplication.setId(lastExistingApplication.getId());
        nixApplication.setClient(lastExistingApplication.getClient());
        //nixApplication.setUser(lastExistingApplication.getUserID());
        // nixApplication.setContent(lastExistingApplication.getContent());
        nixApplication.setSubmissionDate(lastExistingApplication.getSubmissionDate());
        nixApplication.setType(lastExistingApplication.getType());
        nixApplication.setDemandNote(lastExistingApplication.getDemandNote());
        nixApplication.setPortType(lastExistingApplication.getPortType());
        nixApplication.setPortCount(lastExistingApplication.getPortCount());
        // nixApplication.setDemandNoteNeeded(lastExistingApplication.isDemandNoteNeeded);
        nixApplication.setIsServiceStarted(lastExistingApplication.getIsServiceStarted());
        // nixApplication.setComment(lastExistingApplication.getComment());
        // nixApplication.setRequestForCorrectionComment(lastExistingApplication.getRequestForCorrectionComment());

    }

    @Transactional
    public void efrInsert(JsonElement jelement,LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        JsonElement jsonElement = jsonObject.getAsJsonArray("ifr");
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        ArrayList<NIXIFR> list1 = new NIXIFRDeserializer().deserialize_custom_ifr_update(jelement);

        ArrayList<NIXIFR> selectedIFR = new ArrayList<>();
        for (NIXIFR ifr : list1) {
            if (ifr.getSelected() == 1) {
                selectedIFR.add(ifr);
            } else {
                ifr.setSelected(NIXConstants.IFR_IGNORED);
            }
            ifr.setIsForwarded(1);
            ifr.setReplied(1);
            ifr.setLastModificationTime(System.currentTimeMillis());
            nixifrService.updateApplicaton(ifr);
        }
        ArrayList<NIXEFR> lists = new NIXEFRDeserializer().deserialize_custom(jelement);
        for (NIXEFR efr : lists) {
            efr.setIsForwarded(1);
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.insertApplication(efr);
        }

        updateApplicatonState(appID, state);//this need to update state
        sendNotification(nixApplication,state,loginDTO); // jami
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
    }

    @Transactional
    public void updateEFR(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        ArrayList<NIXEFR> lists = new ArrayList<>();
        lists = new NIXEFRDeserializer().deserialize_custom(jelement);
        NIXApplication nixApplication = getApplicationById(appID);
        for (NIXEFR efr : lists) {
            //efr.setIsForwarded(nixApplication.getIsForwarded());
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
            sendNotification(nixApplication,state,loginDTO);//jami

        }
        // TODO: 12/18/2018 efr check is not needed now if needed we will use it later
        //updateApplicatonState(appID, state);
        efrCheck(appID, state);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
    }
    private void efrCheck(long applicationID, int state) throws Exception {
        List<NIXEFR> efrArrayList = nixefrService.getIncomleteEFRByAppID(applicationID);
        if (efrArrayList.size() == 0) {
            updateApplicatonState(applicationID, state);
        }
        else {
            for (NIXEFR efr : efrArrayList) {
               /* if (System.currentTimeMillis() > efr.getQuotationDeadline()) {
                    efr.setQuotationStatus(2);*/
                efr.setIsSelected(1);
                nixefrService.updateApplicaton(efr);
                // }
            }
        }
        List<NIXEFR> efrArrayList2 = nixefrService.getIncomleteEFRByAppID(applicationID);
        if (efrArrayList2.size() == 0) {
            updateApplicatonState(applicationID, state);
        }

    }

    @Transactional
    public void demandNote(JsonElement jsonElement, LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        nixApplication.setSkipPayment(0);
        updateApplicaton(nixApplication);
        ArrayList<NIXEFR> lists = new ArrayList<>();
        lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        for (NIXEFR efr : lists) {
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
        }
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopService.getLocalLoopByAppId(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            nixApplicationLocalLoopService.updateApplicaton(localLoop);
        }
    }


    @Transactional
    public void updateLocalLopps(List<NIXApplicationLocalLoop> localLoops,long clientId)throws Exception{
        localLoops.forEach(localLoop1 -> {
            try {
                long portId = localLoop1.getPortId();
                long application = localLoop1.getApplication();
                NIXApplication nixApplication = ServiceDAOFactory.getService(
                        NIXApplicationService.class).getApplicationById(application);
                localLoop1.setPortType(nixApplication.getPortType());
                nixApplicationLocalLoopService.updateApplicaton(localLoop1);
            } catch (Exception e) {
                throw new RequestFailureException("Error updating local loop " + localLoop1.toString());
            }
        });
    }

    @Transactional
    public void handleConnectionCompleteRequest(JsonElement jsonElement,
                                                LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if(jsonObject.has("connectionName")) {
            if (jsonObject.get("connectionName").getAsString().equals("")) {
                throw new RequestFailureException("Connection Name is Mandatory :");
            }
        }

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        List<NIXApplicationLocalLoop> localLoops = new NIXApplicationLocalLoop().deserialize_custom(jsonElement);
//
//        for (NIXApplicationLocalLoop localLoop:localLoops
//             ) {
//
//            if(localLoop.getPortId()<=0){
//                throw new RequestFailureException("Port must be provided");
//            }
//
//        }

        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID")!=null?appType.get("ID").getAsLong():0;
        NIXApplication nixApplication = getApplicationById(appID);
        updateLocalLopps(localLoops,nixApplication.getClient());
        nixApplicationFlowConnectionManagerService.connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        ServiceDAOFactory.getService(CommentsService.class).insertComments(comments, loginDTO);
        updateApplicatonState(appID, state);
    }

    @Transactional
    public void handleConnectionCompleteRequestUpgrade(JsonElement jsonElement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID")!=null?appType.get("ID").getAsLong():0;
        List<NIXApplicationLocalLoop> localLoops = new NIXApplicationLocalLoop().deserialize_custom(jsonElement);
        NIXApplication nixApplication = getApplicationById(appID);

        for (NIXApplicationLocalLoop nixApplicationLocalLoop:localLoops
             ) {
            if(nixApplicationLocalLoop.getId()>0){
                NIXApplicationLocalLoop tempLoop=globalService.findByPK(NIXApplicationLocalLoop.class,nixApplicationLocalLoop.getId());
                nixApplicationLocalLoop.setBtclDistance(tempLoop.getBtclDistance());
                nixApplicationLocalLoop.setOcdDistance(tempLoop.getOcdDistance());
                nixApplicationLocalLoop.setClientDistance(tempLoop.getClientDistance());
            }

        }
        updateLocalLopps(localLoops,nixApplication.getClient());
        nixApplicationFlowConnectionManagerService.connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        ServiceDAOFactory.getService(CommentsService.class).insertComments(comments, loginDTO);
        updateApplicatonState(appID,state);
    }

    @Transactional
    public void generateAN(JsonElement jsonElement,LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        NIXApplication nixApplication = getApplicationById(appID);//added by Jami for new local loop advice note
        int state = jsonObject.get("nextState").getAsInt();
        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();
        JsonArray userArray = jsonObject.getAsJsonArray("userList");
        preprocessingBeforeGeneratingAdviceNote(jsonElement, appID, state, loginDTO);
        generateAdviceNoteDocument(appID, state, userArray, senderId,loginDTO);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void preprocessingBeforeGeneratingAdviceNote(JsonElement jsonElement, long appID, int state, LoginDTO loginDTO) throws Exception {
        ArrayList<NIXEFR> lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);
        for (NIXEFR efr : lists) {
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
        }
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopService.
                getLocalLoopsAdjustedWithActualDistanceByApplicationId(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            nixApplicationLocalLoopService.updateApplicaton(localLoop);
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        updateApplicatonState(appID, state);
    }

    @Transactional
    public void withoutLoopDemandNoteBatchOperation(JsonElement jsonElement, LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement ifrElement = jsonObject.get("ifr");
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        nixApplication.setSkipPayment(0);
        updateApplicaton(nixApplication);
        List<NIXIFR> ifrs = new ArrayList<>();
        ifrs = new NIXIFRDeserializer().deserialize_custom(jsonElement);
        ArrayList<NIXIFR> selectedIFR = new ArrayList<>();
        for (NIXIFR ifr : ifrs) {
            if (ifr.getSelected() == 1) {
                selectedIFR.add(ifr);
            }
            ifr.setIsForwarded(nixApplication.getIsForwarded());
            ifr.setLastModificationTime(System.currentTimeMillis());
            nixifrService.updateApplicaton(ifr);
        }
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopService.prepareLocalloopFromIFR(selectedIFR);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            nixApplicationLocalLoopService.insertApplication(localLoop);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
    }

    @Transactional
    public void completeWOBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        ArrayList<NIXEFR> lists = new ArrayList<>();
        NIXApplication nixApplication = getApplicationById(appID);
        lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);

        for (NIXEFR efr : lists) {
            if (efr.getActualDistance() <= 0) {
                efr.setActualDistance(efr.getActualDistance());
                // TODO: 12/18/2018 the below thing is not used for my case check later if needed or not
                //efr.setDistanceIsApproved(1);
            }
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
            sendNotification(nixApplication,state,loginDTO);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        WOCheck(appID, state, loginDTO.getUserID());
    }

    private void WOCheck(long applicationID, int state, long vendorId) throws Exception {
        List<NIXEFR> efrArrayList = nixefrService.getNotCompletedWOByVendor(applicationID, vendorId);

        for (NIXEFR efr : efrArrayList) {
            //todo: expire policy
            efr.setWorkCompleted(NIXConstants.EFR_WORK_DONE);
            nixefrService.updateApplicaton(efr);
        }
        List<NIXEFR> efrArrayList2 = nixefrService.getNotCompletedWO(applicationID);
        if (efrArrayList2.size() == 0) {
            updateApplicatonState(applicationID, state);
        }
    }

    @Transactional
    public void ifrUpdateBatchOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        //this need to update state
        ArrayList<NIXIFR> ifrArrayList = new NIXIFRDeserializer().deserialize_custom(jelement);
        nixifrService.updateIFR(ifrArrayList, appID, state);
        NIXApplication nixApplication = getApplicationById(appID);

        if (nixApplication.getLoopProvider() == NIXConstants.LOOP_PROVIDER_CLIENT){
            state = NIXConstants.WITHOUT_LOOP_IFR_RESPONDED_STATE;
        }
        ArrayList<NIXIFR> selectedIFR = new ArrayList<>();
        for (NIXIFR ifr : ifrArrayList) {
            if (ifr.getSelected() == 1) {
                selectedIFR.add(ifr);
            }
        }
        List<NIXApplicationLocalLoop> localLoops = nixApplicationLocalLoopService.prepareLocalloopFromIFR(selectedIFR);

        for (NIXApplicationLocalLoop localLoop : localLoops) {
            localLoop.setApplication(nixApplication.getId());
            nixApplicationLocalLoopService.insertApplication(localLoop);
        }
        updateApplicatonState(appID, state);
        sendNotification(getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
    }


    @Transactional
    private void checkForTD(long clientID) throws Exception {
        List<NIXClientTD> nixClientTD=globalService.getAllObjectListByCondition(NIXClientTD.class,
                new NIXClientTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .tdToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );


        if (nixClientTD!=null&&nixClientTD.size()>0){
            throw new RequestFailureException(" Temporary Disconnected client can not submit connection application. Please Reconnect first");
        }
    }

    @Transactional
    public void insertBatchOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        NIXApplication nixApplicationObject = new NIXApplication().desirializer(jsonObject);

        checkForTD(nixApplicationObject.getClient());
        nixApplicationObject.setState(NIXConstants.NIX_NEW_CONNECTION_STATE);
        nixApplicationObject.setType(NIXConstants.NEW_CONNECTION_APPLICATION);
//        if (ServiceDAOFactory.getService(NIXClientTDService.class).
//                isClientTemporarilyDisconnected(nixApplicationObject.getClient())) {
//            throw new RequestFailureException("Temporary disconnected client cannot apply for new connection.");
//        }

        insert(nixApplicationObject);

        for(NIXApplicationOffice nixApplicationOffice: nixApplicationObject.getNixApplicationOffices()){
            nixApplicationOffice.setApplication(nixApplicationObject.getId());
            nixApplicationOfficeService.insertOffice(nixApplicationOffice);
            nixApplicationOffice.setHistory(nixApplicationOffice.getId());
            globalService.update(nixApplicationOffice);
        }

        Comments comments = new Comments();
        comments.setSubmissionDate(nixApplicationObject.getSubmissionDate());
        comments.setStateID(nixApplicationObject.getState());
        comments.setComments(nixApplicationObject.getComment());
        comments.setApplicationID(nixApplicationObject.getId());
        comments.setUserID(loginDTO.getUserID());
        commentsService.insertComments(comments, loginDTO);
        sendNotification(nixApplicationObject,NIXConstants.NIX_NEW_CONNECTION_STATE,loginDTO);
        // return id;
    }

    @Transactional
    public int closeEFRGeneate(NIXApplication nixApplication,int state,LoginDTO loginDTO) throws Exception {
        List<NIXApplication> nixApplications = getApplicationByConnectionID(nixApplication.getConnection());
        int efrSize=0;
        for (NIXApplication nixApplication1 : nixApplications) {
            if (nixApplication1.getId() != nixApplication.getId()) {
                List<NIXEFR> efrs = ServiceDAOFactory.getService(NIXEFRService.class).getHistoricalCompletedEFR(nixApplication1.getId());
                if (efrs.size() > 0) {
                    efrSize+=efrs.size();
                    for (NIXEFR efr : efrs) {
                        NIXEFR newEFR = efr;
                        newEFR.setApplication(nixApplication.getId());
                        newEFR.setParentEFR(efr.getId());
                        newEFR.setProposedDistance(efr.getActualDistance());
                        newEFR.setWorkCompleted(0);
                        // newEFR.setState(state);
                        ServiceDAOFactory.getService(NIXEFRService.class).insertApplication(efr);
                    }
                }
            }
        }
        return efrSize;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    private List<NIXApplication> getApplicationByConnectionID(long connection)throws Exception {
        List<NIXApplication> nixApplications = nixApplicationDAO.getLLIApplicationByConnectionID(connection);
        if (nixApplications == null) {
            throw new RequestFailureException("No Application found with connection ID " + connection);
        }
        return nixApplications;
    }

    @Transactional
    public void closeWOBatchOperation(JsonElement jsonElement, LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        int efrSize = closeEFRGeneate(nixApplication, state, loginDTO);
        generateWorkOrderDocuments(appID, loginDTO.getUserID());
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        updateApplicatonState(appID, state);
        sendNotification(nixApplication,state,loginDTO);
    }

    @Transactional
    public void closeConnectionCompleteBatchOperation(JsonElement jsonElement, LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        long conID = jsonObject.get("application").getAsJsonObject().get("connection").getAsJsonObject().get("id")==null?-1:
                jsonObject.get("application").getAsJsonObject().get("connection").getAsJsonObject().get("id").getAsLong();

        NIXApplication nixApplication = getApplicationById(appID);
        NIXCloseApplication nixCloseApplication = ServiceDAOFactory.
                getService(NIXCloseApplicationService.class).getApplicationByParent(appID);
        NIXConnection oldConnection = ServiceDAOFactory.getService(NIXConnectionService.class).
                getConnnectionByConnectionId(nixApplication.getConnection());
        oldConnection.setActiveTo(System.currentTimeMillis());
        ServiceDAOFactory.getService(NIXConnectionService.class).updateConnection(oldConnection);
        //insert new connection
        NIXConnection newConnection = new NIXConnection();
        newConnection.setActiveTo(Long.MAX_VALUE);
        newConnection.setActiveFrom(System.currentTimeMillis());
        newConnection.setValidFrom(oldConnection.getValidFrom());
        newConnection.setValidTo(Long.MAX_VALUE);
        newConnection.setApplication(nixApplication.getId());
        newConnection.setIncidentId(NIXConstants.NIX_CLOSE_PORT);
        newConnection.setNixOffices(oldConnection.getNixOffices());
        newConnection.setName(oldConnection.getName());
        newConnection.setClient(oldConnection.getClient());
        newConnection.setStartDate(System.currentTimeMillis());
        newConnection.setConnectionId(oldConnection.getConnectionId());
        ServiceDAOFactory.getService(NIXConnectionService.class).insertConnection(newConnection);
        //insert new office and local loop
        NIXApplicationLocalLoop nixApplicationLocalLoop = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).
                getLocalLoopByPort(nixCloseApplication.getPortId());
        NIXLocalLoop nixLocalLoop = ServiceDAOFactory.getService(NIXLocalLoopService.class).
                getLocalLoopByApplicationID(nixApplicationLocalLoop.getId());
        List<NIXOffice>nixOffices = ServiceDAOFactory.getService(NIXOfficeService.class).
                getOfficesByConnectionID(oldConnection.getId());
        boolean isClose=true;
        for(NIXOffice nixOffice:nixOffices){
            NIXOffice newOffice = new NIXOffice();
            newOffice.setAppication(nixApplication.getId());
            newOffice.setConnection(newConnection.getId());
            newOffice.setName(nixOffice.getName());
            newOffice.setApplication_offfice(nixOffice.getApplication_offfice());
            ServiceDAOFactory.getService(NIXOfficeService.class).insertOffice(newOffice);
            for(NIXLocalLoop nixLocalLoopOld:nixOffice.getLocalLoops()){
                if(nixLocalLoopOld.getId()!=nixLocalLoop.getId()) {
                    isClose =false;
                    NIXLocalLoop newLocalLoop = new NIXLocalLoop();
                    newLocalLoop.setConnection(newConnection.getId());
                    newLocalLoop.setOffice(nixLocalLoopOld.getId());
                    newLocalLoop.setApplicationLocalLoop(nixLocalLoopOld.getId());
                    ServiceDAOFactory.getService(NIXLocalLoopService.class).insertLocalLoop(newLocalLoop);
                }
            }
        }
        if(isClose) {
            newConnection.setActiveTo(System.currentTimeMillis());
            newConnection.setIncidentId(NIXConstants.NIX_CLOSE_APPLICATION);
            ServiceDAOFactory.getService(NIXConnectionService.class).updateConnection(newConnection);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        updateApplicatonState(appID, state);
    }

    @Transactional
    public void downgradeBatchOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        NIXApplication nixApplicationObject = new NIXApplication().desirializer(jsonObject);
        long connectionId =jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong();

        long nixOfficeId = jsonObject.get("office").getAsJsonObject().get("ID").getAsLong();
        NIXOffice nixOffice = ServiceDAOFactory.getService(NIXOfficeService.class).
                getOfficeById(nixOfficeId);

        NIXApplicationOffice nixApplicationOfficeOld = ServiceDAOFactory.getService(NIXApplicationOfficeService.class)
                .getOfficeById(nixOffice.getApplication_offfice());


        NIXDowngradeApplication nixDowngradeApplication = new NIXDowngradeApplication().desirializer(jsonObject);
        NIXConnection nixConnection =ServiceDAOFactory.getService(NIXConnectionService.class).
                getConnnectionByConnectionId(connectionId);

        nixApplicationObject.setZone(nixConnection.getZone());
        nixApplicationObject.setState(NIXConstants.NIX_DOWNGRADE_STATE);
        nixApplicationObject.setConnection(nixConnection.getConnectionId());
        nixApplicationObject.setType(NIXConstants.NIX_DOWNGRADE_APPLICATION);
        nixApplicationObject.setPortCount(1);
        insert(nixApplicationObject);
        insertDowngradeApplication(nixApplicationObject,nixDowngradeApplication,loginDTO);
        nixApplicationOfficeOld.setApplication(nixApplicationObject.getId());
        globalService.save(nixApplicationOfficeOld);

        for(NIXApplicationLocalLoop nixApplicationLocalLoop:nixApplicationOfficeOld.getLoops()){
            if(nixApplicationLocalLoop.getPortId() == nixDowngradeApplication.getOldPortId()){
                nixApplicationLocalLoop.setApplication(nixApplicationObject.getId());
                nixApplicationLocalLoop.setOfficeId(nixApplicationOfficeOld.getId());
                globalService.save(nixApplicationLocalLoop);
                return;
            }
        }

        sendNotification(nixApplicationObject,NIXConstants.NIX_DOWNGRADE_STATE,loginDTO);
    }

    @Transactional
    public void upgradeInsertBatchOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        NIXApplication nixApplicationObject = new NIXApplication().desirializer(jsonObject);
        long connectionId =jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong();

        long nixOfficeId = jsonObject.get("office").getAsJsonObject().get("ID").getAsLong();
        NIXOffice nixOffice = ServiceDAOFactory.getService(NIXOfficeService.class).
                getOfficeById(nixOfficeId);

        NIXApplicationOffice nixApplicationOffice = ServiceDAOFactory.getService(NIXApplicationOfficeService.class).getOfficeById(nixOffice.getApplication_offfice());
        //NIXApplicationOffice nixApplicationOffice = nixOffice.getNixApplicationOffice();


        NIXUpgradeApplication nixUpgradeApplication = new NIXUpgradeApplication().desirializer(jsonObject);
        NIXConnection nixConnection =ServiceDAOFactory.getService(NIXConnectionService.class).
                getConnnectionByConnectionId(connectionId);

        nixApplicationObject.setZone(nixConnection.getZone());
        nixApplicationObject.setState(NIXConstants.NIX_NEW_CONNECTION_STATE);
        nixApplicationObject.setConnection(nixConnection.getConnectionId());
        nixApplicationObject.setType(NIXConstants.NIX_UPGRADE_APPLICATION);
        nixApplicationObject.setPortCount(1);
        insert(nixApplicationObject);
        insertUpgradeApplication(nixApplicationObject,nixUpgradeApplication,loginDTO);

        nixApplicationOffice.setHistory(nixApplicationOffice.getHistory());
        nixApplicationOffice.setApplication(nixApplicationObject.getId());
        nixApplicationOffice.setLastModificationTime(System.currentTimeMillis());
        nixApplicationOffice.setCreated(System.currentTimeMillis());
        globalService.save(nixApplicationOffice);
        boolean isUpgradePossible = false;
        for(NIXApplicationLocalLoop nixApplicationLocalLoop:nixApplicationOffice.getLoops()){
            if(nixApplicationLocalLoop.getPortId() == nixUpgradeApplication.getOldPortId()){
                isUpgradePossible = true;
                nixApplicationLocalLoop.setOfficeId(nixApplicationOffice.getId());
                nixApplicationLocalLoop.setApplication(nixApplicationObject.getId());
//                nixApplicationLocalLoop.setRouterSwitchId(nixApplicationLocalLoop.getRouterSwitchId());
                globalService.save(nixApplicationLocalLoop);
               // return;
            }

        }
        if(!isUpgradePossible){
            throw  new RequestFailureException("No Port matched for upgrade");
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        sendNotification(nixApplicationObject,nixApplicationObject.getState(),loginDTO);
    }

    @Transactional
    public void closePortInsertBatchOperation(JsonElement jelement, LoginDTO loginDTO)throws Exception {
        JsonObject jsonObject = jelement.getAsJsonObject();
        NIXApplication nixApplicationObject = new NIXApplication().desirializer(jsonObject);
        long connectionId =jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong();
        NIXConnection nixConnection =ServiceDAOFactory.getService(NIXConnectionService.class).
                getConnnectionByConnectionId(connectionId);
        String existingPortType = jsonObject.get("existingPortType").getAsString();
        if(InventoryConstants.mapOfPortTypeToPortTypeString.
                get(InventoryConstants.PORT_FE).equalsIgnoreCase(existingPortType)){
            nixApplicationObject.setPortType(InventoryConstants.PORT_FE);
        }
        else if(InventoryConstants.mapOfPortTypeToPortTypeString.
                get(InventoryConstants.PORT_GE).equalsIgnoreCase(existingPortType)){
            nixApplicationObject.setPortType(InventoryConstants.PORT_GE);
        }
        else{
            nixApplicationObject.setPortType(InventoryConstants.PORT_10GE);
        }

        nixApplicationObject.setZone(nixConnection.getZone());
        nixApplicationObject.setState(NIXConstants.NIX_CLOSE_STATE);
        nixApplicationObject.setConnection(nixConnection.getConnectionId());
        nixApplicationObject.setType(NIXConstants.NIX_CLOSE_APPLICATION);
        insert(nixApplicationObject);
        long oldPortId =jsonObject.get("oldPort").getAsJsonObject().get("ID").getAsLong();
        NIXCloseApplication nixCloseApplication = new NIXCloseApplication();
        nixCloseApplication.setParent(nixApplicationObject.getId());
        nixCloseApplication.setPortId(oldPortId);
        nixCloseApplication.setPortType(nixApplicationObject.getPortType());
        ServiceDAOFactory.getService(NIXCloseApplicationService.class).insert(nixCloseApplication);

        long nixOfficeId = jsonObject.get("office").getAsJsonObject().get("ID").getAsLong();
        NIXOffice nixOffice = ServiceDAOFactory.getService(NIXOfficeService.class).
                getOfficeById(nixOfficeId);

        NIXApplicationOffice nixApplicationOfficeOld = ServiceDAOFactory.getService(NIXApplicationOfficeService.class)
                .getOfficeById(nixOffice.getApplication_offfice());

        NIXApplicationOffice nixApplicationOfficeNew = new NIXApplicationOffice();

        nixApplicationOfficeNew.setName(nixApplicationOfficeOld.getName());
        nixApplicationOfficeNew.setAddress(nixApplicationOfficeOld.getAddress());
        nixApplicationOfficeNew.setHistory(nixApplicationOfficeOld.getId());
        nixApplicationOfficeNew.setApplication(nixApplicationObject.getId());
        nixApplicationOfficeNew.setLastModificationTime(System.currentTimeMillis());
        nixApplicationOfficeNew.setCreated(System.currentTimeMillis());
        ServiceDAOFactory.getService(NIXApplicationOfficeService.class).insertOffice(nixApplicationOfficeNew);

        for(NIXApplicationLocalLoop nixApplicationLocalLoop:nixApplicationOfficeOld.getLoops()){
            if(nixApplicationLocalLoop.getPortId() == nixCloseApplication.getPortId()){
               // nixApplicationLocalLoop.ge

                boolean efrExist = nixefrService.getCompletedEFRByLocalLoopID(nixApplicationLocalLoop.getId());
                if(efrExist){
                    nixApplicationObject.setState(NIXConstants.NIX_CLOSE_STATE);
                    globalService.update(nixApplicationObject);
                }
                else {
                    nixApplicationObject.setState(NIXConstants.NIX_CLOSE_STATE_WITHOUT_WO);
                    globalService.update(nixApplicationObject);
                }
                NIXApplicationLocalLoop nixApplicationLocalLoopNew = new NIXApplicationLocalLoop();
                nixApplicationLocalLoopNew.setBtclDistance(nixApplicationLocalLoop.getBtclDistance());
                nixApplicationLocalLoopNew.setClientDistance(nixApplicationLocalLoop.getClientDistance());
                nixApplicationLocalLoopNew.setOcdDistance(nixApplicationLocalLoop.getOcdDistance());
                nixApplicationLocalLoopNew.setPopId(nixApplicationLocalLoop.getPopId());
                nixApplicationLocalLoopNew.setPopName(nixApplicationLocalLoop.getPopName());
                nixApplicationLocalLoopNew.setApplication(nixApplicationObject.getId());
                nixApplicationLocalLoopNew.setOfficeId(nixApplicationOfficeNew.getId());
                ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).insertApplication(nixApplicationLocalLoopNew);
                return;
            }
        }

        sendNotification(nixApplicationObject,NIXConstants.NIX_CLOSE_STATE,loginDTO);

    }

    @Transactional
    public void skipDemandNoteBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        nixApplication.setSkipPayment(1);
        updateApplicaton(nixApplication);
        ArrayList<NIXEFR> lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        for (NIXEFR efr : lists) {
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
        }

        List<NIXApplicationLocalLoop> localLoops = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).getLocalLoopByAppId(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).updateApplicaton(localLoop);
        }

    }

    @Transactional
    public void withoutLoopSkipDemandNoteBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement ifrElement = jsonObject.get("ifr");
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        NIXApplication nixApplication = getFlowApplicationByApplicationID(appID);
        nixApplication.setSkipPayment(1);
        updateApplicaton(nixApplication);
        List<NIXIFR> ifrs = new NIXIFRDeserializer().deserialize_custom(jsonElement);
        ArrayList<NIXIFR> selectedIFR = new ArrayList<>();
        for (NIXIFR ifr : ifrs) {

            if (ifr.getSelected() == 1) {
                selectedIFR.add(ifr);
            }
            ifr.setLastModificationTime(System.currentTimeMillis());
            ifr.setIsForwarded(nixApplication.getIsForwarded());
            nixifrService.updateApplicaton(ifr);
        }
        List<NIXApplicationLocalLoop> localLoops = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).prepareLocalloopFromIFR(selectedIFR);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).insertApplication(localLoop);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);


        //lliApplicationService.updateApplicatonState(appID,state);


    }
}