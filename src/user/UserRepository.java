package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import common.RequestFailureException;
import exception.NoDataFoundException;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;

import repository.Repository;
import repository.RepositoryManager;
import util.ServiceDAOFactory;
import util.SqlGenerator;


public class UserRepository implements Repository {
	UserService userService = ServiceDAOFactory.getService(UserService.class);
	
	
	static Logger logger = Logger.getLogger(UserRepository.class);
	Map<String, UserDTO>mapOfUserDTOToUserName;
	Map<Long, UserDTO>mapOfUserDTOToUserID;
	Map<Long, Set<UserDTO> >mapOfUserDTOsToRoleID;

	static UserRepository instance = null;  
	private UserRepository(){
		mapOfUserDTOToUserName = new ConcurrentHashMap<>();
		mapOfUserDTOToUserID = new ConcurrentHashMap<>();
		mapOfUserDTOsToRoleID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}

	public synchronized static UserRepository getInstance(){
		if (instance == null){
			instance = new UserRepository();
		}
		return instance;
	}

	public void reload(boolean reloadAll){
		try {
			List<UserDTO> userDTOs = userService.getAllUsersFromRepository(UserDTO.class, reloadAll);
			for(UserDTO userDTO : userDTOs) {
				UserDTO oldUserDTO = mapOfUserDTOToUserID.get(userDTO.getUserID());

				if( oldUserDTO != null ) {
					if(oldUserDTO.getLastModifyTime() == userDTO.getLastModifyTime() ) {
						continue;
					}
					mapOfUserDTOToUserID.remove(oldUserDTO.getUserID());
					mapOfUserDTOToUserName.remove(oldUserDTO.getUsername());
					if(mapOfUserDTOsToRoleID.containsKey(oldUserDTO.getRoleID())) {
						mapOfUserDTOsToRoleID.get(oldUserDTO.getRoleID()).remove(oldUserDTO);
					}
					if(mapOfUserDTOsToRoleID.get(oldUserDTO.getRoleID()).isEmpty()) {
						mapOfUserDTOsToRoleID.remove(oldUserDTO.getRoleID());
					}
				}
				if(userDTO.isDeleted() == false) {
					mapOfUserDTOToUserID.put(userDTO.getUserID(), userDTO);
					mapOfUserDTOToUserName.put(userDTO.getUsername(), userDTO);
					if( ! mapOfUserDTOsToRoleID.containsKey(userDTO.getRoleID())) {
						mapOfUserDTOsToRoleID.put(userDTO.getRoleID(), new ConcurrentHashSet<>());
					}
					mapOfUserDTOsToRoleID.get(userDTO.getRoleID()).add(userDTO);
				}
			}
			
		} catch (Exception e) {
			logger.debug("FATAL", e);
		}
	}
	
	public List<UserDTO> getUserList() {
		List <UserDTO> users = new ArrayList<UserDTO>(this.mapOfUserDTOToUserID.values());
		return users;
	}
	public UserDTO getUserDTOByUserID( long userID){
		UserDTO userDTO = mapOfUserDTOToUserID.get(userID);
		if(userDTO == null ){
			throw new NoDataFoundException("No User Found for User ID " + userID);
		}
		return userDTO;
	}
	public UserDTO getUserDTOByUserName( String userName){
		return mapOfUserDTOToUserName.get(userName);
	}
	public List<UserDTO> getUserDTOsByIsBTCLPersonnel(boolean isBTCLPersonnel) {
		return this.mapOfUserDTOToUserID.values().stream()
				.filter(x->x.isBTCLPersonnel() == isBTCLPersonnel)
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public Set<UserDTO> getUsersByRoleID(long roleID) {
		return (Set<UserDTO>) mapOfUserDTOsToRoleID.get(roleID);
	}
	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(UserDTO.class);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;
	}
	public static void main(String args[])throws Exception{
		UserRepository.getInstance();
	}

}

