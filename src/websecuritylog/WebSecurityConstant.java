package websecuritylog;

import java.util.HashMap;

public class WebSecurityConstant {
	
	public static final int ATTEMPT_TYPE_INVALID_LOGIN=1;
	public static final int ATTEMPT_TYPE_CSRF=2;
	public static final int ATTEMPT_TYPE_REFERER=3;
	public static final int ATTEMPT_TYPE_USER_INVALID_IP = 4;
	
	public static final HashMap<Integer, String> attemptTypeMap= new HashMap<Integer, String>();
	static{
		attemptTypeMap.put(ATTEMPT_TYPE_INVALID_LOGIN, "Invalid Login");
		attemptTypeMap.put(ATTEMPT_TYPE_CSRF, "CSRF");
		attemptTypeMap.put(ATTEMPT_TYPE_REFERER, "Referer");
		attemptTypeMap.put(ATTEMPT_TYPE_USER_INVALID_IP, "Invalid IP Address");
	}
	
	public static final int ATTEMPT_LIST_LIMIT=20;
	
}
