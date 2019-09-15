package lli.monthlyUsage;

import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;
import util.ModifiedSqlGenerator;

public class LLIMonthlyUsageByConnectionDAO {

	Class<LLIMonthlyUsageByConnection> classObject = LLIMonthlyUsageByConnection.class;
	
	public void insertItem(LLIMonthlyUsageByConnection object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public LLIMonthlyUsageByConnection getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyUsageByConnection object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	public List<LLIMonthlyUsageByConnection> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyUsageByConnectionConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
	
	}
	
	public List<LLIMonthlyUsageByConnection> getListByMonthlyUsageByClientId(long monthlyUsageByClientId) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyUsageByConnectionConditionBuilder()
				.Where()
				.monthlyUsageByClientIdEquals(monthlyUsageByClientId)
				.getCondition());
		
	}
}
