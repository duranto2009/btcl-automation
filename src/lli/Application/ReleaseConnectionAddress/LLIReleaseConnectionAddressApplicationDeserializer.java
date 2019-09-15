package lli.Application.ReleaseConnectionAddress;

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

public class LLIReleaseConnectionAddressApplicationDeserializer implements JsonDeserializer<LLIReleaseConnectionAddressApplication>{

	@Override
	public LLIReleaseConnectionAddressApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIReleaseConnectionAddressApplication lliReleaseConnectionAddressApplication = new LLIReleaseConnectionAddressApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliReleaseConnectionAddressApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliReleaseConnectionAddressApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliReleaseConnectionAddressApplication.setOfficeID(jsonObject.get("office").getAsJsonObject().get("ID").getAsLong());
		
		lliReleaseConnectionAddressApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliReleaseConnectionAddressApplication, LLIApplication.class);
		
		return lliReleaseConnectionAddressApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}



}
