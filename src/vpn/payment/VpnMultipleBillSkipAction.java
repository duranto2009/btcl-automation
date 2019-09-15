package vpn.payment;

import common.CommonActionStatusDTO;
import common.CommonService;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import permission.PermissionDAO;
import sessionmanager.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VpnMultipleBillSkipAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	CommonService commonService = new CommonService();


	PermissionDAO permissionDAO = new PermissionDAO();
	LoginDTO loginDTO = null;
	CommonActionStatusDTO actionDTO = new CommonActionStatusDTO();
	PaymentService paymentService= new PaymentService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		PaymentDTO paymentDTO = (PaymentDTO) form;
		int moduleID=Integer.parseInt(request.getParameter("moduleID"));
		
		
		paymentService.skipMultipleBill(paymentDTO, moduleID, loginDTO, request);
		
		ActionRedirect forward = new ActionRedirect();
		forward.setPath("/SearchBill.do?moduleID="+moduleID);
		forward.setRedirect(true);
		return forward;
	}
	
	

}
