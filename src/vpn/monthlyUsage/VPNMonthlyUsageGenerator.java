package vpn.monthlyUsage;

import annotation.Transactional;
import client.ClientTypeService;
import common.ModuleConstants;
import common.UniversalDTOService;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import entity.facade.DemandNoteAutofillFacade;
import entity.localloop.LocalLoop;
import entity.office.Office;
import entity.office.OfficeService;
import global.GlobalService;
import inventory.InventoryService;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lli.monthlyBill.ConnectionBandwidthBreakDown;
import org.apache.log4j.Logger;
import requestMapping.Service;
import user.UserDTO;
import user.UserService;
import util.*;
import vpn.VPNClientService;
import vpn.VPNConstants;
import vpn.VPNOTC;
import vpn.client.ClientDetailsDTO;
import vpn.monthlyBill.VPNLocalLoopBreakDown;
import vpn.monthlyOutsourceBill.*;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class VPNMonthlyUsageGenerator {

    static Logger logger = Logger.getLogger(VPNMonthlyUsageGenerator.class);

    @Service
    OfcInstallationCostService ofcInstallationCostService;
    @Service
    UserService userService;
    @Service
    VPNClientService vpnClientService;
    @Service
    GlobalService globalService;
    @Service
    InventoryService inventoryService;
    @Service
    CostConfigService costConfigService;
    @Service
    VPNNetworkLinkService vpnNetworkLinkService;
    @Service
    DistrictOfcInstallationService districtOfcInstallationService;
    @Service
    VPNMonthlyUsageByLinkService vpnMonthlyUsageByLinkService;
    @Service
    VPNMonthlyUsageByClientService vpnMonthlyUsageByClientService;
    @Service
    DemandNoteAutofillFacade demandNoteAutofillFacade;
    @Service
    OfficeService officeService;
    @Service
    VPNMonthlyOutsourceBillService vpnMonthlyOutsourceBillService;
    @Service
    VPNMonthlyOutsourceBillByLinkService vpnMonthlyOutsourceBillByLinkService;

    List<UserDTO> vendors = new ArrayList<>();
    List<ClientDetailsDTO> vpnClients = new ArrayList<>();
    List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
    Map<Long, VPNMonthlyOutsourceBill> vpnOutsourceBillMap = new HashMap<>();

    double registrationCharge = 0;
    double vatRate = 0;
    int localLoopMinimumCost = 0;
    int localLoopDistanceCheckpoint = 0;
    double ratioOfOutsourceBillForVendor = 0.9; //vendor will get 90% of total local loop bill

    //clients related start
    List<VPNNetworkLink> vpnLinks = new ArrayList<>();
    List<VPNNetworkLink> vpnLinkIncidents = new ArrayList<>();
    //client related ends

    class DayWiseUsage {
        double value;
        Map<Double, Double> discountUsage = new HashMap<>();
    }

    public VPNMonthlyUsageGenerator() {
    }

    public List<VPNMonthlyUsageByClient> generateMonthlyUsage(List<ClientDetailsDTO> clients) {
        loadData();

        List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients = new ArrayList<>();
        for (ClientDetailsDTO client : clients) {
            try {
                //get the TableDTO which contains BW rate chart
                TableDTO tableDto = getTableDTO(client.getClientID());
                if (tableDto == null)
                    continue;

                VPNMonthlyUsageByClient bill = populateMonthlyUsage(client.getClientID(), tableDto);

                calculateBill(bill);
                save(bill);
                vpnMonthlyUsageByClients.add(bill);

            } catch (ParseException e) {
                logger.error("error while populating monthly usage for client " + client.getClientID());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("error while saving monthly usage for client " + client.getClientID());
                e.printStackTrace();
            }
        }

        clearData();
        return vpnMonthlyUsageByClients;
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void save(VPNMonthlyUsageByClient vpnMonthlyUsageByClient) {
        vpnMonthlyUsageByClientService.save(vpnMonthlyUsageByClient);
        if (vpnMonthlyUsageByClient.getId() == null)
            return;

        for (VPNMonthlyUsageByLink link : vpnMonthlyUsageByClient.getMonthlyUsageByLinks()) {
            link.setMonthlyUsageByClientId(vpnMonthlyUsageByClient.getId());
            vpnMonthlyUsageByLinkService.save(link);
        }
        //TODO - Outsource Bill Calculation
        calculateAndSaveOutsourceBill(vpnMonthlyUsageByClient);
    }

    private void calculateAndSaveOutsourceBill(VPNMonthlyUsageByClient vpnMonthlyUsageByClient)
    {
        Map<Long, List<VPNMonthlyOutsourceBillByLink>> vendor_vs_link_outsourceBill = calculateLinkWiseVendorCharge(vpnMonthlyUsageByClient);

        for (Long vendorId : vendor_vs_link_outsourceBill.keySet()) {

            List<VPNMonthlyOutsourceBillByLink> outsourceBillByConnections = vendor_vs_link_outsourceBill.get(vendorId);
            VPNMonthlyOutsourceBill outsourceBill = calculateVendorCharge(vendorId, outsourceBillByConnections);

            try {
                saveOutsourceBill(outsourceBill, outsourceBillByConnections);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    private void saveOutsourceBill(VPNMonthlyOutsourceBill outsourceBill, List<VPNMonthlyOutsourceBillByLink> outsourceBillByLinks) throws Exception {
        vpnMonthlyOutsourceBillService.save(outsourceBill);
        if(outsourceBill.getId() == null)
            throw new Exception("outsource bill not saving");

        for (VPNMonthlyOutsourceBillByLink outsourceBillByLink : outsourceBillByLinks)
        {
            outsourceBillByLink.setOutsourceBillId(outsourceBill.getId());
            vpnMonthlyOutsourceBillByLinkService.save(outsourceBillByLink);
        }
    }

    private VPNMonthlyOutsourceBill calculateVendorCharge(Long vendorId, List<VPNMonthlyOutsourceBillByLink> outsourceBillByConnections)
    {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        VPNMonthlyOutsourceBill outsourceBill;
        if(vpnOutsourceBillMap.containsKey(vendorId))
            outsourceBill = vpnOutsourceBillMap.get(vendorId);
        else
            outsourceBill = VPNMonthlyOutsourceBill.builder()
                    .vendorId(vendorId)
                    .month(DateUtils.getMonthFromDate(firstDay))
                    .year(DateUtils.getYearFromDate(firstDay))
                    .status(OutsourceBillStatus.ACTIVE)
                    .build();

        for (VPNMonthlyOutsourceBillByLink outsourceBillByLink: outsourceBillByConnections) {

            outsourceBill.setTotalDue(NumberUtils.formattedValue(outsourceBill.getTotalDue() + outsourceBillByLink.getTotalDue()));
            outsourceBill.setTotalPayable(NumberUtils.formattedValue(outsourceBill.getTotalPayable() + outsourceBillByLink.getTotalPayable()));
            outsourceBill.setLoopLength(NumberUtils.formattedValue(outsourceBill.getLoopLength() + outsourceBillByLink.getVendorLength()));
            outsourceBill.setLoopLengthSingle(NumberUtils.formattedValue(outsourceBill.getLoopLengthSingle() + outsourceBillByLink.getLoopLengthSingle()));
            outsourceBill.setLoopLengthDouble(NumberUtils.formattedValue(outsourceBill.getLoopLengthDouble() + outsourceBillByLink.getLoopLengthDouble()));
        }

        vpnOutsourceBillMap.putIfAbsent(outsourceBill.getVendorId(), outsourceBill);

        return outsourceBill;
    }




    private Map<Long, List<VPNMonthlyOutsourceBillByLink>> calculateLinkWiseVendorCharge(VPNMonthlyUsageByClient vpnMonthlyUsageByClient)
    {
        Map<Long, List<VPNMonthlyOutsourceBillByLink>> vendor_vs_link_outsourceBill = new HashMap<>();

        for (VPNMonthlyUsageByLink usageByLink : vpnMonthlyUsageByClient.getMonthlyUsageByLinks())
        {
            for (VPNLocalLoopBreakDown loopBreakDown : usageByLink.localLoopBreakDowns)
            {
                if(loopBreakDown.getVendorId() == null || loopBreakDown.getVendorLength() == 0)
                    continue;

                double vendorsCharge = loopBreakDown.getCost()*loopBreakDown.getVendorLength()/loopBreakDown.getValue();

                if(! vendor_vs_link_outsourceBill.containsKey(loopBreakDown.getVendorId()))
                    vendor_vs_link_outsourceBill.put(loopBreakDown.getVendorId(), new ArrayList<>());

                List<VPNMonthlyOutsourceBillByLink> outsourceBillByLinks = vendor_vs_link_outsourceBill.get(loopBreakDown.getVendorId());
                VPNMonthlyOutsourceBillByLink outsourceBillByLink = outsourceBillByLinks.stream()
                        .filter(x-> x.getLinkId() == usageByLink.getLinkId())
                        .findFirst()
                        .orElse(null);

                if(outsourceBillByLink == null) {
                    outsourceBillByLink = VPNMonthlyOutsourceBillByLink.builder()
                            .linkId(usageByLink.getLinkId())
                            .build();
                    outsourceBillByLinks.add(outsourceBillByLink);
                }

                outsourceBillByLink.setBtclLength(NumberUtils.formattedValue(outsourceBillByLink.getBtclLength() + loopBreakDown.getBtclLength()));
                outsourceBillByLink.setVendorLength(NumberUtils.formattedValue(outsourceBillByLink.getVendorLength() + loopBreakDown.getVendorLength()));
                outsourceBillByLink.setTotalDue(NumberUtils.formattedValue(outsourceBillByLink.getTotalDue() + vendorsCharge));
                outsourceBillByLink.setTotalPayable(NumberUtils.formattedValue(outsourceBillByLink.getTotalPayable() + vendorsCharge * ratioOfOutsourceBillForVendor));

                if(loopBreakDown.getFiberCableType() == 1)
                    outsourceBillByLink.setLoopLengthSingle(NumberUtils.formattedValue(outsourceBillByLink.getLoopLengthSingle() + loopBreakDown.getVendorLength()));
                else
                    outsourceBillByLink.setLoopLengthDouble(NumberUtils.formattedValue(outsourceBillByLink.getLoopLengthDouble() + loopBreakDown.getVendorLength()));
            }
        }
        return vendor_vs_link_outsourceBill;
    }




    private void calculateBill(VPNMonthlyUsageByClient vpnMonthlyUsageByClient) {

        double grandTotal = 0;
        double totalDiscount = 0;

        for (VPNMonthlyUsageByLink link : vpnMonthlyUsageByClient.getMonthlyUsageByLinks()) {
            link.setDiscount(NumberUtils.formattedValue(link.getMbpsCost() * link.getDiscountRate() / 100));
            link.setGrandCost(NumberUtils.formattedValue(link.getTotalLoopCost() + link.getMbpsCost()
                    - link.getDiscount()));

            link.setVat(NumberUtils.formattedValue(link.getGrandCost() * link.getVatRate() / 100));
            link.setTotalCost(NumberUtils.formattedValue(link.getGrandCost() + link.getVat()));

            grandTotal += link.getGrandCost();
            totalDiscount += link.getDiscount();
        }

        //grand total without vat and ltc
        vpnMonthlyUsageByClient.setGrandTotal(NumberUtils.formattedValue(grandTotal));

        vpnMonthlyUsageByClient.setDiscountPercentage(vpnMonthlyUsageByClient.getDiscountPercentage());
        vpnMonthlyUsageByClient.setDiscount(NumberUtils.formattedValue(totalDiscount));

        vpnMonthlyUsageByClient.setTotalPayable(NumberUtils.formattedValue(grandTotal - totalDiscount));

        vpnMonthlyUsageByClient.setVatPercentage(vatRate);
        vpnMonthlyUsageByClient.setVAT(NumberUtils.formattedValue(vpnMonthlyUsageByClient.getTotalPayable() *
                vpnMonthlyUsageByClient.getVatPercentage() / 100));

        vpnMonthlyUsageByClient.setNetPayable(NumberUtils.formattedValue(vpnMonthlyUsageByClient.getTotalPayable() +
                vpnMonthlyUsageByClient.getVAT()));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    private TableDTO getTableDTO(long clientId) throws Exception {
        TableDTO tableDTO = null;
        Integer tariffCategory = ClientTypeService.getClientCategoryByModuleIDAndClientID(ModuleConstants.Module_ID_VPN, clientId);

        if (tariffCategory == null)
            return tableDTO;

        try {
            long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
            tableDTO = costConfigService.getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(lastDay, ModuleConstants.Module_ID_VPN, tariffCategory);

        } catch (Exception e) {
        }


        return tableDTO;
    }

    private void loadData() {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        try {

            VPNOTC vpnotc = ServiceDAOFactory.getService(UniversalDTOService.class).get(VPNOTC.class);
            registrationCharge = vpnotc.getRegistrationChargePerLink();
            vatRate = vpnotc.getVatPercentage();
        } catch (Exception e) {

            e.printStackTrace();
        }
        logger.info("registrationCharge : " + registrationCharge + " ; vatRate : " + vatRate);

        OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostService.getLatestByDate(lastDay);

        if (ofcInstallationCostDTO != null) {
            localLoopMinimumCost = ofcInstallationCostDTO.getFiberCost();
            localLoopDistanceCheckpoint = ofcInstallationCostDTO.getFiberLength();
        }
        getAllVendor();
        getOutsourceBills();
        getAllClient();
        getAllLinkIncidents();
        getAllDistrictOfcInstallationCost();
    }

    @Transactional(transactionType=util.TransactionType.READONLY)
    private void getOutsourceBills()
    {
        long datetime = DateUtils.getLastDayOfMonth(-1).getTime();;
        vpnOutsourceBillMap.clear();
        for(VPNMonthlyOutsourceBill outsourceBill : vpnMonthlyOutsourceBillService.getByMonthByYear(
                DateUtils.getMonthFromDate(datetime),
                DateUtils.getYearFromDate(datetime)))
        {
            vpnOutsourceBillMap.putIfAbsent(outsourceBill.getVendorId(), outsourceBill);
        }
    }


    private void getAllVendor() {
        vendors.clear();
        vendors.addAll(userService.getLocalLoopProviderList());
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void getAllLinkIncidents() {
        Date fromDate = DateUtils.getFirstDayOfMonth(-1);
        Date toDate = DateUtils.getLastDayOfMonth(-1);

        try {
            //get connection incidents
            vpnLinkIncidents = vpnNetworkLinkService.getVPNLinksByDateRange(fromDate.getTime(), toDate.getTime())
                    .stream()
                    .filter(x -> x.getActiveTo() > fromDate.getTime())
                    //this is required as we are converting toDate
                    // to the last day while fetching using @ManipulateEndDate
                    .collect(Collectors.toList());

            //vpnLinks will contain only those links from vpnLinkIncidents
            // that were active in the last day of last month
            long lastDateLastHourOfLastMonth = toDate.getTime();
            long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);

            vpnLinks = vpnLinkIncidents.stream()
                    .filter(x -> x.getActiveFrom() < lastDateLastHourOfLastMonth
                            && x.getActiveTo() > lastDateFirstHourOfLastMonth)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.fatal("error in getAllLink incidents VPN");
        }
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void getAllClient() {
        Date toDate = DateUtils.getLastDayOfMonth(-1);
        vpnClients.clear();
        vpnClients.addAll(
                vpnClientService.getAllVPNClient()
                        .stream()
                        .filter(x -> x.getActivationDate() < toDate.getTime())
                        .collect(Collectors.toList()));
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void getAllDistrictOfcInstallationCost() {
        try {
            districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();//taking latest. cause no way to fetch old data
        } catch (Exception e) {
            logger.fatal("error in getAllDistrictOfcInstallationCost");
        }
    }

    private void clearData() {
        vendors.clear();
        vpnClients.clear();
        vpnLinks.clear();
        districtOfcInstallationDTOs.clear();
        vpnOutsourceBillMap.clear();
        vpnLinkIncidents.clear();
    }

    private VPNMonthlyUsageByClient populateMonthlyUsage(long clientId, TableDTO tableDto) throws Exception {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();

        Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());
        VPNMonthlyUsageByClient bill = new VPNMonthlyUsageByClient();

        bill.setClientId(clientId);
        bill.setCreatedDate(date.getTime());
        bill.setDeleted(false);
        bill.setMonth(DateUtils.getMonthFromDate(firstDay));
        bill.setYear(DateUtils.getYearFromDate(firstDay));

        // get links of that client
        List<VPNNetworkLink> links = vpnLinks.stream()
                .filter(x -> x.getClientId() == clientId)
                .collect(Collectors.toList());

        //get breakdowns calculation
        for (VPNNetworkLink vpnNetworkLink : links) {
            // get link incidents
            VPNMonthlyUsageByLink vpnMonthlyUsageByLink = new VPNMonthlyUsageByLink();
            vpnMonthlyUsageByLink.setClientId(vpnNetworkLink.getClientId());
            vpnMonthlyUsageByLink.setLinkId(vpnNetworkLink.getHistoryId());
            vpnMonthlyUsageByLink.setLinkDistance(vpnNetworkLink.getLinkDistance());
            vpnMonthlyUsageByLink.setLinkName(vpnNetworkLink.getLinkName());
            vpnMonthlyUsageByLink.setCreatedDate(DateUtils.getCurrentTime());
//            vpnMonthlyUsageByLink.setDiscountRate(vpnNetworkLink.getDiscountRate());
            vpnMonthlyUsageByLink.setVatRate(vatRate);

            List<VPNNetworkLink> vpnNetworkLinks = vpnLinkIncidents.stream()
                    .filter(x -> x.getHistoryId() == vpnNetworkLink.getHistoryId())
                    .collect(Collectors.toList());

            vpnMonthlyUsageByLink.setLocalLoopBreakDowns(getLocalLoopBreakDowns(vpnNetworkLinks));
            vpnMonthlyUsageByLink.setLinkBandwidthBreakDowns(getBandwidthBreakDowns(vpnNetworkLinks));

            calculateLocalLoopCost(vpnMonthlyUsageByLink);

            bill.getMonthlyUsageByLinks().add(vpnMonthlyUsageByLink);

            //set json contents
            vpnMonthlyUsageByLink.setConnectionBandwidthBreakDownsContent();
            vpnMonthlyUsageByLink.setLocalLoopBreakDownsContent();
        }

        //calculate BW cost
        calculateBandwidthCost(bill, tableDto);

        //set json contents
        bill.setMbpsBreakDownsContent();
        bill.setBillingRangeBreakDownsContent();

        return bill;
    }

    private void calculateBandwidthCost(VPNMonthlyUsageByClient vpnMonthlyUsageByClient, TableDTO tableDto) throws Exception {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
        int totalDays = DateUtils.getDaysInMonth(firstDay);

        for (VPNMonthlyUsageByLink link : vpnMonthlyUsageByClient.getMonthlyUsageByLinks()) {
            double totalBWCost = 0;

            for (ConnectionBandwidthBreakDown bandWidth : link.getLinkBandwidthBreakDowns()) {
                long fromDate = bandWidth.getFromDate();
                long toDate = bandWidth.getToDate();
                int days = DateUtils.getDiffInDays(fromDate, toDate);

                VPNNetworkLink networkLink = globalService.findByPK(VPNNetworkLink.class, link.getLinkId());
                networkLink.setOffices();
                Office localEndOffice = networkLink.getLocalEndOffice();
                Office remoteEndOffice = networkLink.getRemoteEndOffice();

                long localEndPopId = officeService.getLocalLoopsByOfficeId(localEndOffice.getId()).get(0).getPopId();
                long remoteEndPopId = officeService.getLocalLoopsByOfficeId(remoteEndOffice.getId()).get(0).getPopId();

                double distance = demandNoteAutofillFacade.getPopToPopDistance(localEndPopId, remoteEndPopId);
                double bw = bandWidth.getValue();
                double fullMonthCost = tableDto.getCostByRowValueAndColumnValue(bw, distance);

                double actualCost = ((fullMonthCost * 1.0) * days) / totalDays;
                totalBWCost += actualCost;
            }

            link.setMbpsCost(NumberUtils.formattedValue(totalBWCost));
        }
    }

    private List<ConnectionBandwidthBreakDown> getBandwidthBreakDowns(List<VPNNetworkLink> vpnLinkIncidents) {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        List<ConnectionBandwidthBreakDown> bandWidthBreakDowns = new ArrayList<>();

        if (vpnLinkIncidents.size() == 0)
            return bandWidthBreakDowns;


        Map<Long, ConnectionBandwidthBreakDown> bandWidthBreakDownMap = new HashMap<>();

        for (VPNNetworkLink link : vpnLinkIncidents) {
            if (isLinkClosed(link))
                continue;

            Long key = link.getHistoryId();
            logger.info("connection name : " + link.getLinkName() + " ; key : " + key + "; bandwidth : " + link.getLinkBandwidth());

            ConnectionBandwidthBreakDown bandWidthBreakDown = null;
            if (bandWidthBreakDownMap.containsKey(key)) {
                bandWidthBreakDown = bandWidthBreakDownMap.get(key);

                //if bw value not changed means incident occurrence not related to bw
                if (!isLinkTemporarilyDisconnected(link) &&
                        bandWidthBreakDown.getValue() == link.getLinkBandwidth()) {
                    bandWidthBreakDown.setToDate(link.getActiveTo() > lastDay ? lastDay : link.getActiveTo());
                    continue;
                }
                //if TD
                //or change occurred
                //remove from map and add into lliMonthlyUsageByConnection
                else {
                    bandWidthBreakDowns.add(bandWidthBreakDown);
                    bandWidthBreakDownMap.remove(key);
                }
            }

            bandWidthBreakDown = new ConnectionBandwidthBreakDown();

            //bandWidthBreakDown.setStatus(instance.getStatus());
            bandWidthBreakDown.setValue(link.getLinkBandwidth());
            bandWidthBreakDown.setFromDate(link.getActiveFrom() < firstDay ? firstDay : link.getActiveFrom());
            bandWidthBreakDown.setToDate(link.getActiveTo() > lastDay ? lastDay : link.getActiveTo());

            //if new incident
            if (link.getActiveFrom() >= firstDay) {
//                bandWidthBreakDown.setRemark(getIncidentInString(link.getIncident()));
            }

            bandWidthBreakDownMap.putIfAbsent(key, bandWidthBreakDown);
        }

        bandWidthBreakDowns.addAll(bandWidthBreakDownMap.values());

        return bandWidthBreakDowns;
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void calculateLocalLoopCost(VPNMonthlyUsageByLink vpnMonthlyUsageByLink) throws Exception {

        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();
        int totalDays = DateUtils.getDaysInMonth(firstDay);

        double totalLoopCost = 0;
        for (VPNLocalLoopBreakDown loop : vpnMonthlyUsageByLink.getLocalLoopBreakDowns()) {
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
                long districtID = loop.getDisctrictId();
                Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs
                        .stream()
                        .filter(x -> x.getDistrictID() == districtID)
                        .findFirst();

                loopCost += loopLength * (ofcInstallationCostByDistrictID.isPresent() ?
                        ofcInstallationCostByDistrictID.get().getInstallationCost() * days / totalDays    //for those days
                        : 0);
            }
            //calculation using OFC type
            loopCost *= loop.getFiberCableType();

            loop.setCost(loopCost);

            VPNNetworkLink link = globalService.findByPK(VPNNetworkLink.class, vpnMonthlyUsageByLink.getLinkId());

            if (loop.getOfficeId() == link.getRemoteOfficeId()) {
                vpnMonthlyUsageByLink.setRemoteEndLoopCost(loopCost);

            } else if (loop.getOfficeId() == link.getLocalOfficeId()) {
                vpnMonthlyUsageByLink.setLocalEndLoopCost(loopCost);
            }
            totalLoopCost += loopCost;
        }
        vpnMonthlyUsageByLink.setTotalLoopCost(NumberUtils.formattedValue(totalLoopCost));
    }

    private List<VPNLocalLoopBreakDown> getLocalLoopBreakDowns(List<VPNNetworkLink> vpnNetworkLinks)
            throws Exception {
        long firstDay = DateUtils.getFirstDayOfMonth(-1).getTime();
        long lastDay = DateUtils.getLastDayOfMonth(-1).getTime();

        List<VPNLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();

        if (vpnNetworkLinks.size() == 0)
            return localLoopBreakDowns;

        Map<Long, VPNLocalLoopBreakDown> localLoopBreakDownMap = new HashMap<>();

        for (VPNNetworkLink link : vpnNetworkLinks) {
            if (isLinkClosed(link))
                //no local loop bill for closed link
                continue;

            List<LocalLoop> loops = vpnNetworkLinkService.getBillApplicableLocalLoopsByLinkId(link.getHistoryId());
            for (LocalLoop loop : loops) {

                Long key = loop.getId();
                long calculatableDistance = loop.getBtclDistance() + loop.getOcDistance();

                logger.info("connection name : " + link.getLinkName() + " ; key : " + key + "; loop : " + calculatableDistance);

                VPNLocalLoopBreakDown vpnLocalLoopBreakDown = null;

                if (localLoopBreakDownMap.containsKey(key)) {
                    vpnLocalLoopBreakDown = localLoopBreakDownMap.get(key);

                    if (vpnLocalLoopBreakDown.getValue() == calculatableDistance &&
                            vpnLocalLoopBreakDown.getFiberCableType() == loop.getOfcType()) {

                        vpnLocalLoopBreakDown.setToDate(link.getActiveTo() > lastDay ? lastDay : link.getActiveTo());
                        continue;
                    }
                    //change occurred
                    //remove from map and add into localLoopBreakDowns
                    else {
                        localLoopBreakDowns.add(vpnLocalLoopBreakDown);
                        localLoopBreakDownMap.remove(key);
                    }
                }
                vpnLocalLoopBreakDown = new VPNLocalLoopBreakDown();

                vpnLocalLoopBreakDown.setFromDate(link.getActiveFrom() < firstDay ? firstDay : link.getActiveFrom());
                vpnLocalLoopBreakDown.setToDate(link.getActiveTo() > lastDay ? lastDay : link.getActiveTo());
                vpnLocalLoopBreakDown.setId(loop.getId());
                vpnLocalLoopBreakDown.setOfficeId(loop.getOfficeId());
                vpnLocalLoopBreakDown.setLinkId(link.getHistoryId());
                vpnLocalLoopBreakDown.setPortID(loop.getPortId());
                vpnLocalLoopBreakDown.setVendorId(loop.getVendorId());
                vpnLocalLoopBreakDown.setDisctrictId(loop.getDistrictId());

                vpnLocalLoopBreakDown.setFiberCableType(loop.getOfcType());
                //set the adjusted distance as the actual distance
                vpnLocalLoopBreakDown.setValue(calculatableDistance + loop.getAdjustedBTCLDistance() + loop.getAdjustedOCDistance());
                vpnLocalLoopBreakDown.setBtclLength(loop.getBtclDistance());
                vpnLocalLoopBreakDown.setVendorLength(loop.getOcDistance());

                localLoopBreakDownMap.putIfAbsent(key, vpnLocalLoopBreakDown);
            }
        }
        localLoopBreakDowns.addAll(localLoopBreakDownMap.values());

        return localLoopBreakDowns;
    }

    //TODO- change the method according to actual status of the link
    public boolean isLinkClosed(VPNNetworkLink link) {
        return VPNConstants.Status.getStatusByName(link.getLinkStatus()) == VPNConstants.Status.VPN_CLOSE;
    }

    public boolean isLinkTemporarilyDisconnected(VPNNetworkLink link) {
        return VPNConstants.Status.getStatusByName(link.getLinkStatus()) == VPNConstants.Status.VPN_TD;
    }

    public boolean isLinkActive(VPNNetworkLink link) {
        return VPNConstants.Status.getStatusByName(link.getLinkStatus()) == VPNConstants.Status.VPN_ACTIVE;
    }


    public List<VPNMonthlyUsageByClient> generateMonthlyUsage() {

        List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients = generateMonthlyUsage(vpnClients);
        return vpnMonthlyUsageByClients;
    }
}
