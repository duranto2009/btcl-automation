package lli.Application.Office;
import com.google.gson.*;
import common.ValidationService;
import lli.Application.Office.Office;

import java.lang.reflect.Type;

public class OfficeDeserializer implements JsonDeserializer<Office>{

    @Override
    public Office deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Office office = new Office();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        office.setApplicationId(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        office.setHistoryId(jsonObject.get("historyID") != null ? jsonObject.get("historyID").getAsLong() : 0);
        ValidationService.validateNonEmptyString(jsonObject, "officeName", "Office Name");
        office.setOfficeName(jsonObject.get("officeName") != null ? jsonObject.get("officeName").getAsString() : new String(""));
        ValidationService.validateNonEmptyString(jsonObject, "officeAddress", "Office Address");
        office.setOfficeAddress(jsonObject.get("officeAddress") != null ? jsonObject.get("officeAddress").getAsString() : new String(""));

        return office;
    }

    public Office deserialize_custom(JsonElement jsonElement) throws JsonParseException {
        Office office = new Office();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        office.setApplicationId(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        office.setHistoryId(jsonObject.get("historyID") != null ? jsonObject.get("historyID").getAsLong() : 0);
        office.setOfficeAddress(jsonObject.get("address") != null ? jsonObject.get("address").getAsString() : new String(""));
        office.setOfficeName(jsonObject.get("officeName") != null ? jsonObject.get("officeName").getAsString() : new String(""));

        return office;
    }
}