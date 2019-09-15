package common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.repository.AllClientRepository;
import login.LoginDTO;
import request.StateDTO;
import request.StateRepository;
import sessionmanager.SessionConstants;
import vpn.client.ClientDetailsDTO;
/**
 * @author dhrubo
 */
public class PermissionHandler {

	public static void handleClientPermissionByModuleID(HttpServletRequest request, HttpServletResponse response, int moduleID) throws IOException {
		
		LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
		if(loginDTO.getAccountID()>0) {
			ClientDetailsDTO moduleClient = AllClientRepository.getInstance().getVpnClientByClientID(loginDTO.getAccountID(), moduleID);
			int statusOfModuleClient = moduleClient.getCurrentStatus();
			StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(statusOfModuleClient);
			if(stateDTO != null){
				if(stateDTO.getActivationStatus() == EntityTypeConstant.STATUS_NOT_ACTIVE) {
					response.sendRedirect(request.getContextPath());
				}
			}
		}
	}
	
	public static void handleMenuPermission(HttpServletRequest request, HttpServletResponse response, int mandatoryPermissionConstant, int additionalPermissionConstant, int additionalPermissionLevel) throws IOException {
		LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		if( !
			(

				(loginDTO.getMenuPermission(mandatoryPermissionConstant) !=-1)
				&&
				(loginDTO.getMenuPermission(additionalPermissionConstant) >= additionalPermissionLevel)
			)
		){
			new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, request );
			response.sendRedirect(request.getContextPath());
		}
	}
	
	public static void handleClientPermissionByEntityIDAndEntityTypeID(LoginDTO loginDTO, Long clientID, Long entityID, Integer entityTypeID) throws Exception {
		if(loginDTO.getAccountID() > 0) {
			EntityDTO entity = new CommonService().getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, entityID);
			if(entity.getClientID() != clientID) {
				throw new RequestFailureException("You don't have permission to take this action");
			}
		}
	}
	
	/**
	 * @author Dhrubo
	 */
	public static void checkLoginDTOExistenceByRequest(HttpServletRequest request) throws Exception{
		if(request.getSession(false).getAttribute(SessionConstants.USER_LOGIN) == null) {
			throw new RequestFailureException("Please Log In to take this action");
		}
	}
}
