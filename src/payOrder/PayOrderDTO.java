package payOrder;

import java.util.HashMap;
import java.util.Map;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import common.payment.constants.PaymentConstants;

@TableName("at_payment_draft")
public class PayOrderDTO {
	
	public static int DRAFT_TYPE_PAY_ORDER = PaymentConstants.PAYMENT_TYPE_PAYORDER;
	public static int DRAFT_TYPE_CHECK = PaymentConstants.PAYMENT_TYPE_CHECK;
	public static Map<Integer, String> MapOfDraftTypeToDraftTypeID = new HashMap<>();
	static{
		MapOfDraftTypeToDraftTypeID.put(DRAFT_TYPE_PAY_ORDER, "Pay Order");
		MapOfDraftTypeToDraftTypeID.put(DRAFT_TYPE_CHECK, "Check");
	}
	@PrimaryKey
	@ColumnName("payDraftID")
	long ID;
	@ColumnName("payDraftTotalAmount")
	double payDraftTotalAmount;
	@ColumnName("payDraftType")
	int payDraftType;
	@ColumnName("payDraftNumber")
	String payDraftNumber;
	@ColumnName("payDraftBankID")
	int payDraftBankID;
	@ColumnName("payDraftBranchName")
	String payDraftBranchName;
	@ColumnName("payDraftPaymentDate")
	long payDraftPaymentDate;
	@ColumnName("payDraftRemainingAmount")
	double payDraftRemainingAmount;
	@ColumnName("payDraftClientID")
	long payDraftClientID;
	@ColumnName("payDraftModuleID")
	int payDraftModuleID;
	@ColumnName("payIsDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("payLastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public double getPayDraftTotalAmount() {
		return payDraftTotalAmount;
	}
	public void setPayDraftTotalAmount(double payDraftTotalAmount) {
		this.payDraftTotalAmount = payDraftTotalAmount;
	}
	public int getPayDraftType() {
		return payDraftType;
	}
	public void setPayDraftType(int payDraftType) {
		this.payDraftType = payDraftType;
	}
	public String getPayDraftNumber() {
		return payDraftNumber;
	}
	public void setPayDraftNumber(String payDraftNumber) {
		this.payDraftNumber = payDraftNumber;
	}
	public int getPayDraftBankID() {
		return payDraftBankID;
	}
	public void setPayDraftBankID(int payDraftBankID) {
		this.payDraftBankID = payDraftBankID;
	}
	public String getPayDraftBranchName() {
		return payDraftBranchName;
	}
	public void setPayDraftBranchName(String payDraftBranchName) {
		this.payDraftBranchName = payDraftBranchName;
	}
	public long getPayDraftPaymentDate() {
		return payDraftPaymentDate;
	}
	public void setPayDraftPaymentDate(long payDraftPaymentDate) {
		this.payDraftPaymentDate = payDraftPaymentDate;
	}
	public double getPayDraftRemainingAmount() {
		return payDraftRemainingAmount;
	}
	public void setPayDraftRemainingAmount(double payDraftRemainingAmount) {
		this.payDraftRemainingAmount = payDraftRemainingAmount;
	}
	public long getPayDraftClientID() {
		return payDraftClientID;
	}
	public void setPayDraftClientID(long payDraftClientID) {
		this.payDraftClientID = payDraftClientID;
	}
	public int getPayDraftModuleID() {
		return payDraftModuleID;
	}
	public void setPayDraftModuleID(int payDraftModuleID) {
		this.payDraftModuleID = payDraftModuleID;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	@Override
	public String toString() {
		return "PayOrderDTO [ID=" + ID + ", payDraftTotalAmount=" + payDraftTotalAmount + ", payDraftType="
				+ payDraftType + ", payDraftNumber=" + payDraftNumber + ", payDraftBankID=" + payDraftBankID
				+ ", payDraftBranchName=" + payDraftBranchName + ", payDraftPaymentDate=" + payDraftPaymentDate
				+ ", payDraftRemainingAmount=" + payDraftRemainingAmount + ", payDraftClientID=" + payDraftClientID
				+ ", payDraftModuleID=" + payDraftModuleID + ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayOrderDTO other = (PayOrderDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}	
}
