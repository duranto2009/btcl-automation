package lli.Application.AdditionalPort;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lli.Application.LLIApplication;
import lli.Application.LLIApplicationDeserializer;
import lli.Application.LLIApplicationValidationService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

public class LLIAdditionalPortApplicationDeserializer implements JsonDeserializer<LLIAdditionalPortApplication>{

	@Override
	public LLIAdditionalPortApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIAdditionalPortApplication lliAdditionalPortApplication = new LLIAdditionalPortApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliAdditionalPortApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliAdditionalPortApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliAdditionalPortApplication.setOfficeID(jsonObject.get("office").getAsJsonObject().get("ID").getAsLong());
		lliAdditionalPortApplication.setPortCount(jsonObject.get("portCount").getAsInt());
		lliAdditionalPortApplication.setLoopProvider(jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt());
		
		lliAdditionalPortApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
//		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		LLIApplication lliApplication =new LLIApplicationDeserializer().deserialize_custom_port(jsonElement);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliAdditionalPortApplication, LLIApplication.class);
		
		return lliAdditionalPortApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validatePositiveNumber(application, "portCount", "Port Count");
		lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}



}
