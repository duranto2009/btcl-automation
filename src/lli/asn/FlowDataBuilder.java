package lli.asn;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.client.ClientContactDetails;
import common.repository.AllClientRepository;
import entity.comment.Comment;
import entity.comment.CommentService;
import exception.NoDataFoundException;
import flow.FlowService;
import flow.entity.FlowState;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.lang.reflect.Type;
import java.util.*;

public class FlowDataBuilder {

    ASNApplicationService asnApplicationService = ServiceDAOFactory.getService(ASNApplicationService.class);
    CommentService commentsService = ServiceDAOFactory.getService(CommentService.class);


    public JsonObject getCommonPart_new(ASNApplication asnApplication, JsonObject jsonObject, LoginDTO loginDTO) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        try {
            jsonObject = basicDataBuilder(jsonObject, asnApplication, loginDTO);
            JsonObject viewObject = rootViewObjectBuilder(asnApplication, jsonObject, loginDTO);
            jsonArray = UILabelBuilder(viewObject);
            JsonArray asnArray = linkArrayBuilder(asnApplication, asnApplication.getAsns(), loginDTO);
            if (asnArray.size() > 0) {
                for (JsonElement jsonElement : asnArray
                ) {
                    JsonObject asnMapObject = jsonElement.getAsJsonObject();

                }
            }
            jsonObject.add("asn",asnArray);
            jsonObject.add("formElements", jsonArray);
            jsonObject.add("action", commonActionBuilder(asnArray));// todo: global action logic
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JsonObject basicDataBuilder(JsonObject jsonObject, ASNApplication asnApplication, LoginDTO loginDTO) {
        try {
            Gson gson = new Gson();
            jsonObject.addProperty("suggestedDate", asnApplication.getSuggestedDate());
            jsonObject.addProperty("clientId", asnApplication.getClientId());
            jsonObject.addProperty("applicationId", asnApplication.getApplicationId());
            jsonObject.addProperty("asnApplicationId", asnApplication.getAsnAppId());
            jsonObject.addProperty("applicationType", asnApplication.getApplicationType().name());
            jsonObject.addProperty("submissionDate", asnApplication.getSubmissionDate());
            jsonObject.addProperty("status", new FlowService().getStateById(asnApplication.getApplicationState().getState()).getViewDescription());
            jsonObject.addProperty("comment", asnApplication.getComment());

            jsonObject.addProperty("moduleID", ModuleConstants.Module_ID_LLI);
            jsonObject.addProperty("description", asnApplication.getDescription());
            jsonObject.addProperty("state", asnApplication.getApplicationState().getState());
            jsonObject.addProperty("color", new FlowService().getStateById(asnApplication.getApplicationState().getState()).getColor());
            //next states
            FlowState flowState = asnApplicationService.getCurrentState(asnApplication.getApplicationState().getState());
            JsonElement jsonElement = gson.toJsonTree(flowState);
            JsonObject jsonObject1 = (JsonObject) jsonElement;
            jsonObject.add("state", jsonObject1);
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
    private JsonArray linkArrayBuilder(ASNApplication asnApplication, List<ASN> asns, LoginDTO loginDTO) {
        JsonArray linkArray = new JsonArray();
        Gson gson = new Gson();
        for (ASN t : asns) {
            try {
                UserDTO userDTO = null;
                boolean isDistributed = false;
                boolean isDistributionApplicable = true;
                if (loginDTO.getUserID() > 0) {
                    userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
                }
                JsonElement linkElement = gson.toJsonTree(t);
                FlowState currentState = asnApplicationService.getCurrentState(t.getState());

                List<FlowState> flowStates = asnApplicationService.getActionList(t.getState(), (int) loginDTO.getRoleID());
                JsonArray actionArray = new JsonArray();
                for (FlowState flowState : flowStates) {

                    JsonElement jsonElement1 = gson.toJsonTree(flowState);
                    jsonElement1.getAsJsonObject().addProperty("isGlobal", false);
                    actionArray.add(jsonElement1);
                }
                linkElement.getAsJsonObject().add("state", gson.toJsonTree(currentState));
                linkElement.getAsJsonObject().add("action", actionArray);
                linkElement.getAsJsonObject().add("Comments", commentArrayBuilder(asnApplication ,t));
                linkArray.add(linkElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return linkArray;
    }


    public JsonArray commentArrayBuilder(ASNApplication asnApplication, ASN asn) throws Exception {
        JsonArray jsonArray = new JsonArray();
        try {
            List<Comment> comments = commentsService.getCommentByState(asn.getState(), ModuleConstants.Module_ID_LLI, asn.getId());
            for (Comment comment :
                    comments) {
                JsonObject jsonObject = new JsonObject();
                String commentStringBuilder = commentStringBuilder(comment, asnApplication);
                jsonObject.addProperty("comment", commentStringBuilder);
                jsonArray.add(jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }


    private String commentStringBuilder(Comment comment, ASNApplication asnApplication) {
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
            clientDTO = AllClientRepository.getInstance().getClientByClientID(asnApplication.getClientId());
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

    public JsonArray commonActionBuilder(JsonArray asnArray) {
        JsonArray commonActionArray = new JsonArray();
        Gson gson = new Gson();

        Type type = new TypeToken<JsonArray>() {
        }.getType();
        JsonArray temp = gson.fromJson(asnArray, type);

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

        return commonActionArray;
    }

    public JsonObject rootViewObjectBuilder(ASNApplication asnApplication, JsonObject jsonObject, LoginDTO loginDTO) {

        JsonObject viewObject = new JsonObject();

        Gson gson = new Gson();
        viewObject.addProperty("Application ID", asnApplication.getApplicationId());
        viewObject.add("Client Name", gson.toJsonTree(AllClientRepository.getInstance().getClientByClientID(asnApplication.getClientId()).getLoginName()));
        viewObject.add("Application Type", gson.toJsonTree(asnApplication.getApplicationType().getApplicationTypeName()));

        viewObject.addProperty("Submission Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(asnApplication.getSubmissionDate(), "dd-MMM-yyyy"));
        viewObject.addProperty("Suggested Date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(asnApplication.getSuggestedDate(), "dd-MMM-yyyy"));
        if (asnApplication.getUserId() != null
                && asnApplication.getUserId() > 0
        ) {
            try {
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(asnApplication.getUserId());
                viewObject.addProperty("Submitted By", userDTO.getFullName());
            } catch (NoDataFoundException e) {
                //log.fatal(e.getMessage());
            }

        } else {
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(asnApplication.getClientId());
            if (clientDTO != null) {
                viewObject.addProperty("Submitted By", clientDTO.getName());
            }
        }
        return viewObject;
    }

    public JsonObject getASNJson(ASN asn, JsonObject jsonObject, LoginDTO loginDTO) throws Exception{
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        for (ASNmapToIP t : asn.getAsNmapToIPS()) {
            try {
                JsonElement element = gson.toJsonTree(t);
                element.getAsJsonObject().add("ipVersion", gson.toJsonTree(t.getIpVersion()));
                element.getAsJsonObject().add("ip", gson.toJsonTree(t.getIp()));
                jsonArray.add(element);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<ClientContactDetailsDTO> clientContactDetailsDTOS = ServiceDAOFactory.getService(ClientService.class).getVpnContactDetailsListByClientID(asn.getClient());
        ClientContactDetailsDTO clientContactDetailsDTO = clientContactDetailsDTOS.stream().findFirst().orElseThrow(()->new RequestFailureException("No Client Details Found"));
        jsonObject.addProperty("asnNo",asn.getAsnNo());
        jsonObject.addProperty("client",clientContactDetailsDTO.getRegistrantsName()+" "+clientContactDetailsDTO.getRegistrantsLastName());
        jsonObject.add("iPs",jsonArray);
        return jsonObject;
    }
}
