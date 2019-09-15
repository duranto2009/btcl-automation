package nix.constants;

import java.util.HashMap;

import common.ModuleConstants;


public class NixRequestTypeConstants
{
	public static class REQUEST_NEW_CLIENT
	{
		//Terminating Action
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5002);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5003);
		public static final int SYSTEM_APPROVE_APPLICATION = -(ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5005);
		public static final int SYSTEM_DISABLE_CLIENT = -(ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5301);
		public static final int SYSTEM_ENABLE_CLIENT = -(ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5401);

		//Non Terminating Action
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5001;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5002;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5003;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5004;
		public static final int CLIENT_APPLY_REGISTRANTS_ONLY = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 5009;
	}
	
	public static HashMap<Integer, String> requestTypeMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String[][]> requestTypToSearchCriteria = new HashMap<Integer, String[][]>();

	static
	{
		requestTypeMap.put(REQUEST_NEW_CLIENT.CLIENT_APPLY, "New Client");
	}
	static
	{
		//sample Action form mapping, can be deleted
		String[][] SYSTEM_VERIFY_APPLICATION_CRITERIA = {
				{ "../../includes/elements/description.jsp", "description" }};

		requestTypToSearchCriteria.put(REQUEST_NEW_CLIENT.SYSTEM_VERIFY_APPLICATION, SYSTEM_VERIFY_APPLICATION_CRITERIA);

	}
}
