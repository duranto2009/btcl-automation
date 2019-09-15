package crm;

public class CrmEmployeeDetails {
	CrmEmployeeDTO employee;
	CrmDesignationDTO designation;
	CrmDesignationDTO rootDesignation;
	CrmDepartmentDTO department;
	String districtName;
	String upazilaName;
	String unionName;
	public CrmEmployeeDetails() {
	}
	public CrmEmployeeDTO getEmployee() {
		return employee;
	}
	public void setEmployee(CrmEmployeeDTO employee) {
		this.employee = employee;
	}
	public CrmDesignationDTO getDesignation() {
		return designation;
	}
	public void setDesignation(CrmDesignationDTO designation) {
		this.designation = designation;
	}
	public CrmDesignationDTO getRootDesignation() {
		return rootDesignation;
	}
	public void setRootDesignation(CrmDesignationDTO rootDesignation) {
		this.rootDesignation = rootDesignation;
	}
	public CrmDepartmentDTO getDepartment() {
		return department;
	}
	public void setDepartment(CrmDepartmentDTO department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "CrmEmployeeDetails [employee=" + employee + ", designation=" + designation + ", rootDesignation="
				+ rootDesignation + ", department=" + department + "]";
	}
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
	
}
