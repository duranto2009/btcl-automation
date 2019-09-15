package lli.monthlyBill;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import login.LoginDTO;
import util.DateUtils;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

public class LLIMonthlyBillByClientDAO {
	
	Class<LLIMonthlyBillByClient> classObject = LLIMonthlyBillByClient.class;
	
	public void insertItem(LLIMonthlyBillByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public LLIMonthlyBillByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyBillByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public LLIMonthlyBillByClient getByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillByClient> list = getAllObjectList(classObject, new LLIMonthlyBillByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}
	
	public List<LLIMonthlyBillByClient> getByDateRange( long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillByClient> list = getAllObjectList(classObject, new LLIMonthlyBillByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list;
	}
	
	public int getCountByDateRange(long fromDate, long toDate) throws Exception{

		List<LLIMonthlyBillByClient> list = getAllObjectList(classObject, new LLIMonthlyBillByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size();
	}

	
	//jami for search criteria
	public Collection getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria, LoginDTO loginDTO) throws Exception{
		
		
		
		String dateStr = searchCriteria.get("date");
		
		if(dateStr!=null) {
			SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat( "yyyy-MM" );
			Date date = dayMonthYearDateFormat.parse(dateStr);
			
			Date from = DateUtils.getFirstDayOfMonth(dayMonthYearDateFormat.parse(dateStr));
			Date to = DateUtils.getLastDayOfMonth(dayMonthYearDateFormat.parse(dateStr));
			searchCriteria.put("from", from.getTime()+"");
			searchCriteria.put("to", to.getTime()+"");
		}

		String clientId= searchCriteria.get("clientID");
		
		//the generated or non generated is not checked in this function latter we may need to add it
		ResultSet rs = getResultSetBySqlPair(
					new LLIMonthlyBillByClientConditionBuilder()
			 		.selectId()
			 		.fromLLIMonthlyBillByClient()
					.Where()
					.clientIdEqualsString(searchCriteria.get("clientID"))
					.createdDateGreaterThanEquals(TimeConverter.getDateFromString(searchCriteria.get("from")))
					.createdDateLessThanEquals(TimeConverter.getDateFromString(searchCriteria.get("to")))
					.getSqlPair()
				);	
		
		List<Long> idList = getSingleColumnListByResultSet(rs, Long.class); 
		/*chek the client id fpr non generated monthly bill 
		 * */
		return idList;
	}

	public Collection getLLIMonthlyBillByIDList(List<Long> recordIDs) throws Exception{
		return getAllObjectList(classObject,
					new LLIMonthlyBillByClientConditionBuilder()
					.Where()
					.idIn(recordIDs)
					.getCondition()
				);	
	}
	
	// jami for search criteria
}
