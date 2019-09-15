package coLocation;

import annotation.ForwardedAction;
import annotation.JsonPost;
import coLocation.FlowConnectionManager.CoLocationApplicationFlowConnectionManagerService;
import coLocation.FlowConnectionManager.FlowDataBuilder;
import coLocation.accounts.VariableCost.VariableCostDTO;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationDeserializer;
import coLocation.application.CoLocationApplicationService;
import coLocation.application.RequestType;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.ifr.CoLocationApplicationIFRService;
import coLocation.inventory.CoLocationInventoryDTO;
import coLocation.inventory.CoLocationInventoryService;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import coLocation.td.CoLocationProbableTDDTO;
import coLocation.td.CoLocationProbableTDService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ActionRequestMapping(CoLocationConstants.CO_LOCATION_BASE_URL)
public class CoLocationAction extends AnnotatedRequestMappingAction {

    @Service
    CoLocationApplicationFlowConnectionManagerService coLocationApplicationFlowConnectionManagerService;
    @Service
    CoLocationApplicationIFRService coLocationApplicationIFRService;
    @Service
    CoLocationApplicationService coLocationApplicationService;
    @Service
    CoLocationConnectionService coLocationConnectionService;
    @Service
    CoLocationInventoryService coLocationInventoryService;
    @Service
    CoLocationInventoryTemplateService coLocationInventoryTemplateService;
    @Service
    CoLocationProbableTDService coLocationProbableTDService;
    @Service
    FlowDataBuilder flowDataBuilder;
    @Service
    CoLocationApplicationIFRService ifrService;
    @Service
    VariableCostService variableCostService;
    @Service
    BillService billService;

    @ForwardedAction
    @RequestMapping(mapping = "/connection-details", requestMethod = RequestMethod.All)
    public String CoLocationConnectionDetails(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.CONNECTION_DETAILS);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/downgrade-application", requestMethod = RequestMethod.GET)
    public String CoLocationDowngradeApplicaitonView(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.DOWNGRADE_APPLICATION);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    // start- demand note
    @ForwardedAction
    @RequestMapping(mapping = "/demand-note", requestMethod = RequestMethod.GET)
    public String CoLocationNewConnectionApplicaitonDemandNoteView(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
//        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL,"new-connection-application");
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.NEW_CONNECTION_APPLICATION_DEMAND_NOTE);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/new-connection-application-details", requestMethod = RequestMethod.All)
    public String CoLocationNewConnectionApplicaitonDetails(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, "new-connection-application-details");
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

//    @ForwardedAction
//    @RequestMapping(mapping="/close", requestMethod= RequestMethod.GET)
//    public String close(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
//        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL,CoLocationConstants.REVISE_APPLICATION);
//        return CoLocationConstants.CO_LOCATION_BASE_URL;
//    }
//
//
//    @ForwardedAction
//    @RequestMapping(mapping="/reconnect", requestMethod= RequestMethod.GET)
//    public String reconnect(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
//        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL,CoLocationConstants.REVISE_APPLICATION);
//        return CoLocationConstants.CO_LOCATION_BASE_URL;
//    }

    @ForwardedAction
    @RequestMapping(mapping = "/new-connection-application-details-full", requestMethod = RequestMethod.All)
    public String CoLocationNewConnectionApplicaitonDetailsFull(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.NEW_CONNECTION_VIEW_DETAILS_FULL);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    // start - new connection application
    //=============================== START VIEW PAGE ====================================
    @ForwardedAction
    @RequestMapping(mapping = "/new-connection-application", requestMethod = RequestMethod.GET)
    public String CoLocationNewConnectionApplicaitonView(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.NEW_CONNECTION_APPLICATION);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/revise-application", requestMethod = RequestMethod.GET)
    public String CoLocationReviseApplicaitonView(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.REVISE_APPLICATION);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/upgrade-application", requestMethod = RequestMethod.GET)
    public String CoLocationUpgradeApplicaitonView(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.UPGRADE_APPLICATION);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/demand-note-generate", requestMethod = RequestMethod.POST)
    public String DemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(appID);
        coLocationApplicationDTO.setSkipPayment(0);

        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/demand-note-generate-skip", requestMethod = RequestMethod.POST)
    public String DemandNoteSkip(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(appID);
        if (coLocationApplicationDTO.getSkipPayment() != 1) {
            throw new RequestFailureException("You can not skip this demand note for this client");
        }
        coLocationApplicationDTO.setSkipPayment(1);
        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
        return "";//need to go search page
    }

    //    rejected td application
    @JsonPost
    @RequestMapping(mapping = "/reject-td", requestMethod = RequestMethod.POST)
    public String rejectTDRequest(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(appID);

        long connectionID =  coLocationApplicationDTO.getConnectionId();
        //restore the application in td list
        CoLocationProbableTDDTO coLocationProbableTDDTO = coLocationProbableTDService.getCoLocationProbableTDByConnectionID(connectionID);
        coLocationProbableTDDTO.setTDRequested(false);
        coLocationProbableTDService.updateCoLocationProbableTDClient(coLocationProbableTDDTO);

        //
//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);

        coLocationApplicationDTO.setState(state);
        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);

        return "changed";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/change-state", requestMethod = RequestMethod.POST)
    public String applicationStateChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(appID);
//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);

        coLocationApplicationDTO.setState(state);
        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);

        return "changed";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/no-state-change", requestMethod = RequestMethod.POST)
    public String changeStyate(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        if (coLocationApplicationService.getColocationApplication(appID).getApplicationType() == LLIConnectionConstants.RECONNECT) {
            List<BillDTO> unpaidBills = billService.getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(CoLocationConstants.ENTITY_TYPE,
                    loginDTO.getAccountID(), BillConstants.YEARLY_BILL);

            if (unpaidBills != null) {
                throw new RequestFailureException("You have unpaid bills. Please pay them first.");
            }
        }

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/client-correction", requestMethod = RequestMethod.POST)
    public String clientCorrectionSubmit(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        Gson gson = new Gson();
        JsonElement jElement = gson.fromJson(jsonObject.get("application").getAsJsonObject(), JsonElement.class);
        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDeserializer().deserialize_custom(jElement, loginDTO);
        coLocationApplicationDTO.setApplicationID(appID);
        coLocationApplicationDTO.setState(state);
        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/close-connection", requestMethod = RequestMethod.POST)
    public void closeConnectionRequest(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO) throws Exception {

//        coLocationApplicationService.insertApplicationWhichContainConnectionID(jsonString,loginDTO);
        coLocationApplicationService.insertApplicationWhichContainConnectionID(jsonString, loginDTO, CoLocationConstants.CLOSE, CoLocationConstants.STATE.TD_APPLICATION_SUBMITTED.getValue());

    }

    @ForwardedAction
    @RequestMapping(mapping = "/close-connection", requestMethod = RequestMethod.GET)
    public String coLocationCloseConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.CLOSE_CONNECTION);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    /**
     * @author forhad
     */
    //start - inventory
    @ForwardedAction
    @RequestMapping(mapping = "/inventory-add", requestMethod = RequestMethod.GET)
    public String coLocationInventoryAdd(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, "inventory-add");
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/inventory-cost-config", requestMethod = RequestMethod.GET)
    public String coLocationInventoryCostConfigAdd(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.INVENTORY_COST_CONFIG_ADD);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/reconnect", requestMethod = RequestMethod.GET)
    public String coLocationReconnect(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.RECONNECT);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/probable-td", requestMethod = RequestMethod.All)
    public String coLocationTDProbable(LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_COLOCATION_PROBABLE_TD, request, coLocationProbableTDService,
                SessionConstants.VIEW_COLOCATION_PROBABLE_TD,
                SessionConstants.SEARCH_COLOCATION_PROBABLE_TD
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.PROBABLE_TD);
        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/complete-connection", requestMethod = RequestMethod.POST)
    public String completeConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        coLocationApplicationFlowConnectionManagerService.connectionCreatorOrUpdatorManager(jsonObject,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/complete-setup", requestMethod = RequestMethod.POST)
    public String completeSetup(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        coLocationApplicationIFRService.CheckIFRandUpdateState(jsonObject, appID, state,loginDTO);
        return "";//need to go search page
    }


    // ===================================== JSON REQUEST =============================


    //endregion - new connection application

    @JsonPost
    @RequestMapping(mapping = "/advice-note-generate", requestMethod = RequestMethod.POST)
    public void generateAN(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        coLocationApplicationService.handleGenerateAdviceNoteRequest(JsonString, loginDTO);
    }

    /**
     * @author forhad
     */
    @RequestMapping(mapping = "/history-list-json", requestMethod = RequestMethod.All)
    public List<CoLocationConnectionDTO> getConnectionHistoryList(@RequestParameter("id") Long historyId) throws Exception {
        return coLocationConnectionService.getConnectionHistoryByHistoryID(historyId);

    }

    @RequestMapping(mapping = "/get-cost-config-info", requestMethod = RequestMethod.GET)
    public VariableCostDTO getCostConfigInfo(@RequestParameter("typeid") int type, @RequestParameter("quantityid") int quantityid, LoginDTO loginDTO) throws Exception {
        return variableCostService.getCostByTypeAndAmount(type, quantityid);

    }

    @RequestMapping(mapping = "/inventory-cost-config-search", requestMethod = RequestMethod.All)
    public ActionForward getCostConfigList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_COLOCATION_INVENTORY, request, coLocationInventoryService,
                SessionConstants.VIEW_COLOCATION_INVENOTRY,
                SessionConstants.SEARCH_COLOCATION_INVENTORY
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.INVENTORY_SEARCH);
        return mapping.findForward(CoLocationConstants.CO_LOCATION_BASE_URL);
    }

    @RequestMapping(mapping = "/get-inventory-template", requestMethod = RequestMethod.All)
    public List<CoLocationInventoryTemplateDTO> getInventoryTemplateData(@RequestParameter("id") int type, LoginDTO loginDTO) throws Exception {
        return coLocationInventoryTemplateService.getInventoryTemplate(type);
    }

    @RequestMapping(mapping = "/flow-data", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData2(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(applicationID);

        jsonObject = flowDataBuilder.getCommonPart_new(coLocationApplicationDTO, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public ActionForward getRequestedApplicationList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_COLOCATION_APPLICATION, request, coLocationApplicationService,
                SessionConstants.VIEW_COLOCATION_APPLICATION,
                SessionConstants.SEARCH_COLOCATION_APPLICATION
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.APPLICATION_SEARCH);
        return mapping.findForward(CoLocationConstants.CO_LOCATION_BASE_URL);
    }

    @RequestMapping(mapping = "/connection-search", requestMethod = RequestMethod.All)
    public ActionForward getRequestedConnectionList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_COLOCATION_CONNECTION, request, coLocationConnectionService,
                SessionConstants.VIEW_COLOCATION_CONENCTION,
                SessionConstants.SEARCH_COLOCATION_CONNECTION
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.CONNECTION_SEARCH);
        return mapping.findForward(CoLocationConstants.CO_LOCATION_BASE_URL);
    }

    @RequestMapping(mapping = "/get-connection-by-client", requestMethod = RequestMethod.All)
    public List<CoLocationConnectionDTO> getconnectionByClient(@RequestParameter("clientID") long clientID, LoginDTO loginDTO) throws Exception {
        List<CoLocationConnectionDTO> coLocationConnectionDTOs = coLocationConnectionService.getColocationConnectionByClientID(clientID);
        return coLocationConnectionDTOs;
    }

    @RequestMapping(mapping = "/get-reconnect-connection-list-by-client", requestMethod = RequestMethod.All)
    public List<CoLocationConnectionDTO> getReconnectConnectionByClient(@RequestParameter("clientID") long clientID, LoginDTO loginDTO) throws Exception {
        List<CoLocationConnectionDTO> coLocationConnectionDTOs = coLocationConnectionService.getColocationReconnectConnectionListByClientID(clientID);
        return coLocationConnectionDTOs;
    }

    @RequestMapping(mapping = "/get-connection-by-id", requestMethod = RequestMethod.All)
    public CoLocationConnectionDTO getconnectionByID(@RequestParameter("id") long id, LoginDTO loginDTO) throws Exception {
        CoLocationConnectionDTO coLocationConnectionDTOs = coLocationConnectionService.getColocationConnection(id);
        return coLocationConnectionDTOs;
    }

    @JsonPost
    @RequestMapping(mapping = "/ifr-request", requestMethod = RequestMethod.POST)
    public String ifrData(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonParser().parse(JsonString).getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        coLocationApplicationService.ifrRequest(jsonObject, appID, state, false, false,loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/ifr-response", requestMethod = RequestMethod.POST)
    public String ifrUpdate(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);


        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        CoLocationApplicationDTO coLocationApplicationDTO = coLocationApplicationService.getColocationApplication(appID);

        ifrService.updateFeasibility(jsonObject, appID, state,loginDTO);


        //this need to update state


//        ArrayList<IFR> ifrArrayList = new IFRDeserializer().deserialize_custom(jelement);
//
//        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);
//        //todo : logic test
//
//
//        ifrService.insertOrUpdateIFR(ifrArrayList, appID, state);
//        coLocationApplicationDTO.setState(state);
//        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO);


        return "ok"; //need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/downgrade-connection-application", requestMethod = RequestMethod.POST)
    public void postCoLocationDowngradeConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO, HttpServletRequest request) throws Exception {


        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();

        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDeserializer().deserialize_custom(jelement, loginDTO);
        coLocationApplicationDTO.setApplicationType(CoLocationConstants.DOWNGRADE_CONNECTION);
        coLocationApplicationDTO.setState(CoLocationConstants.STATE.DOWNGRADE_APPLICATION_SUBMITTED.getValue());
        coLocationApplicationService.insertApplication(coLocationApplicationDTO, loginDTO);

//        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    // ====================================END VIEW PAGE =================================
    // ===================================== JSON REQUEST =============================
    @JsonPost
    @RequestMapping(mapping = "/new-connection-application", requestMethod = RequestMethod.POST)
    public void postCoLocationNewConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();

        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDeserializer().deserialize_custom(jelement, loginDTO);
        coLocationApplicationDTO.setApplicationType(CoLocationConstants.NEW_CONNECTION);
        coLocationApplicationDTO.setState(CoLocationConstants.STATE.SUBMITTED.getValue());
        coLocationApplicationService.insertApplication(coLocationApplicationDTO, loginDTO);

//        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    /**
     * @author forhad
     */

//    @RequestMapping(mapping="/connection-by-history-id", requestMethod=RequestMethod.All)
//    public CoLocationConnectionDTO getConnectionByHistoryID(@RequestParameter("id") Long historyID) throws Exception{
//        return coLocationConnectionService.getColocationConnectionsByHistoryId(historyID);
//    }

    @JsonPost
    @RequestMapping(mapping = "/revise-connection-application", requestMethod = RequestMethod.POST)
    public void postCoLocationReviseConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();

        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDeserializer().deserialize_custom(jelement, loginDTO);

        CoLocationConnectionDTO coLocationConnectionDTO = coLocationConnectionService.getColocationConnection(coLocationApplicationDTO.getConnectionId());
        RequestType requestType = coLocationApplicationService.getRequestTypeByComparingNewAndPreviousSettings(coLocationConnectionDTO, coLocationApplicationDTO);
        if (requestType == RequestType.COLOCATION_UPGRADE) {
            coLocationApplicationDTO.setApplicationType(CoLocationConstants.UPGRADE_CONNECTION);
            coLocationApplicationDTO.setState(CoLocationConstants.STATE.SUBMITTED.getValue());
            coLocationApplicationService.insertApplication(coLocationApplicationDTO, loginDTO);

        } else if (requestType == RequestType.COLOCATION_DOWNGRADE) {
            coLocationApplicationDTO.setApplicationType(CoLocationConstants.DOWNGRADE_CONNECTION);
            coLocationApplicationDTO.setState(CoLocationConstants.STATE.DOWNGRADE_APPLICATION_SUBMITTED.getValue());
            coLocationApplicationService.insertApplication(coLocationApplicationDTO);
            coLocationApplicationService.ifrRequest(jsonObject, coLocationApplicationDTO.getApplicationID(), coLocationApplicationDTO.getState(), true, true,loginDTO);
        }


//        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/upgrade-connection-application", requestMethod = RequestMethod.POST)
    public void postCoLocationUpgradeConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();

        CoLocationApplicationDTO coLocationApplicationDTO = new CoLocationApplicationDeserializer().deserialize_custom(jelement, loginDTO);
        coLocationApplicationDTO.setApplicationType(CoLocationConstants.UPGRADE_CONNECTION);
        coLocationApplicationDTO.setState(CoLocationConstants.STATE.SUBMITTED.getValue());
        coLocationApplicationService.insertApplication(coLocationApplicationDTO, loginDTO);

//        return CoLocationConstants.CO_LOCATION_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/reconnect", requestMethod = RequestMethod.POST)
    public void reconnectRequest(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO) throws Exception {

//        coLocationApplicationService.insertApplicationWhichContainConnectionID(jsonString,loginDTO);
        coLocationApplicationService.insertApplicationWhichContainConnectionID(jsonString, loginDTO, CoLocationConstants.RECONNECT_CONNECTION, CoLocationConstants.STATE.RECONNECT_APPLICATION_SUBMITTED.getValue());

    }

    @JsonPost
    @RequestMapping(mapping = "/inventory-cost-config", requestMethod = RequestMethod.POST)
    public void saveCostConfig(@RequestParameter(value = "cost", isJsonBody = true) VariableCostDTO cost) throws Exception {
        //if already ID exists in VariableCostDTO then there is an entry - update in this scenario
        //else insert
        if (cost.getID() == 0) variableCostService.insertCostConfig(cost);
        else variableCostService.updateCostConfig(cost);
    }

    @JsonPost
    @RequestMapping(mapping = "/inventory-add", requestMethod = RequestMethod.POST)
    public void saveInventory(@RequestParameter(value = "inventoryData", isJsonBody = true) CoLocationInventoryDTO inventoryData) throws Exception {
        coLocationInventoryService.insertCoLocationInventory(inventoryData);
    }

    @RequestMapping(mapping = "/inventory-search", requestMethod = RequestMethod.All)
    public ActionForward searchInventoryList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_COLOCATION_INVENTORY, request, coLocationInventoryService,
                SessionConstants.VIEW_COLOCATION_INVENOTRY,
                SessionConstants.SEARCH_COLOCATION_INVENTORY
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL, CoLocationConstants.INVENTORY_SEARCH);
        return mapping.findForward(CoLocationConstants.CO_LOCATION_BASE_URL);
    }

    @JsonPost
    @RequestMapping(mapping = "/probable-td-request", requestMethod = RequestMethod.POST)
    public void tdRequest(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO) throws Exception {
//        JsonElement jelement = new JsonParser().parse(jsonString);
//
//        JsonObject jsonObject = jelement.getAsJsonObject();
//        long connectionID = jsonObject.get("connection_id").getAsLong();
////        coLocationInventoryService.insertCoLocationInventory(inventoryData);
//        CoLocationConnectionDTO  coLocationConnectionDTO = coLocationConnectionService.getColocationConnection(connectionID);
//        CoLocationApplicationDTO coLocationApplicationDTO = CoLocationApplicationDTO.builder()
//                .applicationType(CoLocationConstants.TD)
//                .state(CoLocationConstants.STATE.TD_APPLICATION_SUBMITTED.getValue())
//                .connectionId(Long.valueOf(coLocationConnectionDTO.getID()).intValue())
//                .clientID(coLocationConnectionDTO.getClientID())
//
//                .rackNeeded(coLocationConnectionDTO.isRackNeeded())
//                .rackSpace(coLocationConnectionDTO.getRackSpace())
//                .rackTypeID(coLocationConnectionDTO.getRackSize())
//
//                .fiberCore(coLocationConnectionDTO.getFiberCore())
//                .fiberNeeded(coLocationConnectionDTO.isFiberNeeded())
//                .fiberType(coLocationConnectionDTO.getFiberType())
//
//                .powerAmount(coLocationConnectionDTO.getPowerAmount())
//                .powerNeeded(coLocationConnectionDTO.isPowerNeeded())
//                .powerType(coLocationConnectionDTO.getPowerType())
//
//                .build();
//
////        coLocationApplicationDTO.setApplicationType(CoLocationConstants.NEW_CONNECTION);
////        coLocationApplicationDTO.setState(CoLocationConstants.STATE.SUBMITTED.getValue());
//        coLocationApplicationService.insertApplication(coLocationApplicationDTO,loginDTO);

        //insert the application
        coLocationApplicationService.insertApplicationWhichContainConnectionID(jsonString, loginDTO, CoLocationConstants.TD, CoLocationConstants.STATE.TD_APPLICATION_SUBMITTED.getValue());


        long connectionID = new JsonParser().parse(jsonString).getAsJsonObject().get("connection_id").getAsLong();

        //remove the application from td list
        CoLocationProbableTDDTO coLocationProbableTDDTO = coLocationProbableTDService.getCoLocationProbableTDByConnectionID(connectionID);
        coLocationProbableTDDTO.setTDRequested(true);


        coLocationProbableTDService.updateCoLocationProbableTDClient(coLocationProbableTDDTO);


    }


}
