package client;

import common.ClientDTO;
import common.ModuleConstants;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import permission.LoadPermissions;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CleanClient {
	static Logger logger = Logger.getLogger(LoadPermissions.class);
	static Set<Long> clientIDSet = new HashSet<Long>();
	static final int moduleID = ModuleConstants.Module_ID_LLI;
	static
	{
		clientIDSet.add(200013L);		
	}
	public static void main(String args[]) throws Exception
	{
		logger.debug("Clean Client");
		DatabaseConnection databaseConnection = new DatabaseConnection();
		String sql = "";
		Statement statement = null;
		Statement statement2 = null;
		ResultSet resultSet = null;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			statement = databaseConnection.getNewStatement();
			statement2 = databaseConnection.getNewStatement();
			sql = "SET FOREIGN_KEY_CHECKS = 0";
			statement.executeQuery(sql);
			
			/*if(clientIDSet.contains(-1L))
			{
				clientIDSet.remove(-1L);
				sql = "select " + getColumnName(VpnLinkDTO.class, "ID") +  " from at_vpn_client";
				resultSet = statement.executeQuery(sql);
				while(resultSet.next())
				{
					clientIDSet.add(resultSet.getLong(1));
				}
			}*/
			logger.debug("clientIDSet "+clientIDSet);
			
			for(Long clientID: clientIDSet)
			{
				try
				{
					ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);
					ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, 7);
					List<ClientContactDetailsDTO> clientContactDetailsDTOList = SqlGenerator.getAllObjectListFullyPopulated(ClientContactDetailsDTO.class, databaseConnection, " where vclcVpnClientID = " + clientDetailsDTO.getId());
					sql = "delete from at_req where arEntityTypeID = " + ((moduleID*100) + 51) + " and arEntityID = " + clientDTO.getClientID();
					logger.debug("sql " + sql);
					statement.execute(sql);					
					for(ClientContactDetailsDTO clientContactDetailsDTO: clientContactDetailsDTOList)
					{
						SqlGenerator.deleteHardEntityByID(ClientContactDetailsDTO.class, clientContactDetailsDTO.getID(), databaseConnection);
					}
					SqlGenerator.deleteHardEntityByID(ClientDetailsDTO.class, clientDetailsDTO.getId(), databaseConnection);
					SqlGenerator.deleteHardEntityByID(ClientDTO.class, clientDTO.getClientID(), databaseConnection);
					
			
				}catch(Exception ex)
				{
					logger.debug("exception for " + clientID, ex);
				}
			}

			sql = "SET FOREIGN_KEY_CHECKS = 1";
			statement.execute(sql);
			
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("ex", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
		logger.debug("Done");
		System.exit(0);
	}
}
