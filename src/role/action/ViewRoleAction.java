package role.action;

import javax.servlet.http.*;
import login.LoginDTO;
import login.PermissionConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import common.CommonActionStatusDTO;
import role.RoleService;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;

public class ViewRoleAction extends Action
{
	public static Logger logger = Logger.getLogger(ViewRoleAction.class); 
    public ViewRoleAction()
    {
    }

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)
    {
    	//System.out.println("why this kolaveri");
        String target = "success";
        SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request))
        {
        	return p_mapping.findForward("login");
        }
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        
        boolean hasPermission = false;
		
		if(loginDTO.getUserID()>0)
		{
		    if((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) !=-1)
		    		
		      &&(loginDTO.getMenuPermission(PermissionConstants.ROLE_SEARCH) >= PermissionConstants.PERMISSION_READ))
		    	
		        hasPermission=true;
		    
		}

		if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
		
        RoleService service = util.ServiceDAOFactory.getService(RoleService.class);
        //System.out.println("params:"+ SessionConstants.NAV_ROLE + " " +SessionConstants.VIEW_ROLE +" " +  SessionConstants.SEARCH_ROLE);
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_ROLE, p_request, service, SessionConstants.VIEW_ROLE, SessionConstants.SEARCH_ROLE);
        try
        {
            rnManager.doJob(loginDTO);
        }
        catch(Exception e)
        {
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.getMessage());
            logger.debug("Exception", e);
        }
        return p_mapping.findForward(target);
    }
}