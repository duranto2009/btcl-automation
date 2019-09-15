package crm.inventory;

import annotation.*;

@TableName("at_crm_inv_attr_name")
public class CRMInventoryAttributeName {
	@PrimaryKey
	@ColumnName("invatrnmID")
	long ID;
	@ColumnName("invatrnmCatTypID")	
	Integer inventoryCatagoryTypeID;

	@ColumnName("invatrnmIndexNum")
	int indexNumber;
	@ColumnName("invatrnmName")
	String name;
	String defaultValue;
	@ColumnName("invatrnmIsDeleted")
	boolean isDeleted;
	@ColumnName("invatrnmLastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Integer getInventoryCatagoryTypeID() {
		return inventoryCatagoryTypeID;
	}
	public void setInventoryCatagoryTypeID(Integer inventoryCatagoryTypeID) {
		this.inventoryCatagoryTypeID = inventoryCatagoryTypeID;
	}
	public int getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		CRMInventoryAttributeName other = (CRMInventoryAttributeName) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CRMInventoryAttributeName [ID=" + ID
				+ ", inventoryCatagoryTypeID=" + inventoryCatagoryTypeID
				+ ", indexNumber=" + indexNumber + ", name=" + name
				+ ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	
}
