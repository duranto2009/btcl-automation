package lli.Application.AdditionalIP;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIAdditionalIPApplicationSerializer implements JsonSerializer<LLIAdditionalIPApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIAdditionalIPApplication lliAdditionalIPApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliAdditionalIPApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliAdditionalIPApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliAdditionalIPApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("ipCount", lliAdditionalIPApplication.getIpCount());
		
		jsonObject.addProperty("suggestedDate", lliAdditionalIPApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliAdditionalIPApplication, jsonObject, context);
		
		return jsonObject;
	}

}
