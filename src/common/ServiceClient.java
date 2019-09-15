package common;

import annotation.*;

@TableName("")
public class ServiceClient {
	@PrimaryKey
	@ColumnName("")
	long ID;
	@ColumnName("")
	long serviceTypeID;
	@ColumnName("")
	long clientAccountID;
	@ColumnName("")
	double advancedPayment;
	@ColumnName("")
	boolean isDeleted;
	@ColumnName("")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getServiceTypeID() {
		return serviceTypeID;
	}
	public void setServiceTypeID(long serviceTypeID) {
		this.serviceTypeID = serviceTypeID;
	}
	public long getClientAccountID() {
		return clientAccountID;
	}
	public void setClientAccountID(long clientAccountID) {
		this.clientAccountID = clientAccountID;
	}
	public double getAdvancedPayment() {
		return advancedPayment;
	}
	public void setAdvancedPayment(double advancedPayment) {
		this.advancedPayment = advancedPayment;
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
		ServiceClient other = (ServiceClient) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ServiceClient [ID=" + ID + ", serviceTypeID=" + serviceTypeID
				+ ", clientAccountID=" + clientAccountID + ", advancedPayment="
				+ advancedPayment + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}
	
}
