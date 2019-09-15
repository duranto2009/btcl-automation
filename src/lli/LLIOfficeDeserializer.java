package lli;

import java.lang.reflect.Type;
import java.util.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LLIOfficeDeserializer implements JsonDeserializer<LLIOffice>{

	@Override
	public LLIOffice deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)
			throws JsonParseException {
		LLIOffice lliOffice = new LLIOffice();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliOffice.setID( jsonObject.get("ID").getAsLong());
		lliOffice.setName(jsonObject.get("name").getAsString());
		lliOffice.setAddress(jsonObject.get("address").getAsString());
		
		
		List<LLILocalLoop> lliLocalLoopList = new ArrayList<>();
		
		for(JsonElement jsonOfficeElement: jsonObject.get("localLoopList").getAsJsonArray()){
			LLILocalLoop lliLocalLoop = context.deserialize(jsonOfficeElement, LLILocalLoop.class);
			lliLocalLoopList.add(lliLocalLoop);
		}
		
		lliOffice.setLocalLoops(lliLocalLoopList);
		return lliOffice;
	}

}
