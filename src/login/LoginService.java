package login;

import connection.DatabaseConnection;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LoginService {
	private Logger logger = Logger.getLogger(getClass());
	public static int getRoleIdForApplicationsConsideringClient(LoginDTO loginDTO) {
		return loginDTO.getUserID() == -1 ? -1 : (int)loginDTO.getRoleID();
	}
	public LoginDTO validateUser(LoginDTO p_dto) throws Exception {
		LoginDAO dao = new LoginDAO();
		return dao.validateUser(p_dto);
	}

	public void mailPassword(PasswordMailForm p_dto) throws Exception {
		LoginDAO dao = new LoginDAO();
		dao.mailPassword(p_dto);
	}

	public List<ServiceTypeAndCountPairDTO> getServiceTypeCount(LoginDTO loginDTO) {
		LoginDAO loginDAO = new LoginDAO();
		List<ServiceTypeAndCountPairDTO> serviceTypeAndCountPairDTOs = new ArrayList<ServiceTypeAndCountPairDTO>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {

			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			serviceTypeAndCountPairDTOs = loginDAO.getServiceTypeAndCountPairDTOList(loginDTO, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return serviceTypeAndCountPairDTOs;
	}

//	@Transactional(transactionType = TransactionType.READONLY)
//	public boolean isAdmin(long roleID) throws Exception {
//		// Temporary solution
//		Set <UserDTO> set = UserRepository.getInstance().getUsersByRoleID(roleID);
//		UserDTO user = set.stream().findFirst().get();
//		return user.isBTCLPersonnel();
//		//Permanent Solution
////		return ServiceDAOFactory.getDAO(LoginDAO.class).isAdmin(roleID);
//	}
}