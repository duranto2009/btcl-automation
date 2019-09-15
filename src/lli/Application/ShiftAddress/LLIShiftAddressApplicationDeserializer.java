package lli.Application.ShiftAddress;

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

public class LLIShiftAddressApplicationDeserializer implements JsonDeserializer<LLIShiftAddressApplication>{

	@Override
	public LLIShiftAddressApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIShiftAddressApplication lliShiftAddressApplication = new LLIShiftAddressApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliShiftAddressApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliShiftAddressApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliShiftAddressApplication.setOfficeID(jsonObject.get("office").getAsJsonObject().get("ID").getAsLong());
		lliShiftAddressApplication.setAddress(jsonObject.get("address").getAsString());
		lliShiftAddressApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
		
		lliShiftAddressApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliShiftAddressApplication, LLIApplication.class);
		
		return lliShiftAddressApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validateNonEmptyString(application, "address", "Address");
		lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
		
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
