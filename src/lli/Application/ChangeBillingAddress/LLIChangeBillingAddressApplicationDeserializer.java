package lli.Application.ChangeBillingAddress;

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

public class LLIChangeBillingAddressApplicationDeserializer implements JsonDeserializer<LLIChangeBillingAddressApplication>{

	@Override
	public LLIChangeBillingAddressApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		LLIChangeBillingAddressApplication lliChangeBillingAddressApplication = new LLIChangeBillingAddressApplication();
		
		//Receive JSON
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		validate(jsonObject);
		
		//Deserialize Specific LLI New Connection Application
		lliChangeBillingAddressApplication.setExtendedApplicationID(jsonObject.get("extendedApplicationID") != null ? jsonObject.get("extendedApplicationID").getAsLong() : 0);
		
		lliChangeBillingAddressApplication.setFirstName(jsonObject.get("firstName").getAsString());
		lliChangeBillingAddressApplication.setLastName(jsonObject.get("lastName").getAsString());
		lliChangeBillingAddressApplication.setEmail(jsonObject.get("email").getAsString());
		lliChangeBillingAddressApplication.setMobileNumber(jsonObject.get("mobileNumber").getAsString());
		lliChangeBillingAddressApplication.setTelephoneNumber(jsonObject.get("telephoneNumber").getAsString());
		lliChangeBillingAddressApplication.setFaxNumber(jsonObject.get("faxNumber").getAsString());
		lliChangeBillingAddressApplication.setCity(jsonObject.get("city").getAsString());
		lliChangeBillingAddressApplication.setPostCode(jsonObject.get("postCode").getAsString());
		lliChangeBillingAddressApplication.setAddress(jsonObject.get("address").getAsString());

		lliChangeBillingAddressApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
		
		//Deserialize Common LLI Application
		LLIApplication lliApplication = context.deserialize(jsonElement, LLIApplication.class);
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, lliChangeBillingAddressApplication, LLIApplication.class);
		
		return lliChangeBillingAddressApplication;
	}
	private void validate(JsonObject application) {
		LLIApplicationValidationService lliApplicationValidationService = ServiceDAOFactory.getService(LLIApplicationValidationService.class);
		lliApplicationValidationService.validateExistingClient(application);
		
		lliApplicationValidationService.validateNonEmptyString(application, "firstName", "First Name");
		lliApplicationValidationService.validateNonEmptyString(application, "lastName", "last Name");
		lliApplicationValidationService.validateNonEmptyString(application, "email", "E-Mail");
		lliApplicationValidationService.validateNonEmptyString(application, "mobileNumber", "Mobile Number");
		lliApplicationValidationService.validateNonEmptyString(application, "telephoneNumber", "Telephone Number");
		lliApplicationValidationService.validateNonEmptyString(application, "faxNumber", "Fax Number");
		lliApplicationValidationService.validateNonEmptyString(application, "city", "City");
		lliApplicationValidationService.validateNonEmptyString(application, "postCode", "Post Code");
		lliApplicationValidationService.validateNonEmptyString(application, "address", "Address");
		
		lliApplicationValidationService.validateExistence(application, "suggestedDate", "Suggested Date");
	}


}
