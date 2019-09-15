package common;
import java.util.HashMap;
import java.util.Map;



public class ModuleConstants {
	
	public static final int MULTIPLIER = 10000;
	
	public static final int Module_ID_DOMAIN = 1;
	public static final int Module_ID_WEBHOSTING = 2;
	public static final int Module_ID_DNSHOSTING = 3;	
	public static final int Module_ID_COLOCATION = 4;
	public static final int Module_ID_IIG = 5;
	public static final int Module_ID_VPN = 6;
	public static final int Module_ID_LLI = 7;
	public static final int Module_ID_ADSL = 8;
	public static final int Module_ID_NIX = 9;
	public static final int Module_ID_IPADDRESS = 10;
	public static final int Module_ID_UPSTREAM = 11;
	
	public static final int Module_ID_COMPLAIN = 21;//ONLY FOR SUPER USER
	public static final int Module_ID_GENERAL = 100; // This module handles user, client, settings etc
	
	public static final int Module_ID_CLIENT = 101;
	public static final int Module_ID_REQUEST = 110;//ONLY FOR SUPER USER
	
	
	public static final int Module_ID_CRM = 22;
	
	//[R@!h@n]
	private static final String VPN_ROW_HEADING = "Bandwidth";
	private static final String VPN_ROW_UNIT = "MbKM";
	private static final String VPN_COL_HEADING = "Step";
	private static final String VPN_COL_INNER_UNIT = "KM";
	private static final String VPN_COL_OUTER_UNIT = "BDT/MbKM";
	private static final String LLI_ROW_HEADING = "Bandwidth";
	private static final String LLI_ROW_UNIT = "Mbps";
	private static final String LLI_COL_HEADING = "";
	private static final String LLI_COL_INNER_UNIT = "Years";
	private static final String LLI_COL_OUTER_UNIT = "BDT/Mbps";
	
	private static final String[] vpnUnitList = 
	{
		VPN_ROW_HEADING,
		VPN_ROW_UNIT,
		VPN_COL_HEADING,
		VPN_COL_INNER_UNIT,
		VPN_COL_OUTER_UNIT
	};
	private static final String[] lliUnitList = 
	{
		LLI_ROW_HEADING,
		LLI_ROW_UNIT,
		LLI_COL_HEADING, 
		LLI_COL_INNER_UNIT,
		LLI_COL_OUTER_UNIT
	};
	
	
	
	public static Map<Integer, String[] > moduleUnitMap = new HashMap<Integer, String[]>();
	static{
		moduleUnitMap.put(Module_ID_VPN, vpnUnitList);
		moduleUnitMap.put(Module_ID_LLI, lliUnitList);
	}
			
	
	public static Map<Integer, String> ModuleMap = new HashMap<Integer, String>();
	
	
	//	public static HashMap<Integer, Boolean> ModuleStatusMap = new HashMap<Integer, Boolean>();
	static{
		ModuleMap.put(Module_ID_DOMAIN, "Domain");
		ModuleMap.put(Module_ID_WEBHOSTING, "Web Hosting");
		//ModuleMap.put(Module_ID_IPADDRESS, "IP Address");
		ModuleMap.put(Module_ID_COLOCATION, "Colocation");
		ModuleMap.put(Module_ID_CRM, "CRM");
		//ModuleMap.put(Module_ID_IIG, "IIG");
		ModuleMap.put(Module_ID_VPN, "VPN");
		ModuleMap.put(Module_ID_LLI, "LLI");
		ModuleMap.put(Module_ID_ADSL, "ADSL");
		ModuleMap.put(Module_ID_NIX, "NIX");
		ModuleMap.put(Module_ID_DNSHOSTING, "DNS Hosting");
		ModuleMap.put(Module_ID_UPSTREAM, "Upstream");
	}
	
	public static HashMap<Integer, String> mapOfModuleNameToActiveModuleID = new HashMap<Integer, String>();
	static {
		mapOfModuleNameToActiveModuleID.put(Module_ID_VPN, "VPN");
		mapOfModuleNameToActiveModuleID.put(Module_ID_DOMAIN, "Domain");
		mapOfModuleNameToActiveModuleID.put(Module_ID_LLI, "LLI");
	}
	
	public static HashMap<Integer, String> ActiveModuleMap = new HashMap<Integer, String>();
	static {
		ActiveModuleMap.put(Module_ID_DOMAIN, "Domain");
		ActiveModuleMap.put(Module_ID_WEBHOSTING, "Web Hosting");
		ActiveModuleMap.put(Module_ID_IPADDRESS, "IP Address");
		ActiveModuleMap.put(Module_ID_COLOCATION, "Colocation");
		ActiveModuleMap.put(Module_ID_IIG, "IIG");
		ActiveModuleMap.put(Module_ID_VPN, "VPN");
		ActiveModuleMap.put(Module_ID_LLI, "LLI");
		ActiveModuleMap.put(Module_ID_ADSL, "ADSL");
		ActiveModuleMap.put(Module_ID_NIX, "NIX");
		ActiveModuleMap.put(Module_ID_DNSHOSTING, "DNS Hosting");
	}
	/*static {
		ModuleStatusMap.put(Module_ID_DOMAIN, true);
		ModuleStatusMap.put(Module_ID_WEBHOSTING, false);
		ModuleStatusMap.put(Module_ID_IPADDRESS, false);
		ModuleStatusMap.put(Module_ID_COLOCATION, false);
		ModuleStatusMap.put(Module_ID_IIG, false);
		ModuleStatusMap.put(Module_ID_VPN, true);
		ModuleStatusMap.put(Module_ID_LLI, false);
		ModuleStatusMap.put(Module_ID_ADSL, false);
		ModuleStatusMap.put(Module_ID_NIX, false);
		
		ModuleStatusMap.put(Module_ID_COMPLAIN, false);
		ModuleStatusMap.put(Module_ID_GENERAL, true);
		ModuleStatusMap.put(Module_ID_CLIENT, false);
		ModuleStatusMap.put(Module_ID_REQUEST, false);
	}*/


	public static final int SYSTEM_APPLY_CLIENT_REG = 5001;
	public static final int CLIENT_APPLY_CLIENT_REG = 5009;


}
