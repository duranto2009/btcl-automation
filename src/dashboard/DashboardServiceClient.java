package dashboard;

import annotation.Transactional;
import application.*;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationDTOConditionBuilder;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.StringUtils;
import common.bill.BillDTO;
import common.bill.BillDTOConditionBuilder;
import common.repository.AllClientRepository;
import crm.CrmCommonPoolDTO;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseDTOConditionBuilder;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.Application.ownership.LLIOwnerShipChangeApplicationConditionBuilder;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import nix.application.NIXApplication;
import nix.application.NIXApplicationConditionBuilder;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseDTOConditionBuilder;
import requestMapping.Service;
import util.*;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class DashboardServiceClient {

    @Service private GlobalService globalService;
    @Service private DashboardDataBuilder dashboardDataBuilder;
    @Service private DashboardService dashboardService;


    JsonObject getCRMTicketDataForClient(long clientId) throws Exception {
        List<CrmCommonPoolDTO> crmCommonPoolDTOList = globalService.getAllObjectListByCondition(
                CrmCommonPoolDTO.class, " WHERE " + ModifiedSqlGenerator.getColumnName(CrmCommonPoolDTO.class, "clientID")
                        + " = " + clientId
        );

        List<KeyValuePair<String, Integer>> list = crmCommonPoolDTOList.stream()
                .collect(Collectors.groupingBy(CrmCommonPoolDTO::getStatus))

                .entrySet()
                .stream()
                .map(t-> new KeyValuePair<>(CrmCommonPoolDTO.getComplainStatusString(t.getKey()), t.getValue().size()))
                .collect(Collectors.toList());

        List<JsonObject>objects = dashboardDataBuilder.createSimpleColumn(list);
        return dashboardService.createJson(objects, "Total Tickets", crmCommonPoolDTOList.size());

    }

    List<DashboardCounterCard> getEntityDataForClient(long clientId) throws  Exception {
        List<LLIConnectionInstance> listLLI = ServiceDAOFactory.getService(LLIConnectionService.class).getTotalLLIConnectionByClient(clientId);
        List<VPNNetworkLink> listVPN = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getTotalVPNLinkByClient(clientId);
        List<NIXConnection> listNIX = ServiceDAOFactory.getService(NIXConnectionService.class).getTotalNIXConnectionCountByClient(clientId);
        List<CoLocationConnectionDTO> listCoLocation = ServiceDAOFactory.getService(CoLocationConnectionService.class).getTotalCoLocationConnectionCountByClient(clientId);

        int lliCount = listLLI.size();
        int vpnCount = listVPN.size();
        int nixCount = listNIX.size();
        int coLocationCount = listCoLocation.size();

        return Arrays.asList(
                DashboardCounterCard.getDashboardCounterCard(
                        getEntityCountForClient(lliCount, vpnCount, nixCount, coLocationCount),
                        DashboardConstants.TextClass.GREEN_SHARP.getTextClass(), "", "fa fa-chain", ChartType.SimplePie
                ),
                DashboardCounterCard.getDashboardCounterCard(
                        dashboardService.getBWUsage(listLLI, listVPN),
                        DashboardConstants.TextClass.RED_HAZE.getTextClass(), "", "fa fa-money", ChartType.SimpleColumn
                )

        );

    }

    JsonObject getLastPaymentDateDataForClient(long clientId) throws Exception {
        List<BillDTO> bills = globalService.getAllObjectListByCondition(
                BillDTO.class, new BillDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .paymentStatusEquals(BillDTO.UNPAID)
                        .getCondition()
        );

        List<BillDTO> sortedBills = bills.stream()
                .filter(t->t.getLastPaymentDate() > CurrentTimeFactory.getCurrentTime())
                .sorted(Comparator.comparing(BillDTO::getLastPaymentDate))
                .collect(Collectors.toList());


        List<JsonObject> objects = dashboardDataBuilder.createTimeline(


                sortedBills.stream()
                        .map(dashboardService::parseStringForBillTimeline)
                        .limit(5)
                        .collect(toList())

        );

        String value =  sortedBills.isEmpty() ? "No Unpaid Bills":
                TimeConverter.getDateTimeStringByMillisecAndDateFormat(sortedBills.get(0).getLastPaymentDate(), "dd/MM/yyyy");

        return dashboardService.createJson(objects, "Next Payment Dates", value);


    }

    JsonObject getApplicationDataForClient(long clientId) throws Exception {

        List<Integer> modules = AllClientRepository.getInstance().getRegisteredModulesByClientID(clientId);
        Map<Integer, Long> countMap = new HashMap<>();
        for(int module:modules) {
            if (module == ModuleConstants.Module_ID_LLI) {
                countMap = mergeMap(Arrays.asList(countMap, getCountLLI(clientId)));
            }else if(module == ModuleConstants.Module_ID_NIX) {
                countMap = mergeMap(Arrays.asList(countMap, getCountNIX(clientId)));
            }else if (module == ModuleConstants.Module_ID_COLOCATION) {
                countMap = mergeMap(Arrays.asList(countMap, getCountCoLocation(clientId)));
            }else if(module == ModuleConstants.Module_ID_VPN) {
                countMap = mergeMap(Arrays.asList(countMap, getCountVPN(clientId)));
            }
        }


        Map<ApplicationPhaseClient, List<Integer> >mapOfFlowStateIdsToAppPhaseClient = new HashMap<>();
        Map<String, List<FlowState>> mapOfStatesToPhase = ServiceDAOFactory.getService(FlowService.class).flowStateGroupByPhase(0);
        mapOfStatesToPhase.forEach((key, value) -> {
            List<Integer> flowStateIds = value
                    .stream()
                    .map(FlowState::getId)
                    .collect(toList());
            ApplicationPhase applicationPhase = ApplicationPhase.getApplicationPhase(key);
            ApplicationPhaseClient applicationPhaseClient = ApplicationPhase.getApplicationPhaseClientByApplicationPhase(applicationPhase);
            if (applicationPhaseClient != null) {
                mapOfFlowStateIdsToAppPhaseClient.put(applicationPhaseClient, flowStateIds);
            }
        });


        Map<ApplicationPhaseClient, Long> countMapForAppPhaseClient = new HashMap<>();
        for(Map.Entry<ApplicationPhaseClient, List<Integer>> entry: mapOfFlowStateIdsToAppPhaseClient.entrySet()) {
            ApplicationPhaseClient applicationPhaseClient = entry.getKey();
            List<Integer> flowStateIds = entry.getValue();
            long sum = 0;
            for (Integer flowStateId : flowStateIds) {
                sum += countMap.getOrDefault(flowStateId, 0L);
            }
            countMapForAppPhaseClient.putIfAbsent(applicationPhaseClient, sum);
        }



        List<KeyValuePair<String, Long>> list = countMapForAppPhaseClient.entrySet()
                .stream()
                .map(t-> new KeyValuePair<>(t.getKey().getTitle(), t.getValue())).collect(Collectors.toList());

        List<JsonObject> jsonObjects = dashboardDataBuilder.createSimpleColumn(list);

        return dashboardService.createJson(jsonObjects, "Application", 0);
    }

    JsonObject getTotalBillDataForClientForceDirected(long clientId) throws Exception {
        Map<String, Map<String, Map<String, Double>>> map = getBillDataForClient(clientId);
        List<JsonObject> jsonObjectsModule = new ArrayList<>();
        double total = 0;
        for(Map.Entry< String, Map<String, Map<String, Double> > > firstEntry: map.entrySet()) {
            Map<String, Map<String, Double>> secondMap = firstEntry.getValue();
            String module = firstEntry.getKey();

            double moduleWiseSum = 0;
            List<JsonObject> jsonObjectsType = new ArrayList<>();
            for (Map.Entry<String, Map<String, Double>> secondEntry : secondMap.entrySet()) {
                double billTypeWiseSum = 0;
                String billType = secondEntry.getKey();
                Map<String, Double> thirdMap = secondEntry.getValue();
                List<JsonObject> jsonObjectsStatus = new ArrayList<>();
                for (Map.Entry<String, Double> thirdEntry : thirdMap.entrySet()) {
                    String status = thirdEntry.getKey();
                    double sum = thirdEntry.getValue();

                    total += sum;
                    moduleWiseSum += sum;
                    billTypeWiseSum += sum;

                    JsonObject jsonObjectStatus = new JsonObject();
                    jsonObjectStatus.addProperty("name", status);
                    jsonObjectStatus.addProperty("value", sum);
                    jsonObjectsStatus.add(jsonObjectStatus);

                }


                JsonArray jsonArray = new JsonArray();
                jsonObjectsStatus.forEach(jsonArray::add);

                JsonObject jsonObjectType = new JsonObject();
                jsonObjectType.addProperty("name", billType);
                jsonObjectType.add("children", jsonArray);


                jsonObjectsType.add(jsonObjectType);
            }
            JsonArray jsonArray = new JsonArray();
            jsonObjectsType.forEach(jsonArray::add);

            JsonObject jsonObjectModule = new JsonObject();
            jsonObjectModule .addProperty("name", module);
            jsonObjectModule .add("children", jsonArray);


            jsonObjectsModule.add(jsonObjectModule);

        }


        return  dashboardService.createJson(jsonObjectsModule, "Bills", 0);

    }

    @Deprecated
    JsonObject getTotalBillDataForClient(long clientId) throws Exception {
        Map<String, Map<String, Map<String, Double>>> map = getBillDataForClient(clientId);

        List<JsonObject> jsonObjects = new ArrayList<>();
        double total = 0;
        for(Map.Entry< String, Map<String, Map<String, Double> > > firstEntry: map.entrySet()) {
            Map<String, Map<String, Double>> secondMap = firstEntry.getValue();
            String module = firstEntry.getKey();

            double moduleWiseSum = 0;
            for(Map.Entry<String, Map<String, Double>> secondEntry : secondMap.entrySet()) {
                double billTypeWiseSum = 0;
                String billType = secondEntry.getKey();
                Map<String, Double> thirdMap = secondEntry.getValue();
                for(Map.Entry<String, Double> thirdEntry : thirdMap.entrySet()) {
                    String status = thirdEntry.getKey();
                    double sum = thirdEntry.getValue();

                    total += sum;
                    moduleWiseSum += sum;
                    billTypeWiseSum += sum;

                    String labelText = status+" [bold]" + sum + "[/]";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("from", billType );
                    jsonObject.addProperty("to", status);
                    jsonObject.addProperty("value", sum);
                    jsonObject.addProperty("color", "#00aea0");
                    jsonObject.addProperty("labelText", labelText);

                    jsonObjects.add(jsonObject);
                }
                String labelText =  billType+" [bold]" + billTypeWiseSum + "[/]";
                JsonObject jsonObjectBillType = new JsonObject();
                jsonObjectBillType.addProperty("from", module );
                jsonObjectBillType.addProperty("to", billType );
                jsonObjectBillType.addProperty("color", "#5ea9e1" );
                jsonObjectBillType.addProperty("zIndex", "101" );
                jsonObjectBillType.addProperty("value", billTypeWiseSum);
                jsonObjectBillType.addProperty("labelText",labelText);
                jsonObjects.add(jsonObjectBillType);
            }
            String labelText = module+" [bold]" + moduleWiseSum + "[/]";
            JsonObject jsonObjectModule = new JsonObject();
            jsonObjectModule.addProperty("from", "Total Amount" );
            jsonObjectModule.addProperty("to", module );
            jsonObjectModule.addProperty("color", "#5ea9e1" );
            jsonObjectModule.addProperty("value", moduleWiseSum);
            jsonObjectModule.addProperty("labelText", labelText);
            jsonObjects.add(jsonObjectModule);
        }

//        String labelText = "Total Bills Breakdown"+" [bold]" + total + "[/]";
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("from", "Source" );
//        jsonObject.addProperty("to", "Total Amount" );
//        jsonObject.addProperty("color", "#5ea9e1" );
//        jsonObject.addProperty("zIndex", "100" );
//        jsonObject.addProperty("labelText", labelText);
//        jsonObject.addProperty("value", total);
//
//        jsonObjects.add(jsonObject);

        return dashboardService.createJson(jsonObjects, "Bills", 0);
    }

    private JsonObject getEntityCountForClient(int lliCount, int vpnCount, int nixCount, int coLocationCount) {
        List<JsonObject> objects = new ArrayList<>();


        JsonObject jsonObjectLLI = new JsonObject();
        jsonObjectLLI.addProperty("category", "LLI");
        jsonObjectLLI.addProperty("value", lliCount);
        objects.add(jsonObjectLLI);


        JsonObject jsonObjectNIX = new JsonObject();
        jsonObjectNIX.addProperty("category", "NIX");
        jsonObjectNIX.addProperty("value", nixCount);
        objects.add(jsonObjectNIX);

        JsonObject jsonObjectVPN = new JsonObject();
        jsonObjectVPN.addProperty("category", "VPN");
        jsonObjectVPN.addProperty("value", vpnCount);
        objects.add(jsonObjectVPN);

        JsonObject jsonObjectCoLocation = new JsonObject();
        jsonObjectCoLocation.addProperty("category", "CoLocation");
        jsonObjectCoLocation.addProperty("value", coLocationCount);
        objects.add(jsonObjectCoLocation);

        return dashboardService.createJson(objects, "Entity", lliCount + nixCount + vpnCount + coLocationCount);
    }



    private Map<Integer, Long> mergeMap(List<Map<Integer, Long>> maps) {
        Map<Integer, Long> map = new HashMap<>();
        for (Map<Integer, Long> integerLongMap : maps) {
            integerLongMap.forEach((key, value)->{
                long count = map.getOrDefault(key, 0L);
                map.put(key, count + value);
            });
        }
        return map;
    }

    private Map<Integer, Long> getCountCoLocation(long clientId) throws Exception {
        return globalService.getAllObjectListByCondition(CoLocationApplicationDTO.class,
                new CoLocationApplicationDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .getCondition()
        ).stream()
                .collect(groupingBy(CoLocationApplicationDTO::getState, counting()));
    }

    private Map<Integer, Long> getCountNIX(long clientId ) throws Exception {
        Map<Integer, Long> mapNIXApplication = globalService.getAllObjectListByCondition(
                NIXApplication.class,
                new NIXApplicationConditionBuilder()
                        .Where()
                        .clientEquals(clientId)
                        .getCondition()
        ).stream()
                .collect(groupingBy(NIXApplication::getState, counting()));

        Map<Integer, Long> mapNIXReviseApplication = globalService.getAllObjectListByCondition(
                NIXReviseDTO.class,
                new NIXReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .getCondition()
        ).stream()
                .collect(groupingBy(t->(int)t.getState(), counting()));

        return mergeMap(Arrays.asList(mapNIXApplication, mapNIXReviseApplication));
    }

    private Map<Integer, Long> getCountVPN(long clientId ) throws Exception {
        List<Long> appIds = globalService.getAllObjectListByCondition(
                Application.class, new ApplicationConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .moduleIdEquals(ModuleConstants.Module_ID_VPN)
                        .Or(
                                new ApplicationConditionBuilder()

                                        .secondClientEquals(clientId)
                                        .getSqlPair()
                        ).getCondition()
        ).stream()
                .map(Application::getApplicationId)
                .collect(toList());

        if(appIds.isEmpty()) return Collections.emptyMap();

        List<Long> vpnApplicationIds = globalService.getAllObjectListByCondition(
                VPNApplication.class, " WHERE " + SqlGenerator.getForeignKeyColumnName(VPNApplication.class)
                        + " IN " + StringUtils.getCommaSeparatedString(appIds)
        ).stream()
                .map(VPNApplication::getVpnApplicationId)
                .collect(toList());


        if(vpnApplicationIds.isEmpty())return Collections.emptyMap();
        return globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdIn(vpnApplicationIds)
                        .getCondition()
        ).stream()
                .map(VPNApplicationLink::getLinkState)
                .map(ApplicationState::getState)
                .collect(groupingBy(Function.identity(), counting()));

    }

    private Map<Integer, Long> getCountLLI(long clientId ) throws Exception {
        Map<Integer, Long> mapLLIApplicationCount = globalService.getAllObjectListByCondition(
                LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .getCondition()
        ).stream()
                .collect(groupingBy(LLIApplication::getState, counting()));

        Map<Integer, Long>mapLLIReviseCount = globalService.getAllObjectListByCondition(
                ReviseDTO.class,
                new ReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .getCondition()
        ).stream()
                .collect(groupingBy(t->(int)t.getState(), counting()));

        Map<Integer, Long>mapOwnerCount = globalService.getAllObjectListByCondition(
                LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .srcClientEquals(clientId)
                        .Or(
                                new LLIOwnerShipChangeApplicationConditionBuilder()
                                        .dstClientEquals(clientId)
                                        .getSqlPair()
                        )
                        .getCondition()
        ).stream()
                .collect(groupingBy(LLIOwnerShipChangeApplication::getState, counting()));

        return mergeMap(Arrays.asList(mapLLIApplicationCount, mapLLIReviseCount, mapOwnerCount));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    Map<String, Map<String, Map<String, Double>>> getBillDataForClient(long clientId) throws Exception {
        BillDTOConditionBuilder billDTOConditionBuilder = new BillDTOConditionBuilder();
        String selectPart = "SELECT SUM(" + billDTOConditionBuilder.getNetPayableColumnName()
                + ") as sum, " + billDTOConditionBuilder.getEntityTypeIDColumnName() + " as entityType, "
                + billDTOConditionBuilder.getBillTypeColumnName() + " as type, "
                + billDTOConditionBuilder.getPaymentStatusColumnName() + " as status";

        String groupByPart = " GROUP BY entityType, type, status";
        SqlPair sqlPair = billDTOConditionBuilder
                .fromBillDTO()
                .Where()
                .clientIDEquals(clientId)
                .getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql + groupByPart;

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        Map<String, Map<String, Map<String, Double>>> map = new HashMap<>();
        while(rs.next()) {
            double sum = rs.getDouble("sum");
            String entityType = BillDTO.getModule(rs.getInt("entityType"));
            String billType = BillDTO.getBillTypeStr(rs.getInt("type"));
            String status = BillDTO.getStatus(rs.getInt("status"));
            Map<String, Map<String, Double>> firstMap = map.getOrDefault(entityType, new HashMap<>());
            Map<String, Double>  secondMap = firstMap.getOrDefault(billType, new HashMap<>());
            secondMap.putIfAbsent(status, sum);
            firstMap.put(billType, secondMap);
            map.put(entityType, firstMap);
        }
        return map;
    }

}
