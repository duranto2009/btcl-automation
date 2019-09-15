package common.history;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import common.CommonDAO;
import connection.DatabaseConnection;
import login.LoginDTO;
import request.CommonRequestDTO;
import request.RequestUtilDAO;
import util.UtilService;

public class HistoryDAO {
	public static Logger logger = Logger.getLogger(HistoryDAO.class);
	/*public ArrayList<Long> getHistoryIDs(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws Exception
	{
		logger.debug("comDTO " + comDTO);
		CommonDAO commonDAO = new CommonDAO();
		RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
		Set<Long> idSet = null;
		if(comDTO.getReqID() > 0)
		{
			idSet = new HashSet<Long>();
			idSet.add(comDTO.getReqID());
		}
		else
		{
			idSet = requestUtilDAO.getMainRoots(comDTO, false, false, databaseConnection);
		}
		
		logger.debug("idSet " + idSet);
		if(idSet == null || idSet.size() == 0) return new ArrayList<Long>();
		
		ArrayList<ArrayList<Long>> listOfHistoryIDList = new ArrayList<ArrayList<Long>>();

		for(Long reqID: idSet)
		{
			ArrayList<Long> historyIDs = new ArrayList<Long>();
			listOfHistoryIDList.add(historyIDs);
			requestUtilDAO.getRequestsHavingCommonRootRequestID(reqID, historyIDs, databaseConnection);
		}
		logger.debug("listOfHistoryIDList.get(0) " + listOfHistoryIDList.get(0));
		ArrayList<Long> mergedHistoryIDs = new ArrayList<Long>(); 
		
		for(ArrayList<Long> historyIDs: listOfHistoryIDList)
		{
			mergedHistoryIDs.addAll(historyIDs);			
		}
		
		
		
		Collections.sort(mergedHistoryIDs, new Comparator<Long>() {
		    public int compare(Long o1, Long o2) {
		        return o2.compareTo(o1);
		    }
		});
		return mergedHistoryIDs;
	}*/
	
	@SuppressWarnings("unchecked")
	public ArrayList<CommonRequestDTO> getHistoryDTOs(int start, CommonRequestDTO comDTO,	DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {
		CommonDAO commonDAO = new CommonDAO();
		UtilService utilService = new UtilService();
		RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
		
		ArrayList<CommonRequestDTO> commonRequestDTOs = new ArrayList<CommonRequestDTO>();
		ArrayList<Long> historyIDs = new ArrayList<Long>();
		String sql;
		if(comDTO.getReqID() > 0) {
			requestUtilDAO.getRequestsHavingCommonRootRequestID(comDTO.getReqID(), historyIDs, databaseConnection);
		}
		else{
			sql = "SELECT arID FROM at_req WHERE arEntityTypeID = " + comDTO.getEntityTypeID() + " AND arEntityID = " + comDTO.getEntityID() + " AND arRootRequestID IS null";

			Statement statement = databaseConnection.getNewStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				// historyIDs.add(resultSet.getLong("arID"));
				requestUtilDAO.getRequestsHavingCommonRootRequestID(resultSet.getLong("arID"), historyIDs, databaseConnection);
			}
		} 

		StringBuilder conditionString = new StringBuilder();
		if (historyIDs.size() > 0)
			conditionString.append(" where " + getColumnName(CommonRequestDTO.class, "reqID") + " IN "
					+ utilService.arrayListTOString(historyIDs) + " order by "
					+ getColumnName(CommonRequestDTO.class, "requestTime") + " desc limit " + start + " , 10");
		else {
			return commonRequestDTOs;
		}

		return (ArrayList<CommonRequestDTO>) getAllObjectList(CommonRequestDTO.class, databaseConnection,
				conditionString);
	}
	
	public StringBuilder getCommonHistoryID(long entityTypeID, long entityID, boolean isReq,
			DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {
		RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
		UtilService utilService = new UtilService();
		
		ArrayList<Long> historyIDs = new ArrayList<Long>();
		if (!isReq) {
			String sql = "SELECT arID FROM at_req WHERE arEntityTypeID = " + entityTypeID + " AND arEntityID = "
					+ entityID + " AND arRootRequestID IS null";
			Statement statement = databaseConnection.getNewStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				requestUtilDAO.getRequestsHavingCommonRootRequestID(resultSet.getLong("arID"), historyIDs, databaseConnection);
			}
		} else {
			requestUtilDAO.getRequestsHavingCommonRootRequestID(entityID, historyIDs, databaseConnection);
		}

		StringBuilder conditionString = new StringBuilder();
		if (historyIDs.size() > 0)
			conditionString.append(" where " + getColumnName(CommonRequestDTO.class, "reqID") + " IN "
					+ utilService.arrayListTOString(historyIDs));

		return conditionString;
	}	
	
	public ArrayList<CommonRequestDTO> getHistoryForSearch(int iEntityTypeID, long iEntityID, String name,
			String description, long fromDate, long toDate, int requestTypeID, boolean isReq,
			DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {
		CommonDAO commonDAO = new CommonDAO();
		StringBuilder conditionString = new StringBuilder();
		
		conditionString.append(getCommonHistoryID(iEntityTypeID, iEntityID, isReq, databaseConnection, loginDTO));
		if (conditionString.length() > 0) {
			if (!name.equalsIgnoreCase("")) {
				ArrayList<Long> clientIDs = commonDAO.getClientIDsFromName(databaseConnection, name);
				ArrayList<Long> userIDs = commonDAO.getUserIDsFromName(databaseConnection, name);
				commonDAO.addUserNameCondition(conditionString, clientIDs, userIDs);
			}
			if (requestTypeID != 0) {
				conditionString.append(
						" and " + getColumnName(CommonRequestDTO.class, "requestTypeID") + " = " + requestTypeID);
			}
			if (!description.equalsIgnoreCase("")) {
				conditionString.append(" and " + getColumnName(CommonRequestDTO.class, "description") + " like '%"
						+ description + "%' ");
			}
			if (fromDate > 0 && toDate > 0) {
				conditionString.append(" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " BETWEEN "
						+ fromDate + " AND " + toDate + 86400000);
			} else if (fromDate > 0 && toDate == 0) {
				conditionString
						.append(" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " >= " + fromDate);
			} else if (fromDate == 0 && toDate > 0) {
				conditionString.append(
						" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " <= " + (toDate + 86400000));
			}
			conditionString.append(
					" order by " + getColumnName(CommonRequestDTO.class, "requestTime") + " desc limit " + 0 + " , 20");

			return (ArrayList<CommonRequestDTO>) getAllObjectList(CommonRequestDTO.class, databaseConnection, conditionString);
		} else {
			return new ArrayList<CommonRequestDTO>();

		}
	}
	
	public Collection getIDsWithSearchCriteria(int iEntityTypeID, long iEntityID, String name,
			String description, long fromDate, long toDate, int requestTypeID, boolean isReq,
			DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {

		CommonDAO commonDAO = new CommonDAO();
		
		ArrayList<CommonRequestDTO> commonRequestDTOs = new ArrayList<CommonRequestDTO>();
		ArrayList<Long> historyIDs = new ArrayList<Long>();
		
		StringBuilder conditionString = new StringBuilder();
		conditionString.append(getCommonHistoryID(iEntityTypeID, iEntityID, isReq, databaseConnection, loginDTO));
	
		
		if (conditionString.length() > 0) {
			if (!name.equalsIgnoreCase("")) {
				ArrayList<Long> clientIDs = commonDAO.getClientIDsFromName(databaseConnection, name);
				ArrayList<Long> userIDs = commonDAO.getUserIDsFromName(databaseConnection, name);
				commonDAO.addUserNameCondition(conditionString, clientIDs, userIDs);
			}
			if (requestTypeID != 0) {
				conditionString.append(
						" and " + getColumnName(CommonRequestDTO.class, "requestTypeID") + " = " + requestTypeID);
			}
			if (!description.equalsIgnoreCase("")) {
				conditionString.append(" and " + getColumnName(CommonRequestDTO.class, "description") + " like '%"
						+ description + "%' ");
			}
			if (fromDate > 0 && toDate > 0) {
				conditionString.append(" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " BETWEEN "
						+ fromDate + " AND " + toDate + 86400000);
			} else if (fromDate > 0 && toDate == 0) {
				conditionString
						.append(" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " >= " + fromDate);
			} else if (fromDate == 0 && toDate > 0) {
				conditionString.append(
						" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " <= " + (toDate + 86400000));
			}
			conditionString.append(
					" order by " + getColumnName(CommonRequestDTO.class, "requestTime") + " desc limit " + 0 + " , 20");

			commonRequestDTOs=(ArrayList<CommonRequestDTO>) getAllObjectList(CommonRequestDTO.class, databaseConnection, 	conditionString);
			
			logger.debug(commonRequestDTOs.size());
			for(CommonRequestDTO dto: commonRequestDTOs){
				historyIDs.add(dto.getReqID());
			}
			logger.debug(historyIDs);
			return historyIDs;
		} else {
			return  historyIDs;

		}
	}
}
