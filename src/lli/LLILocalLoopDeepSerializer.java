package lli;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.connection.LLIConnectionConstants;
import user.UserRepository;
import util.ServiceDAOFactory;

public class LLILocalLoopDeepSerializer implements JsonSerializer<LLILocalLoop>{

	InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
	
	@Override
	public JsonElement serialize(LLILocalLoop lliLocalLoop, Type arg1, JsonSerializationContext context){
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("ID", lliLocalLoop.getID());
		
		jsonObject.addProperty("clientDistance", lliLocalLoop.getClientDistance());
		jsonObject.addProperty("btclDistance", lliLocalLoop.getBtclDistance());
		jsonObject.addProperty("OCDistance", lliLocalLoop.getOCDistance());
		
		if(lliLocalLoop.getOCID() != null) {
			jsonObject.add("OC", context.serialize(new LLIDropdownPair(lliLocalLoop.getOCID(), UserRepository.getInstance().getUserDTOByUserID(lliLocalLoop.getOCID()).getUsername() )) );
		}
		jsonObject.add("ofcType", context.serialize(new LLIDropdownPair(lliLocalLoop.getOfcType(), LLIConnectionConstants.ofcTypeMap.get(lliLocalLoop.getOfcType()) )));
		
		Map<Integer, InventoryItem> inventoryItemPathMap = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(lliLocalLoop.getPortID());
		jsonObject.add("district", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_DISTRICT).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_DISTRICT).getName() )) );
		jsonObject.add("upazila", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_UPAZILA).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_UPAZILA).getName() )) );
		jsonObject.add("union", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_UNION).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_UNION).getName() )) );
		jsonObject.add("pop", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_POP).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_POP).getName() )) );
		jsonObject.add("router", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_ROUTER).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_ROUTER).getName() )) );
		jsonObject.add("port", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_PORT).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_PORT).getName() )) );
		
		// vlan thakteo pare na o pare. majhe majhe thakbe, majhe majhe thakbena, jokhon thakbe na tokhon null ashbe.
		Map<Integer, InventoryItem> inventoryFromVLanPathMap = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(lliLocalLoop.getVlanID());
		if(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN) != null) {
			jsonObject.add("vlan", context.serialize(new LLIDropdownPair(inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN).getID(), inventoryItemPathMap.get(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN).getName() )) );
		}
		
		return jsonObject;
	}

}
