package costConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import annotation.Transactional;
import client.ClientTypeService;
import common.ModuleConstants;
import common.RequestFailureException;
import connection.DatabaseConnection;
import costConfig.form.CostConfigForm;
import databasemanager.DatabaseManager;
import util.ColumnDTO;
import util.DAOResult;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.RowDTO;
import util.TimeConverter;
import util.TransactionType;

public class CostConfigService {
	static Logger logger = Logger.getLogger(CostConfigService.class);
	CostConfigDAO costConfigDAO = new CostConfigDAO();
	public DAOResult add(CostConfigForm form) throws Exception {
		DAOResult daoResult = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			daoResult = new CostConfigDAO().add(form, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return daoResult;
	}









	
	public TableDTO getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(long fromTime,int moduleID,long categoryID, DatabaseConnection databaseConnection) throws Exception{
		TableDTO tableDTO = costConfigDAO.getActiveTableByDateAndModuleIDAndCategoryID(fromTime, moduleID,categoryID);

		List<RowDTO> rowDTOs = costConfigDAO.getRowListByTableID(databaseConnection, tableDTO.getTableID());
		tableDTO.setRowDTOs(rowDTOs);
		
		List<ColumnDTO> columnDTOs = costConfigDAO.getColumnListByTableID(databaseConnection, tableDTO.getTableID());
		tableDTO.setColumnDTOs(columnDTOs);
		
		List<CostCellDTO> cellDTOs = costConfigDAO.getCellListByTableID(databaseConnection, tableDTO.getTableID());
		tableDTO.setCostCellDTOs(cellDTOs);
		
		return tableDTO;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public TableDTO getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(long fromTime,int moduleID,long categoryID) throws Exception{
		return getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(fromTime, moduleID,categoryID,DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<TableDTO> getActiveCostTableDTOByTimeRangeAndModuleIDAndCategoryID(long fromDate,long toDate
			,int moduleID,int categoryID) throws Exception{
		TableDTO firstActiveTableDTO = costConfigDAO.getActiveTableByDateAndModuleIDAndCategoryID(fromDate, moduleID, categoryID);
		
		if(firstActiveTableDTO == null){
			throw new RequestFailureException("No active cost table found");
		}
		
		List<TableDTO> tableDTOs = costConfigDAO.getActiveTableByDateRangeAndModuleIDAndCategoryID(
				firstActiveTableDTO!=null?firstActiveTableDTO.getActivationDate():0, toDate
						, moduleID,categoryID);
		tableDTOs.add(firstActiveTableDTO);
		for(TableDTO tableDTO: tableDTOs){
			tableDTO.setRowDTOs(costConfigDAO.getRowListByTableID(DatabaseConnectionFactory.getCurrentDatabaseConnection(), tableDTO.getTableID()));
			tableDTO.setColumnDTOs(costConfigDAO.getColumnListByTableID(DatabaseConnectionFactory.getCurrentDatabaseConnection(), tableDTO.getTableID()));
			tableDTO.setCostCellDTOs( costConfigDAO.getCellListByTableID(DatabaseConnectionFactory.getCurrentDatabaseConnection(),tableDTO.getTableID()));
		}
		
		return tableDTOs; 
		
	}



	@Transactional(transactionType = TransactionType.READONLY)
	public List<TableDTO> getRecentAndUpcomingTableIDs(int moduleID, int categoryID)throws Exception {
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		List<TableDTO> recentAndUpcomingTableDTOs = costConfigDAO.getUpcomingTables(databaseConnection
				, moduleID, categoryID);
		
		for(TableDTO tableDTO: recentAndUpcomingTableDTOs){
			tableDTO.setRowDTOs(costConfigDAO.getRowListByTableID(databaseConnection, tableDTO.getTableID()));
			tableDTO.setColumnDTOs(costConfigDAO.getColumnListByTableID(databaseConnection, tableDTO.getTableID()));
			tableDTO.setCostCellDTOs(costConfigDAO.getCellListByTableID(databaseConnection, tableDTO.getTableID()));
		}
		
		
		return recentAndUpcomingTableDTOs;
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public List<TableDTO> getRecentAndUpcomingTableIDs(int moduleID) throws Exception {
		return getRecentAndUpcomingTableIDs(moduleID,1);
	}

	private void validateTableDTO(TableDTO tableDTO){
		if(tableDTO.getActivationDate()<=System.currentTimeMillis()){
			throw new RequestFailureException("Please set a forward date");
		}
		
		List<RowDTO>rowList = tableDTO.getRowDTOs();
		List<ColumnDTO>columnList = tableDTO.getColumnDTOs();
		List<CostCellDTO>costCellList = tableDTO.getCostCellDTOs();
		
		//check all cell is present
		int rowSize = rowList.size();
		int colSize = columnList.size();
		int cellSize = costCellList.size();
		
		if(rowSize*colSize != cellSize){
			throw new RequestFailureException("Please provide cost for all cells");
		}
		
		
		int prevRowUp, prevColUp, i=0;
		prevRowUp = prevColUp = -1;
		
		for(RowDTO r: rowList){
			int low  =  r.getLowerRange();
			int up = r.getUpperRange();
			if(low<0 || up <0){
				throw new RequestFailureException(String.format(
						"Lower Range of row %d must be greater or equal zero", i));
			}
			if(low>up){
				throw new RequestFailureException(String.format(
						"Lower Range in row %d must be lower or equal than upper range of row %d", i, i));
			}
			if(i>0){
				if(prevRowUp+1 != low){
					throw new RequestFailureException(String.format(
							"Lower Range of Row %d must be one greater than upper range of Row %d", i, i-1));
				}
			}
			i++;
			prevRowUp = up;
			
		}
		i = 0;
		for(ColumnDTO c: columnList){
			int low  =  c.getLowerRange();
			int up = c.getUpperRange();
			if(low<0 || up < 0){
				throw new RequestFailureException(String.format(
						"Lower Range of column %d must be greater or equal zero", i));
			}
			if(low>up){
				throw new RequestFailureException(String.format(
						"Lower Range in column %d must be lower or equal than upper range of column %d", i, i));
			}
			if(i>0){
				if(prevColUp+1 != low){
					throw new RequestFailureException(String.format(
							"Lower Range of column %d must be one greater than upper range of column %d", i, i-1));
				}
			}
			i++;
			prevColUp = up;
		}
	}
	
	private void insertCostChartIntoDatabase(TableDTO tableDTO) throws Exception{
		ModifiedSqlGenerator.insert(tableDTO,TableDTO.class,false);
		int numOfRows = tableDTO.getRowDTOs().size();
		int numOfCols = tableDTO.getColumnDTOs().size();
		long []rowIDs = new long[numOfRows];
		long []colIDs = new long[numOfCols];
		int index = 0;
		for(RowDTO rowDTO: tableDTO.getRowDTOs()){
			rowDTO.setTableID(tableDTO.getTableID());
			ModifiedSqlGenerator.insert(rowDTO, RowDTO.class, false);
			rowIDs[index++] = rowDTO.getID();
		}
		index = 0;
		for(ColumnDTO columnDTO: tableDTO.getColumnDTOs()){
			columnDTO.setTableID(tableDTO.getTableID());
			ModifiedSqlGenerator.insert(columnDTO, ColumnDTO.class, false);
			colIDs[index++] = columnDTO.getID();
		}
//		for(CostCellDTO cellDTO: tableDTO.getCostCellDTOs()){
//			cellDTO.setTableID(tableDTO.getTableID());
//			ModifiedSqlGenerator.insert(cellDTO, CostCellDTO.class, false);
//		}
		List<CostCellDTO> costCellDTOs = tableDTO.getCostCellDTOs();
		int rwIndex = -1;
		int colIndex = -1;
		CostCellDTO cellDTO = new CostCellDTO();
		for(int i=0;i<costCellDTOs.size();i++){
			rwIndex = (int)rowIDs[i/numOfCols];
			colIndex = (int)colIDs[i%numOfCols];
			cellDTO = costCellDTOs.get(i);
			cellDTO.setTableID(tableDTO.getTableID());
			cellDTO.setRowID(rwIndex);
			cellDTO.setColID(colIndex);
			ModifiedSqlGenerator.insert(cellDTO, CostCellDTO.class, false);
		}
	}
	
	@Transactional
	public void insertCostChartForVpn(TableDTO tableDTO) throws Exception{
		
		//do
		validateTableDTO(tableDTO);
		if(new CostConfigDAO().getTableOfSpecificActivationDate(tableDTO.getActivationDate(), DatabaseConnectionFactory.getCurrentDatabaseConnection(), tableDTO.getModuleID(), 1)){
			throw new RequestFailureException("Already a cost chart exists on "+TimeConverter.getTimeStringFromLong(tableDTO.getActivationDate())+". At first delete the chart to insert.");
		}
		tableDTO.setCategoryID(1);
		insertCostChartIntoDatabase(tableDTO);
		
	}
	
	@Transactional
	public void insertCostChartForLLI(TableDTO tableDTO,int categoryID) throws Exception{
		
		validateTableDTO(tableDTO);
		
		// new logic
		if(new CostConfigDAO().getTableOfSpecificActivationDate(tableDTO.getActivationDate(), DatabaseConnectionFactory.getCurrentDatabaseConnection(), tableDTO.getModuleID(), categoryID)){
			throw new RequestFailureException("Already a cost chart exists on "+TimeConverter.getTimeStringFromLong(tableDTO.getActivationDate())+". At first delete the chart to insert.");
		}
		tableDTO.setCategoryID(categoryID);
		insertCostChartIntoDatabase(tableDTO);
		
	}
	@Transactional
	public TableDTO createTableDTO(CostConfigForm form, int moduleID) throws  Exception  {
		TableDTO tableDTO = null;
		
		String[] lowerRangeMb = form.getLowerRangeMb();
		String[] upperRangeMb = form.getUpperRangeMb();
		String[] lowerRangeKm = form.getLowerRangeKm();
		String[] upperRangeKm = form.getUpperRangeKm();
		
		
		
		int numberOfRows = lowerRangeMb.length;
		int numberOfColumns = lowerRangeKm.length;
		long[] rowIDs = new long[numberOfRows];
		long[] columnIDs = new long[numberOfColumns];
		long[] cellIDs = new long[numberOfRows * numberOfColumns];
		double[] cellValues = form.getCost();
		System.err.println(lowerRangeMb.length+ " "+upperRangeMb.length+
				" "+lowerRangeKm.length+" "+upperRangeKm.length+" "
				+cellValues.length + " "+numberOfColumns+" "+numberOfRows
				+" "+checkRanges(lowerRangeKm, upperRangeKm) + " " + checkRanges(lowerRangeMb, upperRangeMb)
				+" "+checkCellValues(cellValues) + " " + form.getActivationDate());
		
		if (lowerRangeMb.length != upperRangeMb.length || upperRangeKm.length != lowerRangeKm.length
				|| numberOfRows * numberOfColumns != cellValues.length || !checkRanges(lowerRangeKm, upperRangeKm)
				|| !checkRanges(lowerRangeMb, upperRangeMb) || !checkCellValues(cellValues) || form.getActivationDate() == null) {
			//return tableDTO;
			throw new RequestFailureException("Invalid Table Insertion");
		}
		else {
			tableDTO = new TableDTO();
			if(common.StringUtils.isBlank(form.getActivationDate())){
				throw new RequestFailureException("No valid date found");
			}
			long date = TimeConverter.getTimeFromString(form.getActivationDate());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			
			if(calendar.getTimeInMillis() != date){
				throw new RequestFailureException("Not a start day of the month");
			}
			//date = TimeConverter.getStartTimeOfTheDay(date);
			tableDTO.setActivationDate(date);
			tableDTO.setIsDeleted(false);
			tableDTO.setLastModificationTime(System.currentTimeMillis());
			tableDTO.setModuleID(moduleID);
			
			long tableID = tableDTO.getTableID();
			List<RowDTO> rowDTOs = new ArrayList<>();
			List<ColumnDTO> columnDTOs = new ArrayList<>();
			List<CostCellDTO> costCellDTOs = new ArrayList<>();
			
			for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {

				RowDTO rowDTO = new RowDTO();
				rowDTO.setLowerRange(Integer.parseInt(lowerRangeMb[rowIndex]));
				if (!upperRangeMb[rowIndex].equals("~")) {
					rowDTO.setUpperRange(Integer.parseInt(upperRangeMb[rowIndex]));
				} else {
					rowDTO.setUpperRange(Integer.MAX_VALUE);
				}

				rowDTO.setTableID(tableID);
				rowDTO.setDeleted(false);
				rowDTO.setLastModificationTime(System.currentTimeMillis());
				rowDTO.setIndex(rowIndex);

//				costConfigService.insertRow(rowDTO);
				rowIDs[rowIndex] = rowDTO.getID();

				logger.debug(rowDTO);
				rowDTOs.add(rowDTO);

			}
			for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
				ColumnDTO columnDTO = new ColumnDTO();
				columnIDs[columnIndex] = DatabaseManager.getInstance().getNextSequenceId("at_cost_chart_col");
				// columnDTO.setID(columnIDs[columnIndex]);
				columnDTO.setLowerRange(Integer.parseInt(lowerRangeKm[columnIndex]));
				columnDTO.setUpperRange(Integer.parseInt(upperRangeKm[columnIndex]));
				columnDTO.setTableID(tableID);
				columnDTO.setDeleted(false);
				columnDTO.setLastModificationTime(System.currentTimeMillis());
				columnDTO.setIndex(columnIndex);
				logger.debug(columnDTO);
				columnIDs[columnIndex] = columnDTO.getID();
				columnDTOs.add(columnDTO);
			}
			for (int cellIndex = 0; cellIndex < cellIDs.length; cellIndex++) {
				CostCellDTO cellDTO = new CostCellDTO();
				cellIDs[cellIndex] = DatabaseManager.getInstance().getNextSequenceId("at_vpn_monthly_charge_cell");
				cellDTO.setValue(cellValues[cellIndex]);
				cellDTO.setColID(columnIDs[cellIndex % numberOfColumns]);
//				int cellRowIndex = (int) rowIDs[cellIndex / numberOfColumns];
//				System.err.println(cellRowIndex);
				cellDTO.setTableID(tableID);
				cellDTO.setIsDeleted(false);
				cellDTO.setLastModificationTime(System.currentTimeMillis());
				cellDTO.setRowID(rowIDs[cellIndex / numberOfColumns]);
				logger.debug(cellDTO);
				costCellDTOs.add(cellDTO);
				
			}
			tableDTO.setRowDTOs(rowDTOs);
			tableDTO.setColumnDTOs(columnDTOs);
			tableDTO.setCostCellDTOs(costCellDTOs);
		}
		return tableDTO;
	}
	private boolean checkCellValues(double[] cellValues) {
		for (double cellValue : cellValues) {
			if (cellValue <= 0)
				return false;
		}
		return true;
	}

	public boolean checkRanges(String[] lowerRange, String[] upperRange) {
		if (!checkContinuity(lowerRange, upperRange)) {
			return false;
		}
		for (int index = 0; index < lowerRange.length; index++) {
			int low = Integer.parseInt(lowerRange[index]);

			int high = 0;
			if (!upperRange[index].equals("~")) {
				high = Integer.parseInt(upperRange[index]);
			}
			else high = Integer.MAX_VALUE;

			if (high<=0)
				return false;
			else if (  low<0 )return false;
			else if( high < low)
			return false;

			for (int secondIndex = 0; secondIndex < lowerRange.length; secondIndex++) {
				if (secondIndex == index)
					continue;
				else {
					if (low >= Integer.parseInt(lowerRange[secondIndex])
							&& low <= Integer.parseInt(upperRange[secondIndex])) {
						return false;
					}
					if (high >= Integer.parseInt(lowerRange[secondIndex])
							&& high <= Integer.parseInt(upperRange[secondIndex])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkContinuity(String[] lowerRange, String[] upperRange) {
		for (int index = 1; index < lowerRange.length; index++) {
			int lowerEnd = Integer.parseInt(lowerRange[index]);
			int upperLastEnd = Integer.parseInt(upperRange[index - 1]);
			if (lowerEnd - 1 != upperLastEnd)
				return false;
		}
		return true;
	}
	@Transactional
	public void updateCostConfigChartForLLLI(TableDTO tableDTO,int categoryID,long prevTableID) throws Exception{
		deleteCostChartByTableID(prevTableID);
		insertCostChartForLLI(tableDTO, categoryID);
	}
	
	




	public double getCostLLI( double mb, int year, long clientID ) {
		DatabaseConnection databaseConnection = new DatabaseConnection();

		double result = -1;
		logger.debug( "Get cost lli called");
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			int moduleID =  ModuleConstants.Module_ID_LLI;
			logger.debug( "Module id="+moduleID+", client Id="+ clientID );
			
			int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID(moduleID, clientID );
			
			logger.debug( "categoryID="+categoryID );
			
			result = new CostConfigDAO().getCalculatedCostLLI( databaseConnection, mb, year, moduleID, categoryID );
			databaseConnection.dbTransationEnd();

		} catch (Exception ex) {
			logger.debug("error inside getLatest Table DTO method");
			
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("error inside getLatest Table DTO method");
			}
		} finally {
			databaseConnection.dbClose();
		}
		return result;
	}

	@Transactional
	public void deleteCostChartByTableID(long tableID) throws Exception {
		TableDTO tableDTO = costConfigDAO.getTableDTOByTableID(tableID);
		if(tableDTO==null || tableDTO.isDeleted){
			throw new RequestFailureException("No chart found with id "+tableID);
		}
		if(tableDTO.getActivationDate()<System.currentTimeMillis()){
			throw new RequestFailureException("The chart is already activated.So it can not be deleted");
		}
		
		costConfigDAO.deleteTableDTOByTableID(tableID);
		costConfigDAO.deleteRowDTOsByTableID(tableID);
		costConfigDAO.deleteColumnDTOsByTableID(tableID);
		costConfigDAO.deleteCostCellDTOsByTableID(tableID);
		
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public TableDTO getLatestTableWithCategoryID(int moduleID, long categoryID) throws Exception{
		return getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), moduleID, categoryID);
	}

	public Double getMRCRate(
	        long date,
	        int moduleID,
            long categoryID,
            double mb,
            boolean isLongTerm ) throws Exception
	{

        TableDTO tableDTO = getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(
                System.currentTimeMillis(),
                moduleID,
                categoryID);

		long columnId = -1;
		long rowId = -1;
        //initially regular was 1-4 yrs and long term was more than 5. Here 10 is taken to compare
		int year = isLongTerm ? 10 : 1;

		for (ColumnDTO column : tableDTO.getColumnDTOs()) {
            if ( column.getLowerRange() <= year && column.getUpperRange() >= year ) {
				columnId = column.getID();
				break;
			}
		}

		for( RowDTO row: tableDTO.getRowDTOs() ) {
            if( row.getLowerRange() <= mb && row.getUpperRange() >= mb ) {
                rowId = row.getID();
				break;
			}
		}

		for( CostCellDTO cell: tableDTO.getCostCellDTOs() ) {
			if( cell.getRowID() == rowId && cell.getColID() == columnId ) {
                return cell.getValue();
			}
		}

		return 0.0;
	}

	public int getTotalCategory(int moduleID) {
		// TODO Auto-generated method stub
		// TODO need to implement a repository right now for testing purpose 
		// TODO this function will return a fixed number of category
		
		return 4;
	}

	/*@Transactional(transactionType=util.TransactionType.READONLY)
	public double getBandwidthCostOfVpn(long date,double bandwidth,double distance) throws Exception{
		TableDTO tableDTO = costConfigDAO.getActiveTableByDateAndModuleID(date, ModuleConstants.Module_ID_VPN);
		return costConfigDAO.getBandWidthCostPerMonth(tableDTO.getTableID(), distance, bandwidth, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}*/
/*
	public static void main(String args[]) throws Exception
	{
		CostConfigService costConfigService = ServiceDAOFactory.getService(CostConfigService.class);
		TableDTO tableDTO = costConfigService.getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis());
		double cost = tableDTO.getCostByRowValueAndColumnValue(8, 50);
		logger.debug("cost " + cost);
	}*/
}
