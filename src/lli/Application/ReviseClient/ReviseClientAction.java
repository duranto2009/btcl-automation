package lli.Application.ReviseClient;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import common.ClientConstants;
import common.CommonSelector;
import common.ModuleConstants;
import common.RequestFailureException;
import global.GlobalService;
import lli.Application.ChangeBillingAddress.LLINewBillingAddressApplication;
import lli.Application.ChangeBillingAddress.NewBillingAddressChangeService;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.client.td.LLIClientTDService;
import lli.client.td.LLIProbableTDClientService;
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
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.servlet.http.HttpServletRequest;

@ActionRequestMapping("lli/revise/")
public class ReviseClientAction extends AnnotatedRequestMappingAction {

    @Service private ClientContactDetailService clientContactDetailService;
    @Service private NewBillingAddressChangeService newBillingAddressChangeService;
    @Service private ReviseService reviseService;
    @Service private ReviseClientDataBuilder reviseClientDataBuilder;
    @Service private LLIProbableTDClientService lliProbableTDClientService;
    @Service private LLIClientTDService lliClientTDService;

    @Service
    GlobalService globalService;

    //todo comment for revise application

    @JsonPost
    @RequestMapping(mapping = "tdrequest", requestMethod = RequestMethod.POST)
    public void TDSubmitByClientID(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
//		lliClientTDService.tempDisconnectClientByClientID(clientID,System.currentTimeMillis());



        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        reviseDTO.setApplicationType(LLIConnectionConstants.TD);
        reviseDTO.setState(LLIConnectionConstants.TD_SUBMIT_STATE);
        boolean isRequested = false;
        isRequested = reviseService.isAlreadyRequested(reviseDTO.getClientID(), reviseDTO.getApplicationType());

        if (isRequested) {
            throw new RequestFailureException("This Client Already have an TD Request ");

        }


        reviseService.insertApplication(reviseDTO, loginDTO);
        reviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);

//        String Comment = jsonObject.get("comment").getAsString();
        Comments comment = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        comment.setApplicationID(reviseDTO.getId());
        comment.setStateID(reviseDTO.getState());

    }

    @RequestMapping(mapping = "searchprobable", requestMethod = RequestMethod.All)
    public ActionForward getSearchDNSDomainPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_LLI_PROBABLE_TD_CLIENT, request, lliProbableTDClientService,
                SessionConstants.VIEW_LLI_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_LLI_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("search-client-td-status");
    }


    @JsonPost
    @RequestMapping(mapping = "reconnectrequest", requestMethod = RequestMethod.POST)
    public void ReconnectSubmitByClientID(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
//		lliClientTDService.tempDisconnectClientByClientID(clientID,System.currentTimeMillis());

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);

        boolean isDisconnected= lliClientTDService.isClientTemporarilyDisconnected(reviseDTO.getClientID());
        if(!isDisconnected){
            throw new RequestFailureException("Client is not Temporarily Disconnected");
        }

        reviseDTO.setApplicationType(LLIConnectionConstants.RECONNECT);
        reviseDTO.setState(LLIConnectionConstants.RECONNECT_SUBMIT_STATE);

        reviseService.insertApplication(reviseDTO, loginDTO);
        reviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);



//        String Comment = jsonObject.get("comment").getAsString();
//        Comments comment = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        comment.setApplicationID(reviseDTO.getId());
//        comment.setStateID(reviseDTO.getState());

    }


/*
    @RequestMapping(mapping = "search", requestMethod = RequestMethod.All)
    public ActionForward getSearchTDPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_LLI_PROBABLE_TD_CLIENT, request, reviseService,
                SessionConstants.VIEW_LLI_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_LLI_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("search-client-revise-search");
    }*/


    @RequestMapping(mapping = "revise-client-get-flow", requestMethod = RequestMethod.All)
    public JsonObject getClientData(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {

        Gson gson = new Gson();
        ReviseDTO reviseDTO = reviseService.getappById(applicationID);
        JsonObject jsonObject = new JsonObject();

        jsonObject = reviseClientDataBuilder.getCommonPart_new(reviseDTO, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping = "search", requestMethod = RequestMethod.All)
    public ActionForward getRequestedTDList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_LLI_CLIENT_REVISE, request, reviseService,
                SessionConstants.VIEW_LLI_CLIENT_REVISE,
                SessionConstants.SEARCH_LLI_CLIENT_REVISE
        );
        recordNavigationManager.doJobCustom(loginDTO);
        return mapping.findForward("search");
    }

    @RequestMapping(mapping = "tdcreate", requestMethod = RequestMethod.GET)
    public ActionForward getTDCreateView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {

//        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("tdcreate");
    }

    @ForwardedAction
    @RequestMapping(mapping = "tdcreate", requestMethod = RequestMethod.GET)
    public String getTDCreateViewPost(@RequestParameter("id") long id) throws Exception {

        return "tdcreate";
    }

    @RequestMapping(mapping = "newview", requestMethod = RequestMethod.All)
    public ActionForward getApplicationNewView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        return mapping.findForward("newview");
    }


    @JsonPost
    @RequestMapping(mapping = "demandnotegenerate", requestMethod = RequestMethod.POST)
    public String demandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

//        reviseService.updateApplicatonState(appID, state);


        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "changenostate", requestMethod = RequestMethod.POST)
    public String applicationStateNoChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "changestate", requestMethod = RequestMethod.POST)
    public String applicationStateChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        reviseService.updateApplicatonState(appID, state);
        ReviseDTO reviseDTO=reviseService.getappById(appID);
        reviseService.sendNotification(reviseDTO, state,loginDTO);


//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "advicenotegenerate", requestMethod = RequestMethod.POST)
    public String generateAN(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
//		int state = 33;

        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();

        JsonArray userArray = jsonObject.getAsJsonArray("userList");


        // jhamela ache : transaction error

        ReviseDTO reviseDTO=reviseService.getappById(appID);
        reviseDTO.setState(state);

        globalService.update(reviseDTO);
        reviseService.generateAdviceNoteDocument(reviseDTO, state, userArray, senderId);
        reviseService.sendNotification(reviseDTO, state,loginDTO);



        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "completetd", requestMethod = RequestMethod.POST)
    public String completeConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        lliClientTDService.tempDisconnectClientByClientID(reviseService.getappById(appID).getClientID(), System.currentTimeMillis());
        ReviseDTO reviseDTO = reviseService.getappById(appID);
        reviseDTO.setCompleted(true);
        reviseService.updateApplicaton(reviseDTO);
        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO, state,loginDTO);


        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "completereconnection", requestMethod = RequestMethod.POST)
    public String completeReConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        ReviseDTO reviseDTO=reviseService.getappById(appID);


        lliClientTDService.reconnectClientByClientID(reviseDTO.getClientID(), System.currentTimeMillis());


//
//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);
//
        reviseService.updateApplicatonState(appID, state);

        reviseService.sendNotification(reviseDTO, state,loginDTO);


        return "";//need to go search page
    }

    // revision: long term

    // new long term
    @JsonPost
    @RequestMapping(mapping = "newlongterm", requestMethod = RequestMethod.POST)
    public void longTermSubmitByClientID(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        reviseDTO.setApplicationType(LLIConnectionConstants.NEW_LONG_TERM);
        reviseDTO.setState(LLIConnectionConstants.NEW_LONG_TERM_SUBMITTED);

        reviseService.insertApplication(reviseDTO, loginDTO);
        reviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);
        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "reject-longterm", requestMethod = RequestMethod.POST)
    public void rejectLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        ReviseDTO reviseDTO =reviseService.getappById(appID);

        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "reopen-longterm", requestMethod = RequestMethod.POST)
    public void reopenLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        ReviseDTO reviseDTO =reviseService.getappById(appID);



        reviseService.updateApplicatonState(appID, state);
        reviseService.comment(jsonElement, loginDTO, reviseDTO);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

    }

    @JsonPost
    @RequestMapping(mapping = "complete-longterm", requestMethod = RequestMethod.POST)
    public void completeLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        reviseService.establishNewLongtermContract(jsonObject, jsonElement, loginDTO);
//        reviseService.generateAdviceNoteDocument(appID, );
    }

    // break long term
    @JsonPost
    @RequestMapping(mapping = "break-longterm", requestMethod = RequestMethod.POST)
    public void breakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        reviseDTO.setApplicationType(LLIConnectionConstants.BREAK_LONG_TERM);
        reviseDTO.setState(LLIConnectionConstants.BREAK_LONG_TERM_SUBMITTED);

        reviseService.insertApplication(reviseDTO, loginDTO);
        reviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "reject-break-longterm", requestMethod = RequestMethod.POST)
    public void rejecBreaktLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        ReviseDTO reviseDTO = reviseService.getappById(appID);
        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "reopen-break-longterm", requestMethod = RequestMethod.POST)
    public void reopenBreakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        ReviseDTO reviseDTO = reviseService.getappById(appID);

        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "approve-break-longterm", requestMethod = RequestMethod.POST)
    public void approveBreakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);

        //TODO: maruf: generate demand note here
    }

    @JsonPost
    @RequestMapping(mapping = "paid-break-longterm", requestMethod = RequestMethod.POST)
    public void paidBreakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        ReviseDTO reviseDTO =reviseService.getappById(appID);


        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "verified-payment-break-longterm", requestMethod = RequestMethod.POST)
    public void verifyPaymentBreakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        ReviseDTO reviseDTO = new ReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        ReviseDTO reviseDTO =reviseService.getappById(appID);


        reviseService.updateApplicatonState(appID, state);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        reviseService.comment(jsonElement, loginDTO, reviseDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "complete-break-longterm", requestMethod = RequestMethod.POST)
    public void completeBreakLongTerm(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        reviseService.demolishLongTermContract(jsonObject, jsonElement, loginDTO);

    }


    //billing address change

    @ForwardedAction
    @RequestMapping(mapping = "change-billing-address", requestMethod = RequestMethod.GET)
    public String getChangeBillingAddressApplicationForm() throws Exception {
        return "lli-application-change-billing-address";
    }

    @JsonPost
    @RequestMapping(mapping = "change-billing-address", requestMethod = RequestMethod.POST)
    public String postChangeBillingAddressApplicationForm(
            @RequestParameter(isJsonBody = true, value = "application") String lliChangeBillingAddressApplication,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(lliChangeBillingAddressApplication);
        LLINewBillingAddressApplication lliNewBillingAddressApplication = new LLINewBillingAddressApplication().deserialize(jelement);

        ReviseClientDeserializer reviseClientDeserializer = new ReviseClientDeserializer();
        ReviseDTO reviseDTO = reviseClientDeserializer.deserialize_custom(jelement, loginDTO);
        reviseDTO.setApplicationType(LLIConnectionConstants.CHANGE_BILLING_ADDRESS);
        reviseDTO.setState(LLIConnectionConstants.BILLING_ADDRESS_CHANGE_STATE);
        reviseService.insertApplication(reviseDTO, loginDTO);
        lliNewBillingAddressApplication.setApplicationID(reviseDTO.getId());
        newBillingAddressChangeService.insertNewBillingAddress(lliNewBillingAddressApplication);
        reviseService.sendNotification(reviseDTO, (int) reviseDTO.getState(),loginDTO);


        return "ok";
    }


    //billing-address-change-complete

    @JsonPost
    @RequestMapping(mapping = "billing-address-change-complete", requestMethod = RequestMethod.POST)
    public String postChangeBillingAddressApplicationComplete(
            @RequestParameter(isJsonBody = true, value = "application") String lliChangeBillingAddressApplication,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(lliChangeBillingAddressApplication);

        JsonObject jsonObject = jelement.getAsJsonObject();

        long appId = jsonObject.get("applicationID").getAsLong();

        LLINewBillingAddressApplication lliNewBillingAddressApplication = newBillingAddressChangeService.getNewBillingAddressByAppId(appId);

        ClientContactDetailsDTO clientContactDetailsDTO = newBillingAddressChangeService.deserialize_To_clientContactDetails(lliNewBillingAddressApplication);

        // TODO: 11/25/18 the below part need to check later

        clientContactDetailService.updateClientContactDetails(clientContactDetailsDTO);
        int state = jsonObject.get("nextState").getAsInt();
        reviseService.updateApplicatonState(appId, state);
        ReviseDTO reviseDTO=reviseService.getappById(appId);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        return "ok";
    }

    @RequestMapping(mapping = "get-client-billing-address", requestMethod = RequestMethod.GET)
    public ClientContactDetailsDTO getClientsContactDetailsDTO(
            @RequestParameter(value = "id") long clientId, LoginDTO loginDTO) throws Exception {
        int moduleID = ModuleConstants.Module_ID_LLI;
        ClientService service = new ClientService();
        CommonSelector commonSelector = new CommonSelector();
        commonSelector.moduleID = moduleID;

        ClientDetailsDTO dto = service.getClient(clientId, loginDTO, commonSelector);

        ClientContactDetailsDTO clientContactDetailsDTO = (dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_BILLING));

        return clientContactDetailsDTO;
    }

//    billing-address-change-rejected


    @JsonPost
    @RequestMapping(mapping = "billing-address-change-rejected", requestMethod = RequestMethod.POST)
    public String billingAddressChangeRejected(@RequestParameter(isJsonBody = true, value = "application") String lliChangeBillingAddressApplication,
                                               LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(lliChangeBillingAddressApplication);

        JsonObject jsonObject = jelement.getAsJsonObject();

        long appId = jsonObject.get("applicationID").getAsLong();

        int state = jsonObject.get("nextState").getAsInt();
        reviseService.updateApplicatonState(appId, state);
        ReviseDTO reviseDTO=reviseService.getappById(appId);
        reviseService.sendNotification(reviseDTO,state,loginDTO);

        return "ok";
    }


    //end billing address chagne
}
