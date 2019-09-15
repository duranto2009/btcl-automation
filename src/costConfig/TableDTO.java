package costConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import common.ModuleConstants;
import common.RequestFailureException;
import util.ColumnDTO;
import util.NumberComparator;
import util.NumberUtils;
import util.RowDTO;
import util.TimeConverter;

@TableName("at_cost_chart")
public class TableDTO {
	@ColumnName("tblID")
	@PrimaryKey
	long tableID;
	@ColumnName("tblIsDeleted")
	boolean isDeleted;
	@ColumnName("tblActivationDate")
	long activationDate;
	@ColumnName("tblModuleID")
	int moduleID;
	@ColumnName("tblCategoryID")
	int categoryID;
	@CurrentTime
	@ColumnName("tblLastModificationTime")
	long lastModificationTime;
	List<RowDTO> rowDTOs = new ArrayList<>();
	List<ColumnDTO> columnDTOs = new ArrayList<>();
	List<CostCellDTO> costCellDTOs= new ArrayList<>();


	Map<Long,TreeMap<Long,CostCellDTO>> mapOfCellDTOToColumnIDToRowDTO = new HashMap<>();
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
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
	public void setCostCellDTOs(List<CostCellDTO> costCellDTO) {
		this.costCellDTOs = costCellDTO;
		initializeCellDTOMap();
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
	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
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
	public long getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(long activationDate) {
		this.activationDate = activationDate;
	}
	
	private void initializeCellDTOMap(){
		for(CostCellDTO costCellDTO : costCellDTOs){
			if(!mapOfCellDTOToColumnIDToRowDTO.containsKey(costCellDTO.getRowID())){
				mapOfCellDTOToColumnIDToRowDTO.put(costCellDTO.getRowID(), new TreeMap<>());
			}
			mapOfCellDTOToColumnIDToRowDTO.get(costCellDTO.getRowID()).put(costCellDTO.getColID(), costCellDTO);
		}
	}
	
	private double getCostCellDTOByRowIDAndColumnID(long rowID,double columnValue,double bandwidth){
		if(!mapOfCellDTOToColumnIDToRowDTO.containsKey(rowID)){
			throw new RequestFailureException("No Cell DTO found with row ID "+rowID);
		}
		
		if(moduleID == ModuleConstants.Module_ID_VPN){
		
			double calculatedCost = 0;
			
			
			
			// sort the columnDTOs
			Collections.sort(columnDTOs);
			
	
			if(columnDTOs.size()==0 || columnDTOs.get(columnDTOs.size()-1).getUpperRange()<columnValue){
				throw new RequestFailureException("This table can not evaluate the cost for distance "+columnValue);
			}
			
			calculatedCost = mapOfCellDTOToColumnIDToRowDTO.get(rowID).get(columnDTOs.get(0).getID()).value*bandwidth;
			
			for(int i=1;i<columnDTOs.size();i++){
				ColumnDTO columnDTO = columnDTOs.get(i);
				if(columnDTO.getLowerRange()>columnValue){
					break;
				}
				
				double effectiveDistance = Math.min(columnDTO.getUpperRange(), columnValue)-columnDTO.getLowerRange()+1;
				double cellValue = mapOfCellDTOToColumnIDToRowDTO.get(rowID).get(columnDTO.getID()).value;
				calculatedCost+= effectiveDistance*bandwidth*cellValue;
				
			}
			
			
			return calculatedCost;
		}else if(moduleID == ModuleConstants.Module_ID_LLI){


			// sort the columnDTOs
			Collections.sort(columnDTOs);
			
			
			for(ColumnDTO columnDTO: columnDTOs){
				if(NumberUtils.isBetween(columnDTO.getLowerRange(), columnValue, columnDTO.getUpperRange())){
					long columnID = columnDTO.getID();
					double cost = mapOfCellDTOToColumnIDToRowDTO.get(rowID).get(columnID).getValue()*bandwidth;
					return cost;
				}
			}
			throw new RequestFailureException(getTableDescription()+" has no entry for bandwidth value "+bandwidth+" and "
			+(moduleID==ModuleConstants.Module_ID_VPN?"distance":"year")+" "+columnValue);
			
		}else{
			throw new RequestFailureException("Invalid moduleID for tableID "+tableID+",moduleID = "+moduleID+",categoryID = "+categoryID);
		}
	}
	
	
	private String getTableDescription(){
		return (moduleID == ModuleConstants.Module_ID_VPN?"vpn cost chart table"
				:(moduleID == ModuleConstants.Module_ID_LLI?"LLI cost chart table":"cost chart table with module ID "+moduleID))+
				" with category ID "+categoryID+" which is activated on "+TimeConverter.getTimeStringFromLong(activationDate);
	}
	
	
	private long getMatchingRowID(double rowValue){
		for(RowDTO rowDTO: rowDTOs){
			
			if(NumberComparator.isBetween(rowDTO.getLowerRange(), rowValue, rowDTO.getUpperRange())){
				return rowDTO.getID();
			}
		}
		throw new RequestFailureException("No matching row found for row value "+rowValue+" in table with tableID "+tableID);
	}
	
	private long getMatchingColumnID(double columnValue){
		for(ColumnDTO columnDTO: columnDTOs){
			if(NumberComparator.isBetween(columnDTO.getLowerRange(), columnValue, columnDTO.getUpperRange())){
				return columnDTO.getID();
			}
		}
		throw new RequestFailureException("No matching column found with column value "+columnValue+" for table with tableID "+tableID);
	}
	
	public double getCostByRowValueAndColumnValue(double rowValue,double columnValue){
		
		long matchingRowID = getMatchingRowID(rowValue);
		double cost = getCostCellDTOByRowIDAndColumnID(matchingRowID, columnValue, rowValue);
		
		Logger.getLogger(TableDTO.class).info((moduleID == ModuleConstants.Module_ID_VPN?
				"Vpn ":"LLI with category "+ categoryID) +" cost chart activated at "+TimeConverter.getTimeStringFromLong(activationDate)
				+" returning cost value "+cost+" for row "+rowValue+" and column value "+columnValue);
		
		
		return cost;
	}
	
	public double getCostAfterAggregationByNewBandwidthAndAggregatedBandwidthAndColumnValue(double newBandwidth, double aggregatedBandwidth, double columnValue) {
		long matchingRowID = getMatchingRowID(aggregatedBandwidth);
		double cost = getCostCellDTOByRowIDAndColumnID(matchingRowID, columnValue, newBandwidth);
		return cost;
	}
	
	@Override
	public String toString() {
		return "TableDTO [tableID=" + tableID + ", isDeleted=" + isDeleted + ", activationDate=" + activationDate
				+ ", moduleID=" + moduleID + ", categoryID=" + categoryID + ", lastModificationTime="
				+ lastModificationTime + ", rowDTOs=" + rowDTOs + ", columnDTOs=" + columnDTOs + ", costCellDTOs="
				+ costCellDTOs + ", mapOfCellDTOToColumnIDToRowDTO=" + mapOfCellDTOToColumnIDToRowDTO + "]";
	}
}
