package request;

import annotation.*;

@TableName("")
public class ColocationRackAllocationRequestDTO {
	@ColumnName("")
	long ID;
	@ColumnName("")
	long rackID;	
	@ColumnName("")
	double heightInPercentage;
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
	public long getRackID() {
		return rackID;
	}
	public void setRackID(long rackID) {
		this.rackID = rackID;
	}
	public double getHeightInPercentage() {
		return heightInPercentage;
	}
	public void setHeightInPercentage(double heightInPercentage) {
		this.heightInPercentage = heightInPercentage;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}
	
	@Override
	public String toString() {
		return "ColocationRackAllocationRequest [ID=" + ID + ", rackID="
				+ rackID + ", heightInPercentage=" + heightInPercentage
				+ ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + "]";
	}
	
}
