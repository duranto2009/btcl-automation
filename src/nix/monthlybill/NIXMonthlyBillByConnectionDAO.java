package nix.monthlybill;
import util.ModifiedSqlGenerator;
import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyBillByConnectionDAO {

	Class<NIXMonthlyBillByConnection> classObject = NIXMonthlyBillByConnection.class;

	public void insertItem(NIXMonthlyBillByConnection object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}


	public NIXMonthlyBillByConnection getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}

	public void updateItem(NIXMonthlyBillByConnection object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}


	public List<NIXMonthlyBillByConnection> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyBillByConnectionConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());

	}

	public List<NIXMonthlyBillByConnection> getListByMonthlyBillByClientId(long monthlyBillByClientId) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyBillByConnectionConditionBuilder()
				.Where()
				.monthlyBillByClientIdEquals(monthlyBillByClientId)
				.getCondition());

	}
}
