package util;

import common.repository.AllClientRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import user.UserDTO;
import user.UserRepository;
import vpn.client.ClientDAO;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.client.ClientUtility;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Validator {
	private static ClientDAO clientDAO= new ClientDAO();
	static Logger logger = Logger.getLogger(Validator.class);
	private final static String mobileRegexStr = "^[\\+]?[(]?[0-9\\-]{3}[)]?[-\\s\\.]?[0-9\\-]{3}[-\\s\\.]?[0-9\\-]{3,15}$";

	private Validator() {
		// restrict from creating instance
	}

	/**
	 * @author dhrubo
	 */
	public static boolean isUsedUserName(String username) {
		for(ClientDetailsDTO clientDetailsDTO : AllClientRepository.getInstance().getAllVpnCleint()) {
			if(clientDetailsDTO.getLoginName().equalsIgnoreCase(username)) {
				return true;
			}
		}
		for(UserDTO userDTO : UserRepository.getInstance().getUserList()) {
			if (userDTO.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
		
	}

	public static boolean isUserNameUsedByClientID(String username, long vpnClientID) {
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(vpnClientID);
		if (clientDetailsDTO.getClientID() == vpnClientID) {
			return true;
		}
		if (UserRepository.getInstance().getUserDTOByUserName(username) != null) {
			return false;
		}
		return false;
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public static boolean isEmpty(String emailStr) {
		if (StringUtils.isEmpty(emailStr)) {
			return true;
		}
		return false;
	}

	public static boolean isValidMobile(String mobileNumber) {
		if (mobileNumber.matches(mobileRegexStr)) {
			return true;
		}
		return false;
	}

	public static boolean isValidURL(String url) {
		String[] schemes = { "http", "https" }; // DEFAULT schemes = "http", //
												// "https", "ftp"
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (urlValidator.isValid(url)) {
			return true;
		}
		return false;
	}

	public static boolean isEmailUsed(String email,long clientID) throws Exception {
		List<ClientContactDetailsDTO> listOfContactDetails = ClientUtility.getVpnContactDetailsByEmail(email);

		ClientDetailsDTO clientDetailsDTO = null;
		for (ClientContactDetailsDTO contactDTO : listOfContactDetails) {
			if (contactDTO.getDetailsType() == ClientContactDetailsDTO.REGISTRANT_CONTACT) {
				clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(contactDTO.getVpnClientID());
				if (clientDetailsDTO != null && !clientDAO.isClientDiscarded(clientDetailsDTO) && clientDetailsDTO.getClientID()!=clientID) { // if client is not discarded
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isEmailUsedByClientID(String email, long clientID) throws Exception {
		ArrayList<Long> listOfVpnClientID = AllClientRepository.getInstance().getVpnClientByClientID(clientID);
		for (Long vpnClientID : listOfVpnClientID) {
			ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(vpnClientID);
			// int
			// lengthOfContactDetails=clientDetailsDTO.getVpnContactDetails().size();
			try {
				if (email.equals(clientDetailsDTO.getVpnContactDetails().get(0).getEmail())) {
					return true;
				}
			} catch (Exception ex) {
				logger.fatal("", ex);
			}
		}
		return false;
	}
	
	public static boolean isEmailUsedByClientIDByDB(String email, long clientID) throws Exception {
		ArrayList<ClientDetailsDTO> listOfVpnClientID =(ArrayList<ClientDetailsDTO>) new ClientService().getClientDetailsDTOListByClientIDAndEmail(clientID, email);
		logger.info("validator email: "+ listOfVpnClientID);
		if(listOfVpnClientID.size()>0){
			return true;
		}
		return false;
	}

	public static boolean isIdentityNoUsed(String identityNo) throws Exception {
		if (StringUtils.isBlank(identityNo)) {
			return false;
		}
		HashSet<Long> uniqueClientIDs = new HashSet<Long>();
		List<ClientDetailsDTO> listOfVpnClients = ClientUtility.getVpnVpnClientByIdentityNo(identityNo);

		for (ClientDetailsDTO clientDetailsDTO : listOfVpnClients) {
			if(!clientDAO.isClientDiscarded(clientDetailsDTO)){ // if client is not discarded
				uniqueClientIDs.add(clientDetailsDTO.getClientID());
			}
		}
		logger.debug("Client ID matched: " + uniqueClientIDs.size());

		if (uniqueClientIDs.size() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isIdentityNoUsedByClientID(String identityNo, long clientID) throws Exception {
		if (StringUtils.isBlank(identityNo)) {
			return false;
		}
		ArrayList<Long> listOfVpnClientID = AllClientRepository.getInstance().getVpnClientByClientID(clientID);
		for (Long vpnClientID : listOfVpnClientID) {
			ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByVpnClientID(vpnClientID);
			try {
				for(String identity : clientDetailsDTO.getIdentity().split(",")) {
					if (identityNo.equals((identity.split(":")[1]))) {
						return true;
					}
				}
			} catch (Exception ex) {
				logger.fatal("", ex);
				return true;
			}
		}
		return false;
	}

	public static String getPurifiedDomain(String domainName){
		if(StringUtils.isNotBlank(domainName)){
			return domainName.replaceAll("\\s+","").toLowerCase();
		}
		return "";
	}
	
	public static String test(){
		  return "hello world 1 Ok";
	}

	/**
	 * @author dhrubo
	 */
	public static boolean isMobileUsed(String phoneNumber) {
		return ClientUtility.getClientDTOListByPhoneNumber(phoneNumber).size() > 0 ? true : false;
	}
	
	/**
	 * @author dhrubo
	 */
	public static boolean isMobileUsedByOtherClient(String phoneNumber, long clientID) {
		return ClientUtility.getClientDTOListByPhoneNumberAndClientID(phoneNumber, clientID).size() > 0 ? true : false;
	}
	

}
