package util;

import org.apache.log4j.Logger;
import permission.LoadPermissions;

import java.util.HashSet;
import java.util.Set;

//import ipaddress.IpAddressService;

public class CleanLliLink {
	static Logger logger = Logger.getLogger(LoadPermissions.class);
	static Set<Long> linkIDSet = new HashSet<Long>();
	static
	{
		linkIDSet.add(16095L);
		linkIDSet.add(16096L);
		linkIDSet.add(16101L);
		linkIDSet.add(16102L);
		linkIDSet.add(16104L);
		linkIDSet.add(16119L);
		linkIDSet.add(17004L);
		linkIDSet.add(17006L);
		linkIDSet.add(17031L);
		linkIDSet.add(17087L);
		linkIDSet.add(17089L);
		linkIDSet.add(18027L);
	}
	/*
	@Transactional
	public void clean() throws Exception{
		String sql = "";
		Statement statement = null;
		Statement statement2 = null;
		ResultSet resultSet = null;
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		statement = databaseConnection.getNewStatement();
		statement2 = databaseConnection.getNewStatement();
		sql = "SET FOREIGN_KEY_CHECKS = 0";
		statement.executeQuery(sql);
		
		if(linkIDSet.contains(-1L))
		{
			linkIDSet.remove(-1L);
			sql = "select " + getColumnName(LliLinkDTO.class, "ID") +  " from at_lli_link";
			resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				linkIDSet.add(resultSet.getLong(1));
			}
		}
		logger.debug("linkIDSet "+linkIDSet);
		LliDAO lliDAO = new LliDAO();
//		IpAddressService ipAddressService = new IpAddressService();
		for(Long linkID: linkIDSet)
		{
				System.out.println(linkID);

				LliLinkDTO lliLinkDTO = lliDAO.getLliLinkDTOByID(linkID, databaseConnection);
				logger.debug("lliLinkDTO " + lliLinkDTO);
				
				if(lliLinkDTO == null)
					continue;

				
				LliFarEndDTO lliFarEndDTO = lliDAO.getLliFarEndByID(lliLinkDTO.getFarEndID(), databaseConnection);
				
				if(lliFarEndDTO != null)
				{
					sql = "delete from at_lli_fe where "+ getColumnName(LliFarEndDTO.class, "ID") + "=" + lliFarEndDTO.getID();
					logger.debug(sql);
					statement.execute(sql);
					
					sql = "delete from at_lli_ep where " + getColumnName(LliEndPointDTO.class, "lliEndPointID") + "=" + lliFarEndDTO.getLliEndPointID();
					logger.debug(sql);
					statement.execute(sql);
					
					sql = "delete from at_req where arEntityTypeID = " + EntityTypeConstant.LLI_LINK_FAR_END +" and arEntityID = " + lliFarEndDTO.getID();
					logger.debug(sql);
					statement.execute(sql);
					
					sql = "delete from at_lli_res_fr_external where " + getColumnName(LliFRResponseExternalDTO.class, "nearOrFarEndPointID") + "=" + lliFarEndDTO.getID();
					logger.debug(sql);
					statement.execute(sql);						
					
					sql = "update at_inventory_item set invitIsUsed = 0, invitOccupierEntityID = null, invitOccupierEntityTypeID = null, invitOccupierClientID = null where invitOccupierEntityTypeID = " + EntityTypeConstant.LLI_LINK_FAR_END +" and invitOccupierEntityID = " + lliFarEndDTO.getID(); 
					logger.debug(sql);
					statement.execute(sql);	
				}
				else
				{
					logger.debug("lliFarEndDTO is null");
				}
				sql = "delete from at_lli_res_fr_internal where " + getColumnName(LliFRResponseInternalDTO.class, "linkID")+ " = " + lliLinkDTO.getID();
				logger.debug(sql);
				statement.execute(sql);
				
				sql = "delete from at_req where arEntityTypeID = " + EntityTypeConstant.LLI_LINK +" and arEntityID = " + linkID;
				logger.debug(sql);
				statement.execute(sql);
				
				sql = "delete from at_bill where blEntityID = " + linkID + " and blEntityTypeID = " + EntityTypeConstant.LLI_LINK;
				logger.debug(sql);
				statement.execute(sql);

				sql = "delete from at_payment where pID not in (select blPaymentID from at_bill)";
				logger.debug(sql);
				statement.execute(sql);
				
				/*
				 * 
				 * DeAllocate IP Address by link ID

//				ipAddressService.deallocateIpByLLIID(linkID);

				sql = "delete from at_lli_link where " + getColumnName(LliLinkDTO.class, "ID") +  " = " + linkID;
				logger.debug(sql);
				statement.execute(sql);
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		logger.debug("Clean Link");
		ServiceDAOFactory.getService(CleanLliLink.class).clean();
		logger.debug("Done");
		System.exit(0);
	}
	*/
}
