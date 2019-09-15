package vpn.application;

import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.DAO;
import annotation.Transactional;
import application.Application;
import application.ApplicationService;
import application.ApplicationState;
import application.ApplicationType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.RoleConstants;
import common.client.ClientService;
import entity.comment.CommentService;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import entity.facade.AdviceNoteGenerationFacade;
import entity.facade.WorkOrderGenerationFacade;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConditionBuilder;
import entity.localloop.LocalLoopConsumerMap;
import entity.localloop.LocalLoopConsumerMapConditionBuilder;
import entity.office.Office;
import entity.office.OfficeService;
import exception.NoDataFoundException;
import flow.FlowService;
import flow.entity.FlowState;
import flow.entity.Role;
import flow.repository.FlowRepository;
import global.GlobalService;
import inventory.InventoryAllocationHistoryService;
import inventory.InventoryService;
import lli.LLIDropdownPair;
import location.ZoneDAO;
import location.ZoneService;
import login.LoginDTO;
import login.LoginService;
import lombok.extern.log4j.Log4j;
import notification.NotificationService;
import officialLetter.OfficialLetterService;

import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.*;
import vpn.VPNConstants;
import vpn.demandNote.VPNLoopChargeDiscountEligibility;
import vpn.demandNote.VPNLoopChargeDiscountEligibilityConditionBuilder;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;
import vpn.ownerchange.VPNOnProcessLink;
import vpn.ownerchange.VPNOnProcessLinkService;
import vpn.td.VPNClientTDService;
import vpn.td.VPNProbableTD;
import vpn.td.VPNProbableTDService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public class VPNApplicationService implements NavigationService {


    @DAO private VPNApplicationDAO vpnApplicationDAO;
    @DAO private ZoneDAO zoneDAO;
    @Service private ApplicationService applicationService;
    @Service private GlobalService globalService;
    @Service private NotificationService notificationService;
    @Service private CommentService commentService;
    @Service private FlowService flowService;
    @Service private OfficeService vpnOfficeService;

    private OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    private ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    private ZoneService zoneService = ServiceDAOFactory.getService(ZoneService.class);
    private UserService userService = ServiceDAOFactory.getService(UserService.class);
    private InventoryAllocationHistoryService inventoryAllocationHistoryService=ServiceDAOFactory.getService(InventoryAllocationHistoryService.class);


    @Transactional
    public void sendNotification(VPNApplication application, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        boolean isForClient = false;
        if (loginDTO.getRoleID() > 0) {
            //todo support parallel actions
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_VPN, vpnApplicationLink.getId(), loginDTO.getRoleID(), true, loginDTO.getUserID());
        } else {
            //for updating client notification
            notificationService.markNotificationAsActionTaken(ModuleConstants.Module_ID_VPN, vpnApplicationLink.getId(), loginDTO.getAccountID(), true, loginDTO.getUserID());
        }

        HashMap<Long, Role> uniqueRoles = notificationService.getNextStateRoleListForNotification(vpnApplicationLink.getLinkState().getState());
        if (uniqueRoles.isEmpty()) {
            isForClient = true;
            notificationService.saveNotificationForNextStateUser(
                    isForClient,
                    vpnApplicationLink.getLinkState().getState(),
                    application.getClientId(),
                    -1,
                    ModuleConstants.Module_ID_VPN,
                    application.getApplicationId(),

                    "VPN Application",
                    VPNConstants.VPN_DETAILS_PAGE_URL,
                    true,
                    vpnApplicationLink.getId()
            );

        } else {
            Iterator it = uniqueRoles.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Role role = (Role) pair.getValue();
                Set<UserDTO> userDTOS = UserRepository.getInstance().getUsersByRoleID(role.getId());
                List<Long> userIdsForNotification = new ArrayList<>();


                for (UserDTO userDTO : userDTOS
                ) {
                    if (userDTO.getRoleID() == RoleConstants.LDGM_ROLE) {

                        ArrayList<Integer> zones = zoneDAO.getUserZone((int) userDTO.getUserID());
                        LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
                        LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
                        if (localOfficeLoop.getZoneId() > 0 && localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL
                                && !localOfficeLoop.isCompleted()) {
                            if (zones.contains(localOfficeLoop.getZoneId())) {
                                userIdsForNotification.add(userDTO.getUserID());
                            }
                        }
                        if (remoteOfficeLoop.getZoneId() > 0 && remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL
                                && !remoteOfficeLoop.isCompleted()) {
                            if (zones.contains(remoteOfficeLoop.getZoneId())) {
                                userIdsForNotification.add(userDTO.getUserID());
                            }
                        }
                    } else if (userDTO.getRoleID() == RoleConstants.VENDOR_ROLE) {

                        List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                                new EFRConditionBuilder()
                                        .Where()
                                        .vendorIDEquals(userDTO.getUserID())
                                        .applicationIdEquals(application.getApplicationId())
                                        .getCondition()
                        );
                        if (efrs.size() > 0) {
                            userIdsForNotification.add(efrs.get(0).getVendorID());
                        }


                    } else {

                        userIdsForNotification.add(userDTO.getUserID());


                    }
                }


                for (Long userId : userIdsForNotification) {

                    notificationService.saveNotificationForNextStateUser(
                            isForClient,
                            vpnApplicationLink.getLinkState().getState(),
                            application.getClientId(),
                            userId,
                            ModuleConstants.Module_ID_VPN,
                            application.getApplicationId(),

                            "VPN Application",
                            VPNConstants.VPN_DETAILS_PAGE_URL,
                            true,
                            vpnApplicationLink.getId()
                    );

                }


            }
        }


    }


    //region efr related logic

    @Transactional
    public void forwardRequest(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {
//        updateBatchOperation(vpnApplication);
        boolean isLocalofficeLoopForwarded = false;
        boolean isLocalofficeLoopEfrRequested = false;
        ApplicationState applicationState = null;
        List<Long> processedLinks = new ArrayList<>();


        if (vpnApplication.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : vpnApplication.getVpnApplicationLinks()) {


                LocalLoop localOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getLocalOfficeLoopId());
                LocalLoop remoteOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getRemoteOfficeLoopId());

                if(!(localOfficeLoop.isIfrFeasibility()&&remoteOfficeLoop.isIfrFeasibility())){


                    throw new RequestFailureException("IFR Feasibility of Vpn Link With Id: "+vpnApplicationLink.getId()+ " is negative. Please Reject this application and try again ");


                }else {

                    if (vpnApplicationLink.getLinkState() == null) {
                        throw new RequestFailureException(" No Next State found for Link" + vpnApplicationLink.getId());
                    } else {
                        try {
                            processedLinks.add(vpnApplicationLink.getId());
                            applicationState = vpnApplicationLink.getLinkState();


                            if (vpnApplicationLink.getLocalOffice() != null) {
                                // update local office
                                if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                                    for (LocalLoop localLoop : vpnApplicationLink.getLocalOffice().getLocalLoops()
                                    ) {

                                        if (!localLoop.isCompleted()
                                                && !(localLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT)
                                        ) {

                                            //todo: if cdgm forward and also requested for efr then what to do?

                                            List<EFR> efr = globalService.getAllObjectListByCondition(EFR.class,
                                                    new EFRConditionBuilder()
                                                            .Where()
                                                            .officeIdEquals(localLoop.getOfficeId())
                                                            .getCondition()
                                            );
                                            if (efr.size() > 0) {
                                                for (EFR efr1 : efr
                                                ) {
                                                    efr1.setIgnored(true);
                                                    globalService.update(efr1);
                                                }
                                            }
                                            globalService.update(localLoop); // update local office loop
                                            isLocalofficeLoopForwarded = true;
                                        }

                                    }
                                }
                            }

                            if (vpnApplicationLink.getRemoteOffice() != null) {
                                if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                                    for (LocalLoop localLoop : vpnApplicationLink.getRemoteOffice().getLocalLoops()) {

                                        if (!localLoop.isCompleted()) {
                                            globalService.update(localLoop);
                                        }
                                    }
                                }
                            }

                            globalService.update(vpnApplicationLink);
                            sendNotification(vpnApplication, vpnApplicationLink, loginDTO);


                            //insert the comments
                            commentService.insertComment(
                                    vpnApplication.getComment(),
                                    vpnApplicationLink.getId(),
                                    loginDTO.getUserID(),
                                    ModuleConstants.Module_ID_VPN,
                                    vpnApplicationLink.getLinkState().getState()
                            );
                        } catch (Exception e) {
                            throw new RequestFailureException("Update Failed :" + e.getMessage());
                        }
                    }
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + vpnApplication.getApplicationId());
        }
//        VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
        List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                        .getCondition())
                .stream()
                .filter(
                        t -> !processedLinks.contains(t.getId())
                )
                .collect(Collectors.toList());

        if (vpnApplicationLinks.size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                if (isLocalofficeLoopForwarded) {
                    LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
                    if (remoteOfficeLoop.isCompleted() || remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                        LocalLoop localOfficeOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
                        if (localOfficeOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localOfficeOfficeLoop.isCompleted()) {

                            vpnApplicationLink.setLinkState(applicationState);
                            globalService.update(vpnApplicationLink);
                            sendNotification(vpnApplication, vpnApplicationLink, loginDTO);

                        }


                    }
                }

            }
        }

    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void efrInsertOrUpdate(VPNApplicationLink vpnApplicationLink) throws Exception {

        if (vpnApplicationLink.getLocalOffice() != null) {
            List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                            .isIgnored(false)
                            .getCondition()
            );
            if (efrs != null && efrs.size() > 0) {

            } else {
                if (vpnApplicationLink.getLocalOffice().getEfrs() != null) {
                    if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                        for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                        ) {
                            if (efr.getId() > 0) {

                                globalService.update(efr); // update local office loop
                            } else {
                                globalService.save(efr);
                            }
                        }
                    }
                }
            }
        }
        if (vpnApplicationLink.getRemoteOffice() != null) {
            if (vpnApplicationLink.getRemoteOffice().getEfrs() != null) {
                if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {

                            globalService.update(efr); // update local office loop
                        } else {
                            globalService.save(efr);
                        }
                    }
                }
            }
        }

    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void efrUpdate(VPNApplicationLink vpnApplicationLink) throws Exception {

        if (vpnApplicationLink.getLocalOffice() != null) {


            if (vpnApplicationLink.getLocalOffice().getEfrs() != null) {
                if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {


                            if(efr.getProposedLoopDistance()<=0){
                                throw new RequestFailureException("Loop Distance should be positive:");
                            }

                            globalService.update(efr); // update local office loop
                        }

                    }
                }

            }
        }
        if (vpnApplicationLink.getRemoteOffice() != null) {
            if (vpnApplicationLink.getRemoteOffice().getEfrs() != null) {
                if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {

                            if(efr.getProposedLoopDistance()<=0){
                                throw new RequestFailureException("Loop Distance should be positive:");
                            }


                            globalService.update(efr); // update local office loop
                        }
                    }
                }
            }
        }

    }


    @Transactional
    private void getLinkIntoEfrRequestedState(VPNApplication application, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localLoopEFR = new ArrayList<>();
        List<EFR> remoteLoopEFR = new ArrayList<>();
        boolean localLoopEfrFlag = false;
        boolean remoteLoopEfrFlag = false;
        LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());


        if (vpnApplicationLink.localOfficeId != 0) {
            localLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                            .isIgnored(false)
                            .getCondition()
            );
        }
        if (vpnApplicationLink.remoteOfficeId != 0) {
            remoteLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                            .isReplied(false)
                            .isIgnored(false)
                            .getCondition()
            );
        }
        if (localLoopEFR.size() > 0) {
            localLoopEfrFlag = true;
        } else {
            if (localOfficeLoop.isCompleted() || localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !remoteOfficeLoop.isCompleted()) {

                    localLoopEfrFlag = true;
                }

            }
        }
        if (remoteLoopEFR.size() > 0) {
            remoteLoopEfrFlag = true;

        } else {
            if (remoteOfficeLoop.isCompleted() || remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localOfficeLoop.isCompleted()) {

                    remoteLoopEfrFlag = true;
                }
            }
        }


        if (localLoopEfrFlag && remoteLoopEfrFlag) {

            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_LDGM_FOR_LOOP
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_IFR_RESPONDED
            ) {
                globalService.update(vpnApplicationLink);
                sendNotification(application, vpnApplicationLink, loginDTO);
            }


        }

    }

    @Transactional
    public void efrRequestLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {

        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {

                LocalLoop localOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getLocalOfficeLoopId());
                LocalLoop remoteOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getRemoteOfficeLoopId());

                if(localOfficeLoop.isIfrFeasibility()&&remoteOfficeLoop.isIfrFeasibility()) {


                    applicationState = vpnApplicationLink.linkState;
                    processedLinks.add(vpnApplicationLink.getId());
                    try {

                        efrInsertOrUpdate(vpnApplicationLink);
                        getLinkIntoEfrRequestedState(application, vpnApplicationLink, loginDTO);

                        //insert the comments
                        commentService.insertComment(
                                application.getComment(),
                                vpnApplicationLink.getId(),
                                loginDTO.getUserID(),
                                ModuleConstants.Module_ID_VPN,
                                vpnApplicationLink.getLinkState().getState()
                        );


                    } catch (Exception e) {
                        throw new RequestFailureException("Update Failed :" + e.getMessage());
                    }
                }else{
                    throw new RequestFailureException("Ifr Feasibility of Vpn Link With Id: "+vpnApplicationLink.getId()+ " is negative. Please Reject this application and try again ");

                }
            }
            //update of other links if necessary
            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {


//                    LocalLoop localOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getLocalOfficeLoopId());
//                    LocalLoop remoteOfficeLoop=globalService.findByPK(LocalLoop.class,vpnApplicationLink.getRemoteOfficeLoopId());

//                    if(localOfficeLoop.isIfrFeasibility()&&remoteOfficeLoop.isIfrFeasibility()) {
                        vpnApplicationLink.setLinkState(applicationState);
                        getLinkIntoEfrRequestedState(application, vpnApplicationLink, loginDTO);
//                    }else{
//                        throw new RequestFailureException("Ifr Feasibility of Vpn Link With Id: "+vpnApplicationLink.getId()+ " is negative. Please Reject this application and try again ");
//                    }
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

    private boolean checkAllEfrRepliedOrIgnored(List<EFR> efrs) {
        int localLoopRespondedEFR = 0;
        int localLoopIgnoredEFR = 0;
        boolean allLocalLoopEfrRepliedOrIgnored = false;

        for (EFR efr : efrs) {
            if (efr.isReplied() && !efr.isIgnored()) {
                localLoopRespondedEFR++;
            }
            if (!efr.isReplied() && efr.isIgnored()) {
                localLoopIgnoredEFR++;
            }

        }

        int allEfrSize = efrs.size();
        int allRespondedOrIgnoredSize = localLoopRespondedEFR + localLoopIgnoredEFR;
        if (allEfrSize == allRespondedOrIgnoredSize) {
            allLocalLoopEfrRepliedOrIgnored = true;
        }
        return allLocalLoopEfrRepliedOrIgnored;

    }

    @Transactional
    private void getIntoEFRRespondedState(VPNApplicationLink vpnApplicationLink, VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {

        boolean allLocalEndLocalLoopRepliedOrIgnored;
        boolean allRemoteEndLocalLoopRepliedOrIgnored;
        List<EFR> localLoopEFR;

        if (vpnApplicationLink.getLocalOffice() != null) {

            localLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getLocalOffice().getId())
                            .applicationIdEquals(vpnApplication.getApplicationId())
                            .getCondition()
            );
            allLocalEndLocalLoopRepliedOrIgnored = checkAllEfrRepliedOrIgnored(localLoopEFR);
        } else {
            Office office = globalService.findByPK(Office.class, vpnApplicationLink.getLocalOfficeId());
            localLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(office.getId())
                            .applicationIdEquals(vpnApplication.getApplicationId())
                            .getCondition()
            );

            allLocalEndLocalLoopRepliedOrIgnored = checkAllEfrRepliedOrIgnored(localLoopEFR);
        }
        if (vpnApplicationLink.getRemoteOffice() != null) {
            localLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getRemoteOffice().getId())
                            .applicationIdEquals(vpnApplication.getApplicationId())
                            .getCondition()
            );
            allRemoteEndLocalLoopRepliedOrIgnored = checkAllEfrRepliedOrIgnored(localLoopEFR);
        } else {
            Office office = globalService.findByPK(Office.class, vpnApplicationLink.getRemoteOfficeId());
            localLoopEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(office.getId())
                            .applicationIdEquals(vpnApplication.getApplicationId())
                            .getCondition()
            );
            allRemoteEndLocalLoopRepliedOrIgnored = checkAllEfrRepliedOrIgnored(localLoopEFR);
        }
        if (allLocalEndLocalLoopRepliedOrIgnored && allRemoteEndLocalLoopRepliedOrIgnored) {

            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());

            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_LDGM_EFR_REQUEST_FOR_LOOP
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_EFR_REQUESTED
            ) {
                globalService.update(vpnApplicationLink);
                sendNotification(vpnApplication, vpnApplicationLink, loginDTO);

            }
        }


    }

    @Transactional
    public void efrResponseLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {
                    applicationState = vpnApplicationLink.linkState;
                    processedLinks.add(vpnApplicationLink.getId());
                    efrUpdate(vpnApplicationLink);
                    getIntoEFRRespondedState(vpnApplicationLink, application, loginDTO);

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                    vpnApplicationLink.setLinkState(applicationState);
                    getIntoEFRRespondedState(vpnApplicationLink, application, loginDTO);
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

    @Transactional
    public void efrSelectLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        if (application.getVpnApplicationLinks().size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                applicationState = vpnApplicationLink.linkState;
                processedLinks.add(vpnApplicationLink.getId());
                try {
                    efrUpdate(vpnApplicationLink);
                    getLinkIntoSelectedState(application, vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

            //update of other links if necessary
            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                    vpnApplicationLink.setLinkState(applicationState);
                    getOtherLinkIntoSelectedState(application, vpnApplicationLink, loginDTO);
                }
            }

        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

    @Transactional
    private void calculatePropsedLoopDistances(List<EFR> SelectedEFR) throws Exception {
        List<LocalLoop> localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(SelectedEFR.get(0).getOfficeId())
                        .getCondition()
        );
        int btclDistances = 0;
        int ocDistances = 0;
        long ocID = 0;
        int ofcType = 0;
        for (EFR efr : SelectedEFR
        ) {
            if (efr.getVendorType() == 1) {
                btclDistances += efr.getProposedLoopDistance();
                ofcType = (int) efr.getOfcType();
            } else if (efr.getVendorType() == 2) {
                ocDistances += efr.getProposedLoopDistance();
                ocID = efr.getVendorID();
                ofcType = (int) efr.getOfcType();

            }
        }
        for (LocalLoop localLoop : localLoops) {
            localLoop.setBtclDistance(btclDistances);
            localLoop.setOcDistance(ocDistances);
            localLoop.setVendorId(ocID);
            localLoop.setDistributed(false);
            localLoop.setOfcType(ofcType);
            if (!localLoop.isAdjusted()) {
                globalService.update(localLoop);
            }
        }
    }

    @Transactional
    private void getLinkIntoSelectedState(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localEndLocalLoopSelectedEFR = new ArrayList<>();
        List<EFR> remoteEndLocalLoopSelectedEFR = new ArrayList<>();
        boolean anyLocalEndLocalLoopSelected = false;
        boolean anyRemoteEndLocalLoopselected = false;
        LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());


        localEndLocalLoopSelectedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .getCondition()
        );

        if (localEndLocalLoopSelectedEFR != null && localEndLocalLoopSelectedEFR.size() > 0) {

            calculatePropsedLoopDistances(localEndLocalLoopSelectedEFR);
            anyLocalEndLocalLoopSelected = true;

        } else {
            if (localOfficeLoop.isCompleted() || localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !remoteOfficeLoop.isCompleted()) {

                    anyLocalEndLocalLoopSelected = true;
                }
            }
        }


        remoteEndLocalLoopSelectedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .getCondition()
        );
        if (remoteEndLocalLoopSelectedEFR != null && remoteEndLocalLoopSelectedEFR.size() > 0) {
//            if (remoteEndLocalLoopSelectedEFR.size() > 0) {
            calculatePropsedLoopDistances(remoteEndLocalLoopSelectedEFR);
            anyRemoteEndLocalLoopselected = true;
//            }
        } else {
            if (remoteOfficeLoop.isCompleted() || remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localOfficeLoop.isCompleted()) {

                    anyRemoteEndLocalLoopselected = true;
                }
            }
        }
        if (anyLocalEndLocalLoopSelected && anyRemoteEndLocalLoopselected) {
            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_LDGM_RESPONSE_EXTERNAL_FR
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_EFR_WIP
            ) {

                globalService.update(vpnApplicationLink);
                sendNotification(vpnApplication, vpnApplicationLink, loginDTO);


            }


        }else{
            if(!(localOfficeLoop.isDistributed()&&remoteOfficeLoop.isDistributed())){

                throw new RequestFailureException("At least one EFR should be selected to complete Local or Remote End");
            }
            //todo handle logic when ldgm select efr

        }

    }


    @Transactional
    private void getOtherLinkIntoSelectedState(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localEndLocalLoopSelectedEFR = new ArrayList<>();
        List<EFR> remoteEndLocalLoopSelectedEFR = new ArrayList<>();
        boolean anyLocalEndLocalLoopSelected = false;
        boolean anyRemoteEndLocalLoopselected = false;
        LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());


        localEndLocalLoopSelectedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .getCondition()
        );

        if (localEndLocalLoopSelectedEFR != null && localEndLocalLoopSelectedEFR.size() > 0) {

            calculatePropsedLoopDistances(localEndLocalLoopSelectedEFR);
            anyLocalEndLocalLoopSelected = true;

        } else {
            if (localOfficeLoop.isCompleted() || localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !remoteOfficeLoop.isCompleted()) {

                    anyLocalEndLocalLoopSelected = true;
                }
            }
        }


        remoteEndLocalLoopSelectedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .getCondition()
        );
        if (remoteEndLocalLoopSelectedEFR != null && remoteEndLocalLoopSelectedEFR.size() > 0) {
//            if (remoteEndLocalLoopSelectedEFR.size() > 0) {
            calculatePropsedLoopDistances(remoteEndLocalLoopSelectedEFR);
            anyRemoteEndLocalLoopselected = true;
//            }
        } else {
            if (remoteOfficeLoop.isCompleted() || remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                if (localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localOfficeLoop.isCompleted()) {

                    anyRemoteEndLocalLoopselected = true;
                }
            }
        }
        if (anyLocalEndLocalLoopSelected && anyRemoteEndLocalLoopselected) {
            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_LDGM_RESPONSE_EXTERNAL_FR
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_EFR_WIP
            ) {

                globalService.update(vpnApplicationLink);
                sendNotification(vpnApplication, vpnApplicationLink, loginDTO);


            }


        }

    }

    //endregion

    //region work order related work

    @Transactional
    public void markEFrAsCollaborated(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {

        boolean localEndalreadyApproved = false;
        for (VPNApplicationLink vpnApplicationLink : vpnApplication.getVpnApplicationLinks()
        ) {


            if (!localEndalreadyApproved) {

                if (vpnApplicationLink.getLocalOffice() != null) {
                    int collboratedEfrs = 0;
                    boolean allCollborated = false;
                    int allLocalEndEfrColaborated = 0;

                    if (vpnApplicationLink.getLocalOffice().getEfrs() != null) {
                        if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {

                            for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                            ) {
                                if (efr.getId() > 0) {

                                    if (efr.isCollaborationApproved()) {
                                        collboratedEfrs++;
                                        allLocalEndEfrColaborated++;

                                    } else if (!efr.isIgnored() && efr.isSelected() && efr.isCollaborated() && !efr.isCollaborationApproved()) {
                                        efr.setCollaborationApproved(true);
                                        collboratedEfrs++;
                                        globalService.update(efr); // update local office loop
                                    }
                                }

                            }
                        }

                    }
                    if (allLocalEndEfrColaborated == vpnApplicationLink.getLocalOffice().getEfrs().size()) {
                        localEndalreadyApproved = true;
                    }
                    if (collboratedEfrs == vpnApplicationLink.getLocalOffice().getEfrs().size()) {
                        allCollborated = true;
                    }

                    if (allCollborated) {
                        if (vpnApplicationLink.getLocalOffice().getLocalLoops() != null) {
                            if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                                for (LocalLoop localLoop : vpnApplicationLink.getLocalOffice().getLocalLoops()
                                ) {
                                    if (localLoop.getId() > 0) {

                                        if (!localLoop.isCompleted()
                                                && localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL
                                        ) {

                                            if (localLoop.getZoneId() > 0&&vpnApplicationLink.getLinkState()==ApplicationState.VPN_APPROVE_FORWARDED_SR_COLLABORATION) {
                                                localLoop.setDistributed(true);

                                            }// update local office loop

                                            globalService.update(localLoop);
                                        }
                                    }

                                }
                            }

                        }
                    }
                }
            }
            if (vpnApplicationLink.getRemoteOffice() != null) {
                if (vpnApplicationLink.getRemoteOffice().getEfrs() != null) {
                    if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                        for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()
                        ) {
                            if (efr.getId() > 0) {


                                if (!efr.isIgnored() && efr.isSelected() && efr.isCollaborated()) {

                                    efr.setCollaborationApproved(true);
                                    globalService.update(efr); // update local office loop
                                }
                            }
                        }
                    }
                }

                if (vpnApplicationLink.getRemoteOffice().getLocalLoops() != null) {
                    if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                        for (LocalLoop localLoop : vpnApplicationLink.getRemoteOffice().getLocalLoops()
                        ) {
                            if (localLoop.getId() > 0) {

                                if (!localLoop.isCompleted() && localLoop.getZoneId() > 0 && localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL) {
                                    if (localLoop.getZoneId() > 0 && vpnApplicationLink.getLinkState()==ApplicationState.VPN_APPROVE_FORWARDED_SR_COLLABORATION) {
                                        localLoop.setDistributed(true);
                                    }
                                    globalService.update(localLoop); // update local office loop
                                }
                            }

                        }
                    }

                }
            }

            globalService.update(vpnApplicationLink);
            sendNotification(vpnApplication, vpnApplicationLink, loginDTO);

            //insert the comments
            commentService.insertComment(vpnApplication.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());
        }

    }

    @Transactional
    public void WOforwardRequest(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {
        boolean isLocalofficeLoopForwarded = false;
        boolean isLocalofficeLoopEfrRequested = false;
        ApplicationState applicationState = null;
        List<Long> processedLinks = new ArrayList<>();
        if (vpnApplication.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : vpnApplication.getVpnApplicationLinks()) {
                try {
                    processedLinks.add(vpnApplicationLink.getId());
                    applicationState = vpnApplicationLink.getLinkState();

                    if (vpnApplicationLink.getLocalOffice() != null) {
                        // update local office
                        if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                            for (LocalLoop localLoop : vpnApplicationLink.getLocalOffice().getLocalLoops()
                            ) {

                                if (!localLoop.isCompleted()) {

                                    //todo: if cdgm forward and also requested for efr then what to do?

//                                    if (!(efr.size()>0)){
//                                        efr=globalService.getAllObjectListByCondition(EFR.class,
//                                                new EFRConditionBuilder()
//                                                        .Where()
//                                                        .officeIdEquals(localLoop.getOfficeId())
//                                                        .isReplied(true)
//                                                        .isSelected(true)
//                                                        .getCondition()
//                                        );
//
//                                    }
//                                    if(efr.size()>0){
//                                        localLoop.setDistributed(false);
//                                        globalService.update(localLoop);
//                                        isLocalofficeLoopEfrRequested=true;
//                                    }else{
                                    globalService.update(localLoop); // update local office loop
                                    if (localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL) {
                                        isLocalofficeLoopForwarded = true;
                                    }
//                                    }


                                }

                            }
                        }
                    }

                    if (vpnApplicationLink.getRemoteOffice() != null) {
                        if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                            for (LocalLoop localLoop : vpnApplicationLink.getRemoteOffice().getLocalLoops()) {

                                if (!localLoop.isCompleted()) {
                                    globalService.update(localLoop);
                                }
                            }
                        }
                    }

                    globalService.update(vpnApplicationLink);
                    sendNotification(vpnApplication, vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(vpnApplication.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + vpnApplication.getApplicationId());
        }
//        VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
        List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                        .getCondition())
                .stream()
                .filter(
                        t -> !processedLinks.contains(t.getId())
                )
                .collect(Collectors.toList());

        if (vpnApplicationLinks.size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                if (isLocalofficeLoopForwarded) {
                    LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
                    if (remoteOfficeLoop.isCompleted() || remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
                        LocalLoop localOfficeOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
                        if (localOfficeOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localOfficeOfficeLoop.isCompleted()) {
                            vpnApplicationLink.setLinkState(applicationState);
                            globalService.update(vpnApplicationLink);
                            sendNotification(vpnApplication, vpnApplicationLink, loginDTO);
                        }

                    }
                }

            }
        }

    }

    @Transactional
    private void markEFRAsWorkOrderGiven(VPNApplicationLink vpnApplicationLink) throws Exception {
        Office localOffice = vpnApplicationLink.getLocalOffice();
        Office remoteOffice = vpnApplicationLink.getRemoteOffice();

        if (isOfficeContainsEFR(localOffice)) {
            updateEFRAsSetWorkOrdered(localOffice.getEfrs());
        }
        if (isOfficeContainsEFR(remoteOffice)) {
            updateEFRAsSetWorkOrdered(remoteOffice.getEfrs());
        }
    }

    private void updateEFRAsSetWorkOrdered(List<EFR> efrs) {
        List<EFR> updatedEFRs = efrs.stream()
                .filter(efr -> efr.getId() > 0)
                .peek(efr -> efr.setWorkOrdered(true))
                .collect(Collectors.toList());

        updatedEFRs.forEach(globalService::update);
    }

    private boolean isOfficeContainsEFR(Office office) {
        if (office != null && office.getEfrs() != null) {
            return !office.getEfrs().isEmpty();
        }
        return false;
    }


    @Transactional
    private boolean isLinkGetIntoWorkOrderGivenState(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink) throws Exception {
        List<EFR> localEndLocalLoopSelectedWorkNotGivenEFR = getSelectedWorkNotGivenEFRsByOfficeId(vpnApplication, vpnApplicationLink.getLocalOfficeId());
        List<EFR> remoteEndLocalLoopSelectedWorkNotGivenEFR = getSelectedWorkNotGivenEFRsByOfficeId(vpnApplication, vpnApplicationLink.getRemoteOfficeId());
        LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
        if (vpnApplication.getApplicationType() == ApplicationType.VPN_CLOSE) {
            return localEndLocalLoopSelectedWorkNotGivenEFR.isEmpty() && remoteEndLocalLoopSelectedWorkNotGivenEFR.isEmpty();
        } else if ((
                localOfficeLoop.isCompleted() && remoteOfficeLoop.isCompleted())
                ||
                (localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && remoteOfficeLoop.isCompleted())
                ||
                (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && localOfficeLoop.isCompleted())
                ||
                (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT)
        ) {
            return false;
        } else {

            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_FOR_WORK_ORDER
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_PAYMENT_VERIFIED
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_DEMAND_NOTE_SKIP
            ) {
                return localEndLocalLoopSelectedWorkNotGivenEFR.isEmpty() && remoteEndLocalLoopSelectedWorkNotGivenEFR.isEmpty();

            } else {
                return false;
            }

        }

    }

    private List<EFR> getSelectedWorkNotGivenEFRsByOfficeId(VPNApplication vpnApplication, long officeId) throws Exception {
        return globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(officeId)
                        .applicationIdEquals(vpnApplication.getApplicationId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .isWorkOrdered(false)
                        .getCondition()
        );
    }


    @Transactional
    public void workOrderGiveLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        List<VPNApplicationLink> succesfullProcessedVpnApplicationLinks = new ArrayList<>();

        if (application.getVpnApplicationLinks().size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                applicationState = vpnApplicationLink.linkState;
                processedLinks.add(vpnApplicationLink.getId());
                try {
                    markEFRAsWorkOrderGiven(vpnApplicationLink);
                    if (isLinkGetIntoWorkOrderGivenState(application, vpnApplicationLink)) {
                        globalService.update(vpnApplicationLink);
                        sendNotification(application, vpnApplicationLink, loginDTO);
                        succesfullProcessedVpnApplicationLinks.add(vpnApplicationLink);

                    }

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

            //update of other links if necessary

//            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(application.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                    vpnApplicationLink.setLinkState(applicationState);
                    if (isLinkGetIntoWorkOrderGivenState(application, vpnApplicationLink)) {
                        globalService.update(vpnApplicationLink);
                        sendNotification(application, vpnApplicationLink, loginDTO);
                        succesfullProcessedVpnApplicationLinks.add(vpnApplicationLink);
                    }
                }
            }

            ServiceDAOFactory.getService(WorkOrderGenerationFacade.class)
                    .generateGenericWorkOrder(ModuleConstants.Module_ID_VPN, loginDTO,
                            succesfullProcessedVpnApplicationLinks, application.getApplicationId(),
                            application.getClientId());


        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }


    @Transactional
    private void markEFRAsWorkDone(VPNApplicationLink vpnApplicationLink) throws Exception {

        if (vpnApplicationLink.getLocalOffice() != null) {
            if (vpnApplicationLink.getLocalOffice().getEfrs() != null) {
                if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {
                            efr.setCompleted(true);
                            globalService.update(efr); // update local office loop
                        } else {
                            throw new RequestFailureException("Update Failed no efr found:");

                        }
                    }
                }
            }
        }
        if (vpnApplicationLink.getRemoteOffice() != null) {
            if (vpnApplicationLink.getRemoteOffice().getEfrs() != null) {
                if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {

                            efr.setCompleted(true);
                            globalService.update(efr); // update local office loop
                        } else {
                            throw new RequestFailureException("Update Failed no efr found:");
                        }
                    }
                }
            }
        }

    }


    @Transactional
    private void getLinkIntoWorkOrderDoneState(VPNApplication vpnApplication, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localEndLocalLoopSelectedWorkNotDoneEFR = new ArrayList<>();
        List<EFR> remoteEndLocalLoopSelectedWorkNotDoneEFR = new ArrayList<>();
        boolean allLocalEndLocalLoopWorkOrderCompleted = false;
        boolean allRemoteEndLocalLoopWorkOrderCompleted = false;

        VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());



        localEndLocalLoopSelectedWorkNotDoneEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                        .applicationIdEquals(vpnApplication.getApplicationId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .isWorkOrdered(true)
                        .isCompleted(false)
                        .getCondition()
        );

        if (localEndLocalLoopSelectedWorkNotDoneEFR != null) {
            if (localEndLocalLoopSelectedWorkNotDoneEFR.size() == 0) {
                allLocalEndLocalLoopWorkOrderCompleted = true;
            }
        }


        remoteEndLocalLoopSelectedWorkNotDoneEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                        .applicationIdEquals(vpnApplication.getApplicationId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .isWorkOrdered(true)
                        .isCompleted(false)
                        .getCondition()
        );
        if (remoteEndLocalLoopSelectedWorkNotDoneEFR != null) {
            if (remoteEndLocalLoopSelectedWorkNotDoneEFR.size() == 0) {
                allRemoteEndLocalLoopWorkOrderCompleted = true;

            }
        }
        if (allLocalEndLocalLoopWorkOrderCompleted && allRemoteEndLocalLoopWorkOrderCompleted) {
            LocalLoop localOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
            LocalLoop remoteOfficeLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
            if (vpnApplication.getApplicationType() == ApplicationType.VPN_CLOSE) {
                if (!localOfficeLoop.isCompleted()
                        && localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL

                ) {
                    localOfficeLoop.setDistributed(false);
                    globalService.update(localOfficeLoop);
                }

                if (!remoteOfficeLoop.isCompleted()
                        && remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL

                ) {
                    remoteOfficeLoop.setDistributed(false);
                    globalService.update(remoteOfficeLoop);
                }
                globalService.update(vpnApplicationLink);
                sendNotification(vpnApplication, vpnApplicationLink, loginDTO);

            } else if ((
                    localOfficeLoop.isCompleted() && remoteOfficeLoop.isCompleted())
                    ||
                    (localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && remoteOfficeLoop.isCompleted())
                    ||
                    (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && localOfficeLoop.isCompleted())
                    ||
                    (remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT && localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT)
            ) {

            } else {


                if (currentApplicationLink.getLinkState() == ApplicationState.VPN_GENERATE_WORK_ORDER
                        || currentApplicationLink.getLinkState() == ApplicationState.VPN_GIVE_WORK_ORDER
                        || currentApplicationLink.getLinkState() == ApplicationState.VPN_REJECT_FORWARDED_SR_COLLABORATION
                        || currentApplicationLink.getLinkState() == ApplicationState.VPN_LDGM_REJECT_LOOP_DISTANCE
                        || currentApplicationLink.getLinkState() == ApplicationState.VPN_REJECT_LOOP_DISTANCE
                ) {
                    if (!localOfficeLoop.isCompleted()
                            && localOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL
                            &&currentApplicationLink.getLinkState() != ApplicationState.VPN_LDGM_REJECT_LOOP_DISTANCE

                    ) {
                        localOfficeLoop.setDistributed(false);
                        globalService.update(localOfficeLoop);
                    }

                    if (!remoteOfficeLoop.isCompleted()
                            && remoteOfficeLoop.getLoopProvider() == VPNConstants.LOOP_BTCL
                            &&currentApplicationLink.getLinkState() != ApplicationState.VPN_LDGM_REJECT_LOOP_DISTANCE

                    ) {
                        remoteOfficeLoop.setDistributed(false);
                        globalService.update(remoteOfficeLoop);
                    }
                    globalService.update(vpnApplicationLink);
                    sendNotification(vpnApplication, vpnApplicationLink, loginDTO);
                }


            }
        }

    }


    @Transactional
    public void workOrderDoneLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        if (application.getVpnApplicationLinks().size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                applicationState = vpnApplicationLink.linkState;
                processedLinks.add(vpnApplicationLink.getId());
                try {
                    markEFRAsWorkDone(vpnApplicationLink);
                    getLinkIntoWorkOrderDoneState(application, vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

            //update of other links if necessary
            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                    vpnApplicationLink.setLinkState(applicationState);
                    getLinkIntoWorkOrderDoneState(application, vpnApplicationLink, loginDTO);
                }
            }

        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }


    @Transactional
    public void vendorWorkRejectBatchOperation(VPNApplication application, LoginDTO loginDTO) {
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {

                    if (vpnApplicationLink.getLocalOffice() != null) {

                        if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                            for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                            ) {


                                if (!efr.isCollaborationApproved()) {
                                    efr.setCompleted(false);
                                    globalService.update(efr);
                                }
                                // update local office loop

                            }
                        } else {
//                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                        }
                    } else {
//                        throw new RequestFailureException("No Local office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                    }

                    if (vpnApplicationLink.getRemoteOffice() != null) {

                        if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                            for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()) {

                                if (!efr.isCollaborationApproved()) {
                                    efr.setCompleted(false);
                                    globalService.update(efr);
                                }


                            }
                        } else {
//                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                        }
                    } else {
//                        throw new RequestFailureException("No Remote office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                    }

                    globalService.update(vpnApplicationLink);

                    //insert the comments
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }


    @Transactional
    private void getIntoLoopDistanceRejectState(VPNApplicationLink vpnApplicationLink) throws Exception {
        LocalLoop localOfficelocalLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteOfficelocalLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
        boolean localDistanceReject = false;
        boolean remoteDistanceReject = false;

        if (localOfficelocalLoop.isCompleted() || localOfficelocalLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            localDistanceReject = true;
        } else {
            if (vpnApplicationLink.getLocalOffice() != null) {

                if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                    localDistanceReject = true;
                    for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                    ) {


                        if (!efr.isLoopDistanceIsApproved()) {
                            efr.setCompleted(false);
                            globalService.update(efr);
                        }

                    }
                }
            }
        }
        if (remoteOfficelocalLoop.isCompleted() || remoteOfficelocalLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            remoteDistanceReject = true;
        } else {

            if (vpnApplicationLink.getRemoteOffice() != null) {

                if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                    remoteDistanceReject = true;
                    for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()) {
                        if (!efr.isLoopDistanceIsApproved()) {
                            efr.setCompleted(false);
                            globalService.update(efr);
                        }
                    }
                }
            }
        }
//        if (localDistanceReject && remoteDistanceReject) {
        if (localDistanceReject || remoteDistanceReject) {
            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_APPROVE_SR_COLLABORATION
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_APPROVE_FORWARDED_SR_COLLABORATION
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_APPLY_LOOP_DISTANCE_APPROVAL
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_APPLY_LOOP_DISTANCE_APPROVAL
            ) {

                globalService.update(vpnApplicationLink);
            }
        }

    }

    @Transactional
    public void vendorLoopRejectBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;

        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                applicationState = vpnApplicationLink.linkState;
                processedLinks.add(vpnApplicationLink.getId());
                try {


                    getIntoLoopDistanceRejectState(vpnApplicationLink);
                    commentService.insertComment(application.getComment(), vpnApplicationLink.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, vpnApplicationLink.getLinkState().getState());

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
        VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
        List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                        .getCondition())
                .stream()
                .filter(
                        t -> !processedLinks.contains(t.getId())
                )
                .collect(Collectors.toList());
        if (vpnApplicationLinks.size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                vpnApplicationLink.setLinkState(applicationState);
                getIntoLoopDistanceRejectState(vpnApplicationLink);
            }
        }

    }


    @Transactional
    private void calculateActualLoopDistances(List<EFR> SelectedEFR, LoginDTO loginDTO) throws Exception {
//        if(SelectedEFR.size()>0) {
        List<LocalLoop> localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(SelectedEFR.get(0).getOfficeId())
                        .getCondition()
        );
//        }
        int btclDistances = 0;
        int oldBtclDistances = 0;
        int ocDistances = 0;
        int oldOCDistances = 0;


        for (EFR efr : SelectedEFR
        ) {
            if (efr.isLoopDistanceIsApproved()) {
                if (efr.getVendorType() == 1) {
                    btclDistances += efr.getActualLoopDistance();

                } else if (efr.getVendorType() == 2) {
                    ocDistances += efr.getActualLoopDistance();

                }
            }
        }
        for (LocalLoop localLoop : localLoops) {
            if (!localLoop.isAdjusted()) {
                oldBtclDistances = (int) localLoop.getBtclDistance();
                oldOCDistances = (int) localLoop.getOcDistance();
                if (btclDistances > 0) {
                    localLoop.setBtclDistance(btclDistances);
                    localLoop.setAdjustedBTCLDistance(btclDistances - oldBtclDistances);
                }
                if (ocDistances > 0) {
                    localLoop.setOcDistance(ocDistances);
                    localLoop.setAdjustedOCDistance(ocDistances - oldOCDistances);
                }

                localLoop.setAdjustmentApprovedBy(loginDTO.getUserID());
                localLoop.setAdjusted(true);
                localLoop.setDistributed(false);
                globalService.update(localLoop);

            }

        }
    }

    @Transactional
    private void markEFRAsLoopDistanceApprovedorNotApproved(VPNApplicationLink vpnApplicationLink) throws Exception {

        if (vpnApplicationLink.getLocalOffice() != null) {
            if (vpnApplicationLink.getLocalOffice().getEfrs() != null) {
                if (vpnApplicationLink.getLocalOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getLocalOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {
//                            efr.setLoopDistanceIsApproved(true);
                            efr.setForwarded(true);
                            globalService.update(efr); // update local office loop
                        } else {
                            throw new RequestFailureException("Update Failed no efr found:");

                        }
                    }
                }
            }
        }
        if (vpnApplicationLink.getRemoteOffice() != null) {
            if (vpnApplicationLink.getRemoteOffice().getEfrs() != null) {
                if (vpnApplicationLink.getRemoteOffice().getEfrs().size() > 0) {
                    for (EFR efr : vpnApplicationLink.getRemoteOffice().getEfrs()
                    ) {
                        if (efr.getId() > 0) {

//                            efr.setLoopDistanceIsApproved(true);
                            efr.setForwarded(true);
                            globalService.update(efr); // update local office loop
                        } else {
                            throw new RequestFailureException("Update Failed no efr found:");
                        }
                    }
                }
            }
        }

    }

    @Transactional
    private void getLinkIntoLoopDistanceApprovedState(VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localEndLocalLoopWorkCompletedEFR = new ArrayList<>();
        List<EFR> remoteEndLocalLoopWorkCompletedEFR = new ArrayList<>();
        boolean allLocalEndLocalLoopWorkCompleted = false;
        boolean allRemoteEndLocalLoopWorkCompleted = false;

        LocalLoop localLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
        if (localLoop.isCompleted() || localLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            if (remoteLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !remoteLoop.isCompleted()) {

                allLocalEndLocalLoopWorkCompleted = true;
            }
        } else {

            localEndLocalLoopWorkCompletedEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                            .isReplied(true)
                            .isIgnored(false)
                            .isSelected(true)
                            .isCompleted(true)
                            .isForwarded(true)
                            .getCondition()
            );


            if (localEndLocalLoopWorkCompletedEFR != null && localEndLocalLoopWorkCompletedEFR.size() > 0) {
                allLocalEndLocalLoopWorkCompleted = true;
                calculateActualLoopDistances(localEndLocalLoopWorkCompletedEFR, loginDTO);
            }

        }


        if (remoteLoop.isCompleted() || remoteLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            if (localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localLoop.isCompleted()) {

                allRemoteEndLocalLoopWorkCompleted = true;
            }
        } else {

            remoteEndLocalLoopWorkCompletedEFR = globalService.getAllObjectListByCondition(EFR.class,
                    new EFRConditionBuilder()
                            .Where()
                            .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                            .isReplied(true)
                            .isIgnored(false)
                            .isSelected(true)
                            .isCompleted(true)
                            .isForwarded(true)
                            .getCondition()
            );
            if (remoteEndLocalLoopWorkCompletedEFR != null && remoteEndLocalLoopWorkCompletedEFR.size() > 0) {

                allRemoteEndLocalLoopWorkCompleted = true;
                calculateActualLoopDistances(remoteEndLocalLoopWorkCompletedEFR, loginDTO);

            }
        }

        if (allLocalEndLocalLoopWorkCompleted && allRemoteEndLocalLoopWorkCompleted) {
            VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            if (currentApplicationLink.getLinkState() == ApplicationState.VPN_APPROVE_SR_COLLABORATION
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_APPLY_LOOP_DISTANCE_APPROVAL
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_FORWARD_APPLY_LOOP_DISTANCE_APPROVAL
                    || currentApplicationLink.getLinkState() == ApplicationState.VPN_APPROVE_FORWARDED_SR_COLLABORATION
            ) {
                globalService.update(vpnApplicationLink);
            }
        }

    }


    @Transactional
    public void LoopDistanceApproveLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<Long> processedLinks = new ArrayList<>();
        ApplicationState applicationState = null;
        if (application.getVpnApplicationLinks().size() > 0) {

            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                applicationState = vpnApplicationLink.linkState;
                processedLinks.add(vpnApplicationLink.getId());
                try {
                    markEFRAsLoopDistanceApprovedorNotApproved(vpnApplicationLink);
                    getLinkIntoLoopDistanceApprovedState(vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(
                            application.getComment(),
                            vpnApplicationLink.getId(),
                            loginDTO.getUserID(),
                            ModuleConstants.Module_ID_VPN,
                            vpnApplicationLink.getLinkState().getState()
                    );

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

            //update of other links if necessary
            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                            .Where()
                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                            .getCondition())
                    .stream()
                    .filter(
                            t -> !processedLinks.contains(t.getId())
                    )
                    .collect(Collectors.toList());
            if (vpnApplicationLinks.size() > 0) {

                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
                    vpnApplicationLink.setLinkState(applicationState);
                    getLinkIntoLoopDistanceApprovedState(vpnApplicationLink, loginDTO);
                }
            }

        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

//    @Transactional
//    public void CDGMLoopDistanceApproveLogicOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
//        List<Long> processedLinks = new ArrayList<>();
//        ApplicationState applicationState = null;
//        if (application.getVpnApplicationLinks().size() > 0) {
//
//            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
//                applicationState = vpnApplicationLink.linkState;
//                processedLinks.add(vpnApplicationLink.getId());
//                try {
//                    markEFRAsLoopDistanceApprovedorNotApproved(vpnApplicationLink);
//                    getLinkIntoLoopDistanceApprovedState(vpnApplicationLink, loginDTO);
//
//                } catch (Exception e) {
//                    throw new RequestFailureException("Update Failed :" + e.getMessage());
//                }
//            }
//
//            //update of other links if necessary
//            VPNApplication vpnApplication = (VPNApplication) applicationService.getApplicationByApplicationId(application.getApplicationId());
//            List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
//                    new VPNApplicationLinkConditionBuilder()
//                            .Where()
//                            .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
//                            .getCondition())
//                    .stream()
//                    .filter(
//                            t -> !processedLinks.contains(t.getId())
//                    )
//                    .collect(Collectors.toList());
//            if (vpnApplicationLinks.size() > 0) {
//
//                for (VPNApplicationLink vpnApplicationLink : vpnApplicationLinks) {
//                    vpnApplicationLink.setLinkState(applicationState);
//                    getLinkIntoLoopDistanceApprovedState(vpnApplicationLink, loginDTO);
//                }
//            }
//
//        } else {
//            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
//        }
//    }

    //endregion


    @Transactional
    public void generateAdviceNote(VPNApplication application, JsonArray jsonArray, LoginDTO loginDTO) throws Exception {
        if (application.getVpnApplicationLinks().size() > 0) {

            // Below line will modify the vpn application . specifically the vpn application links; they will hold advice note id which was previously null.

            ServiceDAOFactory.getService(AdviceNoteGenerationFacade.class).generateGenericAdviceNote(ModuleConstants.Module_ID_VPN, loginDTO, application, jsonArray);
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {

                    globalService.update(vpnApplicationLink);
                    sendNotification(application, vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(
                            application.getComment(),
                            vpnApplicationLink.getId(),
                            loginDTO.getUserID(),
                            ModuleConstants.Module_ID_VPN,
                            vpnApplicationLink.getLinkState().getState()
                    );
                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }


    //region search page functions
    @SuppressWarnings("rawtypes")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {
        return applicationService.getIDsWithSearchCriteria(searchCriteria, loginDTO, ModuleConstants.Module_ID_VPN);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//        List<Application> list = (List<Application>) applicationService.getDTOs(recordIDs);
//        List<VPNApplication> list = (List<VPNApplication>) vpnApplicationDAO.getApplicationListByIDList((List<Long>) recordIDs);
        LoginDTO loginDTO = (LoginDTO) objects[0];

        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];

        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);


        List<VPNApplication> vpnApplications = applicationService
                .getAllApplicationsByAppIds((List<Long>) recordIDs, VPNApplication.class);
        if (vpnApplications.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<VPNApplicationLink>> mapOfVpnApplicationLinksByVpnApplicationId = globalService.getAllObjectListByCondition(
                VPNApplicationLink.class, new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdIn(vpnApplications
                                .stream()
                                .map(VPNApplication::getVpnApplicationId)
                                .collect(Collectors.toList())
                        )
                        .getCondition()
        )
                .stream()
                .collect(Collectors.groupingBy(VPNApplicationLink::getVpnApplicationId));


        List<Application> permittedVpnApplicationList = vpnApplications.stream()
                .peek(t -> {
                    StringBuilder concatenatedState = new StringBuilder();
                    boolean hasPermission = false;
                    try {

                        List<VPNApplicationLink> vpnApplicationLinks = mapOfVpnApplicationLinksByVpnApplicationId
                                .getOrDefault(t.getVpnApplicationId(), Collections.emptyList());
                        for (int i = 0; i < vpnApplicationLinks.size(); i++) {
                            VPNApplicationLink vpnApplicationLink = vpnApplicationLinks.get(i);

                            FlowState flowState = FlowRepository.getInstance().getFlowStateByFlowStateId(vpnApplicationLink.getLinkState().getState());
                            //new FlowService().getStateById(vpnApplicationLinks.get(i).getLinkState().getState());
                            List<FlowState> nextStates = FlowRepository.getInstance().getNextStatesByCurrentStateAndRoleId(flowState.getId(), roleId);
                            //new FlowService().getNextStatesFromStateRole(vpnApplicationLinks.get(i).getLinkState().getState(), roleId);

                            if (nextStates.size() > 0) {
                                //todo : search filter for ldgm
//                                if (userDTO.getRoleID() == 22020) {
//
//
////                                    LocalLoop localOfficeLoop=globalService.findByPK(
////                                            LocalLoop.class,
////                                            vpnApplicationLinks.get(i-1).getRemoteOfficeLoopId());
////                                    boolean isRequestedEFR=false;
////                                    List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
////                                            new EFRConditionBuilder()
////                                                    .Where()
////                                                    .popIdEquals()
////                                                    .applicationIdEquals(t.getApplicationId())
////                                                    .getCondition());
////                                    if(efrs.size()>0){
////                                        isRequestedEFR=true;
////                                    }
//                                    hasPermission = true;
//
//
//                                } else {

                                hasPermission = true;
//                                }
                            }
                            concatenatedState.append("<p style= color:")
                                    .append(flowState.getColor())
                                    .append(">Link")
                                    .append(i + 1)
                                    .append(" (")
                                    .append(vpnApplicationLinks.get(i).getId())
                                    .append(") ")
                                    .append(":")
                                    .append(flowState.getViewDescription())
                                    .append("</p> ");
                        }


                    } catch (Exception e) {
                        log.fatal(e.getMessage());
                    }
                    t.setHasPermission(hasPermission);
                    t.setStateDescription(concatenatedState.toString());
//                    t.setColor(fs.getColor());
                })
                .collect(Collectors.toList());

        if (loginDTO.getUserID() > 0) {
            UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());


            // region vendor filter
            if (!userDTO.isBTCLPersonnel()) {
                //vendor can't see other application that his action not needed
                permittedVpnApplicationList = permittedVpnApplicationList
                        .stream()
                        .filter(Application::isHasPermission)
                        .collect(Collectors.toList());
                List<Application> tempList = new ArrayList<>();
                for (Application application : permittedVpnApplicationList
                ) {
                    List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .vendorIDEquals(loginDTO.getUserID())
                                    .applicationIdEquals(application.getApplicationId())
                                    .getCondition()

                    )
                            .stream()
                            .filter(t -> !(t.isCompleted()))
                            //complete work order vendor will not see any more if his work is done
                            .collect(Collectors.toList()
                            );

                    efrs = efrs
                            .stream()
                            .filter(t ->
                                    !t.isReplied()
                                            //if not replied then vendor will see app
                                            ||
                                            (t.isReplied() && !t.isCompleted() && t.isWorkOrdered()))
                            //if replied but not completed then vendor will see app
                            .collect(Collectors.toList());


                    if (efrs.size() > 0) {
//                        for (EFR efr :
//                                efrs) {
//
//                            if(efr.getApplicationId()==application.getApplicationId()){
//
//                                tempList.add(application);
//                            }
//                        }

                        tempList.add(application);


                    }

                }

                permittedVpnApplicationList = tempList;


                //todo: vendor will not take action if he already taken action while other vendor has not taken action yet

            }


            //endregion

        }
        Comparator<Application> compareByHasPermissionAndApplicationId =
                Comparator.comparing(Application::isHasPermission, Comparator.reverseOrder())
                        .thenComparing(Application::getApplicationId, Comparator.reverseOrder());

        return permittedVpnApplicationList.stream()
                .sorted(compareByHasPermissionAndApplicationId)
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());

    }

    //endregion


    //region insert and update method


    public void setLinkName(VPNApplicationLink vpnApplicationLink) {
        vpnApplicationLink.setLinkName(vpnApplicationLink.getLocalOffice().getOfficeName()
                + "--> "
                + vpnApplicationLink.getRemoteOffice().getOfficeName()
                + ": "
                + vpnApplicationLink.getLinkBandwidth()
                + " Mbps");
    }

    private void setAppropriateLinkState(VPNApplicationLink vpnApplicationLink) {
        boolean isLocalOfficeLoopProvidedByClient = false;
        boolean isRemoteOfficeLoopProvidedByClient = false;
        if (vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            isLocalOfficeLoopProvidedByClient = true;
        }
        if (vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            isRemoteOfficeLoopProvidedByClient = true;
        }

        if (isLocalOfficeLoopProvidedByClient && isRemoteOfficeLoopProvidedByClient) {
            vpnApplicationLink.setLinkState(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED);

        } else if (vpnApplicationLink.isLocalOfficeLoopNotNeeded && isRemoteOfficeLoopProvidedByClient) {
            vpnApplicationLink.setLinkState(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED);
        } else if (vpnApplicationLink.isRemoteOfficeLoopNotNeeded && isLocalOfficeLoopProvidedByClient) {
            vpnApplicationLink.setLinkState(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED);

        } else if (vpnApplicationLink.isLocalOfficeLoopNotNeeded && vpnApplicationLink.isRemoteOfficeLoopNotNeeded) {
            vpnApplicationLink.setLinkState(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED);
        } else {
            vpnApplicationLink.setLinkState(ApplicationState.VPN_SUBMITTED);

        }
    }

    private void populateVPNApplicationCommonInfo(VPNApplication application, ApplicationType applicationType) {
        application.setClassName(VPNApplication.class.getCanonicalName());
        application.setApplicationState(ApplicationState.VPN_SUBMITTED); // TODO need to be changed
        application.setApplicationType(applicationType); // TODO need to be changed
        application.setModuleId(ModuleConstants.Module_ID_VPN);
    }

    @Transactional
    public void insertBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        application.setClassName(VPNApplication.class.getCanonicalName());
        application.setApplicationState(ApplicationState.VPN_SUBMITTED); // TODO need to be changed
        application.setSubmissionDate(System.currentTimeMillis());
        application.setApplicationType(ApplicationType.VPN_NEW_CONNECTION); // TODO need to be changed
        application.setModuleId(ModuleConstants.Module_ID_VPN);

        //insert all offices
        int index = 0;
        long localOfficeId = 0;
        long localOfficeLoopId = 0;
        boolean localOfficeLoopNotNeeded = false;
        for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
            try {
                //save office
                if (index == 0) { // save local end only once in office table
                    if (!(vpnApplicationLink.getLocalOffice().getId() > 0)) {

                        globalService.save(vpnApplicationLink.getLocalOffice()); // save local office
                        // get the local Office Id
                    }
                    localOfficeId = vpnApplicationLink.getLocalOffice().getId();

                    if (!(vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getId() > 0)) {
                        vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).setOfficeId(localOfficeId); // local office
                        globalService.save(vpnApplicationLink.getLocalOffice().getLocalLoops().get(0)); // local office loop

                    } else {
                        localOfficeLoopNotNeeded = true;
                    }
                    localOfficeLoopId = vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getId();

                    //
                }
                vpnApplicationLink.setLocalOfficeId(localOfficeId);
                vpnApplicationLink.setLocalOfficeLoopId(localOfficeLoopId);
                vpnApplicationLink.setLocalOfficeLoopNotNeeded(localOfficeLoopNotNeeded);

                if (!(vpnApplicationLink.getRemoteOffice().getId() > 0)) {

                    globalService.save(vpnApplicationLink.getRemoteOffice());
                }
                vpnApplicationLink.setRemoteOfficeId(vpnApplicationLink.getRemoteOffice().getId());


                //save loop


                if (!(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getId() > 0)) {
                    vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).setOfficeId(vpnApplicationLink.getRemoteOffice().getId());
                    globalService.save(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0));
                } else {
                    vpnApplicationLink.setRemoteOfficeLoopNotNeeded(true);

                }
                vpnApplicationLink.setRemoteOfficeLoopId(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getId());


                index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //insert the application
        insert(application);

        //save all the links

        application.getVpnApplicationLinks().forEach(l -> {


            setAppropriateLinkState(l);
            setLinkName(l);
            l.setVpnApplicationId(application.getVpnApplicationId());
            try {
                globalService.save(l);
                sendNotification(application, l, loginDTO);
                commentService.insertComment(
                        application.getComment(),
                        l.getId(),
                        loginDTO.getUserID(),
                        ModuleConstants.Module_ID_VPN,
                        l.getLinkState().getState()
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    @Transactional
    public void insertOrUpdateBatchOperationForShift(VPNApplication application, LoginDTO loginDTO) throws Exception {


        populateVPNApplicationCommonInfo(application, ApplicationType.VPN_SHIFT_LINK);

        //insert all offices
        long localOfficeId = 0;
        long localOfficeLoopId = 0;
        boolean localOfficeLoopNotNeeded = false;
        VPNApplicationLink vpnApplicationLink = application.getVpnApplicationLinks().get(0);

        try {
            //save office
            if (!(vpnApplicationLink.getLocalOffice().getId() > 0)) {

                globalService.save(vpnApplicationLink.getLocalOffice()); // save local office
                // get the local Office Id
            }
            localOfficeId = vpnApplicationLink.getLocalOffice().getId();

            if (!(vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getId() > 0)) {
                vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).setOfficeId(localOfficeId); // local office
                globalService.save(vpnApplicationLink.getLocalOffice().getLocalLoops().get(0)); // local office loop

            } else {
                localOfficeLoopNotNeeded = true;
            }
            localOfficeLoopId = vpnApplicationLink.getLocalOffice().getLocalLoops().get(0).getId();

            //
            vpnApplicationLink.setLocalOfficeId(localOfficeId);
            vpnApplicationLink.setLocalOfficeLoopId(localOfficeLoopId);
            vpnApplicationLink.setLocalOfficeLoopNotNeeded(localOfficeLoopNotNeeded);

            if (!(vpnApplicationLink.getRemoteOffice().getId() > 0)) {

                globalService.save(vpnApplicationLink.getRemoteOffice());
            }
            vpnApplicationLink.setRemoteOfficeId(vpnApplicationLink.getRemoteOffice().getId());


            //save loop


            if (!(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getId() > 0)) {
                vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).setOfficeId(vpnApplicationLink.getRemoteOffice().getId());
                globalService.save(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0));
            } else {
                vpnApplicationLink.setRemoteOfficeLoopNotNeeded(true);

            }
            vpnApplicationLink.setRemoteOfficeLoopId(vpnApplicationLink.getRemoteOffice().getLocalLoops().get(0).getId());


            //insert the application
            insert(application);

            //save all the links


            setAppropriateLinkState(vpnApplicationLink);
            setLinkName(vpnApplicationLink);
            vpnApplicationLink.setVpnApplicationId(application.getVpnApplicationId());


            globalService.save(vpnApplicationLink);
            sendNotification(application, vpnApplicationLink, loginDTO);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @param application     VPNApplication
     * @param applicationType ApplicationType
     * @param loginDTO
     * @throws Exception RequestFailureException
     *                   <p>
     *                   save a application
     *                   save a vpn application for bandwidth modification.
     *                   save a vpn application link
     */
    @Transactional
    public void insertOrUpdateBatchOperationForBandwidthRevise(VPNApplication application, ApplicationType applicationType, LoginDTO loginDTO) throws Exception {
        populateVPNApplicationCommonInfo(application, applicationType);

        // application save; vpn application save
        VPNApplicationLink link = application.getVpnApplicationLinks().get(0);
//        application.
        if (application.getApplicationId() > 0) {
            globalService.update(application);
            if (applicationType == ApplicationType.VPN_UPGRADE_CONNECTION) {
                link.setLinkState(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED);
            }
            globalService.update(link);

        } else {
            applicationService.saveApplication(application);
            link.setLinkState(
                    applicationType == ApplicationType.VPN_DOWNGRADE_CONNECTION
                            ?
                            ApplicationState.VPN_DOWNGRADE_APPLICATION_SUBMITTED
                            :
                            ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED
            );
            link.setVpnApplicationId(application.getVpnApplicationId());
            setLinkName(link);

            globalService.save(link);
            sendNotification(application, link, loginDTO);
        }
        // link save

        //insert the comment
        commentService.insertComment(application.getComment(), link.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, link.getLinkState().getState());
    }

    @Transactional
    public void updateAndCreateLoopForUpgradeOperation(VPNApplication application, LoginDTO loginDTO) {
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {
                    boolean isNewLoopNeeded = false;

                    if (vpnApplicationLink.getLinkState() == ApplicationState.VPN_WITHOUT_LOOP_IFR_RESPONSE) {
                        if (vpnApplicationLink.getLocalOffice() != null) {
                            globalService.update(vpnApplicationLink.getLocalOffice());
                            // update local office
                            if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                                for (LocalLoop localLoop : vpnApplicationLink.getLocalOffice().getLocalLoops()) {
                                    if (localLoop.isChangePOP()) {
                                        LocalLoop newLoop = new LocalLoop();
                                        newLoop.setPopId(localLoop.getPopId());
                                        newLoop.setOfficeId(localLoop.getOfficeId());
                                        newLoop.setIfrFeasibility(localLoop.isIfrFeasibility());
                                        newLoop.setLoopProvider(VPNConstants.LOOP_BTCL);
                                        if (newLoop.getDistrictId() == 0) {
                                            // first time for district id
                                            newLoop.setDistrictId(ServiceDAOFactory.getService(InventoryService.class).getDistrictOfInventoryItem(newLoop.getPopId()));
                                        }
                                        globalService.save(newLoop);
                                        vpnApplicationLink.setLocalOfficeLoopId(newLoop.getId());
                                        isNewLoopNeeded = true;
                                    } else {
//                                        if (!(localLoop.isCompleted())) {

                                            globalService.update(localLoop); // update local office loop
//                                        }
                                    }


                                }
                            } else {
//                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                            }
                        } else {
//                        throw new RequestFailureException("No Local office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                        }

                        if (vpnApplicationLink.getRemoteOffice() != null) {
                            globalService.update(vpnApplicationLink.getRemoteOffice());
                            if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                                for (LocalLoop localLoop : vpnApplicationLink.getRemoteOffice().getLocalLoops()) {
                                    if (localLoop.isChangePOP()) {
                                        LocalLoop newLoop = new LocalLoop();
                                        newLoop.setPopId(localLoop.getPopId());
                                        newLoop.setLoopProvider(VPNConstants.LOOP_BTCL);
                                        newLoop.setOfficeId(localLoop.getOfficeId());
                                        newLoop.setIfrFeasibility(localLoop.isIfrFeasibility());
                                        if (newLoop.getDistrictId() == 0) {
                                            // first time for district id
                                            newLoop.setDistrictId(ServiceDAOFactory.getService(InventoryService.class).getDistrictOfInventoryItem(newLoop.getPopId()));
                                        }
                                        globalService.save(newLoop);
                                        vpnApplicationLink.setRemoteOfficeLoopId(newLoop.getId());
                                        isNewLoopNeeded = true;

                                    } else {
//                                        if (!(localLoop.isCompleted())) {

                                            globalService.update(localLoop); // update local office loop
//                                        }
                                    }
//                                }
                                }
                            } else {
//                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                            }
                        } else {
//                        throw new RequestFailureException("No Remote office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                        }

                        if (isNewLoopNeeded) {
                            vpnApplicationLink.setLinkState(ApplicationState.VPN_IFR_RESPONDED);
                        }


                        globalService.update(vpnApplicationLink);
                        sendNotification(application, vpnApplicationLink, loginDTO);

                    } else {
                        updateBatchOperation(application, loginDTO);
                    }
                    commentService.insertComment(
                            application.getComment(),
                            vpnApplicationLink.getId(),
                            loginDTO.getUserID(),
                            ModuleConstants.Module_ID_VPN,
                            vpnApplicationLink.getLinkState().getState()
                    );


                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }
        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

    @Transactional
    public void updateBatchOperation(VPNApplication application, LoginDTO loginDTO) {
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {

                    if (vpnApplicationLink.getLocalOffice() != null) {
                        globalService.update(vpnApplicationLink.getLocalOffice());
                        // update local office
                        if (vpnApplicationLink.getLocalOffice().getLocalLoops().size() > 0) {
                            for (LocalLoop localLoop : vpnApplicationLink.getLocalOffice().getLocalLoops()
                            ) {
                                if (!(localLoop.getDistrictId() > 0)) {
                                    if (localLoop.getPopId() > 0) {
                                        // if the link has pop id
                                        if (localLoop.getDistrictId() == 0) {
                                            // first time for district id
                                            localLoop.setDistrictId(ServiceDAOFactory.getService(InventoryService.class).getDistrictOfInventoryItem(localLoop.getPopId()));
                                        }
                                    }
                                }


//                                if (!(localLoop.isCompleted())) {

                                if(localLoop.isIfrFeasibility()&&localLoop.getPopId()<=0){
                                    throw new RequestFailureException(" You need to select pop for IFR Response positive");
                                }else{
                                    globalService.update(localLoop); // update local office loop

                                }
//                                }
                            }
                        } else {
                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                        }
                    } else {
                        throw new RequestFailureException("No Local office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                    }

                    if (vpnApplicationLink.getRemoteOffice() != null) {
                        globalService.update(vpnApplicationLink.getRemoteOffice());
                        if (vpnApplicationLink.getRemoteOffice().getLocalLoops().size() > 0) {
                            for (LocalLoop localLoop : vpnApplicationLink.getRemoteOffice().getLocalLoops()) {
                                if (localLoop.getPopId() > 0) {
                                    // if the link has pop id
                                    if (localLoop.getDistrictId() == 0) {
                                        // first time for district id
                                        localLoop.setDistrictId(ServiceDAOFactory.getService(InventoryService.class).getDistrictOfInventoryItem(localLoop.getPopId()));
                                    }
                                }



                                if(localLoop.isIfrFeasibility()&&localLoop.getPopId()<=0){
                                    throw new RequestFailureException(" You need to select pop for IFR Response positive");
                                }else{
                                    globalService.update(localLoop); // update local office loop

                                }
//                                if(!localLoop.isCompleted()){

//                                }
                            }
                        } else {
                            throw new RequestFailureException("No Local Loop Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());
                        }
                    } else {
                        throw new RequestFailureException("No Remote office Found for Update with Office ID" + vpnApplicationLink.getLocalOffice().getId());

                    }

                    globalService.update(vpnApplicationLink);

                    sendNotification(application, vpnApplicationLink, loginDTO);

                    //insert the comments
                    commentService.insertComment(
                            application.getComment(),
                            vpnApplicationLink.getId(),
                            loginDTO.getUserID(),
                            ModuleConstants.Module_ID_VPN,
                            vpnApplicationLink.getLinkState().getState()
                    );


                    sendNotification(application,vpnApplicationLink,loginDTO);
                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }


        } else {
            throw new RequestFailureException("No Application Link Found for Update with ID" + application.getApplicationId());
        }
    }

    @Transactional
    public void insert(VPNApplication vpnApplicationObject) throws Exception {
        applicationService.saveApplication(vpnApplicationObject);
    }

    @Transactional
    public void updateApplicaton(VPNApplication vpnApplication) throws Exception {
        vpnApplicationDAO.updateApplicaton(vpnApplication);
    }

    @Transactional
    public void updateApplicatonState(long applicatonID, int applicationState) throws Exception {
//        vpnApplicationDAO.updateApplicatonState(applicatonID, applicationState);
        applicationService.updateApplicationState(applicatonID, applicationState);
    }

    //endregion

    //region get methods
    @Transactional
    public ApplicationType getApplicationTypeByApplicationID(long applicationID) throws Exception {
        VPNApplication vpnApplication = vpnApplicationDAO.getApplicationById(applicationID);
        return vpnApplication.getApplicationType();
//        return 0;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getVPNApplicationByApplicationIdWithoutVPNLinks(long applicationId) throws Exception {
        return (VPNApplication) applicationService.getApplicationByApplicationId(applicationId);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getVPNApplicationByDemandNoteIdWithoutVPNLinks(long demandNoteId) throws Exception {

        return vpnApplicationDAO.getVPNApplicationsByDemandNoteId(demandNoteId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No VPN Application Found with Demand Note id " + demandNoteId));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getVPNApplicationByDemandNoteId(long demandNoteId) throws Exception {
        List<VPNApplicationLink> links = getVPNApplicationLinksByDemandNoteId(demandNoteId);
        if (links.isEmpty()) {
            throw new NoDataFoundException("No VPN Link Info found with bill id " + demandNoteId);
        }
        VPNApplication vpnApplication = getVPNApplicationByVPNApplicationIdWithoutVPNLinks(links.get(0).getVpnApplicationId());

        vpnApplication.setVpnApplicationLinks(links);
        return vpnApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    private List<VPNApplication> getApplicationByConnectionID(long connection) throws Exception {
        List<VPNApplication> vpnApplications = vpnApplicationDAO.getVPNApplicationByConnectionID(connection);
        if (vpnApplications == null) {
            throw new RequestFailureException("No Application found with connection ID " + connection);
        }
        return vpnApplications;
    }

    @Transactional
    public VPNApplicationLink getVPNApplicationLinkByApplicationLinkId(long applicationLinkId) throws Exception {
        VPNApplicationLink vpnApplicationLink = globalService.findByPK(VPNApplicationLink.class, applicationLinkId);
        vpnApplicationLink.setLocalOffice(globalService.findByPK(Office.class, vpnApplicationLink.getLocalOfficeId()));
        vpnApplicationLink.setRemoteOffice(globalService.findByPK(Office.class, vpnApplicationLink.getRemoteOfficeId()));
        vpnApplicationLink.getLocalOffice().setLocalLoops(
                globalService.getAllObjectListByCondition(LocalLoop.class,
                        new LocalLoopConditionBuilder()
                                .Where()
                                .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                                .getCondition()
                ));

        vpnApplicationLink.getRemoteOffice().setLocalLoops(globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                        .getCondition()
        ));

        return vpnApplicationLink;
    }

    private List<VPNApplicationLink> getVPNLinksByVpnApplicationId(long vpnApplicationId) throws Exception {
        List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .vpnApplicationIdEquals(vpnApplicationId)
                        .getCondition()

        );
        populateVolatileInfo(vpnApplicationLinks);

        return vpnApplicationLinks;
    }

    private void populateVolatileInfo(List<VPNApplicationLink> vpnApplicationLinks) throws Exception {
        // first attempt
        if(vpnApplicationLinks.isEmpty()) {
            return;
        }else {
            long localOfficeId = vpnApplicationLinks.get(0).getLocalOfficeId();
            Office localOffice = globalService.findByPK(Office.class, localOfficeId);
            List<LocalLoop> loopsOfLocalOffice = globalService.getAllObjectListByCondition(LocalLoop.class,
                    new LocalLoopConditionBuilder()
                            .Where()
                            .officeIdEquals(localOfficeId)
                            .getCondition()
            );
            vpnApplicationLinks.forEach(t -> {

                try {
                    t.setLocalOffice(localOffice);
                    t.setRemoteOffice(globalService.findByPK(Office.class, t.getRemoteOfficeId()));
                    t.getLocalOffice().setLocalLoops(loopsOfLocalOffice);
                    t.getRemoteOffice().setLocalLoops(globalService.getAllObjectListByCondition(LocalLoop.class,
                            new LocalLoopConditionBuilder()
                                    .Where()
                                    .officeIdEquals(t.getRemoteOfficeId())
                                    .getCondition()
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

        // first attempt end


    }

    @Transactional(transactionType = TransactionType.READONLY)
    private List<VPNApplicationLink> getVPNApplicationLinksByDemandNoteId(long demandNoteId) throws Exception {
        List<VPNApplicationLink> vpnApplicationLinks = globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                new VPNApplicationLinkConditionBuilder()
                        .Where()
                        .demandNoteIdEquals(demandNoteId)
                        .getCondition()

        );
        populateVolatileInfo(vpnApplicationLinks);
        return vpnApplicationLinks;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getApplicationByApplicationId(long appId) throws Exception {
        VPNApplication vpnApplication = getVPNApplicationByApplicationIdWithoutVPNLinks(appId);
        List<VPNApplicationLink> links = getVPNLinksByVpnApplicationId(vpnApplication.getVpnApplicationId());
        vpnApplication.setVpnApplicationLinks(links);
        return vpnApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getApplicationByClientId(long clientId) throws Exception {
        VPNApplication vpnApplication = getVPNApplicationByApplicationIdWithoutVPNLinks(clientId);
        List<VPNApplicationLink> links = getVPNLinksByVpnApplicationId(vpnApplication.getVpnApplicationId());
        vpnApplication.setVpnApplicationLinks(links);
        return vpnApplication;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNApplication> getListOfVpnApplicationByApplicationIdWithLink(List<Application> applications) throws Exception {
        Map<Long, Application> mapApplication = applications.stream()
                .collect(Collectors.toMap(Application::getApplicationId, Function.identity()));

        List<VPNApplication> vpnApplications = globalService.getAllObjectListByCondition(VPNApplication.class)
                .stream()
                .map(t -> {
                    Application application = mapApplication.getOrDefault(t.getApplicationId(), null);
                    if (application != null) {
                        t.setClientId(application.getClientId());
                        t.setApplicationState(application.getApplicationState());
                        t.setApplicationType(application.getApplicationType());
                        t.setUserId(application.getUserId());
                        t.setModuleId(application.getModuleId());
                        return t;
                    } else {
                        return null;
                    }


                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<VPNApplicationLink> links = globalService.getAllObjectListByCondition(VPNApplicationLink.class);

        vpnApplications.forEach(vpnApplication ->
        {
            List<VPNApplicationLink> applicationLinks = new ArrayList<>();
            links.forEach(
                    t -> {
                        if (t.getVpnApplicationId() == vpnApplication.getVpnApplicationId()) {
                            applicationLinks.add(t);

                        }

                    }
            );

            vpnApplication.setVpnApplicationLinks(applicationLinks);
        });

        return vpnApplications;


    }


    private VPNApplication getApplicationWithLinksBeforeDemandNoteGenerationState(long appId) throws Exception {
        VPNApplication vpnApplication = getApplicationByApplicationId(appId);
        List<VPNApplicationLink> curatedLinks = vpnApplication.getVpnApplicationLinks()
                .stream()
                .filter(t -> ApplicationState.isPriorVPNDemandNoteState(t.getLinkState()))
                .collect(Collectors.toList());

        vpnApplication.setVpnApplicationLinks(curatedLinks);
        return vpnApplication;
    }


    //endregion

    //region flow state builder

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getActionList(Integer state, Integer roleId) {
        List<FlowState> flowStates = FlowRepository.getInstance().getNextStatesByCurrentStateAndRoleId(state, roleId);

        for (FlowState flowState : flowStates) {
            flowState.setUrl(VPNConstants.stateMap.get(flowState.getId()).getUrl());
            flowState.setModal(VPNConstants.stateMap.get(flowState.getId()).getModal());
            flowState.setView(VPNConstants.stateMap.get(flowState.getId()).getView());
            flowState.setRedirect(VPNConstants.stateMap.get(flowState.getId()).getRedirect());
            flowState.setParam(VPNConstants.stateMap.get(flowState.getId()).getParam());

//            ApplicationState applicationState = new ApplicationState();

            flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());
        }
        return flowStates;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public FlowState getCurrentState(Integer state) {
        FlowState flowState = FlowRepository.getInstance().getFlowStateByFlowStateId(state);
        flowState.setUrl(VPNConstants.stateMap.get(state).getUrl());
        flowState.setModal(VPNConstants.stateMap.get(state).getModal());
        flowState.setView(VPNConstants.stateMap.get(state).getView());
        flowState.setRedirect(VPNConstants.stateMap.get(state).getRedirect());
        flowState.setRedirect(VPNConstants.stateMap.get(state).getParam());
        flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());


        return flowState;
    }

    /***
     *
     * @param id either vpn link id / application id depends on isGlobal
     * @param isGlobal if true id = application id, false = vpn link id
     * @return VPNApplication instance populated with either one specific vpn app link or all vpn app links.
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public VPNApplication getAppropriateVPNApplication(long id, boolean isGlobal) throws Exception {
        VPNApplication application;
        if (isGlobal) {
            application = getApplicationWithLinksBeforeDemandNoteGenerationState(id);
        } else {
            VPNApplicationLink link = getVPNApplicationLinkByApplicationLinkId(id);
            application = getVPNApplicationByVPNApplicationIdWithoutVPNLinks(link.getVpnApplicationId());
            application.setVpnApplicationLinks(Collections.singletonList(link));
        }
        return application;
    }

    private VPNApplication getVPNApplicationByVPNApplicationIdWithoutVPNLinks(long vpnApplicationId) throws Exception {
        VPNApplication vpnApplication = vpnApplicationDAO.getFullyPopulatedVPNApplication(vpnApplicationId);
        if (vpnApplication == null) {
            throw new NoDataFoundException("No VPN Application found for vpn application id " + vpnApplicationId);
        }
        return vpnApplication;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isLocalEndLoopChargePreviouslyCalculated(long vpnApplicationId) throws Exception {
        List<VPNLoopChargeDiscountEligibility> discountEligibilityList = getEligibleListByVpnApplicationId(vpnApplicationId);
        return !discountEligibilityList.isEmpty();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean isLocalEndLoopChargePreviouslyCalculatedByLinkId(long vpnApplicationLinkId) throws Exception {
        List<VPNLoopChargeDiscountEligibility> list = globalService.getAllObjectListByCondition(
                VPNLoopChargeDiscountEligibility.class, new VPNLoopChargeDiscountEligibilityConditionBuilder()
                .Where()
                .vpnApplicationLinkIdEquals(vpnApplicationLinkId)
                .getCondition()
        );
        return !list.isEmpty();
    }

    public List<VPNLoopChargeDiscountEligibility> getEligibleListByVpnApplicationId(long vpnApplicationId) throws Exception {
        return globalService.getAllObjectListByCondition(
                VPNLoopChargeDiscountEligibility.class, new VPNLoopChargeDiscountEligibilityConditionBuilder()
                        .Where()
                        .vpnApplicationIdEquals(vpnApplicationId)
                        .getCondition()
        );
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNLoopChargeDiscountEligibility> getLocalLoopDiscountEligibilityByInvoiceId(long invoiceId) throws Exception {
        return globalService.getAllObjectListByCondition(
                VPNLoopChargeDiscountEligibility.class, new VPNLoopChargeDiscountEligibilityConditionBuilder()
                        .Where()
                        .demandNoteIdEquals(invoiceId)
                        .getCondition()
        );
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNLoopChargeDiscountEligibility> getLoopEligibilitiesByLinks(List<VPNApplicationLink> links) throws Exception {
        if(links.isEmpty()) return Collections.emptyList();

        return globalService.getAllObjectListByCondition(
                VPNLoopChargeDiscountEligibility.class, new VPNLoopChargeDiscountEligibilityConditionBuilder()
                        .Where()
                        .vpnApplicationLinkIdIn(links.stream().map(VPNApplicationLink::getId).collect(Collectors.toList()))
                        .getCondition()
        );
    }

    @Transactional
    public void insertTdBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        VPNApplication vpnApplication = new VPNApplication();//new NIXReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long clientID = jsonObject.get("client") != null ? jsonObject.get("client").getAsJsonObject().get("key").getAsLong() : 0;
        if (clientID == 0) {
            clientID = jsonObject.get("clientID").getAsLong();
        }
        long suggestedDate = jsonObject.get("suggestedDate") != null ? jsonObject.get("suggestedDate").getAsLong() : 0;

        vpnApplication.setClientId(clientID);
        vpnApplication.setSuggestedDate(suggestedDate);
        vpnApplication.setApplicationType(ApplicationType.VPN_TD);
        vpnApplication.setApplicationState(ApplicationState.VPN_TD_APPLICATION_SUBMITTED);
        vpnApplication.setCreatedDate(System.currentTimeMillis());
        vpnApplication.setLastModificationTime(System.currentTimeMillis());
        vpnApplication.setClassName(VPNApplication.class.getName());
        vpnApplication.setSubmissionDate(System.currentTimeMillis());
        vpnApplication.setModuleId(ModuleConstants.Module_ID_VPN);
        vpnApplication.setUserId(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : loginDTO.getAccountID());


        VPNProbableTD vpnProbableTD = ServiceDAOFactory.getService(VPNProbableTDService.class).getProbableTDByClientID(vpnApplication.getClientId());
        if (vpnProbableTD.getIsCompleted() != VPNConstants.TD_STATE) {
            throw new RequestFailureException("This Client Already have an TD Request ");
        }

        globalService.save(vpnApplication);
        vpnProbableTD.setIsCompleted(VPNConstants.TD_SUBMITTED);
        globalService.update(vpnProbableTD);

        List<VPNNetworkLink> networkLinks = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveNetworkLinksByClient(vpnApplication.getClientId());
        if (networkLinks.isEmpty()) throw new RequestFailureException("No Network link found for this Client");
        networkLinks.stream().filter(s -> {

            VPNApplicationLink vpnApplicationLink = new VPNApplicationLink();
            vpnApplicationLink.setVpnApplicationId(vpnApplication.getVpnApplicationId());
            vpnApplicationLink.setLocalOfficeId(s.getLocalOfficeId());
            vpnApplicationLink.setLinkName(s.getLinkName());
            vpnApplicationLink.setLinkState(ApplicationState.VPN_TD_APPLICATION_SUBMITTED);
            vpnApplicationLink.setRemoteOfficeId(s.getRemoteOfficeId());
            vpnApplicationLink.setLinkBandwidth(s.getLinkBandwidth());
            vpnApplicationLink.setNetworkLinkId(s.getId());
            vpnApplicationLink.setRemoteOfficeTerminalDeviceProvider(s.getRemoteOfficeTerminalDeviceProvider());
            vpnApplicationLink.setLocalOfficeTerminalDeviceProvider(s.getLocalOfficeTerminalDeviceProvider());
            try {
                List<LocalLoopConsumerMap> list = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class, new LocalLoopConsumerMapConditionBuilder()
                        .Where()
                        .consumerIdEquals(s.getId())
                        .getCondition());
                for (LocalLoopConsumerMap localLoopConsumerMap : list) {
                    LocalLoop localLoop = globalService.findByPK(LocalLoop.class, localLoopConsumerMap.getLocalLoopId());
                    if (localLoop.getOfficeId() == s.getLocalOfficeId()) {
                        vpnApplicationLink.setLocalOfficeLoopId(localLoop.getId());
                    } else if (localLoop.getOfficeId() == s.getRemoteOfficeId()) {
                        vpnApplicationLink.setRemoteOfficeLoopId(localLoop.getId());
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                globalService.save(vpnApplicationLink);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            // TODO: 3/3/2019 insert application link
            return true;
        }).collect(Collectors.toList());


    }


    @Transactional
    public void tdCompleteBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {

        List<VPNApplicationLink> list = application.getVpnApplicationLinks();
        list.forEach(s -> {
                    s.setLinkState(ApplicationState.VPN_TD_APPLICATION_CONNECTION_COMPLETE);
                    s.setServiceStarted(true);
                    try {
                        globalService.update(s);

                        //insert the comment
                        commentService.insertComment(application.getComment(), s.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, s.getLinkState().getState());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        application.setIsServiceStarted(1);
        globalService.update(application);

        List<VPNNetworkLink> networkLinks = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveNetworkLinksByClient(application.getClientId());
        for (VPNNetworkLink vpnNetworkLink : networkLinks) {
            vpnNetworkLink.setActiveTo(System.currentTimeMillis());
            //vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_CLOSE.getStatus());
            globalService.update(vpnNetworkLink);

            List<LocalLoopConsumerMap> localLoopList = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                    new LocalLoopConsumerMapConditionBuilder()
                            .Where()
                            .consumerIdEquals(vpnNetworkLink.getId())
                            .getCondition());

            for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                localLoopConsumerMap.setActive(false);
                localLoopConsumerMap.setToDate(System.currentTimeMillis());
                globalService.update(localLoopConsumerMap);
            }

            vpnNetworkLink.setActiveFrom(System.currentTimeMillis());
            vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_TD.getStatus());
            vpnNetworkLink.setActiveTo(Long.MAX_VALUE);
            vpnNetworkLink.setApplicationId(application.getVpnApplicationId());
            vpnNetworkLink.setIncidentType(VPNConstants.INCIDENT.TD.getIncidentName());
            globalService.save(vpnNetworkLink);

            for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                localLoopConsumerMap.setActive(true);
                localLoopConsumerMap.setToDate(Long.MAX_VALUE);
                localLoopConsumerMap.setConsumerId(vpnNetworkLink.getId());
                globalService.save(localLoopConsumerMap);

            }
        }

        ServiceDAOFactory.getService(VPNClientTDService.class).
                tempDisconnectClientByClientID(application.getClientId(), System.currentTimeMillis());

        VPNProbableTD vpnProbableTD = ServiceDAOFactory.getService(VPNProbableTDService.class).getProbableTDByClientID(application.getClientId());
        vpnProbableTD.setIsCompleted(1);
        globalService.update(vpnProbableTD);

    }

    @Transactional
    public void reconnectInsertBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        VPNApplication vpnApplication = new VPNApplication();//new NIXReviseClientDeserializer().deserialize_custom(jsonElement, loginDTO);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long clientID = jsonObject.get("client") != null ? jsonObject.get("client").getAsJsonObject().get("key").getAsLong() : 0;
        if (clientID == 0) {
            clientID = jsonObject.get("clientID").getAsLong();
        }
        long suggestedDate = jsonObject.get("suggestedDate") != null ? jsonObject.get("suggestedDate").getAsLong() : 0;

        vpnApplication.setClientId(clientID);
        vpnApplication.setSuggestedDate(suggestedDate);
        vpnApplication.setApplicationType(ApplicationType.VPN_RECONNECT);
        vpnApplication.setApplicationState(ApplicationState.VPN_RECONNECT_APPLICATION_SUBMITTED);
        vpnApplication.setCreatedDate(System.currentTimeMillis());
        vpnApplication.setLastModificationTime(System.currentTimeMillis());
        vpnApplication.setClassName(VPNApplication.class.getName());
        vpnApplication.setSubmissionDate(System.currentTimeMillis());
        vpnApplication.setModuleId(ModuleConstants.Module_ID_VPN);
        vpnApplication.setUserId(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : loginDTO.getAccountID());


        boolean isTD = ServiceDAOFactory.getService(VPNClientTDService.class).isClientTemporarilyDisconnected(vpnApplication.getClientId());
        if (!isTD) {
            throw new RequestFailureException("The client is not in the TD, Reconnect applicaiton can not be submitted");
        }

        globalService.save(vpnApplication);

        List<VPNNetworkLink> networkLinks = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveNetworkLinksByClient(vpnApplication.getClientId());
        if (networkLinks.isEmpty()) throw new RequestFailureException("No Network link found for this Client");
        networkLinks.stream().filter(s -> {

            VPNApplicationLink vpnApplicationLink = new VPNApplicationLink();
            vpnApplicationLink.setVpnApplicationId(vpnApplication.getVpnApplicationId());
            vpnApplicationLink.setLocalOfficeId(s.getLocalOfficeId());
            vpnApplicationLink.setLinkName(s.getLinkName());
            vpnApplicationLink.setNetworkLinkId(s.getId());
            vpnApplicationLink.setLinkState(ApplicationState.VPN_RECONNECT_APPLICATION_SUBMITTED);
            vpnApplicationLink.setRemoteOfficeId(s.getRemoteOfficeId());
            vpnApplicationLink.setLinkBandwidth(s.getLinkBandwidth());
            vpnApplicationLink.setRemoteOfficeTerminalDeviceProvider(s.getRemoteOfficeTerminalDeviceProvider());
            vpnApplicationLink.setLocalOfficeTerminalDeviceProvider(s.getLocalOfficeTerminalDeviceProvider());
            try {
                List<LocalLoopConsumerMap> list = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                        new LocalLoopConsumerMapConditionBuilder()
                                .Where()
                                .consumerIdEquals(s.getId())
                                .getCondition());
                for (LocalLoopConsumerMap localLoopConsumerMap : list) {
                    LocalLoop localLoop = globalService.findByPK(LocalLoop.class, localLoopConsumerMap.getLocalLoopId());
                    if (localLoop.getOfficeId() == s.getLocalOfficeId()) {
                        vpnApplicationLink.setLocalOfficeLoopId(localLoop.getId());
                    } else if (localLoop.getOfficeId() == s.getRemoteOfficeId()) {
                        vpnApplicationLink.setRemoteOfficeLoopId(localLoop.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                globalService.save(vpnApplicationLink);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            // TODO: 3/3/2019 insert application link
            return true;
        }).collect(Collectors.toList());

    }

    @Transactional
    public void reconnectCompleteBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<VPNApplicationLink> list = application.getVpnApplicationLinks();
        list.forEach(s -> {
                    s.setLinkState(ApplicationState.VPN_RECONNECT_APPLICATION_CONNECTION_COMPLETE);
                    s.setServiceStarted(true);

                    try {
                        globalService.update(s);

                        //insert the comment
                        commentService.insertComment(application.getComment(), s.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, s.getLinkState().getState());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        application.setIsServiceStarted(1);
        globalService.update(application);

        List<VPNNetworkLink> networkLinks = ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveNetworkLinksByClient(application.getClientId());
        for (VPNNetworkLink vpnNetworkLink : networkLinks) {
            vpnNetworkLink.setActiveTo(System.currentTimeMillis());
            // vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_CLOSE.getStatus());
            globalService.update(vpnNetworkLink);

            List<LocalLoopConsumerMap> localLoopList = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                    new LocalLoopConsumerMapConditionBuilder()
                            .Where()
                            .consumerIdEquals(vpnNetworkLink.getId())
                            .getCondition());

            for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                localLoopConsumerMap.setActive(false);
                localLoopConsumerMap.setToDate(System.currentTimeMillis());
                globalService.update(localLoopConsumerMap);
            }

            vpnNetworkLink.setActiveFrom(System.currentTimeMillis());
            vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_ACTIVE.getStatus());
            vpnNetworkLink.setActiveTo(Long.MAX_VALUE);
            vpnNetworkLink.setApplicationId(application.getVpnApplicationId());
            vpnNetworkLink.setIncidentType(VPNConstants.INCIDENT.RECONNECT.getIncidentName());
            globalService.save(vpnNetworkLink);

            for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                localLoopConsumerMap.setActive(true);
                localLoopConsumerMap.setToDate(Long.MAX_VALUE);
                localLoopConsumerMap.setConsumerId(vpnNetworkLink.getId());
                globalService.save(localLoopConsumerMap);

            }
        }

        ServiceDAOFactory.getService(VPNClientTDService.class).
                reconnectClientByClientID(application.getClientId(), System.currentTimeMillis());
    }

    @Transactional
    public List<VPNNetworkLink> getActiveLinkByClient(long clientId) throws Exception {
        return ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveNetworkLinksByClient(clientId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getActiveVPNLinkNameIDPairListByClient(long clientID) throws Exception {
        return ServiceDAOFactory.getService(VPNNetworkLinkService.class).getActiveVPNLinkNameIDPairListByClient(clientID);
    }


    @Transactional
    public String ownerChangeInsertBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long srcClient = jsonObject.getAsJsonObject("srcClient").get("key").getAsLong() > 0 ?
                jsonObject.getAsJsonObject("srcClient").get("key").getAsLong() : -1;
        long dstClient = jsonObject.getAsJsonObject("dstClient").get("key").getAsLong() > 0 ?
                jsonObject.getAsJsonObject("dstClient").get("key").getAsLong() : -1;
//        ACCOUNT_RECEIVABLE check
        double receivableAmount = ServiceDAOFactory.getService(AccountingEntryService.class).
                getBalanceByClientIDAndAccountID(srcClient, AccountType.VPN_ACCOUNT_RECEIVABLE_TD.getID());
        if (NumberComparator.isGreaterThanOrEqual(receivableAmount, 1.00)) {
            throw new RequestFailureException("You should pay your due first, your due amount is: " + Math.ceil(receivableAmount));
        }
        long suggestedDate = jsonObject.get("suggestedDate") != null ? jsonObject.get("suggestedDate").getAsLong() : 0;

        VPNApplication vpnApplication = new VPNApplication();
        vpnApplication.setClientId(srcClient);
        vpnApplication.setSecondClient(dstClient);
        vpnApplication.setSuggestedDate(suggestedDate);
        vpnApplication.setApplicationType(ApplicationType.VPN_OWNER_CHANGE);
        vpnApplication.setApplicationState(ApplicationState.VPN_OC_APPLICATION_SUBMITTED);
        vpnApplication.setCreatedDate(System.currentTimeMillis());
        vpnApplication.setLastModificationTime(System.currentTimeMillis());
        vpnApplication.setClassName(VPNApplication.class.getName());
        vpnApplication.setSubmissionDate(System.currentTimeMillis());
        vpnApplication.setModuleId(ModuleConstants.Module_ID_VPN);
        vpnApplication.setUserId(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : loginDTO.getAccountID());
        globalService.save(vpnApplication);

        List<VPNNetworkLink> vpnNetworkLinks = deserializeNetworkLinks(jsonObject);
        if (vpnNetworkLinks.isEmpty())
            throw new RequestFailureException("No Network link found for this Client");

        List<KeyValuePair<VPNOnProcessLink, VPNNetworkLink>> pairOfOnProcessLinkAndNetworkLinks = getPairOfVPNOnProcessLinkAndVPNNetworkLink(vpnNetworkLinks);
        String failureLinksText = pairOfOnProcessLinkAndNetworkLinks
                .stream()
                .map(KeyValuePair::getKey)
                .filter(t -> t.getId() > 0)
                .map(t -> String.valueOf(t.getLink()))
                .collect(Collectors.joining(", "));

        List<KeyValuePair<VPNOnProcessLink, VPNNetworkLink>> toBeInsertedLinks = pairOfOnProcessLinkAndNetworkLinks.stream()
                .filter(t -> t.getKey().getId() == 0)
                .peek(t -> {
                    t.getKey().setApplication(vpnApplication.getVpnApplicationId());
                    t.getKey().setLink(t.getValue().getId());
                })
                .collect(Collectors.toList());

        String successLinks = toBeInsertedLinks.stream()
                .map(t -> String.valueOf(t.getKey().getLink()))
                .collect(Collectors.joining(", "));

        toBeInsertedLinks
                .forEach(t -> {
                    VPNApplicationLink vpnApplicationLink = prepareVPNApplicationForOwnerChange(vpnApplication, t.getValue());
                    try {
                        globalService.save(t.getKey());
                        globalService.save(vpnApplicationLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        if (failureLinksText.isEmpty()) {
            return "Success";
        } else {
            String temp = "Failure for Link Id: " + failureLinksText;
            if (!successLinks.isEmpty()) {
                temp = "Success for Link Id: " + successLinks;
            }
            return temp;
        }
    }

    private VPNApplicationLink prepareVPNApplicationForOwnerChange(VPNApplication vpnApplication, VPNNetworkLink networkLink) {
        VPNApplicationLink vpnApplicationLink = new VPNApplicationLink();
        vpnApplicationLink.setVpnApplicationId(vpnApplication.getVpnApplicationId());
        vpnApplicationLink.setLocalOfficeId(networkLink.getLocalOfficeId());
        vpnApplicationLink.setLinkName(networkLink.getLinkName());
        vpnApplicationLink.setLinkState(ApplicationState.VPN_OC_APPLICATION_SUBMITTED);
        vpnApplicationLink.setRemoteOfficeId(networkLink.getRemoteOfficeId());
        vpnApplicationLink.setLinkBandwidth(networkLink.getLinkBandwidth());
        vpnApplicationLink.setRemoteOfficeTerminalDeviceProvider(networkLink.getRemoteOfficeTerminalDeviceProvider());
        vpnApplicationLink.setLocalOfficeTerminalDeviceProvider(networkLink.getLocalOfficeTerminalDeviceProvider());
        try {
            List<LocalLoopConsumerMap> list = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                    new LocalLoopConsumerMapConditionBuilder()
                            .Where()
                            .consumerIdEquals(networkLink.getId())
                            .getCondition());
            for (LocalLoopConsumerMap localLoopConsumerMap : list) {
                LocalLoop localLoop = globalService.findByPK(LocalLoop.class, localLoopConsumerMap.getLocalLoopId());
                if (localLoop.getOfficeId() == networkLink.getLocalOfficeId()) {
                    vpnApplicationLink.setLocalOfficeLoopId(localLoop.getId());
                } else if (localLoop.getOfficeId() == networkLink.getRemoteOfficeId()) {
                    vpnApplicationLink.setRemoteOfficeLoopId(localLoop.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vpnApplicationLink;
    }

    private List<KeyValuePair<VPNOnProcessLink, VPNNetworkLink>> getPairOfVPNOnProcessLinkAndVPNNetworkLink(List<VPNNetworkLink> vpnNetworkLinks) {
        VPNOnProcessLinkService onProcessLinkService = ServiceDAOFactory.getService(VPNOnProcessLinkService.class);
        return
                vpnNetworkLinks.stream()
                        .map(t -> {
                            try {
                                return new KeyValuePair<>(onProcessLinkService.getOnProcessConnectionByLink(t.getId()), t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return new KeyValuePair<>(new VPNOnProcessLink(), t);
                        })
                        .collect(Collectors.toList());
    }

    private List<VPNNetworkLink> deserializeNetworkLinks(JsonObject jsonObject) throws Exception {
        JsonArray jsonArray = (JsonArray) ((jsonObject.get("selectedConnectionList") != null) ? jsonObject.get("selectedConnectionList") : null);
        List<VPNNetworkLink> vpnNetworkLinks = new ArrayList<>();
        if (jsonArray != null) {

            for (JsonElement jsonElement1 : jsonArray) {
                JsonObject ConnectionJsonObject = jsonElement1.getAsJsonObject();
                long linkId = ConnectionJsonObject.get("id").getAsLong();
                VPNNetworkLink link = globalService.findByPK(VPNNetworkLink.class, linkId);
                vpnNetworkLinks.add(link);
            }
        }
        return vpnNetworkLinks;
    }

    @Transactional
    public void ownerChangeCompleteBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        VPNApplication vpnApplication = getApplicationByApplicationId(application.getApplicationId());

        List<VPNApplicationLink> list = application.getVpnApplicationLinks();
        list.forEach(s -> {
                    s.setLinkState(ApplicationState.VPN_OC_COMPLETE);
                    try {
                        globalService.update(s);

                        //insert the comment
                        commentService.insertComment(application.getComment(), s.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, s.getLinkState().getState());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        application.setIsServiceStarted(1);
        globalService.update(application);
        List<VPNOnProcessLink> vpnOnProcessLinks = ServiceDAOFactory.getService(VPNOnProcessLinkService.class).
                getOnProcessLinksByApplication(application.getVpnApplicationId());
        vpnOnProcessLinks.forEach(s -> {
            try {
                VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, s.getLink());
                vpnNetworkLink.setActiveTo(System.currentTimeMillis());
                globalService.update(vpnNetworkLink);

                List<LocalLoopConsumerMap> localLoopList = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                        new LocalLoopConsumerMapConditionBuilder()
                                .Where()
                                .consumerIdEquals(vpnNetworkLink.getId())
                                .getCondition());

                for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                    localLoopConsumerMap.setActive(false);
                    localLoopConsumerMap.setToDate(System.currentTimeMillis());
                    globalService.update(localLoopConsumerMap);
                }

                vpnNetworkLink.setActiveFrom(System.currentTimeMillis());
                vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_ACTIVE.getStatus());
                vpnNetworkLink.setActiveTo(Long.MAX_VALUE);
                vpnNetworkLink.setApplicationId(vpnApplication.getVpnApplicationId());
                vpnNetworkLink.setIncidentType(VPNConstants.INCIDENT.OWNER_CHANGE.getIncidentName());
                vpnNetworkLink.setClientId(vpnApplication.getSecondClient());
                globalService.save(vpnNetworkLink);

                for (LocalLoopConsumerMap localLoopConsumerMap : localLoopList) {
                    localLoopConsumerMap.setActive(true);
                    localLoopConsumerMap.setToDate(Long.MAX_VALUE);
                    localLoopConsumerMap.setConsumerId(vpnNetworkLink.getId());
                    globalService.save(localLoopConsumerMap);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Transactional
    public void closeInsertBatchOperation(JsonElement jsonElement, LoginDTO loginDTO) throws Exception {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long client = jsonObject.getAsJsonObject("client").get("key").getAsLong() > 0 ?
                jsonObject.getAsJsonObject("client").get("key").getAsLong() : -1;

        long selectedlink = jsonObject.getAsJsonObject("selectedLink").get("id").getAsLong() > 0 ?
                jsonObject.getAsJsonObject("selectedLink").get("id").getAsLong() : -1;

        long suggestedDate = jsonObject.get("suggestedDate") != null ? jsonObject.get("suggestedDate").getAsLong() : 0;

        VPNApplication vpnApplication = new VPNApplication();
        vpnApplication.setClientId(client);
        vpnApplication.setSuggestedDate(suggestedDate);
        vpnApplication.setApplicationType(ApplicationType.VPN_CLOSE);
        vpnApplication.setApplicationState(ApplicationState.VPN_CLOSE_APPLICATION_SUBMITTED);
        vpnApplication.setCreatedDate(System.currentTimeMillis());
        vpnApplication.setLastModificationTime(System.currentTimeMillis());
        vpnApplication.setClassName(VPNApplication.class.getName());
        vpnApplication.setSubmissionDate(System.currentTimeMillis());
        vpnApplication.setModuleId(ModuleConstants.Module_ID_VPN);
        vpnApplication.setUserId(loginDTO.getUserID() > 0 ? loginDTO.getUserID() : loginDTO.getAccountID());
        globalService.save(vpnApplication);

        VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, selectedlink);

//        VPNOnProcessLink vpnOnProcessLink = new VPNOnProcessLink();
////        vpnOnProcessLink.setLink(vpnNetworkLink.getId());
////        vpnOnProcessLink.setApplication(vpnApplication.getVpnApplicationId());
////        globalService.save(vpnOnProcessLink);

        VPNApplicationLink vpnApplicationLink = new VPNApplicationLink();
        vpnApplicationLink.setVpnApplicationId(vpnApplication.getVpnApplicationId());
        vpnApplicationLink.setLocalOfficeId(vpnNetworkLink.getLocalOfficeId());
        vpnApplicationLink.setLinkName(vpnNetworkLink.getLinkName());
        vpnApplicationLink.setRemoteOfficeId(vpnNetworkLink.getRemoteOfficeId());
        vpnApplicationLink.setLinkBandwidth(vpnNetworkLink.getLinkBandwidth());
        vpnApplicationLink.setLocalZoneId(vpnNetworkLink.getLocalZoneId());
        vpnApplicationLink.setRemoteZoneId(vpnNetworkLink.getRemoteZoneId());
        vpnApplicationLink.setRemoteOfficeTerminalDeviceProvider(vpnNetworkLink.getRemoteOfficeTerminalDeviceProvider());
        vpnApplicationLink.setLocalOfficeTerminalDeviceProvider(vpnNetworkLink.getLocalOfficeTerminalDeviceProvider());
        vpnApplicationLink.setNetworkLinkId(vpnNetworkLink.getId());

        try {

            //flags for flow choosings
            boolean isBillApplicable = false;
            boolean isUsedByOther = false;
            boolean isUsedChecked = false;
            boolean isVisited = false;
            //flag end

            List<LocalLoopConsumerMap> list = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                    new LocalLoopConsumerMapConditionBuilder()
                            .Where()
                            .consumerIdEquals(vpnNetworkLink.getId())
                            .isActive(true)
                            .getCondition());
            for (LocalLoopConsumerMap localLoopConsumerMap : list) {
                LocalLoop localLoop = globalService.findByPK(LocalLoop.class, localLoopConsumerMap.getLocalLoopId());
                // TODO: 3/12/2019 fetch local loop efrs
                // List<EFR> efrs = ServiceDAOFactory.getService(EFRService.class).ge
                //check if the loop reused in other modal

                List<LocalLoopConsumerMap> localLoopConsumerMaps = globalService.getAllObjectListByCondition(
                        LocalLoopConsumerMap.class,
                        new LocalLoopConsumerMapConditionBuilder()
                                .Where()
                                .localLoopIdEquals(localLoop.getId())
                                .isActive(true)
                                .getCondition())
                        .stream()
                        .filter(s -> s.getId() != localLoopConsumerMap.getId())
                        .collect(Collectors.toList());

                if (localLoopConsumerMaps.size() > 0 && !isUsedChecked) {
                    isUsedChecked = true;
                    isUsedByOther = true;
                }
                // end

                //check if bill is applicable
                if (!isVisited && localLoopConsumerMap.isBillApplicable()) {
                    isBillApplicable = true;
                    isVisited = true;
                }
                if (localLoop.getOfficeId() == vpnNetworkLink.getLocalOfficeId()) {
                    vpnApplicationLink.setLocalOfficeLoopId(localLoop.getId());
                }
                if (localLoop.getOfficeId() == vpnNetworkLink.getRemoteOfficeId()) {
                    vpnApplicationLink.setRemoteOfficeLoopId(localLoop.getId());
                }
            }
            // set flow by checking if bill applicable and loop used by other module
            if (isBillApplicable && !isUsedByOther) {
                list.stream().forEach(m -> {
                    try {
                        LocalLoop l = globalService.findByPK(LocalLoop.class, m.getLocalLoopId());
                        // TODO: 3/12/2019 for the current close application add manual efr entry
                        List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class, new EFRConditionBuilder()
                                .Where()
                                .officeIdEquals(l.getOfficeId())
                                .popIdEquals(l.getPopId())
                                .isCompleted(true)
                                .getCondition()
                        );
                        efrs.stream().forEach(n -> {
                            try {
                                n.setCompleted(false);
                                n.setWorkOrdered(false);
                                n.setWorkOrderNumber(null);
                                n.setApplicationId(vpnApplication.getApplicationId());
                                globalService.save(n);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                vpnApplicationLink.setLinkState(ApplicationState.CLOSE_WITH_LOOP_SUBMITTED);
            } else vpnApplicationLink.setLinkState(ApplicationState.VPN_CLOSE_APPLICATION_SUBMITTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            globalService.save(vpnApplicationLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void closeCompleteBatchOperation(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<VPNApplicationLink> list = application.getVpnApplicationLinks();
        list.forEach(s -> {
                    s.setLinkState(ApplicationState.CLOSE_COMPLETE);
                    try {


                        //insert the comment
                        commentService.insertComment(application.getComment(), s.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, s.getLinkState().getState());

                        VPNNetworkLink vpnNetworkLink = globalService.findByPK(VPNNetworkLink.class, s.getNetworkLinkId());

                        vpnNetworkLink.setActiveTo(System.currentTimeMillis());
                        globalService.update(vpnNetworkLink);
                        vpnNetworkLink.setApplicationId(application.getVpnApplicationId());
                        vpnNetworkLink.setIncidentType(VPNConstants.INCIDENT.CLOSE.getIncidentName());
                        vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_CLOSE.getStatus());
                        // TODO: 3/11/2019 the used ports and vlans should be made free as link will be closed
                        List<LocalLoopConsumerMap> localLoopConsumerMaps = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                                new LocalLoopConsumerMapConditionBuilder()
                                        .Where()
                                        .consumerIdEquals(vpnNetworkLink.getId())
                                        .getCondition());

                        for (LocalLoopConsumerMap consumerMap : localLoopConsumerMaps) {
                            LocalLoop localLoop = globalService.findByPK(LocalLoop.class, consumerMap.getLocalLoopId());

                            // TODO: 3/12/2019 check if the following condition satisfies all conditions
                            List<LocalLoopConsumerMap> maps = globalService.getAllObjectListByCondition(
                                    LocalLoopConsumerMap.class,
                                    new LocalLoopConsumerMapConditionBuilder()
                                            .Where()
                                            .localLoopIdEquals(localLoop.getId())
                                            .isActive(true)
                                            .getCondition())
                                    .stream()
                                    .filter(t -> t.getId() != consumerMap.getId())
                                    .collect(Collectors.toList());

                            if (localLoopConsumerMaps.size() > 0 && consumerMap.isBillApplicable()) {
                                consumerMap.setBillApplicable(false);
                                globalService.save(consumerMap);
                            }
                            //todo end

                            if (localLoop.getVlanId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getVlanId(), ModuleConstants.Module_ID_VPN, application.getClientId());
                            }
                            if (localLoop.getPortId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getPortId(), ModuleConstants.Module_ID_VPN, application.getClientId());
                            }
                            if (localLoop.getRouterOrSwitchId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getRouterOrSwitchId(), ModuleConstants.Module_ID_VPN, application.getClientId());
                            }

                        }
                        globalService.update(s);
//                        globalService.save(vpnNetworkLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        application.setIsServiceStarted(1);
        globalService.update(application);
    }

    @Transactional
    public void closeLink(VPNNetworkLink vpnNetworkLink,long clientId) throws Exception {


                        vpnNetworkLink.setActiveTo(System.currentTimeMillis());
                        globalService.update(vpnNetworkLink);
                        vpnNetworkLink.setIncidentType(VPNConstants.INCIDENT.CLOSE.getIncidentName());
                        vpnNetworkLink.setLinkStatus(VPNConstants.Status.VPN_CLOSE.getStatus());
                        List<LocalLoopConsumerMap> localLoopConsumerMaps = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                                new LocalLoopConsumerMapConditionBuilder()
                                        .Where()
                                        .consumerIdEquals(vpnNetworkLink.getId())
                                        .getCondition());

                        for (LocalLoopConsumerMap consumerMap : localLoopConsumerMaps) {
                            LocalLoop localLoop = globalService.findByPK(LocalLoop.class, consumerMap.getLocalLoopId());

                            // TODO: 3/12/2019 check if the following condition satisfies all conditions
                            List<LocalLoopConsumerMap> maps = globalService.getAllObjectListByCondition(
                                    LocalLoopConsumerMap.class,
                                    new LocalLoopConsumerMapConditionBuilder()
                                            .Where()
                                            .localLoopIdEquals(localLoop.getId())
                                            .isActive(true)
                                            .getCondition())
                                    .stream()
                                    .filter(t -> t.getId() != consumerMap.getId())
                                    .collect(Collectors.toList());

                            if (localLoopConsumerMaps.size() > 0 && consumerMap.isBillApplicable()) {
                                consumerMap.setBillApplicable(false);
                                globalService.save(consumerMap);
                            }

                            if (localLoop.getVlanId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getVlanId(), ModuleConstants.Module_ID_VPN, clientId);
                            }
                            if (localLoop.getPortId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getPortId(), ModuleConstants.Module_ID_VPN, clientId);
                            }
                            if (localLoop.getRouterOrSwitchId() > 0) {
                                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).deallocationInventoryItem(localLoop.getRouterOrSwitchId(), ModuleConstants.Module_ID_VPN, clientId);
                            }

                        }



    }


    @Transactional
    public void addCommentOnALink(VPNApplication application, LoginDTO loginDTO) throws Exception {
        List<VPNApplicationLink> list = application.getVpnApplicationLinks();
        list.forEach(s -> {
                    try {
                        commentService.insertComment(application.getComment(), s.getId(), loginDTO.getUserID(), ModuleConstants.Module_ID_VPN, s.getLinkState().getState());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Transactional
    public void CloseWOforwardRequest(VPNApplication vpnApplication, LoginDTO loginDTO) throws Exception {
        LocalLoop remoteLoop = globalService.findByPK(LocalLoop.class, vpnApplication.getVpnApplicationLinks().get(0).getRemoteOfficeLoopId());
        LocalLoop localLoop = globalService.findByPK(LocalLoop.class, vpnApplication.getVpnApplicationLinks().get(0).getLocalOfficeLoopId());
        if (remoteLoop.getLoopProvider() == VPNConstants.LOOP_BTCL || localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL) {
            if (remoteLoop.getZoneId() > 0 || localLoop.getZoneId() > 0) {
                if (remoteLoop.getZoneId() > 0 && remoteLoop.getLoopProvider() == VPNConstants.LOOP_BTCL) {
                    remoteLoop.setDistributed(true);
                    globalService.update(remoteLoop);
                }
                if (localLoop.getZoneId() > 0 && remoteLoop.getLoopProvider() == VPNConstants.LOOP_BTCL) {
                    localLoop.setDistributed(true);
                    globalService.update(localLoop);
                }
                globalService.update(vpnApplication.getVpnApplicationLinks().get(0));
            } else {
                throw new RequestFailureException("There is no LDGM associated with this link. So You Can not forward to LDGM for this application");
            }
        } else {
            throw new RequestFailureException("There is no LDGM associated with this link. So You Can not forward to LDGM for this application");
        }

    }

    @Transactional
    public void closeApprovalAndForward(VPNApplication application, LoginDTO loginDTO) throws Exception {
        if (application.getVpnApplicationLinks().size() > 0) {
            for (VPNApplicationLink vpnApplicationLink : application.getVpnApplicationLinks()) {
                try {
                    markEFRAsLoopDistanceApprovedorNotApproved(vpnApplicationLink);
                    // getLinkIntoLoopDistanceApprovedState(vpnApplicationLink, loginDTO);
                    getLinkIntoCloseApprovedState(application, vpnApplicationLink, loginDTO);
                    commentService.insertComment(
                            application.getComment(),
                            vpnApplicationLink.getId(),
                            loginDTO.getUserID(),
                            ModuleConstants.Module_ID_VPN,
                            vpnApplicationLink.getLinkState().getState()
                    );

                } catch (Exception e) {
                    throw new RequestFailureException("Update Failed :" + e.getMessage());
                }
            }

        }
    }

    @Transactional
    void getLinkIntoCloseApprovedState(VPNApplication application, VPNApplicationLink vpnApplicationLink, LoginDTO loginDTO) throws Exception {
        List<EFR> localEndLocalLoopWorkCompletedEFR = new ArrayList<>();
        List<EFR> remoteEndLocalLoopWorkCompletedEFR = new ArrayList<>();
        boolean allLocalEndLocalLoopWorkCompleted = false;
        boolean allRemoteEndLocalLoopWorkCompleted = false;

        LocalLoop localLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getLocalOfficeLoopId());
        LocalLoop remoteLoop = globalService.findByPK(LocalLoop.class, vpnApplicationLink.getRemoteOfficeLoopId());
/*        if (localLoop.isCompleted() || localLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            if (remoteLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !remoteLoop.isCompleted()) {

                allLocalEndLocalLoopWorkCompleted = true;
            }
        } else {*/

        localEndLocalLoopWorkCompletedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getLocalOfficeId())
                        .applicationIdEquals(application.getApplicationId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .isCompleted(true)
                        .isForwarded(true)
                        .getCondition()
        );


        if (localEndLocalLoopWorkCompletedEFR != null && localEndLocalLoopWorkCompletedEFR.size() > 0) {
            allLocalEndLocalLoopWorkCompleted = true;
            localLoop.setDistributed(false);
            globalService.update(localLoop);
            // calculateActualLoopDistances(localEndLocalLoopWorkCompletedEFR, loginDTO);
        }

        // }

  /*      if (remoteLoop.isCompleted() || remoteLoop.getLoopProvider() == VPNConstants.LOOP_CLIENT) {
            if (localLoop.getLoopProvider() == VPNConstants.LOOP_BTCL && !localLoop.isCompleted()) {

                allRemoteEndLocalLoopWorkCompleted = true;
            }
        } else {
*/
        remoteEndLocalLoopWorkCompletedEFR = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdEquals(vpnApplicationLink.getRemoteOfficeId())
                        .applicationIdEquals(application.getApplicationId())
                        .isReplied(true)
                        .isIgnored(false)
                        .isSelected(true)
                        .isCompleted(true)
                        .isForwarded(true)
                        .getCondition()
        );
        if (remoteEndLocalLoopWorkCompletedEFR != null && remoteEndLocalLoopWorkCompletedEFR.size() > 0) {
            allRemoteEndLocalLoopWorkCompleted = true;
            remoteLoop.setDistributed(false);
            globalService.update(remoteLoop);
            // calculateActualLoopDistances(remoteEndLocalLoopWorkCompletedEFR, loginDTO);
        }
       /*     }
        }*/
        if (allLocalEndLocalLoopWorkCompleted && allRemoteEndLocalLoopWorkCompleted) {
            //VPNApplicationLink currentApplicationLink = globalService.findByPK(VPNApplicationLink.class, vpnApplicationLink.getId());
            globalService.update(vpnApplicationLink);

        }

    }


}
