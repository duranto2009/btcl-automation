package bank;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_payment_api_client")
public class BankDTO {
	@PrimaryKey
	@ColumnName("id")
	long ID;
	@ColumnName("bankName")
	String bankName;
	@ColumnName("paclBankCode")
	String paclBankCode;
	@ColumnName("paclUserName")
	String paclUserName;
	@ColumnName("paclPassword")
	String paclPassword;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPaclBankCode() {
		return paclBankCode;
	}
	public void setPaclBankCode(String paclBankCode) {
		this.paclBankCode = paclBankCode;
	}
	public String getPaclUserName() {
		return paclUserName;
	}
	public void setPaclUserName(String paclUserName) {
		this.paclUserName = paclUserName;
	}
	public String getPaclPassword() {
		return paclPassword;
	}
	public void setPaclPassword(String paclPassword) {
		this.paclPassword = paclPassword;
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
		return "BankDTO [ID=" + ID + ", bankName=" + bankName + ", paclBankCode=" + paclBankCode + ", paclUserName="
				+ paclUserName + ", paclPassword=" + paclPassword + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
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
		BankDTO other = (BankDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	
}
