package nix.monthlybillsummary;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyBillSummaryByClientService {

	public static Logger logger = Logger.getLogger(NIXMonthlyBillSummaryByClientService.class);
	
	@DAO
	NIXMonthlyBillSummaryByClientDAO nixMonthlyBillByClientSummaryDAO;
	
	
	@Transactional
	public void save(NIXMonthlyBillSummaryByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				nixMonthlyBillByClientSummaryDAO.insertItem(object);
			else
				nixMonthlyBillByClientSummaryDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyBillSummaryByClient getById(long id) {

		try {
			NIXMonthlyBillSummaryByClient client =  nixMonthlyBillByClientSummaryDAO.getItem(id);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			NIXMonthlyBillSummaryByClient client = nixMonthlyBillByClientSummaryDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyBillSummaryByClient> getByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return nixMonthlyBillByClientSummaryDAO.getByDateRange(fromDate, toDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ArrayList<NIXMonthlyBillSummaryByClient>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<Long> getClientIdsByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return nixMonthlyBillByClientSummaryDAO.getClientIdsByDateRange(fromDate, toDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyBillGenerated(int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			return nixMonthlyBillByClientSummaryDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ");
		}
		return true;
	}
	
	
	private void populateNonMappedData(NIXMonthlyBillSummaryByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
	
}
