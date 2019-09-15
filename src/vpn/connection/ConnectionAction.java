//package vpn.connection;
//
//import annotation.ForwardedAction;
//import inventory.InventoryAttributeValue;
//import inventory.InventoryItem;
//import inventory.InventoryService;
//import lli.LLIDropdownPair;
//import login.LoginDTO;
//import nix.application.localloop.LocalLoop;
//import nix.application.localloop.LocalLoopService;
//import nix.localloop.LocalLoop;
//import nix.localloop.LocalLoopService;
//import nix.office.Office;
//import nix.office.OfficeService;
//import requestMapping.AnnotatedRequestMappingAction;
//import requestMapping.RequestParameter;
//import requestMapping.Service;
//import requestMapping.annotation.ActionRequestMapping;
//import requestMapping.annotation.RequestMapping;
//import requestMapping.annotation.RequestMethod;
//import sessionmanager.SessionConstants;
//import util.RecordNavigationManager;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//
//@ActionRequestMapping("nix/connection")
//public class ConnectionAction extends AnnotatedRequestMappingAction {
//
//    @Service
//    LocalLoopService nixApplicationLocalLoopService;
//
//    @Service
//    ConnectionService nixConnectionService;
//
//    @Service
//    OfficeService nixOfficeService;
//
//    @Service
//    LocalLoopService nixLocalLoopService;
//
//
//    @Service
//    InventoryService inventoryService;
//
//    @ForwardedAction
//    @RequestMapping(mapping = "/get-active-connection-by-client", requestMethod = RequestMethod.GET)
//    public List<LLIDropdownPair> getActiveConnectionInfo(@RequestParameter("id")long clientId) throws Exception {
//        List<Connection> nixConnections = nixConnectionService.getConnectionByClientID(clientId);
//        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
//        for(Connection nixConnection:nixConnections){
//            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
//                                                    nixConnection.getConnectionId(),
//                                                    nixConnection.getName(),
//                                                    nixConnection);
//            lliDropdownPairs.add(lliDropdownPair);
//        }
//        return  lliDropdownPairs;
//    }
//
//    @ForwardedAction
//    @RequestMapping(mapping = "/get-officeObjectList-by-connection-id", requestMethod = RequestMethod.GET)
//    public List<LLIDropdownPair> getActiveOfficesByConnection(@RequestParameter("id")long connectionId) throws Exception {
//        List<Office> nixOffices = nixOfficeService.getOfficesByConnectionID(connectionId);
//        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
//        for(Office nixOffice:nixOffices){
//            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
//                    nixOffice.getId(),
//                    nixOffice.getName(),
//                    nixOffice);
//            lliDropdownPairs.add(lliDropdownPair);
//        }
//        return  lliDropdownPairs;
//    }
//
//
//    @ForwardedAction
//    @RequestMapping(mapping = "/get-portList-by-connection-id", requestMethod = RequestMethod.GET)
//    public List<LLIDropdownPair> getPortsByOffice(@RequestParameter("id")long officeId) throws Exception {
//        Office nixOffice = nixOfficeService.getOfficeById(officeId);
//        List<LLIDropdownPair>lliDropdownPairs = new ArrayList<>();
//        for(LocalLoop nixLocalLoop:nixOffice.getLocalLoops()){
//            LocalLoop nixApplicationLocalLoop = nixApplicationLocalLoopService.
//                    getLocalLoopById(nixLocalLoop.getApplicationLocalLoop());
//            long portId = -1;
//            if(nixApplicationLocalLoop != null){
//                portId = nixApplicationLocalLoop.getPortId();
//                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(portId);
//
//                InventoryAttributeValue inventoryAttributeValue = inventoryService.getInventoryAttributeValueByNameIdAndItemId(
//                        73011,inventoryItem.getID());
//                LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
//                        inventoryItem.getID(),
//                        inventoryItem.getName(),
//                        inventoryAttributeValue);
//                lliDropdownPairs.add(lliDropdownPair);
//            }
//
//        }
//        return  lliDropdownPairs;
//    }
//
//    /**
//     * @autohor: Forhad
//     */
//    /*Search  Connection Begins*/
//    @ForwardedAction
//    @RequestMapping(mapping="/search", requestMethod=RequestMethod.All)
//    public String searchLLIConnection(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
//        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV__CONNECTION, request,
//                nixConnectionService, SessionConstants.VIEW__CONNECTION, SessionConstants.SEARCH__CONNECTION);
//        rnManager.doJob(loginDTO);
//        return "nix-connection-search";
//    }
//    /*Search  Connection Ends*/
//
//    /*View  Connection Begins*/
//    @ForwardedAction
//    @RequestMapping(mapping="/view", requestMethod=RequestMethod.All)
//    public String getConnectionForView() throws Exception{
//        return "nix-connection-view";
//    }
//    /**/
//
//    @ForwardedAction
//    @RequestMapping(mapping = "/get-connection-by-history-id", requestMethod = RequestMethod.GET)
//    public List<Connection> getConnectionInstanceListByHistoryID(@RequestParameter("id")long historyID) throws Exception {
//        return  nixConnectionService.getConnectionInstanceListByHistoryID(historyID);
//    }
//
//
//}
