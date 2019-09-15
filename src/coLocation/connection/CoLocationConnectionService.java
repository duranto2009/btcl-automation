package coLocation.connection;

import annotation.DAO;
import annotation.Transactional;
import coLocation.demandNote.CoLocationBillUtilityService;
import common.RequestFailureException;
import global.GlobalService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import util.*;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Log4j
public class CoLocationConnectionService implements NavigationService {

    @DAO private CoLocationConnectionDAO coLocationConnectionDAO;


    @Transactional(transactionType = TransactionType.READONLY)
    public KeyValuePair<List<CoLocationConnectionDTO>, KeyValuePair<LocalDate, Boolean>> getActiveYearlyBillWorthyConnectionsAndNextBillDate
            (long historyId, List<CoLocationConnectionDTO>histories) {


//        List<CoLocationConnectionDTO> histories = getConnectionHistoryByHistoryID(historyId);

        CoLocationConnectionDTO firstHistory = histories.stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No History Found for connection history id " + historyId));
        LocalDate nextBillDate = CoLocationBillUtilityService.getNextBillingDateBasedOnSystemDateAndInitialHistory(firstHistory);
        LocalDate prevServiceCycleStartDate = nextBillDate.minusYears(1).plusMonths(1);
        log.info("Previous Service Cycle Start Date: " + prevServiceCycleStartDate);
        List<CoLocationConnectionDTO>filteredList = histories.stream()
                .filter(this.getBillWorthyConnections(nextBillDate, prevServiceCycleStartDate))
                .collect(Collectors.toList());
        List<CoLocationConnectionDTO> decidedList = filteredList.isEmpty() ? histories : filteredList;
        boolean isAdjustmentCalculationNeeded = !filteredList.isEmpty();
        return new KeyValuePair<>(decidedList, new KeyValuePair<>(nextBillDate, isAdjustmentCalculationNeeded)) ;

    }



    private Predicate<CoLocationConnectionDTO> getBillWorthyConnections(LocalDate nextBillDate,
                                                                        LocalDate prevServiceCycleStartDate
                                                                        ) {
        return t->{
            LocalDate startDateOfThisConnection = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(t.getStartDate()), ZoneId.systemDefault()).toLocalDate();

            return startDateOfThisConnection.isBefore(nextBillDate.plusDays(1)) && startDateOfThisConnection.isAfter(prevServiceCycleStartDate.minusDays(1));

            //TODO add condition for active connection ; considering td, reconnect
        };
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationConnectionDTO> getAllCoLocationConnection() throws Exception {
        return coLocationConnectionDAO.getAllColocationConnection();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, List<CoLocationConnectionDTO>> getAllCoLocationConnectionMappedByHistory() throws Exception {
        return getAllCoLocationConnection()
                .stream()
                .collect(Collectors.groupingBy(CoLocationConnectionDTO::getHistoryID));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationConnectionDTO getColocationConnection(long conID) throws Exception {
        return coLocationConnectionDAO.getColocationConnections(conID)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No CoLocation Connection Instance Found for Connection Id " + conID));
    }

//    @Transactional(transactionType = TransactionType.READONLY)
//    public List<CoLocationConnectionDTO> getColocationConnectionsByHistoryId(long historyId) throws Exception {
//        return coLocationConnectionDAO.getColocationConnectionsByHistoryId(historyId);
//
//    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationConnectionDTO> getColocationConnectionByClientID(long clientID) throws Exception {
        List<CoLocationConnectionDTO> coLocationConnectionDTOs = coLocationConnectionDAO.getColocationConnectionByClientID(clientID);
        if (coLocationConnectionDTOs==null) {
            throw new RequestFailureException("No Data found ");
        }

        return coLocationConnectionDTOs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationConnectionDTO> getColocationReconnectConnectionListByClientID(long clientID) throws Exception {
        log.info("-- fm: start getColocationReconnectConnectionListByClientID --");
        List<CoLocationConnectionDTO> coLocationConnectionDTOs = coLocationConnectionDAO.getColocationReconnectConnectionListByClientID(clientID);
        if (coLocationConnectionDTOs==null) {
            throw new RequestFailureException("No Temporary Disconnected Connection found for Reconnect.");
        }

        log.info("-- fm: end getColocationReconnectConnectionListByClientID --");
        return coLocationConnectionDTOs;

    }


    @Transactional
    public void insertConnection(CoLocationConnectionDTO coLocationConnectionDTO) throws Exception {
        coLocationConnectionDAO.insertCoLocationConnection(coLocationConnectionDTO);
    }


    @Transactional
    public void updateConnection(CoLocationConnectionDTO coLocationApplicationDTO) throws Exception {
        coLocationConnectionDAO.updateCoLocationConnection(coLocationApplicationDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return (List<CoLocationConnectionDTO>) coLocationConnectionDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        return coLocationConnectionDAO.getDTOListByIDList((List<Long>) recordIDs)
                .stream()
                .sorted(Comparator.comparing(CoLocationConnectionDTO::getHistoryID))
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationConnectionDTO> getConnectionHistoryByHistoryID(long historyId) throws Exception {
        return coLocationConnectionDAO.getColocationConnectionsByHistoryId(historyId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationConnectionDTO getAnyColocationConnection(long conID) throws Exception {
        return coLocationConnectionDAO.getAnyColocationConnection(conID)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No CoLocation Connection Instance Found for Connection Id " + conID));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getTotalCoLocationConnectionCount() throws Exception {
        CoLocationConnectionDTOConditionBuilder connectionConditionBuilder = new CoLocationConnectionDTOConditionBuilder();
        String selectPart = "Select count(*) as count ";
        SqlPair sqlPair = connectionConditionBuilder.fromCoLocationConnectionDTO().Where().activeToEquals(Long.MAX_VALUE).getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql;

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        rs.next();
        return rs.getInt("count");
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationConnectionDTO> getTotalCoLocationConnectionCountByClient(long clientId) throws Exception {

        return ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                CoLocationConnectionDTO.class, new CoLocationConnectionDTOConditionBuilder()
                .Where()
                .clientIDEquals(clientId)
                .activeToGreaterThanEquals(CurrentTimeFactory.getCurrentTime())
                .getCondition()
        );
    }

}
