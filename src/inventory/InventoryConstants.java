package inventory;

import java.util.HashMap;

public class InventoryConstants {

	public static final int CATEGORY_ID_DISTRICT = 1;
	public static final int CATEGORY_ID_UPAZILA = 2;
	public static final int CATEGORY_ID_UNION = 3;
	public static final int CATEGORY_ID_POP = 4;
	public static final int CATEGORY_ID_ROUTER = 5;
	public static final int CATEGORY_ID_VIRTUAL_LAN = 6;
	public static final int CATEGORY_ID_GLOBAL_VIRTUAL_LAN = 101;

	public static final int CATEGORY_ID_PORT = 99;
	
	public static final int DIVISION_ID_DHAKA = 1;
	public static final int DIVISION_ID_CHITTAGONG = 2;
	public static final int DIVISION_ID_SYLHET = 3;
	public static final int DIVISION_ID_KHULNA = 4;
	public static final int DIVISION_ID_BOGRA = 5;
	public static final int DIVISION_ID_ALL_OVER_BANGLADESH = 6;

	public static final int PORT_FE = 1;
	public static final int PORT_GE = 2;
	public static final int PORT_10GE = 3;
	
	public static final int IP_PUBLIC = 1;
	public static final int IP_PRIVATE = 2;
	
	public static final int USAGE_ESSENTIAL = 1;
	public static final int USAGE_ADDITIONAL = 2;
	
	public static final HashMap <Integer, String> mapOfIPTypeNameToIPType = new HashMap<>();
	static {
		mapOfIPTypeNameToIPType.put(IP_PUBLIC, "PUBLIC");
		mapOfIPTypeNameToIPType.put(IP_PRIVATE, "PRIVATE");
	}
	
	public static final HashMap<Integer, String> mapOfUsageTypeNameToUsageType = new HashMap<>();
	static {
		mapOfUsageTypeNameToUsageType.put(USAGE_ESSENTIAL, "MANDATORY");
		mapOfUsageTypeNameToUsageType.put(USAGE_ADDITIONAL, "ADDITIONAL");
		
	}
	
	public static final HashMap<Integer, String> mapOfDivisionNameToDivisionID = new HashMap<>();
	static {
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_DHAKA, "DHAKA");
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_CHITTAGONG, "CHITTAGONG");
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_KHULNA, "KHULNA");		
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_SYLHET, "SYLHET");
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_BOGRA, "BOGRA");
		mapOfDivisionNameToDivisionID.put(DIVISION_ID_ALL_OVER_BANGLADESH, "ALL OVER BANGLADESH");
	}

	public static final HashMap<Integer, String> mapOfPortTypeToPortTypeString = new HashMap<Integer, String>();
	static {
		mapOfPortTypeToPortTypeString.put(PORT_FE, "FE");
		mapOfPortTypeToPortTypeString.put(PORT_GE, "GE");
		mapOfPortTypeToPortTypeString.put(PORT_10GE, "10GE");

	}

	public static final HashMap<String,Integer> mapOfStringToPortType = new HashMap<String, Integer>();
	static {
		mapOfStringToPortType.put( "FE",PORT_FE);
		mapOfStringToPortType.put( "GE",PORT_GE);
		mapOfStringToPortType.put( "10GE",PORT_10GE);

	}

	public static HashMap<Long, Integer> districtToDivisionMap = new HashMap<Long, Integer>();
	static {
		districtToDivisionMap.put((long) 1,4);
		districtToDivisionMap.put((long) 2,4);
		districtToDivisionMap.put((long) 3,4);
		districtToDivisionMap.put((long) 4,4);
		districtToDivisionMap.put((long) 5,4);
		districtToDivisionMap.put((long) 6,4);
		districtToDivisionMap.put((long) 7,2);
		districtToDivisionMap.put((long) 8,3);
		districtToDivisionMap.put((long) 9,1);
		districtToDivisionMap.put((long) 10,2);
		districtToDivisionMap.put((long) 11,1);
		districtToDivisionMap.put((long) 12,2);
		districtToDivisionMap.put((long) 13,2);
		districtToDivisionMap.put((long) 14,2);
		districtToDivisionMap.put((long) 15,2);
		districtToDivisionMap.put((long) 16,2);
		districtToDivisionMap.put((long) 17,2);
		districtToDivisionMap.put((long) 18,1);
		districtToDivisionMap.put((long) 19,4);
		districtToDivisionMap.put((long) 20,1);
		districtToDivisionMap.put((long) 21,4);
		districtToDivisionMap.put((long) 22,1);
		districtToDivisionMap.put((long) 23,1);
		districtToDivisionMap.put((long) 24,4);
		districtToDivisionMap.put((long) 25,1);
		districtToDivisionMap.put((long) 26,1);
		districtToDivisionMap.put((long) 27,1);
		districtToDivisionMap.put((long) 28,1);
		districtToDivisionMap.put((long) 29,1);
		districtToDivisionMap.put((long) 30,1);
		districtToDivisionMap.put((long) 31,4);
		districtToDivisionMap.put((long) 32,4);
		districtToDivisionMap.put((long) 33,1);
		districtToDivisionMap.put((long) 34,1);
		districtToDivisionMap.put((long) 35,4);
		districtToDivisionMap.put((long) 36,4);
		districtToDivisionMap.put((long) 37,4);
		districtToDivisionMap.put((long) 38,4);
		districtToDivisionMap.put((long) 39,4);
		districtToDivisionMap.put((long) 40,4);
		districtToDivisionMap.put((long) 41,4);
		districtToDivisionMap.put((long) 42,4);
		districtToDivisionMap.put((long) 43,4);
		districtToDivisionMap.put((long) 44,4);
		districtToDivisionMap.put((long) 45,5);
		districtToDivisionMap.put((long) 46,5);
		districtToDivisionMap.put((long) 47,5);
		districtToDivisionMap.put((long) 48,5);
		districtToDivisionMap.put((long) 49,5);
		districtToDivisionMap.put((long) 50,5);
		districtToDivisionMap.put((long) 51,5);
		districtToDivisionMap.put((long) 52,5);
		districtToDivisionMap.put((long) 53,5);
		districtToDivisionMap.put((long) 54,5);
		districtToDivisionMap.put((long) 55,5);
		districtToDivisionMap.put((long) 56,5);
		districtToDivisionMap.put((long) 57,5);
		districtToDivisionMap.put((long) 58,5);
		districtToDivisionMap.put((long) 59,5);
		districtToDivisionMap.put((long) 60,5);
		districtToDivisionMap.put((long) 61,3);
		districtToDivisionMap.put((long) 62,3);
		districtToDivisionMap.put((long) 63,3);
		districtToDivisionMap.put((long) 64,3);
	}
	public static String getCategoryName (int categoryType ) {
		switch (categoryType) {
			case CATEGORY_ID_PORT : return "Port";
			case CATEGORY_ID_VIRTUAL_LAN : return "Virtual LAN";
			case CATEGORY_ID_POP : return  "Pop";
			case CATEGORY_ID_DISTRICT : return "District";
			case CATEGORY_ID_ROUTER : return "Router/Switch";
			case CATEGORY_ID_UNION : return "Union";
			case CATEGORY_ID_UPAZILA : return "Upazilla";

		}
		return "Invalid Type";
	}
	
}
