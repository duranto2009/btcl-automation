package common.repository;

import client.ClientAction;
import common.*;
import common.client.Client;
import common.client.ClientDetails;
import login.LoginDTO;
import org.apache.log4j.Logger;
import request.StateDTO;
import request.StateRepository;
import util.ModifiedSqlGenerator;
import vpn.client.ClientDetailsDTO;

import java.util.*;
import java.util.stream.Collectors;

import static util.SqlGenerator.populateObjectFromOtherObject;

public class AllClientRepository {
	Logger logger = Logger.getLogger(getClass());
	
	private static AllClientRepository instance = null;

	Map<Long,ClientDTO> mapOfClientToClientID;
	Map<String,ClientDTO> mapOfClientToClientLoginName;
	Map<Long,ClientDetailsDTO> mapOfVpnClientToVpnClientID;
	
	Map<Long,Map<Integer,ClientDetailsDTO> > mapOfModuleClientToClietnIDAndModuleID;

	
	
	AllClientRepository() {
		mapOfClientToClientID = ClientRepository.getInstance().mapOfClientToClientID;
		mapOfClientToClientLoginName = ClientRepository.getInstance().mapOfClientToClientName;
		mapOfVpnClientToVpnClientID = ClientDetailsRepository.getInstance().mapOfVpnClientToVpnClientID;
		mapOfModuleClientToClietnIDAndModuleID = ClientDetailsRepository.getInstance().mapOfModuleClientToModuleIDToClientID;
	}

	public Map<Integer, Set<ClientDetailsDTO>> getModuleWiseClientStat() {
		return ClientDetailsRepository.getInstance().mapOfClientIDSetToModuleID.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public List<ClientDetailsDTO> getClientIDListByModuleID(int moduleID){
		Set<ClientDetailsDTO> clientIDSet = ClientDetailsRepository.getInstance().mapOfClientIDSetToModuleID.get(moduleID);
		return new ArrayList<>(clientIDSet);
	}


	public List<ClientDTO> getClientDTOListBypartialNameAndModuleIDAndResultLimit(String partialName, int moduleID){
		return getClientDTOListBypartialNameAndModuleIDAndResultLimit(partialName, moduleID, Integer.MAX_VALUE);
	}

	public List<ClientDTO> getClientDTOsByPartialClientID(String partialClientID, int moduleID) {

		return mapOfClientToClientID.values().stream()
				.filter(t->String.valueOf(t.getClientID()).startsWith(partialClientID.trim()))
				.map(t->getVpnClientByClientID(t.getClientID(), moduleID))
				.filter(Objects::nonNull)
				.filter(t->StateRepository.getInstance().getStateDTOByStateID(t.getCurrentStatus()).getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE)
				.collect(Collectors.toList());
	}
	public List<ClientDTO> getClientDTOListBypartialNameAndModuleIDAndResultLimit(String partialName, int moduleID, int limit){

		List<ClientDTO> clientDTOListLimited = new ArrayList<>();
		partialName = StringUtils.toUpperCase(partialName);
		int i=0;
		for(ClientDTO clientDTO: mapOfClientToClientID.values()){
			String clientName = StringUtils.toUpperCase(clientDTO.getLoginName());
			if(clientName.contains(partialName)){
				ClientDetailsDTO clientDetailsDTO = getVpnClientByClientID(clientDTO.getClientID(), moduleID);
				if(clientDetailsDTO != null && StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus()).getActivationStatus() 
						== EntityTypeConstant.STATUS_ACTIVE) {
					clientDTOListLimited.add(clientDTO);
					i++;
					if(i==limit){
						break;
					}
				}
				
			}
		}
		return clientDTOListLimited;
		
	}
	public void reload(boolean isFirstReload){
		reloadClientRepository(isFirstReload);
		reloadModuleClientRepository(isFirstReload);
	}
	public void reloadClientRepository(boolean isFirstReload){
		ClientRepository.getInstance().reload(isFirstReload);
	}
	public void reloadModuleClientRepository(boolean isFirstReload){
		ClientDetailsRepository.getInstance().reload(isFirstReload);
	}
	public ClientDTO getClientByClientID(long clientID){
		//		logger.debug("client id "+clientID+" client dto is "+ mapOfClientToClientID.get(clientID));
		ClientDTO clientDTO = mapOfClientToClientID.get(clientID);
		if(clientDTO == null){
			throw new RequestFailureException("No Client Found by id " + clientID);
		}
		return clientDTO;
	}
	public ClientDTO getClientByClientLoginName(String clientName){
		return mapOfClientToClientLoginName.get(clientName);
	}
	public ClientDetailsDTO getModuleClientByClientIDAndModuleID(long clientID,int moduleID){
//		logger.debug("module id "+moduleID);
		Map<Integer,ClientDetailsDTO> mapOfModuleClientToModuleID =  mapOfModuleClientToClietnIDAndModuleID.get(clientID);
		if(mapOfModuleClientToModuleID == null || !mapOfModuleClientToModuleID.containsKey(moduleID)){
			return null;
		}
		ClientDetailsDTO clientDetailsDTO = mapOfModuleClientToModuleID.get(moduleID);
		
		ClientDTO clientDTO = mapOfClientToClientID.get(clientDetailsDTO.getClientID());

		ModifiedSqlGenerator.populateObjectFromOtherObject(clientDTO, clientDetailsDTO, ClientDTO.class);
		
		return clientDetailsDTO;
	}
	public ClientDetailsDTO getVpnClientByVpnClientID(long vpnClientID){
		ClientDetailsDTO clientDetailsDTO = mapOfVpnClientToVpnClientID.get(vpnClientID);
		if(clientDetailsDTO == null){
			return null;
		}
		ClientDTO clientDTO = mapOfClientToClientID.get(clientDetailsDTO.getClientID());
		
		if(clientDTO == null) {
			return null;
		}
		
		try{
//			populateObjectFromOtherObject(clientDetailsDTO, ClientDTO.class, clientDTO, ClientDTO.class);
			
			ModifiedSqlGenerator.populateObjectFromOtherObjectNonRecursively(clientDTO, clientDetailsDTO, ClientDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return clientDetailsDTO;
	}
	
	public ArrayList<ClientDetailsDTO> getAllVpnCleint(){
		ArrayList<ClientDetailsDTO> clientDetailsDTOs = new ArrayList<ClientDetailsDTO>();
		for(Map.Entry<Long,ClientDetailsDTO> entry : mapOfVpnClientToVpnClientID.entrySet()){
			ClientDetailsDTO clientDetailsDTO = getVpnClientByVpnClientID(entry.getValue().getId());
			if(clientDetailsDTO != null ) {
				clientDetailsDTOs.add(clientDetailsDTO);
			}
		}
		Collections.sort(clientDetailsDTOs);
		return clientDetailsDTOs;
	}
	public static synchronized AllClientRepository getInstance(){
		if(instance == null){
			instance = new AllClientRepository();
		}
		return instance;
	}
	/*public HashMap<Integer, ClientStatusDTO> getClientStatusByClientID(long clientID)
	{
		HashMap<Integer, ClientStatusDTO> clientStatusMap = new HashMap<Integer, ClientStatusDTO>();
		
		
		
		Map<Integer,ClientDetailsDTO> mapOfModuleClientToModuleID =  mapOfModuleClientToClietnIDAndModuleID.get(clientID);
		logger.debug("clientID " + clientID);
		logger.debug("mapOfModuleClientToModuleID " + mapOfModuleClientToModuleID);
		for(int moduleID:mapOfModuleClientToModuleID.keySet()){
			ClientStatusDTO clientStatusDTO = new ClientStatusDTO();
			ClientDetailsDTO clientDetailsDTO = mapOfModuleClientToModuleID.get(moduleID);
			clientStatusDTO.setModuleClientID(clientDetailsDTO.getId());
			clientStatusDTO.setStatus(clientDetailsDTO.getStatus());
		//	clientStatusDTO.setApplicationStatus(clientDetailsDTO.getRegistrationStatus());
			clientStatusMap.put(moduleID, clientStatusDTO);
		}

		return clientStatusMap;
	}*/
	
	public ClientDetailsDTO getVpnClientByClientID(long clientID,int moduleID){
		if(!mapOfModuleClientToClietnIDAndModuleID.containsKey(clientID)){
			return null;
		}
		ClientDetailsDTO clientDetailsDTO = mapOfModuleClientToClietnIDAndModuleID.get(clientID).get(moduleID);
		ClientDTO clientDTO = mapOfClientToClientID.get(clientID);
		if(clientDetailsDTO==null ){
			return null;
		}
		try{
			populateObjectFromOtherObject(clientDetailsDTO, ClientDTO.class, clientDTO, ClientDTO.class);
		}catch(Exception ex){
			logger.fatal("FATAL",ex);
			throw new RequestFailureException(ex.getMessage());
		}
		
		return clientDetailsDTO;
	}
	
	public ArrayList<Long> getClientByModuleAndCompanyType(int moduleID, int companyType){
		ArrayList<Long> listOfClientID=new ArrayList<Long>();
		ArrayList<ClientDetailsDTO> listOfClientDTO=getAllVpnCleint();
		for(ClientDetailsDTO clientDTO: listOfClientDTO){
			if((moduleID==clientDTO.getModuleID())&& (clientDTO.getRegistrantType()==companyType)){
				listOfClientID.add(clientDTO.getClientID());
			}
		}
		return listOfClientID;
	}
	
	public ArrayList<Long> getVpnClientByClientID(long clientID){
		ArrayList<Long> listOfVpnClientID=new ArrayList<Long>();
		ArrayList<ClientDetailsDTO> listOfClientDetailsDTO=getAllVpnCleint();
		for(ClientDetailsDTO clientDetailsDTO: listOfClientDetailsDTO){
			if((clientID==clientDetailsDTO.getClientID())){
				listOfVpnClientID.add(clientDetailsDTO.getId());
			}
		}
		return listOfVpnClientID;
	}

	public List<ClientDetailsDTO> getClientDetailsListByClientId(long clientId) {
		return ClientDetailsRepository.getInstance().getClientDetailsDTOByClientID(clientId);
	}

	public int getRegistrantTypeByClientId(long clientId) {
		return getClientDetailsListByClientId(clientId)
				.stream()
				.findFirst()
				.orElseThrow(()->new RequestFailureException("No Client Details Found for Client Id " + clientId))
				.getRegistrantType();
	}
	
	public List<HashMap<String, String>> getModuleListByClientID(long clientID){
		List<HashMap<String, String>> moduleIDListByClientID=new ArrayList<HashMap<String, String>>();
		
		ArrayList<ClientDetailsDTO> listOfClientDetailsDTO=getAllVpnCleint();
		for(ClientDetailsDTO clientDetailsDTO: listOfClientDetailsDTO){
			if((clientID==clientDetailsDTO.getClientID())){
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("moduleID", clientDetailsDTO.getModuleID()+"");
				tempMap.put("moduleName", ModuleConstants.ModuleMap.get(clientDetailsDTO.getModuleID()));

				moduleIDListByClientID.add(tempMap);
			}
		}
		return moduleIDListByClientID;
	}
	public List<Integer>getRegisteredModulesByClientID(long clientID) {
		List<Integer>modules = new ArrayList<>();
		for(ClientDetailsDTO client : getAllVpnCleint()) {
			StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(client.getCurrentStatus());
			if(client.getClientID() == clientID) {
				if(stateDTO != null && stateDTO.getActivationStatus() != EntityTypeConstant.STATUS_NOT_ACTIVE) {
					modules.add(client.getModuleID());
				}
			}
		}
		return modules;
	}

	
	public List<ClientDetailsDTO> getClientListByModuleID(int moduleID){
		List<ClientDetailsDTO> moduleClientList = new ArrayList<>();
		List<ClientDetailsDTO> allClientList = AllClientRepository.getInstance().getAllVpnCleint();
		for(ClientDetailsDTO clientDetailsDTO : allClientList) {
			if(clientDetailsDTO.getModuleID() == moduleID) {
				moduleClientList.add(clientDetailsDTO);
			}
		}
		return moduleClientList;
	}
	
}
