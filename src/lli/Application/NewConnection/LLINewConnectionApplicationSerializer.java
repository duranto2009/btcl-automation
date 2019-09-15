package lli.Application.NewConnection;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import lli.connection.LLIConnectionConstants;

public class LLINewConnectionApplicationSerializer implements JsonSerializer<LLINewConnectionApplication>{

	@Override
	public JsonElement serialize(LLINewConnectionApplication lliNewConnectionApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliNewConnectionApplication.getExtendedApplicationID());
		jsonObject.addProperty("bandwidth", lliNewConnectionApplication.getBandwidth());
		jsonObject.add("connectionType", context.serialize( new LLIDropdownPair(lliNewConnectionApplication.getConnectionType(), LLIConnectionConstants.connectionTypeMap.get(lliNewConnectionApplication.getConnectionType()) ) ));


		jsonObject.add("loopProvider", context.serialize( new LLIDropdownPair(lliNewConnectionApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliNewConnectionApplication.getLoopProvider()) ) ));
		jsonObject.addProperty("duration", lliNewConnectionApplication.getDuration());
		jsonObject.addProperty("suggestedDate", lliNewConnectionApplication.getSuggestedDate());
		
		//Serialize Common LLI Application
		try {
			jsonObject =new  LLIApplicationSerializer().getCommonPart_new(lliNewConnectionApplication, jsonObject, context );
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

}
