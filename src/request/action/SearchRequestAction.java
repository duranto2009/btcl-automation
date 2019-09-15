package request.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.PermissionHandler;
import login.LoginDTO;
import request.RequestSearchService;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;

public class SearchRequestAction extends Action{
	Logger logger = Logger.getLogger(getClass());
    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws NumberFormatException, IOException{	
    	
    	PermissionHandler.handleClientPermissionByModuleID(p_request, p_response, Integer.parseInt(p_request.getParameter("moduleID")));
       
        String target = "success";
        Integer moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
        
        LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
        RequestSearchService service = new RequestSearchService();
        service.setModuleID(moduleID);
        SessionManager sessionManager = new SessionManager();
        if (!sessionManager.isLoggedIn(p_request)) {
            return p_mapping.findForward("login");
        }
     
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_REQUEST, p_request, service, SessionConstants.VIEW_REQUEST, SessionConstants.SEARCH_REQUEST);
        try {
            rnManager.doJob(loginDTO);
        } catch (Exception e) {
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.toString());
        }
        return p_mapping.findForward(target);
    }
}
