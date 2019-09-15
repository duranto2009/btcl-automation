package lli.Application.ReleaseIP;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lli.Application.LLIApplication;
import util.ModifiedSqlGenerator;

public class LLIReleaseIPApplicationDeserializer implements JsonDeserializer<LLIReleaseIPApplication>{

	@Override
	public LLIReleaseIPApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIReleaseIPApplication lliReleaseIPApplication = new LLIReleaseIPApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		//Deserialize Specific LLI New Connection Application
		lliReleaseIPApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliReleaseIPApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliReleaseIPApplication.setIpCount(jsonObject.get("ipCount").getAsInt());
		
		lliReleaseIPApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliReleaseIPApplication, LLIApplication.class);
		
		return lliReleaseIPApplication;
	}



}
