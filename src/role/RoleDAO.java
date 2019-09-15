package role;

import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import annotation.Transactional;
import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import login.LoginDTO;
import permission.ActionPermissionDTO;
import permission.ColumnDTO;
import permission.ColumnPermissionDTO;
import permission.MenuDTO;
import permission.PermissionDTO;
import permission.PermissionRepository;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import util.CurrentTimeFactory;
import util.DAOResult;
import util.DatabaseConnectionFactory;
import util.SqlGenerator;

public class RoleDAO{
	static Logger logger= Logger.getLogger((role.RoleDAO.class).getName());
	public List<RoleDTO> getRolesByRoleIDs(List<Long>roleIDs) {
		List<RoleDTO> roleDTOs=new ArrayList<>();
		for(long roleID: roleIDs) {
			roleDTOs.add(PermissionRepository.getInstance().getRoleDTOByRoleID(roleID));
		}
		return roleDTOs;
	}

    public ArrayList<RoleDTO> getAllRoles() {
        ArrayList<RoleDTO> roleDTOs = (ArrayList<RoleDTO>) PermissionRepository.getInstance().getAllRoleDTOs();
		return roleDTOs;
    }
    
    
    public List<RoleDTO> getRolesByRoleName(String roleName){
    	Collection<RoleDTO> allRoleDTOs =  PermissionRepository.getInstance().getAllRoleDTOs();
    	List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
    	
    	for(RoleDTO roleDTO : allRoleDTOs) {
    		if(roleDTO.getRoleName().equalsIgnoreCase(roleName)) {
    			roleDTOs.add(roleDTO);
    		}
    	}
    	
    	return roleDTOs;
    }
    
    public Collection<Long>getRoleIDsWithSearchCriteria(Hashtable<String, String> criteriaMap, LoginDTO loginDTO) throws Exception {
    	List<Long> roleIDs = new ArrayList<>();
		long userID = loginDTO.getUserID();
		UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
		if(userDTO == null ) {
			throw new RequestFailureException( "No user found with id " + userID );
		}
		long roleID = userDTO.getRoleID();
		
		String criteria1Value = criteriaMap.get("rlRoleName");
		String criteria2Value = criteriaMap.get("rlRoleDesc");
		if(criteria1Value == null) {
			criteria1Value = "";
		}
		if(criteria2Value == null) {
			criteria2Value = "";
		}
		List<RoleDTO>descendantRoles = PermissionRepository.getInstance().getDescendantRoleDTOByRoleID(roleID);
		for(RoleDTO roleDTO: descendantRoles) {
			String roleName = roleDTO.getRoleName();
			String roleDescription = roleDTO.getRoleDescription();
			if(roleName.toLowerCase().contains(criteria1Value.toLowerCase()) 
					&& roleDescription.toLowerCase().contains(criteria2Value.toLowerCase())) {
				roleIDs.add(roleDTO.getRoleID());
			}
		}
		return roleIDs;
    }

    

	public void addRole(RoleDTO roleDTO) throws Exception{
         insert(roleDTO);
	}
    public void addMenuPermissionDTO(PermissionDTO permissionDTO) throws Exception{
    	insert(permissionDTO);
    }
    public void addColumnPermissionDTO(ColumnPermissionDTO columnPermissionDTO) throws Exception{
    	insert(columnPermissionDTO);
    }
    public void addActionPermissionDTO(ActionPermissionDTO actionPermissionDTO) throws Exception{
    	insert(actionPermissionDTO);
    }
    public RoleDTO getRoleDTOByRoleID( long roleID ) throws Exception {
    	return PermissionRepository.getInstance().getRoleDTOByRoleID(roleID);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRolePermission(ArrayList<Integer> roles) throws Exception {
		String     sql = "select prPermissionID, prRoleID, prMenuID, prType from vbPermission where prRoleID IN ("+ roles+")";
		sql = sql.replace("[", "");
		sql = sql.replace("]", "");
		logger.debug(sql);
		Connection connection = DatabaseManager.getInstance().getConnection();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);
		ArrayList data = new ArrayList();
		resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			int menuID = resultSet.getInt("prMenuID");
			int permissionType = resultSet.getInt("prType");
		
			PermissionDTO perdto = new PermissionDTO( menuID, permissionType);
			data.add(perdto);
		}
		resultSet.close();
		stmt.close();
		DatabaseManager.getInstance().freeConnection(connection);
		return data;
    }
    @SuppressWarnings("rawtypes")
	public ArrayList getRolePermission(String p_id) throws Exception {
    	return getRolePermission(p_id, -1);
    }
    public ArrayList<PermissionDTO> getRolePermission(String p_id, int moduleTypeID) throws Exception {
    	ArrayList<PermissionDTO> data = new ArrayList<PermissionDTO>();
    	Connection connection=null;
    	Statement stmt=null;
    	ResultSet resultSet = null;
    	String sql = null;
    	String whereClause = "";
    	try {
    		connection = DatabaseManager.getInstance().getConnection();
    		if(moduleTypeID > 0) {
    			sql = "select mnMenuID from admenu where mnModuleTypeID = " + moduleTypeID;
    			stmt = connection.createStatement();
    			resultSet = stmt.executeQuery(sql);
    			whereClause += "(";
    			while(resultSet.next()) {
    				whereClause += resultSet.getInt(1);
    				if(!resultSet.isLast()) {
    					whereClause += ",";
    				}
    			}
    			whereClause += ")";
    			resultSet.close();
    		}
    		else {
    			stmt = connection.createStatement();
    		}
    		sql = "select prPermissionID, prRoleID, prMenuID, prType from adpermission where prRoleID = "+p_id;
    		if(whereClause.length() > 2) {
    			sql += " and prMenuID in " + whereClause;
    		}
    		logger.debug("sql " + sql);
    		resultSet = stmt.executeQuery(sql);
    		while (resultSet.next()) {
    			PermissionDTO perdto = new PermissionDTO();
    			int menuID = resultSet.getInt("prMenuID");
    			int permissionType = resultSet.getInt("prType");
    			perdto.setMenuID(menuID);
    			perdto.setPermissionType(permissionType);
    			data.add(perdto);    			
    		}
    		resultSet.close();
//    		logger.debug("returning data " + data);
    	}
    	catch (Exception e) {
	   	 	logger.fatal("DAO ", e);
    	}
	    finally {
	    	try {
            	if(resultSet!= null && !resultSet.isClosed()) {
            		resultSet.close();
            	}
            } catch (Exception ex) {
                logger.fatal("DAO ", ex);
            }
	    	try{if (stmt != null){stmt.close();}}catch (Exception e){}
	    	try{if (connection != null){DatabaseManager.getInstance().freeConnection(connection);}}
	    	catch (Exception e){logger.fatal("DAO finally :" + e.toString());}
	    }
	    return data;
    }
    public ArrayList<RoleDTO> getPermittedRoleListForSpAdmin(){
    	String sql="select rlRoleID,rlRoleName from adrole";
    	Connection connection=null;
  		Statement stmt=null;
  		ResultSet resultSet = null;
  		ArrayList<RoleDTO> data = new ArrayList<RoleDTO>();
  		RoleDTO dto = null;
  		try {
  			connection = DatabaseManager.getInstance().getConnection();
  			stmt = connection.createStatement();
  			resultSet = stmt.executeQuery(sql);
		    while (resultSet.next())  {
		        dto=new RoleDTO();
		        dto.setRoleID(resultSet.getInt("rlRoleID"));
		        dto.setRoleName(resultSet.getString("rlRoleName"));
		        data.add(dto);
		    }
		    resultSet.close();
		} catch (Exception e) {
	   	 	logger.fatal("DAO " + e.toString(), e);
    	} finally {
	    	try {
            	if(resultSet!= null && !resultSet.isClosed()) {
            		resultSet.close();
            	}
            } catch (Exception ex) {
                logger.fatal("DAO ", ex);
            }
	    	try{if (stmt != null){stmt.close();}}catch (Exception e){}
	    	try{if (connection != null){DatabaseManager.getInstance().freeConnection(connection);}}
	    	catch (Exception e){logger.fatal("DAO finally :" + e.toString());}
	    }
    	return data;
    }
  
    
    public void deleteAllMenuPermissionByRoleID(long roleID) throws Exception{
    	String sql = "update adpermission set prIsDeleted = 1, prLastModificationTime = " + CurrentTimeFactory.getCurrentTime() + " where prRoleID = "+roleID;
    	DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().execute(sql);
    }
    
    public void deleteAllColumnPermissionByRoleID(long roleID) throws Exception{
    	String sql = "update adcolumnpermission set clIsDeleted = 1, clLastModificationTime = "+CurrentTimeFactory.getCurrentTime()+" where clRoleID="+roleID;
    	DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().execute(sql);
    }

    public void deleteAllActionPermissionByRoleID(long roleID) throws Exception{
        String sql = "update at_action_permission set apIsDeleted = 1, apLastModificationTime = "+CurrentTimeFactory.getCurrentTime()+" where apRoleID = " + roleID;
        DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().execute(sql);
    }
    
    public void updateRole(RoleDTO roleDTO) throws Exception{
        updateEntity(roleDTO);
    }
    
    @SuppressWarnings("unused")
	public DAOResult dropRoles(long[] p_ids) throws Exception {
    	DAOResult daoResult = new DAOResult();
        if (p_ids == null || p_ids.length == 0) {
          daoResult.setResult("", true, DAOResult.DONE);
          return daoResult;
        }
        long currentTime = System.currentTimeMillis();
    	DatabaseConnection databaseConnection = new DatabaseConnection();
      	ResultSet resultSet = null;
    	try {
    		databaseConnection.dbOpen();
    		databaseConnection.dbTransationStart();
    		Statement statement = databaseConnection.getNewStatement();
    		String sql = "delete from adrole where rlRoleID =";
    		String permissionDelSql = "delete from adpermission where prRoleID=";
    		String userDelSql = "delete from aduser where usRoleID=";
    		String columnDelSql= "delete from adcolumnpermission where clRoleID=";
        	for (int i = 0; i < p_ids.length; i++) {
        		/*          String fullQuery = sql+p_ids[i];
          		stmt.executeUpdate(fullQuery);*/
        		SqlGenerator.deleteEntityByID(RoleDTO.class, p_ids[i], currentTime, databaseConnection);
        		String fullQuery = permissionDelSql + p_ids[i];
        		statement.executeUpdate(fullQuery);
        		fullQuery = userDelSql + p_ids[i];
        		statement.executeUpdate(fullQuery);
        		fullQuery = columnDelSql + p_ids[i];
        		statement.executeUpdate(fullQuery);
        	}
        	databaseConnection.dbTransationEnd();
        	daoResult.setResult("", true, DAOResult.DONE);
      } catch (Exception e) {
    	  daoResult.setResult(e.toString(), false, DAOResult.DB_EXCEPTION);
    	  logger.debug("DAO ", e);
    	  try {
			databaseConnection.dbTransationRollBack();
    	  } catch (Exception e1) { }
    	  throw e;
      }
      finally {
        databaseConnection.dbClose();
      }
      return daoResult;
    }
    @Transactional(transactionType=util.TransactionType.READONLY)
    public List<MenuDTO> getAllMenuList() throws Exception{
    	return SqlGenerator.getAllUndeletedObjectList(MenuDTO.class, DatabaseConnectionFactory.getCurrentDatabaseConnection());
    }

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<ColumnDTO> getAllColumnList() throws Exception{
		return SqlGenerator.getAllUndeletedObjectList(ColumnDTO.class, DatabaseConnectionFactory.getCurrentDatabaseConnection()); 
    }

    void addMenuPermissionDTOs(List<PermissionDTO> permissionList) throws Exception {
        String sql = "insert into adpermission(prPermissionID, prRoleID, prMenuID, prType, prLastModificationTime) values (?,?,?,?,?)";
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);

        long currentTime = System.currentTimeMillis();
	    for(PermissionDTO permissionDTO: permissionList) {
            int k = 1;
            ps.setLong(k++, databaseConnection.getNextID("adpermission"));
            ps.setLong(k++, permissionDTO.getRoleID());
            ps.setInt(k++, permissionDTO.getMenuID());
            ps.setInt(k++, permissionDTO.getPermissionType());
            ps.setLong(k++, currentTime);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

   void addColumnPermissionDTOs(List<ColumnPermissionDTO> columnPermissionList) throws Exception {
        String sql = "insert into adcolumnpermission(clPermissionID, clRoleID, clColumnID, clIsDeleted, clLastModificationTime) values (?,?,?,?,?)";
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
        long currentTime = System.currentTimeMillis();
        for(ColumnPermissionDTO columnPermissionDTO : columnPermissionList)
        {
            int k = 1;
            ps.setLong(k++, databaseConnection.getNextID("adcolumnpermission"));
            ps.setLong(k++, columnPermissionDTO.getRoleID());
            ps.setInt(k++, columnPermissionDTO.getColumnID());
            ps.setInt(k++, 0);
            ps.setLong(k++, currentTime);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }
    void addActionPermissionDTOs(List<ActionPermissionDTO> actionPermissionList) throws Exception {
        String sql = "insert into at_action_permission(apID, apStateActionTypeID, apLastModificationTime, apRoleID, apIsDeleted) values (?,?,?,?,?)";
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);

        long currentTime = System.currentTimeMillis();
        for(ActionPermissionDTO actionPermissionDTO : actionPermissionList)
        {
            int k = 1;
            ps.setLong(k++, databaseConnection.getNextID("at_action_permission"));
            ps.setInt(k++, actionPermissionDTO.getStateActionTypeID());
            ps.setLong(k++, currentTime);
            ps.setLong(k++, actionPermissionDTO.getRoleID());
            ps.setInt(k++, 0);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }
}