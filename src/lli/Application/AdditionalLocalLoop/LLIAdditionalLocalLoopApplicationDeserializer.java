package lli.Application.AdditionalLocalLoop;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import lli.Application.LLIApplication;
import lli.Application.LLIApplicationDeserializer;
import lli.Application.LLIApplicationValidationService;
import lli.Application.Office.Office;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
@Deprecated
public class LLIAdditionalLocalLoopApplicationDeserializer implements JsonDeserializer<LLIAdditionalLocalLoopApplication>{

	@Override
	public LLIAdditionalLocalLoopApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIAdditionalLocalLoopApplication lliAdditionalLocalLoopApplication = new LLIAdditionalLocalLoopApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliAdditionalLocalLoopApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		lliAdditionalLocalLoopApplication.setConnectionID(jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong());
		lliAdditionalLocalLoopApplication.setPopID(jsonObject.get("pop")!=null ? jsonObject.get("pop").getAsJsonObject().get("ID").getAsLong() : 0L);
		
		lliAdditionalLocalLoopApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        JsonArray jsonArray = (JsonArray) jsonObject.get("addedOfficeList");
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

        JsonArray jsonArray2 = (JsonArray) jsonObject.get("selectedOfficeList");
        for (JsonElement jsonElement1 : jsonArray2
                ) {
            JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
            Office office = new Office();
            office.setOfficeName(officeJsonObject.get("value").getAsString());
            office.setOfficeAddress(officeJsonObject.get("key").getAsString());
//			office.set
            officeList.add(office);
        }

        lliAdditionalLocalLoopApplication.setOfficeList(officeList);
        //Deserialize Common LLI Application
		//LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
        LLIApplication lliApplication = null;
        try {
            lliApplication = new LLIApplicationDeserializer().deserialize_local_loop(jsonElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliAdditionalLocalLoopApplication, LLIApplication.class);
		
		return lliAdditionalLocalLoopApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		lliApplicationValidationService.validateMandatoryDropdown(application, "connection", "Connection");
		//lliApplicationValidationService.validateMandatoryDropdown(application, "office", "Office");
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
