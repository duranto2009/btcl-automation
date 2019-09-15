package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LLIConnectionApplicationDeserializer implements JsonDeserializer<LLIApplicationType>{

	@Override
	public LLIApplicationType deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)	throws JsonParseException {
		LLIApplicationType lliConnectionApplication = new LLIApplicationType();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliConnectionApplication.setName(jsonObject.get("name").getAsString());
		lliConnectionApplication.setFields(jsonObject.get("fields").getAsJsonArray().toString());
		
		return lliConnectionApplication;
	}

}
