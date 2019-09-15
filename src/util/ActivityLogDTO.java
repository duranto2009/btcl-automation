package util;
import java.util.*;
import annotation.*;

@TableName("at_activity_log")
public class ActivityLogDTO {
	@PrimaryKey
	@ColumnName("aclgID")
	long ID;
	@ColumnName("aclgActivityTypeID")
	int activityTypeID;
	@ColumnName("aclgEntityID")
	long entityID;
	@ColumnName("aclgDescription")
	String description;
//	@ColumnName("aclgReqID")
//	long reqID;
	@ColumnName("aclgEntityTypeID")
	long entityTypeID;
	@ColumnName("aclgByWhomAccountID")
	long userAccountID;
	@ColumnName("aclgActivityTime")
	long activityTime;
	@ColumnName("aclgIsDeleted")
	boolean isDeleted;
	@ColumnName("aclgLastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getActivityTypeID() {
		return activityTypeID;
	}
	public void setActivityTypeID(int activityTypeID) {
		this.activityTypeID = activityTypeID;
	}
	public long getEntityID() {
		return entityID;
	}
//	public long getReqID() {
//		return reqID;
//	}
//	public void setReqID(long reqID) {
//		this.reqID = reqID;
//	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(long entityIDType) {
		this.entityTypeID = entityIDType;
	}
	public long getUserAccountID() {
		return userAccountID;
	}
	public void setUserAccountID(long userAccountID) {
		this.userAccountID = userAccountID;
	}
	public long getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(long activityTime) {
		this.activityTime = activityTime;
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
		ActivityLogDTO other = (ActivityLogDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ActivityLogDTO [ID=" + ID + ", activityTypeID=" + activityTypeID + ", entityID=" + entityID + ", description=" + description + ", entityIDType=" + entityTypeID
				+ ", userAccountID=" + userAccountID + ", activityTime=" + activityTime + ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}
	
}
