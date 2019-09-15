package permission;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_action_permission")
public class ActionPermissionDTO {

	@ColumnName("apID")
	@PrimaryKey
	long ID;
	
	@ColumnName(value = "apRoleID",editable = false)
	long roleID;
	
	@CurrentTime
	@ColumnName("apLastModificationTime")	
	long lastModificationTime;
	
	@ColumnName("apIsDeleted")
	boolean isDeleted;
	
	@ColumnName("apStateActionTypeID")
	int stateActionTypeID;
	
	
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


	public int getStateActionTypeID() {
		return stateActionTypeID;
	}


	public void setStateActionTypeID(int stateActionTypeID) {
		this.stateActionTypeID = stateActionTypeID;
	}


	@Override
	public String toString() {
		return "ActionPermissionDTO [ID=" + ID + ", roleID=" + roleID + ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + ", stateActionTypeID=" + stateActionTypeID + "]";
	}


	
	
	
}
