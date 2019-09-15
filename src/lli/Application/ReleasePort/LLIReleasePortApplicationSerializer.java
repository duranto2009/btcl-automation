package lli.Application.ReleasePort;

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

public class LLIReleasePortApplicationSerializer implements JsonSerializer<LLIReleasePortApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIReleasePortApplication lliReleasePortApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliReleasePortApplication.getExtendedApplicationID());

		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliReleasePortApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
					lliConnectionInstance.getLliOffices()
					.stream()
					.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
					.getOrDefault(lliReleasePortApplication.getOfficeID(),"");
					
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliReleasePortApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.addProperty("portCount", lliReleasePortApplication.getPortCount());
		
		jsonObject.addProperty("suggestedDate", lliReleasePortApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliReleasePortApplication, jsonObject, context);
		
		return jsonObject;
	}

}
