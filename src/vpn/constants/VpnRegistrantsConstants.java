package vpn.constants;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.LongSeqHelper;

import request.StateRepository;

public class VpnRegistrantsConstants {

	private  static final int MASK=(byte)0x01;
	private  static final int LONG_SIZE=8;
	private static final int MAXIMUM_TYPE=4;
	
	public static final int POSITIVE_LONG = MAXIMUM_TYPE * 8;
	//public static final int REG_TYPE_IGW = 0;
	//public static final int REG_TYPE_ICX = 1;
	public static final int REG_TYPE_IIG = 1;
	public static final int REG_TYPE_ISP = 2;
	public static final int REG_TYPE_PROJECT = 3;
	public static final int REG_TYPE_CORPORATE_BODY = 4;
	public static final int REG_TYPE_OTHERS = POSITIVE_LONG - 2;
	
	
	
	public static final HashMap<Integer, String> vpnRegTypeMap = new HashMap<Integer, String>();
	static
	{
		//vpnRegTypeMap.put(REG_TYPE_IGW, "IGW");
		//vpnRegTypeMap.put(REG_TYPE_ICX, "ICX");
		vpnRegTypeMap.put(REG_TYPE_IIG, "IIG");
		vpnRegTypeMap.put(REG_TYPE_ISP, "ISP");
		vpnRegTypeMap.put(REG_TYPE_PROJECT, "Project");
		vpnRegTypeMap.put(REG_TYPE_CORPORATE_BODY, "Corporate Body");
		vpnRegTypeMap.put(REG_TYPE_OTHERS, "Others");
	}
	
	
}
