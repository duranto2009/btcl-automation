package nix.monthlyusage;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

public class NIXMonthlyUsageByClientService {

	public static Logger logger = Logger.getLogger(NIXMonthlyUsageByClientService.class);
	
	@DAO
	NIXMonthlyUsageByClientDAO nixMonthlyUsageByClientDAO;
	
	
	@Transactional
	public void save(NIXMonthlyUsageByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				nixMonthlyUsageByClientDAO.insertItem(object);
			else
				nixMonthlyUsageByClientDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyUsageByClient getById(long id) {

		try {
			NIXMonthlyUsageByClient nixMonthlyUsageByClient =  nixMonthlyUsageByClientDAO.getItem(id);
			populateNonMappedData(nixMonthlyUsageByClient);
			
			return nixMonthlyUsageByClient;
		} catch (Exception e) {
		}
		return null;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyUsageByClient getByClientIdAndMonth(long clientId, int month, int year) {
		
		try {
			NIXMonthlyUsageByClient nixMonthlyUsageByClient = nixMonthlyUsageByClientDAO.getByClientIdAndDateRange(clientId, month, year);
			populateNonMappedData(nixMonthlyUsageByClient);
			
			return nixMonthlyUsageByClient;
			
		} catch (Exception e) {
		}
		return null;
	}

	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyUsageGenerated(int month, int year) {

		try {
			return nixMonthlyUsageByClientDAO.getCountByDateRange(month, year) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ", e);
		}
		return true;
	}

	
	private void populateNonMappedData(NIXMonthlyUsageByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
}
