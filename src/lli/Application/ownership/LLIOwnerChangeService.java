package lli.Application.ownership;

import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.RequestFailureException;
import common.pdf.AsyncPdfService;
import flow.FlowService;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.adviceNote.OwnershipChangeAdviceNote;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import login.LoginService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import requestMapping.Service;
import util.NavigationService;
import util.NumberComparator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LLIOwnerChangeService implements NavigationService{

    @DAO
    LLIOwnerChangeDAO lliOwnerChangeDAO;
    @Service
    CommentsService commentsService;
    @Service
    FlowService flowService;
    @Service
    OfficialLetterService officialLetterService;

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

        return lliOwnerChangeDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//		return lliApplicationDAO.getLLIApplicationListByIDList((List<Long>) recordIDs);
        List<LLIOwnerShipChangeApplication> list = lliOwnerChangeDAO.getApplicationListByIDList((List<Long>) recordIDs);

        LoginDTO loginDTO = (LoginDTO) objects[0];
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);

        Map<Integer, FlowState> flowState = FlowRepository.getInstance().getActionableFlowStatesByRoleId(roleId)
                .stream()
                .distinct()
                .collect(Collectors.toMap(FlowState::getId, Function.identity()));

        Comparator<LLIOwnerShipChangeApplication> compareByHasPermissionAndApplicationId =
                Comparator.comparing(LLIOwnerShipChangeApplication::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(LLIOwnerShipChangeApplication:: getId, Comparator.reverseOrder());

        return list.stream()
                .peek(t-> {
                    FlowState flowState1 = flowState.getOrDefault(t.getState(), null);
                    t.setHasPermission(null != flowState1);
                    FlowState state = FlowRepository.getInstance().getFlowStateByFlowStateId(t.getState());
                    t.setStateDescription(state.getViewDescription());
                    t.setColor(state.getColor());

                }).sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());

    }

    @Transactional
    public void setApplicationAsPaymentClearedByDemandNoteID(long demandNoteID) throws Exception {
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = getApplicationByDemandNoteId(demandNoteID);
        if (lliOwnerShipChangeApplication == null) {
            throw new RequestFailureException("No Such LLI Owner Change Application found");
        }

        if(lliOwnerShipChangeApplication.getStatus() == LLIConnectionConstants.STATUS_PAYMENT_CLEARED)
            return;

        if(lliOwnerShipChangeApplication.getStatus() != LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED){
            throw new RequestFailureException("Application can only be set to payment cleared "
                    + "from demand note generated state.");
        }

        lliOwnerShipChangeApplication.setStatus(LLIConnectionConstants.STATUS_PAYMENT_CLEARED);
        lliOwnerChangeDAO.updateApplicaton(lliOwnerShipChangeApplication);

        // flow: change application state --> PAYMENT_VERIFIED
//		new FlowService().paymentVerified(lliApplication.getApplicationID());
    }
    @Transactional
    public void insertBatchOperation(JsonElement jelement, LoginDTO loginDTO) throws Exception{
        JsonObject jsonObject = jelement.getAsJsonObject();
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = LLIOwnerShipChangeApplication.deserialize(jsonObject);
        lliOwnerShipChangeApplication.setType(LLIConnectionConstants.CHANGE_OWNERSHIP);
        lliOwnerShipChangeApplication.setState(LLIConnectionConstants.OWNER_SHIP_CHANGE_STATE);
//        ACCOUNT_RECEIVABLE check
        double receivableAmount = ServiceDAOFactory.getService(AccountingEntryService.class).
                getBalanceByClientIDAndAccountID(lliOwnerShipChangeApplication.getSrcClient(), AccountType.ACCOUNT_RECEIVABLE_TD.getID());
        if (NumberComparator.isGreaterThanOrEqual(receivableAmount,1.00)){
            throw new RequestFailureException("You should pay your due first, your due amount is: "+Math.ceil(receivableAmount));
        }

        insert(lliOwnerShipChangeApplication);
        JsonArray jsonArray = (JsonArray) ((jsonObject.get("selectedConnectionList")!=null)?jsonObject.get("selectedConnectionList"):null);
        List<LLIConnectionInstance> lliConnectionInstances = new ArrayList<>();
        if(jsonArray !=null){

            for (JsonElement jsonElement1 : jsonArray) {
                JsonObject ConnectionJsonObject = jsonElement1.getAsJsonObject();
                long conId = ConnectionJsonObject.get("ID").getAsLong();
                LLIConnectionInstance lliConnection = ServiceDAOFactory.getService(LLIConnectionService.class).
                        getLLIConnectionByConnectionID(conId);
                if(jsonArray.size() ==1){
                    lliOwnerShipChangeApplication.setZone(lliConnection.getZoneID());
                    updateApplication(lliOwnerShipChangeApplication);
                }
                lliConnection.setStatus(LLIConnectionConstants.OWNERSHIP_CHANGE_ON_PROCESS);
                LLIOnProcessConnection lliOnProcessConnection = new LLIOnProcessConnection();
                lliOnProcessConnection.setApplication(lliOwnerShipChangeApplication.getId());
                lliOnProcessConnection.setConnection(lliConnection.getID());
                ServiceDAOFactory.getService(LLIOnProcessConnectionService.class).insert(lliOnProcessConnection);
            }
        }

    }

    @Transactional
    public void insert(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication)throws Exception {
        lliOwnerChangeDAO.insert(lliOwnerShipChangeApplication);
    }

    @Transactional
    public LLIOwnerShipChangeApplication getApplicationById(long applicationID) throws Exception{
        List<LLIOwnerShipChangeApplication> lliOwnerShipChangeApplications = lliOwnerChangeDAO.getApplicationById(applicationID);
        if(lliOwnerShipChangeApplications.size() == 0) throw new RequestFailureException("No data found for this request");
        else return lliOwnerShipChangeApplications.get(0);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getActionList(Integer state, Integer roleId) {
        List<FlowState> flowStates = new ArrayList<>();
        if (roleId > 0) {
            flowStates = flowService.getNextStatesFromStateRole(state, roleId);

        } else {
            flowStates = flowService.getNextStatesFromState(state);
        }
        for (FlowState flowState : flowStates) {
            long id = flowState.getId();
            flowState.setUrl(LLIOwnerChangeConstant.stateMap.get(flowState.getId()).get("url"));
            flowState.setModal(LLIOwnerChangeConstant.stateMap.get(flowState.getId()).get("modal"));
            flowState.setView(LLIOwnerChangeConstant.stateMap.get(flowState.getId()).get("view"));
            flowState.setRedirect(LLIOwnerChangeConstant.stateMap.get(flowState.getId()).get("redirect"));
            flowState.setParam(LLIOwnerChangeConstant.stateMap.get(flowState.getId()).get("param"));
        }
        return flowStates;
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public FlowState getCurrentState(Integer state) {
        FlowState flowState = flowService.getStateById(state);
        flowState.setUrl(LLIOwnerChangeConstant.stateMap.get(state).get("url"));
        flowState.setModal(LLIOwnerChangeConstant.stateMap.get(state).get("modal"));
        flowState.setView(LLIOwnerChangeConstant.stateMap.get(state).get("view"));
        flowState.setRedirect(LLIOwnerChangeConstant.stateMap.get(state).get("redirect"));
        flowState.setRedirect(LLIOwnerChangeConstant.stateMap.get(state).get("param"));

        return flowState;
    }

    @Transactional
    public void updateApplicatonState(long appID, int state)throws Exception{
        lliOwnerChangeDAO.updateApplicatonState(appID, state);
    }

    @Transactional
    public void updateApplication(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication)throws Exception {
        lliOwnerChangeDAO.updateApplicaton(lliOwnerShipChangeApplication);
    }

    @Transactional
    public void handleGenerateAdviceNoteRequest(String jsonString, LoginDTO loginDTO)throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = getApplicationById(appID);
        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();
        JsonArray userArray = jsonObject.getAsJsonArray("userList");
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        lliOwnerShipChangeApplication.setState(state);
        updateApplication(lliOwnerShipChangeApplication);

        generateAN(userArray, state, lliOwnerShipChangeApplication, loginDTO.getUserID());
    }

    public void generateAN(JsonArray userArray, int state, LLIOwnerShipChangeApplication lliOwnerShipChangeApplication, long senderId) throws Exception {

        lliOwnerShipChangeApplication.setState(state);
        updateApplication(lliOwnerShipChangeApplication);

        List<RecipientElement> recipientElements = officialLetterService.getAllCCAndToList(userArray, state);
        OwnershipChangeAdviceNote adviceNote = new OwnershipChangeAdviceNote(lliOwnerShipChangeApplication.getId(), lliOwnerShipChangeApplication.getSrcClient());
        generateOfficialLetter(adviceNote, recipientElements, senderId);
        AsyncPdfService.getInstance().accept(adviceNote);

    }


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void generateOfficialLetter(OfficialLetter officialLetter, List<RecipientElement>recipientElements, long senderId) throws Exception {
        officialLetterService.saveOfficialLetterTransactionalDefault( officialLetter, recipientElements, senderId );
    }



    @Transactional
    public void handOwnerChangeComplete(String jsonString, LoginDTO loginDTO) throws Exception{
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = getApplicationById(appID);
        List<LLIOnProcessConnection> lliOnProcessConnections = ServiceDAOFactory.getService(LLIOnProcessConnectionService.class).
                getConnectionByAppId(lliOwnerShipChangeApplication.getId());
        for (LLIOnProcessConnection lliOnProcessConnection:lliOnProcessConnections){
            LLIConnection lliConnectionInstanceOld = ServiceDAOFactory.getService(LLIFlowConnectionService.class).
                    getConnectionByID(
                        lliOnProcessConnection.getConnection()
                    );
            //update old connection
            lliConnectionInstanceOld.setStatus(LLIConnectionInstance.STATUS_CLOSED);
            lliConnectionInstanceOld.setActiveTo(System.currentTimeMillis());
           // lliConnectionInstanceOld.setValidTo(System.currentTimeMillis());
            lliConnectionInstanceOld.setIncident(LLIConnectionConstants.CHANGE_OWNERSHIP);
            ServiceDAOFactory.getService(LLIFlowConnectionService.class).updateConnection(lliConnectionInstanceOld);


            LLIConnection lliConnectionInstanceNew = new LLIConnection();

            //owner change connection
            lliConnectionInstanceNew.setClientID(lliOwnerShipChangeApplication.getDstClient());
            lliConnectionInstanceNew.setName(lliConnectionInstanceOld.getName());
            lliConnectionInstanceNew.setConnectionType(lliConnectionInstanceOld.getConnectionType());
            lliConnectionInstanceNew.setID(lliConnectionInstanceOld.getID());
            lliConnectionInstanceNew.setStatus(LLIConnectionConstants.STATUS_ACTIVE);
            lliConnectionInstanceNew.setBandwidth(lliConnectionInstanceOld.getBandwidth());
            lliConnectionInstanceNew.setIncident(LLIConnectionConstants.NEW_CONNECTION);
            lliConnectionInstanceNew.setValidTo(Long.MAX_VALUE);
            lliConnectionInstanceNew.setValidFrom(System.currentTimeMillis());
            lliConnectionInstanceNew.setActiveTo(Long.MAX_VALUE);
            lliConnectionInstanceNew.setActiveFrom(System.currentTimeMillis()+1000l);
            lliConnectionInstanceNew.setDiscountRate(lliConnectionInstanceOld.getDiscountRate());
            lliConnectionInstanceNew.setZoneID(lliConnectionInstanceOld.getZoneID());
            lliConnectionInstanceNew.setCostChartID(lliConnectionInstanceOld.getCostChartID());
            lliConnectionInstanceNew.setStartDate(lliConnectionInstanceOld.getStartDate());
            ServiceDAOFactory.getService(LLIFlowConnectionService.class).insertConnectionForNewOwner(lliConnectionInstanceNew);
        }
        lliOwnerShipChangeApplication.setStatus(LLIOwnerChangeConstant.APPLICATION_COMPLETED);
        lliOwnerShipChangeApplication.setState(state);
        updateApplication(lliOwnerShipChangeApplication);

    }

    @Transactional
    public LLIOwnerShipChangeApplication getApplicationByDemandNoteId(long id)throws Exception {
       return lliOwnerChangeDAO.getApplicationByDemandNoteId(id)
               .stream()
               .findFirst()
               .orElse(null);
    }

    @Transactional
    public void updateApplicaton(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication) throws Exception{
        lliOwnerChangeDAO.updateApplicaton(lliOwnerShipChangeApplication);
    }
}
