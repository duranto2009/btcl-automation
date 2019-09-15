package vpn.payment;

import common.CommonActionStatusDTO;
import common.CommonService;
import common.payment.PaymentService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import permission.PermissionDAO;
import sessionmanager.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VpnTransferBalanceAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	CommonService commonService = new CommonService();

	PermissionDAO permissionDAO = new PermissionDAO();
	LoginDTO loginDTO = null;
	CommonActionStatusDTO actionDTO = new CommonActionStatusDTO();
	PaymentService paymentService = new PaymentService();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if ("POST".equalsIgnoreCase(request.getMethod())) {
//			try {
//				long fromVPNLinkID = Long.parseLong(request.getParameter("vpnLinkFrom"));
//				long toVPNLinkID = Long.parseLong(request.getParameter("vpnLinkTo"));
//				paymentService.transferVPNBalance(fromVPNLinkID, toVPNLinkID, loginDTO, request);
//				new CommonActionStatusDTO().setSuccessMessage("Balance Transfer is successful", false, request);
//			} catch (Exception e) {
//				new CommonActionStatusDTO().setErrorMessage(e.getMessage(), false, request);
//			}
			return mapping.findForward("success");
		} else {
			return mapping.findForward("success");
		}

	}

}
