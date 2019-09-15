package user;

import static util.ModifiedSqlGenerator.getColumnName;
import static util.ModifiedSqlGenerator.getObjectListByIDList;
import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.Transactional;
import util.ModifiedSqlGenerator;
public class UserDAO{
	Class<UserDTO> classObject = UserDTO.class;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getUsers(Collection recordIDs) throws Exception {
		return getObjectListByIDList(classObject, recordIDs,getColumnName(classObject, "isDeleted")+" = 0");
	}
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getUserIDsWithSearchCriteria(Hashtable searchCriteria) throws Exception {
    	String[] keys = new String[] 			{"usUserName", "usStatus", "usFullName", "usPhoneNo", "usDesignation", "usMailAddr"	, "usRoleID", "usIsDeleted"};
    	String[] operators = new String[] 		{"LIKE"		 , "="		 , "LIKE"	   , "LIKE"		, "LIKE"		 , "LIKE"	    , "="		, "="};
    	String[] dtoColumnNames = new String[]	{"username"  , "status"  , "fullName"  , "phoneNo"	, "designation"  , "mailAddress", "roleID"  , "isDeleted"};
    	searchCriteria.put("usIsDeleted", "0");
    	return ModifiedSqlGenerator.getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, searchCriteria, "");
    	
    }
    @Transactional
    public void addUser(UserDTO userDTO) throws Exception {
    	insert(userDTO);
    }
    @Transactional
    public void updateUser(UserDTO userDTO) throws Exception{
    	updateEntity(userDTO);
    }
    @Transactional
    public void dropUsers(List<Long>deleteIDs) throws Exception{
    	ModifiedSqlGenerator.deleteEntityByIDList(classObject, deleteIDs);
    }

//	public List<UserDTO> getUsers(int roleID) throws Exception{
//
//		if(division>0){
//			return ModifiedSqlGenerator.getAllObjectList(UserDTO.class,
//					new UserDTO()
//							.Where()
//							.divisionEquals(division)
//
//							.getCondition()
//			);
//
//		}else{
//			return ModifiedSqlGenerator.getAllObjectList(District.class);
//		}
//
//	}
}