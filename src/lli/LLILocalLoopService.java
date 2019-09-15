package lli;

import java.util.List;

import annotation.DAO;
import annotation.Transactional;
import common.actionProcessor.LLINewConnectionApplicationVerificationProcessor;
import inventory.InventoryService;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.TransactionType;

public class LLILocalLoopService {
	@Service 
	InventoryService inventoryService;
	@DAO
	LLILocalLoopDAO lliLocalLoopDAO;
	public void insertNewLocalLoop(LLILocalLoop lliLocalLoop) throws Exception{
		long lliLocalLoopID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
				.getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLILocalLoop.class));
		lliLocalLoop.setID(lliLocalLoopID);
		insertLocalLoopSnapshot(lliLocalLoop);
	}
	private void insertLocalLoopSnapshot(LLILocalLoop lliLocalLoop) throws Exception{
		lliLocalLoopDAO.insertLocalLoop(lliLocalLoop);
	}
	public void updateLocalLoop(LLILocalLoop lliLocalLoop) throws Exception{
		insertLocalLoopSnapshot(lliLocalLoop);
	}
	public List<LLILocalLoop> getLocalLoopListByOfficeHistoryIDList(List<Long> officeHistoryIDs) throws Exception {
		return lliLocalLoopDAO.getLLiLocalLoopListByOfficeHistoryIDList(officeHistoryIDs);
	}
	@Transactional
	public void deallocateVlansByLLIConnectionHistoryID(long lliConnectionHistoryID) throws Exception{
		List<Long> vlanIDList = lliLocalLoopDAO.getVlanIDListByConnectionHistoryID(lliConnectionHistoryID);
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public List<LLILocalLoop> getLocalLoopListByClientIDAndDateRange(long clientID,long fromDate
			,long toDate) throws Exception{
		return lliLocalLoopDAO.getLLILocalLoopListByClientIDAndDateRange(clientID, fromDate, toDate);
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public List<LLILocalLoop> getLocalLoopListByDateRange(long fromDate,long toDate) throws Exception{
		return lliLocalLoopDAO.getLLILocalLoopListByDateRange(fromDate, toDate);
	}
	public List<Long> getCurrentyOccupiedLocalLoopIDListByLLIConnectionID(long connectionID) throws Exception{
		return lliLocalLoopDAO.getCurrentlyOccupiedVlanIDListByLLIConnectionID(connectionID); 
	}
}
