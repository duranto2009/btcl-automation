package nix.monthlybill;

import annotation.Transactional;
import common.RequestFailureException;
import common.bill.BillService;

import login.LoginDTO;
import nix.common.NIXClientService;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClientService;
import nix.monthlybillsummary.NIXMonthlyBillSummaryService;
import nix.monthlyusage.NIXMonthlyUsageByClient;
import nix.monthlyusage.NIXMonthlyUsageGenerator;
import nix.monthlyusage.NIXMonthlyUsageService;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.*;
import vpn.client.ClientDetailsDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NIXMonthlyBillService implements NavigationService{

	static Logger logger = Logger.getLogger( NIXMonthlyBillService.class );
	
	@Service
	NIXClientService nixClientService;
	@Service 
	NIXMonthlyBillByClientService nixMonthlyBillByClientService;
	@Service 
	NIXMonthlyBillByConnectionService nixMonthlyBillByConnectionService;
	@Service
	NIXMonthlyBillGenerator nixMonthlyBillGenerator;
	@Service
	NIXMonthlyUsageGenerator nixMonthlyUsageGenerator;
	@Service 
	BillService billService;
//	@Service
//	NIXMonthlyBillNotificationService nixMonthlyBillNotificationService;
	@Service
	NIXMonthlyUsageService nixMonthlyUsageService;
	@Service
	NIXMonthlyBillSummaryService nixMonthlyBillSummaryService;
	@Service
	NIXMonthlyBillSummaryByClientService nixMonthlyBillSummaryByClientService;

 	public NIXMonthlyBillByClient  getNIXMonthlyBillByClient(long clientId, int month, int year) throws Exception
	{
		NIXMonthlyBillByClient nixMonthlyBillByClient = nixMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		if(nixMonthlyBillByClient != null)
		{
			//BillDTO billDTO = billService.getBillByBillID(nixMonthlyBillByClient.getId());
			//ModifiedSqlGenerator.populateObjectFromOtherObject(billDTO, nixMonthlyBillByClient, BillDTO.class);
			
			List<NIXMonthlyBillByConnection> list = nixMonthlyBillByConnectionService.getListByMonthlyBillByClientId(nixMonthlyBillByClient.getId());
			nixMonthlyBillByClient.getMonthlyBillByConnections().addAll(list);
		}
		else {
			throw new RequestFailureException("No data found for the month of "+month);
		}
		return nixMonthlyBillByClient;
	}
	
	
	
	public void generateCurrentNIXMonthlyBillForAll() throws Exception
	{
		List<NIXMonthlyUsageByClient> nixMonthlyUsageByClients = new ArrayList<>();
		List<NIXMonthlyBillByClient> nixMonthlyBillByClients = new ArrayList<>();
//
//		if(isCurrentMonthlyBillSummaryGenerated())
//			return;
		
//		if(nixMonthlyUsageService.isCurrentMonthlyUsageGenerated() == false)
//		{
			nixMonthlyUsageByClients = nixMonthlyUsageGenerator.generateMonthlyUsage();
//		}
		//TODO else fetch generated usages
			
//		if(isCurrentMonthlyBillGenerated() == false)
//		{
			nixMonthlyBillByClients = nixMonthlyBillGenerator.generateMonthlyBill();
//		}
		
		
		nixMonthlyBillSummaryService.generateSummary(nixMonthlyBillByClients, nixMonthlyUsageByClients);
	}
	
	public void generateCurrentNIXMonthlyBill(List<Long> clientIds) throws Exception
	{
			
		List<ClientDetailsDTO> clientsToGenerateUsage = new ArrayList<>();
		List<ClientDetailsDTO> clientsToGenerateBill = new ArrayList<>();
		
		List<Long> clientsToFetchUsage = new ArrayList<>();
		List<Long> clientsToFetchBill = new ArrayList<>();
		
		for (Long clientId : clientIds)
		{
			ClientDetailsDTO clientDetailsDTO  = (ClientDetailsDTO) nixClientService.getNIXClient(clientId);
			if(clientDetailsDTO == null)
			{
				logger.warn("nix clientId " + clientId + " not found for generate monthly bill individual");
				continue;
			}
			else
			{
				if(nixMonthlyUsageService.isCurrentMonthlyUsageGenerated(clientId) == false)
					clientsToGenerateUsage.add(clientDetailsDTO);
				else
					clientsToFetchUsage.add(clientId);
					
				if(isCurrentMonthlyBillGenerated(clientId) == false)
					clientsToGenerateBill.add(clientDetailsDTO);
				else
					clientsToFetchBill.add(clientId);
			}
			
		}

        List<NIXMonthlyUsageByClient> nixMonthlyUsageByClients = new ArrayList<>();
		if(clientsToGenerateUsage.size() > 0)
		    nixMonthlyUsageByClients = nixMonthlyUsageGenerator.generateMonthlyUsage(clientsToGenerateUsage);
		
		int month = DateUtils.getMonthFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		int year = DateUtils.getYearFromDate(DateUtils.getFirstDayOfMonth(-1).getTime());
		
		for(long clientId : clientsToFetchUsage)
		{
			nixMonthlyUsageByClients.add(nixMonthlyUsageService.getNIXMonthlyUsageByClient(clientId, month, year));
		}

        List<NIXMonthlyBillByClient> nixMonthlyBillByClients = new ArrayList<>();
		if(clientsToGenerateBill.size() > 0)
		    nixMonthlyBillByClients = nixMonthlyBillGenerator.generateMonthlyBill(clientsToGenerateBill);
		
		month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		for(long clientId : clientsToFetchBill)
		{
			nixMonthlyBillByClients.add(getNIXMonthlyBillByClient(clientId, month, year));
		}
		
		
		nixMonthlyBillSummaryService.generateSummary(nixMonthlyBillByClients, nixMonthlyUsageByClients);
		
	}
	
	
	public boolean isCurrentMonthlyBillGenerated() throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return nixMonthlyBillByClientService.isMonthlyBillGenerated(month, year);
		
	}
	
	public boolean isCurrentMonthlyBillGenerated(long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		NIXMonthlyBillByClient nixMonthlyBillByClient = nixMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);
		
		return nixMonthlyBillByClient == null ? false : true;
		 
	}

	public boolean isCurrentMonthlyBillSummaryGenerated() throws ParseException
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return nixMonthlyBillSummaryService.isMonthlyBillGenerated(month, year);
	}
	
	public boolean isCurrentMonthlyBillSummaryGenerated(long clientId) throws Exception
	{
		int month = DateUtils.getMonthFromDate(DateUtils.getCurrentTime());
		int year = DateUtils.getYearFromDate(DateUtils.getCurrentTime());
		
		return nixMonthlyBillSummaryService.getNIXMonthlyBillSummary(clientId, month, year) == null ? false : true;
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
		List<ClientDetailsDTO> nixClients = nixClientService.getAllNIXClient();
		
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
			NIXMonthlyBillSummaryByClient clientSummary = nixMonthlyBillSummaryByClientService.getByClientIdAndDateRange(cId, month, year);
			
			if(clientSummary == null) {
				ids.add(cId);
			}
		}
		else {
			List<Long> clientIdsOfMonthlyBillSummary = nixMonthlyBillSummaryByClientService.getClientIdsByDateRange(month, year);
			
			for (ClientDetailsDTO nixClient : nixClients)
			{
				if(clientIdsOfMonthlyBillSummary.contains(nixClient.getClientID()) == false)
				{
					ids.add(nixClient.getClientID());
				}
			}
		}
		return ids;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return nixClientService.getNIXClients(recordIDs);
	}
	/*Search Application ends*/

	public static void main(String[] args) throws Exception {
		CurrentTimeFactory.initializeCurrentTimeFactory();


		ServiceDAOFactory.getService(NIXMonthlyBillService.class).generateCurrentNIXMonthlyBillForAll();
		System.out.println("done");
	}
	
	
	
}
