package notification;

import annotation.DAO;
import annotation.Transactional;
import common.*;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import flow.FlowService;
import flow.entity.FlowState;
import flow.entity.Role;
import global.GlobalService;
import location.ZoneDAO;
import login.LoginDTO;
import mail.MailDTO;
import mail.MailSend;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import permission.ActionStateDTO;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import request.RequestUtilDAO;
import smsServer.SMSSender;
import user.UserDTO;
import user.UserRepository;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.*;
import java.util.stream.Collectors;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;

public class NotificationService {
    Logger logger = Logger.getLogger(getClass());
    @DAO
    NotificationDAO notificationDAO;

    @DAO
    ZoneDAO zoneDAO;
    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);


    public List<String> getWebNoficationListByLoginDTO(LoginDTO loginDTO) {
        List<String> notificationList = new ArrayList<String>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();

            RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
            long clientOrUserID = (loginDTO.getIsAdmin() ? -1 * loginDTO.getUserID() : loginDTO.getAccountID());
            List<CommonRequestDTO> commonRequestDTOs = requestUtilDAO.getRequestListByClientOrUserID(clientOrUserID, databaseConnection);

            for (CommonRequestDTO commonRequestDTO : commonRequestDTOs) {
                ActionStateDTO actionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID());
                if (actionStateDTO == null) continue;
                int viewerType = 0;
                if (commonRequestDTO.getClientID() == loginDTO.getAccountID()) {
                    viewerType = NotificationTemplate.CLIENT_TEMPLATE;
                } else if (-1 * commonRequestDTO.getRequestByAccountID() == loginDTO.getUserID()) {
                    viewerType = NotificationTemplate.SENDER_TEMPLATE;
                } else if (-1 * commonRequestDTO.getRequestToAccountID() == loginDTO.getUserID()) {
                    viewerType = NotificationTemplate.RECEIEVER_TEMPLATE;
                }

                NotificationTemplate notificationTemplate = notificationDAO.getNotificationTemplateByActionStateIDAndTemplateTypeAndViewerType(commonRequestDTO.getRequestTypeID(), NotificationTemplate.NOTIFICATION_TYPE_WEB, viewerType, databaseConnection);

                if (notificationTemplate == null) {
                    continue;
                }

                if (!notificationTemplate.isAllowClientForNotification() && !loginDTO.getIsAdmin()) {
                    continue;
                }
                String notificationString = NotificationProcessor.getNotification(notificationTemplate.getDescription(), commonRequestDTO);

                if (notificationString.trim().equals("")) {
                    notificationString = "Nofication template is set for " + commonRequestDTO.getState();
                }

                notificationList.add(notificationString);
            }

        } catch (Exception ex) {
            logger.debug("FATAL", ex);
        } finally {
            databaseConnection.dbClose();
        }
        return notificationList;
    }

    private String createMailSenderListByUserOrClientIDList(List<Long> userOrClientIDList, NotificationTemplate notificationTemplate, int moduleID, DatabaseConnection databaseConnection) throws Exception {
        String mailToString = "";

        for (Long userOrClientID : userOrClientIDList) {
            if (userOrClientID == null) {
                continue;
            }

            if (userOrClientID > 0) {
                if (notificationTemplate.isAllowClientForNotification()) {
                    ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(userOrClientID);
                    ClientDetailsDTO moduleClientDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientDTO.getClientID(), moduleID);
                    if (moduleClientDTO == null) {
                        logger.debug("moduleClientDTO: " + moduleClientDTO);
                    }
                    String conditionString = " where " + getColumnName(ClientContactDetailsDTO.class, "vpnClientID") + " = " + moduleClientDTO.getId();
                    List<ClientContactDetailsDTO> clientContactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, databaseConnection, conditionString);
                    String mailAddress = clientContactDetailsDTOs.get(0).getEmail();
                    if (!StringUtils.isBlank(mailAddress)) {
                        mailToString += (mailToString.equals("") ? "" : ";") + mailAddress;
                    }
                }
            } else {
                long userID = -1 * userOrClientID;
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
                if (!StringUtils.isBlank(userDTO.getMailAddress())) {
                    mailToString += (mailToString.equals("") ? "" : ";") + userDTO.getMailAddress();
                }
            }
        }

        return mailToString;
    }


    public void sendNotification(CommonRequestDTO commonRequestDTO) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Set<CommonRequestDTO> commonRequestDTOs = new HashSet<CommonRequestDTO>();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            databaseConnection.dbTransationRollBack();
        } finally {
            databaseConnection.dbClose();
        }
    }


    public void insertNotification(NotificationDTO notificationDTO) throws Exception {
        globalService.save(notificationDTO);
    }


    public HashMap<Long, Role> getNextStateRoleListForNotification(int stateId) {
        List<FlowState> flowStates = new FlowService().getNextStatesFromState(stateId);

        HashMap<Long, Role> uniqueRoles = new HashMap<>();
        for (FlowState flowState : flowStates
        ) {
            List<Role> roles = new FlowService().getRolesByNextState(flowState.getId())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            if (roles.size() > 0) {
                for (Role role : roles) {

                    if (!uniqueRoles.containsKey(role.getId())) {

                        uniqueRoles.put(role.getId(), role);
                    }

                }
            }
        }

        return uniqueRoles;

    }

    public void saveNotificationForNextStateUser(boolean isForClient, int stateId, long clientId, long userId, int moduleId, long entityId, String
            entityType, String actionURL, boolean hasChild, long childId) throws Exception {


        FlowState currentState = new FlowService().getStateById(stateId);
        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientId);
        String description = "";
        String url = "";
        long roleAccountId=0;
        if (isForClient) {
            description = currentState.getViewDescription() + " For " + entityType + ": " + entityId + " and It needs your attention";
            url = actionURL + entityId;
            roleAccountId=clientDTO.getClientID();
        } else {

            description = currentState.getViewDescription() + " For " + entityType + ": " + entityId + " From " + clientDTO.getLoginName() + " and it needs your attention";
            url = actionURL + entityId;
            UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(userId);
            roleAccountId=userDTO.getRoleID();
        }
        insertNotification(
                setNotification(moduleId, entityId, entityType, roleAccountId, userId, description, url, hasChild, childId, isForClient));


    }


    public void updateNotification(NotificationDTO notificationDTO) throws Exception {
        globalService.update(notificationDTO);
    }

    public void updateNotificationAsSeen(NotificationDTO notificationDTO) throws Exception {
        notificationDTO.setSeen(true);
        globalService.update(notificationDTO);
    }


    public void markNotificationAsActionTaken(long moduleId, long entityId, long roleAccountId, boolean isChild,long userId) throws Exception {
        List<NotificationDTO> notificationDTOS;

        if (isChild) {
            notificationDTOS = globalService.getAllObjectListByCondition(NotificationDTO.class,
                    new NotificationDTOConditionBuilder()
                            .Where()
                            .roleOrAccountIdEquals(roleAccountId)
                            .moduleIdEquals(moduleId)
//                            .entityIdEquals(entityId)
                            .childIdEquals(entityId)
                            .isActionTaken(false)
                            .orderBygenerationTimeDesc()
                            .getCondition()
            );
        } else {
            notificationDTOS = globalService.getAllObjectListByCondition(NotificationDTO.class,
                    new NotificationDTOConditionBuilder()
                            .Where()
                            .roleOrAccountIdEquals(roleAccountId)
                            .userIdEquals(userId)
                            .moduleIdEquals(moduleId)
                            .entityIdEquals(entityId)
                            .isActionTaken(false)
                            .orderBygenerationTimeDesc()
                            .getCondition()
            );
        }
        for (NotificationDTO notificationDTO : notificationDTOS
        ) {
            notificationDTO.setSeen(true);
            notificationDTO.setActionTaken(true);
            globalService.update(notificationDTO);

        }

//        globalService.update(notificationDTO);
    }


    public void markNotificationAsActionTaken(long moduleId, long entityId, long roleAccountId, boolean isChild) throws Exception {
        List<NotificationDTO> notificationDTOS;

        if (isChild) {
            notificationDTOS = globalService.getAllObjectListByCondition(NotificationDTO.class,
                    new NotificationDTOConditionBuilder()
                            .Where()
                            .roleOrAccountIdEquals(roleAccountId)
                            .moduleIdEquals(moduleId)
                            .entityIdEquals(entityId)
                            .childIdEquals(entityId)
                            .isActionTaken(false)
                            .orderBygenerationTimeDesc()
                            .getCondition()
            );
        } else {
            notificationDTOS = globalService.getAllObjectListByCondition(NotificationDTO.class,
                    new NotificationDTOConditionBuilder()
                            .Where()
                            .roleOrAccountIdEquals(roleAccountId)
                            .moduleIdEquals(moduleId)
                            .entityIdEquals(entityId)
                            .isActionTaken(false)
                            .orderBygenerationTimeDesc()
                            .getCondition()
            );
        }
        for (NotificationDTO notificationDTO : notificationDTOS
        ) {
            notificationDTO.setSeen(true);
            notificationDTO.setActionTaken(true);
            globalService.update(notificationDTO);

        }

//        globalService.update(notificationDTO);
    }

    public NotificationDTO setNotification(int moduleId, long entityId, String entityType, long roleAccountId, long userId, String description, String actionURL, boolean hasChild, long childId, boolean isForClient) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setGenerationTime(System.currentTimeMillis());
        notificationDTO.setModuleId(moduleId);
        notificationDTO.setEntityId(entityId);
        notificationDTO.setEntityType(entityType);
        notificationDTO.setRoleOrAccountId(roleAccountId);
        notificationDTO.setUserId(userId);
        notificationDTO.setDescription(description);
        notificationDTO.setActionURL(actionURL);
        notificationDTO.setHasChild(hasChild);
        notificationDTO.setChildId(childId);
        notificationDTO.setForClient(isForClient);
        return notificationDTO;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NotificationDTO> getNotificationListByRoleId(long roleAccountID, LoginDTO loginDTO) throws Exception {

        List<NotificationDTO> notificationDTOS;

        notificationDTOS = globalService.getAllObjectListByCondition(NotificationDTO.class,
                new NotificationDTOConditionBuilder()
                        .Where()
                        .roleOrAccountIdEquals(roleAccountID)
                        .userIdEquals(loginDTO.getUserID())
                        .isActionTaken(false)
                        .orderByidDesc()
                        .getCondition()
        );

        //one ldgm can see only his zones notification
        List<NotificationDTO> tempDTos = new ArrayList<>();
        UserDTO userDTO = null;
        Map<Pair<Long, Long>, List<NotificationDTO>> commonNoti =
                notificationDTOS
                        .stream()
                        .collect(
                                Collectors.groupingBy(w -> Pair.of(w.getEntityId(), w.getModuleId()))
                        );

        Iterator it = commonNoti.entrySet().iterator();
        List<NotificationDTO> notificationDTOS1 = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            List<NotificationDTO> notificationDTOS2 = (List<NotificationDTO>) mapElement.getValue();
            notificationDTOS1.add(notificationDTOS2.get(0));

        }

        notificationDTOS1= notificationDTOS1
                .stream()
                .sorted(Comparator.comparing(NotificationDTO::getId,Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return notificationDTOS1;
    }


    public List<MailDTO> getScheduledMailDTOList() throws Exception {
        List<MailDTO> scheduledMailDTOs = new ArrayList<MailDTO>();
        //	scheduledMailDTOs.addAll(get)

        return scheduledMailDTOs;
    }

    private List<MailDTO> getScheduledMailDTOListOfDomainModule() throws Exception {
        List<MailDTO> mailDTOs = new ArrayList<MailDTO>();
        return mailDTOs;
    }

    private List<MailDTO> getScheduledMailDTOListOfVPNModule() throws Exception {
        List<MailDTO> mailDTOs = new ArrayList<MailDTO>();
        return mailDTOs;
    }


    public void sendNotificationByEntityIDAndConditionString(int entityTypeID, String conditionString
            , int contactDetailsTypeID, String notificationMessage, boolean sendMailNotification
            , boolean sendSmsNotification, String notificationSubject) throws Exception {

        List<NotificationDraft> notificationDrafts = getNotificationDrafts(entityTypeID, conditionString
                , contactDetailsTypeID, notificationMessage);
        for (NotificationDraft notificationDraft : notificationDrafts) {
            if (sendMailNotification) {
                MailSend.getInstance().sendMailWithSubject(notificationDraft.mail, notificationDraft.notificationContent, notificationSubject);
            }
            if (sendSmsNotification) {
                SMSSender.getInstance().sendSMS(notificationDraft.notificationContent, notificationDraft.phoneNumber);
            }
        }
    }


    @Transactional
    public List<NotificationDraft> getNotificationDrafts(int entityTypeID, String conditionString
            , int contactDetailsTypeID, String notificationMessage) throws Exception {


        Class<? extends EntityDTO> entityClass = EntityTypeConstant.entityClassMap.get(entityTypeID);

        List<? extends EntityDTO> entityDTOs = ModifiedSqlGenerator.getAllObjectList(entityClass, conditionString);

        List<NotificationDraft> notificationDrafts = new ArrayList<>();
        int moduleID = entityTypeID / EntityTypeConstant.MULTIPLIER2;
        for (EntityDTO entityDTO : entityDTOs) {
            try {
                ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance()
                        .getModuleClientByClientIDAndModuleID(entityDTO.getClientID(), moduleID);
                ClientContactDetailsDTO clientContactDetailsDTO = new ContactDetailsDAO()
                        .getContactDetailsByModuleClientIDAndDetaislTypeID(clientDetailsDTO.getId(), contactDetailsTypeID
                                , DatabaseConnectionFactory.getCurrentDatabaseConnection());


                notificationMessage = NotificationProcessor.getNotification(notificationMessage, entityDTO, clientDetailsDTO, clientContactDetailsDTO);

                NotificationDraft notificationDraft = new NotificationDraft();
                notificationDraft.mail = clientContactDetailsDTO.getEmail();
                notificationDraft.phoneNumber = clientContactDetailsDTO.getPhoneNumber();
                notificationDraft.notificationContent = notificationMessage;

                notificationDrafts.add(notificationDraft);
            } catch (Throwable th) {
                th.printStackTrace();
            }

        }


        return notificationDrafts;
    }

}
