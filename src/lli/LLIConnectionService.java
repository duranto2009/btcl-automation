package lli;

import annotation.DAO;
import annotation.Transactional;
import common.EntityTypeConstant;
import common.RequestFailureException;
import common.StringUtils;
import global.GlobalService;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.Application.FlowConnectionManager.LLIConnectionConditionBuilder;
import lli.Application.LocalLoop.LocalLoop;
import lli.client.td.LLIClientTDService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import org.apache.commons.collections.CollectionUtils;
import requestMapping.Service;
import user.UserService;
import util.*;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


public class LLIConnectionService implements NavigationService {
    @DAO private LLIConnectionDAO lliConnectionDAO;
    @Service private LLIOfficeService lliOfficeService;
    @Service private LLILocalLoopService lliLocalLoopService;
    @Service private InventoryService inventoryService;
    @Service private LLILongTermService lliLongTermService;
    @Service private LLIClientTDService LLIClientTDService;
	@Service private GlobalService globalService;

    @Transactional
    public long insertNewLLIConnection(LLIConnectionInstance connectionInstance) throws Exception {
        long connectionID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(
                        ModifiedSqlGenerator.getTableName(LLIConnectionInstance.class));

        connectionInstance.setID(connectionID);
        connectionInstance.setStartDate(System.currentTimeMillis());
        connectionInstance.setStatus(LLIConnectionInstance.STATUS_ACTIVE);
        insertLLIConnectionSnapshot(connectionInstance);

        List<Long> vlanIDsToBeAllocated = getVlanIDListByConnectionInstance(connectionInstance);
        allocateVlansByVlanIDListAndClientIDAndConnectionID(vlanIDsToBeAllocated, connectionInstance.getClientID(), connectionInstance.getID());

        return connectionInstance.getID();
    }

    private void allocateIpAddresses(List<?> ipList) throws Exception {

    }

    private void deallocateIpAddress(List<?> ipList) throws Exception {

    }

    private List<?> getIpAddressListByConnectionInstance(LLIConnectionInstance lliConnectionInstance) throws Exception {
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    private void updateIpAddressByNewConnectionInstanceAndLastConnectionInstance(LLIConnectionInstance newConnectionInstance,
            LLIConnectionInstance lastConnectionInstance) throws Exception {
        List<?> newIpAddresses = getIpAddressListByConnectionInstance(newConnectionInstance);
        List<?> lastAllocatedIpAddresses = getIpAddressListByConnectionInstance(lastConnectionInstance);

        List<?> ipAddressesToBeAllocated = (List<?>) CollectionUtils.subtract(lastAllocatedIpAddresses, newIpAddresses);
        List<?> ipAddressesToBeDeallocated = (List<?>) CollectionUtils.subtract(lastAllocatedIpAddresses, newIpAddresses);
        allocateIpAddresses(ipAddressesToBeAllocated);
        deallocateIpAddress(ipAddressesToBeDeallocated);

    }


    private void allocateVlansByVlanIDListAndClientIDAndConnectionID(List<Long> vlanIDs, long clientID, long connectionID) throws Exception {
        // to be implemented later

        InventoryService inventoryService = new InventoryService();

        for (long vlanID : vlanIDs) {
            if (vlanID == -1) {
                continue;
            }
            inventoryService.markInventoryItemAsUsedByOccupierInformation(vlanID, connectionID,
                    EntityTypeConstant.LLI_LINK, clientID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
        }


    }

    private void deallocateVlansByVlanIDList(List<Long> vlanIDs) throws Exception {
        // to be implemented later

        InventoryService inventoryService = new InventoryService();
        for (long vlanID : vlanIDs) {
            inventoryService.markInventoryItemAsUnusedByItemID(vlanID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
        }


    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void updateVlanIDsByNewConnectionInstanceAndLastInstance(LLIConnectionInstance newConnectionInstance
            , LLIConnectionInstance lastConnectionInstance) throws Exception {
        List<Long> newVlanIDs = getVlanIDListByConnectionInstance(newConnectionInstance);
        List<Long> lastAllocatedVlanIDs = getVlanIDListByConnectionInstance(lastConnectionInstance);

        List<Long> vlansToBeAllocated = (List) CollectionUtils.subtract(newVlanIDs, lastAllocatedVlanIDs);
        List<Long> vlansToBeDeallocated = (List) CollectionUtils.subtract(lastAllocatedVlanIDs, newVlanIDs);
        allocateVlansByVlanIDListAndClientIDAndConnectionID(vlansToBeAllocated, lastConnectionInstance.getClientID(), lastConnectionInstance.getID());
        deallocateVlansByVlanIDList(vlansToBeDeallocated);

    }

    private List<Long> getVlanIDListByConnectionInstance(LLIConnectionInstance connectionInstance) throws Exception {

        return connectionInstance
                .getLliOffices()
                .stream()
                .flatMap(
                        e -> e.getLocalLoops()
                                .stream()
                                .map(LLILocalLoop::getVlanID)
                )
                .collect(toList());
    }


    private long insertLLIConnectionSnapshot(LLIConnectionInstance connectionInstance) throws Exception {
        if (connectionInstance.getStatus() != LLIConnectionInstance.STATUS_CLOSED && connectionInstance.getStatus() != LLIConnectionInstance.OWNERSHIP_CHANGED) {
            validateLLIConnectionInstance(connectionInstance);
        }

        connectionInstance.setActiveTo(Long.MAX_VALUE);
        connectionInstance.setValidFrom(CurrentTimeFactory.getCurrentTime());
        connectionInstance.setValidTo(Long.MAX_VALUE);

        lliConnectionDAO.insertLLIConnection(connectionInstance);
        for (LLIOffice lliOffice : connectionInstance.getLliOffices()) {
            lliOffice.setConnectionHistoryID(connectionInstance.getHistoryID());
            if (lliOffice.getID() == 0) {
                lliOfficeService.insertNewLLIOffice(lliOffice);
            } else {
                lliOfficeService.updateLLIOffice(lliOffice);
            }
        }

        return connectionInstance.getHistoryID();
    }
	
    @Transactional
    public void reconnectionConnectionByClientID(long clientID) throws Exception {
        List<Long> connectionIDList = lliConnectionDAO.getCurrentConnectionIDListByClientID(clientID);

        if (connectionIDList.isEmpty()) {
            throw new RequestFailureException("This client has no connection for reconnection");
        }

        for (long connectionID : connectionIDList) {
            reconnectConnection(connectionID, System.currentTimeMillis());
        }

    }

    @Transactional
    public void tdLLIConnectionByClientID(long clientID, long tdActivationDate) throws Exception {
        List<Long> tdConnectionIDList = lliConnectionDAO.getCurrentConnectionIDListByClientID(clientID);

        if (tdConnectionIDList.isEmpty()) {
            throw new RequestFailureException("This client has no lli connection for TD.");
        }

        for (long connectionID : tdConnectionIDList) {

            tdConnectionByConnectionID(connectionID, tdActivationDate);
        }
    }

    @Transactional
    public long updateLLIConnection(LLIConnectionInstance connectionInstance) throws Exception {
        LLIConnectionInstance lastConnectionInstance = markLastConnectionAsHistroy(connectionInstance);

        if (lastConnectionInstance.getStatus() == LLIConnectionInstance.STATUS_CLOSED) {
            throw new RequestFailureException("No update is possible on connection with connection ID " + lastConnectionInstance.getID()
                    + " as the connection is already closed.");
        }
        if (lastConnectionInstance.getStatus() == LLIConnectionInstance.OWNERSHIP_CHANGED) {
            throw new RequestFailureException("No update is possible on connection with connection ID " + lastConnectionInstance.getID()
                    + " as the lli.Application.ownership of the connection has already been changed.");
        }

        setImmutablePropertiesFromLastInstance(lastConnectionInstance, connectionInstance);

//        validateOfficeIDAndLocalLoopID(connectionInstance, lastConnectionInstance);
        updateVlanIDsByNewConnectionInstanceAndLastInstance(connectionInstance, lastConnectionInstance);
        return insertLLIConnectionSnapshot(connectionInstance);
    }


    private void validateOfficeIDAndLocalLoopID(LLIConnectionInstance newConnectionInstance
            , LLIConnectionInstance previousConnectionInstance) throws Exception {

        Map<Long, LLIOffice> mapOfOfficeToOfficeID = previousConnectionInstance
                .getLliOffices()
                .stream()
                .collect(toMap(LLIOffice::getID, Function.identity()));


        Map<Long, LLILocalLoop> mapOfLocalLoopToLocalLoopID = mapOfOfficeToOfficeID
                .values()
                .stream()
                .flatMap(
                        office -> office.getLocalLoops()
                                .stream()
                )
                .collect(toMap(LLILocalLoop::getID, Function.identity()));


        for (LLIOffice lliOffice : newConnectionInstance.getLliOffices()) {

            if (lliOffice.getID() == 0) {
                continue;
            }

            LLIOffice prevOffice = mapOfOfficeToOfficeID.get(lliOffice.getID());

            if (prevOffice == null) {
                throw new RequestFailureException(""); // Good readable message
            }

            for (LLILocalLoop lliLocalLoop : lliOffice.getLocalLoops()) {
                if (lliLocalLoop.getID() != 0) {
                    if (!mapOfLocalLoopToLocalLoopID.containsKey(lliLocalLoop.getID())) {
                        throw new RequestFailureException("");
                    }
                    if (mapOfLocalLoopToLocalLoopID.get(lliLocalLoop.getID()).getLliOfficeHistoryID() != prevOffice.getHistoryID()) {
                        throw new RequestFailureException("");
                    }
                }
            }
        }
    }

    private void setImmutablePropertiesFromLastInstance(LLIConnectionInstance lastConnectionInstance
            , LLIConnectionInstance newConnectionInstance) {
        newConnectionInstance.setClientID(lastConnectionInstance.getClientID());
        newConnectionInstance.setID(lastConnectionInstance.getID());
        newConnectionInstance.setConnectionType(lastConnectionInstance.getConnectionType());
        newConnectionInstance.setName(lastConnectionInstance.getName());
        newConnectionInstance.setStartDate(lastConnectionInstance.getStartDate());
        // check connectionID and local loop id
    }


    public void populateLLIConnectionInstanceWithOfficeAndLocalLoop(LLIConnectionInstance connectionInstance) throws Exception {

       List<LLIOffice> officeList = lliOfficeService.getLLIOfficeListByConnectionHistoryID(connectionInstance.getHistoryID());
        //todo: logic change check 18-11-18
      //  List<LLIOffice> officeList = lliOfficeService.getLLIOfficeListByConnectionHistoryID(connectionInstance.getID());

		
		/*List<Long> officeHistoryIDs = new ArrayList<>();
		for(LLIOffice lliOffice: officeList){
			officeHistoryIDs.add(lliOffice.getHistoryID());
		}*/
        List<Long> officeHistoryIDs = officeList
                .stream()
                .map(LLIOffice::getHistoryID)
                .collect(toList());
				
        List<LLILocalLoop> lliLocalLoopList = lliLocalLoopService.getLocalLoopListByOfficeHistoryIDList(officeHistoryIDs);

        Map<Long, List<LLILocalLoop>> mapOfLocalLoopListToOfficeID = lliLocalLoopList
                .stream()
                .collect(groupingBy(LLILocalLoop::getLliOfficeHistoryID));
        
		for (LLIOffice office : officeList) {
            office.setLocalLoops(mapOfLocalLoopListToOfficeID.getOrDefault(office.getHistoryID(), Collections.emptyList()));
        }
        connectionInstance.setLliOffices(officeList);
    }

    private void populateLLIConnectionInstancesWithOfficeAndLocalLoop(List<LLIConnectionInstance> connectionInstances) throws Exception {

        List<Long> connectionHistoryIDs = connectionInstances
                .stream()
                .map(LLIConnectionInstance::getHistoryID)
                .collect(toList());

        List<LLIOffice> officeList = lliOfficeService.getLLIOfficeListByConnectionHistoryIDList(connectionHistoryIDs);

        List<Long> officeHistoryIDs = officeList
                .stream()
                .map(LLIOffice::getHistoryID)
                .collect(toList());


        List<LLILocalLoop> lliLocalLoopList = lliLocalLoopService.getLocalLoopListByOfficeHistoryIDList(officeHistoryIDs);

        Map<Long, List<LLILocalLoop>> mapOfLocalLoopListToOfficeID = lliLocalLoopList
                .stream()
                .collect(groupingBy(LLILocalLoop::getLliOfficeHistoryID));

        for (LLIOffice office : officeList) {
            office.setLocalLoops(mapOfLocalLoopListToOfficeID.getOrDefault(office.getHistoryID(), Collections.emptyList()));
        }


        Map<Long, List<LLIOffice>> mapOfOfficeListToConnectionID = officeList
                .stream()
                .collect(groupingBy(LLIOffice::getConnectionHistoryID));

        for (LLIConnectionInstance connectionInstance : connectionInstances) {
            connectionInstance.setLliOffices(mapOfOfficeListToConnectionID.getOrDefault(connectionInstance.getHistoryID(), Collections.emptyList()));
        }

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIConnectionInstance getLLLIConnectionInstanceByConnectionHistoryID(long historyID) throws Exception {
        LLIConnectionInstance connectionInstance = lliConnectionDAO.getConnectionInstanceByConnectionHistoryID(historyID);
        if (connectionInstance == null) {
            return null;
        }
        populateLLIConnectionInstanceWithOfficeAndLocalLoop(connectionInstance);
        return connectionInstance;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIConnectionInstance getFirstLLIConnectionInstanceAfterSpecificTime(long connectionId, long activeToTime ) throws Exception {
        LLIConnectionInstance instance = lliConnectionDAO.getFirstLLIConnectionInstanceAfterSpecificTime(connectionId, activeToTime);
        if(instance == null ) {
            throw new RequestFailureException("No Connection Instance found after the time "
                    + TimeConverter.getDateTimeStringByMillisecAndDateFormat(activeToTime, "dd/MM/yyyy"));
        }
        populateLLIConnectionInstancesWithOfficeAndLocalLoop(Arrays.asList(instance));

        return instance;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIConnectionInstance getLLIConnectionByConnectionID(long connectionID) throws Exception {

        LLIConnectionInstance connectionInstance = lliConnectionDAO.getCurrentLLIConnectionInstanceByConnectionID(connectionID);
        if (connectionInstance == null) {
            return null;
        }
        populateLLIConnectionInstanceWithOfficeAndLocalLoop(connectionInstance);
        return connectionInstance;

    }

    private LLIConnectionInstance markLastConnectionAsHistroy(LLIConnectionInstance newConnectionInstance) throws Exception {
        LLIConnectionInstance currentConnectionInstance = lliConnectionDAO
                .getCurrentLLIConnectionInstanceByConnectionID(newConnectionInstance.getID());
        if (currentConnectionInstance == null) {
            throw new RequestFailureException("No LLIConnection Found with connection ID " + newConnectionInstance.getID());
        }
        if (currentConnectionInstance.getActiveFrom() >= newConnectionInstance.getActiveFrom()) {
            throw new RequestFailureException("Current connection entry is active from "
                    + TimeConverter.getTimeStringByDateFormat(currentConnectionInstance.getActiveFrom(), TimeConverter.dateFormatWithTimeString)
                    + ". So the current entry's activation from time must be after this time.");
        }

        currentConnectionInstance.setActiveTo(newConnectionInstance.getActiveFrom());
        lliConnectionDAO.updateLLIConnectionInstance(currentConnectionInstance);
        populateLLIConnectionInstanceWithOfficeAndLocalLoop(currentConnectionInstance);
        return currentConnectionInstance;
    }

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
        return lliConnectionDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }
	
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {

        LoginDTO loginDTO = (LoginDTO) objects[0];
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        List<LLIConnectionInstance> list = lliConnectionDAO.getLLIConnectionListByConnectionIDList((List) recordIDs);

        return list.stream()
                .sorted(Comparator.comparing(LLIConnectionInstance::getHistoryID, Comparator.reverseOrder()))
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getLLIConnectionNameIDPairListByClient(long clientID) throws Exception {
        return lliConnectionDAO.getConnectionNameIDPairListByClientID(clientID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getActiveLLIConnectionNameIDPairListByClient(long clientID) throws Exception {
        return lliConnectionDAO.getActiveConnectionNameIDPairListByClientID(clientID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getTDLLIConnectionNameIDPairListByClient(long clientID) throws Exception {
        return lliConnectionDAO.getTDConnectionNameIDPairListByClientID(clientID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getLLIConnectionOfficeNameIDPairListByConnectionID(long connectionID) throws Exception {
        long connectionHistoryID = getLLIConnectionByConnectionID(connectionID).getHistoryID();
        return lliConnectionDAO.getLLIConnectionOfficeNameIDPairListByConnectionID(connectionHistoryID);
    }
	

    //jami
    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getLLIConnectionOfficeListByConnectionID(long connectionID) throws Exception {
        long connectionHistoryID = getLLIConnectionByConnectionID(connectionID).getHistoryID();
        return lliConnectionDAO.getLLIConnectionOfficeListByConnectionID(connectionHistoryID);
    }

    //end

    /********start********/
    @Transactional(transactionType = TransactionType.READONLY)
	public List<LLIConnectionInstance> getLLIConnectionInstanceListByClientIDAndDateRange(long clientID,
			long fromDate,long toDate) throws Exception{
		List<LLILocalLoop> lliLocalLoopList = lliLocalLoopService.getLocalLoopListByClientIDAndDateRange(clientID
				, fromDate, toDate);
		
		Map<Long,List<LLILocalLoop>> mapOfLocalLoopListToOfficeHistoryID = getMapOfLocalLoopListToOfficeIHistoryID(lliLocalLoopList);
		
		List<LLIOffice> lliOffices = lliOfficeService.getLLIOfficeListByClientIDAndDateInstance(clientID
				, fromDate, toDate);
		for(LLIOffice lliOffice: lliOffices) {
			lliOffice.setLocalLoops(mapOfLocalLoopListToOfficeHistoryID.get(lliOffice.getHistoryID()));
		}
		Map<Long,List<LLIOffice>> mapOfOfficeListToConnectionHistoryID = getMapOfOfficeListToConnectionHistoryID(lliOffices, 
				mapOfLocalLoopListToOfficeHistoryID);
		
		List<LLIConnectionInstance> lliConnectionInstances = lliConnectionDAO.getConnectionListByClientIDAndDateRange(clientID
				,fromDate,toDate);
		
		for(LLIConnectionInstance lliConnectionInstance: lliConnectionInstances){
			lliConnectionInstance.setLliOffices(mapOfOfficeListToConnectionHistoryID.get(lliConnectionInstance.getHistoryID()));
		}
		
		return lliConnectionInstances;
		
	}
    /*-------end--------*/
    
    
    private Map<Long, List<LLIOffice>> getMapOfOfficeListToConnectionHistoryID(List<LLIOffice> lliOffices,
            Map<Long, List<LLILocalLoop>> mapOfLocalLoopListToOfficeID) {
        return lliOffices
                .stream()
                .collect(groupingBy(LLIOffice::getConnectionHistoryID));
    }

    private Map<Long, List<LLILocalLoop>> getMapOfLocalLoopListToOfficeIHistoryID(List<LLILocalLoop> lliLocalLoopList) {
        return lliLocalLoopList
                .stream()
                .collect(Collectors.groupingBy(LLILocalLoop::getLliOfficeHistoryID));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIConnectionInstance> getConnectionHistoryListByConnectionID(long connectionID) throws Exception {
        return lliConnectionDAO.getConnectionHistoryByConnectionID(connectionID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public LLIConnectionInstance getConnectionInstanceByConnectionIDAndHistoryID(long connectionID
            , long historyID) throws Exception {
        return lliConnectionDAO.getConnectionHistoryByConnectionIDAndHistoryID(connectionID, historyID);
    }

    /********start********/
    private void  adjustTimeline(List<LLIConnectionInstance> filteredList) {
		filteredList.forEach(instance-> {
			instance.setActiveFrom(TimeConverter.getStartTimeOfTheDay(instance.getActiveFrom()));
		});
		LLIConnectionInstance prev, now;
		for(int i=1;i<filteredList.size();i++) {
			 prev = filteredList.get(i-1);
			 now = filteredList.get(i);
			 prev.setActiveTo(now.getActiveFrom());
		}
		
	}
    /*-------end--------*/
	
    /**
     * @author Dhrubo
     */
    public String getOcDetails(Long id) throws Exception {
        UserService userService = ServiceDAOFactory.getService(UserService.class);
        String ocDetails = "";
        try {
            ocDetails = "Name: " + userService.getUserDTOByUserID(id).getUsername();
        } catch (Exception e) {
            ocDetails = "Warning: Invalid O/C";
        }
        return ocDetails;
    }

    @Transactional
    public void deleteLastConnectionInstanceByConnectionID(long connectionID) throws Exception {
        LLIConnectionInstance lastConnectionInstance = getLLIConnectionByConnectionID(connectionID);
        if (lastConnectionInstance == null) {
            throw new RequestFailureException(""); // good readable message
        }
        LLIConnectionInstance newCurrentLLIConnectionInstance = lliConnectionDAO.getLLICOnnectionInstanceByActiveToTime(lastConnectionInstance.getActiveFrom());


        if (newCurrentLLIConnectionInstance == null) {
            // this is the first entry. so this deletion will delete the whole lli connection-+
            lastConnectionInstance.setValidTo(CurrentTimeFactory.getCurrentTime());
            lliConnectionDAO.updateLLIConnectionInstance(lastConnectionInstance);

            List<Long> vlansToBeDeallocated = getVlanIDListByConnectionInstance(lastConnectionInstance);
            deallocateVlansByVlanIDList(vlansToBeDeallocated);

            List<?> ipAddressesToBeDeallocated = getIpAddressListByConnectionInstance(lastConnectionInstance);
            deallocateIpAddress(ipAddressesToBeDeallocated);

        } else {
            //

            populateLLIConnectionInstanceWithOfficeAndLocalLoop(newCurrentLLIConnectionInstance);

            updateVlanIDsByNewConnectionInstanceAndLastInstance(newCurrentLLIConnectionInstance, lastConnectionInstance);
            updateIpAddressByNewConnectionInstanceAndLastConnectionInstance(newCurrentLLIConnectionInstance, lastConnectionInstance);

            newCurrentLLIConnectionInstance.setActiveTo(Long.MAX_VALUE);
            lliConnectionDAO.updateLLIConnectionInstance(newCurrentLLIConnectionInstance);


            lastConnectionInstance.setActiveTo(CurrentTimeFactory.getCurrentTime());
            lastConnectionInstance.setValidTo(CurrentTimeFactory.getCurrentTime());
            lliConnectionDAO.updateLLIConnectionInstance(lastConnectionInstance);


        }
    }

    @Transactional
    public void changeOwnerShip(long prevClientID, long newClientID, long ownerShipChangeDate) throws Exception {

        List<Long> connectionIDList = lliConnectionDAO.getConnectionIDListByClientID(prevClientID);
        changeOwnerShip(prevClientID, newClientID, connectionIDList, ownerShipChangeDate);
        lliLongTermService.changeOwnerShip(prevClientID, newClientID, ownerShipChangeDate);
    }

    @Transactional
    public void reviseConnection(LLIConnectionInstance lliConnectionInstance) throws Exception {
        LLIConnectionInstance lastLLIConnectionInstance = getLLIConnectionByConnectionID(lliConnectionInstance.getID());
        if (lastLLIConnectionInstance == null) {
            // throw exception
        }
        lliConnectionInstance.setStatus(lastLLIConnectionInstance.getStatus());
        //to forward the discount rate
      	lliConnectionInstance.setDiscountRate(lastLLIConnectionInstance.getDiscountRate());
        updateLLIConnection(lliConnectionInstance);
    }

    @Transactional
    private void tdConnectionByConnectionID(long lliConnectionID, long tdActivationDate) throws Exception {
        LLIConnectionInstance lastLLIConnectionInstance = getLLIConnectionByConnectionID(lliConnectionID);
        if (lastLLIConnectionInstance == null) {
            throw new RequestFailureException("No lli connection found with connection ID " + lliConnectionID);
        }
        if (lastLLIConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_ACTIVE) {
            throw new RequestFailureException("LLI connection with ID " + lliConnectionID
                    + " is not in active state.So this connection can not be temporalily disconnected.");
        }
        lastLLIConnectionInstance.setActiveFrom(tdActivationDate);
        lastLLIConnectionInstance.setStatus(LLIConnectionInstance.STATUS_TD);
        lastLLIConnectionInstance.setIncident(LLIConnectionConstants.TD);
        updateLLIConnection(lastLLIConnectionInstance);
    }

    @Transactional
    private void reconnectConnection(long lliConnectionID, long reconnectionTime) throws Exception {
        LLIConnectionInstance lastLLIConnectionInstance = getLLIConnectionByConnectionID(lliConnectionID);
        if (lastLLIConnectionInstance == null) {
            throw new RequestFailureException("No connection found with connection ID " + lliConnectionID + ". The connection with connection with connection ID "
                    + lliConnectionID + " can not be reconnected.");
        }
        lastLLIConnectionInstance.setStatus(LLIConnectionInstance.STATUS_ACTIVE);
        lastLLIConnectionInstance.setActiveFrom(reconnectionTime);
        lastLLIConnectionInstance.setIncident(LLIConnectionConstants.RECONNECT);
        updateLLIConnection(lastLLIConnectionInstance);
    }

    private List<LLITotalRegularBandwidthSummary> mergeTotalBandwidthSummary(List<LLITotalRegularBandwidthSummary> list) throws Exception {
        if (list.isEmpty()) {
            return list;
        }
        return mergeTotalBandwidthSummary(list, 0, list.size() - 1);
    }

    private List<LLITotalRegularBandwidthSummary> mergeTotalBandwidthSummary(List<LLITotalRegularBandwidthSummary> list, int L, int R) throws Exception {

        if (L > R) {
            throw new Exception("L must be less than or equal R");
        }
        if (R >= list.size()) {
            throw new Exception("R must be less than list.size()");
        }

        if (L > R) {
            return Collections.emptyList();
        }
        if (L == R) {
            List<LLITotalRegularBandwidthSummary> resultList = new ArrayList<>();
            resultList.add(list.get(L));
            return resultList;
        }


        int mid = (L + R) / 2;


        List<LLITotalRegularBandwidthSummary> list1 = mergeTotalBandwidthSummary(list, L, mid);
        List<LLITotalRegularBandwidthSummary> list2 = mergeTotalBandwidthSummary(list, mid + 1, R);


        int p = 0;
        int q = 0;
        List<LLITotalRegularBandwidthSummary> summaryList = new ArrayList<>();
        while (p != list1.size() && q != list2.size()) {
            if (p == list1.size()) {
                summaryList.add(list2.get(q++));
                continue;
            }
            if (q == list2.size()) {
                summaryList.add(list1.get(p++));
                continue;
            }

            LLITotalRegularBandwidthSummary firstSummary = list1.get(p);
            LLITotalRegularBandwidthSummary secondSummary = list2.get(q);

            if (firstSummary.fromDate < secondSummary.fromDate) {
                p++;
            } else {
                q++;
            }
            long newFromTime = Math.max(firstSummary.fromDate, secondSummary.fromDate);
            long newToTime = Math.min(firstSummary.toDate, secondSummary.toDate);
            if (newFromTime < newToTime) {
                double newTotalBandwidth = firstSummary.totalBandwidth + secondSummary.totalBandwidth;

                summaryList.add(new LLITotalRegularBandwidthSummary(newFromTime, newToTime, newTotalBandwidth));
            }
        }

        return summaryList;
    }

    /********start********/
    public static void checkLongTermContractAndLLIConnectionSummary(List<LLILongTermContractSummary> contractSummaryList,
			List<LLITotalRegularBandwidthSummary> lliTotalBandwidthSummaryList) throws Exception{
		
		int p = 0;
		int q = 0;
		while(p!=contractSummaryList.size() && q!=lliTotalBandwidthSummaryList.size()){
			if(p==contractSummaryList.size()){
				break;
			}
			if(q==lliTotalBandwidthSummaryList.size()){
				break;
			}
			LLILongTermContractSummary lliLongTermContractSummary = contractSummaryList.get(p);
			LLITotalRegularBandwidthSummary lliTotalRegularBandwidthSummary = lliTotalBandwidthSummaryList.get(q);
			
			if(lliLongTermContractSummary.toDate<lliTotalRegularBandwidthSummary.toDate){
				p++;
			}else{
				q++;
			}
			if(Math.max(lliLongTermContractSummary.fromDate, lliTotalRegularBandwidthSummary.fromDate)<Math.min(lliLongTermContractSummary.toDate, lliTotalRegularBandwidthSummary.toDate)){
				if(lliLongTermContractSummary.totalBandwidthContract<lliTotalRegularBandwidthSummary.totalBandwidth){
					long dateInLong = Math.max(lliLongTermContractSummary.fromDate, lliTotalRegularBandwidthSummary.fromDate);
					throw new RequestFailureException("LLI long tern contract mismatch on "+TimeConverter.getDateTimeStringFromDateTime(dateInLong));
				}
			}
			
		}
		
	}
    /*-------end--------*/
	
    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIConnectionInstance> getLLIConnectionInstanceListByClientID(long clientID) throws Exception {
        List<LLIConnectionInstance> lliConnectionInstanceList = lliConnectionDAO.getConnectionListByClientID(clientID);

        for (LLIConnectionInstance lliConnectionInstance : lliConnectionInstanceList) {
            populateLLIConnectionInstanceWithOfficeAndLocalLoop(lliConnectionInstance);
        }


        return lliConnectionInstanceList;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIConnectionInstance> getLLIConnectionInstanceList() throws Exception {
        List<LLIConnectionInstance> lliConnectionInstanceList = lliConnectionDAO.getConnectionList();

        populateLLIConnectionInstancesWithOfficeAndLocalLoop(lliConnectionInstanceList);

        return lliConnectionInstanceList;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIConnectionInstance> getLLIConnectionInstanceListByDateRange(long fromDate, long toDate) throws Exception {

        List<LLIConnectionInstance> lliConnectionInstanceList = lliConnectionDAO.getConnectionListByDateRange(fromDate, toDate);

        populateLLIConnectionInstancesWithOfficeAndLocalLoop(lliConnectionInstanceList);

        return lliConnectionInstanceList;
    }

    public List<LLITotalRegularBandwidthSummary> getTotalBandwidthSummaryByClientIDAndFromDateAndToDate(long clientID, long fromDate, long toDate) throws Exception {
        List<LLITotalRegularBandwidthSummary> lliTotalRegularBandwidthSummaries = lliConnectionDAO.getLLITotalBandwidthSummaryByFromDateAndToDate(fromDate, toDate);
        List<LLITotalRegularBandwidthSummary> mergedLLITotalRegularSummary = mergeTotalBandwidthSummary(lliTotalRegularBandwidthSummaries);
        return mergedLLITotalRegularSummary;
    }

    @Transactional
    public void closeConnectionByConnectionID(long connectionID, long closingDate) throws Exception {
        LLIConnectionInstance lliConnectionInstance = getLLIConnectionByConnectionID(connectionID);
        if (lliConnectionInstance == null) {
            throw new RequestFailureException("No connection found with connection ID " + connectionID);
        }
        if (lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_ACTIVE
                && lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_TD) {
            throw new RequestFailureException("The connection with connection ID " + connectionID + " is not in a status to be closed");
        }


        // deallocate ip address
        lliConnectionInstance.setActiveFrom(closingDate);
        lliConnectionInstance.setStatus(LLIConnectionInstance.STATUS_CLOSED);
        lliConnectionInstance.setIncident(LLIConnectionConstants.CLOSE_CONNECTION);
        lliConnectionInstance.setLliOffices(new ArrayList<>());
        lliConnectionInstance.setBandwidth(0);

        updateLLIConnection(lliConnectionInstance);

    }

    @SuppressWarnings({"unchecked"})
    @Transactional
    private void changeOwnerShip(long currentOwnerID, long nextOwnerID, List<Long> connectionIDList, long ownerShipChangeDate) throws Exception {
        List<LLIConnectionInstance> lliConnectionInstances = (List<LLIConnectionInstance>) getDTOs(connectionIDList);
        for (LLIConnectionInstance lliConnectionInstance : lliConnectionInstances) {

            if (lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_ACTIVE
                    && lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_TD) {
                continue;
            }


            populateLLIConnectionInstanceWithOfficeAndLocalLoop(lliConnectionInstance);

            List<LLIOffice> previousOfficeList = lliConnectionInstance.getLliOffices();
            double previousBandwidth = lliConnectionInstance.getBandwidth();
            // deallocate ip addresses

            lliConnectionInstance.setStatus(LLIConnectionInstance.OWNERSHIP_CHANGED);
            lliConnectionInstance.setActiveFrom(ownerShipChangeDate);
            lliConnectionInstance.setIncident(LLIConnectionConstants.CHANGE_OWNERSHIP);
            lliConnectionInstance.setBandwidth(0);
            lliConnectionInstance.setLliOffices(new ArrayList<>());
            updateLLIConnection(lliConnectionInstance);

            lliConnectionInstance.setStatus(LLIConnectionInstance.STATUS_ACTIVE);
            lliConnectionInstance.setClientID(nextOwnerID);
            lliConnectionInstance.setLliOffices(previousOfficeList);
            lliConnectionInstance.setBandwidth(previousBandwidth);
            insertNewLLIConnection(lliConnectionInstance);

        }

    }

    /********start********/
    @Transactional(transactionType = TransactionType.READONLY)
	public List<InventoryItem> getPopListByConnectionIDAndOfficeID(long connectionID,long officeID) throws Exception{
		
		
		
		return Collections.emptyList();
	}
	
	/**
	 * @author Dhrubo
	 */
	public List<LLILocalLoop> getLLILocalLoopListByConnectionInstance(LLIConnectionInstance lliConnectionInstance){
		List<LLILocalLoop> allLocalLoopUnderConnection = new ArrayList<LLILocalLoop>();
		for(LLIOffice lliOffice : lliConnectionInstance.getLliOffices()) {
			for(LLILocalLoop lliLocalLoop : lliOffice.getLocalLoops()) {
				allLocalLoopUnderConnection.add(lliLocalLoop);
			}
		}
		return allLocalLoopUnderConnection;
	}
	
	@Transactional(transactionType=TransactionType.READONLY)
	public int getBTCLProvidedLocalLoopCount(LLIConnectionInstance lliConnectionInstance) throws Exception{
		int btclProvidedLocalLoopCount = 0;
		
		for(LLIOffice lliOffice : lliConnectionInstance.getLliOffices()) {
			for(LLILocalLoop lliLocalLoop : lliOffice.getLocalLoops()) {
				if(lliLocalLoop.getBtclDistance() + lliLocalLoop.getOCDistance() > 0) {
					btclProvidedLocalLoopCount++;
				}
			}
		}
		return btclProvidedLocalLoopCount;
	}	
    /*-------end--------*/
   
   @Transactional(transactionType = TransactionType.READONLY)
    public int getBTCLProvidedLocalLoopCount(List<LocalLoop> localLoops) throws Exception {
        int btclProvidedLocalLoopCount = 0;

        if(localLoops != null){
            for (LocalLoop localLoop : localLoops) {
                if (localLoop.getBTCLDistances() + localLoop.getOCDistances() > 0) {
                    btclProvidedLocalLoopCount++;
                }
            }
        }

        return btclProvidedLocalLoopCount;
    }

    /**
     * @author Dhrubo
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public double getExistingTotalBandwidthByClientID(long clientID) throws Exception {
        return globalService.getAllObjectListByCondition(
                LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .validToEquals(Long.MAX_VALUE)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()

        ).stream()
                .mapToDouble(LLIConnectionInstance::getBandwidth)
                .sum();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<String, Double> getBandwidthStatisticsByClientId(long clientId) throws Exception {
        List<LLIConnectionInstance> instances = globalService.getAllObjectListByCondition(
                LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
                .Where()
                .clientIDEquals(clientId)
                .validToEquals(Long.MAX_VALUE)
                .activeToGreaterThan(System.currentTimeMillis())
                .getCondition()
        );

        double totalBW, totalRegularBW, totalCacheBW, totalLTBW;
        totalBW = totalRegularBW = totalCacheBW = totalLTBW = 0;
        for (LLIConnectionInstance instance : instances) {
            if(instance.getConnectionType()==LLIConnectionConstants.CONNECTION_TYPE_REGULAR){
                totalRegularBW += instance.getBandwidth();
            }
            totalBW += instance.getBandwidth();
        }

        totalCacheBW = totalBW - totalRegularBW;

        totalLTBW = globalService.getAllObjectListByCondition(
               LLILongTermContract.class , new LLILongTermContractConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .activeToEquals(Long.MAX_VALUE)
                        .validToEquals(Long.MAX_VALUE)
                        .getCondition()
        ).stream().mapToDouble(LLILongTermContract::getBandwidth)
        .sum();
        Map<String, Double> map = new HashMap<>();
        map.put("total", totalBW);
        map.put("regular", totalRegularBW);
        map.put("cache", totalCacheBW);
        map.put("LT", totalLTBW);
        return map;

    }
	
    @Transactional(transactionType=TransactionType.READONLY)
	public double getTotalExistingRegularBWByClientID(long clientID) throws Exception {
		//Excluding cache
		return globalService.getAllObjectListByCondition(
                LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .validToEquals(Long.MAX_VALUE)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()).stream()
									.filter(t->t.getConnectionType()==LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
									.mapToDouble(t->t.bandwidth).sum();
	}	
	
    @Transactional(transactionType = TransactionType.READONLY)
    public void validateLLIConnectionInstance(LLIConnectionInstance lliConnectionInstance) throws Exception {
        if (StringUtils.isBlank(lliConnectionInstance.getName())) {
            throw new RequestFailureException("Connection Name can not be empty.");
        }
        if (lliConnectionInstance.getLliOffices().isEmpty()) {
            throw new RequestFailureException("A connection must have one office.");
        }

        for (int i = 0; i < lliConnectionInstance.getLliOffices().size(); i++) {
            LLIOffice lliOffice = lliConnectionInstance.getLliOffices().get(i);

            if (StringUtils.isBlank(lliOffice.getName())) {
                throw new RequestFailureException("Name of Office " + (i + 1) + " is empty. ");
            }
            if (StringUtils.isBlank(lliOffice.getAddress())) {
                throw new RequestFailureException("Address of Office " + (i + 1) + " is empty. ");
            }

            if (lliOffice.getLocalLoops().isEmpty()) {
                throw new RequestFailureException("Office (" + lliOffice.getName() + ") has no local loop.");
            }
            for (int j = 0; j < lliOffice.getLocalLoops().size(); j++) {
                LLILocalLoop lliLocalLoop = lliOffice.getLocalLoops().get(j);
//                if (lliLocalLoop.getOfcType() != LLIConnectionConstants.OFC_TYPE_DUAL
//                        && lliLocalLoop.getOfcType() != LLIConnectionConstants.OFC_TYPE_SINGLE) {
//                    throw new RequestFailureException("Select an OFC Type for Local Loop "
//                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
//                }
                if (lliLocalLoop.getPortID() == null || inventoryService.getInventoryItemByItemID(lliLocalLoop.getPortID()).getID() == 0) {
                    throw new RequestFailureException("Port is not selected for the Local Loop "
                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
                }

                if (lliLocalLoop.getBtclDistance() < 0) {
                    throw new RequestFailureException("BTCL Distance cannot be negative in Local Loop "
                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
                }

                if (lliLocalLoop.getOCDistance() < 0) {
                    throw new RequestFailureException("OC Distance cannot be negative in Local Loop "
                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
                }

                if (lliLocalLoop.getClientDistance() < 0) {
                    throw new RequestFailureException("Client Distance cannot be negative in Local Loop "
                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
                }

//                if (lliLocalLoop.getOCDistance() > 0
//                        && lliLocalLoop.getOCID() == null) {
//                    throw new RequestFailureException("Select an OC for Local Loop "
//                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
//                }

                //todo:logic check stopped

                if (NumberComparator.isEqual(lliLocalLoop.getOCDistance(), 0.0)
                        && !(lliLocalLoop.getOCID() == null ||lliLocalLoop.getOCID()==0)) {
                    throw new RequestFailureException("There should not be any OC for Local Loop "
                            + (j + 1) + " of office (" + lliOffice.getName() + ").");
                }

            }
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getTotalLLIConnectionCount() throws Exception {
        LLIConnectionConditionBuilder lliConnectionConditionBuilder = new LLIConnectionConditionBuilder();
        String selectPart = "Select count(*) as count ";
        SqlPair sqlPair = lliConnectionConditionBuilder.fromLLIConnection().Where().activeToEquals(Long.MAX_VALUE).getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql;

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        rs.next();
        return rs.getInt("count");
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIConnectionInstance> getTotalLLIConnectionByClient(Long clientId) throws Exception {
        return ServiceDAOFactory.getService(GlobalService.class)
                .getAllObjectListByCondition(
                        LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
                        .Where()
                        .activeToGreaterThan(CurrentTimeFactory.getCurrentTime())
                        .clientIDEquals(clientId)
                        .getCondition()
                );
//        LLIConnectionConditionBuilder lliConnectionConditionBuilder = new LLIConnectionConditionBuilder();
//        String selectPart = "Select count(*) as count ";
//        SqlPair sqlPair = lliConnectionConditionBuilder.fromLLIConnection().Where().activeToEquals(Long.MAX_VALUE).clientIDEquals(clientId).getSqlPair();
//
//        sqlPair.sql = selectPart + sqlPair.sql;
//
//        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
//        rs.next();
//        return rs.getInt("count");
    }

}
