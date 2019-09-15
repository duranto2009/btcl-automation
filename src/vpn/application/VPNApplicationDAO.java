package vpn.application;

import annotation.DAO;
import application.Application;
import application.ApplicationConditionBuilder;
import application.ApplicationService;
import application.ApplicationState;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;
import flow.FlowService;
import flow.entity.FlowState;
import location.ZoneDAO;
import login.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.SqlGenerator;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

public class VPNApplicationDAO {
    private static final Logger logger = LoggerFactory.getLogger(VPNApplicationDAO.class);


    @DAO
    ZoneDAO zoneDAO;
    FlowService flowService = ServiceDAOFactory.getService(FlowService.class);
//    VPNEFRService efrService = ServiceDAOFactory.getService(VPNEFRService.class);
    private static final String SQL_GET_APPLICATION_TYPE_BY_ID = "SELECT applicationType FROM at_lli_application WHERE applicationID = ?";

    Class<VPNApplication> classObject = VPNApplication.class;


//    public void insert(VPNApplication vpnApplicationObject)throws Exception {
//
//        ModifiedSqlGenerator.insert(vpnApplicationObject);
//    }

    public Collection getIDsWithSearchCriteria(Hashtable<String, String> criteria, LoginDTO loginDTO)throws Exception {
        long roleID = loginDTO.getRoleID();//state find with role
        boolean isBTCLPersonnal = false;
        int userId = (int) loginDTO.getUserID();
        ArrayList<Integer> zones = zoneDAO.getUserZone(userId);
        ArrayList<Integer> states = new ArrayList<>();
        ClientDTO clientDTO = new ClientDetailsDTO();
        UserDTO userDTO = null;
        try{
            if(userId>0)userDTO = UserRepository.getInstance().getUserDTOByUserID(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //region zone filter
        long zoneID = loginDTO.getZoneID();//no of zones for a user
        if (zoneID != 0) {
        }
        //endregion
        ResultSet rs = null;
        //region client filter
        if (userId == -1) {
            //TODO please check the following lines are commented
//            clientDTO = new VPNClientService().getVPNClient(loginDTO.getAccountID());
//            if (clientDTO.getClientID() >= 0) {
//                criteria.put("clientName", clientDTO.getLoginName());
//            }
        }
        //endregion
        //region vendor only see his workable app filter
        if (userId != -1) {
            isBTCLPersonnal = userDTO.isBTCLPersonnel();
        }
        List<FlowState> flowStates = new ArrayList<>();
        if (!isBTCLPersonnal && userId != -1) {
            flowStates = flowService.getStatesByRole((int) loginDTO.getRoleID());
            //todo : need to fetch end states
        } else {
            flowStates = flowService.getStates();
        }
        for (FlowState flowState : flowStates) {
            states.add(flowState.getId());
        }
        //endregion
        //region one vendor will not see other vendor application
        List<Long> applicationIDs = new ArrayList<>();
        String appID;
        String[] appIDs = new String[0];
        if (criteria.containsKey("applicationID")) {
            appID = criteria.get("applicationID");
            if (!(appID.equals("") || appID.equals(null))) {
                appIDs = appID.split(",");
            }
        }
        if (appIDs.length > 0) {
            for (String id : appIDs) {
                if (!isBTCLPersonnal && userId != -1) {
//                    List<VPNEFR> efrList = efrService.getEFRByVendorAndAppID(Long.parseLong(id), userId);
//                    if (efrList.size() > 0) {
//                        for (VPNEFR efr : efrList) {
//                            applicationIDs.add(efr.getApplication());
//                        }
//                    }
                } else {
                    applicationIDs.add(Long.parseLong(id));
                }
            }
            if (applicationIDs.size() > 0) {
                rs = getResultSetBySqlPair(
                        new VPNApplicationConditionBuilder()
                                .selectVpnApplicationId()
                                .fromVPNApplication()
                                .Where()
//				.applicationIDEqualsString(criteria.get("applicationID"))
                                .vpnApplicationIdIn(applicationIDs)//changed by bony
//                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
//                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
//                                .typeEqualsString(criteria.get("applicationType"))
//                                .clientInSqlPair(
//                                        new ClientDTOConditionBuilder()
//                                                .selectClientID()
//                                                .fromClientDTO()
//                                                .Where()
//                                                .loginNameBothLike(criteria.get("clientName"))
//                                                .getNullableSqlPair()
//                                )
//                                .clientEqualsString(criteria.get("clientID"))
//                                .(criteria.get("applicationStatus"))
//                                .demandNoteEqualsString(criteria.get("invoiceID"))
//                                .zoneIn(zones)//by bony
//                                .stateIn(states)//states
                                //.clientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
//                                .orderByidDesc()
                                .getSqlPair()
                );
            }

        }



// else {
//            if (!isBTCLPersonnal && userId != -1) {
//
//                List<VPNEFR> efrList = efrService.getEFRByVendor(userDTO.getUserID());
//                for (VPNEFR efr : efrList) {
//
//                    applicationIDs.add(efr.getApplication());
//                }
//
//                if (applicationIDs.size() > 0) {
//
//                    rs = getResultSetBySqlPair(
//                            new VPNApplicationConditionBuilder()
//                                    .selectId()
//                                    .fromVPNApplication()
//                                    .Where()
////				.applicationIDEqualsString(criteria.get("applicationID"))
//                                    .idIn(applicationIDs)//changed by bony
//                                    .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
//                                    .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
//                                    .typeEqualsString(criteria.get("applicationType"))
//                                    .clientInSqlPair(
//                                            new ClientDTOConditionBuilder()
//                                                    .selectClientID()
//                                                    .fromClientDTO()
//                                                    .Where()
//                                                    .loginNameBothLike(criteria.get("clientName"))
//                                                    .getNullableSqlPair()
//                                    )
//                                    .clientEqualsString(criteria.get("clientID"))
//                                    .stateEqualsString(criteria.get("applicationStatus"))
//                                    .demandNoteEqualsString(criteria.get("invoiceID"))
//                                    // .isServiceStartedLike(criteria.get("serviceStarted"))
//                                    //.zoneIn(zones)//by bony
//                                    .stateIn(states)//states
//                                    //.clientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())// this line is not commented the verndor not getting the application
//                                    .orderByidDesc()
//                                    .getSqlPair()
//                    );
//                }
//            }
            else {
                rs = getResultSetBySqlPair(

                        new ApplicationConditionBuilder()
                        .selectApplicationId()
                        .fromApplication()
                        .Where()
                        .applicationIdIn(applicationIDs)
                        .clientIdInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(criteria.get("clientName"))
                                        .getNullableSqlPair()

                        ).orderByapplicationIdDesc()
                        .getSqlPair()


                );
            }

        //endregion
        List<Long> idList = new ArrayList<>();
        if (rs != null) {
            idList = getSingleColumnListByResultSet(rs, Long.class);
        }
        return idList;
    }
    List<? extends Application> getApplicationListByIDList(List<Long> recordIDs)throws Exception {
        return recordIDs.stream()
        .map(t-> {
            try {
                return ServiceDAOFactory.getService(ApplicationService.class).getApplicationByApplicationId(t);
            } catch (Exception e) {

                throw new RequestFailureException(e.getMessage());
            }
        }).collect(Collectors.toList());

    }

    public VPNApplication getApplicationById(long appId) throws Exception{
        VPNApplication vpnApplication = ModifiedSqlGenerator.getObjectByID(VPNApplication.class,appId);
        return vpnApplication;
    }

    public void updateApplicaton(VPNApplication vpnApplication)throws Exception {
        ModifiedSqlGenerator.updateEntity(vpnApplication);
    }

    public void updateApplicatonState(long applicatonID, int applicationState) {
        try {
            VPNApplication vpnApplication = getApplicationById(applicatonID);
            vpnApplication.setApplicationState(ApplicationState.getApplicationStateByStateId(applicationState));
            ModifiedSqlGenerator.updateEntity(vpnApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<VPNApplication> getVPNApplicationsByDemandNoteId(long demandNoteId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(VPNApplication.class,
                new VPNApplicationConditionBuilder()
                        .Where()
                        .demandNoteIdEquals(demandNoteId)
                        .getCondition()
        );
    }

    public int getApplicationTypeById(long id){
        int applicationType = -1;
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_APPLICATION_TYPE_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                applicationType = resultSet.getInt("applicationType");
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return applicationType;
    }

    public List<VPNApplication> getVPNApplicationByConnectionID(long connection)throws Exception {
        List<VPNApplication> vpnApplications = ModifiedSqlGenerator.getAllObjectList(VPNApplication.class,
                new VPNApplicationConditionBuilder()
                        .Where()
//                        .connectionIdEquals(connection)
//                        .orderByidDesc()
                        .getCondition()
        );

        return vpnApplications;
    }

    public List<VPNApplication> getAppByClientAndAppTypeNotCompleted(long clientID, Enum appType)throws Exception {
        // TODO: 3/3/2019 check if td is submitted twice for same client
        List<VPNApplication> vpnApplications = ModifiedSqlGenerator.getAllObjectList(
                VPNApplication.class,
                new VPNApplicationConditionBuilder()
                        .Where()
                        //.clientIdEquals(clientID)
                        //.applicationTypeEquals(appType)
                        //.isCompleted(false)
                        .getCondition()
        );
        return vpnApplications;
    }
    VPNApplication getFullyPopulatedVPNApplication(long vpnApplicationId) throws Exception {
        return ModifiedSqlGenerator.getObjectFullyPopulatedByID(VPNApplication.class, vpnApplicationId);
    }
}
