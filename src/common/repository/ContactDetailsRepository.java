package common.repository;
import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.SqlGenerator.getAllObjectForRepository;

public class ContactDetailsRepository implements Repository{
	private static ContactDetailsRepository instance = null;
	Logger logger = Logger.getLogger(getClass());
	Map<Long, ClientContactDetailsDTO> mapOfContactDetailsToContactDetailsID = new HashMap<Long, ClientContactDetailsDTO>();
	Map<String,Long> mapOfPhoneNumberToAtClientID = new HashMap<String, Long>();
	Map<String,Long> mapOfEmailAddressToAtClientID = new HashMap<String, Long>();
	private ContactDetailsRepository(){
		RepositoryManager.getInstance().addRepository(this);
	}
	public Long getClientIDByEmailAddress(String emailAddress){
		return mapOfEmailAddressToAtClientID.get(emailAddress);
	}
	public Long getClientIDByPhoneNumber(String phoneNumber){
		return mapOfPhoneNumberToAtClientID.get(phoneNumber);
	}
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			databaseConnection.dbOpen();
			Class classObject = ClientContactDetailsDTO.class;
			List<ClientContactDetailsDTO> contactDetailsDTOs = (List<ClientContactDetailsDTO>)getAllObjectForRepository(classObject, databaseConnection, isFirstReload);
			ClientDetailsRepository vpnClientRepository = ClientDetailsRepository.getInstance();
			ClientRepository clientRepository = ClientRepository.getInstance();
			for(ClientContactDetailsDTO contactDetailsDTO: contactDetailsDTOs){
				
				ClientContactDetailsDTO prevContactDetailsDTO = mapOfContactDetailsToContactDetailsID.get(contactDetailsDTO.getID());
				mapOfEmailAddressToAtClientID.remove(prevContactDetailsDTO.getEmail());
				mapOfPhoneNumberToAtClientID.remove(prevContactDetailsDTO.getPhoneNumber());
				mapOfContactDetailsToContactDetailsID.remove(prevContactDetailsDTO.getID());
				
				if(!contactDetailsDTO.isDeleted()){
					mapOfContactDetailsToContactDetailsID.put(contactDetailsDTO.getID(), contactDetailsDTO);
					long vpnClientID = contactDetailsDTO.getVpnClientID();
					ClientDetailsDTO clientDetailsDTO = vpnClientRepository.getClientDetailsDTOByVpnClientID(vpnClientID);
					if(clientDetailsDTO == null && !clientDetailsDTO.isDeleted()){
						long clientID = clientDetailsDTO.getClientID();
						mapOfEmailAddressToAtClientID.put(contactDetailsDTO.getEmail(), clientID);
						mapOfPhoneNumberToAtClientID.put(contactDetailsDTO.getPhoneNumber(), clientID);
					}
				}
				
			}
		}catch(Exception ex){
			
		}finally{
			databaseConnection.dbClose();
		}
		
		
	}
	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(ClientContactDetailsDTO.class);
		}catch(Exception ex){
			logger.debug("fatal",ex);
		}
		return tableName;
	}
	public static synchronized ContactDetailsRepository getInstance(){
		if(instance == null){
			instance = new ContactDetailsRepository();
		}
		return instance;
	}
}
