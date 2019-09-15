package lli;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import util.SqlPair;

import static util.ModifiedSqlGenerator.*;
public class LLILocalLoopDAO {
	Class<LLILocalLoop> classObject = LLILocalLoop.class;
	void insertLocalLoop(LLILocalLoop lliLocalLoop) throws Exception{
		insert(lliLocalLoop);
	}

	public List<Long> getVlanIDListByConnectionHistoryID(long lliConnectionHistoryID) throws Exception{
		
		
		SqlPair sqlPair = new LLILocalLoopConditionBuilder()
							.selectVlanID()
							.fromLLILocalLoop()
							.Where()
							.lliOfficeHistoryIDInSqlPair(
									new LLIOfficeConditionBuilder()
									.selectHistoryID()
									.fromLLIOffice()
									.Where()
									.connectionHistoryIDEquals(lliConnectionHistoryID)
									.getSqlPair()
								).getSqlPair();
		ResultSet rs = getResultSetBySqlPair(sqlPair);
		List<Long> vlanIDList = getSingleColumnListByResultSet(rs, Long.class);
		
		return vlanIDList;
	}
	
	public List<LLILocalLoop> getLLILocalLoopListByConnectionHistoryID(long lliConnectionHistroyID) throws Exception{
		return getAllObjectList(LLILocalLoop.class,
							new LLILocalLoopConditionBuilder()
							.Where()
							.lliOfficeHistoryIDInSqlPair(
									new LLIOfficeConditionBuilder()
									.selectHistoryID()
									.fromLLIOffice()
									.Where()
									.connectionHistoryIDEquals(lliConnectionHistroyID)
									.getSqlPair()
									
							).getCondition()
				);
	}
	public List<LLILocalLoop> getLLILocalLoopListByClientIDAndDateRange(long clientID,long fromDate
			,long toDate) throws Exception{
		return getAllObjectList(classObject,
						new LLILocalLoopConditionBuilder()
						.Where()
						.lliOfficeHistoryIDInSqlPair(
								new LLIOfficeConditionBuilder()
								.selectHistoryID()
								.fromLLIOffice()
								.Where()
								.connectionHistoryIDInSqlPair(
										new LLIConnectionInstanceConditionBuilder()
										.selectHistoryID()
										.fromLLIConnectionInstance()
										.Where()
										.clientIDEquals(clientID)
										.activeToGreaterThan(fromDate)
										.activeFromLessThanEquals(toDate)
										.validToEquals(Long.MAX_VALUE)
										.getSqlPair()
										)
								.getSqlPair())
						.getCondition()
				);
	}
	
	public List<LLILocalLoop> getLLILocalLoopListByDateRange(long fromDate
			,long toDate) throws Exception{
		return getAllObjectList(classObject,
						new LLILocalLoopConditionBuilder()
						.Where()
						.lliOfficeHistoryIDInSqlPair(
								new LLIOfficeConditionBuilder()
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
								.getSqlPair())
						.getCondition()
				);
	}
	
	public List<Long> getCurrentlyOccupiedVlanIDListByLLIConnectionID(long lliConnectionID) throws Exception{
		SqlPair sqlPair = new LLILocalLoopConditionBuilder()
				.selectVlanID()
				.fromLLILocalLoop()
				.lliOfficeHistoryIDInSqlPair(
						new LLIOfficeConditionBuilder()
						.selectHistoryID()
						.fromLLIOffice()
						.connectionHistoryIDInSqlPair(
								new LLIConnectionInstanceConditionBuilder()
								.selectHistoryID()
								.fromLLIConnectionInstance()
								.IDEquals(lliConnectionID)
								.activeToEquals(Long.MAX_VALUE)
								.validToEquals(Long.MAX_VALUE)
								.limit(1)
								.getSqlPair()
								)
						.getSqlPair()
						)
				.getSqlPair();
		ResultSet rs = getResultSetBySqlPair(sqlPair);
		return getSingleColumnListByResultSet(rs, Long.class);
	}

	public List<LLILocalLoop> getLLiLocalLoopListByOfficeHistoryIDList(List<Long> officeHistoryIDs) throws Exception{
		if(officeHistoryIDs.isEmpty()) {
			return Collections.emptyList();
		}
		return getAllObjectList(classObject, 
						new LLILocalLoopConditionBuilder()
						.Where()
						.lliOfficeHistoryIDIn(officeHistoryIDs)
						.getCondition()
				);
	}

}
