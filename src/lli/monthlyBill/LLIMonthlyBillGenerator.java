package lli.monthlyBill;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import accounting.AccountingEntryService;
import accounting.AccountingIncidentService;
import annotation.Transactional;
import client.ClientTypeService;
import common.ModuleConstants;
import costConfig.CostCellDTO;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import inventory.InventoryConstants;
import inventory.InventoryService;
import lli.LLIClientService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.LLILocalLoop;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import lli.LLIOffice;
import lli.client.td.LLIProbableTDClientService;
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lli.connection.LLIConnectionConstants;
import common.pdf.PdfService;
import requestMapping.Service;
import util.ColumnDTO;
import util.DateUtils;
import util.NumberUtils;
import util.RowDTO;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

public class LLIMonthlyBillGenerator {

	static Logger logger = Logger.getLogger( LLIMonthlyBillGenerator.class );
	
	@Service
	LLIClientService lliClientService;
	@Service 
	LLIConnectionService lliConnectionService;
	@Service
	DistrictOfcInstallationService districtOfcInstallationService;
	@Service
	LLILongTermService lliLongTermService;
	@Service
	CostConfigService costConfigService;
	@Service
	LLICostConfigurationService lliCostConfigurationService;
	@Service
	InventoryService inventoryService;
	@Service
	AccountingIncidentService accountingIncidentService;
	@Service
	AccountingEntryService accountingEntryService;
	@Service
	OfcInstallationCostService ofcInstallationCostService;
	@Service
	LLIProbableTDClientService lliProbableTDClientService;
	@Service
	PdfService pdfService;
	
	@Service 
	LLIMonthlyBillByClientService lliMonthlyBillByClientService;
	@Service 
	LLIMonthlyBillByConnectionService lliMonthlyBillByConnectionService;
	
	List<ClientDetailsDTO> lliClients = new ArrayList<>();
	List<LLIConnectionInstance> lliConnections = new ArrayList<>();
	List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
	Map<Long, TableDTO> mapOfTableDTOs = new HashMap<>();
	
	
	double cacheFlatRate = 0;
	double vatRate = 0;	
	int localLoopMinimumCost = 0;
	int localLoopDistanceCheckpoint = 0;
	
	public LLIMonthlyBillGenerator()
	{
		
	}
	
	//@Transactional(transactionType=util.TransactionType.READONLY)
	private void loadData()
	{
		getAllClient();
		getAllConnection();
		getAllDistrictOfcInstallationCost();
		
		try {
			LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
			cacheFlatRate = lliFixedCostConfigurationDTO.getCacheServiceFlatRate();
			vatRate = lliFixedCostConfigurationDTO.getMaximumVatPercentage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.warn("cacheFlatRate : " + cacheFlatRate + " ; vatRate : " + vatRate);
		
		OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostService.getLatestByDate(DateUtils.getCurrentTime());
		
		if(ofcInstallationCostDTO != null)
		{
			localLoopMinimumCost = ofcInstallationCostDTO.getFiberCost();
			localLoopDistanceCheckpoint = ofcInstallationCostDTO.getFiberLength();
		}
	}
	
	private void clearData()
	{
		lliClients.clear();
		lliConnections.clear();
		districtOfcInstallationDTOs.clear();
		mapOfTableDTOs.clear();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllClient()
	{
		lliClients.clear();
		lliClients.addAll(lliClientService.getAllLLIClient());
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllConnection()
	{
		long lastDateLastHourOfLastMonth = DateUtils.getLastDayOfMonth(-1).getTime();
		long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);
		
		try {
			
			lliConnections = lliConnectionService.getLLIConnectionInstanceListByDateRange(lastDateFirstHourOfLastMonth, lastDateLastHourOfLastMonth)
					.stream()
					.filter(x->x.getStartDate() <= lastDateLastHourOfLastMonth)
					.collect(Collectors.toList());

		} catch (Exception e) {
			logger.fatal("error in getAllConnection");
		}
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllDistrictOfcInstallationCost()
	{
		try {
			districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();
		} catch (Exception e) {
			logger.fatal("error in getAllDistrictOfcInstallationCost");
		}
	}
	

	public List<LLIMonthlyBillByClient> generateMonthlyBill() throws Exception
	{
				
		List<LLIMonthlyBillByClient> lliMonthlyBillByClients = generateMonthlyBill(lliClients);
		
		return lliMonthlyBillByClients;
	}
	

	public List<LLIMonthlyBillByClient> generateMonthlyBill(List<ClientDetailsDTO> clients)
	{
		loadData();
		
		List<LLIMonthlyBillByClient> lliMonthlyBillByClients = new ArrayList<>();
		for(ClientDetailsDTO client : clients)
		{
			try 
			{
				LLIMonthlyBillByClient bill = populateMonthlyBill(client.getClientID());
				
				if(bill.getMonthlyBillByConnections().isEmpty()) {
					continue;
				}
				calculateBill(bill, vatRate);
				save(bill);
				
				//calculateTDDate(bill);
				//insertIntoAccounting(bill);
				
				lliMonthlyBillByClients.add(bill);
			}
			catch (Exception e) {
				logger.fatal("failed to store in db LLIMonthlyBillByClient for " + client.getClientID());
			}
		}
		
		clearData();
		
		return lliMonthlyBillByClients;
	}
	
	/*@Transactional
	private void calculateTDDate(LLIMonthlyBillByClient client) throws Exception
	{
		//get security money balance info from table
		double securityAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
				client.getClientId(),
				AccountType.SECURITY.getID());
		
		//get adjustment balance info from table
		double balance = accountingEntryService.getBalanceByClientIDAndAccountID(
				client.getClientId(), 
				AccountType.ADJUSTABLE.getID());
		
		//get total due info from table
		double receivableAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
				client.getClientId(),
				AccountType.ACCOUNT_RECEIVABLE_TD.getID());
		
		
		double availableAmount = securityAmount + balance - receivableAmount;
		
		double perDayCost = client.getGrandTotal()/30;
		int remainingDays = perDayCost <= 0 ? 0 : (int) (availableAmount / perDayCost);
		
		
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, remainingDays);
		long tdDate = cal.getTime().getTime();
		
		lliProbableTDClientService.updateTDDate(client.getClientID(), tdDate);
		
	}*/
	
	/*private void saveIntoDB(List<LLIMonthlyBillByClient> lliMonthlyBillByClients) throws Exception
	{
		for(LLIMonthlyBillByClient client : lliMonthlyBillByClients)
		{
			try {
				save(client);
			} catch (Exception e) {
				logger.fatal("failed to store in db LLIMonthlyBillByClient");
			}
		}
		//logger.warn("--------------STARTING PDF GENERATION--------------");
		//LLIAsyncMonthlyBillPdfService.getInstance().createMonthlyBillPDFForAllClient(lliMonthlyBillByClients);
		//logger.warn("--------------DONE PDF GENERATION--------------");
	}*/
	
	@Transactional(transactionType=TransactionType.INDIVIDUAL_TRANSACTION)
	private void save(LLIMonthlyBillByClient lliMonthlyBillByClient) throws Exception
	{
		lliMonthlyBillByClientService.save(lliMonthlyBillByClient);
		if(lliMonthlyBillByClient.getId() == null)
			return;
		
		for	(LLIMonthlyBillByConnection connection : lliMonthlyBillByClient.getMonthlyBillByConnections())
		{
			connection.setMonthlyBillByClientId(lliMonthlyBillByClient.getId());
			lliMonthlyBillByConnectionService.save(connection);
		}
	}
	
	/*@Transactional
	private void insertIntoAccounting(LLIMonthlyBillByClient lliMonthlyBillByClient) throws Exception {
		AccountingLogic accountingLogic = lliMonthlyBillByClient.getClass().getAnnotation(AccountingLogic.class);
		
		if(accountingLogic != null &&  accountingLogic.value().newInstance() instanceof GenerateBill  ){
			GenerateBill generateMonthlyBill = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
			generateMonthlyBill.generateMonthlyBill(lliMonthlyBillByClient);
		}
	}*/

	//@Transactional(transactionType=util.TransactionType.READONLY)
	private LLIMonthlyBillByClient populateMonthlyBill(long clientId) throws Exception
	{
		
		Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());
		double  totalMbps = 0,
				totalCache = 0;
		
		
		LLIMonthlyBillByClient bill = new LLIMonthlyBillByClient();
		bill.setClientId(clientId);
		
		// get connections
		List<LLIConnectionInstance> lliConnectionInstanceList = 
				lliConnections.stream()
				.filter(x-> x.getClientID() == clientId)
				.collect(Collectors.toList());
		
		//loop cost calculation and info 
		for (LLIConnectionInstance connection : lliConnectionInstanceList)
		{
			
			LLIMonthlyBillByConnection lliMonthlyConnection = new LLIMonthlyBillByConnection();
			
			lliMonthlyConnection.setConnectionId(connection.getID());
			lliMonthlyConnection.setClientId(connection.getClientID());
			lliMonthlyConnection.setName(connection.getName());
			//lliMonthlyConnection.setAddress(connection.getAddress());
			lliMonthlyConnection.setCreatedDate(date.getTime());
			lliMonthlyConnection.setStatus(connection.getStatus());
			lliMonthlyConnection.setType(connection.getConnectionType());
			
			lliMonthlyConnection.setTotalMbps(connection.getBandwidth());
			lliMonthlyConnection.setDiscountRate(connection.getDiscountRate());
			lliMonthlyConnection.setVatRate(vatRate);
			
			logger.info("mbps : " + lliMonthlyConnection.getTotalMbps() + "; type : " + connection.getConnectionType());
			
			//if connection closed, then no bill
			if(connection.getStatus() == LLIConnectionConstants.STATUS_CLOSED)
				continue;
			
			//if TD then only loop cost to be calculated
			else if(connection.getStatus() == LLIConnectionConstants.STATUS_TD)
			{
				lliMonthlyConnection.setLocalLoopBreakDowns(getLocalLoopBreakDown(connection, localLoopDistanceCheckpoint, localLoopMinimumCost));
			}
			else if(connection.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
			{
				lliMonthlyConnection.setLocalLoopBreakDowns(getLocalLoopBreakDown(connection, localLoopDistanceCheckpoint, localLoopMinimumCost));
				totalMbps += connection.getBandwidth();
			}
			else if(connection.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
			{
				lliMonthlyConnection.setLocalLoopBreakDowns(getLocalLoopBreakDown(connection, localLoopDistanceCheckpoint, localLoopMinimumCost));
				totalCache += connection.getBandwidth();
			}
			else
				continue;
				
			double loopCost = lliMonthlyConnection.getLocalLoopBreakDowns().stream().mapToDouble(loop->loop.getCost()).sum();
			lliMonthlyConnection.setLoopCost(NumberUtils.formattedValue(loopCost));
			//totalLoopCost += loopCost;
			
			bill.getMonthlyBillByConnections().add(lliMonthlyConnection);
		}
		
		
		//set billing range
		TableDTO tableDTO = getTableDTO(clientId);
		if(tableDTO == null)
			return bill;
		
		calculateBWAndLTCost(bill,tableDTO, totalMbps, totalCache);
				
		
		
		bill.setCreatedDate(date.getTime());
		
		bill.setDeleted(false);
		bill.setMonth(DateUtils.getMonthFromDate(date.getTime()));
		bill.setYear(DateUtils.getYearFromDate(date.getTime()));
		
		
		//TODO :need to get yearly fee for additional ip

		return bill;
	}
	
	
	private void calculateBWAndLTCost(
			LLIMonthlyBillByClient bill,
			TableDTO tableDto, 
			double totalMbps,
			double totalCache) 
	{
		//set total bandwidth
		MbpsBreakDown totalMbpsBreakDown = new MbpsBreakDown();
		totalMbpsBreakDown.setValue(totalMbps);
		bill.setTotalMbpsBreakDown(totalMbpsBreakDown);
		logger.info("totalMbpsBreakDown : " + totalMbps);
		
		//set total cache
		MbpsBreakDown totalMbpsBreakDownForCache = new MbpsBreakDown();
		totalMbpsBreakDownForCache.setValue(totalCache);
		bill.setTotalMbpsBreakDownForCache(totalMbpsBreakDownForCache);
		logger.info("totalMbpsBreakDownForCache : " + totalCache);
				
				
		//set billing range
		bill.setBillingRangeBreakDown(getBillingRangeBreakDown(tableDto, totalMbps, false));
		
		BillingRangeBreakDown range = new BillingRangeBreakDown();
		range.setRate(cacheFlatRate);
		bill.setBillingRangeBreakDownForCache(range);
		
		
		calculateBandwidthCost(bill, tableDto);
		
		calculateLongTermContract(bill, tableDto, totalMbps);
		
		
	}
	
	private void calculateBandwidthCost(LLIMonthlyBillByClient bill, TableDTO tableDto) 
	{
		//bandwidth cost calculation
		for (LLIMonthlyBillByConnection connection : bill.getMonthlyBillByConnections())
		{
			
			if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
			{
				connection.setMbpsRate(bill.getBillingRangeBreakDown().getRate());
				
				if(connection.getStatus() == LLIConnectionConstants.STATUS_ACTIVE)
					connection.setMbpsCost(connection.getTotalMbps() * connection.getMbpsRate());
				else
					connection.setMbpsCost(0);
				
				//totalMbpsCost += connection.getMbpsCost();
			}
			
			else if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
			{
				connection.setMbpsRate(cacheFlatRate);
				if(connection.getStatus() == LLIConnectionConstants.STATUS_ACTIVE)
					connection.setMbpsCost(connection.getTotalMbps()* connection.getMbpsRate());
				else 
					connection.setMbpsCost(0);
				
				//totalCacheCost += connection.getMbpsCost();
			}
				

			//discount only on mbps cost
			/*connection.setDiscount(formattedValue(connection.getMbpsCost() * connection.getDiscountRate()/100));
			connection.setGrandCost(formattedValue(connection.getLoopCost() + connection.getMbpsCost() - connection.getDiscount()));
			
			connection.setVat(formattedValue(connection.getGrandCost() * connection.getVatRate()/100));
			connection.setTotalCost(formattedValue(connection.getGrandCost() + connection.getVat()));
			*/
			//set json contents
			connection.setConnectionType();
			connection.setConnectionStatus();
			connection.setTotalMonthlyFees();
			connection.setConcatenatedRemark();
			connection.setLocalLoopBreakDownsContent();
		}
	}
	
	private void calculateLongTermContract(LLIMonthlyBillByClient bill, TableDTO tableDto, double totalMbps) 
	{
		
		//longterm contract
		LLILongTermContractBreakDown lTContract = getLongTremContractBreakDown(bill.getClientId(), tableDto, totalMbps);
		bill.setLongTermContractBreakDown(lTContract);
		
		double totalLongTerm = lTContract.getValue();

		//bill.getMonthlyBillByConnections().sort(
		//		(x,y) -> x.getDiscountRate() > y.getDiscountRate() ? -1 : 1 );
		
		List<LLIMonthlyBillByConnection> list = bill.getMonthlyBillByConnections()
				.stream()
				.filter(x->x.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
				.sorted((x,y) -> x.getDiscountRate() > y.getDiscountRate() ? -1 : 1)
				.collect(Collectors.toList());
		
		
		double ltAdjustableAmount = 0, ltcDiscountAdjustment = 0;
		
		for(LLIMonthlyBillByConnection c : list)
		{
			if(totalLongTerm <= c.getTotalMbps())
			{
				double value = totalLongTerm * (c.getMbpsRate() - lTContract.getRate());
				double discount = value * c.getDiscountRate()/100;
				ltAdjustableAmount +=  value - discount;
				ltcDiscountAdjustment += discount;
				break;
			}
			else
			{
				double value = c.getTotalMbps() * (c.getMbpsRate() - lTContract.getRate());
				double discount = value * c.getDiscountRate()/100;
				ltAdjustableAmount +=  value - discount; 
				ltcDiscountAdjustment += discount;
				totalLongTerm -= c.getTotalMbps();
			}
		}
		
		//long term adjustment
		//ltAdjustableAmount = lTContract.getValue() * 
		//		(bill.getBillingRangeBreakDown().getRate() - lTContract.getRate());
		
		bill.setLongTermContructAdjustment(NumberUtils.formattedValue(ltAdjustableAmount));
		bill.setLongTermContructDiscountAdjustment(NumberUtils.formattedValue(ltcDiscountAdjustment));
		
	}
	
	
	@Transactional(transactionType = TransactionType.READONLY)
	private TableDTO getTableDTO(long clientId) throws Exception {
		//set billing range
		Integer tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_LLI, clientId);
		
		TableDTO tableDTO = null;
		if(mapOfTableDTOs.containsKey(tariffCategory))
		{
			tableDTO = mapOfTableDTOs.get(tariffCategory);
		}
		else
		{
			try {
				tableDTO = costConfigService.getLatestTableWithCategoryID(ModuleConstants.Module_ID_LLI, tariffCategory);
			} catch (Exception e) {
				
			}
		}
			
		return tableDTO;
	}
	
	private LLILongTermContractBreakDown getLongTremContractBreakDown(long clientId, TableDTO tableDTO, double clientTotalMbps)
	{
		double longTermContractMb = 0;
		List<LLILongTermContract> lliLongTermContracts = new ArrayList<>();
		try {
			lliLongTermContracts = lliLongTermService.getActiveLLILongTermContractListByClientID(clientId);
		} catch (Exception e) {
			logger.info("issue in fatching long term contract for monthly bill for client id = " + clientId);
		}
		if(lliLongTermContracts != null)
		{
			for(LLILongTermContract item : lliLongTermContracts)
				longTermContractMb += item.getBandwidth();
		}
		
		LLILongTermContractBreakDown lTContract = new LLILongTermContractBreakDown();
		lTContract.setValue(longTermContractMb);
		
		if(longTermContractMb > 0)
			lTContract.setRate(getBillingRangeBreakDown(tableDTO, clientTotalMbps, true).getRate());
		else
			lTContract.setRate(0);
		
		return lTContract;
	}
	
	private void calculateBill(LLIMonthlyBillByClient bill, double vatRate) throws Exception
	{
		
		double grandTotal = 0;
		double totalDiscount = 0;
		
		for (LLIMonthlyBillByConnection connection : bill.getMonthlyBillByConnections())
		{
			
			connection.setDiscount(NumberUtils.formattedValue(connection.getMbpsCost()* connection.getDiscountRate()/100));
			
			//here grandcost is after discount
			connection.setGrandCost(NumberUtils.formattedValue(connection.getLoopCost() + connection.getMbpsCost() - connection.getDiscount()));
			
			connection.setVat(NumberUtils.formattedValue(connection.getGrandCost()* connection.getVatRate()/100));
			connection.setTotalCost(NumberUtils.formattedValue(connection.getGrandCost() + connection.getVat()));
			
			grandTotal += connection.getGrandCost();
			totalDiscount += connection.getDiscount();
		}
		
		totalDiscount = totalDiscount - bill.getLongTermContructDiscountAdjustment();
		bill.setGrandTotal(NumberUtils.formattedValue(grandTotal));

		bill.setDiscountPercentage(0);
		bill.setDiscount(NumberUtils.formattedValue(totalDiscount));
		
		bill.setTotalPayable(NumberUtils.formattedValue(bill.getGrandTotal() - bill.getLongTermContructAdjustment()));
		
		bill.setVatPercentage(vatRate);
		bill.setVAT(NumberUtils.formattedValue(bill.getTotalPayable()* bill.getVatPercentage()/100));
		
		
		bill.setNetPayable(NumberUtils.formattedValue(bill.getTotalPayable() + bill.getVAT()));
		
	}
	
	/*@Transactional(transactionType=util.TransactionType.READONLY)
	private double getRegularLoopCost(LLIConnectionInstance connection, double initialLoopLength, double initialLoopCost) throws Exception
	{
		double loopCostOfConnection = 0;
		for(LLIOffice office : connection.getLliOffices())
		{
			for(LLILocalLoop loop : office.getLocalLoops())
			{
				double loopLength = loop.getOCDistance() + loop.getBtclDistance();
				if(loopLength == 0)
					continue;
				
				//initial flat rate
				double loopCost = initialLoopCost;
				if(loopLength > initialLoopLength)
				{	
					long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(loop.getVlanID()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();					
					Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x->x.getDistrictID() == districtID).findFirst();
					
					loopLength -= initialLoopLength;
					loopCost += loopLength * (ofcInstallationCostByDistrictID.isPresent() ? ofcInstallationCostByDistrictID.get().getInstallationCost() : 0);
				}
					
				//calculation using OFC type
				loopCost *= loop.getOfcType();
				loopCostOfConnection += loopCost; 
			}
		}
	
		return loopCostOfConnection;
	}*/
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	private List<LLILocalLoopBreakDown> getLocalLoopBreakDown(LLIConnectionInstance connection, double initialLoopLength, double initialLoopCost) throws Exception
	{
		List<LLILocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();
		
		for(LLIOffice office : connection.getLliOffices())
		{
			for(LLILocalLoop loop : office.getLocalLoops())
			{
				double loopLength = loop.getOCDistance() + loop.getBtclDistance();
				//skip local loop if it is being closed
				if(loopLength == 0 || (loop.getIs_deleted()==1))
					continue;
				
				LLILocalLoopBreakDown localLoopBreakDown = new LLILocalLoopBreakDown();
				localLoopBreakDown.setConnectionId(connection.getID());
				localLoopBreakDown.setFiberCableType(loop.getOfcType());
				localLoopBreakDown.setId(loop.getID());
				localLoopBreakDown.setOfficeId(loop.getLliOfficeHistoryID());
				localLoopBreakDown.setPortID(loop.getPortID());
				localLoopBreakDown.setValue(loopLength);
				
				//initial flat rate
				long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(loop.getPortID()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();
				Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x->x.getDistrictID() == districtID).findFirst();
				localLoopBreakDown.setRate(ofcInstallationCostByDistrictID.isPresent() ? ofcInstallationCostByDistrictID.get().getInstallationCost() : 0);
				
				double loopCost = initialLoopCost;
				loopLength -= initialLoopLength;
				if(loopLength > 0)
				{	
					loopCost += loopLength * localLoopBreakDown.getRate();
				}
					
				//calculation using OFC type
				loopCost *= loop.getOfcType();
				//loopCostOfConnection += loopCost; 
				
				localLoopBreakDown.setCost(loopCost);
				
				localLoopBreakDowns.add(localLoopBreakDown);
			}
		}
	
		return localLoopBreakDowns;
	}
	
	private BillingRangeBreakDown getBillingRangeBreakDown(TableDTO tableDTO, double mb, boolean isLongTerm )
	{
		long columnId = -1;
		long rowId = -1;
		
		//initially regular was 1-4 yrs and longterm was more than 5.
		int year = isLongTerm ? 10 : 1;
		
		BillingRangeBreakDown billingRangeBreakDown = new BillingRangeBreakDown();
		
		for (ColumnDTO column : tableDTO.getColumnDTOs()) {
			
			if ( column.getLowerRange() <= year && column.getUpperRange() >= year ) {
				columnId = column.getID();
			}
		}
		
		for( RowDTO row: tableDTO.getRowDTOs() ) {
			
			if( row.getLowerRange() <= mb && row.getUpperRange() >= mb ) {
				
				rowId = row.getID();
				billingRangeBreakDown.setFromValue(row.getLowerRange());
				billingRangeBreakDown.setToValue(row.getUpperRange());
			}
		}
		
		for( CostCellDTO cell: tableDTO.getCostCellDTOs() ) {
			
			if( cell.getRowID() == rowId && cell.getColID() == columnId ) {
				
				billingRangeBreakDown.setRate(cell.getValue());
			}
		}
		
		return billingRangeBreakDown;
	}
		
}
