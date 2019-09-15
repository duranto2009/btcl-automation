package crm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import inventory.InventoryItem;

@TableName("at_crm_department")
public class CrmDepartmentDTO 
{
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("departmentName")
	String departmentName;
	@ColumnName("departmentCode")
	Integer departmentCode;
	@ColumnName("rootDesignationId")
	Long rootDesignationID;
	@ColumnName("isNOC")
	boolean isNOC;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	@SearchFieldFromReferenceTable(entityClass=InventoryItem.class,entityColumnName="name",operator="like",fixedCondition=" and invitInvCatTypeID = 1" )
	@ColumnName("districtID")
	Long districtID;
	@SearchFieldFromReferenceTable(entityClass=InventoryItem.class,entityColumnName="name",operator="like", fixedCondition=" and invitInvCatTypeID = 2")
	
	@ColumnName("upazilaID")
	Long upazilaID;
	@SearchFieldFromReferenceTable(entityClass=InventoryItem.class,entityColumnName="name",operator="like", fixedCondition=" and invitInvCatTypeID = 3")
	
	@ColumnName("unionID")
	Long unionID;
	
	String districtName;
	String upazilaName;
	String unionName;
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getUpazilaName() {
		return upazilaName;
	}
	public void setUpazilaName(String upazilaName) {
		this.upazilaName = upazilaName;
	}
	public String getUnionName() {
		return unionName;
	}
	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Integer getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(Integer departmentCode) {
		this.departmentCode = departmentCode;
	}
	public boolean isNOC() {
		return isNOC;
	}
	public void setNOC(boolean isNOC) {
		this.isNOC = isNOC;
	}
	public Long getRootDesignationID() {
		return rootDesignationID;
	}
	public void setRootDesignationID(Long rootDesignationID) {
		this.rootDesignationID = rootDesignationID;
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
	
	public Long getDistrictID() {
		return districtID;
	}
	public void setDistrictID(Long districtID) {
		this.districtID = districtID;
	}
	public Long getUpazilaID() {
		return upazilaID;
	}
	public void setUpazilaID(Long upazilaID) {
		this.upazilaID = upazilaID;
	}
	public Long getUnionID() {
		return unionID;
	}
	public void setUnionID(Long unionID) {
		this.unionID = unionID;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}
	public String getDepartmentCodeString(){
		return (this.departmentCode == null)? "N/A": this.departmentCode.toString();
	}
	public String getDistrictNameString(){
		return (this.districtName == null)?"All":this.districtName;
	}
	public String getUpazilaNameString(){
		return (this.upazilaName == null)?"All":this.upazilaName;
	}
	public String getUnionNameString(){
		return (this.unionName == null)?"All":this.unionName;
	}
	public String isNOCString (){
		return (this.isNOC == true)? "Yes": "No";
	}
	public String getRootDesignationIDString(){
		return (this.rootDesignationID == null)?"N/A":this.rootDesignationID.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrmDepartmentDTO other = (CrmDepartmentDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CrmDepartmentDTO [ID=" + ID + ", departmentName=" + departmentName + ", departmentCode="
				+ departmentCode + ", rootDesignationID=" + rootDesignationID + ", isNOC=" + isNOC + ", isDeleted="
				+ isDeleted + ", lastModificationTime=" + lastModificationTime + ", districtID=" + districtID
				+ ", upazilaID=" + upazilaID + ", unionID=" + unionID + ", districtName=" + districtName
				+ ", upazilaName=" + upazilaName + ", unionName=" + unionName + "]";
	}
}
