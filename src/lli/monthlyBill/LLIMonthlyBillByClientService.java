package lli.monthlyBill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import common.bill.BillDTO;
import lli.monthlyUsage.LLIMonthlyUsageByClient;
import login.LoginDTO;
import util.DateUtils;

public class LLIMonthlyBillByClientService {
	
	public static Logger logger = Logger.getLogger(LLIMonthlyBillByClientService.class);
	
	@DAO
	LLIMonthlyBillByClientDAO lliMonthlyBillByClientDAO;
	
	
	@Transactional
	public void save(LLIMonthlyBillByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyBillByClientDAO.insertItem(object);
			else
				lliMonthlyBillByClientDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillByClient getById(long id) {

		try {
			LLIMonthlyBillByClient client =  lliMonthlyBillByClientDAO.getItem(id);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillByClient getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			LLIMonthlyBillByClient client = lliMonthlyBillByClientDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillByClient> getByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return lliMonthlyBillByClientDAO.getByDateRange(fromDate, toDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ArrayList<LLIMonthlyBillByClient>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyBillGenerated(int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			return lliMonthlyBillByClientDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ");
		}
		return true;
	}

	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		return lliMonthlyBillByClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
	}



	public Collection getLLIMonthlyBillListByIDList(List<Long> recordIDs) throws Exception {
		// TODO Auto-generated method stub
		return lliMonthlyBillByClientDAO.getLLIMonthlyBillByIDList((List<Long>) recordIDs);
	}


	private void populateNonMappedData(LLIMonthlyBillByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
}
