package note;

import java.util.Collection;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import login.LoginDTO;
import util.ActivityLogDTO;
import util.DAOResult;
import util.NavigationService;

public class NoteSearchService implements NavigationService{
	
	NoteSearchDAO searchDao = new NoteSearchDAO();
	int moduleID;
	
	public void setModuleID(int moduleID){
		this.moduleID = moduleID;
	} 
	
	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
       	Collection collection=null;
       	DatabaseConnection databaseConnection = new DatabaseConnection();
        
       	try{
       		databaseConnection.dbOpen();
        	databaseConnection.dbTransationStart();
        	collection = searchDao.getIDs(loginDTO,moduleID, databaseConnection);
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
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects ) throws Exception {
        logger.debug("Inside get Id with criteria of common");
       	Collection collection=null;
        DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			collection=  searchDao.getIDsWithSearchCriteria(moduleID,searchCriteria, loginDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			logger.debug("Exception caught inside getIDsWithSearchCriteria method of RequestSearchService ",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){
				logger.debug("Exception caught inside getIDsWithSearchCriteria method of RequestSearchService ",ex2);
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
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
	        collection = searchDao.getDTOs(recordIDs, databaseConnection);
	        databaseConnection.dbTransationEnd();
		}catch(Exception ex){
			logger.debug("Exception caught inside getDTO method of RequestSearchService ", ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){
				logger.debug("Exception caught inside getDTO method of RequestSearchService ", ex2);
			}
		}finally{
			databaseConnection.dbClose();
		}
		return collection;
	}
}
