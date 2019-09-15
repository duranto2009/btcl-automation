package lli.Application;


import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplication;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplicationDeserializer;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplicationSerializer;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalIP.LLIAdditionalIPApplication;
import lli.Application.AdditionalIP.LLIAdditionalIPService;
import lli.Application.AdditionalPort.*;
import lli.Application.BandwidthConfiguration.BandwidthConfiguration;
import lli.Application.BandwidthConfiguration.BandwidthConfigurationService;
import lli.Application.BreakLongTerm.LLIBreakLongTermApplication;
import lli.Application.BreakLongTerm.LLIBreakLongTermApplicationDeserializer;
import lli.Application.BreakLongTerm.LLIBreakLongTermApplicationSerializer;
import lli.Application.ChangeBillingAddress.LLIChangeBillingAddressApplication;
import lli.Application.ChangeBillingAddress.LLIChangeBillingAddressApplicationDeserializer;
import lli.Application.ChangeBillingAddress.LLIChangeBillingAddressApplicationSerializer;
import lli.Application.CloseConnection.LLICloseConnectionApplication;
import lli.Application.CloseConnection.LLICloseConnectionApplicationDeserializer;
import lli.Application.CloseConnection.LLICloseConnectionApplicationSerializer;
import lli.Application.DowngradeBandwidth.LLIDowngradeBandwidthApplication;
import lli.Application.DowngradeBandwidth.LLIDowngradeBandwidthApplicationDeserializer;
import lli.Application.DowngradeBandwidth.LLIDowngradeBandwidthApplicationSerializer;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRDeserializer;
import lli.Application.EFR.EFRService;
import lli.Application.FlowConnectionManager.*;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRDeserializer;
import lli.Application.IFR.IFRService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopDeserializer;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewConnection.LLINewConnectionApplication;
import lli.Application.NewConnection.LLINewConnectionApplicationDeserializer;
import lli.Application.NewConnection.LLINewConnectionApplicationSerializer;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.NewLongTerm.LLINewLongTermApplication;
import lli.Application.NewLongTerm.LLINewLongTermApplicationDeserializer;
import lli.Application.NewLongTerm.LLINewLongTermApplicationSerializer;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.Reconnect.LLIReconnectApplication;
import lli.Application.Reconnect.LLIReconnectApplicationDeserializer;
import lli.Application.Reconnect.LLIReconnectApplicationSerializer;
import lli.Application.ReleaseConnectionAddress.LLIReleaseConnectionAddressApplication;
import lli.Application.ReleaseConnectionAddress.LLIReleaseConnectionAddressApplicationDeserializer;
import lli.Application.ReleaseConnectionAddress.LLIReleaseConnectionAddressApplicationSerializer;
import lli.Application.ReleaseIP.LLIReleaseIPApplication;
import lli.Application.ReleaseLocalLoop.LLIReleaseLocalLoopApplication;
import lli.Application.ReleaseLocalLoop.LLIReleaseLocalLoopApplicationDeserializer;
import lli.Application.ReleaseLocalLoop.LLIReleaseLocalLoopApplicationSerializer;
import lli.Application.ReleasePort.LLIReleasePortApplication;
import lli.Application.ReleasePort.LLIReleasePortApplicationDeserializer;
import lli.Application.ReleasePort.LLIReleasePortApplicationSerializer;
import lli.Application.ShiftAddress.LLIShiftAddressApplication;
import lli.Application.ShiftAddress.LLIShiftAddressApplicationDeserializer;
import lli.Application.ShiftAddress.LLIShiftAddressApplicationSerializer;
import lli.Application.ShiftBandwidth.LLIShiftBandwidthApplication;
import lli.Application.ShiftBandwidth.LLIShiftBandwidthApplicationDeserializer;
import lli.Application.ShiftBandwidth.LLIShiftBandwidthApplicationSerializer;
import lli.Application.ShiftPop.LLIShiftPopApplication;
import lli.Application.ShiftPop.LLIShiftPopApplicationDeserializer;
import lli.Application.ShiftPop.LLIShiftPopApplicationSerializer;
import lli.Application.TemporaryUpgradeBandwidth.LLITemporaryUpgradeBandwidthApplication;
import lli.Application.TemporaryUpgradeBandwidth.LLITemporaryUpgradeBandwidthApplicationDeserializer;
import lli.Application.TemporaryUpgradeBandwidth.LLITemporaryUpgradeBandwidthApplicationSerializer;
import lli.Application.newOffice.NewOffice;
import lli.Application.newOffice.NewOfficeService;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplication;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplicationDeserializer;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplicationSerializer;
import lli.Comments.Comments;
import lli.Comments.CommentsDeserializer;
import lli.Comments.CommentsService;
import lli.*;
import lli.client.td.LLIClientTDService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import officialLetter.OfficialLetterService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

//import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplication;
//import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplicationDeserializer;
//import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplicationSerializer;
/*import lli.Application.ChangeOwnership.LLIChangeOwnershipApplication;
import lli.Application.ChangeOwnership.LLIChangeOwnershipApplicationDeserializer;
import lli.Application.ChangeOwnership.LLIChangeOwnershipApplicationSerializer;*/
@Log4j
@ActionRequestMapping("lli/application")
public class LLIApplicationAction extends AnnotatedRequestMappingAction {

    @Service
    GlobalService globalService;

    @Service
    LLIAdditionalIPService lliAdditionalIPService; //added by jami
    @Service
    LLIAdditionalPortService lliAdditionalPortService; //jami

    @Service
    LLIApplicationService lliApplicationService;

    @Service
    LLIApplicationFlowOperations applicationFlowOperations;

    @Service
    IFRService ifrService;

    @Service
    EFRService efrService;

    @Service
    CommentsService commentsService;

    @Service
    NewOfficeService newOfficeService;

    @Service
    OfficeService officeService;

    @Service
    LocalLoopService localLoopService;

    @Service
    NewLocalLoopService newlocalLoopService;

    @Service
    LLIApplicationFlowConnectionManagerService lliApplicationFlowConnectionManagerService;

    @Service
    FlowDataBuilder flowDataBuilder;

    @Service
    FlowViewDataBuilder flowViewDataBuilder;

    @Service
    LLIConnectionService lliConnectionService;

    @Service
    LLIFlowConnectionService lliFlowConnectionService;
    @Service
    OfficialLetterService officialLetterService;
    @Service
    LLIClientTDService lliClientTDService;
    @Service
    BandwidthConfigurationService bandwidthConfigurationService;

    @Service
    BillService billService;


    @Override
    public GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                //LLI Application
                .registerTypeAdapter(LLIApplication.class, new LLIApplicationSerializer())
                .registerTypeAdapter(LLIApplication.class, new LLIApplicationDeserializer())
                //1
                .registerTypeAdapter(LLINewConnectionApplication.class, new LLINewConnectionApplicationSerializer())
                .registerTypeAdapter(LLINewConnectionApplication.class, new LLINewConnectionApplicationDeserializer())
                //2
                .registerTypeAdapter(LLIUpgradeBandwidthApplication.class, new LLIUpgradeBandwidthApplicationSerializer())
                .registerTypeAdapter(LLIUpgradeBandwidthApplication.class, new LLIUpgradeBandwidthApplicationDeserializer())
                //3
                .registerTypeAdapter(LLIDowngradeBandwidthApplication.class, new LLIDowngradeBandwidthApplicationSerializer())
                .registerTypeAdapter(LLIDowngradeBandwidthApplication.class, new LLIDowngradeBandwidthApplicationDeserializer())
                //4
                .registerTypeAdapter(LLITemporaryUpgradeBandwidthApplication.class, new LLITemporaryUpgradeBandwidthApplicationSerializer())
                .registerTypeAdapter(LLITemporaryUpgradeBandwidthApplication.class, new LLITemporaryUpgradeBandwidthApplicationDeserializer())
                //5
                .registerTypeAdapter(LLIAdditionalPortApplication.class, new LLIAdditionalPortApplicationSerializer())
                .registerTypeAdapter(LLIAdditionalPortApplication.class, new LLIAdditionalPortApplicationDeserializer())
                //6
                .registerTypeAdapter(LLIReleasePortApplication.class, new LLIReleasePortApplicationSerializer())
                .registerTypeAdapter(LLIReleasePortApplication.class, new LLIReleasePortApplicationDeserializer())
                //7
                //8
                .registerTypeAdapter(LLIReleaseLocalLoopApplication.class, new LLIReleaseLocalLoopApplicationSerializer())
                .registerTypeAdapter(LLIReleaseLocalLoopApplication.class, new LLIReleaseLocalLoopApplicationDeserializer())

                //11
                .registerTypeAdapter(LLIAdditionalConnectionAddressApplication.class, new LLIAdditionalConnectionAddressApplicationSerializer())
                .registerTypeAdapter(LLIAdditionalConnectionAddressApplication.class, new LLIAdditionalConnectionAddressApplicationDeserializer())
                //12
                .registerTypeAdapter(LLIShiftAddressApplication.class, new LLIShiftAddressApplicationSerializer())
                .registerTypeAdapter(LLIShiftAddressApplication.class, new LLIShiftAddressApplicationDeserializer())
                //13
                .registerTypeAdapter(LLIReleaseConnectionAddressApplication.class, new LLIReleaseConnectionAddressApplicationSerializer())
                .registerTypeAdapter(LLIReleaseConnectionAddressApplication.class, new LLIReleaseConnectionAddressApplicationDeserializer())
                //14
                .registerTypeAdapter(LLIShiftPopApplication.class, new LLIShiftPopApplicationSerializer())
                .registerTypeAdapter(LLIShiftPopApplication.class, new LLIShiftPopApplicationDeserializer())
                //15
                .registerTypeAdapter(LLINewLongTermApplication.class, new LLINewLongTermApplicationSerializer())
                .registerTypeAdapter(LLINewLongTermApplication.class, new LLINewLongTermApplicationDeserializer())
                //16
                .registerTypeAdapter(LLIBreakLongTermApplication.class, new LLIBreakLongTermApplicationSerializer())
                .registerTypeAdapter(LLIBreakLongTermApplication.class, new LLIBreakLongTermApplicationDeserializer())
                //17
                .registerTypeAdapter(LLIShiftBandwidthApplication.class, new LLIShiftBandwidthApplicationSerializer())
                .registerTypeAdapter(LLIShiftBandwidthApplication.class, new LLIShiftBandwidthApplicationDeserializer())
                //18
                //.registerTypeAdapter(LLIChangeOwnershipApplication.class, new LLIChangeOwnershipApplicationSerializer())
                //.registerTypeAdapter(LLIChangeOwnershipApplication.class, new LLIChangeOwnershipApplicationDeserializer())
                //19
                .registerTypeAdapter(LLIReconnectApplication.class, new LLIReconnectApplicationDeserializer())
                .registerTypeAdapter(LLIReconnectApplication.class, new LLIReconnectApplicationSerializer())
                //20
                .registerTypeAdapter(LLIChangeBillingAddressApplication.class, new LLIChangeBillingAddressApplicationSerializer())
                .registerTypeAdapter(LLIChangeBillingAddressApplication.class, new LLIChangeBillingAddressApplicationDeserializer())
                //21
                .registerTypeAdapter(LLICloseConnectionApplication.class, new LLICloseConnectionApplicationSerializer())
                .registerTypeAdapter(LLICloseConnectionApplication.class, new LLICloseConnectionApplicationDeserializer())

                //Connection Instance
                .registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionInstanceSerizalizer())
                .registerTypeAdapter(LLIOffice.class, new LLIOfficeSerizalizer())
                .registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopSerizalizer())
                .registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer())
                .registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer())
                .registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer())
        ;
        return gsonBuilder;
    }


    /*Application Independent of ApplicationType Begins*/
    //Application View Page
    @ForwardedAction
    @RequestMapping(mapping = "/view", requestMethod = RequestMethod.All)
    public String getApplicationView(@RequestParameter("id") long applicationID) throws Exception {
//		return lliApplicationService.viewApplicationMappingForwardName(applicationID);
        return lliApplicationService.viewNewApplicationMappingForwardName(applicationID);
    }

    @ForwardedAction
    @RequestMapping(mapping = LLIConnectionConstants.LLI_APPLICATION_DETAILS_PAGE_ACTION_URL, requestMethod = RequestMethod.All)
    public String getApplicationNewView(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(applicationID);
        FlowState flowState = new FlowService().getStateById(lliApplication.getState());
        List<FlowState> flowStates = new FlowService().getStatesByRole((int) loginDTO.getRoleID());
        boolean isApproved = false;
        for (FlowState flowState1 : flowStates) {
            if (flowState1.getId() == flowState.getId()) {
                isApproved = true;
            }
        }

        if (isApproved) {
            return lliApplicationService.viewNewApplicationMappingForwardName(applicationID);
        } else {
            return "404";
        }


    }

    //Application Search Page
    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_APPLICATION, request,
                lliApplicationService, SessionConstants.VIEW_LLI_APPLICATION, SessionConstants.SEARCH_LLI_APPLICATION);
        rnManager.doJobCustom(loginDTO);
        return "lli-application-search";
    }


    //Get Application Data by Demand Note ID
    @RequestMapping(mapping = "/get-app", requestMethod = RequestMethod.GET)
    public LLIApplication getLLIApplication(@RequestParameter("dnID") long id) throws Exception {
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByDemandNoteID(id);
        if (lliApplication == null) {
            throw new RequestFailureException("No Application Found with application ID " + id);
        }
        return lliApplication;

    }
    /*Application Independent of ApplicationType Ends*/


    /*Application Action Begins*/
    @RequestMapping(mapping = "/get-actions", requestMethod = RequestMethod.All)
    public List<LLIActionButton> getActions(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        return lliApplicationService.getAvailableActions(applicationID, loginDTO);
    }

    @RequestMapping(mapping = "/application-verify", requestMethod = RequestMethod.All)
    public void verifyApplication(@RequestParameter("id") long applicationID) throws Exception {
        lliApplicationService.verifyApplication(applicationID);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-request-for-correction", requestMethod = RequestMethod.All)
    public String requestForCorrectionApplication(@RequestParameter("id") long applicationID) throws Exception {
        //lliApplicationService.requestForCorrectionApplication(applicationID);
        return "lli-application-request-for-correction";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-edit", requestMethod = RequestMethod.All)
    public String editApplication(@RequestParameter("id") long applicationID) throws Exception {
        return lliApplicationService.editApplicationMappingForwardName(applicationID);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-reject", requestMethod = RequestMethod.All)
    public String rejectApplication(@RequestParameter("id") long applicationID) throws Exception {
        //lliApplicationService.rejectApplication(applicationID);
        return "lli-application-reject";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-process", requestMethod = RequestMethod.All)
    public String processApplication(@RequestParameter("id") long applicationID) throws Exception {
        return lliApplicationService.processApplicationMappingForwardName(applicationID);
    }

    @RequestMapping(mapping = "/application-finalize", requestMethod = RequestMethod.All)
    public void finalizeApplication(@RequestParameter("id") long applicationID) throws Exception {
        lliApplicationService.finalizeApplication(applicationID);
    }

    @ForwardedAction
    @RequestMapping(mapping = "/application-complete-request", requestMethod = RequestMethod.All)
    public String completeRequestApplication(@RequestParameter("id") long applicationID) throws Exception {
        return lliApplicationService.completeApplicationMappingForwardName(applicationID);
    }
    /*Application Action Ends*/






    /* ============================================================================================================== */
    /* ============================================================================================================== */
    /* ============================================================================================================== */
    /*Application New Connection Begins*/

    //region new connection
    @ForwardedAction
    @RequestMapping(mapping = "/new-connection", requestMethod = RequestMethod.GET)
    public String getNewConnectionApplicationForm() throws Exception {
        return "lli-application-new-connection";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/new-connection-details", requestMethod = RequestMethod.GET)
    public String getNewConnectionDetailsForm(@RequestParameter("id") long applicationID, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        return "lli-application-new-connection-details";
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection", requestMethod = RequestMethod.POST)
    public long postNewConnectionApplicationForm1(@RequestParameter(isJsonBody = true, value = "application") LLIApplication lliNewConnectionApplication, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        if (loginDTO.getIsAdmin()) lliNewConnectionApplication.setUserID(loginDTO.getUserID());
        lliNewConnectionApplication.setDocuments(request.getParameterValues("documents"));

        //check if td then cannot apply except reconnection
        try {
            if (lliClientTDService.isClientTemporarilyDisconnected(lliNewConnectionApplication.getClientID())) {
                throw new RequestFailureException("Temporary disconnected client cannot apply for new connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lliNewConnectionApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY && lliNewConnectionApplication.getDuration() > 30) {
            throw new RequestFailureException("Temporary Connection Can not be requested for more than 30 Days");


        }
        //end of check td


        if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL) {

            lliNewConnectionApplication.setState(LLIConnectionConstants.LOOP_PROVIDER_BTCL_STATE);

        } else if (lliNewConnectionApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_CLIENT) {

            lliNewConnectionApplication.setState(LLIConnectionConstants.LOOP_PROVIDER_CLIENT_STATE);
        }

        lliNewConnectionApplication.setApplicationType(LLIConnectionConstants.NEW_CONNECTION);

        lliNewConnectionApplication.setDemandNoteNeeded(true);

        lliApplicationService.insertApplication(lliNewConnectionApplication, loginDTO);
        lliApplicationService.sendNotification(lliNewConnectionApplication, lliNewConnectionApplication.getState(), loginDTO);

        long id = lliNewConnectionApplication.getApplicationID();
        //new connection application table insert for not having error


        //todo future bypass this
        lliApplicationService.newApplicationInsert(lliNewConnectionApplication);

        for (int i = 0; i < lliNewConnectionApplication.getOfficeList().size(); i++) {
            Office office = lliNewConnectionApplication.getOfficeList().get(i);
            office.setHistoryId(office.getId());
            office.setApplicationId(id);
            officeService.insertOffice(office);
            office.setHistoryId(office.getId());
            officeService.updateOffice(office);

        }
        lliApplicationService.setInsertStageComment(lliNewConnectionApplication, loginDTO);

        return id;
    }

    //Get Application Data by Application ID
    @RequestMapping(mapping = "/new-connection-get", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {

//        LLIApplication lliApplication= lliApplicationService.getNewLLIApplicationByApplicationID(applicationID);
//        LLIApplication lliApplication= lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
//        return lliApplication;

//		LLIApplication lliApplication= lliApplicationService.getNewLLIApplicationByApplicationID(applicationID);
        Gson gson = new Gson();
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
        JsonObject jsonObject = new JsonObject();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                || lliApplication.isNewLoop()
        ) {
            List<NewOffice> newOfficeList = newOfficeService.getOffice(lliApplication.getApplicationID());
            lliApplication.setNewOfficeList(newOfficeList);
        } else if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP) {
            LLIAdditionalIP ipCount = lliAdditionalIPService.getAdditionalIPByApplication(lliApplication.getApplicationID());
            lliApplication.setIp(ipCount.getIpCount());
        }

        jsonObject = flowDataBuilder.getCommonPart_new(lliApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @RequestMapping(mapping = "/new-connection-get-flow", requestMethod = RequestMethod.All)
    public JsonObject getNewConnectionData2(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {
            List<NewOffice> newOfficeList = newOfficeService.getOffice(lliApplication.getApplicationID());
            AdditionalPort additionalPort = lliAdditionalPortService.getAdditionalPortByApplication(lliApplication.getApplicationID());
            lliApplication.setPort(additionalPort.getPortCount());
            lliApplication.setNewOfficeList(newOfficeList);
        } else if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP) {
            LLIAdditionalIP ipCount = lliAdditionalIPService.getAdditionalIPByApplication(lliApplication.getApplicationID());
            lliApplication.setIp(ipCount.getIpCount());
        } else {
            List<Office> officeList = officeService.getOffice(lliApplication.getApplicationID());
            lliApplication.setOfficeList(officeList);
        }


        jsonObject = flowDataBuilder.getCommonPart_new(lliApplication, jsonObject, loginDTO);
        return jsonObject;
    }


    @RequestMapping(mapping = "/connection-view", requestMethod = RequestMethod.All)
    public JsonObject getConnectionDataDetails(@RequestParameter("id") long applicationID, LoginDTO loginDTO) throws Exception {

        Gson gson = new Gson();
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(applicationID);
        JsonObject jsonObject = new JsonObject();

        jsonObject = flowViewDataBuilder.getCommonPart_new(lliApplication, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection-submit", requestMethod = RequestMethod.POST)
    public long postNewConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLINewConnectionApplication lliNewConnectionApplication, LoginDTO loginDTO, HttpServletRequest request) throws Exception {

        lliApplicationService.insertNewConnectionApplication(lliNewConnectionApplication, loginDTO);
        long id = lliNewConnectionApplication.getApplicationID();
        return id;
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection-edit", requestMethod = RequestMethod.POST)
    public long postNewConnectionEdit(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO
    ) throws Exception {


        JsonElement jelement = new JsonParser().parse(jsonString);


        return applicationFlowOperations.applicationEdit(jelement,loginDTO);
    }

    @JsonPost
    @RequestMapping(mapping = "/new-connection-edit-client", requestMethod = RequestMethod.POST)
    public long postNewConnectionEditClient
            (@RequestParameter(isJsonBody = true, value = "application") LLINewConnectionApplication lliNewConnectionApplication,
             @RequestParameter(value = "state") String state) throws Exception {

        JsonElement jelement = new JsonParser().parse(state);
        JsonObject jsonObject = jelement.getAsJsonObject();

        lliApplicationService.editApplication(lliNewConnectionApplication);
        lliApplicationService.updateApplicatonState(lliNewConnectionApplication.getApplicationID(), jsonObject.get("state").getAsInt());
//        lliApplicationService.sendNotification(llliNewConnectionApplication,loginDTO);

        return lliNewConnectionApplication.getApplicationID();
    }



    /*Application New Connection Ends*/
    /* ============================================================================================================== */
    /*Application Upgrade Bandwidth Begins*/

    //endregion


    //region upgrade bandwidth
    @ForwardedAction
    @RequestMapping(mapping = "/upgrade-bandwidth", requestMethod = RequestMethod.GET)
    public String getUpgradeBandwidthApplicationForm() throws Exception {
        return "lli-application-upgrade-bandwidth";
    }

    @JsonPost
    @RequestMapping(mapping = "/upgrade-bandwidth", requestMethod = RequestMethod.POST)
    public long postUpgradeBandwidthApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIApplication lliUpgradeBandwidthApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()){
            lliUpgradeBandwidthApplication.setUserID(loginDTO.getUserID());
        }



        lliUpgradeBandwidthApplication.setState(LLIConnectionConstants.UPGRADE_BANDWIDTH_STATE);
        lliUpgradeBandwidthApplication.setApplicationType(LLIConnectionConstants.UPGRADE_BANDWIDTH);

        lliApplicationService.insertApplication(lliUpgradeBandwidthApplication, loginDTO);

        lliApplicationService.upgradeApplicationInsert(lliUpgradeBandwidthApplication);
        lliApplicationService.sendNotification(lliUpgradeBandwidthApplication, lliUpgradeBandwidthApplication.getState(), loginDTO);

        lliApplicationService.setInsertStageComment(lliUpgradeBandwidthApplication, loginDTO);
        return lliUpgradeBandwidthApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/upgrade-bandwidth-edit", requestMethod = RequestMethod.POST)
    public long postUpgradeBandwidthEdit(@RequestParameter(isJsonBody = true, value = "application") LLIUpgradeBandwidthApplication lliUpgradeBandwidthApplication) throws Exception {
        lliApplicationService.editApplication(lliUpgradeBandwidthApplication);
        return lliUpgradeBandwidthApplication.getApplicationID();
    }


    //endregion


    //region downgrade bandwidth
    @ForwardedAction
    @RequestMapping(mapping = "/downgrade-bandwidth", requestMethod = RequestMethod.GET)
    public String getDowngradeBandwidthApplicationForm() throws Exception {
        return "lli-application-downgrade-bandwidth";
    }

    @JsonPost
    @RequestMapping(mapping = "/downgrade-bandwidth", requestMethod = RequestMethod.POST)
    public long postDowngradeBandwidthApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        //start: policy check
        lliApplicationService.policyCheck(extendedLLIApplication);
        lliApplicationService.cachePolicyCheck(extendedLLIApplication);
        extendedLLIApplication = lliApplicationService.downgradeConnectionStateSet(extendedLLIApplication);
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        extendedLLIApplication.setApplicationType(LLIConnectionConstants.DOWNGRADE_BANDWIDTH);

        LLIConnectionInstance lliConnection = lliConnectionService.getLLIConnectionByConnectionID(extendedLLIApplication.getConnectionId());
        if (extendedLLIApplication.getSuggestedDate() < lliConnection.getStartDate()) {
            throw new RequestFailureException("Suggested date must be after connection start date");
        }

        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        lliApplicationService.sendNotification(extendedLLIApplication, extendedLLIApplication.getState(), loginDTO);
        lliApplicationService.setInsertStageComment(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }


    //endregion


    //region temp upgrade bandwidth
    @ForwardedAction
    @RequestMapping(mapping = "/temporary-upgrade-bandwidth", requestMethod = RequestMethod.GET)
    public String getTemporaryUpgradeBandwidthApplicationForm() throws Exception {
        return "lli-application-temporary-upgrade-bandwidth";
    }

    @JsonPost
    @RequestMapping(mapping = "/temporary-upgrade-bandwidth", requestMethod = RequestMethod.POST)
    public long postTemporaryUpgradeBandwidthApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLITemporaryUpgradeBandwidthApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/temporary-upgrade-bandwidth-edit", requestMethod = RequestMethod.POST)
    public long postTemporaryUpgradeBandwidthEdit(@RequestParameter(isJsonBody = true, value = "application") LLITemporaryUpgradeBandwidthApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/temporary-upgrade-bandwidth-process", requestMethod = RequestMethod.POST)
    public void postTemporaryUpgradeBandwidthProcess(@RequestParameter(isJsonBody = true, value = "application") LLITemporaryUpgradeBandwidthApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/temporary-upgrade-bandwidth-complete", requestMethod = RequestMethod.POST)
    public void postTemporaryUpgradeBandwidthCompletion(@RequestParameter(isJsonBody = true, value = "application") LLITemporaryUpgradeBandwidthApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region additional port
   /*// commented on 7/7/19 by jami
    @ForwardedAction
    @RequestMapping(mapping = "/additional-port", requestMethod = RequestMethod.GET)
    public String getAdditionalPortApplicationForm() throws Exception {
        return "lli-application-additional-port";
    }
*/



/*
    @JsonPost
    @RequestMapping(mapping = "/additional-port", requestMethod = RequestMethod.POST)
    public long postAdditionalPortApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalPortApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }
*/


   /*
   //commented by jami on 7/7/19
   @JsonPost
    @RequestMapping(mapping = "/additional-port", requestMethod = RequestMethod.POST)
    public long postAdditionalPortApplicationForm(
            @RequestParameter(isJsonBody = true, value = "application") LLIAdditionalPortApplication lliNewPortAddApplication,
            LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliNewPortAddApplication.setUserID(loginDTO.getUserID());
        lliNewPortAddApplication.setState(LLIConnectionConstants.ADDITIONAL_PORT_STATE);
        lliNewPortAddApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_PORT);
        lliApplicationService.insertApplication(lliNewPortAddApplication, loginDTO);
        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(lliNewPortAddApplication.getApplicationID());
        lliApplicationService.sendNotification(lliApplication, lliApplication.getState(), loginDTO);
        return lliNewPortAddApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-port-edit", requestMethod = RequestMethod.POST)
    public long postAdditionalPortEdit(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalPortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-port-process", requestMethod = RequestMethod.POST)
    public void postAdditionalPortProcess(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalPortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-port-complete", requestMethod = RequestMethod.POST)
    public void postAdditionalPortCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalPortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }*/
    //endregion


    //region release port
    @ForwardedAction
    @RequestMapping(mapping = "/release-port", requestMethod = RequestMethod.GET)
    public String getReleasePortApplicationForm() throws Exception {
        return "lli-application-release-port";
    }

    @JsonPost
    @RequestMapping(mapping = "/release-port", requestMethod = RequestMethod.POST)
    public long postReleasePortApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIReleasePortApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-port-edit", requestMethod = RequestMethod.POST)
    public long postReleasePortEdit(@RequestParameter(isJsonBody = true, value = "application") LLIReleasePortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-port-process", requestMethod = RequestMethod.POST)
    public void postReleasePortProcess(@RequestParameter(isJsonBody = true, value = "application") LLIReleasePortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/release-port-complete", requestMethod = RequestMethod.POST)
    public void postReleasePortCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIReleasePortApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region New loop
    @ForwardedAction
    @RequestMapping(mapping = "/additional-local-loop", requestMethod = RequestMethod.GET)
    public String getAdditionalLocalLoopApplicationForm() throws Exception {
        return "lli-application-additional-local-loop";
    }

    //jami additional Local loop === new local loop
    @JsonPost
    @RequestMapping(mapping = "/additional-local-loop", requestMethod = RequestMethod.POST)
    public void postAdditionalLocalLoopApplicationForm(
            @RequestParameter(isJsonBody = true, value = "application") String additionalLocalLoopApplication,
            LoginDTO loginDTO) throws Exception {
        JsonElement jelement = new JsonParser().parse(additionalLocalLoopApplication);
        JsonObject jsonObject = jelement.getAsJsonObject();
        lliApplicationService.insertAdditionalLocalLoopApplicationBatchOperation(jsonObject, loginDTO);
    }

    //endregion
    //region release loop
    @ForwardedAction
    @RequestMapping(mapping = "/release-local-loop", requestMethod = RequestMethod.GET)
    public String getReleaseLocalLoopApplicationForm() throws Exception {
        return "lli-application-release-local-loop";
    }

    @JsonPost
    @RequestMapping(mapping = "/release-local-loop", requestMethod = RequestMethod.POST)
    public long postReleaseLocalLoopApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseLocalLoopApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-local-loop-edit", requestMethod = RequestMethod.POST)
    public long postReleaseLocalLoopEdit(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseLocalLoopApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-local-loop-process", requestMethod = RequestMethod.POST)
    public void postReleaseLocalLoopProcess(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseLocalLoopApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/release-local-loop-complete", requestMethod = RequestMethod.POST)
    public void postReleaseLocalLoopCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseLocalLoopApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region additional ip
    @ForwardedAction
    @RequestMapping(mapping = "/additional-ip", requestMethod = RequestMethod.GET)
    public String getAdditionalIPApplicationForm() throws Exception {
        return "lli-application-additional-ip";
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-ip", requestMethod = RequestMethod.POST)
    public long postAdditionalIPApplicationForm(
            @RequestParameter(isJsonBody = true, value = "application") String additionalIPApplication,
            LoginDTO loginDTO) throws Exception {


        // TODO: 12/31/2018 the transactional problem is exist here the all common insert queries should be under one transactional
        JsonElement jelement = new JsonParser().parse(additionalIPApplication);
        JsonObject jsonObject = jelement.getAsJsonObject();
        LLIApplication lliApplication = new LLIApplicationDeserializer().deserialize_ip(jelement);
        if (loginDTO.getIsAdmin()) lliApplication.setUserID(loginDTO.getUserID());
        lliApplication.setState(LLIConnectionConstants.LOOP_PROVIDER_CLIENT_STATE);
        lliApplication.setApplicationType(LLIConnectionConstants.ADDITIONAL_IP);
        lliApplication.setDemandNoteNeeded(true);
        lliApplication.setSubmissionDate(System.currentTimeMillis());
        //lliApplication.setUserID(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : loginDTO.getAccountID());
        lliApplicationService.insertApplication(lliApplication, loginDTO);
        LLIAdditionalIP additionalIP = new LLIAdditionalIP().deserialize(lliApplication);
        lliAdditionalIPService.insertAdditionalIPApplication(additionalIP);
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
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.sendNotification(lliApplication, lliApplication.getState(), loginDTO);

        return lliApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-ip-edit", requestMethod = RequestMethod.POST)
    public long postAdditionalIPEdit(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-ip-process", requestMethod = RequestMethod.POST)
    public void postAdditionalIPProcess(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-ip-complete", requestMethod = RequestMethod.POST)
    public void postAdditionalIPCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region release ip
    @ForwardedAction
    @RequestMapping(mapping = "/release-ip", requestMethod = RequestMethod.GET)
    public String getReleaseIPApplicationForm() throws Exception {
        return "lli-application-release-ip";
    }

    @JsonPost
    @RequestMapping(mapping = "/release-ip", requestMethod = RequestMethod.POST)
    public long postReleaseIPApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseIPApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-ip-edit", requestMethod = RequestMethod.POST)
    public long postReleaseIPEdit(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-ip-process", requestMethod = RequestMethod.POST)
    public void postReleaseIPProcess(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/release-ip-complete", requestMethod = RequestMethod.POST)
    public void postReleaseIPCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseIPApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region additional connection address
    @ForwardedAction
    @RequestMapping(mapping = "/additional-connection-address", requestMethod = RequestMethod.GET)
    public String getAdditionalConnectionAddressApplicationForm() throws Exception {
        return "lli-application-additional-connection-address";
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-connection-address", requestMethod = RequestMethod.POST)
    public long postAdditionalConnectionAddressApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalConnectionAddressApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-connection-address-edit", requestMethod = RequestMethod.POST)
    public long postAdditionalConnectionAddressEdit(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-connection-address-process", requestMethod = RequestMethod.POST)
    public void postAdditionalConnectionAddressProcess(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/additional-connection-address-complete", requestMethod = RequestMethod.POST)
    public void postAdditionalConnectionAddressCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIAdditionalConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }
    //endregion

    //region shift address
    @ForwardedAction
    @RequestMapping(mapping = "/shift-address", requestMethod = RequestMethod.GET)
    public String getShiftAddressApplicationForm() throws Exception {
        return "lli-application-shift-address";
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-address", requestMethod = RequestMethod.POST)
    public long postShiftAddressApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIShiftAddressApplication lliShiftAddressApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliShiftAddressApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliShiftAddressApplication, loginDTO);
        return lliShiftAddressApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-address-edit", requestMethod = RequestMethod.POST)
    public long postShiftAddressEdit(@RequestParameter(isJsonBody = true, value = "application") LLIShiftAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-address-process", requestMethod = RequestMethod.POST)
    public void postShiftAddressProcess(@RequestParameter(isJsonBody = true, value = "application") LLIShiftAddressApplication lliShiftAddressApplication) throws Exception {
        lliApplicationService.processApplication(lliShiftAddressApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-address-complete", requestMethod = RequestMethod.POST)
    public void postShiftAddressCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIShiftAddressApplication lliShiftAddressApplication) throws Exception {
        lliApplicationService.completeApplication(lliShiftAddressApplication);
    }

    //endregion


    //region release connection address
    @ForwardedAction
    @RequestMapping(mapping = "/release-connection-address", requestMethod = RequestMethod.GET)
    public String getReleaseConnectionAddressApplicationForm() throws Exception {
        return "lli-application-release-connection-address";
    }

    @JsonPost
    @RequestMapping(mapping = "/release-connection-address", requestMethod = RequestMethod.POST)
    public long postReleaseConnectionAddressApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseConnectionAddressApplication extendedLLIApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) extendedLLIApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(extendedLLIApplication, loginDTO);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-connection-address-edit", requestMethod = RequestMethod.POST)
    public long postReleaseConnectionAddressEdit(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/release-connection-address-process", requestMethod = RequestMethod.POST)
    public void postReleaseConnectionAddressProcess(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.processApplication(extendedLLIApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/release-connection-address-complete", requestMethod = RequestMethod.POST)
    public void postReleaseConnectionAddressCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIReleaseConnectionAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.completeApplication(extendedLLIApplication);
    }

    //endregion


    //region shift pop
    @ForwardedAction
    @RequestMapping(mapping = "/shift-pop", requestMethod = RequestMethod.GET)
    public String getShiftPopApplicationForm() throws Exception {
        return "lli-application-shift-pop";
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-pop", requestMethod = RequestMethod.POST)
    public long postShiftPopApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIShiftPopApplication lliShiftPopApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliShiftPopApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliShiftPopApplication, loginDTO);
        return lliShiftPopApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-pop-edit", requestMethod = RequestMethod.POST)
    public long postShiftPopEdit(@RequestParameter(isJsonBody = true, value = "application") LLIShiftPopApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/shift-pop-process", requestMethod = RequestMethod.POST)
    public void postShiftPopProcess(@RequestParameter(isJsonBody = true, value = "application") LLIShiftPopApplication lliShiftPopApplication) throws Exception {
        lliApplicationService.processApplication(lliShiftPopApplication);
    }

    //Application Completion Post
    @JsonPost
    @RequestMapping(mapping = "/shift-pop-complete", requestMethod = RequestMethod.POST)
    public void postShiftPopCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIShiftPopApplication lliShiftPopApplication) throws Exception {
        lliApplicationService.completeApplication(lliShiftPopApplication);
    }

    //endregion


    //region long term
    @ForwardedAction
    @RequestMapping(mapping = "/new-long-term", requestMethod = RequestMethod.GET)
    public String getNewLongTermApplicationForm() throws Exception {
        return "lli-application-new-long-term";
    }

    @JsonPost
    @RequestMapping(mapping = "/new-long-term", requestMethod = RequestMethod.POST)
    public long postNewLongTermApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLINewLongTermApplication lliNewLongTermApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliNewLongTermApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliNewLongTermApplication, loginDTO);
        return lliNewLongTermApplication.getApplicationID();
    }


    //endregion


    //region break long term
    @ForwardedAction
    @RequestMapping(mapping = "/break-long-term", requestMethod = RequestMethod.GET)
    public String getBreakLongTermApplicationForm() throws Exception {
        return "lli-application-break-long-term";
    }

    @JsonPost
    @RequestMapping(mapping = "/break-long-term", requestMethod = RequestMethod.POST)
    public long postBreakLongTermApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIBreakLongTermApplication lliBreakLongTermApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliBreakLongTermApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliBreakLongTermApplication, loginDTO);
        return lliBreakLongTermApplication.getApplicationID();
    }


    //endregion


    //region shift bandwidth
    @ForwardedAction
    @RequestMapping(mapping = "/shift-bandwidth", requestMethod = RequestMethod.GET)
    public String getShiftBandwidthApplicationForm() throws Exception {
        return "lli-application-shift-bandwidth";
    }


    @JsonPost
    @RequestMapping(mapping = "/shift-bandwidth", requestMethod = RequestMethod.POST)
    public String postShiftBandwidthApplicationForm(@RequestParameter(isJsonBody = true, value = "application") String jsonString, LoginDTO loginDTO) throws Exception {
//        if (loginDTO.getIsAdmin()) lliShiftBandwidthApplication.setUserID(loginDTO.getUserID());
//        lliApplicationService.insertApplication(lliShiftBandwidthApplication, loginDTO);
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        LLIApplication shiftBandwidthApplication = new LLIApplication();
        //Bandwidth transfer type 1 && BW transfer Create Connection 2
        //todo:change hardcode
        if (jsonObject.get("type").getAsJsonObject().get("ID").getAsLong() == 1L) {
            shiftBandwidthApplication = new LLIApplicationDeserializer().deserializeCustomShiftBW(jsonElement);
            if(shiftBandwidthApplication.getSuggestedDate()<System.currentTimeMillis()){
                throw new RequestFailureException("Suggested Date can't be less than Current Date ");

            }
            List<LLIConnection> source = globalService.getAllObjectListByCondition(LLIConnection.class,
                    new LLIConnectionConditionBuilder()
                            .Where()
                            .clientIDEquals(shiftBandwidthApplication.getClientID())
                            .IDEquals((long) shiftBandwidthApplication.getSourceConnectionID())
                            .activeToGreaterThan(System.currentTimeMillis())
                            .getCondition()
            );
            if(source==null || source.size()==0){
                throw new RequestFailureException("No source Connection found ");
            }


            if(source.size()>0){
                if(source.get(0).getBandwidth()<shiftBandwidthApplication.getBandwidth()){
                    throw new RequestFailureException("Bandwidth can't be greater than source connection Bandwidth ");
                }
            }

            List<LLIConnection> destConnection = globalService.getAllObjectListByCondition(LLIConnection.class,
                    new LLIConnectionConditionBuilder()
                            .Where()
                            .clientIDEquals(shiftBandwidthApplication.getClientID())
                            .IDEquals((long) shiftBandwidthApplication.getConnectionId())
                            .activeToGreaterThan(System.currentTimeMillis())
                            .getCondition()
            );
            if(destConnection==null || destConnection.size()==0){
                throw new RequestFailureException("No Connection found ");
            }


            shiftBandwidthApplication.setApplicationType(LLIConnectionConstants.SHIFT_BANDWIDTH);
            if ((shiftBandwidthApplication.getZoneId() == 0 && shiftBandwidthApplication.getSecondZoneID() == 0)
                    || (shiftBandwidthApplication.getZoneId() == shiftBandwidthApplication.getSecondZoneID())
            ) {
                //source ldgm approve+cdgm not approve-need new state

                shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_SUBMITTED_NO_APPROVAL);


            } else if (shiftBandwidthApplication.getZoneId() > 0 && shiftBandwidthApplication.getSecondZoneID() == 0) {
                //only source approval not handeled here
                shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_SUBMITTED);


            } else if (shiftBandwidthApplication.getZoneId() == 0 && shiftBandwidthApplication.getSecondZoneID() > 0) {
                //cdgm approve
                shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_SUBMITTED_ONLY_CDGM_APPROVAL);

            } else {

                shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_SUBMITTED);
            }
        }
        else if(jsonObject.get("type").getAsJsonObject().get("ID").getAsLong() == 2L) {
            shiftBandwidthApplication = (new LLIApplicationDeserializer()).deserialize_custom(jsonElement);

            if(shiftBandwidthApplication.getSuggestedDate()<System.currentTimeMillis()){
                throw new RequestFailureException("Suggested Date can't be less than Current Date ");

            }

            List<LLIConnection> source = globalService.getAllObjectListByCondition(LLIConnection.class,
                    new LLIConnectionConditionBuilder()
                            .Where()
                            .clientIDEquals(shiftBandwidthApplication.getClientID())
                            .IDEquals((long) shiftBandwidthApplication.getSourceConnectionID())
                            .activeToGreaterThan(System.currentTimeMillis())
                            .getCondition()
            );
            if(source==null || source.size()==0){
                throw new RequestFailureException("No source Connection found ");
            }

            if(source.size()>0){
                if(source.get(0).getBandwidth()<shiftBandwidthApplication.getBandwidth()){
                    throw new RequestFailureException("Bandwidth can't be greater than source connection Bandwidth ");
                }
            }



            shiftBandwidthApplication.setApplicationType(LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION);
            if (shiftBandwidthApplication.getLoopProvider() == 1) {
                if (shiftBandwidthApplication.getZoneId() == shiftBandwidthApplication.getSecondZoneID()) {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_NEW_CONNECTION_SUBMITTED_NO_APPROVAL);

                } else if (shiftBandwidthApplication.getZoneId() > 0 && shiftBandwidthApplication.getSecondZoneID() == 0) {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_NEW_CONNECTION_SUBMITTED);


                } else if (shiftBandwidthApplication.getZoneId() == 0 && shiftBandwidthApplication.getSecondZoneID() > 0) {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_NEW_CONNECTION_SUBMITTED_ONLY_CDGM_APPROVAL);


                } else {

                    shiftBandwidthApplication.setState(LLIConnectionConstants.SHIFT_BW_NEW_CONNECTION_SUBMITTED);
                }
            } else {
                if (shiftBandwidthApplication.getZoneId() == shiftBandwidthApplication.getSecondZoneID()) {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_NO_APPROVAL);

                } else if (shiftBandwidthApplication.getZoneId() > 0 && shiftBandwidthApplication.getSecondZoneID() == 0) {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.WITHOUT_LOOP_SHIFT_BW_NEW_CONNECTION_SUBMITTED);


                } else if (shiftBandwidthApplication.getZoneId() == 0 && shiftBandwidthApplication.getSecondZoneID() > 0) {

                    shiftBandwidthApplication.setState(LLIConnectionConstants.WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_CDGM_APPROVAL);


                } else {
                    shiftBandwidthApplication.setState(LLIConnectionConstants.WITHOUT_LOOP_SHIFT_BW_NEW_CONNECTION_SUBMITTED);
                }
            }


        }else{
            throw new RequestFailureException(" Invalid Shifting Bandwidth Type. You Must select a valid shifting type");
        }


        lliApplicationService.insertApplication(shiftBandwidthApplication, loginDTO);


        if (shiftBandwidthApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION) {
            if (shiftBandwidthApplication.getOfficeList().size() > 0) {
                for (Office office : shiftBandwidthApplication.getOfficeList()) {
                    office.setHistoryId(office.getId());
                    office.setId(office.getId());
                    office.setApplicationId(shiftBandwidthApplication.getApplicationID());
                    officeService.insertOffice(office);
                }
            }
        }


        Comments comments = new Comments();
        comments.setSubmissionDate(shiftBandwidthApplication.getSubmissionDate());
        if (shiftBandwidthApplication.getUserID() == null) {
            comments.setUserID(-1);
        } else {
            comments.setUserID(shiftBandwidthApplication.getUserID());
        }
        comments.setStateID(shiftBandwidthApplication.getState());
        comments.setComments(shiftBandwidthApplication.getComment());
        comments.setApplicationID(shiftBandwidthApplication.getApplicationID());
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.sendNotification(shiftBandwidthApplication, shiftBandwidthApplication.getState(), loginDTO);
        return "";
    }


    //endregion


    //region change owner
    /* @ForwardedAction
    @RequestMapping(mapping = "/change-lli.Application.ownership", requestMethod = RequestMethod.GET)
    public String getChangeOwnershipApplicationForm() throws Exception {
        return "lli-application-change-lli.Application.ownership";
    }

    @JsonPost
    @RequestMapping(mapping = "/change-lli.Application.ownership", requestMethod = RequestMethod.POST)
    public long postChangeOwnershipApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIChangeOwnershipApplication lliChangeOwnershipApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliChangeOwnershipApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliChangeOwnershipApplication, loginDTO);
        return lliChangeOwnershipApplication.getApplicationID();
    }

   @JsonPost
    @RequestMapping(mapping = "/change-lli.Application.ownership-edit", requestMethod = RequestMethod.POST)
    public long postChangeOwnershipEdit(@RequestParameter(isJsonBody = true, value = "application") LLIChangeOwnershipApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/change-lli.Application.ownership-process", requestMethod = RequestMethod.POST)
    public void postChangeOwnershipProcess(@RequestParameter(isJsonBody = true, value = "application") LLIChangeOwnershipApplication lliChangeOwnershipApplication) throws Exception {
        lliApplicationService.processApplication(lliChangeOwnershipApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/change-lli.Application.ownership-complete", requestMethod = RequestMethod.POST)
    public void postChangeOwnershipCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIChangeOwnershipApplication lliChangeOwnershipApplication) throws Exception {
        lliApplicationService.completeApplication(lliChangeOwnershipApplication);
    }
*/
    //endregion


    //region reconnect
    @ForwardedAction
    @RequestMapping(mapping = "/reconnect", requestMethod = RequestMethod.GET)
    public String getReconnectApplicationForm() throws Exception {
        return "lli-application-reconnect";
    }

    @JsonPost
    @RequestMapping(mapping = "/reconnect", requestMethod = RequestMethod.POST)
    public long postReconnectApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIReconnectApplication lliReconnectApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliReconnectApplication.setUserID(loginDTO.getUserID());
        lliApplicationService.insertApplication(lliReconnectApplication, loginDTO);
        return lliReconnectApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/reconnect-edit", requestMethod = RequestMethod.POST)
    public long postReconnectEdit(@RequestParameter(isJsonBody = true, value = "application") LLIReconnectApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/reconnect-process", requestMethod = RequestMethod.POST)
    public void postReconnectProcess(@RequestParameter(isJsonBody = true, value = "application") LLIReconnectApplication lliReconnectApplication) throws Exception {
        lliApplicationService.processApplication(lliReconnectApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/reconnect-complete", requestMethod = RequestMethod.POST)
    public void postReconnectCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIReconnectApplication lliReconnectApplication) throws Exception {
        lliApplicationService.completeApplication(lliReconnectApplication);
    }

    //endregion


    //region change billing address


    //region change billing address
    @JsonPost
    @RequestMapping(mapping = "/change-billing-address-edit", requestMethod = RequestMethod.POST)
    public long postChangeBillingAddressEdit(@RequestParameter(isJsonBody = true, value = "application") LLIChangeBillingAddressApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }


    @JsonPost
    @RequestMapping(mapping = "/change-billing-address-process", requestMethod = RequestMethod.POST)
    public void postChangeBillingAddressProcess(@RequestParameter(isJsonBody = true, value = "application") LLIChangeBillingAddressApplication lliChangeBillingAddressApplication) throws Exception {
        lliApplicationService.processApplication(lliChangeBillingAddressApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/change-billing-address-complete", requestMethod = RequestMethod.POST)
    public void postChangeBillingAddressCompletion(@RequestParameter(isJsonBody = true, value = "application") LLIChangeBillingAddressApplication lliChangeBillingAddressApplication) throws Exception {
        lliApplicationService.completeApplication(lliChangeBillingAddressApplication);
    }

    //endregion


    //region close connection
    @ForwardedAction
    @RequestMapping(mapping = "/close-connection", requestMethod = RequestMethod.GET)
    public String getCloseConnectionApplicationForm() throws Exception {
        return "lli-application-close-connection";
    }

    //    @Service
//    LLIFlowConnectionService lliFlowConnectionService;
    @JsonPost
    @RequestMapping(mapping = "/close-connection", requestMethod = RequestMethod.POST)
    public long postCloseConnectionApplicationForm(@RequestParameter(isJsonBody = true, value = "application") LLIApplication lliCloseConnectionApplication, LoginDTO loginDTO) throws Exception {
        if (loginDTO.getIsAdmin()) lliCloseConnectionApplication.setUserID(loginDTO.getUserID());

        lliApplicationService.policyCheck(lliCloseConnectionApplication);
        lliCloseConnectionApplication.setApplicationType(LLIConnectionConstants.CLOSE_CONNECTION);
        lliApplicationService.insertApplication(lliCloseConnectionApplication, loginDTO);
        lliCloseConnectionApplication = lliApplicationService.closeConnectionStateSet(lliCloseConnectionApplication, loginDTO);
        lliApplicationService.updateApplicaton(lliCloseConnectionApplication);
        return lliCloseConnectionApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/close-connection-edit", requestMethod = RequestMethod.POST)
    public long postCloseConnectionEdit(@RequestParameter(isJsonBody = true, value = "application") LLICloseConnectionApplication extendedLLIApplication) throws Exception {
        lliApplicationService.editApplication(extendedLLIApplication);
        return extendedLLIApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/close-connection-process", requestMethod = RequestMethod.POST)
    public void postCloseConnectionProcess(@RequestParameter(isJsonBody = true, value = "application") LLICloseConnectionApplication lliCloseConnectionApplication) throws Exception {
        lliApplicationService.processApplication(lliCloseConnectionApplication);
    }

    @JsonPost
    @RequestMapping(mapping = "/close-connection-complete", requestMethod = RequestMethod.POST)
    public void postCloseConnectionCompletion(@RequestParameter(isJsonBody = true, value = "application") LLICloseConnectionApplication lliCloseConnectionApplication) throws Exception {
        lliApplicationService.completeApplication(lliCloseConnectionApplication);
    }

    //endregion
    /*Application Close Connection Ends*/
    /* ============================================================================================================== */
    /*Application Global Actions Begins*/
    @JsonPost
    @RequestMapping(mapping = "/request-for-correction", requestMethod = RequestMethod.POST)
    public long requestForCorrectionApplication(@RequestParameter(isJsonBody = true, value = "application") LLIApplication lliApplication) throws Exception {
        lliApplicationService.requestForCorrectionApplication(lliApplication);
        return lliApplication.getApplicationID();
    }

    @JsonPost
    @RequestMapping(mapping = "/reject", requestMethod = RequestMethod.POST)
    public long rejectApplication(@RequestParameter(isJsonBody = true, value = "application") LLIApplication lliApplication) throws Exception {
        lliApplicationService.rejectApplication(lliApplication);
        return lliApplication.getApplicationID();
    }


    @ForwardedAction
    @RequestMapping(mapping = "/preview-content", requestMethod = RequestMethod.All)
    public String getApplicationContentPreview() throws Exception {
        return "lli-application-preview-content";
    }


    //region ifr related part
    @JsonPost
    @RequestMapping(mapping = "/ifrinsertNewLocalLoop", requestMethod = RequestMethod.POST)
    public String ifrDataForNewLocalLoop(@RequestParameter(isJsonBody = true, value = "ifr") String JsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(JsonString);

       applicationFlowOperations.IFRRequestNewLoop(jelement,loginDTO);

        return "";//need to go search page
    }
    //end IFR Local LOOP

    //ifrinsertAdditionalIP
    //ifrinsertAdditionalIP

    @JsonPost
    @RequestMapping(mapping = "/ifrinsertAdditionalIP", requestMethod = RequestMethod.POST)
    public String ifrDataForAdditionalIP(@RequestParameter(isJsonBody = true, value = "ifr") String JsonString,
                                         LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(JsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);

        IFR ifr = new IFRDeserializer().deserialize_additoinal_ip(jelement);
        ifr.setIsForwarded(lliApplication.getIsForwarded());
        ifrService.insertApplication(ifr, loginDTO);

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);//this need to update state
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);


        return "";//need to go search page
    }

    //end Ifr insertAdditionalIP

    @JsonPost
    @RequestMapping(mapping = "/ifrinsert", requestMethod = RequestMethod.POST)
    public String ifrData(@RequestParameter(isJsonBody = true, value = "ifr") String JsonString, LoginDTO loginDTO) throws Exception {

        applicationFlowOperations.ifrInsertBatchOperations(JsonString, loginDTO);
        return "";//need to go search page
    }

    //IFr Update operation for local loop Jami
    //ifrupdateForNewLocalLoop
    @JsonPost
    @RequestMapping(mapping = "/ifrupdateForNewLocalLoop", requestMethod = RequestMethod.POST)
    public String ifrUpdateForNewLocalLoop(@RequestParameter(isJsonBody = true, value = "ifr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        applicationFlowOperations.IFRUpdateNewLoopLogicOperation(jelement,loginDTO);
        return "ok";
    }
    //end

    // ifr update for additionalIP
    @JsonPost
    @RequestMapping(mapping = "/ifrupdateForAdditionalIP", requestMethod = RequestMethod.POST)
    public String ifrupdateForAdditionalIP(@RequestParameter(isJsonBody = true, value = "ifr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        //this need to update state


        IFR ifr = new IFRDeserializer().deserialize_additoinal_ip(jelement);

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        ArrayList<IFR> list = new ArrayList<>();
        list.add(ifr);
        ifrService.insertOrUpdateIFR(list, appID, state);
        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        return "ok"; //need to go search page
    }

    //end ifrUpdateForAdditonalIP

    @JsonPost
    @RequestMapping(mapping = "/ifrupdate", requestMethod = RequestMethod.POST)
    public String ifrUpdate(@RequestParameter(isJsonBody = true, value = "ifr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        applicationFlowOperations.IFRUpdateLogicOperation(jelement, loginDTO);
        return "ok"; //need to go search page
    }

    //endregion

    //region efr related part

    @JsonPost
    @RequestMapping(mapping = "/efrinsert", requestMethod = RequestMethod.POST)
    public String efrData(@RequestParameter(isJsonBody = true, value = "efr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();
        JsonElement jsonElement = jsonObject.getAsJsonArray("ifr");

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        ArrayList<IFR> list1 = new IFRDeserializer().deserialize_custom_ifr_update(jelement);
        ArrayList<IFR> selectedIFR = new ArrayList<>();
        for (IFR ifr : list1) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            } else {
                ifr.setIsSelected(LLIConnectionConstants.IFR_IGNORED);
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.updateApplicaton(ifr);
        }


        List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.insertApplication(localLoop);
        }

        ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom(jelement);
        for (EFR efr : lists) {
            efr.setIsForwarded(lliApplication.getIsForwarded());
            efrService.insertApplication(efr, loginDTO);
        }

        lliApplicationService.updateApplicatonState(appID, state);//this need to update state
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);

        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        return "";//need to go search page
    }

    //efr request for new local loop
    @JsonPost
    @RequestMapping(mapping = "/efrinsertForLocalLoop", requestMethod = RequestMethod.POST)
    public String efrinsertForLocalLoop(@RequestParameter(isJsonBody = true, value = "efr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);
        lliApplicationService.handleEFRInsertLocalLoop(jelement, loginDTO);
        return "";//need to go search page
    }
    // end efr

    private void efrCheck(long applicationID, int state, LoginDTO loginDTO) throws Exception {
        List<EFR> efrArrayList = efrService.getIncomleteEFRByAppID(applicationID);
        if (efrArrayList.size() == 0) {

            lliApplicationService.updateApplicatonState(applicationID, state);
//            lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(applicationID),state,loginDTO);

        } else {
            for (EFR efr : efrArrayList) {
                if (System.currentTimeMillis() > efr.getQuotationDeadline()) {
                    efr.setQuotationStatus(LLIConnectionConstants.EFR_QUOTATION_EXPIRED);
                    efrService.updateApplicaton(efr);
                }

            }
        }


        List<EFR> efrArrayList2 = efrService.getIncomleteEFRByAppID(applicationID);
        if (efrArrayList2.size() == 0) {

            lliApplicationService.updateApplicatonState(applicationID, state);
//            lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(applicationID),state,loginDTO);

        }

    }

    @JsonPost
    @RequestMapping(mapping = "/efrupdate", requestMethod = RequestMethod.POST)
    public String efrUpdate(@RequestParameter(isJsonBody = true, value = "efr") String jsonString, LoginDTO loginDTO) throws Exception {

        JsonElement jelement = new JsonParser().parse(jsonString);

        JsonObject jsonObject = jelement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


//		lliApplicationService.updateApplicatonState(appID,state);

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        ArrayList<EFR> lists = new ArrayList<>();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {
            lists = new EFRDeserializer().deserialize_custom_new_local_loop(jelement);
        } else lists = new EFRDeserializer().deserialize_custom(jelement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jelement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        for (EFR efr : lists) {
            efr.setIsForwarded(lliApplication.getIsForwarded());
            efrService.updateApplicaton(efr);
            lliApplicationService.sendNotification(lliApplication, state, loginDTO);
        }

        efrCheck(appID, state, loginDTO);
        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/forwardedefrselect", requestMethod = RequestMethod.POST)
    public String selectVendorWork(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);


        for (EFR efr : lists) {
            efrService.updateApplicaton(efr);
        }

        List<LocalLoop> localLoops = localLoopService.prepareLocalloop(appID);
        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.updateApplicaton(localLoop);


        }

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/forwarded-efr-select-only-loop", requestMethod = RequestMethod.POST)
    public String selectVendorWorkOnlyLoop(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        ArrayList<EFR> lists = new EFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        for (EFR efr : lists) {
            efrService.updateApplicaton(efr);
        }
        List<LocalLoop> localLoops = localLoopService.prepareLocalloop(appID);
        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.updateApplicaton(localLoop);
        }

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        int oldZone = lliApplication.getSecondZoneID();
        int currentZone = lliApplication.getZoneId();
        lliApplication.setZoneId(oldZone);
        lliApplication.setSecondZoneID(currentZone);
        lliApplication.setIsForwarded(0);
        lliApplication.setState(state);
        lliApplication.setSkipPayment(lliApplication.getSkipPayment());
        lliApplicationService.updateApplicaton(lliApplication);
        lliApplicationService.sendNotification(lliApplication, lliApplication.getState(), loginDTO);

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/forwardedifrselect", requestMethod = RequestMethod.POST)
    public String selectIfrForwarded(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement ifrElement = jsonObject.get("ifr");

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);


        List<IFR> ifrs = new IFRDeserializer().deserialize_custom(jsonElement);
        ArrayList<IFR> selectedIFR = new ArrayList<>();
        for (IFR ifr : ifrs) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.updateApplicaton(ifr);
        }


        List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.insertApplication(localLoop);
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);


        return "";//need to go search page
    }


    //endregion


    @JsonPost
    @RequestMapping(mapping = "/discard", requestMethod = RequestMethod.POST)
    public String discardApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }


    //region cc check part
    @JsonPost
    @RequestMapping(mapping = "/ccrequest", requestMethod = RequestMethod.POST)
    public String ccRequestApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/ccresponse", requestMethod = RequestMethod.POST)
    public String ccResponseApplication(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }

    //endregion

    @JsonPost
    @RequestMapping(mapping = "/applicationforward", requestMethod = RequestMethod.POST)
    public String applicationForward(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        lliApplication.setIsForwarded(1);
        if (lliApplication.getSecondZoneID() == 0) {

            lliApplication.setSecondZoneID(lliApplication.getZoneId());
        }
        lliApplication.setZoneId(jsonObject.get("zoneID").getAsInt());

        lliApplication.setState(state);

        List<IFR> ifrList = ifrService.getIFRByAppID(lliApplication.getApplicationID());
        for (IFR ifR : ifrList
        ) {
            ifR.setIsIgnored(1);
            ifrService.updateApplicaton(ifR);

        }

        lliApplicationService.updateApplicaton(lliApplication);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/application-forward-for-loop", requestMethod = RequestMethod.POST)
    public String applicationForwardForLoop(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        applicationFlowOperations.applicationForwardForEFRPurpose(jsonElement,loginDTO);
        return "";//need to go search page
    }


    //region demand note
    @JsonPost
    @RequestMapping(mapping = "/demandnotegenerate", requestMethod = RequestMethod.POST)
    public String demandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        applicationFlowOperations.DNGenerateLogic(jsonElement, loginDTO, 0);
        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/skipdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String skipDemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        applicationFlowOperations.DNGenerateLogic(jsonElement, loginDTO, 1);

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/withoutloopskipdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String noLoopSkipDemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement ifrElement = jsonObject.get("ifr");

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        lliApplication.setSkipPayment(1);
        lliApplicationService.updateApplicaton(lliApplication);

//		lliApplicationService.updateApplicatonState(appID,state);

        List<IFR> ifrs = new IFRDeserializer().deserialize_custom(jsonElement);
        ArrayList<IFR> selectedIFR = new ArrayList<>();
        for (IFR ifr : ifrs) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.updateApplicaton(ifr);
        }


        List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.insertApplication(localLoop);
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);


        //lliApplicationService.updateApplicatonState(appID,state);


        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/withoutloopdemandnotegenerate", requestMethod = RequestMethod.POST)
    public String withoutLoopDemandNote(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement ifrElement = jsonObject.get("ifr");

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        lliApplication.setSkipPayment(0);
        lliApplicationService.updateApplicaton(lliApplication);
        List<IFR> ifrs = new ArrayList<>();
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {
            ifrs = new IFRDeserializer().deserialize_custom_local_loop(jsonElement);

        } else if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP) {
            IFR ifr = new IFRDeserializer().deserialize_additoinal_ip(jsonElement);
            ifrs.add(ifr);
        } else ifrs = new IFRDeserializer().deserialize_custom(jsonElement);
        ArrayList<IFR> selectedIFR = new ArrayList<>();
        for (IFR ifr : ifrs) {

            if (ifr.getIsSelected() == 1) {
                selectedIFR.add(ifr);
                ifr.setSelectedBW(ifr.getRequestedBW());
            }
            ifr.setIsForwarded(lliApplication.getIsForwarded());
            ifrService.updateApplicaton(ifr);
        }

        if (lliApplication.getApplicationType() != LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                && lliApplication.getApplicationType() != LLIConnectionConstants.ADDITIONAL_PORT) {

            List<LocalLoop> localLoops = localLoopService.prepareLocalloopFromIFR(selectedIFR);
            for (LocalLoop localLoop : localLoops
            ) {
                localLoopService.insertApplication(localLoop);
            }
        }

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);


        //lliApplicationService.updateApplicatonState(appID,state);


        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/demandnotepayment", requestMethod = RequestMethod.POST)
    public String demandNotePay(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        if (lliApplicationService.getLLIApplicationByApplicationID(appID).getApplicationType() == LLIConnectionConstants.RECONNECT) {
            List<BillDTO> unpaidBills = billService.getUnPaidBillDTOListByEntityTypeIDAndClientIDAndBillType(LLIConnectionConstants.ENTITY_TYPE,
                    loginDTO.getAccountID(), BillConstants.MONTHLY_BILL);

            if (unpaidBills != null) {
                throw new RequestFailureException("You have unpaid bills. Please pay them first.");
            }
        }
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);
        String billIds = lliApplication.getDemandNoteID().toString();
//
//        Map<String,String> resultantMap = new HashMap<>();
//
//        Enumeration<String> parameteKeys = request.getParameterNames();
//        while(parameteKeys.hasMoreElements()){
//            String parameterKey = parameteKeys.nextElement();
//            String parameterValue = request.getParameter(parameterKey);
//            resultantMap.put(parameterKey, parameterValue);
//        }
//
//        resultantMap.put("billIDs", String.valueOf(billIds));
//        resultantMap.put("state", String.valueOf(state));
//
//        request.getSession().setAttribute("bill",billIds);
//        request.getSession().setAttribute("state",state);
//
//        System.out.println(request.getSession().getAttribute("billIDs").toString());
//      request.
//		ArrayList<EFR> lists=new EFRDeserializer().deserialize_custom(jsonElement);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
//        lliApplicationService.updateApplicatonState(appID, state);
        return "";//need to go search page
    }
    //endregion


    @JsonPost
    @RequestMapping(mapping = "/changestate", requestMethod = RequestMethod.POST)
    public String applicationStateChange(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);
        if (lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH
                || lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION) {

            if (lliApplication.getZoneId() > 0 && lliApplication.getSecondZoneID() == 0) {
                if (lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH) {

                    state = LLIConnectionConstants.SHIFT_BW_SUBMITTED_ONLY_LDGM_APPROVAL;
                } else {
                    if (lliApplication.getLoopProvider() == LLIConnectionConstants.LOOP_PROVIDER_BTCL) {

                        state = LLIConnectionConstants.SHIFT_BW_NEW_CONNECTION_SUBMITTED_ONLY_LDGM_APPROVAL;
                    } else {
                        state = LLIConnectionConstants.WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_LDGM_APPROVAL;

                    }
                }
                lliApplication.setSecondZoneID(lliApplication.getZoneId());
                lliApplication.setZoneId(0);
                lliApplicationService.updateApplicaton(lliApplication);
            }
        }

        lliApplicationService.sendNotification(lliApplication, state, loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/complete-shift-bw", requestMethod = RequestMethod.POST)
    public String applicationShiftBWComplete(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        long appType = jsonObject.get("applicationType").getAsLong();
        lliApplicationFlowConnectionManagerService.connectionCreatorOrUpdatorManager(jsonObject, appType, loginDTO);
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);

        lliApplicationService.updateApplicatonState(appID, state);
//        lliApplicationService.sendNotification(lliApplication,lliApplication.getState(),loginDTO);


        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/retransfer", requestMethod = RequestMethod.POST)
    public String applicationRetransfer(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        int oldZone = lliApplication.getSecondZoneID();
        int currentZone = lliApplication.getZoneId();
        lliApplication.setZoneId(oldZone);
        lliApplication.setSecondZoneID(currentZone);
        lliApplication.setIsForwarded(0);
        lliApplication.setState(state);
        lliApplication.setSkipPayment(lliApplication.getSkipPayment());

        lliApplicationService.updateApplicaton(lliApplication);
        lliApplicationService.sendNotification(lliApplication, state, loginDTO);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        return "";//need to go search page
    }

//    @JsonPost
//    @RequestMapping(mapping = "/", requestMethod = RequestMethod.POST)
//    public String applicationRetransfer(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
//        JsonElement jsonElement = new JsonParser().parse(JsonString);
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//
//        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
//        int state = jsonObject.get("nextState").getAsInt();
//
//        LLIApplication lliApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
//        int oldZone = lliApplication.getSecondZoneID();
//        int currentZone = lliApplication.getZoneId();
//        lliApplication.setZoneId(oldZone);
//        lliApplication.setSecondZoneID(currentZone);
//        lliApplication.setIsForwarded(0);
//        lliApplication.setState(state);
//
//        lliApplicationService.updateApplication(lliApplication);
//
//        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
//        commentsService.insertComments(comments, loginDTO);
//
//        return "";//need to go search page
//    }


    //region work order
    private void WOCheck(long applicationID, int state, LoginDTO loginDTO) throws Exception {
        List<EFR> efrArrayList = efrService.getNotCompletedWOByVendor(applicationID, loginDTO.getUserID());

        for (EFR efr : efrArrayList) {
//            if (System.currentTimeMillis() > efr.getWorkDeadline()) {
////                efr.setWorkCompleted(2);
////
////            }

            //todo: expire policy

            efr.setWorkCompleted(LLIConnectionConstants.EFR_WORK_DONE);
            efrService.updateApplicaton(efr);

        }


        List<EFR> efrArrayList2 = efrService.getNotCompletedWO(applicationID);
        if (efrArrayList2.size() == 0) {

            lliApplicationService.updateApplicatonState(applicationID, state);
//            lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(applicationID),state,loginDTO);

        }

    }

    @JsonPost
    @RequestMapping(mapping = "/workordergenerate", requestMethod = RequestMethod.POST)
    public String generateWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        lliApplicationService.handleWOGenerationRequest(jsonElement, loginDTO);
        return "";
    }

    @JsonPost
    @RequestMapping(mapping = "/closeworkordergenerate", requestMethod = RequestMethod.POST)
    public String generateCloseWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        lliApplicationService.handleWOGenerationRequest(jsonElement, loginDTO);


        return "";//need to go search page


    }


    @JsonPost
    @RequestMapping(mapping = "/completeworkorder", requestMethod = RequestMethod.POST)
    public String completeWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

//        lliApplicationService.updateApplicatonState(appID, state);
        ArrayList<EFR> lists = new ArrayList<>();
        LLIApplication lliApplication = lliApplicationService.getLLIApplicationByApplicationID(appID);
        if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT) {
            lists = new EFRDeserializer().deserialize_custom_new_local_loop(jsonElement);
        } else lists = new EFRDeserializer().deserialize_custom(jsonElement);

        for (EFR efr : lists) {
            if (efr.getActualLoopDistance() <= 0) {
                efr.setActualLoopDistance(efr.getProposedLoopDistance());
                efr.setLoopDistanceIsApproved(1);
            }
            efrService.updateApplicaton(efr);
            lliApplicationService.sendNotification(lliApplication, state, loginDTO);
        }
        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        WOCheck(appID, state, loginDTO);

        return "";//need to go search page
    }

    //endregion


    //region advice note
    @JsonPost
    @RequestMapping(mapping = "/advicenotegenerate", requestMethod = RequestMethod.POST)
    public String generateAN(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        applicationFlowOperations.adviceNoteGenerateLogicOperations(jsonElement, loginDTO);
        return "";//need to go search page
    }


    //endregion

    @JsonPost
    @RequestMapping(mapping = "/forward-after-work-order", requestMethod = RequestMethod.POST)
    public String forwardAfterWO(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);

        applicationFlowOperations.ldgmForwardAfterWO(jsonElement,loginDTO);

        return "";//need to go search page
    }


    //region complete process part

    @JsonPost
    @RequestMapping(mapping = "/completetesting", requestMethod = RequestMethod.POST)
    public String cpmpleteTest(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        List<LocalLoop> localLoops = new LocalLoopDeserializer().deserialize_custom(jsonElement);

        for (LocalLoop localLoop : localLoops
        ) {
            localLoopService.updateApplicaton(localLoop);

        }


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        lliApplicationService.updateApplicatonState(appID, state);

        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);


        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/completeconnection", requestMethod = RequestMethod.POST)
    public String completeConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();


        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID").getAsLong();

        lliApplicationFlowConnectionManagerService.connectionCreatorOrUpdatorManager(jsonObject, appTypeID, loginDTO);


        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);

        lliApplicationService.updateApplicatonState(appID, state);
//        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID),state,loginDTO);


        return "";//need to go search page
    }

    @JsonPost
    @RequestMapping(mapping = "/closeapplication", requestMethod = RequestMethod.POST)
    public String completeCloseConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        long conID = jsonObject.get("application").getAsJsonObject().get("connectionID").getAsLong();

        lliFlowConnectionService.closeConnectionByConnectionID(conID, System.currentTimeMillis(), appID);

        Comments comments = new CommentsDeserializer().deserialize_custom(jsonElement, loginDTO);
        commentsService.insertComments(comments, loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);
        lliApplicationService.sendNotification(lliApplicationService.getLLIApplicationByApplicationID(appID), state, loginDTO);

        return "";//need to go search page
    }


    @JsonPost
    @RequestMapping(mapping = "/completetestingandcreateconnection", requestMethod = RequestMethod.POST)
    public String completeTestAndConnection(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        lliApplicationFlowConnectionManagerService.newConnectionInsertManager(jsonElement, loginDTO);
        return "";//need to go search page
    }


    //endregion


    //complete application for new local loop
    @JsonPost
    @RequestMapping(mapping = "/completetestingandCreateNewLocalLoop", requestMethod = RequestMethod.POST)
    public String completeTestAndConnectionForNewLocalLoop(
            @RequestParameter(isJsonBody = true, value = "data") String JsonString,
            LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        lliApplicationService.handleNewConnectionComplete(jsonElement, loginDTO);
        return "";//need to go search page
    }

    //    completetestingandCreateAdditionalIP
    @JsonPost
    @RequestMapping(mapping = "/completetestingandCreateAdditionalIP", requestMethod = RequestMethod.POST)
    public String completetestingandCreateAdditionalIP(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        lliApplicationService.handleNewConnectionCompleteAdditionalIP(jsonElement, loginDTO);
        return "";//need to go search page
    }
    //jami end


    @Service
    InventoryService inventoryService;

    @JsonPost
    @RequestMapping(mapping = "/getchild", requestMethod = RequestMethod.POST)
    public List<InventoryItem> getChild(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        int childCategory = 0;
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Long parent = jsonObject.get("parent") != null ? jsonObject.get("parent").getAsLong() : null;
//        if(parent == null || parent == 0L) {
//            log.fatal("NO parent found in jsonString: data");
//            throw new RequestFailureException("Parent Inventory Item Information Needed");
//        }
        Integer catagory = jsonObject.get("catagory") != null ? jsonObject.get("catagory").getAsInt() : null;
        //TODO change hardcode to a constant factory file
        childCategory = catagory;
        return inventoryService.getInventoryItems(childCategory, parent);
    }


    @JsonPost
    @RequestMapping(mapping = "/checkbandwidthconfiguration", requestMethod = RequestMethod.POST)
    public List<BandwidthConfiguration> getBandwidthConfiguration(@RequestParameter(isJsonBody = true, value = "data") String JsonString, LoginDTO loginDTO) throws Exception {
        JsonElement jsonElement = new JsonParser().parse(JsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long bandwidth = jsonObject.get("bandwidth").getAsLong();
        return bandwidthConfigurationService.getBandwidthConfiguration(bandwidth);
    }

    @RequestMapping(mapping = "/get-ol-info", requestMethod = RequestMethod.GET)
    public JsonArray getOfficialLetterInfoByAppId(@RequestParameter("appId") long appId,
                                                  @RequestParameter("moduleId") int moduleId
    ) throws Exception {

        return officialLetterService.getOfficialLettersByApplicationIdAndModuleId(appId, moduleId);
    }

}

