package coLocation.application;

import annotation.DAO;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.adviceNote.ColocationAdviceNote;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.demandNote.CoLocationBillUtilityService;
import coLocation.ifr.CoLocationApplicationIFRDTO;
import coLocation.ifr.CoLocationApplicationIFRService;
import coLocation.inventory.CoLocationInventoryTemplateService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.ModuleConstants;
import common.RequestFailureException;
import common.pdf.AsyncPdfService;
import flow.FlowService;
import flow.entity.FlowState;
import flow.entity.Role;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import login.LoginDTO;
import login.LoginService;
import lombok.extern.log4j.Log4j;
import notification.NotificationService;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Log4j
public class CoLocationApplicationService implements NavigationService {

    @DAO private CoLocationApplicationDAO coLocationApplicationDAO;
    @Service private CommentsService commentsService;
    @Service private FlowService flowService;
    @Service private NotificationService notificationService;
    @Service private GlobalService globalService;
    @Service private OfficialLetterService officialLetterService;
    @Service private CoLocationApplicationIFRService ifrService;
    @Service private CoLocationApplicationService coLocationApplicationService;
    @Service private CoLocationInventoryTemplateService coLocationInventoryTemplateService;
    @Service private CoLocationConnectionService coLocationConnectionService;

    @Transactional
    public void sendNotification(CoLocationApplicationDTO application,int nextState, LoginDTO loginDTO) throws Exception {
        boolean isForClient = false;

        if (loginDTO.getRoleID() > 0) {
            //todo support parallel actions

            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_COLOCATION, application.getApplicationID(), loginDTO.getRoleID(), false,loginDTO.getUserID());

        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_COLOCATION, application.getApplicationID(), loginDTO.getAccountID(), false,loginDTO.getUserID());

        }

        HashMap<Long, Role> uniqueRoles = notificationService.getNextStateRoleListForNotification(nextState);
        if (uniqueRoles.isEmpty()) {
            isForClient = true;
            notificationService.saveNotificationForNextStateUser(
                    isForClient,
                    nextState,
                    application.getClientID(),
                    -1,
                    ModuleConstants.Module_ID_COLOCATION,
                    application.getApplicationID(),

                    "CoLocation Application",
                    CoLocationConstants.COLOCATION_DETAILS_PAGE_URL,
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
                for (Long userId : userIdsForNotification)
                {
                    notificationService.saveNotificationForNextStateUser(
                            isForClient,
                            nextState,
                            application.getClientID(),
                            userId,
                            ModuleConstants.Module_ID_COLOCATION,
                            application.getApplicationID(),

                            "CoLocation Application",
                            CoLocationConstants.COLOCATION_DETAILS_PAGE_URL,
                            false,
                            0
                    );

                }
            }

        }


    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationApplicationDTO> getAllColocationApplication() throws Exception {
        List<CoLocationApplicationDTO> coLocationApplicationDTOS = coLocationApplicationDAO.getAllColocationApplication();
        if (coLocationApplicationDTOS == null) {
            throw new RequestFailureException("No Data found ");
        }

        return coLocationApplicationDTOS;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationApplicationDTO getColocationApplication(long applicationID) throws Exception {
        return coLocationApplicationDAO.getColocationApplication(applicationID)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No CoLocation Application found for app id " + applicationID));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationApplicationDTO getColocationApplicationByDemandNoteId(long demandNoteID) throws Exception {
        return coLocationApplicationDAO.getColocationApplicationByDemandNoteId(demandNoteID)
                                .stream()
                                .findFirst()
                                .orElseThrow(()->new RequestFailureException("No CoLocation Application found by demand note id " + demandNoteID));
    }

    @Transactional
    public void insertApplication(CoLocationApplicationDTO coLocationApplicationDTO, LoginDTO loginDTO) throws Exception {
        coLocationApplicationDAO.insertCoLocationApplication(coLocationApplicationDTO);
        sendNotification(coLocationApplicationDTO,coLocationApplicationDTO.getState(),loginDTO);
    }

    @Transactional
    public void insertApplication(CoLocationApplicationDTO coLocationApplicationDTO) throws Exception {
        coLocationApplicationDAO.insertCoLocationApplication(coLocationApplicationDTO);
    }

    @Transactional
    public void updateApplicaton(CoLocationApplicationDTO coLocationApplicationDTO,LoginDTO loginDTO) throws Exception {
        coLocationApplicationDAO.updateCoLocationApplication(coLocationApplicationDTO);
        sendNotification(coLocationApplicationDTO,coLocationApplicationDTO.getState(),loginDTO);
    }

    @Transactional
    public void updateApplicaton(CoLocationApplicationDTO coLocationApplicationDTO) throws Exception {
        coLocationApplicationDAO.updateCoLocationApplication(coLocationApplicationDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        //        Collections.sort(list, new Comparator<CoLocationApplicationDTO>() {
//            @Override
//            public int compare(CoLocationApplicationDTO coLocationApplicationDTO, CoLocationApplicationDTO t1) {
//                return Boolean.compare(coLocationApplicationDTO.isHasPermission(), t1.isHasPermission());
//            }
//        });
        return (List<CoLocationApplicationDTO>) coLocationApplicationDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        List<CoLocationApplicationDTO> colocationApplicationDTOS = coLocationApplicationDAO.getDTOListByIDList((List<Long>) recordIDs);
        LoginDTO loginDTO = (LoginDTO) objects[0];

        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];

        int roleID = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);
        for (CoLocationApplicationDTO dto : colocationApplicationDTOS) {

            FlowState flowState = FlowRepository.getInstance().getFlowStateByFlowStateId(dto.getState());
            List<FlowState> nextStates = FlowRepository.getInstance().getNextStatesByCurrentStateAndRoleId(flowState.getId(), roleID);
            if (nextStates.size() > 0) {
                dto.setHasPermission(true);
            }
            dto.setStateDescription(flowState.getViewDescription());
            dto.setColor(flowState.getColor());

        }

        Comparator<CoLocationApplicationDTO> compareByHasPermissionAndApplicationId =
                Comparator.comparing(CoLocationApplicationDTO::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(CoLocationApplicationDTO::getApplicationID, Comparator.reverseOrder());
        return colocationApplicationDTOS.stream()
                .sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());
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
            flowState.setUrl(CoLocationConstants.stateMap.get(flowState.getId()).get("url"));
            flowState.setModal(CoLocationConstants.stateMap.get(flowState.getId()).get("modal"));
            flowState.setView(CoLocationConstants.stateMap.get(flowState.getId()).get("view"));
            flowState.setRedirect(CoLocationConstants.stateMap.get(flowState.getId()).get("redirect"));
            flowState.setParam(CoLocationConstants.stateMap.get(flowState.getId()).get("param"));
        }
        return flowStates;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public FlowState getCurrentState(Integer state) {
        FlowState flowState = flowService.getStateById(state);
        flowState.setUrl(CoLocationConstants.stateMap.get(state).get("url"));
        flowState.setModal(CoLocationConstants.stateMap.get(state).get("modal"));
        flowState.setView(CoLocationConstants.stateMap.get(state).get("view"));
        flowState.setRedirect(CoLocationConstants.stateMap.get(state).get("redirect"));
        flowState.setRedirect(CoLocationConstants.stateMap.get(state).get("param"));


        return flowState;
    }

    @Transactional
    public void handleGenerateAdviceNoteRequest(String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        CoLocationApplicationDTO coLocationApplicationDTO = getColocationApplication(appID);

        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();
//
        JsonArray userArray = jsonObject.getAsJsonArray("userList");

        //ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom(jsonElement);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        coLocationApplicationDTO.setState(state);
        updateApplicaton(coLocationApplicationDTO,loginDTO);

        ColocationAdviceNote adviceNote = generateAN(userArray, state, coLocationApplicationDTO, loginDTO.getUserID());
        AsyncPdfService.getInstance().accept(adviceNote);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public ColocationAdviceNote generateAN(JsonArray userArray, int state, CoLocationApplicationDTO coLocationApplicationDTO, long senderId) throws Exception {
        List<RecipientElement> ccList = officialLetterService.getCCRecipientElements(userArray);
        List<RecipientElement> toList = officialLetterService.getToRecipientElements(state);
        List<RecipientElement> recipientElements = Stream.concat(toList.stream(), ccList.stream()).collect(Collectors.toList());
        ColocationAdviceNote colocationAdviceNote = new ColocationAdviceNote(coLocationApplicationDTO.getApplicationID(), coLocationApplicationDTO.getClientID());
        //save official doc
        officialLetterService.saveOfficialLetterTransactionalDefault( colocationAdviceNote, recipientElements, senderId);
        return colocationAdviceNote;
    }

    @Transactional
    public void ifrRequest(JsonObject jsonObject,long appID,int state, boolean isReplied,boolean isSelected,LoginDTO loginDTO ) throws Exception {

        CoLocationApplicationDTO dto  = coLocationApplicationService.getColocationApplication(appID);
        CoLocationApplicationIFRDTO ifrdto = CoLocationApplicationIFRDTO.builder()
                .parentIFRID(0)
                .applicationID(dto.getApplicationID())
                .submissionDate(System.currentTimeMillis())
                .state(state)
                .typeName("")
                .categoryLabel("")
                .categoryName("")
                .amountLabel("")
                .amountName("")
                .isReplied(isReplied)
                .isSelected(isSelected)
                //TODO change hardcode for user role
                .userRoleId(20001)
                .deadline(dto.getSuggestedDate())
//                .isSelected(true)
                .build();
        //if power
        if(dto.isPowerNeeded()){
            ifrdto.setTypeName("Power");
            ifrdto.setCategoryLabel("Power");
            ifrdto.setCategoryName((dto.getPowerType()==CoLocationConstants.POWER_TYPE_AC?"AC":"DC"));
            ifrdto.setAmountLabel((dto.getPowerType()==CoLocationConstants.POWER_TYPE_AC?"AC":"DC")+"(Amp)");
            ifrdto.setAmountName(""+dto.getPowerAmount());
            ifrdto.setUserRoleId(CoLocationConstants.POWER_ROLE);
            ifrService.insertApplication(ifrdto);
        }
        //if rack
        if(dto.isRackNeeded()){
            ifrdto.setTypeName("Rack");
            ifrdto.setCategoryLabel("Rack Size");
            ifrdto.setCategoryName(""+coLocationInventoryTemplateService.getInventoryTemplateByID((long)dto.getRackTypeID()).getValue());
            ifrdto.setAmountLabel("Rack Space");
            ifrdto.setAmountName(""+coLocationInventoryTemplateService.getInventoryTemplateByID((long)dto.getRackSpace()).getValue());
            ifrdto.setUserRoleId(CoLocationConstants.SPACE_OFC_ROLE);
            ifrService.insertApplication(ifrdto);



        }
        // if fiber
        if(dto.isFiberNeeded()){
            ifrdto.setTypeName("Fiber & OFC");
            ifrdto.setCategoryLabel("OFC Type");
            ifrdto.setCategoryName(coLocationInventoryTemplateService.getInventoryTemplateByID((long)dto.getFiberType()).getValue());
            ifrdto.setAmountLabel("Num Of Core:");
            ifrdto.setAmountName(""+dto.getFiberCore());
            ifrdto.setUserRoleId(CoLocationConstants.SPACE_OFC_ROLE);
            ifrService.insertApplication(ifrdto);


        }



        if(dto.isFloorSpaceNeeded()){
            ifrdto.setTypeName("Floor Space");
            ifrdto.setCategoryLabel("Floor Space Type");
            ifrdto.setCategoryName(coLocationInventoryTemplateService.getInventoryTemplateByID((long)dto.getFloorSpaceType()).getValue());
            ifrdto.setAmountLabel("Floor Space Amount(ft.):");
            ifrdto.setAmountName(""+dto.getFloorSpaceAmount());
            ifrdto.setUserRoleId(CoLocationConstants.SPACE_OFC_ROLE);
            ifrService.insertApplication(ifrdto);

        }

//        ArrayList<IFR> lists = new IFRDeserializer().deserialize_custom(jelement);
//
//        for (IFR ifr : lists) {
//            ifr.setIsForwarded(lliApplication.getIsForwarded());
//            ifrService.insertApplication(ifr, loginDTO);
//            //parent ifr id need to be incorporated
//        }

//        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);
        dto.setState(state);
        coLocationApplicationService.updateApplicaton(dto,loginDTO);//this need to update state
    }

    public RequestType getRequestTypeByComparingNewAndPreviousSettings(CoLocationConnectionDTO oldConnectionSettings
            , CoLocationApplicationDTO newApplicationSettings) throws Exception {

        VariableCostService variableCostService = ServiceDAOFactory.getService(VariableCostService.class);

        AllVariableUnitCharge allVariableUnitChargeNew = variableCostService
                .getAllVariableChargeByCoLocationApplication(newApplicationSettings);
        AllVariableUnitCharge allVariableUnitChargeOld = variableCostService
                .getAllVariableUnitChargeByCoLocation(oldConnectionSettings);


        double monthFactor = CoLocationBillUtilityService.getMonthFactorByConnectionWithRespectToNextBillDate(oldConnectionSettings);
        if(monthFactor == 0){
            throw new RequestFailureException("Today is Billing day ! You can not request this day");
        }
        double oldSettingsCost = allVariableUnitChargeOld.getTotalCost() * monthFactor;
        log.info("Old Settings Remaining Cost: " + oldSettingsCost);
        double newSettingsCost = allVariableUnitChargeNew.getTotalCost() * monthFactor;
        log.info("New Settings Remaining Cost: " + newSettingsCost);


        if(oldSettingsCost> newSettingsCost) return RequestType.COLOCATION_DOWNGRADE;
        else if(oldSettingsCost<newSettingsCost)return  RequestType.COLOCATION_UPGRADE;
        else throw new RequestFailureException("The request is neither upgrade nor downgrade");

    }
    @Transactional
    public void insertApplicationWhichContainConnectionID(String jsonString,LoginDTO loginDTO, int applicationType, int state) throws Exception {
        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();
        long connectionID = jsonObject.get("connection_id").getAsLong();

        long suggestedDate = jsonObject.get("suggestedDate")!=null?jsonObject.get("suggestedDate").getAsLong():0;
        String comment = jsonObject.get("comment")!=null?jsonObject.get("comment").getAsString(): null;

//        coLocationInventoryService.insertCoLocationInventory(inventoryData);
        CoLocationConnectionDTO  coLocationConnectionDTO = coLocationConnectionService.getColocationConnection(connectionID);
        CoLocationApplicationDTO coLocationApplicationDTO = CoLocationApplicationDTO.builder()
//                .applicationType(CoLocationConstants.TD)
                .applicationType(applicationType)
                .connectionType(coLocationConnectionDTO.getConnectionType())
                .state(state)
//                .state(CoLocationConstants.STATE.TD_APPLICATION_SUBMITTED.getValue())
                .connectionId(Long.valueOf(coLocationConnectionDTO.getID()).intValue())
                .clientID(coLocationConnectionDTO.getClientID())

//                .submissionDate(coLocationConnectionDTO.)
                .submissionDate(System.currentTimeMillis())

                .suggestedDate(suggestedDate)
                .comment(comment)


                .rackNeeded(coLocationConnectionDTO.isRackNeeded())
                .rackSpace(coLocationConnectionDTO.getRackSpace())
                .rackTypeID(coLocationConnectionDTO.getRackSize())

                .fiberCore(coLocationConnectionDTO.getFiberCore())
                .fiberNeeded(coLocationConnectionDTO.isFiberNeeded())
                .fiberType(coLocationConnectionDTO.getFiberType())

                .powerAmount(coLocationConnectionDTO.getPowerAmount())
                .powerNeeded(coLocationConnectionDTO.isPowerNeeded())
                .powerType(coLocationConnectionDTO.getPowerType())

                .build();

//        coLocationApplicationDTO.setApplicationType(CoLocationConstants.NEW_CONNECTION);
//        coLocationApplicationDTO.setState(CoLocationConstants.STATE.SUBMITTED.getValue());
        insertApplication(coLocationApplicationDTO,loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationApplicationDTO getCoLocationApplicationByConnectionId(long connectionId) throws Exception {
        List<CoLocationApplicationDTO> applications = coLocationApplicationDAO.getColocationApplicationByConnectionId(connectionId);
        if(applications.isEmpty()){
            throw new RequestFailureException("No Application Found with Connection Id " + connectionId);
        }else if(applications.size()>1){
            throw new RequestFailureException("Multiple Applications Found with Connection Id " + connectionId);
        }
        return applications.get(0);
    }


}
