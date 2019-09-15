package util;

import java.util.Random;

public class MessageCreator {
	public static final int MINIMUM_PACKET_LEN=4;
	private static String nonce=null;
	private static String oldnonce=null;
	public static long nonceCreationTime=0;
	
	public static int prepareMessage(byte message[], int Message_type)
	{
	  message[0]=(byte)((Message_type >>8)& 0x00FF);
	  message[1]=(byte)(Message_type & 0x00FF);
	  message[2]=(byte)((MINIMUM_PACKET_LEN>> 8 )& 0x00FF);
	  message[3]=(byte)(MINIMUM_PACKET_LEN & 0x00FF);
	  return MINIMUM_PACKET_LEN;
	}

	public static int addAttribute(int attributeType, byte [] attrValue, int startPosOfAttrValue, int attValueLength, byte [] message)
	{
	  int len=twoByteToInt(message,2);//(int)(message[2]<<8| message[3]);

	  message[len++]=(byte)((attributeType >>8)& 0x00FF);
	  message[len++]=(byte)(attributeType & 0x00FF);
	  message[len++]=(byte)((attValueLength>>8) & 0x00FF);
	  message[len++]=(byte)(attValueLength & 0x00FF);

	  for (int i = startPosOfAttrValue; i < startPosOfAttrValue+attValueLength; i++)
	  {
	    message[len++] = attrValue[i];
	  }
	  message[2]=(byte)((len >>8)& 0x00FF);
	  message[3]=(byte)(len & 0x00FF);
	  return len;
	}

	public static int addAttribute(int attributeType, String attrValue, byte [] message)
	{
	  int len=twoByteToInt(message,2);//(int)(message[2]<<8| message[3]);

	  int length=attrValue.length();
	  
	  message[len++]=(byte)((attributeType >>8)& 0x00FF);
	  message[len++]=(byte)(attributeType & 0x00FF);
	  message[len++]=(byte)((length>>8) & 0x00FF);
	  message[len++]=(byte)(length & 0x00FF);

	  for (int i = 0; i <length; i++)
	  {
	    message[len++] =(byte)attrValue.charAt(i);
	  }
	  message[2]=(byte)((len >>8)& 0x00FF);
	  message[3]=(byte)(len & 0x00FF);
	  return len;
	}


	public static int addAttribute(int attributeType, int attrValue, byte [] message)
	{
	  int len=twoByteToInt(message,2);//(int)(message[2]<<8| message[3]);

	  message[len++]=(byte)((attributeType >>8)& 0x00FF);
	  message[len++]=(byte)(attributeType & 0x00FF);
	  message[len++]=(byte)((2>>8) & 0x00FF);//attribute length is 2 for port
	  message[len++]=(byte)(2 & 0x00FF);

	  message[len++]=(byte)((attrValue >>8)& 0x00FF);
	  message[len++]=(byte)(attrValue & 0x00FF);

	  message[2]=(byte)((len >>8)& 0x00FF);
	  message[3]=(byte)(len & 0x00FF);
	  return len;
	}


	public static int twoByteToInt(byte data[], int index)
	{
	  int t = data[index]&0x00FF;
	  return (t << 8) |(data[index+1]&0x00FF);
	}

	public static  String getNonce() {
		if (nonce == null || System.currentTimeMillis() - nonceCreationTime > 300000) {			
			oldnonce = nonce;
			byte[]  bytes = new byte[4];
			new Random().nextBytes(bytes);
			char[] hexArray = "0123456789ABCDEF".toCharArray();
		    char[] hexChars = new char[bytes.length * 2];
		    for ( int j = 0; j < bytes.length; j++ ) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = hexArray[v >>> 4];
		        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		    }
		    nonce = new String(hexChars);	
		    nonceCreationTime = System.currentTimeMillis();
		}
		return nonce;
	}

	public  static String getOldNonce() {
		if(oldnonce != null)
			return oldnonce;
		else 
			return nonce;
	}
	
}
