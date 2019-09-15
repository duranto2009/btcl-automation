package lli.Application.ShiftBandwidth;

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

public class LLIShiftBandwidthApplicationDeserializer implements JsonDeserializer<LLIShiftBandwidthApplication>{

	@Override
	public LLIShiftBandwidthApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIShiftBandwidthApplication lliShiftBandwidthApplication = new LLIShiftBandwidthApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliShiftBandwidthApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliShiftBandwidthApplication.setConnectionID(jsonObject.get("sourceConnection").getAsJsonObject().get("ID").getAsLong());
		lliShiftBandwidthApplication.setDestinationConnectionID(jsonObject.get("destinationConnection").getAsJsonObject().get("ID").getAsLong());
		lliShiftBandwidthApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		lliShiftBandwidthApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliShiftBandwidthApplication, LLIApplication.class);
		
		return lliShiftBandwidthApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "sourceConnection", "Source Connection");
		lliApplicationValidationService.validateMandatoryDropdown(application, "destinationConnection", "Destination Connection");
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}



}
