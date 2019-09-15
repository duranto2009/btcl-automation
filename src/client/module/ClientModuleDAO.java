/**
 * 
 */
package client.module;

import util.ModifiedSqlGenerator;

import java.util.List;

/**
 * @author Touhid
 *
 */
public class ClientModuleDAO {

	public void addNewModule(ClientModuleDTO clientModuleDTO) throws Exception {
		// TODO Auto-generated method stub
		ModifiedSqlGenerator.insert(clientModuleDTO);
	}

	public List<ClientModuleDTO> getAllModules() throws Exception {
		return ModifiedSqlGenerator.getAllObjectList(ClientModuleDTO.class);
	}
}
