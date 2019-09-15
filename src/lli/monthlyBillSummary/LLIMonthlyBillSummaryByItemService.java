package lli.monthlyBillSummary;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import util.DateUtils;

public class LLIMonthlyBillSummaryByItemService {

	static Logger logger = Logger.getLogger( LLIMonthlyBillSummaryByItemService.class );
	
	@DAO
	LLIMonthlyBillSummaryByItemDAO lliMonthlyBillSummaryByItemDAO;
	
	@Transactional
	public void save(LLIMonthlyBillSummaryByItem object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyBillSummaryByItemDAO.insertItem(object);
			else
				lliMonthlyBillSummaryByItemDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving SummaryByConnection", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillSummaryByItem getByID(long id) {

		try {
			LLIMonthlyBillSummaryByItem item = lliMonthlyBillSummaryByItemDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	/*@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillSummaryByItem> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<LLIMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}*/
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long idOfmonthlyBillSummaryByClient) {

		try {
			List<LLIMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemDAO.getListByMonthlyBillSummaryByClientId(idOfmonthlyBillSummaryByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
			
		}
		return new ArrayList<>();
	}
	
	
	private void populateNonMappedData(List<LLIMonthlyBillSummaryByItem> items)
	{
		for(LLIMonthlyBillSummaryByItem item : items)
		{
			populateNonMappedData(item);
		}
	}
	
	private void populateNonMappedData(LLIMonthlyBillSummaryByItem item)
	{
		if(item == null)
			return;
		
		item.setDataFromDBContent();
	}

}
