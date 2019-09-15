package lli;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.GsonBuilder;
import common.RequestFailureException;
import inventory.InventoryService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import login.LoginDTO;
import org.apache.struts.action.ActionForward;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ActionRequestMapping("lli/connection")
public class LLIConnectionAction extends AnnotatedRequestMappingAction {

    @Service
    LLIConnectionService lliConnectionService;
    @Service
    LLIConnectionApplicationService lliConnectionApplicationService;
    @Service
    InventoryService inventoryService;

    @Override
    public GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //LLI Connection
        gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionInstanceSerizalizer());
        gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeSerizalizer());
        gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopSerizalizer());
        gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer());
        gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer());
        gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer());

        //LLI Connection Application
        gsonBuilder.registerTypeAdapter(LLIApplicationType.class, new LLIConnectionApplicationSerializer());
        gsonBuilder.registerTypeAdapter(LLIApplicationType.class, new LLIConnectionApplicationDeserializer());
        gsonBuilder.registerTypeAdapter(LLIApplicationInstance.class, new LLIConnectionApplicationInstanceSerializer());
        gsonBuilder.registerTypeAdapter(LLIApplicationInstance.class, new LLIConnectionApplicationInstanceDeserializer());
        return gsonBuilder;
    }

    /*New LLI Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping = "/new", requestMethod = RequestMethod.GET)
    public String getConnectionForNew() throws Exception {
        return "lli-connection-new";
    }

    @RequestMapping(mapping = "/new-connection-json", requestMethod = RequestMethod.GET)
    public LLIConnectionInstance getNewConnectionJSON() throws Exception {
        return new LLIConnectionInstance();
    }

    @JsonPost
    @RequestMapping(mapping = "/new", requestMethod = RequestMethod.POST)
    public void postConnectionForNew(@RequestParameter(isJsonBody = true, value = "connection") LLIConnectionInstance lliConnectionInstance) throws Exception {
        lliConnectionService.insertNewLLIConnection(lliConnectionInstance);
    }
    /*New LLI Connection Ends*/

    /*Revise LLI Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping = "/revise", requestMethod = RequestMethod.GET)
    public String getConnectionForRevision() throws Exception {
        return "lli-connection-revise";
    }

    @RequestMapping(mapping = "/revise-connection-json", requestMethod = RequestMethod.GET)
    public LLIConnectionInstance getReviseConnectionJSON(@RequestParameter("id") Long ID) throws Exception {
        //return LLISerializerTest.getTestConnection();
        return lliConnectionService.getLLIConnectionByConnectionID(ID);
    }

    @JsonPost
    @RequestMapping(mapping = "/revise", requestMethod = RequestMethod.POST)
    public void getConnectionForRevision(@RequestParameter(isJsonBody = true, value = "connection") LLIConnectionInstance lliConnectionInstance) throws Exception {
        lliConnectionService.reviseConnection(lliConnectionInstance);
    }
    /*Revise LLI Connection Ends*/

    /*View LLI Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping = "/view", requestMethod = RequestMethod.All)
    public String getConnectionForView() throws Exception {
        return "lli-connection-view";
    }

    @RequestMapping(mapping = "/view-connection-json", requestMethod = RequestMethod.All)
    public LLIConnectionInstance getViewConnectionJSON(@RequestParameter("id") Long ID, LoginDTO loginDTO) throws Exception {
        LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(ID);
        if (!loginDTO.getIsAdmin() && loginDTO.getAccountID() != lliConnectionInstance.getClientID()) {
            throw new RequestFailureException("You do not have permission to see this connection.");
        }
        return lliConnectionInstance;
    }

    @RequestMapping(mapping = "/history-list-json", requestMethod = RequestMethod.All)
    public List<LLIConnectionInstance> getConnectionHistoryList(@RequestParameter("id") Long connectionID) throws Exception {
        return lliConnectionService.getConnectionHistoryListByConnectionID(connectionID);
    }

    @RequestMapping(mapping = "/connection-by-history-id", requestMethod = RequestMethod.All)
    public LLIConnectionInstance getConnectionByHistoryID(@RequestParameter("id") Long historyID) throws Exception {
        return lliConnectionService.getLLLIConnectionInstanceByConnectionHistoryID(historyID);
    }

    @RequestMapping(mapping = "/delete-last-history", requestMethod = RequestMethod.All)
    public void deleteLastHistory(@RequestParameter("id") long connectionID) throws Exception {
        lliConnectionService.deleteLastConnectionInstanceByConnectionID(connectionID);
    }
    /*View LLI Connection Ends*/

    /*Search LLI Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public String searchLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_CONNECTION, request,
                lliConnectionService, SessionConstants.VIEW_LLI_CONNECTION, SessionConstants.SEARCH_LLI_CONNECTION);
        rnManager.doJobCustom(loginDTO);
        return "lli-connection-search";
    }
    /*Search LLI Connection Ends*/


    @ForwardedAction
    @RequestMapping(mapping = "/view-all-connections", requestMethod = RequestMethod.All)
    public String searchLLIConnection(@RequestParameter("clientId") long clientId,
                                      LoginDTO loginDTO, HttpServletRequest request)
            throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_CONNECTION, request,
                lliConnectionService, SessionConstants.VIEW_LLI_CONNECTION, SessionConstants.SEARCH_LLI_CONNECTION);
        rnManager.doJobCustom(loginDTO);
        request.setAttribute("clientId", clientId);
        return "lli-connections-of-a-client";
    }


    /**/
    @RequestMapping(mapping = "/get-connection-by-client", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getConnectionListByClientID(@RequestParameter("id") long clientID) throws Exception {
        return lliConnectionService.getLLIConnectionNameIDPairListByClient(clientID);
    }

    @RequestMapping(mapping = "/get-active-connection-by-client", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getActiveConnectionListByClientID(@RequestParameter("id") long clientID) throws Exception {
        return lliConnectionService.getActiveLLIConnectionNameIDPairListByClient(clientID);
    }

    @RequestMapping(mapping = "/get-td-connection-by-client", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getTDConnectionListByClientID(@RequestParameter("id") long clientID) throws Exception {
        return lliConnectionService.getTDLLIConnectionNameIDPairListByClient(clientID);
    }

    @RequestMapping(mapping = "/get-office-by-connection-id", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getOfficeListByConnectionID(@RequestParameter("id") long connectionID) throws Exception {
        return lliConnectionService.getLLIConnectionOfficeNameIDPairListByConnectionID(connectionID);
    }


    //jami
    @RequestMapping(mapping = "/get-officeObjectList-by-connection-id", requestMethod = RequestMethod.All)
    public List<LLIDropdownPair> getOfficeObjectListByConnectionID(@RequestParameter("id") long connectionID) throws Exception {
        return lliConnectionService.getLLIConnectionOfficeListByConnectionID(connectionID);
    }

    //jami

    @RequestMapping(mapping = "/get-deep-connection-details", requestMethod = RequestMethod.All)
    public ActionForward getDeepConnectionDetailsByConnectionID(@RequestParameter("id") long connectionID, HttpServletResponse response) throws Exception {
        LLIConnectionInstance lliConnection = lliConnectionService.getLLIConnectionByConnectionID(connectionID);

        String connection = new GsonBuilder()
                .registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionInstanceSerizalizer())
                .registerTypeAdapter(LLIOffice.class, new LLIOfficeSerizalizer())
                .registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeepSerializer())
                .create()
                .toJson(lliConnection);

        response.getWriter().write(connection);

        return null;

    }
    /**/

    /*Little AJAX Requests Begins*/
    @RequestMapping(mapping = "/vlan-details", requestMethod = RequestMethod.GET)
    public String getVlanDetails(@RequestParameter("id") long id) throws Exception {
        return inventoryService.getVlanDetailsByVlanID(id);
    }

    @RequestMapping(mapping = "/oc-details", requestMethod = RequestMethod.GET)
    public String getOcDetails(@RequestParameter("id") long id) throws Exception {
        return lliConnectionService.getOcDetails(id);
    }

    @RequestMapping(mapping = "/total-active-bw", requestMethod = RequestMethod.GET)
    public double getTotalActiveBW(@RequestParameter("clientId") long clientId) throws Exception {
        return lliConnectionService.getTotalExistingRegularBWByClientID(clientId);
    }
    /*Little AJAX Requests Ends*/
    @RequestMapping(mapping = "/get-local-loops", requestMethod = RequestMethod.GET)
    public List<LocalLoop> getOfficeDetailsByOfficeId(@RequestParameter("officeId") long officeId)throws Exception{
        List<LocalLoop>localLoops =  ServiceDAOFactory.getService(LocalLoopService.class).getLocalLoopByOffice(officeId);
        return  localLoops;
    }

}
