package forgetPassword;

import static util.SqlGenerator.*;

import java.util.List;

import common.RequestFailureException;
import connection.DatabaseConnection;

/**
 * @author Alam
 */
public class ForgetPasswordDAO {

	/**
	 * This method add a new row to token table
	 * @author Alam
	 * @param forgetPassword
	 * @throws Exception 
	 */
	public static void add(ForgetPassword forgetPassword) throws Exception {
		
		DatabaseConnection db = new DatabaseConnection();
		
		try{
		
			db.dbOpen();
			db.dbTransationStart();
			
			insert( forgetPassword, ForgetPassword.class, db, false);
			
			db.dbTransationEnd();
		}
		catch(Exception ex) {
			
			try {
				
				db.dbTransationRollBack();
				
			} 
			catch (Exception ex2) {
				
			}
			if(ex instanceof RequestFailureException){
				
				throw (RequestFailureException)ex;
			}
			throw ex;
		} 
		finally {
			
			db.dbClose();
		}
	}

	/**
	 * This method returns a which mathces given username from token table
	 * @author Alam
	 * @param username
	 * @return ForgetPassword an ForgetPassword object. Null if not found
	 * @throws Exception 
	 */
	public static ForgetPassword getByUsername(String username) throws Exception {
		
		DatabaseConnection db = new DatabaseConnection();
		List<ForgetPassword> listOfForgetPassword = null;
		try {
			
			String whereClause = " where " + getColumnName(ForgetPassword.class, "username" ) + " = ?";
			
			db.dbOpen();
			db.dbTransationStart();
			
			listOfForgetPassword = (List<ForgetPassword>) getAllObjectList(ForgetPassword.class, db, whereClause, username );
			
			db.dbTransationEnd();	
			
		}
		catch(Exception ex) {
			
			System.out.println( ex.toString() );
			
			try {
				
				db.dbTransationRollBack();
				
			} 
			catch (Exception ex2) {
				
			}
			if(ex instanceof RequestFailureException){
				
				throw (RequestFailureException)ex;
			}
			throw ex;
		} 
		finally {
			
			db.dbClose();
		}
		
		if( listOfForgetPassword.size() == 1 )
			return listOfForgetPassword.get(0);
		else 
			return null;
	}

	/**
	 * This method updates a given row by given object.
	 * @author Alam
	 * @param forgetPassword
	 * @throws Exception 
	 */
	public static void update(ForgetPassword forgetPassword) throws Exception {
		
		DatabaseConnection db = new DatabaseConnection();
		
		try{
		
			db.dbOpen();
			db.dbTransationStart();
			
			updateEntity(forgetPassword, ForgetPassword.class, db, false, false );
			
			db.dbTransationEnd();
		}
		catch(Exception ex) {
			
			try {
				
				db.dbTransationRollBack();
				
			} 
			catch (Exception ex2) {
				
			}
			if(ex instanceof RequestFailureException){
				
				throw (RequestFailureException)ex;
			}
			throw ex;
		} 
		finally {
			
			db.dbClose();
		}
		
	}

	/**
	 * This method hard deletes a row from token table
	 * @author Alam
	 * @param username
	 * @throws Exception 
	 */
	public static void remove( String username, DatabaseConnection db ) throws Exception {
		
		int noOfDeletedRows = 0;
		
		try {
			
			ForgetPassword forgetPassword = getByUsername( username );
			
			if( forgetPassword == null ){
				
				throw new Exception( " Error occured while removing row from token table " );
			}
			
			//If no database connection is sent, create one and atomize the transaction.
			if( db == null ){
				
				try{
					
					db = new DatabaseConnection();
					db.dbOpen();
					db.dbTransationStart();
					
					noOfDeletedRows = deleteHardEntityByID(ForgetPassword.class, forgetPassword.getId(), db);
					
					db.dbTransationEnd();
				}
				catch(Exception ex) {
					
					try {
						
						db.dbTransationRollBack();
						
					} 
					catch (Exception ex2) {
						
					}
					if(ex instanceof RequestFailureException){
						
						throw (RequestFailureException)ex;
					}
					throw ex;
				} 
				finally {
					
					db.dbClose();
				}
				
			}
			else{
				
				noOfDeletedRows = deleteHardEntityByID(ForgetPassword.class, forgetPassword.getId(), db);
			}
			
			if( noOfDeletedRows == 0 ){
				
				throw new Exception( " Error occured while removing row from token table " );
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}
		
	}
}
