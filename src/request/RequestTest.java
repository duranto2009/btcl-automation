package request;

import connection.DatabaseConnection;

public class RequestTest {

	/**
	 * @author dhrubo
	 */
	public static void completeRequestByEntityIDandEntityTypeIDTest(long entityID, int entityTypeID) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
			requestUtilDAO.completeRequestByEntityIDandEntityTypeID(entityID, entityTypeID, true, databaseConnection);
			databaseConnection.dbTransationEnd();			
		}
		catch(Exception ex)	{
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally	{
			databaseConnection.dbClose();
		}
	}
	
	public static void main(String[] args) throws Exception {
		completeRequestByEntityIDandEntityTypeIDTest(123001, 602);
		System.exit(0);
	}
	
}
