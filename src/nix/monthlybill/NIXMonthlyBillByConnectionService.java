package nix.monthlybill;

import annotation.DAO;
import annotation.Transactional;

import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyBillByConnectionService {

	public static Logger logger = Logger.getLogger(NIXMonthlyBillByConnectionService.class);

	@DAO
	NIXMonthlyBillByConnectionDAO nixMonthlyBillByConnectionDAO;

//	public List<LLIMonthlyBillByConnection> getDummyList(LLIMonthlyBillByClient lliMonthlyBillByClient)
//	{
//		List<LLIMonthlyBillByConnection> list = new ArrayList<LLIMonthlyBillByConnection>();
//
//		for(int i = 0; i < 3; i++)
//		{
//
//			LLIMonthlyBillByConnection bill = new LLIMonthlyBillByConnection();
//			bill.setId(1L);
//			bill.setClientId(lliMonthlyBillByClient.getClientId());
//			bill.setMonthlyBillByClientId(1);
//			bill.setConnectionId(1);
//			bill.setCreatedDate(lliMonthlyBillByClient.getCreatedDate());
//			bill.setLoopCost(150);
//			bill.setTotalMbps(150);
//
//			FeeByPortTypeForClient fee = new FeeByPortTypeForClient();
//			fee.setCost(120);
//			fee.setRemark("late fee");
//			bill.getMonthlyBillFees().add(fee);
//
//			list.add(bill);
//		}
//		return list;
//	}
	
	@Transactional
	public void save(NIXMonthlyBillByConnection object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				nixMonthlyBillByConnectionDAO.insertItem(object);
			else
				nixMonthlyBillByConnectionDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public NIXMonthlyBillByConnection getByID(long id) {

		try {
			NIXMonthlyBillByConnection item = nixMonthlyBillByConnectionDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyBillByConnection> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<NIXMonthlyBillByConnection> list = nixMonthlyBillByConnectionDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<NIXMonthlyBillByConnection> getListByMonthlyBillByClientId(long idOfmonthlyBillByClient) {

		try {
			List<NIXMonthlyBillByConnection> list = nixMonthlyBillByConnectionDAO.getListByMonthlyBillByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}
	
	
	private void populateNonMappedData(List<NIXMonthlyBillByConnection> connections)
	{
		for(NIXMonthlyBillByConnection connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(NIXMonthlyBillByConnection connection)
	{
		if(connection == null)
			return;
		
		connection.setDataFromDBContent();
	}
}
