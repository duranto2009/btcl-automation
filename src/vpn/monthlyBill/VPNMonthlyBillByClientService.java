package vpn.monthlyBill;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class VPNMonthlyBillByClientService {
	
	public static Logger logger = Logger.getLogger(VPNMonthlyBillByClientService.class);
	
	@DAO
	VPNMonthlyBillByClientDAO vpnMonthlyBillByClientDAO;
	
	
	@Transactional
	public void save(VPNMonthlyBillByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				vpnMonthlyBillByClientDAO.insertItem(object);
			else
				vpnMonthlyBillByClientDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyBillByClient getById(long id) {

		try {
			VPNMonthlyBillByClient client =  vpnMonthlyBillByClientDAO.getItem(id);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyBillByClient getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			VPNMonthlyBillByClient client = vpnMonthlyBillByClientDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(client);
			
			return client;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<VPNMonthlyBillByClient> getByDateRange( int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
	
			try {
				return vpnMonthlyBillByClientDAO.getByDateRange(fromDate, toDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new ArrayList<VPNMonthlyBillByClient>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyBillGenerated(int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			return vpnMonthlyBillByClientDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ");
		}
		return true;
	}

	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		return vpnMonthlyBillByClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
	}



	public Collection getVPNMonthlyBillListByIDList(List<Long> recordIDs) throws Exception {
		// TODO Auto-generated method stub
		return vpnMonthlyBillByClientDAO.getVPNMonthlyBillByIDList((List<Long>) recordIDs);
	}


	private void populateNonMappedData(VPNMonthlyBillByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
}
