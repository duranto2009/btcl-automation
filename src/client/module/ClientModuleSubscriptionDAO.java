/**
 * 
 */
package client.module;

import java.util.List;

import annotation.Transactional;
import util.ModifiedSqlGenerator;

/**
 * @author Touhid
 *
 */
public class ClientModuleSubscriptionDAO {

	public void addNewSubscription(ClientModuleSubscriptionDTO clientModuleSubscriptionDTO) throws Exception {
		ModifiedSqlGenerator.insert(clientModuleSubscriptionDTO);
	}

	@Transactional
	public List<ClientModuleSubscriptionDTO> findSubscriptionByClientAndModuleId(long clientId, long moduleId)
			throws Exception {
		List<ClientModuleSubscriptionDTO> clientModuleSubscriptionDTOs = ModifiedSqlGenerator
				.getAllObjectList(ClientModuleSubscriptionDTO.class, new ClientModuleSubscriptionDTOConditionBuilder()
						.Where().clientIdEquals(clientId).Where().moduleIdEquals(moduleId).getCondition());

		if (clientModuleSubscriptionDTOs.get(0) == null)
			return null;
		else
			return clientModuleSubscriptionDTOs;
	}

	public void deleteSubscription(List<ClientModuleSubscriptionDTO> clientModuleSubscriptionDTOs) throws Exception {

		for (ClientModuleSubscriptionDTO clientModuleSubscriptionDTO : clientModuleSubscriptionDTOs) {
			ModifiedSqlGenerator.updateEntity(clientModuleSubscriptionDTO);
		}
	}

	public boolean moduleExists(long moduleId) throws Exception {

		List<ClientModuleDTO> clientModuleDTOs = ModifiedSqlGenerator.getAllObjectList(ClientModuleDTO.class);

		for (ClientModuleDTO clientModuleDTO : clientModuleDTOs) {
			if (clientModuleDTO.getId() == moduleId)
				return true;
		}
		return false;
	}

}
