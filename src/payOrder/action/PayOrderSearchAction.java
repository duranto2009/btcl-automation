package payOrder.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import payOrder.service.PayOrderService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("PayOrderSearch")
public class PayOrderSearchAction extends AnnotatedRequestMappingAction{
	@Service
	PayOrderService payOrderService;
	@RequestMapping(mapping = "/PayOrders", requestMethod = RequestMethod.All)
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_PAY_ORDER, p_request,
				payOrderService, SessionConstants.VIEW_PAY_ORDER, SessionConstants.SEARCH_PAY_ORDER);

		rnManager.doJob(loginDTO);
		return (p_mapping.findForward(target));

	}
}
