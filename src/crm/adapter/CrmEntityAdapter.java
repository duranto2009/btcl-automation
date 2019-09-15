package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import common.EntityDTO;
import crm.CrmEmployeeDTO;

public class CrmEntityAdapter implements JsonSerializer<EntityDTO> {
	@Override
	public JsonElement serialize(EntityDTO entityDTO, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", entityDTO.getName());
		jsonObject.addProperty("entityID", entityDTO.getEntityID());

		return jsonObject;
	}
}
