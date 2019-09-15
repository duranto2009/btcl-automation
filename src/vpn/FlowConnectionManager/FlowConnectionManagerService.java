package vpn.FlowConnectionManager;

import annotation.Transactional;
import application.Application;
import application.ApplicationType;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillService;
import entity.comment.CommentService;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConsumerMap;
import entity.localloop.LocalLoopService;
import entity.office.Office;
import global.GlobalService;
import inventory.InventoryAllocationHistoryService;
import ip.IPService;
import login.LoginDTO;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import vpn.VPNConstants;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.demandNote.VPNLoopChargeDiscountEligibility;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlowConnectionManagerService {

    private LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    private GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
    private VPNNetworkLinkService vpnNetworkLinkService = ServiceDAOFactory.getService(VPNNetworkLinkService.class);
    private InventoryAllocationHistoryService inventoryAllocationHistoryService = ServiceDAOFactory.getService(InventoryAllocationHistoryService.class);

    @Service private CommentService commentService;
    @Service private VPNApplicationService vpnApplicationService;


    @Transactional
    public void connectionCreatorOrUpdaterManager(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {
//        vpnApplication= globalService.findByPK(VPNApplication.class,vpnApplication.getVpnApplicationId());
        if (vpnApplication.getApplicationType() == ApplicationType.VPN_NEW_CONNECTION) {
            newLinkInsert(vpnApplication,loginDTO);
        } else if (vpnApplication.getApplicationType() == ApplicationType.VPN_DOWNGRADE_CONNECTION) {
            processBandwidthDowngrade(vpnApplication, loginDTO);
        } else if (vpnApplication.getApplicationType() == ApplicationType.VPN_UPGRADE_CONNECTION) {
            processBandwidthUpgrade(vpnApplication);
        }else if (vpnApplication.getApplicationType() == ApplicationType.VPN_SHIFT_LINK) {
            processLinkShift(vpnApplication);
        }
        //TODO add comment for process bw downgrade & upgrade (Forhad didn't insert anything to comment table for those two steps)!!
//        if(applicationType == NIXConstants.NIX_UPGRADE_APPLICATION||
//                applicationType == NIXConstants.NIX_DOWNGRADE_APPLICATION){
//            upgradeOrDowngraadeConnection(jsonObject,appID);
//
//        }
    }


    private void processLinkShift(VPNApplication vpnApplication) throws Exception {
        if (vpnApplication.getVpnApplicationLinks().isEmpty() || vpnApplication.getVpnApplicationLinks().size() > 1) {
            throw new RequestFailureException("Invalid request");
        }
        VPNApplicationLink vpnApplicationLink = vpnApplication.getVpnApplicationLinks().get(0);
        VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, vpnApplicationLink.getNetworkLinkId());
        final long historyId=vpnNetworkLink.getHistoryId();
        vpnNetworkLinkService.markLatestHistoryObsoleteByNetworkLinkId(vpnNetworkLink);
        VPNNetworkLink networkLink = getDBReadyVPNNetworkLinkWithIncident(vpnApplicationLink, vpnApplication, VPNConstants.INCIDENT.SHIFT);
        networkLink.setLinkName(vpnApplicationLink.getLinkName());
        networkLink.setHistoryId(historyId);
        globalService.save(networkLink);
        saveConsumerMap(vpnApplicationLink.getLocalOffice(),networkLink);
        if(!vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).isCompleted()){
            localLoopInventoryAllocation(vpnApplicationLink.getLocalOffice(),vpnApplication);
        }
        saveConsumerMap(vpnApplicationLink.getRemoteOffice(),networkLink);
        if(!vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).isCompleted()){
            localLoopInventoryAllocation(vpnApplicationLink.getRemoteOffice(),vpnApplication);
        }
        vpnApplicationLink.setServiceStarted(true);
        vpnApplicationLink.setNetworkLinkId(networkLink.getId());
        globalService.update(vpnApplicationLink);
    }

    private void processBandwidthUpgrade(VPNApplication vpnApplication) throws Exception {
        if (vpnApplication.getVpnApplicationLinks().isEmpty() || vpnApplication.getVpnApplicationLinks().size() > 1) {
            throw new RequestFailureException("Invalid request");
        }
        VPNApplicationLink vpnApplicationLink = vpnApplication.getVpnApplicationLinks().get(0);
        VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, vpnApplicationLink.getNetworkLinkId());
        long historyId=vpnNetworkLink.getHistoryId();
        vpnNetworkLinkService.markLatestHistoryObsoleteByNetworkLinkId(vpnNetworkLink);
        VPNNetworkLink networkLink = getDBReadyVPNNetworkLinkWithIncident(vpnApplicationLink, vpnApplication, VPNConstants.INCIDENT.UPGRADE);
        networkLink.setLinkName(vpnApplicationLink.getLocalOffice().getOfficeName() + " => " + vpnApplicationLink.getRemoteOffice().getOfficeName() + " : " + networkLink.getLinkBandwidth() + " Mbps");
        networkLink.setHistoryId(historyId);
        globalService.save(networkLink);
        saveConsumerMap(vpnApplicationLink.getLocalOffice(),networkLink);
        if(!vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).isCompleted()){
            localLoopInventoryAllocation(vpnApplicationLink.getLocalOffice(),vpnApplication);
        }
        saveConsumerMap(vpnApplicationLink.getRemoteOffice(),networkLink);
        if(!vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).isCompleted()){
            localLoopInventoryAllocation(vpnApplicationLink.getRemoteOffice(),vpnApplication);
        }
        vpnApplicationLink.setServiceStarted(true);
        vpnApplicationLink.setNetworkLinkId(networkLink.getId());
        globalService.update(vpnApplicationLink);
    }

    private void processBandwidthDowngrade(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {
        if (vpnApplication.getVpnApplicationLinks().isEmpty() || vpnApplication.getVpnApplicationLinks().size() > 1) {
            throw new RequestFailureException("Invalid request");
        }
        VPNApplicationLink vpnApplicationLink = vpnApplication.getVpnApplicationLinks().get(0);
        VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, vpnApplicationLink.getNetworkLinkId());
        long historyId=vpnNetworkLink.getHistoryId();
        vpnNetworkLinkService.markLatestHistoryObsoleteByNetworkLinkId(vpnNetworkLink);
        VPNNetworkLink networkLink = getDBReadyVPNNetworkLinkWithIncident(vpnApplicationLink, vpnApplication, VPNConstants.INCIDENT.DOWNGRADE);
        networkLink.setLinkName(vpnApplicationLink.getLocalOffice().getOfficeName() + " => " + vpnApplicationLink.getRemoteOffice().getOfficeName() + " : " + networkLink.getLinkBandwidth() + " Mbps");
        networkLink.setHistoryId(historyId);
        globalService.save(networkLink);
        saveConsumerMap(vpnApplicationLink.getLocalOffice(),networkLink);
        saveConsumerMap(vpnApplicationLink.getRemoteOffice(),networkLink);
        vpnApplicationLink.setServiceStarted(true);
        vpnApplicationLink.setNetworkLinkId(networkLink.getId());
        globalService.update(vpnApplicationLink);
    }

    private void localLoopInventoryAllocation(Office office, Application application) throws Exception {
        for (LocalLoop localLoop : office.getLocalLoops()) {
            if (localLoop.getPortId() > 0) {

                inventoryAllocationHistoryService.allocateInventoryItem(localLoop.getPortId(), ModuleConstants.Module_ID_VPN, application.getClientId());
            }
            if (localLoop.getRouterOrSwitchId() > 0) {

                inventoryAllocationHistoryService.allocateInventoryItem(localLoop.getRouterOrSwitchId(), ModuleConstants.Module_ID_VPN, application.getClientId());
            }
            if (localLoop.getVlanId() > 0) {

                inventoryAllocationHistoryService.allocateInventoryItem(localLoop.getVlanId(), ModuleConstants.Module_ID_VPN, application.getClientId());
            }
        }
    }

    private void saveConsumerMap(Office office, VPNNetworkLink vpnNetworkLink) throws Exception {
        if (office.getLocalLoops().size() > 0) {
            for (LocalLoop localLoop :office.getLocalLoops()
            ) {
                globalService.save(setLocalLoopConsumer(vpnNetworkLink, localLoop));
                if (!localLoop.isCompleted()) {
                    localLoop.setCompleted(true);
                    globalService.update(localLoop);
                }
            }
        } else {
            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + office.getId());
        }

    }

    private void checkLocalLoopCostVerified(Map<Long, VPNLoopChargeDiscountEligibility> map,
                                            List<VPNApplicationLink> allAppliedLinks,
                                            VPNApplicationLink vpnApplicationLink) {
        VPNLoopChargeDiscountEligibility eligibility = map.getOrDefault(vpnApplicationLink.getId(), null);

        if(eligibility == null) {
            List<VPNApplicationLink> siblings = allAppliedLinks.stream()
                    .filter(t->t.getId() != vpnApplicationLink.getId())
                    .collect(Collectors.toList());

            siblings.forEach(sibling->{
                VPNLoopChargeDiscountEligibility siblingEligibility = map.getOrDefault(sibling.getId(), null);
                if(siblingEligibility != null) {
                    if(siblingEligibility.isSkipped()) {
                        if(sibling.getNetworkLinkId()==0) {
                            throw new RequestFailureException("You need to complete the application of Link Id: "+ sibling.getId());
                        }
                    }
                    else if(!siblingEligibility.isVerified()) {
                        throw new RequestFailureException("Payment and Payment Verification needed for Application Link Id: " + sibling.getId());
                    }
                }

            });
        }
    }

    private void newLinkInsert(VPNApplication application, LoginDTO loginDTO) {
        try {

            Map<Long, VPNLoopChargeDiscountEligibility> map = vpnApplicationService.getEligibleListByVpnApplicationId(application.getVpnApplicationId())
                    .stream()
                    .collect(Collectors.toMap(VPNLoopChargeDiscountEligibility::getVpnApplicationLinkId, Function.identity()));

            List<VPNApplicationLink> allAppliedLinks = globalService.getAllObjectListByCondition(
                    VPNApplicationLink.class, new VPNApplicationLinkConditionBuilder()
                    .Where()
                    .vpnApplicationIdEquals(application.getVpnApplicationId())
                    .getCondition()
            );

            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {
                    // new entry
                    checkLocalLoopCostVerified(map, allAppliedLinks, vpnApplicationLink);
                    // new entry
                    VPNNetworkLink vpnNetworkLink = getDBReadyVPNNetworkLinkWithIncident(vpnApplicationLink, application, VPNConstants.INCIDENT.NEW);
                    globalService.save(vpnNetworkLink);
                    localLoopInventoryAllocation(vpnApplicationLink.getLocalOffice(), application);
                    saveConsumerMap(vpnApplicationLink.getLocalOffice(),vpnNetworkLink);
                    localLoopInventoryAllocation(vpnApplicationLink.getRemoteOffice(), application);
                    saveConsumerMap(vpnApplicationLink.getRemoteOffice(),vpnNetworkLink);
                    vpnApplicationLink.setServiceStarted(true);
                    vpnApplicationLink.setNetworkLinkId(vpnNetworkLink.getId());
                    globalService.update(vpnApplicationLink);

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestFailureException("Process Not Competed ::" +e.getMessage());

        }
    }

    private VPNNetworkLink getDBReadyVPNNetworkLinkWithIncident(VPNApplicationLink vpnApplicationLink, VPNApplication application, VPNConstants.INCIDENT incident) throws Exception {
        VPNNetworkLink vpnNetworkLink = newLinkCreator(vpnApplicationLink, incident);
        vpnNetworkLink.setClientId(application.getClientId());
        vpnNetworkLink.setApplicationId(application.getVpnApplicationId());
        return vpnNetworkLink;
    }

    private VPNNetworkLink newLinkCreator(VPNApplicationLink vpnApplicationLink, VPNConstants.INCIDENT incident) throws Exception {
        VPNNetworkLink vpnNetworkLink = new VPNNetworkLink();
        long linkID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(
                        ModifiedSqlGenerator.getTableName(VPNNetworkLink.class));
        vpnNetworkLink.setId(linkID);
        vpnNetworkLink.setHistoryId(linkID);
        vpnNetworkLink.setActiveFrom(System.currentTimeMillis());
        vpnNetworkLink.setActiveTo(Long.MAX_VALUE);
        vpnNetworkLink.setValidFrom(System.currentTimeMillis());
        vpnNetworkLink.setValidTo(Long.MAX_VALUE);
        vpnNetworkLink.setLinkBandwidth(vpnApplicationLink.getLinkBandwidth());
        vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_ACTIVE.getStatus());
        vpnNetworkLink.setIncidentType(incident.getIncidentName());
        vpnNetworkLink.setLocalOfficeId(vpnApplicationLink.getLocalOfficeId());
        vpnNetworkLink.setRemoteOfficeId(vpnApplicationLink.getRemoteOfficeId());
        vpnNetworkLink.setLocalOfficeTerminalDeviceProvider(vpnApplicationLink.getLocalOfficeTerminalDeviceProvider());
        vpnNetworkLink.setRemoteOfficeTerminalDeviceProvider(vpnApplicationLink.getRemoteOfficeTerminalDeviceProvider());
        vpnNetworkLink.setLocalZoneId(vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getZoneId());
        vpnNetworkLink.setRemoteZoneId(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getZoneId());
        vpnNetworkLink.setLinkName(vpnApplicationLink.getLinkName());
        vpnNetworkLink.setLinkDistance();
        return vpnNetworkLink;
    }

    private LocalLoopConsumerMap setLocalLoopConsumer(VPNNetworkLink vpnNetworkLink, LocalLoop localLoop) throws Exception {
        LocalLoopConsumerMap localLoopConsumerMap = new LocalLoopConsumerMap();
        localLoopConsumerMap.setConsumerId(vpnNetworkLink.getId());
        localLoopConsumerMap.setConsumerType("VPN Link");//todo: chande hardcode
        localLoopConsumerMap.setConsumerModuleId(ModuleConstants.Module_ID_VPN);
        localLoopConsumerMap.setLocalLoopId(localLoop.getId());
        localLoopConsumerMap.setActive(true);
        localLoopConsumerMap.setFromDate(System.currentTimeMillis());
        localLoopConsumerMap.setToDate(Long.MAX_VALUE);
        boolean isBillApplicable = localLoopService.isBillApplicableForConsumer(localLoop.getId());
        localLoopConsumerMap.setBillApplicable(isBillApplicable);
        return localLoopConsumerMap;
    }


}
