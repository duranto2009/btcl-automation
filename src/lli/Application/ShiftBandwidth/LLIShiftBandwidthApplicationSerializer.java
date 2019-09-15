package lli.Application.ShiftBandwidth;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIShiftBandwidthApplicationSerializer implements JsonSerializer<LLIShiftBandwidthApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIShiftBandwidthApplication lliShiftBandwidthApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliShiftBandwidthApplication.getExtendedApplicationID());

		try {
			jsonObject.add("sourceConnection", context.serialize(new LLIDropdownPair(lliShiftBandwidthApplication.getConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliShiftBandwidthApplication.getConnectionID()).getName())));
			jsonObject.add("destinationConnection", context.serialize(new LLIDropdownPair(lliShiftBandwidthApplication.getDestinationConnectionID(), lliConnectionService.getLLIConnectionByConnectionID(lliShiftBandwidthApplication.getDestinationConnectionID()).getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonObject.addProperty("bandwidth", lliShiftBandwidthApplication.getBandwidth());
		jsonObject.addProperty("suggestedDate", lliShiftBandwidthApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliShiftBandwidthApplication, jsonObject, context);
		
		return jsonObject;
	}

}
