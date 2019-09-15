package complain;

import java.util.HashMap;

public class ComplainConstants {

	public static String COMPLAIN_STATUS_ACTIVE_NAME = "Active";
	public static int COMPLAIN_STATUS_ACTIVE_VALUE = 1;
	public static String COMPLAIN_STATUS_DELETED_NAME = "Deleted";
	public static int COMPLAIN_STATUS_DELETED_VALUE = 0;
	
	
	static final int SUBJECT_ID_FOR_SMS_COMPLAIN = 0;
	static final String SUBJECT_FOR_COMPLAIN_SMS = "SMS Complain";
	static final String SUMMARY_FOR_COMPLAIN_SMS = "Complain By sms";
	static final int COMPLAIN_NOT_TAKEN = 0;

	public static HashMap<Integer, String> PRIORITY_MAP = new HashMap<Integer, String>();
	public static HashMap<String, Integer> PRIORITY_MAP_GET_VAL = new HashMap<String, Integer>();

	public static HashMap<Integer, String> STATUS_MAP = new HashMap<Integer, String>();
	public static HashMap<String, Integer>STATUS_MAP_GET_VAL = new HashMap<String, Integer>();

	static {
		PRIORITY_MAP.put(1, "Low");
		PRIORITY_MAP.put(2, "Medium");
		PRIORITY_MAP.put(3, "High");
		PRIORITY_MAP.put(4, "Urgent");
		
		PRIORITY_MAP_GET_VAL.put("Low", 1);
		PRIORITY_MAP_GET_VAL.put("Medium", 2);
		PRIORITY_MAP_GET_VAL.put("High", 3);
		PRIORITY_MAP_GET_VAL.put("Urgent", 4);

		STATUS_MAP.put(0, "Submitted");
		STATUS_MAP.put(1, "Received");
		STATUS_MAP.put(2, "More Information");
		STATUS_MAP.put(3, "Pending");
		STATUS_MAP.put(4, "Resolved");
		STATUS_MAP.put(5, "Cancel");
		
		STATUS_MAP_GET_VAL.put( "Submitted", 0 );
		STATUS_MAP_GET_VAL.put( "Received", 1 );
		STATUS_MAP_GET_VAL.put( "More Information", 2 );
		STATUS_MAP_GET_VAL.put( "Pending", 3 );
		STATUS_MAP_GET_VAL.put( "Resolved", 4 );
		STATUS_MAP_GET_VAL.put( "Cancel", 5 );

	}

}
