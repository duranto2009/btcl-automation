package nix.connection;

import common.ClientDTOConditionBuilder;
import lli.LLIConnectionInstance;
import login.LoginDTO;
import nix.constants.NIXConstants;
import nix.office.NIXOfficeService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

public class NIXConnectionDAO {

    Class<NIXConnection> classObject = NIXConnection.class;
    public void insertConnection(NIXConnection nixConnection) throws Exception{
        ModifiedSqlGenerator.insert(nixConnection);
    }

    public List<NIXConnection> getConnectionByClientID(long clientId) throws Exception{
        List<NIXConnection>nixConnections = ModifiedSqlGenerator.getAllObjectList(NIXConnection.class,
                new NIXConnectionConditionBuilder()
                    .Where()
                    .statusEquals(1)
                    .clientEquals(clientId)
                    .activeToGreaterThan(System.currentTimeMillis())
                    .getCondition()
                );
        return nixConnections;
    }

    public List<NIXConnection> getActiveConnectionByClientID(long clientId) throws Exception{
        List<NIXConnection>nixConnections = ModifiedSqlGenerator.getAllObjectList(NIXConnection.class,
                new NIXConnectionConditionBuilder()
                    .Where()
                    .statusEquals(1)
                    .clientEquals(clientId)
                    .validToEquals(Long.MAX_VALUE)
                    .activeToEquals(Long.MAX_VALUE)
                    .getCondition()
                );
        return nixConnections;
    }

    public List<NIXConnection> getConnectionByConnectionID(long connectionId)throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXConnection.class,new NIXConnectionConditionBuilder()
                            .Where()
                            .idEquals(connectionId)
                            .validToEquals(Long.MAX_VALUE) // Active Connection
                            .activeToEquals(Long.MAX_VALUE) // Latest History
                            .limit(1)
                            .getCondition());
    }

    public void update(NIXConnection nixConnection) throws Exception{
        ModifiedSqlGenerator.updateEntity(nixConnection);
    }


    public List<NIXConnection> getNIXConnectionInstanceListByDateRange(long fromDate, long toDate) throws Exception {

        return getAllObjectList(classObject, new NIXConnectionConditionBuilder()
                .Where()
                .activeFromLessThan(toDate)
                .activeToGreaterThan(fromDate)
                .validToEquals(Long.MAX_VALUE)
                .getCondition())
                ;


    }

    public List<NIXConnection> getNIXConnectionListByConnectionIDList(List<Long> connectionIDList) throws Exception{
        return getAllObjectList(classObject,
                new NIXConnectionConditionBuilder()
                        .Where()
                        .idIn(connectionIDList)
//					.activeToEquals(Long.MAX_VALUE)//again change by bony
                        .activeToGreaterThan(System.currentTimeMillis())
                        .validToEquals(Long.MAX_VALUE)
                        .orderByidAsc()
                        .getCondition()
        );
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String,String> searchCriteria, LoginDTO loginDTO) throws Exception{
        ResultSet rs = getResultSetBySqlPair(
                new NIXConnectionConditionBuilder()
                        .selectId()
                        .fromNIXConnection()
                        .Where()
                        .clientEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
                        .clientInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameLeftLike(searchCriteria.get("clientName"))
                                        .getNullableSqlPair()
                        )
                        .nameLeftLike(searchCriteria.get("connectionName"))
                        .idEqualsString(searchCriteria.get("connectionID"))
//				.activeToEquals(Long.MAX_VALUE)// todo: how can this show temporary connection? i change it to below line(bony)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .validToEquals(Long.MAX_VALUE)
                        .statusEqualsString(searchCriteria.get("Status"))
                        .orderByidAsc()
                        .getSqlPair()
        );


        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public List<NIXConnection> getNIXConnectionInstanceListByHistoryID(long historyID) throws Exception {

        return getAllObjectList(classObject, new NIXConnectionConditionBuilder()
                .Where()
                .connectionIdEquals(historyID)
                .getCondition());
    }

    public List<Long> getCurrentConnectionIDListByClientID(long clientID) throws Exception{
        return getSingleColumnListByResultSet(getResultSetBySqlPair(
                new NIXConnectionConditionBuilder()
                        .selectId()
                        .fromNIXConnection()
                        .Where()
                        .clientEquals(clientID)
                        .statusIn(Arrays.asList(NIXConstants.STATUS_ACTIVE,NIXConstants.STATUS_TD))
                        .activeToGreaterThan(System.currentTimeMillis())
                        .validToEquals(Long.MAX_VALUE)
                        .getSqlPair()
        ), Long.class);
    }
}
