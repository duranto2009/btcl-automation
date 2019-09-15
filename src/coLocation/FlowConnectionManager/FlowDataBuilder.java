package coLocation.FlowConnectionManager;

import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.ifr.CoLocationApplicationIFRDTO;
import coLocation.ifr.CoLocationApplicationIFRService;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
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
import inventory.InventoryService;
import lli.Application.LLIApplication;
import lli.Comments.Comments;
import lli.Comments.CommentsService;
import lli.LLIDropdownPair;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.*;

public class FlowDataBuilder {

    CoLocationApplicationIFRService coLocationApplicationIFRService = ServiceDAOFactory.getService(CoLocationApplicationIFRService.class);
    BillService billService=ServiceDAOFactory.getService(BillService.class);
    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    CoLocationConnectionService coLocationConnectionService = ServiceDAOFactory.getService(CoLocationConnectionService.class);
    CoLocationApplicationService coLocationApplicationService = ServiceDAOFactory.getService(CoLocationApplicationService.class);


    CoLocationInventoryTemplateService coLocationInventoryTemplateService = ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class);

    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);

    public JsonObject getCommonPart_new(CoLocationApplicationDTO coLocationApplicationDTO, JsonObject jsonObject, LoginDTO loginDTO) {
        //Serialize Common LLI Application
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        CoLocationConnectionDTO CoLocationConnectionDTO = new CoLocationConnectionDTO();

//        Map session = ActionContext.get("session");
        try {
            jsonObject = basicDataBuilder(jsonObject, coLocationApplicationDTO,loginDTO);
            //todo: fetch data of existing connection: fix below commect out line
            if (coLocationApplicationDTO.getConnectionId() > 0) {
                CoLocationConnectionDTO coLocationConnectionDTO = coLocationConnectionService.getAnyColocationConnection(coLocationApplicationDTO.getConnectionId());
                JsonElement jsonElement = gson.toJsonTree(coLocationConnectionDTO);
                jsonObject.add("connection", jsonElement);

            }

            JsonObject viewObject = viewObjectBuilder(coLocationApplicationDTO, CoLocationConnectionDTO, jsonObject,loginDTO);

            //region action
            List<FlowState> flowStates=coLocationApplicationService.getActionList(coLocationApplicationDTO.getState(), (int) loginDTO.getRoleID());


            JsonArray actionArray =new JsonArray();
            for (FlowState flowState : flowStates
            ) {

                JsonElement jsonElement = gson.toJsonTree(flowState);
                JsonObject jsonObject1 = (JsonObject) jsonElement;
                actionArray.add(jsonElement);
            }

            //endregion
            jsonArray = UILabelBuilder(viewObject);




            JsonArray ifrArray = ifrArrayBuilder(coLocationApplicationDTO, loginDTO);

            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
            jsonObject.add("ifr", ifrArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion



        return jsonObject;
    }



    public JsonObject basicDataBuilder(JsonObject jsonObject, CoLocationApplicationDTO coLocationApplicationDTO,LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();

            jsonObject.add("connectionType", gson.toJsonTree(new CoLocationInventoryTemplateDTO(coLocationApplicationDTO.getConnectionType(), CoLocationConstants.connectionTypeNameMap.get(coLocationApplicationDTO.getConnectionType()))));

            jsonObject.addProperty("suggestedDate", coLocationApplicationDTO.getSuggestedDate());

            jsonObject.addProperty("applicationID", coLocationApplicationDTO.getApplicationID());


//            LLIDropdownPair LLIDropdownPair = new LLIDropdownPair(coLocationApplicationDTO.getClientID(), AllClientRepository.getInstance().getClientByClientID(coLocationApplicationDTO.getClientID()).getLoginName());
//            MethodReferences.newKeyValue_Long_String.apply
            KeyValuePair<Long, String> clientKeyValuePair = new KeyValuePair<>(coLocationApplicationDTO.getClientID(),AllClientRepository.getInstance().getClientByClientID(coLocationApplicationDTO.getClientID()).getLoginName());
            jsonObject.add("client", gson.toJsonTree(clientKeyValuePair));

            jsonObject.add("user", coLocationApplicationDTO.getUserID() != null ?
                    gson.toJsonTree(new LLIDropdownPair(coLocationApplicationDTO.getUserID(),
                            UserRepository.getInstance().getUserDTOByUserID(coLocationApplicationDTO.getUserID()).getUsername())) : gson.toJsonTree(null));//problem
            jsonObject.addProperty("submissionDate", coLocationApplicationDTO.getSubmissionDate());
            //      jsonObject.add("status", context.serialize( new LLIDropdownPair(lliApplication.getStatus(), LLIConnectionConstants.applicationStatusMap.get(lliApplication.getStatus())) ));
            jsonObject.addProperty("status", new FlowService().getStateById(coLocationApplicationDTO.getState()).getViewDescription());
//
            if(loginDTO.getUserID()>0){
                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

                if(coLocationApplicationDTO.getDemandNoteID()!=null&&userDTO.isBTCLPersonnel()){
                    jsonObject.addProperty("demandNoteID", coLocationApplicationDTO.getDemandNoteID());

                    BillDTO billDTO=billService.getBillByBillID(coLocationApplicationDTO.getDemandNoteID());
                    jsonObject.add("bill",new Gson().toJsonTree(billDTO));
//                }
                }
            }else{

                jsonObject.addProperty("demandNoteID", coLocationApplicationDTO.getDemandNoteID());

            }
            jsonObject.addProperty("skipPay",coLocationApplicationDTO.getSkipPayment());


//            if(lliApplication.getDemandNoteID()>0){
////                BillDTO billDTO=billService.get
//            }
            jsonObject.add("applicationType", gson.toJsonTree(new LLIDropdownPair(coLocationApplicationDTO.getApplicationType(),
                    CoLocationConstants.applicationTypeNameMap.get(coLocationApplicationDTO.getApplicationType()))));
            if(coLocationApplicationDTO.getDescription()!=null && !coLocationApplicationDTO.getDescription().isEmpty()){
                jsonObject.addProperty("description",coLocationApplicationDTO.getDescription());
            }
            jsonObject.addProperty("comment", coLocationApplicationDTO.getComment());

            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_COLOCATION);
//            jsonObject.addProperty("description", coLocationApplicationDTO.getDescription());

            FlowState flowState=coLocationApplicationService.getCurrentState(coLocationApplicationDTO.getState());

            JsonElement jsonElement = gson.toJsonTree(flowState);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject.add("state",jsonObject1);
            jsonObject.addProperty("color", new FlowService().getStateById(coLocationApplicationDTO.getState()).getColor());
            jsonObject.addProperty("rackNeeded", coLocationApplicationDTO.isRackNeeded());
            if(coLocationApplicationDTO.isRackNeeded()){

                jsonObject.add("rackSize", gson.toJsonTree(coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getRackTypeID())));
                jsonObject.add("rackSpace", gson.toJsonTree(coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getRackSpace())));
//                jsonObject.addProperty("rackSpace", coLocationApplicationDTO.getRackSpace());

            }
            jsonObject.addProperty("powerNeeded", coLocationApplicationDTO.isPowerNeeded());
            if(coLocationApplicationDTO.isPowerNeeded()){

//                jsonObject.addProperty("powerType", coLocationApplicationDTO.getPowerType());
                jsonObject.add("powerType", gson.toJsonTree(coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getPowerType())));

//                jsonObject.addProperty("powerAmount", coLocationApplicationDTO.getPowerAmount());
                jsonObject.addProperty("powerAmount",coLocationApplicationDTO.getPowerAmount()
//                        gson.toJsonTree(coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getPowerAmount()))
                );


            }
            jsonObject.addProperty("fiberNeeded", coLocationApplicationDTO.isFiberNeeded());
            if(coLocationApplicationDTO.isFiberNeeded()){

                jsonObject.add("fiberType", gson.toJsonTree(coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getFiberType())));
                jsonObject.addProperty("fiberCore", coLocationApplicationDTO.getFiberCore());

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject viewObjectBuilder(CoLocationApplicationDTO coLocationApplicationDTO, CoLocationConnectionDTO coLocationConnectionDTO, JsonObject jsonObject,LoginDTO loginDTO) throws Exception {

        JsonObject viewObject = new JsonObject();

        Gson gson = new Gson();

        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", coLocationApplicationDTO.getApplicationID());
        viewObject.addProperty("Application Type", CoLocationConstants.applicationTypeNameMap.get(coLocationApplicationDTO.getApplicationType()));

        //if connection exists
        if(coLocationApplicationDTO.getConnectionId()!=0){
            String connectionName = coLocationConnectionService.getAnyColocationConnection(coLocationApplicationDTO.getConnectionId()).getName();
            viewObject.addProperty("Connection ID", coLocationApplicationDTO.getConnectionId());

            viewObject.add("Connection Name", gson.toJsonTree(connectionName));
        }

        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().getClientByClientID(coLocationApplicationDTO.getClientID()).getLoginName()));
        //bandwidth

        viewObject.addProperty("Submission Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(coLocationApplicationDTO.getSubmissionDate(), "dd-MMM-yyyy")  );
        viewObject.addProperty("Suggested Date",TimeConverter.getDateTimeStringByMillisecAndDateFormat( coLocationApplicationDTO.getSuggestedDate(), "dd-MMM-yyyy")  );
//        if(coLocationApplicationDTO.getConnectionType())
        if(gson.toJsonTree(CoLocationConstants.connectionTypeNameMap.get(coLocationApplicationDTO.getConnectionType()))!=null)
        viewObject.add("Connection Type", gson.toJsonTree(CoLocationConstants.connectionTypeNameMap.get(coLocationApplicationDTO.getConnectionType())));

        if (coLocationApplicationDTO.getDemandNoteID() != null) {
            viewObject.addProperty("Demand Note ID", coLocationApplicationDTO.getDemandNoteID());
        }





        //todo: for existing connection

//        if (coLocationApplicationDTO.getConnectionId() > 0 && coLocationApplicationDTO.getID() > 0) {
//            viewObject.addProperty("Connection Name", lliConnection.getName());
//            viewObject.addProperty("Existing Bandwidth", lliConnection.getBandwidth()+" " + " MB");
//        }

//
//        if(coLocationApplicationDTO.getConnectionType()!=0){
//
//            viewObject.addProperty("Connection Type",LLIConnectionConstants.connectionTypeMap.get(coLocationApplicationDTO.getConnectionType()));
//        }

//
//
//        if(loginDTO.getUserID()>0){
//            UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
//            if(userDTO.isBTCLPersonnel()){
//                viewObject.addProperty("Demand Note Skip",coLocationApplicationDTO.getSkipPayment()==1?"Yes":"No");
//            }
//        }else{
//            viewObject.addProperty("Demand Note Skip",coLocationApplicationDTO.getSkipPayment()==1?"Yes":"No");
//        }


        if(coLocationApplicationDTO.isRackNeeded()){
            viewObject.addProperty("Rack Needed", coLocationApplicationDTO.isRackNeeded()==true?"Yes":"No");
            viewObject.addProperty("Rack Size", coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getRackTypeID()).getValue());
            viewObject.addProperty("Rack Space",coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getRackSpace()).getDescription());

        }else{
            viewObject.addProperty("Rack Needed", coLocationApplicationDTO.isRackNeeded()==true?"Yes":"No");
        }

        if(coLocationApplicationDTO.isPowerNeeded()){
            viewObject.addProperty("Power Needed", coLocationApplicationDTO.isPowerNeeded()==true?"Yes":"No");
            viewObject.addProperty("Power Type",coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getPowerType()).getValue());
            viewObject.addProperty("Power Amount",
                        coLocationApplicationDTO.getPowerAmount()
//                    coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getPowerAmount()).getValue()
            );

        }else{
            viewObject.addProperty("Power Needed", coLocationApplicationDTO.isPowerNeeded()==true?"Yes":"No");

        }
        if(coLocationApplicationDTO.isFiberNeeded()){
            viewObject.addProperty("Fiber Needed", coLocationApplicationDTO.isFiberNeeded()==true?"Yes":"No");
//            viewObject.addProperty("Fiber Type", coLocationApplicationDTO.getFiberType()==1?"Single" : "Dual");
            viewObject.addProperty("Fiber Type",  coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getFiberType()).getValue());
            viewObject.addProperty("Fiber Core",coLocationApplicationDTO.getFiberCore());

        }else{
            viewObject.addProperty("Fiber Needed", coLocationApplicationDTO.isFiberNeeded()==true?"Yes":"No");

        }

        // if floor space needed
        if(coLocationApplicationDTO.isFloorSpaceNeeded()){
            viewObject.addProperty("Floor Space Needed", coLocationApplicationDTO.isFloorSpaceNeeded()==true?"Yes":"No");
            viewObject.addProperty("Floor Space Type",coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getFloorSpaceType()).getValue());
            viewObject.addProperty("Floor Space Amount(ft)",
                    coLocationApplicationDTO.getFloorSpaceAmount()
//                    coLocationInventoryTemplateService.getInventoryTemplateByID((long)coLocationApplicationDTO.getPowerAmount()).getValue()
            );

        }else{
            viewObject.addProperty("Floor Space Needed", coLocationApplicationDTO.isFloorSpaceNeeded()==true?"Yes":"No");

        }

        if(coLocationApplicationDTO.getDescription()!=null && !coLocationApplicationDTO.getDescription().isEmpty()) {
            viewObject.addProperty("Description", coLocationApplicationDTO.getDescription());
        }
        //todo: commect package methun will do it
//       viewObject = commentBuilder(coLocationApplicationDTO, viewObject);
        return viewObject;


    }

    public JsonObject commentBuilder(LLIApplication lliApplication, JsonObject viewObject) {
        try {
            List<Comments> Comment = commentsService.getCommentsByState(lliApplication.getState(), lliApplication.getApplicationID());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            common.ClientDTO clientDTO = new common.ClientDTO();
            if (Comment.size() > 0) {
                if (Comment.get(0).getUserID() != -1) {

                    userDTO = UserRepository.getInstance().getUserDTOByUserID(Comment.get(0).getUserID());
                    if (userDTO.getUsername() != null) {

                        commentPersonName = userDTO.getDesignation().equals("") ? userDTO.getUsername() : userDTO.getDesignation();
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

    public JsonArray ifrArrayBuilder(CoLocationApplicationDTO coLocationApplicationDTO,  LoginDTO loginDTO) {


        //for ifr need to filter with state and user role

        Gson gson = new Gson();


        List<CoLocationApplicationIFRDTO> lists = new ArrayList<>();
        try {

            lists = coLocationApplicationIFRService.getIFRByAppIDAndRole(coLocationApplicationDTO.getApplicationID(),loginDTO.getRoleID());
            if(lists.size()==0){
                lists=coLocationApplicationIFRService.getIFRByAppID(coLocationApplicationDTO.getApplicationID());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonArray ifrArray = new JsonArray();
        for (CoLocationApplicationIFRDTO ifr : lists) {

            // todo : check if the pop has local loop already

            JsonElement jsonElement = gson.toJsonTree(ifr);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            ifrArray.add(jsonObject1);

        }

        return ifrArray;


    }






}
