package lli.client.td;
import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectListByIDList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;
import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import common.ClientDTOConditionBuilder;
import lli.Application.ReviseClient.ReviseDTO;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;
public class LLIClientTDDAO {
	
	Class<LLIClientTD> classObject = LLIClientTD.class;
	
	public List<LLIClientTD> getDTOListByIDList(List<Long> idList) throws Exception{
		return getObjectListByIDList(classObject, idList);
	}
	public void updateClientTD(LLIClientTD lliClientTD) throws Exception{
		updateEntity(lliClientTD);
	}
	public void insertClientTD(LLIClientTD lliClientTD) throws Exception{
		insert(lliClientTD);
	}
	public boolean existsAnyClientTDByClientID(long clientID) throws Exception{
		
		ResultSet rs = getResultSetBySqlPair(
				new LLIClientTDConditionBuilder()
				.selectID()
				.fromLLIClientTD()
				.Where()
				.clientIDEquals(clientID)
				.limit(1)
				.getSqlPair()
				);
		
		List<Long> idList = getSingleColumnListByResultSet(rs, Long.class);
		
		return idList.isEmpty()?false:true;
	}
	
	public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{


		ResultSet rs = getResultSetBySqlPair(
				new LLIClientTDConditionBuilder()
				.selectID()
				.fromLLIClientTD()
				.Where()
				.clientIDEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
				.clientIDInSqlPair(
						new ClientDTOConditionBuilder()
						.selectClientID()
						.fromClientDTO()
						.Where()
						.loginNameBothLike(hashtable.get("clientName"))
						.getNullableSqlPair()
						)
				.tdFromGreaterThanEquals(TimeConverter.getDateFromString( hashtable.get("fromDate")))
				.tdFromLessThan(TimeConverter.getNextDateFromString(hashtable.get("toDate")))
				.getSqlPair()
				);
		
		return getSingleColumnListByResultSet(rs, Long.class);
	}
	
	LLIClientTD getLLIClientTDByClientID(long clientID) throws Exception{
		List<LLIClientTD> clientTDList = getAllObjectList(classObject, 
				new LLIClientTDConditionBuilder()
				.Where()
				.clientIDEquals(clientID)
				.tdToEquals(Long.MAX_VALUE)
				.getCondition()
				);
		
		return clientTDList.isEmpty()?null: clientTDList.get(0);
	}
	public List<LLIClientTD> getTDHistoryByClientID(long clientID) throws Exception {
		List<LLIClientTD> clientTDList = getAllObjectList(classObject, 
				new LLIClientTDConditionBuilder()
				.Where()
				.clientIDEquals(clientID)
				.getCondition()
				);
		
		return clientTDList;
	}

}
