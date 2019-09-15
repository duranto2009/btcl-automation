package lli.Application.FlowConnectionManager;

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
import flow.repository.FlowRepository;
import global.GlobalService;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalIP.LLIAdditionalIPService;
import lli.Application.AdditionalPort.AdditionalPort;
import lli.Application.AdditionalPort.AdditionalPortConditionBuilder;
import lli.Application.AdditionalPort.LLIAdditionalPortService;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.IFR.IFR;
import lli.Application.IFR.IFRService;
import lli.Application.LLIApplication;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.newOffice.NewOffice;
import lli.Application.newOffice.NewOfficeService;
import lli.Comments.Comments;
import lli.Comments.CommentsService;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import location.ZoneService;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;

import java.util.*;
import java.util.stream.Collectors;

public class FlowDataBuilder {
    IFRService ifrService = ServiceDAOFactory.getService(IFRService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);
    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    BillService billService = ServiceDAOFactory.getService(BillService.class);
    ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    NewOfficeService newOfficeService = ServiceDAOFactory.getService(NewOfficeService.class);
    LLIFlowConnectionService lliFlowConnectionService = ServiceDAOFactory.getService(LLIFlowConnectionService.class);
    public static final int vendorRoleID = 16020;
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);

    public JsonObject getCommonPart_new(LLIApplication lliApplication, JsonObject jsonObject, LoginDTO loginDTO) {
        //Serialize Common LLI Application
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        LLIConnection lliConnection = new LLIConnection();

//        Map session = ActionContext.get("session");
        try {
            jsonObject = basicDataBuilder(jsonObject, lliApplication, loginDTO);
            if (lliApplication.getConnectionId() > 0) {
                lliConnection = lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());

                if (lliConnection.getID() > 0) {
                    JsonElement jsonElement = gson.toJsonTree(lliConnection);
                    jsonObject.add("connection", jsonElement);
                }

            }

            if (lliApplication.getSourceConnectionID() > 0) {
                LLIConnection sourceConnection = lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID());

                if (sourceConnection.getID() > 0) {
                    JsonElement jsonElement = gson.toJsonTree(sourceConnection);
                    jsonObject.add("sourceConnection", jsonElement);
                }

            }

            JsonObject viewObject = viewObjectBuilder(lliApplication, lliConnection, jsonObject, loginDTO);
            JsonObject actionObject = new JsonObject();

            //region action
            List<FlowState> flowStates = (loginDTO.getRoleID() > 0 ?
                    FlowRepository.getInstance().getNextStatesByCurrentStateAndRoleId(lliApplication.getState(), (int)loginDTO.getRoleID())
                    : FlowRepository.getInstance().getNextStatesByCurrentState(lliApplication.getState())
                );
//            if (loginDTO.getRoleID() > 0) {
//                flowStates = new FlowService().getNextStatesFromStateRole(lliApplication.getState(), (int) loginDTO.getRoleID());
//
//            } else {
//
//                flowStates = new FlowService().getNextStatesFromState(lliApplication.getState());
//            }

            for (FlowState flowState : flowStates) {
                actionObject.addProperty(flowState.getDescription(), flowState.getId());
            }

            //endregion
            jsonArray = UILabelBuilder(viewObject);
            JsonArray actionArray = UILabelBuilder(actionObject);
            JsonArray ifrArray = ifrArrayBuilder(lliApplication, false);
            JsonArray vendorArray = userArrayBuilder(vendorRoleID, false);

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

            JsonArray efrArray = efrArrayBuilder(lliApplication, false, loginDTO);
            JsonArray efrArray2 = efrArrayBuilder(lliApplication, true, loginDTO);

            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
            jsonObject.add("ifr", ifrArray);
            jsonObject.add("vendors", vendorArray);


            jsonObject.add("fiberdivisions", userArrayBuilder(vendorRoleID, true));
            jsonObject.add("pops", popArray);
            jsonObject.add("incompleteEFR", efrArray);
            jsonObject.add("completeEFR", efrArray2);
            if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP ||
                    lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT||
                    (lliApplication.getApplicationType()==LLIConnectionConstants.UPGRADE_BANDWIDTH&&lliApplication.isNewLoop())
            ) {

                List<NewLocalLoop> newLocalLoopList = getAllNewLoclLoops(lliApplication);
                jsonObject.add("newlocalloops", newlocalLoopsArrayBuilder(newLocalLoopList,lliApplication));
            }

            if (lliApplication.isNewLoop()) {
                List<LocalLoop> localLoopList = getAllLoclLoops(lliApplication);

                List<NewLocalLoop> newLocalLoopList = getAllNewLoclLoops(lliApplication);

                List<Long> oldLoopIds = new ArrayList<>();
                newLocalLoopList
                        .forEach(
                                t -> oldLoopIds.add(t.getOldLoopId())
                        );
                List<LocalLoop> modifiedList = new ArrayList<>();

                if (lliApplication.getApplicationType() == LLIConnectionConstants.UPGRADE_BANDWIDTH) {
                    modifiedList = localLoopList
                            .stream()
                            .filter(t -> !oldLoopIds.contains(t.getHistoryID()))
                            .collect(Collectors.toList());

                    for (NewLocalLoop t : newLocalLoopList
                    ) {


                        LocalLoop localLoop = new LocalLoop();
                        localLoop.setApplicationID(t.getApplicationID());
                        try {
                            localLoop.setOfficeID(newOfficeService.getOneOfficeById(t.getOfficeID()).getOld_office_id());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        localLoop.setPopID(t.getPopID());
                        localLoop.setBandwidth(t.getBandwidth());
                        localLoop.setOfcType(t.getOfcType());
                        localLoop.setRouter_switchID(t.getRouter_switchID());
                        localLoop.setClientDistances(t.getClientDistances());
                        localLoop.setBTCLDistances(t.getBTCLDistances());
                        localLoop.setOCDistances(t.getOCDistances());
                        localLoop.setAdjustedBTClDistance(t.getAdjustedBTClDistance());
                        localLoop.setAdjustedOCDistance(t.getAdjustedOCDistance());
                        localLoop.setOCID(t.getOCID());
                        localLoop.setPortID(t.getPortID());
                        localLoop.setVLANID(t.getVLANID());
                        modifiedList.add(localLoop);

                    }

                    List<IFR> ifrs=ifrService.getIFRByAppID(lliApplication.getApplicationID())
                            .stream()
                            .filter(t-> t.getIsSelected()==1)
                            .collect(Collectors.toList());


                    List<Long> popIds=new ArrayList<>();

                    ifrs.forEach(t-> popIds.add( t.getPopID()));

                    List<LocalLoop>modifiedLoopList=modifiedList
                            .stream()
                            .filter(
                                    t-> popIds.contains(t.getPopID())
                            )
                            .collect(Collectors.toList());

                    jsonObject.add("modifiedlocalloops", localLoopsArrayBuilder(modifiedLoopList,lliApplication));



                }




                jsonObject.add("localloops", localLoopsArrayBuilder(localLoopList,lliApplication));
                jsonObject.add("newlocalloops", newlocalLoopsArrayBuilder(newLocalLoopList,lliApplication));
            }
            else {
                List<LocalLoop> localLoopList = getAllLoclLoops(lliApplication);
                jsonObject.add("localloops", localLoopsArrayBuilder(localLoopList,lliApplication));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion

        officeBuilder(lliApplication, jsonObject);

        return jsonObject;
    }


    public JsonObject officeBuilder(LLIApplication lliApplication, JsonObject jsonObject) {
        try {
//            JsonElement jsonElement=;
            Gson gson = new Gson();
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

            //Jami

            if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                    || lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_PORT
                    || lliApplication.isNewLoop()
            ) {

                AdditionalPort additionalPort = ServiceDAOFactory.getService(LLIAdditionalPortService.class).
                        getAdditionalPortByApplication(lliApplication.getApplicationID());
                jsonObject.addProperty("portCount", lliApplication.getPort());
                if(additionalPort!=null) {
                    jsonObject.addProperty("officeType", additionalPort.getOfficeType());
                    jsonObject.addProperty("loopType", additionalPort.getLoopType());
                }
                JsonArray jsonArray2 = new JsonArray();

                List<NewOffice> officeList = lliApplication.getNewOfficeList();
                if (officeList == null) {
                    officeList = newOfficeService.getOffice(lliApplication.getApplicationID());
                }
                for (NewOffice office : officeList) {

                    JsonElement jsonElement = gson.toJsonTree(office);
                    JsonObject jsonObject1 = (JsonObject) jsonElement;

                    jsonArray2.add(jsonObject1);

                }
                jsonObject.add("newOfficeList", jsonArray2);
            }

            if (lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP) {
                jsonObject.addProperty("ipCount", lliApplication.getIp());
            }

            //end

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2 = commentBuilder(lliApplication, jsonObject2);
        jsonObject.add("Comments", jsonObject2);

        return jsonObject;
    }

    public JsonObject basicDataBuilder(JsonObject jsonObject, LLIApplication lliApplication, LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();

            jsonObject.addProperty("bandwidth", lliApplication.getBandwidth());
            jsonObject.add("connectionType", gson.toJsonTree(new LLIDropdownPair(lliApplication.getConnectionType(), LLIConnectionConstants.connectionTypeMap.get(lliApplication.getConnectionType()))));

            jsonObject.add("loopProvider", gson.toJsonTree(new LLIDropdownPair(lliApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliApplication.getLoopProvider()))));
            jsonObject.addProperty("duration", lliApplication.getDuration());
            jsonObject.addProperty("suggestedDate", lliApplication.getSuggestedDate());

            jsonObject.addProperty("applicationID", lliApplication.getApplicationID());


            LLIDropdownPair LLIDropdownPair = new LLIDropdownPair(lliApplication.getClientID(), AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName());

            jsonObject.add("client", gson.toJsonTree(LLIDropdownPair));

            if( lliApplication.getUserID()!=null && lliApplication.getUserID()>0){

                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(lliApplication.getUserID());
                if(userDTO!=null) {
                    jsonObject.add("user", gson.toJsonTree(new LLIDropdownPair(lliApplication.getUserID(),
                            userDTO.getUsername())));//problem
                }
            }else{
                jsonObject.add("user", gson.toJsonTree(null));
            }

            jsonObject.addProperty("submissionDate", lliApplication.getSubmissionDate());
            //      jsonObject.add("status", context.serialize( new LLIDropdownPair(lliApplication.getStatus(), LLIConnectionConstants.applicationStatusMap.get(lliApplication.getStatus())) ));
            jsonObject.addProperty("status", FlowRepository.getInstance().getFlowStateByFlowStateId(lliApplication.getState()).getViewDescription());
//
            jsonObject.addProperty("content", lliApplication.getContent());
//            jsonObject.addProperty("comment", lliApplication.getComment());
            if (loginDTO.getUserID() > 0) {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

                if (lliApplication.getDemandNoteID() != null && userDTO.isBTCLPersonnel()) {
                    jsonObject.addProperty("demandNoteID", lliApplication.getDemandNoteID());
//                    jsonObject.addProperty("skipPay",lliApplication.getSkipPayment());
                    BillDTO billDTO = billService.getBillByBillID(lliApplication.getDemandNoteID());
                    jsonObject.add("bill", new Gson().toJsonTree(billDTO));
                }
            } else {

                if (lliApplication.getDemandNoteID() != null) {

                    jsonObject.addProperty("demandNoteID", lliApplication.getDemandNoteID());
                }

            }
            jsonObject.addProperty("skipPay", lliApplication.getSkipPayment());


//            if(lliApplication.getDemandNoteID()>0){
////                BillDTO billDTO=billService.get
//            }
            jsonObject.add("applicationType", gson.toJsonTree(new LLIDropdownPair(lliApplication.getApplicationType(), LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType()))));
            jsonObject.addProperty("comment", lliApplication.getComment());

            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_LLI);
            jsonObject.addProperty("description", lliApplication.getDescription());
            jsonObject.addProperty("state", lliApplication.getState());
            jsonObject.addProperty("color", new FlowService().getStateById(lliApplication.getState()).getColor());
            jsonObject.addProperty("connectionID", lliApplication.getConnectionId());
            jsonObject.addProperty("isNewLoop", lliApplication.isNewLoop());

            String val = "";

            if (lliApplication.getZoneId() > 0) {
                val = zoneService.getZonebyId(lliApplication.getZoneId()).getNameEng();
                jsonObject.add("zone", gson.toJsonTree(new LLIDropdownPair(lliApplication.getZoneId(), val)));//need to do from db
            }

//            jsonObject.addProperty("requestForCorrectionComment", lliApplication.getRequestForCorrectionComment());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject viewObjectBuilder(LLIApplication lliApplication, LLIConnection lliConnection, JsonObject jsonObject, LoginDTO loginDTO) throws Exception{

        JsonObject viewObject = new JsonObject();

        Gson gson = new Gson();

        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", lliApplication.getApplicationID());
        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName()));
        //bandwidth

        double bandwidth = jsonObject.get("bandwidth").getAsDouble();

        if (bandwidth > 0) {

            viewObject.addProperty("Bandwidth", jsonObject.get("bandwidth") + " Mbps");
        }
        viewObject.add("Application Type", gson.toJsonTree(LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType())));

        if (jsonObject.get("demandNoteID") != null) {
            viewObject.addProperty("Demand Note ID", lliApplication.getDemandNoteID());
        }
        if(lliApplication.getApplicationType() == LLIConnectionConstants.ADDITIONAL_IP){
            LLIAdditionalIP lliAdditionalIP = ServiceDAOFactory.getService(LLIAdditionalIPService.class).
                                                getAdditionalIPByApplication(lliApplication.getApplicationID());
            viewObject.addProperty("IP Count", lliAdditionalIP.getIpCount());
        }
        if (lliApplication.getConnectionId() > 0 && lliConnection.getID() > 0) {
            viewObject.addProperty("Connection Name", lliConnection.getName());
            viewObject.addProperty("Existing Bandwidth", lliConnection.getBandwidth() + " " + " Mbps");
        }

        if (lliApplication.getSourceConnectionID() > 0) {
            LLIConnection sourceConnection = null;
            try {
                sourceConnection = lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sourceConnection.getID() > 0) {

                viewObject.addProperty("Source Connection Name", sourceConnection.getName());
                viewObject.addProperty("Source Connection Bandwidth", sourceConnection.getBandwidth() + " Mbps");
                viewObject.addProperty("Source Connection Type", LLIConnectionConstants.connectionTypeMap.get(sourceConnection.getConnectionType()));
            }

        }

        if (lliApplication.getConnectionType() != 0) {

            viewObject.addProperty("Connection Type", LLIConnectionConstants.connectionTypeMap.get(lliApplication.getConnectionType()));
        }
        if (lliApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY) {

            viewObject.addProperty("Duration", lliApplication.getDuration() + " Days");
        }


        if (loginDTO.getUserID() > 0) {
            UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

            if (userDTO.isBTCLPersonnel()) {

                viewObject.addProperty("Demand Note Skip", lliApplication.getSkipPayment() == 1 ? "Yes" : "No");
            }
        } else {
            viewObject.addProperty("Demand Note Skip", lliApplication.getSkipPayment() == 1 ? "Yes" : "No");
        }
        if (lliApplication.getZoneId() > 0 && jsonObject.has("zone")) {

            JsonObject object = (JsonObject) jsonObject.get("zone");
            String val = object.get("label").getAsString();
            viewObject.addProperty("Zone", val);//need to do from db
        }

        JsonObject loop = (JsonObject) jsonObject.get("loopProvider");

        if (jsonObject.get("loopProvider").getAsJsonObject().get("label") != null) {
            viewObject.addProperty("Loop Provider", jsonObject.get("loopProvider") != null
                    ? jsonObject.get("loopProvider").getAsJsonObject().get("label").getAsString() : "NOT FOUND");
        }
        viewObject.addProperty("Description", lliApplication.getDescription() != null ? lliApplication.getDescription() : "Not Given");


        if(
                lliApplication.getApplicationType()==LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP
                ||lliApplication.getApplicationType()==LLIConnectionConstants.ADDITIONAL_PORT
        ){
            AdditionalPort additionalPort=ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(AdditionalPort.class
                    ,new AdditionalPortConditionBuilder()
                    .Where()
                    .applicationIdEquals(lliApplication.getApplicationID())
                    .getCondition()
                    ).get(0);

            if(additionalPort!=null){

                viewObject.addProperty("Port Count", additionalPort.getPortCount());
                 String effectInLoop="";
                 if(additionalPort.getLoopType()==1){
                     effectInLoop="Reuse existing POP For new loop";

                 }
                else if(additionalPort.getLoopType()==2){
                    effectInLoop="Adding new POP For new loop";

                }
                 else if(additionalPort.getLoopType()==3){
                     effectInLoop="Replacing POP For new Local loop";

                 }
                 if(!effectInLoop.equals("")){
                     viewObject.addProperty("Effect in Existing Connection ",effectInLoop);
                 }

            }
        }

        viewObject = commentBuilder(lliApplication, viewObject);


        return viewObject;


    }

    public JsonObject commentBuilder(LLIApplication lliApplication, JsonObject viewObject) {
        try {
            List<Comments> Comment = commentsService.getCommentsByState(lliApplication.getState(), lliApplication.getApplicationID());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            common.ClientDTO clientDTO = new common.ClientDTO();
            if (Comment != null && Comment.size() > 0) {
                if (Comment.get(0).getUserID() != -1) {

                    userDTO = UserRepository.getInstance().getUserDTOByUserID(Comment.get(0).getUserID());
                    if (userDTO.getUsername() != null) {

                        commentPersonName = userDTO.getFullName() != "" ? userDTO.getFullName() : userDTO.getUsername();
                    }
                    commentVal += Comment.get(0).getComments().replaceAll("\"", " ");
                } else {
                    clientDTO = AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID());
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

    public JsonArray efrArrayBuilder(LLIApplication lliApplication, boolean getSubmitted, LoginDTO loginDTO) {

        JsonArray efrArray = new JsonArray();
        Gson gson = new Gson();

        List<EFR> efrlist = new ArrayList<>();
        try {
//            long userID=lliApplication.getUserID() ;
            long userID = -1;
            if (!getSubmitted) {
                efrlist = efrService.getIncomleteEFR(lliApplication.getApplicationID(), loginDTO.getUserID());

            } else {
                efrlist = efrService.getCompletedEFR(lliApplication.getApplicationID(), loginDTO.getUserID());


                if (efrlist.size() == 0) {
                    efrlist = efrService.getCompletedEFR(lliApplication.getApplicationID());
                }
                if(lliApplication.getState() == LLIConnectionConstants.FORWARDED_WORK_ORDER_GENERATE
                        || lliApplication.getState() == LLIConnectionConstants.WORK_ORDER_GENERATE
                ) {
                    // Hard Code
                    efrlist = efrlist.stream()
                            .filter(t->t.getWorkGiven() == 1)
                            .collect(Collectors.toList());
                }
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
            jsonObject1.addProperty("vendorName", userDTO.getFullName().equals("") ? userDTO.getUsername() : userDTO.getFullName());
//            JsonArray array=UILabelBuilder(jsonObject1);
            efrArray.add(jsonObject1);

        }

        return efrArray;


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

    public JsonArray ifrArrayBuilder(LLIApplication lliApplication, boolean getSelected)throws Exception {


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

            // todo : check if the pop has local loop already
            if(ifr.getOfficeID()>0) {
                if(lliApplication.getApplicationType() ==LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP||
                        lliApplication.getApplicationType() ==LLIConnectionConstants.ADDITIONAL_PORT){
                    NewOffice office = newOfficeService.getOneOfficeById(ifr.getOfficeID());
                    ifr.setOfficeAddress(office.getOfficeAddress());
                }
                else {
                    Office office = officeService.getOfficeByHistoryId(ifr.getOfficeID());
                    ifr.setOfficeAddress(office.getOfficeAddress());
                }
            }else {
                ifr.setOfficeAddress("default");
            }
            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(ifr.getPopID());
            jsonObject1.addProperty("popName", inventoryItem.getName());
            ifrArray.add(jsonObject1);

        }

        return ifrArray;


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

    public List<LocalLoop> getAllLoclLoops(LLIApplication lliApplication) {
        List<LocalLoop> lists = new ArrayList<>();
        try {
            if (lliApplication.getConnectionId() > 0) {

                List<Office> officeList = officeService.getOfficeByCONWithoutLoop(lliApplication.getConnectionId());
                for (Office office : officeList
                ) {
                    List<LocalLoop> localLoopList = localLoopService.getLocalLoopByOffice(office.getId());
//                    List<LocalLoop> localLoopList =office.getLoops();
//                    for (LocalLoop localLoop : localLoopList
//                    ) {
//                        localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());
//                        lists.add(localLoop);
//
//                    }

                }

            } else {

                lists = localLoopService.getLocalLoop(lliApplication.getApplicationID());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;

    }


    public List<NewLocalLoop> getAllNewLoclLoops(LLIApplication lliApplication) {
        List<NewLocalLoop> lists = new ArrayList<>();
        try {
            List<NewOffice> officeList = lliApplication.getNewOfficeList();
            if (officeList == null) {
                officeList = newOfficeService.getOffice(lliApplication.getApplicationID());
            }
            for (NewOffice office : officeList) {
                List<NewLocalLoop> localLoopList = office.getLoops();
                for (NewLocalLoop localLoop : localLoopList) {
                    localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());
                    lists.add(localLoop);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;

    }


    public JsonArray localLoopsArrayBuilder(List<LocalLoop> lists,LLIApplication lliApplication) throws Exception {


        Gson gson = new Gson();
        JsonArray localLoopArray = new JsonArray();
        for (LocalLoop localLoop : lists) {

            JsonElement jsonElement = gson.toJsonTree(localLoop);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem pop = inventoryService.getInventoryItemByItemID(localLoop.getPopID());
            jsonObject1.addProperty("popName", pop.getName());

//            if(LLIConnectionConstants.completeStates.containsKey(lliApplication.getState())) {
                List<InventoryItem> router_switches = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_ROUTER, localLoop.getPopID());
                JsonArray router_switchArray = new JsonArray();
                for (InventoryItem router_switch : router_switches) {
                    JsonElement router_swi = gson.toJsonTree(router_switch);
                    JsonObject router_swiAsJsonObject = router_swi.getAsJsonObject();
                    router_switchArray.add(router_swiAsJsonObject);

                }

//            List<InventoryItem> ports = inventoryService
//                    .getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_PORT, localLoop.getRouter_switchID());

                List<Long> rs_ids = router_switches.stream()
                        .map(InventoryItem::getID)
                        .collect(Collectors.toList());
                List<InventoryItem> ports = new ArrayList<>();
                List<InventoryItem> vlans = new ArrayList<>();

                if (!rs_ids.isEmpty()) {
                    Map<Integer, List<InventoryItem>> mapForLocalOfficePortVlan =
                            inventoryService.getInventoryItemsByParentInventoryItems(rs_ids);
                    ports = mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_PORT, Collections.emptyList());
                    vlans = mapForLocalOfficePortVlan.getOrDefault(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, Collections.emptyList());

                }

                JsonArray portArray = new JsonArray();
                JsonArray vlanArray = new JsonArray();

                for (InventoryItem port : ports) {
                    JsonElement pGson = gson.toJsonTree(port);
                    JsonObject pObject = pGson.getAsJsonObject();
                    portArray.add(pObject);
                }


                for (InventoryItem vlan : vlans) {
                    JsonElement vGson = gson.toJsonTree(vlan);
                    JsonObject vObject = vGson.getAsJsonObject();
                    vlanArray.add(vObject);
                }


                jsonObject1.add("router_switch", router_switchArray);
                jsonObject1.add("vlans", vlanArray);
                jsonObject1.add("ports", portArray);
                jsonObject1.addProperty("vlanType", 0);
//            }

            localLoopArray.add(jsonObject1);

        }

        return localLoopArray;

    }

    public JsonArray newlocalLoopsArrayBuilder(List<NewLocalLoop> lists,LLIApplication lliApplication) {
        Gson gson = new Gson();

        JsonArray localLoopArray = new JsonArray();
        for (NewLocalLoop localLoop : lists) {

            JsonElement jsonElement = gson.toJsonTree(localLoop);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            InventoryItem pop = inventoryService.getInventoryItemByItemID(localLoop.getPopID());
            jsonObject1.addProperty("popName", pop.getName());

//            if(LLIConnectionConstants.completeStates.containsKey(lliApplication.getState())) {

                List<InventoryItem> router_switches = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_ROUTER, localLoop.getPopID());
                JsonArray router_switchArray = new JsonArray();
                for (InventoryItem router_switch : router_switches) {
                    JsonElement router_swi = gson.toJsonTree(router_switch);
                    JsonObject router_swiAsJsonObject = router_swi.getAsJsonObject();
                    router_switchArray.add(router_swiAsJsonObject);

                }
                List<InventoryItem> ports = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_PORT, localLoop.getRouter_switchID());
                JsonArray portArray = new JsonArray();
                JsonArray vlanArray = new JsonArray();
                for (InventoryItem port : ports) {
                    JsonElement pGson = gson.toJsonTree(port);
                    JsonObject pObject = pGson.getAsJsonObject();
                    portArray.add(pObject);
                }

                List<InventoryItem> vlans = inventoryService.getInventoryItemListByCatagoryIDAndParentItemID(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN, localLoop.getPortID());
                for (InventoryItem vlan : vlans) {
                    JsonElement vGson = gson.toJsonTree(vlan);
                    JsonObject vObject = vGson.getAsJsonObject();
                    vlanArray.add(vObject);
                }


                jsonObject1.add("router_switch", router_switchArray);
                jsonObject1.add("vlans", vlanArray);
                jsonObject1.add("ports", portArray);
//            }

            localLoopArray.add(jsonObject1);
        }
        return localLoopArray;
    }


}
