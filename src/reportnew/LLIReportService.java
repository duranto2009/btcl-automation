package reportnew;

import annotation.Transactional;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import com.google.gson.internal.LinkedHashTreeMap;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import entity.efr.EFRConditionBuilder;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static util.SqlGenerator.getColumnName;

public class LLIReportService {

    @Service
    GlobalService globalService;

    //region lli
    @Transactional
    public Report getReportBatchOperationForlliConnection(PostEntity postEntity, LoginDTO loginDTO) throws Exception{
        HashMap<String, String> searchCriteria = new HashMap<>();
        postEntity.getSelectedSearchCriteria().forEach(s -> {
            if (s.getType().equals("list")) {
                if (s.getInput() != null) {
                    LinkedHashTreeMap<String, Double> hashMap = (LinkedHashTreeMap<String, Double>) s.getInput();
                    Double sID = hashMap.get("ID");
                    if (s.getName().equals("Client Name")) {
                        searchCriteria.put("clientID", sID.toString());
                    }
                    else if(s.getName().equals("Connection Status")){
                        searchCriteria.put("Connection Status",sID.toString());
                    }
                    else if(s.getName().equals("Incident Type")){
                        searchCriteria.put("Incident Type",sID.toString());
                    }
                    else if(s.getName().equals("Registrant Type")){
                        searchCriteria.put("Registrant Type",sID.toString());
                    }
                    else if(s.getName().equals("Registrant Category")){
                        searchCriteria.put("Registrant Category",sID.toString());
                    }
                }

            } else if (s.getType().equals("input")) {
                if (s.getInput() != null) {
                    String sInput = (String) s.getInput();
                    if (s.getName().equals("Connection Name")) {
                        searchCriteria.put("Connection Name", sInput);
                    }
                    else if(s.getName().equals("Bandwidth")){
                        searchCriteria.put("Bandwidth",sInput);
                    }
                }
            } else if (s.getType().equals("date")) {
                if (s.getInput1() != null) {
                    Double d1 = (Double) s.getInput1();
                    Long v1 = d1.longValue();
                    if (s.getName().equals("Active Date")) {
                        searchCriteria.put("Active From", v1.toString());
                    }
                }
                if(s.getInput2() != null){
                    Double d2 = (Double) s.getInput2();
                    Long v2 = d2.longValue();
                    if (s.getName().equals("Active Date")) {
                        searchCriteria.put("Active To", v2.toString());
                    }
                }
            }

        });
        Class<LLIConnection> classObject = LLIConnection.class;
        String conditionalString = " where 1=1 ";
        // TODO: 4/15/2019 the below conditional string need to change
        if (searchCriteria.get("Connection Name") != null) {
            conditionalString += " and " + getColumnName(classObject, "name") + " like '%" + searchCriteria.get("Connection Name") + "%' ";
        }
        if (searchCriteria.get("clientID") != null) {
            conditionalString += " and " + getColumnName(classObject, "clientId") + " = " + (long) Double.parseDouble(searchCriteria.get("clientID")) + " ";
        }
        if (searchCriteria.get("Active From") != null) {
            conditionalString += " and " + getColumnName(classObject, "activeFrom") + " >= " + Long.parseLong(searchCriteria.get("Active From")) + " ";
        }
        if(searchCriteria.get("Active To") != null){
            conditionalString += " and " + getColumnName(classObject, "activeFrom") + " <= " + Long.parseLong(searchCriteria.get("Active To")) + " ";

        }
        if(searchCriteria.get("Connection Status")!=null){
            conditionalString+=" and "+getColumnName(classObject, "status")+" like '%"+ searchCriteria.get("Connection Status")+"%' ";
        }
        if(searchCriteria.get("Incident Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "incident")+" like '%"+ searchCriteria.get("Incident Type")+"%' ";
        }
        if(searchCriteria.get("Bandwidth")!=null){
            conditionalString+=" and "+getColumnName(classObject, "linkBandwidth")+" = "+(int) Double.parseDouble(searchCriteria.get("Incident Type"))+" ";
        }

        List<LLIConnection> lliConnections = globalService.getAllObjectListByCondition(classObject,conditionalString);

        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Connection Name")) {
                Collections.sort(lliConnections, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            }
            else if (t.getName().equals("Client Name")) {
                Collections.sort(lliConnections, Comparator.comparingLong(LLIConnection::getClientID));
            }
            else if (t.getName().equals("Active Date")) {
                Collections.sort(lliConnections, Comparator.comparingLong(LLIConnection::getActiveFrom));
            }
            else if(t.getName().equals("Connection Status")){
                Collections.sort(lliConnections, Comparator.comparingInt(LLIConnection::getStatus));
            }
            else if(t.getName().equals("Incident Type")){
                Collections.sort(lliConnections, Comparator.comparingInt(LLIConnection::getIncident));
            }
            else if(t.getName().equals("Bandwidth")){
                Collections.sort(lliConnections, Comparator.comparingDouble(LLIConnection::getBandwidth));
            }
        });

        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (lliConnections.size() > 0) {
            lliConnections.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Connection Name")) {
                        hashMap.put("Connection Name", m.getName());
                    }
                    else if (n.getName().equals("Client Name")) {
                        if(m.getClientID() !=0) {
                            hashMap.put("Client Name", AllClientRepository.getInstance().getClientByClientID(m.getClientID()).getName());
                        }
                        else {
                            hashMap.put("Client Name","N/A");
                        }
                    }
                    else if (n.getName().equals("Active Date")) {
                        long val = m.getActiveFrom();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Active From", dateText);
                    }
                    else if (n.getName().equals("Registrant Type") || n.getName().equals("Registrant Category")) {
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == m.getClientID()).findFirst().orElse(null);
                        if (clientDetails != null) {
                            if (n.getName().equals("Registrant Type")) {
                                hashMap.put("Registrant Type", RegistrantTypeConstants.RegistrantTypeName.get(clientDetails.getRegistrantType()));
                            } else if (n.getName().equals("Registrant Category")) {
                                hashMap.put("Registrant Category", RegistrantCategoryConstants.RegistrantCategoryName.get(clientDetails.getRegistrantCategory()));
                            }
                        }
                        else{
                            if (n.getName().equals("Registrant Type")) {
                                hashMap.put("Registrant Type", "N/A");
                            } else if (n.getName().equals("Registrant Category")) {
                                hashMap.put("Registrant Category", "N/A");
                            }
                        }
                    }
                    else if (n.getName().equals("BandWidth")) {
                        hashMap.put("BandWidth", m.getBandwidth());
                    }
                    else if(n.getName().equals("Incident Type")){
                        hashMap.put("Incident Type", LLIConnectionConstants.applicationTypeNameMap.get(m.getIncident()));
                    }
                    else if(n.getName().equals("Connection Status")){
                        hashMap.put("Connection Status", LLIConnectionConstants.connectionStatusMap.get(m.getStatus()));
                    }
                });
                mapList.add(hashMap);
            });
        }
        else{
            throw  new RequestFailureException("No Connections Found");
        }
        Report lliConnectionReport = new Report();
        lliConnectionReport.setMapArrayList(mapList);
        lliConnectionReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return lliConnectionReport;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Report getReportBatchOperationForlliApplication(PostEntity postEntity, LoginDTO loginDTO)  throws Exception{
        HashMap<String,String> searchCriteria = new HashMap<>();
        List<FlowState> flowStates = new ArrayList<>();
        postEntity.getSelectedSearchCriteria().forEach(s->{
            if(s.getType().equals("list")){
                if(s.getInput()!=null){
                    LinkedHashTreeMap<String,?>hashMap = (LinkedHashTreeMap<String, ?>) s.getInput();
                    Double sID= (Double) hashMap.get("ID");
                    if(s.getName().equals("Client Name")){
                        searchCriteria.put("clientID",sID.toString());
                    }
                    else if(s.getName().equals("Registrant Category")){
                        searchCriteria.put("Registrant Category",sID.toString());
                    }
                    else if(s.getName().equals("Registrant Type")){
                        searchCriteria.put("Registrant Type",sID.toString());
                    }
                    else if(s.getName().equals("Application Status")){
                        searchCriteria.put("Application Status",sID.toString());
                    }
                    else if(s.getName().equals("Connection Type")){
                        searchCriteria.put("Connection Type",sID.toString());
                    }
                    else if(s.getName().equals("Loop Provider")){
                        searchCriteria.put("Loop Provider",sID.toString());
                    }
                    else if(s.getName().equals("Application State")){
                        List<LinkedHashTreeMap<String,Object>> list = (List<LinkedHashTreeMap<String, Object>>) hashMap.get("object");
                        list.forEach(eachmap->{
                            FlowState flowState = new FlowState();
                            Long s1=Math.round((Double) eachmap.get("id"));
                            flowState.setId(Math.toIntExact(s1));
                            flowState.setName((String)eachmap.get("name"));
                            flowState.setViewDescription((String)eachmap.get("viewDescription"));
                            flowStates.add(flowState);
                        });
                        searchCriteria.put("Application State",sID.toString());
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
                    else if(s.getName().equals("User Name")){
                        searchCriteria.put("User Name",sInput);
                    }
                    else if(s.getName().equals("Vendor Name")){
                        searchCriteria.put("Vendor Name",sInput);
                    }
                    else if(s.getName().equals("Bandwidth")){
                        searchCriteria.put("Bandwidth",sInput);
                    }
                }
            }
            else if(s.getType().equals("date")){
                if(s.getInput1()!=null&&s.getInput2()!=null){
                    Double d1 = (Double) s.getInput1();
                    Double d2 = (Double) s.getInput2();
                    if(s.getName().equals("Created Date")){
                        searchCriteria.put("Created From",d1.toString());
                        searchCriteria.put("Created To",d2.toString());
                    }
                    else if(s.getName().equals("Submission Date")){
                        searchCriteria.put("Submission From",d1.toString());
                        searchCriteria.put("Submission To",d2.toString());
                    }
                }
            }
        });
        Class<LLIApplication> classObject = LLIApplication.class;
        String conditionalString = " where 1=1 ";

        if (searchCriteria.get("Application Id") != null) {
            conditionalString += " and " + getColumnName(classObject, "applicationID") + " = " + (long) Double.parseDouble(searchCriteria.get("Application Id")) + " ";
        }

        if (searchCriteria.get("clientID") != null) {
            conditionalString += " and " + getColumnName(classObject, "clientID") + " = " + (long) Double.parseDouble(searchCriteria.get("clientID")) + " ";
        }
        if (searchCriteria.get("Loop Provider") != null) {
            conditionalString += " and " + getColumnName(classObject, "loopProvider") + " = " + (int) Double.parseDouble(searchCriteria.get("Loop Provider")) + " ";
        }
        if (searchCriteria.get("Connection Type") != null) {
            conditionalString += " and " + getColumnName(classObject, "connectionType") + " = " + (int) Double.parseDouble(searchCriteria.get("Connection Type")) + " ";
        }
        if (searchCriteria.get("User Name") != null) {
            long userId = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name")).getUserID();
            conditionalString += " and " + getColumnName(classObject, "userId") + " = " + userId;
        }

        if (searchCriteria.get("Submission From") != null && searchCriteria.get("Submission To") != null) {
            conditionalString += " and " + getColumnName(classObject, "submissionDate") + " >= " + (long) Double.parseDouble(searchCriteria.get("Submission From")) + " "
                    + " and " + getColumnName(classObject, "submissionDate") + " <= " + (long) Double.parseDouble(searchCriteria.get("Submission To")) + " ";
        }
        if (searchCriteria.get("Application Status") != null) {
            conditionalString += " and " + getColumnName(classObject, "isServiceStarted") + " = " + (int) Integer.parseInt(searchCriteria.get("Application Status")) + " ";
        }

        List<LLIApplication> applications = globalService.getAllObjectListByCondition(classObject, conditionalString);

        for (Iterator<LLIApplication> iter = applications.listIterator(); iter.hasNext(); ) {
            LLIApplication a = iter.next();
            ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(a.getClientID(), ModuleConstants.Module_ID_LLI);
            if (searchCriteria.get("Registrant Type") != null) {
                int type = (int) Double.parseDouble(searchCriteria.get("Registrant Type"));
                if (clientDetailsDTO.getRegistrantType() != type) {
                    iter.remove();
                }
            }
            if (searchCriteria.get("Registrant Category") != null) {
                long category = (int) Double.parseDouble(searchCriteria.get("Registrant Category"));
                if (clientDetailsDTO.getRegistrantCategory() != category) {
                    iter.remove();
                }
            }
        }

        if (searchCriteria.get("Vendor Name") != null) {
            for (Iterator<LLIApplication> iter = applications.listIterator(); iter.hasNext(); ) {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name"));
                LLIApplication a = iter.next();
                final boolean[] needRemove = {true};
                List<lli.Application.EFR.EFR> efrs = globalService.getAllObjectListByCondition(lli.Application.EFR.EFR.class,
                        new EFRConditionBuilder()
                                .Where()
                                .applicationIdEquals(a.getApplicationID())
                                .getCondition());
                efrs.forEach(efr -> {
                    if (efr.getVendorID() == userDTO.getUserID()) {
                        needRemove[0] = false;
                    }
                });
                if (needRemove[0] == true) {
                    iter.remove();
                }
            }
        }

        // TODO: 4/18/2019 application state wise filter for LLI application
        if (searchCriteria.get("Application State") != null) {
            if(flowStates.size()>0){
                applications=getLLIApplicationListOfStateGroup(flowStates,applications);
            }

        }

        if (searchCriteria.get("Application Phase") != null) {
            if(flowStates.size()>0){
                applications=getLLIApplicationListOfStateGroup(flowStates,applications);
            }

        }

        List<LLIApplication> finalApplications = applications;
        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Application Id")) {
                Collections.sort(finalApplications, Comparator.comparingLong(LLIApplication::getApplicationID));
            } else if (t.getName().equals("Application State")) {
                Collections.sort(finalApplications, new Comparator<LLIApplication>() {
                    public int compare(LLIApplication o1, LLIApplication o2) {
                        return Integer.compare(o1.getState(), o2.getState());
                    }
                });
            }  else if (t.getName().equals("Submission Date")) {
                Collections.sort(finalApplications, Comparator.comparingLong(LLIApplication::getSubmissionDate));
            }
            else if (t.getName().equals("User Name")) {
                Collections.sort(finalApplications, Comparator.comparingLong(LLIApplication::getUserID));
            }

            else if (t.getName().equals("Bandwidth")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(LLIApplication::getBandwidth));
            }
            else if (t.getName().equals("Connection Type")) {
                Collections.sort(finalApplications, Comparator.comparingLong(LLIApplication::getConnectionType));
            }

            else if (t.getName().equals("Loop Provider")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(LLIApplication::getLoopProvider));
            }
            else if(t.getName().equals("Registrant Type")){
                Collections.sort(finalApplications, new Comparator<LLIApplication>(){
                    public int compare(LLIApplication o1, LLIApplication o2)
                    {
                        ClientDetailsDTO clientDetails1 = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == o1.getClientID()).findFirst().orElse(null);
                        ClientDetailsDTO clientDetails2 = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == o2.getClientID()).findFirst().orElse(null);
                        if (clientDetails1 == null ||clientDetails2 == null) {
                            return -1;
                        }
                        return Integer.compare(clientDetails1.getRegistrantType(), clientDetails2.getRegistrantType());
                    }
                });
            }

            else if(t.getName().equals("Registrant Category")){
                Collections.sort(finalApplications, new Comparator<LLIApplication>(){
                    public int compare(LLIApplication o1, LLIApplication o2)
                    {
                        ClientDetailsDTO clientDetails1 = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == o1.getClientID()).findFirst().orElse(null);
                        ClientDetailsDTO clientDetails2 = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == o2.getClientID()).findFirst().orElse(null);
                        if (clientDetails1 == null ||clientDetails2 == null) {
                            return -1;
                        }
                        return Long.compare(clientDetails1.getRegistrantCategory(), clientDetails2.getRegistrantCategory());
                    }
                });
            }

        });
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (finalApplications.size() > 0) {
            finalApplications.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Application Id")) {
                        hashMap.put("ID", m.getApplicationID());
                    }
                    else if (n.getName().equals("Client Name")) {
                        if(m.getClientID()>0){
                            ClientDTO client = AllClientRepository.getInstance().getClientByClientID(m.getClientID());
                            hashMap.put("Client Name", client.getName());
                        }
                        else hashMap.put("Client Name", "N/A");
                    }
                    else if (n.getName().equals("User Name")) {
                        if (m.getUserID()!=null && m.getUserID() > 0) {
                            UserDTO usr = UserRepository.getInstance().getUserDTOByUserID(m.getUserID());
                            if (usr != null) hashMap.put("User Name", usr.getUsername());

                        }
                        else hashMap.put("User Name", "N/A");

                    }
                    else if (n.getName().equals("Connection Type")) {
                        hashMap.put("Client Name", LLIConnectionConstants.connectionTypeMap.get(m.getConnectionType()));
                    }
                    else if (n.getName().equals("Loop Provider")) {
                        if(m.getLoopProvider() == 1)hashMap.put("Loop Provider", "BTCL");
                        else hashMap.put("Loop Provider", "Client");
                    }
                    else if (n.getName().equals("Application State")) {
                        if(m.getState()>0){
                            hashMap.put("Application State", FlowRepository.getInstance().getFlowStateByFlowStateId(m.getState()).getViewDescription());
                        }
                        else {
                            hashMap.put("Application State","N/A");
                        }
                    }
                    else if (n.getName().equals("Submission Date")) {
                        long val = m.getSubmissionDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Submission date", dateText);
                    }
                    else if (n.getName().equals("Registrant Type") || n.getName().equals("Registrant Category")) {
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_LLI)
                                .stream()
                                .filter(t -> t.getClientID() == m.getClientID()).findFirst().orElse(null);
                        if (clientDetails != null) {
                            if (n.getName().equals("Registrant Type")) {
                                hashMap.put("Registrant Type", RegistrantTypeConstants.RegistrantTypeName.get(clientDetails.getRegistrantType()));
                            } else if (n.getName().equals("Registrant Category")) {
                                hashMap.put("Registrant Category", RegistrantCategoryConstants.RegistrantCategoryName.get(clientDetails.getRegistrantCategory()));
                            }
                        }
                        else {
                            if (n.getName().equals("Registrant Type")) {
                                hashMap.put("Registrant Type", "N/A");
                            } else if (n.getName().equals("Registrant Category")) {
                                hashMap.put("Registrant Category", "N/A");
                            }
                        }
                    }
                    else if (n.getName().equals("BandWidth")) {
                        hashMap.put("BandWidth", m.getBandwidth()+" Mbps");
                    }
                    else if (n.getName().equals("Application Status")) {
                        if (m.isServiceStarted() == true) {
                            hashMap.put("Application Status", "Completed");
                        } else {
                            hashMap.put("Application Status", "Pending");
                        }
                    }
                });
                mapList.add(hashMap);
            });
        }
        if(finalApplications.size()== 0 ){
            throw  new RequestFailureException("No Data Found");
        }
        Report lliApplicationReport = new Report();
        lliApplicationReport.setMapArrayList(mapList);
        lliApplicationReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return lliApplicationReport;
    }

    @Transactional
    private List<LLIApplication> getLLIApplicationListOfStateGroup(List<FlowState> flowStates, List<LLIApplication> applications) throws Exception{
        List<LLIApplication> filteredApplication=new ArrayList<>();
        List<LLIApplication> lliApplications = globalService.getAllObjectListByCondition(LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .stateIn(flowStates.stream().map(p->p.getId()).collect(Collectors.toList()))
                        .getCondition()
        );

        Map<Long,LLIApplication> mappedStateWiseApp = lliApplications
                .stream()
                .collect(Collectors.toMap(LLIApplication::getApplicationID, Function.identity()));


        applications.forEach(
                t -> {
                    LLIApplication lliApplication = mappedStateWiseApp.getOrDefault(t.getApplicationID(), null);
                    if (lliApplication != null) {
                        filteredApplication.add(t);
                    }
                }
        );

        return filteredApplication;
    }
    //endregion

}
