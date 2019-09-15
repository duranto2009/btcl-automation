package lli;
import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectByID;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;
import static util.ModifiedSqlGenerator.insert;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
public class LLIConnectionApplicationDAO {
	
	Class<LLIApplicationInstance> classObject = LLIApplicationInstance.class;
	public void insertLLIConnectionApplication(LLIApplicationType lliConnectionApplication) throws Exception{
		insert(lliConnectionApplication);
	}
	public void insertLLIConnectionApplicationInstance(LLIApplicationInstance lliConnectionApplicationInstance) throws Exception {
		insert(lliConnectionApplicationInstance);
		
	}
	public List<LLIApplicationType> getApplicationTypeList() throws Exception {
		return getAllObjectList(LLIApplicationType.class);
	}
	public LLIApplicationType getApplicationTypeByApplicationTypeID(Long applicationTypeID) throws Exception {
		return getObjectByID(LLIApplicationType.class, applicationTypeID);
	}
	@SuppressWarnings("rawtypes")
	public Collection getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria, LoginDTO loginDTO) throws Exception{
		ResultSet rs = getResultSetBySqlPair(
			new LLIApplicationInstanceConditionBuilder()
			.selectID()
			.fromLLIApplicationInstance()
			.Where()
			.applicationIDEqualsString(searchCriteria.get("applicationType"))
			.clientIDEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
			.clientIDInSqlPair(
					new ClientDTOConditionBuilder()
					.selectClientID()
					.fromClientDTO()
					.Where()
					.loginNameLeftLike(searchCriteria.get("clientName"))
					.getNullableSqlPair()
					)
			.getSqlPair()
			);
				
		
		return getSingleColumnListByResultSet(rs, Long.class);
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getLLIApplicationListByApplicationIDList(List applicationIDList) throws Exception {
		return getAllObjectList(classObject, 
			new LLIApplicationInstanceConditionBuilder()
			.Where()
			.IDIn(applicationIDList)
			.getCondition()
		);
	}
	public LLIApplicationInstance getApplicationInstanecByApplicationInstanceID(Long applicationInstanceID) throws Exception {
		return getObjectByID(LLIApplicationInstance.class, applicationInstanceID);
	}

}
