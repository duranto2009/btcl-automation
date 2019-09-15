package lli.constants;

import common.ModuleConstants;



public class LliStateConstants {
	
	/*
	 * Related to Link Bandwidth Upgrade
	 */

	public static class REQUEST_UPGRADE
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_LLI;// * ModuleConstants.MULTIPLIER + 100;
		/*
		 * Actions:
		 * CLIENT_APPLY
		 * 
		 */
		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 201;
		/*
		 * Actions:
		 * CLIENT_REAPPLY
		 * CLIENT_CANCEL_APPLICATION
		 * SYSTEM_REJECT_APPLICATION
		 * SYSTEM_VERIFY_APPLICATION
		 * SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION
		 */
		public static final int CLIENT_RECEIVED_CORRECTION_REQUEST = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 202;
		/*
		 * Actions:
		 * CLIENT_REAPPLY
		 */
		public static final int REQUEST_VERIFIED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 203;
		/*
		 * Actions:
		 * SYSTEM_APPROVE_APPLICATION
		 * SYSTEM_REJECT_APPLICATION
		 * SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION
		 */
		public static final int REQUEST_APPROVED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 204;
		/*
		 * Actions:
		 * SYSTEM_GENERATE_DEMAND_NOTE
		 * SYSTEM_FR_REQUEST_FOR_PORT_CHECK
		 */
		public static final int DEMAND_NOTE_GENERATED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 211;
		/*
		 * Actions:
		 * 
		 */
		public static final int PORT_FR_REQUESTED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 212;
		/*
		 * Actions:
		 * SYSTEM_FR_RESPONSE_WITH_PORT_CHECK
		 * 
		 * 
		 */
		public static final int CLIENT_PAID = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 213;
		/*
		 * Actions:
		 * PAYMENT_VERIFIED
		 * PAYMENT_REJECTED
		 */
		public static final int APPLICATION_COMPLETED_SUCCESSFULLY = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 201);
		public static final int APPLICATION_CANCELLED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 202);
		public static final int APPLICATION_REJECTED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 203);		
	}
	
	/*
	 * Related to new Link Request
	 */
	public static class REQUEST_NEW_LINK
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_LLI;// * ModuleConstants.MULTIPLIER + 100;
		/*
		 * Actions:
		 * CLIENT_APPLY
		 * 
		 */
		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 101;
		/*
		 * Actions:
		 * CLIENT_REAPPLY
		 * CLIENT_CANCEL_APPLICATION
		 * SYSTEM_REJECT_APPLICATION
		 * SYSTEM_VERIFY_APPLICATION
		 * SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION
		 */
		public static final int CLIENT_RECEIVED_CORRECTION_REQUEST = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 102;
		/*
		 * Actions:
		 * CLIENT_REAPPLY
		 */
		public static final int REQUEST_VERIFIED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 103;
		/*
		 * Actions:
		 * SYSTEM_APPROVE_APPLICATION
		 * SYSTEM_REJECT_APPLICATION
		 * SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION
		 */
		public static final int REQUEST_APPROVED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 104;
		/*
		 * Actions:
		 * SYSTEM_GENERATE_DEMAND_NOTE
		 * SYSTEM_FR_REQUEST_FOR_PORT_CHECK
		 * SYSTEM_REJECT_APPLICATION
		 */
		public static final int PORT_FR_REQUESTED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 106;
		/*
		 * Actions:
		 * SYSTEM_FR_RESPONSE_WITH_PORT_CHECK
		 * SYSTEM_REJECT_APPLICATION
		 * 
		 */

		public static final int WAITING_FOR_LINK_APPROVAL = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 105;
		public static final int NEAR_END_APPLICATION_APPROVED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 153;
		/*
		 * Actions:
		 * SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_END
		 */
		public static final int FAR_END_APPLICATION_APPROVED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 154;
		/*
		 * Actions:
		 * SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_FAR_END
		 */
		
		public static final int FR_RESPONSED_WITH_LOOP_CHECK_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 157;
		/*
		 * Actions:
		 * Check if FR_RESPONSED_WITH_LOOP_CHECK_FAR_END is reached at bottom otherwise No Action
		 */
		public static final int FR_RESPONSED_WITH_LOOP_CHECK_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 158;
		/*
		 * Actions:
		 * Check if FR_RESPONSED_WITH_LOOP_CHECK_NEAR_END is reached at bottom otherwise No Action
		 */
		
		public static final int WAITING = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 199;
		
		public static final int APPLICATION_COMPLETED_SUCCESSFULLY = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 101);
		public static final int APPLICATION_CANCELLED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 102);
		public static final int APPLICATION_REJECTED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 103);
	}

	public static class REQUEST_NEW_CONNECTION
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_LLI;// * ModuleConstants.MULTIPLIER + 100;
		/*
		 * Actions:
		 * CLIENT_APPLY
		 * 
		 */
		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1;
		/*
		 * Actions:
		 * CONNECTION_CREATED
		 */
		public static final int CONNECTION_CREATED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1);
	}
	
	public static class REQUEST_CLOSE_CONNECTION
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_LLI;// * ModuleConstants.MULTIPLIER + 100;
		/*
		 * Actions:
		 * CLIENT_APPLY
		 * 
		 */
		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 701;
		/*
		 * Actions:
		 * CONNECTION_CREATED
		 */
		
	}
	
	
	public static class REQUEST_NEW_CLIENT
	{
		public static final int CLIENT_IDLE = ModuleConstants.Module_ID_LLI;// * ModuleConstants.MULTIPLIER + 100;

		public static final int CLIENT_NOT_APPROVED = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5090);

		public static final int CLIENT_APPLIED_FOR_VERIFICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5001;
		public static final int APPLICATION_VERIFIED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5003;
		public static final int APPLICATION_CORRECTION_REQUESTED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5004;
		public static final int REGISTRATION_NOT_COMPLETED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5009;
	}
	
}
