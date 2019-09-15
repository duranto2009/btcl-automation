package lli.Application.BreakLongTerm;

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

public class LLIBreakLongTermApplicationDeserializer implements JsonDeserializer<LLIBreakLongTermApplication>{

	@Override
	public LLIBreakLongTermApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIBreakLongTermApplication lliBreakLongTermApplication = new LLIBreakLongTermApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliBreakLongTermApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliBreakLongTermApplication.setContractID(jsonObject.get("contract").getAsJsonObject().get("ID").getAsLong());
		lliBreakLongTermApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		
		lliBreakLongTermApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliBreakLongTermApplication, LLIApplication.class);
		
		return lliBreakLongTermApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "contract", "Contract");
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
