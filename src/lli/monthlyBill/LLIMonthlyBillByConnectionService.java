package lli.monthlyBill;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import util.DateUtils;

public class LLIMonthlyBillByConnectionService {

	public static Logger logger = Logger.getLogger(LLIMonthlyBillByConnectionService.class);
	
	@DAO
	LLIMonthlyBillByConnectionDAO lliMonthlyBillByConnectionDAO;
	
	public List<LLIMonthlyBillByConnection> getDummyList(LLIMonthlyBillByClient lliMonthlyBillByClient)
	{
		List<LLIMonthlyBillByConnection> list = new ArrayList<LLIMonthlyBillByConnection>();
		
		for(int i = 0; i < 3; i++)
		{
			
			LLIMonthlyBillByConnection bill = new LLIMonthlyBillByConnection();
			bill.setId(1L);
			bill.setClientId(lliMonthlyBillByClient.getClientId());
			bill.setMonthlyBillByClientId(1);
			bill.setConnectionId(1);
			bill.setCreatedDate(lliMonthlyBillByClient.getCreatedDate());
			bill.setLoopCost(150);
			bill.setTotalMbps(150);
			
			Fee fee = new Fee();
			fee.setCost(120);
			fee.setRemark("late fee");
			bill.getMonthlyBillFees().add(fee);
		
			list.add(bill);
		}
		return list;
	}
	
	@Transactional
	public void save(LLIMonthlyBillByConnection object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				lliMonthlyBillByConnectionDAO.insertItem(object);
			else
				lliMonthlyBillByConnectionDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public LLIMonthlyBillByConnection getByID(long id) {

		try {
			LLIMonthlyBillByConnection item = lliMonthlyBillByConnectionDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillByConnection> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<LLIMonthlyBillByConnection> list = lliMonthlyBillByConnectionDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<LLIMonthlyBillByConnection> getListByMonthlyBillByClientId(long idOfmonthlyBillByClient) {

		try {
			List<LLIMonthlyBillByConnection> list = lliMonthlyBillByConnectionDAO.getListByMonthlyBillByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}
	
	
	private void populateNonMappedData(List<LLIMonthlyBillByConnection> connections)
	{
		for(LLIMonthlyBillByConnection connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(LLIMonthlyBillByConnection connection)
	{
		if(connection == null)
			return;
		
		connection.setDataFromDBContent();
	}
}
