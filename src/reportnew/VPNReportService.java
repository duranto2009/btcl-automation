package reportnew;

import annotation.Transactional;
import application.Application;
import application.ApplicationState;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import com.google.gson.internal.LinkedHashTreeMap;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import login.LoginDTO;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import util.TransactionType;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.client.ClientDetailsDTO;
import vpn.network.VPNNetworkLink;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.compare;
import static util.SqlGenerator.getColumnName;

public class VPNReportService {

    @Service
    GlobalService globalService;
    @Service
    VPNApplicationService vpnApplicationService;
    //region VPN
    @Transactional(transactionType = TransactionType.READONLY)
    public Report getReportBatchOperationVPNApplication(PostEntity postEntity, LoginDTO loginDTO) throws Exception{
        HashMap<String,String> searchCriteria = new HashMap<>();
        List<FlowState> flowStates = new ArrayList<>();
        postEntity.getSelectedSearchCriteria().forEach(s->{
            if(s.getType().equals("list")){
                if(s.getInput()!=null){
                    LinkedHashTreeMap<String,?> hashMap = (LinkedHashTreeMap<String, ?>) s.getInput();
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
                    else if(s.getName().equals("Layer Type")){
                        searchCriteria.put("Layer Type",sID.toString());
                    }
                    else if(s.getName().equals("Application Status")){
                        searchCriteria.put("Application Status",sID.toString());
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
        Class<Application> classObject = Application.class;
        String conditionalString = " where " + getColumnName(classObject, "moduleId") + " = " + ModuleConstants.Module_ID_VPN;

        if (searchCriteria.get("Application Id") != null) {
            conditionalString += " and " + getColumnName(classObject, "applicationId") + " = " + (long) Double.parseDouble(searchCriteria.get("Application Id")) + " ";
        }

        if (searchCriteria.get("clientID") != null) {
            conditionalString += " and " + getColumnName(classObject, "clientId") + " = " + (long) Double.parseDouble(searchCriteria.get("clientID")) + " ";
        }
        if (searchCriteria.get("User Name") != null) {
            long userId = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name")).getUserID();
            conditionalString += " and " + getColumnName(classObject, "userId") + " = " + userId;
        }
        if (searchCriteria.get("Created From") != null && searchCriteria.get("Created To") != null) {
            conditionalString += " and " + getColumnName(classObject, "createdDate") + " >= " + (long) Double.parseDouble(searchCriteria.get("Created From")) + " "
                    + " and " + getColumnName(classObject, "createdDate") + " <=" + (long) Double.parseDouble(searchCriteria.get("Created To")) + " ";

        }
        if (searchCriteria.get("Submission From") != null && searchCriteria.get("Submission To") != null) {
            conditionalString += " and " + getColumnName(classObject, "submissionDate") + " >= " + (long) Double.parseDouble(searchCriteria.get("Submission From")) + " "
                    + " and " + getColumnName(classObject, "submissionDate") + " <= " + (long) Double.parseDouble(searchCriteria.get("Submission To")) + " ";
        }
        if (searchCriteria.get("Application Status") != null) {
            conditionalString += " and " + getColumnName(classObject, "isServiceStarted") + " = " + (int) Integer.parseInt(searchCriteria.get("Application Status")) + " ";
        }
        List<Application> applications = globalService.getAllObjectListByCondition(classObject, conditionalString);
        for (Iterator<Application> iter = applications.listIterator(); iter.hasNext(); ) {
            Application a = iter.next();
            ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(a.getClientId(), ModuleConstants.Module_ID_VPN);
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
        List<VPNApplication> vpnApplicationWithLink = vpnApplicationService.getListOfVpnApplicationByApplicationIdWithLink(applications);
        if (searchCriteria.get("Layer Type") != null) {
            for (Iterator<VPNApplication> iter = vpnApplicationWithLink.listIterator(); iter.hasNext(); ) {
                VPNApplication a = iter.next();
                int type = (int) Double.parseDouble(searchCriteria.get("Layer Type"));
                if (a.getLayerType() != type) {
                    iter.remove();
                }
            }
        }

        if (searchCriteria.get("Vendor Name") != null) {
            for (Iterator<VPNApplication> iter = vpnApplicationWithLink.listIterator(); iter.hasNext(); ) {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name"));
                VPNApplication a = iter.next();
                final boolean[] needRemove = {true};
                List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                        new EFRConditionBuilder()
                                .Where()
                                .applicationIdEquals(a.getApplicationId())
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

        if (searchCriteria.get("Application State") != null) {
            if(flowStates.size()>0){
                vpnApplicationWithLink=getApplicationListOfStateGroup(flowStates,vpnApplicationWithLink);
            }

        }

        if (searchCriteria.get("Application Phase") != null) {
            if(flowStates.size()>0){
                vpnApplicationWithLink=getApplicationListOfStateGroup(flowStates,vpnApplicationWithLink);
            }

        }
        orderBySelectedItems(postEntity, vpnApplicationWithLink);
        ArrayList<Map<String, Object>> mapList = getReportDisplayMapApplication(vpnApplicationWithLink, postEntity);//new ArrayList<>();
        Report vpnApplicationReport = new Report();
        vpnApplicationReport.setMapArrayList(mapList);
        //vpnApplicationReport.setVpnApplicationList(vpnApplicationWithLink);
        vpnApplicationReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return vpnApplicationReport;
    }

    private void orderBySelectedItems(PostEntity postEntity, List<VPNApplication> vpnApplicationWithLink) {
        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Application Id")) {
                Collections.sort(vpnApplicationWithLink, Comparator.comparingLong(Application::getApplicationId));
            } else if (t.getName().equals("Application State")) {
                Collections.sort(vpnApplicationWithLink, new Comparator<VPNApplication>() {
                    public int compare(VPNApplication o1, VPNApplication o2) {
                        return o1.getApplicationState().compareTo(o2.getApplicationState());
                    }
                });
            } else if (t.getName().equals("Created Date")) {
                Collections.sort(vpnApplicationWithLink, Comparator.comparingLong(Application::getCreatedDate));
            } else if (t.getName().equals("Submission Date")) {
                Collections.sort(vpnApplicationWithLink, Comparator.comparingLong(Application::getSubmissionDate));
            } else if (t.getName().equals("Layer Type")) {
                Collections.sort(vpnApplicationWithLink, Comparator.comparingInt(VPNApplication::getLayerType));
            } else if (t.getName().equals("User Name")) {
                Collections.sort(vpnApplicationWithLink, Comparator.comparingLong(Application::getUserId));
            }

        });
    }

    private ArrayList<Map<String, Object>> getReportDisplayMapApplication(List<VPNApplication> vpnApplicationWithLink, PostEntity postEntity) {
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (vpnApplicationWithLink.size() > 0) {
            vpnApplicationWithLink.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Application Id")) {
                        hashMap.put("ID", m.getApplicationId());
                    } else if (n.getName().equals("Client Name")) {
                        if(m.getClientId()>0){
                            ClientDTO client = AllClientRepository.getInstance().getClientByClientID(m.getClientId());
                            hashMap.put("Client Name", client.getName());
                        }
                        else hashMap.put("Client Name","N/A");
                    } else if (n.getName().equals("User Name")) {
                        if (m.getUserId() > 0) {
                            UserDTO usr = UserRepository.getInstance().getUserDTOByUserID(m.getUserId());
                            if (usr != null) hashMap.put("User Name", usr.getUsername());

                        }
                        else {
                            hashMap.put("User Name","N/A");
                        }
                    }

                    /*else if(n.getName().equals("Vendor Name")){
                        m.getVpnApplicationLinks().forEach(vpnApplicationLink -> {
                            vpnApplicationLink.getE
                        });
                        UserDTO usr=UserRepository.getInstance().getUserDTOByUserID();
                        hashMap.put("User Name", usr.getUsername());
                    }*/

                    else if (n.getName().equals("Application State")) {
                        List<FlowState> flowStates = FlowRepository.getInstance().getAllFlowStates();
                        StringBuilder concatenatedState = new StringBuilder();
                        final String[] applicationState = {new String()};
                        int i = 0;
                        m.getVpnApplicationLinks().stream().forEach(l -> {
                            FlowState flowState = flowStates
                                    .stream()
                                    .filter(
                                            t -> t.getId() == l.getLinkState().getState()
                                    )
                                    .findFirst()
                                    .orElse(null);
                            if (flowState != null) {
                                concatenatedState.append("Link ")
                                        .append(m.getVpnApplicationId())
                                        .append(":")
                                        .append(flowState.getViewDescription())
                                        .append(System.getProperty("line.separator"));
                            } else {
                                concatenatedState.append("");
                            }
                        });
                        hashMap.put("Application State", concatenatedState.toString());
                    } else if (n.getName().equals("Created Date")) {
                        long val = m.getCreatedDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Created Date", dateText);
                    } else if (n.getName().equals("Submission Date")) {
                        long val = m.getSubmissionDate();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Submission date", dateText);
                    } else if (n.getName().equals("Registrant Type") || n.getName().equals("Registrant Category")) {
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_VPN)
                                .stream()
                                .filter(t -> t.getClientID() == m.getClientId()).findFirst().orElse(null);
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
                    } else if (n.getName().equals("BandWidth")) {
                        final String[] bandwidthString = {new String()};
                        m.getVpnApplicationLinks().stream().forEach(l -> {
//                            bandwidthString[0] = bandwidthString[0] +"Link :" +l.getl.getLinkBandwidth()+"\n";
                            bandwidthString[0] = bandwidthString[0] + "Link :" + l.getLinkName() + "\n";
                        });
                        hashMap.put("BandWidth", bandwidthString[0]);
                    } else if (n.getName().equals("Layer Type")) {
                        if (m.getLayerType() == 2) {

                            hashMap.put("Layer Type", "Layer 2");
                        } else if (m.getLayerType() == 3) {

                            hashMap.put("Layer Type", "Layer 3");
                        } else hashMap.put("Layer Type", "Not Defined");
                    } else if (n.getName().equals("Application Status")) {
                        if (m.getIsServiceStarted() == 1) {
                            hashMap.put("Application Status", "Completed");
                        } else {
                            hashMap.put("Application Status", "Pending");
                        }
                    }
                });
                mapList.add(hashMap);
            });
        }
        else{
            throw new RequestFailureException("No Data Found");
        }
        return mapList;
    }

    @Transactional
    public Report getReportBatchOperationForNetworkLink(PostEntity postEntity, LoginDTO loginDTO) throws Exception {
        HashMap<String, String> searchCriteria = new HashMap<>();
        postEntity.getSelectedSearchCriteria().forEach(s -> {
            if (s.getType().equals("list")) {
                if (s.getInput() != null) {
                    LinkedHashTreeMap<String, Double> hashMap = (LinkedHashTreeMap<String, Double>) s.getInput();
                    Double sID = hashMap.get("ID");
                    if (s.getName().equals("Client Name")) {
                        searchCriteria.put("clientID", sID.toString());
                    }
                    else if(s.getName().equals("Link Status")){
                        searchCriteria.put("Link Status",sID.toString());
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
                    if (s.getName().equals("Link Name")) {
                        searchCriteria.put("Link Name", sInput);
                    } else if (s.getName().equals("Link Distance")) {
                        searchCriteria.put("Link Distance", sInput);
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
        Class<VPNNetworkLink> classObject = VPNNetworkLink.class;
        String conditionalString = " where 1=1 ";
        // TODO: 4/15/2019 the below conditional string need to change
        if (searchCriteria.get("Link Name") != null) {
            conditionalString += " and " + getColumnName(classObject, "linkName") + " like '%" + searchCriteria.get("Link Name") + "%' ";
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
        if(searchCriteria.get("Link Status")!=null){
            conditionalString+=" and "+getColumnName(classObject, "linkStatus")+" like '%"+ searchCriteria.get("Link Status")+"%' ";
        }
        if(searchCriteria.get("Incident Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "incidentType")+" like '%"+ searchCriteria.get("Incident Type")+"%' ";
        }
        if(searchCriteria.get("Bandwidth")!=null){
            conditionalString+=" and "+getColumnName(classObject, "linkBandwidth")+" = "+(int) Double.parseDouble(searchCriteria.get("Incident Type"))+" ";
        }
        if(searchCriteria.get("Link Distance")!=null){
            conditionalString+=" and "+getColumnName(classObject, "linkDistance")+" = "+(int) Double.parseDouble(searchCriteria.get("linkDistance"))+" ";
        }
        List<VPNNetworkLink>vpnNetworkLinks = globalService.getAllObjectListByCondition(classObject,conditionalString);

        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Link Name")) {
                Collections.sort(vpnNetworkLinks, (o1, o2) -> o1.getLinkName().compareToIgnoreCase(o2.getLinkName()));
            } else if (t.getName().equals("Client Name")) {
                Collections.sort(vpnNetworkLinks, Comparator.comparingLong(VPNNetworkLink::getClientId));
            } else if (t.getName().equals("Active Date")) {
                Collections.sort(vpnNetworkLinks, Comparator.comparingLong(VPNNetworkLink::getActiveFrom));
            }

            else if(t.getName().equals("Link Status")){
                Collections.sort(vpnNetworkLinks, (o1, o2) -> o1.getLinkStatus().compareToIgnoreCase(o2.getLinkStatus()));
            }

            else if(t.getName().equals("Incident Type")){
                Collections.sort(vpnNetworkLinks, (o1, o2) -> o1.getIncidentType().compareToIgnoreCase(o2.getIncidentType()));
            }

            else if(t.getName().equals("Bandwidth")){
                Collections.sort(vpnNetworkLinks, Comparator.comparingInt(VPNNetworkLink::getLinkBandwidth));
            }
            else if(t.getName().equals("Link Distance")){
                Collections.sort(vpnNetworkLinks, Comparator.comparingLong(VPNNetworkLink::getLinkDistance));
            }

        });

        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (vpnNetworkLinks.size() > 0) {
            vpnNetworkLinks.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Link Name")) {
                        hashMap.put("Link Name", m.getLinkName());
                    } else if (n.getName().equals("Client Name")) {
                        if(m.getClientId() !=0) {
                            hashMap.put("Client Name", AllClientRepository.getInstance().getClientByClientID(m.getClientId()).getName());
                        }
                        else {
                            hashMap.put("Client Name","N/A");
                        }
                    } else if (n.getName().equals("Active Date")) {
                        long val = m.getActiveFrom();
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        hashMap.put("Active From", dateText);
                    } else if (n.getName().equals("Registrant Type") || n.getName().equals("Registrant Category")) {
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_VPN)
                                .stream()
                                .filter(t -> t.getClientID() == m.getClientId()).findFirst().orElse(null);
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
                    } else if (n.getName().equals("BandWidth")) {
                        hashMap.put("BandWidth", m.getLinkName());
                    }
                    else if(n.getName().equals("Incident Type")){
                        hashMap.put("Incident Type", m.getIncidentType());
                    }
                    else if(n.getName().equals("Link Status")){
                        hashMap.put("Link Status", m.getLinkStatus());
                    }
                    else if(n.getName().equals("Link Distance")){
                        hashMap.put("Link Distance", m.getLinkDistance());
                    }
                });
                mapList.add(hashMap);
            });
        }
        Report vpnNetworkReport = new Report();
        vpnNetworkReport.setMapArrayList(mapList);
        //vpnNetworkReport.setNetworkLinks(vpnNetworkLinks);
        vpnNetworkReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return vpnNetworkReport;
    }

    @Transactional
    public List<VPNApplication> getApplicationListOfStateGroup(List<FlowState> flowStates, List<VPNApplication> vpnApplications) throws Exception {
        List<ApplicationState> applicationStates = new ArrayList<>();
        List<VPNApplication> filteredApplication=new ArrayList<>();
        List<String> stateList ;

        for (FlowState flowState : flowStates
        ) {
            ApplicationState applicationState = ApplicationState.getApplicationStateByStateId(flowState.getId());

            applicationStates.add(applicationState);

        }
        List<VPNApplicationLink> applicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .linkStateIn(applicationStates.stream().map(p->p.name()).collect(Collectors.toList()))
                        .getCondition()
        );

        Map<Long, List<VPNApplicationLink>> mappedStateWiseApp = applicationLinks
                .stream()
                .collect(
                        Collectors.groupingBy(VPNApplicationLink::getVpnApplicationId)
                );


        vpnApplications
                .forEach(
                        t -> {
                            List<VPNApplicationLink> applicationLinks1 = mappedStateWiseApp.getOrDefault(t.getVpnApplicationId(), null);
                            if (applicationLinks1 != null) {
                                filteredApplication.add(t);
                            }
                        }
                );

        return filteredApplication;

    }
    //endregion
}
