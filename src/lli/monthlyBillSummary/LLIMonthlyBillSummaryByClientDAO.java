package lli.monthlyBillSummary;

import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class LLIMonthlyBillSummaryByClientDAO {


	Class<LLIMonthlyBillSummaryByClient> classObject = LLIMonthlyBillSummaryByClient.class;
	
	public void insertItem(LLIMonthlyBillSummaryByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public LLIMonthlyBillSummaryByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyBillSummaryByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public LLIMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new LLIMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}

	public List<LLIMonthlyBillSummaryByClient> getByClientId(long clientId) throws Exception{

		List<LLIMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new LLIMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.getCondition());
		return list.size() > 0 ? list : null;
	}
	
	public List<LLIMonthlyBillSummaryByClient> getByDateRange( long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new LLIMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list;
	}
	
	public int getCountByDateRange(long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new LLIMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size();
	}

	
	public List<Long> getClientIdsByDateRange(long fromDate, long toDate) throws Exception {
		
		List<LLIMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new LLIMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		
		return list == null ? new ArrayList<>()
				:list.stream().mapToLong(x->x.getClientId()).boxed().collect(Collectors.toList());
	}

}
