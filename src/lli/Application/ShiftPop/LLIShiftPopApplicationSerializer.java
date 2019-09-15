package lli.Application.ShiftPop;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import inventory.InventoryService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.LLIOffice;
import lli.Application.LLIApplicationSerializer;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;

public class LLIShiftPopApplicationSerializer implements JsonSerializer<LLIShiftPopApplication>{

	LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
	
	@Override
	public JsonElement serialize(LLIShiftPopApplication lliShiftPopApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliShiftPopApplication.getExtendedApplicationID());
		try {
			LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(lliShiftPopApplication.getConnectionID());
			if(lliConnectionInstance !=null) {
				jsonObject.add("connection", context.serialize(new LLIDropdownPair(lliConnectionInstance.getID(), lliConnectionInstance.getName())));
				
				String officeName = 
				lliConnectionInstance.getLliOffices()
				.stream()
				.collect(Collectors.toMap(LLIOffice::getID, LLIOffice::getName))
				.getOrDefault(lliShiftPopApplication.getOfficeID(),"");
				
				jsonObject.add("office", context.serialize(new LLIDropdownPair(lliShiftPopApplication.getOfficeID(), officeName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonObject.add("oldpop", context.serialize( new LLIDropdownPair(lliShiftPopApplication.getPopID(),
				ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(lliShiftPopApplication.getPopID()).getName() ) ));
		jsonObject.add("newpop", context.serialize( new LLIDropdownPair(lliShiftPopApplication.getNewPopID(),
				ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(lliShiftPopApplication.getNewPopID()).getName() ) ));
		jsonObject.add("loopProvider", context.serialize( new LLIDropdownPair(lliShiftPopApplication.getLoopProvider(), LLIConnectionConstants.loopProviderMap.get(lliShiftPopApplication.getLoopProvider()) ) ));

		jsonObject.addProperty("suggestedDate", lliShiftPopApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliShiftPopApplication, jsonObject, context);
		
		return jsonObject;
	}

}
