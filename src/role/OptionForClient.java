package role;

import databasemanager.DatabaseManager;
import java.sql.*;
import org.apache.log4j.Logger;

public class OptionForClient
{
	static Logger logger= Logger.getLogger(role.OptionForClient.class);
    public static final long REFRESH_INTERVAL = (long)(10000 + (int)(Math.random() * 1000));
    static OptionForClient optionForClient = null;
    private long loadingTime;
    private int Op2EditCallerID;
    private int Op2EditClientID;
    private int Op2EditBillingInfo;
    private int Op2EditContactInfo;

	
    private OptionForClient()
    {
        Op2EditCallerID = 0;
        Op2EditClientID = 0;
        Op2EditBillingInfo = 0;
        Op2EditContactInfo = 0;
        forceReload();
    }

    public static OptionForClient getInstance()
    {
        if(optionForClient == null)
            createOption();
        return optionForClient;
    }

    private static synchronized void createOption()
    {
        if(optionForClient == null)
            optionForClient = new OptionForClient();
    }

    private void reload()
    {
        Connection connection;
        Statement stmt;
        int roleID;
        Op2EditCallerID = 0;
        Op2EditClientID = 0;
        Op2EditBillingInfo = 0;
        Op2EditContactInfo = 0;
        logger.debug("Payel");
        String sql = null;
        String sql1 = null;
        connection = null;
        stmt = null;
        ResultSet resultSet = null;
        
        try
        {
        	roleID = -1;
        	sql = "select rlRoleID from vbRole where rlRoleName like '%clientRole%'";
        	connection = DatabaseManager.getInstance().getConnection();
        	stmt = connection.createStatement();
        	resultSet = stmt.executeQuery(sql);
        	if(resultSet.next())
        	{
        		roleID = resultSet.getInt("rlRoleID");
        		logger.debug("OptionForClient: Role ID is " + roleID);
        	} else{
        		logger.debug("RoleForClietnDAO: No ClientRole Selected ");
        	}
	        if(roleID != -1)
	        {
	            sql1 = "select clColumnID from vbColumnPermission where clRoleID =" + roleID;
	            resultSet = stmt.executeQuery(sql1);
	            do
	            {
	                if(!resultSet.next())
	                    break;
	                logger.debug("Column ID : " + resultSet.getLong("clColumnID"));
	                if(resultSet.getLong("clColumnID") == 18L)
	                {
	                    Op2EditClientID = 1;
	                    logger.debug("Client ID Changed: " + Op2EditClientID);
	                }
	                if(resultSet.getLong("clColumnID") == 19L)
	                {
	                    Op2EditCallerID = 1;
	                    logger.debug("Caller ID Changed: " + Op2EditCallerID);
	                }
	                if(resultSet.getLong("clColumnID") == 20L)
	                    Op2EditContactInfo = 1;
	                if(resultSet.getLong("clColumnID") == 21L)
	                    Op2EditBillingInfo = 1;
	            } while(true);
	        }
	        resultSet.close();
        }
        catch(Exception ex)
        {
        	logger.fatal("Error in OptionForClient: " + ex.toString());
        }
        finally
        {
          try{ if (stmt != null) {stmt.close();}} catch (Exception e){}
          try{ if (connection != null){ DatabaseManager.getInstance().freeConnection(connection); } }
          catch (Exception e){ logger.fatal("Error in OptionForClient: " + e.toString()); }
        }
    }

    public int getOp2EditCallerID()
    {
        checkForReload();
        logger.debug("Caller ID retunred: " + Op2EditCallerID);
        return Op2EditCallerID;
    }

    public int getOp2EditClientID()
    {
        checkForReload();
        logger.debug("Client ID retunred: " + Op2EditClientID);
        return Op2EditClientID;
    }

    public int getOp2EditBillingInfo()
    {
        checkForReload();
        return Op2EditBillingInfo;
    }

    public int getOp2EditContactInfo()
    {
        checkForReload();
        return Op2EditContactInfo;
    }

    private void checkForReload()
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime - loadingTime > REFRESH_INTERVAL)
        {
            loadingTime = currentTime;
            reload();
        }
    }

    public synchronized void forceReload()
    {
        loadingTime = System.currentTimeMillis();
        reload();
    }

    public static void main(String args[])
        throws Exception
    {
        String a = " ";
        a = a.trim();
        if(a == null)
        {}
            //System.out.println("null");
        else
        {}
            //System.out.println("String: " + a.length());
    }
}