package crm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_crm_complain_history")
public class CrmComplainHistoryDTO {
	
	/*Status code
	 **/
	public static final int STATUS_SUCCESS = 1;
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName(value = "parentHistoryID",editable = false)
	Long parentComplainHistoryID;
	@ColumnName(value = "rootHistoryID",editable = false)
	Long rootComplainHistoryID;
	@ColumnName("status")
	int status;
	@ColumnName("description")
	String description;//when created
	@ColumnName("feedBack")
	String feedBack;// when rejected 
	@ColumnName("complainResolverID")
	long complainResolverID;// who solve this complain
	@ColumnName("actionTakerID")
	long actionTakerID;
	@ColumnName("complainHistorySubmissionTime")
	long complainHistorySubmissionTime; // when ch created
	@ColumnName("isDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	
	
	@Override
	public String toString() {
		return "CrmComplainHistoryDTO [ID=" + ID + ", parentComplainHistoryID=" + parentComplainHistoryID
				+ ", rootComplainHistoryID=" + rootComplainHistoryID + ", status=" + status + ", description="
				+ description + ", feedBack=" + feedBack + ", complainResolverID=" + complainResolverID
				+ ", actionTakerID=" + actionTakerID + ", complainHistorySubmissionTime="
				+ complainHistorySubmissionTime + ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
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
		CrmComplainHistoryDTO other = (CrmComplainHistoryDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	public Long getRootComplainHistoryID() {
		return rootComplainHistoryID;
	}
	public void setRootComplainHistoryID(Long rootComplainHistoryID) {
		this.rootComplainHistoryID = rootComplainHistoryID;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Long getParentComplainHistoryID() {
		return parentComplainHistoryID;
	}
	public void setParentComplainHistoryID(Long parentComplainHistoryID) {
		this.parentComplainHistoryID = parentComplainHistoryID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	public long getComplainResolverID() {
		return complainResolverID;
	}
	public void setComplainResolverID(long complainResolverID) {
		this.complainResolverID = complainResolverID;
	}
	public long getActionTakerID() {
		return actionTakerID;
	}
	public void setActionTakerID(long actionTakerID) {
		this.actionTakerID = actionTakerID;
	}
	public long getComplainHistorySubmissionTime() {
		return complainHistorySubmissionTime;
	}
	public void setComplainHistorySubmissionTime(long complainHistorySubmissionTime) {
		this.complainHistorySubmissionTime = complainHistorySubmissionTime;
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
	
	

}
