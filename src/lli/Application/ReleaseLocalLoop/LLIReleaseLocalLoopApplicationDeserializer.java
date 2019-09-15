package lli.Application.ReleaseLocalLoop;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lli.Application.LLIApplication;
import lli.Application.LLIApplicationValidationService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

public class LLIReleaseLocalLoopApplicationDeserializer implements JsonDeserializer<LLIReleaseLocalLoopApplication>{

	@Override
	public LLIReleaseLocalLoopApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIReleaseLocalLoopApplication lliReleaseLocalLoopApplication = new LLIReleaseLocalLoopApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliReleaseLocalLoopApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliReleaseLocalLoopApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliReleaseLocalLoopApplication.setOfficeID(jsonObject.get("office").getAsJsonObject().get("ID").getAsLong());
		lliReleaseLocalLoopApplication.setPopID(jsonObject.get("pop")!= null ? jsonObject.get("pop").getAsJsonObject().get("ID").getAsLong() : 0L);
		lliReleaseLocalLoopApplication.setvLanID(0L);
		
		lliReleaseLocalLoopApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliReleaseLocalLoopApplication, LLIApplication.class);
		
		return lliReleaseLocalLoopApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
