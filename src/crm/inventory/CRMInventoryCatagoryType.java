package crm.inventory;
import org.apache.struts.action.ActionForm;

import annotation.*;

@TableName("at_crm_inv_cat_type")
public class CRMInventoryCatagoryType extends ActionForm{
	@PrimaryKey
	@ColumnName(value="invctID",editable=false)
	Integer ID;
	@ColumnName("invctName")
	String name;
	@ColumnName(value="invctLevel",editable=false)
	int level;
	@ColumnName(value="invctParentID",editable=false)
	Integer parentCatagoryTypeID;
	@ColumnName("invctIsDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("invctLastModificationTime")
	long lastModificationTime;
	public boolean hasParentCatagory(){
		return parentCatagoryTypeID !=  null;
	}
	public Integer getParentCatagoryTypeID() {
		return parentCatagoryTypeID;
	}
	public void setParentCatagoryTypeID(Integer parentCatagoryTypeID) {
		this.parentCatagoryTypeID = parentCatagoryTypeID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		CRMInventoryCatagoryType other = (CRMInventoryCatagoryType) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CRMInventoryCatagoryType [ID=" + ID + ", name=" + name
				+ ", level=" + level + ", parentCatagoryTypeID="
				+ parentCatagoryTypeID + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}
	
	
}
