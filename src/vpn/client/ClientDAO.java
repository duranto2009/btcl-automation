package vpn.client;

import common.ClientDTO;
import common.CommonSelector;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import login.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import request.StateDTO;
import request.StateRepository;
import util.SqlGenerator;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static util.SqlGenerator.*;

public class ClientDAO {
	Logger logger = Logger.getLogger(getClass());
	public int moduleID = 0;

	public void addNewVpnClient(ClientDetailsDTO vdto, DatabaseConnection databaseConnection) throws Exception {
		insert(vdto, vdto.getClass(), databaseConnection, false);

	}

	public void addNewClient(ClientDTO cdto, DatabaseConnection databaseConnection) throws Exception {
		insert(cdto, cdto.getClass(), databaseConnection, false);
		// return new DAOResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getIDs(LoginDTO loginDTO, DatabaseConnection databaseConnection, Object... objects)
			throws Exception {
		/*Collection<Long> data = new ArrayList<Long>();
		CommonSelector commonSelector = null;
		String sql;
		if (objects.length == 1) {
			commonSelector = (CommonSelector) objects[0];
			logger.debug("commonSelector " + commonSelector);
			sql = "SELECT vclID from at_client_details where vclModuleID =" + commonSelector.moduleID + " and vclIsDeleted = 0";
		} else {
			sql = "SELECT vclID from at_client_details where vclIsDeleted = 0" ;
		}

		Statement stmt = databaseConnection.getNewStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			long clientID = rs.getLong(1);
			/*
			 * if(commonSelector != null) { HashMap<Integer, ClientStatusDTO>
			 * statusMap =
			 * AllClientRepository.getInstance().getClientStatusByClientID(
			 * clientID); logger.debug("statusMap " + statusMap);
			 * if(statusMap.get(commonSelector.moduleID) == null) continue; }
			 */
		/*
			data.add(clientID);
		}

		logger.debug("data size " + data.size());

		return data;*/
		Hashtable searchCriteria = new Hashtable();
		if(objects.length == 1) {
			searchCriteria.put("moduleID", ((CommonSelector)objects[0]).moduleID + "");
		}
		return getIDWithSearchCriteriaFromSqlGenerator(searchCriteria, databaseConnection);
	}

	@SuppressWarnings("unchecked")
	public Collection<Long> getIDWithSearchCriteriaFromSqlGenerator(Hashtable searchCriteria,
			DatabaseConnection databaseConnection) throws Exception {
		
		
		String isDeletedValue = (String) searchCriteria.get("showDeleted");
		searchCriteria.put("showDeleted", "0");
		if("1".equals(isDeletedValue)) {
			searchCriteria.remove("showDeleted");
		}
//		searchCriteria.remove("isDeleted");
//		searchCriteria.put("isDeleted", "0");

		searchCriteria.remove("detailsType");
		searchCriteria.put("detailsType", "" + ClientContactDetailsDTO.REGISTRANT_CONTACT);

		String[] keys = new String[]      {            "vclcName" , "vclcPhoneNumber", "vclcEmail", "vclcFaxNumber", "detailsType", "showDeleted" };
		String[] operators = new String[] {             "like"    , "like"           , "like"     , "like"         , "="          , "="        };
		String[] dtoColumnNames = new String[] { "registrantsName", "phoneNumber"    , "email"    , "faxNumber"    , "detailsType","isDeleted"  };
  
		List<Long> IDListFromContact = null; 
				
				
		for(String key: keys){		
			if( !StringUtils.isBlank( (String) searchCriteria.get(key))){
				IDListFromContact = (List<Long>) SqlGenerator.getSelectedColumnListFromSearchCriteria(ClientContactDetailsDTO.class,
						keys, operators, dtoColumnNames, searchCriteria, "", databaseConnection, "vpnClientID");
				break; 
			}
		}
				

		if ( IDListFromContact!=null && IDListFromContact.isEmpty()) {
			return new ArrayList<Long>();
		}

		keys = new String[]           { "moduleID", "vclIdentityNo", "Client Status", "Client Type"    , "showDeleted", "clLoginName", "vclRegType" };
		operators = new String[]      { "="       , "like"         , "in"              , "="                 , "="        , "in"        , "=" };
		dtoColumnNames = new String[] { "moduleID", "identityNo"   , "currentStatus"   , "clientCategoryType", "isDeleted","loginName"  , "registrantType" };

		String fixedCondition = (IDListFromContact!=null? getColumnName(ClientDetailsDTO.class, "id") + " in "
				+ common.StringUtils.getCommaSeparatedString(IDListFromContact):"");

		return SqlGenerator.getIDListFromSearchCriteria(ClientDetailsDTO.class, keys, operators, dtoColumnNames,
				searchCriteria, fixedCondition, databaseConnection);
	}

	public static List<? extends Number> getCurrentStatusListFromActivationStatus(Hashtable<String, String> hashtable) {

		int activationStatus = Integer.parseInt(hashtable.get("Client Status"));
		List<Integer> activationStatusList = null;
		if (!StringUtils.isBlank(hashtable.get("moduleID"))) {

			int moduleID = Integer.parseInt(hashtable.get("moduleID"));
			activationStatusList = StateRepository.getInstance().getStatusListByModuleIDAndActivationStatus(moduleID, activationStatus);
		} else {
			activationStatusList = StateRepository.getInstance().getStatusListByActivationStatus(activationStatus);
		}

		return activationStatusList;
	}

	/*public Collection<Long> getIDsWithSearchCriteria(Hashtable p_searchCriteria, LoginDTO loginDTO,
			DatabaseConnection databaseConnection, Object... objects) throws SQLException {

		logger.debug(p_searchCriteria);
		Collection<Long> result = new ArrayList<Long>();
		boolean isVpnClientDirty = false;
		boolean isVpnClientContactDirty = false;
		CommonSelector commonSelector = null;

		String sql;// = "SELECT vclID, vclClientID from at_client_details where
					// vclModuleID="+p_searchCriteria.get("vclModuleID");
		if (objects.length == 1) {
			commonSelector = (CommonSelector) objects[0];
			logger.debug("commonSelector " + commonSelector);
			sql = "SELECT vclID, vclClientID from at_client_details where vclModuleID =" + commonSelector.moduleID;
		} else {
			sql = "SELECT vclID, vclClientID from at_client_details where vclModuleID > 0";
		}

		Enumeration keys = p_searchCriteria.keys();
		if (p_searchCriteria.size() > 0) {
			for (int i = 0; i < p_searchCriteria.size(); i++) {
				String key = (String) keys.nextElement();
				String value = (String) p_searchCriteria.get(key);

				if (key.startsWith("cl") || key.startsWith("vclc"))
					continue;
				if (key.equals("moduleID") || key.equals("vclModuleID"))
					continue;
				if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
					continue;
				}
				isVpnClientDirty = true;
				if ("vclCurrentStatus".equals(key)) {
					List list = StateRepository.getInstance().getStatusListByActivationStatus(Integer.parseInt(value));// need
																														// to
																														// change
																														// with
																														// two
																														// parameter
					sql += " and " + key + " in (" + StringUtils.join(list, ',') + ")";

				} else {
					sql += " and " + key + " like '%" + value + "%'";
				}

			}
		}

		logger.debug("at_client_details : " + sql);

		Statement stmt = databaseConnection.getNewStatement();
		ResultSet rs = stmt.executeQuery(sql);

		String whereClause = "";
		HashMap<Long, Long> vlcIDvsClientID = getWhereClauseAndMapOfID(rs, "vclID", "vclClientID", whereClause);

		sql = "SELECT vclcVpnClientID from at_client_contact_details ";

		// p_searchCriteria.remove("moduleID");

		if (p_searchCriteria != null) {
			keys = p_searchCriteria.keys();
			if (p_searchCriteria.size() > 0) {
				int j = 0;
				for (int i = 0; i < p_searchCriteria.size(); i++) {
					String key = (String) keys.nextElement();
					String value = (String) p_searchCriteria.get(key);
					if (!key.startsWith("vclc"))
						continue;
					if (key.equals("moduleID") || key.equals("vclModuleID"))
						continue;
					if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
						continue;
					}

					if (j == 0)
						sql += " where " + key + " like '%" + value + "%'";
					else
						sql += " and " + key + " like '%" + value + "%'";
					j++;
					isVpnClientContactDirty = true;

				}
				if (whereClause.length() > 2) {
					if (j > 0)
						sql += " and vclcVpnClientID in " + whereClause;
					else {
						sql += " where vclcVpnClientID in " + whereClause;
					}
				}
			}
		}

		logger.debug("at_client_contact_details: " + sql);

		rs = stmt.executeQuery(sql);
		whereClause = getWhereClauseFromMap(rs, "vclcVpnClientID", vlcIDvsClientID);

		sql = "SELECT clID from at_client ";

		if (p_searchCriteria != null) {
			keys = p_searchCriteria.keys();
			if (p_searchCriteria.size() > 0) {
				int j = 0;
				for (int i = 0; i < p_searchCriteria.size(); i++) {
					String key = (String) keys.nextElement();
					String value = (String) p_searchCriteria.get(key);
					if (key.startsWith("vcl") || key.startsWith("vclc"))
						continue;
					if (key.equals("moduleID") || key.equals("vclModuleID"))
						continue;
					if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
						continue;
					}

					logger.debug(key);
					if (j == 0)
						sql += " where " + key + " like '%" + value + "%'";
					else
						sql += " and " + key + " like '%" + value + "%'";
					j++;

				}
				if (whereClause.length() > 2) {
					if (j > 0)
						sql += " and clID in " + whereClause;
					else {
						sql += " where clID in " + whereClause;
					}
				} else if (isVpnClientDirty || isVpnClientContactDirty) {
					if (j > 0)
						sql += " and clID in (-1)";
					else {
						sql += " where clID in (-1)";
					}
				}
			}
		}

		logger.debug("at_client: " + sql);

		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			long id = rs.getLong("clID");
			result.add(id);
		}

		return result;

	}
*/
	String getWhereClause(ResultSet rs, String key) throws SQLException {
		String whereClause = "";
		whereClause += "(";
		while (rs.next()) {
			whereClause += rs.getLong(key);
			if (!rs.isLast()) {
				whereClause += ",";
			}
		}
		whereClause += ")";

		logger.debug("where: " + whereClause);
		return whereClause;
	}

	String getWhereClauseFromMap(ResultSet rs, String key, HashMap<Long, Long> map) throws SQLException {
		String whereClause = "";
		whereClause += "(";
		while (rs.next()) {
			if (map.containsKey(rs.getLong(key))) {
				whereClause += map.get(rs.getLong(key)) + ",";
			}
		}
		whereClause = StringUtils.stripEnd(whereClause, ",") + ")";
		logger.debug("getWhereClauseFromMap: " + whereClause);
		return whereClause;
	}

	HashMap<Long, Long> getWhereClauseAndMapOfID(ResultSet rs, String key, String value, String whereClause)
			throws SQLException {
		HashMap<Long, Long> vlcIDvsClientID = new HashMap<Long, Long>();
		whereClause += "(";
		while (rs.next()) {
			whereClause += rs.getLong(key);
			if (!rs.isLast()) {
				whereClause += ",";
			}
			vlcIDvsClientID.put(rs.getLong(key), rs.getLong(value));
		}
		whereClause += ")";
		logger.debug("map: " + vlcIDvsClientID);
		return vlcIDvsClientID;
	}

	/*public Collection<Long> getIDsWithSearchCriteria1(Hashtable p_searchCriteria, LoginDTO loginDTO,
			DatabaseConnection databaseConnection, Object... objects) throws SQLException {

		logger.debug(p_searchCriteria);
		Collection<Long> result = new ArrayList<Long>();

		String sql = "SELECT * from at_client_contact_details ";

		p_searchCriteria.remove("moduleID");

		if (p_searchCriteria != null) {
			Enumeration keys = p_searchCriteria.keys();
			if (p_searchCriteria.size() > 0) {
				int j = 0;
				for (int i = 0; i < p_searchCriteria.size(); i++) {
					String key = (String) keys.nextElement();
					String value = (String) p_searchCriteria.get(key);
					if (!key.startsWith("vclc"))
						continue;

					if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
						continue;
					}

					if (j == 0)
						sql += " where " + key + " like '%" + value + "%'";
					else
						sql += " and " + key + " like '%" + value + "%'";
					j++;

				}
			}
		}
		logger.debug("the sql for first search: " + sql);

		Statement stmt = databaseConnection.getNewStatement();
		ResultSet rs = stmt.executeQuery(sql);
		String whereClause = "";

		whereClause += "(";
		while (rs.next()) {
			whereClause += rs.getLong("vclcVpnClientID");
			if (!rs.isLast()) {
				whereClause += ",";
			}
		}
		whereClause += ")";

		logger.debug("The result of contact details is:  ===========  " + whereClause);

		sql = "SELECT * from at_client_details ";

		if (p_searchCriteria != null) {
			Enumeration keys = p_searchCriteria.keys();
			if (p_searchCriteria.size() > 0) {
				int j = 0;
				for (int i = 0; i < p_searchCriteria.size(); i++) {
					String key = (String) keys.nextElement();
					String value = (String) p_searchCriteria.get(key);
					if (key.startsWith("vclc"))
						continue;
					if (key.startsWith("cl"))
						continue;
					if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
						continue;
					}

					if (j == 0)
						sql += " where " + key + " like '%" + value + "%'";
					else
						sql += " and " + key + " like '%" + value + "%'";
					j++;

				}
				if (whereClause.length() > 2) {
					if (j > 0)
						sql += " and vclID in " + whereClause;

					else {
						sql += " where vclID in " + whereClause;
					}
				}
			}
		}

		logger.debug("   the sql for vpnclient search ==============" + sql);

		rs = stmt.executeQuery(sql);
		whereClause = "";

		whereClause += "(";

		while (rs.next()) {
			whereClause += rs.getLong("vclClientID");
			if (!rs.isLast()) {
				whereClause += ",";
			}
		}
		whereClause += ")";

		logger.debug("The whereClause after at_client_details search: " + whereClause);

		sql = "SELECT clID from at_client ";

		if (p_searchCriteria != null) {
			Enumeration keys = p_searchCriteria.keys();
			if (p_searchCriteria.size() > 0) {
				int j = 0;
				for (int i = 0; i < p_searchCriteria.size(); i++) {
					String key = (String) keys.nextElement();
					String value = (String) p_searchCriteria.get(key);
					if (key.startsWith("vcl"))
						continue;
					if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
						continue;
					}

					logger.debug(key);
					if (j == 0)
						sql += " where " + key + " like '%" + value + "%'";
					else
						sql += " and " + key + " like '%" + value + "%'";
					j++;

				}
				if (whereClause.length() > 2) {
					if (j > 0)
						sql += " and clID in " + whereClause;
					else {
						sql += " where clID in " + whereClause;
					}
				}
			}
		}
		logger.debug("sql is : " + sql);

		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			long id = rs.getLong("clID");

			result.add(id);

		}

		logger.debug("The result after at_client search: ============== " + result);

		return result;
	}
*/
	public Collection getDTOs(Collection recordIDs, DatabaseConnection databaseConnection, Object... objects)
			throws Exception {
		List list = (List) recordIDs;
		String conditionString = " WHERE " + getColumnName(ClientContactDetailsDTO.class, "vpnClientID") + " IN "
				+ common.StringUtils.getCommaSeparatedString(list) + " and "
				+ getColumnName(ClientContactDetailsDTO.class, "detailsType") + "="
				+ ClientContactDetailsDTO.REGISTRANT_CONTACT;
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(
				ClientContactDetailsDTO.class, databaseConnection, conditionString);

		Map<Long, ClientContactDetailsDTO> mapOfClientContactDetailsDTOToVpnClientID = new HashMap<>();

		for (ClientContactDetailsDTO clientContactDetailsDTO : clientContactDetailsDTOs) {
			mapOfClientContactDetailsDTOToVpnClientID.put(clientContactDetailsDTO.getVpnClientID(), clientContactDetailsDTO);
		}

		conditionString = " where " + getColumnName(ClientDetailsDTO.class, "id") + " IN "
				+ common.StringUtils.getCommaSeparatedString(list);

		List<ClientDetailsDTO> clientDetailsDTOs = (List<ClientDetailsDTO>) (Collection) SqlGenerator
				.getAllObjectListFullyPopulated(ClientDetailsDTO.class, databaseConnection, conditionString);

		for (ClientDetailsDTO clientDetailsDTO : clientDetailsDTOs) {
			if (clientDetailsDTO.getVpnContactDetails() == null) {
				clientDetailsDTO.setVpnContactDetails(new ArrayList<ClientContactDetailsDTO>());
			}
			if (mapOfClientContactDetailsDTOToVpnClientID.containsKey(clientDetailsDTO.getId())) {
				clientDetailsDTO.getVpnContactDetails()
						.add(mapOfClientContactDetailsDTOToVpnClientID.get(clientDetailsDTO.getId()));
			}
		}

		return clientDetailsDTOs;

		/*
		 * CommonSelector commonSelector=null; String sql =
		 * "SELECT * from at_client "; if (recordIDs.size() > 0) { sql +=
		 * " where "; for (int i = 0; i < recordIDs.size(); i++) { sql +=
		 * " clID = " + ((ArrayList) recordIDs).get(i); if (i <=
		 * recordIDs.size() - 2) { sql += " or "; } }
		 * 
		 * } logger.debug("sql " + sql); Collection<ClientDetailsDTO> data = new
		 * ArrayList<ClientDetailsDTO>(); Statement stmt =
		 * databaseConnection.getNewStatement(); ResultSet rs =
		 * stmt.executeQuery(sql); while (rs.next()) { ClientDetailsDTO clientDetailsDTO
		 * = new ClientDetailsDTO();
		 * clientDetailsDTO.setLoginName(rs.getString("clLoginName"));
		 * clientDetailsDTO.setClientID(rs.getLong("clID"));
		 * logger.debug(clientDetailsDTO); data.add(clientDetailsDTO);
		 * 
		 * } //sql = "SELECT *  from at_client_details ";
		 * 
		 * if(objects.length == 1) { commonSelector =
		 * (CommonSelector)objects[0]; logger.debug("commonSelector " +
		 * commonSelector); sql =
		 * "SELECT * from at_client_details where vclModuleID ="+
		 * commonSelector.moduleID; if (recordIDs.size() > 0) { sql+=" and "; }
		 * }else{ sql = "SELECT * from at_client_details "; if (recordIDs.size() >
		 * 0) { sql+=" where "; } }
		 * 
		 * 
		 * if (recordIDs.size() > 0) { sql+=" ( "; for (int i = 0; i <
		 * recordIDs.size(); i++) { sql += " vclClientID = " + ((ArrayList)
		 * recordIDs).get(i); if (i <= recordIDs.size() - 2) { sql += " or "; }
		 * } sql+=" ) "; }
		 * 
		 * logger.debug("final vpn client: "+sql);
		 * 
		 * rs = stmt.executeQuery(sql); while (rs.next()) { long clientID =
		 * rs.getLong("vclClientID");
		 * 
		 * for (ClientDetailsDTO dto : data) {
		 * //logger.debug("Checking dtos of client table: " + dto); if
		 * (dto.getClientID() == clientID) {
		 * dto.setIdentityNo(rs.getString("vclIdentityNo"));
		 * dto.setIdentityType(rs.getInt("vclIdentityType"));
		 * dto.setId(rs.getLong("vclID"));
		 * dto.setModuleID(rs.getInt("vclModuleID"));
		 * dto.setClientCategoryType(rs.getInt("vclClientType"));
		 * dto.setCurrentStatus(rs.getInt("vclCurrentStatus"));
		 * dto.vpnContactDetails =
		 * getVpnContactDetailsListByClientID(rs.getLong("vclID"),
		 * databaseConnection);
		 * //logger.debug("Checking dtos that matched both table: " + dto); } }
		 * }
		 * 
		 * logger.debug("Checking=============final dtos " + data); return data;
		 */
	}

	public ClientDetailsDTO getClient(long id, DatabaseConnection databaseConnection) throws Exception {

		return AllClientRepository.getInstance().getVpnClientByVpnClientID(id);

	}

	public List<ClientContactDetailsDTO> getVpnContactDetailsListByClientID(long clientDetailsID, DatabaseConnection databaseConnection) {
		String conditionString;
		List<ClientContactDetailsDTO> result = new ArrayList<ClientContactDetailsDTO>();
		try {
			conditionString = "  where " + getColumnName(ClientContactDetailsDTO.class, "vpnClientID") + " = ?";

			result = (List<ClientContactDetailsDTO>) getAllObjectList(ClientContactDetailsDTO.class, databaseConnection,
					conditionString, clientDetailsID);
		} catch (Exception e) {

			logger.debug("Fatal:", e);
		}
		// logger.debug("The result found inside
		// getVpnContactDetailsListByClientID method ===========" + result);
		return result;
	}

	public void addNewClientContactDetailsDTO(ClientContactDetailsDTO contactDetailsDTO,
			DatabaseConnection databaseConnection) throws Exception {
			insert(contactDetailsDTO, contactDetailsDTO.getClass(), databaseConnection, false);
	}

	public ClientDetailsDTO getDTO(long clientID, int moduleID, DatabaseConnection databaseConnection) throws Exception {
		logger.debug("clientID " + clientID + " moduleID " + moduleID);
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);
		logger.debug("clientDetailsDTO " + clientDetailsDTO);

		String conditionString = " where " + getColumnName(ClientContactDetailsDTO.class, "vpnClientID") + " = "
				+ clientDetailsDTO.getId();
		List<ClientContactDetailsDTO> clientContactDetailsDTOs = (List<ClientContactDetailsDTO>) getAllObjectList(
				ClientContactDetailsDTO.class, databaseConnection, conditionString);
		clientDetailsDTO.setVpnContactDetails(clientContactDetailsDTOs);
		logger.debug(clientDetailsDTO);
		return clientDetailsDTO;

		/*
		 * ; return
		 * 
		 * String sql = "SELECT * from at_client where clID = "+id;
		 * 
		 * ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO(); Statement stmt =
		 * databaseConnection.getNewStatement(); ResultSet rs =
		 * stmt.executeQuery(sql); while (rs.next()) {
		 * 
		 * clientDetailsDTO.setLoginName(rs.getString("clLoginName"));
		 * 
		 * clientDetailsDTO.setClientID(rs.getLong("clID"));
		 * logger.debug(clientDetailsDTO); break;
		 * 
		 * } sql = "SELECT *  from at_client_details  where vclClientID =  "+id;
		 * 
		 * 
		 * 
		 * rs = stmt.executeQuery(sql); while (rs.next()) { long clientID =
		 * rs.getLong("vclClientID");
		 * 
		 * 
		 * logger.debug("Checking dtos of client table: " + clientDetailsDTO); if
		 * (clientDetailsDTO.getClientID() == clientID) {
		 * clientDetailsDTO.setNID(rs.getLong("vclNID"));
		 * clientDetailsDTO.setId(rs.getLong("vclID"));
		 * 
		 * clientDetailsDTO.vpnContactDetails =
		 * getVpnContactDetailsListByClientID(rs.getLong("vclID"),
		 * databaseConnection);
		 * logger.debug("Checking dtos that matched both table: " +
		 * clientDetailsDTO); }
		 * 
		 * }
		 * 
		 * logger.debug("Checking=============final dtos " + clientDetailsDTO);
		 * return clientDetailsDTO;
		 */
	}

	public void updateNewClient(ClientDTO clientDTO, DatabaseConnection databaseConnection) throws Exception {
		updateEntity(clientDTO, clientDTO.getClass(), databaseConnection, false, false);
	}

	public void updateNewVpnClient(ClientDetailsDTO vdto, DatabaseConnection databaseConnection) throws Exception {

		updateEntity(vdto, vdto.getClass(), databaseConnection, false, false);

	}

	public void updateClientContactDetailsDTO(ClientContactDetailsDTO contactDetailsDTO,
			DatabaseConnection databaseConnection) throws Exception {
		updateEntity(contactDetailsDTO, contactDetailsDTO.getClass(), databaseConnection, false, false);

	}



	public Collection<ClientDetailsDTO> getClientDetailsDTOListByClientIDAndEmail(long ClientID, String email,
			DatabaseConnection databaseConnection) throws Exception {

		Map<String, String> searchCriteria = new HashMap<>();

		searchCriteria.put("email", email);
		searchCriteria.put("isDeleted", "0");

		String[] keys = new String[] { "vclcEmail", "isDeleted" };
		String[] operators = new String[] { "=", "=" };
		String[] dtoColumnNames = new String[] { "email", "isDeleted" };

		List<Long> IDListFromContact = (List<Long>) SqlGenerator.getSelectedColumnListFromSearchCriteria(ClientContactDetailsDTO.class,
				keys, operators, dtoColumnNames, searchCriteria, "", databaseConnection, "vpnClientID");

		if (IDListFromContact.isEmpty()) {
			return new ArrayList<ClientDetailsDTO>();
		}

		logger.info(IDListFromContact);

		keys = new String[] { "isDeleted", "detailsType", };
		operators = new String[] { "0", ClientContactDetailsDTO.REGISTRANT_CONTACT + "" };
		dtoColumnNames = new String[] { "isDeleted", "detailsType", };


		String conditionString = " where " + getColumnName(ClientDetailsDTO.class, "id") + " in "
				+ common.StringUtils.getCommaSeparatedString(IDListFromContact);

		return (Collection<ClientDetailsDTO>) SqlGenerator.getAllObjectList(ClientDetailsDTO.class, databaseConnection,
				conditionString);
	}
	
	public boolean isClientDiscarded(ClientDetailsDTO clientDetailsDTO)
	{
		if(clientDetailsDTO.isDeleted())
			return true;
		Set<Integer> countableStateRemainderSet = new HashSet<>();
		countableStateRemainderSet.add(5301);
		Set<Integer> countableStateSet = new HashSet<>();
		for(Integer moduleID: ModuleConstants.ModuleMap.keySet())
		{
			for(Integer remainder: countableStateRemainderSet)
			{
				countableStateSet.add(-(moduleID * ModuleConstants.MULTIPLIER + remainder));
			}
		}
		
		StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus());
		if(stateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE || stateDTO.getActivationStatus() == EntityTypeConstant.STATUS_SEMI_ACTIVE)
		{
			return false;
		}
		if(stateDTO.getActivationStatus() == EntityTypeConstant.STATUS_NOT_ACTIVE && countableStateSet.contains(stateDTO.getId()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
