package smsServer;

import org.apache.log4j.Logger;

import sms.SmsSend;

public class SMSSender {
	Logger logger = Logger.getLogger(getClass());
	private static SMSSender instance = null;
	
	private SMSSender()
	{
		//AutoSignUp.getInstance();
	}
	public static SMSSender getInstance()
	{
		if(instance == null)
		{
			createInstance();
		}
		return instance;
	}
	private synchronized static void createInstance()
	{
		if(instance == null)
		{
			instance = new SMSSender();
		}
		
	}
	public int sendSMS(String message, String destination) throws Exception
	{
		//return AutoSignUp.getInstance().sendSMS(message, destination);
		SmsSend.getInstance().addSms(destination, message);
		return 1;
	}
}
