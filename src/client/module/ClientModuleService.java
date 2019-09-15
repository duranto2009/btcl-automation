/**
 * 
 */
package client.module;

import annotation.Transactional;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;
import util.TransactionType;

import java.util.List;

/**
 * @author Touhid
 *
 */
public class ClientModuleService {
	ClientModuleDAO clientModuleDAO = new ClientModuleDAO();

	@Transactional
	public void addNewModule(String moduleName, long moduleId) throws Exception {

		CurrentTimeFactory.initializeCurrentTimeFactory();

		ClientModuleDTO clientModuleDTO = new ClientModuleDTO();

		clientModuleDTO.setModuleId(moduleId);
		clientModuleDTO.setName(moduleName);
		clientModuleDTO.setDeleted(false);
		clientModuleDTO.setLastModificationTime(System.currentTimeMillis());

		clientModuleDAO.addNewModule(clientModuleDTO);
		CurrentTimeFactory.destroyCurrentTimeFactory();
	}

	@Transactional(transactionType = TransactionType.READONLY)
	public boolean moduleExists(long moduleId) throws Exception {

		List<ClientModuleDTO> clientModuleDTOs = ModifiedSqlGenerator.getAllObjectList(ClientModuleDTO.class);

		for (ClientModuleDTO clientModuleDTO : clientModuleDTOs) {
			if (clientModuleDTO.getModuleId() == moduleId)
				return true;
		}
		return false;
	}

	@Transactional
	public List<ClientModuleDTO> getAllModules() throws Exception {
		return clientModuleDAO.getAllModules();
	}

}
