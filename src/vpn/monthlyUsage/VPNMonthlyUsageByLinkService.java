package vpn.monthlyUsage;

import annotation.DAO;
import annotation.Transactional;

import org.apache.log4j.Logger;
import util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class VPNMonthlyUsageByLinkService {

	static Logger logger = Logger.getLogger(VPNMonthlyUsageByLinkService.class);
	
	@DAO
	VPNMonthlyUsageByLinkDAO vpnMonthlyUsageByLinkDAO;
	
	
	@Transactional
	public void save(VPNMonthlyUsageByLink object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				vpnMonthlyUsageByLinkDAO.insertItem(object);
			else
				vpnMonthlyUsageByLinkDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyUsageByLink getByID(long id) {

		try {
			VPNMonthlyUsageByLink item = vpnMonthlyUsageByLinkDAO.getItem(id);
			populateNonMappedData(item);
			return item;
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<VPNMonthlyUsageByLink> getByClientIdAndDateRange(long clientId,  int month, int year) {

		long fromDate = DateUtils.getStartTimeOfMonth(month, year);
		long toDate = DateUtils.getEndTimeOfMonth(month, year);
		try {
			List<VPNMonthlyUsageByLink> list = vpnMonthlyUsageByLinkDAO.getListByClientIdAndDateRange(clientId, fromDate, toDate);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<VPNMonthlyUsageByLink> getListByMonthlyUsageByClientId(long idOfmonthlyBillByClient) {

		try {
			List<VPNMonthlyUsageByLink> list = vpnMonthlyUsageByLinkDAO.getListByMonthlyUsageByClientId(idOfmonthlyBillByClient);
			populateNonMappedData(list);
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<>();
	}

	
	private void populateNonMappedData(List<VPNMonthlyUsageByLink> connections)
	{
		for(VPNMonthlyUsageByLink connection : connections)
		{
			populateNonMappedData(connection);
		}
	}
	
	private void populateNonMappedData(VPNMonthlyUsageByLink connection)
	{
		if(connection == null)
			return;

		connection.setDataFromDBContent();
	}
}
