package nix.revise;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.ModuleConstants;
import common.RequestFailureException;
import common.RoleConstants;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import exception.NoDataFoundException;
import flow.FlowService;
import flow.entity.FlowState;
import flow.entity.Role;
import global.GlobalService;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.Comments.RevisedComment;
import login.LoginDTO;
import nix.advicenote.NIXTDReconnectAdviceNote;
import nix.constants.NIXConstants;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NIXReviseService implements NavigationService {

    @Service
    NotificationService notificationService;

    @Service
    GlobalService globalService;

    private static final Logger logger = LoggerFactory.getLogger(NIXReviseService.class);

    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);
    CommentsService commentService = ServiceDAOFactory.getService(CommentsService.class);

    @DAO
    NIXReviseDAO nixreviseDAO;
    @Service
    RoleService roleService;
    private static String ROLE_SERVER_ROOM = "ServerRoom";

    @Transactional
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXReviseDTO getappById(long applicationId) throws Exception {
        NIXReviseDTO reviseDTO = nixreviseDAO.getAppByID(applicationId);
        if(reviseDTO == null){
            throw new RequestFailureException("No NIX Revise Client Application found by app id " + applicationId);
        }
        return reviseDTO;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isAlreadyRequested(long clientID,int appType) throws Exception {
        List<NIXReviseDTO> reviseDTOs = nixreviseDAO.getAppByClientAndAppTypeNotCompleted(clientID,appType);
        if (reviseDTOs.size()> 0) {
            return true;
        }
        else{
            return false;
        }

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXReviseDTO getNIXReviseClientApplicationByDemandNoteId(long dnId) throws Exception {
        return nixreviseDAO.getAppByDemandNoteId(dnId)
                .stream()
                .findFirst()
                .orElseThrow(()->new NoDataFoundException("No NIX Revise Client Application found for Demand Note Id " + dnId));

    }

    @Transactional
    public void setDemandNote(long applicationId, long demandNoteId) {
        NIXReviseDTO reviseDTO = null;
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
        try {
            nixreviseDAO.updateApplication(reviseDTO);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }


    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        List<NIXReviseDTO> list = nixreviseDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @Transactional
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        List<NIXReviseDTO> reviseDTOList = nixreviseDAO.getDTOListByIDList((List<Long>) recordIDs);

        for (NIXReviseDTO reviseDTO : reviseDTOList) {
            LoginDTO loginDTO = (LoginDTO) objects[0];
            int roleID;
            if (loginDTO.getUserID() == -1) {
                roleID = -1;
            } else {
                roleID = (int) loginDTO.getRoleID();
            }

            reviseDTO.setCompleted(
                    reviseDTO.getState() == NIXConstants.TD_DONE
                            || reviseDTO.getState() == NIXConstants.TD_AN
                            || reviseDTO.getState() == NIXConstants.RECONNECT_AN
                            || reviseDTO.getState() == NIXConstants.RECONNECT_DONE

            );

            List<FlowState> flowState = new FlowService().getStatesByRole(roleID);
            for (FlowState flowState1 : flowState) {
                if (reviseDTO.getState() == flowState1.getId()) {
                    reviseDTO.setHasPermission(true);
                    break;
                }
            }

            //todo : view actor for state
            FlowState state = new FlowService().getStateById((int) reviseDTO.getState());
            reviseDTO.setStateDescription(state != null ? state.getViewDescription() : "");
            reviseDTO.setColor(state != null ? state.getColor() : "#eeeeee");
        }
        return reviseDTOList;
    }

    @Transactional
    public void insertApplication(NIXReviseDTO reviseDTO, LoginDTO loginDTO) throws Exception {
        if (!loginDTO.getIsAdmin() && reviseDTO.getClientID() != loginDTO.getAccountID()) {
            throw new RequestFailureException("You can not submit other client's application.");
        }

        nixreviseDAO.insertApp(reviseDTO);
//        new LLIClientTDService().
    }

    @Transactional
    public void updateApplicaton(NIXReviseDTO reviseDTO) throws Exception {
        nixreviseDAO.updateApplication(reviseDTO);
    }

    @Transactional
    public void updateApplicatonState(long applicatonID, int applicationState) throws Exception {
        nixreviseDAO.updateApplicationState(applicatonID, applicationState);
    }

    @Transactional
    public void generateAdviceNoteDocument(long appId, int state, JsonArray userArray, long senderId) throws Exception {
        NIXTDReconnectAdviceNote nixtdReconnectAdviceNote = saveOfficialLetter(appId, state, userArray, senderId);
        //create pdf
        AsyncPdfService.getInstance().accept(nixtdReconnectAdviceNote);

    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public NIXTDReconnectAdviceNote saveOfficialLetter(long appId, int state, JsonArray userArray, long senderId) throws Exception {
        OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
        //get CC List

        List<RecipientElement> ccList = officialLetterService.getCCRecipientElements(userArray);

        //get To List
        List<RecipientElement> toList = officialLetterService.getToRecipientElements(state);


        //create officialLetter
        NIXReviseDTO reviseDTO = getappById(appId);

        NIXTDReconnectAdviceNote nixtdReconnectAdviceNote = new NIXTDReconnectAdviceNote(appId, reviseDTO.getClientID());

        //create recipient list
        List<RecipientElement> recipientElements = Stream.concat(toList.stream(), ccList.stream()).collect(Collectors.toList());
        //save official doc
        officialLetterService.saveOfficialLetterTransactionalDefault(nixtdReconnectAdviceNote, recipientElements, senderId);
        return nixtdReconnectAdviceNote;
    }

    public void comment(JsonElement jsonElement, LoginDTO loginDTO, NIXReviseDTO reviseDTO) {
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
    public void setApplicationAsPaymentClearedByDemandNoteID(long demandNoteID) throws Exception {
        NIXReviseDTO reviseDTO = getNIXReviseClientApplicationByDemandNoteId(demandNoteID);
        long state = reviseDTO.getState();
        List<FlowState> flowStates = new FlowService().getNextStatesFromState((int)state);
        if(flowStates != null){
            if(!flowStates.isEmpty()){
                updateApplicatonState(reviseDTO.getId(), flowStates.get(0).getId());
            }
        }
    }

    @Transactional
    public void reconnectRequestBatchOperation(NIXReviseDTO reviseDTO,LoginDTO loginDTO)throws Exception {
        boolean isTDClient = ServiceDAOFactory.getService(NIXClientTDService.class).
                isClientTemporarilyDisconnected(reviseDTO.getClientID());

        boolean isAlreadyRequested = isAlreadyRequested(reviseDTO.getClientID(),NIXConstants.NIX_RECONNECT);
        if (!isTDClient || isAlreadyRequested)
            throw new RequestFailureException("This client is either temporary disconnected or already applied for reconnection.");

        reviseDTO.setApplicationType(NIXConstants.NIX_RECONNECT);
        reviseDTO.setState(NIXConstants.RECONNECT_SUBMIT_STATE);
        insertApplication(reviseDTO, loginDTO);
    }


    /*notification*/


    @Transactional
    public void sendNotification(NIXReviseDTO application, int nextState, LoginDTO loginDTO) throws Exception {


        boolean isForClient=false;
        if(loginDTO.getRoleID()>0 && loginDTO.getRoleID()== RoleConstants.VENDOR_ROLE){
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), loginDTO.getRoleID(), false,loginDTO.getUserID());

        }
        else if(loginDTO.getRoleID() > 0){

            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), loginDTO.getRoleID(), false);

            if(loginDTO.getRoleID()==RoleConstants.LDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), RoleConstants.CDGM_ROLE, false);
            }
            if(loginDTO.getRoleID()==RoleConstants.CDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), RoleConstants.LDGM_ROLE, false);
            }

        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_NIX, application.getId(), loginDTO.getAccountID(), false);
        }

        //check if any remaining notification haven't taken action

        List<NotificationDTO> notificationDTOS=globalService.getAllObjectListByCondition(NotificationDTO.class,
                new NotificationDTOConditionBuilder()
                        .Where()
                        .moduleIdEquals((long) ModuleConstants.Module_ID_NIX)
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
                        ModuleConstants.Module_ID_NIX,
                        application.getId(),

                        "NIX Application",
                        NIXConstants.NIX_REVISE_DETAILS_PAGE_URL,
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
                                ModuleConstants.Module_ID_NIX,
                                application.getId(),

                                "NIX Application",
                                NIXConstants.NIX_REVISE_DETAILS_PAGE_URL,
                                false,
                                0
                        );

                    }
                }

            }
        }


    }


}
