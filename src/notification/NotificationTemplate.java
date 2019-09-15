package notification;

import annotation.*;

@TableName("at_notification_template")
public class NotificationTemplate {
	public static final int NOTIFICATION_TYPE_SMS = 1;
	public static final int NOTIFICATION_TYPE_MAIL = 2;
	public static final int NOTIFICATION_TYPE_WEB = 3;
	
	public static final int CLIENT_TEMPLATE = 1;
	public static final int SENDER_TEMPLATE = 2;
	public static final int RECEIEVER_TEMPLATE = 3;
	
	
	@PrimaryKey
	@ColumnName("notitmpID")
	long ID;
	@ColumnName("notitmpDescription")
	String description;
	@ColumnName("notitmpType")
	int type; // web,sms,mail
	@ColumnName("notitmpUserType")
	int userType; // client,request sender,request receiver
	@ColumnName("notitmpAllowClient")
	boolean allowClientForNotification;
	@ColumnName("notitmpActionStateID")
	int actionStateID;
	@ColumnName("notitmpCaption")
	String caption;
	
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isAllowClientForNotification() {
		return allowClientForNotification;
	}
	public void setAllowClientForNotification(boolean allowClientForNotification) {
		this.allowClientForNotification = allowClientForNotification;
	}
	public int getActionStateID() {
		return actionStateID;
	}
	public void setActionStateID(int actionStateID) {
		this.actionStateID = actionStateID;
	}
	
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
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
		NotificationTemplate other = (NotificationTemplate) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NotificationTemplate [ID=" + ID + ", description="
				+ description + ", type=" + type
				+ ", allowClientForNotification=" + allowClientForNotification
				+ ", actionStateID=" + actionStateID + ", caption=" + caption
				+ "]";
	}
	
	
}
