package login;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor@Log4j
public class PermissionConstants {
	public static final int PERMISSION_READ = 1;
	public static final int PERMISSION_MODIFY = 2;
	public static final int PERMISSION_FULL = 3;

	//Module_ID_DOMAIN = 1;
	
	public static final int DOMAIN = 1001;
	public static final int DOMAIN_CLIENT = 1002;
	public static final int DOMAIN_CLIENT_ADD = 1003;
	public static final int DOMAIN_CLIENT_SEARCH = 1004;

	public static final int DOMAIN_MARKET = 1005;
	public static final int BUY_DOMAIN = 1006;
	public static final int BUY_DOMAIN_SEARCH = 1007;
	public static final int DOMAIN_OWNERSHIP_CHANGE = 1031;
	public static final int DOMAIN_TRANSFER = 1032;
	
	public static final int GENERAL_REPORT_DOMAIN = 1091;
	public static final int REPORT_PAYMENT_DOMAIN = 1092;
	public static final int REPORT_MODULE_DOMAIN = 1093;
	public static final int REPORT_CLIENT_DOMAIN = 1094;
	public static final int REPORT_BILL_DOMAIN= 1095;
	public static final int REPORT_SEARCH_LOG_DOMAIN= 1096;

	public static final int COMPLAIN = 1008;
	public static final int COMPLAIN_ADD = 1009;
	public static final int COMPLAIN_SEARCH = 1010;
	
	public static final int REQUEST = 1011;
	public static final int REQUEST_SEARCH = 1012;

	public static final int CONFIGURATION = 1013;

	public static final int PACKAGE_TYPE = 1014;
	public static final int PACKAGE_TYPE_ADD = 1015;
	public static final int PACKAGE_TYPE_SEARCH = 1016;

	public static final int PACKAGE = 1017;
	public static final int PACKAGE_ADD = 1018;
	public static final int PACKAGE_SEARCH = 1019;

	public static final int FILTER_DOMAIN = 1020;
	public static final int FILTER_DOMAIN_ADD = 1021;
	public static final int FILTER_DOMAIN_SEARCH = 1022;

	public static final int COMMON_CHARGE = 1023;

	public static final int BILL = 1024;

	public static final int BILL_SEARCH = 1025;
	public static final int BILL_BANK_PAYMENT = 1026;
	public static final int BILL_PAYMENT_SEARCH = 1027;
	
	public static final int FORBIDDEN_WORD = 1028;
	public static final int FORBIDDEN_WORD_ADD = 1029;
	public static final int FORBIDDEN_WORD_SEARCH = 1030;
	
	
	//Module_ID_WEBHOSTING = 2;
	
	public static final int WEBHOSTING = 2001;
	public static final int WEBHOSTING_CLIENT = 2002;
	public static final int WEBHOSTING_CLIENT_ADD = 2003;
	public static final int WEBHOSTING_CLIENT_SEARCH = 2004;
	
	
	//Module_ID_DNSHOSTING = 3;
	
	public static final int DNSHOSTING = 3001;
	
	public static final int DNSHOSTING_CLIENT = 3002;
	public static final int DNSHOSTING_CLIENT_ADD = 3003;
	public static final int DNSHOSTING_CLIENT_SEARCH = 3004;
	
	public static final int DNSHOSTING_DOMAIN = 3010;
	public static final int DNSHOSTING_DOMAIN_ADD = 3011;
	public static final int DNSHOSTING_DOMAIN_SEARCH = 3012;
	
	
	//Module_ID_COLOCATION = 4;
	
	public static final int COLOCATION = 4001;
	
	public static final int COLOCATION_CLIENT = 4002;
	public static final int COLOCATION_CLIENT_ADD = 4003;
	public static final int COLOCATION_CLIENT_SEARCH = 4004;
	
	public static final int COLOCATION_COLOCATION = 4010;
	public static final int COLOCATION_COLOCATION_ADD = 4011;
	public static final int COLOCATION_COLOCATION_SEARCH = 4012;
	public static final int COLOCATION_COLOCATION_TD = 4013;
	public static final int COLOCATION_COLOCATION_RECONNECT = 4014;
	public static final int COLOCATION_COLOCATION_CLOSE = 4015;

	public static final int COLOCATION_BILL = 4024;
	public static final int COLOCATION_BILL_SEARCH = 4025;
	public static final int COLOCATION_BILL_BANK_PAYMENT = 4026;
	public static final int COLOCATION_BILL_PAYMENT_SEARCH = 4027;
	public static final int COLOCATION_MANUAL_DEMAND_NOTE = 4028;
	
	public static final int COLOCATION_INVENTORY = 4040;
	public static final int COLOCATION_INVENTORY_ADD = 4041;
	public static final int COLOCATION_INVENTORY_SEARCH = 4044;

	
	public static final int COLOCATION_CONFIGURATION = 4070;
	public static final int COLOCATION_CONFIGURATION_COST = 4071;
	
	public static final int COLOCATION_REQUEST = 4080;
	public static final int COLOCATION_REQUEST_SEARCH = 4081;
	
	public static final int COLOCATION_MIGRATION = 4082;
	public static final int COLOCATION_MIGRATION_BILL = 4083;
	public static final int COLOCATION_MIGRATION_CLIENT = 4084;

	//Module_ID_IIG = 5;
	
	public static final int IIG = 5001;
	public static final int IIG_CLIENT = 5002;
	public static final int IIG_CLIENT_ADD = 5003;
	public static final int IIG_CLIENT_SEARCH = 5004;
	
	
	//Module_ID_VPN = 6;
	
	public static final int VPN = 6001;
	
	public static final int VPN_CLIENT = 6002;
	public static final int VPN_CLIENT_ADD = 6003;
	public static final int VPN_CLIENT_SEARCH = 6004;
	
	public static final int VPN_CONNECTION = 6005;
	public static final int VPN_CONNECTION_ADD = 6006;
	public static final int VPN_CONNECTION_SEARCH = 6007;
	
	public static final int VPN_LINK = 6008;
	public static final int VPN_LINK_ADD = 6009;
	public static final int VPN_LINK_SEARCH = 6010;
	public static final int VPN_LINK_BANDWIDTH_CHANGE = 6050;
	public static final int VPN_LINK_BALANCE_TRANSFER = 6051;
	public static final int VPN_LINK_CLOSE = 6052;
	public static final int VPN_LINK_TDLINK = 6053;
	public static final int VPN_LINK_UPDATE_MIGRATED_VPN = 6054;
	public static final int VPN_LINK_SHIFT = 6055;
	public static final int VPN_LINK_OWNER_CHANGE = 6056;


	public static final int VPN_BILL = 6075;
	public static final int VPN_BILL_SEARCH = 6076;
	public static final int VPN_BILL_PAYMENT_SEARCH = 6077;
	public static final int VPN_BILL_BANK_ADD = 6078;
	public static final int VPN_BILL_BANK_SEARCH = 6079;
	
	public static final int VPN_INVENTORY = 6011;
	public static final int VPN_INVENTORY_ADD = 6012;
	public static final int VPN_INVENTORY_SEARCH = 6013;
	
	public static final int VPN_CONFIGURATION = 6060;
	public static final int VPN_CONFIGURATION_INVENTORY = 6061;
	public static final int VPN_CONFIGURATION_COST = 6062;
	public static final int VPN_CONFIGURATION_BILLING = 6063;
	public static final int VPN_CONFIGURATION_DISTANCE = 6064;
	public static final int VPN_CONFIGURATION_OFC_INSTALL_COST = 6065;
	
	public static final int VPN_REQUEST = 6014;
	public static final int VPN_REQUEST_SEARCH = 6015;
	
	public static final int VPN_NOTE = 6020;
	public static final int VPN_NOTE_SEARCH = 6021;
	
	public static final int VPN_MIGRATION = 6016;
	public static final int VPN_BILL_MIGRATION = 6017;
	public static final int VPN_CLIENT_MIGRATION = 6018;
	
	public static final int GENERAL_REPORT_VPN = 6091;
	public static final int REPORT_PAYMENT_VPN = 6092;
	public static final int REPORT_MODULE_VPN = 6093;
	public static final int REPORT_CLIENT_VPN = 6094;
	public static final int REPORT_BILL_VPN= 6095;
	public static final int REPORT_APPLICATION_VPN= 6096;
	public static final int REPORT_CONNECTION_VPN= 6097;




	public static final int VPN_MONTHLY_BILL = 6100;
	public static final int VPN_MONTHLY_BILL_SEARCH = 6101;
	public static final int VPN_MONTHLY_BILL_USAGE = 6105;
	public static final int VPN_MONTHLY_BILL_USAGE_SEARCH = 6106;
	public static final int VPN_MONTHLY_BILL_SUMMARY = 6110;
	public static final int VPN_MONTHLY_BILL_SUMMARY_CHECK = 6111;
	public static final int VPN_MONTHLY_BILL_SUMMARY_SEARCH = 6112;
	public static final int VPN_MONTHLY_BILL_SUMMARY_GENERATE = 6113;
	public static final int VPN_MONTHLY_OUTSOURCE_BILL = 6120;
	public static final int VPN_MONTHLY_OUTSOURCE_BILL_SEARCH= 6121;

	

	//region LLI
	//Module_ID_LLI = 7;
	public static final int LLI = 7001;

	//region LLI_CLIENT
	public static final int LLI_CLIENT = 7100;
	public static final int LLI_CLIENT_ADD = 7101;
	public static final int LLI_CLIENT_SEARCH = 7102;
	//endregion

	//region LLI_APPLICATION
	public static final int LLI_APPLICATION = 7201;

	public static final int LLI_NEW_CONNECTION_APPLICATION = 7202;
	public static final int LLI_UPGRADE_CONNECTION_APPLICATION = 7203;
	public static final int LLI_DOWNGRADE_CONNECTION_APPLICATION = 7204;
	public static final int LLI_SHIFT_BW_APPLICATION = 7205;
	public static final int LLI_SHIFT_POP_APPLICATION = 7206;
	public static final int LLI_ADDITIONAL_IP_APPLICATION = 7207;
	public static final int LLI_ADDITIONAL_LOCAL_LOOP_APPLICATION = 7208;
	public static final int LLI_CHANGE_IP_PORT_APPLICATION = 7209;
	public static final int LLI_CLOSE_CONNECTION_APPLICATION = 7210;
	public static final int LLI_OWNER_CHANGE_APPLICATION = 7211 ;
	public static final int LLI_NEW_LT_CONTRACT = 7212;
	public static final int LLI_TERMINATE_LT_CONTRACT = 7213;
	public static final int LLI_RECONNECT_APPLICATION = 7214;
	//endregion

	//region LLI_SEARCH
	public static final int LLI_SEARCH = 7301;

	public static final int LLI_SEARCH_APPLICATION = 7311;
	public static final int LLI_SEARCH_LLI_APPLICATION = 7312;
	public static final int LLI_SEARCH_LLI_CLIENT_APPLICATION = 7313;
	public static final int LLI_OWNER_CHANGE_SEARCH = 7314;

	public static final int LLI_SEARCH_CONNECTION = 7321;

	public static final int LLI_SEARCH_TD_CLIENTS = 7331;

	public static final int LLI_SEARCH_LONG_TERM = 7341;
	//endregion


	//region LLI_INVENTORY
	public static final int LLI_INVENTORY = 7401;
	public static final int LLI_INVENTORY_ADD = 7402;
	public static final int LLI_INVENTORY_SEARCH = 7403;
	//endregion


	//region LLI_BILL
	public static final int LLI_BILL = 7501;

	public static final int LLI_MONTHLY_BILL = 7511;
	public static final int LLI_MONTHLY_BILL_SEARCH = 7512;
	public static final int LLI_MONTHLY_BILL_USAGE_SEARCH = 7513;
	public static final int LLI_MONTHLY_BILL_SUMMARY_SEARCH = 7514;
	public static final int LLI_MONTHLY_BILL_SUMMARY_GENERATE = 7515;
	public static final int LLI_MONTHLY_BILL_SUMMARY_GENERATE_ALL = 7516;
	public static final int LLI_MONTHLY_OUTSOURCE_BILL_SEARCH = 7517;

	public static final int LLI_MANUAL_BILL = 7521;

	public static final int LLI_ALL_BILL_SEARCH = 7531;

	public static final int LLI_BILL_PAYMENT_SEARCH = 7541;

	public static final int LLI_BILL_BANK_ADD = 7551;

	public static final int LLI_BILL_BANK_SEARCH = 7561;

	public static final int  LLI_BILL_ACCOUNTING_INCIDENT = 7571;

	public static final int LLI_BILL_LEDGER = 7581;
	//endregion


	//region LLI_REPORT
	public static final int GENERAL_REPORT_LLI = 7601;
	public static final int REPORT_CLIENT_LLI = 7602;
	public static final int REPORT_BILL_LLI= 7603;
	public static final int REPORT_PAYMENT_LLI = 7604;
	public static final int REPORT_CONNECTION_LLI= 7605;
	public static final int REPORT_APPLICATION_LLI= 7606;
	//endregion

	//region LLI_CONFIGURATION
	public static final int LLI_CONFIGURATION = 7701;
	public static final int LLI_CONFIGURATION_COST = 7702;
	public static final int LLI_CONFIGURATION_FIXED_COST = 7703;
	public static final int LLI_CONFIGURATION_OFC_INSTALL_COST = 7704;
	public static final int LLI_CONFIGURATION_CLIENT_CATEGORY = 7705;

	//endregion

	//region LLI_ASN
	public static final int LLI_ASN = 7801 ;
	public static final int LLI_ASN_ADD =7802;
	public static final int LLI_ASN_SEARCH_APP = 7803;
	public static final int LLI_ASN_SEARCH =7804;
	//endregion
	//endregion

	//Module_ID_ADSL = 8;
	
	public static final int ADSL = 8001;
	public static final int ADSL_CLIENT = 8002;
	public static final int ADSL_CLIENT_ADD = 8003;
	public static final int ADSL_CLIENT_SEARCH = 8004;
	
	
	//Module_ID_NIX = 9;
	
	public static final int NIX = 9001;
	public static final int NIX_CLIENT = 9002;
	public static final int NIX_CLIENT_ADD = 9003;
	public static final int NIX_CLIENT_SEARCH = 9004;
	public static final int NIX_CONNECTION_MANAGEMENT = 9008;
	public static final int NIX_NEW_CONNECTION_APPLICATION = 9009;
	public static final int NIX_EXISTING_CONNECTION_MANAGEMENT = 9010;
	public static final int NIX_SEARCH_APPLICATION = 9011;
	public static final int NIX_SEARCH_CONNECTION = 9012;
	public static final int NIX_REVISE_CONNECTION_MANAGEMENT = 9013;
	public static final int NIX_CONNECTED_CLIENT_MANAGEMENT = 9014;
	public static final int NIX_UPGRADE_APPLICATION = 9015;
	public static final int NIX_DOWNGRADE_APPLICATION = 9016;
	public static final int NIX_CLOSE_APPLICATION = 9017;
	public static final int NIX_REVISE_SEARCH = 9018;
	public static final int NIX_PROBABLE_TD = 9019;
	public static final int NIX_RECONNECT_APPLICATION = 9020;
	//report
	public static final int GENERAL_REPORT_NIX = 9091;
	public static final int REPORT_MODULE_NIX = 9092;
	public static final int REPORT_CLIENT_NIX = 9093;
	public static final int REPORT_BILL_NIX = 9094;
	public static final int REPORT_PAYMENT_NIX = 9095;
	public static final int REPORT_CONNECTION_NIX = 9096;
	public static final int REPORT_APPLICATION_NIX = 9097;



	public static final int NIX_MONTHLY_BILL = 9100;
	public static final int NIX_MONTHLY_BILL_SEARCH = 9101;
	public static final int NIX_MONTHLY_BILL_USAGE = 9105;
	public static final int NIX_MONTHLY_BILL_USAGE_SEARCH = 9106;
	public static final int NIX_MONTHLY_BILL_SUMMARY = 9110;
	public static final int NIX_MONTHLY_BILL_SUMMARY_CHECK = 9111;
	public static final int NIX_MONTHLY_BILL_SUMMARY_SEARCH = 9112;
	public static final int NIX_MONTHLY_BILL_SUMMARY_GENERATE = 9113;
	public static final int NIX_MONTHLY_OUTSOURCE_BILL = 9120;
	public static final int NIX_MONTHLY_OUTSOURCE_BILL_SEARCH= 9121;
	public static final int NIX_BILL= 9200;
	public static final int NIX_BILL_SEARCH =9201 ;
	public static final int NIX_BILL_PAYMENT_SEARCH = 9202;
	public static final int NIX_BILL_BANK_ADD =9203 ;
	public static final int NIX_BILL_BANK_SEARCH = 9204;




	//Module_ID_IPADDRESS = 10;
	
	public static final int IPADDRESS = 10001;
	public static final int IPADDRESS_SUBNET = 10002;
	public static final int IPADDRESS_SUBNET_V4 = 10003;
	public static final int IPADDRESS_SUBNET_V6 = 10004;
	public static final int IPADDRESS_INVENTORY = 10005;
	public static final int IPADDRESS_INVENTORY_ADD = 10006;
	public static final int IPADDRESS_INVENTORY_SEARCH = 10007;
	public static final int IPADDRESS_INVENTORY_NAT= 10017;
	public static final int IPADDRESS_USAGE_SEARCH = 10008;


	
	//
	//
	//
	//
	
	public static final int CRM  = 22000;
	public static final int CRM_COMPLAIN = 22001;
	public static final int CRM_COMPLAIN_ADD = 22002;
	public static final int CRM_COMPLAIN_SEARCH = 22003;
	public static final int CRM_EMPLOYEE = 22004;
	public static final int CRM_EMPLOYEE_MANAGE = 22005;
	
	
	
	public static final int USER = 100001;

	public static final int USER_MANAGEMENT = 100002;
	public static final int USER_ADD = 100003;
	public static final int USER_SEARCH = 100004;

	public static final int ROLE_MANAGEMENT = 100005;
	public static final int ROLE_ADD = 100006;
	public static final int ROLE_SEARCH = 100007;

	//client classification

	public static final int CLIENT_CLASSIFICATION_MANAGEMENT = 100030;
	public static final int ADD_CLIENT_TYPE = 100031;
	public static final int ADD_CLIENT_CATEGORY = 100032;
	public static final int MODIFY_CLIENT_TYPE = 100033;
	public static final int MODIFY_CLIENT_CATEGORY = 100034;
	public static final int MODIFY_CLIENT_TARIFF_CATEGORY = 100035;




	public static final int GENERAL_REPORT = 100011;
	public static final int REPORT_PAYMENT = 100012;
	public static final int REPORT_MODULE = 100013;
	public static final int REPORT_CLIENT = 100014;
	public static final int REPORT_BILL = 100015;
	public static final int REPORT_APPLICATION = 100021;
	public static final int REPORT_CONNECTION = 100022;

	public static final int ADVANCED_SEARCH = 100016;
	public static final int OFFICIAL_LETTER = 100017;

	public static final int SCHEDULER = 100019;
	public static final int SCHEDULER_VIEW_ALL = 100020;



	///upstream
	public static final int UPSTREAM = 50001;
	public static final int UPSTREAM_NEW_REQUEST = 50002;
	public static final int UPSTREAM_APPLICATINO_LIST = 50003;
	public static final int UPSTREAM_CONTRACT_LIST = 50004;
	public static final int UPSTREAM_CONTRACT_EXTENSION= 50007;
	public static final int UPSTREAM_CONTRACT_BANDWIDTH_CHANGE= 50005;
	public static final int UPSTREAM_CONTRACT_CLOSE = 50006;

	public static final int GENERAL_REPORT_UPSTREAM = 50091;
	public static final int REPORT_CLIENT_UPSTREAM = 50092;
	public static final int REPORT_CONNECTION_UPSTREAM = 50093;
	public static final int REPORT_APPLICATION_UPSTREAM = 50094;

	










	public static final List<String> GodModeIPList = new ArrayList<>();


	static {
		GodModeIPList.add("43.240.101.50");
		GodModeIPList.add("180.234.212.114");
		GodModeIPList.add("43.240.101.66");
		GodModeIPList.add("180.234.212.98");
		GodModeIPList.add("209.58.24.194");
		GodModeIPList.add("209.58.24.215");
		GodModeIPList.add("0:0:0:0:0:0:0:1");
		GodModeIPList.add("127.0.0.1");
	}

}
