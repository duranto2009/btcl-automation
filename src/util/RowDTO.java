package util;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_cost_chart_row")
public class RowDTO {
	
	@ColumnName("rwID")
	@PrimaryKey
	long ID;
	@ColumnName("rwIndex")
	int index;
	@ColumnName("rwLowerRange")
	int lowerRange;
	@ColumnName("rwUpperRange")
	int upperRange;
	@ColumnName("rwTableID")
	long tableID;
	
	@ColumnName("rwIsDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("rwLastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getLowerRange() {
		return lowerRange;
	}
	public void setLowerRange(int lowerRange) {
		this.lowerRange = lowerRange;
	}
	public int getUpperRange() {
		return upperRange;
	}
	public void setUpperRange(int upperRange) {
		this.upperRange = upperRange;
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
	
	public long getTableID() {
		return tableID;
	}
	public void setTableID(long tableID) {
		this.tableID = tableID;
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
		RowDTO other = (RowDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RowDTO [ID=" + ID + ", index=" + index + ", lowerRange="
				+ lowerRange + ", upperRange=" + upperRange + ", isDeleted="
				+ isDeleted + ", lastModificationTime=" + lastModificationTime
				+ "]";
	}
	
	
}
