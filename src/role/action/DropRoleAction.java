package role.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import login.PermissionConstants;
import role.RoleService;
import role.form.RoleForm;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.DAOResult;

public class DropRoleAction extends Action
{

    public DropRoleAction()
    {
    }

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception
    {
        String target = "success";
        RoleForm form = (RoleForm)p_form;
        RoleService service = util.ServiceDAOFactory.getService(RoleService.class);
        SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request))
            return p_mapping.findForward("login");
        
        boolean hasPermission = false;
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        
		if(loginDTO.getUserID()>0)
		{
		    if((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) !=-1)
		    		
		      &&(loginDTO.getMenuPermission(PermissionConstants.ROLE_ADD) >= PermissionConstants.PERMISSION_FULL)){
		    	
		        hasPermission=true;
		    }
		}

		if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
		
        DAOResult daoResult = service.dropRole(form.getDeleteIDs());
        CommonActionStatusDTO comStatusDTO= new CommonActionStatusDTO();
        
        if(!daoResult.isSuccessful())
        {
            target = "failure";
            SessionManager.setFailureMessage(p_request, daoResult.getMessage());
            comStatusDTO.setErrorMessage(daoResult.getMessage(), false, p_request);
            
        }
        String successMsg= "Role  deleted successfully.";
    	comStatusDTO.setSuccessMessage(successMsg, false, p_request);
    	
        return p_mapping.findForward(target);
    }
}