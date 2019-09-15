package lli.constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EndPointConstants {
	public static final int NEAR_END_TYPE = 1;
	public static final int FAR_END_TYPE = 2;
	
	
	public static final int FIBRE_TYPE_LLC=1;
	public static final int FIBRE_TYPE_DARK=2;
	
	
	public static final int SERVICE_PROVIDER_BUY=1;
	public static final int SERVICE_PROVIDER_RENT=2;
	
	public static final int OFC_PROVIDER_BTCL = 3;
	
	public static final HashMap<Integer, String> fibreType = new HashMap<Integer, String>();
	static
	{
		fibreType.put(FIBRE_TYPE_LLC, "LLC");
		fibreType.put(FIBRE_TYPE_DARK, "Dark");
	}
	
	public static final int OFC_BTCL=1;
	public static final int OFC_CUSTOMER=2;
	
	public static final int TDC_BTCL=1;
	public static final int TDC_CUSTOMER=2;
	
	public static final HashMap<Integer, String> LaidOFCby = new HashMap<Integer, String>();
	static
	{
		//LaidOFCby.put(OFC_BTCL, "BTCL");
		LaidOFCby.put(OFC_CUSTOMER, "Customer");
	}
	
	public static final HashMap<Integer, String> providerOfOFC = new HashMap<Integer, String>();
	static
	{
		providerOfOFC.put(OFC_BTCL, "BTCL");
		providerOfOFC.put(OFC_CUSTOMER, "Client");
	}
	
	public static final HashMap<Integer, String> terminalDeviceProvider = new HashMap<Integer, String>();
	static
	{
		terminalDeviceProvider.put(TDC_BTCL, "BTCL");
		terminalDeviceProvider.put(TDC_CUSTOMER, "Client");
	}
	
	public static final int CONNECTION_TYPE_PERMANENT_=1;
	public static final int CONNECTION_TYPE_TEMPORARY_=2;
	public static final int CONNECTION_TYPE_SHORT_TIME_=3;
	public static final int CONNECTION_TYPE_FIVE_YEARS_ = 5;
	
	public static final HashMap<Integer, String> connectionType = new HashMap<Integer, String>();
	public static final HashMap<Integer, Integer> yearConnectionTypeMapping = new HashMap<Integer, Integer>();
	
	static
	{
		connectionType.put(CONNECTION_TYPE_PERMANENT_, "Regular");
		//connectionType.put(CONNECTION_TYPE_TEMPORARY_, "Temporary");
		//connectionType.put(CONNECTION_TYPE_FIVE_YEARS_, "Long Time (5 years)");
		
		yearConnectionTypeMapping.put( CONNECTION_TYPE_PERMANENT_ , 4 );
		yearConnectionTypeMapping.put( CONNECTION_TYPE_FIVE_YEARS_ , 5 );
		yearConnectionTypeMapping.put( CONNECTION_TYPE_SHORT_TIME_ , 4 );
		yearConnectionTypeMapping.put( CONNECTION_TYPE_TEMPORARY_ , 4 );
	}
	
	public static final int CORE_TYPE_SINGLE=1;
	public static final int CORE_TYPE_DUAL=2;
	public static final int CORE_TYPE_MULTI=3;
	
	public static final HashMap<Integer, String> coreTypeMap = new HashMap<Integer, String>();
	static
	{
		coreTypeMap.put(CORE_TYPE_SINGLE, "Single");
		coreTypeMap.put(CORE_TYPE_DUAL, "Dual");
	}
	
	public static final int LAYER_TYPE_2=1;
	public static final int LAYER_TYPE_3=2;
	public static final HashMap<Integer, String> layerTypeMap = new HashMap<Integer, String>();
	static
	{
		layerTypeMap.put(LAYER_TYPE_2, "Layer 2");
		layerTypeMap.put(LAYER_TYPE_3, "Layer 3");
	}
	
	/*
	public static final int PORT_TYPE_FE=8002;
	public static final int PORT_TYPE_GE=8003;
	public static final int PORT_TYPE_10GE=8004;
	public static final HashMap<Integer, String> portCategoryMap = new HashMap<Integer, String>();
	static
	{
		portCategoryMap.put(PORT_TYPE_FE, "Port Type FE");
		portCategoryMap.put(PORT_TYPE_GE, "Port Type GE");
		portCategoryMap.put(PORT_TYPE_10GE, "Port Type 10GE");
	}
	*/
	
	public static final int TERMINAL_DEVICE=10000;
	public static final int MEDIA_CONVERTER=10001;
	
	public static Set<Integer> devices = new HashSet<Integer>() ;
	static
	{
		devices.add(TERMINAL_DEVICE);
		devices.add(MEDIA_CONVERTER);
	}
	
}
