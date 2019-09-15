package accounting;
import java.text.DecimalFormat;
import java.util.*;

import common.RequestFailureException;
import util.ModifiedSqlGenerator;
import util.NumberComparator;
import util.NumberUtils;
public class AccountingIncidentBuilder {
	private AccountingIncident accountingIncident = new AccountingIncident();
	private List<AccountingEntry> accountingEntries = new ArrayList<>();
	
	
	public AccountingIncidentBuilder(){
		
	}
	
	public AccountingIncidentBuilder clearAccountingEntryList(){
		accountingEntries = new ArrayList<>();
		return this;
	}
	
	public AccountingIncidentBuilder(AccountingIncident accountingIncident){
		this.accountingIncident = accountingIncident;
	}
	
	public AccountingIncidentBuilder description(String description){
		accountingIncident.setDescription(description);
		return this;
	}
	public AccountingIncidentBuilder moduleID(int moduleID){
		accountingIncident.setModuleID(moduleID);;
		return this;
	}
	public AccountingIncidentBuilder clientID(long clientID){
		accountingIncident.setClientID(clientID);;
		return this;
	}
	public AccountingIncidentBuilder dateOfOccurance(long dateOfOccurance){
		accountingIncident.setDateOfOccurance(dateOfOccurance);;
		return this;
	}
	public AccountingIncidentBuilder dateOfRecord(long dateOfRecord){
		accountingIncident.setDateOfRecord(dateOfRecord);
		return this;
	}
	
	public AccountingIncidentBuilder(String description,int moduleID,long clientID,long dateOfOccurance,long dateOfRecord){
		accountingIncident.setDescription(description);
		accountingIncident.setModuleID(moduleID);
		accountingIncident.setClientID(clientID);
		accountingIncident.setDateOfOccurance(dateOfOccurance);
		accountingIncident.setDateOfRecord(dateOfRecord);
	}
	
	public AccountingIncidentBuilder debit(AccountType accountType,double amount){
		
		if(NumberComparator.isEqual(amount, 0.0)){
			return this;
		}
		
		if(amount<0.0){
			throw new RequestFailureException("No negative amount is "
					+ "acceptable for account "+accountType.getName());
		}
		
		
		AccountingEntry accountingEntry = new AccountingEntry();
		accountingEntry.setAccountID(accountType.getID());
		accountingEntry.setCredit(0);
		accountingEntry.setDebit(amount);
		accountingEntries.add(accountingEntry);
		return this;
	}
	
	public AccountingIncidentBuilder credit(AccountType accountType,double amount){
		
		if(NumberComparator.isEqual(amount, 0.0)){
			return this;
		}

		if(amount<0.0){
			throw new RequestFailureException("No negative amount is "
					+ "acceptable for account "+accountType.getName());
		}
		AccountingEntry accountingEntry = new AccountingEntry();
		accountingEntry.setAccountID(accountType.getID());
		accountingEntry.setCredit(amount);
		accountingEntry.setDebit(0);
		accountingEntries.add(accountingEntry);
		return this;
	}
	
	public AccountingIncidentBuilder replaceDescription(String description){
		accountingIncident.setDescription(description);
		return this;
	}
	
	public AccountingIncidentBuilder replaceAccountType(AccountType oldType,AccountType newType){
		for(AccountingEntry accountingEntry : accountingEntries){
			if(accountingEntry.getAccountID() == oldType.getID()){
				accountingEntry.setAccountID(newType.getID());
			}
		}
		
		return this;
	}
	
	public AccountingIncident createAccountingIncident(){
		accountingIncident.setAccountingEntries(accountingEntries);
		checkAccountingIncident(accountingIncident);
		return accountingIncident;
	}
	
	public AccountingIncidentBuilder reverse(){
		for(AccountingEntry accountingEntry: accountingEntries){
			double debit = accountingEntry.getDebit();
			accountingEntry.setDebit(accountingEntry.getCredit());
			accountingEntry.setCredit(debit);
		}
		return this;
	}
	
	public static AccountingIncident reverseAccountingIncident(AccountingIncident accountingIncident){
		checkAccountingIncident(accountingIncident);
		AccountingIncident reverseAccountingIncident = new AccountingIncident();
		
		ModifiedSqlGenerator.populateObjectFromOtherObject(accountingIncident, reverseAccountingIncident, AccountingIncident.class);
		
		for(AccountingEntry accountingEntry: reverseAccountingIncident.getAccountingEntries()){
			double debit = accountingEntry.getDebit();
			accountingEntry.setDebit(accountingEntry.getCredit());
			accountingEntry.setCredit(debit);
		}
		return reverseAccountingIncident;
	}
	
	public static void checkAccountingIncident(AccountingIncident accountingIncident){
		double totalDebit = 0;
		double totalCredit = 0;
		
		for(AccountingEntry accountingEntry: accountingIncident.getAccountingEntries()){
			AccountType accountType = AccountType.getAccountTypeByAccountID(accountingEntry.getAccountID());
			if(accountType == null){
				throw new RequestFailureException("No account type found with accountID "+accountingEntry.getAccountID());
			}
			totalDebit+=accountingEntry.getDebit();
			totalCredit+=accountingEntry.getCredit();
		}
		if(!NumberComparator.isEqual( NumberUtils.formattedValue(totalDebit), NumberUtils.formattedValue(totalCredit))){
			throw new RequestFailureException(String.format("Invalid accounting incident. Total debit and credit does "
					+ "not match. Total debit is %f and credit is %f.",totalDebit,totalCredit));
		}
		
	}
	
}
