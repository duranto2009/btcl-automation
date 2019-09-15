package request.repository;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.lowagie.text.html.simpleparser.IncTable;

import connection.DatabaseConnection;

import java.util.*;
import repository.Repository;
import request.CommonRequestDTO;
import util.SqlGenerator;
import static util.SqlGenerator.*;

public class CommonRequestRepository implements Repository {
	Logger logger = Logger.getLogger(getClass());
	HashMap<Long,CommonRequestDTO> mapOfCommonRequestToCommonRequestID;
	HashMap<Integer,Integer> mapOfRequestTypeCountToRequestTypeID;
	HashMap<Long,Integer> mapOfClientAccountCountToClientAccountID;
	HashMap<Long,Integer> mapOfRequestedToCountToRequestedToID;
	
	private void incrimentCounts(CommonRequestDTO commonRequestDTO){
		incrimentClientCount(commonRequestDTO.getClientID());
		incrimentRequestedToCount(commonRequestDTO.getRequestToAccountID());
		incrimentRequestTypeCount(commonRequestDTO.getRequestTypeID());
	}
	
	private void decrimentCounts(CommonRequestDTO commonRequestDTO){
		decrementClientCount(commonRequestDTO.getClientID());
		decrementRequestTypeCount(commonRequestDTO.getRequestTypeID());
		decrimentRequestedToCount(commonRequestDTO.getRequestToAccountID());
	}
	
	public int getRequestTypeCountToRequestTypeID(Integer requestTypeID){
		Integer count  = mapOfRequestTypeCountToRequestTypeID.get(requestTypeID);
		return (count!=null)? count: 0;
	}
	private void incrimentRequestTypeCount(int requestTypeID){
		Integer count = mapOfRequestTypeCountToRequestTypeID.get(requestTypeID);
		if(count == null){
			count = new Integer(0);
			mapOfRequestTypeCountToRequestTypeID.put(requestTypeID, count);
		}
		count++;
	}
	
	private void decrementRequestTypeCount(int requestTypeID){
		Integer count = mapOfRequestTypeCountToRequestTypeID.get(requestTypeID);
		if(count == null){
			return;
		}
		count--;
	}
	
	private void incrimentClientCount(long clientID){
		Integer count = mapOfClientAccountCountToClientAccountID.get(clientID);
		if(count == null){
			count = new Integer(0);
			mapOfClientAccountCountToClientAccountID.put(clientID, count);
		}
		count++;
	}
	private void decrementClientCount(long clientID){
		Integer count = mapOfClientAccountCountToClientAccountID.get(clientID);
		if(count == null){
			return;
		}
		count--;
	}
	private void incrimentRequestedToCount(long requestedToID){
		Integer count = mapOfRequestedToCountToRequestedToID.get(requestedToID);
		if(count == null){
			count = new Integer(0);
			mapOfRequestedToCountToRequestedToID.put(requestedToID, count);
		}
		count++;
	}
	private void decrimentRequestedToCount(long requestedToID){
		Integer count = mapOfRequestedToCountToRequestedToID.get(requestedToID);
		if(count == null){
			return;
		}
		count--;
	}
	
	private CommonRequestRepository(){
		mapOfCommonRequestToCommonRequestID = new HashMap<Long, CommonRequestDTO>();
		mapOfRequestTypeCountToRequestTypeID = new HashMap<Integer, Integer>();
		mapOfClientAccountCountToClientAccountID = new HashMap<Long, Integer>();
		mapOfRequestedToCountToRequestedToID = new HashMap<Long, Integer>();
	}

	public CommonRequestDTO getCommonRequestDTOByCommonRequestID(long commmonRequestID){
		logger.debug("mapOfCommonRequestToCommonRequestID " + mapOfCommonRequestToCommonRequestID);
		return mapOfCommonRequestToCommonRequestID.get(commmonRequestID);
	}
	
	
	
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			List<CommonRequestDTO> commonRequestDTOs = (List<CommonRequestDTO>)getAllObjectForRepository(CommonRequestDTO.class, databaseConnection, isFirstReload);
			for(CommonRequestDTO commonRequestDTO: commonRequestDTOs){
				mapOfCommonRequestToCommonRequestID.put(commonRequestDTO.getReqID(),commonRequestDTO );
				if(isFirstReload){
					if(!commonRequestDTO.isDeleted() && commonRequestDTO.isPending()){
						incrimentCounts(commonRequestDTO);
					}
				}else{
					if(!commonRequestDTO.isDeleted() && !commonRequestDTO.isPending()){
						decrimentCounts(commonRequestDTO);
					}
				}
			}
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		
	}

	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(CommonRequestDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;

	}

}
