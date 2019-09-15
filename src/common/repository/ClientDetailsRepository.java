package common.repository;

import java.util.HashMap;

import common.client.ClientDetails;
import org.apache.log4j.Logger;
import connection.DatabaseConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.jetty.util.ConcurrentHashSet;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;
import static util.SqlGenerator.*;
class ClientDetailsRepository implements Repository{
	static ClientDetailsRepository instance = null;
	Logger logger = Logger.getLogger(getClass());
	Map<Long,ClientDetailsDTO> mapOfVpnClientToVpnClientID;
	//Map<Long,ClientDetailsDTO> mapOfVpnClientToClientID;
	Map<Long,Map<Integer,ClientDetailsDTO> > mapOfModuleClientToModuleIDToClientID;
	Map<Integer, Set<ClientDetailsDTO>> mapOfClientIDSetToModuleID;
//	Map<Integer, Set<ClientDetailsDTO> > mapOfClientDetailsDTOsToModuleId;
	
	
	private ClientDetailsRepository(){
		mapOfVpnClientToVpnClientID = new ConcurrentHashMap<>();
		mapOfModuleClientToModuleIDToClientID = new ConcurrentHashMap<>();
		mapOfClientIDSetToModuleID  = new ConcurrentHashMap<>();
//		mapOfClientDetailsDTOsToModuleId = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}

	public List<ClientDetailsDTO> getClientDetailsDTOByClientID(long clientID){
		return mapOfVpnClientToVpnClientID.entrySet().stream()
				.filter(t->t.getValue().getClientID() == clientID)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	public ClientDetailsDTO getClientDetailsDTOByVpnClientID(long vpnClientID){
		return mapOfVpnClientToVpnClientID.get(vpnClientID);
	}
	
	@Override
	public void reload(boolean isFirstReload) {

		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			
			List<ClientDetailsDTO> clientDetailsDTOs = getAllObjectForRepository(ClientDetailsDTO.class, databaseConnection, isFirstReload);
			
			for(ClientDetailsDTO clientDetailsDTO: clientDetailsDTOs){
				int moduleID = clientDetailsDTO.getModuleID();
				if(mapOfClientIDSetToModuleID.containsKey(moduleID)){
					Set<ClientDetailsDTO>clientIDSet = mapOfClientIDSetToModuleID.get(moduleID);
					clientIDSet.remove(clientDetailsDTO);

					if(clientIDSet.isEmpty()){
						mapOfClientIDSetToModuleID.remove(moduleID);
					}

				}



				if(!clientDetailsDTO.isDeleted()){
					if(!mapOfClientIDSetToModuleID.containsKey(moduleID)){
						mapOfClientIDSetToModuleID.put(moduleID, new ConcurrentHashSet<>());
					}
					mapOfClientIDSetToModuleID.get(moduleID).add(clientDetailsDTO);

				}


				mapOfVpnClientToVpnClientID.put(clientDetailsDTO.getId(), clientDetailsDTO);
				if(!mapOfModuleClientToModuleIDToClientID.containsKey(clientDetailsDTO.getClientID())){
					mapOfModuleClientToModuleIDToClientID.put(clientDetailsDTO.getClientID(), new ConcurrentHashMap<>());
				}
				mapOfModuleClientToModuleIDToClientID.get(clientDetailsDTO.getClientID()).put(clientDetailsDTO.getModuleID(), clientDetailsDTO);

			}
			
			
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		
		
		
	}
	
	public static ClientDetailsRepository getInstance(){
		if(instance == null){
			instance = new ClientDetailsRepository();
		}
		return instance;
	}

	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(ClientDetailsDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;
	}

}
