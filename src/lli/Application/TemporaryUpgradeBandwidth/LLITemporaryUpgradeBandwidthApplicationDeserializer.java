package lli.Application.TemporaryUpgradeBandwidth;

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

public class LLITemporaryUpgradeBandwidthApplicationDeserializer implements JsonDeserializer<LLITemporaryUpgradeBandwidthApplication>{

	@Override
	public LLITemporaryUpgradeBandwidthApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLITemporaryUpgradeBandwidthApplication lliTemporaryUpgradeBandwidthApplication = new LLITemporaryUpgradeBandwidthApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliTemporaryUpgradeBandwidthApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliTemporaryUpgradeBandwidthApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliTemporaryUpgradeBandwidthApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		lliTemporaryUpgradeBandwidthApplication.setDuration(jsonObject.get("duration").getAsInt());
		
		lliTemporaryUpgradeBandwidthApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliTemporaryUpgradeBandwidthApplication, LLIApplication.class);
		
		return lliTemporaryUpgradeBandwidthApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validatePositiveNumber(application, "duration", "duration");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}



}
