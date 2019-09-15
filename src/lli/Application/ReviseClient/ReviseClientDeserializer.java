package lli.Application.ReviseClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import common.RequestFailureException;
import login.LoginDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviseClientDeserializer {

    private static final Logger logger = LoggerFactory.getLogger(ReviseClientDeserializer.class);

//    public  checkNullInJson(JsonObject jsonObject,String objName,Class<?> varType){
//
//        if(varType==String.class){
//            return jsonObject.get(objName)!=null?jsonObject.get(objName).getAsString():"";
//        }else{
//
//        }
//
//    }

    public ReviseDTO deserialize_custom(JsonElement jsonElements, LoginDTO loginDTO) throws JsonParseException {

        JsonObject jsonObject = jsonElements.getAsJsonObject();
        ReviseDTO reviseDTO = new ReviseDTO();
        long clientID = jsonObject.get("client") != null ? jsonObject.get("client").getAsJsonObject().get("ID").getAsLong() : 0;
        if(clientID==0){
            clientID=jsonObject.get("clientID").getAsLong();
        }
        long advisedDate = jsonObject.get("suggestedDate") != null ? jsonObject.get("suggestedDate").getAsLong() : 0;

        try {
            long referenceContract = jsonObject.get("existingContract")!=null? jsonObject.get("existingContract").getAsJsonObject().get("ID").getAsLong():0;
            if (referenceContract > 0) {
                reviseDTO.setReferenceContract(referenceContract);
            }
        } catch (Exception ex) {
            logger.error(" + Ignorable error : " + getClass().getName() + " : No existing contract for reference");
        }

        reviseDTO.setId(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        reviseDTO.setClientID(clientID);
        reviseDTO.setSuggestedDate(advisedDate);
        reviseDTO.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        reviseDTO.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : 0);
        reviseDTO.setBandwidth(jsonObject.get("bandwidth") != null ? jsonObject.get("bandwidth").getAsDouble() : 0);

        return reviseDTO;
    }
}
