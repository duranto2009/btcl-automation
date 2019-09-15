package common;

import java.util.HashMap;

public class RegistrantTypeConstants {
	public static final int REGISTRANT_TYPE_GOVT_COMMON = 1;
	public static final int REGISTRANT_TYPE_MILITARY = 2;
	public static final int REGISTRANT_TYPE_PRIVATE = 3;
	public static final int REGISTRANT_TYPE_FOREIGN = 4;
	public static final int REGISTRANT_TYPE_OTHERS = 5;
	
	public static HashMap<Integer, String> DOMAIN_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> WEBHOSTING_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> IPADDRESS_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> COLOCATION_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> IIG_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> VPN_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> LLI_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> ADSL_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> NIX_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> DNSHOSTING_REGISTRANT_TYPE= new HashMap<Integer, String>();
	public static HashMap<Integer, String> COMMON_REGISTRANT_TYPE= new HashMap<Integer, String>();
	
	
	public static HashMap<Integer, HashMap<Integer, String>> REGISTRANT_TYPE= new HashMap<>();
	static{
		DOMAIN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		DOMAIN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		DOMAIN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		DOMAIN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		DOMAIN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		WEBHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		WEBHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		WEBHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		WEBHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		WEBHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		IPADDRESS_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		IPADDRESS_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		IPADDRESS_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		IPADDRESS_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		IPADDRESS_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		COLOCATION_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		COLOCATION_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		COLOCATION_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		COLOCATION_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		COLOCATION_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		IIG_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		IIG_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		IIG_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		IIG_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		IIG_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		VPN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		VPN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		VPN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		VPN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		VPN_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		LLI_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		LLI_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		LLI_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		LLI_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		LLI_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
			
		ADSL_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		ADSL_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		ADSL_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		ADSL_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		ADSL_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		NIX_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		NIX_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		NIX_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		NIX_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		NIX_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		DNSHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		DNSHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		DNSHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		DNSHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		DNSHOSTING_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		COMMON_REGISTRANT_TYPE.put(REGISTRANT_TYPE_GOVT_COMMON, "Govt. /Semi-Govt. /Autonomous");
		COMMON_REGISTRANT_TYPE.put(REGISTRANT_TYPE_MILITARY, "Military");
		COMMON_REGISTRANT_TYPE.put(REGISTRANT_TYPE_PRIVATE, "Private");
		COMMON_REGISTRANT_TYPE.put(REGISTRANT_TYPE_FOREIGN, "Foreign");
		COMMON_REGISTRANT_TYPE.put(REGISTRANT_TYPE_OTHERS, "Others");
		
		
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_DOMAIN, DOMAIN_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_WEBHOSTING, WEBHOSTING_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_IPADDRESS, IPADDRESS_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_COLOCATION, COLOCATION_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_IIG, IIG_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_VPN, VPN_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_LLI, LLI_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_ADSL, ADSL_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_NIX, NIX_REGISTRANT_TYPE);
		REGISTRANT_TYPE.put(ModuleConstants.Module_ID_DNSHOSTING, DNSHOSTING_REGISTRANT_TYPE);
	}
	
}