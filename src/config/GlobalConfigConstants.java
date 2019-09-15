package config;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfigConstants {
	public static final int MODULE_DOMAIN = 1;
	public static final int MODULE_WEBHOSTING = 2;
	public static final int MODULE_DNSHOSTING = 3;
	public static final int MODULE_COLOCATION = 4;
	public static final int MODULE_IIG = 5;
	public static final int MODULE_VPN = 6;
	public static final int MODULE_LLI = 7;
	public static final int MODULE_ADSL = 8;
	public static final int MODULE_NIX = 9;	
	public static final int MODULE_IPADDRESS = 10;
	
	
	public static final int MODULE_COMPLAIN = 21;	
	public static final int Module_ID_CRM = 22;
	
	public static final int MIGRATION_ENABLED = 100001;

	public static Map<String, String> contextMap = new HashMap<>();
	
	
	public static boolean migrationEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MIGRATION_ENABLED).getValue() > 0;
}
