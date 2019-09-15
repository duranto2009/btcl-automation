package vpn.client;

import common.CommonActionStatusDTO;
import common.CommonSelector;
import common.ModuleConstants;
import login.LoginDTO;
import login.PermissionConstants;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientSearchAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		String target = "success";
		
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
		boolean hasPermission = false;
		
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
		int menuClientID = moduleID * 1000 + 2;
		int menuClientSearchID = moduleID * 1000 + 4;
		if(moduleID == ModuleConstants.Module_ID_LLI) {
			menuClientID = moduleID *1000 + 100;
			menuClientSearchID = moduleID *1000 + 102;
		}
		
		if(loginDTO.getUserID()>0)
		{
		    if((loginDTO.getMenuPermission(menuClientID) !=-1)
		      &&(loginDTO.getMenuPermission(menuClientSearchID) >= PermissionConstants.PERMISSION_READ))
		        hasPermission=true;
		}

		hasPermission=true;
		
		if( !hasPermission ){
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}

		ClientService service = new ClientService();
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_VPN_CLIENT, p_request,
				service, SessionConstants.VIEW_VPN_CLIENT, SessionConstants.SEARCH_VPN_CLIENT);

		
		
		logger.debug("moduleID " + moduleID);
		
		CommonSelector commonSelector = new CommonSelector();
		commonSelector.moduleID = moduleID;
		rnManager.doJob(loginDTO, commonSelector);

		target = "search";
		ActionRedirect redirect = new ActionRedirect(p_mapping.findForward(target));
		redirect.addParameter("moduleID", moduleID);
		redirect.setRedirect(false);
		return redirect;
	}
}
