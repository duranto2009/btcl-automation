package vpn.monthlyUsage;

import requestMapping.Service;
import util.DateUtils;
import vpn.client.ClientDetailsDTO;

import java.text.ParseException;
import java.util.List;

public class VPNMonthlyUsageService
{

	@Service
	VPNMonthlyUsageByClientService vpnMonthlyUsageByClientService;
	@Service
	VPNMonthlyUsageByLinkService vpnMonthlyUsageByLinkService;
	@Service
	VPNMonthlyUsageGenerator vpnMonthlyUsageGenerator;


	public VPNMonthlyUsageByClient getVPNMonthlyUsageByClient(long clientId, int month, int year) throws Exception
	{
		VPNMonthlyUsageByClient vpnMonthlyUsageByClient = vpnMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);

		
		if(vpnMonthlyUsageByClient != null)
		{
			List<VPNMonthlyUsageByLink> list = vpnMonthlyUsageByLinkService.getListByMonthlyUsageByClientId(vpnMonthlyUsageByClient.getId());
			vpnMonthlyUsageByClient.setMonthlyUsageByLinks(list);
		}
		return vpnMonthlyUsageByClient;
	}
	

	public void generateLastMonthVPNMonthlyUsageForAll() throws Exception
	{
		vpnMonthlyUsageGenerator.generateMonthlyUsage();
	}
	
	public void generateLastMonthVPNMonthlyUsage(List<ClientDetailsDTO> dtos) throws Exception
	{
		vpnMonthlyUsageGenerator.generateMonthlyUsage(dtos);
	}
	
	public boolean isCurrentMonthlyUsageGenerated() throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);
		
		return vpnMonthlyUsageByClientService.isMonthlyUsageGenerated(month, year);
		 
	}
	
	public boolean isCurrentMonthlyUsageGenerated(long clientId) throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);

		VPNMonthlyUsageByClient vpnMonthlyUsageByClient = vpnMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);
		
		return vpnMonthlyUsageByClient == null ? false : true;
		 
	}


}
