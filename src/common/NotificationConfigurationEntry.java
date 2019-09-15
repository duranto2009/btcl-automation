package common;

import annotation.*;


@TableName("at_scheduled_notification_Msg_config")
public class NotificationConfigurationEntry implements Comparable<NotificationConfigurationEntry>{
	@PrimaryKey
	@ColumnName("atSchnotimsgconfigID")
	long ID;
	@ColumnName("atSchnotimsgconfigEntityTypeID")
	int entityTypeID;
	@ColumnName("atSchnotimsgconfigState")
	String statesSeparatedByComma;
	@ColumnName("atSchnotimsgconfigDays")
	int days;
	@ColumnName("atSchnotimsgconfigMessage")
	String message;
	@ColumnName("atSchnotimsgconfigAllowMailNotification")
	boolean allowMailNotification;
	@ColumnName("atSchnotimsgconfigAllowSmsNotification")
	boolean allowSmsNotification;
	@ColumnName("atSchnotimsgconfigColumnName")
	String columnName;
	
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
		NotificationConfigurationEntry other = (NotificationConfigurationEntry) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public String getStatesSeparatedByComma() {
		return statesSeparatedByComma;
	}
	public void setStatesSeparatedByComma(String states) {
		this.statesSeparatedByComma = states;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAllowMailNotification() {
		return allowMailNotification;
	}
	public void setAllowMailNotification(boolean allowMailNotification) {
		this.allowMailNotification = allowMailNotification;
	}
	public boolean isAllowSmsNotification() {
		return allowSmsNotification;
	}
	public void setAllowSmsNotification(boolean allowSmsNotification) {
		this.allowSmsNotification = allowSmsNotification;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String toString() {
		return "NotificationConfugarationEntry[" + days + "]";
	}
	@Override
	public int compareTo(NotificationConfigurationEntry arg0) {
		if(days==arg0.getDays()){
			return 0;
		}else if(days<arg0.getDays()){
			return -1;
		}else{
			return 1;
		}
	}
	
}
