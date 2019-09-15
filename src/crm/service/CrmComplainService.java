package crm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.DAO;
import annotation.Transactional;
import common.CommonDAO;
import common.EntityTypeConstant;
import common.EntityTypeConstant.ActionTypeForCrmComplain;
import common.RequestFailureException;
import crm.CRMRequestTypeConstants;
import crm.CrmActivityLog;
import crm.CrmActivityLogDAO;
import crm.CrmCommonPoolDTO;
import crm.CrmComplainDTO;
import crm.CrmComplainHistoryDTO;
import crm.CrmComplainTreeNode;
import crm.CrmDesignationDTO;
import crm.CrmEmployeeDTO;
import crm.CrmEmployeeDesignationValuePair;
import crm.dao.CrmCommonPoolDAO;
import crm.dao.CrmComplainDAO;
import crm.dao.CrmComplainHistoryDAO;
import crm.dao.CrmDesignationDAO;
import crm.dao.CrmEmployeeDAO;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import login.LoginDTO;
import request.CommonRequestDTO;
import request.RequestDAO;
import request.RequestUtilDAO;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

public class CrmComplainService implements NavigationService {
	@DAO
	CrmComplainDAO complainDAO;
	@DAO
	CrmComplainHistoryDAO complainHistoryDAO;
	@DAO
	CrmEmployeeDAO employeeDAO;
	@DAO
	CrmDesignationDAO designationDAO;
	@DAO
	CrmActivityLogDAO crmActivityLogDAO;
	@Service
	CrmNotificationService crmNotificationService;
	@DAO
	CrmCommonPoolDAO crmCommonPoolDAO;
	@DAO
	RequestDAO requestDAO;	
	@DAO
	RequestUtilDAO requestUtilDAO;
	/*
	 * Complain Types - complain passed by me - complain assigned by me -
	 * complain assigned by subordinates
	 */
	@Transactional(transactionType = TransactionType.READONLY)
	public List<CrmComplainDTO> getCrmComplainListByPoolID(long commonPoolID) throws Exception {
		return complainDAO.getComplainListByPoolID(commonPoolID);
	}

	public List<CrmComplainDTO> getCrmComplainListByResolverID(long employeeID) throws Exception {
		return complainDAO.getCrmComplainListByResolverID(employeeID);
	}

	public List<CrmComplainDTO> getCrmComplainListByAssignerID(long employeeID) throws Exception {
		return complainDAO.getCrmComplainListByAssignerID(employeeID);
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmComplainDTO getComplainDTOByComplainID(long complainID, long userID) throws Exception {
		CrmComplainDTO complainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		validateUserAccessToComplainDTO(complainDTO.getComplainResolverID(), userID);
		return complainDTO;
	}

	@Transactional
	public void completeComplain(long complainID, long userID, String resolverMsg, LoginDTO loginDTO) throws Exception {
		// complain assigner can not complete the complain. He / she can only
		CrmComplainDTO complainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		validateCrmComplainForComplainCreator(complainDTO);
		CrmEmployeeDTO resolverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		validateResolverEmployeeWithUserID(resolverEmployeeDTO, userID);
		attemptToCompleteComplainByResolverEmployee(complainDTO, resolverEmployeeDTO, userID,loginDTO);
		crmNotificationService.sendNotification(complainDTO, ActionTypeForCrmComplain.COMPLETE_TYPE);
	}

	private void attemptToCompleteComplainByResolverEmployee(CrmComplainDTO complainDTO,
			CrmEmployeeDTO resolverEmployeeDTO, long userID,LoginDTO loginDTO) throws Exception {

		if (!resolverEmployeeDTO.isHasPermissionToChangeStatus()) {
			if (complainDTO.getAssignerID() == complainDTO.getComplainResolverID()
					|| complainDTO.getPointerComplainHistoryID() == null) {
				throw new RequestFailureException(
						"You are the assigner of this complain and you do not have permission to change status");
			}
			String feedBackForComplainRejection = "Please take action to complete the complain. My part is done already";
			
			CrmComplainHistoryDTO currentComplainHistoryDTO = complainHistoryDAO
					.getComplainHistoryByComplainHistoryID(complainDTO.getPointerComplainHistoryID());
			currentComplainHistoryDTO.setFeedBack(feedBackForComplainRejection);
			complainHistoryDAO.updateComplainHistoryDTO(currentComplainHistoryDTO);

			updatePreviousActivityLog(complainDTO);
			CrmComplainHistoryDTO crmComplainHistoryDTO = getComplainHistoryDTOForCompletionProcess(complainDTO.getPointerComplainHistoryID());
						
			complainDTO.setPreviousComplainResolverID(complainDTO.getComplainResolverID());
			complainDTO.setCurrentDescription(feedBackForComplainRejection);
			complainDTO.setComplainResolverID(crmComplainHistoryDTO.getComplainResolverID());
			complainDTO.setPointerComplainHistoryID(crmComplainHistoryDTO.getID());
			complainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());
			complainDAO.updateComplain(complainDTO);
			
			
			CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, crmComplainHistoryDTO.getComplainResolverID());
	
			crmActivityLog.setTakenActionType(CrmActivityLog.ATTEMP_TO_COMPLETE_COMPLAIN);
			crmActivityLog.setDescription(feedBackForComplainRejection);
			crmActivityLogDAO.insert(crmActivityLog);
			

		} else {
			updatePreviousActivityLog(complainDTO);
			Long currentResolverID = complainDTO.getComplainResolverID();
			Long currentHistoryPointerID = complainDTO.getPointerComplainHistoryID();
			updateComplainDTOAfterSuccessfulCompletion(complainDTO, currentResolverID);
			updateComplainHistoryAfterSuccessfulCompletion(complainDTO, currentResolverID, currentHistoryPointerID);

			CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, complainDTO.getAssignerID());
			crmActivityLog.setTakenActionType(CrmActivityLog.COMPLETE_COMPLAIN);
			crmActivityLog.setDescription("Complain Completed");
			crmActivityLog.setTimeOfTakenAction(CurrentTimeFactory.getCurrentTime());
			crmActivityLogDAO.insert(crmActivityLog);
			Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(complainDTO.getID(), EntityTypeConstant.CRM_COMPLAIN);
			CrmCommonPoolDTO crmCommonPoolDTO = crmCommonPoolDAO.getClientComplainByComplainID(complainDTO.getCommonPoolID());
			if(rootRequestID!=null && crmCommonPoolDTO !=null){
				
				CommonRequestDTO commonRequestDTO = 
						createCommonRequestDTOForCrmComplainComplete(complainDTO.getID(), rootRequestID,loginDTO);
				requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
				requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
			}
		}
	}

	private void updateComplainHistoryAfterSuccessfulCompletion(CrmComplainDTO complainDTO, Long currentResolverID,
			Long currentHistoryPointerID) throws Exception {
		CrmComplainHistoryDTO crmComplainHistoryDTO = createComplainHistoryDTOForCompletingComplain(complainDTO);
		crmComplainHistoryDTO.setActionTakerID(currentResolverID);
		crmComplainHistoryDTO.setParentComplainHistoryID(currentHistoryPointerID);
		crmComplainHistoryDTO.setComplainResolverID(complainDTO.getAssignerID());
		complainHistoryDAO.insertComplainHistory(crmComplainHistoryDTO);
	}

	private void updateComplainDTOAfterSuccessfulCompletion(CrmComplainDTO complainDTO, Long currentResolverID)
			throws Exception {
		complainDTO.setComplainResolverID(complainDTO.getAssignerID());
		complainDTO.setPreviousComplainResolverID(currentResolverID);
		complainDTO.setPointerComplainHistoryID(null);
		complainDTO.setCurrentStatus(CrmComplainDTO.COMPLETED);
		complainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());
		complainDAO.updateComplain(complainDTO);
	}

	private void validateCrmComplainForComplainCreator(CrmComplainDTO complainDTO) {
		if (complainDTO.getPointerComplainHistoryID() == null) {
			throw new RequestFailureException("This action can not be completed. As it has been created by you");
		}
	}

	private void validateResolverEmployeeWithUserID(CrmEmployeeDTO resolverEmployeeDTO, long userID) {
		if (resolverEmployeeDTO == null) {
			throw new RequestFailureException("Invalid resolver employee");
		}
		if (resolverEmployeeDTO.getUserID() == null) {
			throw new RequestFailureException("No employee found assigned to this login userID");
		}
		if (resolverEmployeeDTO.getUserID() != userID) {
			throw new RequestFailureException("You do not have permission to handle this complain.");
		}
	}

	private CrmComplainHistoryDTO createComplainHistoryDTOForCompletingComplain(CrmComplainDTO complainDTO)
			throws Exception {// implement
		CrmComplainHistoryDTO crmComplainHistoryDTO = createComplainHistoryDTO(complainDTO, "Complain is solved");
		crmComplainHistoryDTO.setRootComplainHistoryID(complainDTO.getRootCompalinHistoryID());
		crmComplainHistoryDTO.setStatus(CrmComplainDTO.COMPLETED);
		return crmComplainHistoryDTO;
	}

	@Transactional
	public void sendFeedBackList(List<Long> complainIDList, String feedBackMsg, long userID) throws Exception {
		for (long complainID : complainIDList) {
			sendFeedBack(complainID, feedBackMsg, userID);
		}
	}

	@Transactional
	public void sendFeedBack(long complainID, String feedBackMsg, long userID) throws Exception {
		CrmComplainDTO complainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		validateCrmComplainForComplainCreator(complainDTO);
		CrmEmployeeDTO resolverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		validateResolverEmployeeWithUserID(resolverEmployeeDTO, userID);

		updateComplainHistoryAndComplainForFeedback(complainDTO, feedBackMsg);
		crmNotificationService.sendNotification(complainDTO, ActionTypeForCrmComplain.FEEDBACK_TYPE);
	}

	private void updateComplainHistoryAndComplainForFeedback(CrmComplainDTO complainDTO, String feedBackMsg)
			throws Exception {
		CrmComplainHistoryDTO currentComplainHistoryDTO = new CrmComplainHistoryDTO();
		currentComplainHistoryDTO.setID(complainDTO.getPointerComplainHistoryID());
		currentComplainHistoryDTO.setFeedBack(feedBackMsg);
		currentComplainHistoryDTO.setActionTakerID(complainDTO.getComplainResolverID());
		// crmComplainHistoryDTO.setComplainResolverID(crmComplainDTO.getComplainResolverID());
		updatePreviousActivityLog(complainDTO);
		complainHistoryDAO.updateComplainHistoryDTO(currentComplainHistoryDTO,
				new String[] { "feedBack", "lastModification", "actionTakerID" });

		updateComplainForFeedback(complainDTO, currentComplainHistoryDTO);

	}

	private long getNextIDOfActivityLogTable() throws Exception {
		String tableName = ModifiedSqlGenerator.getTableName(CrmActivityLog.class);
		return DatabaseConnectionFactory.getCurrentDatabaseConnection().getNextIDWithoutIncrementing(tableName);
	}

	private void updateComplainForFeedback(CrmComplainDTO complainDTO, CrmComplainHistoryDTO currentComplainHistoryDTO)
			throws Exception {
		CrmComplainHistoryDTO ansestorComplainHistoryDTO = getAnsestorComplainHistoryWithOtherEmployee(
				complainDTO.getPointerComplainHistoryID(), complainDTO.getComplainResolverID());

		long currentResolverID = complainDTO.getComplainResolverID();

		
		complainDTO.setPreviousComplainResolverID(complainDTO.getComplainResolverID());
		complainDTO.setLastResolverMsg(currentComplainHistoryDTO.getFeedBack());
		
		updatePreviousActivityLog(complainDTO);
		
		complainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());

		CrmActivityLog crmActivityLog = null;
				
				
				
		if (ansestorComplainHistoryDTO != null) {
			complainDTO.setComplainResolverID(ansestorComplainHistoryDTO.getComplainResolverID());
			complainDTO.setPointerComplainHistoryID(ansestorComplainHistoryDTO.getID());
			crmActivityLog = createActivityLogByCrmComplain(complainDTO, ansestorComplainHistoryDTO.getComplainResolverID());
		} else {
			complainDTO.setComplainResolverID(complainDTO.getAssignerID());
			complainDTO.setPointerComplainHistoryID(null);
			crmActivityLog = createActivityLogByCrmComplain(complainDTO, complainDTO.getAssignerID());
		}
		crmActivityLog.setDescription(currentComplainHistoryDTO.getFeedBack());
		crmActivityLog.setTakenActionType(CrmActivityLog.COMPLAIN_FEEDBACK);
		crmActivityLogDAO.insert(crmActivityLog);
		
		
		CrmEmployeeDTO assigneeEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		CrmDesignationDTO assigneeDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(assigneeEmployeeDTO.getInventoryCatagoryTypeID());
		complainDTO.setAssignedToDesignationID(assigneeDesignationDTO.getDesignationID());
		complainDAO.updateComplain(complainDTO);

	}

	@Transactional
	public void rejectComplain(long complainID, String rejectionCause, long userID) throws Exception { // implement
		CrmComplainDTO complainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		validateCrmComplainForRejectComplain(complainDTO);
		updatePreviousActivityLog(complainDTO);
		rejectComplain(complainDTO, rejectionCause, userID);
	}

	private void validateCrmComplainForRejectComplain(CrmComplainDTO complainDTO) {
		if (complainDTO == null) {
			throw new RequestFailureException("No such complain exists.");
		}
		validateCrmComplainForComplainCreator(complainDTO);
	}

	public void rejectComplain(CrmComplainDTO complainDTO, String rejectionCause, long userID) throws Exception {

		CrmEmployeeDTO resolverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		validateResolverEmployeeWithUserID(resolverEmployeeDTO, userID);

		updateComplainHistoryAndComplainForReject(complainDTO, rejectionCause);
		crmNotificationService.sendNotification(complainDTO, ActionTypeForCrmComplain.REJECT_TYPE);
		
	}


	private void updateComplainHistoryAndComplainForReject(CrmComplainDTO complainDTO, String rejectionCause)
			throws Exception {
		CrmComplainHistoryDTO currentComplainHistoryDTO = new CrmComplainHistoryDTO();
		currentComplainHistoryDTO.setID(complainDTO.getPointerComplainHistoryID());
		currentComplainHistoryDTO.setFeedBack(rejectionCause);
		currentComplainHistoryDTO.setActionTakerID(complainDTO.getComplainResolverID());
		complainHistoryDAO.updateComplainHistoryDTO(currentComplainHistoryDTO,
				new String[] { "feedBack", "lastModification", "actionTakerID" });

		updateComplainForReject(complainDTO, currentComplainHistoryDTO, rejectionCause);

	}

	private void updateComplainForReject(CrmComplainDTO complainDTO, CrmComplainHistoryDTO currentComplainHistoryDTO,
			String rejectionCause) throws Exception {
		CrmComplainHistoryDTO ansestorComplainHistoryDTO = getAnsestorComplainHistoryWithOtherEmployee(
				complainDTO.getPointerComplainHistoryID(), complainDTO.getComplainResolverID());
		complainDTO.setPreviousComplainResolverID(complainDTO.getComplainResolverID());
		complainDTO.setLastResolverMsg(currentComplainHistoryDTO.getFeedBack());



		CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, ansestorComplainHistoryDTO == null? complainDTO.getAssignerID():ansestorComplainHistoryDTO.getComplainResolverID());
		
		if (ansestorComplainHistoryDTO != null) {
			complainDTO.setComplainResolverID(ansestorComplainHistoryDTO.getComplainResolverID());
			complainDTO.setCurrentStatus(ansestorComplainHistoryDTO.getStatus());
			complainDTO.setPointerComplainHistoryID(ansestorComplainHistoryDTO.getID());
		} else {
			complainDTO.setComplainResolverID(complainDTO.getAssignerID());
			complainDTO.setCurrentStatus(CrmComplainDTO.STARTED);
			complainDTO.setPointerComplainHistoryID(null);
		}
		crmActivityLog.setTakenActionType(CrmActivityLog.COMPLAIN_REJECT);
		crmActivityLog.setDescription(rejectionCause);
		
		CrmEmployeeDTO resolverEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		CrmDesignationDTO resolverDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(resolverEmployeeDTO.getInventoryCatagoryTypeID());
		complainDTO.setAssignedToDesignationID(resolverDesignationDTO.getDesignationID());
		complainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());
		complainDAO.updateComplain(complainDTO);
		crmActivityLogDAO.insert(crmActivityLog);
	}

	@Transactional
	public void passComplain(long complainID, long passedToEmployeeID, long userID, String passingMessage)
			throws Exception {
		CrmComplainDTO crmComplainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		Long currentResolverEmployeeID = crmComplainDTO.getComplainResolverID();
		CrmEmployeeDTO currentResolverEmployee = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(currentResolverEmployeeID);
		CrmEmployeeDTO passedToEmployee = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(passedToEmployeeID);

		checkPassPermission(currentResolverEmployee);
		validateResolverEmployeeWithUserID(currentResolverEmployee, userID);
		checkPassPermissionToOtherDepartment(crmComplainDTO, currentResolverEmployee, passedToEmployee);

		updatePreviousActivityLog(crmComplainDTO);
		attemptToPassComplain(crmComplainDTO, passedToEmployeeID, passingMessage);


		
		CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(crmComplainDTO, passedToEmployeeID);
		crmActivityLog.setTakenActionType(CrmActivityLog.COMPLAIN_PASS);
		crmActivityLog.setDescription(passingMessage);
		crmActivityLogDAO.insert(crmActivityLog);
		updateComplainHistoryAndComplainForPass(crmComplainDTO, passedToEmployee, passedToEmployeeID, passingMessage);
		crmNotificationService.sendNotification(crmComplainDTO, ActionTypeForCrmComplain.PASS_TYPE);
	}
	
	

	/*
	 * This method returns employee ID staying on complain history and has
	 * permission to change complain status.
	 */
	private CrmComplainHistoryDTO getComplainHistoryDTOForCompletionProcess(long currentComplainHistoryID)
			throws Exception {
		CrmComplainHistoryDTO complainHistoryDTO = complainHistoryDAO
				.getComplainHistoryByComplainHistoryID(currentComplainHistoryID);
		if (complainHistoryDTO == null) {
			throw new Exception("Invalid situation. This case only happens if the complain assigner does not "
					+ "have the permission to change complain status");
		}
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(complainHistoryDTO.getComplainResolverID());
		if (crmEmployeeDTO == null) {
			throw new Exception("Employee is deleted after passing or forwarding a complain");
		}
		if (crmEmployeeDTO.isHasPermissionToChangeStatus()) {
			return complainHistoryDTO;
		}
		if (complainHistoryDTO.getParentComplainHistoryID() == null) {
			throw new Exception("Invalid situation. This case only happens if the complain assigner does not "
					+ "have the permission to change complain status");
		}
		return getComplainHistoryDTOForCompletionProcess(complainHistoryDTO.getParentComplainHistoryID());
	}

	private void updateComplainHistoryAndComplainForPass(CrmComplainDTO crmComplainDTO, CrmEmployeeDTO passedToEmployee,
			long passedToEmployeeID, String passingMessage) throws Exception {
		CrmComplainHistoryDTO crmComplainHistoryDTO = new CrmComplainHistoryDTO();
		if (crmComplainDTO.getPointerComplainHistoryID() == null) {
			// if complain is rejected to the assigner then he actually assigns
			// the complain again
			crmComplainHistoryDTO = createComplainHistoryDTOForNewComplain(crmComplainDTO);
			crmComplainHistoryDTO.setDescription(passingMessage);
			crmComplainHistoryDTO.setActionTakerID(crmComplainDTO.getPreviousComplainResolverID());
			crmComplainHistoryDTO.setComplainResolverID(crmComplainDTO.getComplainResolverID());
			complainHistoryDAO.insertComplainHistory(crmComplainHistoryDTO);
			crmComplainDTO.setRootCompalinHistoryID(crmComplainHistoryDTO.getID());
		} else {
			crmComplainHistoryDTO = createComplainHistoryDTOForPassingComplain(crmComplainDTO, passedToEmployeeID);
			crmComplainHistoryDTO.setDescription(passingMessage);
			crmComplainHistoryDTO.setActionTakerID(crmComplainDTO.getPreviousComplainResolverID());
			crmComplainHistoryDTO.setComplainResolverID(crmComplainDTO.getComplainResolverID());
			complainHistoryDAO.insertComplainHistory(crmComplainHistoryDTO);
		}

		updateComplainForPass(crmComplainDTO, passedToEmployee, crmComplainHistoryDTO);

	}

	private void updateComplainForPass(CrmComplainDTO crmComplainDTO, CrmEmployeeDTO passedToEmployee,
			CrmComplainHistoryDTO crmComplainHistoryDTO) throws Exception {
		CrmDesignationDTO designationOfPassedToEmployee = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByInventoryCategoryID(passedToEmployee.getInventoryCatagoryTypeID());
		crmComplainDTO.setAssignedToDesignationID(designationOfPassedToEmployee.getDesignationID());
		crmComplainDTO.setPointerComplainHistoryID(crmComplainHistoryDTO.getID());
		complainDAO.updateComplain(crmComplainDTO);

	}

	private void attemptToPassComplain(CrmComplainDTO crmComplainDTO, long passedToEmployeeID, String passingMessage)
			throws Exception {
		crmComplainDTO.setPreviousComplainResolverID(crmComplainDTO.getComplainResolverID());
		crmComplainDTO.setComplainResolverID(passedToEmployeeID);
		crmComplainDTO.setLastResolverMsg(passingMessage);
		String tableName = ModifiedSqlGenerator.getTableName(CrmActivityLog.class);
		long newIDOfActivityLog = DatabaseConnectionFactory.getCurrentDatabaseConnection()
				.getNextIDWithoutIncrementing(tableName);
		crmComplainDTO.setLastActivityLogID(newIDOfActivityLog);
	}

	private void checkPassPermissionToOtherDepartment(CrmComplainDTO crmComplainDTO, CrmEmployeeDTO resolverEmployeeDTO,
			CrmEmployeeDTO passedToEmployee) {
		CrmDesignationDTO designationOfPassedToEmployee = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByInventoryCategoryID(passedToEmployee.getInventoryCatagoryTypeID());
		CrmDesignationDTO rootDesignationOfPassedToEmployee = CrmAllDesignationRepository.getInstance()
				.getRootDesignationDTOByDesignationID(designationOfPassedToEmployee.getDesignationID());
		CrmDesignationDTO rootDesignationOfCurrentEmployee = CrmAllDesignationRepository.getInstance()
				.getRootDesignationDTOByDesignationID(crmComplainDTO.getAssignedToDesignationID());
		if (rootDesignationOfPassedToEmployee.getDesignationID() != rootDesignationOfCurrentEmployee
				.getDesignationID()) {
			if (!resolverEmployeeDTO.isHasPermissionToPassComplainToOtherDepartment()) {
				throw new RequestFailureException("You do not have permission to pass complain to other department");
			}
		}
	}

	private void checkPassPermission(CrmEmployeeDTO resolverEmployeeDTO) {
		if (!resolverEmployeeDTO.isHasPermissionToPassComplain()) {
			throw new RequestFailureException("You do not have permission to pass this complain");
		}

	}

	/*
	 * The current resolver will be the previous resolver after taking action on
	 * this complain.
	 */
	private CrmActivityLog createActivityLogByCrmComplain(CrmComplainDTO crmComplainDTO,
			long nextResolverID) throws Exception {
		
	
		long crmComplainID = crmComplainDTO.getID();
		long currentResolverID = crmComplainDTO.getComplainResolverID();
		long commonPoolID = crmComplainDTO.getCommonPoolID();
		
		CrmCommonPoolDTO crmCommonPoolDTO = crmCommonPoolDAO.getClientComplainByComplainID(commonPoolID);
		
		
		
		CrmActivityLog crmActivityLog = new CrmActivityLog();

		crmActivityLog.setCrmComplainID(crmComplainID);

		long prevResolverID = currentResolverID;
		CrmEmployeeDTO prevCrmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(prevResolverID);
		if (prevCrmEmployeeDTO == null) {
			throw new Exception(
					"current employee is invalid" + ".That means no employee is found with the employee ID");
		}

		crmActivityLog.setPreviousEmployeeID(currentResolverID);
		crmActivityLog.setPreviousUserID(prevCrmEmployeeDTO.getUserID());

		CrmEmployeeDTO currentEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(nextResolverID);
		if (currentEmployeeDTO == null) {
			throw new RequestFailureException("No employee found with ID " + nextResolverID);
		}
		crmActivityLog.setCurrentEmployeeID(currentEmployeeDTO.getCrmEmployeeID());
		crmActivityLog.setCurrentUserID(currentEmployeeDTO.getUserID());
		crmActivityLog.setFromTimeForEmployeeToTakeAction(CurrentTimeFactory.getCurrentTime());
		
		
		crmActivityLog.setClientID(crmCommonPoolDTO.getClientID());
		crmActivityLog.setEntityID(crmCommonPoolDTO.getEntityID());
		crmActivityLog.setEntityTypeID(crmCommonPoolDTO.getEntityTypeID());
		
		
		return crmActivityLog;

	}

	@Transactional
	public void passComplainList(List<Long> complainIDList, long passedToEmployeeID, long userID, String passingMessage)
			throws Exception {

		for (long complainID : complainIDList) {
			passComplain(complainID, passedToEmployeeID, userID, passingMessage);
		}
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmComplainTreeNode> getWaitingComplainTreeNodeListByUserID(long userID) throws Exception {
		List<CrmEmployeeDTO> employeeDTOs = employeeDAO.getEmployeeDTOListByUserID(userID);

		List<CrmComplainTreeNode> complainTreeNodes = new ArrayList<>();

		for (CrmEmployeeDTO employeeDTO : employeeDTOs) {
			complainTreeNodes.addAll(getWaitingComplainTreeNodeListByEmployeeID(employeeDTO.getCrmEmployeeID()));
		}

		return complainTreeNodes;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmComplainTreeNode> getWaitingComplainTreeNodeListByEmployeeID(long employeeID) throws Exception {

		List<CrmComplainTreeNode> waitingComplainNodes = complainDAO.getComplainTreeNodeListByEmployeeID(
				employeeID);/* need to change Node to DTO */

		for (CrmComplainTreeNode crmComplainTreeNode : waitingComplainNodes) {
			crmComplainTreeNode.setChildCrmComplainNodeList(getChildComplainNodeListByParentNode(crmComplainTreeNode));
		}

		return waitingComplainNodes;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmComplainTreeNode getComplainTreeNodeByComplainID(long complainID) throws Exception {
		return null;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmComplainTreeNode> getComplainTreeNodeListByParentComplainID(long parentComplainID) throws Exception {
		return null;
	}

	@Transactional
	public void sendComplainForCompletionApproval(long complainID) throws Exception {

	}

	public void assignComplain(CrmComplainDTO complainDTO, long employeeID,LoginDTO loginDTO,
			long clientIDOfTheComplain) throws Exception {
		// check if complain assigner can assign problem to assignee
		long currentTime = System.currentTimeMillis();
		CrmEmployeeDTO assignerCrmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(employeeID);
		if (!assignerCrmEmployeeDTO.isHasPermissionToAssignComplain()) {
			throw new RequestFailureException("You do not have permission to assign complain");
		}

		CrmEmployeeDTO assigneeCrmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(complainDTO.getComplainResolverID());
		CrmDesignationDTO assigneeCrmDesignationDTO = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByInventoryCategoryID(assigneeCrmEmployeeDTO.getInventoryCatagoryTypeID());

		complainDTO.setGenerationTime(currentTime);
		complainDTO.setAssignmentTime(currentTime);
		complainDTO.setAssignerID(assignerCrmEmployeeDTO.getCrmEmployeeID());
		complainDTO.setAssignedToDesignationID(assigneeCrmDesignationDTO.getDesignationID());
		complainDTO.setCurrentStatus(CrmComplainDTO.STARTED);

		CrmComplainHistoryDTO crmComplainHistoryDTO = createComplainHistoryDTOForNewComplain(complainDTO);
		if (crmComplainHistoryDTO.getParentComplainHistoryID() == null) {
			crmComplainHistoryDTO.setRootComplainHistoryID(DatabaseConnectionFactory.getCurrentDatabaseConnection()
					.getNextIDWithoutIncrementing("at_crm_complain_history"));
		}
		crmComplainHistoryDTO.setActionTakerID(assignerCrmEmployeeDTO.getCrmEmployeeID());
		crmComplainHistoryDTO.setComplainResolverID(complainDTO.getComplainResolverID());
		complainHistoryDAO.insertComplainHistory(crmComplainHistoryDTO);

		complainDTO.setPointerComplainHistoryID(crmComplainHistoryDTO.getID());
		complainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());
		complainDTO.setRootCompalinHistoryID(crmComplainHistoryDTO.getID());
		complainDAO.insertComplain(complainDTO);

		
		//insert activity log
		
		CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, crmComplainHistoryDTO.getComplainResolverID());
		crmActivityLog.setDescription(complainDTO.getCurrentDescription());
		crmActivityLog.setTakenActionType(CrmActivityLog.COMPLAIN_ASSIGN);
		crmActivityLogDAO.insert(crmActivityLog);
		crmNotificationService.sendNotification(complainDTO, ActionTypeForCrmComplain.ASSIGN_TYPE);

		// insert common request dto
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForNewCrmComplain(complainDTO.getID(),loginDTO);
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
		
	}

	private CrmComplainHistoryDTO createComplainHistoryDTOForNewComplain(CrmComplainDTO complainDTO) throws Exception {// implement
		return createComplainHistoryDTO(complainDTO, "Complain is created");
	}

	private CrmComplainHistoryDTO createComplainHistoryDTOForPassingComplain(CrmComplainDTO complainDTO,
			long passedToID) throws Exception {// implement
		CrmComplainHistoryDTO crmComplainHistoryDTO = createComplainHistoryDTO(complainDTO, "Complain is passed");
		crmComplainHistoryDTO.setParentComplainHistoryID(complainDTO.getPointerComplainHistoryID());
		if (crmComplainHistoryDTO.getParentComplainHistoryID() == null) {
			crmComplainHistoryDTO.setRootComplainHistoryID(DatabaseConnectionFactory.getCurrentDatabaseConnection()
					.getNextIDWithoutIncrementing("at_crm_complain_history"));
		} else {
			crmComplainHistoryDTO.setRootComplainHistoryID(complainDTO.getRootCompalinHistoryID());
		}
		crmComplainHistoryDTO.setComplainResolverID(passedToID);
		return crmComplainHistoryDTO;
	}

	@Transactional
	public void changeStatusOfComplain(long complainID, int newStatus, long userID,String resolverMsg, LoginDTO loginDTO) throws Exception {
		CrmComplainDTO currentCrmComplainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		validCrmComplainForChangeStatus(currentCrmComplainDTO, newStatus);

		CrmEmployeeDTO resolverEmployeeDTO = CrmAllEmployeeRepository.getInstance()
				.getCrmEmployeeDTOByEmployeeID(currentCrmComplainDTO.getComplainResolverID());
		validateResolverEmployeeWithUserID(resolverEmployeeDTO, userID);

		if (!resolverEmployeeDTO.isHasPermissionToChangeStatus()) {
			throw new RequestFailureException("you do not have status change permission");
		}
		if (currentCrmComplainDTO.getCurrentStatus() == newStatus) {
			return;
		}
		

		if(newStatus == CrmComplainDTO.COMPLETED ){
			completeComplain(complainID, userID, resolverMsg, loginDTO);
			
		}else{
		
			int oldStatus = currentCrmComplainDTO.getCurrentStatus();
			currentCrmComplainDTO.setCurrentStatus(newStatus);
			currentCrmComplainDTO.setLastResolverMsg(resolverMsg);
			CrmComplainHistoryDTO crmComplainHistoryDTO = addHistoryForUpdateStatus(currentCrmComplainDTO, oldStatus);
	
			currentCrmComplainDTO.setPointerComplainHistoryID(crmComplainHistoryDTO.getID());
	
			updatePreviousActivityLog(currentCrmComplainDTO);
			currentCrmComplainDTO.setLastActivityLogID(getNextIDOfActivityLogTable());
			complainDAO.updateComplain(currentCrmComplainDTO);
	
			CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(currentCrmComplainDTO, currentCrmComplainDTO.getComplainResolverID());
			crmActivityLog.setDescription("Status changed from " + CrmComplainDTO.mapComplainStatusStringToStatusID.get(oldStatus) + " to " + CrmComplainDTO.mapComplainStatusStringToStatusID.get(newStatus));
			crmActivityLog.setTakenActionType(CrmActivityLog.COMPLAIN_STATUS_CHANGE);
			crmActivityLogDAO.insert(crmActivityLog);
		}
	}

	private void updatePreviousActivityLog(CrmComplainDTO crmComplainDTO) throws Exception {
		if (crmComplainDTO.getLastActivityLogID() != null) {
			CrmActivityLog crmActivityLog = new CrmActivityLog();
			crmActivityLog.setID(crmComplainDTO.getLastActivityLogID());
			crmActivityLog.setTimeOfTakenAction(CurrentTimeFactory.getCurrentTime());
			crmActivityLogDAO.markActivityLogAsCompleted(crmActivityLog);
		}
	}

	private void validCrmComplainForChangeStatus(CrmComplainDTO currentCrmComplainDTO, int newStatus) {
		if (newStatus == CrmComplainDTO.COMPLETED) {
			throw new RequestFailureException("You can not change the status of this complain further");
		}
		if (currentCrmComplainDTO.getCurrentStatus() == CrmComplainDTO.COMPLETED) {
			throw new RequestFailureException("This complain is already completed");
		}
	}

	private CrmComplainHistoryDTO addHistoryForUpdateStatus(CrmComplainDTO crmComplainDTO, int oldStatus)
			throws Exception {
		CrmComplainHistoryDTO crmComplainHistoryDTO = createComplainHistoryDTO(crmComplainDTO,
				"Complain status is change form " + CrmComplainDTO.mapComplainStatusStringToStatusID.get(oldStatus)
						+ " to "
						+ CrmComplainDTO.mapComplainStatusStringToStatusID.get(crmComplainDTO.getCurrentStatus()));

		// status set internally form "createComplainHistoryDTO" method.
		crmComplainHistoryDTO.setActionTakerID(crmComplainDTO.getComplainResolverID());
		crmComplainHistoryDTO.setComplainResolverID(crmComplainDTO.getComplainResolverID());

		crmComplainHistoryDTO.setRootComplainHistoryID(crmComplainDTO.getRootCompalinHistoryID());
		crmComplainHistoryDTO.setParentComplainHistoryID(crmComplainDTO.getPointerComplainHistoryID());
		complainHistoryDAO.insertComplainHistory(crmComplainHistoryDTO);

		return crmComplainHistoryDTO;

	}

	private CrmComplainHistoryDTO createComplainHistoryDTO(CrmComplainDTO complainDTO, String description)
			throws Exception {
		CrmComplainHistoryDTO crmComplainHistoryDTO = new CrmComplainHistoryDTO();

		crmComplainHistoryDTO.setStatus(complainDTO.getCurrentStatus());
		crmComplainHistoryDTO.setDescription(description);
		crmComplainHistoryDTO
				.setFeedBack("");/* Blank need to change: implement */
		crmComplainHistoryDTO.setComplainResolverID(complainDTO.getComplainResolverID());
		/* Random data, need to change: implement */
		crmComplainHistoryDTO.setComplainHistorySubmissionTime(System.currentTimeMillis());
		crmComplainHistoryDTO.setDeleted(false);
		crmComplainHistoryDTO.setLastModificationTime(complainDTO.getLastModificationTime());

		return crmComplainHistoryDTO;
	}

	/**/
	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection<?> getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		Hashtable<String, String> criteriaMap = new Hashtable<>();
		return getIDsWithSearchCriteria(criteriaMap, loginDTO, objects);
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection<Long> getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		CrmEmployeeService crmEmployeeService = (CrmEmployeeService) ServiceDAOFactory
				.getService(CrmEmployeeService.class);
		List<Long> resolverEmployeeIDList = null;
		if (!CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(loginDTO.getUserID()).isEmpty()) {

			List<CrmEmployeeDesignationValuePair> employeeDesignationValuePairs = null;
			String employeeNamePartial = (String) searchCriteria.get("employeeName");

			employeeNamePartial = employeeNamePartial == null ? "" : employeeNamePartial;

			if (CrmDesignationService.isNOC(loginDTO.getUserID())) {
			
			} else {
				
				resolverEmployeeIDList = new ArrayList<>();
				List<CrmEmployeeDTO> crmEmployeeDTOs = employeeDAO.getEmployeeDTOListByUserID(loginDTO.getUserID());

				for (CrmEmployeeDTO crmEmployeeDTO : crmEmployeeDTOs) {
					long employeeID = crmEmployeeDTO.getCrmEmployeeID();
					if (employeeNamePartial.isEmpty()) {
						resolverEmployeeIDList.add(employeeID);
					} else {
						if (crmEmployeeDTO.getName().contains(employeeNamePartial)) {
							resolverEmployeeIDList.add(employeeID);
						}
					}

					employeeDesignationValuePairs = crmEmployeeService
							.getDesignationEmloyeeListByPartialNameAndAnsestorCrmEmployeeID(employeeNamePartial, employeeID,null,loginDTO);
					for (CrmEmployeeDesignationValuePair crmEmployeeDesignationValuePair : employeeDesignationValuePairs) {
						resolverEmployeeIDList.add(crmEmployeeDesignationValuePair.getEmployeeID());
					}

				}

			}

		}
		return complainDAO.getCrmComplainIDListBySearchCriteria(searchCriteria, resolverEmployeeIDList);
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	@Override
	public Collection<?> getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return complainDAO.getCrmCompalinDTOListByComplainIDList(recordIDs);
	}

	public CrmComplainHistoryDTO getComplainHistoryDTOByComplainHisotryID(long complainHisotryID) throws Exception {
		return complainDAO.getComplainHistoryDTOByComplainHisotryID(complainHisotryID);
	}

	public List<CrmComplainHistoryDTO> getCrmComplainHistoryDTOListByRootComplainHistoryID(Long rootComplainHistoryID)
			throws Exception {

		return complainDAO.getCrmComplainHistoryDTOListByRootComplainHistoryID(rootComplainHistoryID);
	}

	/* not used yet */
	private List<CrmComplainTreeNode> getChildComplainNodeListByParentNode(CrmComplainTreeNode complainTreeNode)
			throws Exception {
		List<CrmComplainTreeNode> childComplainList = complainDAO
				.getChildComplainListByParentComplainID(complainTreeNode.getID());
		for (CrmComplainTreeNode childComplainNode : childComplainList) {
			childComplainNode.setChildCrmComplainNodeList(getChildComplainNodeListByParentNode(childComplainNode));
		}
		return childComplainList;
	}

	/* unused */
	public String getMsgOfAComplain(CrmComplainDTO complainDTO) throws Exception {

		CrmComplainHistoryDTO complainHistoryDTO = getLatestComplainHistroyForFeedBackWithAnotherResolver(
				complainDTO.getPointerComplainHistoryID(), complainDTO.getRootCompalinHistoryID(),
				complainDTO.getComplainResolverID());
		if (complainHistoryDTO != null) {
			return complainHistoryDTO.getFeedBack();
		}
		complainHistoryDTO = getComplainHistroyOrAnsestorWithComplainIDAndResolverID(
				complainDTO.getPointerComplainHistoryID(), complainDTO.getComplainResolverID());
		return complainDTO != null ? complainHistoryDTO.getDescription() : "";
	}

	private void validateUserAccessToComplainDTO(long crmEmployeeID, long userID) { // implement

		// validate user
	}

	private CrmComplainHistoryDTO getComplainHistroyOrAnsestorWithComplainIDAndResolverID(Long complainHistoryID,
			long complainResolverID) throws Exception {
		if (complainHistoryID == null) {
			return null;
		}
		CrmComplainHistoryDTO complainHistoryDTO = complainHistoryDAO
				.getComplainHistoryByComplainHistoryID(complainHistoryID);
		if (complainHistoryDTO == null) {
			return null;
		}
		if (complainHistoryDTO.getID() == complainHistoryID
				&& complainHistoryDTO.getComplainResolverID() == complainResolverID) {
			return complainHistoryDTO;
		} else {
			return getComplainHistroyOrAnsestorWithComplainIDAndResolverID(
					complainHistoryDTO.getParentComplainHistoryID(), complainResolverID);
		}
	}

	private CrmComplainHistoryDTO getLatestComplainHistroyForFeedBackWithAnotherResolver(Long complainHistroyID,
			long rootComplainHistoryID, long resolverID) throws Exception {

		CrmComplainHistoryDTO complainHistoryDTO = null;
		if (complainHistroyID == null) {
			complainHistoryDTO = complainHistoryDAO.getComplainHistoryByComplainHistoryID(rootComplainHistoryID);
		} else {
			complainHistoryDTO = complainHistoryDAO.getLatestComplainHistoryDTOByParentHistoryID(complainHistroyID);
		}

		if (complainHistoryDTO == null
				|| (complainHistoryDTO != null && complainHistoryDTO.getComplainResolverID() != resolverID)) {
			return complainHistoryDTO;
		} else {
			return getLatestComplainHistroyForFeedBackWithAnotherResolver(complainHistoryDTO.getID(),
					rootComplainHistoryID, resolverID);
		}
	}

	private CrmComplainHistoryDTO getAnsestorComplainHistoryWithOtherEmployee(Long complainHistoryID, long employeeID)
			throws Exception {
		if (complainHistoryID == null) {
			return null;
		}
		CrmComplainHistoryDTO complainHistoryDTO = complainHistoryDAO
				.getParentComplainHistoryByComplainHistoryID(complainHistoryID);
		if (complainHistoryDTO == null) {
			return null;
		}
		if (complainHistoryDTO.getComplainResolverID() != employeeID) {
			return complainHistoryDTO;
		}
		return getAnsestorComplainHistoryWithOtherEmployee(complainHistoryDTO.getID(), employeeID);
	}
	@Transactional
	public void blockCrmComplain(long complainID, LoginDTO loginDTO) throws Exception {
		//called when crm complain is blocked
		CrmComplainDTO crmComplainDTO = complainDAO.getComplainDTOByComplainID(complainID);
		if(crmComplainDTO == null){
			throw new RequestFailureException("No such crm complain found");
		}
		rejectCrmComplainByAdmin(crmComplainDTO, loginDTO);
	}
	
	
	public void rejectCrmComplainByAdmin(CrmComplainDTO complainDTO,LoginDTO loginDTO) throws Exception{
		if(complainDTO.isBlocked()){
			return;
		}
		complainDTO.setBlocked(true);
		complainDTO.setCurrentStatus(CrmComplainDTO.REJECTED_BY_ADMIN);
		complainDAO.updateComplain(complainDTO);
		Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(complainDTO.getID()
				, EntityTypeConstant.CRM_COMPLAIN);
		if(rootRequestID!=null){
			CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplainReject(
					complainDTO.getID(), rootRequestID,loginDTO);
			requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
			requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		}
	}
	
	public void rejectCrmComplainByCommonPoolDTOByAdmin(long commonPoolID,LoginDTO loginDTO) throws Exception{
		List<CrmComplainDTO> crmComplainDTOs = complainDAO.getComplainListByPoolID(commonPoolID);
		for(CrmComplainDTO complainDTO: crmComplainDTOs){
			
			rejectCrmComplainByAdmin(complainDTO, loginDTO);
			
			updatePreviousActivityLog(complainDTO);
			
			CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, complainDTO.getAssignerID());
			crmActivityLog.setDescription("This complain was rejected by Admin");
			crmActivityLog.setTakenActionType(CrmComplainDTO.REJECTED_BY_ADMIN);
		
		}

		complainDAO.blockCrmComplainByCommonPoolID(commonPoolID);
	}
	
	
	public void cancelCrmComplainByCommonPoolID(long commonPoolID,LoginDTO loginDTO) throws Exception{
		List<CrmComplainDTO> crmComplainDTOs = complainDAO.getComplainListByPoolID(commonPoolID);
		for(CrmComplainDTO complainDTO: crmComplainDTOs){
			
			if(complainDTO.isBlocked()){
				continue;
			}
			
			complainDTO.setCurrentStatus(CrmComplainDTO.CANCELLED_BY_CLIENT);
			complainDAO.updateComplain(complainDTO);
			Long rootRequestID = requestDAO.getRootRequestIDByEntityIDAndEntityTypeID(complainDTO.getID()
					, EntityTypeConstant.CRM_COMPLAIN);
			if(rootRequestID!=null){
				CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplainCancel(
						complainDTO.getID(),rootRequestID,loginDTO);
				requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
				requestUtilDAO.completeRequestByRootID(rootRequestID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
			}
			
			updatePreviousActivityLog(complainDTO);
			
			CrmActivityLog crmActivityLog = createActivityLogByCrmComplain(complainDTO, complainDTO.getAssignerID());
			crmActivityLog.setDescription("This complain was cancelled by Client");
			crmActivityLog.setTakenActionType(CrmComplainDTO.CANCELLED_BY_CLIENT);
		}
		complainDAO.blockCrmComplainByCommonPoolID(commonPoolID);
	}

	private CommonRequestDTO createCommonRequestDTOForNewCrmComplain(long crmComplainID, LoginDTO loginDTO) {
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplain(crmComplainID, 
				CRMRequestTypeConstants.REQUEST_NEW_CRM_COMPLAIN.SYSTEM_COMPAIN, loginDTO);
		return commonRequestDTO; 
	}
	private CommonRequestDTO createCommonRequestDTOForCrmComplainReject(long crmComplainID
			,long rootRequestID, LoginDTO loginDTO) {
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplain(crmComplainID, CRMRequestTypeConstants.REQUEST_NEW_CRM_COMPLAIN.SYSTEM_REJECT_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	private CommonRequestDTO createCommonRequestDTOForCrmComplainComplete(long crmComplainID
			,long rootRequestID,LoginDTO loginDTO){
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplain(crmComplainID, 
				CRMRequestTypeConstants.REQUEST_NEW_CRM_COMPLAIN.SYSTEM_COMPLETE_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	private CommonRequestDTO createCommonRequestDTOForCrmComplainCancel(long crmComplainID
			,long rootRequestID, LoginDTO loginDTO) {
		CommonRequestDTO commonRequestDTO = createCommonRequestDTOForCrmComplain(crmComplainID
				, CRMRequestTypeConstants.REQUEST_NEW_CRM_COMPLAIN.CLIENT_CANCEL_COMPLAIN, loginDTO);
		commonRequestDTO.setRootReqID(rootRequestID);
		return commonRequestDTO; 
	}
	
	private CommonRequestDTO createCommonRequestDTOForCrmComplain(long crmComplainID, int requestTypeID, LoginDTO loginDTO ) {
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityTypeID(EntityTypeConstant.CRM_COMPLAIN);
		commonRequestDTO.setEntityID(crmComplainID);
		commonRequestDTO.setRequestTypeID(requestTypeID);
		commonRequestDTO.setRequestByAccountID(loginDTO.getUserID() > 0 ? -loginDTO.getUserID(): loginDTO.getAccountID());
		commonRequestDTO.setExpireTime(new CommonDAO().getExpireTimeByRequestType(commonRequestDTO.getRequestTypeID()));
		commonRequestDTO.setRequestTime(CurrentTimeFactory.getCurrentTime());
		commonRequestDTO.setIP(loginDTO.getLoginSourceIP());
		return commonRequestDTO; 
	}
	

}
