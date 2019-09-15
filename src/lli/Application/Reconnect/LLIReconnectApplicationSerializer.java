package lli.Application.Reconnect;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIReconnectApplicationSerializer implements JsonSerializer<LLIReconnectApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIReconnectApplication lliReconnectApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliReconnectApplication.getExtendedApplicationID());
		
		jsonObject.addProperty("suggestedDate", lliReconnectApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliReconnectApplication, jsonObject, context);
		
		return jsonObject;
	}

}
