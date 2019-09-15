package lli.Application.IFR;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class IFRSerializer implements JsonSerializer<IFR> {

    public static JsonObject getCommonPart(IFR ifr, JsonObject jsonObject, JsonSerializationContext context) {
        //Serialize Common LLI Application


        jsonObject.addProperty("id",ifr.getId());
        jsonObject.addProperty("applicationID", ifr.getApplicationID());
        jsonObject.addProperty("parentID",ifr.getParentIFRID());
        jsonObject.addProperty("popID",ifr.getPopID());
        jsonObject.addProperty("requestedBW",ifr.getRequestedBW());
        jsonObject.addProperty("availableBW",ifr.getAvailableBW());
        jsonObject.addProperty("selectedBW",ifr.getSelectedBW());
        jsonObject.addProperty("priority",ifr.getPriority());
        jsonObject.addProperty("isReplied",ifr.getIsReplied());
        jsonObject.addProperty("serverRoomLocation",ifr.getServerRoomLocationID());
        jsonObject.addProperty("submissionDate",ifr.getSubmissionDate());

        return jsonObject;
    }
    @Override
    public JsonElement serialize(IFR ifr, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
