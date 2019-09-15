package dashboard;

import annotation.Transactional;
import application.ApplicationConditionBuilder;
import client.RegistrantTypeConstants;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillDTOConditionBuilder;
import common.repository.AllClientRepository;
import global.GlobalService;
import lli.LLIConnectionService;
import lli.outsourceBill.LLIMonthlyOutsourceBillService;
import nix.connection.NIXConnectionService;
import nix.outsourcebill.NIXMonthlyOutsourceBillService;
import org.joda.time.LocalDate;
import permission.PermissionRepository;
import requestMapping.Service;
import role.RoleDTO;
import user.UserDTO;
import user.UserRepository;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.monthlyOutsourceBill.VPNMonthlyOutsourceBillService;
import vpn.network.VPNNetworkLinkService;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class DashboardServiceCDGM {

    @Service private DashboardService dashboardService;
    @Service private DashboardDataBuilder dashboardDataBuilder;
    @Service private GlobalService globalService;

    @Transactional(transactionType = TransactionType.READONLY)
    JsonObject getApplicationData() throws Exception {
        ApplicationConditionBuilder applicationConditionBuilder = new ApplicationConditionBuilder();
        String selectPart = "select count(*) as sum, " + applicationConditionBuilder.getModuleIdColumnName() + " as module, " +
                "DATE_FORMAT( " +
                " FROM_UNIXTIME( "  + applicationConditionBuilder.getSubmissionDateColumnName()+"/1000)  " +
                ",'%Y'" +
                ") as period";


        String groupByPart = " GROUP BY module, period ";
        SqlPair sqlPair = applicationConditionBuilder
                .fromApplication()
                .getSqlPair();
        sqlPair.sql = selectPart + sqlPair.sql + groupByPart;
        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        Map<String, Map<String, Integer>> map = new HashMap<>();
        LocalDate now = LocalDate.now();
        String currentYear = String.valueOf(now.getYear());
        String prevYear = String.valueOf(now.minusYears(1).getYear());
        String prevPrevYear = String.valueOf(now.minusYears(2).getYear());
        while(rs.next()) {
            int moduleId = rs.getInt("module");
            String period = rs.getString("period");
            int count = rs.getInt("sum");

            String moduleName = ModuleConstants.ActiveModuleMap.getOrDefault(moduleId, "N/A");



            Map<String, Integer> map2 = map.getOrDefault(moduleName, new HashMap<>());


            map2.putIfAbsent(currentYear, 0);
            map2.putIfAbsent(prevYear, 0);
            map2.putIfAbsent(prevPrevYear, 0);

            map2.put(period, count);
            map.put(moduleName, map2);

        }
        List<JsonObject> objects = map.entrySet()
                .stream()
                .map(t-> {
                    String categoryName = t.getKey();
                    Map<String, Integer> values = t.getValue();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("category", categoryName);
                    for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet()) {
                        jsonObject.addProperty("value_" + stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                    }
                    return jsonObject;
                }).collect(Collectors.toList());
        return dashboardService.createJson(objects, "Application", 0);
    }

    JsonObject getEntityDataForCDGM() throws Exception {
        List<JsonObject> objects = new ArrayList<>();


        JsonObject jsonObjectLLI = new JsonObject();
        jsonObjectLLI.addProperty("category", "LLI");
        jsonObjectLLI.addProperty("value", ServiceDAOFactory.getService(LLIConnectionService.class).getTotalLLIConnectionCount());
        objects.add(jsonObjectLLI);


        JsonObject jsonObjectNIX = new JsonObject();
        jsonObjectNIX.addProperty("category", "NIX");
        jsonObjectNIX.addProperty("value", ServiceDAOFactory.getService(NIXConnectionService.class).getTotalNIXConnectionCount());
        objects.add(jsonObjectNIX);

        JsonObject jsonObjectVPN = new JsonObject();
        jsonObjectVPN.addProperty("category", "VPN");
        jsonObjectVPN.addProperty("value", ServiceDAOFactory.getService(VPNNetworkLinkService.class).getTotalVPNLinkCount());
        objects.add(jsonObjectVPN);


        return dashboardService.createJson(objects, "Entity", 0);

    }

    JsonObject getActiveUsersData() {
        Collection<RoleDTO> roles = PermissionRepository.getInstance().getAllRoleDTOs();
        int totalActiveUsers = 0;

        List<KeyValuePair<String, Integer>> data = new ArrayList<>();
        for (RoleDTO role : roles) {
            Set<UserDTO> usersForThisRole = UserRepository.getInstance().getUsersByRoleID(role.getRoleID());
            int count = usersForThisRole!=null ?usersForThisRole.size() : 0;
            totalActiveUsers += count;
            data.add(new KeyValuePair<>(role.getRoleName(), count));
        }

        List<JsonObject> objects = dashboardDataBuilder.createSimpleColumn(data);
        return dashboardService.createJson(objects, "Total Active Users", totalActiveUsers);

    }

    JsonObject getOutsourceBillData() throws Exception {
        Map<Long, Double> map = new HashMap<>();
        ServiceDAOFactory.getService(LLIMonthlyOutsourceBillService.class).getAggregatedBillByVendors(map);
        ServiceDAOFactory.getService(NIXMonthlyOutsourceBillService.class).getAggregatedBillByVendors(map);
        ServiceDAOFactory.getService(VPNMonthlyOutsourceBillService.class).getAggregatedBillByVendors(map);

        List<KeyValuePair<String, Double> > data = map.entrySet().stream()
                .map(t-> new KeyValuePair<>(UserRepository.getInstance().getUserDTOByUserID(t.getKey()).getUsername(), t.getValue()))
                .collect(toList());

        double total = data.stream().mapToDouble(KeyValuePair::getValue).sum();
        List<JsonObject> objects = dashboardDataBuilder.createSimpleColumn(data);
        return dashboardService.createJson(objects, "Total O/C Bill", dashboardService.soothingValue(total));

    }

    @Transactional
    JsonObject getDemandNoteData() throws Exception {
        Map<KeyValuePair<String, Double>, Map<String, Double>> map =
                getMapOfTotalGeneratedDemandNoteToPaymentTypeToModule()
                        .entrySet()
                        .stream()
                        .map(t->{
                            double total = t.getValue()
                                    .values()
                                    .stream()
                                    .mapToDouble(d->d)
                                    .sum();
                            return new KeyValuePair<>(new KeyValuePair<>(t.getKey(), total), t.getValue());

                        })
                        .collect(Collectors.toMap(KeyValuePair::getKey, KeyValuePair::getValue));

        double totalAmount = map.keySet()
                .stream()
                .mapToDouble(KeyValuePair::getValue)
                .sum();
        List<JsonObject> objects = dashboardDataBuilder.createBrokenPie(map);
        return dashboardService.createJson(objects, "Generated DN", dashboardService.soothingValue(totalAmount));

    }

    JsonObject getActiveClientData (){
        Map<Integer, Set<ClientDetailsDTO>> map = AllClientRepository.getInstance().getModuleWiseClientStat();

        int total = map.values().stream().mapToInt(Set::size).sum();
        List<JsonObject> objects= map.entrySet()
                .stream()
                .map(t-> {
                    JsonObject jsonObject = new JsonObject();
                    String moduleName = ModuleConstants.ActiveModuleMap.getOrDefault(t.getKey(), "N/A");
                    jsonObject.addProperty("category", moduleName);
                    Map<Integer, List<ClientDetailsDTO> >map2 =
                            t.getValue()
                                    .stream()
                                    .collect(
                                            Collectors.groupingBy(
                                                    ClientDetailsDTO::getRegistrantType
                                            )
                                    );


                    Map<String, java.lang.Number> map3 = new HashMap<>();
                    RegistrantTypeConstants.RegistrantTypeName.forEach((key, value) ->
                            map3.putIfAbsent("value_"+value , map2.getOrDefault(key, new ArrayList<>()).size())
                    );

                    for(Map.Entry <String, Number> entry : map3.entrySet()) {

                        jsonObject.addProperty(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    return jsonObject;



                }).collect(Collectors.toList());

        return dashboardService.createJson(objects, "Active Client", total);


    }

    private Map<String, Map<String, Double>> getMapOfTotalGeneratedDemandNoteToPaymentTypeToModule() throws Exception {
        List<BillDTO> bills = globalService.getAllObjectListByCondition(
                BillDTO.class, new BillDTOConditionBuilder()
                        .Where()
                        .billTypeEquals(BillConstants.DEMAND_NOTE)
                        .getCondition()
        );
        return bills.stream()
                .collect(Collectors.groupingBy(bill->ModuleConstants.ActiveModuleMap.getOrDefault((bill.getEntityTypeID()/100), "N/A")
                        ,
                        Collectors.groupingBy(bill->BillDTO.demandNoteStatusMap.getOrDefault(bill.getPaymentStatus(), "N/A")
                                ,
                                collectingAndThen(Collectors.summingDouble(BillDTO::getNetPayable), t->t)
                        )
                        )
                )


                ;
    }
}
