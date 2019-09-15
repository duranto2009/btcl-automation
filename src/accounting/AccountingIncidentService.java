package accounting;

import annotation.DAO;
import java.util.*;
import java.util.stream.Collectors;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import requestMapping.Service;
import util.NavigationService;
import util.TransactionType;

public class AccountingIncidentService implements NavigationService {
	@Service
	AccountingEntryService accountingEntryService;
	@DAO
	AccountingIncidentDAO accountingIncidentDAO;
	
	@Transactional
	public long insertAccountingIncident(AccountingIncident accountingIncident) throws Exception{
		
		accountingIncidentDAO.insertAccountingIncident(accountingIncident);
		long incidentID = accountingIncident.getID();
		for(AccountingEntry acc_entry: accountingIncident.getAccountingEntries()) {
			acc_entry.setIncidentID(incidentID);
			accountingEntryService.insertAccountingEntry(acc_entry);
		}
		
		return incidentID;
	}
	
	
	
	public void sortAccountingEntryList(List<AccountingEntry> accountingEntryList) throws Exception{
		Collections.sort(accountingEntryList, new Comparator<AccountingEntry>() {

			@Override
			public int compare(AccountingEntry entry1, AccountingEntry entry2) {

				AccountType type1 = AccountType.getAccountTypeByAccountID(entry1.getAccountID());
				AccountType type2 = AccountType.getAccountTypeByAccountID(entry2.getAccountID());
				
				
				if(type1==null || type2==null){
					return 0;
				}
				
				
				
				
				
				if(type1.isDebitAccount() == type2.isDebitAccount()){
					// both are debit or credit account
					
					return common.StringUtils.compareTowString(type1.getName(), type2.getName());
					
				}if(type1.isCreditAccount() == type2.isCreditAccount()){
					return common.StringUtils.compareTowString(type1.getName(), type2.getName());
				
				}else{
					if(type1.isDebitAccount()){
						return -1;
					}else if(type1.isCreditAccount()){
						return 1;
					}else{
						return 0;
					}
				}
			}
		});
	}
	
	
	public void populateAccountingIncidents(List<AccountingIncident> accountingIncidentList) throws Exception{
		
		
		List<Long> incidentIDList = accountingIncidentList
									.stream()
									.map(AccountingIncident::getID)
									.collect(Collectors.toList());
		
		List<AccountingEntry> accountingEntryList = accountingEntryService.getAccountingEntryListByIncidentIDList(incidentIDList)
				.stream()
				.filter(x->AccountType.getAccountTypeByAccountID(x.getAccountID()) != null)
				.collect(Collectors.toList());
		
		
		
		sortAccountingEntryList(accountingEntryList);
		
		
		Map<Long,List<AccountingEntry>> mapOfAccountingEntryListByIncidentID = accountingEntryList
																				.stream()
																				.collect(Collectors.groupingBy(AccountingEntry::getIncidentID));
		
		
		for(AccountingIncident accountingIncident: accountingIncidentList){
			accountingIncident.setAccountingEntries(mapOfAccountingEntryListByIncidentID.getOrDefault(accountingIncident.getID(), new ArrayList<>()));
		}
	}
	@Transactional(transactionType=TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
	}
	@Transactional(transactionType=TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return accountingIncidentDAO.getAccountingIncidentIDList(searchCriteria);
	}
	@Transactional(transactionType=TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		
		List<AccountingIncident> accountingIncidents = accountingIncidentDAO.getAccountingIncidentListByIDList((List<Long>) recordIDs);
		populateAccountingIncidents(accountingIncidents);
		
		return accountingIncidents;
	}
	
	
	public Map<Long,Long> getMapOfRecordDateToIncidentIDByInclidentIDList(List<Long> incidentIDList) throws Exception{
		return accountingIncidentDAO.getMapOfDateToIncidentIDByIncidentIDList(incidentIDList);
	}
	@Transactional(transactionType=TransactionType.READONLY)
	public AccountingIncident getAccountingIncidentByIncidentID(Long incidentID) throws Exception {
		AccountingIncident accountingIncident = accountingIncidentDAO.getAccountingIncidentByIncidentID(incidentID);
		if(accountingIncident == null) {
			throw new RequestFailureException("No such Accounting Incident Found with ID " + incidentID);
		}
		List<AccountingEntry>acc_entries = accountingEntryService.getAccountingEntryListByIncidentIDList(Arrays.asList(incidentID));
		
		
		sortAccountingEntryList(acc_entries);
		
		accountingIncident.setAccountingEntries(acc_entries);
		
		return accountingIncident;
	}
	
}
