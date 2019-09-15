package request;

import java.util.*;
import org.apache.log4j.Logger;
import common.EntityTypeConstant;
import common.ModuleConstants;
import connection.DatabaseConnection;
import util.SqlGenerator;


public class StateRepository {
	Logger logger = Logger.getLogger(getClass());
	private static StateRepository instance = null;
	private static HashMap<Integer, StateDTO> StateDTOByStateID;
	private Map<Integer,Map<Integer,List<Integer>>> mapOfEntityStatusToEntityTypeIDToActivationStatus;
	
	private Map<Integer,List<Integer>> mapOfEntityStatusListToActivationStatus;
	private Map<Integer,Map<Integer,List<Integer>>> mapOfEntitytStatusListToActivationStatusToModuleID;
	
	private StateRepository()
	{
		StateDTOByStateID = new HashMap<Integer, StateDTO>();
		mapOfEntityStatusToEntityTypeIDToActivationStatus = new HashMap<Integer, Map<Integer,List<Integer>>>();
		
		mapOfEntityStatusListToActivationStatus = new HashMap<>();
		mapOfEntitytStatusListToActivationStatusToModuleID = new HashMap<>();
		
		reload();
	}
	public static StateRepository getInstance()
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
			instance = new StateRepository();
		}
		
	}
	public synchronized Collection<StateDTO> getAllStateDTOs()
	{		
		return StateDTOByStateID.values();
	}	
	public synchronized StateDTO getStateDTOByStateID(int stateID)
	{
		//logger.debug("StateNameByStateID " + StateDTOByStateID);
		return StateDTOByStateID.get(stateID);
	}
	private void reload()
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			ArrayList<StateDTO> stateList = (ArrayList<StateDTO>) SqlGenerator.getAllObjectForRepository(StateDTO.class, databaseConnection, true);
			for(StateDTO sdto: stateList)
			{
				StateDTOByStateID.put(sdto.getId(), sdto);
				
				int entityStatus = sdto.getId();
				int activationStatus = sdto.getActivationStatus();
				int moduleID = Math.abs(sdto.getId()/ModuleConstants.MULTIPLIER);
				
				
				if(!mapOfEntityStatusListToActivationStatus.containsKey(activationStatus)){
					mapOfEntityStatusListToActivationStatus.put(activationStatus, new ArrayList<>());
				}
				mapOfEntityStatusListToActivationStatus.get(activationStatus).add(entityStatus);
				
				if(!mapOfEntitytStatusListToActivationStatusToModuleID.containsKey(moduleID)){
					mapOfEntitytStatusListToActivationStatusToModuleID.put(moduleID, new HashMap<>());
				}
				if(!mapOfEntitytStatusListToActivationStatusToModuleID.get(moduleID).containsKey(activationStatus)){
					mapOfEntitytStatusListToActivationStatusToModuleID.get(moduleID).put(activationStatus, new ArrayList<>());
				}
				mapOfEntitytStatusListToActivationStatusToModuleID.get(moduleID).get(activationStatus).add(entityStatus);

				//logger.debug("activation Status"+activationStatus+" and entityTypeID "+entityTypeID);
			}
		}
		catch(Exception ex)
		{
			logger.debug("Fatal", ex);
		}
		finally {
			databaseConnection.dbClose();
		}
		
	}
	
	public List<Integer> getStatusListByModuleIDAndActivationStatus(int moduleID,int activationStatus){
		if(!mapOfEntitytStatusListToActivationStatusToModuleID.containsKey(moduleID)){
			return Collections.EMPTY_LIST;
		}
		if(!mapOfEntitytStatusListToActivationStatusToModuleID.get(moduleID).containsKey(activationStatus)){
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(mapOfEntitytStatusListToActivationStatusToModuleID.get(moduleID).get(activationStatus));
	}
	
	public List<Integer> getStatusListByActivationStatus(int activationStatus){
		if(!mapOfEntityStatusListToActivationStatus.containsKey(activationStatus)){
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(mapOfEntityStatusListToActivationStatus.get(activationStatus));
	}
	
	
	/*
	public List<Integer> getStatusListByActivationStatus(int activationStatus){
		if(!mapOfEntityStatusToEntityTypeIDToActivationStatus.containsKey(activationStatus)){
			return new ArrayList<Integer>();
		}
		List<Integer> statusList = new ArrayList();
		for(List list: mapOfEntityStatusToEntityTypeIDToActivationStatus.get(activationStatus).values()){
			statusList.addAll(list);
		}
		
		return statusList;
	}*/
}
