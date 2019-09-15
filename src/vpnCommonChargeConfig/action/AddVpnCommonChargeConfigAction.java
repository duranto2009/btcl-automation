package vpnCommonChargeConfig.action;

import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;


import databasemanager.DatabaseManager;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;


import vpnCommonChargeConfig.CommonChargeDTO;
import vpnCommonChargeConfig.VpnCommonChargeConfigService;
import vpnCommonChargeConfig.form.vpnCommonChargeConfigForm;

public class AddVpnCommonChargeConfigAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) {
		logger.debug("UpdateCostConfigAction()");
		String target = "success";
		
		vpnCommonChargeConfigForm form = (vpnCommonChargeConfigForm) p_form;
		
		CommonChargeDTO dto = new CommonChargeDTO();

		VpnCommonChargeConfigService service = new VpnCommonChargeConfigService();
		
		try {
			dto.setId(DatabaseManager.getInstance().getNextSequenceId("at_vpn_common_charge_config"));
			dto.setCategoryID(form.getCategoryID());
			dto.setShiftingCharge(form.getShiftingCharge());
			dto.setUpgradationCharge(form.getUpgradationCharge());
			dto.setChargePerPoint(form.getChargePerPoint());
			dto.setRatioL3L2(form.getRatioL3L2());
			dto.setMinAdvancePaymentForMonth(form.getMinAdvancePaymentForMonth());
			dto.setMinAdvancePaymentForSpecialPayment(form.getMinAdvancePaymentForSpecialPayment());
			dto.setMinNumOfConnectionForFreePort(form.getMinNumOfConnectionForFreePort());
			dto.setMinimumBWForFreePort(form.getMinimumBWForFreePort());
			dto.setCompensationForLateBillInPercentage(form.getCompensationForLateBillInPercentage());
			dto.setTimeForNewBIllForLatePaymentInMonth(form.getTimeForNewBIllForLatePaymentInMonth());
			dto.setCompensationForNewBillForLatePayment(form.getCompensationForNewBillForLatePayment());
			dto.setTimeForOldLatePaymentInMonth(form.getTimeForOldLatePaymentInMonth());
			dto.setReConChargeForCutDown(form.getReConChargeForCutDown());
			dto.setOpticalFiberInstallationCharge( form.getOpticalFiberInstallationCharge() );
			dto.setOFChargePerMeter( form.getOFChargePerMeter() ); 
			
			String activationDateString = form.getActivationDate();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date activationDate = df.parse(activationDateString);
			dto.setActivationDate(activationDate.getTime());
			dto.setLastModificationTime(System.currentTimeMillis());
			dto.setDeleted(false);
			logger.debug("====================The DTO to be inserted new as common charge config=================== \n"+dto);
			service.add(dto);
			
		} catch (Exception e) {
			target = "failure";
			logger.fatal("Error inside Add vpn common charge config action "+e);
		}
		return p_mapping.findForward(target);
	}
}
