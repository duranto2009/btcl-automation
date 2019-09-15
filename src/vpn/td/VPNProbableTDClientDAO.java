package vpn.td;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class VPNProbableTDClientDAO {

	Class<VPNProbableTDClient> classObject = VPNProbableTDClient.class;
	
	public List<VPNProbableTDClient> getDTOListByIDList(List<Long> idList) throws Exception{
		return getObjectListByIDList(classObject, idList);
	}
	
	public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{
		
		ResultSet rs = getResultSetBySqlPair(
				new VPNProbableTDClientConditionBuilder()
				.selectID()
				.fromVPNProbableTDClient()
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

	public VPNProbableTDClient getVPNProbableTDClientByClientID(long clientID) throws Exception {
		List<VPNProbableTDClient> vpnProbableTDClientList = getAllObjectList(classObject,
				new VPNProbableTDClientConditionBuilder()
				.Where()
				.clientIDEquals(clientID)
				.getCondition()
				);
		
		return vpnProbableTDClientList.isEmpty()?null: vpnProbableTDClientList.get(0);
	}
	public void insertVPNProbableTDClient(VPNProbableTDClient vpnProbableTDClient) throws Exception {
		ModifiedSqlGenerator.insert(vpnProbableTDClient);
	}
	public void updateVPNProbableTDClient(VPNProbableTDClient vpnProbableTDClient) throws Exception {
		ModifiedSqlGenerator.updateEntity(vpnProbableTDClient);
	}
}
