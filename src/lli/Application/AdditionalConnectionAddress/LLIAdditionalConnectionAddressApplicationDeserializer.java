package lli.Application.AdditionalConnectionAddress;

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

public class LLIAdditionalConnectionAddressApplicationDeserializer implements JsonDeserializer<LLIAdditionalConnectionAddressApplication>{

	@Override
	public LLIAdditionalConnectionAddressApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIAdditionalConnectionAddressApplication lliAdditionalConnectionAddressApplication = new LLIAdditionalConnectionAddressApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliAdditionalConnectionAddressApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliAdditionalConnectionAddressApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliAdditionalConnectionAddressApplication.setAddress(jsonObject.get("address").getAsString());
		lliAdditionalConnectionAddressApplication.setLoopProvider(jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt());
		
		lliAdditionalConnectionAddressApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliAdditionalConnectionAddressApplication, LLIApplication.class);
		
		return lliAdditionalConnectionAddressApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateExistence(application, "address", "Address");
		lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
