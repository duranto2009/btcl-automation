package lli.Application.NewLongTerm;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.Application.LLIApplicationSerializer;

public class LLINewLongTermApplicationSerializer implements JsonSerializer<LLINewLongTermApplication>{

	@Override
	public JsonElement serialize(LLINewLongTermApplication lliNewLongTermApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliNewLongTermApplication.getExtendedApplicationID());

		jsonObject.addProperty("bandwidth", lliNewLongTermApplication.getBandwidth());
		jsonObject.addProperty("suggestedDate", lliNewLongTermApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliNewLongTermApplication, jsonObject, context);
		
		return jsonObject;
	}

}
