package role.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

import common.CommonActionStatusDTO;
import common.RequestFailureException;
import login.LoginDTO;
import login.PermissionConstants;
import permission.PermissionRepository;
import role.RoleDTO;
import role.RoleService;
import role.form.RoleForm;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.ServiceDAOFactory;

public class GetRoleAction extends Action{
	RoleService roleService = ServiceDAOFactory.getService(RoleService.class);
    

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response){
        String target = "success";
        RoleForm form = new RoleForm();
        RoleService service = util.ServiceDAOFactory.getService(RoleService.class);
        SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request)) {
            return p_mapping.findForward("login");
        }
        boolean hasPermission = false;
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		if(loginDTO.getUserID()>0){
		    if((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) !=-1)
		      &&(loginDTO.getMenuPermission(PermissionConstants.ROLE_ADD) >= PermissionConstants.PERMISSION_MODIFY)){
		        hasPermission=true;
		    }
		}
		if( !hasPermission ){
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
        long id = Long.parseLong(p_request.getParameter("id"));
        RoleDTO roleDTO = PermissionRepository.getInstance().getRoleDTOByRoleID(id);
        if(!isRoleAccessible(loginDTO, roleDTO)) {
        	throw new RequestFailureException("Requested Role can not be edited as requested role is not under your role");
        }
        CommonActionStatusDTO comStatusDTO= new CommonActionStatusDTO();
        try{
        	/*if(id == -1) {
        		 p_request.setAttribute(p_mapping.getAttribute(), form);
                 p_request.setAttribute("id", id);
        		return p_mapping.findForward(target);
        	}*/
            RoleDTO dto = service.getRole(id);
            if(dto == null) {
            	throw new RequestFailureException("Invalid Role");
            }
            form.setRoleID(dto.getRoleID());
            form.setRolename(dto.getRoleName());
            form.setRoleDesc(dto.getRoleDescription());
            form.setMaxClientPerDay(dto.getMaxClientPerDay());
            form.setRestrictedtoOwn(dto.getRestrictedtoOwn());
            form.setParentRoleID(dto.getParentRoleID());
            p_request.setAttribute(p_mapping.getAttribute(), form);
            p_request.setAttribute("id", id);
        }
        catch(Exception e){
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.toString());
            comStatusDTO.setErrorMessage(e.getMessage(), false, p_request);
        }
        return p_mapping.findForward(target);
    }

	private boolean isRoleAccessible(LoginDTO loginDTO, RoleDTO accessedRoleDTO) {
		boolean result = true;
		RoleDTO parentRoleDTO = PermissionRepository.getInstance().getRoleDTOByRoleID(loginDTO.getRoleID());
		if(parentRoleDTO != null) {
			if(roleService.isRoleAncestorOfRequestedRole(parentRoleDTO, accessedRoleDTO)) {
				return true;
			}
		}
		
		return result;
	}

}