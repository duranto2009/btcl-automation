package lli.client.td;

import java.util.*;
import java.util.stream.Collectors;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import global.GlobalService;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import login.LoginDTO;
import requestMapping.Service;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

public class LLIClientTDService implements NavigationService {

	@DAO
	LLIClientTDDAO lliClientTDDAO;
	
	@Service
	LLIConnectionService lliConnectionService;
	
	@SuppressWarnings("rawtypes")
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		List <LLIClientTD> list = lliClientTDDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return lliClientTDDAO.getDTOListByIDList((List<Long>) recordIDs);
	}
	
	private int getCountByStatus(List<LLIConnectionInstance> lliConnectionInstances, int status) {
		
		return (int) lliConnectionInstances.stream().filter(a->a.getStatus() == status).count();
		
	}
	
	
	@Transactional
	public void validateConnectionTD(long clientID,long activationTime) throws Exception{
		List<LLIConnectionInstance> currentLLIConnectionList = lliConnectionService.getLLIConnectionInstanceListByClientID(clientID);
		int activeConnectionCount = getCountByStatus(currentLLIConnectionList, LLIConnectionInstance.STATUS_ACTIVE);
		int tdConnectionCount = getCountByStatus(currentLLIConnectionList, LLIConnectionInstance.STATUS_TD);
		
		if(activeConnectionCount*tdConnectionCount != 0){
			throw new RequestFailureException("Client( " + clientID + ") has active and TD connection at the same time");
		}
	}
	
	@Transactional
	public void tempDisconnectClientByClientID(long clientID,long tdActivationDate) throws Exception{
		
		lliConnectionService.tdLLIConnectionByClientID(clientID,tdActivationDate);
		
		LLIClientTD existingLliClientTD = lliClientTDDAO.getLLIClientTDByClientID(clientID);
		if(existingLliClientTD != null){
			throw new RequestFailureException("Client is already Temporarily Disconnected");
		}
		LLIClientTD newLliClientTD = new LLIClientTD(clientID, tdActivationDate, Long.MAX_VALUE);
		lliClientTDDAO.insertClientTD(newLliClientTD);
	}

	@Transactional
	public void reconnectClientByClientID(long clientID, long reconnectionDate) throws Exception{
		
		lliConnectionService.reconnectionConnectionByClientID(clientID);
		
		LLIClientTD lliClientTD = lliClientTDDAO.getLLIClientTDByClientID(clientID);
		if(lliClientTD == null){
			throw new RequestFailureException("Client is not Temporarily Disconnected");
		}
		lliClientTD.setTdTo(reconnectionDate);
		lliClientTDDAO.updateClientTD(lliClientTD);
	}

	@Transactional(transactionType = TransactionType.READONLY)
	public boolean isClientTemporarilyDisconnected(long clientID) throws Exception {
		LLIClientTD existingLliClientTD = lliClientTDDAO.getLLIClientTDByClientID(clientID);
		return existingLliClientTD != null;
	}
	
	@Transactional(transactionType = TransactionType.READONLY)
	public String getClientTDStatus(long clientID) throws Exception{
		return isClientTemporarilyDisconnected(clientID) ? "TD" : "Active";
	}


	public Map<Long, String> getClientTDStatusByClientIDs(List<Long> clientIDs)  {
		if(clientIDs.isEmpty()) return Collections.emptyMap();
		Map<Long, List<LLIClientTD>> map = null;
		try {
			map = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
					LLIClientTD.class, new LLIClientTDConditionBuilder()
					.Where()
					.clientIDIn(clientIDs)
					.tdToEquals(Long.MAX_VALUE)
					.getCondition()
			).stream()
					.collect(Collectors.groupingBy(LLIClientTD::getClientID));
			return map.entrySet()
					.stream()
					.filter(t->t.getValue().size() == 1) // must be one instance of TD client
					.collect(Collectors.toMap(Map.Entry::getKey, (t)-> "TD"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyMap();
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public List<LLIClientTD> getTDHistoryByClientID(long clientID) throws Exception {
		return lliClientTDDAO.getTDHistoryByClientID(clientID);
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public long getProbableTDDateByClientID(long clientID) throws Exception {
		List<LLIProbableTDClient> probableTDClient = ModifiedSqlGenerator.getAllObjectList(LLIProbableTDClient.class, " where clientID = " + clientID);
		return probableTDClient.size() > 0 ? probableTDClient.get(0).getTdDate() : 0;
	}
	
}
