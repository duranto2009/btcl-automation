package common.payment;

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
import util.ServiceDAOFactory;

public class PaymentSearchAction extends Action
{
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;
  
	public ActionForward execute(ActionMapping p_mapping,ActionForm p_form,HttpServletRequest p_request,HttpServletResponse p_response) throws Exception{
		
		PermissionHandler.handleClientPermissionByModuleID(p_request, p_response, Integer.parseInt(p_request.getParameter("moduleID")));
	  
		String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
		
	    PaymentService paymentService = ServiceDAOFactory.getService(PaymentService.class);
	    paymentService.setModuleID(moduleID);
	    
	    RecordNavigationManager rnManager = new RecordNavigationManager(
	    SessionConstants.NAV_PAYMENT, p_request, paymentService,
	    SessionConstants.VIEW_PAYMENT,
	    SessionConstants.SEARCH_PAYMENT);
	    
	    rnManager.doJob(loginDTO);
	
	    return (p_mapping.findForward(target));
	   
	}

}
