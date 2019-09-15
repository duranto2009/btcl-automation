package request;

import common.*;
import common.constants.RequestTypeConstants;
import common.repository.AllClientRepository;
import config.GlobalConfigConstants;
import config.GlobalConfigurationRepository;
import connection.DatabaseConnection;
import login.ColumnPermissionConstants;
import login.LoginDTO;
import org.apache.log4j.Logger;
import permission.ActionStateDTO;
import permission.PermissionRepository;
import permission.PermissionService;
import permission.StateActionDTO;
import util.ServiceDAOFactory;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;
class PendingRequestCount
{
	String itemName;
	int count;
}
 
public class RequestUtilDAO {
	static Logger logger = Logger.getLogger(RequestUtilDAO.class);

	
	private Set<Integer> getNextActionSetByStateSet(Set<Integer> stateSet, CommonRequestDTO comDTO, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		Set<Integer> actionSet = new HashSet<Integer>();		
		actionSet.addAll(getPermittedActionTypeSet(stateSet, comDTO.isVisibleOnly(), loginDTO,databaseConnection));
		
		if(!SqlGenerator.isActivated(EntityTypeConstant.entityClassMap.get(comDTO.getEntityTypeID()), comDTO.getEntityID(), databaseConnection))
		{
			logger.debug("not active");
			logger.debug("actionList " + actionSet);
			return actionSet;
		}
		else
		{
			actionSet.addAll(getGeneralRootActionSet(actionSet, comDTO, loginDTO, databaseConnection));
			return actionSet;
		}
	}
	
	/*
	 * comDTO must have RootRequestID 
	 */
	public void completeRequestByRootID(Long rootID, DatabaseConnection databaseConnection) throws SQLException
	{
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.ALL_PROCESSED
				+ " where arRootRequestID = " + rootID 
				+ " or (arID = " + rootID + " and arRootRequestID  is null)"; 
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);		
	}
	
	public void completeRequestByEntity(Long entityID, int entityTypeID, DatabaseConnection databaseConnection) throws Exception
	{
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.ALL_PROCESSED 
				+ " where " + SqlGenerator.getColumnName(CommonRequestDTO.class, "entityID") + " = " + entityID 
				+ " and " + SqlGenerator.getColumnName(CommonRequestDTO.class, "entityTypeID") + " = " + entityTypeID
				+ " and " + SqlGenerator.getColumnName(CommonRequestDTO.class, "isDeleted") + " = 0";				
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);			
	}


	public void updateRequestByRequestID(long requestID, DatabaseConnection databaseConnection) throws SQLException
	{
		updateRequestByRequestID(requestID, false, databaseConnection);
	}
	
	public void updateRequestByRequestID(long requestID, boolean makeIsDeletedTrue, DatabaseConnection databaseConnection) throws SQLException
	{
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.SEMI_PROCESSED;
		if(makeIsDeletedTrue)
		{
			sql += ", arIsDeleted = 1 ";
		}
		sql += " where arID = " + requestID; 
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);	
	}

	public void updateRequestByRootRequestID(long rootReqID, DatabaseConnection databaseConnection) throws SQLException
	{
		logger.debug("rootReqID " + rootReqID);		
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.SEMI_PROCESSED
				+ " where arRootRequestID = " + rootReqID
				+ " or (arID = " + rootReqID + " and arRootRequestID  is null)";
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);		
	}
	
	public void updateRequestByRootRequestID(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws SQLException
	{
		logger.debug("comDTO " + comDTO);
		//if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(comDTO.getRequestTypeID()).isRootAction())return;
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.SEMI_PROCESSED
				+ " where arRootRequestID = " + comDTO.getRootReqID() 
				+ " or (arID = " + comDTO.getRootReqID() + " and arRootRequestID  is null)"; 
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);		
	}
	
	public void updateRequestByEntity(int entityTypeID, long entityID, DatabaseConnection databaseConnection) throws SQLException
	{
		//if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(comDTO.getRequestTypeID()).isRootAction())return;
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.SEMI_PROCESSED
				+ " where arEntityID = " + entityID + " and arEntityTypeID = " + entityTypeID + " and arCompletionStatus = 0"; 
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);		
	}
	
	public void updateRequestByRootRequestIDExceptLastOne(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws SQLException
	{
		logger.debug("comDTO " + comDTO);
		//if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(comDTO.getRequestTypeID()).isRootAction())return;
		String sql = "update at_req set arCompletionStatus =  " + RequestStatus.SEMI_PROCESSED
				+ " where (arRootRequestID = " + comDTO.getRootReqID() 
				+ " or (arID = " + comDTO.getRootReqID() + " and arRootRequestID  is null))"
				+ " and arID != " + comDTO.getReqID(); 
		logger.debug("sql " + sql);
		Statement stmt = databaseConnection.getNewStatement();
		stmt.executeUpdate(sql);		
	}	
	/*
	 * comDTO must have entityID, entityTypeID, requestTypeID in input
	 * only entityID and entityTypeID together can not be unique. For uniqueness we have to take requestTypeID too 
	 * 
	 */
	public CommonRequestDTO getRequestDTOWithRootValues(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws SQLException
	{
		logger.debug("getRequestDTOWithRootValues comDTO " + comDTO);
		ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(comDTO.requestTypeID);
		if(asdto == null)
			return comDTO;
		
		int rootRequestTypeID = asdto.getRootActionTypeID();
		logger.debug("rootRequestTypeID " + rootRequestTypeID);
		String sql = "select * from at_req "
				+ " where arRequestTypeID = " + rootRequestTypeID 
				+ " and arEntityID = " + comDTO.getEntityID() 
				+ " and arEntityTypeID = "+ comDTO.getEntityTypeID() 
				+ " and arCompletionStatus != "+ RequestStatus.ALL_PROCESSED + " having arReqTime = min(arReqTime)";
		logger.debug("sql "+ sql);
		Statement stmt = databaseConnection.getNewStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next())
		{
			comDTO.setRootReqID(rs.getLong("arID"));
			comDTO.setClientID(rs.getLong("arClientID"));
		}
		logger.debug("returning comDTO " + comDTO);
		return comDTO;		
	}
	/*
	 * 
	 * Returned Set is not null
	 * 
	 */
	private Set<Integer> getGeneralRootActionSet(Set<Integer> stateSet, CommonRequestDTO comDTO, LoginDTO loginDTO,	DatabaseConnection databaseConnection) {
		Set<Integer> rootReqTypeList = RequestActionStateRepository.getInstance().getAllActions(comDTO, loginDTO);
		logger.debug("rootReqTypeList " + rootReqTypeList);
		/*
		 * now filter out root ids which are related to actionlist
		 * may be a column rootRequestType is needed in at_state (in this case isroot column is not necessary). Then we can filter out the roottype
		 */
		Set<Integer> actionTypeListToRemove = new HashSet<Integer>();
		if(stateSet != null)
		{
			for(Integer state: stateSet)
			{			
				logger.debug("state " + state);
				StateDTO sdto = StateRepository.getInstance().getStateDTOByStateID(state);
				actionTypeListToRemove.add(sdto.getRootRequestTypeID());			
			}
		}
		rootReqTypeList.removeAll(actionTypeListToRemove);
		
		
		return rootReqTypeList;
	}

	public ArrayList<StateActionDTO> getNextActionList(CommonRequestDTO comDTO, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception
	{
		return getNextActionList(comDTO, loginDTO, databaseConnection, false);
	}
	public ArrayList<StateActionDTO> getNextActionList(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO, DatabaseConnection databaseConnection, boolean forPermissionCheck) throws Exception
	{
		logger.debug("getNextActionList with comDTO " + commonRequestDTO);

		int moduleID = EntityTypeConstant.entityModuleIDMap.get(commonRequestDTO.getEntityTypeID());
		/*
		 * Checking if the owner of the document is disabled then function return blank action list
		 */
		if(commonRequestDTO.getEntityTypeID() % 100 != 51)
		{
			ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(commonRequestDTO.getClientID(), moduleID);
			logger.debug("clientDetailsDTO " + clientDetailsDTO);
			StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus());
			int activationStatus = stateDTO.getActivationStatus();
			if(activationStatus == EntityTypeConstant.STATUS_NOT_ACTIVE)			
			{
				logger.debug("Client not active");
				return new ArrayList<StateActionDTO>();
			}
			if(activationStatus != EntityTypeConstant.STATUS_ACTIVE && loginDTO.getUserID() > 0)
			{
				logger.debug("Client semi active");
				return new ArrayList<StateActionDTO>();
			}
		}
		
		CommonDAO commonDAO = new CommonDAO();
		
		EntityDTO entityDTO = commonDAO.getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(), databaseConnection);
		logger.debug("entityDTO " + entityDTO);
		logger.debug("entityDTO.getCurrentStatus() " + entityDTO.getCurrentStatus());
		logger.debug("entityDTO.getLatestStatus() " + entityDTO.getLatestStatus());
		StateDTO latestStateDTO = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getLatestStatus());
		StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getCurrentStatus());
		boolean latestStatusActive = (latestStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);		
		boolean currentStatusActive = (currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);
		boolean inAFlow = (!currentStatusActive || (currentStatusActive && !latestStatusActive)); 
		
		boolean currentStatusInactive = (currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_NOT_ACTIVE);
		
		if(entityDTO.isDeleted())
		{
			return new ArrayList<StateActionDTO>();
		}		
		if(RequestStateActionRepository.getInstance().getStateActionByStateID(currentStateDTO.getId()) != null)			
//		if(currentStateDTO.getId() < 0 && Math.abs(currentStateDTO.getId()) % 1000 == 301)//disabled
		{
			
		}
		else if(currentStatusInactive)
		{
			return new ArrayList<StateActionDTO>();
		}
		
		Set<Integer> stateSet = null;
		PermissionService permissionService = new PermissionService();
		Set<CommonRequestDTO> bottomReqDTOSet = new HashSet<CommonRequestDTO>();
		HashMap<Integer, CommonRequestDTO> stateReqMap = new HashMap<Integer, CommonRequestDTO>();  

		bottomReqDTOSet = getBottomRequestDTOsByEntity(commonRequestDTO, databaseConnection);				
		
		Set<Integer> requestTypeSetToRemove = new HashSet<Integer>();
		HashMap<Integer, CommonRequestDTO> statesTakenOnlyForView = new HashMap<Integer, CommonRequestDTO>();
		HashMap<Integer, CommonRequestDTO> statesTakenOnlyForCancel = new HashMap<Integer, CommonRequestDTO>();
		logger.debug("bottomReqDTOSet " + bottomReqDTOSet);
		for(CommonRequestDTO bottomRequestDTO: bottomReqDTOSet)//actually loop for single time now
		{
			logger.debug("bottomRequestDTO " + bottomRequestDTO);
			ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(bottomRequestDTO.getRequestTypeID());
			logger.debug("asdto " + asdto);
			if(asdto == null) continue;
			int stateID = asdto.getNextStateID();
			
			boolean canCancel = false;
			boolean canTakeAction = false;
			boolean canView = false;//always true for understanding
			boolean directlyRelatedToRequest = false;
			if(loginDTO.getUserID() > 0)
			{
				if(bottomRequestDTO.getRequestByAccountID() == -loginDTO.getUserID())
				{
					canCancel = true;
					directlyRelatedToRequest = true;
				}

				if(permissionService.hasPermission(bottomRequestDTO.getRequestTypeID(), loginDTO))// then this is a request to specific person
				{						
					canCancel = true; 
				}

				if(bottomRequestDTO.getRequestToAccountID() == null)
				{
					canTakeAction = true;
				}
				else if(bottomRequestDTO.getRequestToAccountID() == -loginDTO.getUserID())
				{
					canTakeAction = true;
					directlyRelatedToRequest = true;
				}

				ActionStateDTO bottomActionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(bottomRequestDTO.getRequestTypeID());
				logger.debug("bottomActionStateDTO " + bottomActionStateDTO);
				StateDTO bottomStateDTO = StateRepository.getInstance().getStateDTOByStateID(bottomActionStateDTO.getNextStateID());
				logger.debug("bottomStateDTO " + bottomStateDTO);
				if(bottomStateDTO.isVisibleToSystem())
				{
					logger.debug("loginDTO.getColumnPermission(ColumnPermissionConstants.moduleViewPageRestrictionMap.get(moduleID)) " + loginDTO.getColumnPermission(ColumnPermissionConstants.moduleViewPageRestrictionMap.get(moduleID)));
					if(loginDTO.getColumnPermission(ColumnPermissionConstants.moduleViewPageRestrictionMap.get(moduleID)))
					{
						canView = true;
					}
					if(directlyRelatedToRequest)
					{
						canView = true;
					}
				}

				if(!canView)
				{
					canTakeAction = false;
					canCancel = false;
				}				
			}
			else if(loginDTO.getAccountID() > 0)
			{

				if(bottomRequestDTO.getRequestByAccountID() == loginDTO.getAccountID())
				{
					canCancel = true;
				}

				if(bottomRequestDTO.getRequestToAccountID() == null)
				{
					canTakeAction = true;
				}
				else if(bottomRequestDTO.getRequestToAccountID() == loginDTO.getAccountID())
				{
					canTakeAction = true;
				}

				if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(bottomRequestDTO.getRequestTypeID()).isVisibleToClient())
				{
					canView = true;
				}
			}
			logger.debug("canView " + canView + " canTakeAction " + canTakeAction + " canCancel " + canCancel);
			if(canTakeAction)
			{
				stateReqMap.put(stateID, bottomRequestDTO);
			}
			if(canCancel)
			{
				statesTakenOnlyForCancel.put(stateID, bottomRequestDTO);					
			}
			if(canView)
			{
				statesTakenOnlyForView.put(stateID, bottomRequestDTO);						
			}
		}
		stateSet = stateReqMap.keySet();
		logger.debug("stateSet " + stateSet);
		HashMap<Integer, Set<Integer>> permittedStateActionMap = new HashMap<Integer, Set<Integer>>();
		
		int blankState = -(moduleID * ModuleConstants.MULTIPLIER);


		if(loginDTO.getUserID() > 0)
		{
			Set<Integer> saIDSet = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(loginDTO.getRoleID());
									
			HashMap<Integer, HashSet<Integer>> enabledStateActionMap = new HashMap<Integer, HashSet<Integer>>();
			if(saIDSet != null)
			{
				for(Integer saID: saIDSet)
				{				
					StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
					if(sadto == null)continue;
					int action = sadto.getActionTypeIDs().iterator().next();
					if(enabledStateActionMap.get(sadto.getStateID()) == null)
					{
						enabledStateActionMap.put(sadto.getStateID(), new HashSet<Integer>());
					}
					enabledStateActionMap.get(sadto.getStateID()).add(action);
				}
			}
			if(stateSet == null)
			{
				stateSet = new HashSet<Integer>();
			}

			logger.debug("isActive " + currentStatusActive);
			logger.debug("inAFlow " + inAFlow);

			if(inAFlow)

			{							
				for(Integer state: stateSet)
				{
					Set<Integer> enabledActionSet = enabledStateActionMap.get(state);
					logger.debug("enabledActionSet " + enabledActionSet);
					if(enabledActionSet == null) continue;
					Set<Integer> permittedActionSet = getPermittedActionTypeSet(state, commonRequestDTO.isVisibleOnly(), loginDTO, databaseConnection);
					logger.debug("permittedActionSet " + permittedActionSet);
					if(permittedActionSet == null) continue;
					permittedActionSet.retainAll(enabledActionSet);//intersection
					
					if(permittedActionSet.size() == 0) continue;
					permittedStateActionMap.put(state, permittedActionSet);
				}				
			}
			else if(enabledStateActionMap != null && enabledStateActionMap.size() > 0) 
			{
				Set<Integer> basicActionSet = getGeneralRootActionSet(stateSet, commonRequestDTO, loginDTO, databaseConnection);
				logger.debug("basicActionSet " + basicActionSet);
				Set<Integer> blankActions = getPermittedActionTypeSetForEmptyStates(blankState, commonRequestDTO.isVisibleOnly(), commonRequestDTO.getEntityTypeID(), loginDTO, databaseConnection);
				basicActionSet.addAll(blankActions);
				logger.debug("blankActions " + blankActions);
				logger.debug("basicActionSet " + basicActionSet);
				basicActionSet.retainAll(enabledStateActionMap.get(blankState));
				
				permittedStateActionMap.put(blankState, basicActionSet);								
			}						
		}		
		else
		{			
			//if(!new StatusHistoryDAO().isActive(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(), databaseConnection))

			if(inAFlow)
			{
				for(Integer state: stateSet)
				{
					Set<Integer> permittedActionSet = getPermittedActionTypeSet(state, commonRequestDTO.isVisibleOnly(), loginDTO, databaseConnection);
					if(permittedActionSet == null) continue;
					if(permittedActionSet.size() == 0) continue;
					permittedStateActionMap.put(state, permittedActionSet);
				}				
			}
			else
			{
				permittedStateActionMap.put(blankState, getGeneralRootActionSet(stateSet, commonRequestDTO, loginDTO, databaseConnection));				
			}
		}
		logger.debug("permittedStateActionMap " + permittedStateActionMap);
		ArrayList<StateActionDTO> stateActionList = new ArrayList<StateActionDTO>();
		for(Integer state: permittedStateActionMap.keySet())
		{
			Set<Integer> actionTypeSet = permittedStateActionMap.get(state);
			actionTypeSet.removeAll(requestTypeSetToRemove);
			StateActionDTO sadto = new StateActionDTO();
			sadto.setStateID(state);
			sadto.setActionTypeIDs(actionTypeSet);
			if(state == blankState)
			{				
				if(bottomReqDTOSet.size() > 0)
				{
					sadto.setCommonRequestDTO(bottomReqDTOSet.iterator().next());
				}
				else
				{					
					//StateDTO sdto = new StatusHistoryDAO().getRequestStatus(commonRequestDTO, databaseConnection);
					CommonRequestDTO commonRequestDTO2 = new CommonRequestDTO();
					commonRequestDTO2.setDescription(latestStateDTO.getName());
//					commonRequestDTO2.setDescription(currentStateDTO.getName());
					sadto.setCommonRequestDTO(commonRequestDTO2);
				}
			}
			else
			{
				sadto.setCommonRequestDTO(stateReqMap.get(state));
			}
			stateActionList.add(sadto);
		}
		logger.debug("stateActionList " + stateActionList);
		logger.debug("statesTakenOnlyForView " + statesTakenOnlyForView);
		logger.debug("statesTakenOnlyForCancel " + statesTakenOnlyForCancel);
		if(!forPermissionCheck)
		{
			for(Integer state:statesTakenOnlyForView.keySet())
			{
				boolean skip = false;
				for(StateActionDTO stateActionDTO: stateActionList)
				{
					if(stateActionDTO.getStateID() == state)
					{
						skip = true;
						break;
					}
				}
				if(skip)
				{
					continue;
				}
				StateActionDTO sadto = new StateActionDTO();
				sadto.setStateID(state);
				sadto.setActionTypeIDs(new HashSet<Integer>());
				sadto.setCommonRequestDTO(statesTakenOnlyForView.get(state));
				stateActionList.add(sadto);
			}
		}
		
		if(forPermissionCheck)
		{
			for(Integer state:statesTakenOnlyForCancel.keySet())
			{				
				StateActionDTO sadto = new StateActionDTO();
				sadto.setStateID(state);
				Set<Integer> actionSet = new HashSet<Integer>();
				actionSet.add(-(moduleID * ModuleConstants.MULTIPLIER + 199));
				sadto.setActionTypeIDs(actionSet);
				sadto.setCommonRequestDTO(statesTakenOnlyForView.get(state));
				stateActionList.add(sadto);
			}		
		}
		
		Collections.sort(stateActionList, new Comparator<StateActionDTO>() {
		    public int compare(StateActionDTO o1, StateActionDTO o2) {
		        return new Integer(o2.getStateID()).compareTo(o1.getStateID());
		    }
		});
		logger.debug("returning stateActionList " + stateActionList);
		return stateActionList;
		
	}	
	
	/*
	 * It returns all possible actions a state can take irrespective of role permission.
	 * 
	 */
	private Set<Integer> getPermittedActionTypeSet(Integer state, boolean isVisibleOnly, LoginDTO loginDTO, DatabaseConnection databaseConnection) {		
		Set<Integer> actionlist = new HashSet<Integer>();
		logger.debug("loginDTO " + loginDTO);
//		for(Integer state: stateSet)
		{
			logger.debug("dtolist.get(i).getState() " + state);
			//CommonRequestDTO comDTO =  dtolist.get(i);
			StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionByStateID(state);
			if(sadto == null) return null;
			//StateActionDTO filterdSaDTO = new StateActionDTO();
			logger.debug("sadto " + sadto);
			Set<Integer> RequestTypeList = new HashSet<Integer>();
			for(int actionTypeID: sadto.getActionTypeIDs())
			{				
				ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
				if(asdto == null)continue;
				if(loginDTO.getAccountID() > 0)
				{
					if(isVisibleOnly && !asdto.isVisibleToClient())continue;
					if(!asdto.isClientAction())continue;
				}
				else if(loginDTO.getUserID() > 0)
				{
					if(!asdto.isSystemAction())continue;
					if(isVisibleOnly && !asdto.isVisibleToSystem())continue;
				}
				RequestTypeList.add(asdto.getActionTypeID());
			}
			//filterdSaDTO.setActionTypeIDs(RequestTypeList);
			actionlist.addAll(RequestTypeList);			
		}
		
		boolean migrationEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MIGRATION_ENABLED).getValue() > 0;
		if(!migrationEnabled)
		{
			actionlist.removeAll(RequestTypeConstants.migrationActionSet);
		}
		
		return actionlist;
	}	
	
	private Set<Integer> getPermittedActionTypeSetForEmptyStates(Integer state, boolean isVisibleOnly, int entityTypeID, LoginDTO loginDTO, DatabaseConnection databaseConnection) {		
		Set<Integer> actionlist = new HashSet<Integer>();
		logger.debug("loginDTO " + loginDTO);
//		for(Integer state: stateSet)
		{
			logger.debug("dtolist.get(i).getState() " + state);
			//CommonRequestDTO comDTO =  dtolist.get(i);
			StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionByStateID(state);
			if(sadto == null) return null;
			//StateActionDTO filterdSaDTO = new StateActionDTO();
			logger.debug("sadto " + sadto);
			Set<Integer> RequestTypeList = new HashSet<Integer>();
			for(int actionTypeID: sadto.getActionTypeIDs())
			{				
				ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
				if(asdto == null)continue;
				if(loginDTO.getAccountID() > 0)
				{
					if(isVisibleOnly && !asdto.isVisibleToClient())continue;
					if(!asdto.isClientAction())continue;
				}
				else if(loginDTO.getUserID() > 0)
				{
					if(!asdto.isSystemAction())continue;
					if(isVisibleOnly && !asdto.isVisibleToSystem())continue;
				}
				if(asdto.getEntityTypeID() != entityTypeID)
				{
					continue;
				}
				RequestTypeList.add(asdto.getActionTypeID());
			}
			//filterdSaDTO.setActionTypeIDs(RequestTypeList);
			actionlist.addAll(RequestTypeList);			
		}
		return actionlist;
	}	
	
	
	private Set<Integer> getPermittedActionTypeSet(Set<Integer> stateSet, boolean isVisibleOnly, LoginDTO loginDTO, DatabaseConnection databaseConnection) {		
		Set<Integer> actionlist = new HashSet<Integer>();
		logger.debug("loginDTO " + loginDTO);
		for(Integer state: stateSet)
		{
			logger.debug("dtolist.get(i).getState() " + state);
			//CommonRequestDTO comDTO =  dtolist.get(i);
			StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionByStateID(state);
			if(sadto == null) continue;
			new StateActionDTO();
			logger.debug("sadto " + sadto);
			Set<Integer> RequestTypeList = new HashSet<Integer>();
			for(int actionTypeID: sadto.getActionTypeIDs())
			{				
				ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
				if(asdto == null)continue;
				if(loginDTO.getAccountID() > 0)
				{
					if(isVisibleOnly && !asdto.isVisibleToClient())continue;
					if(!asdto.isClientAction())continue;
				}
				else if(loginDTO.getUserID() > 0)
				{
					if(!asdto.isSystemAction())continue;
					if(isVisibleOnly && !asdto.isVisibleToSystem())continue;
				}
				RequestTypeList.add(asdto.getActionTypeID());
			}
			//filterdSaDTO.setActionTypeIDs(RequestTypeList);
			actionlist.addAll(RequestTypeList);			
		}
		return actionlist;
	}
	public Set<Integer> getNextActionSetByStateAndUser(int stateID, LoginDTO loginDTO, DatabaseConnection databaseConnection) {
		Set<Integer> saIDSet = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(loginDTO.getRoleID());
		
		HashMap<Integer, HashSet<Integer>> enabledStateActionMap = new HashMap<Integer, HashSet<Integer>>();
		if(saIDSet != null)
		{
			for(Integer saID: saIDSet)
			{				
				StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
				if(sadto == null)continue;
				int action = sadto.getActionTypeIDs().iterator().next();
				if(enabledStateActionMap.get(sadto.getStateID()) == null)
				{
					enabledStateActionMap.put(sadto.getStateID(), new HashSet<Integer>());
				}
				enabledStateActionMap.get(sadto.getStateID()).add(action);
			}
		}

//		Set<Integer> set2 = new RequestUtilDAO().getPermittedActionTypeSet(70111, false, loginDTO, databaseConnection);
		return enabledStateActionMap.get(stateID);
	} 
	
	
	public static void main(String args[]) throws Exception
	{
		Set<Integer> set = new HashSet<Integer>();
		//set.add(62301);
		set.add(62401);
//		HashMap<Integer, ArrayList<StateDTO>> abc = new RequestUtilDAO().getStatesRelatedToRootRequestTypeIDs(set);
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUserID(4127);
		loginDTO.setRoleID(8127);
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.dbOpen();
		
		Set<Integer> saIDSet = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(loginDTO.getRoleID());
		
		HashMap<Integer, HashSet<Integer>> enabledStateActionMap = new HashMap<Integer, HashSet<Integer>>();
		if(saIDSet != null)
		{
			for(Integer saID: saIDSet)
			{				
				StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
				if(sadto == null)continue;
				int action = sadto.getActionTypeIDs().iterator().next();
				if(enabledStateActionMap.get(sadto.getStateID()) == null)
				{
					enabledStateActionMap.put(sadto.getStateID(), new HashSet<Integer>());
				}
				enabledStateActionMap.get(sadto.getStateID()).add(action);
			}
		}

//		Set<Integer> set2 = new RequestUtilDAO().getPermittedActionTypeSet(70111, false, loginDTO, databaseConnection);
		Set<Integer> set2 = enabledStateActionMap.get(70111);
		logger.debug("set " + set2);
		databaseConnection.dbClose();
		
	}

	
	public HashMap<Integer, ArrayList<StateDTO>> getStatesRelatedToRootRequestTypeIDs(Set<Integer> reqSet)
	{
		if(reqSet == null) return null;
		Collection<StateDTO> stateCol = StateRepository.getInstance().getAllStateDTOs();
		HashMap<Integer, ArrayList<StateDTO>> actionStateMap = new HashMap<Integer, ArrayList<StateDTO>>();
		
		for(StateDTO sdto: stateCol)
		{						
//			logger.debug("sdto.getRootRequestTypeID() " + sdto.getRootRequestTypeID());
			if(reqSet.contains(sdto.getRootRequestTypeID()))
			{
				ArrayList<StateDTO> stateList = actionStateMap.get(sdto.getRootRequestTypeID());
				if(stateList == null)
				{
					stateList = new ArrayList<StateDTO>();
					actionStateMap.put(sdto.getRootRequestTypeID(), stateList);
				}				
				stateList.add(sdto);
			}			
		}
		
		return actionStateMap;		
	}
	

	public List<CommonRequestDTO> getRequestListByClientOrUserID(long clientOrUserID,DatabaseConnection databaseConnection) throws Exception{
		List<CommonRequestDTO> commonRequestDTOList = new ArrayList<CommonRequestDTO>();
		Class classObject = CommonRequestDTO.class;
		String clientIDColumnName = getColumnName(classObject, "clientID");
		String requestByAccountIDColumnName = getColumnName(classObject, "requestByAccountID");
		String requestToAccountIDColumnName = getColumnName(classObject, "requestToAccountID");
		
		String conditionString = " where "+clientIDColumnName+"="+clientOrUserID+" OR "+requestByAccountIDColumnName+" = "+clientOrUserID+" OR "+requestToAccountIDColumnName+" = "+clientOrUserID;
		return (List<CommonRequestDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
	}
	

	
	public void getRequestsHavingCommonRootRequestID(long rootReqID, ArrayList<Long> historyIDs, DatabaseConnection databaseConnection) throws Exception {
		String sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE  ( arRootRequestID IS null AND arID =  "
				+ rootReqID + " ) or (arRootRequestID = " + rootReqID + " ) and arIsDeleted = 0";
		logger.debug("sql " + sql);
		Statement statement = databaseConnection.getNewStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		String whereClause = "(";
		Set<Long> rootParamSet = new HashSet<Long>();
		while(resultSet.next())
		{			
			long reqID = resultSet.getLong("arID");
			historyIDs.add(reqID);
			whereClause += reqID;
			if(!resultSet.isLast())
			{
				whereClause += ",";
			}
			rootParamSet.add(reqID);
		}
		whereClause += ")";
		if(whereClause.length() > 2)
		{
			String sql1 = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE ( arRootRequestID IS null AND arParentRequestID in " + whereClause + " ) ";
			logger.debug("sql1 " + sql1);
			Statement statement1 = databaseConnection.getNewStatement();
			resultSet = statement1.executeQuery(sql1);
			while(resultSet.next())
			{
				long reqID = resultSet.getLong("arID");
				getRequestsHavingCommonRootRequestID(reqID, historyIDs, databaseConnection);
			}
		}
	}
	
	/*public Set<Long> getMainRoots(CommonRequestDTO comDTO, boolean sameBranch, boolean pendingOnly, DatabaseConnection databaseConnection) throws SQLException {
		String sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req ";//WHERE arEntityTypeID = " + entityTypeID + " AND arEntityID = " + entityID + " AND arRootRequestID is NULL";
		if(comDTO.getEntityTypeID() > 0 && comDTO.getEntityID() > 0)
		{
			 sql += " where arEntityTypeID = " + comDTO.getEntityTypeID() + " and arEntityID = " + comDTO.getEntityID() + " and arRootRequestID is null" ;
		}
		else if(comDTO.getReqID() > 0)
		{
			 sql += " where arID = " + comDTO.getReqID();
		}
		if(pendingOnly)
		{
			sql += " and arCompletionStatus != " + RequestStatus.ALL_PROCESSED;
		}
		//if(comDTO.getClientID() > 0)
		logger.debug("Sql 1 : " + sql);
		Statement statement = databaseConnection.getNewStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		Set<Long> rootSet = new HashSet<Long>();
		while (resultSet.next()) {
			long arID = resultSet.getLong("arID");
			
			long arRootRequestID = resultSet.getLong("arRootRequestID");
			
			if(sameBranch) 
			{
				if(comDTO.getReqID() > 0)
				{
					logger.debug("returning arRootRequestID " + arRootRequestID);
					rootSet.add(arRootRequestID);
					continue;
				}
				else
				{
					logger.debug("returning arID " + arID);
					rootSet.add(arID);
					continue;
				}
				//return arID;
			}
			
			long parentReqID = resultSet.getLong("arParentRequestID");
			if(arRootRequestID == 0 && parentReqID == 0)
			{
				rootSet.add(arID);
				continue;
//				return arID;
			}
			else if(arRootRequestID == 0 && parentReqID != 0)
			{
				arID = getMainRootByBranchRootID(parentReqID, databaseConnection);
				rootSet.add(arID);
				continue;
			}
			else if(arRootRequestID > 0)
			{
				
				 * Will never enter here as sql has " and arRootRequestID is null"
				 * 
				 
				arID = getMainRootByBranchRootID(arRootRequestID, databaseConnection);				
				rootSet.add(arID);
				continue;
			}
			else {
				logger.debug("returning arID " + arID);
				rootSet.add(arID);
				continue;
			}
		}
		logger.debug("returning mainRootSet " + rootSet);
		return rootSet;
//		return -1;
		
	}*/
	
	/*public long getMainRootRequestIDsByBranchRoot(long branchRootReqID, DatabaseConnection databaseConnection) throws Exception
	{
		
		CommonRequestDTO branchRequestDTO = (CommonRequestDTO)SqlGenerator.getObjectByID(CommonRequestDTO.class, branchRootReqID , databaseConnection);
		if(branchRequestDTO.getParentReqID() != null)
		{
			CommonRequestDTO parentRequestDTO = (CommonRequestDTO)SqlGenerator.getObjectByID(CommonRequestDTO.class, branchRequestDTO.getParentReqID() , databaseConnection);
			return (parentRequestDTO.getRootReqID() == null) ? parentRequestDTO.getReqID():parentRequestDTO.getRootReqID();
		}		
		return branchRootReqID;
			
		
	}*/
	
	private long getMainRootByBranchRootID(long reqID, DatabaseConnection databaseConnection) throws SQLException
	{
		String sql = "select arID, arParentRequestID from at_req where arRootRequestID is NULL and arID = " + reqID;
		logger.debug("sql " + sql);
		Statement statement = databaseConnection.getNewStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		if (resultSet.next()) {
			long mainRoot = resultSet.getLong("arID");						
			long parentReqID = resultSet.getLong("arParentRequestID");
			if(parentReqID != 0)
			{
				return getMainRootByBranchRootID(parentReqID, databaseConnection);
			}
			else{
				return mainRoot;
			}
		}
		
		return -1;
		
	}
	
	private Object getBranchRootDTOsByMainRootID(long reqID, DatabaseConnection databaseConnection) throws Exception
	{
		return  SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, " where arParentRequestID = " + reqID);
		
	}	
	
	/*public long getMainRootRequestIDsByBranchRoot(long branchRootReqID) throws Exception
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		long mainRoot = -1;
		try
		{
			databaseConnection.dbOpen();
			mainRoot = getMainRootRequestIDsByBranchRoot(branchRootReqID, databaseConnection);
		}
		catch(Exception ex)
		{
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.debug(ex);
			}
			throw ex; 
		}
		finally
		{
			databaseConnection.dbClose();
		}		
		return mainRoot;
	}*/
	
	/*
	 * Compulsory parameter: entityID, entityTypeID, rootRequestID
	 */
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection ) throws Exception {
		
		return getBottomRequestDTOsByEntity( commonRequestDTO, databaseConnection, true );
	}
	
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection, boolean onlyPending ) throws Exception {
		return getBottomRequestDTOsByEntity(commonRequestDTO, databaseConnection, onlyPending, false, true);
	}
	
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection, boolean onlyPending, boolean includeDeleted, boolean considerChildEntity ) throws Exception {
		// TODO Auto-generated method stub	
		
		String conditionString = " where arEntityTypeID = " + commonRequestDTO.getEntityTypeID() + " and arEntityID = " + commonRequestDTO.getEntityID();
				
		if(commonRequestDTO.getRootReqID() != null)
		{
			conditionString += " and arRootRequestID = " + commonRequestDTO.getRootReqID();
		}
		
		if( onlyPending )
		{
			conditionString += " and arCompletionStatus = "+ RequestStatus.PENDING;			
		}

		if(!includeDeleted)
		{
			conditionString += " and arIsDeleted <> 1 ";
		}
//		int moduleID = commonRequestDTO.getEntityTypeID() % EntityTypeConstant.MULTIPLIER2;		
		conditionString += " order by arReqTime desc limit 1 ";
		
		List<CommonRequestDTO> dtoList = (List<CommonRequestDTO>)SqlGenerator.getAllObjectList(CommonRequestDTO.class, databaseConnection, conditionString);
		if(dtoList.size() > 1)
		{
			throw new Exception("Multiple bottom request found");
		}
		else if(dtoList.size() == 0)
		{
			dtoList = new ArrayList<CommonRequestDTO>();
		}


//		if(commonRequestDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK && considerChildEntity)
//		{
//			VpnDAO vpnDAO = new VpnDAO();
//			VpnLinkDTO vpnLinkDTO = vpnDAO.getVpnLinkDTOByID(commonRequestDTO.getEntityID(), databaseConnection);
//
////			if(!vpnLinkDTO.isNearEndReused())
//			{
//				CommonRequestDTO commonRequestDTO2 = new CommonRequestDTO();
//				commonRequestDTO2.setEntityID(vpnLinkDTO.getNearEndID());
//				commonRequestDTO2.setEntityTypeID(EntityTypeConstant.VPN_LINK_NEAR_END);
//				Set<CommonRequestDTO> nearEndList = (Set<CommonRequestDTO>)getBottomRequestDTOsByEntity(commonRequestDTO2, databaseConnection, onlyPending, includeDeleted, considerChildEntity );
//				dtoList.addAll(nearEndList);
//			}
//			CommonRequestDTO commonRequestDTO3 = new CommonRequestDTO();
//			commonRequestDTO3.setEntityID(vpnLinkDTO.getFarEndID());
//			commonRequestDTO3.setEntityTypeID(EntityTypeConstant.VPN_LINK_FAR_END);
//			Set<CommonRequestDTO> farEndList = (Set<CommonRequestDTO>)getBottomRequestDTOsByEntity(commonRequestDTO3, databaseConnection, onlyPending, includeDeleted, considerChildEntity );
//
//
//			dtoList.addAll(farEndList);
//
//		}
//		else if(commonRequestDTO.getEntityTypeID() == EntityTypeConstant.LLI_LINK && considerChildEntity)
//		{
//			LliDAO lliDAO = new LliDAO();
//			LliLinkDTO lliLinkDTO = lliDAO.getLliLinkDTOByID(commonRequestDTO.getEntityID(), databaseConnection);
//
//			CommonRequestDTO commonRequestDTO3 = new CommonRequestDTO();
//			commonRequestDTO3.setEntityID(lliLinkDTO.getFarEndID());
//			commonRequestDTO3.setEntityTypeID(EntityTypeConstant.LLI_LINK_FAR_END);
//			Set<CommonRequestDTO> farEndList = (Set<CommonRequestDTO>)getBottomRequestDTOsByEntity(commonRequestDTO3, databaseConnection, onlyPending, includeDeleted, considerChildEntity );
//
//
//			dtoList.addAll(farEndList);
//
//		}

		Set<CommonRequestDTO> dtoSet = new HashSet<CommonRequestDTO>();
		dtoSet.addAll(dtoList);
		
		return dtoSet;
	}	
	
	public CommonRequestDTO getRequestDTOByReqID(long requestID, DatabaseConnection databaseConnection) throws Exception
	{
		return (CommonRequestDTO) SqlGenerator.getObjectByID(CommonRequestDTO.class, requestID, databaseConnection);
	}
	
	public <T> T getExtendedRequestByRootReqID( Class<T> classObject, long rootRequestID , DatabaseConnection databaseConnection ) throws Exception{
		String conditionString = " where " + SqlGenerator.getForeignKeyColumnName( classObject ) + " = " + rootRequestID;
		
		List<T> Requests = (List<T>) SqlGenerator.getAllObjectListFullyPopulated( classObject, databaseConnection, conditionString );
		
		T extendedRequest = null;
		
		if( !Requests.isEmpty() )
			extendedRequest = Requests.get(0);
		
		return extendedRequest;
	} 
	
	public <T> T getExtendedRequestByEntityAndRequestTypeID( Class<T> classObject, long entityID, int entityTypeID , int requestTypeID, DatabaseConnection databaseConnection ) throws Exception{
		
		String conditionString = " where arEntityID = " + entityID
				+ " and arEntityTypeID = " + entityTypeID
				+ " and arRequestTypeID = " + requestTypeID
				+ " order by arID desc";
		List<CommonRequestDTO> mostRecentRequestList = SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, conditionString);
		if(mostRecentRequestList == null || mostRecentRequestList.size() == 0)
			return null;
		else
			return getExtendedRequestByRootReqID(classObject, mostRecentRequestList.get(0).getReqID(), databaseConnection);

	}

	/**
	 * @author dhrubo
	 */
	public void completeRequestByEntityIDandEntityTypeID(long entityID, int entityTypeID, boolean isNested, DatabaseConnection databaseConnection) throws Exception {
		if(isNested) {
			HierarchicalEntityService hierarchicalEntityService = ServiceDAOFactory.getService(HierarchicalEntityService.class);
			List<HashMap<String, String>> childEntityListByParentEntityIDAndEntityTypeID = hierarchicalEntityService.getChildEntityListByParentEntityIDAndEntityTypeID(entityID, entityTypeID);
			
			String sql = "update at_req set arCompletionStatus =  " + RequestStatus.ALL_PROCESSED + " where (";
			
			for(int i=0; i<childEntityListByParentEntityIDAndEntityTypeID.size(); i++) {
				HashMap<String, String> entity = childEntityListByParentEntityIDAndEntityTypeID.get(i);
				
				sql += " ( " + SqlGenerator.getColumnName(CommonRequestDTO.class, "entityID") + " = " + entity.get("entityID") + " and "
						+ SqlGenerator.getColumnName(CommonRequestDTO.class, "entityTypeID") + " = " + entity.get("entityTypeID") + " ) ";
				sql += (i == childEntityListByParentEntityIDAndEntityTypeID.size() - 1) ? "" : " or ";
			}
			
			sql += " ) and " + SqlGenerator.getColumnName(CommonRequestDTO.class, "isDeleted") + " = 0 ";

			logger.info("sql " + sql);
			Statement stmt = databaseConnection.getNewStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} else {
			completeRequestByEntity(entityID, entityTypeID, databaseConnection);
		}
	}


}
