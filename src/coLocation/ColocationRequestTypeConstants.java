package coLocation;

import java.util.HashMap;

import common.ModuleConstants;


public class ColocationRequestTypeConstants
{
	public static class REQUEST_NEW_CLIENT
	{
		//Terminating Action
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5002);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5003);
		public static final int SYSTEM_APPROVE_APPLICATION = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5005);
		public static final int SYSTEM_DISABLE_CLIENT = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5301);
		public static final int SYSTEM_ENABLE_CLIENT = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5401);
		
		//Non Terminating Action
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5001;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5002;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5003;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5004;
		public static final int CLIENT_APPLY_REGISTRANTS_ONLY = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 5009;
	}
	
	public static class REQUEST_NEW_COLOCATION
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 101;
		public static final int UPDATE_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 102;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 103;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 104;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 105;
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 102);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 103);
		
		//
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 110;//Copied
		
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 123;//Copied
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 142;//Copied
		public static final int SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 195;//Copied
		public static final int SYSTEM_GENERATE_MIGRATION_MRC = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 196;//Copied
		public static final int SYSTEM_CANCEL_REQUEST = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 199);//Copied
	}
	
	public static class REQUEST_UPGRADE
	{
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 242;//Copied
	}
	
	public static class REQUEST_DISABLE_COLOCATION
	{
		public static final int SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 2321;//Copied
		public static final int SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 2421;//Copied
	}
	
	public static class REQUEST_COLOCATION_CLOSE
	{
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 621;//Copied
	}
	
	
	
	public static class REQUEST_COLOCATION_FRESH{
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 111;
		public static final int PAY = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 115;
		
		public static final int ADVICE_NOTE_TO_COLOCATION_SERVER_ROOM = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 121;
		public static final int SETUP_DONE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 122;

		public static final int FORWARD_TO_DIRECTOR_INTERNATIONAL = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 124;
		public static final int FORWARD_TO_CORRESPONDING_DE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 125;
		public static final int DE_REPORT_BACK_TO_DIRECTOR_INTERNATIONAL = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 126;
		public static final int DIRECTOR_INTERNATIONAL_PASS_REPORT_TO_DE = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 127;
		
		
		
		public static final int SERVICE_START = -(ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 120);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static HashMap<Integer, String> requestTypeMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String[][]> requestTypToSearchCriteria = new HashMap<Integer, String[][]>();

	static
	{
		requestTypeMap.put(REQUEST_NEW_CLIENT.CLIENT_APPLY, "New Client");
	}
	static
	{
		String[][] SYSTEM_VERIFY_APPLICATION_CRITERIA = {
				{ "../../includes/elements/description.jsp", "description" }};
		requestTypToSearchCriteria.put(REQUEST_NEW_CLIENT.SYSTEM_VERIFY_APPLICATION, SYSTEM_VERIFY_APPLICATION_CRITERIA);
		
		
		String[][] GENERATE_DEMAND_NOTE = {
				{ "../../includes/elements/generateDemandNoteColocation.jsp", "generateDemandNoteColocation" } };
		requestTypToSearchCriteria.put(REQUEST_COLOCATION_FRESH.SYSTEM_GENERATE_DEMAND_NOTE, GENERATE_DEMAND_NOTE);

		
		
		

	}
}
