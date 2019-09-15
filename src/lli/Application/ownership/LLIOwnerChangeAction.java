package lli.Application.ownership;


import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.LLIConnectionService;
import login.LoginDTO;
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
import java.util.List;

@ActionRequestMapping("lli/ownershipChange")
public class LLIOwnerChangeAction extends AnnotatedRequestMappingAction {

    @Service
    OfficialLetterService officialLetterService;

    @Service
    LLIOwnerChangeService lliOwnerChangeService;

    @Service
    LLIOwnerChangeFlowDataBuilder ownerChangeFlowDataBuilder;

    @Service
    LLIOnProcessConnectionService lliOnProcessConnectionService;

    @Service
    LLIConnectionService lliConnectionService ;

    @ForwardedAction
    @RequestMapping(mapping = "/application-insert", requestMethod = RequestMethod.GET)
    public String newConnectionView(){
        return "lli-application-change-ownership";
    }

    @JsonPost
    @RequestMapping(mapping = "/application-insert", requestMethod = RequestMethod.POST)
    public String postNewConnectionApplicationForm1(
            @RequestParameter(isJsonBody = true, value = "application") String application,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(application);
        lliOwnerChangeService.insertBatchOperation(jelement,loginDTO);
        return "oka";
    }



    //Application Search Page
    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchOwnerChange(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_OWNER_CHANGE, request,
                lliOwnerChangeService, SessionConstants.VIEW_LLI_OWNER_CHANGE, SessionConstants.SEARCH_LLI_OWNER_CHANGE);
        rnManager.doJobCustom(loginDTO);
        return "owner-change-search";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/owner-change-view", requestMethod = RequestMethod.GET)
    public String ownerChangeView(@RequestParameter("id") long applicationID,
                                  LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        return "owner-change-view";
    }

//    flow-data


    @RequestMapping(mapping = "/flow-data", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData2(
            @RequestParameter("id") long applicationID,
            LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = lliOwnerChangeService.getApplicationById(applicationID);

        jsonObject = ownerChangeFlowDataBuilder.getCommonPart_new(lliOwnerShipChangeApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "/change-state", requestMethod = RequestMethod.All)
    public String changeState(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        lliOwnerChangeService.updateApplicatonState(appID, state);//this need to update state
        return "";
    }

    @JsonPost
    @RequestMapping(mapping = "/no-state-change", requestMethod = RequestMethod.POST)
    public String changeStyate(@RequestParameter(isJsonBody = true, value = "data") String JsonString,
                               LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/demand-note-generate", requestMethod = RequestMethod.POST)
    public String DemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/advice-note-generate", requestMethod = RequestMethod.POST)
    public void generateAN(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        lliOwnerChangeService.handleGenerateAdviceNoteRequest(JsonString, loginDTO);
    }

    //complete-connection

    @JsonPost
    @RequestMapping(mapping = "/complete-owner-change", requestMethod = RequestMethod.POST)
    public void completeOwnerChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        lliOwnerChangeService.handOwnerChangeComplete(JsonString, loginDTO);
    }


    @ForwardedAction
    @RequestMapping(mapping = "/view-all-connections-of-prev-owner", requestMethod = RequestMethod.All)
    public String searchLLIConnection(@RequestParameter("applicationId") long applicationId,
                                      @RequestParameter("clientId") long clientId,
                                      LoginDTO loginDTO, HttpServletRequest request)
            throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_CONNECTION, request,
                lliConnectionService, SessionConstants.VIEW_LLI_CONNECTION, SessionConstants.SEARCH_LLI_CONNECTION);
        rnManager.doJobCustom(loginDTO);

        List<LLIConnection> connections =lliOnProcessConnectionService.getOnProcessConnectionsByAppId(applicationId);

        request.setAttribute("connections", connections);
        request.setAttribute("clientId", clientId);

        return "lli-connections-of-prev-client";
    }
    @ForwardedAction
    @RequestMapping(mapping = "/details", requestMethod = RequestMethod.GET)
    public String getNewConnectionDetailsForm(
            @RequestParameter("id") long applicationID,
            LoginDTO loginDTO,
            HttpServletRequest request) throws Exception {
        return "owner-change-details";
    }

    //    get-ol-info.do
    @RequestMapping(mapping="/get-ol-info", requestMethod = RequestMethod.GET)
    public JsonArray getOfficialLetterInfoByAppId(
            @RequestParameter("appId") long appId,
            @RequestParameter("moduleId") int moduleId) throws Exception {

        return officialLetterService.getOfficialLettersByApplicationIdAndModuleId(appId, moduleId);
    }


    @JsonPost
    @RequestMapping(mapping = "/client-correction", requestMethod = RequestMethod.POST)
    public String clientCorrectionSubmit(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
//        Gson gson = new Gson();
//        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = LLIOwnerShipChangeApplication.deserialize(jsonObject);
//        lliOwnerShipChangeApplication.setId(appID);
//        lliOwnerShipChangeApplication.setState(state);
        LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = ServiceDAOFactory.getService(GlobalService.class).
                findByPK(LLIOwnerShipChangeApplication.class, appID);
        lliOwnerShipChangeApplication.setState(state);
        lliOwnerChangeService.updateApplicaton(lliOwnerShipChangeApplication);
        return "";//need to go search page
    }

}
