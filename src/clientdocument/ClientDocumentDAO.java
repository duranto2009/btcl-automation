package clientdocument;

import util.ModifiedSqlGenerator;

public class ClientDocumentDAO {

	public void insertDocumentForAClient(ClientDocumentDTO clientDocumentDTO) throws Exception {
		ModifiedSqlGenerator.insert(clientDocumentDTO);		
	}

	public void deleteDocumentForAClient(ClientDocumentDTO clientDocumentDTO) throws Exception {
		ModifiedSqlGenerator.updateEntity(clientDocumentDTO);	
		
	} 
}
