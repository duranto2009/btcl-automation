package lli.Application.AdditionalConnectionAddress;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;

public class LLIAdditionalConnectionAddressApplicationSerializer implements JsonSerializer<LLIAdditionalConnectionAddressApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIAdditionalConnectionAddressApplication lliAdditionalConnectionAddressApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliAdditionalConnectionAddressApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliAdditionalConnectionAddressApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliAdditionalConnectionAddressApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("address", lliAdditionalConnectionAddressApplication.getAddress());
		jsonObject.add("loopProvider", context.serialize( new LLIDropdownPair(lliAdditionalConnectionAddressApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliAdditionalConnectionAddressApplication.getLoopProvider()) ) ));
		jsonObject.addProperty("suggestedDate", lliAdditionalConnectionAddressApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliAdditionalConnectionAddressApplication, jsonObject, context);
		
		return jsonObject;
	}

}
