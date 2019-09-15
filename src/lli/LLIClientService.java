package lli;

import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.Transactional;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import common.ClientDTO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.RoleConstants;
import common.repository.AllClientRepository;
import lli.Application.ChangeBillingAddress.LLIChangeBillingAddressApplication;
import login.LoginDTO;
import request.StateRepository;
import user.UserDTO;
import user.UserRepository;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientForm;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.*;
import java.util.stream.Collectors;

public class LLIClientService {

	List<LLIDropdownPair> getAllClientListByPartialNameByAndModuleIdPair(String partialName, int moduleId) {
		if(partialName.isEmpty())return Collections.emptyList();
		return AllClientRepository.getInstance().getAllVpnCleint()
				.stream()
				.filter(t->t.getModuleID() == moduleId)
				.filter(t->t.getLoginName().startsWith(partialName))
				.filter(t->isModuleClientActive(t, moduleId))
				.map(t->new LLIDropdownPair(t.getClientID(), t.getLoginName(), t.getRegistrantType()))
				.collect(Collectors.toList());
	}

	List<LLIDropdownClient> getAllClientListByPartialNameByAndModuleIdClient(String partialName, int moduleId) {
		if(partialName.isEmpty())return Collections.emptyList();
		return AllClientRepository.getInstance().getAllVpnCleint()
				.stream()
				.filter(t->t.getModuleID() == moduleId)
				.filter(t->t.getLoginName().startsWith(partialName))
				.filter(t->isModuleClientActive(t, moduleId))
				.map(t->new LLIDropdownClient(t.getClientID(), t.getLoginName(), t.getRegistrantType()))
				.collect(Collectors.toList());
	}

	private boolean isModuleClientActive(ClientDTO clientDTO, int moduleId) {
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance()
				.getModuleClientByClientIDAndModuleID(clientDTO.getClientID(), moduleId);
		if (clientDetailsDTO != null) {
			return isActiveClient(clientDetailsDTO);
		}
		return false;
	}

	@SuppressWarnings("serial")
	List<LLIDropdownPair> getClientListByPartialName(String partialName, LoginDTO loginDTO) {
		if(loginDTO.getIsAdmin()) {
			return getAllClientListByPartialNameByAndModuleIdPair(partialName, ModuleConstants.Module_ID_LLI);
		} else {
			return Collections.singletonList(new LLIDropdownPair(loginDTO.getAccountID(), loginDTO.getUsername()));
		}
	}


	@SuppressWarnings("serial")
	List<LLIDropdownClient> getClientListByPartialNameByResistantType(String partialName, LoginDTO loginDTO) {
		if(loginDTO.getIsAdmin()) {
			return getAllClientListByPartialNameByAndModuleIdClient(partialName, ModuleConstants.Module_ID_LLI);
		} else {
			return Collections.singletonList(new LLIDropdownClient(loginDTO.getAccountID(), loginDTO.getUsername(), -1));
		}
	}

	@SuppressWarnings("serial")
	public List<ClientDetailsDTO> getAllLLIClient() {
			
		List<ClientDetailsDTO> clientList = new ArrayList<>();
		
		ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();
		
		for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList) 
		{
			if(isActiveLLIClient(clientDetailsDTO)
					&& clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_LLI)
			{
				clientList.add(clientDetailsDTO);
			}
		}
		return clientList;
	}
	
	
	@SuppressWarnings("serial")
	public List<ClientDetailsDTO> getLLIClients(Collection recordIDs) {
			
		List<ClientDetailsDTO> clientList = new ArrayList<>();
		
		ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();
		
		for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList) 
		{
			if(isActiveLLIClient(clientDetailsDTO)
					&& clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_LLI
					&& recordIDs.contains(clientDetailsDTO.getClientID()))
			{
				clientList.add(clientDetailsDTO);
			}
		}
		return clientList;
	}
	
	
	public ClientDetailsDTO getLLIClient(long clientId) {
		
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientId, ModuleConstants.Module_ID_LLI);
		
		if(clientDetailsDTO!=null) {
			if (isActiveLLIClient(clientDetailsDTO)
					&& clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_LLI) {
				return clientDetailsDTO;
			}
		}
		
		return null;
	}

	private boolean isActiveClient(ClientDTO clientDTO) {
		return StateRepository.getInstance().getStateDTOByStateID(clientDTO.getCurrentStatus())
				.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE;
	}

	private boolean isActiveLLIClient(ClientDTO clientDTO) {
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientDTO.getClientID(), ModuleConstants.Module_ID_LLI);
		if (clientDetailsDTO != null) {
			return isActiveClient(clientDetailsDTO);
		}
		return false;
	}



	public List<LLIDropdownPair> getUserListByPartialName(String partialName, LoginDTO loginDTO) {
		List<LLIDropdownPair> userList = new ArrayList<LLIDropdownPair>();
		List<UserDTO> allUserList = UserRepository.getInstance().getUserList();
		
		for(UserDTO user : allUserList) {
			userList.add(new LLIDropdownPair(user.getUserID(), user.getUsername()));
		}
		
		return userList;
	}

	public List<LLIDropdownPair> getVendorList( LoginDTO loginDTO) {
		List<LLIDropdownPair> userList = new ArrayList<LLIDropdownPair>();
		Set<UserDTO> allUserList =  UserRepository.getInstance().getUsersByRoleID(RoleConstants.VENDOR_ROLE);

		for(UserDTO user : allUserList) {
			userList.add(new LLIDropdownPair(user.getUserID(), user.getUsername()));
		}

		return userList;
	}
	
	public LLIDropdownPair getClientByID(Long clientID, LoginDTO loginDTO) {
		if(!loginDTO.getIsAdmin() || clientID == null) {
			return new LLIDropdownPair(loginDTO.getAccountID(), loginDTO.getUsername());
		}
		return new LLIDropdownPair(clientID, AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName());
	}

	/**
	 * @author Dhrubo
	 */
	@Transactional
	public void changeBillingAddress(LLIChangeBillingAddressApplication lliChangeBillingAddressApplication) throws Exception {
		ClientDetailsDTO lliClientDetailsDTO =  AllClientRepository.getInstance().getVpnClientByClientID(lliChangeBillingAddressApplication.getClientID(), ModuleConstants.Module_ID_LLI);
		
		ClientContactDetailsDTO clientBillingContanctDetailsDTO = ModifiedSqlGenerator.getAllObjectList(ClientContactDetailsDTO.class,
				" where vclcVpnClientID = " + lliClientDetailsDTO.getId() + " and vclcDetailsType = " + ClientContactDetailsDTO.BILLING_CONTACT).get(0);
		
		clientBillingContanctDetailsDTO.setRegistrantsName(lliChangeBillingAddressApplication.getFirstName());
		clientBillingContanctDetailsDTO.setRegistrantsLastName(lliChangeBillingAddressApplication.getLastName());
		clientBillingContanctDetailsDTO.setEmail(lliChangeBillingAddressApplication.getEmail());
		clientBillingContanctDetailsDTO.setPhoneNumber(lliChangeBillingAddressApplication.getMobileNumber());
		clientBillingContanctDetailsDTO.setLandlineNumber(lliChangeBillingAddressApplication.getTelephoneNumber());
		clientBillingContanctDetailsDTO.setFaxNumber(lliChangeBillingAddressApplication.getFaxNumber());
		clientBillingContanctDetailsDTO.setCity(lliChangeBillingAddressApplication.getCity());
		clientBillingContanctDetailsDTO.setPostCode(lliChangeBillingAddressApplication.getPostCode());
		clientBillingContanctDetailsDTO.setAddress(lliChangeBillingAddressApplication.getAddress());
		ModifiedSqlGenerator.updateEntity(clientBillingContanctDetailsDTO);
				
	}

	/**
	 * @author Dhrubo
	 */
	@Transactional(transactionType=TransactionType.READONLY)
	public List<Map<String, String>> getAccountListByClientID(long clientID) throws Exception {
		AccountingEntryService accountingEntryService = ServiceDAOFactory.getService(AccountingEntryService.class);
		
		List<Map<String, String>> accountList = new ArrayList<>();
		
		//Adding Adjustable
		Map<String,String> adjustableAccount = new HashMap<>();
		adjustableAccount.put("name", "Adjustable");
		adjustableAccount.put("balance", String.valueOf(accountingEntryService.getBalanceByClientIDAndAccountID(clientID, AccountType.ADJUSTABLE.getID())));
		accountList.add(adjustableAccount);
		
		//Adding Security
		Map<String,String> securityAccount = new HashMap<>();
		securityAccount.put("name", "Security");
		securityAccount.put("balance", String.valueOf(accountingEntryService.getBalanceByClientIDAndAccountID(clientID, AccountType.SECURITY.getID())));
		accountList.add(securityAccount);
		
		return accountList;
	}

	/**
	 * @author Dhrubo
	 */
	public long validateOwnClientID(long clientID, LoginDTO loginDTO) {
		if(loginDTO.getIsAdmin()) {
			return clientID;
		} else {
			return loginDTO.getAccountID();
		}
	}
	/**
	 * @author Dhrubo
	 */
	public Map<String, String> getClientDetailsByClient(long clientID) {
		Map<String, String> clientDetails = new HashMap<>();
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID, ModuleConstants.Module_ID_LLI);
		clientDetails.put("clientID", String.valueOf(clientID));
		clientDetails.put("loginName", clientDetailsDTO.getLoginName());
		clientDetails.put("registrantType", RegistrantTypeConstants.RegistrantTypeName.get(clientDetailsDTO.getRegistrantType()));
		clientDetails.put("registrantCategory", RegistrantCategoryConstants.RegistrantCategoryName.get(clientDetailsDTO.getRegistrantCategory()));
		clientDetails.put("clientType", ClientForm.CLIENT_TYPE_STR.get(clientDetailsDTO.getClientCategoryType()));
		return clientDetails;
	}
	
}
