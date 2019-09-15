package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LLIOfficeSerizalizer implements JsonSerializer<LLIOffice>{

	@Override
	public JsonElement serialize(LLIOffice lliOffice, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("historyID", lliOffice.getHistoryID());
		
		jsonObject.addProperty("ID", lliOffice.getID());
		jsonObject.addProperty("name", lliOffice.getName());
		jsonObject.addProperty("address", lliOffice.getAddress());
		
		jsonObject.add("localLoopList", context.serialize(lliOffice.getLocalLoops()));
		
		return jsonObject;
	}

}
