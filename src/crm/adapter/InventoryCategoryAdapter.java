package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import common.bill.BankTransactionHistory;
import crm.inventory.CRMInventoryCatagoryType;
import util.TimeConverter;

public class InventoryCategoryAdapter implements JsonSerializer<CRMInventoryCatagoryType>{

	@Override
	public JsonElement serialize(CRMInventoryCatagoryType inventoryCategoryType, Type arg1, JsonSerializationContext arg2) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("ID", inventoryCategoryType.getID());
		jsonObject.addProperty("parentCatagoryTypeID", inventoryCategoryType.getParentCatagoryTypeID());
		jsonObject.addProperty("name", inventoryCategoryType.getName());
		return jsonObject;
	}
	
}
