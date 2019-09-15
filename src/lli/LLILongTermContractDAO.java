package lli;

import static util.ModifiedSqlGenerator.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.TimeConverter;

public class LLILongTermContractDAO {
    Class<LLILongTermContract> classObject = LLILongTermContract.class;

    public void insertLongTermContract(LLILongTermContract lliLongTermContract) throws Exception {
        insert(lliLongTermContract);
    }

    public void updateLLILongTerm(LLILongTermContract lliLongTermContract) throws Exception {
        updateEntity(lliLongTermContract);
    }

    // always returns the last entry
    public LLILongTermContract getLLILongTermContractByContractID(long contractID) throws Exception {
        List<LLILongTermContract> lliLongTermContracts = getAllObjectList(classObject,
                new LLILongTermContractConditionBuilder()
                        .Where()
                        .activeToEquals(Long.MAX_VALUE)
                        .validToEquals(Long.MAX_VALUE)
                        .IDEquals(contractID)
                        .limit(1)
                        .getCondition()
        );
        return lliLongTermContracts.isEmpty() ? null : lliLongTermContracts.get(0);
    }

    public List<Long> getIDListBySearchCriteria(Hashtable<String, String> searchCriteria, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new LLILongTermContractConditionBuilder()
                        .selectID()
                        .fromLLILongTermContract()
                        .Where()
                        .IDEqualsString(searchCriteria.get("ID"))
                        .clientIDInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(searchCriteria.get("clientName"))
                                        .getNullableSqlPair()
                        )
                        .contractStartDateGreaterThanEquals(TimeConverter.getDateFromString(searchCriteria.get("contractStartDateFrom")))
                        .contractStartDateLessThan(TimeConverter.getNextDateFromString(searchCriteria.get("contractStartDateTo")))
                        .contractEndDateGreaterThanEquals(TimeConverter.getDateFromString(searchCriteria.get("contractEndDateFrom")))
                        .contractEndDateLessThan(TimeConverter.getNextDateFromString(searchCriteria.get("contractEndDateTo")))
                        .statusEqualsString(searchCriteria.get("status"))
                        .activeToEquals(Long.MAX_VALUE)
                        .validToEquals(searchCriteria.contains("showDeleted") ? (searchCriteria.get("showDeleted").equals("0") ? Long.MAX_VALUE : null) : null)
                        .clientIDEquals(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public LLILongTermContract getLongTermContractByActiveToAndValidToTime(long ID, long activeTo, long validTo) throws Exception {
        List<LLILongTermContract> lliLongTermContracts = getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .IDEquals(ID)
                .activeFromEquals(activeTo)
                .validToEquals(validTo)
                .limit(1)
                .getCondition()
        );
        return lliLongTermContracts.isEmpty() ? null : lliLongTermContracts.get(0);
    }

    public List<LLILongTermContract> getLLILongTermHistoryListByContractID(long longTermContractID) throws Exception {
        return getObjectListByResultSet(classObject, getResultSetBySqlPair(new LLILongTermContractConditionBuilder()
                .selectStatus()
                .selectClientID()
                .selectHistoryID()
                .selectActiveFrom()
                .selectValidTo()
                .fromLLILongTermContract()
                .Where()
                .IDEquals(longTermContractID)
                .validToEquals(Long.MAX_VALUE)
                .orderByactiveFromDesc()
                .getSqlPair()
        ));
    }

    public List<LLILongTermContractSummary> getLongTermContractHistryByFromDateAndToDateAndClientID(long fromDate, long toDate, long clientID) throws Exception {
        ResultSet rs = getResultSetBySqlPair(new LLILongTermContractConditionBuilder()
                .selectActiveFrom()
                .selectActiveTo()
                .selectBandwidth()
                .Where()
                .clientIDEquals(clientID)
                .validToEquals(Long.MAX_VALUE)
                .activeFromLessThan(toDate)
                .activeToGreaterThan(fromDate)
                .getSqlPair());

        List<LLILongTermContractSummary> lliLongTermContractSummaries = new ArrayList<>();

        while (rs.next()) {
            long fDate = rs.getLong(1);
            long tDate = rs.getLong(2);
            double bandwidth = rs.getDouble(3);
            LLILongTermContractSummary lliLongTermContractSummary = new LLILongTermContractSummary(fDate, tDate, bandwidth);
            lliLongTermContractSummaries.add(lliLongTermContractSummary);
        }

        return lliLongTermContractSummaries;
    }


    public List<LLILongTermContract> getLLILongTermContractListByIDList(List<Long> idList) throws Exception {
        return getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .IDIn(idList)
                .validToEquals(Long.MAX_VALUE)
                .activeToEquals(Long.MAX_VALUE)
                .getCondition());
    }

    public List<LLILongTermContract> getlliLongTermContractListByClientIDAndStatus(long clientID, int status) throws Exception {
        return getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .clientIDEquals(clientID)
                .activeToEquals(Long.MAX_VALUE)
                .validToEquals(Long.MAX_VALUE)
                .statusEquals(status)
                .getCondition());
    }

    public List<LLILongTermContract> getlliLongTermContractListByClientID(long clientID) throws Exception {
        return getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .clientIDEquals(clientID)
                .activeToEquals(Long.MAX_VALUE)
                .validToEquals(Long.MAX_VALUE)
                .getCondition());
    }

    public LLILongTermContract getLLILongTermContractInstanceByContractHistoryID(long longTermContractHistoryID) throws Exception {
        List<LLILongTermContract> lliLongTermContracts = getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .historyIDEquals(longTermContractHistoryID)
                .validToEquals(Long.MAX_VALUE)
                .getCondition());

        return lliLongTermContracts.isEmpty() ? null : lliLongTermContracts.get(0);
    }

    public List<LLILongTermContract> getLLILongTermContractHistoryListByClientIDAndDateRange(long clientID,
                                                                                             long fromDate, long toDate) throws Exception {
        return getAllObjectList(classObject, new LLILongTermContractConditionBuilder()
                .Where()
                .clientIDEquals(clientID)
                .validToEquals(Long.MAX_VALUE)
                .activeFromLessThan(toDate)
                .activeToGreaterThan(fromDate)
                .getCondition());
    }
}
