package vpn.client;

import org.apache.log4j.Logger;

import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import util.SqlGenerator;
import util.TimeConverter;

public class MigratedClientUpdateService {
	
	MigratedClientUpdateDAO migratedClientUpdateDAO = new MigratedClientUpdateDAO();
	Logger logger = Logger.getLogger(getClass());

	public void updateActivationDate(String vpnClientID, String activationDate) throws Exception{
		ClientDetailsDTO tempClientDetailsDTO = new ClientDetailsDTO();
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(Long.parseLong(vpnClientID));
		SqlGenerator.populateObjectFromOtherObject(tempClientDetailsDTO, ClientDetailsDTO.class, clientDetailsDTO, ClientDetailsDTO.class);
		tempClientDetailsDTO.setActivationDate(TimeConverter.getTimeFromString(activationDate));
		
		
		DatabaseConnection databaseConnection = new DatabaseConnection();

		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			migratedClientUpdateDAO.updateMigratedClient(tempClientDetailsDTO, databaseConnection);
			databaseConnection.dbTransationEnd();

		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("Exception Updating Migrated Client :" ,ex2);
			}
		} finally {
			databaseConnection.dbClose();
		}
	}

}
