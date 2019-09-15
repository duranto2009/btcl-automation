package vpn.monthlyBill;

import login.LoginDTO;
import util.DateUtils;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class VPNMonthlyBillByClientDAO {
	
	Class<VPNMonthlyBillByClient> classObject = VPNMonthlyBillByClient.class;
	
	public void insertItem(VPNMonthlyBillByClient object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public VPNMonthlyBillByClient getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(VPNMonthlyBillByClient object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	public VPNMonthlyBillByClient getByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		List<VPNMonthlyBillByClient> list = getAllObjectList(classObject, new VPNMonthlyBillByClientConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;

	}
	
	public List<VPNMonthlyBillByClient> getByDateRange( long fromDate, long toDate) throws Exception{

		List<VPNMonthlyBillByClient> list = getAllObjectList(classObject, new VPNMonthlyBillByClientConditionBuilder()
				.Where()
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
		return list;
	}
	
	public int getCountByDateRange(long fromDate, long toDate) throws Exception{

		List<VPNMonthlyBillByClient> list = getAllObjectList(classObject, new VPNMonthlyBillByClientConditionBuilder()
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
					new VPNMonthlyBillByClientConditionBuilder()
			 		.selectId()
			 		.fromVPNMonthlyBillByClient()
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

	public Collection getVPNMonthlyBillByIDList(List<Long> recordIDs) throws Exception{
		return getAllObjectList(classObject,
					new VPNMonthlyBillByClientConditionBuilder()
					.Where()
					.idIn(recordIDs)
					.getCondition()
				);
	}
	
	// jami for search criteria
}
