/*
 * Created on Feb 19, 2004
 *
 */
package sessionmanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import login.*;

/**
 * @author shuvohaq
 */
public class SessionManager {

	public static void setFailureMessage(HttpServletRequest p_request,String p_message)
	{
		HttpSession session = p_request.getSession(true);
		session.setAttribute(SessionConstants.FAILURE_MESSAGE,p_message);
	}

	public boolean isLoggedIn(HttpServletRequest p_request)
	{
		HttpSession session = p_request.getSession(true);
                return session.getAttribute(SessionConstants.USER_LOGIN) != null;
	}

	public void saveUser(HttpServletRequest p_request, LoginDTO dto)
	{
		HttpSession session = p_request.getSession(true);
		session.setAttribute(SessionConstants.USER_LOGIN,dto);
	}

	public void resetUser(HttpServletRequest p_request)
	{
		HttpSession session = p_request.getSession(true);
		session.setAttribute(SessionConstants.USER_LOGIN,null);
	}

}
