package user;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import annotation.Transactional;
import common.CategoryConstants;
import common.ClientDTO;
import common.RequestFailureException;
import common.StringUtils;
import common.repository.AllClientRepository;
import crm.CrmEmployeeDTO;
import crm.dao.CrmEmployeeDAO;
import login.LoginDTO;
import officialLetter.RecipientElement;
import officialLetter.RecipientType;
import officialLetter.ReferType;
import role.RoleService;
import user.form.UserForm;
import util.*;

public class UserService implements NavigationService {

    // TODO:must change hard coded CDGM user id
    private static final long CDGM_USER_ID = 42133;

    RoleService roleService = new RoleService();
    UserDAO userDAO = new UserDAO();
    CrmEmployeeDAO crmEmployeeDAO = new CrmEmployeeDAO();

    @Transactional(transactionType = util.TransactionType.READONLY)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return userDAO.getUsers((List<Long>) recordIDs);
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    @SuppressWarnings("rawtypes")
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO);
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    @SuppressWarnings("rawtypes")
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return userDAO.getUserIDsWithSearchCriteria(searchCriteria);
    }

    @Transactional
    public void addUser(UserDTO userDTO, LoginDTO loginDTO) throws Exception {
        validateUserDTO(userDTO);
        userDTO.setPassword(PasswordService.getInstance().encrypt(userDTO.getPassword()));
        userDTO.setAddedBy(loginDTO.getUserID());
        userDTO.setLastModifiedBy(loginDTO.getUserID());
        userDTO.setZoneID(loginDTO.getZoneID());
        userDAO.addUser(userDTO);
    }

    private void validateUserDTO(UserDTO userDTO) {
        //only user name validation : duplicate check has been done.
        UserDTO userDTOFromUserRepository = UserRepository.getInstance().getUserDTOByUserName(userDTO.getUsername());

        if (userDTO.getUserID() == 0) {
            //insert case
            if (userDTOFromUserRepository != null) {
                throw new RequestFailureException("Duplicate user found while adding user");
            }
        } else {
            //update case
            if (userDTOFromUserRepository != null && userDTO.getUserID() != userDTOFromUserRepository.getUserID()) {
                throw new RequestFailureException("Duplicate user found while updating user");
            }
        }

        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientLoginName(userDTO.getUsername());
        if (clientDTO != null) {
            throw new RequestFailureException("One or more client found with same user name");
        }

    }

    @Transactional
    public void dropUser(List<Long> deleteIDs) throws Exception {
        for (long userID : deleteIDs) {
            List<CrmEmployeeDTO> crmEmployeeDTOs = crmEmployeeDAO.getEmployeeDTOListByUserID(userID);
            if (crmEmployeeDTOs.isEmpty() == false) {
                throw new RequestFailureException("One or more CRM Employee is assigned to " + UserRepository.getInstance().getUserDTOByUserID(userID).getUsername()
                        + "\nFirst Delete those employees");
            }
        }
        userDAO.dropUsers(deleteIDs);
    }

    public UserDTO getUserDTOByUserID(long id) throws Exception {
        UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(id);
        if (userDTO == null) {
            throw new RequestFailureException("No such user found with user id: " + id);
        }
        return userDTO;
    }

    @Transactional
    public void updateUser(UserDTO userDTO, LoginDTO loginDTO) throws Exception {
        validateUserDTO(userDTO);
        updatePassword(userDTO);
        userDTO.setLastModifiedBy(loginDTO.getUserID());
        userDAO.updateUser(userDTO);

    }

    private void updatePassword(UserDTO userDTO) throws Exception {
        UserDTO userDTOFromRepository = UserRepository.getInstance().getUserDTOByUserID(userDTO.getUserID());
        if (userDTOFromRepository != null && !userDTOFromRepository.getPassword().equals(userDTO.getPassword())) {
            userDTO.setPassword(PasswordService.getInstance().encrypt(userDTO.getPassword()));
        }
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    public List<UserDTO> getAllUsersFromRepository(Class<UserDTO> classObject, boolean isFirstReload) throws Exception {
        return ModifiedSqlGenerator.getAllObjectForRepository(classObject, isFirstReload);
    }

    public UserForm getUserFormByUserDTO(UserDTO userDTO) throws Exception {
        UserForm userForm = new UserForm();
        userForm.setUserID(userDTO.getUserID());
        userForm.setUsername(userDTO.getUsername());
        userForm.setPassword(userDTO.getPassword());
        userForm.setRepassword(userDTO.getPassword());
        userForm.setRoleID(userDTO.getRoleID());
        userForm.setRoleName(roleService.getRole(userDTO.getRoleID()).getRoleName());
        userForm.setMailAddress(userDTO.getMailAddress());
        userForm.setSecToken(userDTO.getSecToken());
        userForm.setStatus(userDTO.getStatus());
        userForm.setLoginIPs(StringUtils.trim(userDTO.getLoginIPs()).split(",", -1));
        userForm.setFullName(userDTO.getFullName());
        userForm.setDesignation(userDTO.getDesignation());
        userForm.setPhoneNo(userDTO.getPhoneNo());
        userForm.setAdditionalInfo(userDTO.getAdditionalInfo());

        userForm.setAddedBy(userDTO.getAddedBy());
        userForm.setAddTime(userDTO.getAddTime());
        userForm.setLastModifiedBy(userDTO.getLastModifiedBy());
        userForm.setLastModifyTime(userDTO.getLastModifyTime());
        userForm.setAddedByName("");
        userForm.setLastModifiedByName("");
        userForm.setAddDateTime((new Date(userDTO.getAddTime())).toString());
        userForm.setLastModifyDateTime((new Date(userDTO.getLastModifyTime())).toString());

        userForm.setBalance(userDTO.getBalance());
        userForm.setDepartmentName(userDTO.getDepartmentName());
        userForm.setDistrictID(userDTO.getDistrictID());
        userForm.setUpazilaID(userDTO.getUpazilaID());
        userForm.setUnionID(userDTO.getUnionID());
        userDTO.setZoneID(userDTO.getZoneID());

        userForm.setIsBTCLPersonnel(userDTO.isBTCLPersonnel());

        return userForm;
    }

    public UserDTO getUserDTOByUserForm(UserForm userForm, LoginDTO loginDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(userForm.getUserID());
        userDTO.setUsername(userForm.getUsername());
        userDTO.setPassword(userForm.getPassword());
        userDTO.setRoleID(userForm.getRoleID());
        userDTO.setMailAddress(userForm.getMailAddress());
        userDTO.setSecToken(userForm.getSecToken());
        userDTO.setStatus(userForm.getStatus());
        userDTO.setLoginIPs(userForm.getLoginIPs());
        userDTO.setFullName(userForm.getFullName());
        userDTO.setDesignation(userForm.getDesignation());
        userDTO.setPhoneNo(userForm.getPhoneNo());
        userDTO.setAdditionalInfo(userForm.getAdditionalInfo());
        userDTO.setDepartmentName(userForm.getDepartmentName());
        userDTO.setLastModifiedBy(loginDTO.getUserID());
        userDTO.setLastModifyTime(System.currentTimeMillis());

        userForm.setDistrictID(userDTO.getDistrictID());
        userForm.setUpazilaID(userDTO.getUpazilaID());
        userForm.setUnionID(userDTO.getUnionID());
        userDTO.setZoneID(userDTO.getZoneID());

        userForm.setIsBTCLPersonnel(userDTO.isBTCLPersonnel());
        return userDTO;
    }

    /**
     * @author Dhrubo
     */
    public Map<Integer, Long> getInventoryListByUserID(long userID) throws Exception {
        Map<Integer, Long> inventoryListByUserID = new HashMap<Integer, Long>();

        UserDTO userDTO = getUserDTOByUserID(userID);

        Long districtID = userDTO.getDistrictID();
        Long upazilaID = userDTO.getUpazilaID();
        Long unionID = userDTO.getUnionID();
        if (unionID != null) {
            inventoryListByUserID.put(CategoryConstants.CATEGORY_ID_UNION, unionID);
        } else if (upazilaID != null) {
            inventoryListByUserID.put(CategoryConstants.CATEGORY_ID_UPAZILA, upazilaID);
        } else if (districtID != null) {
            inventoryListByUserID.put(CategoryConstants.CATEGORY_ID_DISTRICT, districtID);
        } else {
            inventoryListByUserID.put(0, null);
        }

        return inventoryListByUserID;
    }

    /**
     * @author ferdous
     */
    public List<UserDTO> getLocalLoopProviderList() {
        return UserRepository.getInstance().getUserDTOsByIsBTCLPersonnel(false);
    }

//	@Transactional(transactionType=TransactionType.READONLY)
//	public List<UserDTO> getUserByRoleID(int roleID) throws Exception {
//		List<UserDTO> users= userDAO.get(division);
//		if(users == null) {
//			throw new RequestFailureException("No Division found ");
//		}
//
//		return users;
//	}

    @Transactional(transactionType = TransactionType.READONLY)
    public List<RecipientElement> getUserDTOsByPartialName(String name) {

        return UserRepository.getInstance()
                .getUserList()
                .stream()
                .filter(t ->
                        t.getUsername()
                                .contains(name)
                )
                .map(this::getCCElement)
                .collect(Collectors.toList());
    }

    private RecipientElement getCCElement(UserDTO user) {
        return new RecipientElement(
                user.getUserID(),
                user.getUsername(),
                user.isBTCLPersonnel() ?
                        RecipientType.BTCL_OFFICIAL :
                        RecipientType.VENDOR,
                ReferType.CC
        );
    }

    public static void main(String[] args) {
        ServiceDAOFactory.getService(UserService.class).getUserDTOsByPartialName("tan").stream().forEach(System.out::println);
    }

    public UserDTO getCDGMUserDTO() throws Exception {
        return getUserDTOByUserID(CDGM_USER_ID);
    }

    @Transactional (transactionType = TransactionType.READONLY)
    public Long getUserIdByUsername(String userName) throws Exception {
        Long id = ModifiedSqlGenerator.getAllObjectList(UserDTO.class,
                " Where " +
                        ModifiedSqlGenerator.getColumnName(UserDTO.class, "username") +
                        " = " +
                        userName).get(0).getUserID();
        if( id ==null )
            return 0L;
        else
            return id;
    }
}