package login;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import connection.DatabaseConnection;
import crm.service.CrmComplainService;
import crm.service.CrmDesignationService;
import crm.service.CrmEmployeeService;
import file.FileTypeConstants;
import permission.ColumnDTO;
import permission.ColumnPermissionDTO;
import permission.MenuDTO;
import permission.PermissionDTO;
import permission.PermissionRepository;
import permission.StateActionDTO;
import request.RequestStateActionRepository;
import role.*;
import util.ServiceDAOFactory;
import util.SqlGenerator;

public class LoginDTO {

	// private LoginDTOData data = new LoginDTOData();

	private String username;
	private String password;
	private long userID;
//	private HashMap permissionMap;
//	private Set<Integer> columnPermissionSet;
//	private HashMap<Integer, StateActionDTO> actionPermissionMap;
	private String mailAddress;
	private String secToken;
	private long accountID;
	public long roleID;
	// private ArrayList<Integer> roleIDs;
	private String loginSourceIP;
	private int languageID;
	private int status;

	private  int zoneID;

	boolean isAdmin;
	String profilePicturePath;
	private String userAgent;
	boolean isBTCLPersonnel = false;
	public boolean getIsAdmin() {

		return isBTCLPersonnel;
	}

	public void setIsBTCLAdmin(boolean btclAdmin){
		this.isBTCLPersonnel = this.userID>0;//btclAdmin;
	}

	public LoginDTO() {
		/*permissionMap = new HashMap();
		columnPermissionSet = new HashSet<Integer>();
		actionPermissionMap = new HashMap();*/
		
		// for system users
		userID = -1;
		isAdmin = false;
		// for client
		accountID = -1;
		// roleIDs = null;
		roleID = -1;
		status = -1;
		languageID = 1;

	}


	
	/*public Set<Integer> getAllColumnPermissionSet()
	{
		return columnPermissionSet;
	}*/
	
	public void setUserStatus(int p_status) {
		status = p_status;
	}

	public int getUserStatus() {
		return status;
	}

	public void setUserID(long userID) {
//		isAdmin = true;
		this.userID = userID;
	}

	public long getUserID() {
		return userID;
	}

	/*public void addLoginIP(String p_loginIP) {
		loginIP.put(p_loginIP, p_loginIP);
	}

	public boolean checkLoginSourceIP() {
		if (loginIP == null)
			return true;
		// System.out.println("In the ip domain:"+loginIP.size());
		if (loginIP.get(loginSourceIP) != null)
			return true;
		return false;
	}*/

	public String getLoginSourceIP() {
		return loginSourceIP;
	}

	public void setLoginSourceIP(String p_sourceIP) {
		loginSourceIP = p_sourceIP;
	}

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int p_languageID) {
		languageID = p_languageID;
	}

	public void setRoleID(long p_roleID) {
		roleID = p_roleID;
	}

	public long getRoleID() {
		return roleID;
	}

	public long getAccountID() {
		return accountID;
	}

	public int getZoneID() {
		return zoneID;
	}

	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}

	/*
	 * public ArrayList<Integer> getRoleIDs() { return roleIDs; }
	 * 
	 * public void setRoleIDs(ArrayList<Integer> roleIDs) { this.roleIDs =
	 * roleIDs; }
	 */

	public void setAccountID(long p_accountID) {
//		isAdmin = false;
		accountID = p_accountID;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String p_mailAddress) {
		mailAddress = p_mailAddress;
	}

	public String getSecToken() {
		return secToken;
	}

	public void setSecToken(String p_secToken) {
		secToken = p_secToken;
	}

	/*public void setPermissionList(ArrayList list) {
		for (int i = 0; i < list.size(); i++) {
			PermissionDTO dto = (PermissionDTO) list.get(i);
			permissionMap.put(dto.getMenuID() + "", dto.getPermissionType() + "");
		}
	}

	public void setActionPermission(StateActionDTO saDTO) {
		StateActionDTO existingSaDTO = actionPermissionMap.get(saDTO.getStateID());
		if(existingSaDTO == null)
		{
			actionPermissionMap.put(saDTO.getStateID(), saDTO.getCopy());			
		}
		else
		{
			Set<Integer> set = existingSaDTO.getActionTypeIDs();
			set.addAll(saDTO.getActionTypeIDs());
			
		}
	}*/

	/*public StateActionDTO getActionPermission(int stateID) {
		return actionPermissionMap.get(stateID);
	}*/
	
	/*
	 * Not specific on any document. the following returned action list is global
	 */
	public Set<Integer> getActionPermission(int stateID) {
		return PermissionRepository.getInstance().getActionTypeSetByRoleIDAndStateID(roleID, stateID);
	}

	/*public void setColumnPermission(int columnID) {
		columnPermissionSet.add(columnID);
	}*/

/*	public boolean getColumnPermission(int columnID) {
		return columnPermissionSet.contains(columnID);
	}*/

	/*public boolean getColumnPermission(int columnID) {
		return columnPermissionSet.contains(columnID);
	}*/
	
	public boolean getColumnPermission(int columnID) {
		Map<Integer, ColumnPermissionDTO> columnMap = PermissionRepository.getInstance().getColumnPermissionMapByRoleID(roleID);
		if(columnMap == null)
			return false;
		ColumnPermissionDTO columnPermissionDTO = columnMap.get(columnID);
		if(columnPermissionDTO == null)
			return false;
		return true;
	}

	/*public int getMenuPermission(String menuId) {
		int permission = -1;

		String pers = (String) permissionMap.get(menuId);
		if (pers != null) {
			permission = Integer.parseInt(pers);
		}
		return permission;
		
	}*/

	/*public int getMenuPermission(int menuId) {
		return getMenuPermission(menuId + "");
	}*/
	
	public int getMenuPermission(int menuId) {
		Map<Integer, PermissionDTO> menuMap = PermissionRepository.getInstance().getMenuPermissionMapByRoleID(roleID);
		if(menuMap == null)
			return -1;
		PermissionDTO permissionDTO = menuMap.get(menuId);
		if(permissionDTO == null)
			return -1;
		return permissionDTO.getPermissionType();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String p_username) {
		username = p_username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String p_password) {
		password = p_password;
	}

	public String getProfilePicturePath() {
		if(StringUtils.isEmpty(profilePicturePath)){
			profilePicturePath=FileTypeConstants.DEFAULT_PROFILE_PIC;
		}
		return profilePicturePath;
	}

	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isNOC(){
		return CrmDesignationService.isNOC(this.userID);
	}

	@Override
	public String toString() {
		return "LoginDTO [username=" + username + ", password=" + password + ", userID=" + userID + ", mailAddress="
				+ mailAddress + ", secToken=" + secToken + ", accountID=" + accountID + ", roleID=" + roleID
				+ ", loginSourceIP=" + loginSourceIP + ", languageID=" + languageID + ", status=" + status
				+ ", isAdmin=" + isAdmin + ", profilePicturePath=" + profilePicturePath + ", userAgent=" + userAgent
				+ "]";
	}



}
