package lli.Application.AdditionalPort;

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

public class LLIAdditionalPortApplicationSerializer implements JsonSerializer<LLIAdditionalPortApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIAdditionalPortApplication lliAdditionalPortApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliAdditionalPortApplication.getExtendedApplicationID());

		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliAdditionalPortApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliAdditionalPortApplication.getOfficeID(),"");
				
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliAdditionalPortApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonObject.addProperty("portCount", lliAdditionalPortApplication.getPortCount());
		jsonObject.add("loopProvider", context.serialize( new LLIDropdownPair(lliAdditionalPortApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliAdditionalPortApplication.getLoopProvider()) ) ));
		
		jsonObject.addProperty("suggestedDate", lliAdditionalPortApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliAdditionalPortApplication, jsonObject, context);
		
		return jsonObject;
	}

}
