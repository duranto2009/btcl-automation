package datamigrationLLIClient;

import annotation.Transactional;
import client.IdentityTypeConstants;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.client.ClientContactDetails;
import common.client.ClientContactDetailsConditionBuilder;
import datamigrationGeneric.CSVFileHandler;
import file.FileTypeConstants;
import global.GlobalService;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LLIClientMigrationService {

    public static int count = 1;

    // LLI
    int moduleId = 7;

    public static int defaultStatus = -75005; // make the default status active, otherwise there is problem with client details page

    public int calculateIdentityValue(int moduleId, int identityTypeConstant) {
        return moduleId * 10000 + identityTypeConstant;
    }

    public String buildClientIdentity(List<String> clientInformation, int moduleId) {

        String clientIdentity = "";

        clientIdentity += calculateIdentityValue(moduleId, IdentityTypeConstants.Forwarding_Letter);
        clientIdentity += ":";
        clientIdentity += clientInformation.get(LLIClientExistingDataColumnNameMap.FORWARDING_LETTER_NUMBER);
        clientIdentity += ",";

        clientIdentity += calculateIdentityValue(moduleId, IdentityTypeConstants.NID);
        clientIdentity += ":";
        clientIdentity += clientInformation.get(LLIClientExistingDataColumnNameMap.NID_NUMBER);
        clientIdentity += ",";

        clientIdentity += calculateIdentityValue(moduleId, IdentityTypeConstants.TIN);
        clientIdentity += ":";
        clientIdentity += clientInformation.get(LLIClientExistingDataColumnNameMap.TIN_NUMBER);
        clientIdentity += ",";

        clientIdentity += calculateIdentityValue(moduleId, IdentityTypeConstants.Trade_License);
        clientIdentity += ":";
        clientIdentity += clientInformation.get(LLIClientExistingDataColumnNameMap.TRADE_LICENSE_NUMBER);
        clientIdentity += ",";

        return clientIdentity;
    }

    private int identifyExistingRegistrantType(String existingEntry) {

        if (existingEntry.equalsIgnoreCase("govt.") || existingEntry.equalsIgnoreCase("govt")
                || existingEntry.equalsIgnoreCase("government"))
            return RegistrantTypeConstants.GOVT;
        else if (existingEntry.equalsIgnoreCase("private"))
            return RegistrantTypeConstants.PRIVATE;
        else if (existingEntry.equalsIgnoreCase("military"))
            return RegistrantTypeConstants.MILITARY;
        else if (existingEntry.equalsIgnoreCase("foreign"))
            return RegistrantTypeConstants.FOREIGN;
        else
            return RegistrantTypeConstants.OTHERS;
    }

    private long identifyExistingRegistrantCategory(String existingEntry) {

        if (existingEntry.equalsIgnoreCase("iig"))
            return RegistrantCategoryConstants.IIG;
        else if (existingEntry.equalsIgnoreCase("isp"))
            return RegistrantCategoryConstants.ISP;
        else if (existingEntry.equalsIgnoreCase("project"))
            return RegistrantCategoryConstants.PROJECT;
        else if (existingEntry.equalsIgnoreCase("corporate_body"))
            return RegistrantCategoryConstants.CORPORATE_BODY;
        else if (existingEntry.equalsIgnoreCase("educational institute")
                || existingEntry.equalsIgnoreCase("educational"))
            return RegistrantCategoryConstants.Educational_Institute;
        else if (existingEntry.equalsIgnoreCase("training institute"))
            return RegistrantCategoryConstants.Training_Institute;
        else if (existingEntry.equalsIgnoreCase("educational research network"))
            return RegistrantCategoryConstants.Educational_Research_Network;
        else if (existingEntry.equalsIgnoreCase("organization") || existingEntry.equalsIgnoreCase("org")
                || existingEntry.equalsIgnoreCase("org."))
            return RegistrantCategoryConstants.Organization;
        else if (existingEntry.equalsIgnoreCase("international university"))
            return RegistrantCategoryConstants.International_University;
        else if (existingEntry.equalsIgnoreCase("international training and research center"))
            return RegistrantCategoryConstants.International_Training_And_Research_Center;
        else if (existingEntry.equalsIgnoreCase("international organization"))
            return RegistrantCategoryConstants.International_Organization;
        else if (existingEntry.equalsIgnoreCase("call center"))
            return RegistrantCategoryConstants.Call_Center;
        else if (existingEntry.equalsIgnoreCase("software company"))
            return RegistrantCategoryConstants.Software_Company;
        else if (existingEntry.equalsIgnoreCase("corporate office"))
            return RegistrantCategoryConstants.Corporate_Office;
        else if (existingEntry.equalsIgnoreCase("bank insurance company"))
            return RegistrantCategoryConstants.Bank_Insurance_Company;
        else if (existingEntry.equalsIgnoreCase("limited comany"))
            return RegistrantCategoryConstants.Limited_Company;
        else if (existingEntry.equalsIgnoreCase("bwa"))
            return RegistrantCategoryConstants.BWA;
        else if (existingEntry.equalsIgnoreCase("mobile operator"))
            return RegistrantCategoryConstants.Mobile_Operator;
        else
            return RegistrantCategoryConstants.OTHERS;
    }


    @Transactional
    public void insertClient(List<String> clientInformation) throws Exception {

        ClientDTO client = new ClientDTO();
        CurrentTimeFactory.initializeCurrentTimeFactory();

        client.setLoginName(generateUsernameFromClientName(clientInformation.get(LLIClientExistingDataColumnNameMap.CLIENT_NAME)));
        client.setLoginPassword(
                PasswordService.hashPassword(getDefaultPassword()));
        client.setBalance(0.0);
        client.setLastModificationTime(System.currentTimeMillis());
        client.setDeleted(false);
        client.setActivationDate(0);
        client.setProfilePicturePath(FileTypeConstants.DEFAULT_PROFILE_PIC);
        client.setCurrentStatus(0);
        client.setLatestStatus(0);
        client.setCorporate(false);

        ModifiedSqlGenerator.insert(client);
        long pkOfClientTable = client.getClientID();

        insertClientDetails(pkOfClientTable, clientInformation);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    private void insertClientDetails(long pkOfClientTable, List<String> clientInformation) throws Exception {

        ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();
        clientDetailsDTO.setClientID(pkOfClientTable);
        clientDetailsDTO.setModuleID(moduleId);

        // set Identity ()

        String clientIdentity = buildClientIdentity(clientInformation, moduleId);
        clientDetailsDTO.setIdentity(clientIdentity);

        // set status
        clientDetailsDTO.setCurrentStatus(defaultStatus);
        clientDetailsDTO.setLatestStatus(defaultStatus);

        // set regType and regCategory
        int registrantType, registrantCategory;

        registrantType = identifyExistingRegistrantType(
                clientInformation.get(LLIClientExistingDataColumnNameMap.REGISTRANT_TYPE));
        registrantCategory = (int) identifyExistingRegistrantCategory(
                clientInformation.get(LLIClientExistingDataColumnNameMap.REGISTRANT_CATEGORY));

        clientDetailsDTO.setRegistrantType(registrantType);
        clientDetailsDTO.setRegistrantCategory(registrantCategory);

        //
        clientDetailsDTO.setLastModificationTime(System.currentTimeMillis());
        clientDetailsDTO.setDeleted(false);
        clientDetailsDTO.setActivationDate(0);
        clientDetailsDTO.setClientCategoryType(2); // all organization client for LLI

        ModifiedSqlGenerator.insert(clientDetailsDTO, ClientDetailsDTO.class, false);

        long pkOfClientDetailsTable = clientDetailsDTO.getId();

        insertClientContactDetails(pkOfClientDetailsTable, clientInformation);
    }

    private void insertClientContactDetails(long pkOfClientDetailsTable, List<String> clientInformation)
            throws Exception {

        insertClientContactDetailsRegistrant(pkOfClientDetailsTable, clientInformation);
        insertClientContactDetailsBilling(pkOfClientDetailsTable, clientInformation);
        insertClientContactDetailsAdmin(pkOfClientDetailsTable, clientInformation);
        insertClientContactDetailsTechnical(pkOfClientDetailsTable, clientInformation);
    }

    private void insertClientContactDetailsRegistrant(long pkOfClientDetailsTable, List<String> clientInformation)
            throws Exception {

        ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();
        clientContactDetailsDTO.setVpnClientID(pkOfClientDetailsTable);
        clientContactDetailsDTO.setRegistrantsName(clientInformation.get(LLIClientExistingDataColumnNameMap.CLIENT_NAME));

        // last name, father's name, mother's name, gender not needed

        clientContactDetailsDTO.setDateOfBirth(clientInformation.get(LLIClientExistingDataColumnNameMap.DATE_OF_BIRTH));
        clientContactDetailsDTO.setWebAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.WEB_ADDRESS));
        clientContactDetailsDTO.setDetailsType(ClientContactDetailsDTO.REGISTRANT_CONTACT);

        clientContactDetailsDTO.setAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.GENERAL_ADDRESS));
        clientContactDetailsDTO.setCity(clientInformation.get(LLIClientExistingDataColumnNameMap.GENERAL_CITY));
        clientContactDetailsDTO.setPostCode(clientInformation.get(LLIClientExistingDataColumnNameMap.GENERAL_POST_CODE));
        clientContactDetailsDTO.setCountry(clientInformation.get(LLIClientExistingDataColumnNameMap.COUNTRY));
        clientContactDetailsDTO.setEmail(clientInformation.get(LLIClientExistingDataColumnNameMap.EMAIL));
        clientContactDetailsDTO.setPhoneNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.TELEPHONE_NUMBER));
        clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
        clientContactDetailsDTO.setDeleted(false);
        clientContactDetailsDTO.setLandlineNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.MOBILE_NUMBER));

        ModifiedSqlGenerator.insert(clientContactDetailsDTO, ClientContactDetailsDTO.class, false);
    }

    private void insertClientContactDetailsBilling(long pkOfClientDetailsTable, List<String> clientInformation)
            throws Exception {

        ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();
        clientContactDetailsDTO.setVpnClientID(pkOfClientDetailsTable);
        clientContactDetailsDTO.setRegistrantsName(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_FIRSTNAME));
        clientContactDetailsDTO
                .setRegistrantsLastName(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_LASTNAME));
        // last name, father's name, mother's name, gender not needed

        clientContactDetailsDTO.setDateOfBirth(clientInformation.get(LLIClientExistingDataColumnNameMap.DATE_OF_BIRTH));
        clientContactDetailsDTO.setWebAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.WEB_ADDRESS));
        clientContactDetailsDTO.setDetailsType(ClientContactDetailsDTO.BILLING_CONTACT);

        clientContactDetailsDTO.setAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_ADDRESS));
        clientContactDetailsDTO.setCity(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_CITY));
        clientContactDetailsDTO.setPostCode(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_POST_CODE));
        clientContactDetailsDTO.setCountry(clientInformation.get(LLIClientExistingDataColumnNameMap.COUNTRY));
        clientContactDetailsDTO.setEmail(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_EMAIL));
        clientContactDetailsDTO.setFaxNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_FAX_NUMBER));
        clientContactDetailsDTO.setPhoneNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.BILLING_PHONE_NUMBER));

        clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
        clientContactDetailsDTO.setDeleted(false);
        clientContactDetailsDTO.setLandlineNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.MOBILE_NUMBER));

        ModifiedSqlGenerator.insert(clientContactDetailsDTO, ClientContactDetailsDTO.class, false);
    }

    private void insertClientContactDetailsAdmin(long pkOfClientDetailsTable, List<String> clientInformation)
            throws Exception {

        ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();
        clientContactDetailsDTO.setVpnClientID(pkOfClientDetailsTable);
        clientContactDetailsDTO.setRegistrantsName(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_FIRSTNAME));
        clientContactDetailsDTO.setRegistrantsLastName(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_LASTNAME));
        // last name, father's name, mother's name, gender not needed

        clientContactDetailsDTO.setDateOfBirth(clientInformation.get(LLIClientExistingDataColumnNameMap.DATE_OF_BIRTH));
        clientContactDetailsDTO.setWebAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.WEB_ADDRESS));
        clientContactDetailsDTO.setDetailsType(ClientContactDetailsDTO.ADMIN_CONTACT);

        clientContactDetailsDTO.setAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_ADDRESS));
        clientContactDetailsDTO.setCity(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_CITY));
        clientContactDetailsDTO.setPostCode(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_POST_CODE));
        clientContactDetailsDTO.setCountry(clientInformation.get(LLIClientExistingDataColumnNameMap.COUNTRY));
        clientContactDetailsDTO.setEmail(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_EMAIL));
        clientContactDetailsDTO.setFaxNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_FAX_NUMBER));
        clientContactDetailsDTO.setPhoneNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.ADMIN_PHONE_NUMBER));

        clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
        clientContactDetailsDTO.setDeleted(false);
        clientContactDetailsDTO.setLandlineNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.MOBILE_NUMBER));

        ModifiedSqlGenerator.insert(clientContactDetailsDTO, ClientContactDetailsDTO.class, false);
    }

    private void insertClientContactDetailsTechnical(long pkOfClientDetailsTable, List<String> clientInformation)
            throws Exception {

        ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();
        clientContactDetailsDTO.setVpnClientID(pkOfClientDetailsTable);
        clientContactDetailsDTO
                .setRegistrantsName(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_FIRSTNAME));
        clientContactDetailsDTO
                .setRegistrantsLastName(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_LASTNAME));
        // last name, father's name, mother's name, gender not needed

        clientContactDetailsDTO.setDateOfBirth(clientInformation.get(LLIClientExistingDataColumnNameMap.DATE_OF_BIRTH));
        clientContactDetailsDTO.setWebAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.WEB_ADDRESS));
        clientContactDetailsDTO.setDetailsType(ClientContactDetailsDTO.TECHNICAL_CONTACT);

        clientContactDetailsDTO.setAddress(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_ADDRESS));
        clientContactDetailsDTO.setCity(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_CITY));
        clientContactDetailsDTO.setPostCode(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_POST_CODE));
        clientContactDetailsDTO.setCountry(clientInformation.get(LLIClientExistingDataColumnNameMap.COUNTRY));
        clientContactDetailsDTO.setEmail(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_EMAIL));
        clientContactDetailsDTO.setFaxNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_FAX_NUMBER));
        clientContactDetailsDTO.setPhoneNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.TECHNICAL_PHONE_NUMBER));

        clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());
        clientContactDetailsDTO.setDeleted(false);
        clientContactDetailsDTO.setLandlineNumber(clientInformation.get(LLIClientExistingDataColumnNameMap.MOBILE_NUMBER));

        ModifiedSqlGenerator.insert(clientContactDetailsDTO, ClientContactDetailsDTO.class, false);

    }


    public String generateUsernameFromClientName(String fullName) throws Exception {

        fullName = CSVFileHandler.getStringWithoutQuotes(fullName);

        List<String> strings = new ArrayList<>();

        StringTokenizer tokenizer =
                new StringTokenizer(fullName, " ");

        while (tokenizer.hasMoreTokens())
            strings.add(tokenizer.nextToken().trim().toLowerCase());

        String username = "";

        for (int i = 0; i < strings.size(); i++) {
            if (i == strings.size() - 1)
                username += strings.get(i);
            else
                username += strings.get(i).charAt(0);
        }

        boolean notFound = true;


        //removal of non-alphanumeric chars

        String usernamePolished = "";
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
                usernamePolished += c;
        }

        username = usernamePolished;

        while (notFound) {
            if (checkIfUsernameIsAvailable(username)) {
                notFound = false;
            } else {
                username += count;
                count++;
            }
        }

        return username;
    }

    public String getDefaultPassword() {
        return "hello";
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public boolean checkIfUsernameIsAvailable(String username) throws Exception {

        List<ClientDTO> list = ModifiedSqlGenerator.getAllObjectList(ClientDTO.class,
                new ClientDTOConditionBuilder().Where().loginNameEquals(username).getCondition());

        if (list.isEmpty())
            return true;

        return false;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public long getClientIdByClientFullName(String fullName) throws Exception {

        List<ClientContactDetails> clientContactDetails = ModifiedSqlGenerator.getAllObjectList(
                ClientContactDetails.class, new ClientContactDetailsConditionBuilder()
                        .Where()
                        .nameEquals(fullName)
                        .detailsTypeEquals(ClientContactDetailsDTO.REGISTRANT_CONTACT)
                        .getCondition()
        );

        ClientDetailsDTO clientDetailsDTO = ServiceDAOFactory.getService(
                GlobalService.class).findByPK(ClientDetailsDTO.class, clientContactDetails.get(0).getVpnClientId());

        return clientDetailsDTO.getClientID();
    }






}
