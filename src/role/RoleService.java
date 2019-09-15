package role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.Transactional;
import common.RequestFailureException;
import common.StringUtils;
import login.LoginDTO;
import permission.ActionPermissionDTO;
import permission.ColumnPermissionDTO;
import permission.PermissionDTO;
import permission.PermissionRepository;
import util.DAOResult;
import util.NavigationService;


public class RoleService  implements NavigationService {
	
	RoleDAO roleDAO = new RoleDAO();

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<RoleDTO> getDTOs(Collection recordIDs, Object... objects) throws Exception{
        return roleDAO.getRolesByRoleIDs((List<Long>)recordIDs);
    }
    @Transactional(transactionType=util.TransactionType.READONLY)
    public Collection<Long> getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType=util.TransactionType.READONLY)
	public Collection<Long> getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return roleDAO.getRoleIDsWithSearchCriteria(searchCriteria, loginDTO);
    }
    public List<RoleDTO> getDescendentRolesByRoleID(long roleID) throws Exception {
    	return PermissionRepository.getInstance().getDescendantRoleDTOByRoleID(roleID);
    }
    
    public List<RoleDTO> getDescendentRolesByPartialMatch(LoginDTO loginDTO, String partialMatch) throws Exception {
    	long roleID = loginDTO.getRoleID();
    	List<RoleDTO> descendentRoles = getDescendentRolesByRoleID(roleID);
    	List<RoleDTO> resultantRoles = new ArrayList<>();
    	
    	for(RoleDTO roleDTO : descendentRoles) {
			if(roleDTO.getRoleName().toLowerCase().contains(StringUtils.trim(partialMatch).toLowerCase())) {
				resultantRoles.add(roleDTO);
			}
    	}
    	return resultantRoles;
    }
    private void validateRole(RoleDTO roleDTO) throws Exception{

    	// check for duplicate name
    	
    	if(StringUtils.isBlank(roleDTO.getRoleName())){
    		throw new RequestFailureException("Role name can not be empty");
    	}
    	if(roleDTO.getRoleName().length() > 50 ) {
    		throw new RequestFailureException("Role name is too long");
    	}
    	if(StringUtils.isBlank(roleDTO.getRoleDescription())){
    		throw new RequestFailureException("Role description can not be empty");
    	}
    	if(roleDTO.getRoleDescription().length() > 200) {
    		throw new RequestFailureException("Role description is too long");
    	}
    }
    
    @Transactional
    public void addRole(RoleDTO roleDTO) throws Exception{
    	
    	validateRole(roleDTO);
    	
        roleDAO.addRole(roleDTO);
        
        for(PermissionDTO permissionDTO: roleDTO.getPermissionList()){
        	permissionDTO.setRoleID(roleDTO.getRoleID());
        	roleDAO.addMenuPermissionDTO(permissionDTO);
        }
        
        for(ColumnPermissionDTO columnPermissionDTO: roleDTO.getColumnPermissionList()){
        	columnPermissionDTO.setRoleID(roleDTO.getRoleID());
        	roleDAO.addColumnPermissionDTO(columnPermissionDTO);
        }
        
        for(ActionPermissionDTO actionPermissionDTO: roleDTO.getActionPermissionList()){
        	actionPermissionDTO.setRoleID(roleDTO.getRoleID());
        	roleDAO.addActionPermissionDTO(actionPermissionDTO);
        }
    }

    public DAOResult dropRole(long roleIDs[]) throws Exception {
        return roleDAO.dropRoles(roleIDs);
    }
    public RoleDTO getRole(long roleID) throws Exception {
        return roleDAO.getRoleDTOByRoleID(roleID);
    }

    @Transactional
    public void updateRole(RoleDTO roleDTO) throws Exception{
    	
    	validateRole(roleDTO);
    	
    	roleDAO.deleteAllMenuPermissionByRoleID(roleDTO.getRoleID());
    	roleDAO.deleteAllColumnPermissionByRoleID(roleDTO.getRoleID());
    	roleDAO.deleteAllActionPermissionByRoleID(roleDTO.getRoleID());
        roleDAO.updateRole(roleDTO);


        for(PermissionDTO permissionDTO: roleDTO.getPermissionList()){
        	permissionDTO.setRoleID(roleDTO.getRoleID());
        }
		roleDAO.addMenuPermissionDTOs(roleDTO.getPermissionList());
        
        for(ColumnPermissionDTO columnPermissionDTO: roleDTO.getColumnPermissionList()){
        	columnPermissionDTO.setRoleID(roleDTO.getRoleID());

        }
		roleDAO.addColumnPermissionDTOs(roleDTO.getColumnPermissionList());
        
        for(ActionPermissionDTO actionPermissionDTO: roleDTO.getActionPermissionList()){
        	actionPermissionDTO.setRoleID(roleDTO.getRoleID());
        }
		roleDAO.addActionPermissionDTOs(roleDTO.getActionPermissionList());
        
    }

    
    public ArrayList<RoleDTO> getPermittedRoleListForSpAdmin() {
        return roleDAO.getPermittedRoleListForSpAdmin();
    }
	public boolean isRoleAncestorOfRequestedRole(RoleDTO parentRoleDTO, RoleDTO childRoleDTO) {
		if(childRoleDTO == null || childRoleDTO.getParentRoleID() == null) return false;
		else if(childRoleDTO.getParentRoleID().equals(parentRoleDTO.getRoleID())) return true; 
		return isRoleAncestorOfRequestedRole(parentRoleDTO, PermissionRepository.getInstance().getRoleDTOByRoleID(childRoleDTO.getParentRoleID()));
	}
	
	public List<RoleDTO> getRolesByRoleName(String roleName){
		return roleDAO.getRolesByRoleName(roleName);
	}

}