package reportnew;

import annotation.Transactional;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationDTOConditionBuilder;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import com.google.gson.internal.LinkedHashTreeMap;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import entity.efr.EFRConditionBuilder;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
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

import static java.lang.Long.compare;
import static util.SqlGenerator.getColumnName;

public class CoLocationReportService {

    @Service GlobalService globalService;
    @Service
    CoLocationInventoryTemplateService coLocationInventoryTemplateService;
    //region Co Location
    @Transactional
    public Report getReportBatchOperationForCoLocationConnection(PostEntity postEntity, LoginDTO loginDTO) throws Exception{
        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap =  coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();
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
                    else if(s.getName().equals("Rack Size")){
                        searchCriteria.put("Rack Size",sID.toString());
                    }
                    else if(s.getName().equals("Rack Space")){
                        searchCriteria.put("Rack Space",sID.toString());
                    }

                    else if(s.getName().equals("Fiber Type")){
                        searchCriteria.put("Fiber Type",sID.toString());
                    }
                    else if(s.getName().equals("Floor Space Type")){
                        searchCriteria.put("Floor Space Type",sID.toString());
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
                    else if(s.getName().equals("Power Amount")){
                        searchCriteria.put("Power Amount",sInput);
                    }
                    else if(s.getName().equals("Fiber Core")){
                        searchCriteria.put("Fiber Core",sInput);
                    }else if(s.getName().equals("Floor Space Amount")){
                        searchCriteria.put("Floor Space Amount",sInput);
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
        Class<CoLocationConnectionDTO> classObject = CoLocationConnectionDTO.class;
        String conditionalString = " where 1=1 ";
        // TODO: 4/15/2019 the below conditional string need to change
        if (searchCriteria.get("Connection Name") != null) {
            conditionalString += " and " + getColumnName(classObject, "name") + " like '%" + searchCriteria.get("Connection Name") + "%' ";
        }
        if (searchCriteria.get("clientID") != null) {
            conditionalString += " and " + getColumnName(classObject, "clientID") + " = " + (long) Double.parseDouble(searchCriteria.get("clientID")) + " ";
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
       /* if(searchCriteria.get("Rack Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Rack Needed"))==1?true:false)+" ";
        }*/
        if(searchCriteria.get("Rack Size")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackSize")+" = "+(int) Double.parseDouble(searchCriteria.get("Rack Size"))+" ";
        }
        if(searchCriteria.get("Rack Space")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackSpace")+" = "+ (int) Double.parseDouble(searchCriteria.get("Rack Space"))+" ";
        }
        /*if(searchCriteria.get("Fiber Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Fiber Needed"))==1?true:false)+" ";
        }
        if(searchCriteria.get("Power Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "powerNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Power Needed"))==1?true:false)+" ";
        }*/
        if(searchCriteria.get("Fiber Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberType")+" = "+ (int) Double.parseDouble(searchCriteria.get("Fiber Type"))+" ";
        }
        /*if(searchCriteria.get("Floor Space Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceNeeded")+" = "+  ((int)Double.parseDouble(searchCriteria.get("Floor Space Needed"))==1?true:false)+" ";
        }*/
        if(searchCriteria.get("Floor Space Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceType")+" = "+ (int) Double.parseDouble(searchCriteria.get("Floor Space Type"))+" ";
        }
        if(searchCriteria.get("Power Amount")!=null){
            conditionalString+=" and "+getColumnName(classObject, "powerAmount")+" = "+  Double.parseDouble(searchCriteria.get("Power Amount"))+" ";
        }
        if(searchCriteria.get("Fiber Core")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberCore")+" = "+  (int)Double.parseDouble(searchCriteria.get("Fiber Core"))+" ";
        }
        if(searchCriteria.get("Floor Space Amount")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceAmount")+" = "+ Double.parseDouble(searchCriteria.get("Floor Space Amount"))+"  ";
        }


        List<CoLocationConnectionDTO> coLocationConnectionDTOS = globalService.getAllObjectListByCondition(classObject,conditionalString);

        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Connection Name")) {
                Collections.sort(coLocationConnectionDTOS, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            } else if (t.getName().equals("Client Name")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingLong(CoLocationConnectionDTO::getClientID));
            } else if (t.getName().equals("Active Date")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingLong(CoLocationConnectionDTO::getActiveFrom));
            }

            else if(t.getName().equals("Connection Status")){
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingInt(CoLocationConnectionDTO::getStatus));
            }

            else if(t.getName().equals("Incident Type")){
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingInt(CoLocationConnectionDTO::getIncident));
            }

            else if (t.getName().equals("Rack Space")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingInt(CoLocationConnectionDTO::getRackSpace));
            }

            else if (t.getName().equals("Rack Size")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingInt(CoLocationConnectionDTO::getRackSize));
            }  else if (t.getName().equals("Floor Space Amount")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingDouble(CoLocationConnectionDTO::getFloorSpaceAmount));
            } else if (t.getName().equals("Fiber Core")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingLong(CoLocationConnectionDTO::getFiberCore));
            }
            else if (t.getName().equals("Power Amount")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingDouble(CoLocationConnectionDTO::getPowerAmount));
            }  else if (t.getName().equals("Floor Space Type")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingLong(CoLocationConnectionDTO::getFloorSpaceType));
            } else if (t.getName().equals("Fiber Type")) {
                Collections.sort(coLocationConnectionDTOS, Comparator.comparingLong(CoLocationConnectionDTO::getFiberType));
            }
        });

        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        if (coLocationConnectionDTOS.size() > 0) {
            coLocationConnectionDTOS.forEach(m -> {
                Map<String, Object> hashMap = new LinkedHashTreeMap<>();
                postEntity.getSelectedDisplayItems().forEach(n -> {
                    if (n.getName().equals("Connection Name")) {
                        hashMap.put("Connection Name", m.getName());
                    } else if (n.getName().equals("Client Name")) {
                        if(m.getClientID() !=0) {
                            hashMap.put("Client Name", AllClientRepository.getInstance().getClientByClientID(m.getClientID()).getName());
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
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_COLOCATION)
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
                    else if(n.getName().equals("Incident Type")){
                        hashMap.put("Incident Type", CoLocationConstants.applicationTypeNameMap.get(m.getIncident()));
                    }
                    else if(n.getName().equals("Connection Status")){
                        hashMap.put("Connection Status", CoLocationConstants.applicationTypeNameMap.get(m.getStatus()));
                    }

                    else if(n.getName().equals("Rack Needed")){
                        hashMap.put("Rack Needed",m.isRackNeeded());
                    }
                    else if(n.getName().equals("Rack Size")){
                        hashMap.put("Rack Size",m.getRackSize());
                    }
                    else if(n.getName().equals("Rack Space")){
                        hashMap.put("Rack Space",m.getRackSpace());
                    }
                    else if(n.getName().equals("Fiber Needed")){
                        hashMap.put("Fiber Needed",m.isFiberNeeded());
                    }
                    else if(n.getName().equals("Power Needed")){
                        hashMap.put("Power Needed",m.isPowerNeeded());
                    }else if(n.getName().equals("Fiber Type")){
                        CoLocationInventoryTemplateDTO coLocationInventoryTemplateDTO = inventoryTemplateMap.getOrDefault((long)m.getFiberType(), null);
                        String fiberType = String.valueOf(coLocationInventoryTemplateDTO !=null ? coLocationInventoryTemplateDTO.getValue() : "N/A");
                        hashMap.put("Fiber Type",fiberType); // TODO: 4/25/2019 forhad
                    }
                    else if(n.getName().equals("Floor Space Needed")){
                        hashMap.put("Floor Space Needed",m.isFloorSpaceNeeded());
                    }
                    else if(n.getName().equals("Floor Space Type")){
                        CoLocationInventoryTemplateDTO coLocationInventoryTemplateDTO = inventoryTemplateMap.getOrDefault((long)m.getFloorSpaceType(), null);
                        String floorSpaceType = String.valueOf(coLocationInventoryTemplateDTO !=null ? coLocationInventoryTemplateDTO.getValue() : "N/A");
                        hashMap.put("Floor Space Type",floorSpaceType); // TODO: 4/25/2019 forhad
                    }
                    else if(n.getName().equals("Power Amount")){
                        hashMap.put("Power Amount",m.getPowerAmount());
                    }
                    else if(n.getName().equals("Fiber Core")){
                        hashMap.put("Fiber Core",m.getFiberCore());
                    }
                    else if(n.getName().equals("Floor Space Amount")){
                        hashMap.put("Floor Space Amount",m.getFloorSpaceAmount());
                    }
                });
                mapList.add(hashMap);
            });
        }
        else{
            throw  new RequestFailureException("No Connections Found");
        }
        Report colocationConnectionReport = new Report();
        colocationConnectionReport.setMapArrayList(mapList);
        colocationConnectionReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return colocationConnectionReport;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Report getReportBatchOperationForCoLocationApplication(PostEntity postEntity, LoginDTO loginDTO)throws Exception{

        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap =  coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();


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
                    else if(s.getName().equals("Rack Needed")){
                        searchCriteria.put("Rack Needed",sID.toString());
                    }
                    else if(s.getName().equals("Rack Size")){
                        searchCriteria.put("Rack Size",sID.toString());
                    }
                    else if(s.getName().equals("Rack Space")){
                        searchCriteria.put("Rack Space",sID.toString());
                    }
                    else if(s.getName().equals("Fiber Needed")){
                        searchCriteria.put("Fiber Needed",sID.toString());
                    }
                    else if(s.getName().equals("Power Needed")){
                        searchCriteria.put("Power Needed",sID.toString());
                    }else if(s.getName().equals("Fiber Type")){
                        searchCriteria.put("Fiber Type",sID.toString());
                    }
                    else if(s.getName().equals("Floor Space Needed")){
                        searchCriteria.put("Floor Space Needed",sID.toString());
                    }
                    else if(s.getName().equals("Floor Space Type")){
                        searchCriteria.put("Floor Space Type",sID.toString());
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

                    else if(s.getName().equals("Power Amount")){
                        searchCriteria.put("Power Amount",sInput);
                    }
                    else if(s.getName().equals("Fiber Core")){
                        searchCriteria.put("Fiber Core",sInput);
                    }
                    else if(s.getName().equals("Floor Space Amount")){
                        searchCriteria.put("Floor Space Amount",sInput);
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
        Class<CoLocationApplicationDTO> classObject = CoLocationApplicationDTO.class;
        String conditionalString = " where 1=1 ";

        if (searchCriteria.get("Application Id") != null) {
            conditionalString += " and " + getColumnName(classObject, "applicationID") + " = " + (long) Double.parseDouble(searchCriteria.get("Application Id")) + " ";
        }

        if (searchCriteria.get("clientID") != null) {
            conditionalString += " and " + getColumnName(classObject, "clientID") + " = " + (long) Double.parseDouble(searchCriteria.get("clientID")) + " ";
        }
        if (searchCriteria.get("User Name") != null) {
            long userId = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name")).getUserID();
            conditionalString += " and " + getColumnName(classObject, "userID") + " = " + userId;
        }

        if (searchCriteria.get("Submission From") != null && searchCriteria.get("Submission To") != null) {
            conditionalString += " and " + getColumnName(classObject, "submissionDate") + " > " + (long) Double.parseDouble(searchCriteria.get("Submission From")) + " "
                    + " and " + getColumnName(classObject, "submissionDate") + " < " + (long) Double.parseDouble(searchCriteria.get("Submission To")) + " ";
        }
        if (searchCriteria.get("Application Status") != null) {
            conditionalString += " and " + getColumnName(classObject, "isServiceStarted") + " = " + (int) Integer.parseInt(searchCriteria.get("Application Status")) + " ";
        }

        if(searchCriteria.get("Rack Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Rack Needed"))==1?true:false)+" ";
        }
        if(searchCriteria.get("Rack Size")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackSize")+" = "+(int) Double.parseDouble(searchCriteria.get("Rack Size"))+" ";
        }
        if(searchCriteria.get("Rack Space")!=null){
            conditionalString+=" and "+getColumnName(classObject, "rackSpace")+" = "+ (int) Double.parseDouble(searchCriteria.get("Rack Space"))+" ";
        }
        if(searchCriteria.get("Fiber Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Fiber Needed"))==1?true:false)+" ";
        }
        if(searchCriteria.get("Power Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "powerNeeded")+" = "+ ((int)Double.parseDouble(searchCriteria.get("Power Needed"))==1?true:false)+" ";
        }
        if(searchCriteria.get("Fiber Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberType")+" = "+ (int) Double.parseDouble(searchCriteria.get("Fiber Type"))+" ";
        }
        if(searchCriteria.get("Floor Space Needed")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceNeeded")+" = "+  ((int)Double.parseDouble(searchCriteria.get("Floor Space Needed"))==1?true:false)+" ";
        }
        if(searchCriteria.get("Floor Space Type")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceType")+" = "+ (int) Double.parseDouble(searchCriteria.get("Floor Space Type"))+" ";
        }
        if(searchCriteria.get("Power Amount")!=null){
            conditionalString+=" and "+getColumnName(classObject, "powerAmount")+" = "+  Double.parseDouble(searchCriteria.get("Power Amount"))+" ";
        }
        if(searchCriteria.get("Fiber Core")!=null){
            conditionalString+=" and "+getColumnName(classObject, "fiberCore")+" = "+  (int)Double.parseDouble(searchCriteria.get("Fiber Core"))+" ";
        }
        if(searchCriteria.get("Floor Space Amount")!=null){
            conditionalString+=" and "+getColumnName(classObject, "floorSpaceAmount")+" = "+ Double.parseDouble(searchCriteria.get("Floor Space Amount"))+"  ";
        }


        List<CoLocationApplicationDTO> applications = globalService.getAllObjectListByCondition(classObject, conditionalString);

        for (Iterator<CoLocationApplicationDTO> iter = applications.listIterator(); iter.hasNext(); ) {
            CoLocationApplicationDTO a = iter.next();
            ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(a.getClientID(), ModuleConstants.Module_ID_COLOCATION);
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
            for (Iterator<CoLocationApplicationDTO> iter = applications.listIterator(); iter.hasNext(); ) {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserName(searchCriteria.get("User Name"));
                CoLocationApplicationDTO a = iter.next();
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
                applications=getCoLocationApplicationListOfStateGroup(flowStates,applications);
            }

        }

        if (searchCriteria.get("Application Phase") != null) {
            if(flowStates.size()>0){
                applications=getCoLocationApplicationListOfStateGroup(flowStates,applications);
            }

        }

        List<CoLocationApplicationDTO> finalApplications = applications;
        postEntity.getSelectedOrderItems().forEach(t -> {
            if (t.getName().equals("Application Id")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getApplicationID));
            } else if (t.getName().equals("Application State")) {
                Collections.sort(finalApplications, Comparator.comparingInt(CoLocationApplicationDTO::getState));
            }  else if (t.getName().equals("Submission Date")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getSubmissionDate));
            } else if (t.getName().equals("User Name")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getUserID));
            }
            else if (t.getName().equals("Rack Space")) {
                Collections.sort(finalApplications, Comparator.comparingInt(CoLocationApplicationDTO::getRackSpace));
            }

            else if (t.getName().equals("Rack Size")) {
                Collections.sort(finalApplications, Comparator.comparingInt(CoLocationApplicationDTO::getRackTypeID));
            }  else if (t.getName().equals("Floor Space Amount")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(CoLocationApplicationDTO::getFloorSpaceAmount));
            } else if (t.getName().equals("Fiber Core")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getFiberCore));
            }
            else if (t.getName().equals("Power Amount")) {
                Collections.sort(finalApplications, Comparator.comparingDouble(CoLocationApplicationDTO::getPowerAmount));
            }  else if (t.getName().equals("Floor Space Type")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getFloorSpaceType));
            } else if (t.getName().equals("Fiber Type")) {
                Collections.sort(finalApplications, Comparator.comparingLong(CoLocationApplicationDTO::getFiberType));
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
                        else {
                            hashMap.put("Client Name", "N/A");

                        }
                    }
                    else if (n.getName().equals("User Name")) {
                        if (m.getUserID()!=null && m.getUserID() > 0) {
                            UserDTO usr = UserRepository.getInstance().getUserDTOByUserID(m.getUserID());
                            if (usr != null) hashMap.put("User Name", usr.getUsername());

                        }
                        else{
                            hashMap.put("User Name", "N/A");
                        }
                    }
                    else if (n.getName().equals("Registrant Type") || n.getName().equals("Registrant Category")) {
                        ClientDetailsDTO clientDetails = AllClientRepository.getInstance().getClientListByModuleID(ModuleConstants.Module_ID_COLOCATION)
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

                    else if(n.getName().equals("Rack Needed")){
                        hashMap.put("Rack Needed",m.isRackNeeded());
                    }
                    else if(n.getName().equals("Rack Size")){
                        CoLocationInventoryTemplateDTO coLocationInventoryTemplateDTO = inventoryTemplateMap.getOrDefault((long)m.getRackTypeID(), null);
                        String rackType = String.valueOf(coLocationInventoryTemplateDTO !=null ? coLocationInventoryTemplateDTO.getValue() : "N/A");

                        hashMap.put("Rack Size",rackType); // TODO: 4/25/2019 forhad
                    }
                    else if(n.getName().equals("Rack Space")){
                        hashMap.put("Rack Space",m.getRackSpace());
                    }
                    else if(n.getName().equals("Fiber Needed")){
                        hashMap.put("Fiber Needed",m.isFiberNeeded());
                    }
                    else if(n.getName().equals("Power Needed")){
                        hashMap.put("Power Needed",m.isPowerNeeded());
                    }else if(n.getName().equals("Fiber Type")){
                        CoLocationInventoryTemplateDTO coLocationInventoryTemplateDTO = inventoryTemplateMap.getOrDefault((long)m.getFiberType(), null);
                        String fiberType = String.valueOf(coLocationInventoryTemplateDTO !=null ? coLocationInventoryTemplateDTO.getValue() : "N/A");

                        hashMap.put("Fiber Type",fiberType); // TODO: 4/25/2019 forhad
                    }
                    else if(n.getName().equals("Floor Space Needed")){
                        hashMap.put("Floor Space Needed",m.isFloorSpaceNeeded());
                    }
                    else if(n.getName().equals("Floor Space Type")){
                        CoLocationInventoryTemplateDTO coLocationInventoryTemplateDTO = inventoryTemplateMap.getOrDefault((long)m.getFloorSpaceType(), null);
                        String floorSpaceType = String.valueOf(coLocationInventoryTemplateDTO !=null ? coLocationInventoryTemplateDTO.getValue() : "N/A");

                        hashMap.put("Floor Space Type",floorSpaceType); // TODO: 4/25/2019 forhad
                    }
                    else if(n.getName().equals("Power Amount")){
                        hashMap.put("Power Amount",m.getPowerAmount());
                    }
                    else if(n.getName().equals("Fiber Core")){
                        hashMap.put("Fiber Core",m.getFiberCore());
                    }
                    else if(n.getName().equals("Floor Space Amount")){
                        hashMap.put("Floor Space Amount",m.getFloorSpaceAmount());
                    }

                    else if (n.getName().equals("Application State")) {
                        if(m.getState()>0){
                            hashMap.put("Application State", FlowRepository.getInstance().getFlowStateByFlowStateId(m.getState()).getViewDescription());
                        }
                        else{
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
        Report coLocationApplicationReport = new Report();
        coLocationApplicationReport.setMapArrayList(mapList);
        //coLocationApplicationReport.setCoLocationApplicationDTOS(applications);
        coLocationApplicationReport.setReportDisplayColumns(postEntity.getSelectedDisplayItems());
        return coLocationApplicationReport;
    }

    @Transactional
    private List<CoLocationApplicationDTO> getCoLocationApplicationListOfStateGroup(List<FlowState> flowStates, List<CoLocationApplicationDTO> applications)throws Exception{
        List<CoLocationApplicationDTO> filteredApplication=new ArrayList<>();
        List<CoLocationApplicationDTO> coLocationApplicationDTOS = globalService.getAllObjectListByCondition(CoLocationApplicationDTO.class,
                new CoLocationApplicationDTOConditionBuilder()
                        .Where()
                        .stateIn(flowStates.stream().map(p->p.getId()).collect(Collectors.toList()))
                        .getCondition()
        );

        Map<Long,CoLocationApplicationDTO> mappedStateWiseApp = coLocationApplicationDTOS
                .stream()
                .collect(Collectors.toMap(CoLocationApplicationDTO::getApplicationID, Function.identity()));


        applications.forEach(
                t -> {
                    CoLocationApplicationDTO coLocationApplicationDTO = mappedStateWiseApp.getOrDefault(t.getApplicationID(), null);
                    if (coLocationApplicationDTO != null) {
                        filteredApplication.add(t);
                    }
                }
        );

        return filteredApplication;
    }
    //endregion


}
