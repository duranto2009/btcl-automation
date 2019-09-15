package nix.application;

import annotation.DAO;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import connection.DatabaseConnection;
import flow.FlowService;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import location.ZoneDAO;
import login.LoginDTO;
import nix.efr.NIXEFR;
import nix.common.NIXClientService;
import nix.efr.NIXEFRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.UserDTO;
import user.UserRepository;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

public class NIXApplicationDAO {

    private static final Logger logger = LoggerFactory.getLogger(NIXApplicationDAO.class);


    @DAO
    ZoneDAO zoneDAO;

    FlowService flowService = ServiceDAOFactory.getService(FlowService.class);
    NIXEFRService efrService = ServiceDAOFactory.getService(NIXEFRService.class);
    private static final String SQL_GET_APPLICATION_TYPE_BY_ID = "SELECT applicationType FROM at_lli_application WHERE applicationID = ?";

    Class<NIXApplication> classObject = NIXApplication.class;


    public void insert(NIXApplication nixApplicationObject)throws Exception {

        ModifiedSqlGenerator.insert(nixApplicationObject);
    }
    public NIXApplication getFlowApplicationByID(long applicationID) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(NIXApplication.class, applicationID);
    }
    public Collection getIDsWithSearchCriteria(Hashtable <String, String>criteria, LoginDTO loginDTO)throws Exception {
        long roleID = loginDTO.getRoleID();//state find with role
        boolean isBTCLPersonnal = false;
        int userId = (int) loginDTO.getUserID();
        ArrayList<Integer> zones = zoneDAO.getUserZone(userId);
        ArrayList<Integer> states = new ArrayList<>();
        ClientDTO clientDTO = new ClientDetailsDTO();
        UserDTO userDTO = null;
        try{
            if(userId>0)userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
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
            clientDTO = new NIXClientService().getNIXClient(loginDTO.getAccountID());
            if (clientDTO.getClientID() >= 0) {
                criteria.put("clientName", clientDTO.getLoginName());
            }
        }
        //endregion
        //region vendor only see his workable app filter
        if (userId != -1) {
            isBTCLPersonnal = userDTO.isBTCLPersonnel();
        }
        List<FlowState> flowStates = new ArrayList<>();
        if (!isBTCLPersonnal && userId != -1) {
//            flowStates = flowService.getStatesByRole((int) loginDTO.getRoleID());
            flowStates = FlowRepository.getInstance().getActionableFlowStatesByRoleId(loginDTO.getRoleID());
            //todo : need to fetch end states
        } else {
//            flowStates = flowService.getStates();
            flowStates = FlowRepository.getInstance().getAllFlowStates();

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
                    List<NIXEFR> efrList = efrService.getEFRByVendorAndAppID(Long.parseLong(id), userId);
                    if (efrList.size() > 0) {
                        for (NIXEFR efr : efrList) {
                            applicationIDs.add(efr.getApplication());
                        }
                    }
                } else {
                    applicationIDs.add(Long.parseLong(id));
                }
            }
            if (applicationIDs.size() > 0) {
                rs = getResultSetBySqlPair(
                        new NIXApplicationConditionBuilder()
                                .selectId()
                                .fromNIXApplication()
                                .Where()
//				.applicationIDEqualsString(criteria.get("applicationID"))
                                .idIn(applicationIDs)//changed by bony
                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-from")))
                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-to")))
                                .typeEqualsString(criteria.get("applicationType"))
                                .clientInSqlPair(
                                        new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair()
                                )
                                .clientEqualsString(criteria.get("clientID"))
                                .stateEqualsString(criteria.get("applicationStatus"))
                                .demandNoteEqualsString(criteria.get("invoiceID"))
                                .zoneIn(zones)//by bony
                                .stateIn(states)//states
                                //.clientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                                .orderByidDesc()
                                .getSqlPair()
                );
            }

        } else {
            if (!isBTCLPersonnal && userId != -1) {

                List<NIXEFR> efrList = efrService.getEFRByVendor(userDTO.getUserID());
                for (NIXEFR efr : efrList) {

                    applicationIDs.add(efr.getApplication());
                }

                if (applicationIDs.size() > 0) {

                    rs = getResultSetBySqlPair(
                            new NIXApplicationConditionBuilder()
                                    .selectId()
                                    .fromNIXApplication()
                                    .Where()
                                    .idIn(applicationIDs)//changed by bony
                                    .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-from")))
                                    .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-to")))
                                    .typeEqualsString(criteria.get("applicationType"))
                                    .clientInSqlPair(
                                            new ClientDTOConditionBuilder()
                                                    .selectClientID()
                                                    .fromClientDTO()
                                                    .Where()
                                                    .loginNameBothLike(criteria.get("clientName"))
                                                    .getNullableSqlPair()
                                    )
                                    .clientEqualsString(criteria.get("clientID"))
                                    .stateEqualsString(criteria.get("applicationStatus"))
                                    .demandNoteEqualsString(criteria.get("invoiceID"))
                                    .stateIn(states)//states
                                    .orderByidDesc()
                                    .getSqlPair()
                    );
                }
            }
            else {
                rs = getResultSetBySqlPair(
                        new NIXApplicationConditionBuilder()
                                .selectId()
                                .fromNIXApplication()
                                .Where()
//				                     .applicationIDEqualsString(criteria.get("applicationID"))
//                                   .applicationIDIn(applicationIDs)//changed by bony
                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-from")))
                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-to")))
                                .typeEqualsString(criteria.get("type"))
                                .clientInSqlPair(
                                        new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair()
                                )
                                .clientEqualsString(criteria.get("clientID"))
                                .stateEqualsString(criteria.get("applicationStatus"))
                               .demandNoteEqualsString(criteria.get("invoiceID"))
                               // .isServiceStartedLike(criteria.get("serviceStarted"))
                                .zoneIn(zones)//by bony
                                .stateIn(states)//states
                                .clientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                                .orderByidDesc()
                                .getSqlPair()
                );
            }
        }
        //endregion
        List<Long> idList = new ArrayList<>();
        if (rs != null) {
            idList = getSingleColumnListByResultSet(rs, Long.class);
        }
        return idList;
    }
    public List<NIXApplication> getApplicationListByIDList(List<Long> recordIDs)throws Exception {
        return getAllObjectList(classObject, new NIXApplicationConditionBuilder()
                .Where()
                .idIn(recordIDs)
                .getCondition());
    }

    NIXApplication getApplicationById(long appId) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(NIXApplication.class,appId);
    }

    public void updateApplicaton(NIXApplication nixApplication)throws Exception {
        ModifiedSqlGenerator.updateEntity(nixApplication);
    }

    public void updateApplicatonState(long applicatonID, int applicationState) {
        try {
            NIXApplication nixApplication = getFlowApplicationByID(applicatonID);
            nixApplication.setState(applicationState);
            ModifiedSqlGenerator.updateEntity(nixApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<NIXApplication> getNIXApplicationsByDemandNoteId(long id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXApplication.class,
                new NIXApplicationConditionBuilder()
                .Where()
                .demandNoteEquals(id)
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

    public List<NIXApplication> getLLIApplicationByConnectionID(long connection)throws Exception {
        List<NIXApplication> nixApplications = ModifiedSqlGenerator.getAllObjectList(NIXApplication.class,
                new NIXApplicationConditionBuilder()
                        .Where()
                        .connectionEquals(connection)
                        .orderByidDesc()
                        .getCondition()
        );

        return nixApplications;
    }
}
