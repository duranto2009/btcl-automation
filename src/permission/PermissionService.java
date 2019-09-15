package permission;

import common.Logger;
import connection.DatabaseConnection;
import login.LoginDTO;
import request.CommonRequestDTO;
import user.UserDTO;
import user.UserRepository;

import java.util.ArrayList;
 
public class PermissionService {
	PermissionDAO permissionDAO = new PermissionDAO();
	static Logger logger = Logger.getLogger(PermissionService.class);

	public ArrayList<Long> getUsersHavingPermission(CommonRequestDTO comDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<Long> userIDs = new ArrayList<Long>();
		try
		{
			databaseConnection.dbOpen();
			userIDs = permissionDAO.getUsersHavingPermission(comDTO, databaseConnection);
		}catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}

		return userIDs;
	}
	
	public ArrayList<UserDTO> getUsersDTOHavingPermission(CommonRequestDTO comDTO) throws Exception{
		return getUsersDTOHavingPermission(comDTO, null);
	}
	public ArrayList<UserDTO> getUsersDTOHavingPermission(CommonRequestDTO comDTO, LoginDTO loginDTO) throws Exception{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<UserDTO> userDTOs = new ArrayList<UserDTO>();
		try
		{
			databaseConnection.dbOpen();
			ArrayList<Long> userIDs = permissionDAO.getUsersHavingPermission(comDTO, databaseConnection);			
			for(Long userID : userIDs){
				if(loginDTO != null)
				{
					if(loginDTO.getUserID() == userID)continue;
				}
				userDTOs.add(UserRepository.getInstance().getUserDTOByUserID(userID));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return userDTOs;
	}

	public boolean hasPermission(CommonRequestDTO comDTO, LoginDTO loginDTO) throws Exception
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean result = false;
		try
		{
			databaseConnection.dbOpen();
			result = permissionDAO.hasPermission(comDTO, loginDTO, databaseConnection);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return result;
	}

	public boolean hasMenuPermission(long menuID, int permissionStrength, LoginDTO loginDTO) throws Exception
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean result = false;
		try
		{
			databaseConnection.dbOpen();
			result = permissionDAO.hasMenuPermission(menuID, permissionStrength, loginDTO, databaseConnection);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return result;
	}
	
	public boolean hasColumnPermission(long columnID, LoginDTO loginDTO) throws Exception
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean result = false;
		try
		{
			databaseConnection.dbOpen();
			result = permissionDAO.hasColumnPermission(columnID, loginDTO, databaseConnection);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return result;
	}
	
	public boolean hasPermission(int requestTypeID, LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean result = false;
		try
		{
			databaseConnection.dbOpen();
			result = permissionDAO.hasPermission(requestTypeID, loginDTO, databaseConnection);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return result;
	}
	
//	public static void main(String args[]) throws Exception
//	{
//		PermissionService permissionService = new PermissionService();
//		LoginDTO loginDTO = new LoginDTO();
//		loginDTO.setUserID(-4127);
//		loginDTO.setRoleID(1);
//		int requestTypeID = DomainRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION;
//		boolean result = permissionService.hasPermission(requestTypeID, loginDTO);
//		logger.debug("result " + result);
//	}
}
