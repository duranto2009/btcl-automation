package inventory;

import annotation.*;

@TableName("at_inventory_item")
public class InventoryItem {
	@PrimaryKey
	@ColumnName(value = "invitID",editable = false)
	long ID;
	@ColumnName(value = "invitInvCatTypeID", editable = false)
	Integer inventoryCatagoryTypeID;
	@ColumnName(value = "invitParentItemID", editable = false)
	Long parentID;

	@ColumnName(value = "invitName", editable = true)
	String name;
	@ColumnName(value = "invitIsChildItem", editable = false)
	boolean isChildItem;
	@ColumnName("invitIsUsed")
	Boolean isUsed;
	@ColumnName("invitIsDeleted")
	boolean isDeleted;
	@ColumnName("invitLastModificationTime")
	long lastModificationTime;
	@ColumnName("invitOccupierEntityID")
	Long occupierEntityID;
	@ColumnName("invitOccupierEntityTypeID")
	Integer occupierEntityTypeID;
	@ColumnName("invitOccupierClientID")
	Long occupierClientID;
	
	
	public Long getOccupierClientID() {
		return occupierClientID;
	}
	public void setOccupierClientID(Long occupierClientID) {
		this.occupierClientID = occupierClientID;
	}
	public Long getOccupierEntityID() {
		return occupierEntityID;
	}
	public void setOccupierEntityID(Long occupierEntityID) {
		this.occupierEntityID = occupierEntityID;
	}
	/**
	 * @return the occupierEntityTypeID
	 */
	public int getOccupierEntityTypeID() {
		if (occupierEntityID!=null)	return occupierEntityTypeID;
		else return 0;
	}
	/**
	 * @param occupierEntityTypeID the occupierEntityTypeID to set
	 */
	public void setOccupierEntityTypeID(Integer occupierEntityTypeID) {
		this.occupierEntityTypeID = occupierEntityTypeID;
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
	public String getName() {
		return (name == null || name.isEmpty()) ? "N/A" : name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
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
	public Long getParentID() {
		return parentID;
	}
	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}
	
	
	public boolean isChildItem() {
		return isChildItem;
	}
	public void setChildItem(boolean isChildItem) {
		this.isChildItem = isChildItem;
	}
	public Boolean getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}
	public boolean hasParent(){
		return parentID!=null;
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
		InventoryItem other = (InventoryItem) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "InventoryItem [ID=" + ID + ", inventoryCatagoryTypeID=" + inventoryCatagoryTypeID + ", parentID="
				+ parentID + ", name=" + name + ", isChildItem=" + isChildItem + ", isUsed=" + isUsed + ", isDeleted="			
				+ isDeleted + ", lastModificationTime=" + lastModificationTime + "]";

	}
	
}
