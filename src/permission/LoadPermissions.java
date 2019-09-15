package permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import databasemanager.DatabaseManagerImplementation;
import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import org.jdom.JDOMException;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import request.RequestStateActionRepository;
import request.StateDTO;
import request.StateRepository;
 
public class LoadPermissions {
	static Logger logger = Logger.getLogger(LoadPermissions.class);
	public static void main(String args[]) throws Exception
	{	
		logger.debug("Load Permission");
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			Statement stmt = databaseConnection.getNewStatement();
			
			String sql = "delete from at_action_permission where apStateActionTypeID not in (select saID from at_state_actiontype)";
			stmt.addBatch(sql);
			
			sql = "delete from adpermission where prRoleID = 1";			
			stmt.addBatch(sql);
			
			sql = "delete from at_action_permission where apRoleID = 1";
			stmt.addBatch(sql);
			
			sql = "delete from adcolumnpermission where clRoleID = 1";
			stmt.addBatch(sql);			
			logger.info("Deleting from at_action_permission, adpermission(role=1), at_action_permission(role=1), adcolumnpermission(role=1)");
			stmt.executeBatch();
			
			sql = "select * from admenu";
			logger.info("Fetching all data from admenu");
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Integer> idlist = new ArrayList<>();
			while(rs.next()) {
				idlist.add(rs.getInt("mnMenuID"));
			}
			
			sql = "insert into adpermission(prPermissionID, prRoleID, prMenuID, prType, prLastModificationTime) values (?,?,?,?,?)";
			PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
			for(Integer menuid: idlist) {
				int k = 1;
				ps.setLong(k++, databaseConnection.getNextID("adpermission"));
				ps.setInt(k++, 1);
				ps.setInt(k++, menuid);
				ps.setInt(k++, 3);
				ps.setLong(k++, System.currentTimeMillis());
				ps.addBatch();
			}
			logger.info("Inserting in adpermission using admenu data");
			ps.executeBatch();
			ps.close();
			
			sql = "select * from adcolumn";
			logger.info("Fetching all data from adcolumn");
			rs = stmt.executeQuery(sql);
			idlist = new ArrayList<>();
			while(rs.next()) {
				idlist.add(rs.getInt("columnID"));
			}
			
			sql = "insert into adcolumnpermission(clPermissionID, clRoleID, clColumnID) values (?,?,?)";
			ps = databaseConnection.getNewPrepareStatement(sql);
			for(Integer columnid: idlist) {
				int k = 1;
				ps.setLong(k++, databaseConnection.getNextID("adcolumnpermission"));
				ps.setInt(k++, 1);
				ps.setInt(k++, columnid);				
				ps.addBatch();
			}
			logger.info("Inserting in adcolumnpermission using adcolumn data");
			ps.executeBatch();
			ps.close();			

			Collection<StateDTO> statelist = StateRepository.getInstance().getAllStateDTOs();
			for(StateDTO sdto: statelist) {
				StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionByStateID(sdto.getId());
				
			}
			ArrayList<Integer> saIDList = new ArrayList<>();
			sql = "select * from at_state_actiontype";
			logger.info("Fetching all data from at_state_actiontype");
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
				//rs.getInt("saCurrentStateID");
				int actionType = rs.getInt("saActionTypeID");
				logger.debug("actionType " + actionType);
				if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionType).isSystemAction()) {
					saIDList.add(rs.getInt("saID"));
				}
			}
			
			sql = "insert into at_action_permission(apID, apStateActionTypeID, apLastModificationTime, apRoleID, apIsDeleted) values (?,?,?,?,?)";
			ps = databaseConnection.getNewPrepareStatement(sql);
			
			long currentTime = System.currentTimeMillis();
			for(Integer saID: saIDList) {
				int k = 1;
				ps.setLong(k++, databaseConnection.getNextID("at_action_permission"));
				ps.setInt(k++, saID);
				ps.setLong(k++, currentTime);
				ps.setInt(k++, 1);
				ps.setInt(k++, 0);
				ps.addBatch();
			}
			logger.info("Inserting in at_action_permission using at_state_actiontype data");
			ps.executeBatch();
			ps.close();

			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex) {
			logger.debug("ex", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally {
			databaseConnection.dbClose();
			DatabaseManagerImplementation.getInstance().closeAllConnections();
		}
		logger.info("Done");
		System.exit(0);
	}
}
