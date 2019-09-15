package request;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_req_state")
public class CommonRequestStatusDTO extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("rsID")
	long ID;
	@ColumnName("rsReqID")
	long requestID;
	@ColumnName("rsParentReqID")
	Long parenRequestID;
	@ColumnName("rsRequestTypeID")
	int requestTypeID;
	@ColumnName("rsClientID")
	long clientID;
	@ColumnName("rsState")
	int state;
	@ColumnName("rsEntityTypeID")
	int entityTypeID;
	@ColumnName("rsEntityID")
	long entityID;
	@ColumnName("rsRequestedToAccountID")
	long requestedToAccountID;
	@ColumnName("rsTime")
	long time;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getRequestID() {
		return requestID;
	}
	public void setRequestID(long requestID) {
		this.requestID = requestID;
	}
	public Long getParenRequestID() {
		return parenRequestID;
	}
	public void setParenRequestID(Long parenRequestID) {
		this.parenRequestID = parenRequestID;
	}
	public int getRequestTypeID() {
		return requestTypeID;
	}
	public void setRequestTypeID(int requestTypeID) {
		this.requestTypeID = requestTypeID;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public int getState() {
		return state;
	}
	public void setState(int status) {
		this.state = status;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public long getEntityID() {
		return entityID;
	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	public long getRequestedToAccountID() {
		return requestedToAccountID;
	}
	public void setRequestedToAccountID(long requestedToAccountID) {
		this.requestedToAccountID = requestedToAccountID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "CommonRequestStatusDTO [ID=" + ID + ", requestID=" + requestID + ", parenRequestID=" + parenRequestID + ", requestTypeID=" + requestTypeID + ", clientID=" + clientID + ", state="
				+ state + ", entityTypeID=" + entityTypeID + ", entityID=" + entityID + ", requestedToAccountID=" + requestedToAccountID + ", time=" + time + "]";
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
		CommonRequestStatusDTO other = (CommonRequestStatusDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	

}
