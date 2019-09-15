package lli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.NumberComparator;
import util.ServiceDAOFactory;
import util.TimeConverter;
import util.TransactionType;

public class LLILongTermService implements NavigationService {

    @DAO
    LLILongTermContractDAO lliLongTermContractDAO;

    @Transactional
    public void changeOwnerShip(long prevClientID, long newClientID, long activeFrom) throws Exception {
        List<LLILongTermContract> lliLongTermContracts = getActiveLLILongTermContractListByClientID(prevClientID);

        for (LLILongTermContract lliLongTermContract : lliLongTermContracts) {
            lliLongTermContract.setStatus(LLILongTermContract.STATUS_OWNERSHIP_CHANGED);
            lliLongTermContract.setActiveFrom(activeFrom);

            updateExistingLongTermContract(lliLongTermContract);

            lliLongTermContract.setStatus(LLILongTermContract.STATUS_ACTIVE);
            lliLongTermContract.setClientID(newClientID);

            insertLongTermContract(lliLongTermContract);
        }
    }

    private long insertLongTermContract(LLILongTermContract lliLongTermContract) throws Exception {

        long contractID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLILongTermContract.class));
        lliLongTermContract.setID(contractID);
        lliLongTermContract.setStatus(LLILongTermContract.STATUS_ACTIVE);
        insertLongTermContractInstance(lliLongTermContract);
        return lliLongTermContract.getID();
    }

    private void insertLongTermContractInstance(LLILongTermContract lliLongTermContract) throws Exception {
        lliLongTermContract.setContractEndDate(lliLongTermContract.getContractStartDate() + 5 * TimeConverter.MILLISECONDS_IN_YEAR);
        lliLongTermContract.setActiveTo(Long.MAX_VALUE);
        lliLongTermContract.setValidFrom(CurrentTimeFactory.getCurrentTime());
        lliLongTermContract.setValidTo(Long.MAX_VALUE);
        lliLongTermContractDAO.insertLongTermContract(lliLongTermContract);
    }

    @Transactional
    public long insertNewLongTermContract(LLILongTermContract lliLongTermContract) throws Exception {
        //lliLongTermContract.setActiveFrom(CurrentTimeFactory.getCurrentTime());
        insertLongTermContract(lliLongTermContract);
        return lliLongTermContract.getID();
    }

    @Transactional
    public long insertNewLongTermContractForMigration(LLILongTermContract lliLongTermContract) throws Exception {
        insertLongTermContract(lliLongTermContract);
        return lliLongTermContract.getID();
    }

    private boolean isSameContract(LLILongTermContract newLongTermContract, LLILongTermContract oldLongTermContract) {
        return newLongTermContract.getBandwidth() == oldLongTermContract.getBandwidth()
                && newLongTermContract.getStatus() == oldLongTermContract.getStatus();
    }

    @Transactional
    public void breakLongTermByLongTermIDAndBandwidth(long longTermID, double bandwidth, long contractBreakingTime) throws Exception {
        LLILongTermContract lliLongTermContract = getLLILongTermContractByLongTermContractID(longTermID);
        if (lliLongTermContract == null) {
            throw new RequestFailureException("No long term contract found with contract ID " + longTermID
                    + ". So long term contract can not be broken.");
        }

        if (lliLongTermContract.getStatus() == LLILongTermContract.STATUS_BROKEN) {
            throw new RequestFailureException("The lli long term contract has already been broken.");
        }


        if (lliLongTermContract.getBandwidth() < bandwidth) {
            throw new RequestFailureException("The exising long term contract bandwidth is " + lliLongTermContract.getBandwidth()
                    + "MB. So " + bandwidth + " MB bandwidth can not be broken");
        }
        if (bandwidth < 0) {
            throw new RequestFailureException("Long Term Break Bandwidth Can Not Be Negative.");
        }

        if (NumberComparator.isEqual(lliLongTermContract.getBandwidth(), bandwidth)) {
            // total contract broken
            lliLongTermContract.setStatus(LLILongTermContract.STATUS_BROKEN);
        } else {
            lliLongTermContract.setBandwidth(lliLongTermContract.getBandwidth() - bandwidth);
        }
        lliLongTermContract.setActiveFrom(contractBreakingTime);
        updateExistingLongTermContract(lliLongTermContract);
    }

    @Transactional
    public long updateExistingLongTermContract(LLILongTermContract newLongTermContractInstance) throws Exception {
        LLILongTermContract lastLongTermContractInstance = markLastLongTermContractAsHistory(newLongTermContractInstance);

        if (lastLongTermContractInstance.getStatus() == LLILongTermContract.STATUS_BROKEN) {
            throw new RequestFailureException("This LLI Long Term Contract(" + lastLongTermContractInstance.getID() + ") is already broken.");
        }
        if (lastLongTermContractInstance.getStatus() == LLILongTermContract.STATUS_OWNERSHIP_CHANGED) {
            throw new RequestFailureException("This LLI Long Term Contract(" + lastLongTermContractInstance.getID()
                    + ")'s lli.Application.ownership has already been changed.");
        }

        if (isSameContract(newLongTermContractInstance, lastLongTermContractInstance)) {
            throw new RequestFailureException("No change has taken place.");
        }
        setImmutableProperties(newLongTermContractInstance, lastLongTermContractInstance);
        insertLongTermContractInstance(newLongTermContractInstance);
        return newLongTermContractInstance.getHistoryID();
    }

    private LLILongTermContract markLastLongTermContractAsHistory(LLILongTermContract lliLongTermContract) throws Exception {

        LLILongTermContract lastLongTermContractInstance = lliLongTermContractDAO.getLLILongTermContractByContractID(lliLongTermContract.getID());
        if (lastLongTermContractInstance == null) {
            throw new RequestFailureException(""); // Good readable message
        }
        if (lastLongTermContractInstance.getActiveFrom() >= lliLongTermContract.getActiveFrom()) {
            throw new RequestFailureException("Contract cannot be broken before its Start Date"); // Good readable message
        }
        lastLongTermContractInstance.setActiveTo(lliLongTermContract.getActiveFrom());
        lliLongTermContractDAO.updateLLILongTerm(lastLongTermContractInstance);
        return lastLongTermContractInstance;
    }

    private void setImmutableProperties(LLILongTermContract newLongTermContractInstance, LLILongTermContract lastLongTermContractInstance) throws Exception {
        // to be filled up later on
    }

    @Transactional
    public void deleteLastLongTermContractByID(long ID) throws Exception {
        LLILongTermContract lastLongTermContractInstance = lliLongTermContractDAO.getLLILongTermContractByContractID(ID);
        if (lastLongTermContractInstance.getID() == lastLongTermContractInstance.getHistoryID()) {
            // this is the first long term instance

            lastLongTermContractInstance.setValidTo(CurrentTimeFactory.getCurrentTime());
            lliLongTermContractDAO.updateLLILongTerm(lastLongTermContractInstance);

        } else {
            LLILongTermContract newLastLongTermContract = lliLongTermContractDAO
                    .getLongTermContractByActiveToAndValidToTime(lastLongTermContractInstance.getID(), lastLongTermContractInstance.getActiveFrom(), Long.MAX_VALUE);
            if (newLastLongTermContract == null) {
                throw new Exception(""); // inconsistent data
            }

            newLastLongTermContract.setActiveTo(Long.MAX_VALUE);
            lliLongTermContractDAO.updateLLILongTerm(newLastLongTermContract);

            lastLongTermContractInstance.setActiveTo(CurrentTimeFactory.getCurrentTime());
            lastLongTermContractInstance.setValidTo(CurrentTimeFactory.getCurrentTime());
            lliLongTermContractDAO.updateLLILongTerm(lastLongTermContractInstance);
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLILongTermContract getLLILongTermContractInstanceByContractHistoryID(long longTermContractHistoryID) throws Exception {
        return lliLongTermContractDAO.getLLILongTermContractInstanceByContractHistoryID(longTermContractHistoryID);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLILongTermContract> getLLILongTermHistoryByContractID(long longTermContractID) throws Exception {
        return lliLongTermContractDAO.getLLILongTermHistoryListByContractID(longTermContractID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLILongTermContract> getActiveLLILongTermContractListByClientID(long clientID) throws Exception {
        return lliLongTermContractDAO.getlliLongTermContractListByClientIDAndStatus(clientID, LLILongTermContract.STATUS_ACTIVE);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLILongTermContract> getLLILongTermContractListByClientID(long clientID) throws Exception {
        return lliLongTermContractDAO.getlliLongTermContractListByClientID(clientID);
    }

    // history method
    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLILongTermContract> getLLILongTermContractHistoryByClientIDAndDateRange(long clientID, long fromDate, long toDate) throws Exception {
        return lliLongTermContractDAO.getLLILongTermContractHistoryListByClientIDAndDateRange(clientID, fromDate, toDate);
    }

    private List<LLILongTermContractSummary> filterLongTermContractAndAdjust(List<LLILongTermContractSummary> lliLongTermContracts) {
        List<LLILongTermContractSummary> filteredList = new ArrayList<>();

        for (int i = 0; i < lliLongTermContracts.size(); i++) {

            LLILongTermContractSummary lliLongTermContract = lliLongTermContracts.get(i);

            if (i == 0) {
                filteredList.add(lliLongTermContract);
            } else {
                LLILongTermContractSummary lastLLILongTermContract = lliLongTermContracts.get(lliLongTermContracts.size() - 1);
                if (TimeConverter.getStartTimeOfTheDay(lastLLILongTermContract.fromDate) != TimeConverter.getStartTimeOfTheDay(lliLongTermContract.fromDate)) {
                    //not on the same day
                    filteredList.add(lliLongTermContract);
                }
            }
        }

        return filteredList;
    }

    private void adjustTimeForLongTermContract(List<LLILongTermContractSummary> lliLongTermContracts) {
        for (int i = 0; i < lliLongTermContracts.size(); i++) {
            LLILongTermContractSummary lliLongTermContract = lliLongTermContracts.get(i);
            lliLongTermContract.fromDate = TimeConverter.getStartTimeOfTheDay(lliLongTermContract.fromDate);
        }
        for (int i = 0; i < lliLongTermContracts.size() - 1; i++) {
            lliLongTermContracts.get(i).toDate = lliLongTermContracts.get(i + 1).fromDate;
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLILongTermContract getLLILongTermContractByLongTermContractID(long longTermContractID) throws Exception {
        return lliLongTermContractDAO.getLLILongTermContractByContractID(longTermContractID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {
        return lliLongTermContractDAO.getIDListBySearchCriteria(searchCriteria, loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return lliLongTermContractDAO.getLLILongTermContractListByIDList((List<Long>) recordIDs);
    }

    /**
     * @author Dhrubo
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public double getTotalActiveLongTermBandwidthByClientID(long clientID) throws Exception {
        List<LLILongTermContract> longTermContractList = getLLILongTermContractListByClientID(clientID);
        double aggregatedBandwidth = 0;
        for (LLILongTermContract lliLongTermContract : longTermContractList) {
            aggregatedBandwidth += lliLongTermContract.getBandwidth();
        }
        return aggregatedBandwidth;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public double getTotalNonLongTermBWByClientID(long clientID) throws Exception {
        double existingActiveLongTermBandwidth = getTotalActiveLongTermBandwidthByClientID(clientID);
        double existingActiveTotalBandwidth = ServiceDAOFactory.getService(LLIConnectionService.class).getExistingTotalBandwidthByClientID(clientID);
        double existingActiveNonLongTermBandwidth = existingActiveTotalBandwidth - existingActiveLongTermBandwidth;
        return existingActiveNonLongTermBandwidth;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public double getTotalNonLongTermRegularBWByClientID(long clientID) throws Exception {
        //Excluding Cache
        double existingActiveLongTermBandwidth = getTotalActiveLongTermBandwidthByClientID(clientID);
        double existingActiveTotalBandwidth = ServiceDAOFactory.getService(LLIConnectionService.class).getTotalExistingRegularBWByClientID(clientID);
        double existingActiveNonLongTermBandwidth = existingActiveTotalBandwidth - existingActiveLongTermBandwidth;
        return existingActiveNonLongTermBandwidth;
    }

    /**
     * @author Dhrubo
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public void validateBandwidthOfNewLongTermApplication(double appliedBandwidth, long clientID) throws Exception {
        double existingActiveNonLongTermBandwidth = getTotalNonLongTermRegularBWByClientID(clientID);
        if (appliedBandwidth > existingActiveNonLongTermBandwidth) {
            throw new RequestFailureException("You do not have this much bandwidth to sign contract");
        }
    }
}
