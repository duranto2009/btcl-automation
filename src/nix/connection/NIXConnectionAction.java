package nix.connection;

import annotation.ForwardedAction;
import common.RequestFailureException;
import inventory.InventoryAttributeValue;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.LLIDropdownPair;
import login.LoginDTO;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.localloop.NIXLocalLoop;
import nix.office.NIXOffice;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.localloop.NIXLocalLoopService;
import nix.office.NIXOfficeService;
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

@ActionRequestMapping("nix/connection")
public class NIXConnectionAction extends AnnotatedRequestMappingAction {

    @Service
    NIXApplicationLocalLoopService nixApplicationLocalLoopService;

    @Service
    NIXConnectionService nixConnectionService;

    @Service
    NIXOfficeService nixOfficeService;

    @Service
    NIXLocalLoopService nixLocalLoopService;


    @Service
    InventoryService inventoryService;

    @ForwardedAction
    @RequestMapping(mapping = "/get-active-connection-by-client", requestMethod = RequestMethod.GET)
    public List<LLIDropdownPair> getActiveConnectionInfo(@RequestParameter("id")long clientId) throws Exception {
        List<NIXConnection> nixConnections = nixConnectionService.getConnectionByClientID(clientId);
        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
        for(NIXConnection nixConnection:nixConnections){
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
                                                    nixConnection.getId(),
                                                    nixConnection.getName(),
                                                    nixConnection);
            lliDropdownPairs.add(lliDropdownPair);
        }
        return  lliDropdownPairs;
    }

    @ForwardedAction
    @RequestMapping(mapping = "/get-officeObjectList-by-connection-id", requestMethod = RequestMethod.GET)
    public List<LLIDropdownPair> getActiveOfficesByConnection(@RequestParameter("id")long connectionId) throws Exception {
        List<NIXOffice> nixOffices = nixOfficeService.getOfficesByConnectionID(connectionId);
        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
        for(NIXOffice nixOffice:nixOffices){
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
                    nixOffice.getId(),
                    nixOffice.getName(),
                    nixOffice);
            lliDropdownPairs.add(lliDropdownPair);
        }
        return  lliDropdownPairs;
    }


    @ForwardedAction
    @RequestMapping(mapping = "/get-portList-by-connection-id", requestMethod = RequestMethod.GET)
    public List<LLIDropdownPair> getPortsByOffice(@RequestParameter("id")long officeId) throws Exception {
        NIXOffice nixOffice = nixOfficeService.getOfficeById(officeId);
        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
        for(NIXLocalLoop nixLocalLoop:nixOffice.getLocalLoops()){
            /*NIXApplicationLocalLoop nixApplicationLocalLoop = nixApplicationLocalLoopService.
                    getLocalLoopById(nixLocalLoop.getApplicationLocalLoop());*/
            NIXApplicationLocalLoop nixApplicationLocalLoop = nixLocalLoop.getNixApplicationLocalLoop();
            if(nixApplicationLocalLoop != null){
                long portId = nixApplicationLocalLoop.getPortId();
                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(portId);

                InventoryAttributeValue inventoryAttributeValue = inventoryService.getInventoryAttributeValueByNameIdAndItemId(
                        73011,inventoryItem.getID());
                LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
                        inventoryItem.getID(),
                        inventoryItem.getName(),
                        inventoryAttributeValue);
                lliDropdownPairs.add(lliDropdownPair);
            }
            else {
                throw new RequestFailureException("No Port Found for this connection office");
            }

        }
        return  lliDropdownPairs;
    }

    /**
     * @autohor: Forhad
     */
    /*Search NIX Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping="/search", requestMethod=RequestMethod.All)
    public String searchLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_NIX_CONNECTION, request,
                nixConnectionService, SessionConstants.VIEW_NIX_CONNECTION, SessionConstants.SEARCH_NIX_CONNECTION);
        rnManager.doJob(loginDTO);
        return "nix-connection-search";
    }
    /*Search NIX Connection Ends*/

    /*View NIX Connection Begins*/
    @ForwardedAction
    @RequestMapping(mapping="/view", requestMethod=RequestMethod.All)
    public String getConnectionForView() throws Exception{
        return "nix-connection-view";
    }
    /**/

    @ForwardedAction
    @RequestMapping(mapping = "/get-connection-by-history-id", requestMethod = RequestMethod.GET)
    public List<NIXConnection> getNIXConnectionInstanceListByHistoryID(@RequestParameter("id")long historyID) throws Exception {
        return  nixConnectionService.getNIXConnectionInstanceListByHistoryID(historyID);
    }


}
