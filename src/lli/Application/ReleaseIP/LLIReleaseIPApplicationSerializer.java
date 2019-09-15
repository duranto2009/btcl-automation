package lli.Application.ReleaseIP;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIReleaseIPApplicationSerializer implements JsonSerializer<LLIReleaseIPApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIReleaseIPApplication lliReleaseIPApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliReleaseIPApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliReleaseIPApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliReleaseIPApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("ipCount", lliReleaseIPApplication.getIpCount());
		
		jsonObject.addProperty("suggestedDate", lliReleaseIPApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliReleaseIPApplication, jsonObject, context);
		
		return jsonObject;
	}
}
