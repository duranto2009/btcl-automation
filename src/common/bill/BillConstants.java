package common.bill;

import common.ModuleConstants;
import net.sf.jasperreports.engine.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class BillConstants {
	public static final int PREPAID = 0;
	public static final int POSTPAID = 1;
	public static final int PREPAID_AND_POSTPAID = 2;
	public static final int MONTHLY_BILL = POSTPAID; // 1
	public static final int MULTIPLE_MONTH_BILL = 10; // 1
	public static final int FINAL_BILL = 11; // 1
	public static final int DEMAND_NOTE = PREPAID_AND_POSTPAID; // 2
	public static final int MERGED_UNPAID_BILL = 3;
	public static final int MONTHLY_BILL_ADVANCED = 4;
	public static final int MANUAL_BILL = 5;
	public static final int YEARLY_BILL = 6;
	public static final int MULTIPLE_BILL_PARENT= 2;
	public static final int FINAL_BILL_PARENT= 3;
	public static final int NOT_PART_OF_MULTIPLE_BILL= 0;
	public static final int MULTIPLE_BILL_CHILD = 1;

	
	public static final String DOMAIN_BILL_TEMPLATE_LOCATION = "domain/bill/domain_invoice.jasper";

	
	public static final String COLOCATION_DEMAND_NOTE = "coLocation/dn-colocation.jasper";
	public static final String NIX_DEMAND_NOTE = "nix/dn-nix.jasper";
	

	public static final String VPN_BILL_MONTHLY_TEMPLATE_LOCATION = "vpn/bill/BillTable.jasper";
	public static final String VPN_DEMAND_NOTE = "vpn/dn-vpn.jasper";
	public static final String VPN_WORK_ORDER = "vpn/vpn-work-order.jasper";
	public static final String VPN_LINK_ADVICE_NOTE = "vpn/vpn-advice-note.jasper";
	public static final String VPN_MONTHLY_BILL_TEMPLATE = "vpn/vpn-monthly-bill.jasper";
	public static final String REQUEST_LETTER_TEMPLATE = "request-letter/request-letter.jasper";
	public static final String CLEARANCE_CERTIFICATE_WITH_DUE = "clearance-certificate/clearance-certificate.jasper";
	public static final String CLEARANCE_CERTIFICATE_WITHOUT_DUE = "clearance-certificate/clearance-certificate-no-due.jasper";
	public static final String MULTIPLE_BILL = "multiple-month-bill/multiple-month-bill.jasper";

	/***
	 * new LLI demand notes constant
	 */
	
	public static final String LLI_DEMAND_NOTE_NEW_CONNECTION_TEMPLATE = "lli/bill/dn-new-connection.jasper";
	public static final String LLI_DEMAND_NOTE_UPGRADE_BANDWIDTH_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_DOWNGRADE_BANDWIDTH_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_TEMPORARY_UPGRADE_BANDWIDTH_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_ADDITIONAL_PORT_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_RELEASE_PORT_TEMPLATE = "lli/bill/dn-release-port.jasper";
	public static final String LLI_DEMAND_NOTE_ADDITIONAL_LOCAL_LOOP_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_RELEASE_LOCAL_LOOP_TEMPLATE = "lli/bill/dn-release-local-loop.jasper";
	public static final String LLI_DEMAND_NOTE_ADDITIONAL_IP_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_RELEASE_IP_TEMPLATE = "lli/bill/dn-release-ip.jasper";
	public static final String LLI_DEMAND_NOTE_ADDITIONAL_CONNECTION_ADDRESS_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_SHIFT_CONNECTION_ADDRESS_TEMPLATE = "lli/bill/dn-shift-connection-address.jasper";
	public static final String LLI_DEMAND_NOTE_RELEASE_CONNECTION_ADDRESS_TEMPLATE = "lli/bill/dn-release-connection-address.jasper";
	public static final String LLI_DEMAND_NOTE_SHIFT_POP_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_DEMAND_NOTE_NEW_LONG_TERM_TEMPLATE = "lli/bill/dn-new-long-term.jasper";
	public static final String LLI_DEMAND_NOTE_BREAK_LONG_TERM_TEMPLATE = "lli/bill/dn-break-long-term-sb.jasper";
	public static final String LLI_DEMAND_NOTE_SHIFT_BANDWIDTH_TEMPLATE = "lli/bill/dn-shift-bandwidth.jasper";
	public static final String LLI_DEMAND_NOTE_CHANGE_OWNERSHIP_TEMPLATE = "lli/bill/dn-ownership-change.jasper";
	public static final String LLI_DEMAND_NOTE_RECONNECT_TEMPLATE = "lli/bill/dn-reconnect-connection.jasper";
	public static final String LLI_DEMAND_NOTE_CHANGE_BILLING_ADDRESS_TEMPLATE = "lli/bill/dn-change-billing-address.jasper";
	public static final String LLI_DEMAND_NOTE_CLOSE_CONNECTION_TEMPLATE = "lli/bill/dn-close-connection.jasper";
	public static final String LLI_DEMAND_NOTE_REVISE_CONNECTION_TEMPLATE = "lli/bill/dn-upgrade.jasper";
	public static final String LLI_CLOSE_TD_RECONNECT_ADVICE_NOTE = "lli/connectionAN/lli-td-reconnect-an.jasper";

	/***
	 * new LLI Bill 
	 */
	public static final String LLI_MANUAL_BILL_TEMPLATE = "lli/bill/manual-bill.jasper";
	public static final String LLI_MONTHLY_BILL_TEMPLATE = "lli/bill/lli-monthly-bill.jasper";
	public static final String LLI_CONNECTION_ADVICE_NOTE = "lli/connectionAN/connection-an.jasper";
	public static final String LLI_OWNERSHIP_CHANGE_ADVICE_NOTE = "lli/connectionAN/lli-ownership-change-an.jasper";


	public static final String COLOCATION_ADVICE_NOTE = "coLocation/advice-note-colocation.jasper";


	//NIX
	public static final String NIX_MONTHLY_BILL_TEMPLATE = "nix/nix-monthly-bill.jasper";
	public static final String NIX_ADVICE_NOTE = "lli/connectionAN/connection-an.jasper";
	public static final String NIX_WORK_ORDER = "nix/nix-work-order.jasper";

	public static final String NIX_CLOSE_TD_RECONNECT_ADVICE_NOTE = "nix/connectionAN/nix-td-reconnect-an.jasper";

	public static final int NUMBER_OF_DAY_BEFORE_BILL_IS_EXPIRED = 30;
	public static final long NUMBER_OF_DAY_BEFORE_BILL_IS_EXPIRED_IN_MILLIS = NUMBER_OF_DAY_BEFORE_BILL_IS_EXPIRED *24*60*60*1000L;
	
	public static final Integer[][] IpAddressCostMapping = {{4,800}};
	public static final Integer DEFAULT_IP_ADDRESS_COST = 200;



    public static class BillConfiguration{
		
		public static final int HEADER_TEXT_1 = 1;
		public static final int HEADER_TEXT_2 = 2;
		
		public static final int FOOTER_TEXT_1 = 4;
		public static final int FOOTER_TEXT_2 = 5;
		public static final int FOOTER_TEXT_3 = 3;
	}
	
	public static Map<String, Integer> formFieldName = new TreeMap<>();
	public static Map<String, String> formFieldText = new TreeMap<>();
	public static Map<Integer, String> idToFormField = new TreeMap<>();
	
	public static Map<Pair<Integer, Integer>, String> dummyDocumentLocationByModuleAndDocID = new HashMap< Pair<Integer, Integer>, String>();
	
	static{
		
		formFieldName.put( "header_" + BillConfiguration.HEADER_TEXT_1, BillConfiguration.HEADER_TEXT_1 );
		formFieldName.put( "header_" + BillConfiguration.HEADER_TEXT_2, BillConfiguration.HEADER_TEXT_2 );
		
		formFieldName.put( "footer_" + BillConfiguration.FOOTER_TEXT_1, BillConfiguration.FOOTER_TEXT_1 );
		formFieldName.put( "footer_" + BillConfiguration.FOOTER_TEXT_2, BillConfiguration.FOOTER_TEXT_2 );
		formFieldName.put( "footer_" + BillConfiguration.FOOTER_TEXT_3, BillConfiguration.FOOTER_TEXT_3 );
		
		formFieldText.put( "header_" + BillConfiguration.HEADER_TEXT_1, "Header Text 1" );
		formFieldText.put( "header_" + BillConfiguration.HEADER_TEXT_2, "Header Text 2" );
		
		formFieldText.put( "footer_" + BillConfiguration.FOOTER_TEXT_1, "Footer Text 1" );
		formFieldText.put( "footer_" + BillConfiguration.FOOTER_TEXT_2, "Footer Text 2" );
		formFieldText.put( "footer_" + BillConfiguration.FOOTER_TEXT_3, "Footer Text 3" );
		
		idToFormField.put( BillConfiguration.HEADER_TEXT_1, "header_" + BillConfiguration.HEADER_TEXT_1 );
		idToFormField.put( BillConfiguration.HEADER_TEXT_2, "header_" + BillConfiguration.HEADER_TEXT_2 );
		
		idToFormField.put( BillConfiguration.FOOTER_TEXT_1, "footer_" + BillConfiguration.FOOTER_TEXT_1 );
		idToFormField.put( BillConfiguration.FOOTER_TEXT_2, "footer_" + BillConfiguration.FOOTER_TEXT_2 );
		idToFormField.put( BillConfiguration.FOOTER_TEXT_3, "footer_" + BillConfiguration.FOOTER_TEXT_3 );
		
		dummyDocumentLocationByModuleAndDocID.put(new Pair<>(ModuleConstants.Module_ID_VPN, BillConstants.DEMAND_NOTE) , "/vpn/bill/dummyDemandNote.jsp" );
		dummyDocumentLocationByModuleAndDocID.put(new Pair<>(ModuleConstants.Module_ID_VPN, BillConstants.POSTPAID) , "/vpn/bill/dummyMonthlyBill.jsp" );
	}
}