package permission;

import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import login.LoginDTO;
import org.apache.log4j.Logger;
import request.*;
import vpn.client.ClientDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static util.SqlGenerator.populateObjectFromDB;

public class PermissionDAO {
	static Logger logger = Logger.getLogger(PermissionDAO.class);
	// private static HashMap<Integer, StateActionDTO> stateActionDTOByID;
	private static HashMap<String, ArrayList<Integer>> IDByStateActionDTO;

	public boolean hasAuthentication(long entityID, int actionTypeID,
			LoginDTO loginDTO, DatabaseConnection dbConnection) {

		return false;
	}
	/*
	 * Compulsory in comDTO:- EntityID, EntityTypeID, RequestTypeID, clientID
	 * If ReqID is given then EntityID, EntityTypeID is not necessary 
	 */
	public boolean hasPermission(CommonRequestDTO comDTO, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception
	{
		RequestUtilDAO rdao = new RequestUtilDAO();
		ArrayList<StateActionDTO> actionList = rdao.getNextActionList(comDTO, loginDTO, databaseConnection, true);
		boolean hasPermission = false;
		for(StateActionDTO sadto:actionList)
		{
			if(sadto.getActionTypeIDs().contains(comDTO.getRequestTypeID()))
			{
				hasPermission = true;
				break;
			}
		}
		
//		if( ( comDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//			||  comDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//				&& loginDTO.getMenuPermission( login.PermissionConstants.VPN_BILL_MIGRATION ) != -1 ){
//
//			hasPermission = true;
//		}

//		else if( ( comDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//			||  comDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//				//&& loginDTO.getMenuPermission( login.PermissionConstants.LLI_BILL_MIGRATION ) != -1
//		){
//
//			hasPermission = true;
//		}
		
		return hasPermission;
	}
	
	public boolean hasMenuPermission(long menuID, int permissionStrength, LoginDTO loginDTO, DatabaseConnection databaseConnection)
	{
		boolean hasPermission = false;
		
		Map<Integer, PermissionDTO> menuPermissionMap = PermissionRepository.getInstance().getMenuPermissionMapByRoleID(loginDTO.getRoleID());
		hasPermission = (menuPermissionMap.get(menuID) != null);
		return hasPermission;
	}
	
	public boolean hasColumnPermission(long columnID, LoginDTO loginDTO, DatabaseConnection databaseConnection)
	{
		boolean hasPermission = false;
		
		Map<Integer, ColumnPermissionDTO> columnPermissionMap = PermissionRepository.getInstance().getColumnPermissionMapByRoleID(loginDTO.getRoleID());
		hasPermission = (columnPermissionMap.get(columnID) != null);
		return hasPermission;
		
	}	
	
	// will be used mainly for sending FR request
	/*
	 * Input (Compulsory):
	 * requestTypeID
	 * entityID
	 * entityTypeID
	 */
	public ArrayList<Long> getUsersHavingPermission(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws SQLException {
		String sql = "select usUserID from aduser where usIsDeleted <> 1 and usRoleID in (select apRoleID from at_action_permission where apStateActionTypeID in (select saID from at_state_actiontype where saCurrentStateID = "
				+ comDTO.getState() + " and saActionTypeID = " + comDTO.getRequestTypeID() + ") and apIsDeleted = 0) and usRoleID != 1";

		logger.debug("sql " + sql);
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList<Long> userIDList = null;
		stmt = databaseConnection.getNewStatement();
		rs = stmt.executeQuery(sql);
		userIDList = new ArrayList<Long>();
		while (rs.next()) {
			userIDList.add(rs.getLong(1));  
		}

		return userIDList;
	}

	

	public HashMap<Integer, ActionStateFormDTO> getActionStateForms(HashMap<Integer, ActionStateFormDTO> actionFormHashMap,	ArrayList<ActionStateDTO> actionStateDTOs) throws Exception {
		if (actionStateDTOs.size() > 0) {
			StringBuilder contidionString = new StringBuilder();	
			DatabaseConnection databaseConnection = new DatabaseConnection();
			databaseConnection.dbOpen();
			contidionString.append("SELECT * FROM at_action_form WHERE afID IN ");
			contidionString.append(arrayListTOString(actionStateDTOs));
			
			Statement stmt = databaseConnection.getNewStatement();
			ResultSet rs =  stmt.executeQuery(contidionString.toString());
			while(rs.next()) {
				ActionStateFormDTO actionStateFormDTO = new ActionStateFormDTO();
				populateObjectFromDB(actionStateFormDTO, rs);
				actionFormHashMap.put(actionStateFormDTO.getID(), actionStateFormDTO);
			}
			databaseConnection.dbClose();
			
		}
		return actionFormHashMap;
	}
	
	

	private StringBuilder arrayListTOString(ArrayList<ActionStateDTO> data) {
		StringBuilder dataString = new StringBuilder();
		int i = 0;
		dataString.append(" ( ");
		for (ActionStateDTO actionStateDTO : data) {
			i++;
			dataString.append(" " + actionStateDTO.getActionTypeID() + " ");

			if (data.size() != i) {
				dataString.append(" , ");
			}

		}
		dataString.append(" ) ");
		return dataString;

	}

	public static void main(String args[])
	{
		PermissionDAO pdao = new PermissionDAO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			/*CommonRequestDTO comDTO = new CommonRequestDTO();
			comDTO.setState(VpnStateConstants.REQUEST_NEW_LINK.NEAR_END_APPLICATION_APPROVED);
			comDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_END);
			ArrayList<Long> list = pdao.getUsersHavingPermission(comDTO, databaseConnection);
			System.out.println("list " + list);*/
			
			LoginDTO loginDTO = new LoginDTO();
			loginDTO.setAccountID(1);
			ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(1, 1);
			logger.debug("clientDetailsDTO.getCurrentStatus() " + clientDetailsDTO.getCurrentStatus());
			int activationStatus = StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus()).getActivationStatus();
			logger.debug("activationStatus " + activationStatus + " clientDetailsDTO.getModuleID() " + clientDetailsDTO.getModuleID());
			
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}

	public boolean hasPermission(int requestTypeID, LoginDTO loginDTO, DatabaseConnection databaseConnection) {
		
		if(loginDTO.getAccountID() > 0)
		{
			if(!RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestTypeID).isClientAction())
			{
				return false;
			}					
		}
		else 
		{
			if(!RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestTypeID).isSystemAction())
			{
				return false;
			}	
		}
		
//		Set<Integer> saIDSet = ActionPermissionRepository.getInstance().getStateActionTypeIDSet(loginDTO.getRoleID());
		Set<Integer> saIDSet = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(loginDTO.getRoleID());
		if(saIDSet != null)
		{
			for(Integer saID: saIDSet)
			{				
				StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
				if(sadto == null)continue;
				int actionTypeID = sadto.getActionTypeIDs().iterator().next();

				if(actionTypeID == requestTypeID)
				{
					return true;
				}
			}
		}
		return false;
	}
}
