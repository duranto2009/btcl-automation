package crm.service;

import org.apache.log4j.Logger;

import common.EntityTypeConstant.ActionTypeForCrmComplain;
import crm.CrmComplainDTO;
import crm.CrmEmployeeDTO;
import crm.repository.CrmAllEmployeeRepository;
import mail.MailDTO;
import mail.MailSend;
import smsServer.SMSSender;
import user.UserDTO;
import user.UserRepository;

public class CrmNotificationService {
	
	
	public void sendNotification (CrmComplainDTO crmComplainDTO, ActionTypeForCrmComplain type) throws Exception {
		sms(crmComplainDTO , type);
		email(crmComplainDTO, type);
	}
	private void sms(CrmComplainDTO crmComplainDTO, ActionTypeForCrmComplain type) {
		CrmEmployeeDTO smsReceiverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());
		String notificationMsg = getNotificationMsg(crmComplainDTO.getID(), smsReceiverEmployeeDTO.getName(), type, false);
		sendSMS(smsReceiverEmployeeDTO.getUserID(), notificationMsg);
	}
	
	private void email(CrmComplainDTO crmComplainDTO, ActionTypeForCrmComplain type) throws Exception {
		CrmEmployeeDTO emailReceiverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());
		String mailSubject = getMailSubject(type);
		String notificationMsg = getNotificationMsg(crmComplainDTO.getID(), emailReceiverEmployeeDTO.getName(), type, true);
		sendMail(emailReceiverEmployeeDTO.getUserID(), mailSubject, notificationMsg);
	}
	
	private String getNotificationMsg(long complainID, String receiver, ActionTypeForCrmComplain type, boolean forMail) {
		String notificationMsg = "";
		String delimeter = (forMail == true ? "\n": " ");
		String thanksWithDelimeter = delimeter + "Thanks";
		String greetings = "Dear " + receiver + "," + delimeter;
		String msgBody = "";
		switch(type) {
			case COMPLETE_TYPE: 
				msgBody = "Complain with forwarding id:"+ complainID + " is completed.";
				break;
			case FEEDBACK_TYPE:
				msgBody = "You have received feedback on forwarding id:" + complainID + ".";
				break;
			case REJECT_TYPE:
				msgBody = "You have received a complain with forwarding id:" + complainID + "(REJECTED).";
				break;
			case PASS_TYPE:
				msgBody = "A complain is passed to you with forwarding id:" + complainID + ".";
				break;
			case ASSIGN_TYPE:
				msgBody = "A new complain is assigned to you with forwarding id:" + complainID + "." ;
				break;
		}
		notificationMsg = greetings + msgBody + thanksWithDelimeter;
		return notificationMsg;
	}
	private String getMailSubject(ActionTypeForCrmComplain type) {
		String mailSubject = "";
		switch(type) {
		case COMPLETE_TYPE: 
			mailSubject = "Complain Complete";
			break;
		case FEEDBACK_TYPE:
			mailSubject = "Received Feedback";
			break;
		case REJECT_TYPE:
			mailSubject = "Received Feedback(Rejected)";
			break;
		case PASS_TYPE:
			mailSubject = "Received A Passed Complain";
			break;
		case ASSIGN_TYPE:
			mailSubject = "Assign A Complain";
			break;
		}
		return mailSubject;
	}
	private void sendSmsNotification(Long userID, String notificationMsg) throws Exception {
		String mobileNuber = null;
		String smsReceiverName = null;
		if (userID != null && userID > 0) {
			UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
			mobileNuber = "+8801670312750"/* userDTO.getPhoneNo() */;
			smsReceiverName = userDTO.getUsername();
		}

		SMSSender smsSender = SMSSender.getInstance();
		smsSender.sendSMS(notificationMsg, mobileNuber);
	}
	private void sendMailNotification(Long userID, String mailSubject, String notificationMsg) throws Exception {
		
		String mailAddress = null;
		String mailReceiverName = null;
		if (userID != null && userID > 0) {
			UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
			mailAddress = "kawser@revesoft.com"/*
												 * userDTO.getMailAddress()
												 */;
			mailReceiverName = userDTO.getUsername();
		}

		MailDTO mailDTO = new MailDTO();
		mailDTO.isHtmlMail = false;
		mailDTO.mailSubject = mailSubject;
		mailDTO.msgText = notificationMsg;
		mailDTO.msgText += "\nData & Internet Division,\nBangladesh Telecommunications Company Limited (BTCL)";
		mailDTO.toList = mailAddress;

		MailSend mailSend = MailSend.getInstance();
		mailSend.sendMailWithContentAndSubject(mailDTO);
	
	}
	private void sendSMS(Long userID, String notificationMsg) {
		try {
			// sendSmsNotification(userID, notificationMsg);
		} catch (Exception ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.debug("SMS Send Error :" + ex);
		}

	}
	private void sendMail(Long userID, String mailSubject, String notificationMsg) {
		try {
			// sendMailNotification(userID, notificationMsg);
		} catch (Exception ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.debug("Mail Send Error :" + ex);
		}
	}
	
}
