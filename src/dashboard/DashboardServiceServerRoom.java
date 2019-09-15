package dashboard;

import annotation.Transactional;
import application.ApplicationState;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseDTOConditionBuilder;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.Application.ownership.LLIOwnerShipChangeApplicationConditionBuilder;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lombok.extern.log4j.Log4j;
import nix.application.NIXApplication;
import nix.application.NIXApplicationConditionBuilder;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseDTOConditionBuilder;
import requestMapping.Service;
import util.*;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;
@Log4j
public class DashboardServiceServerRoom {

    @Service private GlobalService globalService;
    @Service private DashboardService dashboardService;
    @Service private DashboardDataBuilder dashboardDataBuilder;

    KeyValuePair<DashboardMainFrame, List<DashboardCounterCard>> getInventoryDashboardCounterCards () throws Exception {
        Map<Integer, Long> mapTotal = getInventoryData();
        Map<Integer, Long > mapUsed = getUsedInventoryData();

        Map<String, Map<String, Long>> mapForForcedDirectedTree = new HashMap<>();
        DashboardCounterCard dashboardCounterCardPort = DashboardCounterCard.getDashboardCounterCard(
                getData(InventoryConstants.CATEGORY_ID_PORT, mapTotal, mapUsed, mapForForcedDirectedTree),
                DashboardConstants.TextClass.RED_HAZE.getTextClass(), "", "fa fa-money", ChartType.SimplePie
        );


        DashboardCounterCard dashboardCounterCardVlan = DashboardCounterCard.getDashboardCounterCard(
                getData(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, mapTotal, mapUsed, mapForForcedDirectedTree),
                DashboardConstants.TextClass.BLUE_SHARP.getTextClass(), "", "fa fa-money", ChartType.SimplePie
        );

        DashboardCounterCard dashboardCounterCardRS = DashboardCounterCard.getDashboardCounterCard(
                getData(InventoryConstants.CATEGORY_ID_ROUTER, mapTotal, mapUsed, mapForForcedDirectedTree),
                DashboardConstants.TextClass.PURPLE_SOFT.getTextClass(), "", "fa fa-money", ChartType.SimplePie
        );


        List<JsonObject> jsonObjectsTypes = new ArrayList<>();
        mapForForcedDirectedTree.forEach((k,v)->{
            List<JsonObject> jsonObjects = new ArrayList<>();
            v.forEach((k1, v1)->{
                JsonObject jsonObjectStatus = new JsonObject();
                jsonObjectStatus.addProperty("name", k1);
                jsonObjectStatus.addProperty("value", v1);
                jsonObjects.add(jsonObjectStatus);
            });
            JsonArray jsonArray = new JsonArray();
            jsonObjects.forEach(jsonArray::add);

            JsonObject jsonObjectCategory = new JsonObject();
            jsonObjectCategory.addProperty("name", k);
            jsonObjectCategory.add("children", jsonArray);
            jsonObjectsTypes.add(jsonObjectCategory);
        });
        JsonObject inventoryJsonObject = dashboardService.createJson(jsonObjectsTypes, "Inventory", 0);

        return new KeyValuePair<>(
                DashboardMainFrame.getMainFrame(
                        inventoryJsonObject, "Inventory", "chart-div-right", ChartType.CollapsibleForceDirectedTree
                ),  Arrays.asList(dashboardCounterCardPort, dashboardCounterCardRS, dashboardCounterCardVlan));


    }

    private JsonObject getData(int categoryType, Map<Integer, Long> mapTotal, Map<Integer, Long> mapUsed,
                               Map<String, Map<String, Long>> mapForForcedDirectedTree) {
        long total = mapTotal.getOrDefault(categoryType, 0L);
        long used = mapUsed.getOrDefault(categoryType, 0L);
        String categoryTypeString = InventoryConstants.getCategoryName(categoryType);

        if(total == 0){
            throw new RequestFailureException("Total Count of " + categoryTypeString + " can not be Zero");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("category", "Used");
        jsonObject.addProperty("value", used);

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("category", "Unused");
        jsonObject1.addProperty("value", total - used);

        List<JsonObject> objects = Arrays.asList(jsonObject, jsonObject1);

        mapForForcedDirectedTree.put(categoryTypeString, new HashMap<>());
        mapForForcedDirectedTree.get(categoryTypeString).put("Used", used);
        mapForForcedDirectedTree.get(categoryTypeString).put("Unused", total-used);

        return dashboardService.createJson(objects, categoryTypeString, used +"/" + total);


    }


    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Integer, Long> getInventoryData() throws Exception {

//        List<InventoryItem> items = globalService.getAllObjectListByCondition(InventoryItem.class,
//                " FORCE INDEX (invitInvCatTypeID) WHERE " + ModifiedSqlGenerator.getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")
//                        + " IN ( 4, 5, 6, 99, 101)"
//        );

        String columnName = ModifiedSqlGenerator.getColumnName(InventoryItem.class, "inventoryCatagoryTypeID");


        return globalService.getMapOfCountByKey(InventoryItem.class, columnName, "");

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Integer, Long> getUsedInventoryData() throws Exception {
        String sql = "select distinct(a.itemId) as id, i.invitInvCatTypeID as catType FROM  at_inventory_allocation a join at_inventory_item i" +

                " WHERE " +
                "i.invitID = a.itemId and " +
                "a.count > 0";

        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
        log.info(ps);
        ResultSet rs = ps.executeQuery();
        List<KeyValuePair<Long, Integer>> list = new ArrayList<>();
        while(rs.next()) {
            list.add(new KeyValuePair<>(rs.getLong("id"), rs.getInt("catType")));
        }
//        System.out.println(list.size());

//        list.forEach(System.out::println);



        return list.stream()
                .collect(Collectors.groupingBy(KeyValuePair::getValue, Collectors.counting()));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    DashboardCounterCard getBWUsageData() throws Exception {
        List<LLIConnectionInstance> listLLI = ServiceDAOFactory.getService(LLIConnectionService.class).getTotalLLIConnectionByClient(null);
        List<VPNNetworkLink> listVPN = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getTotalVPNLinkByClient(null);

        return DashboardCounterCard.getDashboardCounterCard(
                dashboardService.getBWUsage(listLLI, listVPN),
                DashboardConstants.TextClass.GREEN_SHARP.getTextClass(), "", "fa fa-money", ChartType.SimpleColumn
        );

    }
    @Transactional(transactionType = TransactionType.READONLY)
    JsonObject getApplicationPhaseData () {
        List<FlowState> flowStates = FlowRepository.getInstance().getActionableFlowStatesByRoleId(16021);
        System.out.println(flowStates.size());
        Map<KeyValuePair<String, Long>, Map<String, Long>> map = new HashMap<>();
        flowStates.stream()
                .collect(
                        Collectors.groupingBy(FlowState::getPhase,
                            Collectors.mapping(FlowState::getId, Collectors.toList())
                        )
                ).forEach((k,v)->{
                    long lliCount = getLLICount(v);
                    long nixCount = getNIXCount(v);
                    long vpnCount = getVPNCount(v);

                    long totalCount = lliCount+ nixCount +vpnCount;

                    KeyValuePair<String, Long> firstKey = new KeyValuePair<>(k, totalCount);
                    if(!map.containsKey(firstKey)){
                        map.put(firstKey, new HashMap<>());
                    }
                    Map<String, Long > innerMap = new HashMap<>();
                    innerMap.putIfAbsent("LLI", lliCount);
                    innerMap.putIfAbsent("NIX", nixCount);
                    innerMap.putIfAbsent("VPN", vpnCount);
                    map.put(firstKey, innerMap);


            });
        List<JsonObject> objects = dashboardDataBuilder.createBrokenPie(map);
        return dashboardService.createJson(objects, "Application Phase",0);

    }

    @Transactional(transactionType = TransactionType.READONLY)
    long getVPNCount(List<Integer> stateIds) {
        if(stateIds.isEmpty())return 0;
        return globalService.getCount(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .linkStateIn(
                                ApplicationState.getApplicationStatesByStateIds(stateIds)
                                        .stream()
                                        .map(Enum::name)
                                        .collect(Collectors.toList())
                        )
                        .getCondition()
        );
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long getNIXCount(List<Integer> stateIds) {
        if(stateIds.isEmpty())return 0;
        long count = globalService.getCount(NIXApplication.class,
                new NIXApplicationConditionBuilder()
                        .Where()
                        .stateIn(stateIds)
                        .getCondition()
        );

        count += globalService.getCount(NIXReviseDTO.class,
                new NIXReviseDTOConditionBuilder()
                        .Where()
                        .stateIn(stateIds)
                        .getCondition()
        );
        return count;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long getLLICount(List<Integer> stateIds) {
        if(stateIds.isEmpty()) return 0;
        long count = globalService.getCount(LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .stateIn(stateIds)
                        .getCondition()
        );
        count += globalService.getCount(ReviseDTO.class,
                new ReviseDTOConditionBuilder()
                        .Where()
                        .stateIn(stateIds)
                        .getCondition()

        );

        count += globalService.getCount(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .stateIn(stateIds)
                        .getCondition()
        );
        return count;
    }


    public static void main(String[] args) throws Exception {
        ServiceDAOFactory.getService(DashboardServiceServerRoom.class).getApplicationPhaseData();
    }


}
