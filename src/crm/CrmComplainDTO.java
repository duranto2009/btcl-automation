package crm;

import java.util.HashMap;
import java.util.Map;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_crm_complain")
public class CrmComplainDTO {

	// complain status
	public static final int STARTED = 1;
	public static final int ON_GOING = 2;
	public static final int COMPLETED = 3;
	public static final int REJECTED_BY_ADMIN = 4;
	public static final int CANCELLED_BY_CLIENT = 5;
	
	
	@SuppressWarnings("serial")
	public static Map<Integer,String > mapComplainStatusStringToStatusID = new HashMap<Integer,String>(){{
		put(null,"All");
		put(STARTED, "<label class='label label-warning'>Started</label>");
		put(ON_GOING,"<label class='label label-info'>On Going</label>");
		put(COMPLETED, "<label class='label label-success'>Completed</label>");
	}};
	
	public static int LOW = 1;
	public static int NORMAL = 2;
	public static int HIGH = 3;
	
	@SuppressWarnings("serial")
	public static Map<Integer,String > mapComplainPriorityStringToPriorityID = new HashMap<Integer,String>(){{
		put(null,"All");
		put(LOW, "<label class='label label-danger'>LOW</label>");
		put(NORMAL,"<label class='label label-warning'>NORMAL</label>");
		put(HIGH, "<label class='label label-info'>HIGH</label>");
	}};

	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("commonPoolID")
	Long commonPoolID;
	@ColumnName("tag")
	String tag;
	@ColumnName("pointerComplainHistoryID")
	Long pointerComplainHistoryID;
	@ColumnName(value = "rootComplainHistoryID") 
	long rootCompalinHistoryID;
	@ColumnName("parentComplainID")
	Long parentComplainID;
	@ColumnName("generationTime")
	long generationTime;
	@ColumnName("assignmentTime")
	long assignmentTime;
	@ColumnName("assignedToDesignationID")
	Long assignedToDesignationID;
	@ColumnName(value = "assignerID", editable = false) 
	long assignerID; 
	@ColumnName("complainResolverID")
	long complainResolverID; //who solve the complain
	@ColumnName("previousComplainResolverID")
	Long previousComplainResolverID;
	@ColumnName("lastResolverMsg")
	String lastResolverMsg;
	@ColumnName("currentStatus")
	int currentStatus;
	@ColumnName("priority")
	int priority;
	@ColumnName("currentDescription")
	String currentDescription;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	@ColumnName("isBlocked")
	boolean isBlocked;
	@ColumnName("lastActivityLogID")
	Long lastActivityLogID;

	@Override
	public String toString() {
		return "CrmComplainDTO [ID=" + ID + ", commonPoolID=" + commonPoolID + ", tag=" + tag
				+ ", pointerComplainHistoryID=" + pointerComplainHistoryID + ", rootCompalinHistoryID="
				+ rootCompalinHistoryID + ", parentComplainID=" + parentComplainID + ", generationTime="
				+ generationTime + ", assignmentTime=" + assignmentTime + ", assignedToDesignationID="
				+ assignedToDesignationID + ", assignerID=" + assignerID + ", complainResolverID=" + complainResolverID
				+ ", previousComplainResolverID=" + previousComplainResolverID + ", lastResolverMsg=" + lastResolverMsg
				+ ", currentStatus=" + currentStatus + ", priority=" + priority + ", currentDescription="
				+ currentDescription + ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime
				+ ", isBlocked=" + isBlocked + ", lastActivityLogID=" + lastActivityLogID + "]";
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
		CrmComplainDTO other = (CrmComplainDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	

	public Long getLastActivityLogID() {
		return lastActivityLogID;
	}

	public void setLastActivityLogID(Long lastActivityLogID) {
		this.lastActivityLogID = lastActivityLogID;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public Long getCommonPoolID() {
		return commonPoolID;
	}

	public void setCommonPoolID(Long commonPoolID) {
		this.commonPoolID = commonPoolID;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getPointerComplainHistoryID() {
		return pointerComplainHistoryID;
	}

	public void setPointerComplainHistoryID(Long pointerComplainHistoryID) {
		this.pointerComplainHistoryID = pointerComplainHistoryID;
	}

	public Long getRootCompalinHistoryID() {
		return rootCompalinHistoryID;
	}

	public void setRootCompalinHistoryID(long rootCompalinHistoryID) {
		this.rootCompalinHistoryID = rootCompalinHistoryID;
	}

	public Long getParentComplainID() {
		return parentComplainID;
	}

	public void setParentComplainID(Long parentComplainID) {
		this.parentComplainID = parentComplainID;
	}

	public long getGenerationTime() {
		return generationTime;
	}

	public void setGenerationTime(long generationTime) {
		this.generationTime = generationTime;
	}

	public long getAssignmentTime() {
		return assignmentTime;
	}

	public void setAssignmentTime(long assignmentTime) {
		this.assignmentTime = assignmentTime;
	}

	public Long getAssignedToDesignationID() {
		return assignedToDesignationID;
	}

	public void setAssignedToDesignationID(Long assignedToDesignationID) {
		this.assignedToDesignationID = assignedToDesignationID;
	}

	public long getAssignerID() {
		return assignerID;
	}

	public void setAssignerID(long assignerID) {
		this.assignerID = assignerID;
	}

	public long getComplainResolverID() {
		return complainResolverID;
	}

	public void setComplainResolverID(long complainResolverID) {
		this.complainResolverID = complainResolverID;
	}

	public Long getPreviousComplainResolverID() {
		return previousComplainResolverID;
	}

	public void setPreviousComplainResolverID(Long previousComplainResolverID) {
		this.previousComplainResolverID = previousComplainResolverID;
	}

	public String getLastResolverMsg() {
		return lastResolverMsg;
	}

	public void setLastResolverMsg(String lastResolverMsg) {
		this.lastResolverMsg = lastResolverMsg;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentDescription() {
		return currentDescription;
	}

	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	

}
