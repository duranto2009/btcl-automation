package lli;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LLIConnectionApplicationInstanceDeserializer implements JsonDeserializer<LLIApplicationInstance>{

	@Override
	public LLIApplicationInstance deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context)	throws JsonParseException {
		LLIApplicationInstance lliConnectionApplicationInstance = new LLIApplicationInstance();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		lliConnectionApplicationInstance.setApplicationID(jsonObject.get("applicationID").getAsLong());
		lliConnectionApplicationInstance.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
		lliConnectionApplicationInstance.setFields(jsonObject.get("fields").getAsJsonArray().toString());
		
		return lliConnectionApplicationInstance;
	}

}
