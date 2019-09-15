package lli.Application;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;

import common.ClientDTO;
import common.repository.AllClientRepository;
import flow.FlowService;
import flow.entity.FlowState;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Comments.Comments;
import lli.Comments.CommentsService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;

import location.ZoneService;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;

public class LLIApplicationSerializer implements JsonSerializer<LLIApplication> {


    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);

    LLIFlowConnectionService lliFlowConnectionService = ServiceDAOFactory.getService(LLIFlowConnectionService.class);

    public static final int vendorRoleID = 16020;
    public static final int fibreDivUserID = 36127;

    public static JsonObject getCommonPart(LLIApplication lliApplication, JsonObject jsonObject, JsonSerializationContext context) {
        //Serialize Common LLI Application


        jsonObject.addProperty("applicationID", lliApplication.getApplicationID());
        jsonObject.add("client", context.serialize(new LLIDropdownPair(lliApplication.getClientID(), AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName())));
        jsonObject.add("user", lliApplication.getUserID() != null ? context.serialize(new LLIDropdownPair(lliApplication.getUserID(), UserRepository.getInstance().getUserDTOByUserID(lliApplication.getUserID()).getUsername())) : context.serialize(null));

        jsonObject.addProperty("submissionDate", lliApplication.getSubmissionDate());
//		jsonObject.add("status", context.serialize( new LLIDropdownPair(lliApplication.getStatus(), LLIConnectionConstants.applicationStatusMap.get(lliApplication.getStatus())) ));
        jsonObject.addProperty("status", new FlowService().getStateById(lliApplication.getState()).getDescription());

        jsonObject.addProperty("content", lliApplication.getContent());
        jsonObject.addProperty("demandNoteID", lliApplication.getDemandNoteID());

        jsonObject.add("applicationType", context.serialize(new LLIDropdownPair(lliApplication.getApplicationType(),
                LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType()))));

        jsonObject.addProperty("comment", lliApplication.getComment());
        jsonObject.addProperty("comment", lliApplication.getRejectionComment());
        jsonObject.addProperty("description", lliApplication.getDescription());

        //jsonObject.addProperty("zone",lliApplication.getZoneId());

        try {
            //String val=ZoneService.getZonebyId(lliApplication.getZoneId()).getNameEng();
            jsonObject.add("zone", context.serialize(new LLIDropdownPair(lliApplication.getZoneId(),
                    "moghbazar")));//need to do from db
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.addProperty("requestForCorrectionComment", lliApplication.getRequestForCorrectionComment());
        return jsonObject;
    }

    public JsonObject getCommonPart_new(LLIApplication lliApplication, JsonObject jsonObject, JsonSerializationContext context) {
        //Serialize Common LLI Application
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

//        Map session = ActionContext.get("session");
        try {
            jsonObject = basicDataBuilder(jsonObject, lliApplication, context);
            LLIConnection lliConnection = new LLIConnection();
            if (lliApplication.getConnectionId() > 0) {
                lliConnection = lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());
                JsonElement jsonElement = gson.toJsonTree(lliConnection);
                jsonObject.add("connection", jsonElement);
            }
            JsonObject viewObject = viewObjectBuilder(lliApplication, lliConnection, jsonObject, context);
            JsonObject actionObject = new JsonObject();

            //region action

            List<FlowState> flowStates = new FlowService().getNextStatesFromState(lliApplication.getState());
            for (FlowState flowState : flowStates
                    ) {

                actionObject.addProperty(flowState.getDescription(), flowState.getId());
            }

            //endregion
            jsonArray = UILabelBuilder(viewObject);
            JsonArray actionArray = UILabelBuilder(actionObject);
            JsonArray ifrArray = ifrArrayBuilder(lliApplication, false);
            JsonArray vendorArray = userArrayBuilder(vendorRoleID);

            //region pop
            List<IFR> list2 = new ArrayList<>();
            try {
                list2 = ifrService.getSelectedIFRByAppID(lliApplication.getApplicationID(), lliApplication.getState());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonArray popArray = new JsonArray();
            for (IFR ifr : list2) {
                InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPopID());
                JsonElement jsonElement = gson.toJsonTree(inventoryItem);
                JsonObject jsonObject1 = (JsonObject) jsonElement;
                JsonObject object = new JsonObject();
                object.add("ID", jsonObject1.get("ID"));
                object.add("label", jsonObject1.get("name"));
//            JsonArray array=UILabelBuilder(jsonObject1);
                popArray.add(object);

            }

            //endregion

            JsonArray efrArray = efrArrayBuilder(lliApplication, false);
            JsonArray efrArray2 = efrArrayBuilder(lliApplication, true);

            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
            jsonObject.add("ifr", ifrArray);
            jsonObject.add("vendors", vendorArray);

            //todo change for fibre division
            jsonObject.add("fiberdivisions", dummyUserArrayBuilder());
            jsonObject.add("pops", popArray);
            jsonObject.add("incompleteEFR", efrArray);
            jsonObject.add("completeEFR", efrArray2);

            jsonObject.add("localloops", localLoopsArrayBuilder(lliApplication));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        JsonArray efrArray = efrArrayBuilder(lliApplication, false);
        JsonArray efrArray2 = efrArrayBuilder(lliApplication, true);


        try {
//            JsonElement jsonElement=;
            List<Office> offices;
            if (lliApplication.getConnectionId() > 0) {
                offices = officeService.getOfficeByCON(lliApplication.getConnectionId());
            } else {
                offices = officeService.getOffice(lliApplication.getApplicationID());
            }

            JsonArray jsonArray1 = new JsonArray();
            for (Office office : offices) {

                JsonElement jsonElement = gson.toJsonTree(office);
                JsonObject jsonObject1 = (JsonObject) jsonElement;

                jsonArray1.add(jsonObject1);

            }
            jsonObject.add("officeList", jsonArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.add("localloops", localLoopsArrayBuilder(lliApplication));
        return jsonObject;
    }

    @Override
    public JsonElement serialize(LLIApplication arg0, Type arg1, JsonSerializationContext arg2) {
        return null;
    }

    public JsonObject basicDataBuilder(JsonObject jsonObject, LLIApplication lliApplication, JsonSerializationContext context) {

        try {
            jsonObject.addProperty("applicationID", lliApplication.getApplicationID());

            jsonObject.add("client", context.serialize(new LLIDropdownPair(lliApplication.getClientID(), AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName())));
            jsonObject.add("user", lliApplication.getUserID() != null ? context.serialize(new LLIDropdownPair(lliApplication.getUserID(), UserRepository.getInstance().getUserDTOByUserID(lliApplication.getUserID()).getUsername())) : context.serialize(null));//problem
            jsonObject.addProperty("submissionDate", lliApplication.getSubmissionDate());
            //      jsonObject.add("status", context.serialize( new LLIDropdownPair(lliApplication.getStatus(), LLIConnectionConstants.applicationStatusMap.get(lliApplication.getStatus())) ));
            jsonObject.addProperty("status", new FlowService().getStateById(lliApplication.getState()).getViewDescription());

            jsonObject.addProperty("content", lliApplication.getContent());
            jsonObject.addProperty("comment", lliApplication.getRejectionComment());
            jsonObject.addProperty("demandNoteID", lliApplication.getDemandNoteID());
            jsonObject.add("applicationType", context.serialize(new LLIDropdownPair(lliApplication.getApplicationType(), LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType()))));
            jsonObject.addProperty("comment", lliApplication.getComment());
            jsonObject.addProperty("description", lliApplication.getDescription());
            jsonObject.addProperty("state", lliApplication.getState());
            jsonObject.addProperty("color", new FlowService().getStateById(lliApplication.getState()).getColor());
            jsonObject.addProperty("connectionID", lliApplication.getConnectionId());

            String val = "";
            val = zoneService.getZonebyId(lliApplication.getZoneId()).getNameEng();
            jsonObject.add("zone", context.serialize(new LLIDropdownPair(lliApplication.getZoneId(), val)));//need to do from db

            jsonObject.addProperty("requestForCorrectionComment", lliApplication.getRequestForCorrectionComment());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

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

    public JsonObject viewObjectBuilder(LLIApplication lliApplication, LLIConnection lliConnection, JsonObject jsonObject, JsonSerializationContext context) {

        JsonObject viewObject = new JsonObject();

        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", lliApplication.getApplicationID());
        viewObject.add("Client Name", context.serialize(AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName()));
        //bandwidth


        viewObject.addProperty("Bandwidth", jsonObject.get("bandwidth") + " MB");
        viewObject.add("Application Type", context.serialize(LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType())));

        if (lliApplication.getDemandNoteID() != null) {
            viewObject.addProperty("Demand Note ID", lliApplication.getDemandNoteID());
        }

        if (lliApplication.getConnectionId() > 0) {
            viewObject.addProperty("Connection Name", lliConnection.getName());
            viewObject.addProperty("Existing Bandwidth", lliConnection.getBandwidth());
        }

//        try {
//
//            //Todo: Office List multiple Office
//
//            List<Office> offices = officeService.getOffice(lliApplication.getApplicationID());
//
//            if (offices.size() > 0) {
//
//
//                Office office = offices.get(0);
////
//                viewObject.addProperty("Office Name", office.getOfficeName());
//                viewObject.addProperty("Office Address", office.getOfficeAddress());
//
//            } else {
//
//                LLIUpgradeBandwidthApplication lliUpgradeBandwidthApplication = (LLIUpgradeBandwidthApplication) lliApplication;
//
//                Office office1 = officeService.getOfficeByCON(lliUpgradeBandwidthApplication.getConnectionID()).get(0);
//
//
//                viewObject.addProperty("Office Name", office1.getOfficeName());
//                viewObject.addProperty("Office Address", office1.getOfficeAddress());
//
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JsonObject object = (JsonObject) jsonObject.get("zone");
        String val = object.get("label").getAsString();
        viewObject.addProperty("Zone", val);//need to do from db

        JsonObject loop = (JsonObject) jsonObject.get("loopProvider");

        //todo : change hardcode
        viewObject.addProperty("Loop Provider", jsonObject.get("loopProvider") != null
                ? jsonObject.get("loopProvider").getAsJsonObject().get("label").getAsString() : "BTCL");
        viewObject.addProperty("Description", lliApplication.getDescription());

        viewObject = commentBuilder(lliApplication, viewObject);


        return viewObject;


    }

    public JsonObject commentBuilder(LLIApplication lliApplication, JsonObject viewObject) {
        try {
            List<Comments> comments = commentsService.getCommentsByState(lliApplication.getState(), lliApplication.getApplicationID());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            ClientDTO clientDTO = new ClientDTO();
            if (!comments.isEmpty()) {
                if (comments.get(0).getUserID() != -1) {
                    userDTO = UserRepository.getInstance().getUserDTOByUserID(comments.get(0).getUserID());
                    if (userDTO.getUsername() != null) {
                        commentPersonName = userDTO.getDesignation().equals("")? userDTO.getUsername():userDTO.getDesignation();
                    }
                } else {
                    clientDTO = AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID());
                    if (clientDTO.getLoginName() != null) {
                        commentPersonName = clientDTO.getName();
                    }
                }
                commentVal += comments.get(0).getComments().replaceAll("\"", " ");

            } else {
                commentVal = "Not Applicable or given";
            }

            if (commentPersonName.isEmpty()) {
                viewObject.addProperty("Comments ", commentVal);
            } else {
                viewObject.addProperty("Comments by " + commentPersonName, commentVal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewObject;
    }

    public JsonArray ifrArrayBuilder(LLIApplication lliApplication, boolean getSelected) {


        //for ifr need to filter with state and user role

        Gson gson = new Gson();


        List<IFR> lists = new ArrayList<>();
        try {
            if (getSelected) {

            } else {
                lists = ifrService.getIFRByAppID(lliApplication.getApplicationID(), lliApplication.getState());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonArray ifrArray = new JsonArray();
        for (IFR ifr : lists) {

            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPopID());
            jsonObject1.addProperty("popName", inventoryItem.getName());
            ifrArray.add(jsonObject1);

        }

        return ifrArray;


    }

    public JsonArray localLoopsArrayBuilder(LLIApplication lliApplication) {


        Gson gson = new Gson();


        List<LocalLoop> lists = new ArrayList<>();
        try {
            lists = localLoopService.getLocalLoop(lliApplication.getApplicationID());


        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonArray localLoopArray = new JsonArray();
        for (LocalLoop localLoop : lists) {

            JsonElement jsonElement = gson.toJsonTree(localLoop);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem pop = inventoryService.getInventoryItemByItemID(localLoop.getPopID());
            jsonObject1.addProperty("popName", pop.getName());

            //todo change harcoded inventory type;
            List<InventoryItem> router_switches = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(5, localLoop.getPopID());
            JsonArray router_switchArray = new JsonArray();
            for (InventoryItem router_switch : router_switches) {
                //todo change harcoded inventory type;
                List<InventoryItem> ports = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(99, router_switch.getID());
                JsonArray portArray = new JsonArray();

                for (InventoryItem port:ports) {

                    List<InventoryItem> vlans=inventoryService.getUnusedInventoryItemListByCatagoryIDAndParentItemID(6,port.getID());
//                    inventoryService.
                    JsonElement vlanElement = gson.toJsonTree(vlans);
                    JsonElement portElement = gson.toJsonTree(port);
                    JsonObject portObject = portElement.getAsJsonObject();
                    portObject.add("vlan",vlanElement);
                    portArray.add(portObject);

                }

                JsonElement router_swi = gson.toJsonTree(router_switch);
                JsonObject router_swiAsJsonObject = router_swi.getAsJsonObject();
                router_swiAsJsonObject.add("port", portArray);
                router_switchArray.add(router_swiAsJsonObject);

            }


            jsonObject1.add("router_switch", router_switchArray);

            localLoopArray.add(jsonObject1);

        }

        return localLoopArray;


    }

    public JsonArray userArrayBuilder(long roleID) {


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

        return userArray;


    }

    public JsonArray dummyUserArrayBuilder() {


        //for ifr need to filter with state and user role

        JsonArray userArray = new JsonArray();
        Gson gson = new Gson();


        UserDTO user = new UserDTO();

        //todo :change for fibre division
        user.setUserID(fibreDivUserID);
        user.setUsername("Fiber Division");

        JsonElement jsonElement = gson.toJsonTree(user);
        JsonObject jsonObject1 = (JsonObject) jsonElement;

        JsonObject object = new JsonObject();
        object.add("ID", jsonObject1.get("userID"));
        object.add("label", jsonObject1.get("username"));

        userArray.add(object);


        return userArray;


    }

    public JsonArray efrArrayBuilder(LLIApplication lliApplication, boolean getSubmittd) {

        JsonArray efrArray = new JsonArray();
        Gson gson = new Gson();

        List<EFR> efrlist = new ArrayList<>();
        try {
//            long userID=lliApplication.getUserID() ;
            long userID = -1;
            //Todo:need to fetch from login DTO for filter efr vendor specific
            LoginDTO loginDTO = new LoginDTO();
            if (!getSubmittd) {
                efrlist = efrService.getIncomleteEFR(lliApplication.getApplicationID(), userID);

            } else {
                efrlist = efrService.getCompletedEFR(lliApplication.getApplicationID(), userID);
            }

//            efrlist=efrService.getIncomleteEFR(lliApplication.getApplicationID(),userID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDTO userDTO = new UserDTO();

        for (EFR efr : efrlist) {

            try {
                userDTO = UserRepository.getInstance().getUserDTOByUserID(efr.getVendorID());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonElement jsonElement = gson.toJsonTree(efr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject1.addProperty("vendorName", userDTO.getFullName().equals("") ?  userDTO.getUsername() :userDTO.getFullName());
//            JsonArray array=UILabelBuilder(jsonObject1);
            efrArray.add(jsonObject1);

        }

        return efrArray;


    }


}
