package vpn.payment;

import common.CommonActionStatusDTO;
import common.CommonService;
import common.ModuleConstants;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import common.payment.constants.PaymentConstants;
import login.LoginDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import permission.PermissionDAO;
import sessionmanager.SessionConstants;
import util.ServiceDAOFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VpnMultipleBillPaymentAction extends Action {
    Logger logger = Logger.getLogger(getClass());
    CommonService commonService = new CommonService();

    PermissionDAO permissionDAO = new PermissionDAO();
    LoginDTO loginDTO = null;
    CommonActionStatusDTO actionDTO = new CommonActionStatusDTO();
    PaymentService paymentService = ServiceDAOFactory.getService(PaymentService.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        PaymentDTO paymentDTO = (PaymentDTO) form;
        String actionType = request.getParameter("actionType");
        if ("verification".equals(actionType)) {
            return processVerification(paymentDTO, request);
        } else if ("approval".equals(actionType)) {
            return processApproval(paymentDTO, request);
        } else {
            return null;
        }

    }

    /**
     * this is the first step while client is paying bill or insert payment info
     *
     * @param paymentDTO
     * @param request
     * @return
     */
    private ActionForward processVerification(PaymentDTO paymentDTO, HttpServletRequest request) {
        ActionRedirect forward = new ActionRedirect();
        int moduleID = Integer.parseInt(request.getParameter("moduleID"));

        try {

            logger.debug(paymentDTO.toString());
            if (paymentDTO.getBillIDs().length == 0) {
                actionDTO.setSuccessMessage("Invalid bill  ID", false, request);

                if (moduleID == ModuleConstants.Module_ID_LLI) {
                    forward.setPath("/lli/application/search.do");
                } else if (moduleID == ModuleConstants.Module_ID_COLOCATION) {
                    forward.setPath("/co-location/search.do");
                } else if (moduleID == ModuleConstants.Module_ID_NIX) {
                    forward.setPath("/nix/application/search.do");
                } else if (moduleID == ModuleConstants.Module_ID_VPN) {
                    forward.setPath("/vpn/application/search.do");
                }


                forward.setRedirect(true);
                return forward;
            }

            paymentService.insertNewBankPayment(paymentDTO, loginDTO, request);

        } catch (Exception ex) {

            logger.debug("error:", ex);
            if (request.getSession(true).getAttribute("actionStatusDTO") == null) {
                actionDTO.setErrorMessage("Payment is not successful try again", false, request);
            }

            String backUrl = request.getHeader("referer");
            String[] billIDs = request.getParameterValues("billIDs");

            backUrl += "?";

            for (String id : billIDs)
                backUrl += "billIDs=" + id.trim() + "&";

            forward.setPath(backUrl);
			
			/*if( request.getParameter("moduleID") != null && Integer.parseInt( request.getParameter("moduleID") ) == ModuleConstants.Module_ID_VPN ) 
				forward.setPath("/SearchPayment.do?moduleID="+ModuleConstants.Module_ID_VPN);
			else if( request.getParameter("moduleID") != null && Integer.parseInt( request.getParameter("moduleID") ) == ModuleConstants.Module_ID_LLI ) 
				forward.setPath("/SearchPayment.do?moduleID="+ModuleConstants.Module_ID_LLI);*/

            forward.setRedirect(true);

            return forward;
        }

        actionDTO.setSuccessMessage("Payment is  successful. Please wait for approval", false, request);

        if (request.getParameter("moduleID") != null && moduleID == ModuleConstants.Module_ID_VPN) {
            if (loginDTO.getUserID() < 0) {
                forward.setPath("/vpn/link/search.do");
            } else {
//            forward.setPath("/SearchPayment.do?moduleID=" + ModuleConstants.Module_ID_LLI);
                forward.setPath("/common/payment/linkPayment.jsp?paymentID=" + paymentDTO.getID() + "&moduleID=" + ModuleConstants.Module_ID_LLI);
            }
        } else if (request.getParameter("moduleID") != null && moduleID == ModuleConstants.Module_ID_LLI) {

            loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
            if (loginDTO.getUserID() < 0) {
                forward.setPath("/lli/application/search.do");
            } else {
//            forward.setPath("/SearchPayment.do?moduleID=" + ModuleConstants.Module_ID_LLI);
                forward.setPath("/common/payment/linkPayment.jsp?paymentID=" + paymentDTO.getID() + "&moduleID=" + ModuleConstants.Module_ID_LLI);
            }
        } else if (request.getParameter("moduleID") != null && moduleID == ModuleConstants.Module_ID_COLOCATION) {

            loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
            if (loginDTO.getUserID() < 0) {
                forward.setPath("/co-location/search.do");
            } else {
//            forward.setPath("/SearchPayment.do?moduleID=" + ModuleConstants.Module_ID_LLI);
                forward.setPath("/common/payment/linkPayment.jsp?paymentID=" + paymentDTO.getID() + "&moduleID=" + ModuleConstants.Module_ID_COLOCATION);
            }
        } else if (request.getParameter("moduleID") != null && moduleID == ModuleConstants.Module_ID_NIX) {

            loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
            if (loginDTO.getUserID() < 0) {
                forward.setPath("/nix/application/search.do");
            } else {
//            forward.setPath("/SearchPayment.do?moduleID=" + ModuleConstants.Module_ID_LLI);
                forward.setPath("/common/payment/linkPayment.jsp?paymentID=" + paymentDTO.getID() + "&moduleID=" + ModuleConstants.Module_ID_NIX);
            }
        }

        forward.setRedirect(true);

        return forward;
    }

    /**
     * this the 2nd step while revenue team approving the payment
     *
     * @param paymentDTO
     * @param request
     * @return
     */
    private ActionForward processApproval(PaymentDTO paymentDTO, HttpServletRequest request) {
        ActionRedirect forward = new ActionRedirect();
        int moduleID = Integer.parseInt(request.getParameter("moduleID"));
        try {

            logger.debug(paymentDTO);
            String action = request.getParameter("action");
            if ("approve".equals(action)) {
                paymentDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_APPROVED);
            } else if ("reject".equals(action)) {
                paymentDTO.setPaymentStatus(PaymentConstants.PAYMENT_STATUS_REJECTED);
            }
            actionDTO = paymentService.approveBankPayment(paymentDTO, loginDTO);
            actionDTO.setSuccessMessage(actionDTO.getMessage(), false, request);
            setForwardPathForVerifyPaymentStep(request, forward, moduleID);
            forward.setRedirect(true);
            return forward;

        } catch (Exception ex) {
            logger.debug("error:", ex);
            actionDTO.setErrorMessage(actionDTO.getMessage(), false, request);
            return null;
        }
    }

    private void setForwardPathForVerifyPaymentStep(HttpServletRequest request, ActionForward forward, int moduleID) {
        forward.setPath("/payment/search.do?moduleID=" +moduleID);
    }

}
