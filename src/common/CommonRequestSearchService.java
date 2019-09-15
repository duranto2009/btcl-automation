package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.history.HistoryDAO;
import connection.DatabaseConnection;
import login.LoginDTO;
import request.CommonRequestDTO;
import request.RequestSearchDAO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.ActivityLogDTO;
import util.DAOResult;
import util.NavigationService;
import util.RecordNavigationManager;
import util.SqlGenerator;
import vpn.client.ClientDAO;
import vpn.client.ClientService;

public class CommonRequestSearchService implements NavigationService{
	RequestSearchDAO searchDao = new RequestSearchDAO();
	CommonRequestDTO comDTO=new CommonRequestDTO();
	Logger logger = Logger.getLogger(getClass());
	int entityTypeID;
	long iEntityID ;
	public  CommonRequestSearchService(int entityTypeID, long iEntityID ) {
		// TODO Auto-generated constructor stub
		this.entityTypeID=entityTypeID;
		this.iEntityID=iEntityID;
	}
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		logger.debug("Inside getID of common");
       	Collection collection=null;
       	CommonDAO commonDAO= new CommonDAO();
       
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try{
        	databaseConnection.dbOpen();
        	databaseConnection.dbTransationStart();
        	
        	Hashtable<String, String> criteriaMap = new Hashtable<String, String>();
        	criteriaMap.put("entityID", String.valueOf(this.iEntityID));
        	criteriaMap.put("entityTypeID", String.valueOf(this.entityTypeID));
        	
        	RequestSearchDAO requestSearchDAO = new RequestSearchDAO();
        	collection = requestSearchDAO.getIDsWithSearchCriteria(this.entityTypeID/100, criteriaMap, loginDTO, databaseConnection);
        	
        	databaseConnection.dbTransationEnd();
        }
        catch(Exception ex){
        	logger.debug("Exception", ex);
        	try{
        		databaseConnection.dbTransationRollBack();
        	}catch(Exception ex2){
        		logger.debug(ex2);
        	}
        }finally{
        	databaseConnection.dbClose();
        }
        return collection;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        logger.debug("Inside get Id with criteria of common");
        
        logger.debug(searchCriteria);
       	Collection collection=null;

       	DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			searchCriteria.put("entityID", String.valueOf(this.iEntityID));
			searchCriteria.put("entityTypeID", String.valueOf(this.entityTypeID));
        	RequestSearchDAO requestSearchDAO = new RequestSearchDAO();
        	collection = requestSearchDAO.getIDsWithSearchCriteria(this.entityTypeID/100, searchCriteria, loginDTO, databaseConnection);
	
			databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			logger.debug("Exception caught inside getIDsWithSearchCriteria method of CommonRequestSearchService ",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){
				logger.debug("Exception caught inside getIDsWithSearchCriteria method of CommonRequestSearchService ",ex2);
			}
		}finally{
			databaseConnection.dbClose();
		}
        return collection;
	}

	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ActivityLogDTO activityLogDTO = new ActivityLogDTO();
		DAOResult daoResult = null;
		Collection collection = null;
		CommonDAO commonDAO= new CommonDAO();
		logger.debug(recordIDs);
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
	        collection = commonDAO.getDTOs(recordIDs, databaseConnection);
	        //SqlGenerator.getAllIDList(CommonRequestDTO.class, databaseConnection, recordIDs.toArray()[0]);
	        databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			logger.debug("Exception caught inside getDTO method of CommonRequestSearchService "+ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){
				logger.debug("Exception caught inside getDTO method of CommonRequestSearchService "+ex2);
			}
		}finally{
			databaseConnection.dbClose();
		}
		logger.debug(collection);
		return collection;
	}
	
	public CommonRequestSearchDTO buildDTO(Hashtable searchCriteria) {
		CommonRequestSearchDTO dto= new CommonRequestSearchDTO();
		SimpleDateFormat sdf=new  SimpleDateFormat("dd/MM/yyyy");
		String searchFieldInfo[][]= SessionConstants.SEARCH_COMMON_REQUEST;
		if(searchFieldInfo != null && searchFieldInfo.length > 0)
		{
			for(int i = 0; i < searchFieldInfo.length;i++) {
					
			}
			dto.setName((String)searchCriteria.get((String)searchFieldInfo[0][1]));
			
			dto.setRequestTypeID(Integer.parseInt((String)searchCriteria.get((String)searchFieldInfo[1][1])));
			try {
				dto.setFromDate( sdf.parse((String)searchCriteria.get((String)searchFieldInfo[2][1])).getTime());
			} catch (Exception e) {
				dto.setFromDate(0);
			}
			try {
				dto.setToDate( sdf.parse((String)searchCriteria.get((String)searchFieldInfo[3][1])).getTime());
			} catch (Exception e) {
				dto.setToDate(0);
			}
			dto.setDescription((String)searchCriteria.get((String)searchFieldInfo[4][1]));
					
		}
		 return dto;
	}
	
	class CommonRequestSearchDTO{
		int iEntityTypeID;
		long iEntityID;
		String name="";
		String description="";
		long fromDate=0;
		long toDate=0;
		int requestTypeID=0;
		boolean isReq=false;
		
		public int getiEntityTypeID() {
			return iEntityTypeID;
		}

		public void setiEntityTypeID(int iEntityTypeID) {
			this.iEntityTypeID = iEntityTypeID;
		}

		public long getiEntityID() {
			return iEntityID;
		}

		public void setiEntityID(long iEntityID) {
			this.iEntityID = iEntityID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public long getFromDate() {
			return fromDate;
		}

		public void setFromDate(long fromDate) {
			this.fromDate = fromDate;
		}

		public long getToDate() {
			return toDate;
		}

		public void setToDate(long toDate) {
			this.toDate = toDate;
		}

		public int getRequestTypeID() {
			return requestTypeID;
		}

		public void setRequestTypeID(int requestTypeID) {
			if(requestTypeID<0){
				this.requestTypeID=0;
			}else{
				this.requestTypeID = requestTypeID;
			}
		}

		public boolean isReq() {
			return isReq;
		}

		public void setReq(boolean isReq) {
			this.isReq = isReq;
		}

		@Override
		public String toString() {
			return "CommonRequestSearchDTO [iEntityTypeID=" + iEntityTypeID + ", iEntityID=" + iEntityID + ", name="
					+ name + ", description=" + description + ", fromDate=" + fromDate + ", toDate=" + toDate
					+ ", requestTypeID=" + requestTypeID + ", isReq=" + isReq + "]";
		}
		
	}
}