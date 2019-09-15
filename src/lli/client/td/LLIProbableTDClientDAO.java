package lli.client.td;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectListByIDList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;
import static util.ModifiedSqlGenerator.getSingleColumnListByResultSet;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

public class LLIProbableTDClientDAO {

	private Class<LLIProbableTDClient> classObject = LLIProbableTDClient.class;
	
	public List<LLIProbableTDClient> getDTOListByIDList(List<Long> idList) throws Exception{
		return getObjectListByIDList(classObject, idList);
	}
	
	public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{
		
		ResultSet rs = getResultSetBySqlPair(
				new LLIProbableTDClientConditionBuilder()
				.selectID()
				.fromLLIProbableTDClient()
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
				.tdDateGreaterThanEquals(TimeConverter.getDateFromString( hashtable.get("fromDate")))
				.tdDateLessThan(TimeConverter.getNextDateFromString(hashtable.get("toDate")))
				.orderBytdDateAsc()
				.getSqlPair()
				);
		
		return getSingleColumnListByResultSet(rs, Long.class);
	}

	public LLIProbableTDClient getLLIProbableTDClientByClientID(long clientID) throws Exception {
		List<LLIProbableTDClient> lliProbableTDClientList = getAllObjectList(classObject, 
				new LLIProbableTDClientConditionBuilder()
				.Where()
				.clientIDEquals(clientID)
				.getCondition()
				);
		
		return lliProbableTDClientList.isEmpty()?null: lliProbableTDClientList.get(0);
	}
	public void insertLLIProbableTDClient(LLIProbableTDClient lliProbableTDClient) throws Exception {
		ModifiedSqlGenerator.insert(lliProbableTDClient);
	}
	public void updateLLIProbableTDClient(LLIProbableTDClient lliProbableTDClient) throws Exception {
		ModifiedSqlGenerator.updateEntity(lliProbableTDClient);
	}
}
