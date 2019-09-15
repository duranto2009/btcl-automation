package lli.Application.upgradeBandwidth;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.Application.LLIApplicationSerializer;
import util.ServiceDAOFactory;

public class LLIUpgradeBandwidthApplicationSerializer implements JsonSerializer<LLIUpgradeBandwidthApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIUpgradeBandwidthApplication lliUpgradeBandwidthApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliUpgradeBandwidthApplication.getExtendedApplicationID());

		try {
			jsonObject.add(
					"connection",
					context.serialize(
							new LLIDropdownPair(lliUpgradeBandwidthApplication.getConnectionID(),
							lliConnectionService.getLLIConnectionByConnectionID(lliUpgradeBandwidthApplication.getConnectionID()).getName())
					)
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("bandwidth", lliUpgradeBandwidthApplication.getBandwidth());
		
		jsonObject.addProperty("suggestedDate", lliUpgradeBandwidthApplication.getSuggestedDate());


		try {
			jsonObject =new  LLIApplicationSerializer().getCommonPart_new(lliUpgradeBandwidthApplication, jsonObject, context );
		} catch (Exception e) {
			e.printStackTrace();
		}


		return jsonObject;
	}

}
