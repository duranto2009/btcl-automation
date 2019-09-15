package lli.Application.ShiftPop;

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

public class LLIShiftPopApplicationDeserializer implements JsonDeserializer<LLIShiftPopApplication>{

	@Override
	public LLIShiftPopApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIShiftPopApplication lliShiftPopApplication = new LLIShiftPopApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliShiftPopApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		lliShiftPopApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());	
		lliShiftPopApplication.setOfficeID(jsonObject.get("office").getAsJsonObject().get("ID").getAsLong());	
		lliShiftPopApplication.setPopID(jsonObject.get("oldpop").getAsJsonObject().get("ID").getAsLong());		
		lliShiftPopApplication.setNewPopID(jsonObject.get("newpop").getAsJsonObject().get("ID").getAsLong());
		lliShiftPopApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));		
		lliShiftPopApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliShiftPopApplication, LLIApplication.class);
		
		return lliShiftPopApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validateMandatoryDropdown(application, "oldpop", "Existing PoP");
		lliApplicationValidationService.validateMandatoryDropdown(application, "newpop", "New PoP");
		lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}



}
