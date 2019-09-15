package lli.monthlyBill;

import annotation.Transactional;
import common.RequestFailureException;
import common.bill.BillService;
import lli.LLIClientService;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClientService;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryService;
import lli.monthlyUsage.LLIMonthlyUsageByClient;
import lli.monthlyUsage.LLIMonthlyUsageGenerator;
import lli.monthlyUsage.LLIMonthlyUsageService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.DateUtils;
import util.NavigationService;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LLIMonthlyBillService implements NavigationService{

	static Logger logger = Logger.getLogger( LLIMonthlyBillService.class );
	
	@Service
	LLIClientService lliClientService;
	@Service 
	LLIMonthlyBillByClientService lliMonthlyBillByClientService;
	@Service 
	LLIMonthlyBillByConnectionService lliMonthlyBillByConnectionService;
	@Service
	LLIMonthlyBillGenerator lliMonthlyBillGenerator;
	@Service
	LLIMonthlyUsageGenerator lliMonthlyUsageGenerator;
	@Service 
	BillService billService;
	@Service
	LLIMonthlyBillNotificationService lliMonthlyBillNotificationService;
	@Service
	LLIMonthlyUsageService lliMonthlyUsageService;
	@Service
	LLIMonthlyBillSummaryService lliMonthlyBillSummaryService;
	@Service
	LLIMonthlyBillSummaryByClientService lliMonthlyBillSummaryByClientService;


//
//	public List<LLIMonthlyBillByClient> getMultipleMonthlyBillInrange(long ClientId,long fromDate,long Todate){
//
//	}

 	public LLIMonthlyBillByClient  getLLIMonthlyBillByClient(long clientId, int month, int year) throws Exception
	{
		LLIMonthlyBillByClient lliMonthlyBillByClient = lliMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		if(lliMonthlyBillByClient != null)
		{
			//BillDTO billDTO = billService.getBillByBillID(lliMonthlyBillByClient.getId());
			//ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, lliMonthlyBillByClient, BillDTO.class);
			
			List<LLIMonthlyBillByConnection> list = lliMonthlyBillByConnectionService.getListByMonthlyBillByClientId(lliMonthlyBillByClient.getId());
			lliMonthlyBillByClient.getMonthlyBillByConnections().addAll(list);
		}
		else {
			throw new RequestFailureException("No data found for the month of "+month);
		}
		return lliMonthlyBillByClient;
	}

	public LLIMonthlyBillByClient  getMultipleLLIMonthlyBillByClient(long clientId, int month, int year) throws Exception
	{
		LLIMonthlyBillByClient lliMonthlyBillByClient = lliMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);

		if(lliMonthlyBillByClient != null)
		{
			//BillDTO billDTO = billService.getBillByBillID(lliMonthlyBillByClient.getId());
			//ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, lliMonthlyBillByClient, BillDTO.class);

			List<LLIMonthlyBillByConnection> list = lliMonthlyBillByConnectionService.getListByMonthlyBillByClientId(lliMonthlyBillByClient.getId());
			lliMonthlyBillByClient.getMonthlyBillByConnections().addAll(list);
		}
		return lliMonthlyBillByClient;
	}
	
	
	public void generateCurrentLLIMonthlyBillForAll() throws Exception
	{
		List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients = new ArrayList<>();
		List<LLIMonthlyBillByClient> lliMonthlyBillByClients = new ArrayList<>();

		if(isCurrentMonthlyBillSummaryGenerated())
			return;
		
		if(lliMonthlyUsageService.isCurrentMonthlyUsageGenerated() == false)
		{
			lliMonthlyUsageByClients = lliMonthlyUsageGenerator.generateMonthlyUsage();
		}
		//TODO else fetch generated usages
			
		if(isCurrentMonthlyBillGenerated() == false)
		{
			lliMonthlyBillByClients = lliMonthlyBillGenerator.generateMonthlyBill();
		}
		
		
		lliMonthlyBillSummaryService.generateSummary(lliMonthlyBillByClients, lliMonthlyUsageByClients);
	}
	
	public void generateCurrentLLIMonthlyBill(List<Long> clientIds) throws Exception
	{
			
		List<ClientDetailsDTO> clientsToGenerateUsage = new ArrayList<>();
		List<ClientDetailsDTO> clientsToGenerateBill = new ArrayList<>();
		
		List<Long> clientsToFetchUsage = new ArrayList<>();
		List<Long> clientsToFetchBill = new ArrayList<>();
		
		for (Long clientId : clientIds)
		{
			ClientDetailsDTO clientDetailsDTO  = lliClientService.getLLIClient(clientId);
			if(clientDetailsDTO == null)
			{
				logger.warn("lli clientId " + clientId + " not found for generate monthly bill individual");
				continue;
			}
			else
			{
				if(lliMonthlyUsageService.isCurrentMonthlyUsageGenerated(clientId) == false)
					clientsToGenerateUsage.add(clientDetailsDTO);
				else
					clientsToFetchUsage.add(clientId);
					
				if(isCurrentMonthlyBillGenerated(clientId) == false)
					clientsToGenerateBill.add(clientDetailsDTO);
				else
					clientsToFetchBill.add(clientId);
			}
			
		}

        List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients = new ArrayList<>();
		if(clientsToGenerateUsage.size() > 0)
		    lliMonthlyUsageByClients = lliMonthlyUsageGenerator.generateMonthlyUsage(clientsToGenerateUsage);
		
		int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		
		for(long clientId : clientsToFetchUsage)
		{
			lliMonthlyUsageByClients.add(lliMonthlyUsageService.getLLIMonthlyUsageByClient(clientId, month, year));
		}

        List<LLIMonthlyBillByClient> lliMonthlyBillByClients = new ArrayList<>();
		if(clientsToGenerateBill.size() > 0)
		    lliMonthlyBillByClients = lliMonthlyBillGenerator.generateMonthlyBill(clientsToGenerateBill);
		
		month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		for(long clientId : clientsToFetchBill)
		{
			lliMonthlyBillByClients.add(getLLIMonthlyBillByClient(clientId, month, year));
		}
		
		
		lliMonthlyBillSummaryService.generateSummary(lliMonthlyBillByClients, lliMonthlyUsageByClients);
		
	}
	
	
	public boolean isCurrentMonthlyBillGenerated() throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return lliMonthlyBillByClientService.isMonthlyBillGenerated(month, year);
		
	}
	
	public boolean isCurrentMonthlyBillGenerated(long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		LLIMonthlyBillByClient lliMonthlyBillByClient = lliMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		return lliMonthlyBillByClient == null ? false : true;
		 
	}

	public boolean isCurrentMonthlyBillSummaryGenerated() throws ParseException
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return lliMonthlyBillSummaryService.isMonthlyBillGenerated(month, year);
	}
	
	public boolean isCurrentMonthlyBillSummaryGenerated(long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return lliMonthlyBillSummaryService.getLLIMonthlyBillSummary(clientId, month, year) == null ? false : true;
	}

	/*Search Application begins*/
	@SuppressWarnings("rawtypes")
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		List<ClientDetailsDTO> lliClients = lliClientService.getAllLLIClient();
		
		String dateStr = (String) searchCriteria.get("Month");
		int month=-1,year=-1;
		if(dateStr!=null) {
			SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat( "yyyy-MM" );
			Date date = dayMonthYearDateFormat.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
		}
		String clientId= (String) searchCriteria.get("clientID");
		
		ArrayList<Long> ids = new ArrayList<>();
	
		
		if(clientId!=null && clientId.length()>0) {
			long cId = Long.parseLong(clientId);
			LLIMonthlyBillSummaryByClient clientSummary = lliMonthlyBillSummaryByClientService.getByClientIdAndDateRange(cId, month, year);
			
			if(clientSummary == null) {
				ids.add(cId);
			}
		}
		else {
			List<Long> clientIdsOfMonthlyBillSummary = lliMonthlyBillSummaryByClientService.getClientIdsByDateRange(month, year);
			
			for (ClientDetailsDTO lliClient : lliClients) 
			{
				if(clientIdsOfMonthlyBillSummary.contains(lliClient.getClientID()) == false)
				{
					ids.add(lliClient.getClientID());
				}
			}
		}
		return ids;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return lliClientService.getLLIClients(recordIDs);
	}
	/*Search Application ends*/
	
	
	
}
