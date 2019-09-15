package request;

import annotation.*;

@TableName("at_ownership_change_request")
@ForeignKeyName("ownchrqReqID")
public class OwnerShipChangeRequest extends CommonRequestDTO{
	@PrimaryKey
	@ColumnName("ownchrqID")
	long ID;
	@ColumnName("ownchrqCurrentClientID")
	long oldClientID;
	@ColumnName("ownchrqNewClientID")
	long newClientID;
	@ColumnName("ownchrqLastModificationTime")
	long lastModificationTime;
	@ColumnName("ownchrqIsDeleted")
	boolean isDeleted;

	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}

	public long getOldClientID() {
		return oldClientID;
	}
	public void setOldClientID(long oldClientID) {
		this.oldClientID = oldClientID;
	}
	public long getNewClientID() {
		return newClientID;
	}
	public void setNewClientID(long newClientID) {
		this.newClientID = newClientID;
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
	@Override
	public String toString() {
		return "OwnerShipChangeRequest [ID=" + ID + ", oldClientID=" + oldClientID + ", newClientID=" + newClientID
				+ ", lastModificationTime=" + lastModificationTime + ", isDeleted=" + isDeleted + ", receiverName="
				+ receiverName + ", reqID=" + reqID + ", requestTypeID=" + requestTypeID + ", entityTypeID="
				+ entityTypeID + ", entityID=" + entityID + ", rootReqID=" + rootReqID + ", parentReqID=" + parentReqID
				+ ", clientID=" + clientID + ", requestTime=" + requestTime + ", requestByAccountID="
				+ requestByAccountID + ", requestToAccountID=" + requestToAccountID + ", priority=" + priority
				+ ", expireTime=" + expireTime + ", description=" + description + ", completionStatus="
				+ completionStatus + ", sourceRequestID=" + sourceRequestID + ", IP=" + IP + ", actionName="
				+ actionName + ", state=" + state + ", moduleID=" + moduleID + ", visibleOnly=" + visibleOnly
				+ ", rootActionsOnly=" + rootActionsOnly + ", clientName=" + clientName + ", entityName=" + entityName
				+ ", expireTimeFormated=" + expireTimeFormated + ", billID=" + billID + ", relatedEntityList="
				+ relatedEntityList + ", servlet=" + servlet + ", multipartRequestHandler=" + multipartRequestHandler
				+ "]";
	}
	
}
