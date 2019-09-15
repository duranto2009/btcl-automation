package lli.Application.ChangeBillingAddress;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.LLIClientService;
import lli.Application.LLIApplication;
import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

@TableName("at_lli_application_change_billing_address")
@ForeignKeyName("parentApplicationID")
public class LLIChangeBillingAddressApplication extends LLIApplication{
	@PrimaryKey
	@ColumnName("extendedApplicationID")
	long extendedApplicationID;
	
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
	
	
	
	
	//
	
	public long getExtendedApplicationID() {
		return extendedApplicationID;
	}
	public void setExtendedApplicationID(long extendedApplicationID) {
		this.extendedApplicationID = extendedApplicationID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getSuggestedDate() {
		return suggestedDate;
	}
	public void setSuggestedDate(long suggestedDate) {
		this.suggestedDate = suggestedDate;
	}
	
	
	
	@Override
	@Transactional
	public void insertApplication() throws Exception {
		this.setApplicationType(LLIConnectionConstants.CHANGE_BILLING_ADDRESS);
		this.setDemandNoteNeeded(false);
		super.insertApplication();
	}
	@Override
	public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
		if(!(lastApplicationSnapshot instanceof LLIChangeBillingAddressApplication)) {
			throw new RequestFailureException("This is not a valid Change Billing Address Application");
		}

		LLIChangeBillingAddressApplication lliChangeBillingAddressApplication = (LLIChangeBillingAddressApplication) lastApplicationSnapshot;
		String content = this.getContent();
		ModifiedSqlGenerator.populateObjectFromOtherObject(lliChangeBillingAddressApplication, this, LLIChangeBillingAddressApplication.class);
		setContent(content);
	}
	@Override
	@Transactional
	public void completeApplication() throws Exception {
		ServiceDAOFactory.getService(LLIClientService.class).changeBillingAddress(this);
		super.completeApplication();
	}

}
