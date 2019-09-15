package vpn.monthlyBill;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class VPNMonthlyBillByLinkDAO {

	Class<VPNMonthlyBillByLink> classObject = VPNMonthlyBillByLink.class;
	
	public void insertItem(VPNMonthlyBillByLink object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public VPNMonthlyBillByLink getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(VPNMonthlyBillByLink object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	public List<VPNMonthlyBillByLink> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new VPNMonthlyBillByLinkConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
	}
	
	public List<VPNMonthlyBillByLink> getListByMonthlyBillByClientId(long monthlyBillByClientId) throws Exception{

		return getAllObjectList(classObject, new VPNMonthlyBillByLinkConditionBuilder()
				.Where()
				.monthlyBillByClientIdEquals(monthlyBillByClientId)
				.getCondition());
	}
}
