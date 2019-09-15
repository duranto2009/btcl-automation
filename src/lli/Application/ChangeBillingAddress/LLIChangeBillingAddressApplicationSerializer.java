package lli.Application.ChangeBillingAddress;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import lli.Application.LLIApplicationSerializer;

public class LLIChangeBillingAddressApplicationSerializer implements JsonSerializer<LLIChangeBillingAddressApplication>{

	@Override
	public JsonElement serialize(LLIChangeBillingAddressApplication lliChangeBillingAddressApplication, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();

		//Serialize Specific LLI New Connection Application
		jsonObject.addProperty("extendedApplicationID", lliChangeBillingAddressApplication.getExtendedApplicationID());
		
		jsonObject.addProperty("firstName", lliChangeBillingAddressApplication.getFirstName());
		jsonObject.addProperty("lastName", lliChangeBillingAddressApplication.getLastName());
		jsonObject.addProperty("email", lliChangeBillingAddressApplication.getEmail());
		jsonObject.addProperty("mobileNumber", lliChangeBillingAddressApplication.getMobileNumber());
		jsonObject.addProperty("telephoneNumber", lliChangeBillingAddressApplication.getTelephoneNumber());
		jsonObject.addProperty("faxNumber", lliChangeBillingAddressApplication.getFaxNumber());
		jsonObject.addProperty("city", lliChangeBillingAddressApplication.getCity());
		jsonObject.addProperty("postCode", lliChangeBillingAddressApplication.getPostCode());
		jsonObject.addProperty("address", lliChangeBillingAddressApplication.getAddress());

		jsonObject.addProperty("suggestedDate", lliChangeBillingAddressApplication.getSuggestedDate());
		
		jsonObject = LLIApplicationSerializer.getCommonPart(lliChangeBillingAddressApplication, jsonObject, context);
		
		return jsonObject;
	}

}
