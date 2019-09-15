package vpn.FlowConnectionManager;

import application.ApplicationType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.ClientDTO;
import common.ModuleConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import entity.comment.Comment;
import entity.comment.CommentService;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import entity.efr.EFRService;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConditionBuilder;
import entity.localloop.LocalLoopService;
import entity.office.Office;
import entity.office.OfficeService;
import exception.NoDataFoundException;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import location.ZoneService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.VPNConstants;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.connection.Connection;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Log4j
public class FlowDataBuilder {
    VPNApplicationService vpnApplicationService = ServiceDAOFactory.getService(VPNApplicationService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    BillService billService = ServiceDAOFactory.getService(BillService.class);
    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);

    CommentService commentsService = ServiceDAOFactory.getService(CommentService.class);
    //    VPNFlowConnectionService vpnFlowConnectionService = ServiceDAOFactory.getService(VPNFlowConnectionService.class);
    public static final int vendorRoleID = 16020;
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);

    public JsonObject getCommonPart_new(VPNApplication vpnApplication, JsonObject jsonObject, LoginDTO loginDTO) {
        //Serialize Common VPN Application
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        Connection vpnConnection = new Connection();

//        Map session = ActionContext.get("session");
        try {
            jsonObject = basicDataBuilder(jsonObject, vpnApplication, loginDTO);


            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition()

            );
            JsonArray linkArray = linkArrayBuilder(vpnApplication, vpnApplicationLinks, loginDTO);
            List<InventoryItem> globalVlans = inventoryService.getGlobalVlans();
            if (linkArray.size() > 0) {
                for (JsonElement jsonElement : linkArray
                ) {
                    JsonObject linkObject = jsonElement.getAsJsonObject();
                    JsonObject localOffice = linkObject.has("localOffice") ? linkObject.get("localOffice").getAsJsonObject() : null;
                    JsonObject remoteOffice = linkObject.has("remoteOffice") ? linkObject.get("remoteOffice").getAsJsonObject() : null;

                    JsonArray localOfficeLoop = localOffice != null ? localOffice.get("localLoops").getAsJsonArray() : null;
                    JsonArray remoteOfficeLoop = remoteOffice != null ? remoteOffice.get("localLoops").getAsJsonArray() : null;

                    //todo need to call below line on state conditionally for faster page loading

//                    VPN_ADVICE_NOTE_PUBLISH
//                    VPN_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH
//                    VPN_DOWNGRADE_APPLICATION_ADVICE_NOTE_PUBLISH
                    String state = linkObject.get("linkState").getAsString();
                    getRouterSwitchPortVlanData(localOfficeLoop, globalVlans, gson);
                    getRouterSwitchPortVlanData(remoteOfficeLoop, globalVlans, gson);

                }
            }
            JsonObject viewObject = rootViewObjectBuilder(vpnApplication, jsonObject, loginDTO);
            jsonArray = UILabelBuilder(viewObject);
            JsonArray vendorArray = userArrayBuilder(vendorRoleID, false);
            jsonObject.add("formElements", jsonArray);
            jsonObject.add("vpnApplicationLinks", linkArray);
            jsonObject.add("action", commonActionBuilder(linkArray));// todo: global action logic
//            jsonObject.add("ifr", ifrArray);

            jsonObject.add("vendors", vendorArray);
//
//
            jsonObject.add("fiberdivisions", userArrayBuilder(vendorRoleID, true));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

//        officeBuilder(vpnApplication, jsonObject);

        return jsonObject;
    }

    private void getRouterSwitchPortVlanData(JsonArray localLoops, List<InventoryItem> globalVlans, Gson gson) throws Exception {
        if (localLoops != null) {
            for (JsonElement localLoop : localLoops) {
                long popId = localLoop.getAsJsonObject().get("popId").getAsLong();
                List<InventoryItem> router_switches = inventoryService
                        .getInventoryItems(InventoryConstants.CATEGORY_ID_ROUTER, popId);
                Map<Integer, List<InventoryItem>> mapForLocalOfficePortVlan = inventoryService.getInventoryItemsByParentInventoryItems(
                        router_switches.stream().map(InventoryItem::getID).collect(Collectors.toList())
                );

                JsonObject localLoopJsonObject = localLoop.getAsJsonObject();
                localLoopJsonObject.add("router_switch", getJsonArrayFromInventoryItems(router_switches, gson));
                localLoopJsonObject.add("ports",
                        getJsonArrayFromInventoryItems(
                                mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_PORT, Collections.emptyList())
                                , gson
                        )
                );
                localLoopJsonObject.add("vlans",
                        getJsonArrayFromInventoryItems(
                                mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, Collections.emptyList())
                                , gson
                        )
                );
                localLoopJsonObject.add("globalVlans", getJsonArrayFromInventoryItems(globalVlans, gson));

            }

        }
    }

    public static JsonArray getJsonArrayFromInventoryItems(List<InventoryItem> inventoryItems, Gson gson) {
        JsonArray jsonArray = new JsonArray();
        for (InventoryItem router_switch : inventoryItems) {
            JsonElement router_swi = gson.toJsonTree(router_switch);
            JsonObject router_swiAsJsonObject = router_swi.getAsJsonObject();
            jsonArray.add(router_swiAsJsonObject);

        }
        return jsonArray;
    }

    public JsonArray commonActionBuilder(JsonArray linkArray) {
        JsonArray commonActionArray = new JsonArray();
        Gson gson = new Gson();

        Type type = new TypeToken<JsonArray>() {
        }.getType();
        JsonArray temp = gson.fromJson(linkArray, type);

        Set<JsonObject> commonActionSet = new HashSet<>();
        if (temp.size() > 0) {
            if (temp.get(0).getAsJsonObject().get("action").isJsonArray()) {
                if (temp.get(0).getAsJsonObject().get("action").getAsJsonArray().size() > 0) {
                    for (JsonElement jsonElement : temp.get(0).getAsJsonObject().get("action").getAsJsonArray()
                    ) {
                        commonActionSet.add(jsonElement.getAsJsonObject());
                    }
                }
            }


            temp.forEach(t ->
            {
                JsonArray actionArray = t.getAsJsonObject().get("action").getAsJsonArray();
                Set<JsonObject> actionSet = new HashSet<>();
                for (JsonElement jsonElement : actionArray
                ) {

                    actionSet.add(jsonElement.getAsJsonObject());
                }

                commonActionSet.retainAll(actionSet);
            });


            Iterator iterator = commonActionSet.iterator();
            while (iterator.hasNext()) {
                JsonObject jsonObject = (JsonObject) iterator.next();
                jsonObject.addProperty("isGlobal", true);
                commonActionArray.add(jsonObject);
            }
        }




//        if (linkArray.size()>1){
            return commonActionArray;
//        }else {
//            return null;
//            return new JsonArray();
//        }
    }

    public JsonArray linkArrayBuilder(VPNApplication vpnApplication, List<VPNApplicationLink> vpnApplicationLinks, LoginDTO loginDTO) {
        JsonArray linkArray = new JsonArray();
        Gson gson = new Gson();
        for (VPNApplicationLink t : vpnApplicationLinks) {
            try {
                ArrayList<Integer> zones = (ArrayList<Integer>) zoneService.getUserZone((int) loginDTO.getUserID());
                UserDTO userDTO = null;
                boolean isDistributed = false;
                boolean isDistributionApplicable = true;
                if (loginDTO.getUserID() > 0) {

                    userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                }

                //check if cable division logged in and has any efr
                ArrayList<Integer> officeids = new ArrayList<>();
                officeids.add((int) t.getLocalOfficeId());
                officeids.add((int) t.getRemoteOfficeId());


                if (userDTO != null) {

                    if (zones.size() > 0 && userDTO.isBTCLPersonnel()) {

                        isDistributed = true;
                    } else if (userDTO.isBTCLPersonnel()) {
                        List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                                new EFRConditionBuilder()
                                        .Where()
                                        .officeIdIn(officeids)
                                        .applicationIdEquals(vpnApplication.getApplicationId())
                                        .vendorIDEquals(userDTO.getUserID())
                                        .getCondition()

                        );
                        if (efrs.size() > 0) {
                            isDistributionApplicable = false;
                        } else {
                            isDistributionApplicable = true;
                        }

                    } else {
                        isDistributionApplicable = false;
                    }

                } else {
                    isDistributionApplicable = false;
                }

                Office localOffice = officeBuilderWithChildData(
                        t.getLocalOfficeId()
                        , t.getLocalOfficeLoopId()
                        , isDistributionApplicable
                        , isDistributed
                        , zones
                        , vpnApplication
                        , loginDTO
                        , userDTO
                );
                if (localOffice != null) {
                    if (localOffice.getLocalLoops() != null) {
                        t.setLocalOffice(localOffice);
                    }
                }

                Office remoteOffice = officeBuilderWithChildData(
                        t.getRemoteOfficeId()
                        , t.getRemoteOfficeLoopId()
                        , isDistributionApplicable
                        , isDistributed
                        , zones
                        , vpnApplication
                        , loginDTO
                        , userDTO
                );
                if (remoteOffice != null) {
                    if (remoteOffice.getLocalLoops() != null) {
                        t.setRemoteOffice(remoteOffice);
                    }
                }


                JsonElement linkElement = gson.toJsonTree(t);
                FlowState currentState = vpnApplicationService.getCurrentState(t.getLinkState().getState());

                List<FlowState> flowStates = vpnApplicationService.getActionList(t.getLinkState().getState(), (int) loginDTO.getRoleID());
                JsonArray actionArray = new JsonArray();
                for (FlowState flowState : flowStates
                ) {

                    JsonElement jsonElement1 = gson.toJsonTree(flowState);
                    jsonElement1.getAsJsonObject().addProperty("isGlobal", false);
                    actionArray.add(jsonElement1);
                }
                linkElement.getAsJsonObject().add("state", gson.toJsonTree(currentState));
                linkElement.getAsJsonObject().add("action", actionArray);
                JsonObject viewObject = linkViewObjectBuilder(vpnApplication, t, linkElement.getAsJsonObject(), loginDTO);
                JsonArray formArray = UILabelBuilder(viewObject);
                linkElement.getAsJsonObject().add("formElements", formArray);
                linkElement.getAsJsonObject().add("Comments", commentArrayBuilder(vpnApplication, t));
                if (t.getDemandNoteId() != null) {
                    if (t.getDemandNoteId() > 0) {
                        BillDTO billDTO = billService.getBillByBillID(t.getDemandNoteId());
                        linkElement.getAsJsonObject().add("bill", new Gson().toJsonTree(billDTO));
                    }
                }

                if (actionArray.size() > 0 && (t.getLocalOffice() != null || t.getRemoteOffice() != null)) {
//                    if (loginDTO.getUserID() > 0 && !userDTO.isBTCLPersonnel() ) {
                    if(loginDTO.getRoleID()>0) {
                        if (userDTO.getRoleID() == VPNConstants.VENDOR_ROLE) {
                            boolean localEFr = false;
                            boolean remoteEfr = false;

                            if (t.getLocalOffice().getEfrs() != null) {

                                if (t.getLocalOffice().getEfrs().size() != 0) {
                                    localEFr = true;
                                }
                            }
                            if (t.getRemoteOffice().getEfrs() != null) {
                                if (t.getRemoteOffice().getEfrs().size() != 0) {
                                    remoteEfr = true;
                                }
                            }
                            if (localEFr || remoteEfr) {
                                linkArray.add(linkElement);
                            }
                        }
                        else{
                            linkArray.add(linkElement);
                        }
                    }else {

                        linkArray.add(linkElement);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return linkArray;
    }

    public Office officeBuilderWithChildData(Long officeId,
                                             Long LoopId,
                                             boolean isDistributionApplicable,
                                             boolean isDistributed,
                                             List<Integer> zones,
                                             VPNApplication vpnApplication,
                                             LoginDTO loginDTO,
                                             UserDTO userDTO) throws Exception {
        Office office = globalService.findByPK(Office.class, officeId);
        List<LocalLoop> localLoops = new ArrayList<>();
        if (isDistributionApplicable) {
            localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                    new LocalLoopConditionBuilder()
                            .Where()
//                                    .officeIdEquals(localOffice.getId())
                            .idEquals(LoopId)
                            .zoneIdIn(zones)
                            .isDistributed(isDistributed)
                            .getCondition()
            );
        } else {
            localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                    new LocalLoopConditionBuilder()
                            .Where()
//                                    .officeIdEquals(localOffice.getId())
                            .idEquals(LoopId)

                            .zoneIdIn(zones)
                            .getCondition()
            );
        }
        boolean isLocalLoopDone = false;
        if (localLoops.size() > 0) {
            isLocalLoopDone = localLoops.get(0).isCompleted();
        }
        List<EFR> repliedOfficeEfrs = new ArrayList<>();
        List<EFR> OfficeEfrs = new ArrayList<>();
        if (!isLocalLoopDone || vpnApplication.getApplicationType() == ApplicationType.VPN_CLOSE) {

            if (loginDTO.getUserID() > 0) {
                if (userDTO.isBTCLPersonnel() && userDTO.getRoleID() != VPNConstants.VENDOR_ROLE) {
                    repliedOfficeEfrs = globalService.getAllObjectListByCondition(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .officeIdEquals(office.getId())
                                    .applicationIdEquals(vpnApplication.getApplicationId())
                                    .isReplied(true)
                                    .isIgnored(false)
                                    .getCondition()
                    );
                }
            }

            OfficeEfrs = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(office.getId())
                            .applicationIdEquals(vpnApplication.getApplicationId())
                            .vendorIDEquals(loginDTO.getUserID())
                            .isCompleted(false)
                            .isIgnored(false)
                            .getCondition()
            );

        }

        if (localLoops.size() > 0) {

            if (OfficeEfrs.size() == 0) {
                if (repliedOfficeEfrs.size() == 0) {

                    office.setEfrs(new ArrayList<>());
                } else {

                    efrService.setVolatile(repliedOfficeEfrs);
                    office.setEfrs(repliedOfficeEfrs);
                }
            } else {

                efrService.setVolatile(OfficeEfrs);

                office.setEfrs(OfficeEfrs);
            }

            localLoopService.setVolatileProperties(localLoops);
            office.setLocalLoops(localLoops);

        }
        return office;

    }

    public JsonObject basicDataBuilder(JsonObject jsonObject, VPNApplication vpnApplication, LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();
            jsonObject.addProperty("suggestedDate", vpnApplication.getSuggestedDate());
            jsonObject.addProperty("clientId", vpnApplication.getClientId());

            jsonObject.addProperty("applicationId", vpnApplication.getApplicationId());
            jsonObject.addProperty("vpnApplicationId", vpnApplication.getVpnApplicationId());
            jsonObject.addProperty("applicationType", vpnApplication.getApplicationType().name());
            jsonObject.addProperty("layerType", vpnApplication.getLayerType());


            jsonObject.addProperty("submissionDate", vpnApplication.getSubmissionDate());
            jsonObject.addProperty("status", new FlowService().getStateById(vpnApplication.getApplicationState().getState()).getViewDescription());

            if (loginDTO.getUserID() > 0) {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

                if (vpnApplication.getDemandNoteId() != null && userDTO.isBTCLPersonnel()) {
                    jsonObject.addProperty("demandNoteID", vpnApplication.getDemandNoteId());
//                    jsonObject.addProperty("skipPay",vpnApplication.getSkipPayment());
                    BillDTO billDTO = billService.getBillByBillID(vpnApplication.getDemandNoteId());
                    jsonObject.add("bill", new Gson().toJsonTree(billDTO));
                }
            } else {

                jsonObject.addProperty("demandNoteID", vpnApplication.getDemandNoteId());

            }
            jsonObject.addProperty("skipPay", vpnApplication.isSkipPayment());

            jsonObject.addProperty("comment", vpnApplication.getComment());

            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_VPN);
            jsonObject.addProperty("description", vpnApplication.getDescription());
            jsonObject.addProperty("state", vpnApplication.getApplicationState().getState());
            jsonObject.addProperty("color", new FlowService().getStateById(vpnApplication.getApplicationState().getState()).getColor());
            FlowState flowState = vpnApplicationService.getCurrentState(vpnApplication.getApplicationState().getState());

            JsonElement jsonElement = gson.toJsonTree(flowState);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject.add("state", jsonObject1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject rootViewObjectBuilder(VPNApplication vpnApplication, JsonObject jsonObject, LoginDTO loginDTO) {

        JsonObject viewObject = new JsonObject();

        Gson gson = new Gson();
        viewObject.addProperty("Application ID", vpnApplication.getApplicationId());
        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId()).getLoginName()));

        viewObject.add("Application Type", gson.toJsonTree(vpnApplication.getApplicationType().getApplicationTypeName()));

        if (vpnApplication.getDemandNoteId() != null) {
            viewObject.addProperty("Demand Note ID", vpnApplication.getDemandNoteId());
        }


        if (vpnApplication.getLayerType() == 2) {

            viewObject.addProperty("Layer Type", "Layer 2");

        }
        if (vpnApplication.getLayerType() == 3) {

            viewObject.addProperty("Layer Type", "Layer 3");

        }
        if (vpnApplication.getLayerType() == 0) {

            viewObject.addProperty("Layer Type", "Not Found");

        }


        viewObject.addProperty("Submission Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSubmissionDate(), "dd-MMM-yyyy"));
        viewObject.addProperty("Suggested Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSuggestedDate(), "dd-MMM-yyyy"));
        if (vpnApplication.getUserId() != null
                && vpnApplication.getUserId() > 0
        ) {
            try {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(vpnApplication.getUserId());
                viewObject.addProperty("Submitted By", userDTO.getFullName());
            } catch (NoDataFoundException e) {
                log.fatal(e.getMessage());
            }

        } else {
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId());
            if (clientDTO != null) {

                viewObject.addProperty("Submitted By", clientDTO.getName());
            }
        }


        return viewObject;


    }

    public JsonObject linkViewObjectBuilder(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink, JsonObject jsonObject, LoginDTO loginDTO) {

        JsonObject viewObject = new JsonObject();
        viewObject.addProperty("Status", new FlowService().getStateById(vpnApplicationLink.getLinkState().getState()).getViewDescription());
        viewObject.addProperty("Application Link ID", vpnApplicationLink.getId());
        if (vpnApplicationLink.getNetworkLinkId() > 0) {

            viewObject.addProperty("Network Link ID", vpnApplicationLink.getNetworkLinkId());

        }
        viewObject.addProperty("Link Name", vpnApplicationLink.getLinkName());

        double bandwidth = vpnApplicationLink.getLinkBandwidth();
        if (bandwidth > 0) {

            viewObject.addProperty("Requested Bandwidth", bandwidth + " Mbps");
        }

        if (vpnApplicationLink.getDemandNoteId() != null) {
            if (vpnApplicationLink.getDemandNoteId() != 0) {
                viewObject.addProperty("Demand Note ID", vpnApplicationLink.getDemandNoteId());
            }
        }


        if (loginDTO.getUserID() > 0) {
            UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

            if (userDTO.isBTCLPersonnel()) {

                viewObject.addProperty("Demand Note Skip", vpnApplicationLink.isSkipPayment() ? "Yes" : "No");
            }
        } else {
            viewObject.addProperty("Demand Note Skip", vpnApplicationLink.isSkipPayment() ? "Yes" : "No");
        }

        if (vpnApplicationLink.getLocalOffice() != null) {
            if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                int localLoop = vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getLoopProvider();
                long terminalDevice = vpnApplicationLink.getLocalOfficeTerminalDeviceProvider();
//
                if (vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).isCompleted()) {
                    viewObject.addProperty("Local End Loop Provider", localLoop == 1 ? "BTCL(Reused)" : "Client(Reused)");

                } else {

                    viewObject.addProperty("Local End Loop Provider", localLoop == 1 ? "BTCL" : "Client");
                }
                viewObject.addProperty("Local End Terminal Device Provider", terminalDevice == 1 ? "BTCL" : "Client");
            }
        }

        if (vpnApplicationLink.getRemoteOffice() != null) {
            if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                int remoteLoop = vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getLoopProvider();
                long terminalDevice = vpnApplicationLink.getRemoteOfficeTerminalDeviceProvider();

                if (vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).isCompleted()) {
                    viewObject.addProperty("Remote End Loop Provider", remoteLoop == 1 ? "BTCL(Reused)" : "Client(Reused)");
                } else {

                    viewObject.addProperty("Remote End Loop Provider", remoteLoop == 1 ? "BTCL" : "Client");
                }
//

                viewObject.addProperty("Remote End Terminal Device Provider", terminalDevice == 1 ? "BTCL" : "Client");

            }
        }

        viewObject = commentBuilder(vpnApplication, vpnApplicationLink, viewObject);


        return viewObject;

    }

    private String commentStringBuilder(Comment comment, VPNApplication vpnApplication) {
        String commentString = "";
        String commentVal = "";
        String commentPersonName = "";
        UserDTO userDTO = new UserDTO();
        common.ClientDTO clientDTO = new common.ClientDTO();

        if (comment.getUserId() != -1) {

            userDTO = UserRepository.getInstance().getUserDTOByUserID(comment.getUserId());
            if (userDTO.getUsername() != null) {

                commentPersonName = userDTO.getFullName() != "" ? userDTO.getFullName() + " ( " + userDTO.getDesignation() + " )" : userDTO.getUsername() + " ( " + userDTO.getDesignation() + " )";
            }
        } else {
            clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId());
            if (clientDTO.getLoginName() != null) {
                commentPersonName = clientDTO.getName();
            }

        }
        commentVal += comment.getComment().replaceAll("\"", " ");


        if (commentPersonName == "") {
            commentString = "Comments " + " On" + TimeConverter.getDateTimeStringByMillisecAndDateFormat(comment.getSubmissionDate(), "dd-MM-YYYY") + " : " + commentVal;
        } else {
            commentString = "Comments by " + commentPersonName + " On " + TimeConverter.getDateTimeStringByMillisecAndDateFormat(comment.getSubmissionDate(), "dd-MM-YYYY") + " : " + commentVal;
        }
        return commentString;
    }


    public JsonObject commentBuilder(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink, JsonObject viewObject) {
        try {
            List<Comment> Comment = commentsService.getCommentByState(vpnApplicationLink.getLinkState().getState(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getId());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            common.ClientDTO clientDTO = new common.ClientDTO();
            if (Comment.size() > 0) {
                if (Comment.get(0).getUserId() != -1) {

                    userDTO = UserRepository.getInstance().getUserDTOByUserID(Comment.get(0).getUserId());
                    if (userDTO.getUsername() != null) {

                        commentPersonName = userDTO.getFullName() != "" ? userDTO.getFullName() : userDTO.getUsername();
                    }
                } else {
                    clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId());
                    if (clientDTO.getLoginName() != null) {
                        commentPersonName = clientDTO.getName();
                    }

                }
                commentVal += Comment.get(0).getComment().replaceAll("\"", " ");


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


    public JsonArray commentArrayBuilder(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink) throws Exception {
        JsonArray jsonArray = new JsonArray();
        try {
            List<Comment> comments = commentsService.getCommentByState(vpnApplicationLink.getLinkState().getState(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getId());
            for (Comment comment :
                    comments) {
                JsonObject jsonObject = new JsonObject();
                String commentStringBuilder = commentStringBuilder(comment, vpnApplication);

//                jsonObject.addProperty("Comment"+comment.getId(),commentStringBuilder);
                jsonObject.addProperty("comment", commentStringBuilder);
                jsonArray.add(jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

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

    public JsonArray userArrayBuilder(long roleID, boolean isBTCLPersonal) {


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
            if (isBTCLPersonal) {
                if (user.isBTCLPersonnel()) {

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
            } else {
                if (!user.isBTCLPersonnel()) {
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

}
