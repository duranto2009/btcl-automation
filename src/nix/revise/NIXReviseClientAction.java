package nix.revise;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import common.RequestFailureException;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.LLIDropdownPair;
import login.LoginDTO;
import nix.constants.NIXConstants;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;
import vpn.client.ClientDAO;
import vpn.clientContactDetails.ClientContactDetailService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ActionRequestMapping("nix/revise/")
public class NIXReviseClientAction extends AnnotatedRequestMappingAction {

    private static Logger logger = LoggerFactory.getLogger(NIXReviseClientAction.class);

    ClientDAO vpnDAO = new ClientDAO();

    @Service
    ClientContactDetailService clientContactDetailService;
    @Service
    NIXProbableTDClientService nixProbableTDClientService;
    @Service
    NIXReviseService nixreviseService;

    @Service
    NIXReviseClientDataBuilder nixreviseClientDataBuilder;


    @Service
    NIXClientTDService nixClientTDService;

    //todo comment for revise application

    @JsonPost
    @RequestMapping(mapping = "tdrequest", requestMethod = RequestMethod.POST)
    public void TDSubmitByClientID(
            @RequestParameter(isJsonBody = true, value = "data") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        NIXReviseDTO reviseDTO = new NIXReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        reviseDTO.setApplicationType(NIXConstants.NIX_TD);
        reviseDTO.setState(NIXConstants.TD_SUBMIT_STATE);
        boolean isRequested = false;
        isRequested = nixreviseService.isAlreadyRequested(reviseDTO.getClientID(), reviseDTO.getApplicationType());
        if (isRequested) {
            throw new RequestFailureException("This Client Already have an TD Request ");
        }
        nixreviseService.insertApplication(reviseDTO, loginDTO);
        Comments comment = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        comment.setApplicationID(reviseDTO.getId());
        comment.setStateID(reviseDTO.getState());
    }

    @RequestMapping(mapping = "searchprobable", requestMethod = RequestMethod.All)
    public ActionForward getSearchDNSDomainPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_NIX_PROBABLE_TD_CLIENT, request, nixProbableTDClientService,
                SessionConstants.VIEW_NIX_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_NIX_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        return mapping.findForward("search-client-td-status");
    }

    @RequestMapping(mapping = "revise-client-get-flow", requestMethod = RequestMethod.All)
    public JsonObject getClientData(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        NIXReviseDTO reviseDTO = nixreviseService.getappById(applicationID);
        JsonObject jsonObject = new JsonObject();

        jsonObject = nixreviseClientDataBuilder.getCommonPart_new(reviseDTO, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "reconnectrequest", requestMethod = RequestMethod.POST)
    public void ReconnectSubmitByClientID(@RequestParameter(isJsonBody = true, value = "data") String jsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        NIXReviseDTO reviseDTO = new NIXReviseClientDeserializer().deserialize_custom_reconnection(jsonElement, loginDTO);
       nixreviseService.reconnectRequestBatchOperation(reviseDTO,loginDTO);
    }

    @RequestMapping(mapping = "search", requestMethod = RequestMethod.All)
    public ActionForward getRequestedTDList(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_NIX_CLIENT_REVISE, request, nixreviseService,
                SessionConstants.VIEW_NIX_CLIENT_REVISE,
                SessionConstants.SEARCH_NIX_CLIENT_REVISE
        );
        recordNavigationManager.doJobCustom(loginDTO);
        return mapping.findForward("search");
    }

    @RequestMapping(mapping = "tdcreate", requestMethod = RequestMethod.GET)
    public ActionForward getTDCreateView(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
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
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "changenostate", requestMethod = RequestMethod.POST)
    public String applicationStateNoChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "changestate", requestMethod = RequestMethod.POST)
    public String applicationStateChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixreviseService.updateApplicatonState(appID, state);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "advicenotegenerate", requestMethod = RequestMethod.POST)
    public String generateAN(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        if (jsonObject.get("senderId") == null) {
            throw new RequestFailureException("Invalid Login");
        }
        long senderId = jsonObject.get("senderId").getAsLong();
        JsonArray userArray = jsonObject.getAsJsonArray("userList");
        nixreviseService.updateApplicatonState(appID, state);
        nixreviseService.generateAdviceNoteDocument(appID, state, userArray, senderId);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "completetd", requestMethod = RequestMethod.POST)
    public String completeConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixClientTDService.tempDisconnectClientByClientID(nixreviseService.getappById(appID).getClientID(), System.currentTimeMillis());
        NIXReviseDTO reviseDTO = nixreviseService.getappById(appID);
        reviseDTO.setCompleted(true);
        nixreviseService.updateApplicaton(reviseDTO);
        nixreviseService.updateApplicatonState(appID, state);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "completereconnection", requestMethod = RequestMethod.POST)
    public String completeReConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        nixClientTDService.reconnectClientByClientID(nixreviseService.getappById(appID).getClientID(), System.currentTimeMillis());
        nixreviseService.updateApplicatonState(appID, state);
        return "";//need to go search page
    }

    @RequestMapping(mapping="get-all-td", requestMethod=RequestMethod.GET)
    public List<LLIDropdownPair> getAllTd(LoginDTO loginDTO) throws Exception {
        return nixClientTDService.getallTd();
    }

}
