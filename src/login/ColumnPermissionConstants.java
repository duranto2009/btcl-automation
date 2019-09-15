package login;

import java.util.HashMap;

import common.ModuleConstants;

public class ColumnPermissionConstants {
 
 /************DOMAIN RELATED COLUMN PERMISSIONS*****************/
	public static class DOMAIN
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 1;
		public static final int CAN_TERMINATE_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 2;
		public static final int GET_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 3;
		public static final int VIEW_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 4;
		public static final int CHECK_DOMAIN_AVAILABILITY = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 5;
		public static final int CHECKED_DOMAIN_BUY = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 7;
		public static final int EDIT_ACTIVE_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 6;
		public static final int UPDATE_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 102;
		public static final int CHANGE_PRIVILEGE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 9;
		public static final int DELETE_FORBIDDEN_WORD = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 10;
		public static final int CHECK_OWNERSHIP = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 11;
		public static final int OWNERSHIP_REQUEST = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 12;
		public static final int ADD_DOMAIN_PACKAGE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 13;
		public static final int REMOVE_PACKAGE_TYPE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 14;
		public static final int UPDATE_DOMAIN_PACKAGE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 15;
		public static final int TRANSFER_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 16;
		public static final int CHECK_DOMAIN_TRANSFER = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 17;

		public static final int ADD_DOMAIN_USING_FILE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 18;
		public static final int ADD_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 19;
		public static final int ADD_FORBIDDEN_WORD_USING_FILE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 20;
		public static final int ADD_FORBIDDEN_WORD = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 21;
		public static final int DOMAIN_CHARGE_CONFIG = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 22;
		public static final int GET_DOMAIN_CHARGE_CONFIG = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 23;
		public static final int DELETE_DOMAIN = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 24;

		public static final int CHANGE_RENEW_BLOCK = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 25;
		
		public static final int DOMAIN_RENEW_BLOCK = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 26;
		public static final int DOMAIN_RENEW_UNBLOCK = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 27;

		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 100;
		public static final int BTCL_PERSONEL = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 101;
		
		public static final int CLIENT_UPDATE = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 5002;
		
		public static final int APPROVE_PAYMENT = ModuleConstants.Module_ID_DOMAIN * ModuleConstants.MULTIPLIER + 115;
	}
 /************DOMAIN RELATED COLUMN PERMISSIONS END*****************/
 
 
 /************VPN RELATED COLUMN PERMISSIONS START*****************/
	public static class VPN
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 100;
		public static final int BTCL_PERSONNEL = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 101;
		
		public static final int LINK_UPDATE = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 102;
		
		public static final int VERIFY_PAYMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 114;
		public static final int APPROVE_PAYMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 115;
		public static final int SKIP_DEMAND_NOTE_PAYMENT = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 194;
		
		public static final int CLIENT_UPDATE = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 5002;
		public static final int VIEW_CLIENT_SUMMARY = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 5006;
		public static final int VIEW_CLIENT_DETAILS = ModuleConstants.Module_ID_VPN * ModuleConstants.MULTIPLIER + 5007;
	}
 /************VPN RELATED COLUMN PERMISSIONS END*****************/
	public static class LLI
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 100;
		public static final int BTCL_PERSONNEL = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 101;
		
		public static final int LINK_UPDATE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 102;
		
		public static final int VERIFY_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 114;
		public static final int APPROVE_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 115;
		public static final int SKIP_DEMAND_NOTE_PAYMENT = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 194;
		
		public static final int CLIENT_UPDATE = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5002;
		public static final int VIEW_CLIENT_SUMMARY = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5006;
		public static final int VIEW_CLIENT_DETAILS = ModuleConstants.Module_ID_LLI * ModuleConstants.MULTIPLIER + 5007;
	}
	public static class WEBHOSTING
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_WEBHOSTING * ModuleConstants.MULTIPLIER + 100;
	}
	public static class DNSHOSTING
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_DNSHOSTING * ModuleConstants.MULTIPLIER + 100;
	}	
	public static class COLOCATION
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_COLOCATION * ModuleConstants.MULTIPLIER + 100;
	}
	public static class NIX
	{
		public static final int VIEW_PAGE_NOT_RESTRICTED = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 1;
		public static final int REQUEST_SEARCH_NOT_BOUND_TO_SELF = ModuleConstants.Module_ID_NIX * ModuleConstants.MULTIPLIER + 100;
	}


 
 public static HashMap<Integer, Integer> moduleViewPageRestrictionMap = new HashMap<Integer, Integer>();
 static
 {
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_DOMAIN, DOMAIN.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_WEBHOSTING, WEBHOSTING.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_COLOCATION, COLOCATION.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_VPN, VPN.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_LLI, LLI.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_DNSHOSTING, DNSHOSTING.VIEW_PAGE_NOT_RESTRICTED);
  moduleViewPageRestrictionMap.put(ModuleConstants.Module_ID_NIX, NIX.VIEW_PAGE_NOT_RESTRICTED);

 }
}