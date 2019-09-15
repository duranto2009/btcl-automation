package nix.connection;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import global.GlobalService;
import login.LoginDTO;
import nix.constants.NIXConstants;
import nix.office.NIXOfficeService;
import util.*;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class NIXConnectionService implements NavigationService {
    @DAO
    NIXConnectionDAO nixConnectionDAO;

    NIXOfficeService nixOfficeService= ServiceDAOFactory.getService(NIXOfficeService.class);

    @Transactional
    public void insertConnection(NIXConnection nixConnection)throws Exception {
        nixConnectionDAO.insertConnection(nixConnection);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXConnection> getNIXConnectionInstanceListByDateRange(long fromDate, long toDate) throws Exception {

        List<NIXConnection> nixConnections = nixConnectionDAO
                .getNIXConnectionInstanceListByDateRange(fromDate, toDate)
                .stream()
                .map(t ->
                {
                    try {
                        t.setNixOffices(nixOfficeService.getOfficesByConnectionID(t.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                }).collect(Collectors.toList());
//
//        populateNIXConnectionInstancesWithOfficeAndLocalLoop(lliConnectionInstanceList);

        return nixConnections;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXConnection> getNIXConnectionInstanceListByHistoryID(long historyID) throws Exception {

        List<NIXConnection> nixConnections = nixConnectionDAO
                .getNIXConnectionInstanceListByHistoryID(historyID)
                .stream()
                .map(t ->
                {
                    try {
                        t.setNixOffices(nixOfficeService.getOfficesByConnectionID(t.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                }).collect(Collectors.toList());


        return nixConnections;
    }

    @Transactional
    public List<NIXConnection> getConnectionByClientID(long clientId) throws Exception{
        return nixConnectionDAO.getConnectionByClientID(clientId);
    }

    @Transactional
    public List<NIXConnection> getActiveConnectionByClientID(long clientId) throws Exception{
        return nixConnectionDAO.getActiveConnectionByClientID(clientId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXConnection getConnnectionByConnectionId(long connectionId)throws Exception{
        List<NIXConnection>nixConnections = nixConnectionDAO.getConnectionByConnectionID(connectionId);
        if(nixConnections.isEmpty()) {
            throw new RequestFailureException("No NIX Connection Instance Found for Connection Id : " + connectionId);
        }
        else{
            nixConnections.get(0)
                    .setNixOffices(ServiceDAOFactory.getService(NIXOfficeService.class)
                            .getOfficesByConnectionID(nixConnections.get(0).getId()));
            return nixConnections.get(0);
        }
    }

    @Transactional
    public void updateConnection(NIXConnection nixConnection) throws Exception{
        nixConnectionDAO.update(nixConnection);
    }

    @Transactional
    public void reconnectionConnectionByClientID(long clientID) throws Exception{
        List<Long> connectionIDList = nixConnectionDAO.getCurrentConnectionIDListByClientID(clientID);

        if (connectionIDList.isEmpty()) {
            throw new RequestFailureException("This client has no connection for reconnection");
        }

        for (long connectionID : connectionIDList) {
            reconnectConnection(connectionID, System.currentTimeMillis());
        }
    }

    @Transactional
    private void reconnectConnection(long nixConnectionID, long reconnectionTime) throws Exception {
        NIXConnection lastNIXConnectionInstance = getConnnectionByConnectionId(nixConnectionID);
        if (lastNIXConnectionInstance == null) {
            throw new RequestFailureException("No connection found with connection ID " + nixConnectionID + ". The connection with connection with connection ID "
                    + nixConnectionID + " can not be reconnected.");
        }
        lastNIXConnectionInstance.setStatus(NIXConstants.STATUS_ACTIVE);
        lastNIXConnectionInstance.setActiveFrom(reconnectionTime);
        lastNIXConnectionInstance.setIncidentId(NIXConstants.NIX_RECONNECT);
        updateConnection(lastNIXConnectionInstance);
    }

    @Transactional
    public void tdNIXConnectionByClientID(long clientID, long tdActivationDate)throws Exception {
        List<Long> tdConnectionIDList = nixConnectionDAO.getCurrentConnectionIDListByClientID(clientID);

        if (tdConnectionIDList.isEmpty()) {
            throw new RequestFailureException("This client has no lli connection for TD.");
        }

        for (long connectionID : tdConnectionIDList) {

            tdConnectionByConnectionID(connectionID, tdActivationDate);
        }
    }


    @Transactional
    private void tdConnectionByConnectionID(long nixConnectionID, long tdActivationDate) throws Exception {
        NIXConnection lastNIXConnectionInstance = getConnnectionByConnectionId(nixConnectionID);
        if (lastNIXConnectionInstance == null) {
            throw new RequestFailureException("No lli connection found with connection ID " + nixConnectionID);
        }
        if (lastNIXConnectionInstance.getStatus() != NIXConstants.STATUS_ACTIVE) {
            throw new RequestFailureException("LLI connection with ID " + nixConnectionID
                    + " is not in active state.So this connection can not be temporalily disconnected.");
        }
        lastNIXConnectionInstance.setActiveFrom(tdActivationDate);
        lastNIXConnectionInstance.setStatus(NIXConstants.STATUS_TD);
        lastNIXConnectionInstance.setIncidentId(NIXConstants.NIX_TD);
        updateConnection(lastNIXConnectionInstance);
    }


    /**
     * @author forhad
     */
    @SuppressWarnings("rawtypes")
    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO, objects);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return nixConnectionDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return nixConnectionDAO.getNIXConnectionListByConnectionIDList((List) recordIDs);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public NIXConnection getLatestNIXConnectionByConnectionHistoryId(long connectionId) throws Exception {
        return getNIXConnectionInstanceListByHistoryID(connectionId)
                .stream()
                .reduce((f, s)->s)
                .orElseThrow(()->new RequestFailureException("No Data Found with NIX connection history id " + connectionId));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getTotalNIXConnectionCount() throws Exception {
        NIXConnectionConditionBuilder nixConnectionBuilder = new NIXConnectionConditionBuilder();
        String selectPart = "Select count(*) as count ";
        SqlPair sqlPair = nixConnectionBuilder.fromNIXConnection().Where().activeToEquals(Long.MAX_VALUE).getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql;

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        rs.next();
        return rs.getInt("count");
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXConnection> getTotalNIXConnectionCountByClient(long clientId ) throws Exception {

        return  ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                NIXConnection.class, new NIXConnectionConditionBuilder()
                .Where()
                .clientEquals(clientId)
                .activeToGreaterThan(CurrentTimeFactory.getCurrentTime())
                .getCondition()
        );
    }
}
