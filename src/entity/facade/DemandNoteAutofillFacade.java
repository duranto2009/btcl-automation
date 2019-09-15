package entity.facade;

import annotation.Transactional;
import application.ApplicationType;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import client.classification.ClientClassificationService;
import client.classification.RegistrantCategoryDTO;
import client.classification.RegistrantTypeDTO;
import common.*;
import common.repository.AllClientRepository;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import distance.DistanceService;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConsumerMap;
import entity.localloop.LocalLoopConsumerMapConditionBuilder;
import entity.office.Office;
import exception.NoDataFoundException;
import global.GlobalService;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.LLILocalLoop;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import lombok.extern.log4j.Log4j;
import nix.application.localloop.NIXApplicationLocalLoop;
import requestMapping.Service;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.VPNConstants;
import vpn.VPNOTC;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationService;
import vpn.client.ClientDetailsDTO;
import vpn.demandNote.VPNDemandNote;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;
import vpn.ofcinstallation.DistrictOfcInstallationService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class DemandNoteAutofillFacade {

    @Service private VPNApplicationService vpnApplicationService;
    @Service private ClientClassificationService clientClassificationService;
    @Service private GlobalService globalService;

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNDemandNote autofillVPNDemandNote(long id, boolean isGlobal, ApplicationGroupType appGroupType) throws Exception {
        VPNApplication vpnApplication = vpnApplicationService.getAppropriateVPNApplication(id, isGlobal);

        if(vpnApplication.getVpnApplicationLinks().isEmpty()){
            // jiboneo eikhane asha uchit na.
            throw new RequestFailureException("No Link Information Found");
        }
        return autofillVPNLinkDemandNote(vpnApplication, appGroupType);
    }

    private VPNDemandNote autofillVPNLinkDemandNote(VPNApplication vpnApplication, ApplicationGroupType appGroupType) throws Exception {
        VPNDemandNote demandNote = new VPNDemandNote();
        String description = "Demand Note for Virtual Private Network(VPN) with application : " + vpnApplication.getApplicationId();
        if(vpnApplication.getVpnApplicationLinks().size() == 1){
            description += " and link id " + vpnApplication.getVpnApplicationLinks().get(0).getId();
        }
        demandNote.setDescription(description);
        demandNote.setApplicationGroupType(appGroupType);
        demandNote.setClientID(vpnApplication.getClientId());

        if(appGroupType == ApplicationGroupType.VPN_LINK_CLIENT_APPLICATION) {
            calculateOwnershipChangeDemandNoteEssentialCharges(demandNote, vpnApplication.getVpnApplicationLinks());
            return demandNote;
        }else if(appGroupType == ApplicationGroupType.VPN_CLIENT_APPLICATION) {
            calculateReconnectDemandNoteEssentialCharges(demandNote, vpnApplication.getVpnApplicationLinks());
            return demandNote;
        }
        else {
            calculateEssentialCharges(demandNote, vpnApplication);
        }
        return demandNote;
    }

    private  void calculateEssentialCharges(VPNDemandNote demandNote, VPNApplication vpnApplication) throws Exception {

        List<VPNApplicationLink> links = vpnApplication.getVpnApplicationLinks();
        VPNOTC vpnOTC = getVPNOTC();
        demandNote.setVatPercentage(vpnOTC.getVatPercentage());
        if(vpnApplication.getApplicationType() == ApplicationType.VPN_NEW_CONNECTION){
            calculateRegistrationCharge(demandNote, links, vpnOTC);
            calculateLocalLoopCharge(demandNote, links);
        }
        if(vpnApplication.getApplicationType()== ApplicationType.VPN_NEW_CONNECTION
                || vpnApplication.getApplicationType() == ApplicationType.VPN_UPGRADE_CONNECTION
        ){
            calculateBWAndSecurityCharge(demandNote, links, vpnOTC, vpnApplication.getLayerType());
        }
        if(vpnApplication.getApplicationType() == ApplicationType.VPN_SHIFT_LINK) {
            if(vpnApplication.getVpnApplicationLinks().size()>1){
                throw new RequestFailureException("Multiple Link Shifting operation not allowed");
            }
            VPNApplicationLink vpnApplicationLink = vpnApplication.getVpnApplicationLinks().get(0);
            VPNNetworkLink vpnNetworkLink =globalService
                        .findByPK(VPNNetworkLink.class, vpnApplicationLink.getNetworkLinkId());

            boolean isBandwidthChanged = (vpnNetworkLink.getLinkBandwidth() != vpnApplicationLink.getLinkBandwidth());
            // TODO needs clarification.
            boolean isAddressChanged = (vpnNetworkLink.getLocalOfficeId() != vpnApplicationLink.getLocalOfficeId())
                                        || (vpnNetworkLink.getRemoteOfficeId() != vpnApplicationLink.getRemoteOfficeId());

            LocalLoop localEnd = vpnApplicationLink.getLocalOffice().getLocalLoops().get(0);
            LocalLoop remoteEnd = vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0);
            boolean isChargeApplicableForLocalEnd = !(localEnd.isCompleted() || localEnd.getLoopProvider()==VPNConstants.LOOP_CLIENT);
            boolean isChargeApplicableForRemoteEnd = !(remoteEnd.isCompleted() || remoteEnd.getLoopProvider() == VPNConstants.LOOP_CLIENT);

            demandNote.setShiftingCharge(isBandwidthChanged ? demandNote.getShiftingCharge() + vpnOTC.getBwShiftingOTC() : demandNote.getShiftingCharge());
            demandNote.setShiftingCharge(isAddressChanged ? demandNote.getShiftingCharge() + vpnOTC.getAddressShiftingOTC() : demandNote.getShiftingCharge());
            demandNote.setShiftingCharge(isChargeApplicableForLocalEnd ? demandNote.getShiftingCharge() + vpnOTC.getLocalLoopShiftingOTC() : demandNote.getShiftingCharge());
            demandNote.setShiftingCharge(isChargeApplicableForRemoteEnd ? demandNote.getShiftingCharge() + vpnOTC.getLocalLoopShiftingOTC() : demandNote.getShiftingCharge());



        }
    }

    // for calculating local loop charge pdf view
    public double calculateLocalLoopCost(LocalLoop localLoop, boolean isCompleted, long networkLinkId) throws Exception {

        if(networkLinkId == 0) {
            // connection is not created yet
            return calculateLocalLoopCost(localLoop, isCompleted);
        }else {
            List<LocalLoopConsumerMap> consumers = globalService.getAllObjectListByCondition(
                    LocalLoopConsumerMap.class, new LocalLoopConsumerMapConditionBuilder()
                    .Where()
                    .consumerIdEquals(networkLinkId)
                    .isActive(true)
                    .localLoopIdEquals(localLoop.getId())
                    .getCondition()
            );
            if(!consumers.isEmpty()) {
                if (consumers.get(0).isBillApplicable()) {
                    return calculateLocalLoopCost(localLoop.getDistrictId(),
                            localLoop.getBtclDistance() + localLoop.getOcDistance(),
                            localLoop.getOfcType()
                    );
                } else {
                    return 0;
                }
            }

            return 0;

        }
    }

    // for calculating local loop charge web view.
    public double calculateLocalLoopCost ( LocalLoop localLoop , boolean isCompleted) {

        if(isCompleted){

            log.info("This loop is reused. Abort Calculation!!!!!!!!!!!");
            return 0.0;
        }
        double cost = calculateLocalLoopCost(localLoop.getDistrictId(),
                localLoop.getBtclDistance() + localLoop.getOcDistance(),
                localLoop.getOfcType()
        );
        log.info("local-loop cost: " + cost);
        return cost;
    }

    public double calculateRemoteEndLoopCost (List<VPNApplicationLink> links) {

        return links.stream()
                .map(t->new KeyValuePair<>(t.getRemoteOffice().getLocalLoops().get(0), t.getNetworkLinkId()))
                .mapToDouble(t-> {
                    try {
                        return calculateLocalLoopCost(t.getKey(), t.getKey().isCompleted(), t.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                })
                .sum();
    }

    public double calculateLocalLoopCostFromLLIApplicationLocalLoop (List<NewLocalLoop> localLoops) {
        return localLoops.stream()
                .mapToDouble(t-> {
                    try {
                        return calculateLocalLoopCost(t);
                    } catch (Exception e) {
                        log.fatal("invalid cost for lli application local loop id: " + t.getId());
                        return 0.0;
                    }
                })
                .sum();
    }

    private double calculateLocalLoopCost(NewLocalLoop localLoop) throws Exception {
        long districtId = ServiceDAOFactory.getService(InventoryService.class).getDistrictOfInventoryItem(localLoop.getPopID());
        double cost = calculateLocalLoopCost(districtId,
                localLoop.getBTCLDistances() + localLoop.getOCDistances(),
                (int)localLoop.getOfcType()
        );
        log.info("local-loop cost: " + cost);
        return cost;
    }

    public static double calculateLocalLoopCost(NIXApplicationLocalLoop nixApplicationLocalLoop) throws Exception {
        InventoryItem district = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemDetailsFromRootByItemID(nixApplicationLocalLoop.getPopId()).get(0);
        return calculateLocalLoopCost(
                district.getID(),
                nixApplicationLocalLoop.getBtclDistance() + nixApplicationLocalLoop.getOcdDistance(),
                nixApplicationLocalLoop.getOfcType()
        );
    }

    public static long getDistrictIdByPopId(long popId) throws Exception {
        InventoryItem district = ServiceDAOFactory.getService(InventoryService.class)
                .getInventoryItemDetailsFromRootByItemID(popId).get(0);
        return district.getID();
    }


    public static double calculateLocalLoopCost(lli.Application.LocalLoop.LocalLoop lliLocalLoop) throws Exception {
        long districtId = getDistrictIdByPopId(lliLocalLoop.getPopID());
        return calculateLocalLoopCost(
                districtId,
                lliLocalLoop.getBTCLDistances() + lliLocalLoop.getOCDistances(),
                (int)lliLocalLoop.getOfcType()
        );
    }



    private void calculateReconnectDemandNoteEssentialCharges(VPNDemandNote demandNote, List<VPNApplicationLink> links) throws Exception {
        VPNOTC vpnotc = getVPNOTC();
        demandNote.setVatPercentage(vpnotc.getVatPercentage());
        if(links.isEmpty()){
            throw new NoDataFoundException("No Application Link Information found in VPN Ownership change Application");
        }
        double reconnectCharge = links.size() * vpnotc.getReconnectChargePerLink();
        demandNote.setReconnectCharge(reconnectCharge);
    }

    private void calculateOwnershipChangeDemandNoteEssentialCharges(VPNDemandNote demandNote, List<VPNApplicationLink> links) throws Exception {
        VPNOTC vpnotc = getVPNOTC();
        demandNote.setVatPercentage(vpnotc.getVatPercentage());
        if(links.isEmpty()){
            throw new NoDataFoundException("No Application Link Information found in VPN Ownership change Application");
        }
        double estimatedOwnershipChangeCharge = links.size() * vpnotc.getMinimumOwnershipChangeCharge();
        demandNote.setOwnershipChangeCharge(
                Math.min(
                        estimatedOwnershipChangeCharge,
                        vpnotc.getMaximumOwnershipChangeCharge()
                )
        );
    }


    public KeyValuePair<Double, Double> calculateBWCostOfALinkForVPN (VPNApplicationLink link,
                                                                      TableDTO costChart,
                                                                      double discountPercentage,
                                                                      VPNOTC vpnotc,
                                                                      int layerType) {
        long localEndPopId = link.getLocalOffice().getLocalLoops().get(0).getPopId(); // TODO ; null check needed
        long remoteEndPopId = link.getRemoteOffice().getLocalLoops().get(0).getPopId(); // TODO; null check needed

        try {
            double distance = getPopToPopDistance(localEndPopId, remoteEndPopId);
            double bw = link.getLinkBandwidth();
            log.info("distance: " + distance + " bw: " + bw);
            // TODO go Refactor the getCostByRowValueAndColumnValue method; it is poorly written; add functional nature
            // TODO so that other developer may understand what is going on under the hood.


            double cost = costChart.getCostByRowValueAndColumnValue(bw, distance);

            log.info("Layer Type : " + layerType);
            if(layerType == 3) {
                double extraCost = cost * vpnotc.getLayer3Percentage() / 100.;
                log.info("Extra Cost For Layer 3: " + extraCost);
                cost = cost + extraCost;
            }
            double discount = Math.ceil(cost * discountPercentage / 100.); // TODO raihan; NEED a decision; round or ceil.
            log.info("Cost: " + cost + " discount: " + discount);

            return new KeyValuePair<>(cost, discount);

        } catch (Exception e) {
            throw new RequestFailureException("Error Calculating BW cost for VPN Link id, " + link.getId());
        }
    }

    public double getPopToPopDistance(long localEndPopId, long remoteEndPopId) throws Exception {
        // super null check needed ;
        // TODO raihan
        List<InventoryItem> localEndInventoryListFromRoot = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemDetailsFromRootByItemID(localEndPopId);
        List<InventoryItem> remoteEndInventoryListFromRoot = ServiceDAOFactory.getService(InventoryService.class).getInventoryItemDetailsFromRootByItemID(remoteEndPopId);

        return ServiceDAOFactory.getService(DistanceService.class)
                .getDistanceBetweenTwoDistricts(localEndInventoryListFromRoot.get(0).getID(), remoteEndInventoryListFromRoot.get(0).getID())
                .getDistance();

        //map.forEach(t-> System.out.println(t));(localEndPopId, remoteEndPopId);
    }

    public static OfcInstallationCostDTO getOfcInstallationCost() {
        return ServiceDAOFactory.getService(OfcInstallationCostService.class)
                .getLatestByDate(System.currentTimeMillis());
    }
    public static double calculateLocalLoopCost(long districtId, double distance, int numberOfCore) {

        OfcInstallationCostDTO ofcInstallationCostDTO = getOfcInstallationCost();
        long minimumLength = ofcInstallationCostDTO.getFiberLength();
        double minimumLengthCost = ofcInstallationCostDTO.getFiberCost();
        double factor = getFactorForLocalLoopCostByDistrictId(districtId);
        log.info("Total Distance: " + distance);
        log.info("District OFC installation factor: " + factor);
        log.info("Number of core: " + numberOfCore);
        return calculateLocalLoopCost(distance, minimumLength, minimumLengthCost, numberOfCore, factor);
    }

    public static double getFactorForLocalLoopCostByDistrictId(long districtId) {
        return  ServiceDAOFactory.getService(DistrictOfcInstallationService.class)
                .getOfcInstallationCostByDistrictID(districtId);
    }

    public static double calculateLocalLoopCost ( double localLoopDistance,
                                           double minimumLength,
                                           double minimumLengthCost,
                                           int numberOfCore,
                                           double factor ) {

        if (localLoopDistance <= minimumLength) {
            return minimumLengthCost * numberOfCore;
        } else {
            double restOfLength = localLoopDistance - minimumLength;
            double restOfLengthCost = (factor * restOfLength);
            return (restOfLengthCost + minimumLengthCost) * numberOfCore;
        }
    }



    //TODO make it private after test
    private VPNOTC getVPNOTC() throws Exception {
        return ServiceDAOFactory.getService(UniversalDTOService.class).get(VPNOTC.class);
    }

    private void calculateBWAndSecurityCharge(VPNDemandNote demandNote, List<VPNApplicationLink> links, VPNOTC vpnotc, int layerType) throws Exception {

        TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class)
                .getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, 1);
        RegistrantCategoryDTO registrantCategory = clientClassificationService.getRegistrantCategoryByClientAndModuleId(demandNote.getClientID(), ModuleConstants.Module_ID_VPN);

        double discountRate = registrantCategory.getDiscount();
        log.info("discount rate: " + discountRate);


        demandNote.setDiscountPercentage(discountRate);

        List<KeyValuePair<Double, Double>> bwCostAndDiscountList = links.stream()
                .map(t->calculateBWCostOfALinkForVPN(t, tableDTO, discountRate, vpnotc, layerType))
                .collect(Collectors.toList());

        KeyValuePair<Double, Double> accumulatedBWCostAndDiscount = bwCostAndDiscountList.stream()
                .reduce(
                        new KeyValuePair<>(0.0, 0.0),
                        (acc, next)-> new KeyValuePair<>(acc.getKey() + next.getKey(), acc.getValue() + next.getValue())
                );
        double totalBWCost = accumulatedBWCostAndDiscount.getKey();
        double totalDiscount = accumulatedBWCostAndDiscount.getValue();
        log.info("total bw cost: " + totalBWCost);
        log.info("total discount: " + totalDiscount);

        log.info("before discount bw cost: " + totalBWCost);

        demandNote.setDiscount(totalDiscount); // both security & bw
        double bwCostWithDiscount =  totalBWCost - totalDiscount;
        log.info("after discount applied bw cost: " + bwCostWithDiscount);

        demandNote.setBandwidthCharge(totalBWCost);

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(demandNote.getClientID(), ModuleConstants.Module_ID_VPN);
        RegistrantTypeDTO registrantType = ServiceDAOFactory.getService(ClientClassificationService.class).getClientRegistrantTypeByRegistrantTypeId(clientDetailsDTO.getRegistrantType());
        if(registrantType.getRegTypeId() != RegistrantTypeConstants.GOVT){
            demandNote.setSecurityCharge(totalBWCost);
        }
    }

    private void calculateRegistrationCharge(VPNDemandNote demandNote, List<VPNApplicationLink> links, VPNOTC vpnOTC) {
        if(links.size()>vpnOTC.getRegistrationChargeSpecialCaseLinkCount()) {
            demandNote.setRegistrationCharge(links.size() * vpnOTC.getRegistrationChargePerLinkSpecialCase());
        }else {
            demandNote.setRegistrationCharge(links.size() * vpnOTC.getRegistrationChargePerLink());
        }
        demandNote.setOtcLocalLoopBTCL(
                getCountForLoopProviderBTCL(links) * vpnOTC.getRegistrationChargePerLink()
        );
    }

    private long getCountForLoopProviderBTCL(List<VPNApplicationLink> links) {

        long count =  links.stream()
                .map(link->
                        link.getRemoteOffice().getLocalLoops().get(0)
                )
                .filter(localLoop -> localLoop.getLoopProvider() == 1)
                .count();
        log.info("Total Remote End BTCL Provided Loop Count " + count);
        count += links.get(0).getLocalOffice().getLocalLoops().get(0).getLoopProvider() == 1 ? 1 : 0;
        log.info("Total BTCL Provided Loop Count " + count);
        return count ;
    }



    public void calculateLocalLoopCharge(VPNDemandNote demandNote, List<VPNApplicationLink> links) throws Exception {

        List<LocalLoop> remoteEnds = links.stream()
                .map(VPNApplicationLink::getRemoteOffice)
                .map(Office::getLocalLoops)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // consider remote end if set then for all it either takes consideration; or rejects; TODO it will vary individually
        log.info("Calculating All Remote Ends Local Loop Cost");
        double remoteLocalLoopCharge = remoteEnds.stream()
                .reduce(0.0,
                        (acc, next)-> acc + calculateLocalLoopCost(next, next.isCompleted()), // serious problem if isCompleted is used.
                        (s1, s2)->s1+s2
                );
        log.info("Total Remote Local-Loop Charge: " + remoteLocalLoopCharge);
        double totalLoopCost = remoteLocalLoopCharge;
        log.info("Calculating Local End Loop Cost");

        VPNApplicationLink firstLink = links.get(0);
        LocalLoop firstLinkLocalEndLoop = firstLink.getLocalOffice().getLocalLoops().get(0);
        if(vpnApplicationService.isLocalEndLoopChargePreviouslyCalculated(firstLink.getVpnApplicationId())) {
            log.info("Local End Loop Cost :: Aborted :: Reason :: Already Calculated and asked in DN");
        }else {
            totalLoopCost += calculateLocalLoopCost(firstLinkLocalEndLoop, firstLinkLocalEndLoop.isCompleted()); // serious problem if isCompleted is used;
        }
        log.info("Total local-loop charge (Considering Remote (all) & Local (one)): "+totalLoopCost);
        demandNote.setLocalLoopCharge(totalLoopCost);
    }


}
