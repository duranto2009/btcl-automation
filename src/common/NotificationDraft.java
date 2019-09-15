package common;

public class NotificationDraft {
	public String mail;
	public String phoneNumber;
	public String notificationContent;
	@Override
	public String toString() {
		return "mail: "+mail+",mobile Num: "+phoneNumber
				+"\n"+notificationContent;
	}
	
	
}
