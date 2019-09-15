package crm.dao;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getColumnName;
import static util.ModifiedSqlGenerator.getIDListFromSearchCriteria;
import static util.ModifiedSqlGenerator.getObjectListByIDList;
import static util.ModifiedSqlGenerator.getTableName;
import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import common.RequestFailureException;
import common.StringUtils;
import crm.CrmDepartmentDTO;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;


public class CrmDepartmentDAO {
	public void insertCrmDepartmentDTO(CrmDepartmentDTO crmDepartmentDTO) throws Exception{
		insert(crmDepartmentDTO, CrmDepartmentDTO.class, false);
	}
	
	public void updateDepartmentDTO(CrmDepartmentDTO crmDepartmentDTO) throws Exception{
		updateEntity(crmDepartmentDTO, CrmDepartmentDTO.class,false,false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllDepartmentIDWithSearchCriteria(Hashtable searchCriteria) throws Exception {
		Class classObject = CrmDepartmentDTO.class;
		String []keys= new String[]            {"districtName",      "upazilaName",      "unionName",   "departmentName",  	"isDeleted" };
		String []operators = new String[]      {"IN",             	 "IN",             	 "IN",       	"like"		,     	 "="	    };
		String []dtoColumnNames = new String[] {"districtID",      	 "upazilaID",     	 "unionID",     "departmentName",	 "isDeleted"};
		searchCriteria.put("isDeleted", "0");
		
		return getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, searchCriteria, "");
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CrmDepartmentDTO> getCrmDeptListByIDList(Collection recordIDs) throws Exception {
		Class<?> classObject = CrmDepartmentDTO.class;
		return (List<CrmDepartmentDTO>) getObjectListByIDList(classObject, recordIDs,getColumnName(classObject, "isDeleted")+" = 0");
	}
	
	

	public void deleteDepartmentByDepartmentIDList(List<Long> departmentIDs) throws Exception {
		Class<?> classObject = CrmDepartmentDTO.class;
		String sql = "update " + getTableName(classObject) + " set " + getColumnName(classObject, "isDeleted") + " = 1,"
					+getColumnName(classObject, "lastModificationTime")+"="+CurrentTimeFactory.getCurrentTime()
						+ " where " + getColumnName(classObject, "ID") + " in " + StringUtils.getCommaSeparatedString(departmentIDs);
						
		int numOfAffectedRows = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		if(numOfAffectedRows != departmentIDs.size()){
			throw new RequestFailureException("At least one department has a root designation id, it can not be deleted");
		}	
	}

}