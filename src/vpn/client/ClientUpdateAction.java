package vpn.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import common.BtclRedirectUtility;
import common.CommonActionStatusDTO;
import common.repository.AllClientRepository;
import login.LoginDTO;
import request.RequestUtilService;
import sessionmanager.SessionConstants;
import util.Validator;

public class ClientUpdateAction extends Action {
	LoginDTO loginDTO = null;
	Logger logger = Logger.getLogger(getClass());
	long currentTime = System.currentTimeMillis();
	RequestUtilService requestUtilService = new RequestUtilService();
	ClientUpdateService clientUpdateService = new ClientUpdateService();
	
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception {
		
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		ClientForm clientForm = (ClientForm) p_form;		
		ClientDetailsDTO clientDetailsDTOfromForm = clientForm.getClientDetailsDTO();
		
		//Module ID
		int moduleID=-1;
		if(clientDetailsDTOfromForm.getModuleID() > 0){
			moduleID=clientDetailsDTOfromForm.getModuleID();
		} else {
			throw new Exception("Module ID not found");
		}
		
		ClientDetailsDTO clientDetailsDTOExisting = AllClientRepository.getInstance().getVpnClientByClientID(clientDetailsDTOfromForm.getClientID(), moduleID);
		CommonActionStatusDTO commonActionStatusDTO = clientUpdateService.processClientUpdatePermission(clientDetailsDTOExisting, loginDTO, p_request);
		if(commonActionStatusDTO.getStatusCode() == CommonActionStatusDTO.ERROR_STATUS_CODE)
		{
			return p_mapping.findForward( "applicationRoot" );
		}
		

		String target="success";
		
		try {
			commonActionStatusDTO = processValidity(clientForm, p_request);
			
			logger.debug(commonActionStatusDTO);
			if(commonActionStatusDTO.getStatusCode() == CommonActionStatusDTO.ERROR_STATUS_CODE){
				target=BtclRedirectUtility.getTarget(moduleID, "Client", "Edit", "Error");
				
				p_request.setAttribute(p_mapping.getAttribute(), clientForm);
				ActionRedirect forward = new ActionRedirect(p_mapping.findForward(target));
				BtclRedirectUtility.setForwardParameters(forward, moduleID, clientDetailsDTOfromForm.getClientID());
				forward.addParameter("edit","");
				return forward;
			}
			clientUpdateService.clientUpdateService(p_mapping, p_form, p_request, p_response);
			target=BtclRedirectUtility.getTarget(moduleID, "Client", "Edit", "Success");
			commonActionStatusDTO.setSuccessMessage("Edited Sucessfully.", false, p_request);
		} catch (Exception ex) {
			logger.fatal("Exception ", ex);
			target=BtclRedirectUtility.getTarget(moduleID, "Client", "Edit", "Error");
			commonActionStatusDTO.setErrorMessage(ex.toString(), false, p_request);			
		}
		
		logger.debug(target);
		ActionRedirect forward = new ActionRedirect(p_mapping.findForward(target));
		BtclRedirectUtility.setForwardParameters(forward, moduleID, clientDetailsDTOfromForm.getClientID());
		return forward;
	}
	
	
	
	
	
	
	private CommonActionStatusDTO processValidity(ClientForm vpnClientform, HttpServletRequest p_request) throws Exception  {
		String validationMessage="";
		StringUtils.trimToEmpty(vpnClientform.getClientDetailsDTO().getLoginName());
		
		if (vpnClientform.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_INDIVIDUAL) {
			if (StringUtils.isEmpty(vpnClientform.registrantContactDetails.getRegistrantsName())) {
				validationMessage += "Person Name can not be empty <br>";
			}
			
		} else if (vpnClientform.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_COMPANY) {
			if (StringUtils.isEmpty(vpnClientform.registrantContactDetails.getRegistrantsName())) {
				validationMessage += "Company Name can not be empty <br>";
			}
			if (vpnClientform.registrantContactDetails.getWebAddress().length() > 0
					&& !Validator.isValidURL(vpnClientform.registrantContactDetails.getWebAddress())) {
				validationMessage += "web address is not valid <br>";
			}
		}
		
		String emailStr=vpnClientform.registrantContactDetails.getEmail();
		if (!Validator.isValidEmailAddress(emailStr)) {
			validationMessage += "Email is not valid <br>";
		}
		if(Validator.isEmailUsed(emailStr,vpnClientform.getClientDetailsDTO().getClientID())){
			validationMessage +="Email : "+ vpnClientform.registrantContactDetails.getEmail()+ " is already used by another user <br>";
		}
		if (!Validator.isValidMobile(vpnClientform.registrantContactDetails.getPhoneNumber())) {
			validationMessage += "Mobile is not valid <br>";
		}

		if (StringUtils.isEmpty(vpnClientform.registrantContactDetails.getCity())) {
			validationMessage += "City is not valid <br>";
		}
		
		if (StringUtils.isEmpty(vpnClientform.clientDetailsDTO.getLoginPassword())) {
			validationMessage += "Password can't be empty <br>";
		}
		if (!vpnClientform.clientDetailsDTO.getLoginPassword().equals(p_request.getParameter("confirmLoginPassword"))) {
			validationMessage += "Password and confirm password does not match <br>";
		}
		if (!Validator.isValidMobile(vpnClientform.registrantContactDetails.getPhoneNumber())) {
			validationMessage += "Mobile is not valid <br>";
		}
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		if(validationMessage.length() > 0)
		{
			commonActionStatusDTO.setErrorMessage(validationMessage, false, p_request);
		}
		return commonActionStatusDTO;
	}
}
