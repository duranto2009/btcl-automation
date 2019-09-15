package nix.monthlyusage;

import lli.monthlyUsage.LLIMonthlyUsageService;
import requestMapping.Service;
import util.DateUtils;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;

import java.text.ParseException;
import java.util.List;

public class NIXMonthlyUsageService 
{
	
	@Service
	NIXMonthlyUsageByClientService nixMonthlyUsageByClientService;
	@Service 
	NIXMonthlyUsageByConnectionService nixMonthlyUsageByConnectionService;
	@Service
	NIXMonthlyUsageGenerator nixMonthlyUsageGenerator;


	public NIXMonthlyUsageByClient getNIXMonthlyUsageByClient(long clientId, int month, int year) throws Exception
	{
		NIXMonthlyUsageByClient nixMonthlyUsageByClient = nixMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);

		
		if(nixMonthlyUsageByClient != null)
		{
			List<NIXMonthlyUsageByConnection> list = nixMonthlyUsageByConnectionService.getListByMonthlyUsageByClientId(nixMonthlyUsageByClient.getId());
			nixMonthlyUsageByClient.setMonthlyUsageByConnections(list);
		}
		return nixMonthlyUsageByClient;
	}
	

	public void generateLastMonthNIXMonthlyUsageForAll() throws Exception
	{
		nixMonthlyUsageGenerator.generateMonthlyUsage();
	}
	
	public void generateLastMonthNIXMonthlyUsage(List<ClientDetailsDTO> dtos) throws Exception
	{
		nixMonthlyUsageGenerator.generateMonthlyUsage(dtos);
	}
	
	public boolean isCurrentMonthlyUsageGenerated() throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);
		
		return nixMonthlyUsageByClientService.isMonthlyUsageGenerated(month, year);
		 
	}
	
	public boolean isCurrentMonthlyUsageGenerated(long clientId) throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);
		
		NIXMonthlyUsageByClient nixMonthlyUsageByClient = nixMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);
		
		return nixMonthlyUsageByClient == null ? false : true;
		 
	}

	public static void main(String[] args) throws Exception {
		ServiceDAOFactory.getService(NIXMonthlyUsageService.class).getNIXMonthlyUsageByClient(407003,11,2018);
	}
}
