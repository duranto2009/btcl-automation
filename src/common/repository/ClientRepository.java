package common.repository;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import common.ClientDTO;
import org.apache.log4j.Logger;
import connection.DatabaseConnection;

import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;
import static util.SqlGenerator.*;
class ClientRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	static ClientRepository instance = null;
	Map<Long,ClientDTO> mapOfClientToClientID;
	Map<String,ClientDTO> mapOfClientToClientName;
	private ClientRepository(){
		mapOfClientToClientID = new ConcurrentHashMap<>();
		mapOfClientToClientName = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}
	
	
	
	
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			
			ResultSet rs = getAllResultSetForRepository(ClientDTO.class, databaseConnection, isFirstReload);
			
			while(rs.next()){
				ClientDTO clientDTO = new ClientDTO();			
				populateObjectFromDB(clientDTO, rs);
				/*
				mapOfClientToClientID.remove(clientDTO.getClientID());
				mapOfClientToClientName.remove(clientDTO.getLoginName());*/
				
				ClientDTO oldClientDTO = mapOfClientToClientID.get(clientDTO.getClientID());
				if(oldClientDTO != null){
					mapOfClientToClientName.remove(oldClientDTO.getLoginName());
					mapOfClientToClientID.remove(oldClientDTO.getClientID());
				}
				
				if(!clientDTO.isDeleted()){
					mapOfClientToClientID.put(clientDTO.getClientID(), clientDTO);
					mapOfClientToClientName.put(clientDTO.getLoginName(), clientDTO);
				}
			}
			
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
	}
	
	public static synchronized ClientRepository getInstance(){
		if(instance == null){
			instance = new ClientRepository();
		}
		return instance;
	}

	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(ClientDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;
	}

}
