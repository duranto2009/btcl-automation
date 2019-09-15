package common;

import java.util.ArrayList;
import java.util.List;

public class ClientConstants {
	public static final int DETAILS_TYPE_REGISTRANT = 0;
	public static final int DETAILS_TYPE_BILLING = 1;
	public static final int DETAILS_TYPE_ADMIN = 2;
	public static final int DETAILS_TYPE_TECHNICAL = 3;
	
	public static Integer getIndentityTypeByModuleIDAndDocumentSuffix(Integer moduleID, Integer documentSuffix) {
		return (documentSuffix);
	}
	
	public static class ClientAddMode {
		public static final int Client_Add_NoAccount = 1;
		public static final int Client_Add_Account_New = 2;
		public static final int Client_Add_Account_Existing = 3;
	}
	
	public static List<Integer> moduleIDListSupportingCloning = new ArrayList<Integer>();

	public static long MAX_LIFETIME_FOR_TEMP_CLIENT = 3600000L; // 1 hour
}
