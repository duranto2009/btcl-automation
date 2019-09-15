package lli.Application.NewLongTerm;

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

public class LLINewLongTermApplicationDeserializer implements JsonDeserializer<LLINewLongTermApplication>{

	@Override
	public LLINewLongTermApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLINewLongTermApplication lliNewLongTermApplication = new LLINewLongTermApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliNewLongTermApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliNewLongTermApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
		lliNewLongTermApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		
		lliNewLongTermApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliNewLongTermApplication, LLIApplication.class);
		
		return lliNewLongTermApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Start Date");
	}


}
