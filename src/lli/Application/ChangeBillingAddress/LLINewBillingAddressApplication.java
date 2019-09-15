package lli.Application.ChangeBillingAddress;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
@TableName("at_lli_new_billing_address_application")
public class LLINewBillingAddressApplication {
    @ColumnName("id")
    @PrimaryKey
    long id;
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("clientID")
    long clientID;
    @ColumnName("firstName")
    String firstName;
    @ColumnName("lastName")
    String lastName;
    @ColumnName("email")
    String email;
    @ColumnName("mobileNumber")
    String mobileNumber;
    @ColumnName("telephoneNumber")
    String telephoneNumber;
    @ColumnName("faxNumber")
    String faxNumber;
    @ColumnName("city")
    String city;
    @ColumnName("postCode")
    String postCode;
    @ColumnName("address")
    String address;
    @ColumnName("suggestedDate")
    long suggestedDate;


    public LLINewBillingAddressApplication deserialize(JsonElement jelement) {

        JsonObject jsonObject = jelement.getAsJsonObject();
        LLINewBillingAddressApplication lliNewBillingAddressApplication = new LLINewBillingAddressApplication();

        JsonObject cclientDetailsDTO = jsonObject.get("clientDetailsDTO").getAsJsonObject();

        lliNewBillingAddressApplication.setFirstName(cclientDetailsDTO.get("registrantsName").getAsString());
        lliNewBillingAddressApplication.setLastName(cclientDetailsDTO.get("registrantsLastName").getAsString());
        lliNewBillingAddressApplication.setEmail(cclientDetailsDTO.get("email").getAsString());
        lliNewBillingAddressApplication.setMobileNumber(cclientDetailsDTO.get("phoneNumber").getAsString());
        lliNewBillingAddressApplication.setTelephoneNumber(cclientDetailsDTO.get("landlineNumber").getAsString());
        lliNewBillingAddressApplication.setFaxNumber(cclientDetailsDTO.get("faxNumber").getAsString());
        lliNewBillingAddressApplication.setCity(cclientDetailsDTO.get("city").getAsString());
        lliNewBillingAddressApplication.setPostCode(cclientDetailsDTO.get("postCode").getAsString());
        lliNewBillingAddressApplication.setAddress(cclientDetailsDTO.get("address").getAsString());
        lliNewBillingAddressApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliNewBillingAddressApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        return lliNewBillingAddressApplication;

    }
}

