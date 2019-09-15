package config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import util.SqlGenerator;


public class GlobalConfigurationRepository {
	Logger logger = Logger.getLogger(getClass());
	private static GlobalConfigurationRepository instance = null;
	private static HashMap<Integer, GlobalConfigDTO> GlobalConfigDTOByID;
	private GlobalConfigurationRepository()
	{
		GlobalConfigDTOByID = new HashMap<Integer, GlobalConfigDTO>();
		reload();
	}
	public static GlobalConfigurationRepository getInstance()
	{
		if(instance == null)
		{
			createInstance();
		}
		return instance;
	}
	private synchronized static void createInstance()
	{
		if(instance == null)
		{
			instance = new GlobalConfigurationRepository();
		}
		
	}
	public synchronized Collection<GlobalConfigDTO> getAllStateDTOs()
	{		
		return GlobalConfigDTOByID.values();
	}	
	public synchronized GlobalConfigDTO getGlobalConfigDTOByID(int configID)
	{
//		logger.debug("StateNameByStateID " + GlobalConfigDTOByID);
		if(GlobalConfigDTOByID.get(configID) == null)
		{
			GlobalConfigDTO globalConfigDTO = new GlobalConfigDTO();
			globalConfigDTO.setValue(0);
			return globalConfigDTO;
		}
		return GlobalConfigDTOByID.get(configID);
	}
	public void reload()
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			ArrayList<GlobalConfigDTO> configList = (ArrayList<GlobalConfigDTO>) SqlGenerator.getAllObjectForRepository(GlobalConfigDTO.class, databaseConnection, true);
			for(GlobalConfigDTO sdto: configList)
			{
				GlobalConfigDTOByID.put(sdto.getID(), sdto);
			}
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			databaseConnection.dbClose();
		}
		
	}
}
