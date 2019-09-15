package lli.Application.NewConnection;

import com.google.gson.*;
import common.ValidationService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationDeserializer;
import lli.Application.LLIApplicationValidationService;
import lli.Application.Office.Office;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LLINewConnectionApplicationDeserializer implements JsonDeserializer<LLINewConnectionApplication>{

	@Override
	public LLINewConnectionApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLINewConnectionApplication lliNewConnectionApplication = new LLINewConnectionApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliNewConnectionApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		lliNewConnectionApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
		lliNewConnectionApplication.setConnectionType(jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt());
		lliNewConnectionApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
		lliNewConnectionApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
		lliNewConnectionApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

		JsonArray jsonArray = (JsonArray) jsonObject.get("officeList");
		List<Office> officeList = new ArrayList<>();
		for (JsonElement jsonElement1 : jsonArray
				) {
			JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
			Office office = new Office();
			office.setOfficeName(officeJsonObject.get("officeName").getAsString());
			office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
//			office.set
			officeList.add(office);
		}
		lliNewConnectionApplication.setOfficeList(officeList);

		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliNewConnectionApplication, LLIApplication.class);

		return lliNewConnectionApplication;
	}


	public LLINewConnectionApplication deserialize_custom(JsonElement jsonElement) throws Exception {
		LLINewConnectionApplication lliNewConnectionApplication = new LLINewConnectionApplication();

		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		validate(jsonObject);

		//Deserialize Specific LLI New Connection Application
		lliNewConnectionApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);

		lliNewConnectionApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());

		lliNewConnectionApplication.setConnectionType(jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt());
		lliNewConnectionApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
		lliNewConnectionApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);

		lliNewConnectionApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

//		officeList parse
		JsonArray jsonArray = (JsonArray) jsonObject.get("officeList");
		List<Office> officeList = new ArrayList<>();
		if (jsonArray != null) {
			for (JsonElement jsonElement1 : jsonArray) {
				JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
				Office office = new Office();
				office.setOfficeName(officeJsonObject.get("officeName").getAsString());
				office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
				office.setId(officeJsonObject.get("id").getAsLong());
//			office.set
				officeList.add(office);
			}
			lliNewConnectionApplication.setOfficeList(officeList);
		}


		//Deserialize Common LLI Application
		LLIApplication lliApplication = new LLIApplicationDeserializer().deserialize_custom(jsonElement);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliNewConnectionApplication, LLIApplication.class);

		return lliNewConnectionApplication;
	}



	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");
		lliApplicationValidationService.validateConnectionType(application);
//		lliApplicationValidationService.validateNonEmptyString(application, "officeAddress", "Office Address");
		lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
		lliApplicationValidationService.validateMandatoryDropdownZone(application, "zone", "Zone");
		if(application.get("connectionType").getAsJsonObject().get("ID").getAsInt() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY) {
			lliApplicationValidationService.validatePositiveNumber(application, "duration", "Duration");
		}
		lliApplicationValidationService.validatePositiveNumber(application, "suggestedDate", "Suggested Date");

        for (JsonElement jsonOfficeElement : application.get("officeList").getAsJsonArray()) {
            ValidationService.validateNonEmptyString(jsonOfficeElement.getAsJsonObject(), "officeName", "Office Name");
            ValidationService.validateNonEmptyString(jsonOfficeElement.getAsJsonObject(), "officeAddress", "Office Address");

        }


	}

    private String nullCheckString(JsonObject jsonObject, String data) {
        return jsonObject.get(data) != null ? jsonObject.get(data).getAsString() : "";
    }



}
