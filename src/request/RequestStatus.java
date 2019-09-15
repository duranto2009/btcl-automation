package request;

import java.util.HashMap;

public class RequestStatus {
	public static final int PENDING = 0;
	public static final int SEMI_PROCESSED = 1;
	public static final int ALL_PROCESSED = 2;
	
	public static HashMap<Integer, String> reqStatusMap = new HashMap<Integer, String>();
	static
	{
		reqStatusMap.put(PENDING, "Pending");
		reqStatusMap.put(SEMI_PROCESSED, "Processed");
		reqStatusMap.put(ALL_PROCESSED, "Completed");
	}
}
