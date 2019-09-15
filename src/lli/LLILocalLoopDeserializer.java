package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LLILocalLoopDeserializer implements JsonDeserializer<LLILocalLoop>{

	@Override
	public LLILocalLoop deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)
			throws JsonParseException {
		LLILocalLoop lliLocalLoop = new LLILocalLoop();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliLocalLoop.setID(jsonObject.get("ID").getAsLong());
		
		lliLocalLoop.setClientDistance(jsonObject.get("clientDistance").getAsInt());
		lliLocalLoop.setBtclDistance(jsonObject.get("btclDistance").getAsInt());
		lliLocalLoop.setOCDistance(jsonObject.get("OCDistance").getAsInt());
		lliLocalLoop.setOCID( ( jsonObject.get("OC")==null ||   jsonObject.get("OC").isJsonNull() ) ? null: jsonObject.get("OC").getAsJsonObject().get("ID").getAsLong());
		lliLocalLoop.setPopID(jsonObject.get("popID")!= null ? jsonObject.get("popID").getAsLong() : null);
		lliLocalLoop.setRouter_switchID(jsonObject.get("router_switchID")!= null ? jsonObject.get("router_switchID").getAsLong() : null);
		lliLocalLoop.setPortID(jsonObject.get("portID")!= null ? jsonObject.get("portID").getAsLong() : null);
		lliLocalLoop.setVlanID(jsonObject.get("vlanID")!= null ? jsonObject.get("vlanID").getAsLong() : null);
		lliLocalLoop.setOfcType(jsonObject.get("ofcType") != null ? jsonObject.get("ofcType").getAsJsonObject().get("ID").getAsInt() : 0);
		
		return lliLocalLoop;
	}

}
