package login;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
public class PasswordSenderBySMS 
{

	public static final int SMS_REQUEST = 0x0101;
	public static final int DESTINATION_ID = 0x0102;
	public static final int SMS_ID = 0x0103;
	public static final int OPERATOR_CODE_ID = 0x0104;
	public static final int USER_NAME_ID = 0X0105;
	public static final int CALLER_ID = 0X0106;
	public static final int MINIMUM_PACKET_LEN=4;
	static HashMap<String,Long> smsRequestStatusByPhoneNumber=new HashMap<String, Long>();
	static PasswordSenderBySMS  passwordSenderBySMS;
	static Logger logger=Logger.getLogger(PasswordSenderBySMS.class);
	byte [] sendBuffer;
	private DatagramPacket sendPacket;
	public static  byte[] smsCallerID;
	
	
	public static int SMSServerPort;
	public static byte [] smsSenderUserName;
	public static byte [] smsServerOperatorCode;
	public static InetAddress SMSServerIP;
	DatagramSocket socket;
	
	  public PasswordSenderBySMS()
	  {
		  try
			 {
				socket=new DatagramSocket(); 
			 }
			 catch(Exception ee)
			 {
				 
			 }
		  sendBuffer = new byte[500];
		  sendPacket=new DatagramPacket(sendBuffer,sendBuffer.length);
		  smsRequestStatusByPhoneNumber=new HashMap<String, Long>();
		
		  
	  }
	  public static PasswordSenderBySMS getInstance()
	  {
	    if (passwordSenderBySMS == null)
	      createInstance();
	    return passwordSenderBySMS;
	  }

	  protected static synchronized void createInstance()
	  {
	    if (passwordSenderBySMS == null)
	    {
	    	passwordSenderBySMS=new PasswordSenderBySMS();
	    }
	   }
	public  boolean  sendPasswordBySMS(String password, String phoneNumber)
	{
			logger.debug("Before Sending SMS");
			boolean sentSMS=false;
			smsRequestStatusByPhoneNumber.put(phoneNumber,System.currentTimeMillis());
			prepareMessage(sendBuffer,SMS_REQUEST);
			addAttribute(OPERATOR_CODE_ID,smsServerOperatorCode,0,smsServerOperatorCode.length, sendBuffer);
			addAttribute(USER_NAME_ID,smsSenderUserName,0,smsSenderUserName.length,sendBuffer);
			addAttribute(CALLER_ID,smsCallerID,0,smsCallerID.length,sendBuffer);
			addAttribute(DESTINATION_ID,phoneNumber,sendBuffer);
			int length=addAttribute(SMS_ID,password,sendBuffer);
			try
			{
				sendPacket.setLength(length);
				sendPacket.setAddress(SMSServerIP);
				sendPacket.setPort(SMSServerPort);
				socket.send(sendPacket);
				sentSMS=true;
			}
			catch (Exception e)
			{
				logger.fatal("Exception at Sending SMS request packet.",e);
			
			}
			
			return sentSMS;
					
	}
	
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
}
