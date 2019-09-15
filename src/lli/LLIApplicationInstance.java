package lli;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_lli_connection_application_instance")
public class LLIApplicationInstance {
	
	@PrimaryKey
	@ColumnName("ID")
	long ID;
	@ColumnName("applicationID")
	long applicationID;
	@ColumnName("clientID")
	long clientID;
	@ColumnName("applicationDate")
	long applicationDate;
	@ColumnName("fields")
	String fields;
	
	//
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getApplicationID() {
		return applicationID;
	}
	public void setApplicationID(long applicationID) {
		this.applicationID = applicationID;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(long applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	
}
