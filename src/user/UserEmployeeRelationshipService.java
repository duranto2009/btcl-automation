package user;

import java.util.List;

public class UserEmployeeRelationshipService {

	/*
	 * user id -> employee ids
	 * (one/multiple)employee ids -> designation ids
	 * designation ids -> departments
	 * departments-> districts,unions,upazilas
	 */
	
	public List<Long> getDistrictIDListByUserID(long userID){
		List<Long> departmentIDList = getDepartmentIDListByUserID(userID);
		return getDistrictIDListByDepartmentIDLsit(departmentIDList);
	}
	
	public List<Long> getUpazilaIDListByUserID(long userID){
		List<Long> departmentIDList = getDepartmentIDListByUserID(userID);
		return getUpazilaIDListByDepartmentIDLsit(departmentIDList);
	}
	
	public List<Long> getUnionIDListByUserID(long userID){
		List<Long> departmentIDList = getDepartmentIDListByUserID(userID);
		return getUnionIDListByDepartmentIDLsit(departmentIDList);
	}
	
	
	public List<Long> getEmployeeIdListByUserID(long userID){
		return null;/*call CrmAllRepository method*/
	}
	
	public List<Long> getDesignationIDListByEmployeeIDList(List<Long> employeeIDs){
		List<Long> designationIDList = null;
		for(Long employeeID:employeeIDs){
			designationIDList.add(null);/*call CrmAllRepository method*/
		}
		return designationIDList;
	}
	
	public List<Long> getRootDesignationIdListByDesignationIdList(List<Long> designationIDs){
		List<Long> rootDesignationIDList = null;
		for(Long designationID:designationIDs){
			rootDesignationIDList.add(null);/*call CrmAllRepository method*/
		}
		return rootDesignationIDList;
	}
	
	public List<Long> getDepartmentIDListByRootDesignationIDLsit(List<Long> rootDesignationIDs){
		List<Long> departmentIDList = null;
		for(Long rootDesignationID:rootDesignationIDs){
			departmentIDList.add(null);/*call CrmAllRepository method*/
		}
		return departmentIDList;
	}
	
	public List<Long> getDepartmentIDListByUserID(long userID){
		List<Long> employeIDList = getEmployeeIdListByUserID(userID);
		List<Long> designationIDList = getDesignationIDListByEmployeeIDList(employeIDList);
		List<Long> rootDesignationIDList = getRootDesignationIdListByDesignationIdList(designationIDList);
		return getDepartmentIDListByRootDesignationIDLsit(rootDesignationIDList);
	}
	
	public List<Long> getDistrictIDListByDepartmentIDLsit(List<Long> departmentIDList) {
		return null;
	}

	public List<Long> getUpazilaIDListByDepartmentIDLsit(List<Long> departmentIDList) {
		return null;
	}

	public List<Long> getUnionIDListByDepartmentIDLsit(List<Long> departmentIDList) {
		return null;
	}
	
}
