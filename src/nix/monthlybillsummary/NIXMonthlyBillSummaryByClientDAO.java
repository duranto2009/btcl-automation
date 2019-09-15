package nix.monthlybillsummary;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyBillSummaryByClientDAO {


	Class<NIXMonthlyBillSummaryByClient> classObject = NIXMonthlyBillSummaryByClient.class;
	
	public void insertItem(NIXMonthlyBillSummaryByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public NIXMonthlyBillSummaryByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(NIXMonthlyBillSummaryByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public NIXMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		List<NIXMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new NIXMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}
	
	public List<NIXMonthlyBillSummaryByClient> getByDateRange( long fromDate, long toDate) throws Exception{

		List<NIXMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new NIXMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list;
	}
	
	public int getCountByDateRange(long fromDate, long toDate) throws Exception{

		List<NIXMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new NIXMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size();
	}

	
	public List<Long> getClientIdsByDateRange(long fromDate, long toDate) throws Exception {
		
		List<NIXMonthlyBillSummaryByClient> list = getAllObjectList(classObject, new NIXMonthlyBillSummaryByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		
		return list == null ? new ArrayList<>()
				:list.stream().mapToLong(x->x.getClientId()).boxed().collect(Collectors.toList());
	}

}
