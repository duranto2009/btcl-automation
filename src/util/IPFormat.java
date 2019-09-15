package util;

public class IPFormat {
	public static int BYTE_LENGTH=8;

	public static long IPToInt(String ipAddr){
		long ip=-1;
		String[] addrArray = ipAddr.split("\\.");
		if(addrArray.length != 4)
			return -1;
        for (int i=0;i<addrArray.length;i++) {
            int power = 3-i;
            long ipFr= Long.parseLong(addrArray[i]);
            if( ipFr<0 || ipFr > 255)
            	return -1;
            ip += (((ipFr & 0xFF) << BYTE_LENGTH*power));
        }
		return ip;
	}
	
	public static String intToIP(long ip){
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." +(ip & 0xFF);
	}
		
}
