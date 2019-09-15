package common.client;

import connection.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ModifiedSqlGenerator;
import vpn.client.ClientDetailsDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ClientDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);

    private static final String SQL_GET_CLIENT_INFO_BY_APPLICATION = "SELECT " +
            "a.applicationID AS applicationId, a.clientID AS clientId, a.userID AS userId, a.demandNoteId AS demandNoteId, " +
            "c.clLoginName AS loginName, c.clBalance AS balance, " +
            "ccd.vclcName AS name, ccd.vclcLastName AS lastName, ccd.vclcFatherName AS father, ccd.vclcMotherName AS mother, " +
            "ccd.vclcGender AS gender, ccd.vclcDateOfBirth AS dateOfBirth, " +
            "ccd.vclcWebAddress AS webAddress, ccd.vclcAddress AS address, ccd.vclcCity as city, ccd.vclcPostCode as postalCode, ccd.vclcCountry as country, " +
            "ccd.vclcEmail as email, ccd.vclcFaxNumber as fax, ccd.vclcPhoneNumber as phone, ccd.vclcSignature as signature, ccd.vclcJobType as jobType, " +
            "ccd.vclcOccupation as occupation, ccd.vclcContactInfoName as contactInfoName, ccd.vclcLandlineNumber as landLineNumber " +
            "FROM at_lli_application a " +
            "LEFT JOIN at_client c ON c.clID = a.clientID " +
            "LEFT JOIN at_client_details cd ON cd.vclClientID = c.clID " +
            "LEFT JOIN at_client_contact_details ccd ON ccd.vclcVpnClientID = cd.vclID " +
            "WHERE a.applicationID = ?";

    private static final String SQL_GET_CLIENT_INFO_BY_REVISED_APPLICATION = "SELECT " +
            "a.id AS applicationId, a.clientID AS clientId, " +
            "c.clLoginName AS loginName, c.clBalance AS balance, " +
            "ccd.vclcName AS name, ccd.vclcLastName AS lastName, ccd.vclcFatherName AS father, ccd.vclcMotherName AS mother, " +
            "ccd.vclcGender AS gender, ccd.vclcDateOfBirth AS dateOfBirth, " +
            "ccd.vclcWebAddress AS webAddress, ccd.vclcAddress AS address, ccd.vclcCity as city, ccd.vclcPostCode as postalCode, ccd.vclcCountry as country, " +
            "ccd.vclcEmail as email, ccd.vclcFaxNumber as fax, ccd.vclcPhoneNumber as phone, ccd.vclcSignature as signature, ccd.vclcJobType as jobType, " +
            "ccd.vclcOccupation as occupation, ccd.vclcContactInfoName as contactInfoName, ccd.vclcLandlineNumber as landLineNumber " +
            "FROM at_lli_connection_revise_client a " +
            "LEFT JOIN at_client c ON c.clID = a.clientID " +
            "LEFT JOIN at_client_details cd ON cd.vclClientID = c.clID " +
            "LEFT JOIN at_client_contact_details ccd ON ccd.vclcVpnClientID = cd.vclID " +
            "WHERE a.id = ?";


    private static final String SQL_GET_CLIENT_INFO_BY_NIX_APPLICATION = "SELECT " +
            "a.id AS applicationId, a.client AS clientId,  a.demand_note AS demandNoteId, " +
            "c.clLoginName AS loginName, c.clBalance AS balance, " +
            "ccd.vclcName AS name, ccd.vclcLastName AS lastName, ccd.vclcFatherName AS father, ccd.vclcMotherName AS mother, " +
            "ccd.vclcGender AS gender, ccd.vclcDateOfBirth AS dateOfBirth, " +
            "ccd.vclcWebAddress AS webAddress, ccd.vclcAddress AS address, ccd.vclcCity as city, ccd.vclcPostCode as postalCode, ccd.vclcCountry as country, " +
            "ccd.vclcEmail as email, ccd.vclcFaxNumber as fax, ccd.vclcPhoneNumber as phone, ccd.vclcSignature as signature, ccd.vclcJobType as jobType, " +
            "ccd.vclcOccupation as occupation, ccd.vclcContactInfoName as contactInfoName, ccd.vclcLandlineNumber as landLineNumber " +
            "FROM nix_application a " +
            "LEFT JOIN at_client c ON c.clID = a.client " +
            "LEFT JOIN at_client_details cd ON cd.vclClientID = c.clID " +
            "LEFT JOIN at_client_contact_details ccd ON ccd.vclcVpnClientID = cd.vclID " +
            "WHERE a.id = ?";

    public ClientDTO getClientByApplicationId(long applicationId) {
        ClientDTO clientDTO = null;
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_CLIENT_INFO_BY_APPLICATION);
            preparedStatement.setLong(1, applicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                int paramIndex = 0;
                clientDTO = new ClientDTO();
                clientDTO.setApplicationId(resultSet.getLong(++paramIndex));
                clientDTO.setClientId(resultSet.getLong(++paramIndex));
                clientDTO.setUserId(resultSet.getLong(++paramIndex));
                clientDTO.setDemandNoteId(resultSet.getLong(++paramIndex));
                clientDTO.setLoginName(resultSet.getString(++paramIndex));
                clientDTO.setBalance(resultSet.getDouble(++paramIndex));
                clientDTO.setName(resultSet.getString(++paramIndex));
                clientDTO.setLastName(resultSet.getString(++paramIndex));
                clientDTO.setFatherName(resultSet.getString(++paramIndex));
                clientDTO.setMotherName(resultSet.getString(++paramIndex));
                clientDTO.setGender(resultSet.getString(++paramIndex));
                clientDTO.setDateOfBirth(resultSet.getString(++paramIndex));
                clientDTO.setWebAddress(resultSet.getString(++paramIndex));
                clientDTO.setAddress(resultSet.getString(++paramIndex));
                clientDTO.setCity(resultSet.getString(++paramIndex));
                clientDTO.setPostCode(resultSet.getString(++paramIndex));
                clientDTO.setCountry(resultSet.getString(++paramIndex));
                clientDTO.setEmail(resultSet.getString(++paramIndex));
                clientDTO.setFax(resultSet.getString(++paramIndex));
                clientDTO.setPhone(resultSet.getString(++paramIndex));
                clientDTO.setSignature(resultSet.getString(++paramIndex));
                clientDTO.setJobType(resultSet.getInt(++paramIndex));
                clientDTO.setOccupation(resultSet.getString(++paramIndex));
                clientDTO.setContactInfoName(resultSet.getString(++paramIndex));
                clientDTO.setLandLineNumber(resultSet.getString(++paramIndex));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        } finally {
            conn.dbClose();
        }
        return clientDTO;
    }

    public ClientDTO getClientByReviseApplication(long applicationId) {
        ClientDTO clientDTO = null;
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_CLIENT_INFO_BY_REVISED_APPLICATION);
            preparedStatement.setLong(1, applicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                int paramIndex = 0;
                clientDTO = new ClientDTO();
                clientDTO.setApplicationId(resultSet.getLong(++paramIndex));
                clientDTO.setClientId(resultSet.getLong(++paramIndex));
                clientDTO.setLoginName(resultSet.getString(++paramIndex));
                clientDTO.setBalance(resultSet.getDouble(++paramIndex));
                clientDTO.setName(resultSet.getString(++paramIndex));
                clientDTO.setLastName(resultSet.getString(++paramIndex));
                clientDTO.setFatherName(resultSet.getString(++paramIndex));
                clientDTO.setMotherName(resultSet.getString(++paramIndex));
                clientDTO.setGender(resultSet.getString(++paramIndex));
                clientDTO.setDateOfBirth(resultSet.getString(++paramIndex));
                clientDTO.setWebAddress(resultSet.getString(++paramIndex));
                clientDTO.setAddress(resultSet.getString(++paramIndex));
                clientDTO.setCity(resultSet.getString(++paramIndex));
                clientDTO.setPostCode(resultSet.getString(++paramIndex));
                clientDTO.setCountry(resultSet.getString(++paramIndex));
                clientDTO.setEmail(resultSet.getString(++paramIndex));
                clientDTO.setFax(resultSet.getString(++paramIndex));
                clientDTO.setPhone(resultSet.getString(++paramIndex));
                clientDTO.setSignature(resultSet.getString(++paramIndex));
                clientDTO.setJobType(resultSet.getInt(++paramIndex));
                clientDTO.setOccupation(resultSet.getString(++paramIndex));
                clientDTO.setContactInfoName(resultSet.getString(++paramIndex));
                clientDTO.setLandLineNumber(resultSet.getString(++paramIndex));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        } finally {
            conn.dbClose();
        }
        return clientDTO;
    }

    public ClientDetails getClientDetails(long clientId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(ClientDetails.class, new ClientDetailsConditionBuilder()
                .Where()
                .clientIdEquals(clientId)
                .getCondition()
        ).get(0);
    }


    public ClientDTO getClientByNIXApplicationId(long applicationId) {
        ClientDTO clientDTO = null;
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_CLIENT_INFO_BY_NIX_APPLICATION);
            preparedStatement.setLong(1, applicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                int paramIndex = 0;
                clientDTO = new ClientDTO();
                clientDTO.setApplicationId(resultSet.getLong(++paramIndex));
                clientDTO.setClientId(resultSet.getLong(++paramIndex));
                clientDTO.setDemandNoteId(resultSet.getLong(++paramIndex));
                clientDTO.setLoginName(resultSet.getString(++paramIndex));
                clientDTO.setBalance(resultSet.getDouble(++paramIndex));
                clientDTO.setName(resultSet.getString(++paramIndex));
                clientDTO.setLastName(resultSet.getString(++paramIndex));
                clientDTO.setFatherName(resultSet.getString(++paramIndex));
                clientDTO.setMotherName(resultSet.getString(++paramIndex));
                clientDTO.setGender(resultSet.getString(++paramIndex));
                clientDTO.setDateOfBirth(resultSet.getString(++paramIndex));
                clientDTO.setWebAddress(resultSet.getString(++paramIndex));
                clientDTO.setAddress(resultSet.getString(++paramIndex));
                clientDTO.setCity(resultSet.getString(++paramIndex));
                clientDTO.setPostCode(resultSet.getString(++paramIndex));
                clientDTO.setCountry(resultSet.getString(++paramIndex));
                clientDTO.setEmail(resultSet.getString(++paramIndex));
                clientDTO.setFax(resultSet.getString(++paramIndex));
                clientDTO.setPhone(resultSet.getString(++paramIndex));
                clientDTO.setSignature(resultSet.getString(++paramIndex));
                clientDTO.setJobType(resultSet.getInt(++paramIndex));
                clientDTO.setOccupation(resultSet.getString(++paramIndex));
                clientDTO.setContactInfoName(resultSet.getString(++paramIndex));
                clientDTO.setLandLineNumber(resultSet.getString(++paramIndex));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        } finally {
            conn.dbClose();
        }
        return clientDTO;
    }
}
