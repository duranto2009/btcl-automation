package accounting;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_accounting_entry")
public class AccountingEntry {
	@PrimaryKey
	@ColumnName("acc_entry_ID")
	long ID;
	@ColumnName("acc_entry_account_ID")
	int accountID;
	@ColumnName("acc_entry_incident_ID")
	long incidentID;
	@ColumnName("acc_entry_debit")
	double debit;
	@ColumnName("acc_entry_credit")
	double credit;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public long getIncidentID() {
		return incidentID;
	}
	public void setIncidentID(long incidentID) {
		this.incidentID = incidentID;
	}
	public double getDebit() {
		return debit;
	}
	public void setDebit(double debit) {
		this.debit = debit;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	@Override
	public String toString() {
		return "AccountingEntry [ID=" + ID + ", accountID=" + accountID + ", incidentID=" + incidentID + ", debit="
				+ debit + ", credit=" + credit + "]";
	}
	
	
}
