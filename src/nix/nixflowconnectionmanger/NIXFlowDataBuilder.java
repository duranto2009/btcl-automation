package nix.nixflowconnectionmanger;

import annotation.Transactional;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import flow.FlowService;
import flow.entity.FlowState;
import inventory.InventoryAttributeValue;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Comments.Comments;
import lli.Comments.CommentsService;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import location.ZoneService;
import login.LoginDTO;
import nix.application.NIXApplication;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.downgrade.NIXDowngradeApplicationService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.application.upgrade.NIXUpgradeApplicationService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.efr.NIXEFR;
import nix.efr.NIXEFRService;
import nix.ifr.NIXIFR;
import nix.ifr.NIXIFRService;
import nix.office.NIXOfficeService;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import vpn.FlowConnectionManager.FlowDataBuilder;

import java.util.*;
import java.util.stream.Collectors;

//the comments for nix might need to change later
// end

public class NIXFlowDataBuilder {
    NIXEFRService nixefrService = ServiceDAOFactory.getService(NIXEFRService.class);
    NIXIFRService nixifrService = ServiceDAOFactory.getService(NIXIFRService.class);
    NIXApplicationOfficeService nixApplicationOfficeService = ServiceDAOFactory.getService(NIXApplicationOfficeService.class);
    NIXApplicationLocalLoopService nixApplicationLocalLoopService = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);
    NIXOfficeService nixOfficeService = ServiceDAOFactory.getService(NIXOfficeService.class);
    NIXUpgradeApplicationService nixUpgradeApplicationService = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class);
    BillService billService=ServiceDAOFactory.getService(BillService.class);
    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    public static final int vendorRoleID = 16020;
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);

    //region nix 12/12/2018

    public JsonArray UILabelBuilder(JsonObject jsonObject) {
        JsonArray jsonArray = new JsonArray();
        Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            String key = ((Map.Entry) obj).getKey().toString();
            String value = ((Map.Entry) obj).getValue().toString();
            JsonObject object = new JsonObject();
            object.addProperty("Label", key);
            if (value.contains("\"")) {
                object.addProperty("Value", value.replaceAll("\"", " "));
            } else {
                object.addProperty("Value", value);
            }

            jsonArray.add(object);
        }
        return jsonArray;

    }

    public JsonArray userArrayBuilder(long roleID,boolean isBTCLPersonal) {


        //for ifr need to filter with state and user role

        JsonArray userArray = new JsonArray();
        Gson gson = new Gson();
        Set<UserDTO> users = null;
        try {
            users = UserRepository.getInstance().getUsersByRoleID(roleID);
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (UserDTO user : users) {

            JsonElement jsonElement = gson.toJsonTree(user);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            if(isBTCLPersonal){
                if(user.isBTCLPersonnel()){

                    JsonObject object = new JsonObject();
                    object.add("ID", jsonObject1.get("userID"));
                    String name = String.valueOf(jsonObject1.get("fullName"));
                    if (jsonObject1.get("fullName").getAsString().equals("")) {
                        object.add("label", jsonObject1.get("username"));
                    } else {
                        object.add("label", jsonObject1.get("fullName"));
                    }

//            JsonArray array=UILabelBuilder(jsonObject1);
                    userArray.add(object);
                }
            }else{
                if(!user.isBTCLPersonnel()){
                    JsonObject object = new JsonObject();
                    object.add("ID", jsonObject1.get("userID"));
                    String name = String.valueOf(jsonObject1.get("fullName"));
                    if (jsonObject1.get("fullName").getAsString().equals("")) {
                        object.add("label", jsonObject1.get("username"));
                    } else {
                        object.add("label", jsonObject1.get("fullName"));
                    }

                    userArray.add(object);

                }
            }
        }
        return userArray;
    }

    public JsonObject getCommonPart_nix(NIXApplication nixApplication, JsonObject jsonObject, LoginDTO loginDTO)throws Exception {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        NIXConnection nixConnection = new NIXConnection();
        try {
            jsonObject = basicDataBuilderNix(jsonObject, nixApplication,loginDTO);
            if (nixApplication.getConnection() > 0) {
                nixConnection = ServiceDAOFactory.getService(NIXConnectionService.class).
                        getLatestNIXConnectionByConnectionHistoryId(nixApplication.getConnection()); // TODO: 5/8/2019 4-8 sec take to read 

                if(nixConnection.getId()>0){
                    JsonElement jsonElement = gson.toJsonTree(nixConnection);
                    jsonObject.add("connection", jsonElement);
                    /*JsonElement selectedConnection = gson.toJsonTree(new LLIDropdownPair(nixConnection.getId(),nixConnection.getName(),nixConnection));
                    jsonObject.add("selectedConnection",selectedConnection);*/
                }
            }
            JsonObject viewObject = viewObjectBuilderNix(nixApplication, jsonObject,loginDTO);
            JsonObject actionObject = new JsonObject();
            //region action
            List<FlowState> flowStates=new ArrayList<>();
            if(loginDTO.getRoleID()>0){
                flowStates = new FlowService().getNextStatesFromStateRole(nixApplication.getState(), (int) loginDTO.getRoleID());

            }else{
                flowStates = new FlowService().getNextStatesFromState(nixApplication.getState());
            }
            for (FlowState flowState : flowStates) {
                actionObject.addProperty(flowState.getDescription(), flowState.getId());
            }
            //endregion
            jsonArray = UILabelBuilder(viewObject);
            JsonArray actionArray =UILabelBuilder(actionObject);
            JsonArray ifrArray = ifrArrayBuilderNix(nixApplication);
            JsonArray vendorArray =userArrayBuilder(vendorRoleID,false);
            //region pop
            List<NIXIFR> list2 = new ArrayList<>();
            try {
                list2 = nixifrService.getSelectedIFRByAppID(nixApplication.getId(), nixApplication.getState());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonArray popArray = new JsonArray();
            for (NIXIFR ifr : list2) {
                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPop());
                JsonElement jsonElement = gson.toJsonTree(inventoryItem);
                JsonObject jsonObject1 = (JsonObject) jsonElement;
                JsonObject object = new JsonObject();
                object.add("ID", jsonObject1.get("ID"));
                object.add("label", jsonObject1.get("name"));
                popArray.add(object);
            }
            //endregion
// TODO: 12/12/2018 do complete efr and ifr task later
            JsonArray efrArray = efrArrayBuilderNix(nixApplication, false,loginDTO);
            JsonArray efrArray2 = efrArrayBuilderNix(nixApplication, true,loginDTO);

            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
            jsonObject.add("ifr", ifrArray);
            jsonObject.add("vendors", vendorArray);
            jsonObject.add("fiberdivisions",userArrayBuilder(vendorRoleID,true));
            jsonObject.add("pops", popArray);
            jsonObject.addProperty("portCount", nixApplication.getPortCount());
            jsonObject.addProperty("portType", nixApplication.getPortType());
            JsonElement port = gson.toJsonTree(new LLIDropdownPair(
                    nixApplication.getPortType(),
                    InventoryConstants.mapOfPortTypeToPortTypeString.get(nixApplication.getPortType())));

            jsonObject.add("selectedPort",port);
            jsonObject.add("incompleteEFR", efrArray);
            jsonObject.add("completeEFR", efrArray2);
            jsonObject.add("localloops", localLoopsArrayBuilderNix(nixApplication));
            if(nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION){
                // TODO: 1/6/2019 the port info fetcher need to test for all cases
                NIXUpgradeApplication nixUpgradeApplication = nixUpgradeApplicationService.getApplicationByParent(nixApplication.getId());
                NIXApplicationLocalLoop nixLocalLoop = nixApplicationLocalLoopService.getLocalLoopByPort(nixUpgradeApplication.getOldPortId());
                InventoryAttributeValue inventoryAttributeValue = inventoryService.getInventoryAttributeValueByNameIdAndItemId(
                        73011,nixLocalLoop.getPortId());
                jsonObject.add("oldPortInfo",gson.toJsonTree(inventoryAttributeValue));
                jsonObject.addProperty("newPortType",nixUpgradeApplication.getNewPortType());
                JsonElement newport = gson.toJsonTree(new LLIDropdownPair(
                        nixUpgradeApplication.getNewPortType(),
                        InventoryConstants.mapOfPortTypeToPortTypeString.get(nixUpgradeApplication.getNewPortType())));
                JsonElement oldPort = gson.toJsonTree( new LLIDropdownPair(
                        inventoryAttributeValue.getID(),
                        inventoryAttributeValue.getValue(),
                        inventoryAttributeValue));
                jsonObject.add("selectedOldPort",oldPort);
                jsonObject.add("selectedNewPort",newport);
            }

            if(nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION){
                // TODO: 1/6/2019 the port info fetcher need to test for all cases
                NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class).
                        getApplicationByParent(nixApplication.getId());
                NIXApplicationLocalLoop nixLocalLoop = nixApplicationLocalLoopService.getLocalLoopByPort(nixDowngradeApplication.getOldPortId());
                InventoryAttributeValue inventoryAttributeValue = inventoryService.getInventoryAttributeValueByNameIdAndItemId(
                        73011,nixLocalLoop.getPortId());
                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(nixDowngradeApplication.getOldPortId());

                jsonObject.add("oldPortInfo",gson.toJsonTree(inventoryAttributeValue));
                JsonElement oldPort = gson.toJsonTree( new LLIDropdownPair(
                                inventoryItem.getID(),
                                inventoryItem.getName(),
                                inventoryAttributeValue));
                jsonObject.add("selectedOldPort",oldPort);

                jsonObject.addProperty("newPortType",nixDowngradeApplication.getNewPortType());
                JsonElement newport = gson.toJsonTree(new LLIDropdownPair(
                        nixDowngradeApplication.getNewPortType(),
                        InventoryConstants.mapOfPortTypeToPortTypeString.get(nixDowngradeApplication.getNewPortType())));

                jsonObject.add("selectedNewPort",newport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion
        officeBuilderNix(nixApplication,jsonObject);
        return jsonObject;
    }

    public JsonObject basicDataBuilderNix(JsonObject jsonObject, NIXApplication nixApplication, LoginDTO loginDTO) {
        try {
            Gson gson = new Gson();
            // TODO: 12/12/2018 LLIDropDown Pair and LLIloop Provider map will be replaced by nix dropdown and nix loop provide
            jsonObject.add("loopProvider", gson.toJsonTree
                    (new LLIDropdownPair(nixApplication.getLoopProvider(),
                            LLIConnectionConstants.loopProviderMap.get(nixApplication.getLoopProvider()))));
            jsonObject.addProperty("suggestedDate", nixApplication.getSuggestedDate());
            jsonObject.addProperty("applicationID", nixApplication.getId());
            LLIDropdownPair LLIDropdownPair = new LLIDropdownPair(
                    nixApplication.getClient(),
                    AllClientRepository.getInstance().getClientByClientID(nixApplication.getClient()).getLoginName());
            jsonObject.add("client", gson.toJsonTree(LLIDropdownPair));
            jsonObject.addProperty("submissionDate", nixApplication.getSubmissionDate());
            jsonObject.addProperty("status", new FlowService().
                    getStateById(nixApplication.getState()).getViewDescription());
            if(loginDTO.getUserID()>0){
                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                if(nixApplication.getDemandNote()>0&&userDTO.isBTCLPersonnel()){
                    jsonObject.addProperty("demandNoteID", nixApplication.getDemandNote());
//                    jsonObject.addProperty("skipPay",lliApplication.getSkipPayment());
                    BillDTO billDTO=billService.getBillByBillID(nixApplication.getDemandNote());
                    jsonObject.add("bill",new Gson().toJsonTree(billDTO));
                }
            }else{
                jsonObject.addProperty("demandNoteID", nixApplication.getDemandNote());
            }
            jsonObject.addProperty("skipPay",nixApplication.getSkipPayment());
            jsonObject.add("applicationType", gson.toJsonTree(
                    new LLIDropdownPair(nixApplication.getType(),
                            NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()))));
            jsonObject.addProperty("comment", nixApplication.getComment());
            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_NIX);
            jsonObject.addProperty("state", nixApplication.getState());
            jsonObject.addProperty("color", new FlowService().getStateById(nixApplication.getState()).getColor());
            String val = "";
            val = zoneService.getZonebyId(nixApplication.getZone()).getNameEng();
            if(nixApplication.getZone()>0) {
                jsonObject.add("zone", gson.toJsonTree(new
                        LLIDropdownPair(nixApplication.getZone(), val)));//need to do from db
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JsonArray efrArrayBuilderNix(NIXApplication nixApplication, boolean getSubmittd, LoginDTO loginDTO) throws Exception{
        JsonArray efrArray = new JsonArray();
        Gson gson = new Gson();
        List<NIXEFR> efrlist = new ArrayList<>();
        try {
//            long userID=lliApplication.getUserID() ;
            long userID = -1;
            if (!getSubmittd) {
                efrlist = nixefrService.getIncomleteEFR(nixApplication.getId(), loginDTO.getUserID());

            } else {
                efrlist = nixefrService.getCompletedEFR(nixApplication.getId(), loginDTO.getUserID());
                if(efrlist.size()==0){
                    efrlist=nixefrService.getCompletedEFR(nixApplication.getId());
                }
            }
//            efrlist=efrService.getIncomleteEFR(lliApplication.getApplicationID(),userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserDTO userDTO = new UserDTO();
        for (NIXEFR efr : efrlist) {
            try {
                userDTO = UserRepository.getInstance().getUserDTOByUserID(efr.getVendor());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            JsonElement jsonElement = gson.toJsonTree(efr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject1.addProperty("vendorName", userDTO.getFullName().equals("") ? userDTO.getUsername() : userDTO.getFullName());
//            JsonArray array=UILabelBuilder(jsonObject1);
            efrArray.add(jsonObject1);
        }
        return efrArray;
    }
    private JsonArray ifrArrayBuilderNix(NIXApplication nixApplication) {
        Gson gson = new Gson();
        List<NIXIFR> lists = new ArrayList<>();
        try {
            lists = nixifrService.getIFRByAppID(nixApplication.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArray ifrArray = new JsonArray();
        for (NIXIFR ifr : lists) {
            // todo : check if the pop has local loop already
            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPop());
            jsonObject1.addProperty("popName", inventoryItem.getName());
            ifrArray.add(jsonObject1);
        }
        return ifrArray;

    }

    private JsonObject officeBuilderNix(NIXApplication nixApplication, JsonObject jsonObject)throws Exception {
        try {
            Gson gson = new Gson();
            List<NIXApplicationOffice> nixApplicationOffices = new ArrayList<>();
            JsonArray jsonArray1 = new JsonArray();
            nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixApplication.getId());
            /*if(nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION||nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION){
                JsonElement selectedOffice = gson.toJsonTree(new LLIDropdownPair(nixApplicationOffices.get(0).getId(),nixApplicationOffices.get(0).getName(),nixApplicationOffices.get(0)));
                jsonObject.add("selectedOffice", selectedOffice);

            }*/
            for (NIXApplicationOffice nixApplicationOffice : nixApplicationOffices) {
                JsonElement jsonElement = gson.toJsonTree(nixApplicationOffice);
                JsonObject jsonObject1 = (JsonObject) jsonElement;
                jsonArray1.add(jsonObject1);
            }
            jsonObject.add("officeList", jsonArray1);
        } catch(Exception e){
            e.printStackTrace();
        }
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2 = commentBuilderNix(nixApplication, jsonObject2);
        jsonObject.add("Comments", jsonObject2);
        return jsonObject;
    }


    public JsonObject viewObjectBuilderNix(NIXApplication nixApplication,JsonObject jsonObject,LoginDTO loginDTO) {

        JsonObject viewObject = new JsonObject();
        Gson gson = new Gson();
        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", nixApplication.getId());
        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().
                getClientByClientID(nixApplication.getClient()).getLoginName()));
        viewObject.add("Application Type", gson.toJsonTree(NIXConstants.
                nixapplicationTypeNameMap.get(nixApplication.getType())));

        if (jsonObject.get("demandNoteID") != null) {
            viewObject.addProperty("Demand Note ID", nixApplication.getDemandNote());
        }

        if(loginDTO.getUserID()>0){
            UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

            if(userDTO.isBTCLPersonnel()){

                viewObject.addProperty("Demand Note Skip",nixApplication.getSkipPayment()==1?"Yes":"No");
            }
        }else{
            viewObject.addProperty("Demand Note Skip",nixApplication.getSkipPayment()==1?"Yes":"No");
        }
        if(nixApplication.getZone()>0) {
            JsonObject object = (JsonObject) jsonObject.get("zone");
            String val = object.get("label").getAsString();
            viewObject.addProperty("Zone", val);//need to do from db
        }

        // TODO: 1/6/2019 check if comment builder and loop provider would be needed or not
        if(nixApplication.getType() == NIXConstants.NEW_CONNECTION_APPLICATION) {
            //JsonObject portType = (JsonObject) jsonObject.get("portType");
            viewObject.addProperty("Port Type", InventoryConstants.mapOfPortTypeToPortTypeString.
                    get(nixApplication.getPortType()));
            // JsonObject portCount = (JsonObject) jsonObject.get("portType");
            viewObject.addProperty("Port Count", nixApplication.getPortCount());
        }
        else if (nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION){
            // JsonObject oldPortType = (JsonObject) jsonObject.get("oldPortType");
            try {
                NIXUpgradeApplication nixUpgradeApplication = ServiceDAOFactory.getService(NIXUpgradeApplicationService.class).
                        getApplicationByParent(nixApplication.getId());
                viewObject.addProperty("Old Port Type", InventoryConstants.mapOfPortTypeToPortTypeString.
                        get(nixUpgradeApplication.getOldPortType()));
                // JsonObject newPortType = (JsonObject) jsonObject.get("newPortType");
                viewObject.addProperty("New Port Type", InventoryConstants.mapOfPortTypeToPortTypeString.
                        get(nixUpgradeApplication.getNewPortType()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION){
            try{ NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class).
                    getApplicationByParent(nixApplication.getId());
                viewObject.addProperty("Old Port Type", InventoryConstants.mapOfPortTypeToPortTypeString.
                        get(nixDowngradeApplication.getOldPortType()));
                // JsonObject newPortType = (JsonObject) jsonObject.get("newPortType");
                viewObject.addProperty("New Port Type", InventoryConstants.mapOfPortTypeToPortTypeString.
                        get(nixDowngradeApplication.getNewPortType()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (jsonObject.get("loopProvider").getAsJsonObject().get("label") != null) {
            viewObject.addProperty("Loop Provider", jsonObject.get("loopProvider") != null
                    ? jsonObject.get("loopProvider").getAsJsonObject().get("label").getAsString() : "NOT FOUND");
        }
        //viewObject.addProperty("Description", nixApplication.getD() != null ? nixApplication.getDescription() : "Not Given");

        viewObject = commentBuilderNix(nixApplication, viewObject);
        return viewObject;
    }

    public JsonObject commentBuilderNix(NIXApplication nixApplication, JsonObject viewObject) {
        try {
            List<Comments> Comment = commentsService.getCommentsByState(nixApplication.getState(), nixApplication.getId());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            common.ClientDTO clientDTO = new common.ClientDTO();
            if (Comment!=null && Comment.size() > 0) {
                if (Comment.get(0).getUserID() > 0) {

                    userDTO = UserRepository.getInstance().getUserDTOByUserID(Comment.get(0).getUserID());
                    if(userDTO!=null){
                        if (userDTO.getUsername() != null) {

                            commentPersonName = userDTO.getDesignation().equals("") ? userDTO.getUsername() : userDTO.getDesignation();
                        }
                    }
                    commentVal += Comment.get(0).getComments().replaceAll("\"", " ");
                } else {
                    clientDTO = AllClientRepository.getInstance().getClientByClientID(nixApplication.getClient());
                    if (clientDTO.getLoginName() != null) {
                        commentPersonName = clientDTO.getName();
                    }
                    commentVal += Comment.get(0).getComments().replaceAll("\"", " ");

                }

            } else {
                commentVal = "Not Applicable or given";
            }

            if (commentPersonName == "") {
                viewObject.addProperty("Comments ", commentVal);
            } else {
                viewObject.addProperty("Comments by " + commentPersonName, commentVal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewObject;
    }

    @Transactional
    public JsonArray localLoopsArrayBuilderNix(NIXApplication nixApplication) throws Exception{
        Gson gson = new Gson();
        List<NIXApplicationLocalLoop> lists = new ArrayList<>();
        String newPortType = "";
        if(nixApplication.getType() == NIXConstants.NEW_CONNECTION_APPLICATION){

            newPortType = InventoryConstants.mapOfPortTypeToPortTypeString.get(nixApplication.getPortType());
        }else if(nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION) {
            NIXUpgradeApplication nixUpgradeApplication = nixUpgradeApplicationService.getApplicationByParent(nixApplication.getId());
            newPortType = InventoryConstants.mapOfPortTypeToPortTypeString.get(nixUpgradeApplication.getNewPortType());
        }else  if(nixApplication.getType() == NIXConstants.NIX_DOWNGRADE_APPLICATION) {
            NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(NIXDowngradeApplicationService.class)
                    .getApplicationByParent(nixApplication.getId());
            newPortType = InventoryConstants.mapOfPortTypeToPortTypeString.get(nixDowngradeApplication.getNewPortType());
        }
        try {
            List<NIXApplicationOffice> nixApplicationOffices;
            nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixApplication.getId());
            for (NIXApplicationOffice nixApplicationOffice : nixApplicationOffices) {
                List<NIXApplicationLocalLoop> nixApplicationLocalLoops = nixApplicationLocalLoopService.
                        getLocalLoopByOffice(nixApplicationOffice.getId());
                for (NIXApplicationLocalLoop nixApplicationLocalLoop : nixApplicationLocalLoops) {
                    nixApplicationLocalLoop.setPopName(inventoryService.getInventoryItemByItemID
                            (nixApplicationLocalLoop.getPopId()).getName());
                    lists.add(nixApplicationLocalLoop);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArray localLoopArray = new JsonArray();
        List<InventoryItem> globalVlans = inventoryService.getGlobalVlans();
        for (NIXApplicationLocalLoop nixApplicationLocalLoop : lists) {
            JsonElement jsonElement = gson.toJsonTree(nixApplicationLocalLoop);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            //select only selected loop
            boolean workCompleted = checkIsWorkCompleted(nixApplicationLocalLoop);
            if(workCompleted)jsonObject1.addProperty("workCompleted",1);
            else jsonObject1.addProperty("workCompleted",0);

            if(nixApplicationLocalLoop.getPortId()>0){
                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(nixApplicationLocalLoop.getPortId());
                jsonObject1.addProperty("oldPortName",inventoryItem.getName());
            }
            InventoryItem pop = inventoryService.getInventoryItemByItemID(nixApplicationLocalLoop.getPopId());
            jsonObject1.addProperty("popName", pop.getName());
            List<InventoryItem> router_switches = inventoryService.getInventoryItems
                    (InventoryConstants.CATEGORY_ID_ROUTER, nixApplicationLocalLoop.getPopId());

            Map<Integer, List<InventoryItem>> mapForLocalOfficePortVlan = inventoryService.getInventoryItemsByParentInventoryItems(
                    router_switches.stream().map(InventoryItem::getID).collect(Collectors.toList())
            );

            JsonObject localLoopJsonObject = jsonElement.getAsJsonObject();
            localLoopJsonObject.add("router_switch", FlowDataBuilder.getJsonArrayFromInventoryItems(router_switches, gson));
            List<InventoryItem> allPortsUnderRS = mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_PORT, Collections.emptyList());
            Map<Long, String> mapAttributeNames = inventoryService.getMapOfAttributeValueToItemIdByItemIds(
                    allPortsUnderRS
                            .stream()
                            .map(InventoryItem::getID)
                            .collect(Collectors.toList())
            );
            String portType = newPortType;
            allPortsUnderRS = allPortsUnderRS.stream()
                    .filter(t->{
                        String portTypeStr = mapAttributeNames.getOrDefault(t.getID(), null);
                        return portType.equals(portTypeStr);
                    }).collect(Collectors.toList());


            localLoopJsonObject.add("ports",
                    FlowDataBuilder.getJsonArrayFromInventoryItems(
                           allPortsUnderRS
                            , gson
                    )
            );
            localLoopJsonObject.add("vlans",
                    FlowDataBuilder.getJsonArrayFromInventoryItems(
                            mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, Collections.emptyList())
                            , gson
                    )
            );
            localLoopJsonObject.add("globalVlans", FlowDataBuilder.getJsonArrayFromInventoryItems(globalVlans, gson ) );

            jsonObject1.addProperty("vlanType", 0);
            localLoopArray.add(jsonObject1);
        }
        return localLoopArray;
    }

    @Transactional
    private boolean checkIsWorkCompleted(NIXApplicationLocalLoop nixApplicationLocalLoop) throws Exception{
        return ServiceDAOFactory.getService(NIXEFRService.class).getCompletedEFRByLocalLoopID(nixApplicationLocalLoop.getId());
    }



    //endregion
}
