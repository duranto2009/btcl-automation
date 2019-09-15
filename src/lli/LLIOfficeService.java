package lli;
import java.util.*;

import annotation.DAO;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

public class LLIOfficeService {
	@DAO
	LLIOfficeDAO lliOfficeDAO;
	@Service
	LLILocalLoopService lliLocalLoopService;
	
	private void insertOfficeInstance(LLIOffice lliOffice) throws Exception{
		lliOfficeDAO.insertNewOfficeInstance(lliOffice);
		for(LLILocalLoop lliLocalLoop: lliOffice.getLocalLoops()){
			lliLocalLoop.setLliOfficeHistoryID(lliOffice.getHistoryID());
			
			if(lliLocalLoop.getID() == 0){
				lliLocalLoopService.insertNewLocalLoop(lliLocalLoop);
			}else{
				lliLocalLoopService.updateLocalLoop(lliLocalLoop);
			}
			
		}
		
	}
	
	
	public long insertNewLLIOffice(LLIOffice lliOffice) throws Exception{
		long officeID = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLIOffice.class));
		lliOffice.setID(officeID);
		insertOfficeInstance(lliOffice);
		return lliOffice.getHistoryID();
	}
	
	long updateLLIOffice(LLIOffice lliOffice) throws Exception{
		insertOfficeInstance(lliOffice);
		return lliOffice.getHistoryID();
	}
	List<LLIOffice> getLLIOfficeListByConnectionHistoryID(long connectionHistoryID) throws Exception {

		return lliOfficeDAO.getLLIOfficeListByConnectionHistoryID(connectionHistoryID);
	} 
	List<LLIOffice> getLLIOfficeListByConnectionHistoryIDList(List<Long> connectionHistoryIDs) throws Exception {

		List<LLIOffice> lliOffices = lliOfficeDAO.getLLIOfficeListByConnectionHistoryIDList(connectionHistoryIDs);		
		return lliOffices;
	}
	
	List<LLIOffice> getLLIOfficeListByClientIDAndDateInstance(long clientID, long fromDate
			, long toDate) throws Exception{
		return lliOfficeDAO.getLLIOfficeListByDateRangeAndClientID(fromDate, toDate, clientID);
	}
	public List<LLIOffice> getLLIOfficeListByDateInstance(long fromDate
			,long toDate) throws Exception{
		return lliOfficeDAO.getLLIOfficeListByDateRange(fromDate, toDate);
	}
	
}
