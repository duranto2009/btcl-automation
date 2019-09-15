package costConfig;

import common.ModuleConstants;
import common.RequestFailureException;
import connection.DatabaseConnection;
import costConfig.form.CostConfigForm;
import databasemanager.DatabaseManager;
import org.apache.log4j.Logger;
import util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static util.ModifiedSqlGenerator.getPrimaryKeyColumnName;
import static util.SqlGenerator.*;


public class CostConfigDAO {
	Logger logger = Logger.getLogger(getClass());
	Class<TableDTO> classObject = TableDTO.class;
	public void deleteTableDTOByTableID(long tableID) throws Exception{
		Class classObject = TableDTO.class;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject,"isDeleted")+
				"=1 where "+getPrimaryKeyColumnName(classObject)+"="+tableID;
		int numOfAffectedRows = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		if(numOfAffectedRows == 0){
			throw new RequestFailureException("No cost chart found with id "+tableID);
		}
	}
	
	public List<RowDTO> getAllRowList(DatabaseConnection databaseConnection) throws Exception {
		return (List<RowDTO>) SqlGenerator.getAllObjectList(RowDTO.class, databaseConnection);
	}

	public List<ColumnDTO> getAllColumnList(DatabaseConnection databaseConnection) throws Exception {
		return (List<ColumnDTO>) SqlGenerator.getAllObjectList(ColumnDTO.class, databaseConnection);
	}

	public List<CostCellDTO> getAllCellList(DatabaseConnection databaseConnection) throws Exception {

		return (List<CostCellDTO>) SqlGenerator.getAllObjectList(CostCellDTO.class, databaseConnection);
	}

	public List<ColumnDTO> getColumnListByTableID(DatabaseConnection databaseConnection, long tableID)
			throws Exception {
		String tblID = SqlGenerator.getColumnName(ColumnDTO.class, "tableID");
		String tblColIndex = SqlGenerator.getColumnName(ColumnDTO.class, "index");
		String conditionString = " where " + tblID + " = " + tableID + " order by " + tblColIndex;
		return (List<ColumnDTO>) SqlGenerator.getAllObjectList(ColumnDTO.class,databaseConnection,conditionString);
	}

	public List<RowDTO> getRowListByTableID(DatabaseConnection databaseConnection, long tableID) throws Exception {
		String tblID = SqlGenerator.getColumnName(RowDTO.class, "tableID");
		
		String tblRwIndex = SqlGenerator.getColumnName(RowDTO.class, "index");
		String conditionString = " where " + tblID + " = " + tableID + " order by " + tblRwIndex;
		return (List<RowDTO>) SqlGenerator.getAllObjectList(RowDTO.class, databaseConnection,conditionString);
	}

	public List<CostCellDTO> getCellListByTableID(DatabaseConnection databaseConnection, long tableID)
			throws Exception {
		String tblID = SqlGenerator.getColumnName(CostCellDTO.class, "tableID");
		
		return (List<CostCellDTO>) SqlGenerator.getAllObjectList(CostCellDTO.class, databaseConnection,
				" where " + tblID + " = " + tableID);
	}
	
	public List<TableDTO> getActiveCostConfigTableDTOListByDateRangle(long fromDate,long toDate,int moudleID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = TableDTO.class;
		String conditionString = " where ";
		
		return (List<TableDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
	}

	

	public TableDTO getLatestTable(DatabaseConnection databaseConnection, int moduleId, int categoryID ) throws Exception {
		
		String tblColNameActivationDate = SqlGenerator.getColumnName(TableDTO.class, "activationDate");
		String tblColNameModuleID = SqlGenerator.getColumnName(TableDTO.class, "moduleID");
		String tblColNameIsDeleted = SqlGenerator.getColumnName(TableDTO.class, "isDeleted");
		String tblCatID = SqlGenerator.getColumnName( TableDTO.class, "categoryID" );
		
		String conditionString = " WHERE "+tblColNameActivationDate + " < " + System.currentTimeMillis() + " and "
									+ tblColNameModuleID + " = " + moduleId + " and " + tblColNameIsDeleted +" = 0 and "
									+ tblCatID + "=" + categoryID
									+ " order by " + tblColNameActivationDate;
		
		List<TableDTO> list = (List<TableDTO>) SqlGenerator.getAllObjectList(TableDTO.class, databaseConnection, conditionString);
				
		logger.debug(list.size());
		if (list.size() > 0)
			return list.get(list.size() - 1);
		else
			return null;

	}
	
	public ArrayList<TableDTO> getUpcomingTables(DatabaseConnection databaseConnection, int moduleID, int categoryID) throws Exception {

		String tableColumnName = SqlGenerator.getColumnName(TableDTO.class, "activationDate");
		String conditionString = " WHERE "+ tableColumnName + " > " + System.currentTimeMillis() +" and "+getColumnName(TableDTO.class, "isDeleted")+ " = 0 and "
		+getColumnName(TableDTO.class, "moduleID")+"="+moduleID+ " and " + getColumnName(TableDTO.class, "categoryID") + "=" + categoryID+" order by  " + tableColumnName;
		ArrayList<TableDTO> list = (ArrayList<TableDTO>) SqlGenerator.getAllObjectList(TableDTO.class, databaseConnection,conditionString);
		return list;
	}
	public ArrayList<TableDTO> getUpcomingTables(DatabaseConnection databaseConnection, int moduleID) throws Exception {

		String tableColumnName = SqlGenerator.getColumnName(TableDTO.class, "activationDate");
		String conditionString = " WHERE "+ tableColumnName + " > " + System.currentTimeMillis() +" and "+getColumnName(TableDTO.class, "isDeleted")+ " = 0 and "
		+getColumnName(TableDTO.class, "moduleID")+"="+moduleID+" order by  " + tableColumnName;
		ArrayList<TableDTO> list = (ArrayList<TableDTO>) SqlGenerator.getAllObjectList(TableDTO.class, databaseConnection,conditionString);

		return list;
	}

	public DAOResult add(CostConfigForm form, DatabaseConnection databaseConnection) throws Exception {
		DAOResult daoResult = new DAOResult();
		String[] lowerRangeKm = form.getLowerRangeKm();
		String[] lowerRangeMb = form.getLowerRangeMb();
		String[] upperRangeMb = form.getUpperRangeMb();
		String[] upperRangeKm = form.getUpperRangeKm();
		if (upperRangeKm[upperRangeKm.length - 1].equals("~"))
			upperRangeKm[upperRangeKm.length - 1] = "" + Integer.MAX_VALUE;
		if (upperRangeMb[upperRangeMb.length - 1].equals("~"))
			upperRangeMb[upperRangeMb.length - 1] = "" + Integer.MAX_VALUE;
		int[] rowIndex = form.getRowIndex();
		int[] columnIndex = form.getColumnIndex();
		double[] cost = form.getCost();
		int numberOfRows = rowIndex.length;
		int numberOfColumns = columnIndex.length;
		long[] rowIDs = new long[numberOfRows];
		long[] columnIDs = new long[numberOfColumns];
		logger.debug(numberOfColumns);
		logger.debug(numberOfRows);

		try {
			long tableId = DatabaseManager.getInstance().getNextSequenceId("at_cost_chart");
			logger.debug(tableId);
			String query = "insert into at_cost_chart(tblID, tblIsDeleted, tblActivationDate) values(?,?,?)";
			PreparedStatement ps = databaseConnection.getNewPrepareStatement(query);
			ps.setLong(1, tableId);
			ps.setInt(2, 0);
			ps.setLong(3, System.currentTimeMillis());
			ps.executeUpdate();

			for (int index = 0; index < numberOfRows; index++) {
				long rowId = DatabaseManager.getInstance().getNextSequenceId("at_cost_chart_row");
				rowIDs[index] = rowId;
				String sqlRow = "insert into at_cost_chart_row(rwID, rwIndex, rwLowerRange, rwUpperRange, rwTableID, rwIsDeleted, rwLastModificationTime) values(?,?,?,?,?,?,?)";
				ps = databaseConnection.getNewPrepareStatement(sqlRow);
				ps.setLong(1, rowId);
				ps.setInt(2, index);
				ps.setInt(3, Integer.parseInt(lowerRangeMb[index]));

				ps.setInt(4, Integer.parseInt(upperRangeMb[index]));
				ps.setLong(5, tableId);
				ps.setInt(6, 0);
				ps.setLong(7, System.currentTimeMillis());
				ps.executeUpdate();
			}
			for (int index = 0; index < numberOfColumns; index++) {
				long colId = DatabaseManager.getInstance().getNextSequenceId("at_cost_chart_col");
				columnIDs[index] = colId;
				String sqlRow = "insert into at_cost_chart_col(colID, colIndex, colLowerRange, colUpperRange, colTableID, colIsDeleted, colLastModificationTime) values(?,?,?,?,?,?,?)";
				ps = databaseConnection.getNewPrepareStatement(sqlRow);
				ps.setLong(1, colId);
				ps.setInt(2, index);
				ps.setInt(3, Integer.parseInt(lowerRangeKm[index]));

				ps.setInt(4, Integer.parseInt(upperRangeKm[index]));
				ps.setLong(5, tableId);
				ps.setInt(6, 0);
				ps.setLong(7, System.currentTimeMillis());
				ps.executeUpdate();
			}
			int cellIndex = 0;
			for (int index = 0; index < numberOfRows; index++) {
				for (int col_index = 0; col_index < numberOfColumns; col_index++) {
					long cellID = DatabaseManager.getInstance().getNextSequenceId("at_vpn_monthly_charge_cell");
					String sqlRow = "insert into at_vpn_monthly_charge_cell(vmnthcrgID, vmnthcrgChargePerMbps,vmnthcrgRowID, vmnthcrgColID, vmnthcrgTableID, vmnthcrgIsDeleted, vmnthcrgLastModificationTime) values(?,?,?,?,?,?,?)";
					ps = databaseConnection.getNewPrepareStatement(sqlRow);
					ps.setLong(1, cellID);
					ps.setDouble(2, cost[cellIndex++]);
					ps.setLong(3, rowIDs[index]);

					ps.setLong(4, columnIDs[col_index]);
					ps.setLong(5, tableId);
					ps.setInt(6, 0);
					ps.setLong(7, System.currentTimeMillis());
					ps.executeUpdate();
				}
			}

		} catch (Exception e) {
			logger.fatal("Error inside CostConfig DAO:" + e);
		}

		daoResult.setResult("", true, DAOResult.DONE);
		return daoResult;
	}

	public void insertTable(TableDTO tableDTO, DatabaseConnection databaseConnection) {
		try {
			SqlGenerator.insert(tableDTO, TableDTO.class, databaseConnection, false);
		} catch (Exception e) {

			logger.fatal("Error Inside Insert Table  Method of cost config DAO" + e);
		}
	}
	public boolean getTableOfSpecificActivationDate(long activationDate, DatabaseConnection databaseConnection, int moduleID) throws Exception{
		Class classObject = TableDTO.class;
		String sql = "Select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "activationDate")+"="+activationDate
				
				+" and " + getColumnName(classObject, "moduleID") + " = " + moduleID + " and " + 
				getColumnName(classObject, "isDeleted")
				+ "=0";
		ResultSet rs = databaseConnection.getNewPrepareStatement(sql).executeQuery();
		return rs.next();
	}
	public boolean getTableOfSpecificActivationDate(long activationDate, DatabaseConnection databaseConnection, int moduleID, int categoryID) throws Exception{
		Class classObject = TableDTO.class;
		String sql = "Select 1 from "+getTableName(classObject)+" where "+getColumnName(classObject, "activationDate")+"="+activationDate
				
				+" and " + getColumnName(classObject, "moduleID") + " = " + moduleID + " and " + 
				getColumnName(classObject, "categoryID") + " = " + categoryID + " and " + getColumnName(classObject, "isDeleted")
				+ "=0";
		ResultSet rs = databaseConnection.getNewPrepareStatement(sql).executeQuery();
		return rs.next();
	}
	public TableDTO getTableDTOByTableID(Long tableID)throws Exception{
		return (TableDTO) ModifiedSqlGenerator.getObjectByID(TableDTO.class, tableID);
	}
	public void insertRow(RowDTO rowDTO, DatabaseConnection databaseConnection) {
		try {
			SqlGenerator.insert(rowDTO, RowDTO.class, databaseConnection, false);
		} catch (Exception e) {

			logger.fatal("Error Inside Insert Row Method of cost config DAO" + e);
		}
	}

	public void insertColumn(ColumnDTO columnDTO, DatabaseConnection databaseConnection) {
		try {
			SqlGenerator.insert(columnDTO, ColumnDTO.class, databaseConnection, false);
		} catch (Exception e) {

			logger.fatal("Error Inside Insert Column Method of cost config DAO" + e);
		}
	}

	public void insertCell(CostCellDTO cellDTO, DatabaseConnection databaseConnection) {
		try {
			SqlGenerator.insert(cellDTO, CostCellDTO.class, databaseConnection, false);
		} catch (Exception e) {

			logger.fatal("Error Inside Insert Cell Method of cost config DAO" + e);
		}
	}

	public double getCalculatedCostLLI( DatabaseConnection databaseConnection, double mb, double year, int moduleID, int categoryID ) throws Exception {
		
		logger.debug( "Getcalculatedcost called with mb="+mb+", year="+year+", moduleID="+moduleID+", categoryID="+categoryID );
		
		try {
			
			TableDTO tableDTO = getLatestTable(databaseConnection, moduleID, categoryID );
			
			if( tableDTO == null )
				throw new Exception( "No suitable cost chart found" );
			
			long tableID = tableDTO.getTableID();
			
			ArrayList<RowDTO> rowList = ( ArrayList<RowDTO>) getRowListByTableID( databaseConnection, tableID );
			ArrayList<ColumnDTO> columnList = ( ArrayList<ColumnDTO>) getColumnListByTableID( databaseConnection, tableID );
			ArrayList<CostCellDTO> cellList = ( ArrayList<CostCellDTO>) getCellListByTableID( databaseConnection, tableID );
			
			long columnId = -1;
			long rowId = -1;
			
			for (ColumnDTO column : columnList) {
				
				if ( column.getLowerRange() <= year && column.getUpperRange() >= year ) {
					columnId = column.getID();
				}
			}
			
			for( RowDTO row: rowList ) {
				
				if( row.getLowerRange() <= mb && row.getUpperRange() >= mb ) {
					
					rowId = row.getID();
				}
			}
			
			for( CostCellDTO cell: cellList ) {
				
				if( cell.getRowID() == rowId && cell.getColID() == columnId ) {
					
					return cell.getValue();
				}
			}
		}
		catch (Exception e) {
			
			logger.fatal("Error Inside getCalculatedCOst of cost config DAO" + e);
			throw e;
		}
		
		return 0;
	}
	
	/*public double getCalculatedCost(DatabaseConnection databaseConnection, double mb, double km, int moduleID) {
		double cost = 0;

		try {
			long tableID = getLatestTable(databaseConnection, moduleID).getTableID();
			ArrayList<RowDTO> rowList = (ArrayList<RowDTO>) getRowListByTableID(databaseConnection, tableID);
			ArrayList<ColumnDTO> columnList = (ArrayList<ColumnDTO>) getColumnListByTableID(databaseConnection,
					tableID);

			ArrayList<CostCellDTO> cellList = (ArrayList<CostCellDTO>) getCellListByTableID(databaseConnection,
					tableID);
			ArrayList<ColumnDTO> neededColumns = new ArrayList<ColumnDTO>();
			ColumnDTO column = null;
			RowDTO row = null;
			logger.debug("The row list is==== : " + rowList);
			logger.debug("The column list is==== : " + columnList);
			logger.debug("The cell list is==== : " + cellList);
			for (ColumnDTO columnToBeChecked : columnList) {
				if (columnToBeChecked.getLowerRange() <= km) {
					neededColumns.add(columnToBeChecked);
					logger.debug(columnToBeChecked);
					if (columnToBeChecked.getUpperRange() >= km) {
						neededColumns.remove(columnToBeChecked);
						column = columnToBeChecked;
						logger.debug("The column is==== : " + column);

					}
				}

			}
			for (RowDTO rowToBeChecked : rowList) {
				logger.debug("Inside for loop of row: " + rowToBeChecked);
				logger.debug(rowToBeChecked.getLowerRange());
				logger.debug(mb);
				logger.debug(rowToBeChecked.getUpperRange());
				if (rowToBeChecked.getLowerRange() <= mb && rowToBeChecked.getUpperRange() >= mb) {

					row = rowToBeChecked;
					logger.debug("The row is===: " + row);
					break;
				}

			}

			for (CostCellDTO cell : cellList) {
				if (row != null && cell.getRowID() == row.getID()) {

					for (ColumnDTO columnToBeChecked : neededColumns) {
						if (columnToBeChecked.getID() == cell.colID) {
							if (columnToBeChecked.getIndex() != 0) {
								cost += mb * (columnToBeChecked.getUpperRange() - columnToBeChecked.getLowerRange() + 1)
										* cell.value;
								System.out
										.println(
												"===full column added ====" + columnToBeChecked
														+ "\n cost added: " + mb
																* (columnToBeChecked.getUpperRange()
																		- columnToBeChecked.getLowerRange() + 1)
																* cell.value);
							} else {
								cost = mb * cell.value;
								System.out.println("===full column added ====" + columnToBeChecked + "\n cost added: "
										+ mb * cell.value);
							}

						}
						if (column.getID() == cell.colID) {
							if (column.getIndex() == 0) {
								cost += mb * cell.getValue();
								System.out.println("index 0 Column of the cell added cost: " + (mb * cell.getValue()));
							} else {
								cost += mb * (km - column.getLowerRange() + 1) * cell.getValue();
								System.out.println("Column of the cell added cost: "
										+ mb * (km - column.getLowerRange() + 1) * cell.getValue());

							}
						}
					}

				}
			}

		} catch (Exception e) {
			logger.fatal("Error Inside getCalculatedCOst of cost config DAO" + e);
		}

		return cost;

	}*/

	private List<RowDTO> getRowDTOListByTableID(long tableID, DatabaseConnection databaseConnection) throws Exception {
		Class classObject = RowDTO.class;
		String tableIDColumnName = getColumnName(classObject, "tableID");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String conditionString = " where " + tableIDColumnName + " = " + tableID + " and " + isDeletedColumnName
				+ " = 0";
		return (List<RowDTO>) getAllObjectList(classObject, databaseConnection, conditionString);
	}

	private List<ColumnDTO> getColumnDTOListByTableID(long tableID, DatabaseConnection databaseConnection)
			throws Exception {
		Class classObject = ColumnDTO.class;
		String tableIDColumnName = getColumnName(classObject, "tableID");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String conditionString = " where " + tableIDColumnName + " = " + tableID + " and " + isDeletedColumnName
				+ " = 0";
		return (List<ColumnDTO>) getAllObjectList(classObject, databaseConnection, conditionString);
	}

	private List<CostCellDTO> getCostCellDTOByTableID(long tableID, DatabaseConnection databaseConnection)
			throws Exception {
		Class classObject = CostCellDTO.class;
		String tableIDColumnName = getColumnName(classObject, "tableID");
		String isDeletedColumnName = getColumnName(classObject, "isDeleted");
		String conditionString = " where " + tableIDColumnName + " = " + tableID + " and " + isDeletedColumnName
				+ " = 0";
		return (List<CostCellDTO>) getAllObjectList(classObject, databaseConnection, conditionString);
	}

	public CostConfigTableDTO getCostConfigDTOByTableID(long tableID, DatabaseConnection databaseConnection)
			throws Exception {
		CostConfigTableDTO costConfigTableDTO = (CostConfigTableDTO) getObjectByID(CostConfigTableDTO.class, tableID,
				databaseConnection);
		costConfigTableDTO.setColumnDTOs(getColumnDTOListByTableID(tableID, databaseConnection));
		costConfigTableDTO.setRowDTOs(getRowDTOListByTableID(tableID, databaseConnection));
		costConfigTableDTO.setCostCellDTOs(getCostCellDTOByTableID(tableID, databaseConnection));
		return costConfigTableDTO;
	}

	public List<Long> getSuitableCostConfigTableIDListByDateRange(long fromDate, long toDate,
			DatabaseConnection databaseConnection) throws Exception {
		List<Long> IDList = new ArrayList<Long>();
		return IDList;
	}
	
	/*public double getBandWidthCostPerMonth(long tableID, double distance, double bandwidth,
			DatabaseConnection databaseConnection) throws Exception {

		String sql = "select " + getColumnName(VpnMonthlyChargeCell.class, "chargePerMBps") + " from "
				+ getTableName(VpnMonthlyChargeCell.class) + " where "
				+ getColumnName(VpnMonthlyChargeCell.class, "tableID") + " = " + tableID + " and "
				+ getColumnName(VpnMonthlyChargeCell.class, "isDeleted") + " = 0 and "
				+ getColumnName(VpnMonthlyChargeCell.class, "rowID") + " in (select "
				+ getColumnName(RowDTO.class, "ID") + " from " + getTableName(RowDTO.class) + " where "
				+ getColumnName(RowDTO.class, "tableID") + " = " + tableID + " and " + bandwidth + "<="
				+ getColumnName(RowDTO.class, "upperRange") + " and " + bandwidth + ">= "
				+ getColumnName(RowDTO.class, "lowerRange") + " and " + getColumnName(RowDTO.class, "isDeleted")
				+ " = 0)" + " and " + getColumnName(VpnMonthlyChargeCell.class, "columnID") + " in (select "
				+ getColumnName(ColumnDTO.class, "ID") + " from " + getTableName(ColumnDTO.class) + " where "
				+ getColumnName(ColumnDTO.class, "tableID") + " = " + tableID + " and " + distance + ">="
				+ getColumnName(ColumnDTO.class, "lowerRange") + " and " + distance + "<= "
				+ getColumnName(ColumnDTO.class, "upperRange") + " and " + getColumnName(ColumnDTO.class, "isDeleted")
				+ " = 0)";

		logger.debug("sql:" + sql);
		ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);

		if (!rs.next()) {
			throw new RequestFailureException("No suitable charge found");
		}
		return rs.getDouble(1) * bandwidth;
	}*/
	
	public TableDTO getActiveTableByDateAndModuleIDAndCategoryID(long date,int moduleID,long categoryID) throws Exception{
		Class<TableDTO> classObject = TableDTO.class;
		String conditionString = " where "+getColumnName(classObject, "activationDate")+"<="+date
				+" and "+getColumnName(classObject, "moduleID")+" = "+moduleID+ " and "+getColumnName(classObject, "isDeleted")+" = 0"
				+" and "+ getColumnName(classObject, "categoryID")+"="+categoryID+" order by "+ getColumnName(classObject, "activationDate")+" desc limit 1";
		List<TableDTO> tableDTOList = ModifiedSqlGenerator.getAllObjectList(classObject, conditionString);
		if( tableDTOList.isEmpty()){
			throw new RequestFailureException("No cost chart found at time "+TimeConverter.getDateTimeStringFromDateTime(date)+" for module "+ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID) + " and CategoryID " + categoryID);
		}
		return 	tableDTOList.get(0);
	}
	
	public List<TableDTO> getActiveTableByDateRangeAndModuleIDAndCategoryID(long fromDate,long toDate
			,int moduleID,int categoryID) throws Exception{
		
		String conditionString = " where "+getColumnName(classObject, "activationDate")+">="+fromDate+" and "
				+getColumnName(classObject, "activationDate")+"<="+toDate
				+" and "+getColumnName(classObject, "moduleID")+" = "+moduleID
				+" and "+getColumnName(classObject, "categoryID")+" = "+categoryID
				+ " and "+getColumnName(classObject, "isDeleted")+" = 0"
				+" order by "+ getColumnName(classObject, "activationDate");
		return ModifiedSqlGenerator.getAllObjectList(classObject, conditionString);
	}

	public void deleteRowDTOsByTableID(long tableID) throws Exception {
		// TODO Auto-generated method stub
		Class classObject = RowDTO.class;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject,"isDeleted")+
				"=1 where "+getColumnName(classObject,"tableID")+"="+tableID;
		int numOfAffecedRows = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		if(numOfAffecedRows == 0){
			throw new RequestFailureException("No RowDTO found with tableID "+tableID);
		}
	}

	public void deleteColumnDTOsByTableID(long tableID) throws Exception {
		// TODO Auto-generated method stub
		Class classObject = ColumnDTO.class;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject,"isDeleted")+
				"=1 where "+getColumnName(classObject,"tableID")+"="+tableID;
		int numOfAffecedRows = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		if(numOfAffecedRows == 0){
			throw new RequestFailureException("No ColumnDTO found with tableID "+tableID);
		}
	}

	public void deleteCostCellDTOsByTableID(long tableID) throws Exception {
		// TODO Auto-generated method stub
		Class classObject = CostCellDTO.class;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject,"isDeleted")+
				"=1 where "+getColumnName(classObject,"tableID")+"="+tableID;
		int numOfAffecedRows = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		if(numOfAffecedRows == 0){
			throw new RequestFailureException("No CostCellDTO found with tableID "+tableID);
		}
	}

	public TableDTO getLatestTableWithCategoryID(DatabaseConnection databaseConnection, int moduleID, int categoryID) throws Exception {
		// TODO Auto-generated method stub
		String tblColNameActivationDate = SqlGenerator.getColumnName(TableDTO.class, "activationDate");
		String tblColNameModuleID = SqlGenerator.getColumnName(TableDTO.class, "moduleID");
		String tblColNameIsDeleted = SqlGenerator.getColumnName(TableDTO.class, "isDeleted");
		String tblColNameCategoryID = SqlGenerator.getColumnName(TableDTO.class, "categoryID");
		String conditionString = " WHERE "+tblColNameActivationDate + " < " + System.currentTimeMillis() + " and "
									+ tblColNameModuleID + " = " + moduleID + " and " + tblColNameIsDeleted +" = 0 " 
									+ " and " + tblColNameCategoryID + " = " + categoryID + " order by " + tblColNameActivationDate;
		List<TableDTO> list = (List<TableDTO>) SqlGenerator.getAllObjectList(TableDTO.class, databaseConnection, conditionString);
				
		logger.debug(list.size());
		if (list.size() > 0)
			return list.get(list.size() - 1);
		else
			return null;
	}

	public List<CostCellDTO> getCellListByTableIDSyncWithRowAndColIndex(long tableID, List<RowDTO> rowList,
			List<ColumnDTO> columnList) {
		// TODO Auto-generated method stub
		return null;
	}


}
