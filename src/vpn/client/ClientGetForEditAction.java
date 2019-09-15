package vpn.client;

import common.ClientConstants;
import common.CommonSelector;
import common.ModuleConstants;
import common.PermissionHandler;
import login.LoginDTO;
import login.PermissionConstants;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sessionmanager.SessionConstants;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientGetForEditAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception {
		
		PermissionHandler.handleClientPermissionByModuleID(p_request, p_response, Integer.parseInt(p_request.getParameter("moduleID")));
		
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		ClientForm form = (ClientForm) p_form;

		if(p_request.getParameter("entityID") == null || p_request.getParameter("moduleID") == null) {
			p_response.sendRedirect(p_request.getContextPath());
		}
		
		long id = Integer.parseInt(p_request.getParameter("entityID"));
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));


		int clientMenuId = (moduleID == ModuleConstants.Module_ID_LLI ? moduleID*1000 + 100 : moduleID*1000 + 2);
		int clientSearchId = (moduleID == ModuleConstants.Module_ID_LLI ? moduleID*1000 + 102 : moduleID*1000 + 4);
		if(loginDTO.getAccountID() > 0 && !(loginDTO.getAccountID() == id)){
			p_response.sendRedirect(p_request.getContextPath());
		}
		else if( loginDTO.getUserID()>0 ){
			if(p_request.getParameter("edit")!=null){

				PermissionHandler.handleMenuPermission(p_request, p_response, clientMenuId, clientSearchId, PermissionConstants.PERMISSION_MODIFY);
			}
		    else{
		    	PermissionHandler.handleMenuPermission(p_request, p_response, clientMenuId, clientSearchId, PermissionConstants.PERMISSION_READ);
		    }
		}
		
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = moduleID;

		ClientService service = new ClientService();
		ClientDetailsDTO dto = service.getClient(id, loginDTO, commonSelector);
		form.setClientDetailsDTO(dto);
		if(dto.getClientCategoryType()==ClientForm.CLIENT_TYPE_COMPANY){
			ClientContactDetailsDTO newVpnRegistrantContactDTO= dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT);
			newVpnRegistrantContactDTO.setRegistrantsLastName("");
		}
		
		form.setRegistrantContactDetails(dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_REGISTRANT));
		form.setBillingContactDetails(dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_BILLING));
		form.setAdminContactDetails(dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_ADMIN));
		form.setTechnicalContactDetails(dto.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_TECHNICAL));

		String target = (p_request.getParameter("edit") != null) ? "getClientUpdate" : "getClientPreview";

		p_request.setAttribute("form", form);
		return (p_mapping.findForward(target));
	}

}
