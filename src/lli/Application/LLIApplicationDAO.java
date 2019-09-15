package lli.Application;

import annotation.DAO;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.RoleConstants;
import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import flow.FlowService;
import flow.entity.FlowState;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRService;
import lli.Application.NewConnection.LLINewConnectionApplication;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplication;
import lli.LLIClientService;
import lli.connection.LLIConnectionConstants;
import location.ZoneDAO;
import login.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.UserDTO;
import user.UserRepository;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.SqlGenerator;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

//import javafx.scene.effect.Effect;
//import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplication;

public class LLIApplicationDAO {

    private static final Logger logger = LoggerFactory.getLogger(LLIApplicationDAO.class);

    private static final String SQL_GET_APPLICATION_TYPE_BY_ID = "SELECT applicationType FROM at_lli_application WHERE applicationID = ?";

    Class<LLIApplication> classObject = LLIApplication.class;
    @DAO
    ZoneDAO zoneDAO;

    FlowService flowService = ServiceDAOFactory.getService(FlowService.class);
    EFRService efrService = ServiceDAOFactory.getService(EFRService.class);


    public void updateLLIApplication(LLIApplication lliApplication) throws Exception {
        ModifiedSqlGenerator.updateEntity(lliApplication);
    }

    public LLIApplication getLLIApplicationByID(long applicationID) throws Exception {

        LLIApplication lliApplication = getObjectByID(LLIApplication.class, applicationID);

        Class<? extends LLIApplication> childApplicationClass = LLIConnectionConstants.applicationTypeClassnameMap.get(lliApplication.getApplicationType());

        String conditionString = " where " + SqlGenerator.getForeignKeyColumnName(childApplicationClass) + " = " + lliApplication.getApplicationID() + " limit 1";

        List<? extends LLIApplication> childApplications = getAllObjectList(childApplicationClass, conditionString);

        LLIApplication childApplication = (childApplications.isEmpty() ? null : childApplications.get(0));

        if (childApplication != null) {
            ModifiedSqlGenerator.populateObjectFromOtherObject(lliApplication, childApplication, LLIApplication.class);
        }

        return childApplication;
    }

    public LLIApplication getFlowLLIApplicationByID(long applicationID) throws Exception {

        //TODO commented out by raihan.
//        List<LLIApplication> lliApplications = ModifiedSqlGenerator.getAllObjectList(LLIApplication.class,
//                new LLIApplicationConditionBuilder()
//                        .Where()
//                        .applicationIDEquals(applicationID)
//                        .getCondition()
//        );
//
//        if (lliApplications.size() > 0) {
//            return lliApplications.get(0);
//        } else {
//            return new LLIApplication();
//        }
        return ModifiedSqlGenerator.getObjectByID(LLIApplication.class, applicationID);
        // it will return null for no application found cases.
    }

    //get application by connection id
    public List<LLIApplication> getLLIApplicationByConnectionID(long connectionID) throws Exception {

        List<LLIApplication> lliApplication = ModifiedSqlGenerator.getAllObjectList(LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .connectionIdEquals((int) connectionID)
                        .orderByapplicationIDDesc()
                        .getCondition()
        );

        return lliApplication;
    }
    //end of application by connection id

    public LLIApplication getLLIApplicationDetailsByID(long applicationID) throws Exception {

        LLIApplication lliApplication = getObjectByID(LLIApplication.class, applicationID);


        return lliApplication;
    }


    public LLIApplication getChildLLIApplicationByForeignKey(long foreignKey, Class<? extends LLIApplication> childClassObject) throws Exception {
        String conditionString = " where " + SqlGenerator.getForeignKeyColumnName(childClassObject) + " = " + foreignKey + " limit 1";

        List<? extends LLIApplication> childApplications = getAllObjectList(childClassObject, conditionString);

        LLIApplication childApplication = (childApplications.isEmpty() ? null : childApplications.get(0));

        return childApplication;
    }


    public void insertLLIApplication(LLIApplication lliApplication) throws Exception {
        insert(lliApplication);
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> criteria, LoginDTO loginDTO) throws Exception {

        long roleID = loginDTO.getRoleID();//state find with role
        boolean isBTCLPersonnal = false;
        int userId = (int) loginDTO.getUserID();
        ArrayList<Integer> zones = zoneDAO.getUserZone(userId);
        ArrayList<Integer> states = new ArrayList<>();
        ClientDTO clientDTO = new ClientDetailsDTO();
        UserDTO userDTO = null;
        try{
            if(loginDTO.getUserID()>0) {
                userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
            }
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
            clientDTO = new LLIClientService().getLLIClient(loginDTO.getAccountID());
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

            flowStates = flowService.getStatesByRole((int) loginDTO.getRoleID());

            //todo : need to fetch end states


        } else {
            flowStates = flowService.getStates();
        }
        for (FlowState flowState : flowStates
        ) {

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

//                if (!isBTCLPersonnal && userId != -1 ) {
                if (loginDTO.getRoleID()== RoleConstants.VENDOR_ROLE) {

//                  List<EFR> efrList = efrService.getEFRByVendorAndAppID(Long.parseLong(id), userId);
                    List<EFR> efrList = efrService.efrByVendorQuationNotCompletedandAppId(Long.parseLong(id), userId);
                    List<EFR> efrList2 = efrService.getWorkGivenByVendor(userDTO.getUserID());

                    if (efrList.size() > 0) {
                        for (EFR efr : efrList) {

                            applicationIDs.add(efr.getApplicationID());
                        }
                    }
                    if (efrList2.size() > 0) {
                        for (EFR efr : efrList2) {

                            applicationIDs.add(efr.getApplicationID());
                        }
                    }


                } else {
                    applicationIDs.add(Long.parseLong(id));
                }
            }
            if (applicationIDs.size() > 0) {

                rs = getResultSetBySqlPair(
                        new LLIApplicationConditionBuilder()
                                .selectApplicationID()
                                .fromLLIApplication()
                                .Where()
//				.applicationIDEqualsString(criteria.get("applicationID"))
                                .applicationIDIn(applicationIDs)//changed by bony
                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
                                .applicationTypeEqualsString(criteria.get("Type"))
                                .clientIDInSqlPair(
                                        new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair()
                                )
                                .clientIDEqualsString(criteria.get("clientID"))
//                                .statusEqualsString(criteria.get("Status"))
                                .demandNoteIDEqualsString(criteria.get("invoiceID"))
                                .isServiceStartedLike(criteria.get("serviceStarted"))
                                .zoneIdIn(zones)//by bony
                                .stateIn(states)//states
                                .clientIDEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                                .orderByapplicationIDDesc()
                                .getSqlPair()
                );
            }

        } else {
//            if (!isBTCLPersonnal && userId != -1) {
            if (loginDTO.getRoleID()==RoleConstants.VENDOR_ROLE) {

//                List<EFR> efrList = efrService.getEFRByVendor(userDTO.getUserID());
                List<EFR> efrList = efrService.getEFRQuationNotGivenByVendor(userDTO.getUserID());
                List<EFR> efrList2 = efrService.getWorkGivenByVendor(userDTO.getUserID());


                if(efrList.size()>0) {
                    for (EFR efr : efrList) {

                        applicationIDs.add(efr.getApplicationID());
                    }
                }
                if(efrList2.size()>0) {
                    for (EFR efr : efrList2) {

                        applicationIDs.add(efr.getApplicationID());
                    }
                }

                if (applicationIDs.size() > 0) {

                    rs = getResultSetBySqlPair(
                            new LLIApplicationConditionBuilder()
                                    .selectApplicationID()
                                    .fromLLIApplication()
                                    .Where()
//				.applicationIDEqualsString(criteria.get("applicationID"))
                                    .applicationIDIn(applicationIDs)//changed by bony
                                    .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-from")))
                                    .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-to")))
                                    .applicationTypeEqualsString(criteria.get("Type"))
                                    .clientIDInSqlPair(
                                            new ClientDTOConditionBuilder()
                                                    .selectClientID()
                                                    .fromClientDTO()
                                                    .Where()
                                                    .loginNameBothLike(criteria.get("clientName"))
                                                    .getNullableSqlPair()
                                    )
                                    .clientIDEqualsString(criteria.get("clientID"))
//                                    .statusEqualsString(criteria.get("Status"))
                                    .demandNoteIDEqualsString(criteria.get("invoiceID"))
                                    .isServiceStartedLike(criteria.get("serviceStarted"))
                                    .zoneIdIn(zones)//by bony
                                    .stateIn(states)//states
                                    //.clientIDEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())// this line is not commented the verndor not getting the application
                                    .orderByapplicationIDDesc()
                                    .getSqlPair()
                    );
                }


            }
            else {

                rs = getResultSetBySqlPair(
                        new LLIApplicationConditionBuilder()
                                .selectApplicationID()
                                .fromLLIApplication()
                                .Where()
//				                     .applicationIDEqualsString(criteria.get("applicationID"))
//                                   .applicationIDIn(applicationIDs)//changed by bony
                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-from")))
                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("application-date-to")))
                                .applicationTypeEqualsString(criteria.get("Type"))
                                .clientIDInSqlPair(
                                        new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair()
                                )
                                .clientIDEqualsString(criteria.get("clientID"))
//                                .statusEqualsString(criteria.get("Status"))
                                .demandNoteIDEqualsString(criteria.get("invoiceID"))
                                .isServiceStartedLike(criteria.get("serviceStarted"))
                                .zoneIdIn(zones)//by bony
                                .stateIn(states)//states
                                .clientIDEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                                .orderByapplicationIDDesc()
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

    public List<LLIApplication> getLLIApplicationListByIDList(List<Long> applicationIDList) throws Exception {
        return getAllObjectList(classObject, new LLIApplicationConditionBuilder()
                .Where()
                .applicationIDIn(applicationIDList)
//                .orderByapplicationIDDesc()
                .getCondition());
    }

    public List<LLIApplication> getLLIApplicationListByIDAndZoneList(List<Long> applicationIDList, List<Long> zoneId, int roleId) throws Exception {
        return getAllObjectList(classObject, new LLIApplicationConditionBuilder()
                .Where()
                .applicationIDIn(applicationIDList).zoneIdIn(zoneId)
                .getCondition());
    }

    public LLIApplication getLLIApplicationByDemandNoteID(long demandNoteID) throws Exception {

        List<LLIApplication> lliApplications = getAllObjectList(classObject, new LLIApplicationConditionBuilder()
                .Where()
                .demandNoteIDEquals(demandNoteID)
                .getCondition());

        return lliApplications.isEmpty() ? null : lliApplications.get(0);
    }


    public void updateLLIApplicationState(long applicatonID, int applicationSate) {
        //ModifiedSqlGenerator.u
        try {
            LLIApplication lliApplication = getFlowLLIApplicationByID(applicatonID);
            lliApplication.setState(applicationSate);
            ModifiedSqlGenerator.updateEntity(lliApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertLLINewConnectionApplication(LLINewConnectionApplication lliNewConnectionApplication) throws Exception {
        insert(lliNewConnectionApplication);
    }


    public void newApplicationInsert(LLINewConnectionApplication lliNewConnectionApplication) throws Exception {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection cn = null;
//		ArrayList<Integer> zoneIds=new ArrayList<>();
//		String sql="Select zone_id from geo_zone_user_mapping where user_id = ? ";
        String sql = "insert into at_lli_application_new_connection(extendedApplicationID, parentApplicationID, bandwidth, connectionType, loopProvider, duration, suggestedDate) values(?,?,?,?,?,?,?)";
        try {
            cn = DatabaseManager.getInstance().getConnection();
            preparedStatement = cn.prepareStatement(sql);
            int index = 1;
            preparedStatement.setLong(index++, lliNewConnectionApplication.getExtendedApplicationID());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getExtendedApplicationID());
            preparedStatement.setDouble(index++, lliNewConnectionApplication.getBandwidth());
            preparedStatement.setInt(index++, lliNewConnectionApplication.getConnectionType());
            preparedStatement.setInt(index++, lliNewConnectionApplication.getLoopProvider());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getDuration());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getSuggestedDate());

            preparedStatement.executeUpdate();

//			resultSet=preparedStatement.executeQuery();
//			while (resultSet.next()){
//				zoneIds.add(resultSet.getInt("zone_id"));
//			}
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cn != null) {
                cn.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
//		return zoneIds;

    }

    public void newUpgradeInsert(LLIUpgradeBandwidthApplication lliNewConnectionApplication) throws Exception {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection cn = null;
//		ArrayList<Integer> zoneIds=new ArrayList<>();
//		String sql="Select zone_id from geo_zone_user_mapping where user_id = ? ";
        String sql = "insert into at_lli_application_upgrade_bandwidth(extendedApplicationID, parentApplicationID,connectionID, bandwidth,suggestedDate) values(?,?,?,?,?)";
        try {
            cn = DatabaseManager.getInstance().getConnection();
            preparedStatement = cn.prepareStatement(sql);
            int index = 1;
            preparedStatement.setLong(index++, lliNewConnectionApplication.getExtendedApplicationID());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getExtendedApplicationID());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getConnectionID());
            preparedStatement.setDouble(index++, lliNewConnectionApplication.getBandwidth());
            preparedStatement.setLong(index++, lliNewConnectionApplication.getSuggestedDate());

            preparedStatement.executeUpdate();

//			resultSet=preparedStatement.executeQuery();
//			while (resultSet.next()){
//				zoneIds.add(resultSet.getInt("zone_id"));
//			}
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cn != null) {
                cn.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
//		return zoneIds;

    }

//    public void newApplicationLocalLoopInsert(LLIAdditionalLocalLoopApplication lliAdditionalLocalLoopApplication) throws Exception {
//
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        Connection cn = null;
////		ArrayList<Integer> zoneIds=new ArrayList<>();
////		String sql="Select zone_id from geo_zone_user_mapping where user_id = ? ";
//        String sql = "insert into at_lli_application_additional_local_loop(extendedApplicationID, parentApplicationID,connectionID,suggestedDate) values(?,?,?,?)";
//        try {
//            cn = DatabaseManager.getInstance().getConnection();
//            preparedStatement = cn.prepareStatement(sql);
//            int index = 1;
//            preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getExtendedApplicationID());
//            preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getExtendedApplicationID());
//            preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getConnectionID());
//           // preparedStatement.setDouble(index++, lliAdditionalLocalLoopApplication.getBandwidth());
//            preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getSuggestedDate());
//            //preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getO());
//
//           // preparedStatement.setLong(index++, lliAdditionalLocalLoopApplication.getSuggestedDate());
//
//
//            preparedStatement.executeUpdate();
//
////			resultSet=preparedStatement.executeQuery();
////			while (resultSet.next()){
////				zoneIds.add(resultSet.getInt("zone_id"));
////			}
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            if (cn != null) {
//                cn.close();
//            }
//            if (preparedStatement != null) {
//                preparedStatement.close();
//            }
//            if (resultSet != null) {
//                resultSet.close();
//            }
//        }
////		return zoneIds;
//
//    }


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
}
