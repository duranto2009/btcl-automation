package lli.Application.ReleaseLocalLoop;

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

public class LLIReleaseLocalLoopApplicationSerializer implements JsonSerializer<LLIReleaseLocalLoopApplication>{
	
	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

	@Override
	public JsonElement serialize(LLIReleaseLocalLoopApplication lliReleaseLocalLoopApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliReleaseLocalLoopApplication.getExtendedApplicationID());

		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliReleaseLocalLoopApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliReleaseLocalLoopApplication.getOfficeID(),"");
				
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliReleaseLocalLoopApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.add("pop", context.serialize(new LLIDropdownPair(lliReleaseLocalLoopApplication.getPopID(), "DUMMYDUMMYDUMMY")));
		jsonObject.addProperty("vLanID", lliReleaseLocalLoopApplication.getvLanID());
		jsonObject.addProperty("suggestedDate", lliReleaseLocalLoopApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliReleaseLocalLoopApplication, jsonObject, context);
		
		return jsonObject;
	}

}
