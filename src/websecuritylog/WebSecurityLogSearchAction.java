package websecuritylog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

public class WebSecurityLogSearchAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if(!loginDTO.getIsAdmin())
		{
			new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}

		
		WebSecurityLogService webSecurityLogService = new WebSecurityLogService();
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_WEB_SECURITY_LOG,
				p_request, webSecurityLogService, SessionConstants.VIEW_WEB_SECURITY_LOG,
				SessionConstants.SEARCH_WEB_SECURITY_LOG);

		rnManager.doJob(loginDTO);

		return (p_mapping.findForward(target));

	}
}
