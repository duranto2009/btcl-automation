package login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LoginDTOData {
	public String m_username;
	public String m_password;
	public long m_userID;
	public HashMap m_permissionMap;
	public HashMap m_columnPermissionMap;
	public HashMap m_actionPermissionMap;
	public String m_mailAddress;
	public String m_secToken;
	public long m_uniqueID;	
//	public int m_roleID;
	public ArrayList<Integer> roleIDs;
	public String m_loginSourceIP;
	public int m_languageID;
	public int m_status;
	public HashMap m_loginIP;

	public LoginDTOData() {
	}
}