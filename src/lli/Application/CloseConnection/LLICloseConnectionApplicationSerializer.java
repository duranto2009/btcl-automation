package lli.Application.CloseConnection;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLICloseConnectionApplicationSerializer implements JsonSerializer<LLICloseConnectionApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLICloseConnectionApplication lliCloseConnectionApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliCloseConnectionApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliCloseConnectionApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliCloseConnectionApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonObject.addProperty("suggestedDate", lliCloseConnectionApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliCloseConnectionApplication, jsonObject, context);
		
		return jsonObject;
	}

}
