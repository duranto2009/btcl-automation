package lli.monthlyBillSummary;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class LLIMonthlyBillSummaryByClientService {

	public static Logger logger = Logger.getLogger(LLIMonthlyBillSummaryByClientService.class);
	
	@DAO
	LLIMonthlyBillSummaryByClientDAO lliMonthlyBillByClientSummaryDAO;
	
	
	@Transactional
	public void save(LLIMonthlyBillSummaryByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyBillByClientSummaryDAO.insertItem(object);
			else
				lliMonthlyBillByClientSummaryDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillSummaryByClient getById(long id) {

		try {
			LLIMonthlyBillSummaryByClient client =  lliMonthlyBillByClientSummaryDAO.getItem(id);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillSummaryByClient getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			LLIMonthlyBillSummaryByClient client = lliMonthlyBillByClientSummaryDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillSummaryByClient> getByClientId(long clientId) {

		try {
			List<LLIMonthlyBillSummaryByClient> lliMonthlyBillSummaryByClients = lliMonthlyBillByClientSummaryDAO.getByClientId(clientId);
			for (LLIMonthlyBillSummaryByClient lliMonthlyBillSummaryByClient:lliMonthlyBillSummaryByClients
				 ) {

				populateNonMappedData(lliMonthlyBillSummaryByClient);
			}

			return lliMonthlyBillSummaryByClients;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillSummaryByClient> getByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return lliMonthlyBillByClientSummaryDAO.getByDateRange(fromDate, toDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ArrayList<LLIMonthlyBillSummaryByClient>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<Long> getClientIdsByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return lliMonthlyBillByClientSummaryDAO.getClientIdsByDateRange(fromDate, toDate);
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
			return lliMonthlyBillByClientSummaryDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ");
		}
		return true;
	}
	
	
	private void populateNonMappedData(LLIMonthlyBillSummaryByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
	
}
