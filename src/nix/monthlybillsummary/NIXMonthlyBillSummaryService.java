package nix.monthlybillsummary;

import annotation.Transactional;
import common.bill.BillDTO;
import common.bill.BillService;
import nix.monthlybill.NIXMonthlyBillByClient;
import nix.monthlyusage.NIXMonthlyUsageByClient;
import requestMapping.Service;
import util.DateUtils;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

public class NIXMonthlyBillSummaryService {
	
	@Service
	NIXMonthlyBillSummaryGenerator nixMonthlyBillSummaryGenerator;
	@Service
	NIXMonthlyBillSummaryByClientService nixMonthlyBillSummaryByClientService;
	@Service
	NIXMonthlyBillSummaryByItemService nixMonthlyBillSummaryByItemService;
	@Service
	BillService billService;

	public NIXMonthlyBillSummaryByClient getNIXMonthlyBillSummary(Long clientId, int month, int year) throws Exception
	{
		NIXMonthlyBillSummaryByClient clientSummary = nixMonthlyBillSummaryByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		if(clientSummary != null)
		{
			BillDTO billDTO = billService.getBillByBillID(clientSummary.getID());	//this is billID
			ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, clientSummary, BillDTO.class);
			
			List<NIXMonthlyBillSummaryByItem> list = nixMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSummary.getId());
			clientSummary.getNixMonthlyBillSummaryByItems().addAll(list);
		}
		
		
		return clientSummary;
	}
	
	public NIXMonthlyBillSummaryByClient getCurrentNIXMonthlyBillSummary(Long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return getNIXMonthlyBillSummary(clientId, month, year);
	}

	public void generateSummary(List<NIXMonthlyBillByClient> nixMonthlyBillByClients,
			List<NIXMonthlyUsageByClient> nixMonthlyUsageByClients) {

		List<NIXMonthlyBillSummaryByClient> lastMonthSummaries = new ArrayList<>();
		
		int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		
		for (NIXMonthlyBillByClient nixMonthlyBillByClient : nixMonthlyBillByClients)
		{
			NIXMonthlyBillSummaryByClient clientSumary = nixMonthlyBillSummaryByClientService.getByClientIdAndDateRange(
					nixMonthlyBillByClient.getClientId(),
					month,
					year);
			if(clientSumary != null)
			{
				clientSumary.setNixMonthlyBillSummaryByItems(nixMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSumary.getClientId()));
				lastMonthSummaries.add(clientSumary);
			}
		}
		List<NIXMonthlyBillSummaryByClient> summaries = nixMonthlyBillSummaryGenerator.generateSummary(nixMonthlyBillByClients, nixMonthlyUsageByClients, lastMonthSummaries);
		
//		ServiceDAOFactory.getService(NIXMonthlyBillNotificationService.class).notifyUsers(summaries);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyBillGenerated(int month, int year) 
	{
		return nixMonthlyBillSummaryByClientService.isMonthlyBillGenerated(month, year);
	}
}
