package common;

import annotation.Transactional;
import connection.DatabaseConnection;
import util.ModifiedSqlGenerator;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.List;

import static util.SqlGenerator.getColumnName;

public class ModuleContactDetailsService {
	Logger logger = Logger.getLogger(getClass());
	ContactDetailsDAO contactDetailsDAO = new ContactDetailsDAO();
	public ClientContactDetailsDTO getContactDetailsByModuleClientIDAndDetaislTypeID(long moduleClientID, int detailsTypeID){
		ClientContactDetailsDTO clientContactDetailsDTO = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			clientContactDetailsDTO = contactDetailsDAO.getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientID, detailsTypeID, databaseConnection);
		}catch(Exception ex){
			logger.debug("fatal",ex);
		}finally{
			databaseConnection.dbClose();
		}
		return clientContactDetailsDTO;
	}
	
	@Transactional
	public List<ClientContactDetailsDTO> getContactDetailsDTOListByDetailsType(int detailsType) throws Exception{

		String conditionString = " where "+getColumnName(ClientContactDetailsDTO.class, "detailsType")+"="+detailsType+" and "+getColumnName(ClientContactDetailsDTO.class, "isDeleted")+" = 0";
		
		return ModifiedSqlGenerator.getAllObjectList(ClientContactDetailsDTO.class, conditionString);
	}
	
}
