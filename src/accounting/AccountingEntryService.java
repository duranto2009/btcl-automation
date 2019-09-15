package accounting;

import java.util.*;
import java.util.stream.Collectors;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import requestMapping.Service;
import util.NavigationService;
import util.TransactionType;

public class AccountingEntryService{
	@DAO
	AccountingEntryDAO accountingEntryDAO;
	@Service
	AccountingIncidentService accountingIncidentService;
	
	
	private void validateAccountingEntry(AccountingEntry accountingEntry) throws Exception{
		if(!AccountType.isValidAccountID(accountingEntry.getAccountID())){
			throw new RequestFailureException("Invalid Account ID.");
		}
		// check debit credit columns
	}
	
	public void insertAccountingEntry(AccountingEntry accountingEntry) throws Exception{
		validateAccountingEntry(accountingEntry);
		accountingEntryDAO.insertAccountingEntry(accountingEntry);
	}
	private List<CumulativeAccountingEntry> getAccountingEntriesByAccountId(Integer accountId, Long fromDate, Long toDate, Long clientID) throws Exception {
		AccountType accountType = AccountType.getAccountTypeByAccountID(accountId);
		List<AccountType> leafAccountTypes = accountType.getLeafAccountTypeList();

		List<Integer> accountIDList =   leafAccountTypes
				.stream()
				.map(AccountType::getID)
				.collect(Collectors.toList());


		List<CumulativeAccountingEntry> accountingEntries = accountingEntryDAO
				.getAccountingEntryListByAccountIDListAndDateRage(accountIDList, fromDate, toDate,clientID);

		List<Long> incidentIDList = accountingEntries
				.stream()
				.map(CumulativeAccountingEntry::getIncidentID)
				.collect(Collectors.toList());

		Map<Long,Long> mapOfDateToIncidentID = accountingIncidentService.getMapOfRecordDateToIncidentIDByInclidentIDList(incidentIDList);

		CumulativeAccountingEntry lastCumulativeAccountingEntry = accountingEntryDAO.getBalanceByAccountIDListAndClientIDAndDate(accountIDList
				, clientID, fromDate);
		boolean isDebitAccount = accountType.isDebitAccount();

		double totalDebit = lastCumulativeAccountingEntry.getCumulativeDebit();
		double totalCredit = lastCumulativeAccountingEntry.getCumulativeCredit();

		for(CumulativeAccountingEntry cumulativeAccountingEntry: accountingEntries){
			totalDebit +=  cumulativeAccountingEntry.getDebit();
			totalCredit += cumulativeAccountingEntry.getCredit();
			double balance = (isDebitAccount?(totalDebit-totalCredit):(totalCredit-totalDebit));
			cumulativeAccountingEntry.setCumulativeDebit(totalDebit);
			cumulativeAccountingEntry.setCumulativeCredit(totalCredit);
			cumulativeAccountingEntry.setBalance(balance);
			long date = mapOfDateToIncidentID.getOrDefault(cumulativeAccountingEntry.getIncidentID(),0L);
			cumulativeAccountingEntry.setDateOfRecord(date);
		}
		return accountingEntries;
	}
	
	@Transactional(transactionType = TransactionType.READONLY)
	public List<CumulativeAccountingEntry> getAccountingEntryByAccountIDAndDateRange(Integer accountID
			,Long fromDate,Long toDate,Long clientID,LoginDTO loginDTO) throws Exception{

		if(!loginDTO.getIsAdmin()){
			clientID = loginDTO.getAccountID();
		}

		if(accountID == null && clientID == null) {
			return Collections.emptyList();
		}
		if(accountID == null) {
			List<CumulativeAccountingEntry> list = new ArrayList<>();
			for (AccountType firstLevelAccountType : AccountType.getFirstLevelAccountTypes()) {
				list.addAll(getAccountingEntriesByAccountId(firstLevelAccountType.getID(), fromDate, toDate, clientID));
			}
			return list;
		}


		return getAccountingEntriesByAccountId(accountID, fromDate, toDate, clientID);
	}
	
	@Transactional(transactionType = TransactionType.READONLY)
	public List<AccountingEntry> getAccountingEntryListByIncidentIDList(List<Long> incidentIDList) throws Exception{
		return accountingEntryDAO.getAccountingEntryListByIncidentIDList(incidentIDList);
	}

	@Transactional(transactionType = TransactionType.READONLY)
	public double getBalanceByClientIDAndAccountID(long clientID,int accountID) throws Exception{
		
		AccountType accountType = AccountType.getAccountTypeByAccountID(accountID);
		if(accountType == null){
			throw new RequestFailureException("No account found with account ID "+accountID);
		}
		List<AccountType> childAccountTypeList = accountType.getLeafAccountTypeList(); 
		List<Integer> accountIDList = childAccountTypeList
				.stream()
				.map(AccountType::getID)
				.collect(Collectors.toList());
		
		CumulativeAccountingEntry cumulativeAccountingEntry = accountingEntryDAO.getBalanceByAccountIDListAndClientIDAndDate(accountIDList
				, clientID, System.currentTimeMillis());
	
		return cumulativeAccountingEntry.getBalance();
	}
	
}
