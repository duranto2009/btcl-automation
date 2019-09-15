package request;
import annotation.*;

 
@TableName("")
public class CoLocationSpaceAllocationRequest extends CommonRequestDTO{
	@ColumnName("")
	long ID;
	@ColumnName("")
	long regionID;
	@ColumnName("")
	int allocationType; // 0->full , 1->half
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
	public long getRegionID() {
		return regionID;
	}
	public void setRegionID(long regionID) {
		this.regionID = regionID;
	}
	public int getAllocationType() {
		return allocationType;
	}
	public void setAllocationType(int allocationType) {
		this.allocationType = allocationType;
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
		int result = super.hashCode();
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoLocationSpaceAllocationRequest other = (CoLocationSpaceAllocationRequest) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CoLocationSpaceAllocationRequest [ID=" + ID + ", regionID="
				+ regionID + ", allocationType=" + allocationType
				+ ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + "]";
	}
	
}
