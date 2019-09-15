package inventory;

import annotation.*;

@TableName("at_inv_attr_name")
public class InventoryAttributeName {
	
	
	public static final int ATTRIBUTE_TYPE_TEXT = 0;
	public static final int ATTRIBUTE_TYPE_RADIO_BUTTON = 1;
	public static final int ATTRIBUTE_TYPE_DRPDOWN = 2;
	
	@PrimaryKey
	@ColumnName("invatrnmID")
	long ID;
	@ColumnName("invatrnmCatTypID")	
	Integer inventoryCatagoryTypeID;

	@ColumnName("invatrnmIndexNum")
	int indexNumber;
	@ColumnName("invatrnmName")
	String name;
	@ColumnName("invatrnmIsDeleted")
	boolean isDeleted;
	@ColumnName("invatrnmLastModificationTime")
	long lastModificationTime;
	@ColumnName("invatrnmAttributeType")
	int attributeType;
	@ColumnName("invatrnmAttributeTypeRegex")
	String regexOfAttributeType = ".*";
	@ColumnName("invatrnmAttributeTypeNames")
	String attributeTypeNames;

	public int getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(int attributeType) {
		this.attributeType = attributeType;
	}
	public String getRegexOfAttributeType() {
		return regexOfAttributeType;
	}
	public void setRegexOfAttributeType(String regexOfAttributeType) {
		this.regexOfAttributeType = regexOfAttributeType;
	}
	public String getAttributeTypeNames() {
		return attributeTypeNames;
	}
	public void setAttributeTypeNames(String attributeTypeNames) {
		this.attributeTypeNames = attributeTypeNames;
	}
	
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
		InventoryAttributeName other = (InventoryAttributeName) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "InventoryAttributeName [ID=" + ID
				+ ", inventoryCatagoryTypeID=" + inventoryCatagoryTypeID
				+ ", indexNumber=" + indexNumber + ", name=" + name
				+ ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	
}
