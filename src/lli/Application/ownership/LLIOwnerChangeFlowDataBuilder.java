package lli.Application.ownership;

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
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LLIOwnerChangeFlowDataBuilder {

    BillService billService=ServiceDAOFactory.getService(BillService.class);
    public JsonObject getCommonPart_new(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication,
                                        JsonObject jsonObject, LoginDTO loginDTO) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        try {
            jsonObject = basicDataBuilder(jsonObject, lliOwnerShipChangeApplication,loginDTO);
            List<FlowState> flowStates=ServiceDAOFactory.getService(LLIOwnerChangeService.class).getActionList(
                                                lliOwnerShipChangeApplication.getState(),
                                                (int) loginDTO.getRoleID());
            JsonArray actionArray =new JsonArray();
            for (FlowState flowState : flowStates) {
                JsonElement jsonElement = gson.toJsonTree(flowState);
                actionArray.add(jsonElement);
            }
            JsonObject viewObject = viewObjectBuilder(lliOwnerShipChangeApplication, jsonObject,loginDTO);
            JsonArray connectionArray = connectionObjectBuilder(lliOwnerShipChangeApplication);
            jsonArray = UILabelBuilder(viewObject);
            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", actionArray);
            jsonObject.add("connections", connectionArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JsonArray connectionObjectBuilder(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication) throws Exception{
        JsonArray jsonArray = new JsonArray();
        List<LLIOnProcessConnection> lliOnProcessConnections = ServiceDAOFactory.getService(LLIOnProcessConnectionService.class).
                getConnectionByAppId(lliOwnerShipChangeApplication.getId());
        for(LLIOnProcessConnection lliOnProcessConnection:lliOnProcessConnections){
            LLIConnection lliConnectionInstance = ServiceDAOFactory.getService(LLIFlowConnectionService.class).
                        getConnectionByID(lliOnProcessConnection.getConnection());
            JsonObject object = new JsonObject();
            object.addProperty("name",lliConnectionInstance.getName());
            object.addProperty("type",lliConnectionInstance.getConnectionType());
            object.addProperty("bandwidth",lliConnectionInstance.getBandwidth());
            jsonArray.add(object);
        }
        return jsonArray;
    }

    public JsonObject viewObjectBuilder(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication,
                                        JsonObject jsonObject,
                                        LoginDTO loginDTO) throws Exception {

        JsonObject viewObject = new JsonObject();

        Gson gson = new Gson();

        viewObject.add("Status", jsonObject.get("status"));
        viewObject.addProperty("Application ID", lliOwnerShipChangeApplication.getId());
        viewObject.addProperty("Application Type", LLIConnectionConstants.applicationTypeNameMap.
                                get(lliOwnerShipChangeApplication.getType()));


        viewObject.add("Source Client Name", gson.toJsonTree(AllClientRepository.getInstance().
                getClientByClientID(lliOwnerShipChangeApplication.getSrcClient()).getLoginName()));
        viewObject.add("Destination Client Name", gson.toJsonTree(AllClientRepository.getInstance().
                getClientByClientID(lliOwnerShipChangeApplication.getDstClient()).getLoginName()));
        //bandwidth

        viewObject.addProperty("Submission Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(
                lliOwnerShipChangeApplication.getSubmissionDate(), "dd-MMM-yyyy")  );
        viewObject.addProperty("Suggested Date",TimeConverter.getDateTimeStringByMillisecAndDateFormat(
                lliOwnerShipChangeApplication.getSuggestedDate(), "dd-MMM-yyyy")  );
        if (lliOwnerShipChangeApplication.getDemandNote() != null) {
            viewObject.addProperty("Demand Note ID", lliOwnerShipChangeApplication.getDemandNote());
        }

        if(lliOwnerShipChangeApplication.getDescription()!=null && !lliOwnerShipChangeApplication.getDescription().isEmpty()) {
            viewObject.addProperty("Description", lliOwnerShipChangeApplication.getDescription());
        }
        //todo: commect package methun will do it
//       viewObject = commentBuilder(coLocationApplicationDTO, viewObject);
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

    public JsonObject basicDataBuilder(JsonObject jsonObject,
                                       LLIOwnerShipChangeApplication lliOwnerShipChangeApplication,
                                       LoginDTO loginDTO) {

        try {
            Gson gson = new Gson();
            jsonObject.addProperty("suggestedDate", lliOwnerShipChangeApplication.getSuggestedDate());
            jsonObject.addProperty("applicationID", lliOwnerShipChangeApplication.getId());
            KeyValuePair<Long, String> clientKeyValuePair = new KeyValuePair<>(lliOwnerShipChangeApplication.getSrcClient(),
                    AllClientRepository.getInstance().getClientByClientID(lliOwnerShipChangeApplication.getSrcClient()).
                            getLoginName());

            KeyValuePair<Long, String> clientKeyValuePairDst = new KeyValuePair<>(lliOwnerShipChangeApplication.getDstClient(),
                    AllClientRepository.getInstance().getClientByClientID(lliOwnerShipChangeApplication.getDstClient()).
                            getLoginName());

            jsonObject.add("client", gson.toJsonTree(clientKeyValuePair));
            jsonObject.add("clientDst", gson.toJsonTree(clientKeyValuePairDst));
            jsonObject.add("srcClient",
                    gson.toJsonTree(new LLIDropdownPair(clientKeyValuePair.getKey(), clientKeyValuePair.getValue())));
            jsonObject.add("dstClient",
                    gson.toJsonTree(new LLIDropdownPair(clientKeyValuePairDst.getKey(), clientKeyValuePairDst.getValue())));

//            jsonObject.addProperty("dstClient", lliOwnerShipChangeApplication.getDstClient());
            jsonObject.addProperty("submissionDate", lliOwnerShipChangeApplication.getSubmissionDate());
            jsonObject.addProperty("status", new FlowService().getStateById(lliOwnerShipChangeApplication.getState())
                    .getViewDescription());
            if(loginDTO.getUserID()>0){
                UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                if(lliOwnerShipChangeApplication.getDemandNote()!=null&&userDTO.isBTCLPersonnel()){
                    jsonObject.addProperty("demandNoteID", lliOwnerShipChangeApplication.getDemandNote());
                    BillDTO billDTO=billService.getBillByBillID(lliOwnerShipChangeApplication.getDemandNote());
                    jsonObject.add("bill",new Gson().toJsonTree(billDTO));
                }
            }else{
                jsonObject.addProperty("demandNoteID", lliOwnerShipChangeApplication.getDemandNote());
            }
            jsonObject.addProperty("skipPay",lliOwnerShipChangeApplication.getSkipPayment());
            jsonObject.add("applicationType", gson.toJsonTree(new LLIDropdownPair(lliOwnerShipChangeApplication.getType(),
                    LLIConnectionConstants.applicationTypeNameMap.get(lliOwnerShipChangeApplication.getType()))));
            if(lliOwnerShipChangeApplication.getDescription()!=null && !lliOwnerShipChangeApplication.getDescription().isEmpty()){
                jsonObject.addProperty("description",lliOwnerShipChangeApplication.getDescription());
            }
            jsonObject.addProperty("comment", lliOwnerShipChangeApplication.getComment());
            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_LLI);
            FlowState flowState=ServiceDAOFactory.getService(LLIOwnerChangeService.class).getCurrentState(lliOwnerShipChangeApplication.getState());

            JsonElement jsonElement = gson.toJsonTree(flowState);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject.add("state",jsonObject1);
            jsonObject.addProperty("color", new FlowService().getStateById(lliOwnerShipChangeApplication
                    .getState()).getColor());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

}
