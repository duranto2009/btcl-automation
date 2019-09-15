package lli.Application.ReleaseConnectionAddress;

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
import util.ServiceDAOFactory;

public class LLIReleaseConnectionAddressApplicationSerializer implements JsonSerializer<LLIReleaseConnectionAddressApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIReleaseConnectionAddressApplication lliReleaseConnectionAddressApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliReleaseConnectionAddressApplication.getExtendedApplicationID());

		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliReleaseConnectionAddressApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliReleaseConnectionAddressApplication.getOfficeID(),"");
				
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliReleaseConnectionAddressApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonObject.addProperty("suggestedDate", lliReleaseConnectionAddressApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliReleaseConnectionAddressApplication, jsonObject, context);
		
		return jsonObject;
	}

}
