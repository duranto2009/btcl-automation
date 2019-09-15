/**
 * 
 */
package forgetPassword;

import common.ClientDTO;
import common.ModuleConstants;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import mail.MailDTO;
import mail.MailSend;
import org.apache.commons.lang3.StringUtils;
import util.SqlGenerator;
import vpn.client.ClientDAO;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alam
 */
public class ForgetPasswordUtility {

	//This array will be used for making hex numbers
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static int crypto_rand_secure(int min, int max) {
		int range = max - min;
		int rnd = 0;
		if (range < 0)
			return min;
		double log = Math.log10(range) / Math.log10(2);
		int bytes = (int) (log / 8) + 1;
		int bits = (int) log + 1;
		int filter = (int) (1 << bits) - 1;
		do {
			SecureRandom random = new SecureRandom();
			byte byt[] = new byte[bytes];
			random.nextBytes(byt);
			String hex = bytesToHex(byt);
			rnd = Integer.parseInt(hex, 16);
			rnd = rnd & filter;

		} while (rnd >= range);
		return min + rnd;
	}

	/**
	 * This method returns a random token of given length.
	 * String will contain only alphanumeric character
	 * @author Alam
	 * @param length Length of the token
	 * @return a string of size of given length
	 */
	
	public static String getPasswordResetToken(int length) {
		
		StringBuilder token = new StringBuilder(length);
		
		String alphabet = "0123456789";
		
		for (int i = 0; i < length; i++) {
			token.append(alphabet.charAt(crypto_rand_secure(0, alphabet.length())));
		}
		
		return token.toString();
	}
	
	
	/**
	 * This method sends a mail to a user to reset password
	 * @author Alam
	 * @param host host of mail server of sender (System mail)
	 * @param user username of mail (System mail username)
	 * @param pass password of mail (System mail password)
	 * @param to mail address of user whom we will send
	 * @param token secret token 
	 * @param username username of user to whom mail is sent
	 * @param identifierGiven 
	 * @throws Exception 
	 */
	public static void sendMail( List<String> to, String token, String username, String identifierGiven, HttpServletRequest request) throws Exception{
		
		ServletContext context = request.getServletContext();
		
		String systemURL = request.getScheme() + "://" + request.getServerName() +
							":" + request.getServerPort() + request.getContextPath();
		
		String mailList = "";
		
		for( String mail: to ){
			
			mailList += mail +",;";
		}
		
		MailDTO mailDTO = new MailDTO();
		mailDTO.mailSubject = "Password reset link for BTCL";
		mailDTO.toList = mailList;
		mailDTO.isHtmlMail = false;
		mailDTO.msgText = "To reset your password for username '" + username + 
						"' please visit this link " + systemURL + 
						"/ForgetPassword.do?method=resetPassword&token=" + token + 
						"&username=" + username + "&identifierGiven=" + identifierGiven +
						"\n\nIf you didn't request for anything like this, please ignore the message. " + 
						"Your password won't be changed.\n\n" +
						"This link will become inactive after five minutes\n\n" +
						"Don't reply to this mail. It's an auto generated mail";
		
		MailSend mailSend = MailSend.getInstance();
		mailSend.sendMailWithContentAndSubject(mailDTO);
		System.out.println("mail sent successfully...");   
	}

	/**
	 * This method returns a list of mail address of a client from username
	 * @author Alam
	 * @param usernameOrEmail username for which mail addresses are searched. 
	 * @return A List of string containing mail addresses. If no mail address is found, empty list is returned.
	 * @throws Exception Exception is thrown when no client is found with given username
	 */
	public static List<String> getClientEmailFromUsername( String username ) throws Exception {
		
		//Get client dto from username
		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientLoginName( username );
		
		if( clientDTO == null ){
			
			throw new Exception( "Username doesn't exist" );
		}
		
		List<String> mailList = new ArrayList<String>();
		
		ClientDAO dao = new ClientDAO();
		DatabaseConnection db = new DatabaseConnection();
		db.dbOpen();
		
		for(Integer i: ModuleConstants.mapOfModuleNameToActiveModuleID.keySet()){
			ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientDTO.getClientID(), i );
			if( clientDetailsDTO == null ) {
				continue;
			}
			List<ClientContactDetailsDTO> details = dao.getVpnContactDetailsListByClientID( clientDetailsDTO.getId(), db );
			if( details == null || details.size() == 0 ) {
				continue;
			}
			if( !StringUtils.isEmpty( details.get(0).getEmail() )){
				mailList.add( details.get(0).getEmail() );
			}
					
		}
		db.dbClose();
		return mailList;
	}
	
	/**
	 * This method returns a clientDTO associated with a given email address.
	 * @author Alam
	 * @param email Email address to search for
	 * @return ClientDTO
	 */
	public static ClientDTO getClientDTOFromEmail( String email ){
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			
			String conditionString = " where " + SqlGenerator.getColumnName( ClientContactDetailsDTO.class, "email" ) + " = ? limit 1 ";
			
			databaseConnection.dbOpen();
			
			List<ClientContactDetailsDTO> contactDetailsDTO = (List<ClientContactDetailsDTO>) SqlGenerator.getAllObjectList( ClientContactDetailsDTO.class, databaseConnection, conditionString, email );
			
			if( contactDetailsDTO == null || contactDetailsDTO.size() == 0 )
				return null;
			
			ClientContactDetailsDTO clientContactDetailsDTO =  contactDetailsDTO.get(0);
			
			long vpnClientID = clientContactDetailsDTO.getVpnClientID();
			
			conditionString = " where " + SqlGenerator.getColumnName( ClientDetailsDTO.class, "id" ) + " = ? limit 1 ";
			
			List<ClientDetailsDTO> clientDetailsDTOs = (List<ClientDetailsDTO>) SqlGenerator.getAllObjectList( ClientDetailsDTO.class, databaseConnection, conditionString, vpnClientID );
			
			ClientDetailsDTO clientDetailsDTO = clientDetailsDTOs.get(0);
			
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( clientDetailsDTO.getClientID() );
			
			if( clientDTO != null )
				return clientDTO;
			
		}
		catch( Exception ex ){
			
			System.out.println( ex );
		}
		finally {
			
			databaseConnection.dbClose();
		}
		return null;
	}

	/**
	 * This method checks if a given username and token pair is valid.
	 * @author Alam
	 * @param username
	 * @param token
	 * @return true if valid. Otherwise false
	 * @throws Exception 
	 */
	public static boolean verifyUsernameAndToken(String username, String token) throws Exception {
		
		long tokenExpireTime = 5*60*1000;  //Time in milli seconds
		
		try{
			
			ForgetPassword forgetPassword = ForgetPassword.getByUsername( username );
			
			if( forgetPassword == null ){
				
				return false;
			}
			else if( forgetPassword.getLastModificationTime() < ( System.currentTimeMillis() - tokenExpireTime ) ){
				
				return false;
			}
			else if( forgetPassword.getToken().equals( token ) && forgetPassword.getUsername().equals( username )){
				
				return true;
			}
			else{
				
				return false;
			}
		}
		catch( Exception e ){
			
			throw e;
		}
	}

	/**
	 * This method inserts a username and token pair in vbforgetpasswordauth table.
	 * If there is already an entry with same username, token will be updated at that entry and timestamp will be set
	 * otherwise a new row will be inserted.
	 * @author Alam
	 * @param usernameOrEmail
	 * @param token
	 * @throws Exception Throws exception if any database error occured.
	 */
	public static void insertTokenAndUsername(String usernameOrEmail, String token) throws Exception {
		
		ForgetPassword forgetPassword = null;
		
		//If token for this user already exists then update. otherwise insert a new row
		forgetPassword = ForgetPassword.getByUsername( usernameOrEmail );
		
		if( forgetPassword != null ){
			try{
				
				forgetPassword.setToken( token );
				forgetPassword.setLastModificationTime( System.currentTimeMillis() );
				
				forgetPassword.update();
			}
			catch( Exception e ){
				
				System.out.println(  e );
				throw e;
			}
		}
		else{
			
			forgetPassword = new ForgetPassword();
			forgetPassword.setUsername( usernameOrEmail );
			forgetPassword.setToken( token );
			forgetPassword.setLastModificationTime( System.currentTimeMillis() );
			
			try{
				
				forgetPassword.insert();
			}
			catch( Exception e ){
				
				System.out.println(  e );
				throw e;
			}
		}
	}
	
	/**
	 * This method update user password and remove entry from token table.
	 * this two operation are atomic. If one transaction fails, operation will roleback
	 * @author Alam
	 * @param clientDTO
	 * @param username
	 */
	public static void updatePassword( ClientDTO clientDTO, String username ){
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ClientDAO vpnDAO = new ClientDAO();
		
		try {
			
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			if( clientDTO == null )
				throw new Exception( "ClientDTO is null" );	
				
			vpnDAO.updateNewClient(clientDTO, databaseConnection);
			ForgetPassword.remove( username, databaseConnection );
			
			databaseConnection.dbTransationEnd();

		}
		catch (Exception ex) {
			
			System.out.println( ex );
			try {

				databaseConnection.dbTransationRollBack();
			} 
			catch (Exception ex2) {
				
				System.out.println( ex2 );
			}
		}
		finally {
			
			databaseConnection.dbClose();
		}
	}
}
