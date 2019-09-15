/**
 * 
 */
package common.note;

import java.util.List;

import org.apache.log4j.Logger;
import connection.DatabaseConnection;
import util.SqlGenerator;

/**
 * 
 * @author Alam
 */
public class CommonNoteService {

	public static Logger logger = Logger.getLogger(CommonNoteService.class);
	
	/**
	 * 
	 * @author Alam
	 * @param id
	 * @return
	 */
	public static CommonNote getById( long id ){
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try {
			
			databaseConnection.dbOpen();
			return (CommonNote) SqlGenerator.getObjectByID( CommonNote.class, id, databaseConnection );
		} 
		catch (Exception ex) {
		
			logger.debug("fatal", ex);
		} 
		finally {
			
			databaseConnection.dbClose();
		}
		return null;
	}
	
	/**
	 * 
	 * @author Alam
	 * @param note
	 * @throws Exception
	 */
	public void insert( CommonNote note ) throws Exception{
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			SqlGenerator.insert( note, note.getClass(), databaseConnection, false );
			
			databaseConnection.dbTransationEnd();
		}
		catch( Exception e){
			
			try{
				
				databaseConnection.dbTransationRollBack();
			}
			catch( Exception ex ){}
			
			throw e;
		}
		finally {
			
			databaseConnection.dbClose();
		}
	}

	public void insert( CommonNote note, DatabaseConnection  databaseConnection) throws Exception{
		SqlGenerator.insert( note, note.getClass(), databaseConnection, false );
	}

	/**
	 * @author Alam
	 * @param entityTypeId
	 * @param entityId
	 * @return
	 * @throws Exception 
	 */
	
	public static CommonNote getNote(long entityTypeId, long entityId, long requestID) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			
			databaseConnection.dbOpen();
			
			String conditionString = " where " + SqlGenerator.getColumnName( CommonNote.class, "entityTypeId" ) + " = " + entityTypeId
					   + " and " + SqlGenerator.getColumnName( CommonNote.class, "entityId" ) + " = " + entityId
					   + " and " + SqlGenerator.getColumnName( CommonNote.class, "reqID" ) + " = " + requestID;
			
			List<CommonNote> notes = (List<CommonNote>) SqlGenerator.getAllObjectList( CommonNote.class, databaseConnection, conditionString );
			
			if( notes != null && notes.size() > 0){
				
				return notes.get(0);
			}
		}
		catch( Exception ex ){
			
			logger.debug( "Exception while getting common note by document type id and document id \n" + ex.toString() );
			throw ex;
		}
		finally {
			
			databaseConnection.dbClose();
		}
		return null;
	}
}
