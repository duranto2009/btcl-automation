package reportnew;

import annotation.Transactional;
import application.ApplicationType;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import coLocation.CoLocationConstants;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.ObjectPair;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import flow.FlowService;
import flow.entity.FlowState;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.constants.NIXConstants;
import requestMapping.Service;
import upstream.UpstreamConstants;
import upstream.inventory.UpstreamInventoryService;
import util.TransactionType;
import vpn.VPNConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportCheckBoxBuilder {

    @Service
    FlowService flowService;

    @Service
    CoLocationInventoryTemplateService coLocationInventoryTemplateService;

    @Service
    private UpstreamInventoryService upstreamInventoryService;

    //region application checkbox
    @Transactional(transactionType = TransactionType.READONLY)
    private JsonArray getCommonApplcationSearchCriteria(int moduleId)throws Exception {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();

        reportSearchCriteria.setName("Application Id");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
        JsonObject jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Client Name");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> clientDropDown = new ArrayList<>();
        AllClientRepository.getInstance().getClientListByModuleID(moduleId).stream().forEach(s->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(s.getClientID(),s.getName());
            clientDropDown.add(lliDropdownPair);
        });
        reportSearchCriteria.setList(clientDropDown);
        reportSearchCriteria.setDataType(ReportConstant.STRING);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Registrant Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> typeList = new ArrayList<>();

        RegistrantTypeConstants.RegistrantTypeName.keySet().forEach(t->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(t,RegistrantTypeConstants.RegistrantTypeName.get(t));
            typeList.add(lliDropdownPair);
        });
        reportSearchCriteria.setList(typeList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Registrant Category");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> categoryList = new ArrayList<>();

        RegistrantCategoryConstants.RegistrantCategoryName.keySet().forEach(t->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(t,RegistrantCategoryConstants.RegistrantCategoryName.get(t));
            categoryList.add(lliDropdownPair);
        });
        reportSearchCriteria.setList(categoryList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("User Name");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.STRING);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application State");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        Map<String,List<FlowState>> stringMap = flowService.flowStateGroup(moduleId);
        List<LLIDropdownPair> applicationStateList =new ArrayList<>();
        int i=0;
        stringMap.keySet().forEach(state->{
            applicationStateList.add(new LLIDropdownPair(i+1,state,stringMap.get(state)));
        });
        reportSearchCriteria.setList(applicationStateList); // TODO: 4/17/2019 application state drowp down prepare
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application Phase");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        Map<String,List<FlowState>> phaseMap = flowService.flowStateGroupByPhase(moduleId);
        List<LLIDropdownPair> applicationPhaseList =new ArrayList<>();
        int j=0;
        phaseMap.keySet().forEach(phase->{
            applicationPhaseList.add(new LLIDropdownPair(j+1,phase,phaseMap.get(phase)));
        });
        reportSearchCriteria.setList(applicationPhaseList); // TODO: 4/17/2019 application state drowp down prepare
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application Status");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> appStatusList = new ArrayList<>();
        appStatusList.add(new LLIDropdownPair(0,"Not Completed"));
        appStatusList.add(new LLIDropdownPair(1,"Completed"));
        reportSearchCriteria.setList(appStatusList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Created Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Submission Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        return jsonArray;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public JsonObject getCheckBoxesApplication(int module, JsonObject jsonObject, LoginDTO loginDTO) throws Exception{
        // TODO: 4/8/2019  the below search criterias may need to be changed for refined search item builder
        JsonArray criteria = getCriteriaArrayForApplication(module);
        jsonObject.add("criteria",criteria);
        jsonObject.add("display",criteria);
        jsonObject.add("orderby",criteria);
        return jsonObject;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    private JsonArray getCriteriaArrayForApplication(int module)throws Exception {
        Gson gson = new Gson();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();
        JsonArray jsonArray = new JsonArray();
        jsonArray = getCommonApplcationSearchCriteria(module);
        if(module == ModuleConstants.Module_ID_LLI){
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("BandWidth");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Vendor Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_VPN){
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Layer Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> layerTypeList = new ArrayList<>();
            layerTypeList.add(new LLIDropdownPair(2,"Layer 2"));
            layerTypeList.add(new LLIDropdownPair(3,"Layer 3"));
            reportSearchCriteria.setList(layerTypeList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Loop Provider");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> loopProviderList = new ArrayList<>();
            loopProviderList.add(new LLIDropdownPair(1,"BTCL"));
            loopProviderList.add(new LLIDropdownPair(2,"Client"));
            reportSearchCriteria.setList(loopProviderList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Connection Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> connectionTypeList = new ArrayList<>();
            LLIConnectionConstants.connectionTypeMap.keySet().forEach(type->{
                connectionTypeList.add(new LLIDropdownPair(type,LLIConnectionConstants.connectionTypeMap.get(type)));
            });
            reportSearchCriteria.setList(connectionTypeList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("BandWidth");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Vendor Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_NIX){
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Loop Provider");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> loopProviderList = new ArrayList<>();
            loopProviderList.add(new LLIDropdownPair(1,"BTCL"));
            loopProviderList.add(new LLIDropdownPair(2,"Client"));
            reportSearchCriteria.setList(loopProviderList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Port Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> portTypeList = new ArrayList<>();
            portTypeList.add(new LLIDropdownPair(1,"FE"));
            portTypeList.add(new LLIDropdownPair(2,"GE"));
            portTypeList.add(new LLIDropdownPair(3,"TEN_GE"));
            reportSearchCriteria.setList(portTypeList);
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);


            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Port Count");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Vendor Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }
        else if( module == ModuleConstants.Module_ID_COLOCATION){
            // TODO: 4/23/2019 other search criteria need to be added

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Rack Needed");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> rackList = new ArrayList<>();
            rackList.add(new LLIDropdownPair(1,"Yes"));
            rackList.add(new LLIDropdownPair(0,"No"));
            reportSearchCriteria.setList(rackList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> rackSizeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE);
            reportSearchCriteria.setName("Rack Size");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> rackSizeDropDownList = new ArrayList<>();
            rackSizeList.forEach(type->{
                rackSizeDropDownList.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(rackSizeDropDownList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> rackSpaceList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE);
            reportSearchCriteria.setName("Rack Space");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair>rackSpaceDropDown = new ArrayList<>();
            rackSpaceList.forEach(space->{
                rackSpaceDropDown.add(new LLIDropdownPair(space.getId(),space.getValue(),space));
            });
            reportSearchCriteria.setList(rackSpaceDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Power Needed");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setList(rackList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Power Amount");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);


            List<CoLocationInventoryTemplateDTO> powerTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE);
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Power Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> powerList = new ArrayList<>();
            powerTypeList.forEach(type->{
                powerList.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(powerList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Fiber Needed");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setList(rackList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Fiber Core");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> fiberTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE);
            reportSearchCriteria.setName("Fiber Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> fiberTypeDropDown = new ArrayList<>();
            fiberTypeList.forEach(type->{
                fiberTypeDropDown.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(fiberTypeDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Floor Space Needed");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setList(rackList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Floor Space Amount");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.LONG);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();

            List<CoLocationInventoryTemplateDTO> floorSpaceTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE);

            reportSearchCriteria.setName("Floor Space Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> floorSpaceDropDown = new ArrayList<>();
            floorSpaceTypeList.forEach(type->{
                floorSpaceDropDown.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(floorSpaceDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }

        else if( module == ModuleConstants.Module_ID_UPSTREAM){
            jsonArray = getUpStreamApplcationSearchCriteria(module);
            return jsonArray;
        }
        else {
            throw new RequestFailureException("There is no Module with this number");
        }
    }
    //endregion

    //region Connection Checkbox
    private JsonArray getCommonCriteriaArrayForConnection() {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Client Name");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> clientDropDown = new ArrayList<>();
        AllClientRepository.getInstance().getAllVpnCleint().stream().forEach(s->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(s.getClientID(),s.getName());
            clientDropDown.add(lliDropdownPair);

        });
        reportSearchCriteria.setList(clientDropDown);
        reportSearchCriteria.setDataType(ReportConstant.STRING);
        JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
        JsonObject jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Registrant Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> typeList = new ArrayList<>();

        RegistrantTypeConstants.RegistrantTypeName.keySet().forEach(t->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(t,RegistrantTypeConstants.RegistrantTypeName.get(t));
            typeList.add(lliDropdownPair);
        });
        reportSearchCriteria.setList(typeList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Registrant Category");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> categoryList = new ArrayList<>();

        RegistrantCategoryConstants.RegistrantCategoryName.keySet().forEach(t->{
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(t,RegistrantCategoryConstants.RegistrantCategoryName.get(t));
            categoryList.add(lliDropdownPair);
        });
        reportSearchCriteria.setList(categoryList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Active Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);
        return jsonArray;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public JsonObject getCheckBoxesConnection(int module, JsonObject jsonObject, LoginDTO loginDTO)throws Exception {
        JsonArray jsonArray = new JsonArray();
        // TODO: 4/8/2019  the below search criterias would be changed for refined search item builder
        JsonArray criteria = getCriteriaArrayForConnection(module);
        jsonObject.add("criteria",criteria);
        jsonObject.add("display",criteria);
        jsonObject.add("orderby",criteria);

        return jsonObject;

    }

    @Transactional(transactionType = TransactionType.READONLY)
    private JsonArray getCriteriaArrayForConnection(int module) throws Exception{
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();
        jsonArray = getCommonCriteriaArrayForConnection();
        if(module == ModuleConstants.Module_ID_LLI){
            reportSearchCriteria.setName("Connection Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("BandWidth");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Connection Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> connectionTypeList = new ArrayList<>();
            LLIConnectionConstants.connectionTypeMap.keySet().forEach(connectionType->{
                connectionTypeList.add(new LLIDropdownPair(connectionType,LLIConnectionConstants.connectionTypeMap.get(connectionType)));
            });
            reportSearchCriteria.setList(connectionTypeList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Connection Status");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            List<LLIDropdownPair> statusLliDropdownPairs = new ArrayList<>();
            LLIConnectionConstants.connectionStatusMap.keySet().forEach(status->{
                statusLliDropdownPairs.add(new LLIDropdownPair(status,LLIConnectionConstants.connectionStatusMap.get(status)));
            });
            reportSearchCriteria.setList(statusLliDropdownPairs);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Incident Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> incidentList = new ArrayList<>();
            LLIConnectionConstants.applicationTypeNameMap.keySet().forEach(incident->{
                incidentList.add(new LLIDropdownPair(incident,LLIConnectionConstants.applicationTypeNameMap.get(incident)));
            });
            reportSearchCriteria.setList(incidentList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_VPN){
            reportSearchCriteria.setName("Link Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("BandWidth");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Link Status");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            List<LLIDropdownPair> statusLliDropdownPairs = new ArrayList<>();
            LLIDropdownPair lliDropdownPair = new LLIDropdownPair(
                    VPNConstants.Status.VPN_ACTIVE.ordinal(), VPNConstants.Status.VPN_ACTIVE.getStatus());
            LLIDropdownPair lliDropdownPair1 = new LLIDropdownPair(
                    VPNConstants.Status.VPN_TD.ordinal(),VPNConstants.Status.VPN_TD.getStatus());
            LLIDropdownPair lliDropdownPair2 = new LLIDropdownPair(
                    VPNConstants.Status.VPN_CLOSE.ordinal(),VPNConstants.Status.VPN_CLOSE.getStatus());
            statusLliDropdownPairs.add(lliDropdownPair);
            statusLliDropdownPairs.add(lliDropdownPair1);
            statusLliDropdownPairs.add(lliDropdownPair2);
            reportSearchCriteria.setList(statusLliDropdownPairs);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Incident Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> incidentList = new ArrayList<>();
            incidentList.add(new LLIDropdownPair(1,VPNConstants.INCIDENT.NEW.getIncidentName()));
            incidentList.add(new LLIDropdownPair(2,VPNConstants.INCIDENT.UPGRADE.getIncidentName()));
            incidentList.add(new LLIDropdownPair(3,VPNConstants.INCIDENT.DOWNGRADE.getIncidentName()));
            incidentList.add(new LLIDropdownPair(4,VPNConstants.INCIDENT.SHIFT.getIncidentName()));
            incidentList.add(new LLIDropdownPair(5,VPNConstants.INCIDENT.CLOSE.getIncidentName()));
            incidentList.add(new LLIDropdownPair(6,VPNConstants.INCIDENT.TD.getIncidentName()));
            incidentList.add(new LLIDropdownPair(7,VPNConstants.INCIDENT.RECONNECT.getIncidentName()));
            incidentList.add(new LLIDropdownPair(8,VPNConstants.INCIDENT.OWNER_CHANGE.getIncidentName()));
            reportSearchCriteria.setList(incidentList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Link Distance");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_NIX){
            reportSearchCriteria.setName("Connection Name");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.STRING);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

       /* reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Connection Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> connectionTypeList = new ArrayList<>();
        LLIConnectionConstants.connectionTypeMap.keySet().forEach(connectionType->{
            connectionTypeList.add(new LLIDropdownPair(connectionType,LLIConnectionConstants.connectionTypeMap.get(connectionType)));
        });
        reportSearchCriteria.setList(connectionTypeList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);*/

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Connection Status");
            reportSearchCriteria.setType("list");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            List<LLIDropdownPair> statusLliDropdownPairs = new ArrayList<>();
            LLIConnectionConstants.connectionStatusMap.keySet().forEach(status->{
                statusLliDropdownPairs.add(new LLIDropdownPair(status, NIXConstants.nixConnectionStatus.get(status)));
            });
            reportSearchCriteria.setList(statusLliDropdownPairs);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Incident Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> incidentList = new ArrayList<>();
            LLIConnectionConstants.applicationTypeNameMap.keySet().forEach(incident->{
                incidentList.add(new LLIDropdownPair(incident,NIXConstants.nixapplicationTypeNameMap.get(incident)));
            });
            reportSearchCriteria.setList(incidentList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_COLOCATION){
            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> rackSizeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE);
            reportSearchCriteria.setName("Rack Size");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> rackSizeDropDownList = new ArrayList<>();
            rackSizeList.forEach(type->{
                rackSizeDropDownList.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(rackSizeDropDownList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> rackSpaceList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE);
            reportSearchCriteria.setName("Rack Space");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair>rackSpaceDropDown = new ArrayList<>();
            rackSpaceList.forEach(space->{
                rackSpaceDropDown.add(new LLIDropdownPair(space.getId(),space.getValue(),space));
            });
            reportSearchCriteria.setList(rackSpaceDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Power Amount");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);


            List<CoLocationInventoryTemplateDTO> powerTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE);
            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Power Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> powerList = new ArrayList<>();
            powerTypeList.forEach(type->{
                powerList.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(powerList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Fiber Core");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            List<CoLocationInventoryTemplateDTO> fiberTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE);
            reportSearchCriteria.setName("Fiber Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> fiberTypeDropDown = new ArrayList<>();
            fiberTypeList.forEach(type->{
                fiberTypeDropDown.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(fiberTypeDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Floor Space Amount");
            reportSearchCriteria.setType("input");
            reportSearchCriteria.setDataType(ReportConstant.LONG);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();

            List<CoLocationInventoryTemplateDTO> floorSpaceTypeList =  coLocationInventoryTemplateService.getInventoryTemplate(CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE);

            reportSearchCriteria.setName("Floor Space Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> floorSpaceDropDown = new ArrayList<>();
            floorSpaceTypeList.forEach(type->{
                floorSpaceDropDown.add(new LLIDropdownPair(type.getId(),type.getValue(),type));
            });
            reportSearchCriteria.setList(floorSpaceDropDown);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);

            reportSearchCriteria = new ReportSearchCriteria();
            reportSearchCriteria.setName("Incident Type");
            reportSearchCriteria.setType("list");
            List<LLIDropdownPair> incidentList = new ArrayList<>();
            CoLocationConstants.connectionTypeNameMap.keySet().forEach(incident->{
                incidentList.add(new LLIDropdownPair(incident,CoLocationConstants.connectionTypeNameMap.get(incident)));
            });

            reportSearchCriteria.setList(incidentList);
            reportSearchCriteria.setDataType(ReportConstant.INTEGER);
            jsonElement = gson.toJsonTree(reportSearchCriteria);
            jsonObject1 = (JsonObject) jsonElement;
            jsonArray.add(jsonObject1);
            return jsonArray;
        }
        else if(module == ModuleConstants.Module_ID_UPSTREAM){
            jsonArray = getUpstreamConnectionCriteria();
            return jsonArray;
        }
        else {
            throw new RequestFailureException("No Module Match for this number");
        }
    }
    //endregion

    @Transactional(transactionType = TransactionType.READONLY)
    private JsonArray getUpStreamApplcationSearchCriteria(int module_id_upstream) throws Exception {
        Gson gson = new Gson();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();
        JsonArray jsonArray = new JsonArray();
        reportSearchCriteria.setName("Application Id");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
        JsonObject jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair> applicationTypeList =new ArrayList<>();
        applicationTypeList.add(new LLIDropdownPair(ApplicationType.UPSTREAM_NEW_REQUEST.ordinal(),"Upstream New request"));
        applicationTypeList.add(new LLIDropdownPair(ApplicationType.UPSTREAM_UPGRADE.ordinal(),"Upstream upgrade request"));
        applicationTypeList.add(new LLIDropdownPair(ApplicationType.UPSTREAM_DOWNGRADE.ordinal(),"Upstream downgrade request"));
        applicationTypeList.add(new LLIDropdownPair(ApplicationType.UPSTREAM_CONTRACT_EXTENSION_REQUEST.ordinal(),"Upstream Contract extension request"));
        applicationTypeList.add(new LLIDropdownPair(ApplicationType.UPSTREAM_CONTRACT_CLOSE_REQUEST.ordinal(),"Upstream Contract close request"));
        reportSearchCriteria.setList(applicationTypeList);
        reportSearchCriteria.setDataType(ReportConstant.STRING);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);


        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> typeOfBWList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.TYPE_OF_BW.getInventoryItemName());
        reportSearchCriteria.setName("Bandwidth Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair>bdTypeDropDown = new ArrayList<>();
        typeOfBWList.forEach(pair->{
            bdTypeDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(bdTypeDropDown);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);
        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Bandwidth Capacity");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> mediaList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.MEDIA.getInventoryItemName());
        List<LLIDropdownPair>mediaDropDown = new ArrayList<>();
        mediaList.forEach(pair->{
            mediaDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(mediaDropDown);
        reportSearchCriteria.setName("Media");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> serviceLocationList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION.getInventoryItemName());
        List<LLIDropdownPair>serviceLocationDropDown = new ArrayList<>();
        serviceLocationList.forEach(pair->{
            serviceLocationDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(serviceLocationDropDown);
        reportSearchCriteria.setName("Service Location");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> providerLocationList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER_LOCATION.getInventoryItemName());
        List<LLIDropdownPair>providerLocationDropDown = new ArrayList<>();
        providerLocationList.forEach(pair->{
            providerLocationDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(providerLocationDropDown);
        reportSearchCriteria.setName("Provider Location");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> providerList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER.getInventoryItemName());
        List<LLIDropdownPair>providerDropDown = new ArrayList<>();
        providerList.forEach(pair->{
            providerDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(providerDropDown);
        reportSearchCriteria.setName("Provider");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Bandwidth Price");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Srf Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application State");
        reportSearchCriteria.setType("list");
       /* Map<String,List<FlowState>> stateM = flowService.flowStateGroupByPhase(module_id_upstream);
        List<LLIDropdownPair> applicationPhaseList =new ArrayList<>();
        int j=0;
        phaseMap.keySet().forEach(phase->{
            applicationPhaseList.add(new LLIDropdownPair(j+1,phase,phaseMap.get(phase)));
        });
        reportSearchCriteria.setList(applicationPhaseList);*/
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Application Phase");
        reportSearchCriteria.setType("list");
        Map<String,List<FlowState>> phaseMap = flowService.flowStateGroupByPhase(module_id_upstream);
        List<LLIDropdownPair> applicationPhaseList =new ArrayList<>();
        int j=0;
        phaseMap.keySet().forEach(phase->{
            applicationPhaseList.add(new LLIDropdownPair(j+1,phase,phaseMap.get(phase)));
        });
        reportSearchCriteria.setList(applicationPhaseList);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        return jsonArray;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    private JsonArray getUpstreamConnectionCriteria() throws Exception{
        Gson gson = new Gson();
        ReportSearchCriteria reportSearchCriteria = new ReportSearchCriteria();
        JsonArray jsonArray = new JsonArray();
        reportSearchCriteria.setName("Contract Name");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.STRING);
        JsonElement jsonElement = gson.toJsonTree(reportSearchCriteria);
        JsonObject jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> typeOfBWList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.TYPE_OF_BW.getInventoryItemName());
        reportSearchCriteria.setName("Bandwidth Type");
        reportSearchCriteria.setType("list");
        List<LLIDropdownPair>bdTypeDropDown = new ArrayList<>();
        typeOfBWList.forEach(pair->{
            bdTypeDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(bdTypeDropDown);
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);
        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Bandwidth Capacity");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> mediaList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.MEDIA.getInventoryItemName());
        List<LLIDropdownPair>mediaDropDown = new ArrayList<>();
        mediaList.forEach(pair->{
            mediaDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(mediaDropDown);
        reportSearchCriteria.setName("Media");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> serviceLocationList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION.getInventoryItemName());
        List<LLIDropdownPair>serviceLocationDropDown = new ArrayList<>();
        serviceLocationList.forEach(pair->{
            serviceLocationDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(serviceLocationDropDown);
        reportSearchCriteria.setName("Service Location");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> providerLocationList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER_LOCATION.getInventoryItemName());
        List<LLIDropdownPair>providerLocationDropDown = new ArrayList<>();
        providerLocationList.forEach(pair->{
            providerLocationDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(providerLocationDropDown);
        reportSearchCriteria.setName("Provider Location");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        List<ObjectPair<Long, String>> providerList = upstreamInventoryService.getAllUpstreamItemsInPairByItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER.getInventoryItemName());
        List<LLIDropdownPair>providerDropDown = new ArrayList<>();
        providerList.forEach(pair->{
            providerDropDown.add(new LLIDropdownPair(pair.key,pair.value));
        });
        reportSearchCriteria.setList(providerDropDown);
        reportSearchCriteria.setName("Provider");
        reportSearchCriteria.setType("list");
        reportSearchCriteria.setDataType(ReportConstant.INTEGER);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Bandwidth Price");
        reportSearchCriteria.setType("input");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Srf Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        reportSearchCriteria = new ReportSearchCriteria();
        reportSearchCriteria.setName("Active Date");
        reportSearchCriteria.setType("date");
        reportSearchCriteria.setDataType(ReportConstant.LONG);
        jsonElement = gson.toJsonTree(reportSearchCriteria);
        jsonObject1 = (JsonObject) jsonElement;
        jsonArray.add(jsonObject1);

        return jsonArray;
    }
}
