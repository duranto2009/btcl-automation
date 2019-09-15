package vpn.client;

import connection.DatabaseConnection;
import util.SqlGenerator;

public class MigratedClientUpdateDAO {

	public void updateMigratedClient(ClientDetailsDTO clientDetailsDTO, DatabaseConnection databaseConnection) throws Exception {
		SqlGenerator.updateEntity(clientDetailsDTO, ClientDetailsDTO.class, databaseConnection, false, false);
	}

}
