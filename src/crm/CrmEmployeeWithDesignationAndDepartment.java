package crm;

public class CrmEmployeeWithDesignationAndDepartment {
	private long employeeID;
	private String employeeName;
	private String designationName;
	private String departmentName;
	public long getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(long iD) {
		employeeID = iD;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
