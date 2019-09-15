package accounting;
import static util.ModifiedSqlGenerator.*;

import java.sql.ResultSet;
import java.util.*;

import org.apache.log4j.Logger;

import common.ClientDTOConditionBuilder;
import util.TimeConverter;

public class AccountingIncidentDAO {
	
	Class<AccountingIncident> classObject = AccountingIncident.class;
	
	public void insertAccountingIncident(AccountingIncident accountingIncident) throws Exception{
		insert(accountingIncident);
	}
	
	public List<Long> getAccountingIncidentIDList(Hashtable<String, String> hashTable) throws Exception{
		
		ResultSet rs = getResultSetBySqlPair( new AccountingIncidentConditionBuilder()
				.selectID()
				.fromAccountingIncident()
				.Where()
				.clientIDInSqlPair(
						new ClientDTOConditionBuilder()
						.selectClientID()
						.fromClientDTO()
						.Where()
						.loginNameBothLike(hashTable.get("clientName"))
						.isDeleted(false)
						.getNullableSqlPair()
						)
				.dateOfOccuranceLessThan(TimeConverter.getNextDateFromString(   hashTable.get("toDate")))
				.dateOfOccuranceGreaterThanEquals(TimeConverter.getDateFromString(hashTable.get("fromDate")))
				.orderBydateOfOccuranceAsc()
				.getSqlPair());
		
		List<Long> accountinIncidentIDList = getSingleColumnListByResultSet(rs, Long.class);
		return accountinIncidentIDList;
	}
	
	public List<AccountingIncident> getAccountingIncidentListByIDList(List<Long> incidentIDList) throws Exception{
		
		return getAllObjectList(classObject, new AccountingIncidentConditionBuilder()
				.Where()
				.IDIn(incidentIDList)
				.orderBydateOfOccuranceAsc()
				.getCondition()
			);
	}
	
	
	public Map<Long,Long> getMapOfDateToIncidentIDByIncidentIDList(List<Long> incidentIDList) throws Exception{
		Map<Long,Long> mapOfDateToIncidentID = new HashMap<>();
		
		ResultSet rs = getResultSetBySqlPair(
				new AccountingIncidentConditionBuilder()
				.selectID()
				.selectDateOfRecord()
				.fromAccountingIncident()
				.Where()
				.IDIn(incidentIDList)
				.getSqlPair());
		
		
		while(rs.next()){
			long incidentID = rs.getLong(1);
			long date = rs.getLong(2);
			mapOfDateToIncidentID.put(incidentID, date);
		}
		
		
		return mapOfDateToIncidentID;
	}

	public AccountingIncident getAccountingIncidentByIncidentID(Long incidentID) throws Exception {
		return getObjectByID(classObject, incidentID);
	}
	
}
