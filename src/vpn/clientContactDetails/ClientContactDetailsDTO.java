package vpn.clientContactDetails;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@TableName("at_client_contact_details")
public class ClientContactDetailsDTO implements Serializable{
	public static final int REGISTRANT_CONTACT = 0;
	public static final int BILLING_CONTACT = 1;
	public static final int ADMIN_CONTACT = 2;
	public static final int TECHNICAL_CONTACT = 3;

	
	public static final int isVerified=1;
	public static final int isNotVerified=0;
	
	@PrimaryKey
	@ColumnName("vclcID")
	long ID;
	
	@ColumnName("vclcVpnClientId")
	long vpnClientID;
	
	@ColumnName("vclcName")
	String registrantsName;
	
	@ColumnName("vclcLastName")
	String registrantsLastName;
	
	@ColumnName("vclcFatherName")
	String fatherName;
	
	@ColumnName("vclcMotherName")
	String motherName;
	
	@ColumnName("vclcGender")
	String gender;
	
	@ColumnName("vclcDateOfBirth")
	String dateOfBirth;
	
	@ColumnName("vclcEmail")
	String email;
	
	@ColumnName("vclcIsEmailVerified")
	int isEmailVerified;
	
	@ColumnName("vclcOrganization")
	String organization;
	
	@ColumnName("vclcOccupation")
	String occupation;

	@ColumnName("vclcWebAddress")
	String webAddress;

	@ColumnName("vclcFaxNumber")
	String faxNumber;
	
	@ColumnName("vclcPhoneNumber")
	String phoneNumber;

	@ColumnName("vclcIsPhoneNumberVerified")
	int isPhoneNumberVerified;

	@ColumnName("vclcLandlineNumber")
	String landlineNumber;
	
	@ColumnName("vclcDetailsType")
	int detailsType;
	

	@ColumnName("vclcCountry")
	String country;
	
	@ColumnName("vclcCity")
	String city;
	
	@ColumnName("vclcPostCode")
	String postCode;
	
	@ColumnName("vclcAddress")
	String address;
	
//	@ColumnName("vclcDate")
//	long date;
	
	
	@ColumnName("vclcSignature")
	String signature;
	
	public String getLandlineNumber() {
		return landlineNumber;
	}

	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	@ColumnName("vclcContactInfoName")
	String contactInfoName;

	@CurrentTime
	@ColumnName("vclcLastModificationTime")
	long lastModificationTime;
	
	@ColumnName("vclcIsDeleted")
	boolean isDeleted;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	
	
	public long getVpnClientID() {
		return vpnClientID;
	}

	public void setVpnClientID(long vpnClientID) {
		this.vpnClientID = vpnClientID;
	}

//	public void setDate(Long date) {
//		this.date = date;
//	}

	public String getRegistrantsName() {
		return registrantsName;
	}

	public void setRegistrantsName(String registrantsName) {
		this.registrantsName = registrantsName;
	}

	public int getDetailsType() {
		return detailsType;
	}

	public void setDetailsType(int detailsType) {
		this.detailsType = detailsType;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = StringUtils.trimToEmpty(email);
	}
	
	public int getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(int isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getIsPhoneNumberVerified() {
		return isPhoneNumberVerified;
	}

	public void setIsPhoneNumberVerified(int isPhoneNumberVerified) {
		this.isPhoneNumberVerified = isPhoneNumberVerified;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getContactInfoName() {
		return contactInfoName;
	}

	public void setContactInfoName(String contactInfoName) {
		this.contactInfoName = contactInfoName;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	
//	public long getDate() {
//		return date;
//	}
//
//	public void setDate(long date) {
//		this.date = date;
//	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getRegistrantsLastName() {
		return registrantsLastName;
	}

	public void setRegistrantsLastName(String registrantsLastName) {
		this.registrantsLastName = registrantsLastName;
	}

	public String getFullName()
	{
		String fullName = ((registrantsName == null)?"":registrantsName) + " " +  ((registrantsLastName == null)?"":registrantsLastName); 
		return fullName;
	}
	
	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if(StringUtils.isEmpty(country)){
			country="BD";
		}
		this.country = country;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientContactDetailsDTO other = (ClientContactDetailsDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientContactDetailsDTO [ID=" + ID + ", vpnClientID=" + vpnClientID + ", registrantsName="
				+ registrantsName + ", registrantsLastName=" + registrantsLastName + ", fatherName=" + fatherName
				+ ", motherName=" + motherName + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", email="
				+ email + ", isEmailVerified=" + isEmailVerified + ", organization=" + organization + ", occupation="
				+ occupation + ", webAddress=" + webAddress + ", faxNumber=" + faxNumber + ", phoneNumber="
				+ phoneNumber + ", isPhoneNumberVerified=" + isPhoneNumberVerified + ", detailsType=" + detailsType
				+ ", country=" + country + ", city=" + city + ", postCode=" + postCode + ", address=" + address
				+ ", signature=" + signature + ", contactInfoName=" + contactInfoName + ", lastModificationTime="
				+ lastModificationTime + ", isDeleted=" + isDeleted + "]";
	}

}
