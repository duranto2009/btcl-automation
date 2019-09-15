package webHosting.constants;

import common.ModuleConstants;



public class WebHostingStateConstants {
	
	public static class REQUEST_NEW_CLIENT
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_WEBHOSTING;// * ModuleConstants.MULTIPLIER + 100;
		public static final int CLIENT_NOT_APPROVED = -(ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 5090);
		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 5001;
		public static final int APPLICATION_VERIFIED = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 5003;
		public static final int APPLICATION_CORRECTION_REQUESTED = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 5004;
		public static final int REGISTRATION_NOT_COMPLETED = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 5009;
	}
	
}
