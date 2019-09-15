package comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import util.ActivityLogDTO;
import util.DAOResult;
import util.NavigationService;
import login.LoginDTO;
import request.CommonRequestDTO;
import request.RequestSearchDAO;
import sessionmanager.SessionConstants;
import common.CommonDAO;
import common.EntityDTO;

import org.apache.log4j.Logger;
import connection.DatabaseConnection;

public class CommentService implements NavigationService {
	Logger logger = Logger.getLogger(getClass());
	CommentDAO commentDAO;
	long currentTime = 0;
	RequestSearchDAO searchDao = new RequestSearchDAO();
	CommonRequestDTO comDTO=new CommonRequestDTO();
	CommonDAO commonDAO= new CommonDAO();
	
	int entityTypeID;
	long iEntityID ;
	public  CommentService( ) {
		// TODO Auto-generated constructor stub
		
	}
	
	public  CommentService(int entityTypeID,long iEntityID ) {
		// TODO Auto-generated constructor stub
		this.entityTypeID=entityTypeID;
		this.iEntityID=iEntityID;
	}
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		logger.debug("Inside getID of common");
       	Collection collection=null;
       	CommentDAO commentDAO= new CommentDAO();
       
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try{
        	CommonRequestDTO comDTO= new CommonRequestDTO();
           	comDTO.setEntityTypeID(this.entityTypeID);
           	comDTO.setEntityID(this.iEntityID);
           	
        	databaseConnection.dbOpen();
        	databaseConnection.dbTransationStart();
        	collection = commentDAO.getIDs(this.iEntityID, databaseConnection, loginDTO);
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

	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        logger.debug("Inside get Id with criteria of common");
        
        logger.debug(searchCriteria);
       	Collection collection=null;
     	CommentDAO commentDAO= new CommentDAO();
        DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			collection=commentDAO.getIDs(this.iEntityID, databaseConnection, loginDTO);
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
		CommentDAO commentDAO= new CommentDAO();
		logger.debug(recordIDs);
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			collection=commentDAO.getDTOs(recordIDs, databaseConnection);
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
	
	
	
	public String addComment(CommentDTO commentDTO, LoginDTO loginDTO) throws Exception {
		commentDAO = new CommentDAO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			currentTime = System.currentTimeMillis();
			commentDTO.setLastModificationTime(currentTime);
			
			if(loginDTO.getIsAdmin()){
				commentDTO.setMemberID(-loginDTO.getUserID());
			}
			else{
				commentDTO.setMemberID(loginDTO.getAccountID());
			}
			commentDAO.insertComment(commentDTO, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				logger.fatal("Fatal: ", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				throw ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		
		return "success";

	}

	public ArrayList<CommentDTO> getComment(int iStart, int iEntityTypeID, long iEntityID, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<CommentDTO> commentLogDTOs = null;
		commentDAO = new CommentDAO();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commentLogDTOs = (ArrayList<CommentDTO>) commentDAO.getCommentDTOByEntityTypeAndIDAndModuleID( iEntityTypeID, iEntityID, databaseConnection, iStart, 10);
			
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				logger.debug("Fatal :", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				throw ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		return commentLogDTOs;
		
	}
	public EntityDTO getEntityDTOByEntityIDAndEntityTypeID(int entityTypeID, long entityID){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		EntityDTO entityDTO=null;
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			entityDTO= commonDAO.getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, entityID, databaseConnection);
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
		logger.debug(entityDTO);
		return entityDTO;
	}
}
