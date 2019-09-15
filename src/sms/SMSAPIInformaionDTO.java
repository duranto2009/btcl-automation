package sms;

public class SMSAPIInformaionDTO {
	public String smsURL;
	public String providerName;
	public String username;
	public String password;
	public boolean isActive;
	@Override
	public String toString() {
		return "SMSAPIInformaionDTO [smsUrl=" + smsURL + ", providerName=" + providerName + ", username=" + username
				+ ", password=" + password + ", isActive=" + isActive + "]";
	}
	
	
}
