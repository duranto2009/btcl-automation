package nix.monthlyusage;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyUsageByConnectionService {

	static Logger logger = Logger.getLogger(NIXMonthlyUsageByConnectionService.class);
	
	@DAO
	NIXMonthlyUsageByConnectionDAO nixMonthlyUsageByConnectionDAO;
	
	
	@Transactional
	public void save(NIXMonthlyUsageByConnection object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				nixMonthlyUsageByConnectionDAO.insertItem(object);
			else
				nixMonthlyUsageByConnectionDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyUsageByConnection getByID(long id) {

		try {
			NIXMonthlyUsageByConnection item = nixMonthlyUsageByConnectionDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyUsageByConnection> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<NIXMonthlyUsageByConnection> list = nixMonthlyUsageByConnectionDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyUsageByConnection> getListByMonthlyUsageByClientId(long idOfmonthlyBillByClient) {

		try {
			List<NIXMonthlyUsageByConnection> list = nixMonthlyUsageByConnectionDAO.getListByMonthlyUsageByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}

	
	private void populateNonMappedData(List<NIXMonthlyUsageByConnection> connections)
	{
		for(NIXMonthlyUsageByConnection connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(NIXMonthlyUsageByConnection connection)
	{
		if(connection == null)
			return;
		
		connection.setConnectionType();
		connection.setDataFromDBContent();
	}
}
