package vpn.monthlyBill;

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
import org.apache.log4j.Logger;
import requestMapping.Service;
import user.UserDTO;
import user.UserService;
import util.DateUtils;
import util.NumberUtils;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.VPNClientService;
import vpn.VPNOTC;
import vpn.client.ClientDetailsDTO;
import vpn.monthlyUsage.VPNMonthlyUsageByClientService;
import vpn.monthlyUsage.VPNMonthlyUsageByLinkService;
import vpn.monthlyUsage.VPNMonthlyUsageGenerator;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VPNMonthlyBillGenerator {

    static Logger logger = Logger.getLogger(VPNMonthlyBillGenerator.class);


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
    VPNMonthlyUsageGenerator vpnMonthlyUsageGenerator;
    @Service
    VPNMonthlyBillByClientService vpnMonthlyBillByClientService;
    @Service
    VPNMonthlyBillByLinkService vpnMonthlyBillByLinkService;

    List<UserDTO> vendors = new ArrayList<>();
    List<ClientDetailsDTO> vpnClients = new ArrayList<>();
    List<DistrictOfcInstallationDTO> districtOfcInstallationDTOs = new ArrayList<>();
//    Map<Long, LLIMonthlyOutsourceBill> lliOutsourceBillMap = new HashMap<>();

    double registrationCharge = 0;
    double vatRate = 0;
    int localLoopMinimumCost = 0;
    int localLoopDistanceCheckpoint = 0;


    List<VPNNetworkLink> vpnLinks = new ArrayList<>();


    public List<VPNMonthlyBillByClient> generateMonthlyBill(List<ClientDetailsDTO> clients) {
        loadData();

        List<VPNMonthlyBillByClient> vpnMonthlyBillByClients = new ArrayList<>();
        for (ClientDetailsDTO client : clients) {
            try {
                VPNMonthlyBillByClient bill = populateMonthlyBill(client.getClientID());

                if (bill.getMonthlyBillByLinks().isEmpty()) {
                    continue;
                }
                calculateBill(bill, vatRate);
                save(bill);

                //calculateTDDate(bill);
                //insertIntoAccounting(bill);

                vpnMonthlyBillByClients.add(bill);
            } catch (Exception e) {
                logger.fatal("failed to store in db LLIMonthlyBillByClient for " + client.getClientID());
            }
        }

        clearData();

        return vpnMonthlyBillByClients;
    }


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void save(VPNMonthlyBillByClient vpnMonthlyBillByClient) throws Exception {
        vpnMonthlyBillByClientService.save(vpnMonthlyBillByClient);
        if (vpnMonthlyBillByClient.getId() == null)
            return;

        for (VPNMonthlyBillByLink link : vpnMonthlyBillByClient.getMonthlyBillByLinks()) {
            link.setMonthlyBillByClientId(vpnMonthlyBillByClient.getId());
            vpnMonthlyBillByLinkService.save(link);
        }
    }


    private void calculateBill(VPNMonthlyBillByClient bill, double vatRate) throws Exception {
        double grandTotal = 0;
        double totalDiscount = 0;

        for (VPNMonthlyBillByLink billByLink : bill.getMonthlyBillByLinks()) {

            billByLink.setDiscount(NumberUtils.formattedValue(billByLink.getMbpsCost() * billByLink.getDiscountRate() / 100));

            //here grandcost is after discount
            billByLink.setGrandCost(NumberUtils.formattedValue(billByLink.getTotalLoopCost() + billByLink.getMbpsCost() - billByLink.getDiscount()));

            billByLink.setVat(NumberUtils.formattedValue(billByLink.getGrandCost() * billByLink.getVatRate() / 100));
            billByLink.setTotalCost(NumberUtils.formattedValue(billByLink.getGrandCost() + billByLink.getVat()));

            grandTotal += billByLink.getGrandCost();
            totalDiscount += billByLink.getDiscount();
        }

        totalDiscount = totalDiscount;
        bill.setGrandTotal(NumberUtils.formattedValue(grandTotal));

        bill.setDiscountPercentage(0);
        bill.setDiscount(NumberUtils.formattedValue(totalDiscount));

        bill.setTotalPayable(NumberUtils.formattedValue(bill.getGrandTotal()));

        bill.setVatPercentage(vatRate);
        bill.setVAT(NumberUtils.formattedValue(bill.getTotalPayable() * bill.getVatPercentage() / 100));


        bill.setNetPayable(NumberUtils.formattedValue(bill.getTotalPayable() + bill.getVAT()));
    }


    private VPNMonthlyBillByClient populateMonthlyBill(long clientId) throws Exception {
        Date date = DateUtils.getFirstDayOfMonth(DateUtils.getCurrentDateTime());
        double totalMbps = 0;

        VPNMonthlyBillByClient bill = new VPNMonthlyBillByClient();
        bill.setClientId(clientId);

        List<VPNNetworkLink> vpnNetworkLinks = vpnLinks.stream()
                .filter(x -> x.getClientId() == clientId)
                .collect(Collectors.toList());

        for (VPNNetworkLink link : vpnNetworkLinks) {
            VPNMonthlyBillByLink vpnMonthlyBillByLink = new VPNMonthlyBillByLink();

            vpnMonthlyBillByLink.setClientId(link.getClientId());
            vpnMonthlyBillByLink.setLinkId(link.getId());
            vpnMonthlyBillByLink.setLinkDistance(link.getLinkDistance());
            vpnMonthlyBillByLink.setLinkStatus(link.getLinkStatus());
            vpnMonthlyBillByLink.setLinkName(link.getLinkName());
            vpnMonthlyBillByLink.setCreatedDate(date.getTime());

//            vpnMonthlyBillByLink.setLinkType(link.get);

            vpnMonthlyBillByLink.setLinkBandwidth(link.getLinkBandwidth());
//            vpnMonthlyBillByLink.setDiscountRate();
            vpnMonthlyBillByLink.setVatRate(vatRate);

            logger.info("mbps : " + link.getLinkBandwidth() + ";");

            if (vpnMonthlyUsageGenerator.isLinkClosed(link)) {
                continue;
            }

            vpnMonthlyBillByLink.setLocalLoopBreakDowns(getLocalLoopBreakDown(vpnMonthlyBillByLink,
                    localLoopDistanceCheckpoint, localLoopMinimumCost));
            totalMbps += link.getLinkBandwidth();

            double loopCost = vpnMonthlyBillByLink.getLocalLoopBreakDowns()
                    .stream()
                    .mapToDouble(loop -> loop.getCost())
                    .sum();

            vpnMonthlyBillByLink.setTotalLoopCost(loopCost);
            bill.getMonthlyBillByLinks().add(vpnMonthlyBillByLink);
        }

        TableDTO tableDTO = getTableDTO(clientId);
        if (tableDTO == null)
            return bill;

        CalculateBWCost(bill, tableDTO, totalMbps);

        bill.setCreatedDate(date.getTime());

        bill.setDeleted(false);
        bill.setMonth(DateUtils.getMonthFromDate(date.getTime()));
        bill.setYear(DateUtils.getYearFromDate(date.getTime()));

        return bill;
    }

    private void CalculateBWCost(VPNMonthlyBillByClient bill, TableDTO tableDTO, double totalMbps) throws Exception {
        calculateBandwidthCost(bill, tableDTO);
    }

    private void calculateBandwidthCost(VPNMonthlyBillByClient bill, TableDTO tableDTO) throws Exception {
        for (VPNMonthlyBillByLink billByLink : bill.getMonthlyBillByLinks()) {

            double bw = billByLink.getLinkBandwidth();

            VPNNetworkLink networkLink = globalService.findByPK(VPNNetworkLink.class, billByLink.getLinkId());

            networkLink.setOffices();
            Office localEndOffice = networkLink.getLocalEndOffice();
            Office remoteEndOffice = networkLink.getRemoteEndOffice();

            long localEndPopId = officeService.getLocalLoopsByOfficeId(localEndOffice.getId()).get(0).getPopId();
            long remoteEndPopId = officeService.getLocalLoopsByOfficeId(remoteEndOffice.getId()).get(0).getPopId();

            double distance = demandNoteAutofillFacade.getPopToPopDistance(localEndPopId, remoteEndPopId);
            double fullMonthCost = tableDTO.getCostByRowValueAndColumnValue(bw, distance);

            if (vpnMonthlyUsageGenerator.isLinkTemporarilyDisconnected(networkLink)) {
                //handle TD here
                billByLink.setMbpsCost(0);
            } else {
                billByLink.setMbpsCost(fullMonthCost);
            }

            billByLink.setLocalLoopBreakDownsContent();
        }
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

    @Transactional(transactionType = util.TransactionType.READONLY)
    private List<VPNLocalLoopBreakDown> getLocalLoopBreakDown(VPNMonthlyBillByLink billByLink, double initialLoopLength, double initialLoopCost) throws Exception {
        List<VPNLocalLoopBreakDown> localLoopBreakDowns = new ArrayList<>();

        List<LocalLoop> localLoops = vpnNetworkLinkService.getBillApplicableLocalLoopsByLinkId(billByLink.getLinkId());

        for (LocalLoop loop : localLoops) {
            double loopLength =
                    loop.getOcDistance()
                            + loop.getBtclDistance()
                            + loop.getAdjustedBTCLDistance()
                            + loop.getAdjustedOCDistance();

            if (loopLength == 0)
                continue;

            VPNLocalLoopBreakDown localLoopBreakDown = new VPNLocalLoopBreakDown();
            localLoopBreakDown.setLinkId(billByLink.getLinkId());
            localLoopBreakDown.setFiberCableType(loop.getOfcType());
            localLoopBreakDown.setId(loop.getId());
//                localLoopBreakDown.setOfficeId(loop.getLliOfficeHistoryID());
            localLoopBreakDown.setPortID(loop.getPortId());
            localLoopBreakDown.setValue(loopLength);

            //initial flat rate
            long districtID = loop.getDistrictId();

            Optional<DistrictOfcInstallationDTO> ofcInstallationCostByDistrictID = districtOfcInstallationDTOs.stream().filter(x -> x.getDistrictID() == districtID).findFirst();
            localLoopBreakDown.setRate(ofcInstallationCostByDistrictID.isPresent() ? ofcInstallationCostByDistrictID.get().getInstallationCost() : 0);

            double loopCost = initialLoopCost;
            loopLength -= initialLoopLength;
            if (loopLength > 0) {
                loopCost += loopLength * localLoopBreakDown.getRate();
            }

            //calculation using OFC type
            loopCost *= loop.getOfcType();
            //loopCostOfConnection += loopCost;
            localLoopBreakDown.setCost(loopCost);

            localLoopBreakDowns.add(localLoopBreakDown);

            VPNNetworkLink link = globalService.findByPK(VPNNetworkLink.class, billByLink.getLinkId());

            if (loop.getOfficeId() == link.getLocalOfficeId()) {
                billByLink.setLocalEndLoopCost(loopCost);
            } else if (loop.getOfficeId() == link.getRemoteOfficeId()) {
                billByLink.setRemoteEndLoopCost(loopCost);
            }
        }
        return localLoopBreakDowns;
    }

    private void clearData() {
        vendors.clear();
        vpnClients.clear();
        vpnLinks.clear();
        districtOfcInstallationDTOs.clear();
//        lliOutsourceBillMap.clear();
        vpnLinks.clear();
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

        getAllClient();
        getAllLinks();
        getAllDistrictOfcInstallationCost();
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void getAllDistrictOfcInstallationCost() {
        try {
            districtOfcInstallationDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();//taking latest. cause no way to fetch old data
        } catch (Exception e) {
            logger.fatal("error in getAllDistrictOfcInstallationCost");
        }
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    private void getAllLinks() {
        long lastDateLastHourOfLastMonth = DateUtils.getLastDayOfMonth(-1).getTime();
        long lastDateFirstHourOfLastMonth = DateUtils.getNthDayFirstHourFromDate(lastDateLastHourOfLastMonth, 0);

        try {

            vpnLinks = vpnNetworkLinkService.getVPNLinksByDateRange(lastDateFirstHourOfLastMonth, lastDateLastHourOfLastMonth)
                    .stream()
                    .filter(x -> x.getActiveFrom() <= lastDateLastHourOfLastMonth)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.fatal("error in getAllConnection");
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


    public List<VPNMonthlyBillByClient> generateMonthlyBill() throws Exception {
        List<VPNMonthlyBillByClient> lliMonthlyBillByClients = generateMonthlyBill(vpnClients);
        return lliMonthlyBillByClients;
    }

}
