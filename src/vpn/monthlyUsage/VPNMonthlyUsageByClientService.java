package vpn.monthlyUsage;

import annotation.DAO;
import annotation.Transactional;
import global.GlobalService;

import org.apache.log4j.Logger;
import requestMapping.Service;

public class VPNMonthlyUsageByClientService {

	public static Logger logger = Logger.getLogger(VPNMonthlyUsageByClientService.class);

	@DAO
	VPNMonthlyUsageByClientDAO vpnMonthlyUsageByClientDAO;

	@Service
	GlobalService globalService;
	
	
	@Transactional
	public void save(VPNMonthlyUsageByClient object) {

		try {
			if(object.getId() == null || object.getId() == 0)
				vpnMonthlyUsageByClientDAO.insertItem(object);
			else
				vpnMonthlyUsageByClientDAO.updateItem(object);
		} catch (Exception e) {
			logger.error("error while saving ", e);
		}
		return;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyUsageByClient getById(long id) {

		try {
			VPNMonthlyUsageByClient vpnMonthlyUsageByClient =  vpnMonthlyUsageByClientDAO.getItem(id);
			populateNonMappedData(vpnMonthlyUsageByClient);
			
			return vpnMonthlyUsageByClient;
		} catch (Exception e) {
		}
		return null;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public VPNMonthlyUsageByClient getByClientIdAndMonth(long clientId, int month, int year) {
		
		try {
			VPNMonthlyUsageByClient vpnMonthlyUsageByClient = vpnMonthlyUsageByClientDAO.getByClientIdAndDateRange(clientId, month, year);
			populateNonMappedData(vpnMonthlyUsageByClient);
			
			return vpnMonthlyUsageByClient;
			
		} catch (Exception e) {
		}
		return null;
	}

	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyUsageGenerated(int month, int year) {

		try {
			return vpnMonthlyUsageByClientDAO.getCountByDateRange(month, year) > 0 ? true : false;
		} catch (Exception e) {
			logger.fatal("error in isMonthlyBillGenerated ", e);
		}
		return true;
	}

	
	private void populateNonMappedData(VPNMonthlyUsageByClient client)
	{
		if(client == null)
			return;
		
		client.setDataFromDBContent();
	}
}
