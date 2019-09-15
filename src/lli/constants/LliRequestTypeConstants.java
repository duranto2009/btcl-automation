package lli.constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import common.ModuleConstants;


public class LliRequestTypeConstants
{
	public static class REQUEST_NEW_CLIENT
	{
		//Terminating Action
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5002);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5003);
		public static final int SYSTEM_APPROVE_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5005);
		public static final int SYSTEM_DISABLE_CLIENT = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5301);
		public static final int SYSTEM_ENABLE_CLIENT = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5401);

		//Non Terminating Action
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5001;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5002;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5003;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5004;
		public static final int CLIENT_APPLY_REGISTRANTS_ONLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5009;
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
	

	/*
	 * 
	 * RequestTypeConstants holds the request created only from client
	 * 
	 */
	public static class ROOT_REQUEST_TYPE_ID
	{
		public static final int REQUEST_NEW_CONNECTION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 0;
		public static final int REQUEST_NEW_LINK = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 100;
		public static final int REQUEST_UPGRADE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 200;
		public static final int REQUEST_DOWNGRADE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 300;
		public static final int REQUEST_REDUNDANT_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 400;
		public static final int REQUEST_CONNECTION_CLOSE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 500;
	}
	public static class REQUEST_NEW_CONNECTION
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1;
		public static final int SYSTEM_CREATE_CONNECTION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1);
	}	
	public static class REQUEST_NEW_LINK
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 101;
		public static final int UPDATE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 102;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 103;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 104;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 105;
		
		public static final int SYSTEM_PREPARED_TO_GENERATE_MRC = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 130;
		/*******************************************/
		
		public static final int SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 141;
		/*
		 * Next State:
		 * 
		 */
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 142;
		
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR_NEGATIVE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 142);
		
		public static final int GET_PREPARED_TO_UPDATE_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 143;
		/*
		 * Next State:
		 * 
		 */
//		public static final int SYSTEM_PREPARE_FOR_EXTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 143;
		/*
		 * Next State:
		 * 
		 */		
		/*******************************************/
		
		
		public static final int CLIENT_APPLY_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 151;
		/*
		 * Next State:
		 * WAITING_FOR_LINK_APPROVAL 
		 */
		public static final int SYSTEM_APPROVE_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 153;
		/*
		 * Next State:
		 * NEAR_END_APPLICATION_APPROVED
		 */
		public static final int SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 155;
		/*
		 * Next State:
		 * 
		 */
		public static final int SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 157;
		/*
		 * Next State:
		 * 
		 */
		public static final int SYSTEM_PREPARED_TO_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 161;				
		public static final int SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 163;
		public static final int SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 165;
		public static final int SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 167;
		
		public static final int SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 159;
		
		/*********************************************/

		public static final int CLIENT_APPLY_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 152;
		/*
		 * Next State:
		 * WAITING_FOR_LINK_APPROVAL 
		 */

		public static final int SYSTEM_GET_PREPARED_FOR_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 154;
		/*
		 * Next State:
		 * FAR_END_APPLICATION_APPROVED
		 */
		
		public static final int SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 156;
		/*
		 * Next State:
		 * 
		 */
		
		public static final int SYSTEM_PREPARED_TO_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 162;
		public static final int SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 164;
		public static final int SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 166;
		public static final int SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 168;
		
		public static final int SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 158;
		/*
		 * Next State:
		 * PreparedForDemandNoteGeneration
		 * 
		 */
		public static final int SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 160;
		/************************************************/
		
		/************************************************/
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 102);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 103);
		public static final int SYSTEM_START_SERVICE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 120);
		
		
		/************************************************/
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 110;
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 111;
		public static final int SYSTEM_CANCEL_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 112;
		public static final int SYSTEM_EXTEND_DEMAND_NOTE_DEADLINE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 113;
		
		/************************************************/
		
		public static final int CLIENT_PAY_FOR_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 115;
		public static final int SYSTEM_VERIFY_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 116;
		public static final int SYSTEM_REJECT_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 117;
		
		/************************************************/
		
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 121;
		public static final int SYSTEM_INFORM_SETUP_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 122;
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 123;
/*		public static final int CLIENT_COMPLETE_TEST_WITH_SUCCESS = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 125;		
		public static final int GET_PREPARED_TO_START_SERVICE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 126;*/
		
		/**************************************************/
		public static final int SYSTEM_CANCEL_REQUEST = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 199);//ANY REQUEST THAT WAS CREATED BY HIM
		public static final int SYSTEM_SKIP_DEMAND_NOTE_DEMAND_NOTE_GENERATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 191;
		public static final int SYSTEM_SKIP_DEMAND_NOTE_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 194;
		public static final int SYSTEM_SKIP_MRC_GENERATION = (ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 192);
		
		
		public static final int SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 195;
		public static final int SYSTEM_GENERATE_MIGRATION_MRC = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 196;
		
		
	}
	public static class REQUEST_UPGRADE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 201;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 202;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 203;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 204;		
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 205;		
		
		/************************************************/
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 210;
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 211;	
		
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 202);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 203);
		public static final int SYSTEM_START_SERVICE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 220);
		
	
		
		/************************************************/
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 221;
		public static final int SYSTEM_INFORM_SETUP_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 222;
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 223;		
		
		public static final int  SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 241;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 242;
	}
	
	
	public static class REQUEST_DOWNGRADE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 301;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 302;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 303;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 304;		
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 305;		
		
		
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 310;
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 311;
		
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 321;
		public static final int SYSTEM_INFORM_SETUP_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 322;
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 323;
		
		/************************************************/
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 302);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 303);
		public static final int SYSTEM_START_SERVICE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 320);
		
		/************************************************/
		
		public static final int  SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 341;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 342;		
	}
	
	public static class REQUEST_CONNECTION_CLOSE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 501;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 502;
		public static final int CLIENT_CANCEL_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 503;
		public static final int SYSTEM_REJECT_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 504;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 505;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 506;
		public static final int  SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 541;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 542;
		
	}
	public static class REQUEST_LINK_CLOSE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 601;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 602;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 603;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 605;		
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 604;
		
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 621;
		public static final int SYSTEM_REQUEST_FINANCIAL_CLEARANCE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 631;
		public static final int SYSTEM_COMPLETE_CLOSE_LINK = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 620);				
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 602);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 603);
	}
	public static class REQUEST_CONNECTION_OWNERSHIP_CHANGE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 701;
		public static final int CLIENT_REAPPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 702;
		public static final int CLIENT_CANCEL_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 703;
		public static final int SYSTEM_REJECT_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 704;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 705;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 706;	
		
		public static final int  SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 741;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 742;
	}
	public static class REQUEST_LINK_OWNERSHIP_CHANGE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 801;
	}
	public static class REQUEST_POP_CHANGE
	{	
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 901;
		public static final int UPDATE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 902;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 903;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 904;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 905;
		
		public static final int SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 941;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 942;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR_NEGATIVE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 942);
		
		public static final int GET_PREPARED_TO_UPDATE_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 943;
		
		public static final int CLIENT_APPLY_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 951;
		public static final int CLIENT_APPLY_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 952;
		public static final int SYSTEM_GET_PREPARED_FOR_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 953;
		public static final int SYSTEM_GET_PREPARED_FOR_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 954;
		public static final int SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 955;
		public static final int SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 956;
		public static final int SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 957;
		public static final int SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 958;
		public static final int SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 959;
		public static final int SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 960;
		
		public static final int SYSTEM_PREPARED_TO_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 961;				
		public static final int SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 963;
		public static final int SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 965;
		public static final int SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 967;
		public static final int SYSTEM_SELECT_PORT_FOR_NEAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 969;

		public static final int SYSTEM_PREPARED_TO_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 962;
		public static final int SYSTEM_REQUEST_EXACT_EXTERNAL_FR_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 964;
		public static final int SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 966;
		public static final int SYSTEM_ACCEPT_EXACT_EXTERNAL_FR_OF_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 968;
		public static final int SYSTEM_SELECT_PORT_FOR_FAR_END = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 970;
		

		/************************************************/
		public static final int SYSTEM_START_SERVICE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 920);
		
		
		/************************************************/
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 910;
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 911;
		public static final int SYSTEM_CANCEL_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 912;
		public static final int SYSTEM_EXTEND_DEMAND_NOTE_DEADLINE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 913;
		
		/************************************************/
		
		public static final int CLIENT_PAY_FOR_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 915;
		public static final int SYSTEM_VERIFY_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 916;
		public static final int SYSTEM_REJECT_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 917;
		
		/************************************************/
		
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 921;
		public static final int SYSTEM_INFORM_SETUP_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 922;
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 923;
		
		public static final int SYSTEM_PREPARE_TO_START_SERVICE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 930;
		
		/**************************************************/
		public static final int SYSTEM_CANCEL_REQUEST = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 999);//ANY REQUEST THAT WAS CREATED BY HIM
		
		public static final int SYSTEM_SKIP_DEMAND_NOTE_DEMAND_NOTE_GENERATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 991;
		public static final int SYSTEM_SKIP_MRC_GENERATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 992);
		
		public static final int SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 995;
		public static final int SYSTEM_GENERATE_MIGRATION_MRC = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 996;
		public static final int SYSTEM_SKIP_DEMAND_NOTE_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 994;
		public static final int SYSTEM_GENERATE_MIGRATION_DEMANDNOTE = 0;
		
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 902);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 903);
	}
	public static class REQUEST_LOCAL_LOOP_CHANGE
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1001;//Thinking POP is unchanged the client wants to only extend local loop etc
	}
	
	
	public static class REQUEST_MIGRATE_CLIENT //merge two clients info to one existing one
	{
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5101;
	}
	public static class REQUEST_DELETE_CLIENT
	{
		public static final int SYSTEM_DELETE_CLIENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5201;
	}
	public static class REQUEST_DISABLE_CLIENT
	{
		public static final int SYSTEM_DISABLE_CLIENT = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5301);
		public static final int SYSTEM_ENABLE_CLIENT = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5401);
	}	
	public static class REQUEST_DISABLE_LINK
	{
		public static final int CLIENT_APPLY_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2301;
		public static final int UPDATE_APPLICATION_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2302;
		public static final int SYSTEM_VERIFY_APPLICATION_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2303;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2304;
		public static final int SYSTEM_APPROVE_APPLICATION_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2305;
		public static final int SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2321;
		public static final int SYSTEM_REQUEST_WITH_INTERNAL_FR_FOR_DISABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2341;
		public static final int SYSTEM_DISABLE_LINK = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2301);				
		

		public static final int CLIENT_APPLY_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2401;
		public static final int UPDATE_APPLICATION_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2402;
		public static final int SYSTEM_VERIFY_APPLICATION_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2403;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2404;
		public static final int SYSTEM_APPROVE_APPLICATION_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2405;
		public static final int SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2421;
		public static final int SYSTEM_REQUEST_WITH_INTERNAL_FR_FOR_ENABLE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2441;
		public static final int SYSTEM_ENABLE_LINK = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 2401);
	}
	
	public static class REMAINDERS
	{
		public static final int REM_SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = 04;
		public static final int REM_SYSTEM_REQUEST_WITH_INTERNAL_FR = 41;
		public static final int REM_SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END = 55;
		public static final int REM_SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END = 56;
		public static final int REM_SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END = 57;
		public static final int REM_SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END = 58;
		
	}
	
	
	//Meaningful to LLI
	public static class REQUEST_IPADDRESS {
		/************************************************/
		public static final int CLIENT_APPLY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 401;
		public static final int UPDATE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 402;
		public static final int SYSTEM_VERIFY_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 403;
		public static final int SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 404;
		public static final int SYSTEM_APPROVE_APPLICATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 405;
		/************************************************/
		public static final int SYSTEM_REQUEST_WITH_INTERNAL_FR = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 441;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR= ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 442;
		public static final int SYSTEM_RESPONSE_WITH_INTERNAL_FR_NEGATIVE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 442);
		/************************************************/
		public static final int SYSTEM_START_SERVICE = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 420);
		/************************************************/
		public static final int GET_PREPARED_TO_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 410;
		public static final int SYSTEM_GENERATE_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 411;
		public static final int SYSTEM_CANCEL_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 412;
		public static final int SYSTEM_EXTEND_DEMAND_NOTE_DEADLINE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 413;
		/************************************************/
		public static final int CLIENT_PAY_FOR_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 415;
		public static final int SYSTEM_VERIFY_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 416;
		public static final int SYSTEM_REJECT_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 417;
		/************************************************/
		public static final int SYSTEM_REQUEST_ADVICE_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 421;
		public static final int SYSTEM_INFORM_SETUP_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 422;
		public static final int SYSTEM_INFORM_TESTING_DONE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 423;
		public static final int SYSTEM_PREPARE_TO_START_SERVICE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 430;
		/**************************************************/
		public static final int SYSTEM_CANCEL_REQUEST = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 499);
		/************************************************/		
		public static final int SYSTEM_SKIP_DEMAND_NOTE_DEMAND_NOTE_GENERATION = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 491;
		public static final int SYSTEM_SKIP_MRC_GENERATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 492);
		/************************************************/
		public static final int SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 495;
		public static final int SYSTEM_GENERATE_MIGRATION_MRC = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 496;
		public static final int SYSTEM_SKIP_DEMAND_NOTE_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 494;
		public static final int SYSTEM_GENERATE_MIGRATION_DEMANDNOTE = 0;
		/************************************************/
		public static final int CLIENT_CANCEL_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 402);
		public static final int SYSTEM_REJECT_APPLICATION = -(ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 403);
	}
	
	
	
	
	
	public static Set<Integer> remainderSetOfResponseBack = new HashSet<Integer>();
	static
	{
		remainderSetOfResponseBack.add(REMAINDERS.REM_SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END);
		remainderSetOfResponseBack.add(REMAINDERS.REM_SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END);
	}

	static
	{
		

		requestTypeMap.put(REQUEST_NEW_CONNECTION.CLIENT_APPLY, "New Connection");
		requestTypeMap.put(REQUEST_NEW_LINK.CLIENT_APPLY, "New Link");
		requestTypeMap.put(REQUEST_UPGRADE.CLIENT_APPLY, "Upgrade");
		requestTypeMap.put(REQUEST_DOWNGRADE.CLIENT_APPLY, "Downgrade");
		requestTypeMap.put(REQUEST_CONNECTION_CLOSE.CLIENT_APPLY, "Connection Close");
		requestTypeMap.put(REQUEST_LINK_CLOSE.CLIENT_APPLY, "Link Close");
		requestTypeMap.put(REQUEST_CONNECTION_OWNERSHIP_CHANGE.CLIENT_APPLY, "Connection Ownership change");
		requestTypeMap.put(REQUEST_LINK_OWNERSHIP_CHANGE.CLIENT_APPLY, "Link Ownership Close");
		requestTypeMap.put(REQUEST_POP_CHANGE.CLIENT_APPLY, "Pop Change");
		requestTypeMap.put(REQUEST_LOCAL_LOOP_CHANGE.CLIENT_APPLY, "Local Loop Change");
		
		requestTypeMap.put(REQUEST_NEW_CLIENT.CLIENT_APPLY, "New Client");
		requestTypeMap.put(REQUEST_MIGRATE_CLIENT.CLIENT_APPLY, "Migrate Client");
		
		requestTypeMap.put(REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE, "Request advice note" );
	}
	static
	{
		
		String[][] LINK_UPDATE_APPLICATION_ACTION_CRITERIA = {
				{ "../../LliLinkAction.do?", "Update Connection Information" }};
		String[][] SYSTEM_RESPONSE_WITH_INTERNAL_FR_ACTION_CRITERIA = {
				{ "../../LliLinkAction.do?getMode=internalFrResponse&", "Response Internal FR" }};
		
		/*String[][] SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END = {				
				{ "../../includes/elements/portTypeList.jsp", "portType" },
				{ "../../includes/elements/description.jsp", "description" }
				};*/
		String[][] UPDATE_INTERNAL_FR_LLI = {
				{ "../../LliLinkAction.do?getMode=internalFrResponse&", "Update Internal FR" },
				{ "../../includes/elements/description.jsp", "description" }
		};
		String[][] SELECTION_AND_DESCRIPTION = {
				{ "../../includes/elements/adminList.jsp", "requestToAccountID" },
				{ "../../includes/elements/description.jsp", "description" }, };				
		
		String[][] SYSTEM_VERIFY_APPLICATION_CRITERIA = {
				{ "../../includes/elements/description.jsp", "description" }};

		String[][] SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION_CRITERIA = {
				{ "../../includes/elements/description.jsp", "description" } };

		String[][] SYSTEM_REJECT_APPLICATION_CRITERIA = {
				{ "../../includes/elements/description.jsp", "description" },
				{ "../../includes/elements/uploadDocuments.jsp", "uploadDocuments" }};
		
		String[][] SYSTEM_FR_RESPONSE_WITH_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA = {
				{"../../includes/elements/externalFRResponseLLI.jsp"},
				{ "../../includes/elements/description.jsp", "description" },
				{ "../../includes/elements/uploadDocuments.jsp", "uploadDocuments" }
				};

		String[][] SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA = {
				{ "../../includes/elements/adminList.jsp", "requestToAccountID" },
				{ "../../includes/elements/description.jsp", "description" } };
		
		String[][] REQUEST_ADVICE_NOTE = {
				{ "../../includes/elements/adminList.jsp", "requestToAccountID" },
				{ "../../includes/elements/description.jsp", "description" } };
		
		String[][] REQUEST_BANDWIDTH_CHANGE = {
				{ "Target Bandwidth", "targetBandWidth" },
				{ "../../includes/elements/description.jsp", "description" } };		
		
		String[][] GENERATE_DEMAND_NOTE = {
				{ "../../includes/elements/generateDemandNoteLinkLLI.jsp", "generateDemandNoteLink" } };
		
		String[][] GENERATE_DEMAND_NOTE_BANDWIDTH_CHANGE = {
				{ "../../includes/elements/generateDemandNoteBandwidthChangeLLI.jsp", "generateDemandNoteLink" } };
		
		//
		String[][] REQUEST_IPADDRESS_SYSTEM_RESPONSE_WITH_INTERNAL_FR = {
				{ "../../LliLinkIpaddress/responseInternalFR.do?", "Response Internal FR" }};

		/***********REQUEST_NEW_CLIENT*********/
		
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.UPDATE_APPLICATION, LINK_UPDATE_APPLICATION_ACTION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR, SYSTEM_RESPONSE_WITH_INTERNAL_FR_ACTION_CRITERIA);
		/*requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END, SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END, SYSTEM_ACCEPT_EXTERNAL_FR_OF_NEAR_END);*/
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, UPDATE_INTERNAL_FR_LLI);
		
		
		requestTypToSearchCriteria.put(REQUEST_NEW_CLIENT.SYSTEM_VERIFY_APPLICATION, SYSTEM_VERIFY_APPLICATION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_CLIENT.SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION, SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION, SYSTEM_REJECT_APPLICATION_CRITERIA);

		/***********REQUEST_NEW_LINK*********/
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_VERIFY_APPLICATION, SYSTEM_VERIFY_APPLICATION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION, SYSTEM_REQUEST_FOR_CORRECTED_APPLICATION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_REJECT_APPLICATION, SYSTEM_REJECT_APPLICATION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_REQUEST_WITH_INTERNAL_FR, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_CANCEL_DEMAND_NOTE, SYSTEM_VERIFY_APPLICATION_CRITERIA);
		/***********FAR END*********/
		
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END, SYSTEM_FR_RESPONSE_WITH_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		
		requestTypToSearchCriteria.put(REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END, SYSTEM_FR_RESPONSE_WITH_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		
		requestTypToSearchCriteria.put( REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE, GENERATE_DEMAND_NOTE);
		
		requestTypToSearchCriteria.put( REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put( REQUEST_UPGRADE.SYSTEM_REQUEST_ADVICE_NOTE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put( REQUEST_DOWNGRADE.SYSTEM_REQUEST_ADVICE_NOTE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put( REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put( REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put( REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE, REQUEST_ADVICE_NOTE );
		requestTypToSearchCriteria.put(REQUEST_LINK_CLOSE.SYSTEM_REQUEST_FINANCIAL_CLEARANCE, SELECTION_AND_DESCRIPTION);
//		requestTypToSearchCriteria.put( REQUEST_NEW_LINK.SYSTEM_REQUEST_SETUP, REQUEST_SETUP );
		
		/**************************************/
		requestTypToSearchCriteria.put( REQUEST_UPGRADE.CLIENT_APPLY, REQUEST_BANDWIDTH_CHANGE );
		requestTypToSearchCriteria.put(REQUEST_UPGRADE.SYSTEM_REQUEST_WITH_INTERNAL_FR, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, SYSTEM_RESPONSE_WITH_INTERNAL_FR_ACTION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_DOWNGRADE.SYSTEM_REQUEST_WITH_INTERNAL_FR, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, SYSTEM_RESPONSE_WITH_INTERNAL_FR_ACTION_CRITERIA);
		requestTypToSearchCriteria.put( REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE, GENERATE_DEMAND_NOTE_BANDWIDTH_CHANGE);
		/*************************************/
		requestTypToSearchCriteria.put(REQUEST_POP_CHANGE.SYSTEM_REQUEST_WITH_INTERNAL_FR, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_POP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR, SYSTEM_RESPONSE_WITH_INTERNAL_FR_ACTION_CRITERIA);
		requestTypToSearchCriteria.put(REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE, UPDATE_INTERNAL_FR_LLI);
		
		/*************************************/
		requestTypToSearchCriteria.put(REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR, REQUEST_IPADDRESS_SYSTEM_RESPONSE_WITH_INTERNAL_FR);
		requestTypToSearchCriteria.put(REQUEST_IPADDRESS.SYSTEM_REQUEST_WITH_INTERNAL_FR, SYSTEM_FR_REQUEST_FOR_LOOP_CHECK_NEAR_AND_FAR_END_CRITERIA);
		
		
	}
}
