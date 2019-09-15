package common;

import java.util.HashMap;

import sessionmanager.SessionConstants;

public class UtilityConstants {
	private UtilityConstants(){
		
	}
	public static final String GLOBAL_SELECT_ALL_OPTION_VAL="-1";
	public static final String GLOBAL_SELECT_ALL_VAL="ALL";
	public static final int  GLOBAL_DEFAULT_VAL=-1;
	
	public static final HashMap<String, String> propertyNameToPropertyLabelMap=new HashMap<String, String>();
	static{
		propertyNameToPropertyLabelMap.put(SessionConstants.SEARCH_REQUEST[4][1], "Requested By");
		propertyNameToPropertyLabelMap.put(SessionConstants.SEARCH_REQUEST[5][1], "Requested To");
	}
	
}
