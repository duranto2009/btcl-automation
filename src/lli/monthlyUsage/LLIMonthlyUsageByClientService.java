package lli.monthlyUsage;

import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import util.DateUtils;

public class LLIMonthlyUsageByClientService {

	public static Logger logger = Logger.getLogger(LLIMonthlyUsageByClientService.class);
	
	@DAO
	LLIMonthlyUsageByClientDAO lliMonthlyUsageByClientDAO;
	
	
	@Transactional
	public void save(LLIMonthlyUsageByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyUsageByClientDAO.insertItem(object);
			else
				lliMonthlyUsageByClientDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyUsageByClient getById(long id) {

		try {
			LLIMonthlyUsageByClient lliMonthlyUsageByClient =  lliMonthlyUsageByClientDAO.getItem(id);
			populateNonMappedData(lliMonthlyUsageByClient);
			
			return lliMonthlyUsageByClient;
		} catch (Exception e) {
		}
		return null;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyUsageByClient getByClientIdAndMonth(long clientId, int month, int year) {
		
		try {
			LLIMonthlyUsageByClient lliMonthlyUsageByClient = lliMonthlyUsageByClientDAO.getByClientIdAndDateRange(clientId, month, year);
			populateNonMappedData(lliMonthlyUsageByClient);
			
			return lliMonthlyUsageByClient;
			
		} catch (Exception e) {
		}
		return null;
	}

	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyUsageGenerated(int month, int year) {

		try {
			return lliMonthlyUsageByClientDAO.getCountByDateRange(month, year) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ", e);
		}
		return true;
	}

	
	private void populateNonMappedData(LLIMonthlyUsageByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
}
