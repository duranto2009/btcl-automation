package vpn.monthlyUsage;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyUsageByClientDAO {

	
	Class<VPNMonthlyUsageByClient> classObject = VPNMonthlyUsageByClient.class;
	
	public void insertItem(VPNMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public VPNMonthlyUsageByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(VPNMonthlyUsageByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public VPNMonthlyUsageByClient getByClientIdAndDateRange(long clientId, int month, int year) throws Exception{

		List<VPNMonthlyUsageByClient> list = getAllObjectList(classObject, new VPNMonthlyUsageByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}
	
	public int getCountByDateRange(int month, int year) throws Exception{

		List<VPNMonthlyUsageByClient> list = getAllObjectList(classObject, new VPNMonthlyUsageByClientConditionBuilder()
				.Where()
				.monthEquals(month)
				.yearEquals(year)
				.getCondition());
		return list.size();

	}

}
