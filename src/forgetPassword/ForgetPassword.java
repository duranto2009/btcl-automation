package forgetPassword;

import java.io.Serializable;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.EntityDTO;
import connection.DatabaseConnection;

/**
 * @author Alam
 */
@TableName("vbforgetpasswordauth")
public class ForgetPassword implements Serializable {

	private static final long serialVersionUID = -7996395171609749651L;

	@PrimaryKey
	@ColumnName("id")
	private long id;
	
	@ColumnName("token")
	private String token;
	
	@ColumnName("username")
	private String username;
	
	@ColumnName("timestamp")
	private long lastModificationTime;
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public long getLastModificationTime() {
		
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		
		this.lastModificationTime = lastModificationTime;
		
	}

	@Override
	public String toString() {
		return "ForgetPassword [id=" + id + ", token=" + token + ", username=" + username + ", lastModificationTime="
				+ lastModificationTime + "]";
	}

	public static ForgetPassword getByUsername( String username ) throws Exception{
		
		return ForgetPasswordDAO.getByUsername( username );
	}
	
	/**
	 * This method inserts object of the class to database
	 * @author Alam
	 * @throws Exception
	 */
	public void insert() throws Exception{
		
		ForgetPassword forgetPassword = getByUsername( this.getUsername() );
		
		if( forgetPassword == null ){
			ForgetPasswordDAO.add( this );
		}
		else{
			
			this.setId( forgetPassword.getId() );
			update();
		}
	}
	
	/**
	 * This method updates object of the class to database
	 * @author Alam
	 * @throws Exception
	 */
	public void update() throws Exception{
		
		ForgetPasswordDAO.update( this );
	}
	
	/**
	 * This method removes an object of the class from database using the given databse connection
	 * @author Alam
	 * @throws Exception
	 */
	public void remove( DatabaseConnection db ) throws Exception{
		
		ForgetPasswordDAO.remove( this.username, db );
	}
	
	/**
	 * This method removes a row from table which has given username using given DatabaseConnection
	 * @author Alam
	 * @throws Exception
	 */
	public static void remove( String username, DatabaseConnection db ) throws Exception{
		
		ForgetPasswordDAO.remove( username, db );
	}
	
	/**
	 * This method removes an object from databse of forgetpassword table
	 * @author Alam
	 * @throws Exception
	 */
	public void remove() throws Exception{
		
		ForgetPasswordDAO.remove( this.username, null );
	}
}
