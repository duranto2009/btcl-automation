package vpn.FlowConnectionManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ClientDTO;
import common.ModuleConstants;
import common.bill.BillService;
import common.repository.AllClientRepository;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import entity.efr.EFRService;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConditionBuilder;
import entity.localloop.LocalLoopService;
import entity.office.Office;
import entity.office.OfficeService;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import inventory.InventoryService;
import location.ZoneService;
import login.LoginDTO;
import officialLetter.OfficialLetterService;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.connection.Connection;

import java.util.*;

public class FlowViewDataBuilder {
    VPNApplicationService vpnApplicationService = ServiceDAOFactory.getService(VPNApplicationService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    BillService billService = ServiceDAOFactory.getService(BillService.class);
    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    //CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
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
//            if (vpnApplication.getConnectionId() > 0) {
//                vpnConnection = vpnFlowConnectionService.getConnectionByID(vpnApplication.getConnectionId());
//
//                if(vpnConnection.getID()>0){
//                    JsonElement jsonElement = gson.toJsonTree(vpnConnection);
//                    jsonObject.add("connection", jsonElement);
//                }

//            }

//            if (vpnApplication.getSourceConnectionID() > 0) {
//                VPNConnection sourceConnection = vpnFlowConnectionService.getConnectionByID(vpnApplication.getSourceConnectionID());
//
//                if(sourceConnection.getID()>0){
//                    JsonElement jsonElement = gson.toJsonTree(sourceConnection);
//                    jsonObject.add("sourceConnection", jsonElement);
//                }
//
//            }


            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition()

            );
            JsonArray linkArray = linkArrayBuilder(vpnApplicationLinks, loginDTO);
            JsonObject viewObject = rootViewObjectBuilder(vpnApplication, jsonObject, loginDTO);
            jsonArray = UILabelBuilder(viewObject);
            jsonObject.add("formElements", jsonArray);
            jsonObject.add("vpnApplicationLinks", linkArray);
            JsonArray officialLetterInfo = officialLetterService.
                    getOfficialLettersByApplicationIdAndModuleId(vpnApplication.getApplicationId(),ModuleConstants.Module_ID_VPN);
            jsonObject.add("officialLetterInfo",officialLetterInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

//        officeBuilder(vpnApplication, jsonObject);

        return jsonObject;
    }



    public JsonArray linkArrayBuilder(List<VPNApplicationLink> vpnApplicationLinks, LoginDTO loginDTO) {
        JsonArray linkArray = new JsonArray();
        Gson gson = new Gson();
        for (VPNApplicationLink t : vpnApplicationLinks) {
            try {
                ArrayList<Integer> zones = (ArrayList<Integer>) zoneService.getUserZone((int) loginDTO.getUserID());
                UserDTO userDTO = null;


                //check if cable division logged in and has any efr


                Office localOffice = officeBuilderWithChildData(
                        t.getLocalOfficeId()
                        , t.getLocalOfficeLoopId()
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
                        , loginDTO
                        , userDTO
                );
                if (remoteOffice != null) {
                    if (remoteOffice.getLocalLoops() != null) {
                        t.setRemoteOffice(remoteOffice);
                    }
                }


                JsonElement jsonElement = gson.toJsonTree(t);
                FlowState currentState = vpnApplicationService.getCurrentState(t.getLinkState().getState());


                jsonElement.getAsJsonObject().add("state", gson.toJsonTree(currentState));
                JsonObject viewObject = linkViewObjectBuilder(t, jsonElement.getAsJsonObject(), loginDTO);
                JsonArray formArray = UILabelBuilder(viewObject);
                jsonElement.getAsJsonObject().add("formElements", formArray);
                if (t.getLocalOffice() != null || t.getRemoteOffice() != null) {

                    linkArray.add(jsonElement);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return linkArray;
    }

    public Office officeBuilderWithChildData(Long officeId, Long LoopId, LoginDTO loginDTO, UserDTO userDTO) throws Exception {
        Office office = globalService.findByPK(Office.class, officeId);
        List<LocalLoop> localLoops = new ArrayList<>();
        localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .idEquals(LoopId)
                        .getCondition()
        );

        List<EFR> OfficeEfrs = new ArrayList<>();
        OfficeEfrs = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(office.getId())
                        .getCondition()
        );


        if (localLoops.size() > 0) {

            if (OfficeEfrs.size() == 0) {

                office.setEfrs(new ArrayList<>());
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

//            jsonObject.addProperty("bandwidth", vpnApplication.getBandwidth());
//            jsonObject.add("connectionType", gson.toJsonTree(new VPNDropdownPair(vpnApplication.getConnectionType(), VPNConnectionConstants.connectionTypeMap.get(vpnApplication.getConnectionType()))));

//            jsonObject.add("loopProvider", gson.toJsonTree(new VPNDropdownPair(vpnApplication.getLoopProvider(), VPNConnectionConstants.loopProviderMap.get(vpnApplication.getLoopProvider()))));
//            jsonObject.addProperty("duration", vpnApplication.getDuration());
            jsonObject.addProperty("suggestedDate", vpnApplication.getSuggestedDate());
            jsonObject.addProperty("clientId", vpnApplication.getClientId());

            jsonObject.addProperty("applicationId", vpnApplication.getApplicationId());
            jsonObject.addProperty("vpnApplicationId", vpnApplication.getVpnApplicationId());
            jsonObject.addProperty("applicationType", vpnApplication.getApplicationType().name());
            jsonObject.addProperty("layerType", vpnApplication.getLayerType());


//            VPNDropdownPair VPNDropdownPair = new VPNDropdownPair(vpnApplication.getClientID(), AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientID()).getLoginName());
//
//            jsonObject.add("client", gson.toJsonTree(VPNDropdownPair));

//            jsonObject.add("user", vpnApplication.getUserId() != null ?
//                    gson.toJsonTree(new VPNDropdownPair(vpnApplication.getUserID(), UserRepository.getInstance().getUserDTOByUserID(vpnApplication.getUserID()).getUsername())) : gson.toJsonTree(null));//problem
//

            jsonObject.addProperty("submissionDate", vpnApplication.getSubmissionDate());
            //      jsonObject.add("status", context.serialize( new VPNDropdownPair(vpnApplication.getStatus(), VPNConnectionConstants.applicationStatusMap.get(vpnApplication.getStatus())) ));
            jsonObject.addProperty("status", new FlowService().getStateById(vpnApplication.getApplicationState().getState()).getViewDescription());

            if (loginDTO.getUserID() > 0) {
            } else {

                jsonObject.addProperty("demandNoteID", vpnApplication.getDemandNoteId());

            }
            jsonObject.addProperty("skipPay", vpnApplication.isSkipPayment());



//            jsonObject.add("applicationType", gson.toJsonTree(new VPNDropdownPair(vpnApplication.getApplicationType(), VPNConnectionConstants.applicationTypeNameMap.get(vpnApplication.getApplicationType()))));
            jsonObject.addProperty("comment", vpnApplication.getComment());

            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_VPN);
            jsonObject.addProperty("description", vpnApplication.getDescription());
            jsonObject.addProperty("state", vpnApplication.getApplicationState().getState());
            jsonObject.addProperty("color", new FlowService().getStateById(vpnApplication.getApplicationState().getState()).getColor());
//            jsonObject.addProperty("connectionID", vpnApplication.getConnectionId());

            //next states
            FlowState flowState = vpnApplicationService.getCurrentState(vpnApplication.getApplicationState().getState());

            JsonElement jsonElement = gson.toJsonTree(flowState);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject.add("state", jsonObject1);

            String val = "";
//

//            jsonObject.addProperty("requestForCorrectionComment", vpnApplication.getRequestForCorrectionComment());

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

//        if (vpnApplication.getConnectionId() > 0 && vpnConnection.getID() > 0) {
//            viewObject.addProperty("Connection Name", vpnConnection.getName());
//            viewObject.addProperty("Existing Bandwidth", vpnConnection.getBandwidth()+" " + " Mbps");
//        }

//        if (vpnApplication.getSourceConnectionID() > 0) {
//            VPNConnection sourceConnection = null;
//            try {
//                sourceConnection = vpnFlowConnectionService.getConnectionByID(vpnApplication.getSourceConnectionID());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if(sourceConnection.getID()>0){
//
//                viewObject.addProperty("Source Connection Name", sourceConnection.getName());
//                viewObject.addProperty("Source Connection Bandwidth", sourceConnection.getBandwidth()+ " Mbps");
//                viewObject.addProperty("Source Connection Type", VPNConnectionConstants.connectionTypeMap.get(vpnApplication.getConnectionType()));
//            }
//
//        }

        if (vpnApplication.getLayerType() == 2) {

            viewObject.addProperty("Layer Type", "Layer 2");

        }
        if (vpnApplication.getLayerType() == 3) {

            viewObject.addProperty("Layer Type", "Layer 3");

        }
//        if(vpnApplication.getConnectionType()==VPNConnectionConstants.CONNECTION_TYPE_TEMPORARY){
//
//            viewObject.addProperty("Duration",vpnApplication.getDuration()+ " Days");
//        }


//        if (loginDTO.getUserID() > 0) {
//            UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
//
//            if (userDTO.isBTCLPersonnel()) {
//
//                viewObject.addProperty("Demand Note Skip", vpnApplication.isSkipPayment() ? "Yes" : "No");
//            }
//        } else {
////            viewObject.addProperty("Demand Note Skip",vpnApplication.isSkipPayment()?"Yes":"No");
//        }


        viewObject.addProperty("Submission Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSubmissionDate(), "dd-MMM-yyyy"));
        viewObject.addProperty("Suggested Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSuggestedDate(), "dd-MMM-yyyy"));
        if (vpnApplication.getUserId() > 0) {
            UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(vpnApplication.getUserId());
            if (userDTO != null) {

                viewObject.addProperty("Submitted By", userDTO.getFullName());
            }

        } else {
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId());
            if (clientDTO != null) {

                viewObject.addProperty("Submitted By", clientDTO.getName());
            }
        }
//        viewObject = commentBuilder(vpnApplication, viewObject);


        return viewObject;


    }

    public JsonObject linkViewObjectBuilder(VPNApplicationLink vpnApplicationLink, JsonObject jsonObject, LoginDTO loginDTO) {

        JsonObject viewObject = new JsonObject();
        viewObject.addProperty("Status", new FlowService().getStateById(vpnApplicationLink.getLinkState().getState()).getViewDescription());
        viewObject.addProperty("Link ID", vpnApplicationLink.getId());
        viewObject.addProperty("Link Name", vpnApplicationLink.getLinkName());
        double bandwidth = vpnApplicationLink.getLinkBandwidth();
        if (bandwidth > 0) {

            viewObject.addProperty("Bandwidth", bandwidth + " Mbps");
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

        return viewObject;

    }

//    public JsonObject commentBuilder(VPNApplication vpnApplication, JsonObject viewObject) {
//        try {
//            List<Comments> Comment = commentsService.getCommentsByState(vpnApplication.getState(), vpnApplication.getApplicationID());
//            String commentVal = "";
//            String commentPersonName = "";
//            UserDTO userDTO = new UserDTO();
//            common.ClientDTO clientDTO = new common.ClientDTO();
//            if (Comment.size() > 0) {
//                if (Comment.get(0).getUserID() != -1) {
//
//                    userDTO = UserRepository.getInstance().getUserDTOByUserID(Comment.get(0).getUserID());
//                    if (userDTO.getUsername() != null) {
//
//                        commentPersonName = userDTO.getFullName()!=""?userDTO.getFullName():userDTO.getUsername();
//                    }
//                    commentVal += Comment.get(0).getComments().replaceAll("\"", " ");
//                } else {
//                    clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientID());
//                    if (clientDTO.getLoginName() != null) {
//                        commentPersonName = clientDTO.getName();
//                    }
//                    commentVal += Comment.get(0).getComments().replaceAll("\"", " ");
//
//                }
//
//            } else {
//                commentVal = "Not Applicable or given";
//            }
//
//            if (commentPersonName == "") {
//                viewObject.addProperty("Comments ", commentVal);
//            } else {
//                viewObject.addProperty("Comments by " + commentPersonName, commentVal);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return viewObject;
//    }

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

}
