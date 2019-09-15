package common.bill;

import annotation.*;

@TableName("at_bank_transaction")
public class BankTransactionHistory {
	public static final int BANK_PAYMENT = 1;
	public static final int PAYMENT_CANCEL = 2;
	@PrimaryKey
	@ColumnName(value = "btID",editable = false)
	long ID;
	@ColumnName(value = "btBillID",editable = false)
	long billID;
	@ColumnName(value = "btTime",editable = false)
	long time;
	@ColumnName(value = "btBankCode",editable = false)
	String bankCode;
	@ColumnName(value = "btBranchCode",editable = false)
	String branchCode;
	@ColumnName(value = "btTxType",editable = false)
	int txType;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getBillID() {
		return billID;
	}
	public void setBillID(long billID) {
		this.billID = billID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public int getTxType() {
		return txType;
	}
	public void setTxType(int txType) {
		this.txType = txType;
	}
}

