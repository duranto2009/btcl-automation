package lli.monthlyBillSummary;

import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;

import util.ModifiedSqlGenerator;

public class LLIMonthlyBillSummaryByItemDAO {

	Class<LLIMonthlyBillSummaryByItem> classObject = LLIMonthlyBillSummaryByItem.class;
	
	public void insertItem(LLIMonthlyBillSummaryByItem object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public LLIMonthlyBillSummaryByItem getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyBillSummaryByItem object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	/*public List<LLIMonthlyBillSummaryByItem> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyBillSummaryByItemConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
	
	}*/
	
	public List<LLIMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long monthlyBillByClientId) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyBillSummaryByItemConditionBuilder()
				.Where()
				.monthlyBillSummaryByClientIdEquals(monthlyBillByClientId)
				.getCondition());
		
	}

}
