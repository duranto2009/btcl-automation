package mail;

import common.UniversalDTOService;
import common.bill.BillService;
import org.apache.log4j.Logger;
import util.ServiceDAOFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;

public class MailSend extends Thread {
	static Logger logger = Logger.getLogger(MailSend.class);
//	public String from;
	public String toList;
	public String subject;
	public String messageText;
	public static MailServerInformationDTO mailServerInformationDTO = null;
//	public String authenticationEmailAddress = "tech.carrier@inaani.com";
//	public String authenticationEmailPassword = "nOcoUt7inAni";
//	public String mailServerPort = "25";
//	public boolean startTLS = false;	// shantanu
//	public boolean needAuthentication = false;
	public static LinkedBlockingQueue<MailDTO> mailQueue;
	volatile boolean running = false;
	public static MailSend mailSend;
	public static String contextPath;
	public static String blockIpEmailContent;
	public long lastMailConfigReadTime=0;
	
	public static MailSend getInstance() throws Exception {
		if (mailSend == null) {
			createInstance();
		}
		return mailSend;
	}

	private MailSend() throws Exception {
		super("Mail Sender");
		setPriority(Thread.MIN_PRIORITY);
		mailQueue = new LinkedBlockingQueue<MailDTO>();
		try {
			readMailConfigFromDB();
		}
		catch(Exception ex) {
			logger.debug("Exception in reading mail config");
			return;
		}
		
//		readConfigFromFile();
		this.setDaemon(true);
		start();
	}

	public void readMailConfigFromDB() throws Exception {
		mailServerInformationDTO = ServiceDAOFactory.getService(UniversalDTOService.class).get(MailServerInformationDTO.class);
		if(!mailServerInformationDTO.isActive){
			logger.debug("Mail Service is not Active !!!");
			throw new Exception("Mail Service is not Active !!!");
		}
		logger.debug("mailServerInformationDTO " +  mailServerInformationDTO);
		lastMailConfigReadTime=System.currentTimeMillis();
	}

	private static synchronized MailSend createInstance() throws Exception {
		if (mailSend == null) {
			mailSend = new MailSend();
		}
		return mailSend;
	}

	public void sendMailWithSubject(String p_toList, String mailContent, String mailSubject) {
		if(!running)return;
		MailDTO mailDTO = new MailDTO(p_toList, mailContent);
		mailDTO.mailSubject = mailSubject;
		mailQueue.offer(mailDTO);
	}

	public void sendMailWithContentAndSubject(String mailContent, String subject) {
		if(!running)return;
		MailDTO mailDTO = new MailDTO(toList, mailContent);
		mailDTO.mailSubject = subject;
		mailQueue.offer(mailDTO);

	}
	
	//kayesh
	public void sendMailWithContentAndSubject(String p_list,String mailContent, String subject, boolean isHtmlContent) {
		if(!running)return;
		MailDTO mailDTO = new MailDTO(p_list, mailContent);
		mailDTO.mailSubject = subject;
		mailDTO.isHtmlMail=isHtmlContent;
		mailQueue.offer(mailDTO);

	}
	
	//kayesh
	public void sendMailWithContentAndSubject(MailDTO mailDTO) {
		logger.debug("sendMailWithContentAndSubject with running " + running);
		if(!running)return;
		mailQueue.offer(mailDTO);
		logger.info("Mail Prepared for sending");
	}
	
	public void run() {
		running = true;
		logger.debug("Mail server run is called");
		while (running) {
			try {
				MailDTO mailDTO = mailQueue.take();												
				sendMailToParticipients(mailDTO);
			} catch (Exception e) {
				logger.debug("Exception", e);
			}
		}
	}

	public void sendMailToParticipients(MailDTO mailDTO) {
		String toList = mailDTO.toList;
		String msgText = mailDTO.msgText;
		String ccList = mailDTO.ccList;
		String attachment = mailDTO.attachmentPath;
		try {
			StringTokenizer stk = new StringTokenizer(toList, ",;");
			String to[] = new String[stk.countTokens()];
			for (int i = 0; i < to.length; i++) {
				to[i] = stk.nextToken().trim();
			}
			logger.debug("ccList " + ccList);
			String cc[] = null;
			if(ccList != null && ccList.length() > 0) {
				stk = new StringTokenizer(ccList, ",;");
				cc = new String[stk.countTokens()];
				if(cc != null && cc.length > 0) {
					for (int i = 0; i < cc.length; i++) {
						cc[i] = stk.nextToken().trim();
					}
				}
			}
			Properties props = new Properties();
			Authenticator authenticator = null;
			
			authenticator = new Authenticator(mailServerInformationDTO.authEmailAddesstxt,mailServerInformationDTO.authEmailPasstxt);
		
			props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
			props.setProperty("mail.smtp.auth", "" + mailServerInformationDTO.authFromServerChk);
			props.setProperty("mail.smtp.host", mailServerInformationDTO.mailServertxt);
			props.setProperty("mail.smtp.port", mailServerInformationDTO.mailServerPorttxt);
		
				
			logger.debug("mailServerInformationDTO.tlsRequired " + mailServerInformationDTO.tlsRequired);
			if( mailServerInformationDTO.tlsRequired ){
				props.setProperty("mail.smtp.starttls.enable", "true" ); // shantanu
				logger.debug( "Starting TLS" );
			}
		

			Session session = Session.getInstance(props, authenticator);
		
		/*Transport transport = session.getTransport("smtp");
		transport.connect(mailServerInformationDTO.mailServertxt, 
				Integer.parseInt(mailServerInformationDTO.mailServerPorttxt),
				    mailServerInformationDTO.authEmailAddesstxt,
				    mailServerInformationDTO.authEmailPasstxt);*/
		
		
			session.setDebug(false);


			// create a message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailServerInformationDTO.fromAddresstxt));
			msg.setHeader("Content-Type", "text/plain; charset=UTF-8");
			
			InternetAddress[] address = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				logger.debug("to["+i+"] " + to[i]);
//				if(to[i] == null || to[i].length() == 0) continue;
				address[i] = new InternetAddress(to[i]);
				logger.debug("address["+ i + "] " + address[i]);
			}
			
			InternetAddress[] addressCC = null;
			

			msg.setRecipients(Message.RecipientType.TO, address);
			if(cc != null && cc.length > 0)
			{
				addressCC = new InternetAddress[cc.length];
				for (int i = 0; i < cc.length; i++) {
					logger.debug("cc["+i+"] " + cc[i]);
//					if(cc[i] == null || cc[i].length() == 0) continue;
					addressCC[i] = new InternetAddress(cc[i]);
					logger.debug("addressCC["+ i + "] " + addressCC[i]);
				} 
				msg.setRecipients(Message.RecipientType.CC, addressCC);
			}
			msg.setSubject(mailDTO.mailSubject);
			msg.setSentDate(new Date());


			if (attachment != null) {
				BodyPart messageBodyPart = new MimeBodyPart();
				if(mailDTO.isHtmlMail){messageBodyPart.setContent(msgText,"text/html");}
				else messageBodyPart.setText(msgText);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				addAttachments(attachment, multipart, "Offer Rates.csv");
				msg.setContent(multipart);
			}
			else
			{
				if(mailDTO.isHtmlMail) {
					
					msg.setContent(msgText,"text/html");
				}
				else{
					
					msg.setContent(msgText, "text/plain; charset=UTF-8");
					//msg.setText(msgText);
					logger.debug("msg: " + msg.getContent());
				}
			}
			try {
				Transport.send(msg);
			} catch (Exception ex) {
				logger.fatal("Exception");
				logger.fatal(ex.getMessage());
			}
			
//			transport.sendMessage(msg, address);
		} catch (Exception mex) {
			logger.fatal("Exception handling in SendMail.java", mex);

			mex.printStackTrace();

			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						logger.fatal("    ** Invalid Addresses");
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								logger.fatal("         " + invalid[i]);
						}
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						logger.fatal("    ** ValidUnsent Addresses");
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								logger.fatal("         " + validUnsent[i]);
						}
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						logger.fatal("    ** ValidSent Addresses");
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								logger.fatal("         " + validSent[i]);
						}
					}
				}

				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		}
		finally{
//			try{
//				if(attachment != null){
//					File file = new File(attachment);
//					file.delete();
//				}
//			}
//			catch(Exception ex){
//				logger.fatal("Exception deleting mail attachment", ex);
//			}
		}
		toList = msgText = null;
		logger.debug("DONE");
	}
	

	protected void addAttachments(String attachment, Multipart multipart, String AttachmentName)
			throws Exception {
		File file = new File(attachment);
		if(!file.exists()) {
			throw new Exception("Attachment Not Found");
		}
		
		MimeBodyPart attachmentBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(attachment);
		attachmentBodyPart.setDataHandler(new DataHandler(source));
		attachmentBodyPart.setFileName(file.getName());
		multipart.addBodyPart(attachmentBodyPart);
	}
	
	public static void main(String args[]) throws Exception {
		
		String billViewLink = "http://localhost:8080/BTCL_Automation/common/bill/billView.jsp?id=";
		
    	MailDTO mailDTO = new MailDTO();
		mailDTO.isHtmlMail = true;
		mailDTO.mailSubject = "BTCL Bill Generation  Notification";
		mailDTO.msgText = "Dear Customer,<br> Your have a bill waiting to be paid. To view or download the bill " +
						"Log in to BTCL system and click the following link : "+billViewLink+1;
		mailDTO.toList = "kayesh.parvez@revesoft.com";
		Logger.getLogger(BillService.class).debug("mailDTO: "+mailDTO);
		MailSend mailSend = MailSend.getInstance();
		mailSend.sendMailWithContentAndSubject(mailDTO);
	}
}
