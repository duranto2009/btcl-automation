package config;

public class GlobalConfigModuleStatusDTO {	
		public static boolean moduleDomainEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_DOMAIN).getValue() > 0;
		public static boolean moduleWebHostingEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_WEBHOSTING).getValue() > 0;
		public static boolean moduleIpaddressEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_IPADDRESS).getValue() > 0;
		public static boolean moduleColocationEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_COLOCATION).getValue() > 0;
		public static boolean moduleIigEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_IIG).getValue() > 0;
		public static boolean moduleVPNEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_VPN).getValue() > 0;
		public static boolean moduleLliEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_LLI).getValue() > 0;
		public static boolean moduleAdslEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_ADSL).getValue() > 0;
		public static boolean moduleNixEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_NIX).getValue() > 0;
		public static boolean moduleDnshostingEnabled = GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MODULE_DNSHOSTING).getValue() > 0;
	
}
