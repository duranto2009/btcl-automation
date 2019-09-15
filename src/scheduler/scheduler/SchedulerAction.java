package scheduler.scheduler;

import mail.MailSend;
import notification.NotificationService;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sms.SmsSend;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.List;

@ActionRequestMapping("scheduler/")
public class SchedulerAction extends AnnotatedRequestMappingAction {
	Logger logger = Logger.getLogger(getClass());
	@Service
	SchedulerConfigurationService schedulerService;
	
	@Service
	NotificationService notificationService;
	
	@RequestMapping(mapping="viewScheduler", requestMethod = RequestMethod.GET)
	public ActionForward getSchedulerBySchedulerID(HttpServletRequest request,ActionMapping mapping, @RequestParameter("schedulerID")long ID) throws Exception {
		 SchedulerConfiguration schedulerConfiguration = schedulerService.getSchedulerBySchedulerID(ID);
		request.setAttribute("scheduler", schedulerConfiguration);
		return mapping.findForward("getSchedulerConfig");
		
	}
	@RequestMapping(mapping="getSchedulers", requestMethod=RequestMethod.GET) 
	public ActionForward getAllSchedulers(ActionMapping mapping, HttpServletRequest request) throws Exception {
		List<SchedulerConfiguration> schedulers = schedulerService.getAllSchedulers();
		request.setAttribute("schedulers", schedulers);
		return mapping.findForward("getAllSchedulers");
	}
	@RequestMapping(mapping="update", requestMethod = RequestMethod.POST) 
	public void uodateScheduler(ActionMapping mapping, HttpServletRequest request, 
			SchedulerConfiguration schedulerConfiguration) throws Exception {
		schedulerService.updateScheduler(schedulerConfiguration);
//		request.setAttribute("scheduler", schedulerConfiguration);
//		return mapping.findForward("getSchedulerConfig");
	}
	@RequestMapping(mapping="getMonthlyBill", requestMethod=RequestMethod.GET)
	public ActionForward getMonthlyBillFormPage(ActionMapping mapping, HttpServletRequest request) {
		request.setAttribute("title", "Monthly Bill");
		return mapping.findForward("getMonthlyBillForm");
	}
	@RequestMapping(mapping="cancelMonthlyBill", requestMethod=RequestMethod.GET)
	public ActionForward getCancelMonthlyBillFormPage(HttpServletRequest request, ActionMapping mapping) {
		request.setAttribute("title", "Cancel Bill");
		return mapping.findForward("getMonthlyBillForm");
	}
	@RequestMapping(mapping="viewConfiguration", requestMethod=RequestMethod.GET)
	public ActionForward getConfigurationPage(ActionMapping mapping) {
		return mapping.findForward("getConfigurationView");
	}
	@RequestMapping(mapping="getNotificationTemplate", requestMethod=RequestMethod.GET)
	public ActionForward getNotificationTemplateFormPage(ActionMapping mapping) {
		return mapping.findForward("getNotificationTemplatePage");
	}
//	@RequestMapping(mapping="getPreviewTemplate", requestMethod=RequestMethod.POST)
//	public List<NotificationDraft> getPreviewTemplate(@RequestParameter("entityTypeID") int entityTypeID
//			,@RequestParameter("entityConditionString") String entityConditionString
//			,@RequestParameter("contactDetailsType")int contactDetailsType
//			,@RequestParameter("notificationTemplate") String notificationTemplate) throws Exception{
//
//		logger.debug(notificationTemplate);
//		return null;
////		return notificationService.getNotificationDrafts(entityTypeID, " "+entityConditionString
////				, contactDetailsType, notificationTemplate);
//	}
//
	

	
	@RequestMapping(mapping="sendTemplate", requestMethod=RequestMethod.POST)
	public void sendTemplate(@RequestParameter("entityTypeID") int entityTypeID
			,@RequestParameter("entityConditionString") String entityConditionString
			,@RequestParameter("contactDetailsType")int contactDetailsType
			,@RequestParameter("notificationTemplate") String notificationTemplate
			,@RequestParameter("notificationSubject")String notificationSubject
			,@RequestParameter("smsNotification")boolean sendSmsNotification
			,@RequestParameter("emailNotification")boolean sendMailNotification
			) throws Exception{
		
		
		notificationService.sendNotificationByEntityIDAndConditionString(entityTypeID
				, " "+entityConditionString, contactDetailsType, notificationTemplate, sendMailNotification
				, sendSmsNotification, notificationSubject);
	}
	
	
	class ServerNotificationQueueStatus{
		public String serverIpAddress;
		public int remainingMail;
		public int remainSms;
	}
	
	public ServerNotificationQueueStatus getServerNotificationStatus() throws Exception{
		ServerNotificationQueueStatus serverNotificationQueueStatus = new ServerNotificationQueueStatus();
		serverNotificationQueueStatus.serverIpAddress = InetAddress.getLocalHost().getHostAddress();
		serverNotificationQueueStatus.remainingMail = MailSend.mailQueue.size();
		serverNotificationQueueStatus.remainSms = SmsSend.getInstance().getMessageQueueSize();
		
		return serverNotificationQueueStatus;
	}
	
	
}
