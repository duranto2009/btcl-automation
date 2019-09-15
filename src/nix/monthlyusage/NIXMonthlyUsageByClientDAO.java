package nix.monthlyusage;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyUsageByClientDAO {

	
	Class<NIXMonthlyUsageByClient> classObject = NIXMonthlyUsageByClient.class;

	public void insertItem(NIXMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}

	public NIXMonthlyUsageByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}

	public void updateItem(NIXMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}

	public NIXMonthlyUsageByClient getByClientIdAndDateRange(long clientId, int month, int year) throws Exception{

		List<NIXMonthlyUsageByClient> list = getAllObjectList(classObject, new NIXMonthlyUsageByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}

	public int getCountByDateRange(int month, int year) throws Exception{

		List<NIXMonthlyUsageByClient> list = getAllObjectList(classObject, new NIXMonthlyUsageByClientConditionBuilder()
				.Where()
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size();
	}

}
