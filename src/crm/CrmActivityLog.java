package crm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.SearchFieldFromMethod;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import common.ClientDTO;
@TableName("at_crm_activity_log")
public class CrmActivityLog {
	
	public static final int COMPLAIN_PASS = 1;
	public static final int COMPLAIN_ASSIGN = 2;
	public static final int COMPLAIN_FEEDBACK = 3;
	public static final int COMPLAIN_REJECT = 4;
	public static final int COMPLAIN_STATUS_CHANGE = 5;
	public static final int ATTEMP_TO_COMPLETE_COMPLAIN = 6;
	public static final int COMPLETE_COMPLAIN = 7;
	public static final int REJECTED_BY_ADMIN = 8;
	public static final int CANCELLED_BY_CLIENT = 9;
	@PrimaryKey
	@ColumnName("caclgID")
	long ID;
	@ColumnName(value = "caclgPrevEmplID",editable = false)
	Long previousEmployeeID;
	@ColumnName(value = "caclgPrevUsrID",editable = false)
	Long previousUserID;
	@ColumnName(value = "caclgEmployeeID",editable = false)
	Long currentEmployeeID;
	@ColumnName(value = "caclgUsrID",editable = false)
	Long currentUserID;
	@ColumnName(value = "caclgActnType",editable = false)
	int takenActionType;
	@ColumnName(value = "caclgDescription",editable = false)
	String description;
	@CurrentTime
	@ColumnName(value = "caclgFromTime",editable = false)
	long fromTimeForEmployeeToTakeAction;
	@ColumnName("caclgToTime")
	Long timeOfTakenAction;
	@ColumnName(value = "caclgComplainID",editable = false)
	long crmComplainID;
	@SearchFieldFromReferenceTable(entityClass=ClientDTO.class,operator="like",entityColumnName="loginName")
	@ColumnName(value ="caclgClientID", editable=false)
	Long clientID;
	@ColumnName(value="caclgEntityID", editable=false)
	Long entityID;
	@ColumnName(value="caclgEntityTypeID", editable=false)
	Integer entityTypeID;
	public long getCrmComplainID() {
		return crmComplainID;
	}
	public void setCrmComplainID(long crmComplainID) {
		this.crmComplainID = crmComplainID;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Long getPreviousEmployeeID() {
		return previousEmployeeID;
	}
	public void setPreviousEmployeeID(Long previousEmployeeID) {
		this.previousEmployeeID = previousEmployeeID;
	}
	public Long getPreviousUserID() {
		return previousUserID;
	}
	public void setPreviousUserID(Long previousUserID) {
		this.previousUserID = previousUserID;
	}
	public Long getCurrentEmployeeID() {
		return currentEmployeeID;
	}
	public void setCurrentEmployeeID(Long currentEmployeeID) {
		this.currentEmployeeID = currentEmployeeID;
	}
	public Long getCurrentUserID() {
		return currentUserID;
	}
	public void setCurrentUserID(Long currentUserID) {
		this.currentUserID = currentUserID;
	}
	public int getTakenActionType() {
		return takenActionType;
	}
	public void setTakenActionType(int takenActionType) {
		this.takenActionType = takenActionType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getFromTimeForEmployeeToTakeAction() {
		return fromTimeForEmployeeToTakeAction;
	}
	public void setFromTimeForEmployeeToTakeAction(long fromTimeForEmployeeToTakeAction) {
		this.fromTimeForEmployeeToTakeAction = fromTimeForEmployeeToTakeAction;
	}
	public Long getTimeOfTakenAction() {
		return timeOfTakenAction;
	}
	public void setTimeOfTakenAction(Long timeOfTakenAction) {
		this.timeOfTakenAction = timeOfTakenAction;
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
		CrmActivityLog other = (CrmActivityLog) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CrmActivityLog [ID=" + ID + ", previousEmployeeID=" + previousEmployeeID + ", previousUserID="
				+ previousUserID + ", currentEmployeeID=" + currentEmployeeID + ", currentUserID=" + currentUserID
				+ ", takenActionType=" + takenActionType + ", description=" + description
				+ ", fromTimeForEmployeeToTakeAction=" + fromTimeForEmployeeToTakeAction + ", timeOfTakenAction="
				+ timeOfTakenAction + ", crmComplainID=" + crmComplainID + "]";
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public Long getEntityID() {
		return entityID;
	}
	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}
	public Integer getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(Integer entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	

}
