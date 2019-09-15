package nix.monthlybillsummary;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyBillSummaryByItemService {

	static Logger logger = Logger.getLogger( NIXMonthlyBillSummaryByItemService.class );
	
	@DAO
	NIXMonthlyBillSummaryByItemDAO lliMonthlyBillSummaryByItemDAO;
	
	@Transactional
	public void save(NIXMonthlyBillSummaryByItem object) {

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
	public NIXMonthlyBillSummaryByItem getByID(long id) {

		try {
			NIXMonthlyBillSummaryByItem item = lliMonthlyBillSummaryByItemDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	/*@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyBillSummaryByItem> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<NIXMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}*/
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyBillSummaryByItem> getListByMonthlyBillSummaryByClientId(long idOfmonthlyBillSummaryByClient) {

		try {
			List<NIXMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemDAO.getListByMonthlyBillSummaryByClientId(idOfmonthlyBillSummaryByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
			
		}
		return new ArrayList<>();
	}
	
	
	private void populateNonMappedData(List<NIXMonthlyBillSummaryByItem> items)
	{
		for(NIXMonthlyBillSummaryByItem item : items)
		{
			populateNonMappedData(item);
		}
	}
	
	private void populateNonMappedData(NIXMonthlyBillSummaryByItem item)
	{
		if(item == null)
			return;
		
		item.setDataFromDBContent();
	}

}
