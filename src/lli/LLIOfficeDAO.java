package lli;
import static util.ModifiedSqlGenerator.*;

import java.util.List;
public class LLIOfficeDAO {
	Class<LLIOffice> classObject = LLIOffice.class;
	public long insertNewOfficeInstance(LLIOffice lliOffice) throws Exception{
		insert(lliOffice);
		return lliOffice.getHistoryID();
	}

	public List<LLIOffice> getLLIOfficeListByConnectionHistoryID(long connectionHistoryID) throws Exception{
		return getAllObjectList(classObject,
					new LLIOfficeConditionBuilder()
					.Where()
					.connectionHistoryIDEquals(connectionHistoryID)
					.getCondition()
				);
	}
	
	public List<LLIOffice> getLLIOfficeListByConnectionHistoryIDList(List<Long> connectionHistoryIDs) throws Exception{
		return getAllObjectList(classObject,
					new LLIOfficeConditionBuilder()
					.Where()
					.connectionHistoryIDIn(connectionHistoryIDs)
					.getCondition()
				);
	}
	
	public List<LLIOffice> getLLIOfficeListByDateRangeAndClientID(long fromDate,long toDate,long clientID) throws Exception{
		
		return getAllObjectList(classObject, new LLIOfficeConditionBuilder()
												.Where()
												.connectionHistoryIDInSqlPair(
														new LLIConnectionInstanceConditionBuilder()
														.selectHistoryID()
														.fromLLIConnectionInstance()
														.Where()
														.activeToGreaterThan(fromDate)
														.activeFromLessThanEquals(toDate)
														.clientIDEquals(clientID)
														.validToEquals(Long.MAX_VALUE)
														.getSqlPair()
														)
												.getCondition()
												);
				
	}

	public List<LLIOffice> getLLIOfficeListByDateRange(long fromDate,long toDate) throws Exception{
		
		return getAllObjectList(classObject, new LLIOfficeConditionBuilder()
												.fromLLIOffice()
												.connectionHistoryIDInSqlPair(
														new LLIConnectionInstanceConditionBuilder()
														.selectHistoryID()
														.fromLLIConnectionInstance()
														.Where()
														.activeToGreaterThan(fromDate)
														.activeFromLessThanEquals(toDate)
														.validToEquals(Long.MAX_VALUE)
														.getSqlPair()
														)
												.getCondition()
												);
				
	}

}
