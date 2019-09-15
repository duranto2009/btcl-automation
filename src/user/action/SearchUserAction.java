package user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.LoginDTO;
import login.PermissionConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CommonActionStatusDTO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import user.UserService;
import util.RecordNavigationManager;

public class SearchUserAction extends Action
{


    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)
    {
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
		    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
		    		
		      &&(loginDTO.getMenuPermission(PermissionConstants.USER_SEARCH) >= PermissionConstants.PERMISSION_READ))
		    	
		        hasPermission=true;
		    
		}

		if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
        
        UserService service = new UserService();
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_USER, p_request, service, SessionConstants.VIEW_USER, SessionConstants.SEARCH_USER);
        try
        {
            rnManager.doJob(loginDTO);
        }
        catch(Exception e)
        {
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.toString());
        }
        return p_mapping.findForward(target);
    }
}