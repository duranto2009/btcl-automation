package client.module;

import java.util.List;

import annotation.Transactional;
import util.CurrentTimeFactory;

/**
 * @author Touhid
 *
 */
public class ClientModuleSubscriptionService {

	ClientModuleSubscriptionDAO clientModuleSubscriptionDAO = new ClientModuleSubscriptionDAO();

	@Transactional
	public void addNewSubscripton(long clientId, long moduleId) throws Exception {

		CurrentTimeFactory.initializeCurrentTimeFactory();
		ClientModuleSubscriptionDTO clientModuleSubscriptionDTO = new ClientModuleSubscriptionDTO();
                                              
		clientModuleSubscriptionDTO.setClientId(clientId);
		clientModuleSubscriptionDTO.setModuleId(moduleId);
		clientModuleSubscriptionDTO.setDeleted(false);
		clientModuleSubscriptionDTO.setLastModificationTime(System.currentTimeMillis());

		clientModuleSubscriptionDAO.addNewSubscription(clientModuleSubscriptionDTO);

		CurrentTimeFactory.destroyCurrentTimeFactory();
	}

	@Transactional
	public void deleteSubscription(long clientId, long moduleId) throws Exception {

		List<ClientModuleSubscriptionDTO> clientModuleSubscriptionDTOs = clientModuleSubscriptionDAO.findSubscriptionByClientAndModuleId(clientId,
				moduleId);

		for (ClientModuleSubscriptionDTO clientModuleSubscriptionDTO : clientModuleSubscriptionDTOs) {
			clientModuleSubscriptionDTO.setDeleted(true);
		}
		
		clientModuleSubscriptionDAO.deleteSubscription(clientModuleSubscriptionDTOs);
	}

}
