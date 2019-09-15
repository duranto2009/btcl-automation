package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import util.ServiceDAOFactory;
import util.TimeConverter;

public class LLIConnectionApplicationInstanceSerializer implements JsonSerializer<LLIApplicationInstance>{

	@Override
	public JsonElement serialize(LLIApplicationInstance lliConnectionApplicationInstance, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("ID", lliConnectionApplicationInstance.getID());
		jsonObject.addProperty("applicationID", lliConnectionApplicationInstance.getApplicationID());
		try {
			jsonObject.addProperty("applicationTypeName", ServiceDAOFactory.getService(LLIConnectionApplicationService.class).getApplicationTypeByID(lliConnectionApplicationInstance.getApplicationID()).getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("clientID", lliConnectionApplicationInstance.getClientID());
		jsonObject.addProperty("applicationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliConnectionApplicationInstance.getApplicationDate(), "dd/MM/yyyy hh:mm a"));
		jsonObject.addProperty("fields", lliConnectionApplicationInstance.getFields());
		
		return jsonObject;
	}

}
