package role;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import permission.ColumnPermissionDTO;
import util.SqlGenerator;


public class ColumnRepository {
	Logger logger = Logger.getLogger(getClass());
	private static ColumnRepository instance = null;
	private static HashMap<Integer, ColumnPermissionDTO> ColumnPermissionDTOByColumnID;
	private ColumnRepository()
	{
		ColumnPermissionDTOByColumnID = new HashMap<Integer, ColumnPermissionDTO>();
		reload();
	}
	public static ColumnRepository getInstance()
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
			instance = new ColumnRepository();
		}
		
	}
	public synchronized Collection<ColumnPermissionDTO> getAllColumnPermissionDTO()
	{		
		return ColumnPermissionDTOByColumnID.values();
	}	
	public synchronized ColumnPermissionDTO getColumnPermissionDTOByColumnID(int stateID)
	{
		logger.debug("ColumnPermissionDTOByColumnID " + ColumnPermissionDTOByColumnID);
		return ColumnPermissionDTOByColumnID.get(stateID);
	}
	public void reload()
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			String sql = "select * from adcolumn";
			Statement stmt = databaseConnection.getNewStatement();
			ResultSet rs =  stmt.executeQuery(sql);
			while(rs.next())
			{				
				ColumnPermissionDTO columnPermissionDTO = new ColumnPermissionDTO();
				columnPermissionDTO.setColumnID(rs.getInt("columnID"));
				columnPermissionDTO = (ColumnPermissionDTO)SqlGenerator.getObjectByID(ColumnPermissionDTO.class, columnPermissionDTO.getColumnID() , databaseConnection);
				ColumnPermissionDTOByColumnID.put(columnPermissionDTO.getColumnID(), columnPermissionDTO);
				
			}
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			databaseConnection.dbClose();
		}
		
	}
}
