package vpn.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import common.CommonActionStatusDTO;

public class MigratedClientUpdateAction extends DispatchAction {

	Logger logger = Logger.getLogger(getClass());
	MigratedClientUpdateService migratedClientUpdateService = new MigratedClientUpdateService();
	
	public ActionForward migrateVpnClient(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("MigratedClientUpdateAction");
		logger.debug(request.getParameter("vpnClientID"));
		logger.debug(request.getParameter("activationDate"));
		
		migratedClientUpdateService.updateActivationDate(request.getParameter("vpnClientID"), request.getParameter("activationDate"));
		
		new CommonActionStatusDTO().setSuccessMessage("Activation Date is updated successfully.", false, request);
		return mapping.findForward("successVpn");
	}

	public ActionForward migrateLliClient(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("MigratedClientUpdateAction");
		logger.debug(request.getParameter("lliClientID"));
		logger.debug(request.getParameter("activationDate"));
		
		migratedClientUpdateService.updateActivationDate(request.getParameter("lliClientID"), request.getParameter("activationDate"));
		
		new CommonActionStatusDTO().setSuccessMessage("Activation Date is updated successfully.", false, request);
		return mapping.findForward("successLli");
	}

}
