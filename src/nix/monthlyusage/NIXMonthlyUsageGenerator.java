package nix.monthlyusage;

import accounting.AccountingEntryService;
import annotation.Transactional;
import client.ClientTypeService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.ModuleConstants;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import inventory.InventoryConstants;
import inventory.InventoryService;
import lli.configuration.LLICostConfigurationService;
import lli.configuration.LLIFixedCostConfigurationDTO;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.common.NIXClientService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.localloop.NIXLocalLoop;
import nix.monthlybill.NIXLocalLoopBreakDown;
import nix.monthlybill.NIXPortBreakDown;
import nix.nixportconfig.NIXPortConfigService;
import nix.office.NIXOffice;
import nix.outsourcebill.*;
import org.apache.log4j.Logger;
import requestMapping.Service;
import user.UserDTO;
import user.UserService;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class NIXMonthlyUsageGenerator {

    static Logger logger = Logger.getLogger(NIXMonthlyUsageGenerator.class);

    class DayWiseUsage {
        double value;
        Map<Double, Double> discountUsage = new HashMap<>();
    }

    @Service
    NIXClientService NIXClientService;
    @Service
    NIXPortConfigService nixPortConfigService;

    @Service
    NIXApplicationLocalLoopService nixApplicationLocalLoopService;
    @Service
    NIXConnectionService nixConnectionService;
    @Service
    DistrictOfcInstallationService districtOfcInstallationService;
    @Service
    CostConfigService costConfigService;
    @Service
    LLICostConfigurationService nixCostConfigurationService;
    @Service
    InventoryService inventoryService;
    @Service
    AccountingEntryService accountingEntryService;
    @Service
    OfcInstallationCostService ofcInstallationCostService;

    @Service
    NIXMonthlyUsageByClientService nixMonthlyUsageByClientService;
    @Service
    NIXMonthlyUsageByConnectionService nixMonthlyUsageByConnectionService;

    @Service
    UserService userService;
    @Service
    NIXMonthlyOutsourceBillService nixMonthlyOutsourceBillService;
    @Service
    NIXMonthlyOutsourceBillByConnectionService nixMonthlyOutsourceBillByConnectionService;

    //not dependant on client
    List<UserDTO> vendors = new ArrayList<>();
    List<ClientDetailsDTO> nixClients = new ArrayList<>();
    List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
    Map<Long, TableDTO> mapOfTableDTOs = new HashMap<>();
    Map<Long, NIXMonthlyOutsourceBill> nixOutsourceBillMap = new HashMap<>();

    double cacheFlatRate = 0;
    double vatRate = 0;
    int localLoopMinimumCost = 0;
    int localLoopDistanceCheckpoint = 0;
    double ratioOfOutsourceBillForVendor = 0.9; //vendor will get 90% of total local loop bill

    //clients related start
    List<NIXConnection> nixConnections = new ArrayList<>();
    List<NIXConnection> nixConnectionIncidents = new ArrayList<>();
    Map<Long, Double> ltBenefitsForClient = new HashMap<>();
    //client related ends

    public NIXMonthlyUsageGenerator() {

    }

    /**
     * load data which are needed for bill generation
     */
    private void loadData() {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        //fetch fixed rate from cost configuration
        try {

            List<LLIFixedCostConfigurationDTO> nixFixedCostConfigurationDTOs = nixCostConfigurationService.getLLI_FixedCostConfigurationDTOByDateRange(firstDay, lastDay);
            if (nixFixedCostConfigurationDTOs.size() != 0) {
                LLIFixedCostConfigurationDTO nixFixedCostConfigurationDTO = nixFixedCostConfigurationDTOs.get(0);
                vatRate = nixFixedCostConfigurationDTO.getMaximumVatPercentage();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.info("cacheFlatRate : " + cacheFlatRate + " ; vatRate : " + vatRate);

        OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostService.getLatestByDate(lastDay);

        if (ofcInstallationCostDTO != null) {
            localLoopMinimumCost = ofcInstallationCostDTO.getFiberCost();
            localLoopDistanceCheckpoint = ofcInstallationCostDTO.getFiberLength();
        }


        getAllVendor();
//        getOutsourceBills();
        getAllClient();
        getAllConnectionIncidents();
        getAllDistrictOfcInstallationCost();
    }

    private void clearData() {
        vendors.clear();
        nixClients.clear();
        nixConnections.clear();
        districtOfcInstallationDTOs.clear();
        mapOfTableDTOs.clear();
//        nixOutsourceBillMap.clear();
    }

    /**
     * fetch all vendor list to calculate their last month charge (outsourcing bill)
     */
    private void getAllVendor() {
        vendors.clear();
        vendors.addAll(userService.getLocalLoopProviderList());
    }

    /**
     * get already calculated outsource bill for last month <br>
     * keep it in map so that we can update vendor's bill without fetching from db before each saving
     */
//    @Transactional(transactionType= TransactionType.READONLY)
//    private void getOutsourceBills()
//    {
//        long datetime = DateUtils.getLastDayOfMonth(-1).getTime();;
////        nixOutsourceBillMap.clear();
//        for(NIXMonthlyOutsourceBill monthlyOutsourceBill : nixMonthlyOutsourceBillService.getByMonthByYear(
//                DateUtils.getMonthFromDate(datetime),
//                DateUtils.getYearFromDate(datetime)))
//        {
//            nixOutsourceBillMap.putIfAbsent(monthlyOutsourceBill.getVendorId(), monthlyOutsourceBill);
//        }
//
//    }

    /**
     * fetch all the clients which were active in last month
     */
    @Transactional(transactionType = TransactionType.READONLY)
    private void getAllClient() {
        Date toDate = DateUtils.getLastDayOfMonth(-1);
        nixClients.clear();
        nixClients.addAll(
                NIXClientService.getAllNIXClient()
                        .stream()
                        .filter(x -> x.getActivationDate() < toDate.getTime())
                        .collect(Collectors.toList()));
    }

    /**
     * fetch last month connection related incidents <br>
     * from those incidents get unique connections
     */
    @Transactional(transactionType = TransactionType.READONLY)
    private void getAllConnectionIncidents() {
        Date fromDate = DateUtils.getFirstDayOfMonth(-1);
        Date toDate = DateUtils.getLastDayOfMonth(-1);

        try {
            //get connection incidents
            nixConnectionIncidents = nixConnectionService.getNIXConnectionInstanceListByDateRange(fromDate.getTime(), toDate.getTime())
                    .stream()
                    .filter(x -> x.getActiveTo() > fromDate.getTime())
                    //this is required as we are converting toDate to the last day while fetching using @ManipulateEndDate
                    .collect(Collectors.toList());

            //get unique connections
            long lastDateLastHourOfLastMonth = toDate.getTime();
            long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);
			
		/*	nixConnections = nixConnectionService.getNIXConnectionListByDateRange(lastDateFirstHourOfLastMonth, lastDateLastHourOfLastMonth)
					.stream()
					.filter(x->x.getStartDate() <= lastDateLastHourOfLastMonth)
					.collect(Collectors.toList());*/
            nixConnections = nixConnectionIncidents
                    .stream()
                    .filter(x -> x.getActiveFrom() < lastDateLastHourOfLastMonth
                            && x.getActiveTo() > lastDateFirstHourOfLastMonth
                            && x.getStartDate() <= lastDateLastHourOfLastMonth)
                    .collect(Collectors.toList());

            System.out.println(nixConnections.get(1).toString());


        } catch (Exception e) {
            logger.fatal("error in getAllConnection incidents");
        }
    }

    /**
     * fetch district wise ofc installation cost
     */
    @Transactional(transactionType = TransactionType.READONLY)
    private void getAllDistrictOfcInstallationCost() {
        try {
            districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();//taking latest. cause no way to fetch old data
        } catch (Exception e) {
            logger.fatal("error in getAllDistrictOfcInstallationCost");
        }
    }

    /**
     * this method is called for generating bill for all clients
     *
     * @return list of last month bill for those clients
     */
    public List<NIXMonthlyUsageByClient> generateMonthlyUsage() {

        List<NIXMonthlyUsageByClient> nixMonthlyUsageByClients = generateMonthlyUsage(nixClients);
        return nixMonthlyUsageByClients;
    }

    /**
     * this method is called for generating bill for provided clients
     *
     * @param clients for whom bill will be generated
     * @return list of last month bill for those clients
     */
    public List<NIXMonthlyUsageByClient> generateMonthlyUsage(List<ClientDetailsDTO> clients) {

        loadData();

        List<NIXMonthlyUsageByClient> nixMonthlyUsageByClients = new ArrayList<>();
        for (ClientDetailsDTO client : clients) {
            try {
                //get the TableDTO which contains BW rate chart
//                TableDTO tableDto = getTableDTO(client.getClientID());
//                if (tableDto == null)
//                    continue;

                NIXMonthlyUsageByClient bill = populateMonthlyUsage(client.getClientID());

                calculateBill(bill);
                save(bill);
                nixMonthlyUsageByClients.add(bill);

            } catch (ParseException e) {
                logger.error("error while populating monthly usage for client " + client.getClientID());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("error while saving monthly usage for client " + client.getClientID());
                e.printStackTrace();
            }
        }

        clearData();
        return nixMonthlyUsageByClients;
    }

    /**
     * save monthly usage of a client <br>
     * then save connection wise usage using that client's monthly usage id <br>
     * save long term benefits that client consumed <br>
     * then save outsource bill for the vendors who were involved in that client's connections
     *
     * @param nixMonthlyUsageByClient
     */
    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void save(NIXMonthlyUsageByClient nixMonthlyUsageByClient) {
        nixMonthlyUsageByClientService.save(nixMonthlyUsageByClient);
        if (nixMonthlyUsageByClient.getId() == null)
            return;

        for (NIXMonthlyUsageByConnection connection : nixMonthlyUsageByClient.getMonthlyUsageByConnections()) {
            connection.setMonthlyUsageByClientId(nixMonthlyUsageByClient.getId());
            nixMonthlyUsageByConnectionService.save(connection);
        }

//		saveLongTermBenefits(nixMonthlyUsageByClient.getClientId());
        //todo :outsource bill
        calculateAndSaveOutsourceBill(nixMonthlyUsageByClient);
    }

    private NIXMonthlyUsageByClient populateMonthlyUsage(long clientId) throws Exception {

        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();

        Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());
        NIXMonthlyUsageByClient bill = new NIXMonthlyUsageByClient();

        bill.setClientId(clientId);
        bill.setCreatedDate(date.getTime());
        bill.setDeleted(false);
        bill.setMonth(DateUtils.getMonthFromDate(firstDay));
        bill.setYear(DateUtils.getYearFromDate(firstDay));


        // get connections of that client
        List<NIXConnection> nixConnectionList = nixConnections
                .stream()
                .filter(x -> x.getClient() == clientId)
                .collect(Collectors.toList());

        //get breakdowns calculation
        List<NIXMonthlyUsageByConnection> nixMonthlyUsageByConnections=new ArrayList<>();
        for (NIXConnection nixConnection : nixConnectionList) {
            // get connection incidents
            List<NIXConnection> nixConnectionIncidentList = nixConnectionIncidents
                    .stream()
                    .filter(x -> x.getConnectionId() == nixConnection.getConnectionId())
                    .collect(Collectors.toList());

            //nixConnectionIncidentList.sort((x,y)-> x.getActiveFrom() < y.getActiveFrom() ? -1 : 1);

            NIXMonthlyUsageByConnection nixMonthlyUsageByConnection = new NIXMonthlyUsageByConnection();
            nixMonthlyUsageByConnection.setConnectionId(nixConnection.getId());
            nixMonthlyUsageByConnection.setClientId(nixConnection.getClient());
//			nixMonthlyUsageByConnection.setType(nixConnection.getConnectionType());
            nixMonthlyUsageByConnection.setName(nixConnection.getName());
            nixMonthlyUsageByConnection.setCreatedDate(DateUtils.getCurrentTime());
//			nixMonthlyUsageByConnection.setDiscountRate(nixConnection.getDiscountRate());
            nixMonthlyUsageByConnection.setDiscountRate(0);
            nixMonthlyUsageByConnection.setVatRate(vatRate);

            nixMonthlyUsageByConnection.setLocalLoopBreakDowns(getLocalLoopBreakDowns(nixConnectionIncidentList));
            nixMonthlyUsageByConnection.setNixPortBreakDowns(getPortBreakDowns(nixConnectionIncidentList));

            calculateLocalLoopCost(nixMonthlyUsageByConnection);
            calculatePortCost(nixMonthlyUsageByConnection);

            nixMonthlyUsageByConnection.setRemark(NIXConstants.nixapplicationTypeHyphenSeperatedMap.get(nixConnection.getIncidentId()));

            bill.getMonthlyUsageByConnections().add(nixMonthlyUsageByConnection);

            //set json contents
            JsonArray loopArray = new JsonArray();
            for (NIXLocalLoopBreakDown nixLocalLoopBreakDown : nixMonthlyUsageByConnection.getLocalLoopBreakDowns()
            ) {
                JsonElement jsonElement = new Gson().toJsonTree(nixLocalLoopBreakDown);
                loopArray.add(jsonElement);

            }

            //set json contents
            JsonArray portArray = new JsonArray();
            for (NIXPortBreakDown nixPortBreakDown : nixMonthlyUsageByConnection.getNixPortBreakDowns()
            ) {
                JsonElement jsonElement = new Gson().toJsonTree(nixPortBreakDown);
                portArray.add(jsonElement);

            }
            nixMonthlyUsageByConnection.setLocalLoopBreakDownsContent(loopArray.toString());
            nixMonthlyUsageByConnection.setPortBreakDownsContent(portArray.toString());
            nixMonthlyUsageByConnections.add(nixMonthlyUsageByConnection);

        }
        bill.setMonthlyUsageByConnections(nixMonthlyUsageByConnections);


        //TODO :need to get yearly fee for additional ip

        return bill;
    }

    /**
     * calculate the loop cost of a connection usage using ofc installation cost which is found by district of inventory common
     *
     * @param nixMonthlyUsageByConnection
     */
    @Transactional(transactionType = TransactionType.READONLY)
    private void calculateLocalLoopCost(NIXMonthlyUsageByConnection nixMonthlyUsageByConnection) {

        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
        int totalDays = DateUtils.getDaysInMonth(firstDay);

        double totalLoopCost = 0;
        for (NIXLocalLoopBreakDown loop : nixMonthlyUsageByConnection.getLocalLoopBreakDowns()) {
            int days = DateUtils.getDiffInDays(loop.getFromDate(), loop.getToDate());
            if (days == 0)
                return;

            double loopLength = loop.getValue();
            if (loopLength == 0)
                continue;

            //initial flat rate
            double loopCost = localLoopMinimumCost * days / totalDays;    //for those days
            loopLength -= localLoopDistanceCheckpoint;

            if (loopLength > 0) {
                long districtID = inventoryService.getInventoryParentItemPathMapUptoRootByItemID(loop.getPortID()).get(InventoryConstants.CATEGORY_ID_DISTRICT).getID();
                Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x -> x.getDistrictID() == districtID).findFirst();

                loopCost += loopLength * (ofcInstallationCostByDistrictID.isPresent() ?
                        ofcInstallationCostByDistrictID.get().getInstallationCost() * days / totalDays    //for those days
                        : 0);
            }

            //calculation using OFC type
            loopCost *= loop.getFiberCableType();

            loop.setCost(loopCost);
            totalLoopCost += loopCost;
        }

        nixMonthlyUsageByConnection.setLoopCost(NumberUtils.formattedValue(totalLoopCost));
    }


    @Transactional(transactionType = TransactionType.READONLY)
    private void calculatePortCost(NIXMonthlyUsageByConnection nixMonthlyUsageByConnection) throws Exception {

        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
        int totalDays = DateUtils.getDaysInMonth(firstDay);

        double totalPortCost = 0;
        for (NIXPortBreakDown port : nixMonthlyUsageByConnection.getNixPortBreakDowns()) {
            int days = DateUtils.getDiffInDays(port.getFromDate(), port.getToDate());
            if (days == 0)
                return;

            double portType = port.getPortType();
            if (portType <= 0)
                continue;
            double portCost = (nixPortConfigService.getPortConfigByPortType(port.getPortType()).getPortCharge() * days)/ totalDays;    //for those days


            port.setCost(portCost);
            totalPortCost += portCost;
        }

        nixMonthlyUsageByConnection.setPortCost(NumberUtils.formattedValue(totalPortCost));
    }


    /**
     * now calculate the total bill <br>
     * connection wise as well as client wise
     *
     * @param nixMonthlyUsageByClient
     */
    private void calculateBill(NIXMonthlyUsageByClient nixMonthlyUsageByClient) {

        double grandTotal = 0;
        double totalDiscount = 0;

        for (NIXMonthlyUsageByConnection connection : nixMonthlyUsageByClient.getMonthlyUsageByConnections()) {
//			connection.setDiscount(NumberUtils.formattedValue(connection.getMbpsCost()* connection.getDiscountRate()/100));
            connection.setDiscount(NumberUtils.formattedValue(0));
            connection.setGrandCost(NumberUtils.formattedValue(connection.getLoopCost() + connection.getPortCost() - connection.getDiscount()));

            connection.setVat(NumberUtils.formattedValue(connection.getGrandCost() * connection.getVatRate() / 100));
            connection.setTotalCost(NumberUtils.formattedValue(connection.getGrandCost() + connection.getVat()));

            grandTotal += connection.getGrandCost();
            totalDiscount += connection.getDiscount();
        }

//		totalDiscount = totalDiscount - nixMonthlyUsageByClient.getLongTermContructDiscountAdjustment();
        totalDiscount = totalDiscount;

        //grand total without vat and ltc
        nixMonthlyUsageByClient.setGrandTotal(NumberUtils.formattedValue(grandTotal));

        nixMonthlyUsageByClient.setDiscountPercentage(0);
        nixMonthlyUsageByClient.setDiscount(NumberUtils.formattedValue(totalDiscount));

        nixMonthlyUsageByClient.setTotalPayable(NumberUtils.formattedValue(nixMonthlyUsageByClient.getGrandTotal()));
//				- nixMonthlyUsageByClient.getLongTermContructAdjustment()));

        nixMonthlyUsageByClient.setVatPercentage(vatRate);
        nixMonthlyUsageByClient.setVAT(NumberUtils.formattedValue(nixMonthlyUsageByClient.getTotalPayable() * nixMonthlyUsageByClient.getVatPercentage() / 100));

        nixMonthlyUsageByClient.setNetPayable(NumberUtils.formattedValue(nixMonthlyUsageByClient.getTotalPayable() + nixMonthlyUsageByClient.getVAT()));

    }


    private List<NIXLocalLoopBreakDown> getLocalLoopBreakDowns(List<NIXConnection> nixConnectionIncidents) throws Exception {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        List<NIXLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();

        if (nixConnectionIncidents.size() == 0)
            return localLoopBreakDowns;

        Map<Long, NIXLocalLoopBreakDown> localLoopBreakDownMap = new HashMap<>();

        for (NIXConnection instance : nixConnectionIncidents) {
            if (instance.getStatus() == NIXConstants.STATUS_CLOSED)
                continue;


            for (NIXOffice office : instance.getNixOffices()) {
                for (NIXLocalLoop loop : office.getLocalLoops()) {
                    Long key = loop.getId();
                    NIXApplicationLocalLoop nixApplicationLocalLoop = nixApplicationLocalLoopService.getLocalLoopById(loop.getApplicationLocalLoop());
                    long calculatableDistance = nixApplicationLocalLoop.getBtclDistance() + nixApplicationLocalLoop.getOcdDistance();

                    logger.info("connection name : " + instance.getName() + " ; key : " + key + "; loop : " + calculatableDistance);

                    NIXLocalLoopBreakDown nixLocalLoopBreakDown = null;
                    if (localLoopBreakDownMap.containsKey(key)) {
                        nixLocalLoopBreakDown = localLoopBreakDownMap.get(key);

                        if (nixLocalLoopBreakDown.getValue() == calculatableDistance &&
                                nixLocalLoopBreakDown.getFiberCableType() == nixApplicationLocalLoop.getOfcType()) {

                            nixLocalLoopBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
                            continue;
                        }
                        //change occurred
                        //remove from map and add into nixMonthlyUsageByConnection
                        else {
                            localLoopBreakDowns.add(nixLocalLoopBreakDown);
                            localLoopBreakDownMap.remove(key);
                        }
                    }


                    nixLocalLoopBreakDown = new NIXLocalLoopBreakDown();

                    nixLocalLoopBreakDown.setFromDate(instance.getActiveFrom() < firstDay ? firstDay : instance.getActiveFrom());
                    nixLocalLoopBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
                    nixLocalLoopBreakDown.setId(nixApplicationLocalLoop.getId());
                    nixLocalLoopBreakDown.setOfficeId(loop.getOffice());
                    nixLocalLoopBreakDown.setConnectionId(instance.getConnectionId());
                    nixLocalLoopBreakDown.setPortID(nixApplicationLocalLoop.getPortId());

                    //todo: set oc ID
                    nixLocalLoopBreakDown.setVendorId(nixApplicationLocalLoop.getVendor());

                    nixLocalLoopBreakDown.setFiberCableType(nixApplicationLocalLoop.getOfcType());
                    nixLocalLoopBreakDown.setValue(calculatableDistance);
                    nixLocalLoopBreakDown.setBtclLength(nixApplicationLocalLoop.getBtclDistance());
                    nixLocalLoopBreakDown.setVendorLength(nixApplicationLocalLoop.getOcdDistance());


                    localLoopBreakDownMap.putIfAbsent(key, nixLocalLoopBreakDown);
                }
            }

        }

        localLoopBreakDowns.addAll(localLoopBreakDownMap.values());

        return localLoopBreakDowns;
    }

    private List<NIXPortBreakDown> getPortBreakDowns(List<NIXConnection> nixConnectionIncidents) throws Exception {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        List<NIXPortBreakDown> portBreakDowns = new ArrayList<>();

        if (nixConnectionIncidents.size() == 0)
            return portBreakDowns;

        Map<Long, NIXPortBreakDown> portBreakDownMap = new HashMap<>();

        for (NIXConnection instance : nixConnectionIncidents) {
            if (instance.getStatus() == NIXConstants.STATUS_CLOSED)
                continue;


            for (NIXOffice office : instance.getNixOffices()) {
                for (NIXLocalLoop loop : office.getLocalLoops()) {
                    Long key = loop.getId();
                    NIXApplicationLocalLoop nixApplicationLocalLoop = nixApplicationLocalLoopService.getLocalLoopById(loop.getApplicationLocalLoop());
                    long calculatableDistance = nixApplicationLocalLoop.getBtclDistance() + nixApplicationLocalLoop.getOcdDistance();

                    logger.info("connection name : " + instance.getName() + " ; key : " + key + "; loop : " + calculatableDistance);

                    NIXPortBreakDown nixPortBreakDown = null;
                    if (portBreakDownMap.containsKey(key)) {
                        nixPortBreakDown = portBreakDownMap.get(key);

                        if (nixPortBreakDown.getPortID() == nixApplicationLocalLoop.getPortId()) {

                            nixPortBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
                            continue;
                        }
                        //change occurred
                        //remove from map and add into nixMonthlyUsageByConnection
                        else {
                            portBreakDowns.add(nixPortBreakDown);
                            portBreakDownMap.remove(key);
                        }
                    }


                    nixPortBreakDown = new NIXPortBreakDown();
                    nixPortBreakDown.setFromDate(instance.getActiveFrom() < firstDay ? firstDay : instance.getActiveFrom());
                    nixPortBreakDown.setToDate(instance.getActiveTo() > lastDay ? lastDay : instance.getActiveTo());
                    nixPortBreakDown.setId(nixApplicationLocalLoop.getId());
                    nixPortBreakDown.setOfficeId(loop.getOffice());
                    nixPortBreakDown.setConnectionId(instance.getConnectionId());
                    nixPortBreakDown.setPortID(nixApplicationLocalLoop.getPortId());
                    nixPortBreakDown.setPortType(nixApplicationLocalLoop.getPortType());
                    nixPortBreakDown.setName(InventoryConstants.mapOfPortTypeToPortTypeString.get(nixApplicationLocalLoop.getPortType()));
                    portBreakDownMap.putIfAbsent(key, nixPortBreakDown);
                }
            }

        }

        portBreakDowns.addAll(portBreakDownMap.values());

        return portBreakDowns;
    }

    private String getIncidentInString(Integer incidentId) {
        if (NIXConstants.nixapplicationTypeHyphenSeperatedMap.containsKey(incidentId))
            return NIXConstants.nixapplicationTypeHyphenSeperatedMap.get(incidentId);

        return "";
    }



    /**
     * save the calculated long term benefits client consumed last month
     * @param clientId
     */


    /**
     * calculate the vendor part's outsource bill which will be paid to vender due to clients
     */
    private void calculateAndSaveOutsourceBill(NIXMonthlyUsageByClient nixMonthlyUsageByClient)
	{
        Map<Long, List<NIXMonthlyOutsourceBillByConnection>> vendor_vs_connection_outsourceBill = calculateConnectionWiseVendorCharge(nixMonthlyUsageByClient);

        for (Long vendorId : vendor_vs_connection_outsourceBill.keySet()) {

            List<NIXMonthlyOutsourceBillByConnection> outsourceBillByConnections = vendor_vs_connection_outsourceBill.get(vendorId);
            NIXMonthlyOutsourceBill outsourceBill = calculateVendorCharge(vendorId, outsourceBillByConnections);

            try {
                saveOutsourceBill(outsourceBill, outsourceBillByConnections);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    private NIXMonthlyOutsourceBill calculateVendorCharge(Long vendorId, List<NIXMonthlyOutsourceBillByConnection> outsourceBillByConnections)
    {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        NIXMonthlyOutsourceBill outsourceBill;
        if(nixOutsourceBillMap.containsKey(vendorId))
            outsourceBill = nixOutsourceBillMap.get(vendorId);
        else
            outsourceBill = NIXMonthlyOutsourceBill.builder()
                    .vendorId(vendorId)
                    .month(DateUtils.getMonthFromDate(firstDay))
                    .year(DateUtils.getYearFromDate(firstDay))
                    .status(OutsourceBillStatus.ACTIVE)
                    .build();

        for (NIXMonthlyOutsourceBillByConnection outsourceBillByConnection: outsourceBillByConnections) {

            outsourceBill.setTotalDue(NumberUtils.formattedValue(outsourceBill.getTotalDue() + outsourceBillByConnection.getTotalDue()));
            outsourceBill.setTotalPayable(NumberUtils.formattedValue(outsourceBill.getTotalPayable() + outsourceBillByConnection.getTotalPayable()));
            outsourceBill.setLoopLength(NumberUtils.formattedValue(outsourceBill.getLoopLength() + outsourceBillByConnection.getVendorLength()));
            outsourceBill.setLoopLengthSingle(NumberUtils.formattedValue(outsourceBill.getLoopLengthSingle() + outsourceBillByConnection.getLoopLengthSingle()));
            outsourceBill.setLoopLengthDouble(NumberUtils.formattedValue(outsourceBill.getLoopLengthDouble() + outsourceBillByConnection.getLoopLengthDouble()));
        }

        nixOutsourceBillMap.putIfAbsent(outsourceBill.getVendorId(), outsourceBill);

        return outsourceBill;
    }

    private Map<Long, List<NIXMonthlyOutsourceBillByConnection>> calculateConnectionWiseVendorCharge(NIXMonthlyUsageByClient nixMonthlyUsageByClient)
    {
        Map<Long, List<NIXMonthlyOutsourceBillByConnection>> vendor_vs_connection_outsourceBill = new HashMap<>();

        for (NIXMonthlyUsageByConnection connectionUsage : nixMonthlyUsageByClient.getMonthlyUsageByConnections())
        {
            for (NIXLocalLoopBreakDown loopBreakDown : connectionUsage.localLoopBreakDowns)
            {
            	if(loopBreakDown.getVendorId() == null || loopBreakDown.getVendorLength() == 0)
            		continue;

                double vendorsCharge = loopBreakDown.getCost()*loopBreakDown.getVendorLength()/loopBreakDown.getValue();

				if(! vendor_vs_connection_outsourceBill.containsKey(loopBreakDown.getVendorId()))
                    vendor_vs_connection_outsourceBill.put(loopBreakDown.getVendorId(), new ArrayList<>());

                List<NIXMonthlyOutsourceBillByConnection> outsourceBillByConnections = vendor_vs_connection_outsourceBill.get(loopBreakDown.getVendorId());
				NIXMonthlyOutsourceBillByConnection outsourceBillByConnection = outsourceBillByConnections.stream()
						.filter(x-> x.getConnectionId() == connectionUsage.getConnectionId())
						.findFirst()
						.orElse(null);

                if(outsourceBillByConnection == null) {
                    outsourceBillByConnection = NIXMonthlyOutsourceBillByConnection.builder()
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
    private void saveOutsourceBill(NIXMonthlyOutsourceBill outsourceBill, List<NIXMonthlyOutsourceBillByConnection> outsourceBillByConnections) throws Exception {
        nixMonthlyOutsourceBillService.save(outsourceBill);
        if(outsourceBill.getId() == null)
            throw new Exception("outsource bill not saving");

        for (NIXMonthlyOutsourceBillByConnection outsourceBillByConnection : outsourceBillByConnections)
        {
            outsourceBillByConnection.setOutsourceBillId(outsourceBill.getId());
            nixMonthlyOutsourceBillByConnectionService.save(outsourceBillByConnection);
        }
    }
    public static void main(String[] args) {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        ServiceDAOFactory.getService(NIXMonthlyUsageGenerator.class).generateMonthlyUsage();
    }

}
