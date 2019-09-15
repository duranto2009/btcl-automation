package crm.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import crm.service.CrmCommonPoolService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("CrmClientComplainSearch")
public class CrmClientComplainSearchAction extends AnnotatedRequestMappingAction{
	@Service
	CrmCommonPoolService crmCommonPoolService;
	@RequestMapping(mapping = "/Complains", requestMethod = RequestMethod.All)
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_CRM_CLIENT_COMPLAIN, p_request,
				crmCommonPoolService, SessionConstants.VIEW_CRM_CLIENT_COMPLAIN, SessionConstants.SEARCH_CRM_CLIENT_COMPLAIN);

		rnManager.doJob(loginDTO);

		return (p_mapping.findForward(target));

	}
}
