package accounting;

public class CumulativeAccountingEntry extends AccountingEntry{
	double balance;
	double cumulativeDebit;
	double cumulativeCredit;
	long dateOfRecord;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCumulativeDebit() {
		return cumulativeDebit;
	}

	public void setCumulativeDebit(double cumulativeDebit) {
		this.cumulativeDebit = cumulativeDebit;
	}

	public double getCumulativeCredit() {
		return cumulativeCredit;
	}

	public void setCumulativeCredit(double cumulativeCredit) {
		this.cumulativeCredit = cumulativeCredit;
	}

	public long getDateOfRecord() {
		return dateOfRecord;
	}

	public void setDateOfRecord(long dateOfRecord) {
		this.dateOfRecord = dateOfRecord;
	}

	@Override
	public String toString() {
		return "CumulativeAccountingEntry [balance=" + balance + ", cumulativeDebit=" + cumulativeDebit
				+ ", cumulativeCredit=" + cumulativeCredit + ", dateOfRecord=" + dateOfRecord + ", ID=" + ID
				+ ", accountID=" + accountID + ", incidentID=" + incidentID + ", debit=" + debit + ", credit=" + credit
				+ "]";
	}
	
	
	
}
