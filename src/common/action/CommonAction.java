package common.action;

import coLocation.ColocationRequestTypeConstants;
import common.*;
import common.repository.AllClientRepository;
import global.GlobalService;
import lli.constants.LliRequestTypeConstants;
import login.LoginDTO;
import notification.NotificationDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import permission.PermissionService;
import request.*;
import requestMapping.Service;
import sessionmanager.SessionConstants;
import util.ServiceDAOFactory;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.constants.VpnRequestTypeConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


public class CommonAction extends Action {

    Logger logger = Logger.getLogger(getClass());
    //	VpnLinkService vpnService = new VpnLinkService();
//	LliLinkService lliService = new LliLinkService();
    CommonService commonService = new CommonService();
    PermissionService permissionService = new PermissionService();
    ClientService clientService = new ClientService();
    LoginDTO loginDTO = null;

    @Service private GlobalService globalService=ServiceDAOFactory.getService(GlobalService.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String actionForwardName = "";
        loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        logger.debug(loginDTO.getUsername());
        if (request.getMethod().equalsIgnoreCase("get")) {
            actionForwardName = handleGet(mapping, form, request, response);
        } else {
            CommonRequestDTO commonRequestDTO = (CommonRequestDTO) form;
            CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
            boolean isPermitted = permissionService.hasPermission(commonRequestDTO, loginDTO);
            if (isPermitted) {
                return handlePost(mapping, form, request, response);
            } else {
                commonActionStatusDTO.setErrorMessage("You have no permission to take this action", false, request);
            }
        }
        return mapping.findForward(actionForwardName);
    }

    private String handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String parameterValue = request.getParameter("id");
        logger.debug("get called");

        if (parameterValue != null && !parameterValue.trim().equals("")) {
            return null;//viewLink(mapping, form, request, response);
        } else {
            return null;//searchLink(mapping, form, request, response);
        }
    }


    private void handleDomainPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {
        switch (commonRequestDTO.getRequestTypeID()) {
            //case DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_APPROVE_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_REJECT_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.SYSTEM_REJECT_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_RENEW.CLIENT_CANCEL_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION:
//			case DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.CLIENT_CANCEL_APPLICATION:
//			{
//				commonService.cancelOrReject(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_APPROVE_APPLICATION:
//			{
//				commonService.approveClient(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_CANCEL_REQUEST:
//			{
//				commonService.rollbackRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_GENERATE_DEMAND_NOTE:
//			{
//				commonService.generateDemandNoteForNewDomain(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_RENEW.CLIENT_APPLY:
//			case DomainRequestTypeConstants.REQUEST_PARK_DOMAIN.SYSTEM_GENERATE_DEMAND_NOTE:
//			{
//				new DomainService().renewDomain(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_DISABLE_DOMAIN.SYSTEM_DISABLE_DOMAIN:
//			case DomainRequestTypeConstants.REQUEST_PARK_DOMAIN.SYSTEM_PARK_DOMAIN:
//			{
//				commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, false, true);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_DISABLE_CLIENT.SYSTEM_DISABLE_CLIENT:
//			{
//				commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, false, true);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.SYSTEM_FORWARD_FOR_APPROVAL:
//			{
//				logger.debug(""+DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.SYSTEM_FORWARD_FOR_APPROVAL);
//				logger.debug(request.getParameterNames());
//				commonService.approveOwnershipChange(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
//				break;
//			}
//			case DomainRequestTypeConstants.REQUEST_DOMAIN_OWNERSHIP_CHANGE.SYSTEM_GENERATE_DEMAND_NOTE:
//			{
//				commonService.generateDemandNoteForOwnershipChange(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }

        }

    }


    private void handleWebHostingPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleIpaddressPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleColocationPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            case ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.SYSTEM_GENERATE_DEMAND_NOTE: {
                commonService.generateDemandNoteForNewColocation(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleIigPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleVPNPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {
        switch (commonRequestDTO.getRequestTypeID()) {
            //CLIENT
            case VpnRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
            case VpnRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION: {
                commonService.cancelOrReject(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case VpnRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_APPROVE_APPLICATION: {
                commonService.approveClient(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case VpnRequestTypeConstants.REQUEST_DISABLE_CLIENT.SYSTEM_DISABLE_CLIENT:
                //case VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_DISABLE_LINK:
            {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, false, true);
                break;
            }

            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    /*private void handleLliPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception{

        switch(commonRequestDTO.getRequestTypeID())
        {
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_UPGRADE.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_POP_CHANGE.CLIENT_CANCEL_APPLICATION:
            {
                commonService.cancelOrReject(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_DISABLE_CLIENT.SYSTEM_DISABLE_CLIENT:
//			case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_DISABLE_LINK:
            {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, false, true);
                break;
            }

            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_DEMAND_NOTE_PAYMENT:
            {
                commonService.processSkipBillPaymentRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }

            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_CONNECTION_OWNERSHIP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			{
//				((LliLinkService)ServiceDAOFactory.getService(LliLinkService.class)).processInternalFrResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            {
                ((LliLinkService)ServiceDAOFactory.getService(LliLinkService.class)).processInternalFrResponseIPAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				lliService.processInternalFrResponseIPAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_APPROVE_APPLICATION:
            {
                commonService.approveClient(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
            {
                commonService.processExternalFRResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
            {
                commonService.processExternalFRResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END:
            {
                commonService.processExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END:
            {
                commonService.processExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END:
            {
                commonService.acceptExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_NEAR_END:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_FAR_END:
            {
                commonService.acceptExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_CANCEL_REQUEST:
            {
                commonService.rollbackRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE:
            case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE:
            case LliRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE:
            {
                commonService.generateAdviceNoteLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
            {
                ((CommonService)ServiceDAOFactory.getService(CommonService.class)).generateDemandNote(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_GENERATE_DEMAND_NOTE:
            {
                commonService.generateDemandNoteForIpAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_INFORM_SETUP_DONE:
            {
                commonService.processSetupDoneLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
                break;
            }
            /*case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MRC:
            {
                commonService.updateLinkBalanceLLI( commonRequestDTO );
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_START_SERVICE:
            {
                commonService.linkCreationCompleteLLI(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            /*case VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_INFORM_SETUP_DONE:
            case VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_INFORM_SETUP_DONE:{
                commonService.processBandWidthChangeSetupDoneLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
                break;
            }
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_START_SERVICE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_START_SERVICE:{
                commonService.bandwidthChangeCompleteLli(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            default:
            {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
            }
        }
    }
    */
    private void handleAdslPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleNixPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }

    private void handleDnshostingPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }


    private ActionForward handlePost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String modeValue = request.getParameter("mode");
        logger.debug("modeValue " + modeValue);
        CommonRequestDTO commonRequestDTO = (CommonRequestDTO) form;

		manageNotifications(request, commonRequestDTO);

//		commonRequestDTO.setActionName(request.getHeader("Referer"));
        commonRequestDTO.setActionName(EntityActionGenerator.getAction(commonRequestDTO.getRootEntityID(), commonRequestDTO.getRootEntityTypeID()));

        if (commonRequestDTO.getRequestToAccountID() == 0) {
            commonRequestDTO.setRequestToAccountID(null);
        }

        logger.debug("received commonRequestDTO " + commonRequestDTO);
        logger.debug("request.getParameter(reqID) " + request.getParameter("sourceRequestID"));

        if (request.getAttribute("description") != null) {
            commonRequestDTO.setDescription((String) request.getAttribute("description"));
        }

        if (loginDTO.getIsAdmin()) {
            commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
        } else {
            commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID());
        }

        boolean isRollbackRequest = false;

        if ((Math.abs(commonRequestDTO.getRequestTypeID()) % 100) == 99) {
            isRollbackRequest = true;
        }

        if (commonRequestDTO.getExpireTime() == 0 && !isRollbackRequest) {
            int nextState = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getNextStateID();
            commonRequestDTO.setExpireTime(System.currentTimeMillis() + StateRepository.getInstance().getStateDTOByStateID(nextState).getDurationInMillis());
        }

        if ((commonRequestDTO.getRequestTypeID() % EntityTypeConstant.MULTIPLIER2) == 4) {
            commonRequestDTO.setRequestToAccountID(commonRequestDTO.getClientID());
        }


        int moduleID = Math.abs(commonRequestDTO.getRequestTypeID() / ModuleConstants.MULTIPLIER);

        ActionForward actionForward = new ActionForward();

        CommonRequestDTO sourceRequestDTO = null;
        Set<CommonRequestDTO> bottomRequestDTOSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);
        for (CommonRequestDTO bottomRequestDTO : bottomRequestDTOSet) {
            if (bottomRequestDTO.getEntityID() == commonRequestDTO.getEntityID() && bottomRequestDTO.getEntityTypeID() == commonRequestDTO.getEntityTypeID()) {
                sourceRequestDTO = bottomRequestDTO;
            }
            continue;
        }

//		CommonRequestDTO sourceRequestDTO = new RequestUtilService().getRequestDTOByReqID(commonRequestDTO.getSourceRequestID());

        logger.debug("sourceRequestDTO " + sourceRequestDTO);
        boolean pageChanged = false;
        if (sourceRequestDTO != null) {
            if (sourceRequestDTO.getCompletionStatus() != RequestStatus.PENDING) {
                pageChanged = true;
            }
            if (commonRequestDTO.getReqID() > 0 && sourceRequestDTO.getReqID() != commonRequestDTO.getReqID()) {
                pageChanged = true;
            }
        }

        if (pageChanged) {
            /*******return with error********/
            actionForward.setPath(commonRequestDTO.getActionName());
            actionForward.setRedirect(true);
            CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
            commonActionStatusDTO.setErrorMessage("Someone has updated this page. Please try again.", false, request);
            return actionForward;
        }


        commonRequestDTO.setModuleID(moduleID);

        CommonRequestDTO newCommonRequestDTO = null;

        if (sourceRequestDTO != null) {
            newCommonRequestDTO = new CommonRequestDTO();
            SqlGenerator.populateObjectFromOtherObject(newCommonRequestDTO, CommonRequestDTO.class, sourceRequestDTO, CommonRequestDTO.class);
            newCommonRequestDTO.setSourceRequestID("" + sourceRequestDTO.getReqID());
            newCommonRequestDTO.setRequestTypeID(commonRequestDTO.getRequestTypeID());
            newCommonRequestDTO.setRequestToAccountID(commonRequestDTO.getRequestToAccountID());
            newCommonRequestDTO.setDescription(commonRequestDTO.getDescription());
            newCommonRequestDTO.setRequestByAccountID((loginDTO.getAccountID() > 0) ? loginDTO.getAccountID() : (-loginDTO.getUserID()));
            newCommonRequestDTO.setIP(request.getRemoteAddr());
            newCommonRequestDTO.setModuleID(moduleID);
            newCommonRequestDTO.setParentReqID(null);
            newCommonRequestDTO.setRootEntityID(commonRequestDTO.getRootEntityID());
            newCommonRequestDTO.setExpireTime(commonRequestDTO.getExpireTime());
        } else {
            newCommonRequestDTO = commonRequestDTO;
        }
        logger.debug("commonRequestDTO " + commonRequestDTO);

        if (moduleID == ModuleConstants.Module_ID_DOMAIN) {
            handleDomainPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_WEBHOSTING) {
            handleWebHostingPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_IPADDRESS) {
            handleIpaddressPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_COLOCATION) {
            handleColocationPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_IIG) {
            handleIigPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_VPN) {
            handleVPNPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_LLI) {
            handleLliPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_ADSL) {
            handleAdslPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_NIX) {
            handleNixPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        } else if (moduleID == ModuleConstants.Module_ID_DNSHOSTING) {
            handleDnshostingPost(mapping, newCommonRequestDTO, sourceRequestDTO, request, response, actionForward);
        }


//		if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//		{
//			request.setAttribute("returnUrl" , "/vpn/link/generateDemandNoteMigration.jsp" );
//		}
//		else if( commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//		{
//			request.setAttribute("returnUrl" , "/lli/link/generateDemandNoteMigration.jsp" );
//		}

        if (request.getAttribute("returnUrl") == null) {
            actionForward.setPath(commonRequestDTO.getActionName());
            actionForward.setRedirect(true);
        } else {

            actionForward.setPath(request.getAttribute("returnUrl").toString());
            actionForward.setRedirect(true);
        }
        if (request.getSession(true).getAttribute("actionStatusDTO") == null) {

            CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
            commonActionStatusDTO.setSuccessMessage("Action is completed successfully.", false, request);
        }

        return actionForward;

    }

    private void manageNotifications(HttpServletRequest request, CommonRequestDTO commonRequestDTO) throws Exception {
        loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        int moduleID = Math.abs(commonRequestDTO.getRequestTypeID() / ModuleConstants.MULTIPLIER);

        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(commonRequestDTO.getClientID());
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance()
                .getVpnClientByClientID(commonRequestDTO.getClientID(), moduleID);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setGenerationTime(System.currentTimeMillis());
        notificationDTO.setModuleId(clientDetailsDTO.getModuleID());
        notificationDTO.setEntityType("Update on registration from admin");
        notificationDTO.setRoleOrAccountId(clientDetailsDTO.getClientID());
        notificationDTO.setUserId(-1);
        notificationDTO.setDescription("Update on registration from admin " + loginDTO.getUsername());

        String actionURL = "/GetClientForView.do?moduleID=" + clientDetailsDTO.getModuleID() + "&entityID=" +
                clientDTO.getClientID();

        notificationDTO.setActionURL(actionURL);
        notificationDTO.setForClient(true);
        globalService.save(notificationDTO);
    }

    private void handleLliPost(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, ActionForward actionForward) throws Exception {

        switch (commonRequestDTO.getRequestTypeID()) {
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_UPGRADE.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.CLIENT_CANCEL_APPLICATION:

            case LliRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_REJECT_APPLICATION:
            case LliRequestTypeConstants.REQUEST_POP_CHANGE.CLIENT_CANCEL_APPLICATION: {
                commonService.cancelOrReject(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_DISABLE_CLIENT.SYSTEM_DISABLE_CLIENT:
//			case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_DISABLE_LINK:
            {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, false, true);
                break;
            }

//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_DEMAND_NOTE_PAYMENT:
//			{
//				commonService.processSkipBillPaymentRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
//			}

            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_CONNECTION_OWNERSHIP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE: {
                //((LliLinkService)ServiceDAOFactory.getService(LliLinkService.class)).processInternalFrResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR: {
                //((LliLinkService)ServiceDAOFactory.getService(LliLinkService.class)).processInternalFrResponseIPAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				lliService.processInternalFrResponseIPAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_APPROVE_APPLICATION: {
                commonService.approveClient(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
//			{
//				commonService.processExternalFRResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
//			{
//				commonService.processExternalFRResponse(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END:
//			{
//				commonService.processExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END:
//			{
//				commonService.processExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END:
//			{
//				commonService.acceptExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_NEAR_END:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_FAR_END:
//			{
//				commonService.acceptExternalFRRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_CANCEL_REQUEST:
//			{
//				commonService.rollbackRequest(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
//				break;
//			}
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
            case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE:
            case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE:
            case LliRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE: {
                //commonService.generateAdviceNoteLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC:
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_GENERATE_DEMAND_NOTE: {
                ((CommonService) ServiceDAOFactory.getService(CommonService.class)).generateDemandNote(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_GENERATE_DEMAND_NOTE: {
                //commonService.generateDemandNoteForIpAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
                break;
            }
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_INFORM_SETUP_DONE: {
                //commonService.processSetupDoneLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
                break;
            }
			/*case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MRC:
			{
				commonService.updateLinkBalanceLLI( commonRequestDTO );
				commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
				break;
			}*/
            case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_START_SERVICE: {
                //commonService.linkCreationCompleteLLI(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;
            }
			/*case VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_INFORM_SETUP_DONE:
			case VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_INFORM_SETUP_DONE
				commonService.processBandWidthChangeSetupDoneLLI( mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO );
				break;
			}*/
            case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_START_SERVICE:
            case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_START_SERVICE:
                //commonService.bandwidthChangeCompleteLli(mapping, commonRequestDTO, sourceRequestDTO, request, response,loginDTO);
                break;

            default: {
                commonService.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO);
            }
        }
    }


}