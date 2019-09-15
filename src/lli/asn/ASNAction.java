package lli.asn;

import annotation.ForwardedAction;
import annotation.JsonPost;
import application.ApplicationService;
import application.ApplicationState;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import global.GlobalService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
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

@Log4j
@ActionRequestMapping(ASNConstant.ASN_BASE_URL)
public class ASNAction extends AnnotatedRequestMappingAction {
    @Service private ASNApplicationService asnApplicationService;
    @Service private FlowDataBuilder flowDataBuilder;
    @Service private ASNService asnService;

    @ForwardedAction
    @RequestMapping(mapping = "/add", requestMethod = RequestMethod.GET)
    public String addGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_ADD);
        return ASNConstant.ASN_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/add", requestMethod = RequestMethod.POST)
    public void addPost(@RequestParameter(value = "application", isJsonBody = true) String application, LoginDTO loginDTO) throws Exception {
        asnApplicationService.insertBatchOperation(application, loginDTO);

    }

    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchAsn(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_ASN_APP, request,
                asnApplicationService, SessionConstants.VIEW_ASN_APP, SessionConstants.SEARCH_ASN_APP);
        rnManager.doJobCustom(loginDTO);
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_APP_SEARCH);
        return ASNConstant.ASN_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/details", requestMethod = RequestMethod.GET)
    public String detailsGet(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_APP_DETAILS);
        return ASNConstant.ASN_BASE_URL;
    }

    @RequestMapping(mapping = "/flow-data", requestMethod = RequestMethod.All)
    public JsonObject getApplicationDataByApplicationId(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonObject();

        ASNApplication asnApplication =(ASNApplication) ServiceDAOFactory.getService(ApplicationService.class).getApplicationByApplicationId(applicationID);
        List<ASN> asnList = asnApplicationService.getASNSByAppId(asnApplication.getAsnAppId());
        asnApplication.setAsns(asnList);
        jsonObject = flowDataBuilder.getCommonPart_new(asnApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "/request-reject", requestMethod = RequestMethod.POST)
    public void rejectRequest(
            @RequestParameter(value = "application", isJsonBody = true) ASNApplication application
            , LoginDTO loginDTO
    ) throws Exception {
        ASNApplication asnApplication =(ASNApplication) ServiceDAOFactory.getService(ApplicationService.class).getApplicationByApplicationId(application.getApplicationId());
        List<ASN> asnList = asnApplicationService.getASNSByAppId(asnApplication.getAsnAppId());
        asnList.forEach(s->{
            s.setState(ApplicationState.REQUEST_REJECT.getState());
            ServiceDAOFactory.getService(GlobalService.class).update(s);
        });
    }

    @JsonPost
    @RequestMapping(mapping = "/request-accept", requestMethod = RequestMethod.POST)
    public void acceptRequest(@RequestParameter(value = "application", isJsonBody = true) ASNApplication application, LoginDTO loginDTO) throws Exception {
        asnApplicationService.acceptASNRequest(application,loginDTO);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/search-asn", requestMethod = RequestMethod.All)
    public String searchActiveASN(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_ASN, request,
                asnService, SessionConstants.VIEW_ASN, SessionConstants.SEARCH_ASN);
        rnManager.doJob(loginDTO);
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_SEARCH);
        return ASNConstant.ASN_BASE_URL;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/details-asn", requestMethod = RequestMethod.GET)
    public String detailsASN(@RequestParameter("id") long asnId, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_DETAILS);
        return ASNConstant.ASN_BASE_URL;
    }


    @RequestMapping(mapping = "/flow-data-view", requestMethod = RequestMethod.All)
    public JsonObject getASNByID(@RequestParameter("id") long asnId, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonObject();
        ASN asn = ServiceDAOFactory.getService(GlobalService.class).findByPK(ASN.class,asnId);
        List<ASNmapToIP> asNmapToIPS = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(ASNmapToIP.class,
                new ASNmapToIPConditionBuilder()
                        .Where()
                        .asnIdEquals(asn.getId())
                        .isDeletedEquals(0)
                        .getCondition());
        asn.setAsNmapToIPS(asNmapToIPS);
        jsonObject = flowDataBuilder.getASNJson(asn, jsonObject, loginDTO);
        return jsonObject;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/edit", requestMethod = RequestMethod.GET)
    public String editGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_EDIT);
        return ASNConstant.ASN_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/edit", requestMethod = RequestMethod.POST)
    public void editASN(@RequestParameter(value = "application", isJsonBody = true) String application, LoginDTO loginDTO) throws Exception {
        asnApplicationService.editBatchOperation(application, loginDTO);
        /*JsonElement jsonElement = new JsonParser().parse(application);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long asnId = jsonObject.get("asnId").getAsLong();
        JsonObject jsonObjectNew = new JsonObject();
        ASN asn = ServiceDAOFactory.getService(GlobalService.class).findByPK(ASN.class,asnId);
        List<ASNmapToIP> asNmapToIPS = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(ASNmapToIP.class,
                new ASNmapToIPConditionBuilder()
                        .Where()
                        .asnIdEquals(asn.getId())
                        .isDeletedEquals(0)
                        .getCondition());
        asn.setAsNmapToIPS(asNmapToIPS);
        jsonObjectNew = flowDataBuilder.getASNJson(asn, jsonObjectNew, loginDTO);
        return jsonObjectNew;*/
    }

    @JsonPost
    @RequestMapping(mapping = "/delete-ip", requestMethod = RequestMethod.POST)
    public void deleteasnip(@RequestParameter(value = "id", isJsonBody = true) long ipId, LoginDTO loginDTO) throws Exception {
        asnApplicationService.deleteBatchOperation(ipId);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/details-view", requestMethod = RequestMethod.GET)
    public String detailsAppView(@RequestParameter("id") long asnId, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(ASNConstant.ASN_SESSION_URL, ASNConstant.ASN_APP_DETAILS_VIEW);
        return ASNConstant.ASN_BASE_URL;
    }

}
