package lli.Application.ownership;

import annotation.DAO;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import flow.FlowService;
import flow.entity.FlowState;
import lli.LLIClientService;
import location.ZoneDAO;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

public class LLIOwnerChangeDAO {

    @DAO
    ZoneDAO zoneDAO;
    FlowService flowService = ServiceDAOFactory.getService(FlowService.class);


    public void insert(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication) throws Exception {
        ModifiedSqlGenerator.insert(lliOwnerShipChangeApplication);
    }

    public Collection getIDsWithSearchCriteria(Hashtable<String, String> criteria, LoginDTO loginDTO) throws Exception {
        boolean isBTCLPersonnal = false;
        int userId = (int) loginDTO.getUserID();
        ArrayList<Integer> zones = zoneDAO.getUserZone(userId);
//        ArrayList<Integer> states = new ArrayList<>();
        ClientDTO clientDTO = new ClientDetailsDTO();

        // TODO: 2/6/2019 actionable client identifier filter should be added
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
                applicationIDs.add(Long.parseLong(id));
            }
            if (applicationIDs.size() > 0) {
                rs = getResultSetBySqlPair(
                        new LLIOwnerShipChangeApplicationConditionBuilder()
                                .selectId()
                                .fromLLIOwnerShipChangeApplication()
                                .Where()
                                .idIn(applicationIDs)//changed by bony

                                .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
                                .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
                                .typeEqualsString(criteria.get("applicationType"))
                                .srcClientInSqlPair(
                                        new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair()
                                )
                                .Or(new LLIOwnerShipChangeApplicationConditionBuilder()
                                        .dstClientInSqlPair(new ClientDTOConditionBuilder()
                                                .selectClientID()
                                                .fromClientDTO()
                                                .Where()
                                                .loginNameBothLike(criteria.get("clientName"))
                                                .getNullableSqlPair())
                                        .getNullableSqlPair())
                                //.dstClientEqualsString(criteria.get("clientID"))
                                .stateEqualsString(criteria.get("applicationStatus"))
                                .demandNoteEqualsString(criteria.get("invoiceID"))
//                                .stateIn(states)//states
                                .zoneIn(zones)
                                .orderByidDesc()
                                .getSqlPair()
                );
            }

        } else {

            rs = getResultSetBySqlPair(
                    new LLIOwnerShipChangeApplicationConditionBuilder()
                            .selectId()
                            .fromLLIOwnerShipChangeApplication()
                            .Where()
                            .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
                            .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
                            .idEqualsString(criteria.get("connectionID"))
                            .typeEqualsString(criteria.get("type"))
                            .srcClientInSqlPair(
                                    new ClientDTOConditionBuilder()
                                            .selectClientID()
                                            .fromClientDTO()
                                            .Where()
                                            .loginNameBothLike(criteria.get("clientName"))
                                            .getNullableSqlPair()
                            )
                            .Or(new LLIOwnerShipChangeApplicationConditionBuilder()
                                    .dstClientInSqlPair(new ClientDTOConditionBuilder()
                                            .selectClientID()
                                            .fromClientDTO()
                                            .Where()
                                            .loginNameBothLike(criteria.get("clientName"))
                                            .getNullableSqlPair())
                                    .getNullableSqlPair())
                            .stateEqualsString(criteria.get("applicationStatus"))
                            .demandNoteEqualsString(criteria.get("invoiceID"))
//                            .stateIn(states)//states
                            .srcClientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                            .Or(new LLIOwnerShipChangeApplicationConditionBuilder()
                                    .dstClientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())
                                    .getNullableSqlPair())
                            .zoneIn(zones)
                            .orderByidDesc()
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


    public List<LLIOwnerShipChangeApplication> getApplicationListByIDList(List<Long> recordIDs) throws Exception {
        return getAllObjectList(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .idIn(recordIDs)
                        .getCondition()
        );
    }

    public List<LLIOwnerShipChangeApplication> getApplicationById(long applicationID) throws Exception {
        return getAllObjectList(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .idEquals(applicationID)
                        .getCondition()
        );
    }

    public void updateApplicatonState(long appID, int state) {
        try {
            LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = getFlowApplicationByID(appID);
            lliOwnerShipChangeApplication.setState(state);
            ModifiedSqlGenerator.updateEntity(lliOwnerShipChangeApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LLIOwnerShipChangeApplication getFlowApplicationByID(long applicationID) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(LLIOwnerShipChangeApplication.class, applicationID);
    }

    public void updateApplicaton(LLIOwnerShipChangeApplication lliOwnerShipChangeApplication) throws Exception {
        ModifiedSqlGenerator.updateEntity(lliOwnerShipChangeApplication);
    }

    public List<LLIOwnerShipChangeApplication> getApplicationByDemandNoteId(long id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .demandNoteEquals(id)
                        .getCondition());
    }
}
