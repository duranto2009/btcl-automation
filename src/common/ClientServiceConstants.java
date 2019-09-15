package common;

import java.util.HashMap;

public class ClientServiceConstants {
	
	public static final int MULTIPLIER = 10000;
	
	public static final int CLIENT_SERVICE_PRODUCT = 1;
	public static final int CLIENT_SERVICE_DOMAIN = 2;
	public static final int CLIENT_SERVICE_WEB_HOSTING = 3;
	public static final int CLIENT_SERVICE_COLOCATION = 4;
	public static final int CLIENT_SERVICE_IIG= 5;
	public static final int CLIENT_SREVICE_VPN = 6;
	public static final int CLIENT_SREVICE_LLI = 7;
	
	public static HashMap<Integer, String> ClientServiceMap = new HashMap<Integer, String>();
	
	static{
		ClientServiceMap.put(1, "Products/Services");
		ClientServiceMap.put(2, "Domains");
		ClientServiceMap.put(3, "Webhosting");
		ClientServiceMap.put(4, "Colocation");
		ClientServiceMap.put(5, "IIG");
		ClientServiceMap.put(6, "VPN");
		ClientServiceMap.put(7, "LLI");
	}
}
