package common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import common.CommonRequestSearchService;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;

public class CommonRequestSearchAction  extends Action{
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) {

		String target = "success";

		CommonRequestSearchService service = new CommonRequestSearchService(Integer.parseInt(p_request.getParameter("entityTypeID")), Long.parseLong(p_request.getParameter("entityID")));
		SessionManager sessionManager = new SessionManager();
		if (!sessionManager.isLoggedIn(p_request)) {
			return p_mapping.findForward("login");
		}
		LoginDTO loginDTO = (LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_COMMON_REQUEST, p_request,
				service, SessionConstants.VIEW_COMMON_REQUEST, SessionConstants.SEARCH_COMMON_REQUEST);
		try {
			rnManager.doJob(loginDTO);
		} catch (Exception e) {
			target = "failure";
			SessionManager.setFailureMessage(p_request, e.toString());
		}
		return p_mapping.findForward(target);
	}
}
