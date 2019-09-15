package lli.Application;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.RoleConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.client.ClientDTO;
import common.client.ClientService;
import common.pdf.AsyncPdfService;
import flow.entity.FlowState;
import flow.entity.Role;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Application.AdditionalPort.AdditionalPort;
import lli.Application.AdditionalPort.LLIAdditionalPortService;
import lli.Application.BandwidthConfiguration.BandwidthConfiguration;
import lli.Application.BandwidthConfiguration.BandwidthConfigurationService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRConditionBuilder;
import lli.Application.EFR.EFRDeserializer;
import lli.Application.EFR.EFRService;
import lli.Application.FlowConnectionManager.LLIApplicationFlowConnectionManagerService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRDeserializer;
import lli.Application.IFR.IFRService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewConnection.LLINewConnectionApplication;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.Office.OfficeService;
import lli.Application.ShiftBandwidth.LLIShiftBandwidthApplication;
import lli.Application.newOffice.NewOffice;
import lli.Application.newOffice.NewOfficeService;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplication;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.LLIActionButton;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.adviceNote.ConnectionAdviceNote;
import lli.connection.LLIConnectionConstants;
import lli.workOrder.LLIWorkOrder;
import location.ZoneDAO;
import login.LoginDTO;
import login.LoginService;
import lombok.extern.log4j.Log4j;
import notification.NotificationDTO;
import notification.NotificationDTOConditionBuilder;
import notification.NotificationService;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import org.bouncycastle.util.Arrays;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplication;
@Log4j
public class LLIApplicationService implements NavigationService {

    @DAO private LLIApplicationDAO lliApplicationDAO;
    @DAO private ZoneDAO zoneDAO;
    @Service private LLIConnectionService lliConnectionService;
    @Service private CommentsService commentsService;
    @Service private GlobalService globalService;
    @Service private BillService billService;
    @Service
    IFRService ifrService;
    @Service
    NewLocalLoopService newlocalLoopService;
    private OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    private LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    private NewOfficeService newOfficeService = ServiceDAOFactory.getService(NewOfficeService.class);
    private ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    private EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    private OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    private BandwidthConfigurationService bandwidthConfigurationService = ServiceDAOFactory.getService(BandwidthConfigurationService.class);
    private NotificationService notificationService = ServiceDAOFactory.getService(NotificationService.class);

    @Service private UserService userService;
    // Todo: to be edited my bony vai
    public static long distance = 225;

    @Transactional
    public void setInsertStageComment(LLIApplication lliApplication,LoginDTO loginDTO) throws Exception {
        Comments comments=new Comments();
        comments.setApplicationID(lliApplication.getApplicationID());
        comments.setStateID(lliApplication.getState());
        comments.setUserID(loginDTO.getUserID());
        comments.setComments(lliApplication.getComment());
        comments.setSubmissionDate(System.currentTimeMillis());
        comments.setSequenceID(1);
        commentsService.insertComments(comments,loginDTO);
    }



    @Transactional
    public void sendNotification(LLIApplication application,int nextState, LoginDTO loginDTO) throws Exception {


        boolean isForClient=false;
        if(loginDTO.getRoleID()>0 && loginDTO.getRoleID()==RoleConstants.VENDOR_ROLE){
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getApplicationID(), loginDTO.getRoleID(), false,loginDTO.getUserID());

        }
        else if(loginDTO.getRoleID() > 0){

            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getApplicationID(), loginDTO.getRoleID(), false);

            if(loginDTO.getRoleID()==RoleConstants.LDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getApplicationID(), RoleConstants.CDGM_ROLE, false);
            }
            if(loginDTO.getRoleID()==RoleConstants.CDGM_ROLE){
                notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getApplicationID(), RoleConstants.LDGM_ROLE, false);
            }

        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_LLI, application.getApplicationID(), loginDTO.getAccountID(), false);
        }

        //check if any remaining notification haven't taken action

        List<NotificationDTO> notificationDTOS=globalService.getAllObjectListByCondition(NotificationDTO.class,
                new NotificationDTOConditionBuilder()
                .Where()
                .moduleIdEquals((long) ModuleConstants.Module_ID_LLI)
                .entityIdEquals(application.getApplicationID())
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
                        application.getApplicationID(),

                        "LLI Application",
                        LLIConnectionConstants.LLI_DETAILS_PAGE_URL,
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
                            if (application.getZoneId() > 0) {

                                if (zones.contains(application.getZoneId())) {
                                    userIdsForNotification.add(userDTO.getUserID());
                                }
                            }

                        } else if (userDTO.getRoleID() == RoleConstants.VENDOR_ROLE) {
                            ArrayList<Integer> quataion=new ArrayList<>();
                            quataion.add(1);
                            quataion.add(0);
                            List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                                    new EFRConditionBuilder()
                                            .Where()
                                            .vendorIDEquals(userDTO.getUserID())
                                            .quotationStatusIn(quataion)
                                            .applicationIDEquals(application.getApplicationID())
                                            .getCondition()
                            );
                            if (efrs.size() > 0) {
                                userIdsForNotification.add(efrs.get(0).getVendorID());
                            }

                        } else {

                            userIdsForNotification.add(userDTO.getUserID());


                        }
                    }
                    for (Long userId : userIdsForNotification) {
                        notificationService.saveNotificationForNextStateUser(
                                isForClient,
                                nextState,
                                application.getClientID(),
                                userId,
                                ModuleConstants.Module_ID_LLI,
                                application.getApplicationID(),

                                "LLI Application",
                                LLIConnectionConstants.LLI_DETAILS_PAGE_URL,
                                false,
                                0
                        );

                    }
                }

            }
        }


    }








    @Transactional
    public void setDemandNoteId(long demandNoteID, long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No lli application found with application ID " + applicationID);
        }
        // TODO: update connection demand note; ends here
        if (!lliApplication.isDemandNoteNeeded()) {
            throw new RequestFailureException(
                    "No demand note is needed for applicaton with application ID " + applicationID);
        }
        if (lliApplication.getDemandNoteID() != null) {
            throw new RequestFailureException(
                    "Demand note has already been created for application with application ID " + applicationID);
        }
        lliApplication.setDemandNoteID(demandNoteID);
        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);
        lliApplicationDAO.updateLLIApplication(lliApplication);

    }

    /*
     * Handles submission of the forms which come after redirection from view page
     */
    @Transactional
    public void insertApplication(LLIApplication lliApplication, LoginDTO loginDTO) throws Exception {

        if (!loginDTO.getIsAdmin() && lliApplication.getClientID() != loginDTO.getAccountID()) {
            throw new RequestFailureException("You can not submit other client's application.");
        }
        lliApplication.setSubmissionDate(System.currentTimeMillis());
        lliApplication.setStatus(LLIConnectionConstants.STATUS_APPLIED);

        lliApplicationDAO.insertLLIApplication(lliApplication);
    }

    @Transactional
    public void insertNewConnectionApplication(LLINewConnectionApplication lliApplication, LoginDTO loginDTO)
            throws Exception {

        if (!loginDTO.getIsAdmin() && lliApplication.getClientID() != loginDTO.getAccountID()) {
            throw new RequestFailureException("You can not submit other client's application.");
        }

        lliApplicationDAO.insertLLINewConnectionApplication(lliApplication);
    }

    @Transactional
    public void requestForCorrectionApplication(LLIApplication lliApplication) throws Exception {
        LLIApplication existingLLIApplication = getLLIApplicationByApplicationID(lliApplication.getApplicationID());

        if (existingLLIApplication == null) {
            throw new RequestFailureException(
                    "No application found with application ID " + lliApplication.getApplicationID());
        }

        if (existingLLIApplication.getStatus() != LLIConnectionConstants.STATUS_APPLIED) {
            throw new RequestFailureException(
                    "Application can only be sent to client for correction from applied state.");
        }

        existingLLIApplication.setStatus(LLIConnectionConstants.STATUS_REQUESTED_FOR_CORRECTION);
        existingLLIApplication.setRequestForCorrectionComment(lliApplication.getRequestForCorrectionComment());
        ModifiedSqlGenerator.updateEntity(existingLLIApplication);
    }

    @Transactional
    public void rejectApplication(LLIApplication lliApplication) throws Exception {
        LLIApplication existingLliApplication = getLLIApplicationByApplicationID(lliApplication.getApplicationID());

        if (existingLliApplication == null) {
            throw new RequestFailureException(
                    "No application found with application ID " + lliApplication.getApplicationID());
        }
        if (existingLliApplication.getStatus() != LLIConnectionConstants.STATUS_APPLIED) {
            throw new RequestFailureException("Application can only be rejected from applied state.");
        }

        existingLliApplication.setStatus(LLIConnectionConstants.STATUS_REJECTED);
        existingLliApplication.setRejectionComment(lliApplication.getRejectionComment());
        ModifiedSqlGenerator.updateEntity(existingLliApplication);
    }

    private void setImmutablePropertiesForApplicationEdit(LLIApplication lliApplication,
                                                          LLIApplication lastExistingApplication) {

        lliApplication.setApplicationID(lastExistingApplication.getApplicationID());
        lliApplication.setClientID(lastExistingApplication.getClientID());
        lliApplication.setUserID(lastExistingApplication.getUserID());
        lliApplication.setContent(lastExistingApplication.getContent());
        lliApplication.setSubmissionDate(lastExistingApplication.getSubmissionDate());
        lliApplication.setApplicationType(lastExistingApplication.getApplicationType());
        lliApplication.setDemandNoteID(lastExistingApplication.getDemandNoteID());
        lliApplication.setDemandNoteNeeded(lastExistingApplication.isDemandNoteNeeded);
        lliApplication.setServiceStarted(lastExistingApplication.isServiceStarted());
        lliApplication.setRejectionComment(lastExistingApplication.getRejectionComment());
        lliApplication.setRequestForCorrectionComment(lastExistingApplication.getRequestForCorrectionComment());

    }

    @Transactional
    public void editApplication(LLIApplication lliApplication) throws Exception {
        LLIApplication lastLLIApplicationInstance = getLLIApplicationByApplicationID(lliApplication.getApplicationID());
        setImmutablePropertiesForApplicationEdit(lliApplication, lastLLIApplicationInstance);

        lliApplication.setStatus(LLIConnectionConstants.STATUS_APPLIED);
        lliApplicationDAO.updateLLIApplication(lliApplication);
    }

    @Transactional
    public void processApplication(LLIApplication lliApplication) throws Exception {
        LLIApplication lastLLIApplicationInstance = getLLIApplicationByApplicationID(lliApplication.getApplicationID());

        if (lastLLIApplicationInstance == null) {
            throw new RequestFailureException(
                    "No application found with application ID " + lastLLIApplicationInstance.getApplicationID());
        }
        if (lastLLIApplicationInstance.getStatus() != LLIConnectionConstants.STATUS_VERIFIED
                && lastLLIApplicationInstance.getStatus() != LLIConnectionConstants.STATUS_PROCESSED) {
            throw new RequestFailureException(
                    "You can only verify an application from a Application Verified or processed state.");
        }

        lliApplication.setImmutablePropertyWhileProcessing(lastLLIApplicationInstance); // written in extended
        // application dto
        lliApplication.setStatus(LLIConnectionConstants.STATUS_PROCESSED);
        lliApplicationDAO.updateLLIApplication(lliApplication);
    }

    @Transactional
    public void completeApplication(LLIApplication lliApplication) throws Exception {

        LLIApplication lastExistingLLIApplication = getLLIApplicationByApplicationID(lliApplication.getApplicationID());

        if (lastExistingLLIApplication == null) {
            throw new RequestFailureException(
                    "No application found with application ID " + lliApplication.getApplicationID());
        }

        if (lastExistingLLIApplication.isDemandNoteNeeded()) {
            if (lastExistingLLIApplication.getDemandNoteID() == null) {
                throw new RequestFailureException("No Demand Note has been generated");
            }
            BillDTO demandNote = billService.getBillByBillID(lliApplication.getDemandNoteID());
            if (demandNote == null) {
                throw new RequestFailureException("Invalid Demand Note");
            }
            if (demandNote.getPaymentStatus() != BillDTO.PAID_VERIFIED
                    && demandNote.getPaymentStatus() != BillDTO.SKIPPED

            ) {
                throw new RequestFailureException("Demand Note has not been paid/skipped");
            }
        } else {
            // demand note not needed
            if (lastExistingLLIApplication.getStatus() != LLIConnectionConstants.STATUS_FINALIZED) {
                throw new RequestFailureException("This application can only be completed from finalized state.");
            }
        }

        lliApplication.setStatus(LLIConnectionConstants.STATUS_COMPLETED);
        lliApplication.setServiceStarted(true);
        lliApplication.completeApplication(); // written in extended application dto
        lliApplicationDAO.updateLLIApplication(lliApplication);
    }
    /*
     * Handles submission of the forms which come after redirection from view page
     */

    /* Fetch LLI Application begins */
    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getLLIApplicationByApplicationID(long applicationID) throws Exception {
//		LLIApplication lliApplication = lliApplicationDAO.getLLIApplicationByID(applicationID);
        LLIApplication lliApplication = lliApplicationDAO.getFlowLLIApplicationByID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application found with ID " + applicationID);
        }
        return lliApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getFlowLLIApplicationByApplicationID(long applicationID) throws Exception {
        LLIApplication lliApplication = lliApplicationDAO.getFlowLLIApplicationByID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application found with ID " + applicationID);
        }
        return lliApplication;
    }

    //	get application by connection id
    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIApplication> getLLIApplicationByConnectionID(long connectinID) throws Exception {
        List<LLIApplication> lliApplication = lliApplicationDAO.getLLIApplicationByConnectionID(connectinID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application found with connection ID " + connectinID);
        }
        return lliApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getNewLLIApplicationByApplicationID(long applicationID) throws Exception {
        LLIApplication lliApplication = lliApplicationDAO.getLLIApplicationByID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application found with ID " + applicationID);
        }
        return lliApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getNewLLIApplicationByApplicationID(long applicationID, HttpServletRequest request) throws Exception {
        LLIApplication lliApplication = lliApplicationDAO.getLLIApplicationByID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application found with ID " + applicationID);
        }
        return lliApplication;
    }

    // TODO: ?????!!!!!!
    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getLLIApplicationByDemandNoteID(long demandNoteID) throws Exception {
        LLIApplication lliApplication = lliApplicationDAO.getLLIApplicationByDemandNoteID(demandNoteID);

        if (lliApplication == null) {
            return null;
        }

        int applicationType = lliApplication.getApplicationType();
        Class<? extends LLIApplication> childClassObject = LLIConnectionConstants.applicationTypeClassnameMap.get(applicationType);

        if (childClassObject == null) {
            throw new RequestFailureException("No application type found " + "in appllication with application ID "
                    + lliApplication.getApplicationID());
        }

        LLIApplication childApplication = lliApplicationDAO.getChildLLIApplicationByForeignKey(lliApplication.getApplicationID(), childClassObject);

        if (childApplication == null) {
            return lliApplication;
        }

        ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, childApplication, LLIApplication.class);
        return childApplication;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public LLIApplication getNewFlowLLIApplicationByDemandNoteID(long demandNoteID) throws Exception {
        LLIApplication lliApplication = lliApplicationDAO.getLLIApplicationByDemandNoteID(demandNoteID);


        return lliApplication;
    }

    /* Fetch LLI Application ends */

    /* Search Application begins */
    @SuppressWarnings("rawtypes")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {

        return lliApplicationDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//		return lliApplicationDAO.getLLIApplicationListByIDList((List<Long>) recordIDs);
        List<LLIApplication> list = lliApplicationDAO.getLLIApplicationListByIDList((List<Long>) recordIDs);
        LoginDTO loginDTO = (LoginDTO) objects[0];
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);
        Map<Integer, FlowState> flowState = FlowRepository.getInstance().getActionableFlowStatesByRoleId(roleId)
                .stream()
                .distinct()
                .collect(Collectors.toMap(FlowState::getId, Function.identity()));

        Comparator<LLIApplication> compareByHasPermissionAndApplicationId =
                Comparator.comparing(LLIApplication::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(LLIApplication:: getApplicationID, Comparator.reverseOrder());

        return list.stream()
                .peek(lliApplication->{
                    FlowState flowState1 = flowState.getOrDefault(lliApplication.getState(), null);
                    lliApplication.setHasPermission(null != flowState1);
                    FlowState state = FlowRepository.getInstance().getFlowStateByFlowStateId(lliApplication.getState());
                    lliApplication.setStateDescription(state.getViewDescription());
                    lliApplication.setColor(state.getColor());

                })
                .sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());
    }
    /* Search Application ends */

    /* State Action begins */
    public List<LLIActionButton> getAvailableActions(long applicationID, LoginDTO loginDTO) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        int status = lliApplication.getStatus();

        List<LLIActionButton> buttonList = new ArrayList<LLIActionButton>();

        if (loginDTO.getIsAdmin()) {
            List<LLIActionButton> listOfActionButtons = LLIConnectionConstants.applicationActionMap.getOrDefault(status,
                    new ArrayList<>());
            buttonList = new ArrayList<>(listOfActionButtons);

            if (status == LLIConnectionConstants.STATUS_FINALIZED) {
                if (lliApplication.isDemandNoteNeeded()) {
                    if (lliApplication.getApplicationType() == LLIConnectionConstants.BREAK_LONG_TERM) {
                        buttonList.add(new LLIActionButton("Generate Short Bill", "lli/dn/new.do", true));
                    } else {
                        buttonList.add(new LLIActionButton("Generate Demand Note", "lli/dn/new.do", true));
                    }
                } else {
                    buttonList.add(new LLIActionButton("Complete Request",
                            "lli/application/application-complete-request.do", true));
                }
            }
        } else {
            if (status == LLIConnectionConstants.STATUS_REQUESTED_FOR_CORRECTION) {
                buttonList.add(new LLIActionButton("Edit", "lli/application/application-edit.do", true));
            }
        }

        return buttonList;
    }

    @Transactional
    public void verifyApplication(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No application found with application ID " + applicationID);
        }

        int[] allowedPreviousStates = {LLIConnectionConstants.STATUS_APPLIED};
        if (!Arrays.contains(allowedPreviousStates, lliApplication.getStatus())) {
            throw new RequestFailureException("You can not verify application from "
                    + LLIConnectionConstants.applicationActionMap.get(lliApplication.getStatus()) + " status");
        }

        lliApplication.setStatus(LLIConnectionConstants.STATUS_VERIFIED);
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }

    @Transactional
    public void finalizeApplication(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No application found with application ID " + applicationID);
        }
        if (lliApplication.getStatus() != LLIConnectionConstants.STATUS_PROCESSED) {
            throw new RequestFailureException("You can only finalize an application from a processed state.");
        }

        lliApplication.setStatus(LLIConnectionConstants.STATUS_FINALIZED);
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }

    @Transactional
    public void setApplicationStatusToDemandNoteGenerated(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        if (lliApplication == null) {
            throw new RequestFailureException("No application found with application ID " + applicationID);
        }
        if (lliApplication.getStatus() != LLIConnectionConstants.STATUS_FINALIZED) {
            throw new RequestFailureException("Demand note can only be created from finalized state.");
        }

        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }

    @Transactional
    public void setApplicationStatusToDemandNotePaid(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        lliApplication.setStatus(LLIConnectionConstants.STATUS_PAYMENT_CLEARED);
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }

    @Transactional
    public void completeRequestApplication(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        lliApplication.setStatus(LLIConnectionConstants.STATUS_COMPLETED);
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }
    /* State Action ends */

    /* Mapping Name Forwarding begins */
    public String editApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getLLIApplicationByApplicationID(applicationID).getApplicationType();
        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType)
                + "-edit";
    }

    public String processApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getLLIApplicationByApplicationID(applicationID).getApplicationType();
        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType)
                + "-process";
    }

    public String viewApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getLLIApplicationByApplicationID(applicationID).getApplicationType();
        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType)
                + "-view";
    }

    public String viewNewApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getFlowLLIApplicationByApplicationID(applicationID).getApplicationType();
//        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType) + "-view";
        return "lli-application-new-connection-view";
//		return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType)+ "-view";
    }

    public String completeApplicationMappingForwardName(long applicationID) throws Exception {
        int applicationType = getFlowLLIApplicationByApplicationID(applicationID).getApplicationType();
        return "lli-application-" + LLIConnectionConstants.applicationTypeHyphenSeperatedMap.get(applicationType)
                + "-completion";
    }

    /* Mapping Name Forwarding ends */

    /* Content Analysis Begins */
    public LLIConnectionInstance getLLIConnectionFromApplicationContent(LLIApplication lliApplication) {

        long connectionId = lliApplication.getConnectionId();

        try {
            return lliConnectionService.getLLIConnectionByConnectionID(connectionId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
		/*
		String content = lliApplication.getContent();
		
		// TODO: handle null json
		JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
		LLIConnectionInstance lliConnectionInstance = new GsonBuilder()
				.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer())
				.registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer())
				.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer()).create()
				.fromJson(jsonObject, LLIConnectionInstance.class);

		return lliConnectionInstance;
		*/
    }
    /* Content Analysis ends */

    @Transactional
    public void setApplicationAsPaymentClearedByDemandNoteID(long demandNoteID) throws Exception {
        LLIApplication lliApplication = getNewFlowLLIApplicationByDemandNoteID(demandNoteID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Such LLI Application found");
        }

        if (lliApplication.getStatus() == LLIConnectionConstants.STATUS_PAYMENT_CLEARED)
            return;

        if (lliApplication.getStatus() != LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED) {
            throw new RequestFailureException("Application can only be set to payment cleared "
                    + "from demand note generated state.");
        }

        lliApplication.setStatus(LLIConnectionConstants.STATUS_PAYMENT_CLEARED);
        lliApplicationDAO.updateLLIApplication(lliApplication);

        // flow: change application state --> PAYMENT_VERIFIED
//		new FlowService().paymentVerified(lliApplication.getApplicationID());
    }

    @Transactional
    public void setApplicationAsDemandNoteGeneratedByDemandNoteID(long demandNoteID) throws Exception {
        LLIApplication lliApplication = getNewFlowLLIApplicationByDemandNoteID(demandNoteID);
        if (lliApplication == null) {
            throw new RequestFailureException("No Such LLI Application found");
        }
        lliApplication.setStatus(LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED);
        lliApplicationDAO.updateLLIApplication(lliApplication);

    }

    @Transactional
    public int getApplicationTypeByApplicationID(long applicationID) throws Exception {
        LLIApplication lliApplication = getLLIApplicationByApplicationID(applicationID);
        return lliApplication.getApplicationType();
    }

    @Transactional
    public void updateApplicaton(LLIApplication lliApplication) throws Exception {
        lliApplicationDAO.updateLLIApplication(lliApplication);
    }

    @Transactional
    public void updateApplicatonState(long applicatonID, int applicationState) throws Exception {
        lliApplicationDAO.updateLLIApplicationState(applicatonID, applicationState);
    }

    public LLIConnectionInstance getConnectionFromShiftBandwidthApplicationContent(
            LLIShiftBandwidthApplication lliShiftBandwidthApplication, int connectionIndex) {


        long connectionId = lliShiftBandwidthApplication.getConnectionId();

        try {
            return lliConnectionService.getLLIConnectionByConnectionID(connectionId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
		/*
		String content = lliShiftBandwidthApplication.getContent();
		JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();

		JsonObject connection = connectionIndex == 0 ? jsonObject.get("sourceConnection").getAsJsonObject()
				: jsonObject.get("destinationConnection").getAsJsonObject();
		LLIConnectionInstance lliConnectionInstance = new GsonBuilder()
				.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer())
				.registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer())
				.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer()).create()
				.fromJson(connection, LLIConnectionInstance.class);

		return lliConnectionInstance;
		*/
    }

    public void validateShiftBandwidthApplication(LLIConnectionInstance sourceConnection,
                                                  LLIConnectionInstance destinationConnection) throws Exception {
        lliConnectionService.validateLLIConnectionInstance(sourceConnection);
        lliConnectionService.validateLLIConnectionInstance(destinationConnection);

        LLIConnectionInstance existingSourceConnection = lliConnectionService
                .getLLIConnectionByConnectionID(sourceConnection.getID());
        LLIConnectionInstance existingDestinationConnection = lliConnectionService
                .getLLIConnectionByConnectionID(destinationConnection.getID());

        if (existingSourceConnection.getBandwidth()
                + existingDestinationConnection.getBandwidth() != sourceConnection.getBandwidth()
                + destinationConnection.getBandwidth()) {
            throw new RequestFailureException("The sum of two connections must be same as before.");
        }
    }


    public WorkOrderDTO getWorkOrderDTO(int applicationId, int loggedInUserId) {
        WorkOrderDTO workOrderDTO = new WorkOrderDTO();
        try {
            workOrderDTO.setLliApplication(getLLIApplicationByApplicationID(applicationId));
            workOrderDTO.setClientDTO(clientService.getClientInfoFromApplicationId(applicationId));
            workOrderDTO.setUserDTO(userService.getUserDTOByUserID(loggedInUserId));
            List<EFR> efrs = efrService.getSelected(applicationId);
            workOrderDTO.setEfrs(efrs);

            UserDTO tempUserDTO;
            List<UserDTO> vendors = new ArrayList<>();
            if (efrs != null) {
                for (EFR efr : efrs) {
                    if ((tempUserDTO = userService.getUserDTOByUserID(efr.getVendorID())) != null)
                        vendors.add(tempUserDTO);
                }
            }

            workOrderDTO.setVendors(vendors);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return workOrderDTO;
    }

    // TODO: generate a file path string
    // TODO: check access authorization of the file
    private void generateWorkOrderDocuments(long applicationId, LoginDTO loginDTO) {
        List<EFR> efrs = efrService.getSelected(applicationId);
        ClientDTO clientDTO = clientService.getClientInfoFromApplicationId(applicationId);
        Map<Long, List<EFR>> mapOfSelectedEFRsToVendorId = efrs.stream()
                .collect(
                        Collectors.groupingBy(EFR::getVendorID)
                );
        log.info("Generating All Work Orders");
        mapOfSelectedEFRsToVendorId.entrySet()
                .forEach(t-> {
                    try {
                        workorderTask(applicationId, clientDTO.getClientId(),loginDTO , t);
                    } catch (Exception e) {
                        log.fatal(e.getMessage());
                        e.printStackTrace();
                    }
                });
        log.info("Done All Work Orders");
    }

    @Transactional
    public void generateAdviceNoteDocument(long appId, int state, JsonArray userArray, long senderId) throws Exception {
        ConnectionAdviceNote adviceNote = generateAN(userArray, state, appId, senderId);
        //create pdf
        AsyncPdfService.getInstance().accept(adviceNote);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public ConnectionAdviceNote generateAN(JsonArray userArray, int state, long appId, long senderId) throws Exception {
        //get CC List
        List<RecipientElement> ccList = officialLetterService.getCCRecipientElements(userArray);

        //get To List
        List<RecipientElement> toList = officialLetterService.getToRecipientElements(state);

        //create officialLetter
        LLIApplication application = getLLIApplicationByApplicationID(appId);

        ConnectionAdviceNote adviceNote = new ConnectionAdviceNote(appId, application.getClientID());

        //create recipient list
        List<RecipientElement> recipientElements = Stream.concat(toList.stream(), ccList.stream()).collect(Collectors.toList());

        //save official doc
        officialLetterService.saveOfficialLetterTransactionalDefault(adviceNote, recipientElements, senderId);
        return adviceNote;
    }


    //	@Transactional
    public void newApplicationInsert(LLIApplication application) throws Exception {
        LLINewConnectionApplication lliNewConnectionApplication = new LLINewConnectionApplication();

        lliNewConnectionApplication.setOfficeList(application.getOfficeList());
        lliNewConnectionApplication.setDuration(application.getDuration());
//		lliNewConnectionApplication.setApplicationID(application.getApplicationID());
        lliNewConnectionApplication.setBandwidth(application.getBandwidth());
        lliNewConnectionApplication.setLoopProvider(application.getLoopProvider());
        lliNewConnectionApplication.setSuggestedDate(application.getSuggestedDate());
        lliNewConnectionApplication.setExtendedApplicationID(application.getApplicationID());

        System.out.println("======================before insert(new connection application)====================");
        lliApplicationDAO.newApplicationInsert(lliNewConnectionApplication);
        System.out.println("======================after insert(new connection application)====================");


//		lliApplicationDAO.updateLLIApplicationState(applicatonID, applicationStte);
    }

    public void upgradeApplicationInsert(LLIApplication application) throws Exception {
        LLIUpgradeBandwidthApplication lliNewConnectionApplication = new LLIUpgradeBandwidthApplication();

        lliNewConnectionApplication.setDuration(application.getDuration());
//		lliNewConnectionApplication.setApplicationID(application.getApplicationID());
        lliNewConnectionApplication.setBandwidth(application.getBandwidth());
        lliNewConnectionApplication.setLoopProvider(application.getLoopProvider());
        lliNewConnectionApplication.setSuggestedDate(application.getSuggestedDate());
        lliNewConnectionApplication.setExtendedApplicationID(application.getApplicationID());
        lliNewConnectionApplication.setConnectionID(application.getConnectionId());

        System.out.println("======================before insert(new connection application)====================");
        lliApplicationDAO.newUpgradeInsert(lliNewConnectionApplication);
        System.out.println("======================after insert(new connection application)====================");


//		lliApplicationDAO.updateLLIApplicationState(applicatonID, applicationStte);
    }


    @Transactional
    void workorderTask(long appId, long clientId, LoginDTO loginDTO, Map.Entry<Long, List<EFR>> entry)
            throws Exception {
        long vendorId = entry.getKey();
        List<EFR> selectedEFRs = entry.getValue();
        log.info("Generating work order for vendor id " + vendorId);

        LLIWorkOrder lliWorkOrder = new LLIWorkOrder(appId, clientId, vendorId, selectedEFRs );
        List<RecipientElement> recipientElements = officialLetterService.getWorkOrderSpecificRecipientElement(vendorId, clientId, loginDTO.getUserID());
        officialLetterService.saveOfficialLetterTransactionalIndividual(lliWorkOrder,recipientElements, loginDTO.getUserID());
        selectedEFRs.forEach(t->{
            t.setWorkOrderNumber(lliWorkOrder.getId());
            globalService.update(t);
        });
        AsyncPdfService.getInstance().accept(lliWorkOrder);
        log.info("Done work order for vendor id " + vendorId);
    }

    @Transactional
    int closeEFRGeneate(LLIApplication lliApplication, int state, LoginDTO loginDTO) throws Exception {
        List<LLIApplication> lliApplications = getLLIApplicationByConnectionID(lliApplication.getConnectionId());
        int efrSize = 0;
        for (LLIApplication lliApplication1 : lliApplications) {

            if (lliApplication1.getApplicationID() == lliApplication.getApplicationID()) {

            } else {
                List<EFR> efrs = efrService.getHistoricalCompletedEFR(lliApplication1.getApplicationID());
                if (efrs.size() > 0) {
                    efrSize += efrs.size();
                    for (EFR efr : efrs
                    ) {

                        EFR newEFR = efr;
                        newEFR.setApplicationID(lliApplication.getApplicationID());
                        newEFR.setParentEFRID(efr.getId());
                        newEFR.setProposedLoopDistance(efr.getActualLoopDistance());
                        newEFR.setWorkCompleted(0);
                        newEFR.setState(state);
                        efrService.insertApplication(efr, loginDTO);

                    }

//                    lliApplicationService.generateWorkOrderDocuments(lliApplication1.getApplicationID(), loginDTO.getUserID());
                }
            }

        }
        return efrSize;
    }

    public void policyCheck(LLIApplication lliApplication) throws Exception {
        if ((lliApplication.getPolicyType() > 2 || lliApplication.getPolicyType() < 0)) {
            throw new RequestFailureException("The suggested Policy Type doesn't match any configuration policy.");
        }
        if (lliApplication.getPolicyType() == LLIConnectionConstants.POLICY_ACCORDING_SETTING) {
            List<BandwidthConfiguration> list = bandwidthConfigurationService.getBandwidthConfiguration((long) lliApplication.getBandwidth());
            if (list.size() > 0) {
                Date date = new Date();
                long currentTimeMillis = date.getTime();
                if (list.get(0).getDuration() + currentTimeMillis > lliApplication.getSuggestedDate()) {
                    throw new RequestFailureException("The suggested date should be greater the configuration policy.");
                }
            }
        }
    }

    public void cachePolicyCheck(LLIApplication lliApplication) throws Exception {
        if (lliApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE) {
            double totalExistingCacheBandwidth = 0, totalRegularBandwidth = 0;
            try {
                totalRegularBandwidth = ServiceDAOFactory.getService(LLIConnectionService.class).getTotalExistingRegularBWByClientID(lliApplication.getClientID());
                totalExistingCacheBandwidth = ServiceDAOFactory.getService(LLIConnectionService.class).getExistingTotalBandwidthByClientID(lliApplication.getClientID());
            } catch (Exception e) {
                e.printStackTrace();
            }

            double requestedBandwidth = lliApplication.getBandwidth();
            if ((totalExistingCacheBandwidth - requestedBandwidth) * 2.0 < totalRegularBandwidth) {
                throw new RequestFailureException("Amount of Transmission Bandwidth for Cache should be minimum half of existing internet bandwidth.");
            }
//                lliApplicationValidationService.validatePositiveNumber(application, "duration", "Duration");
        }
    }

    public LLIApplication closeConnectionStateSet(LLIApplication lliApplication, LoginDTO loginDTO) throws Exception {

        if (lliApplication.getPolicyType() == LLIConnectionConstants.POLICY_ACCORDING_SETTING) {

            int efrSize = closeEFRGeneate(lliApplication, 0, loginDTO);
            if (efrSize > 0) {
                lliApplication.setState(LLIConnectionConstants.CLOSE_CONNECTION_POLICY_STATE);
            } else {
                lliApplication.setState(LLIConnectionConstants.CLOSE_CONNECTION_POLICY_BYPASS_STATE);
            }

        } else if (lliApplication.getPolicyType() == LLIConnectionConstants.POLICY_INSTANT) {
            lliApplication.setState(LLIConnectionConstants.CLOSE_CONNECTION_STATE);

        }
        return lliApplication;
    }

    public LLIApplication downgradeConnectionStateSet(LLIApplication lliApplication) throws Exception {

        if (lliApplication.getPolicyType() == LLIConnectionConstants.POLICY_ACCORDING_SETTING) {
            lliApplication.setState(LLIConnectionConstants.DOWNGRADE_BANDWIDTH_POLICY_STATE);
        } else if (lliApplication.getPolicyType() == LLIConnectionConstants.POLICY_INSTANT) {
            lliApplication.setState(LLIConnectionConstants.DOWNGRADE_BANDWIDTH_STATE);
        }
        return lliApplication;
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void updateLocalLopps(List<LocalLoop> localLoops) {
        LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
        localLoops.forEach(localLoop1 -> {
            try {
                localLoopService.updateApplicaton(localLoop1);
            } catch (Exception e) {
                throw new RequestFailureException("Error updating local loop " + localLoop1.toString());
            }
        });
    }

    @Transactional
    public void handleConnectionCompleteRequest(List<LocalLoop> localLoops, JsonObject jsonObject, JsonElement jsonElement, long appTypeID, long appID, int state, LoginDTO loginDTO) throws Exception {
        updateLocalLopps(localLoops);
        ServiceDAOFactory.getService(LLIApplicationFlowConnectionManagerService.class).connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        ServiceDAOFactory.getService(CommentsService.class).insertComments(comments, loginDTO);
        updateApplicatonState(appID, state);
    }

    @Transactional
    public void handleNewConnectionComplete(JsonElement jsonElement,LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID").getAsLong();
        ServiceDAOFactory.getService(LLIApplicationFlowConnectionManagerService.class)
                .connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);

        updateApplicatonState(appID, state);

    }

    @Transactional
    public void handleNewConnectionCompleteAdditionalIP(JsonElement jsonElement,LoginDTO loginDTO) throws Exception {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID").getAsLong();
        ServiceDAOFactory.getService(LLIApplicationFlowConnectionManagerService.class)
                .connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);
        updateApplicatonState(appID, state);
    }

    @Transactional
    public void handleEFRInsertLocalLoop(JsonElement jelement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jelement.getAsJsonObject();
        JsonElement jsonElement = jsonObject.getAsJsonArray("ifr");

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = getFlowLLIApplicationByApplicationID(appID);

        ArrayList<IFR> list1 = new IFRDeserializer().deserialize_ifr_update_for_new_local_loop(jelement);
        ArrayList<IFR> selectedIFR = new ArrayList<>();

        for (IFR ifr : list1) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            } else {
                ifr.setIsSelected(LLIConnectionConstants.IFR_IGNORED);
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ServiceDAOFactory.getService(IFRService.class).updateApplicaton(ifr);
        }

        List<NewLocalLoop> localLoops = ServiceDAOFactory.getService(NewLocalLoopService.class).getLocalLoop(appID);
        if (localLoops.size() == 0) {
            localLoops = ServiceDAOFactory.getService(NewLocalLoopService.class).prepareLocalloopFromIFR(selectedIFR);
            for (NewLocalLoop localLoop : localLoops) {
                ServiceDAOFactory.getService(NewLocalLoopService.class).insertApplication(localLoop);
            }
        }


        ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom_new_local_loop(jelement);
        for (EFR efr : lists) {
            efr.setIsForwarded(lliApplication.getIsForwarded());
            efrService.insertApplication(efr, loginDTO);
        }

        updateApplicatonState(appID, state);//this need to update state
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        sendNotification(lliApplication,state,loginDTO);
        ServiceDAOFactory.getService(CommentsService.class).insertComments(comments, loginDTO);
    }

    @Transactional
    public void insertAdditionalLocalLoopApplicationBatchOperation(JsonObject jsonObject, LoginDTO loginDTO) throws Exception {
        LLIApplication lliApplication = new LLIApplicationDeserializer().additional_local_loop(jsonObject);


        int isReuse = jsonObject.get("isReuse") != null ? jsonObject.get("isReuse").getAsJsonObject().get("ID").getAsInt() : -1;
        int selectOld=jsonObject.get("selectOld")!=null?jsonObject.get("selectOld").getAsJsonObject().get("ID").getAsInt():-1;



        if(isReuse==LLIConnectionConstants.REUSE_ADDITIONAL_LOOP
                ||isReuse==LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP
        ){
            if(jsonObject.get("portCount").getAsLong()>1){
                throw new RequestFailureException("Port Count must be 1 while reuse or replacing existing loop");
            }
        }

        if (loginDTO.getIsAdmin()) lliApplication.setUserID(loginDTO.getUserID());
        if (lliApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL
                && isReuse!=LLIConnectionConstants.REUSE_ADDITIONAL_LOOP) {
            lliApplication.setState(LLIConnectionConstants.NEW_LOCAL_LOOP_BTCL_STATE);
            lliApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);

        } else if (lliApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_CLIENT &&
                (isReuse==LLIConnectionConstants.REUSE_ADDITIONAL_LOOP
                ||isReuse==LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP)
        ) {
            lliApplication.setState(LLIConnectionConstants.LOOP_PROVIDER_CLIENT_STATE);
            lliApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_PORT);
        }

        else if (lliApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL &&
                (isReuse==LLIConnectionConstants.REUSE_ADDITIONAL_LOOP||
                isReuse==LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP)
        ) {
            lliApplication.setState(LLIConnectionConstants.NEW_LOCAL_LOOP_BTCL_STATE);
            lliApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);
        }


        // todo: loop btcl with reuse
        //LLIConnection lliConnection = lliConnectionService.getC(lliApplication.getConnectionId());
        lliApplication.setDemandNoteNeeded(true);

        lliApplication.setSubmissionDate(System.currentTimeMillis());
        lliApplication.setUserID(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : 0);
        insertApplication(lliApplication, loginDTO);
        NewOffice office = null;
        if (lliApplication.getNewOfficeList().size() > 0) {
            office = lliApplication.getNewOfficeList().get(0);
            office.setHistoryId(office.getId());
            office.setApplicationId(lliApplication.getApplicationID());
            office.setConnectionID(lliApplication.getConnectionId());
            newOfficeService.insertOffice(office);
        }
        if (isReuse > 0 && isReuse != LLIConnectionConstants.ADD_NEW_ADDITIONAL_LOOP) {
            JsonArray loopAry = (JsonArray) jsonObject.get("selectedLoopList");

            if(loopAry.size()>1){

                throw new RequestFailureException("You should select only one loop to Replace or Reuse");


            }


            List<NewLocalLoop> localLoops = new ArrayList<>();
            for (JsonElement jsonElement1 : loopAry) {
                JsonObject loopObject = jsonElement1.getAsJsonObject();
                NewLocalLoop newLocalLoop = new NewLocalLoop();
                newLocalLoop.setOldLoopId(loopObject.get("historyID").getAsLong());
                newLocalLoop.setIsRemovable(isReuse);
                newLocalLoop.setApplicationID(lliApplication.getApplicationID());
                newLocalLoop.setOfficeID(office.getId());
                newLocalLoop.setPopID(loopObject.get("popID").getAsLong());
//                newLocalLoop.setPortID(loopObject.get("portID").getAsLong());
                newLocalLoop.setPortID(0);
//                newLocalLoop.setRouter_switchID(loopObject.get("router_switchID").getAsLong());
                newLocalLoop.setRouter_switchID(0);
//                newLocalLoop.setVLANID(loopObject.get("VLANID").getAsLong());
                newLocalLoop.setVLANID(0);
                ServiceDAOFactory.getService(NewLocalLoopService.class).insertApplication(newLocalLoop);
            }
        }


        AdditionalPort additionalPort = new AdditionalPort().deserialize(lliApplication,isReuse,selectOld);
        ServiceDAOFactory.getService(LLIAdditionalPortService.class).insertAdditionalPortApplication(additionalPort);



        Comments comments = new Comments();
        comments.setSubmissionDate(lliApplication.getSubmissionDate());
        if (lliApplication.getUserID() == null) {
            comments.setUserID(-1);
        } else {
            comments.setUserID(lliApplication.getUserID());
        }
        comments.setStateID(lliApplication.getState());
        comments.setComments(lliApplication.getComment());
        comments.setApplicationID(lliApplication.getApplicationID());
        ServiceDAOFactory.getService(CommentsService.class).insertComments(comments, loginDTO);
        sendNotification(lliApplication,lliApplication.getState(),loginDTO);
    }

    @Transactional
    void handleWOGenerationRequest(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();



        //



        LLIApplication lliApplication = getLLIApplicationByApplicationID(appID);
        if(lliApplication.getApplicationType()==LLIConnectionConstants.CLOSE_CONNECTION){  //newly introduced
            if (
                    lliApplication.getState() != LLIConnectionConstants.CLOSE_CONNECTION_POLICY_STATE &&
                    lliApplication.getState() != LLIConnectionConstants.CLOSE_CONNECTION_POLICY_REOPEN_STATE
            ) {
                int efrSize = closeEFRGeneate(lliApplication, state, loginDTO);
            }
        }
        //
        generateWorkOrderDocuments(appID, loginDTO);
        updateApplicatonState(appID, state);
        sendNotification(lliApplication,state,loginDTO);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
    }
}
