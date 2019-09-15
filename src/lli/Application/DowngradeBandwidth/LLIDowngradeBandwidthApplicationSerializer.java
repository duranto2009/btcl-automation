package lli.Application.DowngradeBandwidth;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIDowngradeBandwidthApplicationSerializer implements JsonSerializer<LLIDowngradeBandwidthApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIDowngradeBandwidthApplication lliDowngradeBandwidthApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliDowngradeBandwidthApplication.getExtendedApplicationID());

		try {
			jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliDowngradeBandwidthApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliDowngradeBandwidthApplication.getConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("bandwidth", lliDowngradeBandwidthApplication.getBandwidth());
		
		jsonObject.addProperty("suggestedDate", lliDowngradeBandwidthApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliDowngradeBandwidthApplication, jsonObject, context);
		
		return jsonObject;
	}

}
