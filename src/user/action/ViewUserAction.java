package user.action;

import javax.servlet.http.*;
import login.LoginDTO;
import org.apache.struts.action.*;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import user.UserService;
import util.RecordNavigationManager;

public class ViewUserAction extends Action
{

    public ViewUserAction()
    {
    }

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)
    {
        String target = "success";
        SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request))
        {
        	return p_mapping.findForward("login");
        }
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
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