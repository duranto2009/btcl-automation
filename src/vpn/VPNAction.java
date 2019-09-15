package vpn;

import annotation.ForwardedAction;
import annotation.JsonPost;
import application.ApplicationState;
import application.ApplicationType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.RequestFailureException;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopService;
import entity.office.Office;
import entity.office.OfficeService;
import global.GlobalService;
import lli.LLIDropdownClient;
import lli.LLIDropdownPair;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
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
import vpn.FlowConnectionManager.FlowConnectionManagerService;
import vpn.FlowConnectionManager.FlowDataBuilder;
import vpn.FlowConnectionManager.FlowViewDataBuilder;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationService;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;
import vpn.td.VPNProbableTDService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Log4j
@ActionRequestMapping(VPNConstants.VPN_BASE_URL)
public class VPNAction extends AnnotatedRequestMappingAction {

    @Service private VPNProbableTDService vpnProbableTDService;
    @Service private VPNApplicationService vpnApplicationService;
    @Service private VPNNetworkLinkService vpnNetworkLinkService;
    @Service private FlowDataBuilder flowDataBuilder;
    @Service private FlowViewDataBuilder flowViewDataBuilder;
    @Service private GlobalService globalService;
    @Service private LocalLoopService localLoopService;
    @Service private OfficeService officeService;
    @Service private FlowConnectionManagerService flowConnectionManagerService;
    @Service private VPNClientService vpnClientService;

    @ForwardedAction
    @RequestMapping(mapping = "/link/add", requestMethod = RequestMethod.GET)
    public String addGet(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.LINK_ADD);
        return VPNConstants.VPN_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/link/add", requestMethod = RequestMethod.POST)
    public void addPost(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {

        vpnApplicationService.insertBatchOperation(application, loginDTO);

    }


    @JsonPost
    @RequestMapping(mapping = "/link/shift", requestMethod = RequestMethod.POST)
    public void shiftLinkPost(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {

        vpnApplicationService.insertOrUpdateBatchOperationForShift(application, loginDTO);

    }


    @JsonPost
    @RequestMapping(mapping = "/link/application-update", requestMethod = RequestMethod.POST)
    public void applicationUpdate(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {

        if(application.getApplicationType()==ApplicationType.VPN_UPGRADE_CONNECTION){
            vpnApplicationService.updateAndCreateLoopForUpgradeOperation(application,loginDTO);
        }else{

            vpnApplicationService.updateBatchOperation(application, loginDTO);
        }
    }

    @JsonPost
    @RequestMapping(mapping = "/link/vendor-work-reject", requestMethod = RequestMethod.POST)
    public void applicationSRUpdate(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.vendorWorkRejectBatchOperation(application, loginDTO);
    }


    @JsonPost
    @RequestMapping(mapping = "/link/vendor-loop-reject", requestMethod = RequestMethod.POST)
    public void applicationGMUpdate(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.vendorLoopRejectBatchOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/vendor-work-approve", requestMethod = RequestMethod.POST)
    public void applicationSRApprove(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.markEFrAsCollaborated(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/application-forward", requestMethod = RequestMethod.POST)
    public void applicationForward(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.forwardRequest(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/application-wo-forward", requestMethod = RequestMethod.POST)
    public void applicationWOForward(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.WOforwardRequest(application, loginDTO);
    }


    @JsonPost
    @RequestMapping(mapping = "/link/close-wo-forward", requestMethod = RequestMethod.POST)
    public void closeWOForward(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.CloseWOforwardRequest(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/connection-complete", requestMethod = RequestMethod.POST)
    public void completeConnection(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        flowConnectionManagerService.connectionCreatorOrUpdaterManager(application, loginDTO);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/link/details", requestMethod = RequestMethod.GET)
    public String detailsGet(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.LINK_DETAILS);
        return VPNConstants.VPN_BASE_URL;
    }


    @JsonPost
    @RequestMapping(mapping = "/link/efr-request", requestMethod = RequestMethod.POST)
    public void efrRequest(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.efrRequestLogicOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/check-if-skip", requestMethod = RequestMethod.POST)
    public void skipCheckChange(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {

                LocalLoop localOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getLocalOfficeLoopId());
                LocalLoop remoteOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getRemoteOfficeLoopId());

                if(localOfficeLoop.isIfrFeasibility()&&remoteOfficeLoop.isIfrFeasibility()){


                    if (!vpnApplicationLink.isSkipPayment()
                            &&
                            (vpnApplicationLink.getLinkState()== ApplicationState.VPN_DEMAND_NOTE_SKIP
                                    ||vpnApplicationLink.getLinkState()== ApplicationState.VPN_WITHOUT_LOOP_DEMAND_NOTE_SKIP)


                    ) {
                        throw new RequestFailureException("You Can not Skip This Demand note");
                    }else if (vpnApplicationLink.isSkipPayment()
                            &&
                            (vpnApplicationLink.getLinkState()== ApplicationState.VPN_DEMAND_NOTE_GENERATED
                                    ||vpnApplicationLink.getLinkState()== ApplicationState.VPN_WITHOUT_LOOP_DEMAND_NOTE_GENERATED)
                    ){
                        throw new RequestFailureException("You Should Skip This Demand note as per client Request");
                    }
                }else{
                    throw new RequestFailureException("Ifr Feasibility of Vpn Link With Id: "+vpnApplicationLink.getId()+ " is negative. Please Reject this application and try again ");
                }
            }
        }else{
            throw new RequestFailureException(" No Link Found for this application");
        }
    }

    @JsonPost
    @RequestMapping(mapping = "/link/no-state-change", requestMethod = RequestMethod.POST)
    public void noStateChange(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) {
        //TODO add comment if necessary( by forhad )
//        vpnApplicationService.efrRequestLogicOperation(application);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/efr-response", requestMethod = RequestMethod.POST)
    public void efrResponse(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.efrResponseLogicOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/efr-select", requestMethod = RequestMethod.POST)
    public void efrSelect(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.efrSelectLogicOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/work-order-generate", requestMethod = RequestMethod.POST)
    public void giveWorkOrder(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.workOrderGiveLogicOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/work-done", requestMethod = RequestMethod.POST)
    public void doneWorkOrder(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application , LoginDTO loginDTO) throws Exception {
        vpnApplicationService.workOrderDoneLogicOperation(application, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/loop-distance-approval", requestMethod = RequestMethod.POST)
    public void approveWorkOrderandLoopDistance(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.LoopDistanceApproveLogicOperation(application, loginDTO);
    }


    @JsonPost
    @RequestMapping(mapping = "/link/close-wo-approval", requestMethod = RequestMethod.POST)
    public void approveWorkOrderandLoopDistanceClose(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.closeApprovalAndForward(application, loginDTO);
    }

//    @JsonPost
//    @RequestMapping(mapping = "/link/cdgm-loop-distance-approval", requestMethod = RequestMethod.POST)
//    public void approveWorkOrderandLoopDistanceCDGM(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
//        vpnApplicationService.LoopDistanceApproveLogicOperation(application,loginDTO);
//    }

    @JsonPost
    @RequestMapping(mapping = "/link/advice-note-generate", requestMethod = RequestMethod.POST)
    public void generateAN(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application,
                           @RequestParameter(value = "ccList", isJsonBody = true) String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement element = new JsonParser().parse(JsonString);

        JsonArray jsonArray=new JsonArray();
        if(element.isJsonArray()){

            jsonArray= element.getAsJsonArray();
        }else{

        }

        vpnApplicationService.generateAdviceNote(application, jsonArray, loginDTO);
    }

    @RequestMapping(mapping = "/link/flow-data", requestMethod = RequestMethod.All)
    public JsonObject getApplicationDataByApplicationId(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonObject();

        VPNApplication vpnApplication = vpnApplicationService.getApplicationByApplicationId(applicationID);

        jsonObject = flowDataBuilder.getCommonPart_new(vpnApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping = "/link/flow-view-data", requestMethod = RequestMethod.All)
    public JsonObject getApplicationViewDataByApplicationId(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = new JsonObject();

        VPNApplication vpnApplication = vpnApplicationService.getApplicationByApplicationId(applicationID);

        jsonObject = flowViewDataBuilder.getCommonPart_new(vpnApplication, jsonObject, loginDTO);
        return jsonObject;
    }



    @RequestMapping(mapping = "/link/get-office-by-client", requestMethod = RequestMethod.All)
    public List<Office> getOfficeByClient(@RequestParameter("clientId") long clientId) throws Exception {

        return officeService.getOfficesByClientId(clientId);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/ifr-request-show", requestMethod = RequestMethod.POST)
    public void ifrRequest(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, HttpServletRequest request) throws Exception {

//        application.getOffices().forEach( office ->{
////            Office office = t;
////            office.setLastModificationTime(System.currentTimeMillis());
////            office.setVpnApplicationId(application.getVpnApplicationId());
//            try {
////                officeService.insertOffice(office);
//                office.getLoops().forEach(loop->{
////                    x.setOfficeId(office.getId());
////                    x.setVpnApplicationId(application.getVpnApplicationId());
//                    try {
//                        localLoopService.updateApplicaton(loop);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        vpnApplicationService.updateApplicatonState(application.getVpnApplicationId(),application.getApplicationState().getState());

        int x = 0;
    }

    //Application Search Page
    @ForwardedAction
    @RequestMapping(mapping = "/link/search", requestMethod = RequestMethod.All)
    public String searchLink(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_VPN_LINK, request,
                vpnApplicationService, SessionConstants.VIEW_VPN_LINK, SessionConstants.SEARCH_VPN_LINK);
        rnManager.doJobCustom(loginDTO);
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.LINK_SEARCH);
        return VPNConstants.VPN_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/link/add-comment", requestMethod = RequestMethod.POST)
    public void addComment(@RequestParameter(value = "application", isJsonBody = true) VPNApplication application, LoginDTO loginDTO) throws Exception {
        vpnApplicationService.addCommentOnALink(application, loginDTO);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/network/search", requestMethod = RequestMethod.All)
    public String getRequestedConnectionList( HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_VPN_NETWORK_LINK, request, vpnNetworkLinkService,
                SessionConstants.VIEW_VPN_NETWORK_LINK,
                SessionConstants.SEARCH_NETWORK_VPN_LINK
        );
        recordNavigationManager.doJobCustom(loginDTO);
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.NETWORK_SEARCH);
        return VPNConstants.VPN_BASE_URL;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/network/details", requestMethod = RequestMethod.GET)
    public String detailsConnectionGet(HttpServletRequest request) {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.NETWORK_DETAILS);
        return VPNConstants.VPN_BASE_URL;
    }

    @RequestMapping(mapping = "/network/data", requestMethod = RequestMethod.All)
    public List<VPNNetworkLink> getLInkDataById(@RequestParameter("id") long id) throws Exception {
        return vpnNetworkLinkService.allInfo(id);
    }


    //region revise application td and reconnect

    @RequestMapping(mapping = "/td/search", requestMethod = RequestMethod.All)
    public ActionForward getSearchDNSDomainPage(ActionMapping mapping, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
        RecordNavigationManager recordNavigationManager = new RecordNavigationManager(
                SessionConstants.NAV_VPN_PROBABLE_TD_CLIENT, request, vpnProbableTDService,
                SessionConstants.VIEW_VPN_PROBABLE_TD_CLIENT,
                SessionConstants.SEARCH_VPN_PROBABLE_TD_CLIENT
        );
        recordNavigationManager.doJob(loginDTO);
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.TD_SEARCH);
        return mapping.findForward(VPNConstants.VPN_BASE_URL);
    }


    @JsonPost
    @RequestMapping(mapping = "/td/request", requestMethod = RequestMethod.POST)
    public void TDSubmitByClientID(
            @RequestParameter(isJsonBody = true, value = "data") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        vpnApplicationService.insertTdBatchOperation(jsonElement, loginDTO);

    }

    @JsonPost
    @RequestMapping(mapping = "/link/td-complete", requestMethod = RequestMethod.POST)
    public void tdComplete(
            @RequestParameter(isJsonBody = true, value = "application") VPNApplication application,
            LoginDTO loginDTO) throws Exception {
        vpnApplicationService.tdCompleteBatchOperation(application, loginDTO);

    }
    //endregion

    //region Reconnect
    @JsonPost
    @RequestMapping(mapping = "/reconnect/request", requestMethod = RequestMethod.POST)
    public void reconnectRequestByClientID(
            @RequestParameter(isJsonBody = true, value = "data") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        vpnApplicationService.reconnectInsertBatchOperation(jsonElement, loginDTO);

    }

    @ForwardedAction
    @RequestMapping(mapping = "/reconnect/application", requestMethod = RequestMethod.GET)
    public String reconnectApplicationPage(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.RECONNECT_APPLICATION);
        return VPNConstants.VPN_BASE_URL;
    }


    @JsonPost
    @RequestMapping(mapping = "/link/reconnect-complete", requestMethod = RequestMethod.POST)
    public void reconnectComplete(
            @RequestParameter(isJsonBody = true, value = "application") VPNApplication application,
            LoginDTO loginDTO) throws Exception {
        vpnApplicationService.reconnectCompleteBatchOperation(application, loginDTO);

    }
    //endregion


    //region bandwidth change
    //region Owner change
    @ForwardedAction
    @RequestMapping(mapping = "/ownerchange/application", requestMethod = RequestMethod.GET)
    public String ownerChangeApplicationPage(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.OWNER_CHANGE_APPLICATION);
        return VPNConstants.VPN_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/network/bandwidth-change", requestMethod = RequestMethod.GET)
    public String bandwidthChangeGet(HttpServletRequest request) {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.BANDWIDTH_CHANGE);
        return VPNConstants.VPN_BASE_URL;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/link/shift", requestMethod = RequestMethod.GET)
    public String shiftGet(HttpServletRequest request) {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.SHIFT);
        return VPNConstants.VPN_BASE_URL;
    }

    @RequestMapping(mapping = "/network/get-all-by-client", requestMethod = RequestMethod.GET)
    public List<VPNNetworkLink> getAllNetworkLinksByClientId(@RequestParameter("id") long clientId) throws Exception {
        List<VPNNetworkLink> networkLinks = vpnNetworkLinkService.getActiveNetworkLinksByClient(clientId);

        for (VPNNetworkLink networkLink : networkLinks) {
            networkLink.setOffices();
        }
        return networkLinks;
    }

    @JsonPost
    @RequestMapping(mapping = "/link/bandwidth-change", requestMethod = RequestMethod.POST)
    public void bandwidthChangeAppicationPost(
            @RequestParameter(value = "application", isJsonBody = true) VPNApplication application,
            @RequestParameter(value = "previousBandwidth", isJsonBody = true) double previousBandwidth,
            LoginDTO loginDTO

    ) throws Exception {
        double requestedBW = application.getVpnApplicationLinks().get(0).getLinkBandwidth();
        ApplicationType applicationType =  requestedBW < previousBandwidth ? ApplicationType.VPN_DOWNGRADE_CONNECTION :
                                                requestedBW > previousBandwidth ? ApplicationType.VPN_UPGRADE_CONNECTION :
                                                        ApplicationType.INVALID_TYPE;

        if(applicationType == ApplicationType.INVALID_TYPE){
            throw new RequestFailureException("Invalid Application Type: Requested BW is equal to Previous BW");
        }
        vpnApplicationService.insertOrUpdateBatchOperationForBandwidthRevise(application, applicationType, loginDTO);
    }

    //endregion


    /*@RequestMapping(mapping = "/link/get-active-link-by-client", requestMethod = RequestMethod.All)
    public List<VPNNetworkLink> getActiveLinksByClient(@RequestParameter("clientId") long clientId, LoginDTO loginDTO) throws Exception {
        return vpnApplicationService.getActiveLinkByClient(clientId);
    }*/

    @JsonPost
    @RequestMapping(mapping = "/ownerchange/insert", requestMethod = RequestMethod.POST)
    public String ownerChangeInsert(
            @RequestParameter(isJsonBody = true, value = "application") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        return vpnApplicationService.ownerChangeInsertBatchOperation(jsonElement, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/ownerchange-complete", requestMethod = RequestMethod.POST)
    public void ownerChangeComplete(
            @RequestParameter(isJsonBody = true, value = "application") VPNApplication application,
            LoginDTO loginDTO) throws Exception {
        vpnApplicationService.ownerChangeCompleteBatchOperation(application, loginDTO);
    }


    @RequestMapping(mapping = "/link/get-all-active-links-of-client", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getActiveLinksOfClient(@RequestParameter("clientId") long clientId, LoginDTO loginDTO) throws Exception {
        return vpnApplicationService.getActiveVPNLinkNameIDPairListByClient(clientId);
    }
    //endregion
    
    //region close
    @ForwardedAction
    @RequestMapping(mapping = "/close/application", requestMethod = RequestMethod.GET)
    public String closeApplicationPage(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        request.setAttribute(VPNConstants.VPN_SESSION_URL, VPNConstants.VPN_CLOSE_APPLICATION);
        return VPNConstants.VPN_BASE_URL;
    }

    @JsonPost
    @RequestMapping(mapping = "/close/insert", requestMethod = RequestMethod.POST)
    public void closeInsert(
            @RequestParameter(isJsonBody = true, value = "application") String jsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        vpnApplicationService.closeInsertBatchOperation(jsonElement, loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/link/close-complete", requestMethod = RequestMethod.POST)
    public void linkCloseComplete(
            @RequestParameter(isJsonBody = true, value = "application") VPNApplication application,
            LoginDTO loginDTO) throws Exception {
        vpnApplicationService.closeCompleteBatchOperation(application, loginDTO);
    }
    //endregion


    //begin client

    @RequestMapping(mapping="/client/get-client-details", requestMethod=RequestMethod.All)
    public Map<String, String> getClientDetailsByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
        clientID = vpnClientService.validateOwnClientID(clientID, loginDTO);
        return vpnClientService.getClientDetailsByClient(clientID);
    }


    @RequestMapping(mapping="/client/get-client", requestMethod=RequestMethod.All)
    public List<LLIDropdownClient> getLLIClient(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
        return vpnClientService.getClientListByPartialNameByResistantType(partialName, loginDTO);
    }

//    end client
}
