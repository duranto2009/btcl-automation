package websecuritylog;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

public class WebSecurityLogAction extends AnnotatedRequestMappingAction {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {

		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if (!loginDTO.getIsAdmin()) {
			new CommonActionStatusDTO().setErrorMessage("You don't have permission to see this page", false, p_request);
			return p_mapping.findForward("applicationRoot");
		}

		WebSecurityLogService webSecurityLogService = new WebSecurityLogService();
		try {

			int attemptCategory = Integer.parseInt(p_request.getParameter("attemptCategory"));
			String start = p_request.getParameter("start");
			String end = p_request.getParameter("end");
			String mode = p_request.getParameter("mode");

			logger.fatal("Entered GetAllAttempts");
			logger.debug("attemptCategory: " + attemptCategory);
			logger.debug("start: " + start);
			logger.debug("end: " + end);
			logger.debug("mode: " + mode);
			String response="";
			if (mode.equals("user")) {
				 response=new Gson().toJson(webSecurityLogService.getAllUserAttemptsByAttemptCategoryAndDuration(attemptCategory,
						start, end));
			} else if (mode.equals("ip")) {
				 response=new Gson().toJson(webSecurityLogService.getAllIpAttemptsByAttemptCategoryAndDuration(attemptCategory,
						start, end));
			}
			p_response.getWriter().write(response);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}

}
