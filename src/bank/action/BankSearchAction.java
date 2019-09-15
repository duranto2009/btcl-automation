package bank.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bank.service.BankService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("BankSearch")
public class BankSearchAction extends AnnotatedRequestMappingAction{
	@Service
	BankService bankService;
	@RequestMapping(mapping = "/Banks", requestMethod = RequestMethod.All)
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_BANK, p_request,
				bankService, SessionConstants.VIEW_BANK, SessionConstants.SEARCH_BANK);

		rnManager.doJob(loginDTO);

		return (p_mapping.findForward(target));

	}
}
