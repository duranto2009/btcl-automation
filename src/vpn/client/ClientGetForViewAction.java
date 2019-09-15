package vpn.client;

import comment.CommentService;
import common.*;
import login.LoginDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import permission.StateActionDTO;
import request.CommonRequestDTO;
import sessionmanager.SessionConstants;
import util.ForwardView;
import util.RecordNavigationManager;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ClientGetForViewAction extends Action {
	
	Logger logger = Logger.getLogger(getClass());
	CommonService commonService = new CommonService();
	LoginDTO loginDTO = null;

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception {
		
		PermissionHandler.handleClientPermissionByModuleID(p_request, p_response, Integer.parseInt(p_request.getParameter("moduleID")));
		
		String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute( SessionConstants.USER_LOGIN );
		
		String idStr = p_request.getParameter("entityID");
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
		long id = -1;
		if (idStr != null){
			id = Long.parseLong(idStr);
		}

		
		boolean hasPermission = false;
		if( loginDTO.getAccountID() > 0 && loginDTO.getAccountID() == Long.parseLong(idStr) ){
			hasPermission = true;
			p_request.setAttribute( "hasPermission", true );
		}
		else if( loginDTO.getUserID()>0 ){
		    
/*			if((loginDTO.getMenuPermission(PermissionConstants.DOMAIN_CLIENT) !=-1) &&(loginDTO.getMenuPermission(PermissionConstants.DOMAIN_CLIENT_SEARCH) >= PermissionConstants.PERMISSION_READ)){
		        hasPermission=true;
		        p_request.setAttribute( "hasPermission", true );
		    }*/
			int VIEW_CLIENT_SUMMARY = (moduleID * ModuleConstants.MULTIPLIER) + 5006;
			int VIEW_CLIENT_DETAILS = (moduleID * ModuleConstants.MULTIPLIER) + 5007;
			
			logger.debug("VIEW_CLIENT_SUMMARY " + VIEW_CLIENT_SUMMARY);
			
			hasPermission = loginDTO.getColumnPermission(VIEW_CLIENT_SUMMARY);		    
	        p_request.setAttribute( "hasPermission", hasPermission );
		}
		if( !hasPermission ){
			p_request.setAttribute( "hasPermission", false );
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
		
		ClientForm form = (ClientForm) p_form;


		CommonSelector commonSelector= new CommonSelector();
		commonSelector.moduleID=moduleID;
		logger.debug(commonSelector);
		ClientService service = new ClientService();
		ClientDetailsDTO clientDetailsDTO = service.getClient(id, loginDTO, commonSelector);

		logger.debug(clientDetailsDTO);
		
		String currentTab = p_request.getParameter("currentTab");
		logger.debug("get called");

		p_request.setAttribute("clientID", id);
		p_request.setAttribute("entityID", clientDetailsDTO.getClientID());
		p_request.setAttribute("form", form);
		
		if ("3".equals(currentTab)) {
			logger.debug("history search");
			return searchHistory(p_mapping, form, p_request, p_response);
		}
		if ("2".equals(currentTab)) {
			logger.debug("comment search POST");
			return searchComment(p_mapping, form, p_request, p_response);
		}
		
		
		form.setClientDetailsDTO(clientDetailsDTO);
		if (clientDetailsDTO.getVpnContactDetails() != null && clientDetailsDTO.getVpnContactDetails().size() > 0)
		{
			form.setRegistrantContactDetails(clientDetailsDTO.getVpnContactDetails().get(ClientContactDetailsDTO.REGISTRANT_CONTACT));
		}
		logger.debug("dto " + clientDetailsDTO);

		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID));
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		ArrayList<StateActionDTO> stateActionDTOs = commonService.getAllNextAction(commonRequestDTO, loginDTO);

		logger.debug("sending stateActionDTOs " + stateActionDTOs);
		p_request.setAttribute("stateActions", stateActionDTOs);
		target=ForwardView.getTarget(moduleID);
		return (p_mapping.findForward(target));
	}
	
	private ActionForward searchHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("search called");
		int entityTypeID=Integer.parseInt((String)request.getParameter("entityTypeID"));
		long iEntityID=Long.parseLong((String)request.getParameter("entityID"));
		int moduleID=Integer.parseInt((String)request.getParameter("moduleID"));
		
		CommonRequestSearchService crsService= new CommonRequestSearchService(entityTypeID,iEntityID);
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_COMMON_REQUEST, request,
				crsService, SessionConstants.VIEW_COMMON_REQUEST, SessionConstants.SEARCH_COMMON_REQUEST);
		rnManager.doJob(loginDTO);
		return  mapping.findForward(ForwardView.getTarget(moduleID));
	}
	private ActionForward searchComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("comment search called");
		int entityTypeID=Integer.parseInt((String)request.getParameter("entityTypeID"));
		long iEntityID=Long.parseLong((String)request.getParameter("entityID"));
		int moduleID=Integer.parseInt((String)request.getParameter("moduleID"));
		CommentService cmService= new CommentService(entityTypeID,iEntityID);
		
		if(!loginDTO.getIsAdmin()){
			EntityDTO entityDTO=cmService.getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, iEntityID);
			if((entityDTO==null) ||  (entityDTO.getClientID()!=loginDTO.getAccountID())){
				new CommonActionStatusDTO().setErrorMessage("Permission Failure", false, request);
				return  mapping.findForward(ForwardView.getTarget(moduleID));
			}
		}
		
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_COMMENT, request,
				cmService, SessionConstants.VIEW_COMMENT, SessionConstants.SEARCH_COMMENT);
		rnManager.doJob(loginDTO);
		return  mapping.findForward(ForwardView.getTarget(moduleID));
	}

}
