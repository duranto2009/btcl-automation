package crm;

import common.ModuleConstants;

public class CRMRequestTypeConstants {

	public static class REQUEST_NEW_CLIENT_COMPLAIN
	{
		public static final int CLIENT_COMPAIN = ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 101;
		public static final int CLIENT_CANCEL_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 102);
		public static final int SYSTEM_REJECT_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 103);
		public static final int SYSTEM_COMPLETE_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 120);
	}
	public static class REQUEST_NEW_CRM_COMPLAIN
	{
		public static final int SYSTEM_COMPAIN = ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 201;
		public static final int CLIENT_CANCEL_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 202);
		public static final int SYSTEM_REJECT_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 203);
		public static final int SYSTEM_COMPLETE_COMPLAIN = -(ModuleConstants.Module_ID_CRM * ModuleConstants.MULTIPLIER + 220);
	}
}
