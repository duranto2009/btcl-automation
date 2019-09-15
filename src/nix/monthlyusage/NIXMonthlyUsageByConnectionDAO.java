package nix.monthlyusage;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class NIXMonthlyUsageByConnectionDAO {

	Class<NIXMonthlyUsageByConnection> classObject = NIXMonthlyUsageByConnection.class;

	public void insertItem(NIXMonthlyUsageByConnection object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}


	public NIXMonthlyUsageByConnection getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}

	public void updateItem(NIXMonthlyUsageByConnection object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}


	public List<NIXMonthlyUsageByConnection> getListByClientIdAndDateRange(long clientId, long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyUsageByConnectionConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.createdDateGreaterThanEquals(fromDate)
				.createdDateLessThanEquals(toDate)
				.getCondition());

	}

	public List<NIXMonthlyUsageByConnection> getListByMonthlyUsageByClientId(long monthlyUsageByClientId) throws Exception{

		return getAllObjectList(classObject, new NIXMonthlyUsageByConnectionConditionBuilder()
				.Where()
				.monthlyUsageByClientIdEquals(monthlyUsageByClientId)
				.getCondition());

	}
}
