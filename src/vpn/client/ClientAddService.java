package vpn.client;

import client.temporaryClient.TemporaryClientService;
import coLocation.ColocationRequestTypeConstants;
import common.*;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import dnshosting.constants.DnshostingRequestTypeConstants;
import file.FileDTO;
import file.FileService;
import file.FileTypeConstants;
import global.GlobalService;
import iig.constants.IigRequestTypeConstants;
import lli.constants.LliRequestTypeConstants;
import login.LoginDTO;
import nix.constants.NixRequestTypeConstants;
import nl.captcha.Captcha;
import notification.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import request.CommonRequestDTO;
import request.CommonRequestStatusDTO;
import request.RequestActionStateRepository;
import request.RequestDAO;
import requestMapping.Service;
import sessionmanager.SessionConstants;
import util.ActivityLogDAO;
import util.PasswordService;
import util.Validator;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.constants.VpnRequestTypeConstants;
import webHosting.constants.WebHostingRequestTypeConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//import domain.constants.DomainRequestTypeConstants;

public class ClientAddService {
    Logger logger = Logger.getLogger(getClass());
    ClientDAO vpnDAO = new ClientDAO();
    ActivityLogDAO activityLogDAO = new ActivityLogDAO();
    RequestDAO requestDAO = new RequestDAO();
    public int moduleID = 0;

    @Service
    private TemporaryClientService temporaryClientService;

    @Service private GlobalService globalService;


    public ClientDTO clientAddService(ActionMapping p_mapping,
                                      ActionForm p_form,
                                      HttpServletRequest p_request,
                                      HttpServletResponse p_response) throws Exception {


        ClientForm vpnClientform = (ClientForm) p_form;
        //remove client from temporary table
        temporaryClientService.deleteByEmailIdIfExists(vpnClientform.registrantContactDetails.getEmail());

        LoginDTO loginDTO = (LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        ClientDetailsDTO clientDetailsDTO = vpnClientform.getClientDetailsDTO();

        ClientDTO clientDTO = new ClientDTO();
        CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        boolean successful = false;


        Boolean isExistingClientRegisteringNewModule = (clientDetailsDTO.getExistingClientID() != -1) ? true : false;

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            if (!isExistingClientRegisteringNewModule) {
                clientDTO.setLoginName(clientDetailsDTO.getLoginName());
                clientDTO.setLoginPassword(PasswordService.getInstance().encrypt(clientDetailsDTO.getLoginPassword()));
                clientDTO.setBalance(0.0);
                clientDTO.setDeleted(false);
                clientDTO.setDocuments(vpnClientform.getDocuments());
                clientDTO.setLastModificationTime(System.currentTimeMillis());
                clientDTO.setProfilePicturePath(FileTypeConstants.DEFAULT_PROFILE_PIC);
                clientDTO.setCorporate(clientDetailsDTO.isCorporate());
                vpnDAO.addNewClient(clientDTO, databaseConnection);

                clientDetailsDTO.setClientID(clientDTO.getClientID());
            } else {
                long existingClientID = clientDetailsDTO.getExistingClientID();
                clientDetailsDTO.setClientID(existingClientID);
                clientDTO = AllClientRepository.getInstance().getClientByClientID(existingClientID);
                clientDetailsDTO.setLoginName(clientDTO.getLoginName());
            }


            clientDetailsDTO.setClientCategoryType(vpnClientform.getClientDetailsDTO().getClientCategoryType());
            clientDetailsDTO.setRegistrantType(vpnClientform.getClientDetailsDTO().getRegistrantType());
            clientDetailsDTO.setRegSubCategory(vpnClientform.getClientDetailsDTO().getRegSubCategory());
            clientDetailsDTO.setDeleted(false);
            clientDetailsDTO.setLastModificationTime(System.currentTimeMillis());
            clientDetailsDTO.setBtrcLicenseDate(vpnClientform.getClientDetailsDTO().getBtrcLicenseDate());

            //Setting Registrant Categories
            if (clientDetailsDTO.getClientCategoryType() == 2) {
                String[] registrantCategories = p_request.getParameterValues("clientDetailsDTO.regiCategories");
                long registrantCategory = Long.parseLong(registrantCategories[0]);
                clientDetailsDTO.setRegistrantCategory(registrantCategory);
            }

            //Setting Document String Array
            if (p_request.getParameter("documents") != null && p_request.getParameterValues("documents").length > 0) {
                vpnClientform.setDocuments(p_request.getParameterValues("documents"));
            }

            commonRequestDTO = handleModuleSpecificClientAdd(clientDetailsDTO, p_request, loginDTO, databaseConnection);
            if (vpnClientform.getDocument() != null) {
                uploadDocument(vpnClientform, clientDetailsDTO.getModuleID(), loginDTO, p_request, databaseConnection);
            }


            logger.debug(vpnClientform.getRegistrantContactDetails());

            ArrayList<ClientContactDetailsDTO> clientContactDetailsDTOs = new ArrayList<ClientContactDetailsDTO>();

            clientContactDetailsDTOs.add(vpnClientform.getRegistrantContactDetails());
            clientContactDetailsDTOs.add(vpnClientform.getBillingContactDetails());
            clientContactDetailsDTOs.add(vpnClientform.getAdminContactDetails());
            clientContactDetailsDTOs.add(vpnClientform.getTechnicalContactDetails());

            for (int index = 0; index < 4; index++) {
                ClientContactDetailsDTO clientContactDetailsDTO = clientContactDetailsDTOs.get(index);
                clientContactDetailsDTO.setDeleted(false);
                clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
                clientContactDetailsDTO.setVpnClientID(clientDetailsDTO.getId());
                vpnDAO.addNewClientContactDetailsDTO(clientContactDetailsDTO, databaseConnection);
            }

            if (vpnClientform.getDocuments() != null && vpnClientform.getDocuments().length > 0) {
                clientDetailsDTO.setClientID(clientDTO.getClientID());
                vpnClientform.setClientDetailsDTO(clientDetailsDTO);
                uploadDocuments(vpnClientform, loginDTO, p_request, databaseConnection);
            }

            //new CommonDAO().inputStatusHistory(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
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
        if (successful) {
            AllClientRepository.getInstance().reload(false);
            new NotificationService().sendNotification(commonRequestDTO);

            sendVerificationaMail(vpnClientform, p_request, p_response);
        }

        return clientDTO;
    }

    public CommonActionStatusDTO processValidation(ClientForm vpnClientform,
                                                   HttpServletRequest p_request,
                                                   boolean isExistingClientRegisteringNewModule,
                                                   LoginDTO loginDTO) throws Exception {
        String validationMessage = "";
        String username = StringUtils.trimToEmpty(vpnClientform.getClientDetailsDTO().getLoginName());


        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        Captcha captcha = (Captcha) p_request.getSession().getAttribute(Captcha.NAME);

        p_request.setCharacterEncoding("UTF-8");

        String answer = p_request.getParameter("answer");

        if (loginDTO == null && !captcha.isCorrect(answer)) {
            validationMessage = "Capcha doest not match<br>" + validationMessage;
            if (validationMessage.length() > 0) {
                commonActionStatusDTO.setErrorMessage(validationMessage, false, p_request);
            }
            return commonActionStatusDTO;
        }


        if (!isExistingClientRegisteringNewModule) {
            if (username.length() == 0 || username.contains(" ")) {
                validationMessage += "Whitespace is not allowed in username <br>";
            }
            if (Validator.isUsedUserName(username)) {
                validationMessage += "Username is already taken. Please try a different username <br>";
            }
            if (StringUtils.isEmpty(vpnClientform.clientDetailsDTO.getLoginName())) {
                validationMessage += "User Name is not valid <br>";
            }

            if (StringUtils.isEmpty(vpnClientform.clientDetailsDTO.getLoginPassword())) {
                validationMessage += "Password can't be empty <br>";
            }
            if (!vpnClientform.clientDetailsDTO.getLoginPassword().equals(p_request.getParameter("confirmLoginPassword"))) {
                validationMessage += "Password and confirm password does not match <br>";
            }
            if (Validator.isEmailUsed(vpnClientform.registrantContactDetails.getEmail(), 0)) {
                validationMessage += "Email : " + vpnClientform.registrantContactDetails.getEmail() + " is already used by another user<br>";
            }
			/*
			if(Validator.isMobileUsed(vpnClientform.registrantContactDetails.getPhoneNumber())){
				validationMessage += "Mobile : "+ vpnClientform.registrantContactDetails.getPhoneNumber()+ " is already used by another user<br>";
			}
			*/
        }

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
                validationMessage += "Web address is not valid <br>";
            }
        }
        if (!Validator.isValidEmailAddress(vpnClientform.registrantContactDetails.getEmail())) {
            validationMessage += "Email is not valid <br>";
        }
        if (vpnClientform.registrantContactDetails.getIsPhoneNumberVerified() == 0) {
            if (!Validator.isValidMobile(vpnClientform.registrantContactDetails.getPhoneNumber())) {
                validationMessage += "Mobile is not valid <br>";
            }
        }

        if (StringUtils.isEmpty(vpnClientform.registrantContactDetails.getCity())) {
            validationMessage += "City is not valid <br>";
        }

        if (vpnClientform.clientDetailsDTO.getModuleID() < 1) {
            validationMessage += "Invalid Module ID<br>";
        }

        if (validationMessage.length() > 0) {
            commonActionStatusDTO.setErrorMessage(validationMessage, false, p_request);
        }
        p_request.getSession().removeAttribute("actionStatusDTO");
        return commonActionStatusDTO;

    }

    public Boolean hasPermission(LoginDTO loginDTO, ClientDetailsDTO clientDetailsDTO) throws Exception {
        boolean hasPermission = false;
        if (loginDTO == null) {
            hasPermission = true;
        } else if (loginDTO.getUserID() > 0) {
            CommonRequestDTO commonRequestDTO = new CommonRequestDTO();

            int entityTypeID = clientDetailsDTO.getModuleID() * 100 + 51;
            commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
            commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
            commonRequestDTO.setEntityTypeID(entityTypeID);
            commonRequestDTO.setRequestTypeID(((entityTypeID / 100) * ModuleConstants.MULTIPLIER) + 5001);

            Set<Integer> actionSet = loginDTO.getActionPermission(-clientDetailsDTO.getModuleID()
                    * ModuleConstants.MULTIPLIER);
            hasPermission = actionSet.contains(commonRequestDTO.getRequestTypeID());
        } else if (loginDTO.getAccountID() > 0) {
            List<HashMap<String, String>> modulesRegisteredByClient = AllClientRepository
                    .getInstance().getModuleListByClientID(clientDetailsDTO.getClientID());
            hasPermission = true;
            for (HashMap<String, String> module : modulesRegisteredByClient) {
                if (module.containsKey(clientDetailsDTO.getModuleID() + "")) {
                    hasPermission = false;
                    break;
                }
            }
        }
        return hasPermission;
    }

    public CommonRequestDTO handleModuleSpecificClientAdd(ClientDetailsDTO clientDetailsDTO,
                                                          HttpServletRequest request,
                                                          LoginDTO loginDTO,
                                                          DatabaseConnection databaseConnection) throws Exception {
        switch (clientDetailsDTO.getModuleID()) {
            case ModuleConstants.Module_ID_DOMAIN: {
//				return handleDomainClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
                return null;
            }
            case ModuleConstants.Module_ID_WEBHOSTING: {
                return handleWebHostingClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
//			case ModuleConstants.Module_ID_IPADDRESS: {
//				return handleIpaddressClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
//			}
            case ModuleConstants.Module_ID_COLOCATION: {
                return handleColocationClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_IIG: {
                return handleIigClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_VPN: {
                return handleVpnClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_LLI: {
                return handleLliClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_ADSL: {
                return handleAdslClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_NIX: {
                return handleNixClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            case ModuleConstants.Module_ID_DNSHOSTING: {
                return handleDnshostingClientAdd(clientDetailsDTO, request, loginDTO, databaseConnection);
            }
            default: {
                throw new RequestFailureException("Invalid Module ID selected");
            }
        }

    }

//	public CommonRequestDTO handleDomainClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception
//	{
//		int status = 0;
//		int requestType = 0;
//		if(loginDTO == null)
//		{
//			requestType = DomainRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
//		}
//		else
//		{
//			requestType = DomainRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
//		}
//		status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
//		clientDetailsDTO.setLatestStatus(status);
//		clientDetailsDTO.setCurrentStatus(status);
//		logger.debug("clientDetailsDTO " + clientDetailsDTO);
//		vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);
//
//
//
//		CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
//		commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
//		commonRequestDTO.setRequestTypeID(requestType);
//		commonRequestDTO.setEntityTypeID(EntityTypeConstant.DOMAIN_CLIENT);
//		commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
//		LoginDTO loginDTO2 = loginDTO;
//		if(loginDTO2 == null)
//		{
//			loginDTO2 = new LoginDTO();
//			loginDTO2.setAccountID(clientDetailsDTO.getClientID());
//			loginDTO2.setLoginSourceIP(request.getRemoteAddr());
//		}
//		requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);
//
//		//new CommonService().inputStatusHistory(commonRequestDTO, loginDTO);
//		/*CommonDAO commonDAO = new CommonDAO();
//		commonDAO.inputStatusHistory(commonRequestDTO, loginDTO,  EntityTypeConstant.STATUS_LATEST, databaseConnection);*/
//		return commonRequestDTO;
//	}

    public CommonRequestDTO handleWebHostingClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = WebHostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = WebHostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }
        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.WEBHOSTING_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        //new CommonService().inputStatusHistory(commonRequestDTO, loginDTO);
		/*CommonDAO commonDAO = new CommonDAO();
		commonDAO.inputStatusHistory(commonRequestDTO, loginDTO,  EntityTypeConstant.STATUS_LATEST, databaseConnection);*/
        return commonRequestDTO;
    }

    /*
    public CommonRequestDTO handleIpaddressClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception
    {
        int status = 0;
        int requestType = 0;

        //Class classObject =RequestTypeConstants.RequestTypeMapByModuleID.get(clientDetailsDTO.getModuleID());
        //Field[] fields = classObject.getDeclaredFields();

        if(loginDTO == null)
        {
            requestType = IpaddressRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        }
        else
        {
            requestType = IpaddressRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }
        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);

        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.IPADDRESS_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if(loginDTO2 == null)
        {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }
    */
    public CommonRequestDTO handleColocationClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = ColocationRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = ColocationRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.COLOCATION_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleIigClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = IigRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = IigRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.IIG_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleVpnClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = VpnRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = VpnRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);

        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleLliClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.LLI_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleAdslClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
//		if(loginDTO == null)
//		{
//			requestType = AdslRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
//		}
//		else
//		{
//			requestType = AdslRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
//		}

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.ADSL_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleNixClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = NixRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = NixRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.NIX_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    public CommonRequestDTO handleDnshostingClientAdd(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
        int status = 0;
        int requestType = 0;
        if (loginDTO == null) {
            requestType = DnshostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY_REGISTRANTS_ONLY;
        } else {
            requestType = DnshostingRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_APPLY;
        }

        status = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setCurrentStatus(status);
        logger.debug("clientDetailsDTO " + clientDetailsDTO);
        vpnDAO.addNewVpnClient(clientDetailsDTO, databaseConnection);


        CommonRequestDTO commonRequestDTO = prepareBasicClientRequestDTO(clientDetailsDTO, request, loginDTO);
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTypeID(requestType);
        commonRequestDTO.setEntityTypeID(EntityTypeConstant.DNSHOSTING_CLIENT);
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        LoginDTO loginDTO2 = loginDTO;
        if (loginDTO2 == null) {
            loginDTO2 = new LoginDTO();
            loginDTO2.setAccountID(clientDetailsDTO.getClientID());
            loginDTO2.setLoginSourceIP(request.getRemoteAddr());
        }
        requestDAO.addRequest(commonRequestDTO, loginDTO2.getLoginSourceIP(), databaseConnection);

        return commonRequestDTO;
    }

    private CommonRequestStatusDTO createRequestStatusDTO(CommonRequestStatusDTO commonRequestStatusDTO, CommonRequestDTO commonRequestDTO, ClientDetailsDTO clientDetailsDTO) {
        commonRequestStatusDTO.setRequestID(commonRequestDTO.getReqID());
        commonRequestStatusDTO.setRequestTypeID(commonRequestDTO.getRequestTypeID());
        commonRequestStatusDTO.setClientID(commonRequestDTO.getClientID());
        commonRequestStatusDTO.setEntityTypeID(commonRequestDTO.getEntityTypeID());
        commonRequestStatusDTO.setEntityID(commonRequestDTO.getEntityID());
        commonRequestStatusDTO.setTime(System.currentTimeMillis());

        if (RequestActionStateRepository.getInstance()
                .getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()) != null)
            commonRequestStatusDTO.setState(RequestActionStateRepository.getInstance()
                    .getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getNextStateID());
        return commonRequestStatusDTO;
    }

    private CommonRequestDTO prepareBasicClientRequestDTO(ClientDetailsDTO clientDetailsDTO, HttpServletRequest request, LoginDTO loginDTO) {
        CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
        commonRequestDTO.setEntityID(clientDetailsDTO.getClientID());
        commonRequestDTO.setRequestTime(System.currentTimeMillis());
        commonRequestDTO.setLastModificationTime(System.currentTimeMillis());
        commonRequestDTO.setClientID(clientDetailsDTO.getClientID());
        if (loginDTO != null) {
            if (!loginDTO.getIsAdmin())
                commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID());
            else
                commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
        } else {
            commonRequestDTO.setRequestByAccountID(clientDetailsDTO.getClientID());
        }
        commonRequestDTO.setIP(request.getRemoteAddr());
        commonRequestDTO.setDescription("New client add request is submitted(" + clientDetailsDTO.getLoginName() + ")");
        return commonRequestDTO;
    }

    private void uploadDocuments(ClientForm vpnClientform, LoginDTO loginDTO, HttpServletRequest p_request, DatabaseConnection databaseConnection) throws Exception {
        FileService fileService = new FileService();

        int moduleID = vpnClientform.getClientDetailsDTO().getModuleID();
        int entityTypeID = EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID);
        long entityID = vpnClientform.getClientDetailsDTO().getClientID();
        for (int i = 0; i < vpnClientform.getDocuments().length; i++) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setDocOwner(loginDTO.getAccountID() > 0 ? loginDTO.getAccountID() : (-loginDTO.getUserID()));
            fileDTO.setDocEntityTypeID(entityTypeID);
            fileDTO.setDocEntityID(entityID);
            fileDTO.setLastModificationTime(System.currentTimeMillis());

            fileDTO.createLocalFileFromNames(vpnClientform.getDocuments()[i]);
            fileService.insert(fileDTO, databaseConnection);
        }

    }

    private void uploadDocument(ClientForm vpnClientform, int moduleID, LoginDTO loginDTO, HttpServletRequest p_request,
                                DatabaseConnection databaseConnection) throws Exception {
        FileService fileService = new FileService();

        int entityTypeID = EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID);
        long entityID = vpnClientform.getClientDetailsDTO().getClientID();

        FileDTO fileDTO = new FileDTO();
        fileDTO.setDocOwner(vpnClientform.getClientDetailsDTO().getClientID());
        fileDTO.setDocEntityTypeID(entityTypeID);
        fileDTO.setDocEntityID(entityID);
        fileDTO.setLastModificationTime(System.currentTimeMillis());
        fileDTO.setDocument(vpnClientform.getDocument());


        logger.debug("vpnClientform.getClientDetailsDTO().getIdentity() " + vpnClientform.getClientDetailsDTO().getIdentity());


//		boolean uploadStatus=fileDTO.createLocalFileUsingFormFile(Integer.parseInt((vpnClientform.getClientDetailsDTO().getIdentity().split(",")[0]).split(":")[0]));
        boolean uploadStatus = fileDTO.createLocalFileUsingFormFile(Integer.parseInt(vpnClientform.getClientDetailsDTO().getIdentity()));
        if (!uploadStatus) {
            new CommonActionStatusDTO().setWarningMessage("You have tried to upload an invalid file. ", false, p_request);
        } else {
            fileService.insert(fileDTO, databaseConnection);
        }

    }

    /*
     * This method sends a email verification link to specified mail address
     * @author Alam
     * @param ClientForm vpnClientform
     * @param request request object
     * @param response response object
     * @throws Exception
     */
    private void sendVerificationaMail(ClientForm vpnClientform, HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        String username = vpnClientform.getClientDetailsDTO().getLoginName();
        String email = vpnClientform.getRegistrantContactDetails().getEmail();
        String mblNo = vpnClientform.getRegistrantContactDetails().getPhoneNumber();
        // remove any country code, if given
        try {
            ClientService.sendVerificationMail(username, email, request);
			/*if (mblNo.startsWith(Country2Phone.country2phone.get("BD"))) {
				mblNo = StringUtils.replaceOnce(mblNo, Country2Phone.country2phone.get("BD"), "0");
				
				SMSSender sender = SMSSender.getInstance();
				System.out.println("Sending OTP to " + mblNo);
				sender.sendSMS(ForgetPasswordUtility.getPasswordResetToken(6), mblNo);
			} else {
				logger.debug("Dont send message or otp  to the uses starts without +880");
			}*/
        } catch (Exception e) {
            logger.debug("", e);
        }

    }
}