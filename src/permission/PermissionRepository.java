package permission;

import static util.SqlGenerator.getAllObjectForRepository;
import static util.SqlGenerator.getAllObjectList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;

import connection.DatabaseConnection;
import repository.Repository;
import repository.RepositoryManager;
import request.RequestStateActionRepository;
import role.RoleDTO;
import role.RoleService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

public class PermissionRepository implements Repository{
	RoleService roleService = new RoleService();
	
	public static Logger logger = Logger.getLogger(PermissionRepository.class);
	Map<Long, RoleDTO> mapOfRoleDTOToRoleID;
	Map<Long, Set<RoleDTO>>mapOfSetOfRoleDTOsToRoleParentID;
	Map<Long, Map<Integer, PermissionDTO>> mapOfPermissionDTOToMenuIDToRoleID;
	Map<Integer, MenuDTO> mapOfMenuDTOToMenuID;
	Map<Long, Map<Integer, ColumnPermissionDTO>> mapOfColumnPermissionDTOToColumnIDToRoleID;
	Map<Integer, ColumnDTO> mapOfColumnDTOToColumnID;
	Map<Long, Map<Integer, StateActionDTO>> mapOfStateActionDTOToStateActionTypeIDToRoleID;
	static PermissionRepository instance = null;
	
	private PermissionRepository(){
		mapOfColumnDTOToColumnID = new ConcurrentHashMap<>();
		mapOfColumnPermissionDTOToColumnIDToRoleID = new ConcurrentHashMap<>();
		mapOfMenuDTOToMenuID = new ConcurrentHashMap<>();
		mapOfPermissionDTOToMenuIDToRoleID = new ConcurrentHashMap<>();
		mapOfRoleDTOToRoleID = new ConcurrentHashMap<>();
		mapOfStateActionDTOToStateActionTypeIDToRoleID = new ConcurrentHashMap<>();
		mapOfSetOfRoleDTOsToRoleParentID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}
	public synchronized static PermissionRepository getInstance(){
		if(instance == null){
			instance = new PermissionRepository();
		}
		return instance;
	}
	public void reload(boolean isFirstReload){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			if(isFirstReload) {
				populateMenuMap(databaseConnection);
				populateColumnMap(databaseConnection);
			}
			//RoleID -> RoleDTO
			populateRoleDTOMap(isFirstReload,databaseConnection);
			populatePermissionMap(isFirstReload,databaseConnection);
			populateColumnPermissionMap(isFirstReload,databaseConnection);	
			populateStateActionMap(isFirstReload,databaseConnection);
			
		}catch(Exception e ) {
			logger.debug("FATAL", e);
		}finally{
			databaseConnection.dbClose();
		}
	}
	
	private void populateMenuMap(DatabaseConnection databaseConnection) throws Exception {
		//menuID->munuDTO
		List<MenuDTO> menuDTOs = getAllObjectList(MenuDTO.class,databaseConnection);
		for(MenuDTO menuDTO : menuDTOs) {
			mapOfMenuDTOToMenuID.put(menuDTO.getMenuID(), menuDTO);
		}
	}
	private void populateColumnMap(DatabaseConnection databaseConnection) throws Exception {
		//columnID->columnDTO
		List<ColumnDTO> columnDTOs = getAllObjectList(ColumnDTO.class,databaseConnection);
		for(ColumnDTO columnDTO: columnDTOs) {
			mapOfColumnDTOToColumnID.put(columnDTO.getColumnID(), columnDTO);
		}
	}
	private void populateRoleDTOMap(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception {
		List<RoleDTO> roleDTOs = getAllObjectForRepository(RoleDTO.class, databaseConnection,isFirstReload);
		for(RoleDTO roleDTO: roleDTOs) {
			RoleDTO oldRoleDTO = getRoleDTOByRoleID(roleDTO.getRoleID());
			if( oldRoleDTO != null ) {
				if(oldRoleDTO.getLastModificationTime() == roleDTO.getLastModificationTime()) {
					continue;
				}
				mapOfRoleDTOToRoleID.remove(roleDTO.getRoleID());
				if(oldRoleDTO.hasParent()) {
					mapOfSetOfRoleDTOsToRoleParentID.get(oldRoleDTO.getParentRoleID()).remove(oldRoleDTO);
					if(mapOfSetOfRoleDTOsToRoleParentID.get(oldRoleDTO.getParentRoleID()).isEmpty()) {
						mapOfSetOfRoleDTOsToRoleParentID.remove(oldRoleDTO.getParentRoleID());
					}
				}
				
			}
			if(roleDTO.isDeleted() == false) {
				mapOfRoleDTOToRoleID.put(roleDTO.getRoleID(), roleDTO);
				if(roleDTO.hasParent()) {
					if(!mapOfSetOfRoleDTOsToRoleParentID.containsKey(roleDTO.getParentRoleID())) {
						mapOfSetOfRoleDTOsToRoleParentID.put(roleDTO.getParentRoleID(), new ConcurrentHashSet<>());
					}
					mapOfSetOfRoleDTOsToRoleParentID.get(roleDTO.getParentRoleID()).add(roleDTO);
				}
				
			}
		}
	}
	
	
	public List<RoleDTO> getDescendantRoleDTOByRoleID(long roleID){
		List<RoleDTO> descendantRoles = new ArrayList<>();

		if(mapOfSetOfRoleDTOsToRoleParentID.containsKey(roleID)) {
			for(RoleDTO childRole: mapOfSetOfRoleDTOsToRoleParentID.get(roleID)) {
				descendantRoles.add(childRole);
				descendantRoles.addAll(getDescendantRoleDTOByRoleID(childRole.getRoleID()));
			}
		}
		return descendantRoles;
	}
	
	private void populatePermissionMap(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception {
		// roleID -> Map(menuID->permissionDTO)
		List<PermissionDTO> permissionDTOList = getAllObjectForRepository(PermissionDTO.class,databaseConnection, isFirstReload);
		for(PermissionDTO permissionDTO: permissionDTOList){
			if(mapOfPermissionDTOToMenuIDToRoleID.containsKey(permissionDTO.getRoleID()) &&
					mapOfPermissionDTOToMenuIDToRoleID.get(permissionDTO.getRoleID()).containsKey(permissionDTO.getMenuID())){
				
				if(mapOfPermissionDTOToMenuIDToRoleID.get(permissionDTO.getRoleID()).get(permissionDTO.getMenuID())
						.getLastModificationTime() == permissionDTO.getLastModificationTime()){
					continue;
				}
				
				mapOfPermissionDTOToMenuIDToRoleID.get(permissionDTO.getRoleID()).remove(permissionDTO.getMenuID());
				if(mapOfPermissionDTOToMenuIDToRoleID.get(permissionDTO.getRoleID()).isEmpty()){
					mapOfPermissionDTOToMenuIDToRoleID.remove(permissionDTO.getRoleID());
				}
			}
			if(permissionDTO.isDeleted() == false){
				if(!mapOfPermissionDTOToMenuIDToRoleID.containsKey(permissionDTO.getRoleID())){
					mapOfPermissionDTOToMenuIDToRoleID.put(permissionDTO.getRoleID(), new ConcurrentHashMap<>());
				}
				mapOfPermissionDTOToMenuIDToRoleID.get(permissionDTO.getRoleID())
				.put(permissionDTO.getMenuID(), permissionDTO);
			}
		}
	}
	private void populateColumnPermissionMap(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception {
		//roleID->Map(columnID->columnPermissionDTO)
		List<ColumnPermissionDTO> columnPermissionDTOs = getAllObjectForRepository(ColumnPermissionDTO.class,databaseConnection, isFirstReload);
		for(ColumnPermissionDTO columnPermissionDTO: columnPermissionDTOs){
			if(mapOfColumnPermissionDTOToColumnIDToRoleID.containsKey(columnPermissionDTO.getRoleID()) &&
					mapOfColumnPermissionDTOToColumnIDToRoleID.get(columnPermissionDTO.getRoleID()).containsKey(columnPermissionDTO.getColumnID())){
				
				
				if(mapOfColumnPermissionDTOToColumnIDToRoleID.get(columnPermissionDTO.getRoleID())
						.get(columnPermissionDTO.getColumnID()).getLastModificationTime() == columnPermissionDTO.getLastModificationTime()){
					continue;
				}
				
				mapOfColumnPermissionDTOToColumnIDToRoleID.get(columnPermissionDTO.getRoleID()).remove(columnPermissionDTO.getColumnID());
				if(mapOfColumnPermissionDTOToColumnIDToRoleID.get(columnPermissionDTO.getRoleID()).isEmpty()){
					mapOfColumnPermissionDTOToColumnIDToRoleID.remove(columnPermissionDTO.getRoleID());
				}
			}
			if(columnPermissionDTO.isDeleted() == false){
				if(!mapOfColumnPermissionDTOToColumnIDToRoleID.containsKey(columnPermissionDTO.getRoleID())){
					mapOfColumnPermissionDTOToColumnIDToRoleID.put(columnPermissionDTO.getRoleID(), new ConcurrentHashMap<>());
				}
				mapOfColumnPermissionDTOToColumnIDToRoleID.get(columnPermissionDTO.getRoleID())
				.put(columnPermissionDTO.getColumnID(), columnPermissionDTO);
			}
		}
	}
	private void populateStateActionMap(boolean isFirstReload,DatabaseConnection databaseConnection) throws Exception {
		//roleID -> map(stateActionTypeID -> stateActionDTO)
		
		
		List<ActionPermissionDTO> actionPermissionDTOs = getAllObjectForRepository(ActionPermissionDTO.class, databaseConnection, isFirstReload);
		for(ActionPermissionDTO actionPermissionDTO : actionPermissionDTOs) {
			int stateActionTypeID = actionPermissionDTO.getStateActionTypeID();
			long roleID = actionPermissionDTO.getRoleID();
			boolean isDeleted = actionPermissionDTO.isDeleted();
			if(mapOfStateActionDTOToStateActionTypeIDToRoleID.containsKey(roleID) && 
				mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID).containsKey(stateActionTypeID) ){		
				mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID).remove(stateActionTypeID);
				if(mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID).isEmpty()) {
					mapOfStateActionDTOToStateActionTypeIDToRoleID.remove(roleID);
				}
			}
			if(isDeleted == false) {
				if(!mapOfStateActionDTOToStateActionTypeIDToRoleID.containsKey(roleID)) {
					mapOfStateActionDTOToStateActionTypeIDToRoleID.put(roleID, new ConcurrentHashMap<>());
				}
				StateActionDTO stateActionDTO = RequestStateActionRepository.getInstance().getStateActionBySaID(stateActionTypeID);
				if(stateActionDTO != null){
					mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID).put(stateActionTypeID, stateActionDTO);					
				}else{
					logger.debug("Null found for stateActionTypeID " + stateActionTypeID);
				}
			}
		}
	}
	
	public  Collection<RoleDTO> getAllRoleDTOs(){		
		return mapOfRoleDTOToRoleID.values();
	}	
	public  RoleDTO getRoleDTOByRoleID(long roleID){
		return mapOfRoleDTOToRoleID.get(roleID);
	}
	public  Map<Integer, PermissionDTO> getMenuPermissionMapByRoleID(long roleID){
		return mapOfPermissionDTOToMenuIDToRoleID.get(roleID);
	}
	public Map<Integer, ColumnPermissionDTO> getColumnPermissionMapByRoleID(long roleID){
		if(mapOfColumnPermissionDTOToColumnIDToRoleID.get(roleID) == null){
			return Collections.emptyMap();
		}
		return mapOfColumnPermissionDTOToColumnIDToRoleID.get(roleID);
	}
	public Set<Integer> getStateActionTypeSetByRoleID(long roleID){		
		if(mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID) == null){
			return Collections.emptySet();
		}
		return mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID).keySet();
	}
	public Map<Integer, StateActionDTO> getStateActionTypeMapByRoleID(long roleID){		
		return mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID);
	}
	public Set<Integer> getActionTypeSetByRoleIDAndStateID(long roleID, int stateID){		
		Map<Integer, StateActionDTO> stateActionMap = mapOfStateActionDTOToStateActionTypeIDToRoleID.get(roleID);
		
		Set<Integer> actionSet = new HashSet<>();
		if(stateActionMap == null)
		{
			return actionSet;
		}
		for(StateActionDTO stateActionDTO: stateActionMap.values()){
			if(stateActionDTO.getStateID() == stateID){
				actionSet.addAll(stateActionDTO.getActionTypeIDs());
			}
		}
		return actionSet;
	}
	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = ModifiedSqlGenerator.getTableName(RoleDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;
	}
	public ColumnDTO getColumnDTOByColumnID(int columnID) {
		return mapOfColumnDTOToColumnID.get(columnID);
	}
	
	public MenuDTO getMenuDTOByMenuID(int menuID){
		return mapOfMenuDTOToMenuID.get(menuID);
	}
}
