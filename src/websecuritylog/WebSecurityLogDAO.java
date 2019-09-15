package websecuritylog;

import static util.SqlGenerator.getAllIDList;
import static util.SqlGenerator.getColumnName;
import static util.SqlGenerator.getIDListFromSearchCriteria;
import static util.SqlGenerator.getObjectByID;
import static util.SqlGenerator.getObjectListByIDList;
import static util.SqlGenerator.getTableName;
import static util.SqlGenerator.insert;
import static util.SqlGenerator.updateEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import util.DatabaseConnectionFactory;
import util.TimeConverter;

public class WebSecurityLogDAO {
	Logger logger = Logger.getLogger(getClass());

	public WebSecurityLogDTO getWebSecurityLogById(long wslId, DatabaseConnection databaseConnection) throws Exception {
		WebSecurityLogDTO webSecurityLogDTO = (WebSecurityLogDTO) getObjectByID(WebSecurityLogDTO.class, wslId,
				databaseConnection);
		return webSecurityLogDTO;
	}

	public void insertWebSecurityLog(WebSecurityLogDTO webSecurityDTO, DatabaseConnection databaseConnection)
			throws Exception {
		insert(webSecurityDTO, WebSecurityLogDTO.class, databaseConnection, false);
	}

	public void updateWebSecurityLog(WebSecurityLogDTO webSecurityDTO, DatabaseConnection databaseConnection)
			throws Exception {
		updateEntity(webSecurityDTO, WebSecurityLogDTO.class, databaseConnection, false, false);
	}

	public List<Long> getIDs(DatabaseConnection databaseConnection) throws Exception {
		Class classObject = WebSecurityLogDTO.class;
		String fixedCondition = " where  " + getColumnName(classObject, "isDeleted") + " = 0";
		fixedCondition += " order by " + getColumnName(classObject, "attemptTime") + " desc ";
		return (List<Long>) getAllIDList(classObject, databaseConnection, fixedCondition);
	}

	public List<Long> getLogListBySearchCriteri(Hashtable searchCriteria, DatabaseConnection databaseConnection)
			throws Exception {
		List<Long> IDList = new ArrayList<Long>();
		Class classObject = WebSecurityLogDTO.class;
		String tableName = getTableName(classObject);

		if (searchCriteria.get("awslAttemptType").equals("-1")) {
			searchCriteria.put("awslAttemptType", "");
		}

		String[] keys = new String[] { "awslUsername", "awslAttemptType", "awslTimeTo", "awslTimeFrom", "awslIpAddress",
				"awslPort" };
		String[] operators = new String[] { "like", "=", "<=", ">=", "=", "=" };
		String[] dtoColumnNames = new String[] { "username", "attemptType", "attemptTime", "attemptTime", "url",
				"port" };

		String fixedCondition = " " + getColumnName(classObject, "isDeleted") + " = 0";
		fixedCondition += " order by " + getColumnName(classObject, "attemptTime") + " desc ";

		IDList = getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, searchCriteria,
				fixedCondition, databaseConnection);
		return IDList;
	}

	public List<WebSecurityLogDTO> getLogDTOListByIDList(List<Long> logIDList, DatabaseConnection databaseConnection)
			throws Exception {
		Class classObject = WebSecurityLogDTO.class;

		String fixedCondition = "   " + getColumnName(classObject, "isDeleted") + " = 0";
		fixedCondition += " order by " + getColumnName(classObject, "attemptTime") + " desc ";

		return (List<WebSecurityLogDTO>) getObjectListByIDList(WebSecurityLogDTO.class, logIDList, databaseConnection,
				fixedCondition);
	}

	public List<HashMap<String, String>> getAllUserAttemptsByAttemptCategoryAndDuration(int attemptCategory,
			DateTime startDate, DateTime endDate) throws Exception {

		List<HashMap<String, String>> allAttemptsByAttemptCategoryAndDuration = new ArrayList<HashMap<String, String>>();

		long startDateTimeLong = startDate.getMillis();
		long endDateTimeLong = endDate.getMillis();

		String attemptTypeQuery = " ";
		if (attemptCategory != 0) {
			attemptTypeQuery = " AND awslAttemptType = " + attemptCategory + " ";
		}
		String timeQuery = " AND " + startDateTimeLong + " < lastAttemptTime AND " + endDateTimeLong
				+ " > lastAttemptTime ";

		String sql = "select awslUsername, count(awslUsername) as attemptCount, max(awslAttemptTime) as lastAttemptTime from at_web_security where awslUsername != 'N/A' "
				+ attemptTypeQuery + " GROUP BY awslUsername HAVING attemptCount>0 " + timeQuery
				+ " ORDER BY attemptCount DESC LIMIT " + WebSecurityConstant.ATTEMPT_LIST_LIMIT;

		logger.debug(sql);

		/*
		 * DatabaseConnection databaseConnection =
		 * DatabaseConnectionFactory.getCurrentDatabaseConnection();
		 * PreparedStatement preparedStatement =
		 * databaseConnection.getNewPrepareStatement(sql); ResultSet resultSet =
		 * preparedStatement.executeQuery();
		 */

		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseManager.getInstance().getConnection();
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("username", resultSet.getString("awslUsername"));
				tempMap.put("count", resultSet.getString("attemptCount"));
				tempMap.put("lastAttemptTime", TimeConverter.getMeridiemTime(resultSet.getLong("lastAttemptTime")));

				allAttemptsByAttemptCategoryAndDuration.add(tempMap);
			}
			logger.debug(
					"allAttemptsByAttemptCategoryAndDuration size:  " + allAttemptsByAttemptCategoryAndDuration.size());

		} catch (Exception e) {
			logger.fatal("DAO " + e.toString(), e);
		} finally {
			try {
				if (resultSet != null && !resultSet.isClosed()) {
					resultSet.close();
				}
			} catch (Exception ex) {

			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (connection != null) {
					DatabaseManager.getInstance().freeConnection(connection);
				}
			} catch (Exception e) {
				logger.fatal("DAO finally :" + e.toString());
			}
		}
		// return data;

		return allAttemptsByAttemptCategoryAndDuration;

	}

	public List<HashMap<String, String>> getAllIpAttemptsByAttemptCategoryAndDuration(int attemptCategory,
			DateTime startDate, DateTime endDate) throws Exception {

		List<HashMap<String, String>> allAttemptsByAttemptCategoryAndDuration = new ArrayList<HashMap<String, String>>();

		long startDateTimeLong = startDate.getMillis();
		long endDateTimeLong = endDate.getMillis();

		String attemptTypeQuery = " ";
		if (attemptCategory != 0) {
			attemptTypeQuery = " WHERE awslAttemptType = " + attemptCategory + " ";
		}
		String timeQuery = " AND " + startDateTimeLong + " < lastAttemptTime AND " + endDateTimeLong
				+ " > lastAttemptTime ";

		String sql = "select awslRemoteUrl, count(awslRemoteUrl) as attemptCount, max(awslAttemptTime) as lastAttemptTime from at_web_security "
				+ attemptTypeQuery + " GROUP BY awslRemoteUrl HAVING attemptCount>0 " + timeQuery
				+ " ORDER BY attemptCount DESC LIMIT " + WebSecurityConstant.ATTEMPT_LIST_LIMIT;

		logger.debug(sql);

		/*
		 * DatabaseConnection databaseConnection =
		 * DatabaseConnectionFactory.getCurrentDatabaseConnection();
		 * PreparedStatement preparedStatement =
		 * databaseConnection.getNewPrepareStatement(sql); ResultSet resultSet =
		 * preparedStatement.executeQuery();
		 */

		Connection connection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseManager.getInstance().getConnection();
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("ipAddress", resultSet.getString("awslRemoteUrl"));
				tempMap.put("count", resultSet.getString("attemptCount"));
				tempMap.put("lastAttemptTime", TimeConverter.getMeridiemTime(resultSet.getLong("lastAttemptTime")));

				allAttemptsByAttemptCategoryAndDuration.add(tempMap);
			}
			logger.debug(
					"allAttemptsByAttemptCategoryAndDuration size:  " + allAttemptsByAttemptCategoryAndDuration.size());

		} catch (Exception e) {
			logger.fatal("DAO " + e.toString(), e);
		} finally {
			try {
				if (resultSet != null && !resultSet.isClosed()) {
					resultSet.close();
				}
			} catch (Exception ex) {

			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (connection != null) {
					DatabaseManager.getInstance().freeConnection(connection);
				}
			} catch (Exception e) {
				logger.fatal("DAO finally :" + e.toString());
			}
		}
		// return data;

		return allAttemptsByAttemptCategoryAndDuration;

	}
}
