/*
 * Created on Mar 7, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package config;

import java.util.*;
import databasemanager.xml.*;
import databasemanager.*;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ConfigurationManager {
	
	String fileName = "DatabaseConnection.xml"; 
		
	public Configuration getConfiguration() throws Exception  {
		Configuration config = new Configuration();	 	
		DatabaseXMLDAO dao = new DatabaseXMLDAO(fileName);
		DatabaseConnection connection = dao.getDefaultDatabaseConnection();
		
		String db_url = connection.getDatabaseURL();
		config.server = db_url.substring(db_url.lastIndexOf("/") + 1,db_url.lastIndexOf(":"));
		config.username = connection.getUserName();
		config.password = connection.getPassword();
		return config; 	
	}

	public Configuration saveConfiguration(Configuration config) throws Exception  {

		DatabaseXMLDAO dao = new DatabaseXMLDAO(fileName);
		DatabaseConnection connection = dao.getDefaultDatabaseConnection();

		String db_url = connection.getDatabaseURL();
		String serverMachine = db_url.substring(db_url.lastIndexOf("/") + 1,db_url.lastIndexOf(":"));


		connection.setUserName(config.username);
		connection.setPassword(config.password);
		connection.setDatabaseURL(connection.getDatabaseURL().replaceFirst(serverMachine,config.server));

		Collection c = new ArrayList();
		c.add(connection);  
		dao.saveAllDatabaseConnections(c);
/* ADDED BY SAJAL 30_05_2004 */
/* this code will allow to change database  connection information
without restarting the tomcat server*/
                DatabaseManager.getInstance().reLoad();
/* END OF ADDED BY SAJAL 30_05_2004 */
		return config; 	
	}

	public static void main(String[] args) throws Exception {
		ConfigurationManager manager = new ConfigurationManager(); 
		Configuration config = manager.getConfiguration();  
		
		System.out.println(config.toString());

		config.server = "localhost";
		config.username = "voip";
		config.password = "voip";
		
		//manager.saveConfiguration(config);
		
		
	}
	
}
