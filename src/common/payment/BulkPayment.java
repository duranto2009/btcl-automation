package common.payment;

import annotation.ColumnName;
import annotation.TableName;


@TableName("")
public class BulkPayment {
	@ColumnName(value="",editable=false)
	long ID;
	@ColumnName(value="",editable=false)
	Long cashDepositeHistoryID;
	@ColumnName(value = "",editable=false)
	long serviceClientID;
	@ColumnName("")
	Long paymentDate; //nullable
	@ColumnName(value="",editable=false)
	double advancedPayment;
	@ColumnName(value = "",editable=false)
	long issueDate;
	@ColumnName("")
	Long expirationDate; //nullable
	@ColumnName("")
	long lastModificationTime;
	@ColumnName("")
	boolean isDeleted;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Long getCashDepositeHistoryID() {
		return cashDepositeHistoryID;
	}
	public void setCashDepositeHistoryID(Long cashDepositeHistoryID) {
		this.cashDepositeHistoryID = cashDepositeHistoryID;
	}
	public long getServiceClientID() {
		return serviceClientID;
	}
	public void setServiceClientID(long serviceClientID) {
		this.serviceClientID = serviceClientID;
	}
	public Long getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}
	public double getAdvancedPayment() {
		return advancedPayment;
	}
	public void setAdvancedPayment(double advancedPayment) {
		this.advancedPayment = advancedPayment;
	}
	public long getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(long issueDate) {
		this.issueDate = issueDate;
	}
	public Long getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Long expirationDate) {
		this.expirationDate = expirationDate;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public boolean isPaid(){
		return expirationDate != null;
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
		BulkPayment other = (BulkPayment) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BulkPayment [ID=" + ID + ", cashDepositeHistoryID="
				+ cashDepositeHistoryID + ", serviceClientID="
				+ serviceClientID 
				+ ", paymentDate=" + paymentDate + ", advancedPayment="
				+ advancedPayment + ", issueDate=" + issueDate
				+ ", expirationDate=" + expirationDate
				+ ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + "]";
	}
	
}
