package logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import sessionmanager.SessionManager;

public class LogoutAction extends Action
{

    public LogoutAction()
    {
    }

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)
    {
    	String target = "login";
        SessionManager sessionManager = new SessionManager();
        sessionManager.resetUser(p_request);
        p_request.getSession().invalidate();
        return p_mapping.findForward(target);
    }
}