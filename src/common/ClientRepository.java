package common;

import clientdocument.ClientDocumentDTO;
import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static util.SqlGenerator.*;





/*
 * first load the subclasses into map (VpnClient)
 * inside relaodClient() method call SqlGenerator.populateFromDB(mapOfVpnClientToClientID(clientID))
 * */


public class ClientRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
	private static ClientRepository instance = null;


	private HashMap<Long,ClientDTO> mapOfClientToClientID;
	private HashMap<Long,ClientDetailsDTO> mapOfVpnClientToVpnClientID;
	private HashMap<String,ClientDetailsDTO> mapOfVpnClientToClientName;
	private HashMap<Long,ClientDetailsDTO> mapOfVpnClientToClientID;
	private HashMap<Long,ClientDocumentDTO> mapOfClientDocumentToDocumentID ;
	private HashMap<Long,Set<ClientDocumentDTO>> mapOfClientDocumentSetToClietnID;

	
	public ClientDetailsDTO getClientDetailsDTOFromClientName(String name) {
		ClientDetailsDTO clientDetailsDTO = null;
		if(mapOfVpnClientToClientName.containsKey(name)){
			clientDetailsDTO = mapOfVpnClientToClientName.get(name);
		}
		return clientDetailsDTO;
	}
	

	
	public ClientDTO getClient(long clientID)
	{
		return mapOfClientToClientID.get(clientID);
	}
	
	public ClientDetailsDTO getVpnClient(long clientID)
	{
		logger.debug("mapOfVpnClientToClientID " + mapOfVpnClientToClientID);
		return mapOfVpnClientToClientID.get(clientID);
	}
	
	private void reloadVpnClient(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception{
		String tableName = SqlGenerator.getTableName(ClientDetailsDTO.class);
		String lastModificationColumnName = getColumnName(ClientDetailsDTO.class, "lastModificationTime");
		String sql = "select * from "+tableName+(isFirstReload?"":" where "+lastModificationColumnName+">="+RepositoryManager.lastModifyTime);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();
			populateObjectFromDB(clientDetailsDTO, rs);
			mapOfVpnClientToVpnClientID.put(clientDetailsDTO.getId(), clientDetailsDTO);
			mapOfVpnClientToClientID.put(clientDetailsDTO.getClientID(), clientDetailsDTO);
		}
		logger.debug(mapOfVpnClientToVpnClientID);
		logger.debug("mapOfVpnClientToClientID " + mapOfVpnClientToClientID);
	}
	
	private void reloadClient(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception{
		String tableName = SqlGenerator.getTableName(ClientDTO.class);
		String lastModificationTimeColumnName = getColumnName(ClientDTO.class, "lastModificationTime");
		String sql = "select * from "+tableName+(isFirstReload?"":" where "+lastModificationTimeColumnName+">="+RepositoryManager.lastModifyTime);
		logger.debug("sql " + sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			ClientDTO clientDTO = new ClientDTO();			
			populateObjectFromDB(clientDTO, rs);
			mapOfClientToClientID.put(clientDTO.getClientID(), clientDTO);
			
			if(mapOfVpnClientToClientID.containsKey(clientDTO.getClientID())){
				ClientDetailsDTO clientDetailsDTO = mapOfVpnClientToClientID.get(clientDTO.getClientID());
				populateObjectFromDB(clientDetailsDTO, rs,clientDetailsDTO.getClass().getSuperclass());
				mapOfVpnClientToClientName.put(clientDTO.getLoginName(), clientDetailsDTO);
			}			

		}
		logger.debug(mapOfVpnClientToClientName);

		logger.debug(mapOfClientToClientID);

	}
	
	
	private ClientRepository(){
		mapOfVpnClientToClientName = new HashMap<String,ClientDetailsDTO>();
		mapOfClientDocumentToDocumentID = new HashMap<Long, ClientDocumentDTO>();
		mapOfClientDocumentSetToClietnID = new HashMap<Long, Set<ClientDocumentDTO>>();
		mapOfClientToClientID = new HashMap<Long,ClientDTO>();
		mapOfVpnClientToVpnClientID = new HashMap<Long,ClientDetailsDTO>();		
		mapOfVpnClientToClientID = new HashMap<Long,ClientDetailsDTO>();			
		

		
		RepositoryManager.getInstance().addRepository(this);
		
	}
	

	
	private void populate(ClientDocumentDTO clientDocumentDTO){
		mapOfClientDocumentToDocumentID.put(clientDocumentDTO.getID(), clientDocumentDTO);
		if(!mapOfClientDocumentSetToClietnID.containsKey(clientDocumentDTO.getClientID())){
			mapOfClientDocumentSetToClietnID.put(clientDocumentDTO.getClientID(), new HashSet<ClientDocumentDTO>());
		}
		mapOfClientDocumentSetToClietnID.get(clientDocumentDTO.getClientID()).add(clientDocumentDTO);				
	}
	
	private void reloadClientDocuments(boolean firstReload, DatabaseConnection databaseConnection) throws Exception{
		String tableName = SqlGenerator.getTableName(ClientDocumentDTO.class);
		String sql = "select * from "+tableName+(firstReload?"":" where "+getLastModificationTimeColumnName(ClientDocumentDTO.class)
				+" >= "+RepositoryManager.lastModifyTime);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			ClientDocumentDTO clientDocumentDTO = new ClientDocumentDTO();
			populateObjectFromDB(clientDocumentDTO, rs);
			populate(clientDocumentDTO);
		}
		
	}
	
	public static synchronized ClientRepository getInstance(){
		if(1==1){
			throw new RequestFailureException("stop using old client repository");
		}
		if(instance==null){		
					instance = new ClientRepository();			
		}
		return instance;
	}

	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();	
			databaseConnection.dbTransationStart();
			reloadVpnClient(realoadAll, databaseConnection);	
			//reloadDomainClient(realoadAll, databaseConnection);
			reloadClient(realoadAll, databaseConnection);
			databaseConnection.dbTransationEnd();
			databaseConnection.dbClose();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);		
		}
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
