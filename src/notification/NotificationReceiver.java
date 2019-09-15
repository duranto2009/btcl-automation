package notification;

import annotation.*;


@TableName("")
public class NotificationReceiver {
	@ColumnName("")
	long ID;
	@ColumnName("")
	long roleID;
	@ColumnName("")
	long notificationID;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getRoleID() {
		return roleID;
	}
	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}
	public long getNotificationID() {
		return notificationID;
	}
	public void setNotificationID(long notificationID) {
		this.notificationID = notificationID;
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
		NotificationReceiver other = (NotificationReceiver) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NotificationReceiver [ID=" + ID + ", roleID=" + roleID
				+ ", notificationID=" + notificationID + "]";
	}
	
}
