package crm;

import annotation.*;
import crm.inventory.CRMInventoryItem;

import java.util.HashMap;
import java.util.Map;

@TableName("at_crm_employee")
@ForeignKeyName("inventoryItemID")
public class CrmEmployeeDTO extends CRMInventoryItem {
	@PrimaryKey
	@ColumnName("crmEmployeeID")
	long crmEmployeeID;
	@ColumnName("userID")
	Long userID;
	
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
	
	int employeeStatus;
	
	public static final int REGULAR = 1;
	public static final int ON_LEAVE = 2;
	public static final int TRANSIENT = 3;
	
	public static Map<Integer, String> mapOfEmployeeStatusStringToStatusID = new HashMap<Integer,String>(){{
		put(null,"All");
		put(REGULAR, "<label class='label label-danger'>REGULAR</label>");
		put(ON_LEAVE,"<label class='label label-warning'>ON_LEAVE</label>");
		put(TRANSIENT, "<label class='label label-info'>TRANSIENT</label>");
	}};
	
	@Override
	public String toString() {
		return "CrmEmployeeDTO [crmEmployeeID=" + crmEmployeeID + ", userID=" + userID
				+ ", hasPermissionToChangeStatus=" + hasPermissionToChangeStatus
				+ ", hasPermissionToPassStatusChangingPermission=" + hasPermissionToPassStatusChangingPermission
				+ ", hasPermissionToForwardComplain=" + hasPermissionToForwardComplain
				+ ", hasPermissionToPassComplainForwardingPermission=" + hasPermissionToPassComplainForwardingPermission
				+ ", hasPermissionToPassComplain=" + hasPermissionToPassComplain
				+ ", hasPermissionToPassComplainPassingPermission=" + hasPermissionToPassComplainPassingPermission
				+ ", hasPermissionToAssignComplain=" + hasPermissionToAssignComplain
				+ ", hasPermissionToPassComplainAssigningPermission=" + hasPermissionToPassComplainAssigningPermission
				+ ", hasPermissionToAddComplain=" + hasPermissionToAddComplain
				+ ", hasPermissionToPassAddComplainPermission=" + hasPermissionToPassAddComplainPermission
				+ ", hasPermissionToChangePriority=" + hasPermissionToChangePriority
				+ ", hasPermissionToPassChangePriorityPermission=" + hasPermissionToPassChangePriorityPermission
				+ ", hasPermissionToPassComplainToOtherDepartment=" + hasPermissionToPassComplainToOtherDepartment
				+ ", hasPermissionToPassPassComplainPermissionToOtherDepartment="
				+ hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + ", isNOC=" + isNOC + ", departmentName="
				+ departmentName + ", employeeStatus=" + employeeStatus + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (crmEmployeeID ^ (crmEmployeeID >>> 32));
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
		CrmEmployeeDTO other = (CrmEmployeeDTO) obj;
		if (crmEmployeeID != other.crmEmployeeID)
			return false;
		return true;
	}
	public long getCrmEmployeeID() {
		return crmEmployeeID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public void setCrmEmployeeID(long crmEmployeeID) {
		this.crmEmployeeID = crmEmployeeID;
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
	public void setHasPermissionToPassComplainAssigningPermission(boolean hasPermissionToPassComplainAssigningPermission) {
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
			boolean hassPermissionToPassPassComplainPermissionToOtherDepartment) {
		this.hasPermissionToPassPassComplainPermissionToOtherDepartmentPermission = hassPermissionToPassPassComplainPermissionToOtherDepartment;
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
	
	public int getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(int employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
}
