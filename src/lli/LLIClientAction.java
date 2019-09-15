package lli;

import annotation.ForwardedAction;
import client.ClientService;
import com.google.gson.GsonBuilder;
import common.ClientDTO;
import common.ModuleConstants;
import common.repository.AllClientRepository;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import user.UserDTO;
import user.UserService;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ActionRequestMapping("lli/client")
public class LLIClientAction extends AnnotatedRequestMappingAction{
	
	@Service
	LLIClientService lliClientService ;
	@Service
	UserService userService;
	@Service
	LLIConnectionService lliConnectionService;
	@Service
	LLILongTermService lliLongTermService;
	
	
	
	@Override
	public GsonBuilder getGsonBuilder(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		//LLI Connection
		gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionInstanceSerizalizer());
		gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeSerizalizer());
		gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopSerizalizer());
		gsonBuilder.registerTypeAdapter(LLIConnectionInstance.class, new LLIConnectionDeserializer());
		gsonBuilder.registerTypeAdapter(LLIOffice.class, new LLIOfficeDeserializer());
		gsonBuilder.registerTypeAdapter(LLILocalLoop.class, new LLILocalLoopDeserializer());
		
		//LLI Long Term Contract
		gsonBuilder.registerTypeAdapter(LLILongTermContract.class, new LLILongTermSerializer());
		gsonBuilder.registerTypeAdapter(LLILongTermContract.class, new LLILongTermDeserializer());
		return gsonBuilder;
	}
	
	@RequestMapping(mapping="/get-client-old", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getLLIClientOld(@RequestParameter("val") String partialName, LoginDTO loginDTO) {
		return lliClientService.getClientListByPartialName(partialName, loginDTO);
	}

	@RequestMapping(mapping="/get-client", requestMethod=RequestMethod.All)
	public List<LLIDropdownClient> getLLIClient(@RequestParameter("val") String partialName, LoginDTO loginDTO) {
		return lliClientService.getClientListByPartialNameByResistantType(partialName.trim(), loginDTO);
	}

	@RequestMapping(mapping="/get-dest-client", requestMethod=RequestMethod.GET)
	public List<LLIDropdownClient> getDestLLIClient(@RequestParameter("val") String partialName) {
		return lliClientService.getAllClientListByPartialNameByAndModuleIdClient(partialName.trim(), ModuleConstants.Module_ID_LLI);
	}

	@RequestMapping(mapping="/get-all-client-by-id", requestMethod=RequestMethod.GET)
	public List<LLIDropdownClient> getLLIClientByPartialID(@RequestParameter("val") String partialID, LoginDTO loginDTO) {
		if(loginDTO.getAccountID()>0) {
			return Collections.singletonList(new LLIDropdownClient(loginDTO.getAccountID(), loginDTO.getUsername(),-1)); // You wont get Reg type from here
		}

		List<ClientDTO> clients = AllClientRepository.getInstance().getClientDTOsByPartialClientID(partialID, ModuleConstants.Module_ID_LLI);
		return clients.stream()
				.map(t->new LLIDropdownClient(t.getClientID(), t.getLoginName(), ( (ClientDetailsDTO)t).getRegistrantType()))
				.collect(Collectors.toList());

	}
	
	@RequestMapping(mapping="/get-client-by-id", requestMethod=RequestMethod.All)
	public LLIDropdownPair getLLIClientByID(@RequestParameter("id") Long clientID, LoginDTO loginDTO) throws Exception{
		return lliClientService.getClientByID(clientID, loginDTO);
	}
	
	@RequestMapping(mapping="/get-user", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getUserList(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
		return lliClientService.getUserListByPartialName(partialName, loginDTO);
	}

	@RequestMapping(mapping="/get-vendor", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getVendorList( LoginDTO loginDTO) throws Exception{
		return lliClientService.getVendorList(loginDTO);
	}
	
	@RequestMapping(mapping="/get-loop-provider", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getLoopProviderList() throws Exception{
		List<LLIDropdownPair> list = new ArrayList<>();
		List<UserDTO> listOfUserDTO = userService.getLocalLoopProviderList();
		for(UserDTO userDTO : listOfUserDTO) {
			list.add(new LLIDropdownPair(userDTO.getUserID(), userDTO.getFullName()));
		}
		return list;
	}
	
	/*LLI Client Dashboard Begins*/
	@ForwardedAction
	@RequestMapping(mapping="/board", requestMethod=RequestMethod.All)
	public String getLongTermNew() throws Exception {
		return "lli-client-board";
	}
	
	
	
	
	
	
	//ToDo Raihan: Client Validation Annotation begins
	@RequestMapping(mapping="/get-client-details", requestMethod=RequestMethod.All)
	public Map<String, String> getClientDetailsByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
		clientID = lliClientService.validateOwnClientID(clientID, loginDTO);
		return lliClientService.getClientDetailsByClient(clientID);
	}
	@RequestMapping(mapping="/get-contact-details-list", requestMethod=RequestMethod.All)
	public List<ClientContactDetailsDTO> getContactDetailsListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
		clientID = lliClientService.validateOwnClientID(clientID, loginDTO);
		return new ClientService().getClientContactDetailsDTOListByClientIDAndModuleID(clientID, ModuleConstants.Module_ID_LLI);
	}
	@RequestMapping(mapping="/get-connection-list", requestMethod=RequestMethod.All)
	public List<LLIConnectionInstance> getLLIConnectionListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
		clientID = lliClientService.validateOwnClientID(clientID, loginDTO);
		return lliConnectionService.getLLIConnectionInstanceListByClientID(clientID);
	}
	@RequestMapping(mapping="/get-contract-list", requestMethod=RequestMethod.All)
	public List<LLILongTermContract> getLLILongTermContractListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
		clientID = lliClientService.validateOwnClientID(clientID, loginDTO);
		return lliLongTermService.getLLILongTermContractListByClientID(clientID);
	}
	@RequestMapping(mapping="/get-account-list", requestMethod=RequestMethod.All)
	public List<Map<String, String>> getAccountListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
		clientID = lliClientService.validateOwnClientID(clientID, loginDTO);
		return lliClientService.getAccountListByClientID(clientID);
	}
	//ToDo Raihan: Client Validation Annotation ends
	/*LLI Client Dashboard Ends*/
}