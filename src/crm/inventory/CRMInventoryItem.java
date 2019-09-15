package crm.inventory;

import annotation.*;

@TableName("at_crm_inventory_item")
public class CRMInventoryItem {
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
	private
	Boolean isUsed;
	@ColumnName("invitIsDeleted")
	boolean isDeleted;
	@ColumnName("invitLastModificationTime")
	@CurrentTime
	long lastModificationTime;
	@ColumnName(value = "invitPathFromRootToParent", editable = false)
	String pathFromRootToParent;
	
	
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
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getPathFromRootToParent() {
		return pathFromRootToParent;
	}
	public void setPathFromRootToParent(String pathFromRootToParent) {
		this.pathFromRootToParent = pathFromRootToParent;

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
		CRMInventoryItem other = (CRMInventoryItem) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CRMInventoryItem [ID=" + ID + ", inventoryCatagoryTypeID=" + inventoryCatagoryTypeID + ", parentID="
				+ parentID + ", name=" + name + ", isChildItem=" + isChildItem + ", isUsed=" + isUsed + ", isDeleted="			
				+ isDeleted + ", lastModificationTime=" + lastModificationTime + ", pathFromRoot=" + pathFromRootToParent + "]";

	}
	
}
