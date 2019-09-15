package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LLIConnectionApplicationSerializer implements JsonSerializer<LLIApplicationType>{

	@Override
	public JsonElement serialize(LLIApplicationType lliConnectionApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("ID", lliConnectionApplication.getID());
		jsonObject.addProperty("name", lliConnectionApplication.getName());
		jsonObject.addProperty("fields", lliConnectionApplication.getFields());
		
		return jsonObject;
	}

}
