package permission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import databasemanager.DatabaseManager;
import repository.Repository;
import repository.RepositoryManager;

public class ActionPermissionRepository  implements Repository
{
  static Logger logger = Logger.getLogger(ActionPermissionRepository.class);
//  HashMap<String, Set<Integer>> actionTypeSetByStateRole;
  HashMap<Long, Set<Integer>> stateActionIDByRole;
  static ActionPermissionRepository actionPermissionRepository = null;

  String tableName = "at_action_permission";



  private ActionPermissionRepository()
  {

    this.stateActionIDByRole = new HashMap<Long, Set<Integer>>();

    RepositoryManager.getInstance().addRepository(this);
  }

  public static ActionPermissionRepository getInstance()
  {
    if (actionPermissionRepository == null)
    {
      createDestinationRepository();
    }
    return actionPermissionRepository;
  }

  private static synchronized void createDestinationRepository()
  {
    if (actionPermissionRepository == null)
    {
    	actionPermissionRepository = new ActionPermissionRepository();
    }
  }
  public synchronized Set<Integer> getStateActionTypeIDSet(long roleID)
  {
	  logger.debug("stateActionIDByRole.get(roleID) " + stateActionIDByRole.get(roleID));
	  return stateActionIDByRole.get(roleID);
  }
  public synchronized void reload(boolean reloadAll)
  {
    Connection cn = null;
    Statement stmt = null;
    String sql = "";
    ResultSet rs = null;
    try
    {
        cn = DatabaseManager.getInstance().getConnection();
        stmt = cn.createStatement();
        
       /* if(reloadAll)
        {
        	sql = "delete from at_action_permission where apIsDeleted = 1";
        	stmt.executeUpdate(sql);
        }
        
        */
      sql = "select * from at_action_permission";
      if (!reloadAll) {
        sql = sql + " where apLastModificationTime>=" + RepositoryManager.lastModifyTime;
      }
      else
      {
    	  sql += " where apIsDeleted = 0";
      }
      logger.debug("reloading sql " + sql);
      rs = stmt.executeQuery(sql);

      while (rs.next())
      {
//    	int stateActionTypeID = rs.getInt("apID");
    	int stateActionTypeID = rs.getInt("apStateActionTypeID");
        long roleID = rs.getInt("apRoleID");
        boolean isDeleted = rs.getBoolean("apIsDeleted");
        Set<Integer> atypeSet = stateActionIDByRole.get(roleID); 
        if(atypeSet == null)
        {
        	atypeSet = new HashSet<Integer>();
        	stateActionIDByRole.put(roleID, atypeSet);
        }
        if(isDeleted)
        {
        	atypeSet.remove(stateActionTypeID);
        }
        else
        {
        	atypeSet.add(stateActionTypeID);
        }
      }
      
    }
    catch (Exception ex)
    {
    	logger.debug("Exception " , ex);
    }
    finally
    {
       try {
           if (stmt != null) {
               stmt.close();
           }
       } catch (Exception e) {           
       }
       try {
           if (cn != null) {
               DatabaseManager.getInstance().freeConnection(cn);
           }
       } catch (Exception e) {
       }
    }
  }



  public String getTableName()
  {
    return this.tableName;
  }

	public static void main(String args[])
	{
		ActionPermissionRepository.getInstance();
	}
 
}