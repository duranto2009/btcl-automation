package vpn.client;

import coLocation.ColocationRequestTypeConstants;
import coLocation.ColocationStateConstants;
import common.*;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import dnshosting.constants.DnshostingRequestTypeConstants;
import dnshosting.constants.DnshostingStateConstants;
import file.FileDTO;
import file.FileService;
import global.GlobalService;
import iig.constants.IigRequestTypeConstants;
import iig.constants.IigStateConstants;
import login.LoginDTO;
import nix.constants.NixRequestTypeConstants;
import nix.constants.NixStateConstants;
import notification.NotificationDTO;
import notification.NotificationService;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import permission.PermissionService;
import request.*;
import requestMapping.Service;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import util.ActivityLogDAO;
import util.ServiceDAOFactory;
import util.Validator;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import webHosting.constants.WebHostingRequestTypeConstants;
import webHosting.constants.WebHostingStateConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientUpdateService {

	Logger logger = Logger.getLogger(getClass());
	ClientDAO clientDAO = new ClientDAO();
	ActivityLogDAO activityLogDAO = new ActivityLogDAO();
	RequestDAO requestDAO = new RequestDAO();
	RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
	client.ClientService clientService = ServiceDAOFactory.getService(client.ClientService.class);

	@Service private GlobalService globalService;


	FileService fileDAO = new FileService();
	private int moduleID = 0;

	public ClientDTO clientUpdateService(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception {
		LoginDTO loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		ClientForm vpnClientform = (ClientForm) p_form;
		ClientDetailsDTO clientDetailsDTO = vpnClientform.getClientDetailsDTO();
		
		moduleID=clientDetailsDTO.getModuleID();
		ClientDTO clientDTO = new ClientDTO();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean successful = false;
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
		
			ClientDTO existingClientDTO = AllClientRepository.getInstance().getClientByClientID(clientDetailsDTO.getClientID());
			ClientDetailsDTO existingClientDetailsDTO = AllClientRepository.getInstance()
					.getVpnClientByClientID(clientDetailsDTO.getClientID(), moduleID);
			List<ClientContactDetailsDTO> vpnContactDetails = new ClientDAO().getVpnContactDetailsListByClientID(existingClientDetailsDTO.getId(), databaseConnection);
			existingClientDetailsDTO.setVpnContactDetails(vpnContactDetails);
			
			clientDetailsDTO.setId(existingClientDetailsDTO.getId());
			
			boolean isClientUpdateAllowed= ClientUpdateChecker.isClientAllowedForUpdate(existingClientDetailsDTO.getCurrentStatus());
			int currentState=StateRepository.getInstance().getStateDTOByStateID(existingClientDetailsDTO.getCurrentStatus()).getActivationStatus();
			boolean clientCanUpdateHimself=(EntityTypeConstant.STATUS_SEMI_ACTIVE==currentState || EntityTypeConstant.STATUS_NOT_ACTIVE==currentState);
			
			logger.debug("existingClientDetailsDTO " + existingClientDetailsDTO);
			logger.debug("clientDetailsDTO " + clientDetailsDTO);
			logger.debug("existingClientDetailsDTO.getVpnContactDetails()"+ existingClientDetailsDTO.getVpnContactDetails());
			logger.debug("isClientUpdateAllowed " + isClientUpdateAllowed);
			{
				clientDTO.setLoginName(existingClientDTO.getLoginName());
				clientDTO.setLoginPassword(existingClientDTO.getLoginPassword());
				/*
				String encryptedPassword = PasswordService.getInstance().encrypt(clientDetailsDTO.getLoginPassword());
				if (!existingClientDTO.getLoginPassword().equals(clientDetailsDTO.getLoginPassword())) {
					clientDTO.setLoginPassword(encryptedPassword);
				} else {
					clientDTO.setLoginPassword(existingClientDTO.getLoginPassword());
				}
				*/
				clientDTO.setBalance(0.0);
				clientDTO.setDeleted(false);
				clientDTO.setLastModificationTime(System.currentTimeMillis());
				clientDTO.setClientID(clientDetailsDTO.getClientID());
				clientDTO.setCorporate(clientDetailsDTO.isCorporate());
				logger.debug(clientDTO);
				clientDAO.updateNewClient(clientDTO, databaseConnection);
			}
			
			clientDetailsDTO.setLoginName(existingClientDTO.getLoginName());
			clientDetailsDTO.setDeleted(false);
			clientDetailsDTO.setLastModificationTime(System.currentTimeMillis());
			clientDetailsDTO.setLatestStatus(existingClientDetailsDTO.getLatestStatus());
			clientDetailsDTO.setCurrentStatus(existingClientDetailsDTO.getCurrentStatus());
			logger.debug(" existingClientDetailsDTO.size :"+existingClientDetailsDTO.getVpnContactDetails().size());
			
			clientDetailsDTO.setIdentity(clientService.generateIdentityFromClientAddRequest(p_request.getParameterMap()));
			
			if(loginDTO.getIsAdmin() ||isClientUpdateAllowed ){
				clientDetailsDTO.setClientCategoryType(vpnClientform.getClientDetailsDTO().getClientCategoryType());
				//Setting Registrant Categories
				if(clientDetailsDTO.getClientCategoryType() == 2) {
					String[] registrantCategories = p_request.getParameterValues("clientDetailsDTO.regiCategories");
					long registrantCategory = Long.parseLong(registrantCategories[0]);	
					clientDetailsDTO.setRegistrantCategory(registrantCategory);
				}
				//Setting Document String Array
				if(p_request.getParameter("documents") != null  && p_request.getParameterValues("documents").length > 0) {
					vpnClientform.setDocuments(p_request.getParameterValues("documents"));
				}
			}else{
				//restrict from modifying by client
				ClientContactDetailsDTO oldVpnRegistrantContactDTO =existingClientDetailsDTO.getVpnContactDetails().get(ClientContactDetailsDTO.REGISTRANT_CONTACT);
				ClientContactDetailsDTO newVpnRegistrantContactDTO =vpnClientform.getRegistrantContactDetails();
				
				logger.debug(oldVpnRegistrantContactDTO.toString() + " "+ newVpnRegistrantContactDTO.toString() );
				
				oldVpnRegistrantContactDTO.setRegistrantsName(newVpnRegistrantContactDTO.getRegistrantsName());
				oldVpnRegistrantContactDTO.setRegistrantsLastName(newVpnRegistrantContactDTO.getRegistrantsLastName());
				oldVpnRegistrantContactDTO.setWebAddress(newVpnRegistrantContactDTO.getWebAddress());
				
				clientDetailsDTO.setRegistrantType(existingClientDetailsDTO.getRegistrantType());
				clientDetailsDTO.setClientCategoryType(existingClientDetailsDTO.getClientCategoryType());
				clientDetailsDTO.setRegistrantCategory(existingClientDetailsDTO.getRegistrantCategory());
			}
			int CORRECTION_REQUEST = (moduleID * ModuleConstants.MULTIPLIER) + 5004;
			int UPDATE_REQUEST = (moduleID * ModuleConstants.MULTIPLIER) + 5002;
			int HALF_REGISTRATION_REQUEST =  (moduleID * ModuleConstants.MULTIPLIER) + 5009;
			
			if(clientDetailsDTO.getLatestStatus() == CORRECTION_REQUEST)
			{
				int nextState = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(UPDATE_REQUEST).getNextStateID();
				clientDetailsDTO.setLatestStatus(nextState);
				clientDetailsDTO.setCurrentStatus(nextState);
			}
			else if(clientDetailsDTO.getLatestStatus() == HALF_REGISTRATION_REQUEST)
			{
				clientDetailsDTO.setLatestStatus((moduleID * ModuleConstants.MULTIPLIER) + 5001);
				clientDetailsDTO.setCurrentStatus((moduleID * ModuleConstants.MULTIPLIER) + 5001);
			}
			else
			{
				//status will remain unchanged
			}
									
			
			clientDAO.updateNewVpnClient(clientDetailsDTO, databaseConnection);

			ArrayList<ClientContactDetailsDTO> contactDetailsDTOs = new ArrayList<ClientContactDetailsDTO>();
			contactDetailsDTOs.add(vpnClientform.getRegistrantContactDetails());
			contactDetailsDTOs.add(vpnClientform.getBillingContactDetails());
			contactDetailsDTOs.add(vpnClientform.getAdminContactDetails());
			contactDetailsDTOs.add(vpnClientform.getTechnicalContactDetails());

			for (int index = 0; index < 4; index++) {
				ClientContactDetailsDTO contactDetailsDTO = contactDetailsDTOs.get(index);
				contactDetailsDTO.setVpnClientID(clientDetailsDTO.getId());
				contactDetailsDTO.setDeleted(false);
				contactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
				if(!contactDetailsDTO.getEmail().equals(existingClientDetailsDTO.getVpnContactDetails().get(index).getEmail())){
					contactDetailsDTO.setIsEmailVerified(ClientContactDetailsDTO.isNotVerified);//not verified or changed
				}else{
					contactDetailsDTO.setIsEmailVerified(existingClientDetailsDTO.getVpnContactDetails().get(index).getIsEmailVerified());
				}
				if(!contactDetailsDTO.getPhoneNumber().equals(existingClientDetailsDTO.getVpnContactDetails().get(index).getPhoneNumber())){
					contactDetailsDTO.setIsPhoneNumberVerified(ClientContactDetailsDTO.isNotVerified);//not verified or changed
				}else{
					contactDetailsDTO.setIsPhoneNumberVerified(existingClientDetailsDTO.getVpnContactDetails().get(index).getIsPhoneNumberVerified());
				}
				clientDAO.updateClientContactDetailsDTO(contactDetailsDTO, databaseConnection);
			}
			if (vpnClientform.getDocuments() == null) {
				// Upload logic for document
				String []documents= new String[]{};
				vpnClientform.setDocuments(documents);
			}
			if(loginDTO.getIsAdmin() ||isClientUpdateAllowed ){
				uploadDocuments(vpnClientform, loginDTO, p_request, databaseConnection);
			}
			
			commonRequestDTO = handleClientUpdateRequest(clientDetailsDTO, existingClientDetailsDTO, loginDTO, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			successful = true;
			
		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug(ex2);
			}
			logger.debug("Exception ", ex);
			throw ex;
		} finally {
			databaseConnection.dbClose();
		}
		if(successful){
			// do common tasks
			AllClientRepository.getInstance().reloadClientRepository(false);
			new NotificationService().sendNotification(commonRequestDTO);
//			sendVerificationaMail(vpnClientform, p_request, p_response);
		}

		manageNotifications(clientDetailsDTO,clientDTO);


		return clientDTO;
	}

	private void manageNotifications(ClientDetailsDTO clientDetailsDTO, ClientDTO clientDTO) throws Exception {
		Set<UserDTO> userDTOS = UserRepository.getInstance().getUsersByRoleID(RoleConstants.ADMIN_ROLE);
		for (UserDTO userDTO : userDTOS) {

			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setGenerationTime(System.currentTimeMillis());
			notificationDTO.setModuleId(clientDetailsDTO.getModuleID());
			notificationDTO.setEntityType("New Client Registration");
			notificationDTO.setRoleOrAccountId(RoleConstants.ADMIN_ROLE);
			notificationDTO.setUserId(userDTO.getUserID());
			notificationDTO.setDescription("Client Registration/Modification request from " + clientDTO.getLoginName());

			String actionURL =  "/GetClientForView.do?moduleID=" + clientDetailsDTO.getModuleID() + "&entityID="+
					clientDTO.getClientID();

			notificationDTO.setActionURL(actionURL);
			globalService.save(notificationDTO);
		}
	}
	
	
	public CommonRequestDTO handleClientUpdateRequest(ClientDetailsDTO clientDetailsDTO, ClientDetailsDTO existingClientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		
		logger.debug("handleClientUpdateRequest");
		int moduleID = clientDetailsDTO.getModuleID();
				
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();

		StateDTO latestStateDTO = StateRepository.getInstance().getStateDTOByStateID(existingClientDetailsDTO.getLatestStatus());
		StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(existingClientDetailsDTO.getCurrentStatus());
		boolean latestStatusActive = (latestStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);		
		boolean currentStatusActive = (currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);
//		boolean inAFlow = (!currentStatusActive || (currentStatusActive && !latestStatusActive));
		boolean inAFlow = !(currentStatusActive && latestStatusActive);
		int CORRECTION_REQUEST = (moduleID * ModuleConstants.MULTIPLIER) + 5004;
		int UPDATE = (moduleID * ModuleConstants.MULTIPLIER) + 5002;
		int HALF_REGISTRATION_REQUEST =  (moduleID * ModuleConstants.MULTIPLIER) + 5009;
		int CORRECTION_ON_REQUEST = (moduleID * ModuleConstants.MULTIPLIER) + 5006;
		
		logger.debug("inAFlow " + inAFlow);
		logger.debug("latestStateDTO " + latestStateDTO);
		logger.debug("moduleID " + moduleID);
		logger.debug("CORRECTION_REQUEST " + CORRECTION_REQUEST);
		if(inAFlow)
		{
			commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
			commonRequestDTO.setEntityTypeID(clientDetailsDTO.getModuleID() * 100 + 51);
			Set<CommonRequestDTO> bottomSet = requestUtilDAO.getBottomRequestDTOsByEntity(commonRequestDTO, databaseConnection);
			commonRequestDTO = bottomSet.iterator().next();
			commonRequestDTO.setRootReqID(commonRequestDTO.getRootReqID() == null ? commonRequestDTO.getReqID() : commonRequestDTO.getRootReqID());
			requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
			
			commonRequestDTO.setRequestToAccountID(null);
			commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID() > 0 ? loginDTO.getAccountID() : -loginDTO.getUserID());
			commonRequestDTO.setSourceRequestID(""+commonRequestDTO.getReqID());
			
			commonRequestDTO.setRequestTypeID(UPDATE);
			commonRequestDTO.setDescription("Client profile updated.");
			if(latestStateDTO.getId() == CORRECTION_REQUEST)
			{
				commonRequestDTO.setRequestTypeID(CORRECTION_ON_REQUEST);
				commonRequestDTO.setDescription("Client profile corrected.");
			}
			int nextState = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getNextStateID();
			commonRequestDTO.setExpireTime(System.currentTimeMillis() +  StateRepository.getInstance().getStateDTOByStateID(nextState).getDurationInMillis());
			
			
		}
		else
		{
			commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);			
		}

		logger.debug("commonRequestDTO " + commonRequestDTO);
		
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;				
		
	}
	
	public CommonRequestDTO handleWebHostingClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == WebHostingStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(WebHostingStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(WebHostingStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.WEBHOSTING_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(WebHostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.WEBHOSTING_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleIpaddressClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
//		if (clientDetailsDTO.getLatestStatus() == IpaddressStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
//			clientDetailsDTO.setLatestStatus(IpaddressStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//			clientDetailsDTO.setCurrentStatus(IpaddressStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.IPADDRESS_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
//		commonRequestDTO.setRequestTypeID(IpaddressRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.IPADDRESS_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleColocationClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == ColocationStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(ColocationStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(ColocationStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.COLOCATION_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(ColocationRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.COLOCATION_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleIigClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == IigStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(IigStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(IigStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.IIG_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(IigRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.IIG_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleVPNClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
//		if (clientDetailsDTO.getLatestStatus() == VpnStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
//			clientDetailsDTO.setLatestStatus(VpnStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//			clientDetailsDTO.setCurrentStatus(VpnStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.VPN_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		// requestUtilService.updateRequestByRequestID(bottomSet.iterator().next().getReqID());
		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
//		commonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		// clientService.addClientRequest(commonRequestDTO, loginDTO);
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		// new CommonService().inputStatusHistory(commonRequestDTO, loginDTO);
		return commonRequestDTO;
	}
	
	/*public CommonRequestDTO handleLliClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == LliStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(LliStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(LliStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.LLI_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.LLI_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}*/
	
	public CommonRequestDTO handleAdslClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
//		if (clientDetailsDTO.getLatestStatus() == AdslStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
//			clientDetailsDTO.setLatestStatus(AdslStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//			clientDetailsDTO.setCurrentStatus(AdslStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
//		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.ADSL_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
//		commonRequestDTO.setRequestTypeID(AdslRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.ADSL_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleNixClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == NixStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(NixStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(NixStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.NIX_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(NixRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.NIX_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}
	
	public CommonRequestDTO handleDnshostingClientUpdate(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO,
			DatabaseConnection databaseConnection) throws Exception {
		if (clientDetailsDTO.getLatestStatus() == DnshostingStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {
			clientDetailsDTO.setLatestStatus(DnshostingStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
			clientDetailsDTO.setCurrentStatus(DnshostingStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPLIED_FOR_VERIFICATION);
		}
		long entityID = clientDetailsDTO.getClientID();
		int entityTypeID = EntityTypeConstant.DNSHOSTING_CLIENT;

		CommonService commonService = new CommonService();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		Set<CommonRequestDTO> bottomSet = commonService.getBottomRequestDTOsByEntity(commonRequestDTO);

		requestUtilDAO.updateRequestByRequestID(bottomSet.iterator().next().getReqID(), databaseConnection);

		commonRequestDTO = createCommonRequestDTO(clientDetailsDTO, loginDTO);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setRequestTypeID(DnshostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_REAPPLY);
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.DNSHOSTING_CLIENT);
		Long rootReqID = null;
		if (bottomSet.iterator().next().getRootReqID() != null && bottomSet.iterator().next().getRootReqID() != 0) {
			rootReqID = bottomSet.iterator().next().getRootReqID();
		} else {
			rootReqID = bottomSet.iterator().next().getReqID();
		}
		commonRequestDTO.setRootReqID(rootReqID);
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);

		return commonRequestDTO;
	}

	private CommonRequestDTO createCommonRequestDTO(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO) {
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setRequestTypeID((clientDetailsDTO.getModuleID() * ModuleConstants.MULTIPLIER) + 5002);
		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
		commonRequestDTO.setEntityTypeID(clientDetailsDTO.getModuleID() * 100 + 51);
		commonRequestDTO.setRequestTime(System.currentTimeMillis());
		commonRequestDTO.setLastModificationTime(System.currentTimeMillis());
		commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
		commonRequestDTO.setCompletionStatus(RequestStatus.ALL_PROCESSED);
		if (loginDTO != null) {
			if (!loginDTO.getIsAdmin())
				commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID());
			else
				commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
		} else {
			commonRequestDTO.setRequestByAccountID(clientDetailsDTO.getClientID());
		}
		commonRequestDTO.setDescription(ModuleConstants.ModuleMap.get(clientDetailsDTO.getModuleID()) + " Profile of Client " + clientDetailsDTO.getLoginName() + " has be updated" );
		return commonRequestDTO;
	}

	/*
	 * private CommonRequestDTO createCommonRequestDTO(CommonRequestDTO
	 * commonRequestDTO,ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO) {
	 * commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_CLIENT);
	 * commonRequestDTO.setEntityID(clientDetailsDTO.getId());
	 * 
	 * commonRequestDTO.setRequestTime(currentTime);
	 * commonRequestDTO.setLastModificationTime(currentTime);
	 * commonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.
	 * REQUEST_NEW_CLIENT.SYSTEM_VERIFY_APPLICATION);
	 * commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
	 * if(!loginDTO.getIsAdmin())
	 * commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID()); else
	 * commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
	 * commonRequestDTO.setDescription(
	 * "Client verification with update done for "+clientDetailsDTO.getLoginName()
	 * +""); return commonRequestDTO; }
	 */
	private void uploadDocuments(ClientForm vpnClientform, LoginDTO loginDTO, HttpServletRequest p_request, DatabaseConnection databaseConnection) throws Exception {
		FileService fileService = new FileService();

		int moduleID = vpnClientform.getClientDetailsDTO().getModuleID();
		int entityTypeID = EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID);
		long entityID = vpnClientform.getClientDetailsDTO().getClientID();

		String []updatedFileNames= vpnClientform.getDocuments();
		ArrayList<String> newFileNames= new ArrayList<String>();
		ArrayList<String> commonFileNames= new ArrayList<String>();
		ArrayList<Long> removedDocIds=new ArrayList<Long>();
		
		ArrayList<FileDTO> existingFileDTOs=fileService.getFileByEntityTypeAndEntity(entityTypeID,entityID, databaseConnection);
		
		for(int i=0; i<updatedFileNames.length; i++){
			boolean isCommon=false;
			for(FileDTO dto: existingFileDTOs){
				if(updatedFileNames[i].trim().equals(dto.getDocLocalFileName().trim())){
					commonFileNames.add(dto.getDocLocalFileName().trim());
					isCommon=true;
				}
			}
			if(!isCommon){
				newFileNames.add(updatedFileNames[i].trim());
			}
		}
	
		logger.debug(newFileNames + commonFileNames.toString()+ removedDocIds);
		
		for (int i = 0; i < newFileNames.size(); i++) {
			FileDTO fileDTO = new FileDTO();
			fileDTO.setDocOwner(loginDTO.getAccountID()>0?loginDTO.getAccountID():(-loginDTO.getUserID()));
			fileDTO.setDocEntityTypeID(entityTypeID);
			fileDTO.setDocEntityID(entityID);
			fileDTO.setLastModificationTime(System.currentTimeMillis());

			fileDTO.createLocalFileFromNames(newFileNames.get(i));
			fileService.insert(fileDTO, databaseConnection);
		}
		for(FileDTO dto: existingFileDTOs){
			if(updatedFileNames.length>0){
				if(!commonFileNames.contains(dto.getDocLocalFileName().trim())){
					removedDocIds.add(dto.getDocID());
					dto.deleteFileFromDirectory();
				}
			}else{
				removedDocIds.add(dto.getDocID());
				dto.deleteFileFromDirectory();
			}
		}
		//delete the old files from databse
		if(removedDocIds.size()>0){
			fileService.removeFileByDocIDs(removedDocIds, databaseConnection);
		}
	}
//	/**
//	 * This method sends a email verification link to specified mail address
//	 * @author Alam
//	 * @param  vpnClientform
//	 * @param request request object
//	 * @param response response object
//	 * @throws Exception
//	 */
//	private void sendVerificationaMail(ClientForm vpnClientform, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		String username = vpnClientform.getClientDetailsDTO().getLoginName();
//		String email = vpnClientform.getRegistrantContactDetails().getEmail();
//		try {
//			ClientService.sendVerificationMail(username, email, request);
//		} catch (Exception e) {
//			logger.debug("", e);
//		}
//	}

	public boolean processClientUpdatePermission(long clientID, int moduleID, LoginDTO loginDTO, HttpServletRequest p_request) throws Exception
	{
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);
		CommonActionStatusDTO commonActionStatusDTO = processClientUpdatePermission(clientDetailsDTO, loginDTO, p_request);
		
		p_request.getSession().setAttribute("actionStatusDTO", null);
		
		if(commonActionStatusDTO.getStatusCode() == CommonActionStatusDTO.ERROR_STATUS_CODE)
			return false;
		else
			return true;
	}
	public CommonActionStatusDTO processClientUpdatePermission(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO, HttpServletRequest p_request) throws Exception
	{
//		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);
		int moduleID = clientDetailsDTO.getModuleID();
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.SUCCESS_STATUS_CODE);
		StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus());
		int HALF_REGISTRATION_REQUEST =  (moduleID * ModuleConstants.MULTIPLIER) + 5009;
		if(clientDetailsDTO.getCurrentStatus() == HALF_REGISTRATION_REQUEST) {
		}
		else if(loginDTO.getUserID() > 0) 
		{
			boolean isUserPermittedToUpdateClient = false;
			if(currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE)
			{
				int UPDATE_ACTIVE_CLIENT = (moduleID * ModuleConstants.MULTIPLIER) + 5002;
				isUserPermittedToUpdateClient = loginDTO.getColumnPermission(UPDATE_ACTIVE_CLIENT);								
			}
			else
			{
				CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
				
				int entityTypeID = moduleID*100+51;
				commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
				commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
				commonRequestDTO.setEntityTypeID(entityTypeID);
				commonRequestDTO.setRequestTypeID(((entityTypeID/100) * ModuleConstants.MULTIPLIER) + 5002);
				
				isUserPermittedToUpdateClient =  new PermissionService().hasPermission(commonRequestDTO, loginDTO);
//				isUserPermittedToUpdateClient = clientUpdateService.isUserPermittedToUpdateClient(clientDetailsDTOfromForm.getClientID(), moduleID*100+51, loginDTO);				
			}
			if(!isUserPermittedToUpdateClient) {
				commonActionStatusDTO.setErrorMessage( "You do not have permission to update this client" , false, p_request );
			}
		}
		else if(loginDTO.getAccountID() > 0) {
			boolean isClientPermittedToUpdateHimself = false;
			if( clientDetailsDTO.getClientID() != loginDTO.getAccountID())	{
				commonActionStatusDTO.setErrorMessage( "You do not have permission to update this client" , false, p_request );
			}
			isClientPermittedToUpdateHimself = new ClientService().getClientIsActive(clientDetailsDTO.getClientID(), moduleID);
			if(!isClientPermittedToUpdateHimself) {
				commonActionStatusDTO.setErrorMessage( "Client is not active" , false, p_request );
			}
		}
		
		return commonActionStatusDTO;
	}
	
	/**
	 * Previously it was in ClientUpdateAction
	 */
	public void processValidity(ClientForm vpnClientform, HttpServletRequest p_request) throws Exception  {
		String validationMessage="";
		StringUtils.trim(vpnClientform.getClientDetailsDTO().getLoginName());

		if (vpnClientform.getClientDetailsDTO().getModuleID() < 1) {
			validationMessage += "Invalid Module ID<br>";
		}

		if (vpnClientform.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_INDIVIDUAL) {
			if (StringUtils.isBlank(vpnClientform.registrantContactDetails.getRegistrantsName())) {
				validationMessage += "Person Name can not be empty <br>";
			}

		} else if (vpnClientform.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_COMPANY) {
			if (StringUtils.isBlank(vpnClientform.registrantContactDetails.getRegistrantsName())) {
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
		/*
		if(Validator.isMobileUsedByOtherClient(vpnClientform.registrantContactDetails.getPhoneNumber(), vpnClientform.clientDetailsDTO.getClientID())){
			validationMessage += "Mobile : "+ vpnClientform.registrantContactDetails.getPhoneNumber()+ " is already used by another user<br>";
		}
		*/
		if (StringUtils.isBlank(vpnClientform.registrantContactDetails.getCity())) {
			validationMessage += "City is not valid <br>";
		}
		/*
		if (StringUtils.isEmpty(vpnClientform.clientDetailsDTO.getLoginPassword())) {
			validationMessage += "Password can't be empty <br>";
		}
		if (!vpnClientform.clientDetailsDTO.getLoginPassword().equals(p_request.getParameter("confirmLoginPassword"))) {
			validationMessage += "Password and confirm password does not match <br>";
		}
		*/
		if (!Validator.isValidMobile(vpnClientform.registrantContactDetails.getPhoneNumber())) {
			validationMessage += "Mobile is not valid <br>";
		}
		if(validationMessage.length() > 0) {
			throw new RequestFailureException(validationMessage);
		}
	}


}