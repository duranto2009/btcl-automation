package lli.Application.ReviseClient;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.RoleConstants;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import exception.NoDataFoundException;
import flow.entity.FlowState;
import flow.entity.Role;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.Comments.RevisedComment;
import lli.LLILongTermContract;
import lli.LLILongTermContractDAO;
import lli.adviceNote.ClientAdviceNote;
import lli.adviceNote.LLITDReconnectAdviceNote;
import lli.connection.LLIConnectionConstants;
import lli.longTerm.LLILongTermBenefit;
import lli.longTerm.LLILongTermBenefitService;
import location.ZoneDAO;
import login.LoginDTO;
import login.LoginService;
import notification.NotificationDTO;
import notification.NotificationDTOConditionBuilder;
import notification.NotificationService;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import role.RoleService;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReviseService implements NavigationService {

    private static final Logger logger = LoggerFactory.getLogger(ReviseService.class);

    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);
    CommentsService commentService = ServiceDAOFactory.getService(CommentsService.class);

    @DAO
    ReviseDAO reviseDAO;
    @DAO
    ZoneDAO zoneDAO;
    @Service
    RoleService roleService;
    @Service
    LLILongTermBenefitService lliLongTermBenefitService;

    @Service
    GlobalService globalService;
    @DAO
    LLILongTermContractDAO lliLongTermContractDAO;

    @Service
    NotificationService notificationService;


    private static String ROLE_SERVER_ROOM = "ServerRoom";

    public String viewNewApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getappById(applicationID).getApplicationType();
        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType) + "-view";
    }

    @Transactional
    public void sendNotification(ReviseDTO application,int nextState, LoginDTO loginDTO) throws Exception {


        boolean isForClient=false;
        if(loginDTO.getRoleID()>0 && loginDTO.getRoleID()== RoleConstants.VENDOR_ROLE){
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getId(), loginDTO.getRoleID(), false,loginDTO.getUserID());

        }
        else if(loginDTO.getRoleID() > 0){

            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getId(), loginDTO.getRoleID(), false);

            if(loginDTO.getRoleID()==RoleConstants.LDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getId(), RoleConstants.CDGM_ROLE, false);
            }
            if(loginDTO.getRoleID()==RoleConstants.CDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getId(), RoleConstants.LDGM_ROLE, false);
            }

        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getId(), loginDTO.getAccountID(), false);
        }

        //check if any remaining notification haven't taken action

        List<NotificationDTO> notificationDTOS=globalService.getAllObjectListByCondition(NotificationDTO.class,
                new NotificationDTOConditionBuilder()
                        .Where()
                        .moduleIdEquals((long) ModuleConstants.Module_ID_LLI)
                        .entityIdEquals(application.getId())
                        .isActionTaken(false)
                        .getCondition()

        );

        if(notificationDTOS.size()==0) {


            HashMap<Long, Role> uniqueRoles = notificationService.getNextStateRoleListForNotification(nextState);
            if (uniqueRoles.isEmpty()) {
                isForClient = true;
                notificationService.saveNotificationForNextStateUser(
                        isForClient,
                        nextState,
                        application.getClientID(),
                        -1,
                        ModuleConstants.Module_ID_LLI,
                        application.getId(),

                        "LLI Application",
                        LLIConnectionConstants.LLI_REVISE_DETAILS_PAGE_URL,
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


                            userIdsForNotification.add(userDTO.getUserID());



                    }
                    for (Long userId : userIdsForNotification) {
                        notificationService.saveNotificationForNextStateUser(
                                isForClient,
                                nextState,
                                application.getClientID(),
                                userId,
                                ModuleConstants.Module_ID_LLI,
                                application.getId(),

                                "LLI Application",
                                LLIConnectionConstants.LLI_REVISE_DETAILS_PAGE_URL,
                                false,
                                0
                        );

                    }
                }

            }
        }


    }



    @Transactional(transactionType = TransactionType.READONLY)
    public ReviseDTO getappById(long applicationId) throws Exception {
        ReviseDTO reviseDTO = reviseDAO.getAppByID(applicationId);
        if(reviseDTO == null){
            throw new RequestFailureException("No Data found ");
        }

        return reviseDTO;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isAlreadyRequested(long clientID,int appType) throws Exception {
        ReviseDTO reviseDTO = reviseDAO.getAppByClientAndAppTypeNotCompleted(clientID,appType);
        if (reviseDTO.getId() > 1) {
            return true;
        }

        else{
           return false;
        }

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public ReviseDTO getApplicationByDemandNoteId(long dnId) throws Exception {
        return reviseDAO.getAppByDemandNoteId(dnId)
        .stream()
        .findFirst()
        .orElseThrow(()->new NoDataFoundException("No Revise Client Application Found with Demand Note Id " + dnId));
    }

    @Transactional
    public void setDemandNote(long applicationId, long demandNoteId) {
        ReviseDTO reviseDTO = null;
        try {
            reviseDTO = getappById(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return;
        }

        if (reviseDTO == null) {
            logger.error(" - [ X ] No revise application found with id: " + applicationId);
            return;
        }

        if (reviseDTO.getIsDemandNoteNeeded() == 0) {
            logger.error(" - [ X ] No demand note is needed for revise applicaton with id: " + applicationId);
            return;
        }

        reviseDTO.setDemandNoteID(demandNoteId);
        reviseDTO.setState(LLIConnectionConstants.BREAK_LONG_TERM_APPROVED);
        try {
            reviseDAO.updateApplication(reviseDTO);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    @Transactional
    public void setReconnectionDemandNote(long applicationId, long demandNoteId) {
        ReviseDTO reviseDTO = null;
        try {
            reviseDTO = getappById(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return;
        }

        if (reviseDTO == null) {
            logger.error(" - [ X ] No revise application found with id: " + applicationId);
            return;
        }

        if (reviseDTO.getIsDemandNoteNeeded() == 0) {
            logger.error(" - [ X ] No demand note is needed for revise applicaton with id: " + applicationId);
            return;
        }

        reviseDTO.setDemandNoteID(demandNoteId);
        reviseDTO.setState(LLIConnectionConstants.RECONNECT_DEMAND_NOTE);
        try {
            reviseDAO.updateApplication(reviseDTO);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        List<ReviseDTO> list = reviseDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        List<ReviseDTO> reviseDTOList = reviseDAO.getDTOListByIDList((List<Long>) recordIDs);

        LoginDTO loginDTO = (LoginDTO) objects[0];
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);

        Map<Integer, FlowState> flowState = FlowRepository.getInstance().getActionableFlowStatesByRoleId(roleId)
                .stream()
                .distinct()
                .collect(Collectors.toMap(FlowState::getId, Function.identity()));

        Comparator<ReviseDTO> compareByHasPermissionAndApplicationId =
                Comparator.comparing(ReviseDTO::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(ReviseDTO:: getId, Comparator.reverseOrder());

        return reviseDTOList.stream()
                .peek(t-> {
                    t.setCompleted(isReviseApplicationCompleted(t));
                    FlowState flowState1 = flowState.getOrDefault((int)t.getState(), null);
                    t.setHasPermission(null != flowState1);
                    FlowState state = FlowRepository.getInstance().getFlowStateByFlowStateId((int)t.getState());
                    t.setStateDescription(state != null ? state.getViewDescription() : "");
                    t.setColor(state != null ? state.getColor() : "#eeeeee");
                }).sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());


    }

    private boolean isReviseApplicationCompleted (ReviseDTO reviseDTO) {
        return reviseDTO.getState() == LLIConnectionConstants.NEW_LONG_TERM_COMPLETED
                || reviseDTO.getState() == LLIConnectionConstants.BREAK_LONG_TERM_COMPLETED
                //changed raihan
                || reviseDTO.getState() == LLIConnectionConstants.TD_DONE
                || reviseDTO.getState() == LLIConnectionConstants.TD_AN
                || reviseDTO.getState() == LLIConnectionConstants.RECONNECT_AN
                || reviseDTO.getState() == LLIConnectionConstants.RECONNECT_DONE;
    }

    @Transactional
    public void insertApplication(ReviseDTO reviseDTO, LoginDTO loginDTO) throws Exception {
        if (!loginDTO.getIsAdmin() && reviseDTO.getClientID() != loginDTO.getAccountID()) {
            throw new RequestFailureException("You can not submit other client's application.");
        }

        reviseDAO.insertApp(reviseDTO);
//        new LLIClientTDService().
    }

    @Transactional
    public void updateApplicaton(ReviseDTO reviseDTO) throws Exception {
        reviseDAO.updateApplication(reviseDTO);
    }

    @Transactional
    public void updateApplicatonState(long applicatonID, int applicationState) throws Exception {
        reviseDAO.updateApplicationState(applicatonID, applicationState);
    }

    @Transactional
    public void generateAdviceNoteDocument(ReviseDTO reviseDTO, int state, JsonArray userArray, long senderId) throws Exception {
        ClientAdviceNote llitdReconnectAdviceNote = saveOfficialLetter(reviseDTO, state, userArray, senderId);
        //create pdf
        AsyncPdfService.getInstance().accept(llitdReconnectAdviceNote);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public ClientAdviceNote saveOfficialLetter(ReviseDTO reviseDTO, int state, JsonArray userArray, long senderId) throws Exception {
        OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
        //get CC List

        List<RecipientElement> ccList = officialLetterService.getCCRecipientElements(userArray);

        //get To List
        List<RecipientElement> toList = officialLetterService.getToRecipientElements(state);


        //create officialLetter

        LLITDReconnectAdviceNote llitdReconnectAdviceNote = new LLITDReconnectAdviceNote(reviseDTO.getId(), reviseDTO.getClientID());

        //create recipient list
        List<RecipientElement> recipientElements = Stream.concat(toList.stream(), ccList.stream()).collect(Collectors.toList());

        //save official doc
        officialLetterService.saveOfficialLetterTransactionalDefault(llitdReconnectAdviceNote, recipientElements, senderId);

        reviseDTO.setAdviceNoteId(llitdReconnectAdviceNote.getId());
        globalService.update(reviseDTO);
        return llitdReconnectAdviceNote;
    }

    public void comment(JsonElement jsonElement, LoginDTO loginDTO, ReviseDTO reviseDTO) {
        RevisedComment comment = new CommentsDeserializer().deserializeReviseComment(jsonElement, loginDTO);
        comment.setApplicationID(reviseDTO.getId());
        comment.setStateID(reviseDTO.getState());

        try {
            commentService.insertComments(comment, loginDTO);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    @Transactional
    public void completeNewLongTerm(ReviseDTO reviseDTO) {
        LLILongTermContract lliLongTermContract = new LLILongTermContract();

        long nextId;
        try {
            nextId = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLILongTermContract.class));
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return;
        }

        lliLongTermContract.setID(nextId);
        lliLongTermContract.setHistoryID(nextId);

        if(reviseDTO.getSuggestedDate()<System.currentTimeMillis()){
            lliLongTermContract.setActiveFrom(System.currentTimeMillis());
        }else{

            lliLongTermContract.setActiveFrom(reviseDTO.getSuggestedDate());
        }
        lliLongTermContract.setActiveTo(Long.MAX_VALUE);
        lliLongTermContract.setValidTo(Long.MAX_VALUE);
        lliLongTermContract.setValidFrom(System.currentTimeMillis());

      //changed by bony on 09-07-19
        if(reviseDTO.getSuggestedDate()<System.currentTimeMillis()){
            lliLongTermContract.setContractStartDate(System.currentTimeMillis());

        }else{

            lliLongTermContract.setContractStartDate(reviseDTO.getSuggestedDate());
        }

        lliLongTermContract.setContractEndDate(lliLongTermContract.getContractStartDate() + 5 * TimeConverter.MILLISECONDS_IN_YEAR);
        lliLongTermContract.setBandwidth(reviseDTO.getBandwidth());
        lliLongTermContract.setClientID(reviseDTO.getClientID());
        lliLongTermContract.setStatus(LLILongTermContract.STATUS_ACTIVE);

        try {
            lliLongTermContractDAO.insertLongTermContract(lliLongTermContract);
        } catch (Exception e) {
            logger.error(" [ X ] Could not insert LLI Long Term Contract into DB ", e);
        }
    }

    @Transactional
    public void completeBreakLongTerm(ReviseDTO reviseDTO) {
        LLILongTermContract previousLLliLongTermContract = null;
        try {
            previousLLliLongTermContract = lliLongTermContractDAO.getLLILongTermContractByContractID(reviseDTO.getReferenceContract());
            if (previousLLliLongTermContract != null) {
                if (closePreviousContract(previousLLliLongTermContract, reviseDTO)) {
                    LLILongTermContract newPartialContract = null;
                    if(previousLLliLongTermContract.getBandwidth() - reviseDTO.getBandwidth() > 0) {
                        newPartialContract = insertNewRevisedContract(previousLLliLongTermContract, reviseDTO);
                    }
                    updateLongTermBenefitCalculation(previousLLliLongTermContract, newPartialContract);
                }
            } else {
                throw new Exception("Previous LLI contract note foud");
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    @Transactional
    private void updateLongTermBenefitCalculation(LLILongTermContract previousLLliLongTermContract, LLILongTermContract newPartialContract) {
        LLILongTermBenefit prevBenefit = lliLongTermBenefitService.getByContractId(previousLLliLongTermContract.getID());
        prevBenefit.setDeleted(true);
        lliLongTermBenefitService.save(prevBenefit);

        if(newPartialContract == null)
            return;;

        //Say previously it was 60 MB and total benefit 6000 taka
        //Now break contract of 20 MB so new contract is 40 MB
        //So charge is 2000 taka and rest 4000 taka will be forward to benefitted amount for that 40 MB
        double amount = prevBenefit.getAmount() * newPartialContract.getBandwidth() /previousLLliLongTermContract.getBandwidth();
        lliLongTermBenefitService.save(LLILongTermBenefit.builder()
                .contractId(newPartialContract.getID())
                .clientId(newPartialContract.getClientID())
                .amount(NumberUtils.formattedValue(amount))
                .build());

    }

    private boolean closePreviousContract(LLILongTermContract lliLongTermContract, ReviseDTO reviseDTO) {
        boolean status = false;
        try {
            lliLongTermContract.setActiveTo(reviseDTO.getSuggestedDate());
            lliLongTermContractDAO.updateLLILongTerm(lliLongTermContract);
            status = true;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
        return status;
    }

    private LLILongTermContract insertNewRevisedContract(LLILongTermContract previousLongTermContract, ReviseDTO reviseDTO) {

        LLILongTermContract lliLongTermContract = new LLILongTermContract();

        long nextId;
        try {
            nextId = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLILongTermContract.class));
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return null;
        }

        lliLongTermContract.setHistoryID(nextId);
        lliLongTermContract.setID(previousLongTermContract.getID());

        lliLongTermContract.setActiveFrom(reviseDTO.getSuggestedDate());
        lliLongTermContract.setActiveTo(Long.MAX_VALUE);
        lliLongTermContract.setValidTo(Long.MAX_VALUE);
        lliLongTermContract.setValidFrom(System.currentTimeMillis());
        lliLongTermContract.setContractStartDate(reviseDTO.getSuggestedDate());
        //lliLongTermContract.setContractEndDate(lliLongTermContract.getContractStartDate() + 5 * TimeConverter.MILLISECONDS_IN_YEAR);
        lliLongTermContract.setContractEndDate(previousLongTermContract.getContractEndDate());

        // if -ve, put 0
        lliLongTermContract.setBandwidth(Math.max(0, previousLongTermContract.getBandwidth() - reviseDTO.getBandwidth()));
        lliLongTermContract.setClientID(reviseDTO.getClientID());
        lliLongTermContract.setStatus(LLILongTermContract.STATUS_ACTIVE);

        try {
            lliLongTermContractDAO.insertLongTermContract(lliLongTermContract);
        } catch (Exception e) {
            logger.error(" [ X ] Could not insert LLI Long Term Contract into DB ", e);
            return null;
        }

        return lliLongTermContract;
    }

    @Transactional
    public void setApplicationAsPaymentClearedByDemandNoteID(long demandNoteID) throws Exception {
        ReviseDTO reviseDTO = getApplicationByDemandNoteId(demandNoteID);
        long state = reviseDTO.getState();

    }

    @Transactional
    public void establishNewLongtermContract(JsonObject jsonObject, JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        ReviseDTO reviseDTO = getappById(appID);

        if(jsonObject.get("senderId") == null){
            throw new RequestFailureException("Sender ID Not Found");
        }
        long senderId = jsonObject.get("senderId").getAsLong();

        JsonArray userArray = jsonObject.getAsJsonArray("userList");

        int state = jsonObject.get("nextState").getAsInt();

        reviseDTO.setState(state);
        globalService.update(reviseDTO);
        sendNotification(reviseDTO,state,loginDTO);
        comment(jsonElement, loginDTO, reviseDTO);
        generateAdviceNoteDocument(reviseDTO, state, userArray, senderId);
        completeNewLongTerm(reviseDTO);

    }

    @Transactional
    public void demolishLongTermContract(JsonObject jsonObject, JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        ReviseDTO reviseDTO = getappById(appID);

        int state = jsonObject.get("nextState").getAsInt();
        if(jsonObject.get("senderId") == null){
            throw new RequestFailureException("Sender ID Not Found");
        }
        long senderId = jsonObject.get("senderId").getAsLong();
        JsonArray userArray = jsonObject.getAsJsonArray("userList");

        reviseDTO.setState(state);
        globalService.update(reviseDTO);
        sendNotification(reviseDTO,state,loginDTO);
        comment(jsonElement, loginDTO, reviseDTO);
        generateAdviceNoteDocument(reviseDTO, state, userArray, senderId);
        completeBreakLongTerm(reviseDTO);
    }
}
