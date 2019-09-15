package lli.Application.DowngradeBandwidth;

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

public class LLIDowngradeBandwidthApplicationDeserializer implements JsonDeserializer<LLIDowngradeBandwidthApplication>{

	@Override
	public LLIDowngradeBandwidthApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIDowngradeBandwidthApplication lliDowngradeBandwidthApplication = new LLIDowngradeBandwidthApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliDowngradeBandwidthApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliDowngradeBandwidthApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliDowngradeBandwidthApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		
		lliDowngradeBandwidthApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliDowngradeBandwidthApplication, LLIApplication.class);
		
		return lliDowngradeBandwidthApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
