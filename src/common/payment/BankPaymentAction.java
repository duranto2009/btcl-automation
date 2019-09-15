package common.payment;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.LoginDTO;
import login.PermissionConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import common.CommonActionStatusDTO;
import common.CommonService;
import sessionmanager.SessionConstants;

public class BankPaymentAction extends Action {

	Logger logger = Logger.getLogger(getClass());
	CommonService commonService = new CommonService();
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
		boolean hasPermission = false;
		
		if( loginDTO.getUserID()>0 )
		{
		    if(loginDTO.getMenuPermission( PermissionConstants.BILL) != -1 
		    		
		      &&( loginDTO.getMenuPermission( PermissionConstants.BILL_BANK_PAYMENT ) >= PermissionConstants.PERMISSION_FULL ) ){
		    	
		        hasPermission=true;
		    }
		}
		
		if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward( "applicationRoot" );
		}
		logger.debug(loginDTO);
		if (request.getMethod().equalsIgnoreCase("get")) {
			
			return handleGet(mapping, form, request, response);
		} else {
			
			return handlePost(mapping, form, request, response);
		}

	}

	private ActionForward handlePost(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("post called");
		
		CommonActionStatusDTO actionDTO = new CommonActionStatusDTO();
		PaymentService paymentService= new PaymentServiceDomain();
		
		try {
			
			PaymentDTO paymentDTO = (PaymentDTO) form;

			logger.debug(paymentDTO.toString());
			
			
			paymentService.insertNewBankPayment(paymentDTO, loginDTO, request);
			
		} catch (Exception ex) {
			
			actionDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
			actionDTO.setMessage("Payment is not successful");
			actionDTO.storeInSession(request, response);
			logger.debug("error:", ex);

			return mapping.findForward("error");
			
		}
		
		actionDTO.setMessage("Payment is successful");
		actionDTO.storeInSession(request, response);
		
		ActionRedirect forward = new ActionRedirect(mapping.findForward("success"));
		forward.addParameter("moduleID", request.getParameter("moduleID"));
		return forward;
		
	}

	private ActionForward handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.debug("get called");
		return mapping.findForward("success");
		
	}

}
