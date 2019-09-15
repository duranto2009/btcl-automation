package nix.monthlybill;


import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.Transactional;
import costConfig.CostCellDTO;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import inventory.InventoryConstants;
import inventory.InventoryService;
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lli.monthlyBill.BillingRangeBreakDown;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.common.NIXClientService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.localloop.NIXLocalLoop;
import nix.nixportconfig.NIXPortConfigService;
import nix.office.NIXOffice;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.util.*;
import java.util.stream.Collectors;

public class NIXMonthlyBillGenerator {

	static Logger logger = Logger.getLogger( NIXMonthlyBillGenerator.class );

	@Service
	NIXClientService nixClientService;
	@Service
	NIXConnectionService nixConnectionService;

	@Service
	NIXPortConfigService nixPortConfigService;
	@Service
	DistrictOfcInstallationService districtOfcInstallationService;

	@Service
	CostConfigService costConfigService;
	@Service
	AccountingEntryService accountingEntryService;
	@Service
	LLICostConfigurationService lliCostConfigurationService;
	@Service
	InventoryService inventoryService;

	@Service
	OfcInstallationCostService ofcInstallationCostService;

	@Service
	NIXApplicationLocalLoopService nixApplicationLocalLoopService;

	@Service
	NIXMonthlyBillByClientService nixMonthlyBillByClientService;
	@Service
	NIXMonthlyBillByConnectionService nixMonthlyBillByConnectionService;

	List<ClientDetailsDTO> nixClients = new ArrayList<>();
	List<NIXConnection> nixConnections = new ArrayList<>();
	List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
	Map<Long, TableDTO> mapOfTableDTOs = new HashMap<>();


	double cacheFlatRate = 0;
	double vatRate = 0;
	int localLoopMinimumCost = 0;
	int localLoopDistanceCheckpoint = 0;

	public NIXMonthlyBillGenerator()
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
//			cacheFlatRate = lliFixedCostConfigurationDTO.getCacheServiceFlatRate();
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
		nixClients.clear();
		nixConnections.clear();
		districtOfcInstallationDTOs.clear();
		mapOfTableDTOs.clear();
	}

	@Transactional(transactionType= TransactionType.READONLY)
	private void getAllClient()
	{
		nixClients.clear();
		nixClients.addAll(nixClientService.getAllNIXClient());
	}

	@Transactional(transactionType= TransactionType.READONLY)
	private void getAllConnection()
	{
		long lastDateLastHourOfLastMonth = DateUtils.getLastDayOfMonth(-1).getTime();
		long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);

		try {
			nixConnections = nixConnectionService.getNIXConnectionInstanceListByDateRange(lastDateFirstHourOfLastMonth, lastDateLastHourOfLastMonth)
					.stream()
					.filter(x->x.getStartDate() <= lastDateLastHourOfLastMonth)
					.collect(Collectors.toList());

		} catch (Exception e) {
			logger.fatal("error in getAllConnection");
		}
	}

	@Transactional(transactionType= TransactionType.READONLY)
	private void getAllDistrictOfcInstallationCost()
	{
		try {
			districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();
		} catch (Exception e) {
			logger.fatal("error in getAllDistrictOfcInstallationCost");
		}
	}


	private NIXMonthlyBillByConnection portCostCalculateAndSet(NIXConnection nixConnection,NIXMonthlyBillByConnection nixMonthlyBillByConnection) throws Exception {
		int port_count=0;
		double port_cost=0;
		long port_type=0;
		double port_rate=0;
		for (NIXOffice nixOffice:nixConnection.getNixOffices()
			 ) {


			for (NIXLocalLoop nixLocalLoop:nixOffice.getLocalLoops()){
				port_count++;
				port_cost+=nixPortConfigService.getPortConfigByPortType(nixLocalLoop.getNixApplicationLocalLoop().getPortType()).getPortCharge();
				port_type=nixLocalLoop.getNixApplicationLocalLoop().getPortType();
				port_rate=nixPortConfigService.getPortConfigByPortType(nixLocalLoop.getNixApplicationLocalLoop().getPortType()).getPortCharge();


			}

		}
		nixMonthlyBillByConnection.setPortCount(port_count);
		nixMonthlyBillByConnection.setPortCost(port_cost);
		nixMonthlyBillByConnection.setPortRate(port_rate);
		nixMonthlyBillByConnection.setPortType(port_type);

		return nixMonthlyBillByConnection;
	}


	public List<NIXMonthlyBillByClient> generateMonthlyBill() throws Exception
	{
		loadData();

		List<NIXMonthlyBillByClient> lliMonthlyBillByClients = generateMonthlyBill(nixClients);

		return lliMonthlyBillByClients;
	}


	public List<NIXMonthlyBillByClient> generateMonthlyBill(List<ClientDetailsDTO> clients)
	{

		loadData();
		List<NIXMonthlyBillByClient> nixMonthlyBillByClients = new ArrayList<>();
		for(ClientDetailsDTO client : clients)
		{
			try
			{
				NIXMonthlyBillByClient bill = populateMonthlyBill(client.getClientID());

				if(bill.getMonthlyBillByConnections().isEmpty()) {
					continue;
				}
				calculateBill(bill, vatRate);
				save(bill);

				//calculateTDDate(bill);
				//insertIntoAccounting(bill);

				nixMonthlyBillByClients.add(bill);
			}
			catch (Exception e) {
				logger.fatal("failed to store in db NIXMonthlyBillByClient for " + client.getClientID());
			}
		}

		clearData();

		return nixMonthlyBillByClients;
	}

	//todo:calculate td date and entry
	@Transactional
	private void calculateTDDate(NIXMonthlyBillByClient client) throws Exception
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

//		lliProbableTDClientService.updateTDDate(client.getClientID(), tdDate);

	}

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
	private void save(NIXMonthlyBillByClient nixMonthlyBillByClient) throws Exception
	{
		nixMonthlyBillByClientService.save(nixMonthlyBillByClient);
		if(nixMonthlyBillByClient.getId() == null)
			return;

		for	(NIXMonthlyBillByConnection connection : nixMonthlyBillByClient.getMonthlyBillByConnections())
		{
			connection.setMonthlyBillByClientId(nixMonthlyBillByClient.getId());
			nixMonthlyBillByConnectionService.save(connection);
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
	private NIXMonthlyBillByClient populateMonthlyBill(long clientId) throws Exception
	{
		System.out.println(nixConnections);

		Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());



		NIXMonthlyBillByClient bill = new NIXMonthlyBillByClient();
		bill.setClientId(clientId);

		// get connections
		List<NIXConnection> nixConnectionInstanceList =
				nixConnections.stream()
				.filter(x-> x.getClient() == clientId)
				.collect(Collectors.toList());

		//loop cost calculation and info
		for (NIXConnection connection : nixConnectionInstanceList)
		{

			NIXMonthlyBillByConnection nixMonthlyBillByConnection = new NIXMonthlyBillByConnection();
            nixMonthlyBillByConnection.setConnectionId(connection.getId());
            nixMonthlyBillByConnection.setClientId(connection.getClient());



            nixMonthlyBillByConnection.setName(connection.getName());
            nixMonthlyBillByConnection.setCreatedDate(date.getTime());
            nixMonthlyBillByConnection.setStatus(connection.getStatus());
            nixMonthlyBillByConnection.setType(connection.getIncidentId());

            nixMonthlyBillByConnection.setDiscountRate(0);
            nixMonthlyBillByConnection.setVatRate(vatRate);

			//if connection closed, then no bill
			if(connection.getStatus() == NIXConstants.STATUS_CLOSED)
				continue;


			else if(connection.getStatus() == NIXConstants.STATUS_TD)
			{
				nixMonthlyBillByConnection.setLocalLoopBreakDowns(getLocalLoopBreakDown(connection, localLoopDistanceCheckpoint, localLoopMinimumCost));
			}
			else
			{
				portCostCalculateAndSet(connection,nixMonthlyBillByConnection);//set port cost
				nixMonthlyBillByConnection.setLocalLoopBreakDowns(getLocalLoopBreakDown(connection, localLoopDistanceCheckpoint, localLoopMinimumCost));
			}

			double loopCost=0;
			double loopRate=0;

			if( nixMonthlyBillByConnection.getLocalLoopBreakDowns().size()>0){
				 loopCost = nixMonthlyBillByConnection.getLocalLoopBreakDowns()
						.stream()
						.mapToDouble(
								loop->loop.getCost())
						.sum();
				 loopRate = nixMonthlyBillByConnection.getLocalLoopBreakDowns()
						.stream()
						.findFirst()
						.get().rate;
			}

			nixMonthlyBillByConnection.setLoopCost(NumberUtils.formattedValue(loopCost));
			nixMonthlyBillByConnection.setLoopRate(loopRate);

			bill.getMonthlyBillByConnections().add(nixMonthlyBillByConnection);
		}


		//set billing range
//		TableDTO tableDTO = getTableDTO(clientId);
//		if(tableDTO == null)
//			return bill;

//		calculateBWAndLTCost(bill,tableDTO, totalMbps, totalCache);



		bill.setCreatedDate(date.getTime());
		bill.setDeleted(false);
		bill.setMonth(DateUtils.getMonthFromDate(date.getTime()));
		bill.setYear(DateUtils.getYearFromDate(date.getTime()));


		//TODO :need to get yearly fee for additional ip

		return bill;
	}




//	@Transactional(transactionType = TransactionType.READONLY)
//	private TableDTO getTableDTO(long clientId) throws Exception {
//		//set billing range
//		Integer tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_LLI, clientId);
//
//		TableDTO tableDTO = null;
//		if(mapOfTableDTOs.containsKey(tariffCategory))
//		{
//			tableDTO = mapOfTableDTOs.get(tariffCategory);
//		}
//		else
//		{
//			try {
//				tableDTO = costConfigService.getLatestTableWithCategoryID(ModuleConstants.Module_ID_LLI, tariffCategory);
//			} catch (Exception e) {
//
//			}
//		}
//
//		return tableDTO;
//	}
	private void calculateBill(NIXMonthlyBillByClient bill, double vatRate) throws Exception
	{

		double grandTotal = 0;
		double totalDiscount = 0;

		for (NIXMonthlyBillByConnection connection : bill.getMonthlyBillByConnections())
		{

			connection.setDiscount(0);

			//here grandcost is after discount
			connection.setGrandCost(NumberUtils.formattedValue(connection.getLoopCost() + connection.getPortCost() - connection.getDiscount()));

			connection.setVat(NumberUtils.formattedValue(connection.getGrandCost()* connection.getVatRate()/100));
			connection.setTotalCost(NumberUtils.formattedValue(connection.getGrandCost() + connection.getVat()));

			grandTotal += connection.getGrandCost();
			totalDiscount += connection.getDiscount();
		}

		totalDiscount = totalDiscount ;
		bill.setGrandTotal(NumberUtils.formattedValue(grandTotal));

		bill.setDiscountPercentage(0);
		bill.setDiscount(NumberUtils.formattedValue(totalDiscount));

//		bill.setTotalPayable(NumberUtils.formattedValue(bill.getGrandTotal() - bill.getLongTermContructAdjustment()));
		bill.setTotalPayable(NumberUtils.formattedValue(bill.getGrandTotal()));

		bill.setVatPercentage(vatRate);
		bill.setVAT(NumberUtils.formattedValue(bill.getTotalPayable()* bill.getVatPercentage()/100));


		bill.setNetPayable(NumberUtils.formattedValue(bill.getTotalPayable() + bill.getVAT()));

	}



	@Transactional(transactionType= TransactionType.READONLY)
	private List<NIXLocalLoopBreakDown> getLocalLoopBreakDown(NIXConnection connection, double initialLoopLength, double initialLoopCost) throws Exception
	{
		List<NIXLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();

		for(NIXOffice office : connection.getNixOffices())
		{
			for(NIXLocalLoop loop : office.getLocalLoops())
			{
                NIXApplicationLocalLoop nixApplicationLocalLoop=nixApplicationLocalLoopService.getLocalLoopById(loop.getApplicationLocalLoop());
				double loopLength = nixApplicationLocalLoop.getOcdDistance() + nixApplicationLocalLoop.getBtclDistance();
				if(loopLength == 0)
					continue;

				NIXLocalLoopBreakDown localLoopBreakDown = new NIXLocalLoopBreakDown();
				localLoopBreakDown.setConnectionId(connection.getId());
				localLoopBreakDown.setFiberCableType(nixApplicationLocalLoop.getOfcType());
				localLoopBreakDown.setId(loop.getId());
				localLoopBreakDown.setOfficeId(loop.getOffice());
				localLoopBreakDown.setPortID(nixApplicationLocalLoop.getPortId());
				localLoopBreakDown.setValue(loopLength);

				//initial flat rate
				long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(nixApplicationLocalLoop.getPopId()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();
				Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x->x.getDistrictID() == districtID).findFirst();
				localLoopBreakDown.setRate(ofcInstallationCostByDistrictID.isPresent() ? ofcInstallationCostByDistrictID.get().getInstallationCost() : 0);

				double loopCost = initialLoopCost;
				loopLength -= initialLoopLength;
				if(loopLength > 0)
				{
					loopCost += loopLength * localLoopBreakDown.getRate();
				}

				//calculation using OFC type
				loopCost *= nixApplicationLocalLoop.getOfcType();
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

    public static void main(String[] args) throws Exception {
		CurrentTimeFactory.initializeCurrentTimeFactory();
        System.out.println(ServiceDAOFactory.getService(NIXMonthlyBillGenerator.class).generateMonthlyBill());
    }

}
