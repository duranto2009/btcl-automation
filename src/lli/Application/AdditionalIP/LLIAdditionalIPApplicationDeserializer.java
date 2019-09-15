package lli.Application.AdditionalIP;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lli.Application.LLIApplication;
import util.ModifiedSqlGenerator;

public class LLIAdditionalIPApplicationDeserializer implements JsonDeserializer<LLIAdditionalIPApplication>{

	@Override
	public LLIAdditionalIPApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIAdditionalIPApplication lliAdditionalIPApplication = new LLIAdditionalIPApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		//Deserialize Specific LLI New Connection Application
		lliAdditionalIPApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliAdditionalIPApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliAdditionalIPApplication.setIpCount(jsonObject.get("ipCount").getAsInt());
		
		lliAdditionalIPApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliAdditionalIPApplication, LLIApplication.class);
		
		return lliAdditionalIPApplication;
	}



}
