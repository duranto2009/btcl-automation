package crm;

import annotation.ColumnName;

public class CrmEmployeeDesignationValuePair {
	@ColumnName("crmEmployeeID")
	long employeeID;
	@ColumnName("employeeName")
	String employeeName;
	@ColumnName("designationName")
	String designationName;
	public CrmEmployeeDesignationValuePair(){}
	public CrmEmployeeDesignationValuePair(long employeeID,String employeeName,String designationName){
		this.employeeID = employeeID;
		this.employeeName = employeeName;
		this.designationName = designationName;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
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

	@Override
	public String toString() {
		return "CrmEmployeeMinDTO [employeeID=" + employeeID + ", employeeName=" + employeeName + ", designationName="
				+ designationName + "]";
	}

}
