package lli.Application.TemporaryUpgradeBandwidth;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLITemporaryUpgradeBandwidthApplicationSerializer implements JsonSerializer<LLITemporaryUpgradeBandwidthApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLITemporaryUpgradeBandwidthApplication lliTemporaryUpgradeBandwidthApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliTemporaryUpgradeBandwidthApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliTemporaryUpgradeBandwidthApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliTemporaryUpgradeBandwidthApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("bandwidth", lliTemporaryUpgradeBandwidthApplication.getBandwidth());
		jsonObject.addProperty("duration", lliTemporaryUpgradeBandwidthApplication.getDuration());
		
		jsonObject.addProperty("suggestedDate", lliTemporaryUpgradeBandwidthApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliTemporaryUpgradeBandwidthApplication, jsonObject, context);
		
		return jsonObject;
	}

}
