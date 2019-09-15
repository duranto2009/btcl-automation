package accounting;
import static util.ModifiedSqlGenerator.*;

import java.sql.ResultSet;
import java.util.List;

import login.LoginDTO;
import util.SqlPair;
public class AccountingEntryDAO {
	Class<AccountingEntry> classObject = AccountingEntry.class;
	public void insertAccountingEntry(AccountingEntry accountingEntry) throws Exception{
		insert(accountingEntry);
	}
	public List<CumulativeAccountingEntry> getAccountingEntryListByAccountIDListAndDateRage(
			List<Integer> idList,Long fromDate,Long toDate,Long clientID) throws Exception{
		
		
		return getAllObjectList(classObject,CumulativeAccountingEntry.class, 
												new AccountingEntryConditionBuilder()
												.Where()
												.incidentIDInSqlPair(
														new AccountingIncidentConditionBuilder()
														.selectID()
														.fromAccountingIncident()
														.Where()
														.clientIDEquals(clientID)
														.dateOfOccuranceGreaterThanEquals(fromDate)
														.dateOfOccuranceLessThanEquals(toDate)
														.getNullableSqlPair()
														)
												.accountIDIn(idList)
												.getCondition());
				
		
	}
	
	public CumulativeAccountingEntry getBalanceByAccountIDListAndClientIDAndDate(List<Integer> accountIDList,Long clientID
			,Long date) throws Exception{
		
		if(date == null){
			date = 0L;
		}
		
		AccountingEntryConditionBuilder accountingEntryConditionBuilder = new AccountingEntryConditionBuilder();
		
		String selectPart = "select sum("+accountingEntryConditionBuilder.getDebitColumnName()+"),"
				+"Sum("+accountingEntryConditionBuilder.getCreditColumnName()+") ";
		
		SqlPair sqlPair = accountingEntryConditionBuilder
				.fromAccountingEntry()
				.Where()
				.accountIDIn(accountIDList)
				.incidentIDInSqlPair(
						new AccountingIncidentConditionBuilder()
						.selectID()
						.fromAccountingIncident()
						.Where()
						.dateOfOccuranceLessThan(date)
						.clientIDEquals(clientID)
						.getNullableSqlPair()
						)
				.getSqlPair();
		
		sqlPair.sql = selectPart+sqlPair.sql;
		
		ResultSet rs = getResultSetBySqlPair(sqlPair);
				
		double totalDebit = 0;
		double totoalCredit = 0;
		
		if(rs.next()){
			totalDebit = rs.getDouble(1);
			totoalCredit = rs.getDouble(2);
		}
		
		CumulativeAccountingEntry cumulativeAccountingEntry = new CumulativeAccountingEntry();
		cumulativeAccountingEntry.setCumulativeDebit(totalDebit);
		cumulativeAccountingEntry.setCumulativeCredit(totoalCredit);
		if(AccountType.getAccountTypeByAccountID(accountIDList.get(0)).isDebitAccount()){
			cumulativeAccountingEntry.setBalance(totalDebit-totoalCredit);
		}else{
			cumulativeAccountingEntry.setBalance(totoalCredit-totalDebit);
		}
		
		return cumulativeAccountingEntry;
	}
	
	public List<AccountingEntry> getAccountingEntryByIncidentID(long incidentID) throws Exception{
		return getAllObjectList(classObject, new AccountingEntryConditionBuilder()
				.Where()
				.incidentIDEquals(incidentID)
				.getCondition());
	}
	
	public List<AccountingEntry> getAccountingEntryListByIncidentIDList(List<Long> incidentIDList ) throws Exception{
		return getAllObjectList(classObject, new AccountingEntryConditionBuilder()
				.Where()
				.incidentIDIn(incidentIDList)
				.getCondition());
	}
	
	
}
