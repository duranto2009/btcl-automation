package crm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import crm.inventory.CRMInventoryCatagoryType;

@TableName("at_crm_designation_type")
@ForeignKeyName("inventoryCategoryID")
public class CrmDesignationDTO extends CRMInventoryCatagoryType {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("designationTypeID")
	Long designationTypeID;
	@ColumnName("hasPermissionToChangeStatus")
	boolean hasPermissionToChangeStatus;
	boolean hasPermissionToPassStatusChangingPermission;
	@ColumnName("hasPermissionToForwardComplain")
	boolean hasPermissionToForwardComplain;
	boolean hasPermissionToPassComplainForwardingPermission;
	@ColumnName("hasPermissionToPassComplain")
	boolean hasPermissionToPassComplain;
	boolean hasPermissionToPassComplainPassingPermission;
	@ColumnName("hasPermissionToAssignComplain")
	boolean hasPermissionToAssignComplain;
	boolean hasPermissionToPassComplainAssigningPermission;
	@ColumnName("hasPermissionToAddComplain")
	boolean hasPermissionToAddComplain;
	boolean hasPermissionToPassAddComplainPermission;
	@ColumnName("hasPermissionToChangePriority")
	boolean hasPermissionToChangePriority;
	boolean hasPermissionToPassChangePriorityPermission;
	@ColumnName("hasPermissionToPassComplainToOtherDepartment")
	boolean hasPermissionToPassComplainToOtherDepartment;
	boolean hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission;

	@ColumnName("isDeleted")
	boolean isDeleted;

	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	
	boolean isNOC;
	String departmentName;
	
	
	public boolean isNOC() {
		return isNOC;
	}
	public void setNOC(boolean isNOC) {
		this.isNOC = isNOC;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((designationTypeID == null) ? 0 : designationTypeID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrmDesignationDTO other = (CrmDesignationDTO) obj;
		if (designationTypeID == null) {
			if (other.designationTypeID != null)
				return false;
		} else if (!designationTypeID.equals(other.designationTypeID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CrmDesignationDTO [designationTypeID=" + designationTypeID + ", hasPermissionToChangeStatus="
				+ hasPermissionToChangeStatus + ", hasPermissionToPassStatusChangingPermission="
				+ hasPermissionToPassStatusChangingPermission + ", hasPermissionToForwardComplain="
				+ hasPermissionToForwardComplain + ", hasPermissionToPassComplainForwardingPermission="
				+ hasPermissionToPassComplainForwardingPermission + ", hasPermissionToPassComplain="
				+ hasPermissionToPassComplain + ", hasPermissionToPassComplainPassingPermission="
				+ hasPermissionToPassComplainPassingPermission + ", hasPermissionToAssignComplain="
				+ hasPermissionToAssignComplain + ", hasPermissionToPassComplainAssigningPermission="
				+ hasPermissionToPassComplainAssigningPermission + ", hasPermissionToAddComplain="
				+ hasPermissionToAddComplain + ", hasPermissionToPassAddComplainPermission="
				+ hasPermissionToPassAddComplainPermission + ", hasPermissionToChangePriority="
				+ hasPermissionToChangePriority + ", hasPermissionToPassChangePriorityPermission="
				+ hasPermissionToPassChangePriorityPermission + ", hasPermissionToPassComplainToOtherDepartment="
				+ hasPermissionToPassComplainToOtherDepartment
				+ ", hasPermissionToPassPassComplainPermissionToOtherDepartment="
				+ hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + ", isNOC=" + isNOC + ", departmentName="
				+ departmentName + ", ID=" + getID() + ", name=" + getName() + ", level=" + getLevel() + ", parentCatagoryTypeID="
				+ getParentCatagoryTypeID() + ", servlet=" + servlet + ", multipartRequestHandler=" + multipartRequestHandler
				+ "]";
	}

	public Long getDesignationID() {
		return designationTypeID;
	}

	public void setDesignationID(Long designationTypeID) {
		this.designationTypeID = designationTypeID;
	}

	public boolean isHasPermissionToChangeStatus() {
		return hasPermissionToChangeStatus;
	}

	public void setHasPermissionToChangeStatus(boolean hasPermissionToChangeStatus) {
		this.hasPermissionToChangeStatus = hasPermissionToChangeStatus;
	}

	public boolean isHasPermissionToPassStatusChangingPermission() {
		return hasPermissionToPassStatusChangingPermission;
	}

	public void setHasPermissionToPassStatusChangingPermission(boolean hasPermissionToPassStatusChangingPermission) {
		this.hasPermissionToPassStatusChangingPermission = hasPermissionToPassStatusChangingPermission;
	}

	public boolean isHasPermissionToForwardComplain() {
		return hasPermissionToForwardComplain;
	}

	public void setHasPermissionToForwardComplain(boolean hasPermissionToForwardComplain) {
		this.hasPermissionToForwardComplain = hasPermissionToForwardComplain;
	}

	public boolean isHasPermissionToPassComplainForwardingPermission() {
		return hasPermissionToPassComplainForwardingPermission;
	}

	public void setHasPermissionToPassComplainForwardingPermission(
			boolean hasPermissionToPassComplainForwardingPermission) {
		this.hasPermissionToPassComplainForwardingPermission = hasPermissionToPassComplainForwardingPermission;
	}

	public boolean isHasPermissionToPassComplain() {
		return hasPermissionToPassComplain;
	}

	public void setHasPermissionToPassComplain(boolean hasPermissionToPassComplain) {
		this.hasPermissionToPassComplain = hasPermissionToPassComplain;
	}

	public boolean isHasPermissionToPassComplainPassingPermission() {
		return hasPermissionToPassComplainPassingPermission;
	}

	public void setHasPermissionToPassComplainPassingPermission(boolean hasPermissionToPassComplainPassingPermission) {
		this.hasPermissionToPassComplainPassingPermission = hasPermissionToPassComplainPassingPermission;
	}

	public boolean isHasPermissionToAssignComplain() {
		return hasPermissionToAssignComplain;
	}

	public void setHasPermissionToAssignComplain(boolean hasPermissionToAssignComplain) {
		this.hasPermissionToAssignComplain = hasPermissionToAssignComplain;
	}

	public boolean isHasPermissionToPassComplainAssigningPermission() {
		return hasPermissionToPassComplainAssigningPermission;
	}

	public void setHasPermissionToPassComplainAssigningPermission(
			boolean hasPermissionToPassComplainAssigningPermission) {
		this.hasPermissionToPassComplainAssigningPermission = hasPermissionToPassComplainAssigningPermission;
	}

	public boolean isHasPermissionToAddComplain() {
		return hasPermissionToAddComplain;
	}

	public void setHasPermissionToAddComplain(boolean hasPermissionToAddComplain) {
		this.hasPermissionToAddComplain = hasPermissionToAddComplain;
	}

	public boolean isHasPermissionToPassAddComplainPermission() {
		return hasPermissionToPassAddComplainPermission;
	}

	public void setHasPermissionToPassAddComplainPermission(boolean hasPermissionToPassAddComplainPermission) {
		this.hasPermissionToPassAddComplainPermission = hasPermissionToPassAddComplainPermission;
	}

	public boolean isHasPermissionToChangePriority() {
		return hasPermissionToChangePriority;
	}

	public void setHasPermissionToChangePriority(boolean hasPermissionToChangePriority) {
		this.hasPermissionToChangePriority = hasPermissionToChangePriority;
	}

	public boolean isHasPermissionToPassChangePriorityPermission() {
		return hasPermissionToPassChangePriorityPermission;
	}

	public void setHasPermissionToPassChangePriorityPermission(boolean hasPermissionToPassChangePriorityPermission) {
		this.hasPermissionToPassChangePriorityPermission = hasPermissionToPassChangePriorityPermission;
	}

	public boolean isHasPermissionToPassComplainToOtherDepartment() {
		return hasPermissionToPassComplainToOtherDepartment;
	}

	public void setHasPermissionToPassComplainToOtherDepartment(boolean hasPermissionToPassComplainToOtherDepartment) {
		this.hasPermissionToPassComplainToOtherDepartment = hasPermissionToPassComplainToOtherDepartment;
	}

	public boolean isHasPermissionToPassPassComplainPermissionToOtherDepartment() {
		return hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission;
	}

	public void setHasPermissionToPassPassComplainPermissionToOtherDepartment(
			boolean hasPermissionToPassPassComplainPermissionToOtherDepartment) {
		this.hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission = hasPermissionToPassPassComplainPermissionToOtherDepartment;
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
