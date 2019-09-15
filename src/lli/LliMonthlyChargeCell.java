package lli;

import annotation.*;
@TableName("at_lli_monthly_charge_cell")
public class LliMonthlyChargeCell  {
	@PrimaryKey
	@ColumnName("ID")
	long ID;
	@ColumnName("chargePerMbps")
	double chargePerMBps;
	@ColumnName("rowID")
	long rowID;
	@ColumnName("colID")
	long columnID;
	@ColumnName("tableID")
	long tableID;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public double getChargePerMBps() {
		return chargePerMBps;
	}
	public void setChargePerMBps(double chargePerMBps) {
		this.chargePerMBps = chargePerMBps;
	}
	public long getRowID() {
		return rowID;
	}
	public void setRowID(long rowID) {
		this.rowID = rowID;
	}
	public long getColumnID() {
		return columnID;
	}
	public void setColumnID(long columnID) {
		this.columnID = columnID;
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
		LliMonthlyChargeCell other = (LliMonthlyChargeCell) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LliMonthlyChargeCell [ID=" + ID + ", chargePerMBps="
				+ chargePerMBps + ", rowID=" + rowID + ", columnID=" + columnID
				+ ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	
}
