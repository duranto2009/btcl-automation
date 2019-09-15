package lli.Application.ReviseClient;

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
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIConnectionConditionBuilder;
import lli.Comments.CommentsService;
import lli.Comments.RevisedComment;
import lli.LLIDropdownPair;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReviseClientDataBuilder {

    CommentsService commentsService = ServiceDAOFactory.getService(CommentsService.class);
    BillService billService=ServiceDAOFactory.getService(BillService.class);
    GlobalService globalService=ServiceDAOFactory.getService(GlobalService.class);


    public JsonObject getCommonPart_new(ReviseDTO reviseDTO, JsonObject jsonObject, LoginDTO loginDTO)throws Exception {

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

        try {
            jsonObject = basicDataBuilder(jsonObject, reviseDTO,loginDTO);

            JsonObject viewObject = viewObjectBuilder(reviseDTO, jsonObject);
            JsonObject actionObject = new JsonObject();

            List<FlowState> flowStates = new FlowService().getNextStatesFromState((int) reviseDTO.getState());
            for (FlowState flowState : flowStates
                    ) {

                actionObject.addProperty(flowState.getDescription(), flowState.getId());
            }

            //endregion
            jsonArray = UILabelBuilder(viewObject);
            JsonArray actionArray = UILabelBuilder(actionObject);

            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
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

    public JsonObject basicDataBuilder(JsonObject jsonObject, ReviseDTO reviseDTO,LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();


            jsonObject.addProperty("suggestedDate", reviseDTO.getSuggestedDate());

            jsonObject.addProperty("applicationID", reviseDTO.getId());
            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_LLI);


            jsonObject.addProperty("referenceContract", reviseDTO.getReferenceContract());
            double bandwidth = reviseDTO.getBandwidth();
            if (bandwidth > 0) {
                jsonObject.addProperty("bandwidth", bandwidth);
            }

            LLIDropdownPair LLIDropdownPair = new LLIDropdownPair(reviseDTO.getClientID(), AllClientRepository.getInstance().getClientByClientID(reviseDTO.getClientID()).getLoginName());

            jsonObject.add("client", gson.toJsonTree(LLIDropdownPair));

            //todo: take userID if needed

            jsonObject.addProperty("status", new FlowService().getStateById((int) reviseDTO.getState()).getViewDescription());


            if(loginDTO.getUserID()>0){
                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());

                if(reviseDTO.getDemandNoteID()>0&&userDTO.isBTCLPersonnel()){
                    jsonObject.addProperty("demandNoteID", reviseDTO.getDemandNoteID());
                    BillDTO billDTO=billService.getBillByBillID(reviseDTO.getDemandNoteID());
                    jsonObject.add("bill",new Gson().toJsonTree(billDTO));
                }
            }else{

                jsonObject.addProperty("demandNoteID", reviseDTO.getDemandNoteID());

            }

            jsonObject.add("applicationType", gson.toJsonTree(new LLIDropdownPair(reviseDTO.getApplicationType(), LLIConnectionConstants.applicationTypeNameMap.get(reviseDTO.getApplicationType()))));

            jsonObject.addProperty("description", reviseDTO.getDescription());
            jsonObject.addProperty("state", reviseDTO.getState());
            jsonObject.addProperty("color", new FlowService().getStateById((int) reviseDTO.getState()).getColor());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject viewObjectBuilder(ReviseDTO reviseDTO, JsonObject jsonObject) throws Exception{
        JsonObject viewObject = new JsonObject();
        Gson gson = new Gson();

        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", reviseDTO.getId());
        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().getClientByClientID(reviseDTO.getClientID()).getLoginName()));

        double bandwidth = reviseDTO.getBandwidth();
        if (bandwidth > 0) {
            viewObject.addProperty("Bandwidth (Mbps)", bandwidth);
        }
        /*
         * jami break lt*/
        LLILongTermContract lliLongTermContract = ServiceDAOFactory.getService(LLILongTermService.class).getLLILongTermContractByLongTermContractID(reviseDTO.getReferenceContract());
        if(lliLongTermContract!=null){
            String label, startDate, endDate, dateTimeFormat = "dd/MM/yyyy";
            startDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliLongTermContract.getContractStartDate(), dateTimeFormat);
            endDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliLongTermContract.getContractEndDate(), dateTimeFormat);
            label = lliLongTermContract.getBandwidth() + " Mbps (" + startDate + " - " + endDate + ")";
            viewObject.addProperty("Existing Contract",label);
            viewObject.addProperty("Existing Contract Bandwidth (Mbps)",lliLongTermContract.getBandwidth());

        }
        viewObject.add("Application Type", gson.toJsonTree(LLIConnectionConstants.applicationTypeNameMap.get(reviseDTO.getApplicationType())));

        if (reviseDTO.getDemandNoteID() > 0) {
            viewObject.addProperty("Demand Note ID", reviseDTO.getDemandNoteID());
        }

        if (!reviseDTO.getDescription().equals("")) {
            viewObject.addProperty("Description", reviseDTO.getDescription());
        }

        Date date = new Date(reviseDTO.getSuggestedDate());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String Adviseddate = df.format(date);

        viewObject.addProperty("Adviced Date", String.valueOf(date));

        if(reviseDTO.getApplicationType()==LLIConnectionConstants.TD || reviseDTO.getApplicationType()==LLIConnectionConstants.RECONNECT ){

            ArrayList<Integer> conTypeList=new ArrayList<>();
            conTypeList.add(LLIConnectionConstants.CONNECTION_TYPE_REGULAR);
            conTypeList.add(LLIConnectionConstants.CONNECTION_TYPE_CACHE);
            conTypeList.add(LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG);
            List<LLIConnection> lliConnections=globalService.getAllObjectListByCondition(LLIConnection.class
                    ,new LLIConnectionConditionBuilder()
                    .Where()
                    .clientIDEquals(reviseDTO.getClientID())
                    .connectionTypeIn(conTypeList)
                    .activeToGreaterThan(System.currentTimeMillis())
                    .getCondition()


                    );

            if(lliConnections!=null){

                String connectionString="";
                for (LLIConnection lliConnection:lliConnections
                     ) {

                    connectionString+= lliConnection.getName()+"("+lliConnection.getBandwidth()+ " Mbps)"+", ";

                }

                if(reviseDTO.getApplicationType()==LLIConnectionConstants.TD){

                    viewObject.addProperty(" Probable TD Connection List",connectionString);
                }

                else if(reviseDTO.getApplicationType()==LLIConnectionConstants.RECONNECT){

                    viewObject.addProperty(" Probable Reconnect Connection List",connectionString);
                }


            }
        }


        viewObject = commentBuilder(reviseDTO, viewObject);
        return viewObject;
    }

    public JsonObject commentBuilder(ReviseDTO reviseDTO, JsonObject viewObject) {
        try {
            List<RevisedComment> comments = commentsService.getRevisedCommentsByState(reviseDTO.getState(), reviseDTO.getId());
            String commentVal = "";
            String commentPersonName = "";
            UserDTO userDTO = new UserDTO();
            common.ClientDTO clientDTO = new common.ClientDTO();

            if (comments.size() > 0) {
                if (comments.get(0).getUserID() != -1) {
                    userDTO = UserRepository.getInstance().getUserDTOByUserID(comments.get(0).getUserID());
                    if (userDTO.getUsername() != null) {
                        commentPersonName = userDTO.getDesignation().equals("") ? userDTO.getUsername() : userDTO.getDesignation();
                    }
                } else {
                    clientDTO = AllClientRepository.getInstance().getClientByClientID(reviseDTO.getClientID());
                    if (clientDTO.getLoginName() != null) {
                        commentPersonName = clientDTO.getName();
                    }
                }
                commentVal += comments.get(0).getComments().replaceAll("\"", " ");
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
}
