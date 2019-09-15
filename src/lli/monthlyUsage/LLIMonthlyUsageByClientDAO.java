package lli.monthlyUsage;

import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;

import requestMapping.Service;
import util.ModifiedSqlGenerator;

public class LLIMonthlyUsageByClientDAO {

	
	Class<LLIMonthlyUsageByClient> classObject = LLIMonthlyUsageByClient.class;
	
	public void insertItem(LLIMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public LLIMonthlyUsageByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public LLIMonthlyUsageByClient getByClientIdAndDateRange(long clientId, int month, int year) throws Exception{

		List<LLIMonthlyUsageByClient> list = getAllObjectList(classObject, new LLIMonthlyUsageByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}
	
	public int getCountByDateRange(int month, int year) throws Exception{

		List<LLIMonthlyUsageByClient> list = getAllObjectList(classObject, new LLIMonthlyUsageByClientConditionBuilder()
				.Where()
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size();
	}

}
