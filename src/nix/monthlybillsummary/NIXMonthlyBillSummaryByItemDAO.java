package nix.monthlybillsummary;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyBillSummaryByItemDAO {

	Class<NIXMonthlyBillSummaryByItem> classObject = NIXMonthlyBillSummaryByItem.class;
	
	public void insertItem(NIXMonthlyBillSummaryByItem object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public NIXMonthlyBillSummaryByItem getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(NIXMonthlyBillSummaryByItem object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	/*public List<NIXMonthlyBillSummaryByItem> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyBillSummaryByItemConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());

	}*/
	
	public List<NIXMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long monthlyBillByClientId) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyBillSummaryByItemConditionBuilder()
				.Where()
				.monthlyBillSummaryByClientIdEquals(monthlyBillByClientId)
				.getCondition());
		
	}

}
