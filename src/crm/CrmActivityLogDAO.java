package crm;

import common.RequestFailureException;
import util.ModifiedSqlGenerator;

import java.util.Collection;
import java.util.Hashtable;

import static util.ModifiedSqlGenerator.*;


public class CrmActivityLogDAO {
	Class<CrmActivityLog> classObject = CrmActivityLog.class;
	public void insert(CrmActivityLog crmActivityLog) throws Exception{
		ModifiedSqlGenerator.insert(crmActivityLog);
	}
	public CrmActivityLog getCrmActivityLogByLogID(long crmActivityLogID) throws Exception{
		return getObjectByID(classObject, crmActivityLogID);
	}
	public void markActivityLogAsCompleted(CrmActivityLog crmActivityLog) throws Exception{
		int numberOfAffectedRows = updateEntityByPropertyList(crmActivityLog, classObject, false, false, new String[]{"timeOfTakenAction"});
		if(numberOfAffectedRows == 0){
			throw new RequestFailureException("Update failed in activity log with ID "+crmActivityLog.getID());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllCrmActivityLogIDsBySearchCriteria(Hashtable searchCriteria) throws Exception {
		Class classObject = CrmActivityLog.class;
		String []keys= new String[]            {"complainID"	, "actionType"		, 	"previousEmployee"	, "currentEmployee"	 , "clientName"  ,"status"	};
		String []operators = new String[]      {"=" 			, "="				,	"="					, "="				 ,	"IN"		 ,"NULL"	};
		String []dtoColumnNames = new String[] {"crmComplainID"	, "takenActionType"	,	"previousEmployeeID", "currentEmployeeID",	"clientID"   ,"timeOfTakenAction"};
		
		return getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, searchCriteria, "");
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllCrmActivityLogByIDs(Collection recordIDs) throws Exception {
		return (Collection) ModifiedSqlGenerator.getObjectListByIDList(CrmActivityLog.class, recordIDs);
	}
}
