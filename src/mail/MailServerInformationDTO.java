package mail;

import java.util.ArrayList;

public class MailServerInformationDTO {
	public String mailServertxt;
	public String additionToAddresstxt;
	public String fromAddresstxt;
	public String mailServerPorttxt;
	public boolean authFromServerChk;
	public String authEmailAddesstxt;
	public String authEmailPasstxt;
	public boolean tlsRequired;
	public boolean isActive;
	@Override
	public String toString() {
		return "MailServerInformationDTO [mailServertxt=" + mailServertxt + ", additionToAddresstxt="
				+ additionToAddresstxt + ", fromAddresstxt=" + fromAddresstxt + ", mailServerPorttxt="
				+ mailServerPorttxt + ", authFromServerChk=" + authFromServerChk + ", authEmailAddesstxt="
				+ authEmailAddesstxt + ", authEmailPasstxt=" + authEmailPasstxt + ", tlsRequired=" + tlsRequired + "]";
	}
	
}
