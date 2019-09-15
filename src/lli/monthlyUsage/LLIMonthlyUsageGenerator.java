package lli.monthlyUsage;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import lli.longTerm.LLILongTermBenefit;
import lli.longTerm.LLILongTermBenefitService;
import lli.outsourceBill.*;
import org.apache.log4j.Logger;

import accounting.AccountingEntryService;
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
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lli.connection.LLIConnectionConstants;
import lli.monthlyBill.BillingRangeBreakDown;
import lli.monthlyBill.ConnectionBandwidthBreakDown;
import lli.monthlyBill.LLILocalLoopBreakDown;
import lli.monthlyBill.LLILongTermContractBreakDown;
import lli.monthlyBill.MbpsBreakDown;
import requestMapping.Service;
import user.UserDTO;
import user.UserService;
import util.ColumnDTO;
import util.DateUtils;
import util.NumberUtils;
import util.RowDTO;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;


public class LLIMonthlyUsageGenerator {

	static Logger logger = Logger.getLogger( LLIMonthlyUsageGenerator.class );
	class DayWiseUsage
	{
		double value;
		Map<Double, Double> discountUsage = new HashMap<>();
	}

	@Service
	LLIClientService LLIClientService;
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
	AccountingEntryService accountingEntryService;
	@Service
	OfcInstallationCostService ofcInstallationCostService;

	@Service 
	LLIMonthlyUsageByClientService lliMonthlyUsageByClientService;
	@Service 
	LLIMonthlyUsageByConnectionService lliMonthlyUsageByConnectionService;
    @Service
    LLILongTermBenefitService lliLongTermBenefitService;
	@Service
    LLIMonthlyOutsourceBillService lliMonthlyOutsourceBillService;
    @Service
    LLIMonthlyOutsourceBillByConnectionService lliMonthlyOutsourceBillByConnectionService;
    @Service
    UserService userService;

    //not dependant on client
    List<UserDTO> vendors = new ArrayList<>();
	List<ClientDetailsDTO> lliClients = new ArrayList<>();
	List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
	Map<Long, TableDTO> mapOfTableDTOs = new HashMap<>();
    Map<Long, LLIMonthlyOutsourceBill> lliOutsourceBillMap = new HashMap<>();

	double cacheFlatRate = 0;
	double vatRate = 0;	
	int localLoopMinimumCost = 0;
	int localLoopDistanceCheckpoint = 0;
	double ratioOfOutsourceBillForVendor = 0.9; //vendor will get 90% of total local loop bill

    //clients related start
    List<LLIConnectionInstance> lliConnections = new ArrayList<>();
    List<LLIConnectionInstance> lliConnectionIncidents = new ArrayList<>();
    Map<Long, Double> ltBenefitsForClient = new HashMap<>();
    //client related ends
	
	public LLIMonthlyUsageGenerator()
	{
		
	}

    /**
     * load data which are needed for bill generation
     */
	private void loadData()
	{
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        //fetch fixed rate from cost configuration
		try {

            List<LLIFixedCostConfigurationDTO> lliFixedCostConfigurationDTOs = lliCostConfigurationService.getLLI_FixedCostConfigurationDTOByDateRange(firstDay, lastDay);
			if(lliFixedCostConfigurationDTOs.size() != 0)
			{
				LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliFixedCostConfigurationDTOs.get(0);
				cacheFlatRate = lliFixedCostConfigurationDTO.getCacheServiceFlatRate();
				vatRate = lliFixedCostConfigurationDTO.getMaximumVatPercentage();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("cacheFlatRate : " + cacheFlatRate + " ; vatRate : " + vatRate);
		
		OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostService.getLatestByDate(lastDay);

		if(ofcInstallationCostDTO != null)
		{
			localLoopMinimumCost = ofcInstallationCostDTO.getFiberCost();
			localLoopDistanceCheckpoint = ofcInstallationCostDTO.getFiberLength();
		}


        getAllVendor();
        getOutsourceBills();
		getAllClient();
		getAllConnectionIncidents();
		getAllDistrictOfcInstallationCost();
	}
	
	private void clearData()
	{
        vendors.clear();
		lliClients.clear();
		lliConnections.clear();
		districtOfcInstallationDTOs.clear();
		mapOfTableDTOs.clear();
        lliOutsourceBillMap.clear();
	}

    /**
     * fetch all vendor list to calculate their last month charge (outsourcing bill)
     */
    private void getAllVendor()
    {
        vendors.clear();
        vendors.addAll(userService.getLocalLoopProviderList());
    }

    /**
     * get already calculated outsource bill for last month <br>
     * keep it in map so that we can update vendor's bill without fetching from db before each saving
     */
    @Transactional(transactionType=util.TransactionType.READONLY)
    private void getOutsourceBills()
    {
        long datetime = DateUtils.getLastDayOfMonth(-1).getTime();;
        lliOutsourceBillMap.clear();
        for(LLIMonthlyOutsourceBill outsourceBill : lliMonthlyOutsourceBillService.getByMonthByYear(
                DateUtils.getMonthFromDate(datetime),
                DateUtils.getYearFromDate(datetime)))
        {
            lliOutsourceBillMap.putIfAbsent(outsourceBill.getVendorId(), outsourceBill);
        }

    }

    /**
     * fetch all the clients which were active in last month
     */
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllClient()
	{
		Date toDate = DateUtils.getLastDayOfMonth(-1);
		lliClients.clear();
		lliClients.addAll(
				LLIClientService.getAllLLIClient()
				.stream()
				.filter(x->x.getActivationDate() < toDate.getTime())
				.collect(Collectors.toList()));
	}

    /**
     * fetch last month connection related incidents <br>
     * from those incidents get unique connections
     */
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllConnectionIncidents()
	{
		Date fromDate = DateUtils.getFirstDayOfMonth(-1);
		Date toDate = DateUtils.getLastDayOfMonth(-1);
		
		try {
		    //get connection incidents
			lliConnectionIncidents = lliConnectionService.getLLIConnectionInstanceListByDateRange(fromDate.getTime(), toDate.getTime())
                    .stream()
                    .filter(x->x.getActiveTo() > fromDate.getTime())	//this is required as we are converting toDate to the last day while fetching using @ManipulateEndDate
                    .collect(Collectors.toList());

			//get unique connections
			long lastDateLastHourOfLastMonth = toDate.getTime();
			long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);
			
		/*	lliConnections = lliConnectionService.getLLIConnectionInstanceListByDateRange(lastDateFirstHourOfLastMonth, lastDateLastHourOfLastMonth)
					.stream()
					.filter(x->x.getStartDate() <= lastDateLastHourOfLastMonth)
					.collect(Collectors.toList());*/
            lliConnections = lliConnectionIncidents.stream()
                    .filter(x->x.getActiveFrom() < lastDateLastHourOfLastMonth
                            && x.getActiveTo() > lastDateFirstHourOfLastMonth
                            && x.getStartDate() <= lastDateLastHourOfLastMonth)
                    .collect(Collectors.toList());

			
		} catch (Exception e) {
			logger.fatal("error in getAllConnection incidents");
		}
	}

    /**
     * fetch district wise ofc installation cost
     */
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void getAllDistrictOfcInstallationCost()
	{
		try {
			districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();//taking latest. cause no way to fetch old data
		} catch (Exception e) {
			logger.fatal("error in getAllDistrictOfcInstallationCost");
		}
	}

    /**
     * this method is called for generating bill for all clients
     * @return list of last month bill for those clients
     */
	public List<LLIMonthlyUsageByClient> generateMonthlyUsage() {
		
		List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients = generateMonthlyUsage(lliClients);
		return lliMonthlyUsageByClients;
	}

    /**
     * this method is called for generating bill for provided clients
     * @param clients for whom bill will be generated
     * @return list of last month bill for those clients
     */
	public List<LLIMonthlyUsageByClient> generateMonthlyUsage(List<ClientDetailsDTO> clients)
	{

		loadData();
		
		List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients = new ArrayList<>();
		for(ClientDetailsDTO client : clients)
		{
			try 
			{
			    //get the TableDTO which contains BW rate chart
				TableDTO tableDto = getTableDTO(client.getClientID());
				if(tableDto == null)
					continue;
				
				LLIMonthlyUsageByClient bill = populateMonthlyUsage(client.getClientID(), tableDto);
				
				calculateBill(bill);
				save(bill);
				lliMonthlyUsageByClients.add(bill);
				
			} catch (ParseException e) {
				logger.error("error while populating monthly usage for client " + client.getClientID());
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("error while saving monthly usage for client " + client.getClientID());
				e.printStackTrace();
			}
		}
		
		clearData();
		return lliMonthlyUsageByClients;
	}

    /**
     * save monthly usage of a client <br>
     * then save connection wise usage using that client's monthly usage id <br>
     * save long term benefits that client consumed <br>
     * then save outsource bill for the vendors who were involved in that client's connections
     * @param lliMonthlyUsageByClient
     */
	@Transactional(transactionType=TransactionType.INDIVIDUAL_TRANSACTION)
	private void save(LLIMonthlyUsageByClient lliMonthlyUsageByClient)
	{
		lliMonthlyUsageByClientService.save(lliMonthlyUsageByClient);
		if(lliMonthlyUsageByClient.getId() == null)
			return;
		
		for	(LLIMonthlyUsageByConnection connection : lliMonthlyUsageByClient.getMonthlyUsageByConnections())
		{
			connection.setMonthlyUsageByClientId(lliMonthlyUsageByClient.getId());
			lliMonthlyUsageByConnectionService.save(connection);
		}

		saveLongTermBenefits(lliMonthlyUsageByClient.getClientId());
        calculateAndSaveOutsourceBill(lliMonthlyUsageByClient);
    }

	private LLIMonthlyUsageByClient populateMonthlyUsage(long clientId, TableDTO tableDto) throws ParseException 
	{
        ltBenefitsForClient.clear();

		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		
		Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());
		LLIMonthlyUsageByClient bill = new LLIMonthlyUsageByClient();

		bill.setClientId(clientId);
		bill.setCreatedDate(date.getTime());
		bill.setDeleted(false);
		bill.setMonth(DateUtils.getMonthFromDate(firstDay));
		bill.setYear(DateUtils.getYearFromDate(firstDay));
		
		
		// get connections of that client
		List<LLIConnectionInstance> lliConnectionList = lliConnections.stream()
																		.filter(x-> x.getClientID() == clientId)
																		.collect(Collectors.toList());
																
		//get breakdowns calculation 
		for (LLIConnectionInstance lliConnection : lliConnectionList)
		{
			// get connection incidents
			List<LLIConnectionInstance> lliConnectionIncidentList = lliConnectionIncidents.stream()
																							.filter(x-> x.getID() == lliConnection.getID())
																							.collect(Collectors.toList());
			
			//lliConnectionIncidentList.sort((x,y)-> x.getActiveFrom() < y.getActiveFrom() ? -1 : 1);
			
			LLIMonthlyUsageByConnection lliMonthlyUsageByConnection = new LLIMonthlyUsageByConnection();
			lliMonthlyUsageByConnection.setConnectionId(lliConnection.getID());
			lliMonthlyUsageByConnection.setClientId(lliConnection.getClientID());
			lliMonthlyUsageByConnection.setType(lliConnection.getConnectionType());
			lliMonthlyUsageByConnection.setName(lliConnection.getName());
			lliMonthlyUsageByConnection.setCreatedDate(DateUtils.getCurrentTime());
			lliMonthlyUsageByConnection.setDiscountRate(lliConnection.getDiscountRate());
			lliMonthlyUsageByConnection.setVatRate(vatRate);
			
			lliMonthlyUsageByConnection.setLocalLoopBreakDowns(getLocalLoopBreakDowns(lliConnectionIncidentList));
			lliMonthlyUsageByConnection.setConnectionBandwidthBreakDowns(getBandwidthBreakDowns(lliConnection, lliConnectionIncidentList));
			
			calculateLocalLoopCost(lliMonthlyUsageByConnection);
			
			bill.getMonthlyUsageByConnections().add(lliMonthlyUsageByConnection);
			
			//set json contents
			lliMonthlyUsageByConnection.setConnectionBandwidthBreakDownsContent();
			lliMonthlyUsageByConnection.setLocalLoopBreakDownsContent();
			lliMonthlyUsageByConnection.setFeesContent();
		}

		//calculate BW and Long term cost
		calculateBWAndLTCost(bill, tableDto);
		
		//set json contents
		bill.setMbpsBreakDownsContent();
		bill.setBillingRangeBreakDownsContent();
		bill.setMbpsBreakDownsForCacheContent();
		bill.setBillingRangeBreakDownsForCacheContent();
		bill.setLongTermContractBreakDownsContent();

		//TODO :need to get yearly fee for additional ip
		
		return bill;
	}

    /**
     * @param lliMonthlyUsageByClient which will be populated with connection wise info
     * @param tableDto which contains the rate chart
     */
	private void calculateBWAndLTCost(LLIMonthlyUsageByClient lliMonthlyUsageByClient, TableDTO tableDto) 
	{
		
		//calculate regular bw breakdown
		DayWiseUsage[] bwPerDay = getDaywiseBWUsage(lliMonthlyUsageByClient, LLIConnectionConstants.CONNECTION_TYPE_REGULAR);
		List<MbpsBreakDown> totalMbpsBreakDowns = getTotalBandwidthBreakDowns(bwPerDay);
		lliMonthlyUsageByClient.setTotalMbpsBreakDowns(totalMbpsBreakDowns);

		//calculate billing range break down
		List<BillingRangeBreakDown> billingRangeBreakDowns = getBillingRangeBreakDowns(totalMbpsBreakDowns, tableDto);
		lliMonthlyUsageByClient.setBillingRangeBreakDowns(billingRangeBreakDowns);
		
		//calculate cache bw breakdown
		DayWiseUsage[] cachePerDay = getDaywiseBWUsage(lliMonthlyUsageByClient, LLIConnectionConstants.CONNECTION_TYPE_CACHE);
		List<MbpsBreakDown> totalMbpsBreakDownsForCache = getTotalBandwidthBreakDowns(cachePerDay);
		lliMonthlyUsageByClient.setTotalMbpsBreakDownsForCache(totalMbpsBreakDownsForCache);

        //set billing range break down
		BillingRangeBreakDown range = new BillingRangeBreakDown();
		range.setRate(cacheFlatRate);
		lliMonthlyUsageByClient.getBillingRangeBreakDownsForCache().add(range);
		
		//calculate BW related cost
		calculateBandwidthCost(lliMonthlyUsageByClient, tableDto);
		//calculate Long term contract cost saving
		calculateLongTermContract(lliMonthlyUsageByClient, tableDto, bwPerDay);
		
	}

    /**
     * calculate the BW (mbps) cost using the BW break downs and billing range break down for each connection usage.
     * @param lliMonthlyUsageByClient
     * @param tableDto
     */
	private void calculateBandwidthCost(LLIMonthlyUsageByClient lliMonthlyUsageByClient, TableDTO tableDto) 
	{
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		int totalDays = DateUtils.getDaysInMonth(firstDay);
		
		for( LLIMonthlyUsageByConnection connection : lliMonthlyUsageByClient.getMonthlyUsageByConnections())
		{
			if( ! (connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR ||
					connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE))
			{
				continue;
			}
			
			double totalBWCost = 0;
			
			for(ConnectionBandwidthBreakDown bandWidth : connection.getConnectionBandwidthBreakDowns()) 
			{
				long fromDate = bandWidth.getFromDate();
				long toDate = bandWidth.getToDate();
				int days = DateUtils.getDiffInDays(fromDate, toDate);
				
				if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
				{
					//bandWidth.setRate(cacheFlatRate);
					bandWidth.setCost((bandWidth.getValue() * cacheFlatRate) * days / totalDays);
					totalBWCost += bandWidth.getCost();
					continue;
				}
				
				
				//calculate for regular connection
				for(BillingRangeBreakDown billingRangeBreakDown : lliMonthlyUsageByClient.getBillingRangeBreakDowns())
				{
					//if in that date range
					if( fromDate >= billingRangeBreakDown.getFromDate() &&
						fromDate < billingRangeBreakDown.getToDate())
					{
						//if full covered
						if( toDate <= billingRangeBreakDown.getToDate())
						{
							days = DateUtils.getDiffInDays(fromDate, toDate);
						}
						//date range in more than one billing range
						else
						{
							days = DateUtils.getDiffInDays(fromDate, billingRangeBreakDown.getToDate());
							fromDate = DateUtils.getNthDayFromDate(fromDate, days);
						}
						
						//set cost
						bandWidth.setCost((bandWidth.getValue() * billingRangeBreakDown.getRate()) * days / totalDays);
						totalBWCost += bandWidth.getCost();
						
					}
				}
			}
			
			connection.setMbpsCost(NumberUtils.formattedValue(totalBWCost));
		}
	}

    /**
     * calculate the loop cost of a connection usage using ofc installation cost which is found by district of inventory common
     * @param lliMonthlyUsageByConnection
     */
	@Transactional(transactionType=util.TransactionType.READONLY)
	private void calculateLocalLoopCost(LLIMonthlyUsageByConnection lliMonthlyUsageByConnection) {
		
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		int totalDays = DateUtils.getDaysInMonth(firstDay);
		
		double totalLoopCost = 0;
		for(LLILocalLoopBreakDown loop : lliMonthlyUsageByConnection.getLocalLoopBreakDowns())
		{
			int days = DateUtils.getDiffInDays(loop.getFromDate(), loop.getToDate());
			if(days == 0)
				return;
			
			double loopLength = loop.getValue();
			if(loopLength == 0)
				continue;
			
			//initial flat rate
			double loopCost = localLoopMinimumCost * days / totalDays;	//for those days
			loopLength -= localLoopDistanceCheckpoint;
			
			if(loopLength > 0)
			{	
				long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(loop.getPortID()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();
				Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x->x.getDistrictID() == districtID).findFirst();

				loopCost += loopLength * (ofcInstallationCostByDistrictID.isPresent() ?
											ofcInstallationCostByDistrictID.get().getInstallationCost() * days / totalDays	//for those days
											: 0);
			}
				
			//calculation using OFC type
			loopCost *= loop.getFiberCableType();
			
			loop.setCost(loopCost);
			totalLoopCost += loopCost;
		}
		
		lliMonthlyUsageByConnection.setLoopCost(NumberUtils.formattedValue(totalLoopCost));
	}

    /**
     * calculate long term contract cost savings <br>
     * using daywise BW cost and daywise Long term contract <br>
     * and calculate long term benefits client got in last month
     * @param lliMonthlyUsageByClient
     * @param tableDto
     * @param bwPerDay day wise regular BW usage
     */
	private void calculateLongTermContract(LLIMonthlyUsageByClient lliMonthlyUsageByClient, TableDTO tableDto, DayWiseUsage[] bwPerDay)
	{
		if(lliMonthlyUsageByClient.getBillingRangeBreakDowns().size() == 0)
			return;

		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		int totalDays = DateUtils.getDaysInMonth(firstDay);
		
		List<LLILongTermContract> lliLongTermContracts = new ArrayList<>();
		
		try {
		    //fetch long term contracts which were active last month
			lliLongTermContracts = lliLongTermService.getLLILongTermContractHistoryByClientIDAndDateRange(lliMonthlyUsageByClient.getClientId(), firstDay, lastDay);
		} catch (Exception e) {
			logger.info("issue in fatching long term contract for monthly bill for client id = " + lliMonthlyUsageByClient.getClientId());
		}
		
		if(lliLongTermContracts.size() == 0)
			return;

		//calculate day wise long term contract usage
		DayWiseUsage[] ltPerDay = getDaywiseLongTermUsage(lliLongTermContracts, bwPerDay);
		List<LLILongTermContractBreakDown> ltBreakDowns = new ArrayList<>();;
		
		
		double totalLTCAdjustAmount = 0, ltcDiscountAdjustment = 0;
		double prevBW = 0, ltRate = 0, bwRate = 0;
		long prevToDate = 0;
		
		//calculate long term contracts break down and rate using billing range
       //besides calculate benefits daywise to sum up to get total benefits
		for(int i = 0; i< totalDays; i++)
		{
			long tempDateTime = DateUtils.getNthDayFromDate(firstDay, i);
			
			
			if(prevBW != ltPerDay[i].value 
					|| (ltPerDay[i].value != 0 && prevToDate < tempDateTime))//check whether range changed or not 
			{
				if(ltBreakDowns.size() != 0)
				{
					LLILongTermContractBreakDown br = ltBreakDowns.get(ltBreakDowns.size()-1);
					br.setToDate(DateUtils.getNthDayLastHourFromDate(firstDay, i-1));
				}
				
				LLILongTermContractBreakDown br = new LLILongTermContractBreakDown();
				br.setFromDate(DateUtils.getNthDayFromDate(firstDay, i));
				br.setToDate(lastDay);
				br.setValue(ltPerDay[i].value);
				
				for(BillingRangeBreakDown billingRangeBreakDown : lliMonthlyUsageByClient.getBillingRangeBreakDowns())
				{
					if( tempDateTime >= billingRangeBreakDown.getFromDate() &&
							tempDateTime <= billingRangeBreakDown.getToDate())
					{
						prevToDate = billingRangeBreakDown.getToDate();
						ltRate = getBillingRangeBreakDown(tableDto, billingRangeBreakDown.getFromValue(), true).getRate();
						bwRate = billingRangeBreakDown.getRate();
						br.setRate(ltRate);
						break;
					}
				}
				
				ltBreakDowns.add(br);
				prevBW = ltPerDay[i].value;
				
				
			}
			
			if(ltPerDay[i].value == 0)
				continue;

			//calculate long term benefits in that day
			double todaysBenefit = totalLTCAdjustAmount;
			for (Double discountKey : ltPerDay[i].discountUsage.keySet())
			{
				double value = ltPerDay[i].discountUsage.get(discountKey) * (bwRate-ltRate) / totalDays;
				double discount = value * discountKey/100;
				totalLTCAdjustAmount += value - discount;
				ltcDiscountAdjustment += discount;
			}

            todaysBenefit = totalLTCAdjustAmount - todaysBenefit;
			for(LLILongTermContract lt : lliLongTermContracts)
            {
                if( lt.getActiveFrom() <= tempDateTime && tempDateTime <= lt.getActiveTo())
                {
                    //as there we discussed that discount will be applicable for all connection so no need to separate discount rate wise benefit
                    double benefit = todaysBenefit * lt.getBandwidth()/ ltPerDay[i].value ;
                    if(ltBenefitsForClient.containsKey(lt.getID()))
                    	ltBenefitsForClient.put(lt.getID(), ltBenefitsForClient.get(lt.getID()) + benefit);
					else
						ltBenefitsForClient.put(lt.getID(), benefit);
                }
            }

		}
		
		lliMonthlyUsageByClient.setLongTermContractBreakDowns(ltBreakDowns);
		lliMonthlyUsageByClient.setLongTermContructAdjustment(NumberUtils.formattedValue(totalLTCAdjustAmount));
		lliMonthlyUsageByClient.setLongTermContructDiscountAdjustment(NumberUtils.formattedValue(ltcDiscountAdjustment));
	}

    /**
     * now calculate the total bill <br>
     * connection wise as well as client wise
     * @param lliMonthlyUsageByClient
     */
	private void calculateBill(LLIMonthlyUsageByClient lliMonthlyUsageByClient) {
		
		double grandTotal = 0;
		double totalDiscount = 0;
		
		for( LLIMonthlyUsageByConnection connection : lliMonthlyUsageByClient.getMonthlyUsageByConnections())
		{
			connection.setDiscount(NumberUtils.formattedValue(connection.getMbpsCost()* connection.getDiscountRate()/100));
			connection.setGrandCost(NumberUtils.formattedValue(connection.getLoopCost() + connection.getMbpsCost() - connection.getDiscount()));
			
			connection.setVat(NumberUtils.formattedValue(connection.getGrandCost()* connection.getVatRate()/100));
			connection.setTotalCost(NumberUtils.formattedValue(connection.getGrandCost() + connection.getVat()));
			
			grandTotal += connection.getGrandCost();
			totalDiscount += connection.getDiscount();
		}
		
		totalDiscount = totalDiscount - lliMonthlyUsageByClient.getLongTermContructDiscountAdjustment();
		
		//grand total without vat and ltc
		lliMonthlyUsageByClient.setGrandTotal(NumberUtils.formattedValue(grandTotal));
		
		lliMonthlyUsageByClient.setDiscountPercentage(0);
		lliMonthlyUsageByClient.setDiscount(NumberUtils.formattedValue(totalDiscount));
		
		lliMonthlyUsageByClient.setTotalPayable(NumberUtils.formattedValue(lliMonthlyUsageByClient.getGrandTotal() - lliMonthlyUsageByClient.getLongTermContructAdjustment()));
		
		lliMonthlyUsageByClient.setVatPercentage(vatRate);
		lliMonthlyUsageByClient.setVAT(NumberUtils.formattedValue(lliMonthlyUsageByClient.getTotalPayable()* lliMonthlyUsageByClient.getVatPercentage()/100));
		
		lliMonthlyUsageByClient.setNetPayable(NumberUtils.formattedValue(lliMonthlyUsageByClient.getTotalPayable() + lliMonthlyUsageByClient.getVAT()));
		
	}
	
	
	
	private List<ConnectionBandwidthBreakDown> getBandwidthBreakDowns(LLIConnectionInstance lliConnection, List<LLIConnectionInstance> lliConnectionIncidents) 
	{
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		
		List<ConnectionBandwidthBreakDown> bandWidthBreakDowns = new ArrayList<>();
		
		if(lliConnectionIncidents.size() == 0)
			return bandWidthBreakDowns;
		
		
		Map<Long, ConnectionBandwidthBreakDown> bandWidthBreakDownMap = new HashMap<>();
		
		for(LLIConnectionInstance instance : lliConnectionIncidents)
		{
			if(	instance.getStatus() == LLIConnectionConstants.STATUS_CLOSED )
				continue;
			
			Long key = instance.getID();
			logger.info("connection name : " + instance.getName() + " ; key : " + key + "; bandwidth : " + instance.getBandwidth());
			
			ConnectionBandwidthBreakDown bandWidthBreakDown = null;
			if(bandWidthBreakDownMap.containsKey(key))
			{
				bandWidthBreakDown = bandWidthBreakDownMap.get(key);
			
				//if bw value not changed means incident occurrence not related to bw
				if(instance.getStatus() != LLIConnectionConstants.STATUS_TD &&
						bandWidthBreakDown.getValue() == instance.getBandwidth())
				{
					bandWidthBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
					continue;
				}
				//if TD
				//or change occurred
				//remove from map and add into lliMonthlyUsageByConnection
				else
				{
					bandWidthBreakDowns.add(bandWidthBreakDown);
					bandWidthBreakDownMap.remove(key);
				}
			}
			//if TD then dont count it
			if(	instance.getStatus() == LLIConnectionConstants.STATUS_TD)
				continue;
					
			bandWidthBreakDown = new ConnectionBandwidthBreakDown();
					
			//bandWidthBreakDown.setStatus(instance.getStatus());
			bandWidthBreakDown.setValue(instance.getBandwidth());
			bandWidthBreakDown.setFromDate(instance.getActiveFrom() < firstDay ? firstDay : instance.getActiveFrom());
			bandWidthBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
			
			//if new incident
			if(instance.getActiveFrom() >= firstDay)
			{
				bandWidthBreakDown.setRemark(getIncidentInString(instance.getIncident()));
			}
			
			bandWidthBreakDownMap.putIfAbsent(key, bandWidthBreakDown);
		}

		bandWidthBreakDowns.addAll(bandWidthBreakDownMap.values());
		
		return bandWidthBreakDowns;
	}
	
	private List<BillingRangeBreakDown> getBillingRangeBreakDowns(List<MbpsBreakDown> mbpsBreakDowns, TableDTO tableDTO)
	{

		List<BillingRangeBreakDown> billingRangeBreakDowns = new ArrayList<>();
		
		for(MbpsBreakDown mbpsBreakDown : mbpsBreakDowns)
		{
			//should also check whether range changed or not 
			if(billingRangeBreakDowns.size() != 0)
			{
				BillingRangeBreakDown br = billingRangeBreakDowns.get(billingRangeBreakDowns.size()-1);
				
				//if in range then nothing to do
				if( mbpsBreakDown.getValue() >= br.getFromValue() && mbpsBreakDown.getValue() <= br.getToValue())
				{
					br.setToDate(mbpsBreakDown.getToDate());
					continue;
				}
			}
				
				
			BillingRangeBreakDown br = getBillingRangeBreakDown(tableDTO, mbpsBreakDown.getValue(), false);
			br.setFromDate(mbpsBreakDown.getFromDate());
			br.setToDate(mbpsBreakDown.getToDate());
			
			billingRangeBreakDowns.add(br);
		}
		
		for(BillingRangeBreakDown br : billingRangeBreakDowns)
		{
			logger.info("billing range from date : " + br.getFromDate() + " ; to date : "+ br.getToDate());
		}
		return billingRangeBreakDowns;
	}
	
	private List<MbpsBreakDown> getTotalBandwidthBreakDowns(DayWiseUsage[] bwPerDay)
	{
		
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		int totalDays = DateUtils.getDaysInMonth(firstDay);

		List<MbpsBreakDown> mbpsBreakDowns = new ArrayList<>();
		
		double prevBW = 0;
		
		for(int i = 0; i< totalDays; i++)
		{
			//should also check whether range changed or not 
			if(prevBW != bwPerDay[i].value)
			{
				if(mbpsBreakDowns.size() != 0)
				{
					MbpsBreakDown br = mbpsBreakDowns.get(mbpsBreakDowns.size()-1);
					br.setToDate(DateUtils.getNthDayLastHourFromDate(firstDay, i-1));
				}
				
				MbpsBreakDown br = new MbpsBreakDown();
				br.setFromDate(DateUtils.getNthDayFromDate(firstDay, i));
				br.setToDate(lastDay);
				br.setValue(bwPerDay[i].value);
				
				mbpsBreakDowns.add(br);
				prevBW = bwPerDay[i].value;
			}
		}
		
		return mbpsBreakDowns;
	}
	
	private List<LLILocalLoopBreakDown> getLocalLoopBreakDowns( List<LLIConnectionInstance> lliConnectionIncidents)
	{
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		
		List<LLILocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();
		
		if(lliConnectionIncidents.size() == 0)
			return localLoopBreakDowns;
		
		Map<Long, LLILocalLoopBreakDown> localLoopBreakDownMap = new HashMap<>();
		
		for(LLIConnectionInstance instance : lliConnectionIncidents)
		{
			if(instance.getStatus() == LLIConnectionInstance.STATUS_CLOSED)
				continue;
			
			
			for(LLIOffice office : instance.getLliOffices())
			{
				for(LLILocalLoop loop : office.getLocalLoops())
				{
					Long key = loop.getID();
					// skip the loop if it is being closed
					if (loop.getIs_deleted()==1) continue;

					long calculatableDistance = loop.getBtclDistance()+ loop.getOCDistance();
					
					logger.info("connection name : " + instance.getName() + " ; key : " + key + "; loop : " + calculatableDistance);
					
					LLILocalLoopBreakDown lliLocalLoopBreakDown = null;
					if(localLoopBreakDownMap.containsKey(key))
					{
						lliLocalLoopBreakDown = localLoopBreakDownMap.get(key);
					
						if( lliLocalLoopBreakDown.getValue() == calculatableDistance &&
							lliLocalLoopBreakDown.getFiberCableType() == loop.getOfcType())
						{
							
							lliLocalLoopBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
							continue;
						}
						//change occurred
						//remove from map and add into lliMonthlyUsageByConnection
						else
						{
							localLoopBreakDowns.add(lliLocalLoopBreakDown);
							localLoopBreakDownMap.remove(key);
						}
					}
					
					
					lliLocalLoopBreakDown = new LLILocalLoopBreakDown();
					
					lliLocalLoopBreakDown.setFromDate(instance.getActiveFrom() < firstDay ? firstDay : instance.getActiveFrom());
					lliLocalLoopBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
					lliLocalLoopBreakDown.setId(loop.getID());
					lliLocalLoopBreakDown.setOfficeId(loop.getLliOfficeHistoryID());
					lliLocalLoopBreakDown.setConnectionId(instance.getHistoryID());
					lliLocalLoopBreakDown.setPortID(loop.getPortID());
					lliLocalLoopBreakDown.setVendorId(loop.getOCID());
					
					lliLocalLoopBreakDown.setFiberCableType(loop.getOfcType());
					lliLocalLoopBreakDown.setValue(calculatableDistance);
					lliLocalLoopBreakDown.setBtclLength(loop.getBtclDistance());
					lliLocalLoopBreakDown.setVendorLength(loop.getOCDistance());


					localLoopBreakDownMap.putIfAbsent(key, lliLocalLoopBreakDown);
				}
			}
			
		}

		localLoopBreakDowns.addAll(localLoopBreakDownMap.values());
		
		return localLoopBreakDowns;
	}
	

	private DayWiseUsage[] getDaywiseLongTermUsage(List<LLILongTermContract> lliLongTermContracts, DayWiseUsage[] bwPerDay)
	{
		
		long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
		long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
		double[] ltDaywise = new double[50];
		
		for( LLILongTermContract longTermContract : lliLongTermContracts)
		{
			long fromDate = longTermContract.getActiveFrom() < firstDay ? firstDay : longTermContract.getActiveFrom();
			long toDate = longTermContract.getActiveTo() > lastDay ? lastDay : longTermContract.getActiveTo();
			
			int days = DateUtils.getDiffInDays(fromDate, toDate);
			if(days == 0)
				continue;
			
			int dayOfMonth = DateUtils.getDayOfMonth(fromDate) - 1;	//-1 for 0 based indexing
			int i = 0;
			while (i++ < days)
			{
				ltDaywise[dayOfMonth++] += longTermContract.getBandwidth();
			}
		}
		
		
		DayWiseUsage[] ltPerDay = new DayWiseUsage[31];
		for(int i=0; i< 31; i++)
		{
			ltPerDay[i] = new DayWiseUsage();
		}
		
		for(int i=0; i < 31; i++)
		{
			if(ltDaywise[i] == 0)
				continue;
			
			List<Double> discountRates = new ArrayList<>(bwPerDay[i].discountUsage.keySet());
			
			Collections.sort(discountRates);
			
			ltPerDay[i].value = ltDaywise[i];
			
			for(Double discountRate : discountRates)
			{
				double bandwidth = bwPerDay[i].discountUsage.get(discountRate);
				
				if(ltDaywise[i] <= bandwidth)
				{
					bwPerDay[i].discountUsage.put(discountRate, bandwidth - ltDaywise[i]);
					ltPerDay[i].discountUsage.put(discountRate, ltDaywise[i]);
					ltDaywise[i] = 0;
					break;
				}
				else
				{
					bwPerDay[i].discountUsage.put(discountRate, 0.0);
					ltPerDay[i].discountUsage.put(discountRate, bandwidth);
					ltDaywise[i] -= bandwidth;
				}
			}
		}
		return ltPerDay;
	}
	
	private DayWiseUsage[] getDaywiseBWUsage(LLIMonthlyUsageByClient lliMonthlyUsageByClient, int type)
	{
		DayWiseUsage[] bwPerDay = new DayWiseUsage[31];
		for(int i=0; i< 31; i++)
		{
			bwPerDay[i] = new DayWiseUsage();
		}
		
		for( LLIMonthlyUsageByConnection connection : lliMonthlyUsageByClient.getMonthlyUsageByConnections())
		{
			if(connection.getType() != type)
				continue;
			
			for(ConnectionBandwidthBreakDown bandWidth : connection.getConnectionBandwidthBreakDowns()) 
			{
				int days = DateUtils.getDiffInDays(bandWidth.getFromDate(), bandWidth.getToDate());
				if(days == 0)
					continue;
				
				int dayOfMonth = DateUtils.getDayOfMonth(bandWidth.getFromDate()) - 1;	//-1 for 0 based indexing
				int i = 0;
				while (i++ < days)
				{
					
					bwPerDay[dayOfMonth].value += bandWidth.getValue();
					//bwPerDay[dayOfMonth].rate = bandWidth.getRate();
					
					
					if(bwPerDay[dayOfMonth].discountUsage.containsKey(connection.getDiscountRate()))
					{
						Double val = bwPerDay[dayOfMonth].discountUsage.get(connection.getDiscountRate());
						val += bandWidth.getValue();
						bwPerDay[dayOfMonth].discountUsage.put(connection.getDiscountRate(), val);
					}
					else
					{
						bwPerDay[dayOfMonth].discountUsage.put(connection.getDiscountRate(), bandWidth.getValue());
					}
					
					dayOfMonth++;
				}	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			}
		}
		
		return bwPerDay;
	}

	private String getIncidentInString(Integer incidentId)
	{
		if(LLIConnectionConstants.applicationTypeNameMap.containsKey(incidentId))
			return LLIConnectionConstants.applicationTypeNameMap.get(incidentId);
		
		return "";
	}

    /**
     * fetch tariff category and then
     * fetch TableDTO using that tariff category
     * TableDTO contains the BW rate chart
     * @param clientId
     * @return TableDTO
     */
	@Transactional(transactionType = TransactionType.READONLY)
	private TableDTO getTableDTO(long clientId) throws Exception {
		TableDTO tableDTO = null;
		Integer tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_LLI, clientId);
		
		if(tariffCategory == null)
			return tableDTO;
		
		if(mapOfTableDTOs.containsKey(tariffCategory))
		{
			tableDTO = mapOfTableDTOs.get(tariffCategory);
		}
		else
		{
			try {
				long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
				tableDTO = costConfigService.getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(lastDay, ModuleConstants.Module_ID_LLI, tariffCategory);
				
			} catch (Exception e) {
			}
		}
			
		return tableDTO;
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

    /**
     * save the calculated long term benefits client consumed last month
     * @param clientId
     */
    @Transactional
	private void saveLongTermBenefits(long clientId)
    {
        List<LLILongTermBenefit> ltBenefitsFromDB = lliLongTermBenefitService.getActiveListByClientId(clientId);

        ltBenefitsForClient.forEach((k, v)->
                {
                    LLILongTermBenefit obj =  ltBenefitsFromDB.stream()
                            .filter(x->x.getContractId().equals(k))
                            .findFirst()
                            .orElse(null);

                    if(obj == null) {
                        lliLongTermBenefitService.save(LLILongTermBenefit.builder()
                                .contractId(k)
                                .clientId(clientId)
                                .amount(NumberUtils.formattedValue(v))
                                .build());
                    }
                    else
                    {
                        obj.setAmount(NumberUtils.formattedValue(obj.getAmount() + v));
                        lliLongTermBenefitService.save(obj);
                    }
                }
        );

    }

    /**
     * calculate the vendor part's outsource bill which will be paid to vender due to clients
     * @param lliMonthlyUsageByClient
     */
    private void calculateAndSaveOutsourceBill(LLIMonthlyUsageByClient lliMonthlyUsageByClient)
	{
        Map<Long, List<LLIMonthlyOutsourceBillByConnection>> vendor_vs_connection_outsourceBill = calculateConnectionWiseVendorCharge(lliMonthlyUsageByClient);

        for (Long vendorId : vendor_vs_connection_outsourceBill.keySet()) {

            List<LLIMonthlyOutsourceBillByConnection> outsourceBillByConnections = vendor_vs_connection_outsourceBill.get(vendorId);
            LLIMonthlyOutsourceBill outsourceBill = calculateVendorCharge(vendorId, outsourceBillByConnections);

            try {
                saveOutsourceBill(outsourceBill, outsourceBillByConnections);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    private LLIMonthlyOutsourceBill calculateVendorCharge(Long vendorId, List<LLIMonthlyOutsourceBillByConnection> outsourceBillByConnections)
    {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        LLIMonthlyOutsourceBill outsourceBill;
        if(lliOutsourceBillMap.containsKey(vendorId))
            outsourceBill = lliOutsourceBillMap.get(vendorId);
        else
            outsourceBill = LLIMonthlyOutsourceBill.builder()
                    .vendorId(vendorId)
                    .month(DateUtils.getMonthFromDate(firstDay))
                    .year(DateUtils.getYearFromDate(firstDay))
                    .status(OutsourceBillStatus.ACTIVE)
                    .build();

        for (LLIMonthlyOutsourceBillByConnection outsourceBillByConnection: outsourceBillByConnections) {

            outsourceBill.setTotalDue(NumberUtils.formattedValue(outsourceBill.getTotalDue() + outsourceBillByConnection.getTotalDue()));
            outsourceBill.setTotalPayable(NumberUtils.formattedValue(outsourceBill.getTotalPayable() + outsourceBillByConnection.getTotalPayable()));
            outsourceBill.setLoopLength(NumberUtils.formattedValue(outsourceBill.getLoopLength() + outsourceBillByConnection.getVendorLength()));
            outsourceBill.setLoopLengthSingle(NumberUtils.formattedValue(outsourceBill.getLoopLengthSingle() + outsourceBillByConnection.getLoopLengthSingle()));
            outsourceBill.setLoopLengthDouble(NumberUtils.formattedValue(outsourceBill.getLoopLengthDouble() + outsourceBillByConnection.getLoopLengthDouble()));
        }

        lliOutsourceBillMap.putIfAbsent(outsourceBill.getVendorId(), outsourceBill);

        return outsourceBill;
    }

    private Map<Long, List<LLIMonthlyOutsourceBillByConnection>> calculateConnectionWiseVendorCharge(LLIMonthlyUsageByClient lliMonthlyUsageByClient)
    {
        Map<Long, List<LLIMonthlyOutsourceBillByConnection>> vendor_vs_connection_outsourceBill = new HashMap<>();

        for (LLIMonthlyUsageByConnection connectionUsage : lliMonthlyUsageByClient.getMonthlyUsageByConnections())
        {
            for (LLILocalLoopBreakDown loopBreakDown : connectionUsage.localLoopBreakDowns)
            {
            	if(loopBreakDown.getVendorId() == null || loopBreakDown.getVendorLength() == 0)
            		continue;

                double vendorsCharge = loopBreakDown.getCost()*loopBreakDown.getVendorLength()/loopBreakDown.getValue();

				if(! vendor_vs_connection_outsourceBill.containsKey(loopBreakDown.getVendorId()))
                    vendor_vs_connection_outsourceBill.put(loopBreakDown.getVendorId(), new ArrayList<>());

                List<LLIMonthlyOutsourceBillByConnection> outsourceBillByConnections = vendor_vs_connection_outsourceBill.get(loopBreakDown.getVendorId());
				LLIMonthlyOutsourceBillByConnection outsourceBillByConnection = outsourceBillByConnections.stream()
						.filter(x-> x.getConnectionId() == connectionUsage.getConnectionId())
						.findFirst()
						.orElse(null);

                if(outsourceBillByConnection == null) {
                    outsourceBillByConnection = LLIMonthlyOutsourceBillByConnection.builder()
                            .connectionId(connectionUsage.getConnectionId())
                            .build();
                    outsourceBillByConnections.add(outsourceBillByConnection);
                }

				outsourceBillByConnection.setBtclLength(NumberUtils.formattedValue(outsourceBillByConnection.getBtclLength() + loopBreakDown.getBtclLength()));
				outsourceBillByConnection.setVendorLength(NumberUtils.formattedValue(outsourceBillByConnection.getVendorLength() + loopBreakDown.getVendorLength()));
				outsourceBillByConnection.setTotalDue(NumberUtils.formattedValue(outsourceBillByConnection.getTotalDue() + vendorsCharge));
				outsourceBillByConnection.setTotalPayable(NumberUtils.formattedValue(outsourceBillByConnection.getTotalPayable() + vendorsCharge * ratioOfOutsourceBillForVendor));

				if(loopBreakDown.getFiberCableType() == 1)
					outsourceBillByConnection.setLoopLengthSingle(NumberUtils.formattedValue(outsourceBillByConnection.getLoopLengthSingle() + loopBreakDown.getVendorLength()));
				else
					outsourceBillByConnection.setLoopLengthDouble(NumberUtils.formattedValue(outsourceBillByConnection.getLoopLengthDouble() + loopBreakDown.getVendorLength()));
            }
        }

        return vendor_vs_connection_outsourceBill;
    }

    @Transactional
    private void saveOutsourceBill(LLIMonthlyOutsourceBill outsourceBill, List<LLIMonthlyOutsourceBillByConnection> outsourceBillByConnections) throws Exception {
        lliMonthlyOutsourceBillService.save(outsourceBill);
        if(outsourceBill.getId() == null)
            throw new Exception("outsource bill not saving");

        for (LLIMonthlyOutsourceBillByConnection outsourceBillByConnection : outsourceBillByConnections)
        {
            outsourceBillByConnection.setOutsourceBillId(outsourceBill.getId());
            lliMonthlyOutsourceBillByConnectionService.save(outsourceBillByConnection);
        }
    }

}
