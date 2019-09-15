package client;

import annotation.Transactional;
import client.temporaryClient.TemporaryClientService;
import common.ClientDTO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import global.GlobalService;
import login.LoginDTO;
import nl.captcha.Captcha;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import request.RequestDAO;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.PasswordService;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.List;

public class RegistrationService {

    @Service
    private GlobalService globalService;

    @Service
    private TemporaryClientService temporaryClientService;

    public ClientDTO addNewClient(RegistrantInformation client, Captcha captcha, boolean fromSystem) throws Exception {

        if (!checkCaptcha(client.captchaAnswer, captcha)) {
            throw new RequestFailureException("incorrect captcha, please try again.");
        }

        if (!passwordMatchCheck(client.getLoginPassword(), client.getConfirmPassword())) {
            throw new RequestFailureException("Password mismatch, please try again.");
        }

        ClientDTO clientDTO = insertBasicClient(client);
        ClientDetailsDTO clientDetailsDTO = insertClientWithModuleSpecificInfo(client, fromSystem, clientDTO);
        insertClientContactDetails(client, clientDetailsDTO);

        temporaryClientService.deleteByEmailIdIfExists(client
                .getContactInfoList()
                .get(ClientContactDetailsDTO.REGISTRANT_CONTACT)
                .getEmail());


        AllClientRepository.getInstance().reload(false);

        return clientDTO;
    }

    private ClientDTO insertBasicClient(RegistrantInformation client) throws Exception {

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setLoginName(client.getLoginName());
        clientDTO.setLoginPassword(PasswordService.getInstance().encrypt(client.getLoginPassword()));
        clientDTO.setDeleted(false);
        clientDTO.setBalance(0.0);
        clientDTO.setProfilePicturePath("avatar.png");
        clientDTO.setCurrentStatus(0);
        clientDTO.setLatestStatus(0);
        clientDTO.setCorporate(false);

        globalService.save(clientDTO);

        return clientDTO;
    }

    @Transactional
    ClientDetailsDTO insertClientWithModuleSpecificInfo(RegistrantInformation client, boolean fromSystem, ClientDTO clientDTO) throws Exception {

        ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();

        clientDetailsDTO.setClientID(clientDTO.getClientID());

        clientDetailsDTO.setRegistrantType(client.getRegistrantType());
        clientDetailsDTO.setClientCategoryType(client.getClientCategoryType());
        clientDetailsDTO.setModuleID(client.getModuleID());
        clientDetailsDTO.setRegCategory(client.getRegCategory());
        clientDetailsDTO.setRegSubCategory(client.getRegSubCategory());
        clientDetailsDTO.setDeleted(false);

        int status = getNextStatebyRequestType(this.getRequestType(client.getModuleID(), fromSystem));

        clientDetailsDTO.setCurrentStatus(status);
        clientDetailsDTO.setLatestStatus(status);
        clientDetailsDTO.setIdentity("");
        clientDetailsDTO.setBtrcLicenseDate("");

        ModifiedSqlGenerator.insert(clientDetailsDTO, ClientDetailsDTO.class, false);

        return clientDetailsDTO;
    }

    private void insertClientContactDetails(
            RegistrantInformation client,
            ClientDetailsDTO clientDetailsDTO) throws Exception {

        List<RegistrantContactInformation> contactInfoList = client.getContactInfoList();

        for (RegistrantContactInformation information : contactInfoList) {
            insertContactDetails(information, clientDetailsDTO);
        }
    }

    private void insertContactDetails(
            RegistrantContactInformation information,
            ClientDetailsDTO clientDetailsDTO) throws Exception {
        ClientContactDetailsDTO contact = new ClientContactDetailsDTO();

        contact.setVpnClientID(clientDetailsDTO.getId());
        contact.setRegistrantsName(information.getRegistrantsName());
        contact.setRegistrantsLastName(information.getRegistrantsLastName());
        contact.setEmail(information.getEmail());
        contact.setIsEmailVerified(information.getIsEmailVerified());
        contact.setWebAddress(information.getWebAddress());

        contact.setFaxNumber(information.getFaxNumber());
        contact.setPhoneNumber(information.getPhoneNumber());
        contact.setIsPhoneNumberVerified(information.getIsPhoneNumberVerified());
        contact.setLandlineNumber(information.getLandlineNumber());
        contact.setDetailsType(information.getDetailsType());

        contact.setCountry(information.getCountry());
        contact.setCity(information.getCity());
        contact.setPostCode(information.getPostCode());
        contact.setAddress(information.getAddress());
        contact.setDeleted(false);

        globalService.save(contact);
    }

    private boolean passwordMatchCheck(String loginPassword, String confirmPassword) {
        return loginPassword.equals(confirmPassword);
    }

    private boolean checkCaptcha(String answer, Captcha captcha) {
        return captcha.isCorrect(answer);
    }

    private int getRequestType(int moduleId, boolean fromSystem) {
        return moduleId * ModuleConstants.MULTIPLIER +
                (fromSystem ? ModuleConstants.SYSTEM_APPLY_CLIENT_REG : ModuleConstants.CLIENT_APPLY_CLIENT_REG);
    }

    private int getNextStatebyRequestType(int requestType) {
        return RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType).getNextStateID();
    }


    @Transactional
    public void addRequest(
            ClientDTO clientDTO,
            RegistrantInformation client,
            boolean fromSystem,
            LoginDTO loginDTO,
            String remoteAddress) throws Exception {
        CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
        commonRequestDTO.setEntityID(clientDTO.getClientID());
        commonRequestDTO.setRequestTime(System.currentTimeMillis());
        commonRequestDTO.setLastModificationTime(System.currentTimeMillis());
        commonRequestDTO.setRequestTypeID(
                getRequestType(client.getModuleID(), fromSystem)
        );

        commonRequestDTO.setEntityTypeID(
                client.getModuleID() * EntityTypeConstant.MULTIPLIER2 + 51
        );
        commonRequestDTO.setClientID(clientDTO.getClientID());


        if (loginDTO != null) {
            if (!loginDTO.getIsAdmin())
                commonRequestDTO.setRequestByAccountID(loginDTO.getAccountID());
            else
                commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
        } else {
            commonRequestDTO.setRequestByAccountID(clientDTO.getClientID());
        }

        commonRequestDTO.setIP(remoteAddress);
        commonRequestDTO.setDescription("New client add request is submitted(" + clientDTO.getLoginName() + ")");


        LoginDTO freshLoginDTO = loginDTO;
        if (freshLoginDTO == null) {
            freshLoginDTO = new LoginDTO();
            freshLoginDTO.setAccountID(clientDTO.getClientID());
            freshLoginDTO.setLoginSourceIP(remoteAddress);
        }
        ServiceDAOFactory.getDAO(RequestDAO.class)
                .addRequest(commonRequestDTO, freshLoginDTO.getLoginSourceIP(),
                        DatabaseConnectionFactory.getCurrentDatabaseConnection());


    }
}
