package request;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import permission.StateActionDTO;

/*
 * 
 * One time load repository
 * 
 */

public class RequestStateActionRepository {

	Logger logger = Logger.getLogger(getClass()); 
	private static RequestStateActionRepository instance = null;
	private static Map<Integer, StateActionDTO> StateActionDTOByStateID;
	private static Map<Integer, StateActionDTO> StateActionDTOBySaID;
	private static Map<String, Integer> StateActionIDByStateRequestID;
	private RequestStateActionRepository()
	{
		StateActionDTOByStateID = new HashMap<Integer, StateActionDTO>();
		StateActionDTOBySaID = new HashMap<Integer, StateActionDTO>() ;
		StateActionIDByStateRequestID = new HashMap<String, Integer>();
		reload();		
	}
	public static RequestStateActionRepository getInstance()
	{
		if(instance == null)
		{
			createInstance();
		}
		return instance;
	}
	private synchronized static void createInstance()
	{
		if(instance == null)
		{
			instance = new RequestStateActionRepository();
		}		
	}
	public Collection getAllDTOs()
	{
		return StateActionDTOBySaID.values();
	}
	public StateActionDTO getStateActionByStateID(int stateID)
	{
//		logger.debug("StateActionDTOByStateID " + StateActionDTOByStateID);
		return StateActionDTOByStateID.get(stateID);
	}
	public Integer getStateActionIDByStateReqeustID(String stateID, String actionTypeID)
	{
//		logger.debug("StateActionIDByStateRequestID " + StateActionIDByStateRequestID);
		return StateActionIDByStateRequestID.get(stateID + "_" + actionTypeID);
	}	
	public Integer getStateActionIDByStateReqeustID(int stateID, int actionTypeID)
	{
//		logger.debug("StateActionIDByStateRequestID " + StateActionIDByStateRequestID);
		return StateActionIDByStateRequestID.get(stateID + "_" + actionTypeID);
	}

	public StateActionDTO getStateActionBySaID(int saID)//primary key
	{
//		logger.debug("StateActionDTOBySaID " + StateActionDTOBySaID);
		return StateActionDTOBySaID.get(saID);
	}
	public void reload()
	{
		logger.debug("reload");
		DatabaseConnection dbConnection = new DatabaseConnection();
		try
		{
			dbConnection.dbOpen();
			dbConnection.dbTransationStart();
			String sql = "select * from at_state_actiontype";
			Statement stmt = dbConnection.getNewStatement();
			ResultSet rs =  stmt.executeQuery(sql);
			while(rs.next())
			{
				StateActionDTO dto = new StateActionDTO();
				dto.setSaID(rs.getInt("saID"));				
				dto.setStateID(rs.getInt("saCurrentStateID"));
				
				Set<Integer> set = new HashSet<Integer>();
				int actionTypeID = rs.getInt("saActionTypeID");
				set.add(actionTypeID);
				dto.setActionTypeIDs(set);
				StateActionDTOBySaID.put(dto.getSaID(), dto);
				StateActionIDByStateRequestID.put(dto.getStateID() + "_" + actionTypeID, dto.getSaID());
				
				StateActionDTO dto2 = StateActionDTOByStateID.get(dto.getStateID());
				if(dto2 == null)
				{					
					dto2 = new StateActionDTO();
					StateActionDTOByStateID.put(dto.getStateID(), dto2);
				}
								
				dto2.setSaID(dto.getSaID());
				dto2.setStateID(dto.getStateID());
				Set<Integer> set2 = dto2.getActionTypeIDs();
				if(set2 == null)
				{
					set2 = new HashSet<Integer>();
					dto2.setActionTypeIDs(set2);
				}

				set2.add(actionTypeID);
				
				
			}
			dbConnection.dbTransationEnd();
//			logger.debug("StateActionDTOByStateID " + StateActionDTOByStateID);
//			logger.debug("StateActionDTOBySaID " + StateActionDTOBySaID);
		}
		catch(Exception ex)
		{
			logger.debug("Fatal ", ex);
			try {
				dbConnection.dbTransationRollBack();
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		finally {
			dbConnection.dbClose();
		}
		
	}
	
	public static void main(String args[])
	{
		RequestStateActionRepository.getInstance();
	}
}
