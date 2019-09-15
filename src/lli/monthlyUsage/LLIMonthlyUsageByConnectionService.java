package lli.monthlyUsage;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import util.DateUtils;

public class LLIMonthlyUsageByConnectionService {

	static Logger logger = Logger.getLogger(LLIMonthlyUsageByConnectionService.class);
	
	@DAO
	LLIMonthlyUsageByConnectionDAO lliMonthlyUsageByConnectionDAO;
	
	
	@Transactional
	public void save(LLIMonthlyUsageByConnection object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyUsageByConnectionDAO.insertItem(object);
			else
				lliMonthlyUsageByConnectionDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyUsageByConnection getByID(long id) {

		try {
			LLIMonthlyUsageByConnection item = lliMonthlyUsageByConnectionDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyUsageByConnection> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<LLIMonthlyUsageByConnection> list = lliMonthlyUsageByConnectionDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyUsageByConnection> getListByMonthlyUsageByClientId(long idOfmonthlyBillByClient) {

		try {
			List<LLIMonthlyUsageByConnection> list = lliMonthlyUsageByConnectionDAO.getListByMonthlyUsageByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}

	
	private void populateNonMappedData(List<LLIMonthlyUsageByConnection> connections)
	{
		for(LLIMonthlyUsageByConnection connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(LLIMonthlyUsageByConnection connection)
	{
		if(connection == null)
			return;
		
		connection.setConnectionType();
		connection.setDataFromDBContent();
	}
}
