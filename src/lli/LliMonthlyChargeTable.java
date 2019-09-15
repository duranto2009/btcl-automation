package lli;

import java.util.*;

import util.ColumnDTO;
import util.RowDTO;

public class LliMonthlyChargeTable {
	long ID;
	long tableID;
	Date activationDate;
	List<RowDTO> rowList;
	List<ColumnDTO> columnList;
	List<LliMonthlyChargeCell> cellList;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getTableID() {
		return tableID;
	}
	public void setTableID(long tableID) {
		this.tableID = tableID;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	public List<RowDTO> getRowList() {
		return rowList;
	}
	public void setRowList(List<RowDTO> rowList) {
		this.rowList = rowList;
	}
	public List<ColumnDTO> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<ColumnDTO> columnList) {
		this.columnList = columnList;
	}
	public List<LliMonthlyChargeCell> getCellList() {
		return cellList;
	}
	public void setCellList(List<LliMonthlyChargeCell> cellList) {
		this.cellList = cellList;
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
		LliMonthlyChargeTable other = (LliMonthlyChargeTable) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "VpnMonthlyChargeTable [ID=" + ID + ", tableID=" + tableID
				+ ", activationDate=" + activationDate + ", rowList=" + rowList
				+ ", columnList=" + columnList + ", cellList=" + cellList + "]";
	}
	
	
}
