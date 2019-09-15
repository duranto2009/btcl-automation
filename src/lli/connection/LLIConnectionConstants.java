package lli.connection;

import common.bill.BillConstants;
import lli.Application.AdditionalConnectionAddress.LLIAdditionalConnectionAddressApplication;
import lli.Application.AdditionalIP.LLIAdditionalIPApplication;
import lli.Application.AdditionalLocalLoop.LLIAdditionalLocalLoopApplication;
import lli.Application.AdditionalPort.LLIAdditionalPortApplication;
import lli.Application.BreakLongTerm.LLIBreakLongTermApplication;
import lli.Application.ChangeBillingAddress.LLIChangeBillingAddressApplication;
import lli.Application.CloseConnection.LLICloseConnectionApplication;
import lli.Application.DowngradeBandwidth.LLIDowngradeBandwidthApplication;
import lli.Application.LLIApplication;
import lli.Application.NewConnection.LLINewConnectionApplication;
import lli.Application.NewLongTerm.LLINewLongTermApplication;
import lli.Application.Reconnect.LLIReconnectApplication;
import lli.Application.ReleaseConnectionAddress.LLIReleaseConnectionAddressApplication;
import lli.Application.ReleaseIP.LLIReleaseIPApplication;
import lli.Application.ReleaseLocalLoop.LLIReleaseLocalLoopApplication;
import lli.Application.ReleasePort.LLIReleasePortApplication;
import lli.Application.ShiftAddress.LLIShiftAddressApplication;
import lli.Application.ShiftBandwidth.LLIShiftBandwidthApplication;
import lli.Application.ShiftPop.LLIShiftPopApplication;
import lli.Application.TemporaryUpgradeBandwidth.LLITemporaryUpgradeBandwidthApplication;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.Application.upgradeBandwidth.LLIUpgradeBandwidthApplication;
import lli.LLIActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLIConnectionConstants {



    public static final int ADVICE_NOTE_GENERATION_FOR_LOCAL_LOOP = 119;
	public static final int BILLING_ADDRESS_CHANGE_STATE = 120;
	public final static String LLI_APPLICATION_BASE_URL = "lli/application";
	public final static String LLI_APPLICATION_DETAILS_PAGE_ACTION_URL = "/newview";

	public final static String LLI_DETAILS_PAGE_URL = "/"+LLI_APPLICATION_BASE_URL+LLI_APPLICATION_DETAILS_PAGE_ACTION_URL+".do?id=";
	public final static String LLI_REVISE_DETAILS_PAGE_URL = "/lli/revise/newview.do?id=";


	public static final Map<Integer,String> completeStates=new HashMap(){{
		put(28,"Final");
		put(41,"Final");
		put(43,"Final");
		put(44,"Final");
		put(75,"Final");
		put(76,"Final");
		put(78,"Final");
		put(80,"Final");
		put(84,"Final");
		put(90,"Final");
		put(108,"Final");
		put(117,"Final");
		put(514,"Final");
		put(1016,"Final");
		put(2012,"Final");
	}};











	//3/21/19 for additional loop application
	public static final int ADD_NEW_ADDITIONAL_LOOP = 2;
	public static final int REUSE_ADDITIONAL_LOOP = 1;
	public static final int REPLACE_ADDITIONAL_LOOP = 3;



	public  enum IPUsageType {
		MANDATORY, ADDITIONAL
	}
	/*EntityType*/
	public static final int ENTITY_TYPE = 702;
	
	/*Connection Status*/
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 500;
	public static final int STATUS_TD = 2;
	public static final int STATUS_CLOSED = 3;
	public static final int STATUS_OWNERSHIP_CHANGED = 5;
	public static final int OWNERSHIP_CHANGE_ON_PROCESS = 6;

	//jami new local loop state
    public static final int NEW_LOCAL_LOOP_BTCL_STATE = 118;
	public static final int ADDITIONAL_PORT_STATE = 106;
    // end
	public static final int OWNER_SHIP_CHANGE_STATE=500;

	public static final int LOOP_PROVIDER_BTCL_STATE=1;
	public static final int LOOP_PROVIDER_CLIENT_STATE=54;
	public static final int UPGRADE_BANDWIDTH_STATE=45;

	public static final int CLOSE_CONNECTION_STATE=1001;
	public static final int CLOSE_CONNECTION_PAYMENT_DONE_STATE=1008;
	public static final int CLOSE_CONNECTION_POLICY_STATE=1101;
	public static final int CLOSE_CONNECTION_POLICY_REOPEN_STATE=1104;
	public static final int CLOSE_CONNECTION_POLICY_BYPASS_STATE=1102;
	public static final int CLOSE_CONNECTION_WO_BYPASS=1017;

	public static final int DOWNGRADE_BANDWIDTH_STATE=46;
	public static final int DOWNGRADE_BANDWIDTH_POLICY_STATE=1201;
	public static final int TD_SUBMIT_STATE=82;
	public static final int RECONNECT_SUBMIT_STATE=85;
	public static final int WITHOUT_LOOP_IFR_RESPONSE=59;
	public static final int NEW_CONNECTION_CORRECTION_CLIENT_LOOP_SUBMITTED = 60;
	public static final int NEW_CONNECTION_CORRECTION_BTCL_LOOP_SUBMITTED = 3;
	public static final int SHIFT_BW_SUBMITTED = 2001;
	public static final int SHIFT_BW_SUBMITTED_NO_APPROVAL = 2013;
	public static final int SHIFT_BW_SUBMITTED_ONLY_CDGM_APPROVAL = 2014;
	public static final int SHIFT_BW_SUBMITTED_ONLY_LDGM_APPROVAL = 2015;
	public static final int SHIFT_BW_NEW_CONNECTION_SUBMITTED = 2101;
	public static final int SHIFT_BW_NEW_CONNECTION_SUBMITTED_NO_APPROVAL = 2108;
	public static final int SHIFT_BW_NEW_CONNECTION_SUBMITTED_ONLY_CDGM_APPROVAL = 2109;
	public static final int SHIFT_BW_NEW_CONNECTION_SUBMITTED_ONLY_LDGM_APPROVAL = 2110;

	public static final int WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_NO_APPROVAL = 2209;
	public static final int WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_CDGM_APPROVAL = 2210;
	public static final int WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_LDGM_APPROVAL = 2211;

	public static final int WITHOUT_LOOP_SHIFT_BW_NEW_CONNECTION_SUBMITTED = 2201;
	public static final int SHIFT_BW_NEW_DN_BYPASS = 2208;
	public static final int WITHOUT_LOOP_DEMAND_NOTE=63;

	//TODO: maruf/bony: change hard coded flow init state
	public static final int NEW_LONG_TERM_SUBMITTED=107;
	public static final int NEW_LONG_TERM_COMPLETED=108;
	public static final int BREAK_LONG_TERM_SUBMITTED=111;
	public static final int BREAK_LONG_TERM_COMPLETED=117;
	public static final int BREAK_LONG_TERM_APPROVED = 112;
	public static final int RECONNECT_DEMAND_NOTE = 86;
	//raihan for testing
	public static final int TD_AN = 83;
	public static final int TD_DONE= 84;
	public static final int RECONNECT_DONE= 90;
	public static final int RECONNECT_AN= 89;


	public static final int IFR_SELECTED=1;
	public static final int IFR_INITIAL=0;
	public static final int IFR_IGNORED=2;


	public static final long EFR_QUOTATION_GIVEN= 1;
	public static final long EFR_QUOTATION_INITIAL= 0;
	public static final long EFR_QUOTATION_IGNORED= 3;
	public static final long EFR_QUOTATION_EXPIRED= 2;



	public static final int EFR_WORK_GIVEN=1;
	public static final int EFR_WORK_INITIAL=0;
	public static final int EFR_WORK_EXPIRED=2;



	public static final int EFR_WORK_DONE=1;
	public static final int EFR_WORK_NOT_DONE=2;

	public static final int FORWARDED_WORK_ORDER_GENERATE = 17;
	public static final int WORK_ORDER_GENERATE = 39;



	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> connectionStatusMap = new HashMap() {{
		put(STATUS_ACTIVE, "Active");
		put(STATUS_INACTIVE, "Inactive");
		put(STATUS_TD, "Temporarily Disconnected");
		put(STATUS_CLOSED, "Closed");
		put(STATUS_OWNERSHIP_CHANGED, "Ownership Changed");
	}};
	
	/*Connection Status*/
	
	
	/*Connection Type*/
	public static final int CONNECTION_NOT_APPLICABLE = 0;
	public static final int CONNECTION_TYPE_REGULAR = 1;
	public static final int CONNECTION_TYPE_TEMPORARY = 2;
	public static final int CONNECTION_TYPE_CACHE = 3;
	public static final int CONNECTION_TYPE_REGULAR_LONG = 4;
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> connectionTypeMap = new HashMap() {{
		put(CONNECTION_NOT_APPLICABLE, "Not Applicable");
		put(CONNECTION_TYPE_REGULAR, "Regular");
		put(CONNECTION_TYPE_TEMPORARY, "Temporary");
		put(CONNECTION_TYPE_CACHE, "Transmission for Cache");
		put(CONNECTION_TYPE_REGULAR_LONG, "Regular with Long Term");
	}};
	/*Connection Type*/
	
	/*Loop Provider Type*/
	public static final int LOOP_PROVIDER_BTCL = 1;
	public static final int LOOP_PROVIDER_CLIENT = 2;
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> loopProviderMap = new HashMap() {{
		put(LOOP_PROVIDER_BTCL, "BTCL");
		put(LOOP_PROVIDER_CLIENT, "Client");
	}};


	public static final int POLICY_ACCORDING_SETTING = 1;
	public static final int POLICY_INSTANT = 2;

	/*Loop Provider Type*/
	
	/*OFC Type*/
	public static final int OFC_TYPE_SINGLE = 1;
	public static final int OFC_TYPE_DUAL = 2;
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> ofcTypeMap = new HashMap() {{
		put(OFC_TYPE_SINGLE, "Single");
		put(OFC_TYPE_DUAL, "Dual");
	}};
	/*OFC Type*/
	
	
	
	/*Application Status*/
	public static final int STATUS_APPLIED = 1;
	public static final int STATUS_VERIFIED = 2;
	public static final int STATUS_PROCESSED = 3;
	public static final int STATUS_FINALIZED = 4;
	public static final int STATUS_DEMAND_NOTE_GENERATED = 5;
	public static final int STATUS_PAYMENT_CLEARED = 6;
	public static final int STATUS_COMPLETED = 7;
	public static final int STATUS_REQUESTED_FOR_CORRECTION = 50;
	public static final int STATUS_REJECTED = 51;
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> applicationStatusMap = new HashMap() {{
		put(STATUS_APPLIED, "Applied");
		put(STATUS_VERIFIED, "Verified");
		put(STATUS_PROCESSED, "Processed");
		put(STATUS_FINALIZED, "Finalized");
		put(STATUS_DEMAND_NOTE_GENERATED, "Demand Note Generated");
		put(STATUS_PAYMENT_CLEARED, "Payment Cleared");
		put(STATUS_COMPLETED, "Completed");
		put(STATUS_REQUESTED_FOR_CORRECTION, "Requested For Correction");
		put(STATUS_REJECTED, "Rejected");
	}};
	/*Application Status*/
	
	/*Application Type*/
	public static final int NEW_CONNECTION = 1;
	public static final int UPGRADE_BANDWIDTH = 2;
	public static final int DOWNGRADE_BANDWIDTH = 3;
	public static final int TEMPORARY_UPGRADE_BANDWIDTH = 4;
	public static final int ADDITIONAL_PORT = 5;
	public static final int RELEASE_PORT = 6;
	public static final int ADDITIONAL_LOCAL_LOOP = 7;
	public static final int RELEASE_LOCAL_LOOP = 8;
	public static final int ADDITIONAL_IP = 9;
	public static final int RELEASE_IP = 10;
	public static final int ADDITIONAL_CONNECTION_ADDRESS = 11;
	public static final int SHIFT_CONNECTION_ADDRESS = 12;
	public static final int RELEASE_CONNECTION_ADDRESS = 13;
	public static final int SHIFT_POP = 14;
	public static final int NEW_LONG_TERM = 15;
	public static final int BREAK_LONG_TERM = 16;
	public static final int SHIFT_BANDWIDTH = 17;
	public static final int CHANGE_OWNERSHIP = 18;
	public static final int RECONNECT = 19;
	public static final int CHANGE_BILLING_ADDRESS = 20;
	public static final int CLOSE_CONNECTION = 21;
	public static final int TD = 22;
	public static final int REVISE_CONNECTION = 100;
	public static final int SHIFT_BANDWIDTH_NEW_CONNECTION = 101;

	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> applicationTypeNameMap = new HashMap() {{
		put(NEW_CONNECTION, "New Connection");
		put(UPGRADE_BANDWIDTH, "Upgrade Bandwidth");
		put(DOWNGRADE_BANDWIDTH, "Downgrade Bandwidth");
		put(TEMPORARY_UPGRADE_BANDWIDTH, "Temporary Upgrade Bandwidth");
		put(ADDITIONAL_PORT, "Additional Port");
		put(RELEASE_PORT, "Release Port");
		put(ADDITIONAL_LOCAL_LOOP, "Additional Local Loop");
		put(RELEASE_LOCAL_LOOP, "Release Local Loop");
		put(ADDITIONAL_IP, "Additional IP");
		put(RELEASE_IP, "Release IP");
		put(ADDITIONAL_CONNECTION_ADDRESS, "Additional Address");
		put(SHIFT_CONNECTION_ADDRESS, "Shift Address");
		put(RELEASE_CONNECTION_ADDRESS, "Release Address");
		put(SHIFT_POP, "Shift Pop");
		put(NEW_LONG_TERM, "New Long Term");
		put(BREAK_LONG_TERM, "Break Long Term");
		put(SHIFT_BANDWIDTH, "Shift Bandwidth");
		put(CHANGE_OWNERSHIP, "Change Ownership");
		put(RECONNECT, "Reconnect");
		put(CHANGE_BILLING_ADDRESS, "Change Billing Address");
		put(CLOSE_CONNECTION, "Close Connection");
		put(TD, "Temporary Disconnect");
		put(REVISE_CONNECTION, "Revise Connection");
		put(SHIFT_BANDWIDTH_NEW_CONNECTION, "New Connection from Bandwidth Shift");
	}};
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> applicationTypeHyphenSeperatedMap = new HashMap() {{
		put(NEW_CONNECTION, "new-connection");
		put(UPGRADE_BANDWIDTH, "upgrade-bandwidth"); // REVISE
		put(DOWNGRADE_BANDWIDTH, "downgrade-bandwidth"); // REVISE
		put(TEMPORARY_UPGRADE_BANDWIDTH, "temporary-upgrade-bandwidth"); // REVISE
		put(ADDITIONAL_PORT, "additional-port"); // REVISE
		put(RELEASE_PORT, "release-port"); // REVISE
		put(ADDITIONAL_LOCAL_LOOP, "additional-local-loop"); // REVISE
		put(RELEASE_LOCAL_LOOP, "release-local-loop"); // REVISE
		put(ADDITIONAL_IP, "additional-ip"); // REVISE
		put(RELEASE_IP, "release-ip"); // REVISE
		put(ADDITIONAL_CONNECTION_ADDRESS, "additional-connection-address"); // REVISE
		put(SHIFT_CONNECTION_ADDRESS, "shift-address"); // REVISE
		put(RELEASE_CONNECTION_ADDRESS, "release-connection-address"); // REVISE
		put(SHIFT_POP, "shift-pop"); // REVISE
		put(NEW_LONG_TERM, "new-long-term"); 
		put(BREAK_LONG_TERM, "break-long-term");
		put(SHIFT_BANDWIDTH, "shift-bandwidth");
		put(CHANGE_OWNERSHIP, "change-lli.Application.ownership");
		put(RECONNECT, "reconnect");
		put(CHANGE_BILLING_ADDRESS, "change-billing-address");
		put(CLOSE_CONNECTION, "close-connection");
		put(SHIFT_BANDWIDTH_NEW_CONNECTION, "new-connection-shift-bw");
	}};
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, Class<? extends LLIApplication>> applicationTypeClassnameMap = new HashMap() {{
		put(NEW_CONNECTION, LLINewConnectionApplication.class);
		put(UPGRADE_BANDWIDTH, LLIUpgradeBandwidthApplication.class);
		put(DOWNGRADE_BANDWIDTH, LLIDowngradeBandwidthApplication.class);
		put(TEMPORARY_UPGRADE_BANDWIDTH, LLITemporaryUpgradeBandwidthApplication.class);
		put(ADDITIONAL_PORT, LLIAdditionalPortApplication.class);
		put(RELEASE_PORT, LLIReleasePortApplication.class);
		put(ADDITIONAL_LOCAL_LOOP, LLIAdditionalLocalLoopApplication.class);
		put(RELEASE_LOCAL_LOOP, LLIReleaseLocalLoopApplication.class);
		put(ADDITIONAL_IP, LLIAdditionalIPApplication.class);
		put(RELEASE_IP, LLIReleaseIPApplication.class);
		put(ADDITIONAL_CONNECTION_ADDRESS, LLIAdditionalConnectionAddressApplication.class);
		put(SHIFT_CONNECTION_ADDRESS, LLIShiftAddressApplication.class);
		put(RELEASE_CONNECTION_ADDRESS, LLIReleaseConnectionAddressApplication.class);
		put(SHIFT_POP, LLIShiftPopApplication.class);
		put(NEW_LONG_TERM, LLINewLongTermApplication.class);
		put(BREAK_LONG_TERM, LLIBreakLongTermApplication.class);
		put(SHIFT_BANDWIDTH, LLIShiftBandwidthApplication.class);
		put(CHANGE_OWNERSHIP, LLIOwnerShipChangeApplication.class);
		put(RECONNECT, LLIReconnectApplication.class);
		put(CHANGE_BILLING_ADDRESS, LLIChangeBillingAddressApplication.class);
		put(CLOSE_CONNECTION, LLICloseConnectionApplication.class);
	}};
	/*Application Type*/
	
	/*Bill Template Mapping with application*/
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> mapOfDemandNoteTemplateToApplicationType = new HashMap() {{
		put(NEW_CONNECTION, BillConstants.LLI_DEMAND_NOTE_NEW_CONNECTION_TEMPLATE);
		put(UPGRADE_BANDWIDTH, BillConstants.LLI_DEMAND_NOTE_UPGRADE_BANDWIDTH_TEMPLATE);
		put(DOWNGRADE_BANDWIDTH, BillConstants.LLI_DEMAND_NOTE_DOWNGRADE_BANDWIDTH_TEMPLATE);
		put(TEMPORARY_UPGRADE_BANDWIDTH, BillConstants.LLI_DEMAND_NOTE_TEMPORARY_UPGRADE_BANDWIDTH_TEMPLATE);
		put(ADDITIONAL_PORT, BillConstants.LLI_DEMAND_NOTE_ADDITIONAL_PORT_TEMPLATE);
		put(RELEASE_PORT, BillConstants.LLI_DEMAND_NOTE_RELEASE_PORT_TEMPLATE);
		put(ADDITIONAL_LOCAL_LOOP, BillConstants.LLI_DEMAND_NOTE_ADDITIONAL_LOCAL_LOOP_TEMPLATE);
		put(RELEASE_LOCAL_LOOP, BillConstants.LLI_DEMAND_NOTE_RELEASE_LOCAL_LOOP_TEMPLATE);
		put(ADDITIONAL_IP, BillConstants.LLI_DEMAND_NOTE_ADDITIONAL_IP_TEMPLATE);
		put(RELEASE_IP, BillConstants.LLI_DEMAND_NOTE_RELEASE_IP_TEMPLATE);
		put(ADDITIONAL_CONNECTION_ADDRESS, BillConstants.LLI_DEMAND_NOTE_ADDITIONAL_CONNECTION_ADDRESS_TEMPLATE);
		put(SHIFT_CONNECTION_ADDRESS, BillConstants.LLI_DEMAND_NOTE_SHIFT_CONNECTION_ADDRESS_TEMPLATE);
		put(RELEASE_CONNECTION_ADDRESS, BillConstants.LLI_DEMAND_NOTE_RELEASE_CONNECTION_ADDRESS_TEMPLATE);
		put(SHIFT_POP, BillConstants.LLI_DEMAND_NOTE_SHIFT_POP_TEMPLATE);
		put(NEW_LONG_TERM, BillConstants.LLI_DEMAND_NOTE_NEW_LONG_TERM_TEMPLATE);
		put(BREAK_LONG_TERM, BillConstants.LLI_DEMAND_NOTE_BREAK_LONG_TERM_TEMPLATE);
		put(SHIFT_BANDWIDTH, BillConstants.LLI_DEMAND_NOTE_SHIFT_BANDWIDTH_TEMPLATE);
		put(CHANGE_OWNERSHIP, BillConstants.LLI_DEMAND_NOTE_CHANGE_OWNERSHIP_TEMPLATE);
		put(RECONNECT, BillConstants.LLI_DEMAND_NOTE_RECONNECT_TEMPLATE);
		put(CHANGE_BILLING_ADDRESS, BillConstants.LLI_DEMAND_NOTE_CHANGE_BILLING_ADDRESS_TEMPLATE);
		put(CLOSE_CONNECTION, BillConstants.LLI_DEMAND_NOTE_CLOSE_CONNECTION_TEMPLATE);
	}};
	
	
	/*Actions on Application for specific Status */
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, List<LLIActionButton>> applicationActionMap = new HashMap ( new HashMap() {{
		put(STATUS_APPLIED, new ArrayList( new ArrayList() {{
			add(new LLIActionButton("Verify", "lli/application/application-verify.do", false));
			add(new LLIActionButton("Request For Correction", "lli/application/application-request-for-correction.do", true));
			add(new LLIActionButton("Reject", "lli/application/application-reject.do", true));
		}}));
		put(STATUS_VERIFIED, new ArrayList( new ArrayList() {{
			add(new LLIActionButton("Process", "lli/application/application-process.do", true));
		}}));
		put(STATUS_PROCESSED, new ArrayList( new ArrayList() {{
			add(new LLIActionButton("Process", "lli/application/application-process.do", true));
			add(new LLIActionButton("Finalize", "lli/application/application-finalize.do", false));
		}}));
		put(STATUS_FINALIZED, new ArrayList( new ArrayList() {{
			//will be handled based on business logic in application common
		}}));
		put(STATUS_DEMAND_NOTE_GENERATED, new ArrayList( new ArrayList() {{
		}}));
		put(STATUS_PAYMENT_CLEARED, new ArrayList( new ArrayList() {{
			add(new LLIActionButton("Complete Request", "lli/application/application-complete-request.do", true));
		}}));
		put(STATUS_COMPLETED, new ArrayList( new ArrayList() {{
		}}));
		put(STATUS_REQUESTED_FOR_CORRECTION, new ArrayList( new ArrayList() {{
		}}));
		put(STATUS_REJECTED, new ArrayList( new ArrayList() {{
		}}));
	}});
	/*Actions on Application for specific Status */
	
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	public static final List<Integer> REVISE_CONNECTION_APPLICATIONS = new ArrayList<>( new ArrayList() 
		{{
			add (new Integer(LLIConnectionConstants.UPGRADE_BANDWIDTH));
			add (new Integer(LLIConnectionConstants.DOWNGRADE_BANDWIDTH));
			add (new Integer(LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH));
			add (new Integer(LLIConnectionConstants.ADDITIONAL_PORT));
			add (new Integer(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP));
			add (new Integer(LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS));
			add (new Integer(LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS));
			add (new Integer(LLIConnectionConstants.SHIFT_POP));
			add (new Integer(LLIConnectionConstants.ADDITIONAL_IP));
		}}
	);
}
