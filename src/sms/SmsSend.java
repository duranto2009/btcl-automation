package sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


import smsServer.AutoSignUp;
import util.ServiceDAOFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import common.Assert;
import common.UniversalDTOService;
import mail.MailDTO;
import mail.MailServerInformationDTO;



public class SmsSend extends Thread{

	String endpoint = /*"http://208.74.74.4/smsportal/smsrequest.do"*/null;
	String user = "2020";		
	Logger logger = Logger.getLogger(SmsSend.class);
	
	private volatile static boolean isRunning = true;
	private static SMSAPIInformaionDTO smsapiInformaionDTO = null;
	public long lastMailConfigReadTime=0;
	private static SmsSend sender;
	private static LinkedBlockingQueue<SmsDTO> messageQueue = null;
	
	
	public int getMessageQueueSize(){
		return messageQueue.size();
	}
	
	private SmsSend() throws Exception
	{
		messageQueue = new LinkedBlockingQueue<SmsDTO>();
		readSMSConfigFromDB();
		start();
	}


	private void readSMSConfigFromDB() throws Exception {
		// TODO Auto-generated method stub		
		smsapiInformaionDTO = ServiceDAOFactory.getService(UniversalDTOService.class).get(SMSAPIInformaionDTO.class);
		if(!smsapiInformaionDTO.isActive)
		{
			logger.info("SMS Service is not Active !!!");
			throw new Exception("SMS Service is not Active !!!");
		}
		logger.info("smsapiInformaionDTO " +  smsapiInformaionDTO);
		lastMailConfigReadTime=System.currentTimeMillis();
	}


	public static SmsSend getInstance() throws Exception
	{
		if(sender == null)
		{
			sender = new SmsSend();
		}	

		return sender;
	}


	
	
	public void  addSms(String to,String msgText) 
	{
		logger.info("Adding message to message queue");
		synchronized (messageQueue)
		{
			try
			{
				SmsDTO store = new SmsDTO();
				store.setReceiver(to);				
				store.setMsgText(msgText);				
				messageQueue.add(store);
				messageQueue.notifyAll();
			}	
			catch(Exception e)
			{
				e.printStackTrace();
				//System.out.println("Error in message formatting");
			}


		}

		//System.out.println("I got out of it");
	}

	public void sendMessage(SmsDTO smsDTO)
	{
		try
		{
			String msg = URLEncoder.encode(smsDTO.getMsgText(), "utf-8");
			String phoneNo = smsDTO.getReceiver();

			//Removes any illegal characters.
			phoneNo = phoneNo.replace( "+", "" ).replace( "-", "" );

			if(phoneNo.startsWith("88"))
			{
				phoneNo = phoneNo.substring(2);
			}
			else if(phoneNo.startsWith("088"))
			{
				phoneNo = phoneNo.substring(3);
			}

			String smsUrl = new String(smsapiInformaionDTO.smsURL);
			logger.info("smsUrl " + smsUrl);
			smsUrl = smsUrl.replaceFirst(SMSConstants.RECEIVER, phoneNo);
			smsUrl = smsUrl.replaceFirst(SMSConstants.USERNAME, smsapiInformaionDTO.username);
			smsUrl = smsUrl.replaceFirst(SMSConstants.PASSWORD, smsapiInformaionDTO.password);
			smsUrl = smsUrl.replaceFirst(SMSConstants.MESSAGE, msg);


			logger.info("sms url " + smsUrl);

			URL url = new URL(smsUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(10 * 1000);
			urlConnection.setReadTimeout(10 * 1000);
			long requestTime = System.currentTimeMillis();
			URLConnection urlconnection = url.openConnection();

			BufferedReader br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
			String m_responseMessage = new String();

			while (true) {
				String line = br.readLine();
				if (line != null) {
					m_responseMessage += line;
				} else {
					break;
				}
			}
			boolean successfull = true;
			if (!m_responseMessage.contains("SUCCESS")) {
				successfull = false;
				throw new Exception("SMS Failed :: response from API :: " + m_responseMessage);
				//		                	break;
			}
			else {

			}
			long responseTime = System.currentTimeMillis();

			logger.info("Response : " + m_responseMessage);

			logger.info("took " + (responseTime - requestTime) / 1000.0 + " seconds to receive reply");




		}

		catch(Exception ex)
		{
			logger.info("Exception ", ex);
		}
		finally
		{					

		}


	}   

	
	
/*	public String SendSMS()
	{
		SmsDTO store = null;
		String result = "Not sent";
		synchronized (messageQueue) 
		{
			store = messageQueue.peek();
		}
		while (store == null) {
			////System.out.println("Waiting for data in messageQueue!!!");
			synchronized (messageQueue) 
			{
				try 
				{
					messageQueue.wait(1000);
				}
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				store = messageQueue.peek();
			}
		}


		//System.out.println("Sending SMS!!!");
		synchronized (messageQueue) 
		{

			SmsDTO sms = messageQueue.poll();
			if(sms!=null)
			{

				try
				{

					endpoint = "http://208.74.74.4/smsportal/smsrequest.do""http://123.49.3.58:8081/teletalkSMS/index.php";
					senderNo = "8801817535737"EssentialRepository.getInstance().getEssential(0).getSmsTelephoneNo();
					password = util.CEncrypt.decryptString(EssentialRepository.getInstance().getEssential(0).getSmsPassword());
					
					//System.out.println("Sms Password:"+password);
					MessageDTO dto = new MessageDTO();
					dto.setMsgFrom(senderNo);
					dto.setMsgTo(sms.getReceiver());
					dto.setMsgSendTime(System.currentTimeMillis());
					dto.setMsgSentTime(System.currentTimeMillis());
					dto.setMsgText(sms.getMsgText());
					dto.setMsgType(MessageConstants.MESSAGE_TYPE_SMS);
					final WebClient webClient = new WebClient();
					HtmlPage page = null;
					try 
					{

						page = webClient.getPage(endpoint);
						Assert.verify(page.getTitleText().equalsIgnoreCase("Teletalk SMS"));			
						final HtmlForm form = page.getFormByName("Form1");
						final HtmlButtonInput submitbutton = form.getInputByName("btnsubmit");
						final HtmlTextInput textField = form.getInputByName("txtMobileNo");
						final HtmlPasswordInput passField = form.getInputByName("txtPassword");
						textField.setValueAttribute(senderNo);
						passField.setValueAttribute(password);
						final HtmlPage page2 = submitbutton.click();			
						Assert.verify(page.getTitleText().equalsIgnoreCase("Teletalk SMS"));
						final HtmlForm form1 = page2.getFormByName("Form1");
						final HtmlTextInput textNumberTo = form1.getInputByName("txtToPhoneNumber");
						final HtmlTextArea textMessage = form1.getTextAreaByName("txtMessage");
						final HtmlButtonInput sendbutton = form1.getInputByName("btnSubmit");
						textNumberTo.setValueAttribute(sms.getReceiver());
						textMessage.setText(sms.getMsgText());
						final HtmlPage page3 = sendbutton.click();
						if(page3.asText().contains("Sent successfully"))
						{
							result =  "Sent successfully";
							dto.setMsgStatus(message.MsgConstants.MESSAGE_USED);
						}	 

					} 

					catch (FailingHttpStatusCodeException e) 

					{
						// TODO Auto-generated catch block
						dto.setMsgStatus(message.MsgConstants.MESSAGE_UNUSED);
						e.printStackTrace();
					}


					catch (MalformedURLException e) 		
					{
						// TODO Auto-generated catch block
						dto.setMsgStatus(message.MsgConstants.MESSAGE_UNUSED);
						e.printStackTrace();
					}

					catch (IOException e)
					{
						// TODO Auto-generated catch block
						dto.setMsgStatus(message.MsgConstants.MESSAGE_UNUSED);
						e.printStackTrace();
					}

					finally
					{
						webClient.closeAllWindows();
						MessageService common = new MessageService();
						common.addMessage(dto);
					}

				}

				catch(Exception ex)
				{
					ex.printStackTrace();
				}

			}
		}   






		return result;
	}*/

	public void run()
	{
		logger.info("inside run");
		while (isRunning) {
			try {

				SmsDTO smsDTO = messageQueue.take();												
				sendMessage(smsDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("Exception", e);
			}
		}
	}


	public void stopSending()
	{
		isRunning = false;
	}

}
