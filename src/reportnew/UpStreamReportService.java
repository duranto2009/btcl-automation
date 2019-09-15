package reportnew;

import annotation.Transactional;
import com.google.gson.internal.LinkedHashTreeMap;
import common.RequestFailureException;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import login.LoginDTO;
import requestMapping.Service;
import upstream.application.UpstreamApplication;
import upstream.application.UpstreamApplicationConditionBuilder;
import upstream.contract.UpstreamContract;
import upstream.inventory.UpstreamInventoryItem;
import upstream.inventory.UpstreamInventoryService;
import util.TransactionType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static util.SqlGenerator.getColumnName;

public class UpStreamReportService {

    @Service
    GlobalService globalService;
    @Service
    UpstreamInventoryService upstreamInventoryService;
    //region Upstream

    @Transactional(transactionType = TransactionType.READONLY)
    public Report getReportBatchOperationForUpStreamApplication(PostEntity postEntity, LoginDTO loginDTO) throws Exception{
        Map<Long, UpstreamInventoryItem> upstreamInventoryMap = upstreamInventoryService.getAllUpstreamItemsMapById();
        HashMap<String,String> searchCriteria = new HashMap<>();
        List<FlowState> flowStates = new ArrayList<>();
        postEntity.getSelectedSearchCriteria().forEach(s->{
            if(s.getType().equals("list")){
                if(s.getInput()!=null){
                    LinkedHashTreeMap<String,?> hashMap = (LinkedHashTreeMap<String, ?>) s.getInput();
                    Double sID= (Double) hashMap.get("ID");
                    if (s.getName().equals("Bandwidth Type")) {
                        searchCriteria.put("Bandwidth Type", sID.toString());
                    }
                    else if(s.getName().equals("Service Location")){
                        searchCriteria.put("Service Location",sID.toString());
                    }
                    else if(s.getName().equals("Provider")){
                        searchCriteria.put("Provider",sID.toString());
                    }
                    else if(s.getName().equals("Provider")){
                        searchCriteria.put("Provider",sID.toString());
                    }
                    else if(s.getName().equals("Application Type")){
                        searchCriteria.put("Application Type",sID.toString());
                    }
                    else if(s.getName().equals("Media")){
                        searchCriteria.put("Media",sID.toString());
                    }
                    else if(s.getName().equals("Application Phase")){
                        List<LinkedHashTreeMap<String,Object>> list = (List<LinkedHashTreeMap<String, Object>>) hashMap.get("object");
                        list.forEach(eachmap->{
                            FlowState flowState = new FlowState();
                            Long s1=Math.round((Double) eachmap.get("id"));
                            flowState.setId(Math.toIntExact(s1));
                            flowState.setName((String)eachmap.get("name"));
                            flowState.setViewDescription((String)eachmap.get("viewDescription"));
                            flowStates.add(flowState);
                        });
                        searchCriteria.put("Application Phase",sID.toString());
                    }
                }
            }
            else if(s.getType().equals("input")){
                if(s.getInput()!=null){
                    String sInput = (String) s.getInput();
                    if(s.getName().equals("Application Id")){
                        searchCriteria.put("Application Id",sInput);
                    }
                    else if(s.getName().equals("Bandwidth Price")){
                        searchCriteria.put("Bandwidth Price",sInput);
                    }
                    else if(s.getName().equals("Bandwidth Capacity")){
                        searchCriteria.put("Bandwidth Capacity",sInput);
                    }
                }
            }
            else if(s.getType().equals("date")){
                if(s.getInput1()!=null&&s.getInput2()!=null){
                    Double d1 = (Double) s.getInput1();
                    Double d2 = (Double) s.getInput2();
                    if(s.getName().equals("Application Date")){
                        searchCriteria.put("Created From",d1.toString());
                        searchCriteria.put("Created To",d2.toString());
                    }
                    else if(s.getName().equals("Srf Date")){
                        searchCriteria.put("Srf From",d1.toString());
                        searchCriteria.put("Srf To",d2.toString());
                    }
                }
            }
        });
        Class<UpstreamApplication> classObject = UpstreamApplication.class;
        String conditionalString = " where 1=1 ";
        if (searchCriteria.get("Application Id") != null) {
            conditionalString += " and " + getColumnName(classObject, "applicationId") + " = " + (long) Double.parseDouble(searchCriteria.get("Application Id")) + " ";
        }
        if (searchCriteria.get("Bandwidth Capacity") != null) {
            conditionalString += " and " + getColumnName(classObject, "bandwidthCapacity") + " > " + Double.parseDouble(searchCriteria.get("Bandwidth Capacity")) + " ";
        }
        if (searchCriteria.get("Bandwidth Price") != null) {
            conditionalString += " and " + getColumnName(classObject, "bandwidthPrice") + " > " +  Double.parseDouble(searchCriteria.get("Bandwidth Price")) + " ";
        }
        if (searchCriteria.get("Bandwidth Type") != null) {
            conditionalString += " and " + getColumnName(classObject, "typeOfBandwidthId") + " > " + (long) Double.parseDouble(searchCriteria.get("Bandwidth Type")) + " ";
        }
        if (searchCriteria.get("Media") != null) {
            conditionalString += " and " + getColumnName(classObject, "mediaId") + " > " + (long) Double.parseDouble(searchCriteria.get("Media")) + " ";
        }
        if (searchCriteria.get("Service Location") != null) {
            conditionalString += " and " + getColumnName(classObject, "btclServiceLocationId") + " > " + (long) Double.parseDouble(searchCriteria.get("Service Location")) + " ";
        }
        if (searchCriteria.get("Provider Location") != null) {
            conditionalString += " and " + getColumnName(classObject, "providerLocationId") + " > " + (long) Double.parseDouble(searchCriteria.get("Provider Location")) + " ";
        }
        if (searchCriteria.get("Provider") != null) {
            conditionalString += " and " + getColumnName(classObject, "selectedProviderId") + " > " + (long) Double.parseDouble(searchCriteria.get("Provider")) + " ";
        }
        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(classObject, conditionalString);
        if (searchCriteria.get("Application Phase") != null) {
            if(flowStates.size()>0){
                applications=getUpStreamApplicationListOfStateGroup(flowStates,applications);
            }
        }
        List<UpstreamApplication> finalApplications = applications;
        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Application Id")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getApplicationId));
            } else if (t.getName().equals("Application State")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getState));
            }
            else if (t.getName().equals("Bandwidth Capacity")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(UpstreamApplication::getBandwidthCapacity));
            }
            else if (t.getName().equals("Bandwidth Price")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(UpstreamApplication::getBandwidthPrice));
            }
            else if (t.getName().equals("Bandwidth Type")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getTypeOfBandwidthId));
            }
            else if (t.getName().equals("Media")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getMediaId));
            }
            else if (t.getName().equals("Service Location")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getBtclServiceLocationId));
            }
            else if (t.getName().equals("Provider Location")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getProviderLocationId));
            }
            else if (t.getName().equals("Provider")) {
                Collections.sort(finalApplications, Comparator.comparingLong(UpstreamApplication::getSelectedProviderId));
            }
        });
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (finalApplications.size() > 0) {
            finalApplications.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Application Id")) {
                        hashMap.put("ID", m.getApplicationId());
                    }
                    else if(n.getName().equals("Service Location")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getBtclServiceLocationId());
                        if(inventoryItem!=null){
                            String serviceLocation = inventoryItem.getItemName();
                            hashMap.put("Service Location",m.getBtclServiceLocationId());
                        }
                        else hashMap.put("Service Location","N/A");
                    }
                    else if(n.getName().equals("Provider")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getSelectedProviderId());
                        if(inventoryItem!=null){
                            String provider = inventoryItem.getItemName();
                            hashMap.put("Provider",provider);
                        }
                        else hashMap.put("Provider","N/A");
                    }
                    else if(n.getName().equals("Provider Location")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getProviderLocationId());
                        if(inventoryItem!=null){
                            String providerLocation = inventoryItem.getItemName();
                            hashMap.put("Provider Location",providerLocation);
                        }
                        else hashMap.put("Provider Location","N/A");
                    }
                    else if(n.getName().equals("Media")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getMediaId());
                        if(inventoryItem!=null){
                            String media = inventoryItem.getItemName();
                            hashMap.put("Media",media);
                        }
                        else hashMap.put("Media","N/A");
                    }
                    else if(n.getName().equals("Bandwidth Price")){
                        hashMap.put("Bandwidth Price",m.getBandwidthPrice());
                    }
                    else if(n.getName().equals("Bandwidth Capacity")){
                        hashMap.put("Bandwidth Capacity",m.getBandwidthCapacity());
                    }
                    else if (n.getName().equals("Srf Date")) {
                        long val = m.getSrfDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Srf From", dateText);
                    }
                    else if (n.getName().equals("Application State")) {
                        if(m.getState()>0){
                            hashMap.put("Application State", FlowRepository.getInstance().getFlowStateByFlowStateId((int) m.getState()).getViewDescription());
                        }
                    } else if (n.getName().equals("Application Date")) {
                        long val = m.getApplicationDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Application date", dateText);
                    }
                });
                mapList.add(hashMap);
            });
        }
        if(finalApplications.size()== 0 ){
            throw  new RequestFailureException("No Data Found");
        }
        Report nixApplicationReport = new Report();
        nixApplicationReport.setMapArrayList(mapList);
        nixApplicationReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return nixApplicationReport;
    }

    @Transactional
    private List<UpstreamApplication> getUpStreamApplicationListOfStateGroup(List<FlowState> flowStates, List<UpstreamApplication> applications) throws Exception{
        List<UpstreamApplication> filteredApplication=new ArrayList<>();
        List<UpstreamApplication> upstreamApplications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .stateIn(flowStates.stream().map(p->p.getId()).collect(Collectors.toList()))
                        .getCondition()
        );
        Map<Long,UpstreamApplication> mappedStateWiseApp = upstreamApplications
                .stream()
                .collect(Collectors.toMap(UpstreamApplication::getApplicationId, Function.identity()));
        applications.forEach(
                t -> {
                    UpstreamApplication upstreamApplication = mappedStateWiseApp.getOrDefault(t.getApplicationId(), null);
                    if (upstreamApplication != null) {
                        filteredApplication.add(t);
                    }
                }
        );
        return filteredApplication;
    }
    public Report getReportBatchOperationForUpStreamConnection(PostEntity postEntity, LoginDTO loginDTO) throws Exception{
        Map<Long, UpstreamInventoryItem> upstreamInventoryMap = upstreamInventoryService.getAllUpstreamItemsMapById();
        HashMap<String, String> searchCriteria = new HashMap<>();
        postEntity.getSelectedSearchCriteria().forEach(s -> {
            if (s.getType().equals("list")) {
                if (s.getInput() != null) {
                    LinkedHashTreeMap<String, Double> hashMap = (LinkedHashTreeMap<String, Double>) s.getInput();
                    Double sID = hashMap.get("ID");
                    if (s.getName().equals("Bandwidth Type")) {
                        searchCriteria.put("Bandwidth Type", sID.toString());
                    }
                    else if(s.getName().equals("Service Location")){
                        searchCriteria.put("Service Location",sID.toString());
                    }
                    else if(s.getName().equals("Provider")){
                        searchCriteria.put("Provider",sID.toString());
                    }
                    else if(s.getName().equals("Provider Location")){
                        searchCriteria.put("Provider Location",sID.toString());
                    }
                    else if(s.getName().equals("Application Type")){
                        searchCriteria.put("Application Type",sID.toString());
                    }
                    else if(s.getName().equals("Media")){
                        searchCriteria.put("Media",sID.toString());
                    }
                }
            } else if (s.getType().equals("input")) {
                if (s.getInput() != null) {
                    String sInput = (String) s.getInput();
                    if (s.getName().equals("Contract Name")) {
                        searchCriteria.put("Contract Name", sInput);
                    }
                    else if(s.getName().equals("Bandwidth Price")){
                        searchCriteria.put("Bandwidth Price",sInput);
                    }
                    else if(s.getName().equals("Bandwidth Capacity")){
                        searchCriteria.put("Bandwidth Capacity",sInput);
                    }
                }
            } else if (s.getType().equals("date")) {
                if (s.getInput1() != null && s.getInput2() != null) {
                    Double d1 = (Double) s.getInput1();
                    Double d2 = (Double) s.getInput2();
                    if (s.getName().equals("Active Date")) {
                        searchCriteria.put("Active From", d1.toString());
                        searchCriteria.put("Active To", d2.toString());
                    }
                    else if (s.getName().equals("Srf Date")) {
                        searchCriteria.put("Srf From", d1.toString());
                        searchCriteria.put("Srf To", d2.toString());
                    }
                }
            }
        });
        Class<UpstreamContract> classObject = UpstreamContract.class;
        String conditionalString = " where 1=1 ";
        // TODO: 4/15/2019 the below conditional string need to change
        if (searchCriteria.get("Contract Name") != null) {
            conditionalString += " and " + getColumnName(classObject, "contractName") + " like '%" + searchCriteria.get("Contract Name") + "%' ";
        }
        if (searchCriteria.get("Active From") != null && searchCriteria.get("Active To") != null) {
            conditionalString += " and " + getColumnName(classObject, "activeFrom") + " > " + (long) Double.parseDouble(searchCriteria.get("Active From")) + " "
                    + " and " + getColumnName(classObject, "activeTo") + " < " + (long) Double.parseDouble(searchCriteria.get("Active To")) + " ";
        }
        if (searchCriteria.get("Bandwidth Capacity") != null) {
            conditionalString += " and " + getColumnName(classObject, "bandwidthCapacity") + " > " + Double.parseDouble(searchCriteria.get("Bandwidth Capacity")) + " ";
        }
        if (searchCriteria.get("Bandwidth Price") != null) {
            conditionalString += " and " + getColumnName(classObject, "bandwidthPrice") + " > " +  Double.parseDouble(searchCriteria.get("Bandwidth Price")) + " ";
        }
        if (searchCriteria.get("Bandwidth Type") != null) {
            conditionalString += " and " + getColumnName(classObject, "typeOfBandwidthId") + " > " + (long) Double.parseDouble(searchCriteria.get("Bandwidth Type")) + " ";
        }
        if (searchCriteria.get("Media") != null) {
            conditionalString += " and " + getColumnName(classObject, "mediaId") + " > " + (long) Double.parseDouble(searchCriteria.get("Media")) + " ";
        }
        if (searchCriteria.get("Service Location") != null) {
            conditionalString += " and " + getColumnName(classObject, "btclServiceLocationId") + " > " + (long) Double.parseDouble(searchCriteria.get("Service Location")) + " ";
        }
        if (searchCriteria.get("Provider Location") != null) {
            conditionalString += " and " + getColumnName(classObject, "providerLocationId") + " > " + (long) Double.parseDouble(searchCriteria.get("Provider Location")) + " ";
        }
        if (searchCriteria.get("Provider") != null) {
            conditionalString += " and " + getColumnName(classObject, "selectedProviderId") + " > " + (long) Double.parseDouble(searchCriteria.get("Provider")) + " ";
        }
        List<UpstreamContract>upstreamContracts = globalService.getAllObjectListByCondition(classObject,conditionalString);
        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Contract Name")) {
                Collections.sort(upstreamContracts, (o1, o2) -> o1.getContractName().compareToIgnoreCase(o2.getContractName()));
            }
            else if (t.getName().equals("Bandwidth Capacity")) {
                Collections.sort(upstreamContracts, Comparator.comparingDouble(UpstreamContract::getBandwidthCapacity));
            }
            else if (t.getName().equals("Bandwidth Price")) {
                Collections.sort(upstreamContracts, Comparator.comparingDouble(UpstreamContract::getBandwidthPrice));
            }
            else if (t.getName().equals("Bandwidth Type")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getTypeOfBandwidthId));
            }
            else if (t.getName().equals("Media")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getMediaId));
            }
            else if (t.getName().equals("Service Location")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getBtclServiceLocationId));
            }
            else if (t.getName().equals("Provider Location")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getProviderLocationId));
            }
            else if (t.getName().equals("Provider")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getSelectedProviderId));
            }
            else if (t.getName().equals("Active Date")) {
                Collections.sort(upstreamContracts, Comparator.comparingLong(UpstreamContract::getActiveFrom));
            }
        });
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (upstreamContracts.size() > 0) {
            upstreamContracts.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Contract Name")) {
                        hashMap.put("Contract Name", m.getContractName());
                    }  else if (n.getName().equals("Active Date")) {
                        long val = m.getActiveFrom();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Active From", dateText);
                    }
                    else if(n.getName().equals("Service Location")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getBtclServiceLocationId());
                        if(inventoryItem!=null){
                            String serviceLocation = inventoryItem.getItemName();
                            hashMap.put("Service Location",m.getBtclServiceLocationId());
                        }
                        else hashMap.put("Service Location","N/A");
                    }
                    else if(n.getName().equals("Provider")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getSelectedProviderId());
                        if(inventoryItem!=null){
                            String provider = inventoryItem.getItemName();
                            hashMap.put("Provider",provider);
                        }
                        else hashMap.put("Provider","N/A");
                    }
                    else if(n.getName().equals("Provider Location")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getProviderLocationId());
                        if(inventoryItem!=null){
                            String providerLocation = inventoryItem.getItemName();
                            hashMap.put("Provider Location",providerLocation);
                        }
                        else hashMap.put("Provider Location","N/A");
                    }
                    else if(n.getName().equals("Media")){
                        UpstreamInventoryItem inventoryItem = upstreamInventoryMap.get(m.getMediaId());
                        if(inventoryItem!=null){
                            String media = inventoryItem.getItemName();
                            hashMap.put("Media",media);
                        }
                        else hashMap.put("Media","N/A");
                    }
                    else if(n.getName().equals("Bandwidth Price")){
                        hashMap.put("Bandwidth Price",m.getBandwidthPrice());
                    }
                    else if(n.getName().equals("Bandwidth Capacity")){
                        hashMap.put("Bandwidth Capacity",m.getBandwidthCapacity());
                    }
                    else if (n.getName().equals("Srf Date")) {
                        long val = m.getSrfDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Srf From", dateText);
                    }
                });
                mapList.add(hashMap);
            });
        }
        else{
            throw  new RequestFailureException("No Connections Found");
        }
        Report upStreamConnectionReport = new Report();
        upStreamConnectionReport.setMapArrayList(mapList);
        upStreamConnectionReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return upStreamConnectionReport;
    }
    //endregion
}
