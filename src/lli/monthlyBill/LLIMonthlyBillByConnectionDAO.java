package lli.monthlyBill;

import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;
import util.ModifiedSqlGenerator;

public class LLIMonthlyBillByConnectionDAO {

	Class<LLIMonthlyBillByConnection> classObject = LLIMonthlyBillByConnection.class;
	
	public void insertItem(LLIMonthlyBillByConnection object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	
	public LLIMonthlyBillByConnection getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIMonthlyBillByConnection object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}
	
	
	public List<LLIMonthlyBillByConnection> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyBillByConnectionConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());
	
	}
	
	public List<LLIMonthlyBillByConnection> getListByMonthlyBillByClientId(long monthlyBillByClientId) throws Exception{

		return getAllObjectList(classObject, new LLIMonthlyBillByConnectionConditionBuilder()
				.Where()
				.monthlyBillByClientIdEquals(monthlyBillByClientId)
				.getCondition());
		
	}
}
