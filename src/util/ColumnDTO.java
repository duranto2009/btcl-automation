package util;

import annotation.*;

@TableName("at_cost_chart_col")
public class ColumnDTO implements Comparable<ColumnDTO>{
	@ColumnName("colID")
	@PrimaryKey
	long ID;
	@ColumnName("colIndex")
	int index;
	@ColumnName("colLowerRange")
	int lowerRange;
	@ColumnName("colUpperRange")
	int upperRange;
	@ColumnName("colTableID")
	long tableID;	
	@ColumnName("colIsDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("colLastModificationTime")
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
	public long getTableID() {
		return tableID;
	}
	public void setTableID(long tableID) {
		this.tableID = tableID;
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
		ColumnDTO other = (ColumnDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ColumnDTO [ID=" + ID + ", index=" + index + ", lowerRange="
				+ lowerRange + ", upperRange=" + upperRange + ", tableID="
				+ tableID + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}
	@Override
	public int compareTo(ColumnDTO o) {
		return lowerRange<o.lowerRange?-1:1;
	}
	
}
