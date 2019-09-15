package permission;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("adcolumnpermission")
public class ColumnPermissionDTO {

	@ColumnName("clPermissionID")
	@PrimaryKey
	long ID;
	
	@ColumnName(value = "clRoleID",editable = false)
	long roleID;
	
	@CurrentTime
	@ColumnName("clLastModificationTime")	
	long lastModificationTime;
	
	@ColumnName("clIsDeleted")
	boolean isDeleted;
	
	@ColumnName("clColumnID")
	int columnID;
	
	
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


	public int getColumnID() {
		return columnID;
	}


	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}


	@Override
	public String toString() {
		return "ColumnPermissionDTO [ID=" + ID + ", roleID=" + roleID + ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + ", columnID=" + columnID + "]";
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
		ColumnPermissionDTO other = (ColumnPermissionDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	
}
