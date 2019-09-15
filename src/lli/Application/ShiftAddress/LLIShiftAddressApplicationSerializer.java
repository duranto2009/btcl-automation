package lli.Application.ShiftAddress;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.LLIOffice;
import lli.Application.LLIApplicationSerializer;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;

public class LLIShiftAddressApplicationSerializer implements JsonSerializer<LLIShiftAddressApplication>{

	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
	
	@Override
	public JsonElement serialize(LLIShiftAddressApplication lliShiftAddressApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliShiftAddressApplication.getExtendedApplicationID());
		
		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliShiftAddressApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliShiftAddressApplication.getOfficeID(),"");
				
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliShiftAddressApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("address", lliShiftAddressApplication.getAddress());
		jsonObject.add("loopProvider", context.serialize( new LLIDropdownPair(lliShiftAddressApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliShiftAddressApplication.getLoopProvider()) ) ));
		
		jsonObject.addProperty("suggestedDate", lliShiftAddressApplication.getSuggestedDate());
		
		//Serialize Common LLI Application
		jsonObject = LLIApplicationSerializer.getCommonPart(lliShiftAddressApplication, jsonObject, context);
		
		return jsonObject;
	}

}
