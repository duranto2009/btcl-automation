package lli.Application.AdditionalLocalLoop;

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

public class LLIAdditionalLocalLoopApplicationSerializer implements JsonSerializer<LLIAdditionalLocalLoopApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIAdditionalLocalLoopApplication lliAdditionalLocalLoopApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliAdditionalLocalLoopApplication.getExtendedApplicationID());

		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliAdditionalLocalLoopApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliAdditionalLocalLoopApplication.getOfficeID(),"");
				
				//jsonObject.add("office", context.serialize(new LLIDropdownPair(lliAdditionalLocalLoopApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.add("pop", context.serialize(new LLIDropdownPair(lliAdditionalLocalLoopApplication.getPopID(), "DUMMYDUMMYDUMMY")));
		
		jsonObject.addProperty("suggestedDate", lliAdditionalLocalLoopApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliAdditionalLocalLoopApplication, jsonObject, context);
		
		return jsonObject;
	}

}
