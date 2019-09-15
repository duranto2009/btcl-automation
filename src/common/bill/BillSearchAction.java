package common.bill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.PermissionHandler;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

public class BillSearchAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		
		PermissionHandler.handleClientPermissionByModuleID(p_request, p_response, Integer.parseInt(p_request.getParameter("moduleID")));
		
		String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));

		BillSearchService billSearchService = new BillSearchService();
		billSearchService.setModuleID(moduleID);
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_BILL, p_request,
				billSearchService, SessionConstants.VIEW_BILL, SessionConstants.SEARCH_BILL);

		rnManager.doJob(loginDTO);

		return (p_mapping.findForward(target));

	}

}
