package util;

import java.util.*;

import org.apache.commons.validator.ISBNValidator;

import costConfig.CostCellDTO;

import annotation.*;

@TableName("at_cost_chart")
public class CostConfigTableDTO{
	@PrimaryKey
	@ColumnName("tblID")
	long ID;
	@ColumnName("tblActivationDate")
	Date activationDate;
	
	@ColumnName("tblIsDeleted")
	boolean isDeleted;
	@ColumnName("tblLastModificationTime")
	long lastModificationTime;
	
	List<RowDTO> rowDTOs ;
	List<ColumnDTO> columnDTOs;
	List<CostCellDTO> costCellDTOs;
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
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
	
	public List<RowDTO> getRowDTOs() {
		return rowDTOs;
	}
	public void setRowDTOs(List<RowDTO> rowDTOs) {
		this.rowDTOs = rowDTOs;
	}
	public List<ColumnDTO> getColumnDTOs() {
		return columnDTOs;
	}
	public void setColumnDTOs(List<ColumnDTO> columnDTOs) {
		this.columnDTOs = columnDTOs;
	}
	public List<CostCellDTO> getCostCellDTOs() {
		return costCellDTOs;
	}
	public void setCostCellDTOs(List<CostCellDTO> costCellDTOs) {
		this.costCellDTOs = costCellDTOs;
	}
	
	
	
	public double getMonthlyChargeByDistanceAndBandwidth(double distance,double bandwidth){
		
		long rowID = 0;
		long columnID = 0;
		
		for(RowDTO rowDTO: rowDTOs){
			if(NumberUtils.isBetween((double)rowDTO.getLowerRange(), bandwidth, (double)rowDTO.getUpperRange())){
				rowID = rowDTO.getID();
				break;
			}
		}
		
		for(ColumnDTO columnDTO:columnDTOs){
			if(NumberUtils.isBetween((double)columnDTO.getLowerRange(), distance, (double)columnDTO.getUpperRange())){
				columnID = columnDTO.getID();
				break;
			}
		}
		
		for(CostCellDTO costCellDTO:costCellDTOs){
			if(costCellDTO.getRowID() == rowID && costCellDTO.getColID() == columnID){
				return costCellDTO.getValue();
			}
		}
		
		throw new RuntimeException("No suitable charge found"); 
	}
	
}
