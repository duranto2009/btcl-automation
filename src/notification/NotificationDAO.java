package notification;

import common.CommonDAO;
import common.ContactDetailsDAO;
import common.EntityDTO;
import common.ModuleContactDetailsService;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import mail.MailDTO;
import mail.MailSend;
import org.apache.log4j.Logger;
import request.CommonRequestDTO;
import smsServer.SMSSender;
import user.UserDTO;
import user.UserRepository;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.List;

import static util.SqlGenerator.*;

public class NotificationDAO {
	Logger logger = Logger.getLogger(NotificationDAO.class);

	public void addNotification(NotificationDTO notificationDTO, DatabaseConnection databaseConnection) {
		try {
			insert(notificationDTO, notificationDTO.getClass(), databaseConnection, true);
		} catch (Exception ex) {
			logger.debug(ex);
		}
	}

	public void addNotificationTemplate(NotificationTemplate notificationTemplate,
			DatabaseConnection databaseConnection) {
		try {
			insert(notificationTemplate, notificationTemplate.getClass(), databaseConnection, true);
		} catch (Exception ex) {
			logger.debug(ex);
		}
	}

	public void addNotificationReceiver(NotificationReceiver notificationReceiver,
			DatabaseConnection databaseConnection) {
		try {
			insert(notificationReceiver, notificationReceiver.getClass(), databaseConnection, true);
		} catch (Exception ex) {
			logger.debug(ex);
		}
	}

	public List<NotificationTemplate> getNotificationTemplateByActionStateID(int actionStateID,
			DatabaseConnection databaseConnection) {
		List<NotificationTemplate> temp = null;
		try {

			Class classObject = NotificationTemplate.class;
			String conditionString = " where " + getColumnName(classObject, "actionStateID") + " = " + actionStateID;
			temp = (List<NotificationTemplate>) getAllObjectList(classObject, databaseConnection, conditionString);
		} catch (Exception ex) {
			logger.debug(ex);
		}

		return temp;
	}

	public List<NotificationTemplate> getNotificationTemplateByActionStateIDAndTemplateType(int actionStateID,
			int templateType, DatabaseConnection databaseConnection) {
		List<NotificationTemplate> temp = null;
		try {
			Class classObject = NotificationTemplate.class;
			String conditionString = " where " + getColumnName(classObject, "actionStateID") + " = " + actionStateID
					+ " and " + getColumnName(classObject, "type") + "=" + templateType;
			temp = (List<NotificationTemplate>) getAllObjectList(classObject, databaseConnection, conditionString);

		} catch (Exception ex) {
			logger.debug(ex);
		}
		return temp;
	}

	public NotificationTemplate getNotificationTemplateByActionStateIDAndTemplateTypeAndViewerType(int actionStateID,
			int templateType, int viewerType, DatabaseConnection databaseConnection) {
		List<NotificationTemplate> notificationTemplates = null;

		try {
			Class classObject = NotificationTemplate.class;
			String conditionString;
			conditionString = " where " + getColumnName(classObject, "actionStateID") + " = " + actionStateID + " and "
					+ getColumnName(classObject, "type") + "=" + templateType + " and "
					+ getColumnName(classObject, "userType") + "=" + viewerType;
			notificationTemplates = (List<NotificationTemplate>) getAllObjectList(classObject, databaseConnection,
					conditionString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return notificationTemplates.isEmpty() ? null : notificationTemplates.get(0);
	}

	public void sendNotification(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection) {
		try {
			EntityDTO entityDTO = new CommonDAO().getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO,
					databaseConnection);
			commonRequestDTO.setEntityName(entityDTO.getName());

			List<NotificationTemplate> mailNotificationTemplates = getNotificationTemplateByActionStateID(
					commonRequestDTO.getRequestTypeID(), databaseConnection);
			logger.debug("mailNotificationTemplates " + mailNotificationTemplates);
			for (NotificationTemplate notificationTemplate : mailNotificationTemplates) {

				if (notificationTemplate.getType() == NotificationTemplate.NOTIFICATION_TYPE_MAIL) {

					sendMailNotification(commonRequestDTO, notificationTemplate, databaseConnection);

				} else if (notificationTemplate.getType() == NotificationTemplate.NOTIFICATION_TYPE_SMS) {
					sendSmsNotification(commonRequestDTO, notificationTemplate, databaseConnection);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendMailNotification(CommonRequestDTO commonRequestDTO, NotificationTemplate notificationTemplate,
			DatabaseConnection databaseConnection) {
		try {
			String mailAddress = null;
			String mailReceiverName = null;
			if (notificationTemplate.getUserType() == NotificationTemplate.CLIENT_TEMPLATE) {
				ModuleContactDetailsService moduleContactDetailsService = new ModuleContactDetailsService();
				mailReceiverName = AllClientRepository.getInstance().getClientByClientID(commonRequestDTO.getClientID())
						.getName();
				ClientDetailsDTO moduleClientDTO = AllClientRepository.getInstance()
						.getModuleClientByClientIDAndModuleID(commonRequestDTO.getClientID(),
								commonRequestDTO.getModuleIDFromThisDTO());
				// ClientContactDetailsDTO moduleContactDetailsDTO =
				// moduleContactDetailsService.getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(),
				// ClientContactDetailsDTO.REGISTRANT_CONTACT);
				ClientContactDetailsDTO moduleContactDetailsDTO = new ContactDetailsDAO()
						.getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(),
								ClientContactDetailsDTO.REGISTRANT_CONTACT, databaseConnection);
				mailAddress = moduleContactDetailsDTO.getEmail();
			} else if (notificationTemplate.getUserType() == NotificationTemplate.SENDER_TEMPLATE) {
				UserDTO userDTO = UserRepository.getInstance()
						.getUserDTOByUserID(-1 * commonRequestDTO.getRequestByAccountID());
				mailAddress = userDTO.getMailAddress();
				mailReceiverName = userDTO.getUsername();
			} else if (notificationTemplate.getUserType() == NotificationTemplate.RECEIEVER_TEMPLATE) {
				UserDTO userDTO = UserRepository.getInstance()
						.getUserDTOByUserID(-1 * commonRequestDTO.getRequestToAccountID());
				mailAddress = userDTO.getMailAddress();
				mailReceiverName = userDTO.getUsername();
			} else {
				logger.debug("Invalid notification template type");
				return;
			}
			commonRequestDTO.setReceiverName(mailReceiverName);
			MailDTO mailDTO = new MailDTO();
			mailDTO.isHtmlMail = false;
			mailDTO.mailSubject = notificationTemplate.getCaption();
			mailDTO.msgText = NotificationProcessor.getNotification(notificationTemplate.getDescription(),
					commonRequestDTO);
			mailDTO.msgText += "\n\nData & Internet Division,\nBangladesh Telecommunications Company Limited (BTCL)";
			mailDTO.toList = mailAddress;

			MailSend mailSend = MailSend.getInstance();

			logger.debug("The mailDTO readey to be sent: " + mailDTO);
			mailSend.sendMailWithContentAndSubject(mailDTO);

		} catch (Exception ex) {
			logger.debug(ex);
		}
	}

	private void sendSmsNotification(CommonRequestDTO commonRequestDTO, NotificationTemplate notificationTemplate,
			DatabaseConnection databaseConnection) {
		try {
			logger.debug("commonRequestDTO " + commonRequestDTO);
			String mobileNuber = null;
			String smsReceiverName = null;
			if (notificationTemplate.getUserType() == NotificationTemplate.CLIENT_TEMPLATE) {
				ModuleContactDetailsService moduleContactDetailsService = new ModuleContactDetailsService();
				smsReceiverName = AllClientRepository.getInstance().getClientByClientID(commonRequestDTO.getClientID())
						.getName();
				ClientDetailsDTO moduleClientDTO = AllClientRepository.getInstance()
						.getModuleClientByClientIDAndModuleID(commonRequestDTO.getClientID(),
								commonRequestDTO.getModuleIDFromThisDTO());
				// ClientContactDetailsDTO moduleContactDetailsDTO =
				// moduleContactDetailsService.getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(),
				// ClientContactDetailsDTO.ADMIN_CONTACT);
				ClientContactDetailsDTO moduleContactDetailsDTO = new ContactDetailsDAO()
						.getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(),
								ClientContactDetailsDTO.REGISTRANT_CONTACT, databaseConnection);
				mobileNuber = moduleContactDetailsDTO.getPhoneNumber();
			} else if (notificationTemplate.getUserType() == NotificationTemplate.SENDER_TEMPLATE) {
				UserDTO userDTO = UserRepository.getInstance()
						.getUserDTOByUserID(-1 * commonRequestDTO.getRequestByAccountID());
				mobileNuber = userDTO.getPhoneNo();
				smsReceiverName = userDTO.getUsername();
			} else if (notificationTemplate.getUserType() == NotificationTemplate.RECEIEVER_TEMPLATE) {
				UserDTO userDTO = UserRepository.getInstance()
						.getUserDTOByUserID(-1 * commonRequestDTO.getRequestToAccountID());
				mobileNuber = userDTO.getPhoneNo();
				smsReceiverName = userDTO.getUsername();
			} else {
				logger.debug("Invalid notification template type");
				return;
			}

			logger.debug("entityName : " + commonRequestDTO.getEntityName());
			String notificationString = NotificationProcessor.getNotification(notificationTemplate.getDescription(),
					commonRequestDTO);
			logger.debug("notificationString : " + notificationString);
			SMSSender smsSender = SMSSender.getInstance();
			smsSender.sendSMS(notificationString, mobileNuber);

		} catch (Exception ex) {
			logger.debug(ex);
		}
	}
}
