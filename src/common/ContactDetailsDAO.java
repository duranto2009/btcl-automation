package common;

import connection.DatabaseConnection;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.List;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;

public class ContactDetailsDAO {
	Logger logger = Logger.getLogger(getClass());
	public List<ClientContactDetailsDTO> getContactDetailsListByModuleClientID(long moduleClientID, DatabaseConnection databaseConnection) throws Exception{
		
		Class classObject = ClientContactDetailsDTO.class;
		String conditionString = " where "+getColumnName(classObject, "vpnClientID")+" = "+moduleClientID;
		return (List<ClientContactDetailsDTO>)getAllObjectList(classObject, databaseConnection, conditionString);
		 
	}
	public ClientContactDetailsDTO getContactDetailsByModuleClientIDAndDetaislTypeID(long moduleClientID,int detailsTypeID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = ClientContactDetailsDTO.class;
		String conditionString = " where "+getColumnName(classObject, "vpnClientID")+" = "+moduleClientID+" and "+getColumnName(classObject, "detailsType")+"="+detailsTypeID;
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = (List<ClientContactDetailsDTO>)getAllObjectList(classObject, databaseConnection, conditionString); 
		return clientContactDetailsDTOs.isEmpty()? null: clientContactDetailsDTOs.get(0); 
		 
	}
}
