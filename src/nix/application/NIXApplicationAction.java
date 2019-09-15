package nix.application;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import flow.FlowService;
import flow.entity.FlowState;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.constants.NIXConstants;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRDeserializer;
import nix.efr.NIXEFRService;
import nix.ifr.NIXIFR;
import nix.ifr.NIXIFRDeserializer;
import nix.ifr.NIXIFRService;
import nix.nixflowconnectionmanger.NIXFlowDataBuilder;
import nix.nixflowconnectionmanger.NIXFlowViewDataBuilder;
import officialLetter.OfficialLetterService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;
import util.ServiceDAOFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ActionRequestMapping("nix/application")
public class NIXApplicationAction extends AnnotatedRequestMappingAction {

    // TODO: 1/23/2019 5019 flow state should be deleted and loop distances should be updated 
    
    @Service
    OfficialLetterService officialLetterService;
    @Service
    NIXIFRService nixifrService;

    @Service
    NIXEFRService nixefrService;

    @Service
    NIXFlowDataBuilder flowDataBuilder;

    @Service
    NIXFlowViewDataBuilder nixFlowViewDataBuilder;

    @Service
    NIXApplicationService nixApplicationService;

    @Service
    CommentsService commentsService;

    @Service
    NIXApplicationOfficeService nixApplicationOfficeService;

    @Service
    InventoryService inventoryService;

    @Service
    BillService billService;

    //region reconnect
    @ForwardedAction
    @RequestMapping(mapping = "/reconnect", requestMethod = RequestMethod.GET)
    public String getReconnectApplicationForm() throws Exception {
        return "nix-application-reconnect";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-reject", requestMethod = RequestMethod.All)
    public String rejectApplication(@RequestParameter("id") long applicationID) throws Exception {
        //lliApplicationService.rejectApplication(applicationID);
        return "lli-application-reject";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-request-for-correction", requestMethod = RequestMethod.All)
    public String requestForCorrectionApplication(@RequestParameter("id") long applicationID) throws Exception {
        //lliApplicationService.requestForCorrectionApplication(applicationID);
        return "lli-application-request-for-correction";
    }

    //region cc check part
    @JsonPost
    @RequestMapping(mapping = "/ccrequest", requestMethod = RequestMethod.POST)
    public String ccRequestApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        nixApplicationService.updateApplicatonState(appID, state);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/ccresponse", requestMethod = RequestMethod.POST)
    public String ccResponseApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplication,state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/changestate", requestMethod = RequestMethod.POST)
    public String applicationStateChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }
    //endregion

    @RequestMapping(mapping = "/connection-view", requestMethod = RequestMethod.All)
    public JsonObject getConnectionDataDetails(@RequestParameter("id") long applicationID,
                                               LoginDTO loginDTO) throws Exception {

        Gson gson = new Gson();
        NIXApplication nixApplication = nixApplicationService.getFlowApplicationByApplicationID(applicationID);
        JsonObject jsonObject = new JsonObject();
        jsonObject = nixFlowViewDataBuilder.getCommonPart_nix(nixApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping="/get-ol-info", requestMethod = RequestMethod.GET)
    public JsonArray getOfficialLetterInfoByAppId(
            @RequestParameter("appId") long appId,
            @RequestParameter("moduleId") int moduleId) throws Exception {

        return officialLetterService.getOfficialLettersByApplicationIdAndModuleId(appId, moduleId);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/new-connection-details", requestMethod = RequestMethod.GET)
    public String getNewConnectionDetailsForm(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        return "nix-application-new-connection-details";
    }

    @JsonPost
    @RequestMapping(mapping = "/getchildPorts", requestMethod = RequestMethod.POST)
    public List<InventoryItem> getchildPorts(@RequestParameter(isJsonBody = true, value = "data") String JsonString,
                                        LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Long parent = jsonObject.get("parent")!=null?jsonObject.get("parent").getAsLong():null;
        Integer childCategory = jsonObject.get("catagory")!=null?jsonObject.get("catagory").getAsInt():null;
        Integer portType = jsonObject.get("portType")!=null?jsonObject.get("portType").getAsInt():null;
        return inventoryService.getInventoryItems(childCategory, parent,portType);
    }

    @JsonPost
    @RequestMapping(mapping = "/getchildVlans", requestMethod = RequestMethod.POST)
    public List<InventoryItem> getchildVlans(@RequestParameter(isJsonBody = true, value = "data") String JsonString,
                                        LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Long parent = jsonObject.get("parent")!=null?jsonObject.get("parent").getAsLong():null;
        Integer childCategory = jsonObject.get("catagory")!=null?jsonObject.get("catagory").getAsInt():null;
        return inventoryService.getInventoryItems(childCategory, parent);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/new-connection", requestMethod = RequestMethod.GET)
    public String newConnectionView(){
        return "new";
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection", requestMethod = RequestMethod.POST)
    public String postNewConnectionApplicationForm1(
            @RequestParameter(isJsonBody = true, value = "application") String application,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(application);
        //check if td then cannot apply except reconnection

        //end of check td
        nixApplicationService.insertBatchOperation(jelement,loginDTO);

        return "oka";

    }

    @RequestMapping(mapping = "/new-connection-get", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData(
            @RequestParameter("id") long applicationID,
            LoginDTO loginDTO) throws Exception {
        NIXApplication nixApplication = nixApplicationService.getFlowApplicationByApplicationID(applicationID);
        JsonObject jsonObject = new JsonObject();
        jsonObject = flowDataBuilder.getCommonPart_nix(nixApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/view", requestMethod = RequestMethod.All)
    public String getApplicationView(@RequestParameter("id") long applicationID) throws Exception {
//		return lliApplicationService.viewApplicationMappingForwardName(applicationID);
        return nixApplicationService.viewNewApplicationMappingForwardName(applicationID);
    }

    //Application Search Page
    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchNIXConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_NIX_APPLICATION, request,
                nixApplicationService, SessionConstants.VIEW_NIX_APPLICATION, SessionConstants.SEARCH_NIX_APPLICATION);
        rnManager.doJobCustom(loginDTO);
        return "nix-application-search";
    }


    @ForwardedAction
    @RequestMapping(mapping = "/newview", requestMethod = RequestMethod.All)
    public String getApplicationNewView(
            @RequestParameter("id") long applicationID,
            LoginDTO loginDTO,
            HttpServletRequest request) throws Exception {

        NIXApplication nixApplication = nixApplicationService.getApplicationById(applicationID);
        FlowState flowState = new FlowService().getStateById(nixApplication.getState());
        List<FlowState> flowStates = new FlowService().getStatesByRole((int) loginDTO.getRoleID());
        boolean isApproved = false;
        for (FlowState flowState1 : flowStates) {
            if (flowState1.getId() == flowState.getId()) {
                isApproved = true;
            }
        }
        if (isApproved) {
            return nixApplicationService.viewNewApplicationMappingForwardName(applicationID);
        } else {
            return "404";
        }
    }


    @RequestMapping(mapping = "/new-connection-get-flow", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData2(
            @RequestParameter("id") long applicationID,
            LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        NIXApplication nixApplication = nixApplicationService.getFlowApplicationByApplicationID(applicationID);
        List<NIXApplicationOffice> nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixApplication.getId());
        nixApplication.setNixApplicationOffices(nixApplicationOffices);
        jsonObject = flowDataBuilder.getCommonPart_nix(nixApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection-edit", requestMethod = RequestMethod.POST)
    public long postNewConnectionEdit(@RequestParameter(isJsonBody = true, value = "application") String jsonString,LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        int nextState = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = new NIXApplication().desirializer_custom(jsonObject);
        nixApplicationService.editApplication(nixApplication,nextState,jsonObject,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return nixApplication.getId();
    }

    @JsonPost
    @RequestMapping(mapping = "/discard", requestMethod = RequestMethod.POST)
    public String discardApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString,
                                     LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/ifrinsert", requestMethod = RequestMethod.POST)
    public String ifrData(
            @RequestParameter(isJsonBody = true, value = "ifr") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = nixApplicationService.getFlowApplicationByApplicationID(appID);
        ArrayList<NIXIFR> lists = new NIXIFRDeserializer().deserialize_custom(jelement);

        for (NIXIFR ifr : lists) {
            ifr.setIsForwarded(nixApplication.getIsForwarded());
            ifr.setLastModificationTime(System.currentTimeMillis());
            nixifrService.insertApplication(ifr, loginDTO);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        nixApplicationService.updateApplicatonState(appID, state);//this need to update state
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/ifrupdate", requestMethod = RequestMethod.POST)
    public String ifrUpdate(
            @RequestParameter(isJsonBody = true, value = "ifr") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(jsonString);
        nixApplicationService.ifrUpdateBatchOperation(jelement,loginDTO);
        return "ok"; //need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/efrinsert", requestMethod = RequestMethod.POST)
    public String efrData(
            @RequestParameter(isJsonBody = true, value = "efr") String jsonString,
            LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        nixApplicationService.efrInsert(jelement,loginDTO);
        return "";//need to go search page
    }



    @JsonPost
    @RequestMapping(mapping = "/efrupdate", requestMethod = RequestMethod.POST)
    public String efrUpdate(
            @RequestParameter(isJsonBody = true, value = "efr") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(jsonString);
        nixApplicationService.updateEFR(jelement,loginDTO);
        return "";//need to go search page
    }



    @JsonPost
    @RequestMapping(mapping = "/demandnotegenerate", requestMethod = RequestMethod.POST)
    public String demandNote(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.demandNote(jsonElement,loginDTO);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/demandnoteforClose", requestMethod = RequestMethod.POST)
    public String demandNoteforClose(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/demandnoteskipforClose", requestMethod = RequestMethod.POST)
    public String demandNoteforCloseSkip(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        nixApplication.setSkipPayment(1);
        nixApplicationService.updateApplicaton(nixApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }
    @JsonPost
    @RequestMapping(mapping = "/demandnotepayment", requestMethod = RequestMethod.POST)
    public String demandNotePay(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        if (nixApplicationService.getApplicationById(appID).getType() == LLIConnectionConstants.RECONNECT) {
            List<BillDTO> unpaidBills = billService.getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(NIXConstants.ENTITY_TYPE,
                    loginDTO.getAccountID(), BillConstants.MONTHLY_BILL);

            if (unpaidBills != null) {
                throw new RequestFailureException("You have unpaid bills. Please pay them first.");
            }
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";
    }


    @JsonPost
    @RequestMapping(mapping = "/workordergenerate", requestMethod = RequestMethod.POST)
    public String generateWO(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
//		int state = 33;
        nixApplicationService.generateWorkOrderDocuments(appID, loginDTO.getUserID());
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/closeworkordergenerate", requestMethod = RequestMethod.POST)
    public String generateCloseWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.closeWOBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/completeworkorder", requestMethod = RequestMethod.POST)
    public String completeWO(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.completeWOBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/advicenotegenerate", requestMethod = RequestMethod.POST)
    public String generateAN(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.generateAN(jsonElement,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/withoutloopdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String withoutLoopDemandNote(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.withoutLoopDemandNoteBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/forwardToLDGM", requestMethod = RequestMethod.POST)
    public String forwardtoLDGM(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        // TODO: 12/26/2018 this method might have to change later according to the requirments
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
       /* if (nixApplication.getSecondZone() == 0) {

            nixApplication.setSecondZone(nixApplication.getZone());
        }
        nixApplication.setZone(jsonObject.get("zoneID").getAsInt());*/

        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }
    @JsonPost
    @RequestMapping(mapping = "/rejectAndForwardtoVendor", requestMethod = RequestMethod.POST)
    public String rejectAndForwardtoVendor(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        // TODO: 12/26/2018 this method might have to change later according to the requirments
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }
    @JsonPost
    @RequestMapping(mapping = "/forwardtoldgmforifr", requestMethod = RequestMethod.POST)
    public String forwardtoLDGMForIFR(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        // TODO: 12/26/2018 this method might have to change later according to the requirments
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/forwardtolcdgm", requestMethod = RequestMethod.POST)
    public String forwardtolcdgm(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        // TODO: 12/26/2018 this method might have to change later according to the requirments
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.updateApplicatonState(appID, state);
        nixApplicationService.sendNotification(nixApplicationService.getApplicationById(appID),state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }
    @JsonPost
    @RequestMapping(mapping = "/retransfer", requestMethod = RequestMethod.POST)
    public String applicationRetransfer(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        NIXApplication nixApplication = nixApplicationService.getFlowApplicationByApplicationID(appID);
        //int oldZone = nixApplication.getSecondZone();
        int currentZone = nixApplication.getZone();
       // nixApplication.setZone(oldZone);
        //nixApplication.setSecondZone(currentZone);
        nixApplication.setIsForwarded(0);
        nixApplication.setState(state);
        nixApplication.setSkipPayment(nixApplication.getSkipPayment());
        nixApplicationService.updateApplicaton(nixApplication);
        nixApplicationService.sendNotification(nixApplication,state,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/completetestingandcreateconnection", requestMethod = RequestMethod.POST)
    public String completeTestAndConnection(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.handleConnectionCompleteRequest(jsonElement, loginDTO);
        return "";
        // TODO: 12/26/2018 get office and make entry to office document
    }

    //region Upgrade Application
    @ForwardedAction
    @RequestMapping(mapping = "/upgrade-port", requestMethod = RequestMethod.GET)
    public String getUpgradepplicationForm() throws Exception {
        return "nix-application-upgrade";
    }

    @JsonPost
    @RequestMapping(mapping = "/upgrade-port", requestMethod = RequestMethod.POST)
    public String postUpgradePortApplicationForm1(
            @RequestParameter(isJsonBody = true, value = "application") String application,
            LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(application);

        nixApplicationService.upgradeInsertBatchOperation(jelement,loginDTO);

        return  "oka";

    }

    @JsonPost
    @RequestMapping(mapping = "/ifrupdateUpgrade", requestMethod = RequestMethod.POST)
    public String ifrUpdateForUpgrade(
            @RequestParameter(isJsonBody = true, value = "ifr") String jsonString,
            LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixApplicationService.upgradeBatchOperation(jelement,state,appID,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "ok"; //need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/closeapplication", requestMethod = RequestMethod.POST)
    public String completeCloseConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.closeConnectionCompleteBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/completetestingUpgradeOrDowngrade", requestMethod = RequestMethod.POST)
    public String completeTestAndConnectionUpgrade(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {

        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.handleConnectionCompleteRequestUpgrade(jsonElement, loginDTO);
        return "";
        // TODO: 12/26/2018 get office and make entry to office document
    }

    //endregion
    //region close port
    @ForwardedAction
    @RequestMapping(mapping = "/close-port", requestMethod = RequestMethod.GET)
    public String getClosePortForm() throws Exception {
        return "nix-application-close";
    }
    @JsonPost
    @RequestMapping(mapping = "/close-port", requestMethod = RequestMethod.POST)
    public String postClosePortApplicationForm1(
            @RequestParameter(isJsonBody = true, value = "application") String application,
            LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(application);
        nixApplicationService.closePortInsertBatchOperation(jelement,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return  "oka";

    }

    //endregion


    //region downgrade Application
    @ForwardedAction
    @RequestMapping(mapping = "/downgrade-port", requestMethod = RequestMethod.GET)
    public String getdowngradeepplicationForm() throws Exception {
        return "nix-application-downgrade";
    }

    @JsonPost
    @RequestMapping(mapping = "/downgrade-port", requestMethod = RequestMethod.POST)
    public String postDowngradePortApplicationForm1(
            @RequestParameter(isJsonBody = true, value = "application") String application,
            LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(application);
        nixApplicationService.downgradeBatchOperation(jelement,loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return  "oka";

    }

    @JsonPost
    @RequestMapping(mapping = "/without-IFR-EFR-WO-demandnotegenerate", requestMethod = RequestMethod.POST)
    public String demandNoteWithoutIFREFRWO(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        //nixApplicationService.updateApplicatonState(appID,state);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/demandnoteskipdowngrade", requestMethod = RequestMethod.POST)
    public String demandNotedowngradeSkip(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        nixApplication.setSkipPayment(1);
        nixApplicationService.updateApplicaton(nixApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }
    //endregion


    @JsonPost
    @RequestMapping(mapping = "/skipdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String skipDemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.skipDemandNoteBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/withoutloopskipdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String noLoopSkipDemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        nixApplicationService.withoutLoopSkipDemandNoteBatchOperation(jsonElement,loginDTO);
        return "";//need to go search page
    }

    //7/3/19
    @JsonPost
    @RequestMapping(mapping = "/applicationforward", requestMethod = RequestMethod.POST)
    public String applicationForward(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO)
            throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        nixApplication.setIsForwarded(1);
        if (nixApplication.getSecondZone() == 0) {

            nixApplication.setSecondZone(nixApplication.getZone());
        }
        nixApplication.setZone(jsonObject.get("zoneID").getAsInt());

        nixApplication.setState(state);

        List<NIXIFR> ifrList = nixifrService.getIFRByAppID(nixApplication.getId());
        for (NIXIFR ifR : ifrList
        ) {
            ifR.setIsIgnored(1);
            nixifrService.updateApplicaton(ifR);

        }

        nixApplicationService.updateApplicaton(nixApplication);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        nixApplicationService.sendNotification(nixApplication, state, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/application-forward-for-loop", requestMethod = RequestMethod.POST)
    public String applicationForwardForLoop(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        ArrayList<NIXIFR> list1 = new NIXIFRDeserializer().deserialize_custom_ifr_update(jsonElement);
        ArrayList<NIXIFR> selectedIFR = new ArrayList<>();
        for (NIXIFR ifr : list1) {

            if (ifr.getSelected() == 1) {
                selectedIFR.add(ifr);
            } else {
                ifr.setSelected(NIXConstants.IFR_IGNORED);
            }
            ifr.setIsForwarded(nixApplication.getIsForwarded());
            nixifrService.updateApplicaton(ifr);
        }

        NIXApplicationLocalLoopService nixApplicationLocalLoopService =  ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);
        List<NIXApplicationLocalLoop> localLoops = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).prepareLocalloopFromIFR(selectedIFR);
        for (NIXApplicationLocalLoop localLoop : localLoops
        ) {
            nixApplicationLocalLoopService.insertApplication(localLoop);
        }

        nixApplication.setIsForwarded(1);
        if (nixApplication.getSecondZone() == 0) {

            nixApplication.setSecondZone(nixApplication.getZone());
        }
        nixApplication.setZone(jsonObject.get("zoneID").getAsInt());
        nixApplication.setState(state);
        nixApplicationService.updateApplicaton(nixApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        nixApplicationService.sendNotification(nixApplication, state, loginDTO);


        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/forwarded-efr-select-only-loop", requestMethod = RequestMethod.POST)
    public String selectVendorWorkOnlyLoop(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        ArrayList<NIXEFR> lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        NIXEFRService nixefrService = ServiceDAOFactory.getService(NIXEFRService.class);
        for (NIXEFR efr : lists) {
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
        }
        NIXApplicationLocalLoopService localLoopService = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);
        List<NIXApplicationLocalLoop> localLoops = localLoopService.prepareLocalloop(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops
        ) {
            localLoopService.updateApplicaton(localLoop);
        }

        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);
        int oldZone = nixApplication.getSecondZone();
        int currentZone = nixApplication.getZone();
        nixApplication.setZone(oldZone);
        nixApplication.setSecondZone(currentZone);
        nixApplication.setIsForwarded(0);
        nixApplication.setState(state);
        nixApplication.setSkipPayment(nixApplication.getSkipPayment());
        nixApplicationService.updateApplicaton(nixApplication);
        nixApplicationService.sendNotification(nixApplication, nixApplication.getState(), loginDTO);

        return "";//need to go search page
    }






    @JsonPost
    @RequestMapping(mapping = "/forward-after-work-order", requestMethod = RequestMethod.POST)
    public String forwardAfterWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;

        NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);//added by Jami for new local loop advice note

        int state = jsonObject.get("nextState").getAsInt();

        ArrayList<NIXEFR> lists = new ArrayList<>();

        lists = new NIXEFRDeserializer().deserialize_custom(jsonElement);

//
        for (NIXEFR efr : lists) {
            efr.setLastModificationTime(System.currentTimeMillis());
            nixefrService.updateApplicaton(efr);
        }

        List<NIXApplicationLocalLoop> localLoops = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).prepareLocalloop(appID);
        for (NIXApplicationLocalLoop localLoop : localLoops) {
            ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).updateApplicaton(localLoop);
        }


        int oldZone = nixApplication.getSecondZone();
        int currentZone = nixApplication.getZone();
        nixApplication.setZone(oldZone);
        nixApplication.setSecondZone(currentZone);
        nixApplication.setIsForwarded(0);
        nixApplication.setState(state);
        nixApplication.setSkipPayment(nixApplication.getSkipPayment());

        nixApplicationService.updateApplicaton(nixApplication);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        nixApplicationService.sendNotification(nixApplication, state, loginDTO);

        return "";//need to go search page
    }


}
