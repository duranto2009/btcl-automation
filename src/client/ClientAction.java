package client;

import annotation.JsonPost;
import client.classification.ClientClassificationService;
import client.temporaryClient.TemporaryClient;
import client.temporaryClient.TemporaryClientService;
import common.*;
import common.repository.AllClientRepository;
import forgetPassword.ForgetPassword;
import global.GlobalService;
import login.LoginDTO;
import nl.captcha.Captcha;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.Validator;
import vpn.client.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@ActionRequestMapping("Client")
public class ClientAction extends AnnotatedRequestMappingAction {
    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
    ClientUpdateService clientUpdateService = ServiceDAOFactory.getService(ClientUpdateService.class);
    ClientAddService clientAddService = ServiceDAOFactory.getService(ClientAddService.class);
    ClientClassificationService clientClassificationService = ServiceDAOFactory.getService(ClientClassificationService.class);

    @Service
    private TemporaryClientService temporaryClientService;

    @Service
    private GlobalService globalService;

    @Service
    private vpn.client.ClientService vpnClientService;

    @Service
    private RegistrationService registrationService;

    /**
     * @author dhrubo
     */
    @RequestMapping(mapping = "/Registration", requestMethod = RequestMethod.GET)
    public ActionForward getClientRegistration(ActionMapping mapping, HttpServletRequest request) throws Exception {

        if (request.getParameter("moduleID") != null && request.getParameter("accountType") != null) {
            return mapping.findForward("getClientRegistrationStep2");
        }

        if (request.getParameter("redirect") != null && request.getParameter("redirect").equals("1")) {
            request.setAttribute("temporaryClientId", request.getParameter("clientId"));
        }

        temporaryClientService.deleteTemporaryClientsOlderThanAnHour();
        return mapping.findForward("getClientRegistration");
    }

    /**
     * @author dhrubo
     */
    @RequestMapping(mapping = "/Registration", requestMethod = RequestMethod.POST)
    public ActionForward postClientRegistration(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ClientForm clientForm) {
        return mapping.findForward("applicationRoot");
    }

    /**
     * @author dhrubo
     */
    @RequestMapping(mapping = "/Update", requestMethod = RequestMethod.POST)
    public Map<String, String> postClientUpdate(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ClientForm clientForm, LoginDTO loginDTO) throws Exception {
        Map<String, String> successMessage = new HashMap<String, String>();

        clientUpdateService.processValidity(clientForm, request);

        ClientDetailsDTO clientDetailsDTO = clientForm.getClientDetailsDTO();
        if (!clientUpdateService.processClientUpdatePermission(clientDetailsDTO.getClientID(), clientDetailsDTO.getModuleID(), loginDTO, request)) {
            throw new RequestFailureException("You do not have permission");
        }

        try {
            clientUpdateService.clientUpdateService(mapping, clientForm, request, response);
        } catch (Exception ex) {
            ActionRedirect forward = new ActionRedirect(mapping.findForward("postClientUpdateError"));
            BtclRedirectUtility.setForwardParameters(forward, clientDetailsDTO.getModuleID(), clientDetailsDTO.getClientID());
            forward.addParameter("edit", "");
            successMessage.put("location", StringUtils.getBaseUrl(request) + forward.getPath());
            return successMessage;
        }
        AllClientRepository.getInstance().reload(false);
        new CommonActionStatusDTO().setSuccessMessage("Edited Sucessfully.", false, request);
        ActionRedirect forward = new ActionRedirect(mapping.findForward("postClientUpdateSuccess"));
        BtclRedirectUtility.setForwardParameters(forward, clientDetailsDTO.getModuleID(), clientDetailsDTO.getClientID());
        successMessage.put("location", StringUtils.getBaseUrl(request) + forward.getPath());

        request.getSession().removeAttribute("registrantContactDetails");

        return successMessage;
    }

    @RequestMapping(mapping = "/Add", requestMethod = RequestMethod.GET)
    public ActionForward getClientAdd(ActionMapping mapping, HttpServletRequest request) throws Exception {
        PermissionHandler.checkLoginDTOExistenceByRequest(request);
        if (request.getParameter("moduleID") == null && request.getParameter("accountType") == null && !clientService.isModuleIDAndAccountTypeValid(Integer.parseInt(request.getParameter("moduleID")), Integer.parseInt(request.getParameter("accountType")))) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("getClientAdd");
    }


    @RequestMapping(mapping = "/Add", requestMethod = RequestMethod.POST)
    public Map<String, String> postClientAdd(ActionMapping mapping, ClientForm clientForm, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        LoginDTO loginDTO = (LoginDTO) request.getSession(false).getAttribute(SessionConstants.USER_LOGIN);

        ClientDetailsDTO clientDetailsDTO = clientForm.getClientDetailsDTO();

        Boolean isExistingClientRegisteringNewModule = (clientDetailsDTO.getExistingClientID() != -1) ? true : false;

        if (!clientAddService.hasPermission(loginDTO, clientDetailsDTO))
            throw new RequestFailureException("You don't have permission to see this page");

        ClientDTO clientDTO = null;
        Integer moduleID = clientDetailsDTO.getModuleID();
        String target = "success";

        CommonActionStatusDTO actionDTO = new CommonActionStatusDTO();
        try {
            actionDTO = clientAddService.processValidation(clientForm, request, isExistingClientRegisteringNewModule, loginDTO);

            if (actionDTO.getStatusCode() == CommonActionStatusDTO.ERROR_STATUS_CODE) {
                throw new RequestFailureException(actionDTO.getMessage());
            }

            clientDetailsDTO.setIdentity(clientService.generateIdentityFromClientAddRequest(request.getParameterMap()));
            clientDTO = clientAddService.clientAddService(mapping, clientForm, request, response);

        } catch (Exception ex) {
            actionDTO.setErrorMessage(ex.getStackTrace().toString(), false, request);
            throw ex;
        }

        switch (clientService.getClientAddMode(clientDetailsDTO, loginDTO)) {
            case ClientConstants.ClientAddMode.Client_Add_NoAccount:
                actionDTO.setSuccessMessage("Congratulation ! Your request" +
                        " is successfully received. Please continue to complete the registration after login and" +
                        " upload necessary documents. An email verification link is sent to your mail address.",
                        false, request);
                target = "login";
                break;
            case ClientConstants.ClientAddMode.Client_Add_Account_New:
                actionDTO.setSuccessMessage("Client " + clientDTO.getLoginName() + " is created " +
                        "successfully!!!", false, request);
                target = "postClientAddSuccess";
                break;
            case ClientConstants.ClientAddMode.Client_Add_Account_Existing:
                actionDTO.setSuccessMessage("Registration of " +
                        ModuleConstants.ModuleMap.get(clientDetailsDTO.getModuleID()) +
                        " Module for Client " + clientDTO.getLoginName() + " is created successfully!!!",
                        false, request);
                target = "postClientAddSuccess";
                break;
        }

        ActionRedirect forward = new ActionRedirect(mapping.findForward(target));
        BtclRedirectUtility.setForwardParameters(forward, moduleID, clientDTO.getClientID());

        Map<String, String> successMessage = new HashMap<String, String>();
        successMessage.put("location", StringUtils.getBaseUrl(request) + forward.getPath());
        return successMessage;
    }

    @JsonPost
    @RequestMapping(mapping = "/new-client-registration-by-client", requestMethod = RequestMethod.POST)
    public boolean addNewClientAddedByClient(
            @RequestParameter(isJsonBody = true, value = "client") RegistrantInformation client,
            HttpServletRequest request,
            LoginDTO loginDTO
    ) throws Exception {
        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        boolean fromSystem = false;
        ClientDTO clientDTO =registrationService.addNewClient(client, captcha, fromSystem);
        registrationService.addRequest(clientDTO, client, fromSystem, loginDTO, request.getRemoteAddr());
        return true;
    }




    //TODO : not ready, has to be implemented
    @JsonPost
    @RequestMapping(mapping = "/new-client-registration-by-admin", requestMethod = RequestMethod.POST)
    public boolean addNewClientAddedByAdmin(
            @RequestParameter(isJsonBody = true, value = "client") RegistrantInformation client,
            HttpServletRequest request,
            LoginDTO loginDTO
    ) throws Exception {
        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        boolean fromSystem = true;
        registrationService.addNewClient(client, captcha, fromSystem);
        return true;
    }





    @RequestMapping(mapping = "/UsernameAvailability", requestMethod = RequestMethod.GET)
    public boolean isUsernameAvailable(HttpServletRequest request) throws Exception {
        return Validator.isUsedUserName(request.getParameter("username").toLowerCase());
    }

    class ClientInfo {
        long key;
        String value;
        int registrantType;

        ClientInfo(long key, String value, int registrantType) {
            this.key = key;
            this.value = value;
            this.registrantType = registrantType;
        }
    }

    @RequestMapping(mapping = "/all", requestMethod = RequestMethod.GET)
    public List<ClientInfo> getAllClientByModuleIdAndClientName(@RequestParameter("query") String partialName,
                                                   @RequestParameter("module") int moduleId
                                                 ){

        if(partialName.isEmpty())return Collections.emptyList();
        return AllClientRepository.getInstance().getClientDTOListBypartialNameAndModuleIDAndResultLimit(partialName, moduleId)
                .stream()
                .map(t-> {
                            ClientInfo clientInfo = null;
                            try {
                                clientInfo = new ClientInfo(t.getClientID(), t.getLoginName(),
                                        AllClientRepository.getInstance()
                                                .getVpnClientByClientID(t.getClientID(), moduleId).getRegistrantType());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return clientInfo;
                        }
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @RequestMapping(mapping = "/all-by-id", requestMethod = RequestMethod.GET)
    public List<ClientInfo> getAllClientByModuleIdAndClientId(@RequestParameter("query") String partialID,
                                                   @RequestParameter("module") int moduleId
                                                   ){

        return  AllClientRepository.getInstance().getClientDTOsByPartialClientID(partialID, moduleId)
                .stream()
                .map(t-> {
                            ClientInfo clientInfo = null;
                            try {
                                clientInfo = new ClientInfo(t.getClientID(), t.getLoginName(),
                                        AllClientRepository.getInstance()
                                                .getVpnClientByClientID(t.getClientID(), moduleId).getRegistrantType());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return clientInfo;
                        }
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @RequestMapping(mapping = "/get-by-id-module", requestMethod = RequestMethod.GET)
    public KeyValuePair<Long, String> getClientByIdAndModuleId(@RequestParameter("id") long clientId, @RequestParameter("module") int moduleId) {
        ClientDTO clientDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientId, moduleId);
        if (clientDTO == null) {
            //
        }
        return new KeyValuePair<>(clientId, clientDTO.getLoginName());
    }

    @JsonPost
    @RequestMapping(mapping = "/get-all-by-id-module", requestMethod = RequestMethod.POST)
    public Map<Long, String> getAllClientByClientIdAndModuleId(@RequestParameter("list") List<KeyValuePair<Long, Integer>> list) throws Exception {
        Map<Long, String> map = list.stream()
                .map(t -> {
                    ClientDTO clientDTO = AllClientRepository.getInstance().getVpnClientByClientID(t.key, t.value);
                    if (clientDTO == null) {
                        return new KeyValuePair<>(-1L, "N/A");
                    }
                    return new KeyValuePair<>(clientDTO.getClientID(), clientDTO.getLoginName());
                }).collect(Collectors.toMap(KeyValuePair::getKey, KeyValuePair::getValue));
        return map;
    }

    @JsonPost
    @RequestMapping(mapping = "/temporaryAdd", requestMethod = RequestMethod.POST)
    public long addTemporaryClient(
            @RequestParameter(isJsonBody = true, value = "email") String emailId,
            @RequestParameter(isJsonBody = true, value = "intlMobileNumber") String mobileNumber,
            @RequestParameter(isJsonBody = true, value = "countryCode") String countryCode,
            @RequestParameter(isJsonBody = true, value = "moduleId") int moduleId) throws Exception {

        long clientId = temporaryClientService.addTemporaryClient(emailId,
                mobileNumber, countryCode, moduleId, ClientForm.CLIENT_TYPE_COMPANY);

        return clientId;
    }


    @RequestMapping(mapping = "/get-verification-page", requestMethod = RequestMethod.All)
    public ActionForward getVerificationPage(ActionMapping mapping, HttpServletRequest request,
                                             @RequestParameter("clientId") long clientId) {
        request.setAttribute("clientId", clientId);
        return mapping.findForward("reg-verification-page");
    }


    @RequestMapping(mapping = "/get-reg-form", requestMethod = RequestMethod.All)
    public ActionForward getRegForm(ActionMapping mapping, HttpServletRequest request,
                                    @RequestParameter("email") String email,
                                    @RequestParameter("mobile") String mobile,
                                    @RequestParameter("tempClientId") Long tempClientId) {
        request.setAttribute("email", email);
        request.setAttribute("mobile", mobile);
        request.setAttribute("tempClientId", tempClientId);

        return mapping.findForward("get-reg-form");
    }

    /**
     * @author Touhid
     */
    @RequestMapping(mapping = "/get-client-details", requestMethod = RequestMethod.All)
    public Map<String, String> getClientDetailsByClient(@RequestParameter("clientId") long clientId,
                                                        @RequestParameter("moduleId") int moduleId) throws Exception {
        return clientService.getClientDetailsByClientIdAndModuleId(clientId, moduleId);
    }


    @JsonPost
    @RequestMapping(mapping = "/check-availability", requestMethod = RequestMethod.POST)
    public boolean checkEmailIsUsed(@RequestParameter(value = "param", isJsonBody = true) String param,
                                    @RequestParameter(value = "type", isJsonBody = true) String type)
            throws Exception {

        if (type.trim().equalsIgnoreCase("email")) {
            return vpnClientService.isEmailIdFree(param);
        } else if (type.trim().equalsIgnoreCase("mobile")) {
            return vpnClientService.isMobileNumberFree(param);
        }
        return false;
    }

    @RequestMapping(mapping = "/send-verification-mail", requestMethod = RequestMethod.GET)
    public void sendVerificationMailTemporaryClient(
            @RequestParameter("email") String email,
            @RequestParameter("clientId") Long clientId,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String username = "";
        vpn.client.ClientService.sendVerificationMailTemporaryClient(username, email, request, Long.toString(clientId));
    }

    @RequestMapping(mapping = "/send-verification-sms", requestMethod = RequestMethod.GET)
    public boolean sendVerificationSMS(@RequestParameter("phoneNo") String phoneNo,
                                       @RequestParameter("clientId") Long clientId,
                                       HttpServletRequest request, HttpServletResponse response
    ) throws Exception {

        phoneNo = phoneNo.replace("+", "").replace("-", "");
        try {
            vpn.client.ClientService.sendVerificationSMS(Long.toString(clientId), phoneNo, request);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @JsonPost
    @RequestMapping(mapping = "/verify-sms", requestMethod = RequestMethod.POST)
    public void verifySMS(@RequestParameter(isJsonBody = true, value = "clientId") long clientId,
                             @RequestParameter(isJsonBody = true, value = "token") String token,
                             @RequestParameter(isJsonBody = true, value = "mobile") String mobile) throws Exception {

        mobile = mobile.replace("+", "").replace("-", "");
        if (mobile.length() == 10) {
            mobile = "0" + mobile;
        }
        try {
            ForgetPassword forgetPassword = ForgetPassword.getByUsername(mobile);
            System.out.println(forgetPassword);
            System.out.println(forgetPassword.getToken());
            System.out.println(token);
            token = token.trim();

            if (!forgetPassword.getToken().equals(token)) {
                System.out.println("Token mile nai");
                throw new RequestFailureException("OTP does not match.");

            } else {
                if (forgetPassword.getLastModificationTime() + 120 * 1000 < System.currentTimeMillis()) {
                    throw new RequestFailureException("Token invalidated due to time out");

                } else {
                    try {
                        ClientUtility.verifyPhone(mobile);
                        vpnClientService.updateTemporaryClientMobileStateIfNeeded(clientId);
                        forgetPassword.remove();
                    } catch (Exception e) {
                        throw new ReflectiveOperationException(e.getMessage());

                    }
                }
            }
        } catch (Exception e) {
            throw new RequestFailureException(e.getMessage());
        }

    }

    @RequestMapping(mapping = "/get-temporary-client", requestMethod = RequestMethod.GET)
    public TemporaryClient getTemporaryClient(
            @RequestParameter("clientId") Long clientId) throws Exception {
        return globalService.findByPK(TemporaryClient.class, clientId);
    }


    @RequestMapping(mapping = "/get-all-countries", requestMethod = RequestMethod.GET)
    public List<KeyValuePair<String, String>> getAllCountries() {

        return Arrays.asList(Locale.getISOCountries())
                .stream()
                .map(t -> new Locale("", t))
                .map(t -> new KeyValuePair<>(t.getCountry(), t.getDisplayCountry()))
                .collect(Collectors.toList());

    }




}
