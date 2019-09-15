package crm.dao;

import static util.ModifiedSqlGenerator.*;
import static util.SqlGenerator.getColumnName;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import common.RequestFailureException;
import crm.CrmClientComplainSubjectDTO;
import crm.CrmCommonPoolDTO;
import crm.CrmComplainDTO;
import login.LoginDTO;
import util.ModifiedSqlGenerator;

public class CrmCommonPoolDAO {
	
	public void insertCrmComplain(CrmCommonPoolDTO crmCommonPoolDTO) throws Exception{ 
		insert(crmCommonPoolDTO);
	}
	
	public void updateCrmComplain(CrmCommonPoolDTO crmCommonPoolDTO) throws Exception {
		updateEntity(crmCommonPoolDTO);
	}
	
	public void deleteCrmComplain(CrmCommonPoolDTO crmCommonPoolDTO) throws Exception{
		deleteEntity(crmCommonPoolDTO, CrmCommonPoolDTO.class,false);
	}
	
	public CrmCommonPoolDTO getCrmCommonPoolDTOByCommonPoolID(long id) throws Exception {
		return (CrmCommonPoolDTO) getObjectByID(CrmCommonPoolDTO.class, id);
	}

	public Collection getCrmClientComplainIDListBySearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
		searchCriteria.put("isDeleted", "0");
		

		String str = (String)searchCriteria.get("ID");
		if(str != null){
			str = str.replaceAll("[^\\d.]", "");
			searchCriteria.put("ID", str);
		}
		
		String[] keys = new String[]          {"status","priority", "ID" ,"entityTypeID", "isDeleted", "submissionTo", "submissionFrom", "cleintName"};
		String[] operators = new String[]     {"="	   ,"="       , " = ","="           , "="        , "<=", 			">="            , "IN"};
		String[] dtoColumnNames = new String[]{"status","priority", "ID" ,"entityTypeID", "isDeleted", "submissionTime", "submissionTime", "clientID"};
		Class classObject = CrmCommonPoolDTO.class;
		String fixedCondition = "";
		
		// String employeeNamePartial = (String)searchCriteria.get("employeeName");
		/*
		if(!resolverEmployeeIDList.isEmpty()){
			if(employeeNamePartial != null && employeeNamePartial.length() > 0 ){
				fixedCondition = " "+getColumnName(classObject, "nocEmployeeID")+" IN "+ common.StringUtils.getCommaSeparatedString(resolverEmployeeIDList) ;
			}else{
				fixedCondition = "  ( "+getColumnName(classObject, "nocEmployeeID")+" IN "+ common.StringUtils.getCommaSeparatedString(resolverEmployeeIDList) +
						 " OR "+getColumnName(classObject, "nocEmployeeID") + " IS NULL )";
			}
		}
		
		*/
		
		if(!loginDTO.getIsAdmin()) {
			fixedCondition+="  "+getColumnName(classObject, "clientID")+"="+loginDTO.getAccountID();
		}
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, searchCriteria, fixedCondition);
		
		return IDList;
	}

	public List<CrmCommonPoolDTO> getCrmClientCompalinDTOListByComplainIDList(Collection recordIDs) throws Exception {
		return (List<CrmCommonPoolDTO>)getObjectListByIDList(CrmCommonPoolDTO.class, recordIDs);
	}

	public CrmCommonPoolDTO getClientComplainByComplainID(long complainID) throws Exception {
		return (CrmCommonPoolDTO) getObjectByID(CrmCommonPoolDTO.class, complainID);
	}

	@SuppressWarnings("unchecked")
	public List<CrmClientComplainSubjectDTO> getComplainSubjectByEntityTypeID(String complainSubject,
			Integer entityTypeID) throws Exception {
		Class<?> classObject = CrmClientComplainSubjectDTO.class;
		String conditionString = " WHERE " + 
				( complainSubject != null && complainSubject.length() > 0 ?
				(getColumnName(classObject, "subject")+ " LIKE '%"+ complainSubject +"%' AND " ) : "")+getColumnName(classObject, "entityTypeID")+ " = "+entityTypeID;
		return (List<CrmClientComplainSubjectDTO>) getAllObjectList(classObject, conditionString);
	}

	public void updateBlockedStatus(CrmCommonPoolDTO crmCommonPoolDTO) throws Exception {
		int numberOfAffectedRows = ModifiedSqlGenerator.updateEntityByPropertyList(crmCommonPoolDTO, 
				CrmCommonPoolDTO.class, false, false, new String[]{"isBlocked","lastModificationTime"});
		if(numberOfAffectedRows == 0){
			throw new RequestFailureException("No such complain found with id: " + crmCommonPoolDTO.getID());
		}
	}
}
