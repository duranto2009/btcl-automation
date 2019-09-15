package costConfig.form;
import java.util.*;

import org.apache.struts.action.ActionForm;

import util.*;


public class CostConfigForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<RowDTO> rowList;
	public List<ColumnDTO> columnList;
	public int tableID;
	public String [] lowerRangeMb;
	public String [] upperRangeMb;
	public String [] lowerRangeKm;
	public String [] upperRangeKm;
	public double[] cost;
	public int [] rowIndex;
	public int [] columnIndex;
	public long rowIDs[];
	public long columnIDs[];
	public long [] cellIDs;
	String activationDate;
	
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public long[] getCellIDs() {
		return cellIDs;
	}
	public void setCellIDs(long[] cellIDs) {
		this.cellIDs = cellIDs;
	}
	public long[] getRowIDs() {
		return rowIDs;
	}
	public void setRowIDs(long[] rowIDs) {
		this.rowIDs = rowIDs;
	}
	public long[] getColumnIDs() {
		return columnIDs;
	}
	public void setColumnIDs(long[] columnIDs) {
		this.columnIDs = columnIDs;
	}
	public int getTableID() {
		return tableID;
	}
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}
	public RowDTO geRowDTO(int index){
		return rowList.get(index);
	}
	public ColumnDTO getColumnDTO(int index){
		return columnList.get(index);
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

	public int[] getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int[] rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int[] getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int[] columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	
	
	public String[] getUpperRangeMb() {
		return upperRangeMb;
	}
	public void setUpperRangeMb(String[] upperRangeMb) {
		this.upperRangeMb = upperRangeMb;
	}

	public String[] getLowerRangeMb() {
		return lowerRangeMb;
	}
	public void setLowerRangeMb(String[] lowerRangeMb) {
		this.lowerRangeMb = lowerRangeMb;
	}
	public String[] getLowerRangeKm() {
		return lowerRangeKm;
	}
	public void setLowerRangeKm(String[] lowerRangeKm) {
		this.lowerRangeKm = lowerRangeKm;
	}
	public String[] getUpperRangeKm() {
		return upperRangeKm;
	}
	public void setUpperRangeKm(String[] upperRangeKm) {
		this.upperRangeKm = upperRangeKm;
	}
	public double[] getCost() {
		return cost;
	}
	public void setCost(double[] cost) {
		this.cost = cost;
	}
	@Override
	public String toString() {
		return "CostConfigForm [rowList=" + rowList + ", columnList=" + columnList + ", tableID=" + tableID
				+ ", lowerRangeMb=" + Arrays.toString(lowerRangeMb) + ", upperRangeMb=" + Arrays.toString(upperRangeMb)
				+ ", lowerRangeKm=" + Arrays.toString(lowerRangeKm) + ", upperRangeKm=" + Arrays.toString(upperRangeKm)
				+ ", cost=" + Arrays.toString(cost) + ", rowIndex=" + Arrays.toString(rowIndex) + ", columnIndex="
				+ Arrays.toString(columnIndex) + ", rowIDs=" + Arrays.toString(rowIDs) + ", columnIDs="
				+ Arrays.toString(columnIDs) + ", cellIDs=" + Arrays.toString(cellIDs) + ", activationDate="
				+ activationDate + "]";
	}

	
	
}
