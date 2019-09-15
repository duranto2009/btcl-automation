package lli;
import common.ClientDTOConditionBuilder;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.Office.Office;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class LLIConnectionDAO {
	Class<LLIConnectionInstance> classObject = LLIConnectionInstance.class;
	public void insertLLIConnection(LLIConnectionInstance connectionInstance) throws Exception{
		insert(connectionInstance);
	}
	public void insertLLIConnectionFlow(LLIConnection lliConnection) throws Exception{
		insert(lliConnection);
	}


	public List<Long> getConnectionIDListByClientID(long clientID) throws Exception{
		return getSingleColumnListByResultSet(getResultSetBySqlPair(new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.fromLLIConnectionInstance()
				.Where()
//				.activeToEquals(Long.MAX_VALUE)
				.activeToGreaterThan(System.currentTimeMillis())
				.validToEquals(Long.MAX_VALUE)
				.clientIDEquals(clientID)
				.getSqlPair()), Long.class);
	}


	public List<Long> getCurrentConnectionIDListByClientID(long clientID) throws Exception{
		return getSingleColumnListByResultSet(getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.fromLLIConnectionInstance()
				.Where()
				.clientIDEquals(clientID)
				.statusIn(Arrays.asList(LLIConnectionInstance.STATUS_ACTIVE,LLIConnectionInstance.STATUS_TD))
//				.activeToEquals(Long.MAX_VALUE)
				.activeToGreaterThan(System.currentTimeMillis())
				.validToEquals(Long.MAX_VALUE)
				.getSqlPair()
				), Long.class);
	}

	List<LLIConnectionInstance> getConnectionListByClientID(long clientID) throws Exception{
		return getAllObjectList(classObject, new LLIConnectionInstanceConditionBuilder()
				.Where()
				.clientIDEquals(clientID)
				.validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.getCondition()
				);
	}

	public List<LLIConnectionInstance> getConnectionList() throws Exception{
		return getAllObjectList(classObject, new LLIConnectionInstanceConditionBuilder()
				.Where()
				.validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.getCondition()
				);
	}


	public List<LLIConnectionInstance> getConnectionListByClientIDAndDateRange(long clientID, long fromDate,
			long toDate) throws Exception{

		return getAllObjectList(classObject, new LLIConnectionInstanceConditionBuilder()
											.Where()
											.clientIDEquals(clientID)
											.activeFromLessThan(toDate)
											.activeToGreaterThan(fromDate)
											.validToEquals(Long.MAX_VALUE)
											.getCondition());
	}

	public List<LLIConnectionInstance> getConnectionListByDateRange(long fromDate, long toDate) throws Exception{

		return getAllObjectList(classObject, new LLIConnectionInstanceConditionBuilder()
											.Where()
											.activeFromLessThan(toDate)
											.activeToGreaterThan(fromDate)
											.validToEquals(Long.MAX_VALUE)
											.getCondition());
	}

	public LLIConnectionInstance getCurrentLLIConnectionInstanceByConnectionID(long connectionID)
			throws Exception{
		List<LLIConnectionInstance> lliConnectionInstanceList = getAllObjectList(classObject,
				new LLIConnectionInstanceConditionBuilder()
				.Where()
				.IDEquals(connectionID)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.validToEquals(Long.MAX_VALUE)
				.limit(1)
				.getCondition()
				);
		return lliConnectionInstanceList.isEmpty()?null: lliConnectionInstanceList.get(0);
	}

	public LLIConnectionInstance getFirstLLIConnectionInstanceAfterSpecificTime(long connectionId, long activeToTime) throws Exception {
		return getAllObjectList(classObject, new LLIConnectionInstanceConditionBuilder()
		.Where()
		.IDEquals(connectionId)
		.activeToGreaterThanEquals(activeToTime)
		.limit(1)
		.getCondition()).stream().findFirst().orElse(null);
	}


	public LLIConnectionInstance getLLIConnectionInstanceByConnectionID(long connectionID)
			throws Exception{
		List<LLIConnectionInstance> lliConnectionInstanceList = getAllObjectList(classObject,
				new LLIConnectionInstanceConditionBuilder()
				.Where()
				.IDEquals(connectionID)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.validToEquals(Long.MAX_VALUE)
				.limit(1)
				.getCondition()
				);
		return lliConnectionInstanceList.isEmpty()?null: lliConnectionInstanceList.get(0);
	}

	public void updateLLIConnectionInstance(LLIConnectionInstance lliConnectionInstance) throws Exception {
		updateEntity(lliConnectionInstance);
	}

	public List<Long> getIDsWithSearchCriteria(Hashtable<String,String> searchCriteria, LoginDTO loginDTO) throws Exception{
		ResultSet rs = getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.fromLLIConnectionInstance()
				.Where()
				.clientIDEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
				.clientIDInSqlPair(
						new ClientDTOConditionBuilder()
						.selectClientID()
						.fromClientDTO()
						.Where()
						.loginNameLeftLike(searchCriteria.get("clientName"))
						.getNullableSqlPair()
						)
				.nameBothLike(searchCriteria.get("connectionName"))
                        .IDEqualsString(searchCriteria.get("connectionId"))
//				.activeToEquals(Long.MAX_VALUE)// todo: how can this show temporary connection? i change it to below line(bony)
				.activeToGreaterThan(System.currentTimeMillis())
				.validToEquals(Long.MAX_VALUE)
				.statusEqualsString(searchCriteria.get("Status"))
				.orderByIDAsc()
				.getSqlPair()
				);


		return getSingleColumnListByResultSet(rs, Long.class);
	}

	public List<LLIConnectionInstance> getConnectionHistoryByConnectionID(long connectionID) throws Exception{
		ResultSet rs = getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.selectHistoryID()
				.selectActiveFrom()
				.selectValidFrom()
				.selectValidTo()
				.selectIncident()
				.fromLLIConnectionInstance()
				.Where()
				.IDEquals(connectionID)
				.validToEquals(Long.MAX_VALUE)
				.orderByactiveFromDesc()
				.getSqlPair()

				);
		return getObjectListByResultSet(classObject, rs);
	}

	public LLIConnectionInstance getConnectionHistoryByConnectionIDAndHistoryID(long connectionID,
			long historyID) throws Exception{
		List<LLIConnectionInstance> connectionList = getAllObjectList(classObject,
					new LLIConnectionInstanceConditionBuilder()
					.Where()
					.IDEquals(connectionID)
					.historyIDEquals(historyID)
					.limit(1)
					.getCondition()
				);
		return connectionList.isEmpty()?null:connectionList.get(0);
	}



	public List<LLIConnectionInstance> getLLIConnectionListByConnectionIDList(List<Long> connectionIDList) throws Exception{
		return getAllObjectList(classObject,
					new LLIConnectionInstanceConditionBuilder()
					.Where()
					.IDIn(connectionIDList)
//					.activeToEquals(Long.MAX_VALUE)//again change by bony
					.activeToGreaterThan(System.currentTimeMillis())
					.validToEquals(Long.MAX_VALUE)
					.orderByIDAsc()
					.getCondition()
				);
	}

	public LLIConnectionInstance getConnectionInstanceByConnectionHistoryID(long historyID) throws Exception{
		return getObjectByID(classObject, historyID);
	}

	public List<LLIDropdownPair> getConnectionNameIDPairListByClientID(long clientID) throws Exception{
		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		ResultSet rs = getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.selectName()
				.fromLLIConnectionInstance()
				.Where()
				.validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.clientIDEquals(clientID)
				.getSqlPair()
				);
		while(rs.next()) {
			Long ID = rs.getLong(1);
			String label = rs.getString(2);
			list.add(new LLIDropdownPair(ID, label));
		}
		return list;
	}
	public List<LLIDropdownPair> getActiveConnectionNameIDPairListByClientID(long clientID) throws Exception{
		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		ResultSet rs = getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.selectName()
				.selectBandwidth()
				.fromLLIConnectionInstance()
				.Where()
				.validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)//changed by bony
				.activeToGreaterThan(System.currentTimeMillis())
				.clientIDEquals(clientID)
				.statusEquals(LLIConnectionConstants.STATUS_ACTIVE)
				.getSqlPair()
				);
		while(rs.next()) {
			Long ID = rs.getLong(1);
			String label = rs.getString(2);
			Long bandwidth = rs.getLong(3);
			list.add(new LLIDropdownPair(ID, label,bandwidth));
		}
		return list;
	}
	public List<LLIDropdownPair> getTDConnectionNameIDPairListByClientID(long clientID) throws Exception{
		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		ResultSet rs = getResultSetBySqlPair(
				new LLIConnectionInstanceConditionBuilder()
				.selectID()
				.selectName()
				.fromLLIConnectionInstance()
				.Where()
				.validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)
						.activeToGreaterThan(System.currentTimeMillis())
				.clientIDEquals(clientID)
				.statusEquals(LLIConnectionConstants.STATUS_TD)
				.getSqlPair()
				);
		while(rs.next()) {
			Long ID = rs.getLong(1);
			String label = rs.getString(2);
			list.add(new LLIDropdownPair(ID, label));
		}
		return list;
	}
	public LLIConnectionInstance getLLICOnnectionInstanceByActiveToTime(long activeTo) throws Exception{
		List<LLIConnectionInstance> lliConnectionInstances = getAllObjectList(classObject,
				new LLIConnectionInstanceConditionBuilder()
				.Where()
				.activeToEquals(activeTo)
				.validToEquals(Long.MAX_VALUE)
				.getCondition()
				);
		return lliConnectionInstances.isEmpty()?null:lliConnectionInstances.get(0);
	}
	public double getTotalRegularBandwidthByClientID(long clientID) throws Exception{
		ResultSet rs = getResultSetBySqlPair(		new LLIConnectionInstanceConditionBuilder()
													.selectBandwidth()
													.fromLLIConnectionInstance()
													.Where()
													.validToEquals(Long.MAX_VALUE)
													.validToEquals(Long.MAX_VALUE)
													.connectionTypeEquals(LLIConnectionInstance.CONNECTION_TYPE_REGULAR)
													.clientIDEquals(clientID)
													.getSqlPair());

		List<Double> bandwidthList = getSingleColumnListByResultSet(rs, Double.class);
		double totalRegularBandwidth = bandwidthList.stream().mapToDouble(Double::doubleValue).sum();

		return totalRegularBandwidth;
	}

	public List<LLITotalRegularBandwidthSummary> getLLITotalBandwidthSummaryByFromDateAndToDate(long fromDate,long toDate) throws Exception{

		ResultSet rs = getResultSetBySqlPair(new LLIConnectionInstanceConditionBuilder()
												.selectActiveFrom()
												.selectActiveTo()
												.selectBandwidth()
												.fromLLIConnectionInstance()
												.validToEquals(Long.MAX_VALUE)
												.activeFromLessThan(toDate)
												.activeToGreaterThan(fromDate)
												.getSqlPair()
				);

		List<LLITotalRegularBandwidthSummary> summaryList = new ArrayList<>();

		while(rs.next()){
			summaryList.add(new LLITotalRegularBandwidthSummary(rs.getLong(1), rs.getLong(2), rs.getDouble(3)));
		}
		return summaryList;
	}

	public List<LLIDropdownPair> getLLIConnectionOfficeNameIDPairListByConnectionID(long connectionHistoryID) throws Exception {
		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		ResultSet rs = getResultSetBySqlPair(
				new LLIOfficeConditionBuilder()
				.selectID()
				.selectName()
				.fromLLIOffice()
				.Where()
				.connectionHistoryIDEquals(connectionHistoryID)
				.getSqlPair()
				);

		while(rs.next()) {
			Long ID = rs.getLong(1);
			String label = rs.getString(2);
			list.add(new LLIDropdownPair(ID, label));
		}
		return list;
	}


	public List<LLIDropdownPair> getLLIConnectionOfficeListByConnectionID(long connectionHistoryID) throws Exception {


		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		ResultSet rs = getResultSetBySqlPair(
				new LLIOfficeConditionBuilder()
						.selectID()
						.selectName()
						.selectHistoryID()
						.selectAddress()
						.selectConnectionHistoryID()
						.fromLLIOffice()
						.Where()
						.connectionHistoryIDEquals(connectionHistoryID)
						.getSqlPair()
		);

		while(rs.next()) {
			Long ID = rs.getLong(1);
			String name = rs.getString(2);
			Long historyId= rs.getLong(3);
			String address = rs.getString(4);
			Long con = rs.getLong(5);
			Office office = new Office();
			office.setOfficeAddress(address);
			office.setHistoryId(historyId);
			office.setOfficeName(name);
			office.setConnectionID(con);
			list.add(new LLIDropdownPair(historyId, name,office));
		}
		return list;
	}

}
