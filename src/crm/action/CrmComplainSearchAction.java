package crm.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import crm.service.CrmComplainService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;
import util.ServiceDAOFactory;

@ActionRequestMapping("CrmComplainSearch")
public class CrmComplainSearchAction extends AnnotatedRequestMappingAction {
	@RequestMapping(mapping = "/Complains", requestMethod = RequestMethod.GET)
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		CrmComplainService crmComplainService = (CrmComplainService) ServiceDAOFactory
				.getService(CrmComplainService.class);
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_CRM_COMPLAIN, p_request,
				crmComplainService, SessionConstants.VIEW_CRM_COMPLAIN, SessionConstants.SEARCH_CRM_COMPLAIN);

		rnManager.doJob(loginDTO);

		return (p_mapping.findForward(target));

	}
}
