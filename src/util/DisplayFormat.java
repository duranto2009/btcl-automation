

package util;


public class DisplayFormat
{
	public static String EMPTY_STRING = "";
	
    public DisplayFormat()
    {
    }

    public static String getGatewayDisplayString(String ip, String desc)
    {
        if(ip == null && desc == null)
            return EMPTY_STRING;
        if(ip == null || ip.trim().length() == 0)
            return desc;
        if(desc == null || desc.trim().length() == 0)
            return ip;
        else
            return desc + '(' + ip + ')';
    }

    

}