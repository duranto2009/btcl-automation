package lli.Options;

import client.RegistrantTypeConstants;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import vpn.client.ClientDetailsDTO;

import java.util.ArrayList;
import java.util.List;

@ActionRequestMapping("lli/options")
public class LLIOptionsAction extends AnnotatedRequestMappingAction{

	@RequestMapping(mapping="/connection-type", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getConnectionTypeOptions(@RequestParameter("id") Long clientID) {
		if(clientID == null) {
			return new ArrayList<LLIDropdownPair>();
		}
		
		List<LLIDropdownPair> connectionTypeOptions = new ArrayList<LLIDropdownPair>();
		
		connectionTypeOptions.add(new LLIDropdownPair(LLIConnectionConstants.CONNECTION_TYPE_REGULAR, LLIConnectionConstants.connectionTypeMap.get(LLIConnectionConstants.CONNECTION_TYPE_REGULAR) ));
		connectionTypeOptions.add(new LLIDropdownPair(LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY, LLIConnectionConstants.connectionTypeMap.get(LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY) ));
		connectionTypeOptions.add(new LLIDropdownPair(LLIConnectionConstants.CONNECTION_TYPE_CACHE, LLIConnectionConstants.connectionTypeMap.get(LLIConnectionConstants.CONNECTION_TYPE_CACHE) ));
		
		
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID, ModuleConstants.Module_ID_LLI);
		if(clientDetailsDTO == null) {
			throw new RequestFailureException("Select a valid client");
		}
		
		if(clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT) {
			connectionTypeOptions.add(new LLIDropdownPair(LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG, LLIConnectionConstants.connectionTypeMap.get(LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG) ));
		}
		
		return connectionTypeOptions;
	}
}
