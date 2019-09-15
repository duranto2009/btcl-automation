package vpn.client;

import annotation.Transactional;
import client.temporaryClient.TemporaryClient;
import common.*;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import file.FileService;
import forgetPassword.ForgetPassword;
import forgetPassword.ForgetPasswordUtility;
import global.GlobalService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import request.CommonRequestDTO;
import request.CommonRequestStatusDTO;
import request.RequestDAO;
import request.StateRepository;
import requestMapping.Service;
import smsServer.SMSSender;
import util.ActivityLogDAO;
import util.NavigationService;
import util.SOP;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTOConditionBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static util.SqlGenerator.getObjectByID;

public class ClientService implements NavigationService {

    @Service
    private GlobalService globalService;

    Logger logger = Logger.getLogger(getClass());
    ClientDAO clientDAO = new ClientDAO();
    ActivityLogDAO activityLogDAO = new ActivityLogDAO();
    RequestDAO requestDAO = new RequestDAO();
    FileService fileDAO = new FileService();
    public int moduleID = 0;

    public void addNewClient(ClientDetailsDTO vdto, LoginDTO loginDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDAO.addNewVpnClient(vdto, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug("Exception Add Vpn Cleint :", ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }

    }

    public void addNewGeneralClient(ClientDTO cdto, LoginDTO loginDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDAO.addNewClient(cdto, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug("Exception add Cleint :", ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        Collection collection = null;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDAO.moduleID = moduleID;
            if (objects.length == 1)
                collection = clientDAO.getIDs(loginDTO, databaseConnection, objects[0]);
            else
                collection = clientDAO.getIDs(loginDTO, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return collection;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        logger.debug("Inside get Id with criteria of common");
        Collection collection = null;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            collection = clientDAO.getIDWithSearchCriteriaFromSqlGenerator(searchCriteria, databaseConnection);
            //collection = vpnDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO, databaseConnection,  objects);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.info("Exception inside id with criteria of client common: ", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug("Exception inside id with criteria of client common: ", ex2);

            }
        } finally {
            databaseConnection.dbClose();
        }
        return collection;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        Collection collection = null;
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            ClientDAO dao = new ClientDAO();
            if (objects.length == 1)
                collection = dao.getDTOs(recordIDs, databaseConnection, objects[0]);
            else
                collection = dao.getDTOs(recordIDs, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            try {
                logger.debug("Error inside getDTO method of clientService ", ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return collection;
    }

    public ClientDetailsDTO getClient(long id, LoginDTO loginDTO, Object... objects) throws Exception {

        // return ClientRepository.getInstance().getVpnClient(id);
        DatabaseConnection databaseConnection = new DatabaseConnection();

        ClientDetailsDTO clientDTO = null;
        int moduleID = ((CommonSelector) objects[0]).moduleID;
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            ClientDAO dao = new ClientDAO();
            clientDTO = dao.getDTO(id, moduleID, databaseConnection);
            //clientDTO.setFileDTO(fileDAO.getFileByEntityTypeAndEntity(EntityTypeConstant.moduleIDClientTypeIDMap.get(moduleID),clientDTO.getClientID()).get(0));
            databaseConnection.dbTransationEnd();
            logger.debug("clientDTO " + clientDTO);
        } catch (Exception ex) {
            logger.debug("Fatal", ex);
            try {
                logger.debug("Error inside getDTO method of clientService " + ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return clientDTO;
    }

    public void insertContactDetailsDTOs(ArrayList<ClientContactDetailsDTO> contactDetailsDTOs) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (ClientContactDetailsDTO contactDetailsDTO : contactDetailsDTOs)
                clientDAO.addNewClientContactDetailsDTO(contactDetailsDTO, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug("Exception Vpn Cleint Contact Details : ", ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateNewGeneralClient(ClientDTO clientDTO, LoginDTO loginDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDAO.updateNewClient(clientDTO, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateNewClient(ClientDetailsDTO vdto, LoginDTO loginDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDAO.updateNewVpnClient(vdto, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateContactDetailsDTOs(ArrayList<ClientContactDetailsDTO> contactDetailsDTOs) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (ClientContactDetailsDTO contactDetailsDTO : contactDetailsDTOs) {
                clientDAO.updateClientContactDetailsDTO(contactDetailsDTO, databaseConnection);
            }
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addClientRequest(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        logger.debug("addClientRequest ");
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);
			/*if(loginDTO.getAccountID() > 0)
			{
				CommonDAO commonDAO = new CommonDAO();		
				commonRequestDTO.setState(DomainStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED);
				commonDAO.inputStatusHistory(commonRequestDTO, null, loginDTO, databaseConnection);
			}*/
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }

    }

    public void addClientRequestStatus(CommonRequestStatusDTO commonRequestStatusDTO) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            requestDAO.addRequestStatus(commonRequestStatusDTO, databaseConnection);
            databaseConnection.dbTransationEnd();

        } catch (Exception ex) {
            logger.debug(ex);
            try {

                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }

    }

    /*public ArrayList<CommonRequestDTO> getClientPendingRequest(long clientID, LoginDTO loginDTO) {
        // TODO Auto-generated method stub
        CommonRequestDTO comDTO = new CommonRequestDTO();
        comDTO.setClientID(clientID);
        DatabaseConnection databaseConnection = new DatabaseConnection();
        ArrayList<CommonRequestDTO> list = new ArrayList<CommonRequestDTO>();
        try
        {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            list = new RequestUtilDAO().getPendingRequestDTOList(comDTO, loginDTO, databaseConnection);
            logger.debug("list " + list);
            databaseConnection.dbTransationEnd();
        }
        catch(Exception ex)
        {
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        finally
        {
            databaseConnection.dbClose();
        }
        return list;
    }*/
    public ClientDTO getClientDTO(long id, LoginDTO loginDTO) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        ClientDTO clientDTO = null;
        SOP.print(moduleID + "");
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            clientDTO = (ClientDTO) getObjectByID(ClientDTO.class, id, databaseConnection);
            databaseConnection.dbTransationEnd();
            logger.debug("clientDTO " + clientDTO);
        } catch (Exception ex) {
            logger.debug("Fatat", ex);
            try {
                logger.debug("Error inside getDTO method of clientService " + ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return clientDTO;
    }

    public ClientDTO getClientDTOByUsername(String username, LoginDTO loginDTO) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        ClientDTO clientDTO = null;
        SOP.print(moduleID + "");
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
//			clientDTO = (ClientDTO)getObjectByID(ClientDTO.class, username, databaseConnection);
            databaseConnection.dbTransationEnd();
            logger.debug("clientDTO " + clientDTO);
        } catch (Exception ex) {
            logger.debug("Fatat", ex);
            try {
                logger.debug("Error inside getDTO method of clientService " + ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return clientDTO;
    }

    public List<ClientContactDetailsDTO> getVpnContactDetailsListByClientID(long clientDetailsID) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<ClientContactDetailsDTO> list = null;
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            list = clientDAO.getVpnContactDetailsListByClientID(clientDetailsID, databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.debug("Fatat", ex);
            try {
                logger.debug("Error inside getDTO method of clientService " + ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return list;
    }

    public Collection<ClientDetailsDTO> getClientDetailsDTOListByClientIDAndEmail(long ClientID, String email) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<ClientDetailsDTO> list = null;
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            list = (List<ClientDetailsDTO>) clientDAO.getClientDetailsDTOListByClientIDAndEmail(ClientID, email, databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.debug("Fatat", ex);
            try {
                logger.debug("Error inside getDTO method of clientService " + ex);
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
                logger.debug(ex2);
            }
        } finally {
            databaseConnection.dbClose();
        }
        return list;
    }

    /**
     * This method sends a verification mail to the given email address to verify the email address that user
     * gave during creation of account.
     *
     * @param username username given by the user
     * @param email    mail address submitted by the user in client add form
     * @throws Exception
     * @author Alam
     */
    public static void sendVerificationMail(String username, String email, HttpServletRequest request) throws Exception {

        //Though written to generate token for forget password module. We can use this in current
        //purpose also
        String token = ForgetPasswordUtility.getPasswordResetToken(32);

        String remoteAddress = request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();

        //String remoteAddress = request.getScheme() + "://" + "bdia.btcl.com.bd/" + request.getContextPath();

        ClientUtility.sendEmailVerificationMail(email, username, token, remoteAddress);

        ForgetPassword forgetPassword = new ForgetPassword();

        forgetPassword.setUsername(email);
        forgetPassword.setToken(token);
        forgetPassword.setLastModificationTime(System.currentTimeMillis());

        forgetPassword.insert();

    }

    public static void sendVerificationMailTemporaryClient(String username, String email, HttpServletRequest request, String id) throws Exception {

        //Though written to generate token for forget password module. We can use this in current
        //purpose also
        String token = ForgetPasswordUtility.getPasswordResetToken(32);

        String remoteAddress = request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();

        //String remoteAddress = request.getScheme() + "://" + "bdia.btcl.com.bd/" + request.getContextPath();

        ClientUtility.sendEmailVerificationMailTemporaryClient(email, username, token, remoteAddress, id);

        ForgetPassword forgetPassword = new ForgetPassword();

        forgetPassword.setUsername(email);
        forgetPassword.setToken(token);
        forgetPassword.setLastModificationTime(System.currentTimeMillis());

        forgetPassword.insert();

    }

    public static void sendVerificationSMS(String username, String phoneNo, HttpServletRequest request) throws Exception {

        String token = ForgetPasswordUtility.getPasswordResetToken(6);

        ForgetPassword forgetPassword = new ForgetPassword();

        phoneNo = phoneNo.trim();


        if (phoneNo.startsWith("8800")) {
            phoneNo = phoneNo.substring(3);
        } else if (phoneNo.startsWith("880")) {
            phoneNo = phoneNo.substring(2);
        }


        forgetPassword.setUsername(phoneNo);
        forgetPassword.setToken(token);
        forgetPassword.setLastModificationTime(System.currentTimeMillis());

        System.out.println("Sending OTP to " + phoneNo);
        SMSSender.getInstance().sendSMS(token, phoneNo);
        forgetPassword.insert();
    }

    public boolean getClientIsActive(long clientID, int moduleID) {
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);
        int activationStatus = StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus()).getActivationStatus();
        if (activationStatus != EntityTypeConstant.STATUS_NOT_ACTIVE) {
            return true;
        }
        return false;
    }


    /**
     * @author dhrubo
     */
    public HashMap<String, String> getFormDataByClientIDandModuleID(int moduleID2, long clientID) throws Exception {
        HashMap<String, String> formDataByClientIDandModuleID = new HashMap<String, String>();

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID2);

        ClientContactDetailsDTO registrantContactDetails = getVpnContactDetailsListByClientID(clientDetailsDTO.getId()).get(ClientConstants.DETAILS_TYPE_REGISTRANT);
        ClientContactDetailsDTO billingContactDetails = getVpnContactDetailsListByClientID(clientDetailsDTO.getId()).get(ClientConstants.DETAILS_TYPE_BILLING);
        ClientContactDetailsDTO adminContactDetails = getVpnContactDetailsListByClientID(clientDetailsDTO.getId()).get(ClientConstants.DETAILS_TYPE_ADMIN);
        ClientContactDetailsDTO technicalContactDetails = getVpnContactDetailsListByClientID(clientDetailsDTO.getId()).get(ClientConstants.DETAILS_TYPE_TECHNICAL);

        //Client Category
        formDataByClientIDandModuleID.put("clientDetailsDTO.clientCategoryType", clientDetailsDTO.getClientCategoryType() + "");

        //Client Individual
        formDataByClientIDandModuleID.put("clientDetailsDTO.registrantType", clientDetailsDTO.getRegistrantType() + "");
        formDataByClientIDandModuleID.put("clientDetailsDTO.regiCat", "0");
        formDataByClientIDandModuleID.put("registrantContactDetails.registrantsName", registrantContactDetails.getRegistrantsName());
        formDataByClientIDandModuleID.put("registrantContactDetails.registrantsLastName", registrantContactDetails.getRegistrantsLastName());
        formDataByClientIDandModuleID.put("registrantContactDetails.fatherName", registrantContactDetails.getFatherName());
        formDataByClientIDandModuleID.put("registrantContactDetails.motherName", registrantContactDetails.getMotherName());
        formDataByClientIDandModuleID.put("registrantContactDetails.gender", registrantContactDetails.getGender());
        formDataByClientIDandModuleID.put("registrantContactDetails.email", registrantContactDetails.getEmail());
        formDataByClientIDandModuleID.put("clientDetailsDTO.identity", clientDetailsDTO.getIdentity());
        formDataByClientIDandModuleID.put("registrantContactDetails.phoneNumber", registrantContactDetails.getPhoneNumber());
        formDataByClientIDandModuleID.put("intlMobileNumber", registrantContactDetails.getPhoneNumber());

        formDataByClientIDandModuleID.put("registrantContactDetails.dateOfBirth", registrantContactDetails.getDateOfBirth());
        formDataByClientIDandModuleID.put("registrantContactDetails.occupation", registrantContactDetails.getOccupation());
        formDataByClientIDandModuleID.put("registrantContactDetails.isEmailVerified", registrantContactDetails.getIsEmailVerified()+"");
        formDataByClientIDandModuleID.put("registrantContactDetails.isPhoneNumberVerified",
                registrantContactDetails.getIsPhoneNumberVerified()+"");

        //Client Company
        formDataByClientIDandModuleID.put("registrantContactDetails.registrantsName", registrantContactDetails.getRegistrantsName());
        formDataByClientIDandModuleID.put("registrantContactDetails.registrantsLastName", registrantContactDetails.getRegistrantsLastName());
        formDataByClientIDandModuleID.put("registrantContactDetails.webAddress", registrantContactDetails.getWebAddress());
        formDataByClientIDandModuleID.put("clientDetailsDTO.registrantType", clientDetailsDTO.getRegistrantType() + "");
        formDataByClientIDandModuleID.put("registrantContactDetails.email", registrantContactDetails.getEmail());
        formDataByClientIDandModuleID.put("clientDetailsDTO.identity", clientDetailsDTO.getIdentity());
        formDataByClientIDandModuleID.put("intlMobileNumber", registrantContactDetails.getPhoneNumber());
        formDataByClientIDandModuleID.put("clientDetailsDTO.regiCategories", Long.toString(clientDetailsDTO.getRegistrantCategory()));//
        formDataByClientIDandModuleID.put("clientDetailsDTO.regSubCategory", Long.toString(clientDetailsDTO.getRegSubCategory()));
        formDataByClientIDandModuleID.put("clientDetailsDTO.btrcLicenseDate", clientDetailsDTO.getBtrcLicenseDate());
        //General
        formDataByClientIDandModuleID.put("registrantContactDetails.ID", registrantContactDetails.getID() + "");
        formDataByClientIDandModuleID.put("registrantContactDetails.landlineNumber", registrantContactDetails.getLandlineNumber());
        formDataByClientIDandModuleID.put("registrantContactDetails.faxNumber", registrantContactDetails.getFaxNumber());
        formDataByClientIDandModuleID.put("registrantContactDetails.country", registrantContactDetails.getCountry());
        formDataByClientIDandModuleID.put("registrantContactDetails.city", registrantContactDetails.getCity());
        formDataByClientIDandModuleID.put("registrantContactDetails.postCode", registrantContactDetails.getPostCode());
        formDataByClientIDandModuleID.put("registrantContactDetails.address", registrantContactDetails.getAddress());

        formDataByClientIDandModuleID.put("billingContactDetails.ID", billingContactDetails.getID() + "");
        formDataByClientIDandModuleID.put("billingContactDetails.registrantsName", billingContactDetails.getRegistrantsName());
        formDataByClientIDandModuleID.put("billingContactDetails.registrantsLastName", billingContactDetails.getRegistrantsLastName());
        formDataByClientIDandModuleID.put("billingContactDetails.email", billingContactDetails.getEmail());
        formDataByClientIDandModuleID.put("billingContactDetails.phoneNumber", billingContactDetails.getPhoneNumber());
        formDataByClientIDandModuleID.put("billingContactDetails.landlineNumber", billingContactDetails.getLandlineNumber());
        formDataByClientIDandModuleID.put("billingContactDetails.faxNumber", billingContactDetails.getFaxNumber());
        formDataByClientIDandModuleID.put("billingContactDetails.city", billingContactDetails.getCity());
        formDataByClientIDandModuleID.put("billingContactDetails.postCode", billingContactDetails.getPostCode());
        formDataByClientIDandModuleID.put("billingContactDetails.address", billingContactDetails.getAddress());

        formDataByClientIDandModuleID.put("adminContactDetails.ID", adminContactDetails.getID() + "");
        formDataByClientIDandModuleID.put("adminContactDetails.registrantsName", adminContactDetails.getRegistrantsName());
        formDataByClientIDandModuleID.put("adminContactDetails.registrantsLastName", adminContactDetails.getRegistrantsLastName());
        formDataByClientIDandModuleID.put("adminContactDetails.email", adminContactDetails.getEmail());
        formDataByClientIDandModuleID.put("adminContactDetails.phoneNumber", adminContactDetails.getPhoneNumber());
        formDataByClientIDandModuleID.put("adminContactDetails.landlineNumber", adminContactDetails.getLandlineNumber());
        formDataByClientIDandModuleID.put("adminContactDetails.faxNumber", adminContactDetails.getFaxNumber());
        formDataByClientIDandModuleID.put("adminContactDetails.city", adminContactDetails.getCity());
        formDataByClientIDandModuleID.put("adminContactDetails.postCode", adminContactDetails.getPostCode());
        formDataByClientIDandModuleID.put("adminContactDetails.address", adminContactDetails.getAddress());

        formDataByClientIDandModuleID.put("technicalContactDetails.ID", technicalContactDetails.getID() + "");
        formDataByClientIDandModuleID.put("technicalContactDetails.registrantsName", technicalContactDetails.getRegistrantsName());
        formDataByClientIDandModuleID.put("technicalContactDetails.registrantsLastName", technicalContactDetails.getRegistrantsLastName());
        formDataByClientIDandModuleID.put("technicalContactDetails.email", technicalContactDetails.getEmail());
        formDataByClientIDandModuleID.put("technicalContactDetails.phoneNumber", technicalContactDetails.getPhoneNumber());
        formDataByClientIDandModuleID.put("technicalContactDetails.landlineNumber", technicalContactDetails.getLandlineNumber());
        formDataByClientIDandModuleID.put("technicalContactDetails.faxNumber", technicalContactDetails.getFaxNumber());
        formDataByClientIDandModuleID.put("technicalContactDetails.city", technicalContactDetails.getCity());
        formDataByClientIDandModuleID.put("technicalContactDetails.postCode", technicalContactDetails.getPostCode());
        formDataByClientIDandModuleID.put("technicalContactDetails.address", technicalContactDetails.getAddress());

        //Login Details
        formDataByClientIDandModuleID.put("clientDetailsDTO.loginName", clientDetailsDTO.getLoginName());
        //formDataByClientIDandModuleID.put("clientDetailsDTO.loginPassword", clientDetailsDTO.getLoginPassword());
        //formDataByClientIDandModuleID.put("confirmLoginPassword", clientDetailsDTO.getLoginPassword());


        return formDataByClientIDandModuleID;
    }


    public boolean isEmailIdFree(String emailId) throws Exception {

        String email = globalService.getAllObjectListByCondition(ClientContactDetailsDTO.class,
                new ClientContactDetailsDTOConditionBuilder()
                        .Where()
                        .detailsTypeEquals(ClientContactDetailsDTO.REGISTRANT_CONTACT)
                        .emailEquals(emailId.trim())
                        .getCondition())
                .stream()
                .map(x -> x.getEmail().trim())
                .findFirst()
                .orElse(null);

        if (email != null) {
            return false;
        }

        long count = globalService.getAllObjectListByCondition(TemporaryClient.class)
                .stream()
                .map(x -> x.getEmailId().trim())
                .filter(x -> x.equals(emailId.trim()))
                .count();
        if (count > 0) {
            return false;
        }
        return true;

    }

    public boolean isMobileNumberFree(String mobileNumber) throws Exception {

        // mobile number must be with country code ; if not; then for BD Client's mobile number; unexpected result will occur.
        String mobile = globalService.getAllObjectListByCondition(ClientContactDetailsDTO.class,
                new ClientContactDetailsDTOConditionBuilder()
                        .Where()
                        .detailsTypeEquals(ClientContactDetailsDTO.REGISTRANT_CONTACT)
                        .phoneNumberEquals(mobileNumber.trim())
                        .getCondition())
                .stream()
                .map(x -> x.getPhoneNumber().trim())
                .findFirst()
                .orElse(null);

        if (mobile != null) {
            return false;
        }

        long count = globalService.getAllObjectListByCondition(TemporaryClient.class)
                .stream()
                .map(x->{
                    if(x.getMobileNumber().startsWith("0")){
                        return x.getCountryCode().trim() + x.getMobileNumber().trim().substring(1);
                    }else {
                        return x.getCountryCode().trim() + x.getMobileNumber().trim();
                    }
                })

                .filter(x -> x.equals(mobileNumber.trim()))
                .count();
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Transactional
    public void updateTemporaryClientMobileStateIfNeeded(long clientId) throws Exception {
        TemporaryClient client = globalService.findByPK(TemporaryClient.class, clientId);

        if (client == null) {
            throw new RequestFailureException("client doesn't exist");
        }
        client.setMobileNumberVerified(true);
        globalService.update(client);
    }


}
