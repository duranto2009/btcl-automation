package lli.changeIPPort;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.*;
import common.ModuleConstants;
import common.RequestFailureException;
import connection.DatabaseConnection;
import flow.entity.Module;
import global.GlobalService;
import inventory.*;
import ip.IPBlockForConnection;
import ip.IPConstants;
import ip.IPService;
import ip.ipUsage.IPBlockUsage;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import requestMapping.Service;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ChangeIPPortService {


    @Service private InventoryService inventoryService;
    @Service private GlobalService globalService;
    @DAO private InventoryDAO inventoryDAO;
    private IPService ipService = ServiceDAOFactory.getService(IPService.class);
    private InventoryAllocationHistoryService allocationHistoryService = ServiceDAOFactory.getService(InventoryAllocationHistoryService.class);

    @Transactional
    JsonObject deserializeInfo(LLIConnection  lliConnection) throws Exception {
        List<Office> officeList = ServiceDAOFactory.getService(OfficeService.class).getOfficeByConnectionHistoryId(lliConnection.getHistoryID());

        lliConnection.setOffices(officeList);

        List<Long> portIds = officeList.stream()
                .map(Office::getLoops)
                .flatMap(List::stream)
                .map(LocalLoop::getPortID)
                .collect(Collectors.toList());

        List<Long> vlanIds = officeList.stream()
                .map(Office::getLoops)
                .flatMap(List::stream)
                .map(LocalLoop::getVLANID)
                .collect(Collectors.toList());
        Map<Long, String> mapOfPortTypesToPortId = getPortTypes(portIds);

        Map<Long, String> mapOfVlanTypesToVlanId = getVlanTypes(vlanIds);

        List<Long>portVlanIds = Stream.concat(portIds.stream(), vlanIds.stream()).collect(Collectors.toList());

        Map<Long, List<InventoryAllocationHistory>> modifiedVLANsAndPorts = inventoryService.getMapOfAllocationHistoriesByItemIds(portVlanIds);

        Map<Long, String> isUsedMap = new HashMap<>();
        portVlanIds.forEach(t-> {
            String isUsed = modifiedVLANsAndPorts.getOrDefault(t, Collections.emptyList()).isEmpty() ? "Not Used" : "Used";
            isUsedMap.put(t, isUsed);
        });

        Gson gson = new Gson();
//
        String informationByConnectionID = gson.toJson(lliConnection);
        JsonElement jElement = new JsonParser().parse(informationByConnectionID);
        JsonObject jsonObjectConnection = jElement.getAsJsonObject();
        jsonObjectConnection.add("usedNotUsedMap", gson.toJsonTree(isUsedMap));
        jsonObjectConnection.add("portTypeMap", gson.toJsonTree(mapOfPortTypesToPortId));
        jsonObjectConnection.add("vlanTypeMap", gson.toJsonTree(mapOfVlanTypesToVlanId));

        return jsonObjectConnection;
    }



    @Transactional
    JsonArray getswitchByPop(long popId) throws Exception{

        List<InventoryItem> router_switches = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants
                .CATEGORY_ID_ROUTER, popId);
        JsonArray router_switchArray = new JsonArray();
        for (InventoryItem router_switch : router_switches) {
            JsonElement router_swi = new Gson().toJsonTree(router_switch);
            JsonObject router_swiAsJsonObject = router_swi.getAsJsonObject();
            router_switchArray.add(router_swiAsJsonObject);

        }

        return router_switchArray;
    }


    @Transactional
    JsonArray getPortBySwitch(long switchId) throws Exception{

        List<InventoryItem> ports = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_PORT,
                switchId);
        JsonArray portArray = new JsonArray();
        JsonArray vlanArray = new JsonArray();
        for (InventoryItem port : ports) {
            JsonElement pGson = new Gson().toJsonTree(port);
            JsonObject pObject = pGson.getAsJsonObject();
            portArray.add(pObject);
        }
        return portArray;
    }

    @Transactional
    JsonArray getVlans(long switchId)throws Exception {

        List<InventoryItem> vlans = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID
                (InventoryConstants.CATEGORY_ID_VIRTUAL_LAN,switchId);
        JsonArray vlanArray = new JsonArray();

        for (InventoryItem vlan : vlans) {
            JsonElement vGson = new Gson().toJsonTree(vlan);
            JsonObject vObject = vGson.getAsJsonObject();
            vlanArray.add(vObject);
        }

        return vlanArray;
    }

    @Transactional
    void saveIpPortLog(IPPortChangeLog ipPortChangeLog) throws Exception{
        ModifiedSqlGenerator.insert(ipPortChangeLog);
    }

    @Transactional
    void deallocationAndAllocation(long usageId,
                                          JsonObject jsonObject,
                                          long connectionId,
                                          long userId)throws Exception {
        IPBlockUsage ipBlockUsage= ServiceDAOFactory.getService(GlobalService.class).findByPK(IPBlockUsage.class,usageId);
        ipService.deallocateIPsByUsageId(usageId);
        //ipService.allocateIPAddress();
        String newInfo = "";
        // TODO: 12/4/18 allocate new IP blocks
        List<IPBlockForConnection> ipBlockList = new ArrayList<>();
        JsonArray jsonArray2 = (JsonArray) jsonObject.get("ip");
        for (JsonElement jsonElement1 : jsonArray2) {

            IPBlockForConnection ipBlockForConnection = new IPBlockForConnection();
            JsonObject iPjsonObject = jsonElement1.getAsJsonObject();
            ipBlockForConnection.setFromIP(iPjsonObject.get("fromIP").getAsString());
            ipBlockForConnection.setToIP(iPjsonObject.get("toIP").getAsString());
            ipBlockForConnection.setRegionId(iPjsonObject.get("regionId").getAsLong());
            ipBlockForConnection.setType(IPConstants.Type.valueOf(iPjsonObject.get("type").getAsString()));
            ipBlockForConnection.setVersion(IPConstants.Version.valueOf(iPjsonObject.get("version").getAsString()));
            ipBlockForConnection.setRealIP(true);
            ipBlockForConnection.setConnectionId(connectionId);
            ipBlockForConnection.setUsageType(LLIConnectionConstants.IPUsageType.MANDATORY);
            ipBlockForConnection.setPurpose(IPConstants.Purpose.LLI_CONNECTION);
            newInfo += ipBlockForConnection.getFromIP()+", "+ipBlockForConnection.getToIP()+", ";

            ipBlockList.add(ipBlockForConnection);

        }

        newInfo +=" added";
        // TODO: 12/4/18 release old info with new
        String oldInfo="";
        if(ipBlockUsage!=null)oldInfo+= ipBlockUsage.getFromIP()+'-'+ipBlockUsage.getToIP()+" have been deleted";
        ipService.allocateIPAddress(ipBlockList);

        LLIConnection  lliConnection = ServiceDAOFactory.getService(LLIFlowConnectionService.class).getConnectionByID(connectionId);

        IPPortChangeLog ipPortChangeLog = new IPPortChangeLog();
        ipPortChangeLog.setNewInfo(newInfo);
        ipPortChangeLog.setPreviousInfo(oldInfo);
        ipPortChangeLog.setClientId(lliConnection.getClientID());
        ipPortChangeLog.setConnectionId(connectionId);
        ipPortChangeLog.setUpdatedBy(userId);
        ipPortChangeLog.setLastModificationTime(System.currentTimeMillis());

        saveIpPortLog(ipPortChangeLog);
    }

    @Transactional
    void updatePortInfoBatchOperation(JsonObject jsonObject, LoginDTO loginDTO)throws Exception {
        long userId = loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID();
        long portId = jsonObject.get("portId") != null ? jsonObject.get("portId").getAsLong() : 0;
        long vlanId = jsonObject.get("vlanId") != null ? jsonObject.get("vlanId").getAsLong() : 0;
        long switchId = jsonObject.get("switchId") != null ? jsonObject.get("switchId").getAsLong() : 0;
        long popId = jsonObject.get("popId") != null ? jsonObject.get("popId").getAsLong() : 0;
        long localLoopId = jsonObject.get("localLoopId") != null ? jsonObject.get("localLoopId").getAsLong() : 0;
        long connectionId = jsonObject.get("connectionId") != null ? jsonObject.get("connectionId").getAsLong() : 0;

        LocalLoop localLoop = ServiceDAOFactory.getService(GlobalService.class).findByPK(LocalLoop.class,localLoopId);
        LLIConnection  lliConnection = ServiceDAOFactory.getService(LLIFlowConnectionService.class).getConnectionByID(connectionId);
        long clientId = lliConnection.getClientID();
        long usedPort = localLoop.getPortID();
        long usedVlan = localLoop.getVLANID();
        long usedSwitch = localLoop.getRouter_switchID();

        //free use port and vlan

        allocationHistoryService.deallocationInventoryItem(usedPort, ModuleConstants.Module_ID_LLI, clientId);
        allocationHistoryService.deallocationInventoryItem(usedVlan, ModuleConstants.Module_ID_LLI, clientId);
        allocationHistoryService.deallocationInventoryItem(usedSwitch, ModuleConstants.Module_ID_LLI, clientId);

        //set new port vlan and switch as used port
        localLoop.setVLANID(vlanId);
        localLoop.setRouter_switchID(switchId);
        localLoop.setPortID(portId);
        ServiceDAOFactory.getService(LocalLoopService.class).updateApplicaton(localLoop);

        allocationHistoryService.allocateInventoryItem(portId, ModuleConstants.Module_ID_LLI, clientId);
        allocationHistoryService.allocateInventoryItem(vlanId, ModuleConstants.Module_ID_LLI, clientId);
        allocationHistoryService.allocateInventoryItem(switchId, ModuleConstants.Module_ID_LLI, clientId);




        IPPortChangeLog ipPortChangeLog = new IPPortChangeLog();
        ipPortChangeLog.setNewInfo(switchId+"(R/S), "+portId+"(port), "+vlanId+"(vlan) have been updated by new entries");
        ipPortChangeLog.setPreviousInfo(usedSwitch+"(R/S), "+usedPort+"(port), "+usedVlan+"(vlan) have been inserted" );
        ipPortChangeLog.setClientId(lliConnection.getClientID());
        ipPortChangeLog.setConnectionId(connectionId);
        ipPortChangeLog.setUpdatedBy(userId);
        ipPortChangeLog.setLastModificationTime(System.currentTimeMillis());

        saveIpPortLog(ipPortChangeLog);

    }

    @Transactional(transactionType = TransactionType.READONLY)
    JsonObject getInventoryData(long popId) throws Exception {
        // db get rs and gVLANs
        List<InventoryItem> rsAndGlobalVLANs = getRouterSwitchAndGVLANs(popId);

        // filter rs
        List<InventoryItem> rs = getInventoryItemsByType(rsAndGlobalVLANs, InventoryConstants.CATEGORY_ID_ROUTER);

        // filter gVLANs
        List<InventoryItem> gVLANs = getInventoryItemsByType(rsAndGlobalVLANs, InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN);

        // get all rs Ids for next db call
        List<Long> parentIdsForPortVLANs = getInventoryIdsFromInventoryItems(rs);

        // db get ports nd VLANs based on previous rsIDs
        List<InventoryItem> portVLANList = getPortVlansByRSIds(parentIdsForPortVLANs);

        // filter ports
        List<InventoryItem> ports = getInventoryItemsByType(portVLANList, InventoryConstants.CATEGORY_ID_PORT);

        // get all port Ids for next db call
        List<Long> portIds = getInventoryIdsFromInventoryItems(ports);

        // get port type with port id map
        Map<Long, String> mapOfPortTypesToPortId = getPortTypes(portIds);

        List<InventoryItem> modifiedVLANsAndPorts = inventoryService.getInventoryItemsModifiedByAllocationHistories(
                Stream.concat(gVLANs.stream(), portVLANList.stream()).collect(Collectors.toList())
        );

        // modified (used/not used) ports
        List<InventoryItem> modifiedPorts = getInventoryItemsByType(modifiedVLANsAndPorts, InventoryConstants.CATEGORY_ID_PORT);
        List<InventoryItem> modifiedGlobalVLANs = getInventoryItemsByType(modifiedVLANsAndPorts, InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN);
        List<InventoryItem> modifiedRegularVLANs = getInventoryItemsByType(modifiedVLANsAndPorts, InventoryConstants.CATEGORY_ID_VIRTUAL_LAN);

        List<JsonObject> dataRS = rs.stream()
                .map(t-> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", t.getID());
                    jsonObject.addProperty("name", t.getName());

                    return  jsonObject;
                }).collect(Collectors.toList());
        List<JsonObject> dataPorts = modifiedPorts
            .stream()
            .map(port-> {
                JsonObject jsonObject = new JsonObject();
                String portType = mapOfPortTypesToPortId.getOrDefault(port.getID(), "N/A");
                jsonObject.addProperty("id", port.getID());
                jsonObject.addProperty("name", port.getName());
                jsonObject.addProperty("type", portType);
                jsonObject.addProperty("used", port.getIsUsed());
                jsonObject.addProperty("parent", port.getParentID());
                return jsonObject;
            }).collect(Collectors.toList());

        List<JsonObject> dataVlans = Stream.concat(modifiedGlobalVLANs.stream(), modifiedRegularVLANs.stream()).collect(Collectors.toList())
                .stream()
                .map(vlan-> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", vlan.getID());
                    jsonObject.addProperty("name", vlan.getName());
                    jsonObject.addProperty("global", vlan.getParentID() == null);
                    jsonObject.addProperty("used", vlan.getIsUsed());
                    jsonObject.addProperty("parent", vlan.getParentID());
                    return jsonObject;
                }).collect(Collectors.toList());

        // prepare data to feed front end
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        jsonObject.add("rs", gson.toJsonTree(dataRS));
        jsonObject.add("vlans", gson.toJsonTree(dataVlans));
        jsonObject.add("ports", gson.toJsonTree(dataPorts));
        return jsonObject;

    }

//    private Map<>
    private Map<Long, String> getPortTypes(List<Long> portIds) throws Exception {
        Map <Long, String> mapOfPortTypesToPortId = new HashMap<>();
        if(!portIds.isEmpty()) {
            mapOfPortTypesToPortId = globalService.getAllObjectListByConditionForBatchOps(
                    InventoryAttributeValue.class, new InventoryAttributeValueConditionBuilder()
                            .Where()
                            // need to change this hard code; this is port type value; go to at_inv_attr_name table;pk of port type
                            .inventoryAttributeNameIDEquals(73011L)
                            .inventoryItemIDIn(portIds)

                            .getCondition()
            ).stream()
                    .collect(Collectors.toMap(InventoryAttributeValue::getInventoryItemID, InventoryAttributeValue::getValue));
        }
        return mapOfPortTypesToPortId;
    }

    private Map<Long, String> getVlanTypes(List<Long> vlanIds) throws Exception {
        if(vlanIds.isEmpty()) return Collections.emptyMap();
        return globalService.getAllObjectListByCondition(
                InventoryItem.class, new InventoryItemConditionBuilder()
                .Where()
                .IDIn(vlanIds)
                .isDeleted(false)
                .getCondition()
        ).stream()
                .collect(Collectors.toMap(InventoryItem::getID, t->t.getParentID()==null ? "Global": "Regular"));
    }

    private List<InventoryItem> getPortVlansByRSIds(List<Long> parentIdsForPortVlan) throws Exception {
        List<InventoryItem> portVLANList = new ArrayList<>();
        if(!parentIdsForPortVlan.isEmpty()) {
            portVLANList = globalService
                    .getAllObjectListByConditionForBatchOps(
                            InventoryItem.class, new InventoryItemConditionBuilder()
                                    .Where()

                                    .inventoryCatagoryTypeIDEqualsWithStartingBracket(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN)
                                    .Or(
                                            new InventoryItemConditionBuilder()
                                                    .inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_PORT)
                                                    .getSqlPair()
                                    ).bracketEnd()
                                    .parentIDIn(parentIdsForPortVlan)
                                    .isDeleted(false)
                                    .getCondition()
                    );
        }
        return portVLANList;
    }

    private List<InventoryItem> getRouterSwitchAndGVLANs(long popId) throws Exception {

        return globalService
                .getAllObjectListByConditionForBatchOps(
                        InventoryItem.class, new InventoryItemConditionBuilder()
                                .Where()
                                .inventoryCatagoryTypeIDEqualsWithStartingBracket(InventoryConstants.CATEGORY_ID_ROUTER)
                                .parentIDEquals(popId)
                                .bracketEnd()
                                .Or(
                                        new InventoryItemConditionBuilder()
                                                .inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN)
                                                .getSqlPair()
                                ).isDeleted(false)
                                .getCondition()
                );

    }

    private List<Long> getInventoryIdsFromInventoryItems(List<InventoryItem> inventoryItems) {
        return inventoryItems.stream()
                .map(InventoryItem::getID)
                .collect(Collectors.toList());
    }

    private List<InventoryItem> getInventoryItemsByType(List<InventoryItem> inventoryItems, int category) {
        return inventoryItems.stream()
                .filter(t->t.getInventoryCatagoryTypeID() == category)
                .collect(Collectors.toList());
    }
}
