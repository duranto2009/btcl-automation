package vpn.monthlyBill;

import annotation.DAO;
import annotation.Transactional;
import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyBillByLinkService {

	public static Logger logger = Logger.getLogger(VPNMonthlyBillByLinkService.class);
	
	@DAO
	VPNMonthlyBillByLinkDAO vpnMonthlyBillByLinkDAO;

	@Transactional
	public void save(VPNMonthlyBillByLink object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				vpnMonthlyBillByLinkDAO.insertItem(object);
			else
				vpnMonthlyBillByLinkDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyBillByLink getByID(long id) {

		try {
			VPNMonthlyBillByLink item = vpnMonthlyBillByLinkDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<VPNMonthlyBillByLink> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<VPNMonthlyBillByLink> list = vpnMonthlyBillByLinkDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<VPNMonthlyBillByLink> getListByMonthlyBillByClientId(long idOfmonthlyBillByClient) {

		try {
			List<VPNMonthlyBillByLink> list = vpnMonthlyBillByLinkDAO.getListByMonthlyBillByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}
	
	
	private void populateNonMappedData(List<VPNMonthlyBillByLink> connections)
	{
		for(VPNMonthlyBillByLink connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(VPNMonthlyBillByLink connection)
	{
		if(connection == null)
			return;
		
		connection.setDataFromDBContent();
	}
}
