package request.form;

import org.apache.struts.action.ActionForm;

import annotation.*;

@TableName("at_req")
@ForeignKeyName("clID")
public class RequestForm extends ActionForm{

	static final private String tableName = "at_req";
	@ColumnName("arID")
	@PrimaryKey
	long requestID;
	@ColumnName("arRequestTypeID")
	int requestTypeID;
	@ColumnName("arClientId")
	long clientID;
	@ColumnName("arActionTypeID")
	int actionTypeID;
	@ColumnName("arReqTime")
	long requestTime;
	@ColumnName("arRequestedByAccountID")
	long requestByAccountID;
	@ColumnName("arRequestedToAccountID")
	long requestToAccountID;
	@ColumnName("arPriority")
	int priority;
	@ColumnName("arExpireTime")
	long expireTime;
	@ColumnName("arLastModificationTime")
	long lastModificationTime;
	@ColumnName("arIsDeleted")
	boolean isDeleted;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (requestID ^ (requestID >>> 32));
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
		RequestForm other = (RequestForm) obj;
		if (requestID != other.requestID)
			return false;
		return true;
	}
	public long getRequestID() {
		return requestID;
	}
	public void setRequestID(long requestID) {
		this.requestID = requestID;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
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
	public int getRequestTypeID() {
		return requestTypeID;
	}
	public void setRequestTypeID(int requestTypeID) {
		this.requestTypeID = requestTypeID;
	}
	public int getActionTypeID() {
		return actionTypeID;
	}
	public void setActionTypeID(int actionTypeID) {
		this.actionTypeID = actionTypeID;
	}
	public long getRequestByAccountID() {
		return requestByAccountID;
	}
	public void setRequestByAccountID(long requestByAccountID) {
		this.requestByAccountID = requestByAccountID;
	}
	public long getRequestToAccountID() {
		return requestToAccountID;
	}
	public void setRequestToAccountID(long requestToAccountID) {
		this.requestToAccountID = requestToAccountID;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	@Override
	public String toString() {
		return "CommonRequestDTO [requestID=" + requestID + ", requestTypeID="
				+ requestTypeID + ", clientID=" + clientID + ", actionTypeID="
				+ actionTypeID + ", requestTime=" + requestTime
				+ ", requestByAccountID=" + requestByAccountID
				+ ", requestToAccountID=" + requestToAccountID + ", priority="
				+ priority + ", expireTime=" + expireTime
				+ ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + "]";
	}
	
}
