package vpn.monthlyUsage;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyUsageByLinkDAO {

	Class<VPNMonthlyUsageByLink> classObject = VPNMonthlyUsageByLink.class;
	
	public void insertItem(VPNMonthlyUsageByLink object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public VPNMonthlyUsageByLink getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(VPNMonthlyUsageByLink object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	public List<VPNMonthlyUsageByLink> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new VPNMonthlyUsageByLinkConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());

	
	}
	
	public List<VPNMonthlyUsageByLink> getListByMonthlyUsageByClientId(long monthlyUsageByClientId) throws Exception{

		return getAllObjectList(classObject, new VPNMonthlyUsageByLinkConditionBuilder()
				.Where()
				.monthlyUsageByClientIdEquals(monthlyUsageByClientId)
				.getCondition());

		
	}
}
