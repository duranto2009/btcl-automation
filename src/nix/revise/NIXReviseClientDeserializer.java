package nix.revise;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import login.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NIXReviseClientDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(NIXReviseClientDeserializer.class);

    public NIXReviseDTO deserialize_custom(JsonElement jsonElements, LoginDTO loginDTO) throws JsonParseException {

        JsonObject jsonObject = jsonElements.getAsJsonObject();
        NIXReviseDTO reviseDTO = new NIXReviseDTO();
        long clientID = jsonObject.get("client") != null ? jsonObject.get("client").getAsJsonObject().get("key").getAsLong() : 0;
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
        return reviseDTO;
    }

    public NIXReviseDTO deserialize_custom_reconnection(JsonElement jsonElement, LoginDTO loginDTO) throws JsonParseException{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        NIXReviseDTO reviseDTO = new NIXReviseDTO();
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
        return reviseDTO;
    }
}
