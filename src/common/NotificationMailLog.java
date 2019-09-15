package common;

import annotation.*;

@TableName("at_notification_mail_log")
public class NotificationMailLog {
	@PrimaryKey
	@ColumnName("ntmlID")
	long ID;
	@ColumnName(value="ntmlEntityID",editable=false)
	long entityID;
	@ColumnName(value = "ntmlEntityTypeID",editable=false)
	int entityTypeID;
	@ColumnName("ntmlMailSendingTime")
	long mailSendingTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getEntityID() {
		return entityID;
	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public long getMailSendingTime() {
		return mailSendingTime;
	}
	public void setMailSendingTime(long mailSendingTime) {
		this.mailSendingTime = mailSendingTime;
	}
	@Override
	public String toString() {
		return "NotificationMailLog [ID=" + ID + ", entityID=" + entityID
				+ ", entityTypeID=" + entityTypeID + ", mailSendingTime="
				+ mailSendingTime + "]";
	}
	
}
