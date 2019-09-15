package accounting;
import java.util.*;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import common.ClientDTO;
@TableName("at_accounting_incident")
public class AccountingIncident {
	@PrimaryKey
	@ColumnName("acc_incident_ID")
	long ID;
	@ColumnName("acc_incident_description")
	String description;
	@ColumnName("acc_incident_module_ID")
	int moduleID;
	@ColumnName("acc_incident_client_ID")
	long clientID;
	@ColumnName("acc_incident_date_of_occurance")
	long dateOfOccurance;
	@ColumnName("acc_incident_date_of_record")
	long dateOfRecord;
	List<AccountingEntry> accountingEntries = new ArrayList<>();
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}
	public long getDateOfOccurance() {
		return dateOfOccurance;
	}
	public void setDateOfOccurance(long dateOfOccurance) {
		this.dateOfOccurance = dateOfOccurance;
	}
	public long getDateOfRecord() {
		return dateOfRecord;
	}
	public void setDateOfRecord(long dateOfRecord) {
		this.dateOfRecord = dateOfRecord;
	}
	public List<AccountingEntry> getAccountingEntries() {
		return accountingEntries;
	}
	public void setAccountingEntries(List<AccountingEntry> accountingEntries) {
		this.accountingEntries = accountingEntries;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
