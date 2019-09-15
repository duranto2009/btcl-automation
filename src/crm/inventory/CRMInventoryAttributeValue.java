package crm.inventory;

import annotation.*;

@TableName("at_crm_inv_attr_value")
public class CRMInventoryAttributeValue {
	@PrimaryKey
	@ColumnName("invatrvalID")
	long ID;
	@ColumnName("invatrvalVal")
	String value;
	@ColumnName("invatrName")
	String name;
	
	@ColumnName("invatrvalInvItmID")
	long inventoryItemID;
	@ColumnName("invatrvalInvAttrNameID")
	long inventoryAttributeNameID;
	@ColumnName("invatrvalIsDeleted")
	boolean isDeleted;
	@ColumnName("invatrvalLastModificationTime")
	long lastModificationTime;
	
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getInventoryItemID() {
		return inventoryItemID;
	}
	public void setInventoryItemID(long inventoryItemID) {
		this.inventoryItemID = inventoryItemID;
	}
	public long getInventoryAttributeNameID() {
		return inventoryAttributeNameID;
	}
	public void setInventoryAttributeNameID(long inventoryAttributeNameID) {
		this.inventoryAttributeNameID = inventoryAttributeNameID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		CRMInventoryAttributeValue other = (CRMInventoryAttributeValue) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CRMInventoryAttributeValue [ID=" + ID + ", value=" + value
				+ ", inventoryItemID=" + inventoryItemID
				+ ", inventoryAttributeNameID=" + inventoryAttributeNameID
				+ ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	
}
