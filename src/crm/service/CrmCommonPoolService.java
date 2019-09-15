package crm.service;

import annotation.DAO;
import annotation.Transactional;
import common.*;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import crm.*;
import crm.dao.CrmCommonPoolDAO;
import crm.dao.CrmComplainDAO;
import crm.dao.CrmEmployeeDAO;
import crm.repository.CrmAllEmployeeRepository;
import file.FileDTO;
import file.FileService;
import login.LoginDTO;
import mail.MailDTO;
import mail.MailSend;
import org.apache.log4j.Logger;
import request.CommonRequestDTO;
import request.RequestDAO;
import request.RequestUtilDAO;
import requestMapping.Service;
import smsServer.SMSSender;
import user.UserDTO;
import user.UserRepository;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.NavigationService;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class CrmCommonPoolService implements NavigationService {
	@DAO
	CrmCommonPoolDAO crmCommonPoolDAO;
	@Service
	CrmComplainService crmComplainService;
	@DAO
	CrmComplainDAO complainDAO;
	@DAO
	CrmEmployeeDAO employeeDAO;
	@Service
	CrmEmployeeService crmEmployeeService;
	@Service
	CrmComplainService complainService;
	@DAO
	RequestDAO requestDAO;
	@Service
	FileService fileService;
	@DAO
	RequestUtilDAO requestUtilDAO;
	
	private void sendSmsNotification(Long clientID,Integer moduleID, Long userID, String notificationMsg) throws Exception{

		String mobileNuber = null;
		String smsReceiverName = null;
		if(clientID != null && clientID > 0){
			ModuleContactDetailsService moduleContactDetailsService = new ModuleContactDetailsService();
			smsReceiverName = AllClientRepository.getInstance().getClientByClientID(clientID).getName();
			ClientDetailsDTO moduleClientDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID, moduleID);
			ClientContactDetailsDTO moduleContactDetailsDTO = new ContactDetailsDAO().getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(), ClientContactDetailsDTO.REGISTRANT_CONTACT, DatabaseConnectionFactory.getCurrentDatabaseConnection());
			mobileNuber = "+8801670312750"/*moduleContactDetailsDTO.getPhoneNumber()*/;
		}
		if(userID != null && userID > 0){
			UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
			mobileNuber = "+8801670312750"/*userDTO.getPhoneNo()*/;
			smsReceiverName = userDTO.getUsername();
		}
	
		SMSSender smsSender = SMSSender.getInstance();
		smsSender.sendSMS(notificationMsg,mobileNuber);
	}
	
	private void sendSMS(CrmCommonPoolDTO crmCommonPoolDTO,Long clientID, Integer moduleID, Long userID, String notificationMsg) {
		try {
			sendSmsNotification(clientID, moduleID, userID, notificationMsg);
		} catch (Exception ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.debug("SMS Send Error :"+ ex);
		}
		
	}
	
	private void sendMailNotification(Long clientID,Integer moduleID, Long userID,String mailSubject, String notificationMsg) throws Exception{
		try{
			String mailAddress = null;
			String mailReceiverName = null;
			if(clientID != null && clientID > 0){
				ModuleContactDetailsService moduleContactDetailsService = new ModuleContactDetailsService();
				mailReceiverName = AllClientRepository.getInstance().getClientByClientID(clientID).getName();
				ClientDetailsDTO moduleClientDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID, moduleID);
				ClientContactDetailsDTO moduleContactDetailsDTO = new ContactDetailsDAO().getContactDetailsByModuleClientIDAndDetaislTypeID(moduleClientDTO.getId(), ClientContactDetailsDTO.REGISTRANT_CONTACT, DatabaseConnectionFactory.getCurrentDatabaseConnection());
				mailAddress = "kawser@revesoft.com"/*moduleContactDetailsDTO.getEmail()*/;
			}
			if(userID != null && userID > 0){
				UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(userID);
				mailAddress = "kawser@revesoft.com"/*userDTO.getMailAddress()*/;
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
		}catch(Exception ex){
			Logger logger = Logger.getLogger(this.getClass());
			logger.debug("Mail Send Error :"+ ex);
		}
	}
	
	@Transactional
	public void addNewClientComplain(CrmCommonPoolDTO crmCommonPoolDTO, String[] documents, LoginDTO loginDTO) throws Exception{
		if(!loginDTO.getIsAdmin()){
			crmCommonPoolDTO.setClientID(loginDTO.getAccountID());
		}else{
			if(crmCommonPoolDTO.getClientID() == null){
				throw new RequestFailureException("Please select a client");
			}
		}
		if(crmCommonPoolDTO.getEntityTypeID() == null ) {
			throw new RequestFailureException("Please select a service type");
		}
		crmCommonPoolDAO.insertCrmComplain(crmCommonPoolDTO);
		
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForNewClientComplain(crmCommonPoolDTO.getID(), crmCommonPoolDTO.getClientID(), loginDTO);
		
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
		uploadDocuments(crmCommonPoolDTO.getID(), documents, loginDTO);
		
		/* sms notification */
		Integer moduleID = EntityTypeConstant.mapOfModuleIDToMainEntityTypeIdForCrm
				.get(crmCommonPoolDTO.getEntityTypeID());
		String notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " User, We have reveived your query.Your Token Id : "+crmCommonPoolDTO.getID()+". Thanks";
		sendSMS(crmCommonPoolDTO,crmCommonPoolDTO.getClientID(),moduleID,null,notificationMsg);	
		/* sms notification */
		
		/*mail notification*/
		String mailSubject = "Query Received";
		notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " User,\n We have reveived your query.We solve your issue as soon as possible.Your Token Id : "+crmCommonPoolDTO.getID()+".\nThanks";
		sendMailNotification(crmCommonPoolDTO.getClientID(),moduleID,null,mailSubject,notificationMsg);
		/*mail notification*/
//		return crmCommonPoolDTO.getID();
		
	}
	
	

	private void uploadDocuments(long ID, String []documents, LoginDTO loginDTO ) throws Exception {
		FileService fileService = new FileService();

		//int moduleID =ModuleConstants.Module_ID_CRM;
		int entityTypeID = EntityTypeConstant.CRM;
		long entityID = ID;
		if(documents == null || documents.length == 0)
			return;
		for (int i = 0; i < documents.length; i++) {
			FileDTO fileDTO = new FileDTO();
			fileDTO.setDocOwner(loginDTO.getAccountID()>0?loginDTO.getAccountID():(-loginDTO.getUserID()));
			fileDTO.setDocEntityTypeID(entityTypeID);
			fileDTO.setDocEntityID(entityID);
			fileDTO.setLastModificationTime(System.currentTimeMillis());

			fileDTO.createLocalFileFromNames(documents[i]);
			
			DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
			fileService.insert(fileDTO, databaseConnection);
		}

	}
	
	@Transactional
	public void updateClientComplain(CrmCommonPoolDTO crmCommonPoolDTO) throws Exception{
		crmCommonPoolDAO.updateCrmComplain(crmCommonPoolDTO);
	}
	
	@Transactional
	public CrmCommonPoolDTO getCrmCommonPoolDTOByCommonPoolID(long id) throws Exception{
		return crmCommonPoolDAO.getCrmCommonPoolDTOByCommonPoolID(id);
	}

	/**/
	@SuppressWarnings("rawtypes")
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		Hashtable<String, String> criteriaMap = new Hashtable<>();
		return getIDsWithSearchCriteria(criteriaMap, loginDTO, objects);
	}

	@SuppressWarnings("rawtypes")
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return crmCommonPoolDAO.getCrmClientComplainIDListBySearchCriteria(searchCriteria, loginDTO);
	}

	@SuppressWarnings("rawtypes")
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return crmCommonPoolDAO.getCrmClientCompalinDTOListByComplainIDList(recordIDs);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmCommonPoolDTO getClientComplainByComplainID(long complainID) throws Exception {
		return crmCommonPoolDAO.getClientComplainByComplainID(complainID);
	}
	
	@Transactional
	public void createCrmComplainFromClientCompain(
			CrmClientComplainToCrmComplainMapper crmClientComplainToCrmComplainMapper, LoginDTO loginDTO) throws Exception {
		
		Long crmEmployeeID = crmClientComplainToCrmComplainMapper.getCrmEmployeeID();
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmEmployeeID);
		
		if(crmEmployeeDTO==null || crmEmployeeDTO.getUserID() == null){
			throw new RequestFailureException("Please refresh the page.");
		}
		
		if(!CrmDesignationService.isNOC(loginDTO.getUserID()) || loginDTO.getUserID()!=crmEmployeeDTO.getUserID()){
			throw new RequestFailureException("You do not have permission to handle this complain.");
		}
		
		
		
		List<CrmComplainDTO> crmComplainDTOs = crmClientComplainToCrmComplainMapper.getCrmComplainDTOs();
		
		if(crmComplainDTOs.isEmpty()){
			throw new RequestFailureException("No Sub Complain found to make");
		}
		
		CrmCommonPoolDTO commonPoolDTO = crmCommonPoolDAO.getCrmCommonPoolDTOByCommonPoolID(crmComplainDTOs.get(0).getCommonPoolID());
		
		if(commonPoolDTO == null){
			throw new RequestFailureException("No such Client Complain exists with this ID");
		}
		
		long commonPoolID = -1;
		for (CrmComplainDTO crmComplainDTO : crmComplainDTOs) {
			if(crmComplainDTO != null && crmComplainDTO.getCommonPoolID() != null){
				if(commonPoolID != -1 && commonPoolID != crmComplainDTO.getCommonPoolID()){
					throw new RequestFailureException("Complain pool id tempered!!!");
				}
				commonPoolID = crmComplainDTO.getCommonPoolID();
				crmComplainService.assignComplain(crmComplainDTO, crmClientComplainToCrmComplainMapper.getCrmEmployeeID()
						,loginDTO,commonPoolDTO.getClientID());
			}
		}
		CrmCommonPoolDTO crmCommonPoolDTO = getCrmCommonPoolDTOByCommonPoolID(commonPoolID);
		
		if(crmCommonPoolDTO == null){ 
			throw new RequestFailureException("No Complain found using this id"); 
		}
		crmCommonPoolDTO.setNocEmployeeID(crmClientComplainToCrmComplainMapper.getCrmEmployeeID());
		updateClientComplain(crmCommonPoolDTO);
	}
	
	@Transactional
	public void sendFeedbackToClient(long commonPoolID, int status, String feedbackOfNoc) throws Exception {
		CrmCommonPoolDTO crmCommonPoolDTO = getCrmCommonPoolDTOByCommonPoolID(commonPoolID);
		crmCommonPoolDTO.setStatus(status);
		crmCommonPoolDTO.setFeedbackOfNoc(feedbackOfNoc);
		updateClientComplain(crmCommonPoolDTO);
		
		/* sms notification */
		Integer moduleID = EntityTypeConstant.mapOfModuleIDToMainEntityTypeIdForCrm
				.get(crmCommonPoolDTO.getEntityTypeID());
		String notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " User, Feedback for token id : "+commonPoolID+"is given. Please check your account. Thanks";
		sendSMS(crmCommonPoolDTO,crmCommonPoolDTO.getClientID(),moduleID,null,notificationMsg);	
		/* sms notification */
		
		/*mail notification*/
		String mailSubject = "New Feedback";
		notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " User,\n Feedback for token id : "+commonPoolID+"is given.Please check your account.\n Thanks";
		sendMailNotification(crmCommonPoolDTO.getClientID(),moduleID,null,mailSubject,notificationMsg);
		/*mail notification*/
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmClientComplainSubjectDTO> getComplainSubjectByEntityTypeID(String complainSubject,
			Integer entityTypeID) throws Exception {
		return crmCommonPoolDAO.getComplainSubjectByEntityTypeID(complainSubject,entityTypeID);
	}
	
	@Transactional
	public void updateComplainFromClient(CrmCommonPoolDTO crmCommonPoolDTO, String[] documents,
			LoginDTO loginDTO) throws Exception {
		CrmCommonPoolDTO currentCrmCommonPoolDTO = getCrmCommonPoolDTOByCommonPoolID(crmCommonPoolDTO.getID());
		String currentMessage = currentCrmCommonPoolDTO.getClientComplain();
		currentMessage = crmCommonPoolDTO.getClientComplain() + "<br>----------------------------------------------<br>"
				+ TimeConverter.getTimeStringFromLong(currentCrmCommonPoolDTO.getLastModificationTime()) + "<br><br>"
				+ currentMessage;
		crmCommonPoolDTO = currentCrmCommonPoolDTO;
		crmCommonPoolDTO.setClientComplain(currentMessage);
		updateClientComplain(crmCommonPoolDTO);
		uploadDocuments(currentCrmCommonPoolDTO.getID(), documents, loginDTO);
		
		/* sms notification */
		Integer moduleID = EntityTypeConstant.mapOfModuleIDToMainEntityTypeIdForCrm
				.get(crmCommonPoolDTO.getEntityTypeID());
		String notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " Admin, The complain with token id :"+ crmCommonPoolDTO.getID()+" has been updated. Plz check this.";
		sendSMS(crmCommonPoolDTO,null,null,currentCrmCommonPoolDTO.getNocEmployeeID(),notificationMsg);	
		/* sms notification */
		
		/*mail notification*/
		String mailSubject = "Client Complain Update";
		notificationMsg = "Dear " + ModuleConstants.mapOfModuleNameToActiveModuleID.get(moduleID)
		+ " Admin,\nThe complain with token id :"+ crmCommonPoolDTO.getID()+" has been updated. Please check this.";
		sendMailNotification(null,null,currentCrmCommonPoolDTO.getNocEmployeeID(),mailSubject,notificationMsg);
		/*mail notification*/
	}
	
	//require notification mail + sms
	@Transactional
	public void sendResponseToClient(Long commonPoolID, int status, String feedback,LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		CrmCommonPoolDTO crmCommonPoolDTO = getCrmCommonPoolDTOByCommonPoolID(commonPoolID);
		if(crmCommonPoolDTO == null){
			throw new RequestFailureException("Inconsistent data. Requested CRM common pool DTO is not found with id"+commonPoolID);
		}
		crmCommonPoolDTO.setFeedbackOfNoc(feedback);
		crmCommonPoolDTO.setStatus(status);
		updateClientComplain(crmCommonPoolDTO);
		Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(commonPoolID
				, EntityTypeConstant.CRM_CLIENT_COMPLAIN);
		if(status == CrmComplainDTO.COMPLETED && rootRequestID !=null){
			CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplainComplete(crmCommonPoolDTO.getID()
					, crmCommonPoolDTO.getClientID(), rootRequestID, loginDTO);
			requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
			requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		}
		
	}
	@Transactional
	public void blockClientComplain(long clientComplainID, LoginDTO loginDTO) throws Exception {
		
		
		CrmCommonPoolDTO commonPoolDTO = getCrmCommonPoolDTOByCommonPoolID(clientComplainID);
		
		if(commonPoolDTO==null){
			throw new RequestFailureException("No such common pool found");
		}
		
		if(commonPoolDTO.isBlocked()){
			throw new RequestFailureException("The complain is already blocked");
		}
		
		CrmCommonPoolDTO crmCommonPoolDTO = new CrmCommonPoolDTO();
		crmCommonPoolDTO.setID(clientComplainID);
		crmCommonPoolDTO.setBlocked(true);
		crmCommonPoolDAO.updateBlockedStatus(crmCommonPoolDTO);
		if(loginDTO.getIsAdmin()){
			rejectClientComplainByAdmin(commonPoolDTO,loginDTO);
		}else{
			CancelClientComplainByClient(commonPoolDTO,loginDTO);
		}
	}
	
	
	@Transactional
	public void rejectClientComplainByAdmin(CrmCommonPoolDTO commonPoolDTO
			,LoginDTO loginDTO) throws Exception{

		crmComplainService.rejectCrmComplainByCommonPoolDTOByAdmin(commonPoolDTO.getID(), loginDTO);
		
		commonPoolDTO.setStatus(CrmComplainDTO.REJECTED_BY_ADMIN);
		
		Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(commonPoolDTO.getID(), EntityTypeConstant.CRM_CLIENT_COMPLAIN);
		if(rootRequestID!=null){
			CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplainReject(
					commonPoolDTO.getID(),commonPoolDTO.getClientID(), rootRequestID,loginDTO);
			requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
			requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		}
	}
	
	@Transactional
	public void CancelClientComplainByClient(CrmCommonPoolDTO commonPoolDTO,LoginDTO loginDTO) throws Exception{
		crmComplainService.cancelCrmComplainByCommonPoolID(commonPoolDTO.getID(),loginDTO);
		
		commonPoolDTO.setStatus(CrmComplainDTO.CANCELLED_BY_CLIENT);
		
		Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(commonPoolDTO.getID(), EntityTypeConstant.CRM_CLIENT_COMPLAIN);
		if(rootRequestID!=null){
			CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplainCancel(
					commonPoolDTO.getID(), rootRequestID,commonPoolDTO.getClientID(),loginDTO);
			requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
			requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		}
	}
	
	private CommonRequestDTO createCommonRequestDTOForNewClientComplain(long clientComplainID, long clientID, LoginDTO loginDTO) throws Exception{
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplain(clientComplainID, clientID, CRMRequestTypeConstants.REQUEST_NEW_CLIENT_COMPLAIN.CLIENT_COMPAIN, loginDTO);
		return commonRequestDTO; 
	}
	
	private CommonRequestDTO createCommonRequestDTOForClientComplainCancel(long clientComplainID
			,long rootRequestID,long clientID, LoginDTO loginDTO){
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplain(clientComplainID, clientID, CRMRequestTypeConstants.REQUEST_NEW_CLIENT_COMPLAIN.CLIENT_CANCEL_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	private CommonRequestDTO createCommonRequestDTOForClientComplainReject(long clientComplainID, long clientID
			,long rootRequestID,LoginDTO loginDTO){
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplain(clientComplainID, clientID
				, CRMRequestTypeConstants.REQUEST_NEW_CLIENT_COMPLAIN.SYSTEM_REJECT_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	private CommonRequestDTO createCommonRequestDTOForClientComplainComplete(long clientComplainID, long clientID
			,long rootRequestID,LoginDTO loginDTO) {
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForClientComplain(clientComplainID, clientID, CRMRequestTypeConstants.REQUEST_NEW_CLIENT_COMPLAIN.SYSTEM_COMPLETE_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	
	private CommonRequestDTO createCommonRequestDTOForClientComplain(long clientComplainID, long clientID, int requestTypeID, LoginDTO loginDTO){		
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.CRM_CLIENT_COMPLAIN);
		commonRequestDTO.setEntityID(clientComplainID);
		commonRequestDTO.setRequestTypeID(requestTypeID);
		commonRequestDTO.setRequestByAccountID(loginDTO.getUserID() > 0 ? -loginDTO.getUserID(): loginDTO.getAccountID());
		commonRequestDTO.setExpireTime(new CommonDAO().getExpireTimeByRequestType(commonRequestDTO.getRequestTypeID()));
		commonRequestDTO.setRequestTime(CurrentTimeFactory.getCurrentTime());
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		return commonRequestDTO; 
	}

	

}
