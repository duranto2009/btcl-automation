package common;

import connection.DatabaseConnection;
import static util.SqlGenerator.*;
public class ServiceDAO {
	public void insertServiceClient(ServiceClient serviceClient,DatabaseConnection databaseConnection) throws Exception{
		insert(serviceClient, ServiceClient.class, databaseConnection, false);
	}

	public ServiceClient getServiceClientByServiceClientID(long serviceClientID,DatabaseConnection databaseConnection) throws Exception{
		return (ServiceClient)getObjectByID(ServiceClient.class, serviceClientID, databaseConnection);
	}
	public void setAdvancedPaymentOfServiceClient(long serviceClientID,double newAdvancedPayment,long lastModificationTime,DatabaseConnection databaseConnection) throws Exception{
		if(newAdvancedPayment<0.00000000001){
			throw new RequestFailureException("Advanced payment can not be negative");
		}
		Class classObject = ServiceClient.class;
		String IDColumnName = getColumnName(classObject, "ID");
		String advancedPaymentColumnName = getColumnName(classObject, "advancedPayment");
		String lastModificationTimeColumnName = getLastModificationTimeColumnName(classObject);
		String tableName = getTableName(classObject);
		String sql = "update "+tableName+" set "+advancedPaymentColumnName+" = "+newAdvancedPayment+
						" where "+IDColumnName+" = "+serviceClientID+" and "+lastModificationTimeColumnName+ " = "+lastModificationTime;
		int numOfAffectedRows = databaseConnection.getNewStatement().executeUpdate(sql);
		if(numOfAffectedRows!=0){
			throw new RequestFailureException("Advanced payment updation failed");
		}
		
	} 
}
