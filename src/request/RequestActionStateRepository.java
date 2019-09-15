package request;

import static util.SqlGenerator.populateObjectFromDB;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import common.ModuleConstants;
import connection.DatabaseConnection;
import login.LoginDTO;
import permission.ActionStateDTO;
import permission.PermissionRepository;

/*
 * 
 * One time load repository
 * 
 */

public class RequestActionStateRepository {
	static Logger logger = Logger.getLogger(RequestActionStateRepository.class);
	private static RequestActionStateRepository instance = null;
	private static HashMap<Integer, ActionStateDTO> ActionStateDTOByActionID;
	private static HashMap<Integer, Collection<ActionStateDTO>> ActionStateDTOByModuleID;
	private static HashMap<Integer, Collection<ActionStateDTO>> ActionStateDTOByEntityTypeID;
	private static HashMap<Integer, ActionStateDTO> ActionStateDTOByColumnID;
	private RequestActionStateRepository()
	{
		ActionStateDTOByActionID = new HashMap<Integer, ActionStateDTO>();
		ActionStateDTOByEntityTypeID =  new HashMap<Integer, Collection<ActionStateDTO>>();
		ActionStateDTOByModuleID = new HashMap<Integer, Collection<ActionStateDTO>>();
		ActionStateDTOByColumnID = new HashMap<Integer, ActionStateDTO>();
		reload();
	}
	public static RequestActionStateRepository getInstance()
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
			instance = new RequestActionStateRepository();
		}
		
	}
	public synchronized ActionStateDTO getActionStateDTOActionTypeID(int actionTypeID)
	{
//		logger.debug("ActionStateDTOByActionID " + ActionStateDTOByActionID);
		return ActionStateDTOByActionID.get(actionTypeID);
	}
	public synchronized ActionStateDTO getActionStateDTOByColumnID(int columnID)
	{
//		logger.debug("ActionStateDTOByActionID " + ActionStateDTOByActionID);
		return ActionStateDTOByColumnID.get(columnID);
	}
	public void reload()
	{
		DatabaseConnection dbConnection = new DatabaseConnection();
		try
		{
			dbConnection.dbOpen();
			dbConnection.dbTransationStart();
			String sql = "select * from at_action_state";
			Statement stmt = dbConnection.getNewStatement();
			ResultSet rs =  stmt.executeQuery(sql);
			
			while(rs.next())
			{
				ActionStateDTO dto = new ActionStateDTO();
				populateObjectFromDB(dto, rs);
				ActionStateDTOByActionID.put(dto.getActionTypeID(), dto);
				int moduleID = Math.abs(dto.getActionTypeID() / ModuleConstants.MULTIPLIER);
				Collection<ActionStateDTO> collectionModule = ActionStateDTOByModuleID.get(moduleID);
				if(collectionModule == null)
				{
					collectionModule = new ArrayList<ActionStateDTO>();					
					ActionStateDTOByModuleID.put(moduleID,  collectionModule);					
				}
				collectionModule.add(dto);
				
				int columnID = rs.getInt("asIsColumnID");
				ActionStateDTOByColumnID.put(columnID, dto);
				
				Collection<ActionStateDTO> collectionEntity = ActionStateDTOByEntityTypeID.get(dto.getEntityTypeID());
				if(collectionEntity == null)
				{
					collectionEntity = new ArrayList<ActionStateDTO>();					
					ActionStateDTOByEntityTypeID.put(dto.getEntityTypeID(), collectionEntity);	
				}
				collectionEntity.add(dto);
//				logger.debug(dto.getEntityTypeID() +" " + dto);
				
			}
			dbConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Fatal", ex);
			try {
				dbConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			dbConnection.dbClose();
		}		
	}

	public synchronized Set<Integer> getAllActions(CommonRequestDTO comDTO, LoginDTO loginDTO) {
		// TODO Auto-generated method stub
		Set<Integer> actionSet = new HashSet<Integer>();
	
		//logger.debug("comDTO " + comDTO);
		Collection<ActionStateDTO> collection = ActionStateDTOByActionID.values();
		if(comDTO.getModuleID() > 0)
		{
			collection = ActionStateDTOByModuleID.get(comDTO.getModuleID());
		}
		else if(comDTO.getEntityTypeID() > 0)
		{
			collection = ActionStateDTOByEntityTypeID.get(comDTO.getEntityTypeID());
		}
		if(collection == null) return actionSet;
		//logger.debug("collection size " + collection.size());
		for(ActionStateDTO asdto : collection)
		{
			
//			ActionStateDTO asdto = ActionStateDTOByActionID.get(actionid);
			if(asdto == null)continue;
			if(comDTO.isRootActionsOnly() && !asdto.isRootAction())continue;
//			if(asdto.getActionTypeID() < rangeLower && asdto.getActionTypeID() >= rangeUpper)continue;
			if(loginDTO.getAccountID() > 0)
			{
				if(comDTO.isVisibleOnly() && !asdto.isVisibleToClient())continue;
				if(!asdto.isClientAction())continue;
			}
			else if(loginDTO.getUserID() > 0)
			{
				//if(!asdto.isSystemAction())continue;
				if(comDTO.isVisibleOnly() && !asdto.isVisibleToSystem())continue;
			}

//			if(comDTO.getEntityTypeID() != 0 && (comDTO.getEntityTypeID() != asdto.getEntityTypeID()))continue;
			actionSet.add(asdto.getActionTypeID());			
		}
		return actionSet;
	}
	public static void main(String args[])
	{
		/*CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setModuleID(7);
		commonRequestDTO.setVisibleOnly(false);
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUserID(4127L);
		Set<Integer> rootReqIDs = RequestActionStateRepository.getInstance().getAllActions(commonRequestDTO, loginDTO);
		logger.debug("rootReqIDs " + rootReqIDs);*/
		
		Set<Integer> set = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(1);
		logger.debug("set " + set);
	}
}
