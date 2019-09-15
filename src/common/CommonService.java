package common;

import annotation.Transactional;
import common.actionProcessor.ActionProcessor;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.history.HistoryDAO;
import common.payment.PaymentDAO;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import file.FileDAO;
import file.FileService;
import login.LoginDTO;
import notification.NotificationDAO;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import permission.ActionStateDTO;
import permission.StateActionDTO;
import request.*;
import util.*;
import vpn.client.ClientDetailsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CommonService {
	static Logger logger = Logger.getLogger(CommonService.class); 
	RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
	RequestDAO requestDAO = new RequestDAO();
	CommonDAO commonDAO = new CommonDAO();
//	VpnLinkService vpnLinkService = new VpnLinkService();
	//LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
	PaymentDAO paymentDAO = new PaymentDAO(); 
	BillDAO billDAO = new BillDAO();
	NotificationDAO notificationDAO = new NotificationDAO();
	FileDAO fileDAO = new FileDAO();
//	VpnDAO vpnDAO = new VpnDAO();
//	LliDAO lliDAO = new LliDAO();
	FileService fileService = ServiceDAOFactory.getService(FileService.class);
//	public Map<Integer, Map<Long, LliLinkDTO>> getMapOfLinkDTOToLinkIDToLink(int moduleID) {
//		return null;
//
//	}
		
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO) throws Exception{
		return getBottomRequestDTOsByEntity( commonRequestDTO, true );
	}
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO, boolean onlyPending) throws Exception 
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Set<CommonRequestDTO> commonRequestDTOs = new HashSet<CommonRequestDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonRequestDTOs = requestUtilDAO.getBottomRequestDTOsByEntity(commonRequestDTO, databaseConnection, onlyPending);
			databaseConnection.dbTransationEnd();			
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return commonRequestDTOs;
	}
//	public void startServiceVPN(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,	HttpServletResponse response, LoginDTO loginDTO) throws Exception
//	{
//
//		long currentTime = System.currentTimeMillis();
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			VpnLinkDTO linkDTO = new VpnLinkDTO();
//			linkDTO.setID(commonRequestDTO.getEntityID());
//			linkDTO.setActivationDate(currentTime);
//			SqlGenerator.updateEntityByPropertyList(linkDTO, VpnLinkDTO.class, databaseConnection, false, false, new String[]{"vlkActivationDate"}, currentTime);
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//
//
//
//	}
	
/*	public void startServiceNewDomain(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try
		{
			databaseConnection.dbOpen();			
			databaseConnection.dbTransationStart();
			
			
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			
			logger.debug("sourceRequestDTO " + sourceRequestDTO);
			CommonRequestDTO sourceOfSourceRequestDTO = new RequestUtilService().getRequestDTOByReqID(sourceRequestDTO.getSourceRequestID());
			BillDTO billDTO = billDAO.getBillByReqID(sourceOfSourceRequestDTO.getSourceRequestID(), databaseConnection);		
			logger.debug("billDTO " + billDTO);
			paymentDAO.payBillByBillID(billDTO.getID(), databaseConnection);				
			billDTO.setLastModificationTime(currentTime);
			billDAO.updateBill(billDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/
	
/*	public void startServiceRenewDomain(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			requestUtilDAO.completeRequestByRootID(commonRequestDTO.getRootReqID(), databaseConnection);
//			CommonRequestDTO sourceOfSourceRequestDTO = new RequestUtilService().getRequestDTOByReqID(sourceRequestDTO.getSourceRequestID());
			logger.debug("sourceRequestDTO " + sourceRequestDTO);
			BillDTO billDTO = billDAO.getBillByReqID(sourceRequestDTO.getSourceRequestID(), databaseConnection);		
			logger.debug("billDTO " + billDTO);
			paymentDAO.payBillByBillID(billDTO.getID(), databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);			
			billDTO.setLastModificationTime(currentTime);
			billDAO.updateBill(billDTO, databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/	

	public void cancelDemandNote(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,	HttpServletResponse response, LoginDTO loginDTO) throws Exception
	{
		
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.dbOpen();
		commonRequestDTO = requestUtilDAO.getRequestDTOWithRootValues(commonRequestDTO, databaseConnection);
		commonRequestDTO.setLastModificationTime(currentTime);
		commonRequestDTO.setRequestTime(currentTime);
		
		commonRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
		requestUtilDAO.updateRequestByRequestID(Long.parseLong(commonRequestDTO.getSourceRequestID()), databaseConnection);
		
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);
		commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
		BillDAO billDAO = new BillDAO();
		List<Long> billIDList = new ArrayList<Long>();		
		ArrayList<BillDTO> billList = (ArrayList<BillDTO>)SqlGenerator.getAllObjectList(BillDTO.class,databaseConnection, " where blReqID = " + commonRequestDTO.getReqID());
		logger.debug("billList " + billList);
		if(billList.size() > 0)
		{
			billIDList.add(billList.get(0).getID());
			billDAO.deleteBillDTOByIDList(billIDList, System.currentTimeMillis(), databaseConnection);
		}
		BillDTO billDTO = billDAO.getBillByReqID(commonRequestDTO.getRootReqID(), databaseConnection);
		commonDAO.rejectTeletalkForPayment(commonRequestDTO, billDTO, databaseConnection);
		
		notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
		databaseConnection.dbClose();
		System.out.println(commonRequestDTO);
	}


	public ArrayList<CommonRequestDTO> getHistory(int iStart, CommonRequestDTO comDTO,
			LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<CommonRequestDTO> commonRequestDTOs = null;
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			HistoryDAO historyDAO = new HistoryDAO();
			commonRequestDTOs = historyDAO.getHistoryDTOs(iStart, comDTO, databaseConnection,
					loginDTO); // getVpnLinkHistory(iStart,iEntityTypeID,iEntityID,
								// databaseConnection, loginDTO);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				logger.debug("In History Ex :", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				throw ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		return commonRequestDTOs;
	}

	public ArrayList<CommonRequestDTO> getHistoryForSearch(int iEntityTypeID, long iEntityID, String name,
			String description, long fromDate, long toDate, int requestTypeID, boolean isReq, LoginDTO loginDTO)
			throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<CommonRequestDTO> commonRequestDTOs = new ArrayList<CommonRequestDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			HistoryDAO historyDAO = new HistoryDAO();
			commonRequestDTOs = historyDAO.getHistoryForSearch(iEntityTypeID, iEntityID, name, description, fromDate,
					toDate, requestTypeID, isReq, databaseConnection, loginDTO);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				logger.debug("In History Ex :", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				throw ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		return commonRequestDTOs;
	}

	public ArrayList<ActionStateDTO> getActionDTOs(int entityTypeID, boolean isRootRequestOnly, LoginDTO loginDTO) {
		ArrayList<ActionStateDTO> actionStateDTOs = new ArrayList<ActionStateDTO>();
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityTypeID(entityTypeID);
		commonRequestDTO.setRootActionsOnly(isRootRequestOnly);
		Set<Integer> actionTypeIDs = RequestActionStateRepository.getInstance().getAllActions(commonRequestDTO,
				loginDTO);

		if (actionTypeIDs != null) {
			for (Integer actionTypeID : actionTypeIDs) {
				actionStateDTOs
						.add(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID));
			}
		}
		return actionStateDTOs;
	}

	public ArrayList<StateActionDTO> getAllNextAction(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO)
			throws Exception {
		RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<StateActionDTO> stateActionDTOs = new ArrayList<StateActionDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			stateActionDTOs = requestUtilDAO.getNextActionList(commonRequestDTO, loginDTO, databaseConnection);
			
			
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug("ex", ex);
			try {
				logger.debug("Ex :", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				throw ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		
		for( StateActionDTO stateAction: stateActionDTOs ) {
			
			if( stateAction.getCommonRequestDTO().getEntityID() == 0 )
				stateAction.getCommonRequestDTO().setEntityID( commonRequestDTO.getEntityID() );
			
			if( stateAction.getCommonRequestDTO().getEntityTypeID() == 0 )
				stateAction.getCommonRequestDTO().setEntityTypeID( commonRequestDTO.getEntityTypeID() );
			
			if( stateAction.getCommonRequestDTO().getClientID() == 0 )
				stateAction.getCommonRequestDTO().setClientID( commonRequestDTO.getClientID() );
		}
		
		return stateActionDTOs;

	}

	public void commonRequestSubmit(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, true, true);
	}
	@SuppressWarnings("unused")
	public void commonRequestSubmit(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO, boolean allowCompleteRequestIfNecessary, boolean allowUpdateCurrentStatusIfNecessary) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			if(commonRequestDTO.getRequestTypeID()/100 == ModuleConstants.Module_ID_VPN || commonRequestDTO.getRequestTypeID()/100 == ModuleConstants.Module_ID_LLI){
				int remainder = commonRequestDTO.getRequestTypeID() % EntityTypeConstant.MULTIPLIER2; 
//				if(remainder == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_INFORM_SETUP_DONE % EntityTypeConstant.MULTIPLIER2){
//					CommonRequestDTO sourceOfCommonDTO = (CommonRequestDTO) SqlGenerator.getObjectByID( commonRequestDTO.getClass(), Long.parseLong(commonRequestDTO.getSourceRequestID()), databaseConnection );
//					commonRequestDTO.setRequestByAccountID( sourceOfCommonDTO.getRequestByAccountID() );
//					commonRequestDTO.setRequestToAccountID( sourceOfCommonDTO.getRequestToAccountID() );
//				}
			}
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, allowCompleteRequestIfNecessary);

			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			EntityDTO entityDTO = commonDAO.getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
			StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getCurrentStatus());
			boolean isBlankAction = false;


			if(sourceRequestDTO != null)
			{
				try{
					fileDAO.forwardFile(sourceRequestDTO.getRequestTypeID(), sourceRequestDTO.getReqID(), commonRequestDTO.getRequestTypeID(), commonRequestDTO.getReqID(), databaseConnection);
				}
				catch(Exception ex)
				{
					logger.debug("File forwarding error:", ex);
				}

			}

			boolean updateCurrentStatus = false;
			boolean completeRequest = false;
			

			
			if(commonRequestDTO.getRequestTypeID() < 0)
			{
				if(allowUpdateCurrentStatusIfNecessary)updateCurrentStatus = true;
			}			
			else if(currentStateDTO.getActivationStatus() != EntityTypeConstant.STATUS_ACTIVE)//until fully approval always the current status will keep updating
			{
				if(allowUpdateCurrentStatusIfNecessary)updateCurrentStatus = true;
			}
			
			
			if(updateCurrentStatus)
			{
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT, databaseConnection);
			}

			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	public void cancelOrReject(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		/*int rootActionType = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getRootActionTypeID();
		ActionStateDTO rootActionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootActionType);
		boolean isPartOfCreationRequest = rootActionStateDTO.isCreationRequest();*/
		/*
		 * If cancel or reject then only 'creator' action flow can change the current status
		 */
		boolean isCreativeRequest = false;
		
		if(commonRequestDTO.getEntityTypeID() % 100 == 51)
		{
			isCreativeRequest = true;
		}
		else
		{
			isCreativeRequest = UtilService.isCreativeRequest(commonRequestDTO.getRequestTypeID());
		}
		commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, true, isCreativeRequest);				
	}
	
	public boolean getIsEntityActive(int entityTypeID, long entityID) throws Exception
	{
		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, entityID);		
		int activationStatus = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getCurrentStatus()).getActivationStatus();
		Set<Integer> extraInactivSet = new HashSet<Integer>();
		extraInactivSet.add(-(EntityTypeConstant.entityModuleIDMap.get(entityTypeID) * ModuleConstants.MULTIPLIER + 301));//disable state
		logger.debug("extraInactivSet " + extraInactivSet);
		if(activationStatus != EntityTypeConstant.STATUS_NOT_ACTIVE)
		{
			if(extraInactivSet.contains(entityDTO.getCurrentStatus()))
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	public EntityDTO getEntityDTOByEntityIDAndEntityTypeID(int entityTypeID, long entityID) throws Exception{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		EntityDTO entityDTO = null;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			entityDTO = commonDAO.getEntityDTOByEntityIDAndEntityTypeID(entityTypeID,  entityID, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return entityDTO;
	}
	
	/**
	 * This method generates an advice note for Vpn Link and stores on at_common_note table
	 * @author Alam
	 * @param actionMapping
	 * @param commonRequestDTO
	 * @param sourceRequestDTO
	 * @param request
	 * @param response
	 * @param loginDTO
	 * @throws Exception
	 */
//	public void generateAdviceNote(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
//			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//
//
//			if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE)
//			{
//				commonDAO.generateAdviceNoteForTD(commonRequestDTO, request, loginDTO, databaseConnection);
//			}
//			else if (commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE)
//			{
//				commonDAO.generateAdviceNoteForTDEnable(commonRequestDTO, request, loginDTO, databaseConnection);
//			}
//			else if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE)
//			{
//				commonDAO.generateAdviceNoteForClosing(commonRequestDTO, request, loginDTO, databaseConnection);
//			}
//			else
//			{
//				commonDAO.generateAdviceNote(commonRequestDTO,sourceRequestDTO, request, loginDTO, databaseConnection);
//			}
//
//			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			}
//
//			//notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}
	/*
	public void generateAdviceNoteLLI(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
				
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE
					|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE) {
				commonDAO.generateAdviceNoteForTDLli(commonRequestDTO, request, loginDTO, databaseConnection);
			}else if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE) {
				commonDAO.generateAdviceNoteForClosingLli(commonRequestDTO, request, loginDTO, databaseConnection);
			}else if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_REQUEST_ADVICE_NOTE) {
				commonDAO.generateAdviceNoteForRequestIPAddressLLI(commonRequestDTO, request, loginDTO, databaseConnection);
			}else {
				commonDAO.generateAdviceNoteLLI(commonRequestDTO, request, loginDTO, databaseConnection);
			}
			
			
			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
			{
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
			}
			else
			{
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
			}
			
			//notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	*/
	@Transactional
	public void generateDemandNote(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();

		if( sourceRequestDTO != null )
			commonRequestDTO.setRequestToAccountID(sourceRequestDTO.getClientID());

		Set<CommonRequestDTO> bottom = getBottomRequestDTOsByEntity( commonRequestDTO );
		
		if( bottom.iterator().hasNext() ){
			
			CommonRequestDTO bottomDTO = bottom.iterator().next();
	
			bottomDTO.setCompletionStatus( RequestStatus.SEMI_PROCESSED );
	
			SqlGenerator.updateEntity( bottomDTO, CommonRequestDTO.class, databaseConnection, false, false );
	
			commonRequestDTO.setRootReqID( bottomDTO.getRootReqID() );
			commonRequestDTO.setSourceRequestID( "" + bottomDTO.getReqID() );
		}
		
		//This is done so that no entry goes to at_req when migrating bill
//		if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//			|| commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC
//			|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//			|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//		{
//			commonRequestDTO.setCompletionStatus(RequestStatus.ALL_PROCESSED);
//		}
		
		commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
		
//		if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE
//			|| commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//			|| commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC
//			)
//		{
//			commonDAO.generateDemandNote(commonRequestDTO, request, loginDTO, databaseConnection);
//		}
//		else if( commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC
//			){
//
//			commonDAO.generateDemandNoteLLI(commonRequestDTO, request, loginDTO, databaseConnection);
//		}
//		else if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE )
//			commonDAO.generateDemandNoteForLinkUpgradation( commonRequestDTO, request, loginDTO, databaseConnection );
//
//		else if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_GENERATE_DEMAND_NOTE )
//			commonDAO.generateDemandNoteForLinkShift( commonRequestDTO, request, loginDTO, databaseConnection );
		
//		else if( commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE)
//			commonDAO.generateDemandNoteForLinkUpgradationLLI( commonRequestDTO, request, loginDTO, databaseConnection );
//		else if( commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_GENERATE_DEMAND_NOTE) {
//			commonDAO.generateDemandNoteForLinkDowngradeLLI(commonRequestDTO, request, loginDTO, databaseConnection );
//		}
//		if( commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC
//				|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE
//				|| commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC)
//		{
//			return;
//		}
		
		if(UtilService.isCreativeRequest(commonRequestDTO.getRequestTypeID()))
		{
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST,  databaseConnection);
		}
		else
		{
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
		}

		notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
	}
	
	public void approveOwnershipChange(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
				
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();	
			
//			commonRequestDTO.setRequestToAccountID(sourceRequestDTO.getClientID());

			logger.debug("commonRequestDTO " + commonRequestDTO);
			
			Long rootReqID = sourceRequestDTO.getRootReqID();
			String conditionString = " where ownchrqReqID = " + rootReqID; 
			ArrayList<OwnerShipChangeRequest> domainList = (ArrayList<OwnerShipChangeRequest>)SqlGenerator.getAllObjectListFullyPopulated(OwnerShipChangeRequest.class, databaseConnection, conditionString);
			if(domainList == null || domainList.size() == 0)
			{
				throw new Exception ("No lli.Application.ownership change req found");
			}
			OwnerShipChangeRequest ownerShipChangeRequest = domainList.get(0);
			commonRequestDTO.setRequestToAccountID(ownerShipChangeRequest.getOldClientID());
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			
			fileDAO.forwardFile(sourceRequestDTO.getRequestTypeID(), sourceRequestDTO.getReqID(), commonRequestDTO.getRequestTypeID(), commonRequestDTO.getReqID(), databaseConnection);
			
			/*FileDAO fileDAO=new FileDAO();
			ArrayList<FileDTO> fileList=fileDAO.getFileByEntity(sourceRequestDTO.getRequestTypeID(), sourceRequestDTO.getReqID(), databaseConnection);
			
			if(fileList != null && fileList.size() > 0)
			{
				for(FileDTO fileDTO: fileList)
				{
					fileDTO.setDocEntityTypeID(commonRequestDTO.getRequestTypeID());					
					fileDTO.setDocEntityID(commonRequestDTO.getReqID());
					fileDAO.insert(fileDTO, databaseConnection);
				}
			}*/
			
//			OwnerShipChangeRequest ownerShipChangeRequest = new OwnerShipChangeRequest();
			/*DomainDTO domainDTO = (DomainDTO) SqlGenerator.getObjectByID(DomainDTO.class, commonRequestDTO.getEntityID(), databaseConnection);
			
			long newClientID = Long.parseLong(request.getParameter("domainClientID"));
			ownerShipChangeRequest.setNewClientID(newClientID);
			SqlGenerator.updateEntityByPropertyList(ownerShipChangeRequest, OwnerShipChangeRequest.class, databaseConnection, false, false, new String[]{"ID","newClientID"});*/
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
	}
	
	public boolean isActiveAndNotInAFlow(int entityTypeID, long entityID) throws Exception
	{

		DatabaseConnection databaseConnection = new DatabaseConnection();
		boolean result = false;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			result = commonDAO.isActiveAndNotInAFlow(entityTypeID, entityID, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return result;
	}
	public void generateDemandNoteForOwnershipChange(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
				
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();			
			commonRequestDTO.setRequestToAccountID(sourceRequestDTO.getClientID());
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.createDemandNoteForDomainOwnershipChange(commonRequestDTO, request, loginDTO, databaseConnection);
			fileDAO.forwardFile(sourceRequestDTO.getRequestTypeID(), sourceRequestDTO.getReqID(), commonRequestDTO.getRequestTypeID(), commonRequestDTO.getReqID(), databaseConnection);
			//commonDAO.waitForPayment(commonRequestDTO, sourceRequestDTO, loginDTO, databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
	}
	
	public void generateDemandNoteForNewDomain(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
				
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();		
			commonRequestDTO.setRequestToAccountID(sourceRequestDTO.getClientID());
			commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.updateExpireDateOfDomain(commonRequestDTO, request, loginDTO, databaseConnection);
//			commonDAO.createDemandNoteForNewDomain(commonRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}	
	
	public void generateDemandNoteForNewColocation(ActionMapping actionMapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
				
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();		
			commonRequestDTO.setRequestToAccountID(sourceRequestDTO.getClientID());
			commonDAO.commonRequestSubmit(actionMapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.createDemandNoteForNewColocation(commonRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex){
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally{
			databaseConnection.dbClose();
		}
	}	
	
	/**
	 * This method is used to roll back vpn link from Prepare to generate demand note state
	 * @author Alam
	 * @param commonRequestDTO Request currently being inserted
	 * @param bottomDtos Bottom of vpn link
	 * @throws Exception
	 */
	public void cancelRequestUsingBottomRequest( CommonRequestDTO commonRequestDTO, Set<CommonRequestDTO> bottomDtos, LoginDTO loginDTO ) throws Exception{
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			for (CommonRequestDTO bottom : bottomDtos) {
				
				//If Near end or far end request is found, copy there previous request and add as incomplete. Mark the current request complete.
				if( bottom.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END 
						|| bottom.getEntityTypeID() == EntityTypeConstant.VPN_LINK_NEAR_END ){
					
					CommonRequestDTO sourceOfBottom = (CommonRequestDTO) SqlGenerator.getObjectByID( bottom.getClass(), Long.parseLong(bottom.getSourceRequestID()), databaseConnection );
					sourceOfBottom.setCompletionStatus( RequestStatus.PENDING );
					
					requestDAO.addRequest(sourceOfBottom, loginDTO.getLoginSourceIP(), databaseConnection);
				}
				
				//If vpn link type req is found, mark as semi processed.
				if( bottom.getEntityTypeID() == EntityTypeConstant.VPN_LINK ){
					
					bottom.setCompletionStatus( RequestStatus.SEMI_PROCESSED );
					bottom.setDeleted( true );
					
					SqlGenerator.updateEntity( bottom, bottom.getClass(), databaseConnection, false, false );
				}
			}
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
		databaseConnection.dbClose();
	}
	
	@SuppressWarnings("unused")
//	public void rollbackRequest(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//
//		//long currentTime = System.currentTimeMillis();
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			boolean rollbackRoot = (sourceRequestDTO.getRootReqID() == null && sourceRequestDTO.getParentReqID() == null);
//			logger.debug("rollbackRoot " + rollbackRoot);
////			requestUtilDAO.updateRequestByRequestID(Long.parseLong(commonRequestDTO.getSourceRequestID()), true, databaseConnection);//making isDeleted too
//			commonDAO.updateSourceForRollback(sourceRequestDTO, request, loginDTO, databaseConnection);
////			ArrayList<CommonRequestDTO> sourceOfSourceRequestDTOList = new ArrayList<>();
//
//			if(rollbackRoot)
//			{
//				EntityDTO entityDTO = commonDAO.getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(), databaseConnection);
//				commonRequestDTO.setState(entityDTO.getCurrentStatus());
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateSourceOfSourceForRollback(sourceRequestDTO, request, loginDTO, databaseConnection);
//			}
//
//			logger.debug("sourceRequestDTO.getRequestTypeID() " + sourceRequestDTO.getRequestTypeID());
//			int requestRemainder = sourceRequestDTO.getRequestTypeID() % EntityTypeConstant.MULTIPLIER2;
//			if(requestRemainder == 11 || requestRemainder == 31 || requestRemainder == 95 || requestRemainder == 96)//cancelling demand note
//			{
//				commonDAO.rollBackBills(sourceRequestDTO, request, loginDTO, databaseConnection);
//			}
//			int sourceModuleID = sourceRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
//			switch(sourceModuleID)
//			{
//				case ModuleConstants.Module_ID_VPN:
//				{
////					vpnDAO.rollBackRequest(sourceRequestDTO, request, loginDTO, databaseConnection);
//
//					if((sourceRequestDTO.getRequestTypeID() % 100) == 42 || (sourceRequestDTO.getRequestTypeID() % 100) == 10){//rollback internal fr
//
//						InventoryService inventoryService = new InventoryService();
//
//						VpnLinkService vpnLinkService = new VpnLinkService();
//						VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
//
//						//Unuse far end ports and vlans
//						VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
//						inventoryService.markInventoryItemAsUnused(vpnFarEndDTO.getPortID());
//						inventoryService.markInventoryItemAsUnused(Long.parseLong(vpnFarEndDTO.getMandatoryVLanID()));
//						if(null != vpnFarEndDTO.getAdditionalVLanID()){
//							for(String feAdditionalVlanID: vpnFarEndDTO.getAdditionalVLanID().split(",")) {
//								inventoryService.markInventoryItemAsUnused(Long.parseLong(feAdditionalVlanID.trim()));
//							}
//						}
//						boolean resetStatus = false;
//						if((sourceRequestDTO.getRequestTypeID() % 100) == 10)
//						{
//							if(vpnFarEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
//							{
//								resetStatus = true;
//							}
//						}
//						else if((sourceRequestDTO.getRequestTypeID() % 100) == 42)
//						{
//							resetStatus = true;
//						}
//
//						if(resetStatus)
//						{
//							vpnFarEndDTO.setCurrentStatus(0);
//							if(UtilService.isCreativeRequest(sourceRequestDTO.getRequestTypeID()))
//							{
//								vpnFarEndDTO.setLatestStatus(0);
//							}
//						}
//						//vpnFarEndDTO.setPortID(0);
//						vpnFarEndDTO.setMandatoryVLanID(null);
//						vpnFarEndDTO.setAdditionalVLanID(null);
//						SqlGenerator.updateEntity(vpnFarEndDTO, VpnFarEndDTO.class, databaseConnection, true, false);
//
//						//Unuse near end ports and vlans
//						VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
//						inventoryService.markInventoryItemAsUnused(vpnNearEndDTO.getPortID());
//						if(vpnNearEndDTO.getMandatoryVLanID() != null && vpnNearEndDTO.getMandatoryVLanID().length() != 0)
//						{
//							inventoryService.markInventoryItemAsUnused(Long.parseLong(vpnNearEndDTO.getMandatoryVLanID()));
//						}
//						if(vpnNearEndDTO.getAdditionalVLanID() != null && vpnNearEndDTO.getAdditionalVLanID().length() != 0)
//						{
//							for(String neAdditionalVlanID: vpnNearEndDTO.getAdditionalVLanID().split(",")) {
//								inventoryService.markInventoryItemAsUnused(Long.parseLong(neAdditionalVlanID.trim()));
//							}
//						}
//
//						resetStatus = false;
//						if((sourceRequestDTO.getRequestTypeID() % 100) == 10)
//						{
//							if(vpnNearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
//							{
//								resetStatus = true;
//							}
//						}
//						else if((sourceRequestDTO.getRequestTypeID() % 100) == 42)
//						{
//							resetStatus = true;
//						}
//
//						if(resetStatus)
//						{
//							vpnNearEndDTO.setCurrentStatus(0);
//							if(UtilService.isCreativeRequest(sourceRequestDTO.getRequestTypeID()))
//							{
//								vpnNearEndDTO.setLatestStatus(0);
//							}
//						}
//
//						//vpnNearEndDTO.setPortID(0);
//						vpnNearEndDTO.setMandatoryVLanID(null);
//						vpnNearEndDTO.setAdditionalVLanID(null);
//						SqlGenerator.updateEntity(vpnNearEndDTO, VpnNearEndDTO.class, databaseConnection, true, false);
//
//					}
//
//					break;
//				}
////				case ModuleConstants.Module_ID_LLI:	{
////					lliLinkService.rollbackRequestLLI(commonRequestDTO, sourceRequestDTO, databaseConnection);
////					break;
////				}
//			}
//
//
//			if(!rollbackRoot)
//			{
////				fileDAO.forwardFile(sourceOfSourceRequestDTO.getRequestTypeID(), oldRequestID, commonRequestDTO.getRequestTypeID(), commonRequestDTO.getReqID(), databaseConnection);
//				notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			}
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//
//		databaseConnection.dbClose();
//		System.out.println(commonRequestDTO);
//	}

	
	public void connectionOwnershipChange(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.connectionOwnershipChange(commonRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}

	}

	public void domainOwnershipChange(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
/*			commonDAO.commonRequestSubmit(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.domainOwnershipChange(commonRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);*/
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}

	}	
	/*
	 * Never Called
	 */
	/*public void requestInternalFR(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
			vpnDAO.splitLinkToEnds(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/
	
//	public void linkCloseRequest(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
//			LoginDTO loginDTO) throws Exception {
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//		long currentTime = System.currentTimeMillis();
//		try {
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			long vpnLinkID = -1;
//			long clientID = -1;
//			if(request.getParameter("vpnLinkID") != null)
//			{
//				vpnLinkID = Long.parseLong(request.getParameter("vpnLinkID"));
//				clientID = loginDTO.getAccountID();
//				if(clientID < 0)
//				{
//					clientID = Long.parseLong(request.getParameter("clientID"));
//				}
//			}
//			else
//			{
//				clientID = commonRequestDTO.getClientID();
//				vpnLinkID = commonRequestDTO.getEntityID();
//			}
//			VpnLinkDTO vpnLinkDTO = vpnDAO.getVpnLink(vpnLinkID, databaseConnection);
//			int stateID = RequestActionStateRepository.getInstance()
//					.getActionStateDTOActionTypeID(VpnRequestTypeConstants.REQUEST_LINK_CLOSE.CLIENT_APPLY)
//					.getNextStateID();
//
//			vpnLinkDTO.setLatestStatus(stateID);
//			vpnLinkDTO.setLastModificationTime(currentTime);
//			SqlGenerator.updateEntityByPropertyList(vpnLinkDTO, VpnLinkDTO.class, databaseConnection, false, false, new String[] { "lastModificationTime", "latestStatus" });
//			VpnLinkCloseRequestDTO vpnLinkCloseRequestDTO = new VpnLinkCloseRequestDTO();
//			vpnLinkCloseRequestDTO.setRequestTime(currentTime);
//			vpnLinkCloseRequestDTO.setLastModificationTime(currentTime);
//			vpnLinkCloseRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_LINK_CLOSE.CLIENT_APPLY);
//			vpnLinkCloseRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK);
//			vpnLinkCloseRequestDTO.setEntityID(vpnLinkID);
//			vpnLinkCloseRequestDTO.setRootReqID(null);
//			vpnLinkCloseRequestDTO.setParentReqID(null);
//			vpnLinkCloseRequestDTO.setClientID(clientID);
//
//
//			if (!loginDTO.getIsAdmin()) {
//				vpnLinkCloseRequestDTO.setRequestByAccountID(loginDTO.getAccountID());
//			} else {
//				vpnLinkCloseRequestDTO.setRequestByAccountID(-loginDTO.getUserID());
//			}
//			vpnLinkCloseRequestDTO.setDescription("Sample");
////			vpnLinkCloseRequestDTO.setVpnLinkID(vpnLinkID);
//
//			if (vpnLinkCloseRequestDTO.getExpireTime() == 0) {
//				long expireTime = new CommonDAO().getExpireTimeByRequestType(vpnLinkCloseRequestDTO.getRequestTypeID());
//				vpnLinkCloseRequestDTO.setExpireTime(expireTime);
//			}
//			SqlGenerator.insert(vpnLinkCloseRequestDTO, DomainRenewRequest.class, databaseConnection, true);
//
//			databaseConnection.dbTransationEnd();
//		} catch (Exception ex) {
//			logger.fatal("Fatal", ex);
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception ex2) {
//			}
//			if (ex instanceof RequestFailureException) {
//				throw (RequestFailureException) ex;
//			}
//			throw ex;
//		} finally {
//			databaseConnection.dbClose();
//		}
//	}
	
	public void approveClient(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			requestUtilDAO.completeRequestByRootID(commonRequestDTO.getRootReqID(), databaseConnection);
			//commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();
			int stateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getNextStateID();
			int moduleID = commonRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
			clientDetailsDTO.setLatestStatus(stateID);
			clientDetailsDTO.setCurrentStatus(stateID);
			clientDetailsDTO.setId(AllClientRepository.getInstance().getVpnClientByClientID(commonRequestDTO.getEntityID(), moduleID).getId());
			clientDetailsDTO.setLastModificationTime(System.currentTimeMillis());
			SqlGenerator.updateEntityByPropertyList(clientDetailsDTO, ClientDetailsDTO.class, databaseConnection, false, false, new String[]{"currentStatus","latestStatus","lastModificationTime"});
			AllClientRepository.getInstance().reloadClientRepository(false);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	/*
	 * Never called
	 */
	/*public void verifyPayment(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);			
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/	

//	public void processSkipBillPaymentRequest(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
//			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
//			boolean skippable = false;
//			if(commonRequestDTO.getModuleID() == ModuleConstants.Module_ID_VPN)
//			{
//				skippable = vpnDAO.checkIfDemandNoteSkippable(commonRequestDTO, databaseConnection);
//			}
//			else
//			{
////				skippable = lliDAO.checkIfDemandNoteSkippable(commonRequestDTO, databaseConnection);
//			}
//			if(!skippable)
//			{
//				throw new RequestFailureException("Sorry this demand note can not be skipped");
//			}
////			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}
//
//	public void processNewLinkSetupDone(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
//			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
//			vpnDAO.splitLinkToEndsForExactExternalFR(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
//			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			}
//
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}
	/*
	 * 
	 * Not used
	 */
	public void processBandWidthChangeSetupDone(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
//			vpnDAO.addLinkBandWidthChangeSetupDoneRequest(loginDTO, commonRequestDTO, sourceRequestDTO, databaseConnection);
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
			
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch(Exception ex){
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		} finally{
			databaseConnection.dbClose();
		}
	}
	public void processBandWidthChangeSetupDoneLLI(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
//			vpnDAO.addLinkBandWidthChangeSetupDoneRequest(loginDTO, commonRequestDTO, sourceRequestDTO, databaseConnection);
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
			
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch(Exception ex){
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		} finally{
			databaseConnection.dbClose();
		}
	}
	/*
	public void processSetupDoneLLI(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
			HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			

			LliBillDTO lliBillDTO = billDAO.getExtendedBillByRootReqID(LliBillDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);

			if(lliBillDTO != null && lliBillDTO.isMinimumOFCUsed())
			{
				requestUtilDAO.updateRequestByRequestID(commonRequestDTO.getReqID(), databaseConnection);
				lliDAO.splitLinkToEndsForExactExternalFR(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
			}
			
			
			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
			{
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
			}
			else
			{
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
			}
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	*/
//	public void processExternalFRResponse(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,	HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			CommonRequestDTO externalFRRequest = null;
//
//			if(commonRequestDTO.getModuleID() == ModuleConstants.Module_ID_VPN)
//				externalFRRequest = commonDAO.processExternalFRResponse(commonRequestDTO, sourceRequestDTO,databaseConnection, request, loginDTO );
//			else if(commonRequestDTO.getModuleID() == ModuleConstants.Module_ID_LLI)
//				//externalFRRequest = commonDAO.processExternalFRResponseLLI( commonRequestDTO, sourceRequestDTO,databaseConnection, request, loginDTO );
//
//			//Dhrubo Uploads Documents. Halka modified by Alam
//			if(request.getParameter("documents") != null && externalFRRequest != null ) {
//
//				String[] documentTempLocationArray = request.getParameterValues("documents");
//				for(String documentTempLocation : documentTempLocationArray) {
//
//					FileDTO fileDTO = new FileDTO();
//					fileDTO.setDocOwner(loginDTO.getAccountID()>0?loginDTO.getAccountID():(-loginDTO.getUserID()));
//					fileDTO.setDocEntityTypeID(EntityTypeConstant.REQUEST);
//					fileDTO.setDocEntityID(externalFRRequest.getReqID());
//					fileDTO.setLastModificationTime(System.currentTimeMillis());
//
//					fileDTO.createLocalFileFromNames(documentTempLocation);
//					fileService.insert(fileDTO, databaseConnection);
//				}
//			}
//
//			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			}
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}
//
//	public void processExternalFRRequest(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,	HttpServletResponse response, LoginDTO loginDTO) throws Exception
//	{
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			switch( commonRequestDTO.getModuleID() ){
//				case ModuleConstants.Module_ID_VPN:
//				{
//					long companyID = vpnDAO.getOutsourcingCompanyID(commonRequestDTO, sourceRequestDTO, request, databaseConnection);
//
//					if(companyID > 0)
//					{
//						commonRequestDTO.setRequestToAccountID(-companyID);
//					}
//					break;
//				}
//				case ModuleConstants.Module_ID_LLI:
//				{
////					long companyID = lliDAO.getOutsourcingCompanyID(commonRequestDTO, sourceRequestDTO, request, databaseConnection);
//
////					if(companyID > 0)
////					{
////						commonRequestDTO.setRequestToAccountID(-companyID);
////					}
//					break;
//				}
//			}
//
//			if(commonRequestDTO.getRequestToAccountID() == null)
//			{
//				throw new RequestFailureException("Request Receiver not found");
//			}
//
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//
//			switch( commonRequestDTO.getModuleID() ){
//
//				case ModuleConstants.Module_ID_VPN:
//					vpnDAO.updateOutsourcingCompanyOfEndPoints(commonRequestDTO, sourceRequestDTO, request, databaseConnection);
////					vpnDAO.updateLinkBottomRequest(commonRequestDTO, sourceRequestDTO, request, databaseConnection );
//					break;
////				case ModuleConstants.Module_ID_LLI:
////					lliDAO.updateOutsourcingCompanyOfEndPoints(commonRequestDTO, sourceRequestDTO, request, databaseConnection);
//////					lliDAO.updateLinkBottomRequest(commonRequestDTO, sourceRequestDTO, request, databaseConnection );
////					break;
//			}
//			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			}
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}

//
//	public void acceptExternalFRRequest(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,	HttpServletResponse response, LoginDTO loginDTO) throws Exception
//	{
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			if(new UtilService().isCreativeRequest(commonRequestDTO.getRequestTypeID()))
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//			}
//			else
//			{
//				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
//			}
//			switch( commonRequestDTO.getModuleID() ){
//
//			case ModuleConstants.Module_ID_VPN:
//				vpnDAO.getPreparedForDemandNoteGeneration(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection );
//				break;
////			case ModuleConstants.Module_ID_LLI:
////				lliDAO.getPreparedForDemandNoteGeneration(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection );
////				break;
//		}
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception", ex);
//			databaseConnection.dbTransationRollBack();
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}

	
	/*public long getMainRoot(int entityTypeID, long entityID, boolean sameBranch)
	{
		long rootID = -1;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			rootID = commonDAO.getMainRootByEntity(entityTypeID, entityID, sameBranch, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return rootID;
	}*/
	/*
	 * comDTO will have
	 * either: entityID, entityTypeID
	 * or reqID
	 */
	/*public Set<Long> getMainRoots(CommonRequestDTO comDTO, boolean sameBranch, boolean pendingOnly)
	{
		Set<Long> rootSet = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			rootSet = requestUtilDAO.getMainRoots(comDTO, sameBranch, pendingOnly, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("ex", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return rootSet;
	}*/	
	/*
	 * Required:
	 * entityID, EntityTypeID
	 * or
	 * arID
	 */
	/*
	 * 
	 * Search in pending requests
	 * 
	 */
	/*public ArrayList<EntityTypeEntityDTO> getRelatedEntity(CommonRequestDTO comDTO,	boolean descendantOnly) throws Exception 
	{
		ArrayList<EntityTypeEntityDTO> entityList = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			entityList = commonDAO.getRelatedEntity(comDTO, descendantOnly, true, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
		logger.debug("returning entityList " + entityList);
		return entityList;
	}*/
/*	public void waitForPayment(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) {
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.waitForPayment( commonRequestDTO, sourceRequestDTO, loginDTO, databaseConnection);

			//commonDAO.commonRequestSubmit(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/
	
/*	public void waitForPaymentForRenew(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) {
		long currentTime = System.currentTimeMillis();
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.waitForPayment(mapping, commonRequestDTO, sourceRequestDTO, request, databaseConnection);

			commonDAO.commonRequestSubmit(commonRequestDTO, sourceRequestDTO, request, loginDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}*/	

	
	
		
//	public void bandwidthChangeComplete(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
//			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) throws Exception {
//
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//
//			if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_START_SERVICE)
//			{
//				vpnDAO.adjustBalanceForBandwidthChange( sourceRequestDTO.getEntityID(), sourceRequestDTO.getRootReqID(), databaseConnection );
//			}
//
//			vpnDAO.bandwidthChangeComplete(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
//
//			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//
//			databaseConnection.dbTransationEnd();
//		} catch(Exception ex){
//			logger.debug("Exception ", ex);
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			throw ex;
//		} finally{
//			databaseConnection.dbClose();
//		}
//	}
	/*
	public void bandwidthChangeCompleteLli(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_START_SERVICE)					
			{
				lliDAO.adjustBalanceForBandwidthChange( sourceRequestDTO.getEntityID(), sourceRequestDTO.getRootReqID(), databaseConnection );
			}
			
			lliDAO.bandwidthChangeComplete(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
			
			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			
			databaseConnection.dbTransationEnd();
		} catch(Exception ex){
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw ex;
		} finally{
			databaseConnection.dbClose();
		}
	}
	*/
//	public void linkCreationComplete(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
//			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
//			LoginDTO loginDTO) throws Exception {
//		// TODO Auto-generated method stub
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//
//		try
//		{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//
//			vpnDAO.linkCreationComplete(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
//
//			vpnDAO.updateActivationTimeOfMrc(commonRequestDTO, sourceRequestDTO, databaseConnection );
//
//			if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_START_SERVICE)
//			{
//				vpnDAO.adjustmentAtLinkCreation(commonRequestDTO, sourceRequestDTO, databaseConnection);
//			}
//			else if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_MRC_GENERATION)
//			{
//				//do nothing
//				//If it does nothing then why the hell it is added?
//			}
//			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			logger.debug("Exception ", ex);
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//	}
	
//	public void linkShiftingComplete(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,	LoginDTO loginDTO) throws Exception {
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//		try{
//			databaseConnection.dbOpen();
//			databaseConnection.dbTransationStart();
//
//			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
//			vpnDAO.linkShiftingComplete(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
//			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
//
//			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
//			databaseConnection.dbTransationEnd();
//		} catch(Exception ex){
//			logger.debug("Exception ", ex);
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			throw ex;
//		} finally{
//			databaseConnection.dbClose();
//		}
//	}
	/*
	public void linkCreationCompleteLLI(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		DatabaseConnection databaseConnection = new DatabaseConnection();		
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
			
			lliDAO.linkCreationComplete(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
			
			lliDAO.updateActivationTimeOfMrc(commonRequestDTO, sourceRequestDTO, databaseConnection );						
			
			if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_START_SERVICE)
			{
				lliDAO.adjustmentAtLinkCreation(commonRequestDTO, sourceRequestDTO, databaseConnection);
			}
			else if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_SKIP_MRC_GENERATION)
			{
				//do nothing
			}
			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST, databaseConnection);
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}	
	

	public Set<Long> getBottomRequestIDs(long rootReqID) {
		// TODO Auto-generated method stub
		Set<Long> reqSet = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();			
			reqSet = commonDAO.getBottomRequestIDsByRootReqID(rootReqID, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return reqSet;
	}
	public void uploadDocument(String []documents, LoginDTO loginDTO, CommonRequestDTO commonRequestDTO, HttpServletRequest p_request) throws Exception {
		FileService fileDAO = new FileService();
		for(int i=0; i<documents.length;i++){
			FileDTO fileDTO = new FileDTO();
			if(loginDTO.getAccountID()>0){
				fileDTO.setDocOwner(loginDTO.getAccountID());
				fileDTO.setDocEntityID(commonRequestDTO.getReqID()); 
			}else{
				fileDTO.setDocOwner(-loginDTO.getUserID());
				fileDTO.setDocEntityID(commonRequestDTO.getReqID()); 
			}
			fileDTO.setDocEntityTypeID(commonRequestDTO.getRequestTypeID()); 
			fileDTO.setLastModificationTime(System.currentTimeMillis());
			fileDTO.createLocalFileFromNames(documents[i]);
			fileDAO.insert(fileDTO);
		}
	}	
	*/

	public void updateStatusOfEntity(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO) throws Exception {
		updateStatusOfEntity(commonRequestDTO, loginDTO, EntityTypeConstant.STATUS_LATEST);
	}
	public void updateStatusOfEntity(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO, int statusType) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			commonDAO.updateStatusOfEntity(commonRequestDTO, loginDTO, statusType, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally
		{
			databaseConnection.dbClose();
		}		
	}
	

//
//	public VpnFRResponseInternalDTO getLatestInternalFR(long rootReqID) throws Exception
//	{
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//		VpnFRResponseInternalDTO vpnFRResponseInternalDTO = null;
//		try
//		{
//			databaseConnection.dbOpen();
////			databaseConnection.dbTransationStart();
//			vpnFRResponseInternalDTO = commonDAO.getLatestInternalFR(rootReqID, databaseConnection);
////			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				logger.debug(ex);
//			}
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//		return vpnFRResponseInternalDTO;
//	}
//	public ArrayList<VpnFRResponseExternalDTO> getLatestExternalFR(long rootReqID) throws Exception
//	{
//		DatabaseConnection databaseConnection = new DatabaseConnection();
//		ArrayList<VpnFRResponseExternalDTO> vpnFRResponseExternalDTOList = null;
//		try
//		{
//			databaseConnection.dbOpen();
////			databaseConnection.dbTransationStart();
//			vpnFRResponseExternalDTOList = commonDAO.getLatestExternalFR(rootReqID, databaseConnection);
////			databaseConnection.dbTransationEnd();
//		}
//		catch(Exception ex)
//		{
//			try {
//				databaseConnection.dbTransationRollBack();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				logger.debug(ex);
//			}
//			throw ex;
//		}
//		finally
//		{
//			databaseConnection.dbClose();
//		}
//		return vpnFRResponseExternalDTOList;
//	}

	public void processFinancialClearance(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			
			
			ArrayList<BillDTO> billList = (ArrayList<BillDTO>) new BillDAO().getBillDTOsByEntityAndEntityType(commonRequestDTO.getEntityID(), commonRequestDTO.getEntityTypeID(), databaseConnection, true);
			if(billList == null || billList.size() == 0)
			{
				databaseConnection.dbTransationStart();
				commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);									
				commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT_AND_LATEST,  databaseConnection);
				notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
				databaseConnection.dbTransationEnd();
			}
			else
			{
				throw new RequestFailureException("Some bills are found unpaid");
			}
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
	}
	
	public Object getExtendedRequest(CommonRequestDTO commonRequestDTO) throws Exception
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			return commonDAO.getExtendedRequest(commonRequestDTO, databaseConnection);
			//databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception ", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	
	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception
	{		
		while(true)
		{
			System.out.println("Enter clientID: ");
			long id = new Scanner(System.in).nextLong();
			CommonService commonService = new CommonService();
			boolean activationStatus = commonService.getIsEntityActive(151, id);
			System.out.println("activationStatus " + activationStatus);
		}
	}


	/*
	public void updateLinkBalanceLLI(CommonRequestDTO commonRequestDTO) throws Exception {
		
		long lliLinkID = commonRequestDTO.getEntityID();
		
		LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID( lliLinkID );
		LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
		
		int farEndBTCLLoopDistance = farEndDTO.getLoopDistanceBTCL();
		
		lli.configuration.LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getCurrentActiveLLI_FixedCostConfigurationDTO();
		
		BigDecimal totalCost = BigDecimal.ZERO;
		
//		totalCost = totalCost.add( new BigDecimal( farEndBTCLLoopDistance * commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ) );
		
		double totalCostDouble = totalCost.doubleValue();
		
		if( totalCostDouble > 1000 ){
			
			double haveToPayExtra = 1000 - totalCostDouble;
			
			lliLinkDTO.setBalance( lliLinkDTO.getBalance() + haveToPayExtra );
			
			DatabaseConnection databaseConnection = new DatabaseConnection();
			try
			{
				databaseConnection.dbOpen();
				SqlGenerator.updateEntity( lliLinkDTO, LliLinkDTO.class, databaseConnection, false, false );
			}
			catch(Exception ex)
			{
				logger.debug("Exception ", ex);
				try {
					databaseConnection.dbTransationRollBack();
				} catch (Exception e) {
					e.printStackTrace();
				}
				throw ex;
			}
			finally
			{
				databaseConnection.dbClose();
			}
		}
	}
	
	/**
	 * @author dhrubo
	 */
	public long getRequestedByAccountID(long rootRequestID, int neededRequestTypeID){
		long requestedByAccountID = 0;
		String sql = "select arRequestedByAccountID from at_req where arRootRequestID = "+rootRequestID+" and arRequestTypeID = "+neededRequestTypeID;
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		
		try{
			databaseConnection.dbOpen();
			statement = databaseConnection.getNewStatement();
			resultSet =  statement.executeQuery(sql);
			
			while (resultSet.next()) {
				requestedByAccountID = resultSet.getLong("arRequestedByAccountID");
				break;
			}
			
		}catch(Exception ex){
			logger.debug("Exception ", ex);
			try {databaseConnection.dbTransationRollBack();}catch (Exception e) {e.printStackTrace();}
			try {throw ex;} catch (Exception e) {e.printStackTrace();}
		}finally{
			try {statement.close();} catch (SQLException e) {e.printStackTrace();}
			try {resultSet.close();} catch (SQLException e) {e.printStackTrace();}
			databaseConnection.dbClose();
		}
		return requestedByAccountID;
	}
	
	/**
	 * @author dhrubo
	 */
	public String getActivationStatusName(Integer status) {
		StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(status);
		if(stateDTO == null){
			return null;
		}
		return EntityTypeConstant.statusMap.get(stateDTO.getActivationStatus());
	}
/*
	public void generateDemandNoteForIpAddress(ActionMapping mapping, CommonRequestDTO commonRequestDTO,
			CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO) throws Exception {
		// TODO Auto-generated method stub
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			commonDAO.commonRequestSubmit(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection, true);
																		
			lliDAO.generateDemandNoteIPAddress(mapping, commonRequestDTO, sourceRequestDTO, request, response, loginDTO, databaseConnection);
			
			commonDAO.updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST, databaseConnection);
			
			
			notificationDAO.sendNotification(commonRequestDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("Exception", ex);
			databaseConnection.dbTransationRollBack();
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
	}
	*/
	
	
	@Transactional(transactionType = TransactionType.READONLY)
	private ActionProcessor getActionProcessorByActionStateID(int actionTypeID) throws Exception{

		
		
		ActionStateDTO actionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
		if(actionStateDTO == null){
			throw new RequestFailureException("No corresponding action state DTO fount with actionStateID "+actionTypeID);
		}
		try{
			Class<? extends ActionProcessor> classObject = (Class<? extends ActionProcessor>) Class.forName(actionStateDTO.getClassName());
			return classObject.newInstance();
		}catch(Exception ex){
			throw new RequestFailureException("");
		}
		
	}
	
	@Transactional
	public void processAction(int entityTypeID,long entityID, int actionTypeID) throws Exception{
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		CommonRequestDTO currentRequestDTO = new RequestUtilDAO().getBottomRequestDTOsByEntity(commonRequestDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection()).iterator().next();
		
		ActionProcessor actionProcessor = getActionProcessorByActionStateID(actionTypeID);
		actionProcessor.process(currentRequestDTO);
	}
	
	@Transactional
	public void rollBlackAction(int entityTypeID,long entityID, int actionTypeID) throws Exception{
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		commonRequestDTO.setEntityID(entityID);
		commonRequestDTO.setEntityTypeID(entityTypeID);
		CommonRequestDTO currentRequestDTO = new RequestUtilDAO().getBottomRequestDTOsByEntity(commonRequestDTO, DatabaseConnectionFactory.getCurrentDatabaseConnection()).iterator().next();
		
		ActionProcessor actionProcessor = getActionProcessorByActionStateID(actionTypeID);
		actionProcessor.rollback(currentRequestDTO);
	}
	
	
	
}
