package api;

import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import org.apache.log4j.Logger;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientAPI {
	private final Logger logger = Logger.getLogger(getClass());
	private static final ClientAPI clientAPI= new ClientAPI();
	
	private ClientAPI() {}
	public static ClientAPI getInstance() {
		return clientAPI;
	}
	
//	public String getLoginNameByClientId(long clientId) {
//		
//	}
	
	public ClientDetailsDTO getClientDetailsDTOByClientIdAndModuleId(long clientId, int moduleId) {
		logger.info("searching client details by client id: " + clientId+", module id: " + moduleId);
		ClientDetailsDTO clientDetailsDTO = null;
		if(moduleId == 0) {
			for(Map.Entry<Integer, String> entry : ModuleConstants.ActiveModuleMap.entrySet()) {
				clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientId, entry.getKey());
				if(clientDetailsDTO != null) {
					break;
				}
			}

		}else {
		 	clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientId, moduleId );
		}
		if(clientDetailsDTO == null) {
			logger.fatal("No client details found by client id: " + clientId+ ", module id: " + moduleId); 
			throw new RequestFailureException("No Client Details Found with client id: "+ clientId+ ", module id: " + moduleId);
		}
		logger.info("client details found");
		return clientDetailsDTO;
	}
	
	public List<ClientContactDetailsDTO> getListOfClientContactByClientIdAndModuleId
				(long clientId, int moduleId) throws Exception {
		
		logger.info("searching client contacts by client id: " + clientId);
		ClientDetailsDTO clientDetailsDTO = getClientDetailsDTOByClientIdAndModuleId(clientId, moduleId);
		List<ClientContactDetailsDTO> listOfClientContact =  
				ServiceDAOFactory.getService(ClientService.class).getVpnContactDetailsListByClientID( clientDetailsDTO.getId() );

		if(listOfClientContact == null || listOfClientContact.isEmpty()) {
			throw new RequestFailureException("No Client Contact Details Found with client id "+ clientId);
		}
		logger.info("client contacts found");
		return listOfClientContact;	
	}
	
	private ClientContactDetailsDTO getSpecificClientContactDetailsByClientIdAndModuleIdAndDetailsType
			(long clientId, int moduleId, int detailsType) throws Exception {
		
		logger.info("searching specific client contact details by client id: " 
						+ clientId + ", moduleId: " + moduleId + ", detailsType: " + detailsType);
		List<ClientContactDetailsDTO> listOfClientContact = getListOfClientContactByClientIdAndModuleId(clientId, moduleId);
		return listOfClientContact.stream()
						.filter(a->a.getDetailsType()==detailsType)
						.findFirst()
						.orElseThrow(()->new RequestFailureException("Requested Details Type not found"));


	}
	
	public KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> getPairOfClientDetailsAndClientContactDetails(long clientId, int moduleId, int detailsType) throws Exception {
		logger.info("searching pair of client details and client specific contact details");
		KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = new KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO>(getClientDetailsDTOByClientIdAndModuleId(clientId, moduleId),
				getSpecificClientContactDetailsByClientIdAndModuleIdAndDetailsType(clientId, moduleId, detailsType)
				);
		logger.info("pair found");
		return pair;
	}

    public String getFullNameOfAClient(long clientId, int moduleId, int detilsType)  {
		String fullName = "";
		try {
			ClientContactDetailsDTO contactDetailsDTO = getSpecificClientContactDetailsByClientIdAndModuleIdAndDetailsType(clientId, moduleId, detilsType);
			fullName = contactDetailsDTO.getRegistrantsName() + contactDetailsDTO.getRegistrantsLastName();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			logger.fatal("Client Contact Details Not Found Returning empty String");
		}
		return fullName;
    }
}
