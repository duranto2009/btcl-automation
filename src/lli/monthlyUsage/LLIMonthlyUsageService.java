package lli.monthlyUsage;

import java.text.ParseException;
import java.util.List;

import annotation.Transactional;
import requestMapping.Service;
import util.DateUtils;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;

public class LLIMonthlyUsageService 
{
	
	@Service
	LLIMonthlyUsageByClientService lliMonthlyUsageByClientService;
	@Service 
	LLIMonthlyUsageByConnectionService lliMonthlyUsageByConnectionService;
	@Service
	LLIMonthlyUsageGenerator lliMonthlyUsageGenerator;


	public LLIMonthlyUsageByClient getLLIMonthlyUsageByClient(long clientId, int month, int year) throws Exception
	{
		LLIMonthlyUsageByClient lliMonthlyUsageByClient = lliMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);

		
		if(lliMonthlyUsageByClient != null)
		{
			List<LLIMonthlyUsageByConnection> list = lliMonthlyUsageByConnectionService.getListByMonthlyUsageByClientId(lliMonthlyUsageByClient.getId());
			lliMonthlyUsageByClient.setMonthlyUsageByConnections(list);
		}
		return lliMonthlyUsageByClient;
	}
	

	public void generateLastMonthLLIMonthlyUsageForAll() throws Exception
	{
		lliMonthlyUsageGenerator.generateMonthlyUsage();
	}
	
	public void generateLastMonthLLIMonthlyUsage(List<ClientDetailsDTO> dtos) throws Exception
	{
		lliMonthlyUsageGenerator.generateMonthlyUsage(dtos);
	}
	
	public boolean isCurrentMonthlyUsageGenerated() throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);
		
		return lliMonthlyUsageByClientService.isMonthlyUsageGenerated(month, year);
		 
	}
	
	public boolean isCurrentMonthlyUsageGenerated(long clientId) throws ParseException
	{
		long firstDayPrevMonth = DateUtils.getFirstDayOfMonth(-1).getTime();
		int month = DateUtils.getMonthFromDate(firstDayPrevMonth);
		int year = DateUtils.getYearFromDate(firstDayPrevMonth);
		
		LLIMonthlyUsageByClient lliMonthlyUsageByClient = lliMonthlyUsageByClientService.getByClientIdAndMonth(clientId, month, year);
		
		return lliMonthlyUsageByClient == null ? false : true;
		 
	}


}
