package costConfig;
import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_vpn_monthly_charge_cell")
public class CostCellDTO {
	@ColumnName("vmnthcrgID")
	@PrimaryKey
	long ID;
	@ColumnName("vmnthcrgChargePerMbps")
	double value;
	@ColumnName("vmnthcrgRowID")
	long rowID;
	@ColumnName("vmnthcrgColID")
	long colID;
	@ColumnName("vmnthcrgTableID")
	long tableID;
		
	@ColumnName("vmnthcrgIsDeleted")
	boolean isDeleted;
	@CurrentTime
	@ColumnName("vmnthcrgLastModificationTime")
	long lastModificationTime;
	
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public long getRowID() {
		return rowID;
	}
	public void setRowID(long rowID) {
		this.rowID = rowID;
	}
	public long getColID() {
		return colID;
	}
	public void setColID(long colID) {
		this.colID = colID;
	}
	public long getTableID() {
		return tableID;
	}
	public void setTableID(long tableID) {
		this.tableID = tableID;
	}
	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "CellDTO [ID=" + ID + ", value=" + value + ", rowID=" + rowID + ", colID=" + colID + ", tableID="
				+ tableID + ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime + "]";
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	
	
}
