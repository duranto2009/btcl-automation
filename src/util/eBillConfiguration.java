package util;

import databasemanager.DatabaseManager;
import java.sql.*;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class eBillConfiguration
{
	static Logger logger = Logger.getLogger(util.eBillConfiguration.class);;
    private static eBillConfiguration m_instance;
    public static final long REFRESH_INTERVAL = 60000;
    public HashMap map;
    private long lastUpdateTime;

    private eBillConfiguration()
    {
        map = new HashMap();
        lastUpdateTime = 0;
        reloadConfiguration();
    }

    public String getConfigurationValue(long ID)
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime - 60000L > lastUpdateTime)
        {
            lastUpdateTime = currentTime;
            reloadConfiguration();
        }
        return (String)map.get(new Long(ID));
    }

    public void forceReload()
    {
        lastUpdateTime = System.currentTimeMillis();
        reloadConfiguration();
    }

    private void reloadConfiguration()
    {
        Connection connection=null;
        Statement stmt=null;
        ResultSet resultSet;
        try
        {
        	connection = DatabaseManager.getInstance().getConnection();
        	stmt = connection.createStatement();
        
	        String sql = "select configurationID, configurationValue from vbeBillConfiguration ";
	        long id;
	        String value;
	        for(resultSet = stmt.executeQuery(sql); resultSet.next(); map.put(new Long(id), value))
	        {
	            id = resultSet.getLong("configurationID");
	            value = resultSet.getString("configurationValue");
	        }
	        resultSet.close();
        }
        catch(Exception ex)
        {
        	logger.fatal("Failed to load configuration", ex);
        }
        finally
        {
        	try{if (stmt != null){stmt.close();}}catch (Exception e){logger.fatal("DAO finally :" + e.toString());}
        	try{if (connection != null){DatabaseManager.getInstance().freeConnection(connection);}}catch (Exception e){logger.fatal("DAO finally :" + e.toString());}
        }
    }

    public static eBillConfiguration getInstance()
    {
        if(m_instance == null)
            createConfigurationInstance();
        return m_instance;
    }

    private static synchronized void createConfigurationInstance()
    {
        if(m_instance == null)
            m_instance = new eBillConfiguration();
    }

    
}