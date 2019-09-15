package vpn.client;

import common.ClientDTO;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import mail.MailDTO;
import mail.MailSend;
import org.apache.log4j.Logger;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.ArrayList;
import java.util.List;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;

/**
 * This class contains utility functionality for client common
 * @author Alam
 */
public class ClientUtility {
	
	public static Logger logger= Logger.getLogger(ClientUtility.class);

	/**
	 * This method sends an account verification link to a specified mail address 
	 * @author Alam
	 * @param to whom to send the mail
	 * @param username username claimed by the user
	 * @param token random token
	 * @param remoteAddress address of system the application running
	 */
	public static void sendEmailVerificationMail( String to, String username, String token, String remoteAddress ){
		
		MailDTO mailDTO = new MailDTO();
		try{
			mailDTO.mailSubject = "Account verification mail from BTCL";
			mailDTO.toList = to;
			mailDTO.isHtmlMail = false;
			mailDTO.msgText = "Please click the below link to verify your email address-\n"
					+ remoteAddress + "/api/client.do?method=verifyEmail&username=" + to + "&token=" + token;
			
			MailSend mailSend = MailSend.getInstance();
			mailSend.sendMailWithContentAndSubject(mailDTO);
			
			logger.debug("mail sent successfully...");   
		}catch(Exception ex){
			logger.fatal("", ex);
		}
		
	}



	public static void sendEmailVerificationMailTemporaryClient( String to, String username, String token, String remoteAddress, String id ){

		MailDTO mailDTO = new MailDTO();
		try{
			mailDTO.mailSubject = "Account verification mail from BTCL";
			mailDTO.toList = to;
			mailDTO.isHtmlMail = false;
			mailDTO.msgText = "Please click the below link to verify your email address-\n"
					+ remoteAddress + "/api/client.do?method=verifyEmailTemporaryClient&username=" + to + "&token=" +
					token + "&clientId=" +id;

			MailSend mailSend = MailSend.getInstance();
			mailSend.sendMailWithContentAndSubject(mailDTO);

			logger.debug("mail sent successfully...");
		}catch(Exception ex){
			logger.fatal("", ex);
		}

	}


	/**
	 * This method returns a list of vpn contact details object that has the given email address.
	 * @author Alam
	 * @param email email, that is being searched.
	 * @return List of vpn contact details, if not found returns null.
	 * @throws Exception
	 */
	public static List<ClientContactDetailsDTO> getVpnContactDetailsByEmail( String email ) throws Exception{
		
		DatabaseConnection db = new DatabaseConnection();
		String whereClause = " where " + getColumnName( ClientContactDetailsDTO.class, "email" ) + " = '" + email + "'";
		
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = null;
		
		try{
			db.dbOpen();
			db.dbTransationStart();
			
			clientContactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, db, whereClause );
			
			db.dbTransationEnd();
		}
		catch( Exception ex ){
			System.out.println( ex.toString() );
			
			try {
				
				db.dbTransationRollBack();
				
			} 
			catch (Exception ex2) {
				
			}
			
			if(ex instanceof RequestFailureException){
				
				throw (RequestFailureException)ex;
			}
			
			throw ex;
		} 
		finally {
				
			db.dbClose();
		}
		return clientContactDetailsDTOs;
	}
	
	public static List<ClientDetailsDTO> getVpnVpnClientByIdentityNo(String identityNo) throws Exception {
		DatabaseConnection db = new DatabaseConnection();
		String whereClause = " where " + getColumnName(ClientDetailsDTO.class, "identityNo") + " = '" + identityNo + "'";
		List<ClientDetailsDTO> ClientDetailsDTOs = null;

		try {
			db.dbOpen();
			db.dbTransationStart();
			ClientDetailsDTOs = (List<ClientDetailsDTO>) getAllObjectList(ClientDetailsDTO.class, db, whereClause);
			db.dbTransationEnd();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			try {
				db.dbTransationRollBack();
			} catch (Exception ex2) {

			}

			if (ex instanceof RequestFailureException) {
				throw (RequestFailureException) ex;
			}
			throw ex;
		} finally {
			db.dbClose();
		}
		return ClientDetailsDTOs;
	}

	/**
	 * This method returns a list of vpn contact details object that has the given Phone No.
	 * @author Alam
//	 * @param phone No Phone no, that is being searched.
	 * @return List of vpn contact details, if not found returns null.
	 * @throws Exception
	 */
	public static List<ClientContactDetailsDTO> getVpnContactDetailsByPhoneNo( String phoneNo ) throws Exception{
		
		DatabaseConnection db = new DatabaseConnection();
		List<ClientContactDetailsDTO> contactDetailsDTOs = null;
		
		try {

			// This removes all '+','-',' ' characters from the data of phone
			// number column if any. It is done
			// to remove any country code formatter.
			String whereClause = " where  replace(replace( replace("
					+ getColumnName(ClientContactDetailsDTO.class, "phoneNumber") + ",'+',''), '-', ''),' ','')  = '"
					+ phoneNo + "'";

			db.dbOpen();
			db.dbTransationStart();

			contactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, db, whereClause);

			db.dbTransationEnd();

		} catch (Exception ex) {

			System.out.println(ex.toString());

			try {

				db.dbTransationRollBack();

			} catch (Exception ex2) {

			}
			if (ex instanceof RequestFailureException) {

				throw (RequestFailureException) ex;
			}
		} finally {

			db.dbClose();
		}

		return contactDetailsDTOs;
	}
	
	
	/**
	 * This method first verify a given username/email and token pair. if verified then update the email veridied
	 * column in vpn contact details table.
	 * @author Alam
	 * @param email Email that is being verified
	 * @throws Exception
	 */
	public static void verifyMail(String email) throws Exception {
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = getVpnContactDetailsByEmail(email);
		DatabaseConnection db = new DatabaseConnection();
		ClientDAO vpnDao = new ClientDAO();

		if (clientContactDetailsDTOs == null) {
			throw new Exception("No entry with this email");
		}

		try {

			db.dbOpen();
			db.dbTransationStart();

			for (int i = 0; i < clientContactDetailsDTOs.size(); i++) {

				ClientContactDetailsDTO tempVpnContactDetails = clientContactDetailsDTOs.get(i);
				tempVpnContactDetails.setIsEmailVerified(1);

				vpnDao.updateClientContactDetailsDTO(tempVpnContactDetails, db);
			}

			db.dbTransationEnd();
		} catch (Exception ex) {
			System.out.println(ex.toString());

			try {

				db.dbTransationRollBack();

			} catch (Exception ex2) {

			}

			if (ex instanceof RequestFailureException) {

				throw (RequestFailureException) ex;
			}

			throw ex;
		} finally {

			db.dbClose();
		}
	}
	
	/**
	 * This method first verify a given username/phoneNo and token pair. if verified then update the phone no verified
	 * column in vpn contact details table.
	 * @author Alam
	 * @param phoneNo PhoneNo that is being verified
	 * @throws Exception
	 */
	public static void verifyPhone( String phoneNo ) throws Exception {
		
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = getVpnContactDetailsByPhoneNo( phoneNo );
		
		DatabaseConnection db = new DatabaseConnection();
		ClientDAO vpnDao = new ClientDAO();

		if (clientContactDetailsDTOs == null) {
			throw new Exception("No entry with this Phone no");
		}

		try {

			db.dbOpen();
			db.dbTransationStart();

			for (int i = 0; i < clientContactDetailsDTOs.size(); i++) {

				ClientContactDetailsDTO tempVpnContactDetails = clientContactDetailsDTOs.get(i);
				tempVpnContactDetails.setIsPhoneNumberVerified(1);

				vpnDao.updateClientContactDetailsDTO(tempVpnContactDetails, db);
			}

			db.dbTransationEnd();
		} catch (Exception ex) {
			System.out.println(ex.toString());

			try {

				db.dbTransationRollBack();

			} catch (Exception ex2) {

			}

			if (ex instanceof RequestFailureException) {

				throw (RequestFailureException) ex;
			}

			throw ex;
		} finally {

			db.dbClose();
		}
	}
	
	/**
	 * Get a list of client by phone a given phone number. This method will remove any '+' or '-' character 
	 * in the phone number.
	 * @author Alam
	 * @param phoneNo Phone no to serach with
	 * @return list of ClientDTO, if no client found, empty list will be returned.
	 */
	public static List<ClientDTO> getByPhoneNumber(String phoneNo) {

		DatabaseConnection db = new DatabaseConnection();
		List<ClientContactDetailsDTO> contactDetailsDTOs = null;
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>();
		try {

			// This removes all '+','-',' ' characters from the data of phone
			// number column if any. It is done
			// to remove any country code formatter.
			String whereClause = " where  replace(replace( replace("
					+ getColumnName(ClientContactDetailsDTO.class, "phoneNumber") + ",'+',''), '-', ''),' ','')  = '"
					+ phoneNo + "'";

			db.dbOpen();
			db.dbTransationStart();

			contactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, db,
					whereClause);

			for (ClientContactDetailsDTO dto : contactDetailsDTOs) {

				ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance()
						.getVpnClientByVpnClientID(dto.getVpnClientID());
				if (clientDetailsDTO == null)
					continue;

				ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientDetailsDTO.getClientID());
				if (clientDTO == null)
					continue;

				clientDTOs.add(clientDTO);
				break;
			}

			db.dbTransationEnd();

		} catch (Exception ex) {

			System.out.println(ex.toString());

			try {

				db.dbTransationRollBack();

			} catch (Exception ex2) {

			}
			if (ex instanceof RequestFailureException) {

				throw (RequestFailureException) ex;
			}
		} finally {

			db.dbClose();
		}

		return clientDTOs;
	}
	
	/**
	 * @author dhrubo
	 */
	public static List<ClientDTO> getClientDTOListByPhoneNumber(String phoneNumber) {

		DatabaseConnection db = new DatabaseConnection();
		List<ClientContactDetailsDTO> contactDetailsDTOs = null;
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>();
		try {

			String whereClause = " where " + getColumnName(ClientContactDetailsDTO.class, "phoneNumber") + " = '" + phoneNumber + "'";

			db.dbOpen();
			db.dbTransationStart();

			contactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, db, whereClause);

			for (ClientContactDetailsDTO dto : contactDetailsDTOs) {

				ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(dto.getVpnClientID());
				if (clientDetailsDTO == null)
					continue;

				ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientDetailsDTO.getClientID());
				if (clientDTO == null)
					continue;

				clientDTOs.add(clientDTO);
				break;
			}

			db.dbTransationEnd();
		} catch (Exception ex) {
			try {
				db.dbTransationRollBack();
			} catch (Exception ex2) {}
			if (ex instanceof RequestFailureException) {
				throw (RequestFailureException) ex;
			}
		} finally {
			db.dbClose();
		}
		return clientDTOs;
	}

	public static List<ClientDTO> getClientDTOListByPhoneNumberAndClientID(String phoneNumber, long clientID) {
		
		List<ClientDTO> clientDTOs = getClientDTOListByPhoneNumber(phoneNumber);
		List<ClientDTO> clientDTOListWithoutThisClient = new ArrayList<>();
		
		for(ClientDTO clientDTO : clientDTOs) {
			if(clientDTO.getClientID() != clientID) {
				clientDTOListWithoutThisClient.add(clientDTO);
			}
		}
		return clientDTOListWithoutThisClient;
	}
}
