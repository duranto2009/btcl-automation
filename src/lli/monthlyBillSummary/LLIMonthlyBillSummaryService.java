package lli.monthlyBillSummary;

import annotation.Transactional;
import common.bill.BillDTO;
import common.bill.BillService;
import lli.monthlyBill.LLIMonthlyBillByClient;
import lli.monthlyBill.LLIMonthlyBillNotificationService;
import lli.monthlyUsage.LLIMonthlyUsageByClient;
import requestMapping.Service;
import util.DateUtils;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LLIMonthlyBillSummaryService {
	
	@Service
	LLIMonthlyBillSummaryGenerator lliMonthlyBillSummaryGenerator;
	@Service
	LLIMonthlyBillSummaryByClientService lliMonthlyBillSummaryByClientService;
	@Service
	LLIMonthlyBillSummaryByItemService lliMonthlyBillSummaryByItemService;
	@Service
	BillService billService;

	public LLIMonthlyBillSummaryByClient getLLIMonthlyBillSummary(Long clientId, int month, int year) throws Exception
	{
		LLIMonthlyBillSummaryByClient clientSummary = lliMonthlyBillSummaryByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		if(clientSummary != null)
		{
			BillDTO billDTO = billService.getBillByBillID(clientSummary.getID());	//this is billID
			ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, clientSummary, BillDTO.class);
			
			List<LLIMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSummary.getId());
			clientSummary.getLliMonthlyBillSummaryByItems().addAll(list);
		}
		
		
		return clientSummary;
	}



	public List<LLIMonthlyBillSummaryByClient> getLLIMonthlyBillSummaryByClient(Long clientId) throws Exception
	{
		List<LLIMonthlyBillSummaryByClient> clientSummary = lliMonthlyBillSummaryByClientService.getByClientId(clientId);

		if(clientSummary != null)
		{
			for (LLIMonthlyBillSummaryByClient lliMonthlyBillSummaryByClient:clientSummary
				 ) {
				BillDTO billDTO = billService.getBillByBillID(lliMonthlyBillSummaryByClient.getID());	//this is billID
				ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, lliMonthlyBillSummaryByClient, BillDTO.class);

				List<LLIMonthlyBillSummaryByItem> list = lliMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(lliMonthlyBillSummaryByClient.getId());
				lliMonthlyBillSummaryByClient.getLliMonthlyBillSummaryByItems().addAll(list);

			}

		}


		return clientSummary;
	}
	
	public LLIMonthlyBillSummaryByClient getCurrentLLIMonthlyBillSummary(Long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return getLLIMonthlyBillSummary(clientId, month, year);
	}

	public void generateSummary(List<LLIMonthlyBillByClient> lliMonthlyBillByClients,
			List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients) throws ParseException 
	{

		List<LLIMonthlyBillSummaryByClient> lastMonthSummaries = new ArrayList<>();
		
		int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		
		for (LLIMonthlyBillByClient lliMonthlyBillByClient : lliMonthlyBillByClients) 
		{
			LLIMonthlyBillSummaryByClient clientSumary = lliMonthlyBillSummaryByClientService.getByClientIdAndDateRange(
					lliMonthlyBillByClient.getClientId(),
					month,
					year);
			if(clientSumary != null)
			{
				clientSumary.setLliMonthlyBillSummaryByItems(lliMonthlyBillSummaryByItemService.getListByMonthlyBillSummaryByClientId(clientSumary.getId()));
				lastMonthSummaries.add(clientSumary);
			}
		}
		List<LLIMonthlyBillSummaryByClient> summaries = lliMonthlyBillSummaryGenerator.
				generateSummary(lliMonthlyBillByClients, lliMonthlyUsageByClients, lastMonthSummaries);
		
		ServiceDAOFactory.getService(LLIMonthlyBillNotificationService.class).notifyUsers(summaries);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isMonthlyBillGenerated(int month, int year) 
	{
		return lliMonthlyBillSummaryByClientService.isMonthlyBillGenerated(month, year);
	}
}
