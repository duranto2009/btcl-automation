package upstream;

import annotation.ForwardedAction;
import annotation.JsonPost;
import application.ApplicationType;
import com.google.gson.JsonObject;
import common.ObjectPair;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import upstream.application.UpstreamApplication;
import upstream.application.UpstreamApplicationService;
import upstream.contract.UpstreamContract;
import upstream.contract.UpstreamContractService;
import upstream.flowUtils.UpstreamFlowDataBuilder;
import upstream.inventory.UpstreamInventoryService;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j
@ActionRequestMapping(UpstreamConstants.UPSTREAM_BASE_URL)
public class UpstreamAction extends AnnotatedRequestMappingAction {

    static Logger logger = Logger.getLogger(UpstreamAction.class);

    @Service
    private UpstreamInventoryService upstreamInventoryService;
    @Service private UpstreamApplicationService upstreamApplicationService;
    @Service private UpstreamContractService upstreamContractService;
//    @Service private  upstreamApplicationService;
    @Service
    private UpstreamFlowDataBuilder upstreamFlowDataBuilder;

    @ForwardedAction
    @RequestMapping(mapping = "/new-request", requestMethod = RequestMethod.GET)
    public String addGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.NEW_REQUEST);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/contract-extension", requestMethod = RequestMethod.GET)
    public String addContractExtensionGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {


        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_CONTRACT, request, upstreamContractService,
                SessionConstants.VIEW_UPSTREAM_CONTRACT,
                SessionConstants.SEARCH_UPSTREAM_CONTRACT
        );
        recordNavigationManager.doJobCustom(loginDTO);


        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.CONTRACT_EXTENSION);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/contract-close", requestMethod = RequestMethod.GET)
    public String closeContractGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_CONTRACT, request, upstreamContractService,
                SessionConstants.VIEW_UPSTREAM_CONTRACT,
                SessionConstants.SEARCH_UPSTREAM_CONTRACT
        );
        recordNavigationManager.doJobCustom(loginDTO);


        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.CONTRACT_CLOSE);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }
    @ForwardedAction
    @RequestMapping(mapping = "/contract-bandwidth-change", requestMethod = RequestMethod.GET)
    public String contractBandwidthChangeGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_CONTRACT, request, upstreamContractService,
                SessionConstants.VIEW_UPSTREAM_CONTRACT,
                SessionConstants.SEARCH_UPSTREAM_CONTRACT
        );
        recordNavigationManager.doJobCustom(loginDTO);


        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.CONTRACT_BANDWIDTH_CHANGE);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }
    //
    @JsonPost
    @RequestMapping(mapping = "/submit-new-request", requestMethod = RequestMethod.POST)
    public void addPost(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        ApplicationType applicationType = ApplicationType.UPSTREAM_NEW_REQUEST;
        upstreamApplicationService.saveApplication(application, loginDTO, applicationType);

    }

    @JsonPost
    @RequestMapping(mapping = "/submit-contract-extension-request", requestMethod = RequestMethod.POST)
    public void contractExtensionRequestPost(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        ApplicationType applicationType = ApplicationType.UPSTREAM_CONTRACT_EXTENSION_REQUEST;
        application.setApplicationId(0L);
        upstreamApplicationService.saveApplication(application, loginDTO, applicationType);

    }

    @JsonPost
    @RequestMapping(mapping = "/submit-contract-close-request", requestMethod = RequestMethod.POST)
    public void contractCloseRequestPost(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        ApplicationType applicationType = ApplicationType.UPSTREAM_CONTRACT_CLOSE_REQUEST;
        application.setApplicationId(0L);
        upstreamApplicationService.saveApplication(application, loginDTO, applicationType);

    }

    @JsonPost
    @RequestMapping(mapping = "/submit-contract-bandwidth-change-request", requestMethod = RequestMethod.POST)
    public void contractBandwidthChangeRequestPost(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {

        upstreamApplicationService.bandwidthChangeApplicationSubmit(application,loginDTO);

    }


    @JsonPost
    @RequestMapping(mapping = "/submit-change", requestMethod = RequestMethod.POST)
    public void changeRequestPost(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        upstreamApplicationService.changeApplication(application, loginDTO);

    }

    @RequestMapping(mapping = "/save-new-item", requestMethod = RequestMethod.POST)
    public void saveNewItem(HttpServletRequest request,
                            @RequestParameter("item_type") String itemType,
                            @RequestParameter("item_name") String itemName) throws Exception {
        upstreamInventoryService.addNewItem(itemType, itemName);
    }

    //to be used for dropdown menu in UI
    @RequestMapping(mapping = "/get-items-by-type", requestMethod = RequestMethod.GET)
    public List<ObjectPair<Long, String>> getItemsByType(@RequestParameter("item_type") String itemType) throws Exception {
        return upstreamInventoryService.getAllUpstreamItemsInPairByItemType(itemType);
    }


    @RequestMapping(mapping = "/application-data", requestMethod = RequestMethod.All)
    public JsonObject getApplicationDataByApplicationId(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonObject();

        UpstreamApplication upstreamApplication = upstreamApplicationService.getApplicationByApplicationId(applicationID);

        jsonObject = upstreamFlowDataBuilder.getCommonPart_new(upstreamApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping = "/contract-data", requestMethod = RequestMethod.All)
    public List<UpstreamContract> getContractList(@RequestParameter("historyId") long historyId, LoginDTO loginDTO) throws Exception {
        List<UpstreamContract> contractList = upstreamContractService.getAllContractsByHistoryId(historyId);
        return contractList;
    }

    @RequestMapping(mapping = "/contract-data-by-id", requestMethod = RequestMethod.All)
    public UpstreamContract getContractByContractId(@RequestParameter("contractId") long contractId, LoginDTO loginDTO) throws Exception {
        return upstreamContractService.getContractByContractId(contractId);
    }

    @RequestMapping(mapping = "/get-all-contracts", requestMethod = RequestMethod.All)
    public List<UpstreamContract> getAllContractList(LoginDTO loginDTO) throws Exception {
        List<UpstreamContract> contractList = upstreamContractService.getAllActiveContracts();
        return contractList;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/request-details", requestMethod = RequestMethod.GET)
    public String requestDetailsGet(@RequestParameter("id") long applicationId, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.REQUEST_DETAILS);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/contract-details", requestMethod = RequestMethod.GET)
    public String contractDetailsGet(@RequestParameter("id") long applicationId, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.CONTRACT_DETAILS);
        return UpstreamConstants.UPSTREAM_BASE_URL;
    }


    @RequestMapping(mapping = "/request-search", requestMethod = RequestMethod.All)
    public ActionForward getSearchUpstreamRequest(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_REQUEST, request, upstreamApplicationService,
                SessionConstants.VIEW_UPSTREAM_REQUEST,
                SessionConstants.SEARCH_UPSTREAM_REQUEST
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.REQUEST_SEARCH);
        return mapping.findForward(UpstreamConstants.UPSTREAM_BASE_URL);
    }

    @RequestMapping(mapping = "/contract-search", requestMethod = RequestMethod.All)
    public ActionForward getSearchUpstreamContract(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_CONTRACT, request, upstreamContractService,
                SessionConstants.VIEW_UPSTREAM_CONTRACT,
                SessionConstants.SEARCH_UPSTREAM_CONTRACT
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.CONTRACT_SEARCH);
        return mapping.findForward(UpstreamConstants.UPSTREAM_BASE_URL);
    }

    @RequestMapping(mapping = "/invoice-search", requestMethod = RequestMethod.All)
    public ActionForward getSearchUpstreamInvoice(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_UPSTREAM_CONTRACT, request, upstreamContractService,
                SessionConstants.VIEW_UPSTREAM_CONTRACT,
                SessionConstants.SEARCH_UPSTREAM_CONTRACT
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(UpstreamConstants.UPSTREAM_SESSION_URL, UpstreamConstants.INVOICE_SEARCH);
        return mapping.findForward(UpstreamConstants.UPSTREAM_BASE_URL);
    }

    @JsonPost
    @RequestMapping(mapping = "/application-update", requestMethod = RequestMethod.POST)
    public void applicationUpdate(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        upstreamApplicationService.update(application, loginDTO);
    }
    @JsonPost
    @RequestMapping(mapping = "/update-circuit-info", requestMethod = RequestMethod.POST)
    public void updateCircuitInfo(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        upstreamApplicationService.updateCircuitInformation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/connection-complete", requestMethod = RequestMethod.POST)
    public void completeConnection(@RequestParameter(value = "application", isJsonBody = true) UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        upstreamApplicationService.connectionCreatorOrUpdaterManager(application, loginDTO);


    }


}
