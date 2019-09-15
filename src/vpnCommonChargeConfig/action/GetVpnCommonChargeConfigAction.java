package vpnCommonChargeConfig.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import vpnCommonChargeConfig.CommonChargeDTO;
import vpnCommonChargeConfig.VpnCommonChargeConfigService;

public class GetVpnCommonChargeConfigAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
		HttpServletResponse p_response) {
		logger.debug("GetVpnCommonChargeConfigAction()");
		String target = "success";

		VpnCommonChargeConfigService service = new VpnCommonChargeConfigService();
		
		try {
			
//			CommonChargeDTO latestDTO = service.getLatestDTO();	
			CommonChargeDTO activeCharge = service.getCurrentActiveCommonChargeDTO();
			List<CommonChargeDTO> allCommonCharges = service.getAllFutureCommonChargeDTOs();
			p_request.setAttribute("latestDTO",null);
			p_request.setAttribute("allCharges",allCommonCharges);
			p_request.setAttribute( "activeCharge", activeCharge );
			
		} catch (Exception e) {
			
			target = "failure";
			logger.fatal(e);
		}
		return p_mapping.findForward(target);
	}
}

