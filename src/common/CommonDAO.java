package common;

import common.bill.BillDAO;
import common.bill.BillDTO;
import common.payment.constants.PaymentConstants;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import file.FileDTO;
import file.FileService;
import lli.constants.LliRequestTypeConstants;
import login.LoginDTO;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import permission.ActionStateDTO;
import request.*;
import user.UserDTO;
import util.SqlGenerator;
import util.UtilService;
import vpn.client.ClientDetailsDTO;
import vpn.constants.VpnRequestTypeConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static util.SqlGenerator.*;

//import ipaddress.IpAddressService;
//import ipaddress.IpBlock;


public class CommonDAO {

	static Logger logger = Logger.getLogger(CommonDAO.class);
	RequestDAO requestDAO = new RequestDAO();
	RequestUtilDAO requestUtilDAO = new RequestUtilDAO();
	
	BillDAO billDAO = new BillDAO();

	public void addNewClient(ClientDTO cdto, DatabaseConnection databaseConnection) throws Exception {
		insert(cdto, cdto.getClass(), databaseConnection, false);
	}





	/*
	 * Shariful: Use it as SearchCriteriaFunction  >> it returns collection of DTO but I need collection of ID so I made a function next to this
	 * 
	 */




	public void addUserNameCondition(StringBuilder conditionString, ArrayList<Long> clientIDs, ArrayList<Long> userIDs)
			throws Exception {
		if (clientIDs.size() > 0 || userIDs.size() > 0) {
			conditionString.append(" and " + getColumnName(CommonRequestDTO.class, "requestByAccountID") + " IN ( ");
			int i = 0;
			for (long userID : userIDs) {
				i++;
				conditionString.append(" " + userID + " ");
				if (userIDs.size() != i) {
					conditionString.append(" , ");
				}
			}
			i = 0;
			for (long clientID : clientIDs) {
				i++;
				conditionString.append(" " + clientID + " ");
				if (clientIDs.size() != i) {
					conditionString.append(" , ");
				}
			}
			conditionString.append(" ) ");
		}
	}

	public ArrayList<Long> getClientIDsFromName(DatabaseConnection databaseConnection, String name) throws Exception {
		ArrayList<Long> vpnClientIDs = new ArrayList<Long>();

		String conditionString = " where " + getColumnName(ClientDTO.class, "loginName") + " like '%" + name + "%' ";
		ArrayList<ClientDTO> VpnClients = (ArrayList<ClientDTO>) getAllObjectList(ClientDTO.class, databaseConnection,
				conditionString);
		for (ClientDTO dto : VpnClients) {
			vpnClientIDs.add(dto.getClientID());
		}

		return vpnClientIDs;
	}

	public ArrayList<Long> getUserIDsFromName(DatabaseConnection databaseConnection, String name) throws Exception {
		ArrayList<Long> vpnUserIDs = new ArrayList<Long>();

		String conditionString = " where " + getColumnName(UserDTO.class, "username") + " like '%" + name + "%' ";
		ArrayList<UserDTO> user = (ArrayList<UserDTO>) getAllObjectList(UserDTO.class, databaseConnection,
				conditionString);
		for (UserDTO dto : user) {
			vpnUserIDs.add(-dto.getUserID());
		}
		return vpnUserIDs;
	}
	
	/*
	 * 
	 * Top to bottom
	 * 
	 */
	/*private void reqTreeTraversal(long id, ArrayList<EntityTypeEntityDTO> entityTypeEntityDTOs,
			DatabaseConnection databaseConnection) throws Exception {
		String sql = "SELECT arID, arRootRequestID, arParentRequestID,arEntityTypeID,arEntityID FROM at_req WHERE  ( arRootRequestID IS null AND arID = "
				+ id + " ) or (arRootRequestID = " + id + ") group by arEntityID, arEntityTypeID";
		logger.debug("Sql >" + sql);
		Statement statement = databaseConnection.getNewStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			long reqID = resultSet.getLong("arID");
			EntityTypeEntityDTO entityTypeEntityDTO = new EntityTypeEntityDTO();
			entityTypeEntityDTO.setEntityTypeID(resultSet.getInt("arEntityTypeID"));
			entityTypeEntityDTO.setEntityID(resultSet.getLong("arEntityID"));
			entityTypeEntityDTOs.add(entityTypeEntityDTO);
			if (resultSet.isLast()) {
				long rootParam;
				long rootReqID = resultSet.getLong("arRootRequestID");
				if (rootReqID == 0)// null will be converted to zero
				{
					rootParam = reqID;
				} else {
					rootParam = rootReqID;
				}
				String sql1 = "SELECT arID FROM at_req WHERE ( arRootRequestID IS null AND arParentRequestID = "
						+ rootParam + " ) ";
				logger.debug("Sql >> "+ sql1);
				Statement statement1 = databaseConnection.getNewStatement();
				ResultSet resultSet1 = statement1.executeQuery(sql1);
				while (resultSet1.next()) {
					reqTreeTraversal(resultSet1.getLong("arID"), entityTypeEntityDTOs, databaseConnection);
				}
			}

		}
	}*/




	/*
	 * Shariful: Use it as getIDs
	 * 
	 */

	
//	public ArrayList<Long> getSortedAndMerged(ArrayList<Long> historyID1, ArrayList<Long> historyID2)
//	{
//		return null;
//	}
//
//	public Set<Long> getMainRootIDsOfEntity(CommonRequestDTO comDTO, DatabaseConnection databaseConnection) throws SQLException
//	{
//		String sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE arEntityTypeID = " + comDTO.getEntityTypeID() + " AND arEntityID = " + comDTO.getEntityID() + " AND arRootRequestID is NULL and arParentRequestID is NULL";
//		Statement statement = databaseConnection.getNewStatement();
//		ResultSet resultSet = statement.executeQuery(sql);
//		Set<Long> set = new HashSet<Long>();
//		while(resultSet.next())
//		{
//			long arID = resultSet.getLong("arID");
//			set.add(arID);
//		}
//		return set;
//	}

	


//	private String setToString(Set<Long> rootReqIDs)
//	{
//		String commaSeparated = "";
//		int i = 0;
//		for(Long rootReqID: rootReqIDs)
//		{
//			i++;
//			commaSeparated += rootReqID;
//			if(i < rootReqIDs.size())
//				commaSeparated += ",";
//		}
//		return commaSeparated;
//	}
	
	private void getNextBottomReqIDs(long rootReqID, Set<Long> bottomReqIDs, DatabaseConnection databaseConnection) throws Exception {
		String sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE  ( arRootRequestID IS null AND arID =  "
				+ rootReqID + " ) or (arRootRequestID = " + rootReqID + " )" + " order by arID asc";
		logger.debug("sql " + sql);
		Statement statement = databaseConnection.getNewStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		Set<Long> rootParamSet = new HashSet<Long>();
		long lastID = 0;
		String whereClause = "(";
		while(resultSet.next())
		{
			long reqID = resultSet.getLong("arID");
			whereClause += reqID;
			if(!resultSet.isLast())
			{
				whereClause += ",";
			}
			rootParamSet.add(reqID);
			lastID = reqID;
		}
		whereClause += ")";
		if(whereClause.length() > 2)
		{
			sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE ( arRootRequestID IS null AND arParentRequestID in " + whereClause + " ) ";
			logger.debug("-- sql " + sql);
			Statement statement1 = databaseConnection.getNewStatement();
			resultSet = statement1.executeQuery(sql);
			boolean hasData = false;
			while(resultSet.next())
			{
				hasData = true;
				long reqID = resultSet.getLong("arID");
				getNextBottomReqIDs(reqID, bottomReqIDs, databaseConnection);
			}
//			if(!hasData)
			{
				bottomReqIDs.add(lastID);
			}
		}
		else
		{
//			historyIDs.addAll(rootParamSet);
		}
	}

	private void getNextBottomReqDTOs(long rootReqID, Set<CommonRequestDTO> bottomReqDTOs, DatabaseConnection databaseConnection) throws Exception {
		String conditionString = " where ( arRootRequestID IS null AND arID =  "	+ rootReqID + " ) or (arRootRequestID = " + rootReqID + " )" + " order by arID asc";
		ArrayList<CommonRequestDTO> reqlistHavingSameRootReqID = (ArrayList<CommonRequestDTO>) SqlGenerator.getAllObjectList(CommonRequestDTO.class, databaseConnection, conditionString);
		String sql = null;
		ResultSet resultSet = null;
		Set<Long> rootParamSet = new HashSet<Long>();

		String whereClause = "(";
		int count = 0;
		CommonRequestDTO lastDTO = null;
		for(CommonRequestDTO commonRequestDTO: reqlistHavingSameRootReqID)
		{
			whereClause += commonRequestDTO.getReqID();
			count++;
			if(count < reqlistHavingSameRootReqID.size())
			{
				whereClause += ",";
			}

			rootParamSet.add(commonRequestDTO.getReqID());
			lastDTO = commonRequestDTO;
		}
		whereClause += ")";

		if(whereClause.length() > 2)
		{
			sql = "SELECT arID, arRootRequestID, arParentRequestID FROM at_req WHERE ( arRootRequestID IS null AND arParentRequestID in " + whereClause + " ) ";
			logger.debug("-- sql " + sql);
			Statement statement1 = databaseConnection.getNewStatement();
			resultSet = statement1.executeQuery(sql);
			boolean hasData = false;
			while(resultSet.next())
			{
				hasData = true;
				long reqID = resultSet.getLong("arID");
				getNextBottomReqDTOs(reqID, bottomReqDTOs, databaseConnection);
			}
			//if(!hasData)
			{
				bottomReqDTOs.add(lastDTO);
			}
		}
		else
		{
//			historyIDs.addAll(rootParamSet);
		}
	}



//
//	public CommonRequestDTO getBottomReqIDsByRootReqAndEntityType( long rootReqID, long entityTypeID, DatabaseConnection databaseConnection ) throws Exception{
//
//		Set<CommonRequestDTO> bottomReqIDs = new HashSet<CommonRequestDTO>();
//		getNextBottomReqDTOs(rootReqID, bottomReqIDs, databaseConnection);
//
//		for (CommonRequestDTO commonRequestDTO : bottomReqIDs) {
//
//			if( commonRequestDTO.getEntityTypeID() == entityTypeID )
//				return commonRequestDTO;
//		}
//
//		return null;
//	}
//
//	public Set<Long> getBottomRequestIDsByRootReqID(long rootReqID, DatabaseConnection databaseConnection) throws Exception {
//		// TODO Auto-generated method stub
//		Set<Long> bottomReqIDs = new HashSet<Long>();
//		getNextBottomReqIDs(rootReqID, bottomReqIDs, databaseConnection);
//		return bottomReqIDs;
//	}
//
//	public Set<CommonRequestDTO> getBottomRequestDTOsByRootReqID(long rootReqID, DatabaseConnection databaseConnection) throws Exception {
//		// TODO Auto-generated method stub
//		Set<CommonRequestDTO> bottomReqIDs = new HashSet<CommonRequestDTO>();
//		getNextBottomReqDTOs(rootReqID, bottomReqIDs, databaseConnection);
//		logger.debug("returning " + bottomReqIDs);
//		return bottomReqIDs;
//	}
//

	public long getExpireTimeByRequestType(int requestType)
	{		
		ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(requestType);		
		if(asdto==null){
			return 0;
		}
		int stateID = asdto.getNextStateID();
		return System.currentTimeMillis() + StateRepository.getInstance().getStateDTOByStateID(stateID).getDurationInMillis();
	}

	public void commonRequestSubmit(ActionMapping mapping, CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request, HttpServletResponse response,
			LoginDTO loginDTO, DatabaseConnection databaseConnection, boolean checkNegativity) throws Exception {

		long currentTime = System.currentTimeMillis();
		
		logger.debug("sourceRequestDTO " + sourceRequestDTO);
		
		Long rootReqID = commonRequestDTO.getRootReqID();
		
		if(sourceRequestDTO != null){
			if(sourceRequestDTO.getRootReqID() != null && sourceRequestDTO.getRootReqID() != 0){
				rootReqID = sourceRequestDTO.getRootReqID();
			}
			else{
				rootReqID = sourceRequestDTO.getReqID();
			}
			commonRequestDTO.setRootReqID(rootReqID);
			commonRequestDTO.setClientID(sourceRequestDTO.getClientID());
		}
		commonRequestDTO.setLastModificationTime(currentTime);
		commonRequestDTO.setRequestTime(currentTime);	
		
		logger.debug("before adding commonRequestDTO " + commonRequestDTO);
		
		requestUtilDAO.updateRequestByRequestID(Long.parseLong(commonRequestDTO.getSourceRequestID()), databaseConnection);

		switch(commonRequestDTO.getRequestTypeID()){

			case VpnRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
			case VpnRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION:
			case LliRequestTypeConstants.REQUEST_NEW_CLIENT.SYSTEM_REJECT_APPLICATION:
			case LliRequestTypeConstants.REQUEST_NEW_CLIENT.CLIENT_CANCEL_APPLICATION:
			{
//				EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
//				List<Integer> registeredModuleCount = AllClientRepository.getInstance().
//						getRegisteredModulesByClientID(commonRequestDTO.getClientID());
//				if(registeredModuleCount.size()>1) {
//					SqlGenerator.deleteEntity(entityDTO, entityDTO.getClass(), databaseConnection, false);
//				}else if(registeredModuleCount.size() == 1) {
//					SqlGenerator.deleteEntity(entityDTO, entityDTO.getClass(), databaseConnection, true);
//				}
				/*dont know what will happen here, commented out portion indicates the rollback we agreed upon in a meeting*/
			

				break;
			}

//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REJECT_APPLICATION:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.CLIENT_CANCEL_APPLICATION:
//			case VpnRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_COMPLETE_CLOSE_LINK:
//			{
//				new VpnDAO().processLinkTermination(commonRequestDTO, loginDTO, databaseConnection);
//				break;
//			}
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REJECT_APPLICATION:
//			case LliRequestTypeConstants.REQUEST_NEW_LINK.CLIENT_CANCEL_APPLICATION:
//			case LliRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_COMPLETE_CLOSE_LINK:
//			{
//				new LliDAO().processLinkTermination(commonRequestDTO, loginDTO, databaseConnection);
//				break;
//			}
		}
		requestDAO.addRequest(commonRequestDTO, loginDTO.getLoginSourceIP(), databaseConnection);
		int moduleID = commonRequestDTO.getRequestTypeID() / ModuleConstants.MULTIPLIER;
		if(checkNegativity && commonRequestDTO.getRequestTypeID() < 0){
			
			
			if(moduleID == ModuleConstants.Module_ID_DOMAIN) {
				requestUtilDAO.completeRequestByRootID(rootReqID, databaseConnection);
			} else {
				requestUtilDAO.completeRequestByEntityIDandEntityTypeID(commonRequestDTO.getEntityID(), commonRequestDTO.getEntityTypeID(), true, databaseConnection);
			}
		}
	}
	
//	public void generateAdviceNoteForTD(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
//			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//
//		CommonNote commonNote = new CommonNote();
//
//		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
//		commonNote.setEntityId( commonRequestDTO.getEntityID() );
//		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_VPN_LINK );
//		commonNote.setReqID( commonRequestDTO.getReqID() );
//
//		VpnLinkDisableRequestDTO vpnLinkDisableRequestDTO = new RequestUtilDAO().getExtendedRequestByRootReqID(VpnLinkDisableRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
//
//		long clientID = Long.parseLong(request.getParameter("clientID"));
//		ClientDetailsDTO clientDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, ModuleConstants.Module_ID_VPN);
//		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
//		List<String> clientDetails = getClientDetails(clientID, ModuleConstants.Module_ID_VPN);
//
//		String tdOrEnable = "TD";
//		if(vpnLinkDisableRequestDTO.getExpirationDate() > 0)
//		{
//			tdOrEnable = "Enable";
//		}
//
//		commonNote.startDescription( "Description", "Value" );
//		commonNote.addRow( "Link Name" ,entityDTO.getName() );
//		commonNote.addRow( tdOrEnable +" Start Date" , TimeConverter.getDateTimeStringFromDateTime(vpnLinkDisableRequestDTO.getStartDate() ));
//		if(vpnLinkDisableRequestDTO.getExpirationDate() > 0)
//		{
//			commonNote.addRow( "TD Expired Date" , TimeConverter.getDateTimeStringFromDateTime(vpnLinkDisableRequestDTO.getExpirationDate()) );
//		}
//		commonNote.addRow( "Description" , vpnLinkDisableRequestDTO.getDescription());
//
//
//		commonNote.endDescription();
//		CommonNoteDAO.insert( commonNote, databaseConnection);
//	}
	
//	public void generateAdviceNoteForClosing(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
//			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		CommonNote commonNote = new CommonNote();
//
//		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
//		commonNote.setEntityId( commonRequestDTO.getEntityID() );
//		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_VPN_LINK );
//		commonNote.setReqID( commonRequestDTO.getReqID() );
//
//		VpnLinkCloseRequestDTO vpnLinkCloseRequestDTO = new RequestUtilDAO().getExtendedRequestByRootReqID(VpnLinkCloseRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
//
//		long clientID = Long.parseLong(request.getParameter("clientID"));
//		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
//		List<String> clientDetails = getClientDetails(clientID, ModuleConstants.Module_ID_VPN);
//
//		commonNote.startDescription( "Description", "Value" );
//		commonNote.addRow( "Link Name" ,entityDTO.getName() );
//		commonNote.addRow( "Expected Closing Date" , TimeConverter.getDateTimeStringFromDateTime(vpnLinkCloseRequestDTO.getExpectedClosingDate() ));
//		commonNote.addRow( "Description" , vpnLinkCloseRequestDTO.getDescription());
//		commonNote.endDescription();
//
//		CommonNoteDAO.insert( commonNote, databaseConnection);
//	}
	/*public void generateAdviceNoteForTDLli(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		CommonNote commonNote = new CommonNote();
		
		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
		commonNote.setEntityId( commonRequestDTO.getEntityID() );
		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_LLI_LINK );
		commonNote.setReqID( commonRequestDTO.getReqID() );
		LliLinkDisableRequestDTO lliLinkDisableRequestDTO = new RequestUtilDAO().getExtendedRequestByRootReqID(LliLinkDisableRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
		logger.debug(request.getParameterMap().toString());
		
		long clientID = Long.parseLong(request.getParameter("clientID"));
		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
		List<String> clientDetails = getClientDetails(clientID, ModuleConstants.Module_ID_LLI);
		
		commonNote.startDescription( "Description", "Value" );
		commonNote.addRow( "Connection Name" ,entityDTO.getName() );
		
		if(lliLinkDisableRequestDTO.getExpirationDate() > 0) {
			commonNote.addRow( "TD Start Date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkDisableRequestDTO.getTdStartDate() ));
		} else {
			commonNote.addRow( "Connection Enable Date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkDisableRequestDTO.getTdStartDate() ));
		}

		if(lliLinkDisableRequestDTO.getExpirationDate() > 0)
		{
			commonNote.addRow( "TD Expired Date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkDisableRequestDTO.getExpirationDate()) );
		}
		commonNote.addRow( "Description" , lliLinkDisableRequestDTO.getDescription());
		
		LliDAO lliDAO = new LliDAO();
		LliLinkDTO lliLinkDTO = lliDAO.getLliLinkDTOByID(commonRequestDTO.getEntityID(), databaseConnection);
		LliFarEndDTO lliFarEndDeDTO =  lliDAO.getLliFarEndByID(lliLinkDTO.getFarEndID(), databaseConnection);
		LliEndPointDetailsDTO farEndDetailsDTO = lli.link.LinkUtils.getEndPointDTODetails( lliFarEndDeDTO );
		commonNote.addRow( "POP Name", farEndDetailsDTO.getPopName());
		commonNote.addRow( "Port Type", farEndDetailsDTO.getPortCateogryTypeName() );
		commonNote.addRow( "Port Name", farEndDetailsDTO.getPortName() );
		
		commonNote.endDescription();
		
		CommonNoteDAO.insert( commonNote, databaseConnection);
		
	}
	*/
//	List<String> getClientDetails(long clientID, int moduleID) throws Exception {
//		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientID, moduleID );
//		List<String>details = new ArrayList<>();
//		if( clientDetailsDTO != null ){
//			List<ClientContactDetailsDTO> clientContactDetailsDTOs = (List<ClientContactDetailsDTO>) new ClientService().getVpnContactDetailsListByClientID( clientDetailsDTO.getId() );
//			if( clientContactDetailsDTOs != null && clientContactDetailsDTOs.size() > 0 ){
//			    ClientContactDetailsDTO contactDetailsDTO = clientContactDetailsDTOs.get(0);
//			    details.add(contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
//			    details.add(contactDetailsDTO.getAddress());
//			    details.add(RegistrantTypeConstants.VPN_REGISTRANT_TYPE.get( clientDetailsDTO.getRegistrantType()));
//			}
//		}
//		return details;
//	}
	/*
	public void generateAdviceNoteForClosingLli(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		CommonNote commonNote = new CommonNote();
		
		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
		commonNote.setEntityId( commonRequestDTO.getEntityID() );
		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_LLI_LINK );
		commonNote.setReqID( commonRequestDTO.getReqID() );
		
		LliLinkCloseRequestDTO lliLinkCloseRequestDTO = new RequestUtilDAO().getExtendedRequestByRootReqID(LliLinkCloseRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
		long clientID = Long.parseLong(request.getParameter("clientID"));
		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
		List<String> clientDetails = getClientDetails(clientID, ModuleConstants.Module_ID_LLI);
		
		commonNote.startDescription( "Description", "Value" );
		commonNote.addRow( "Connection Name" ,entityDTO.getName() );
//		commonNote.addRow( "Client Name" , clientDetails.get(0) );
//		commonNote.addRow( "Client Address" , clientDetails.get(1) );
//		commonNote.addRow( "Client Registrant Type" , clientDetails.get(2) );
		commonNote.addRow( "Expected Closing Date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkCloseRequestDTO.getExpectedClosingDate() ));
		commonNote.addRow( "Description" , lliLinkCloseRequestDTO.getDescription());
		
		
		commonNote.endDescription();
		CommonNoteDAO.insert( commonNote, databaseConnection);
	}
	*/
//	/**
//	 * Enters an advice note entry for the document
//	 * @author Alam
//	 * @param commonRequestDTO
//	 * @param sourceRequestDTO
//	 * @param request
//	 * @param loginDTO
//	 * @param databaseConnection
//	 * @throws Exception
//	 */
//	public void generateAdviceNote(CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, HttpServletRequest request,
//			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//
//		VpnLinkService vpnLinkService = new VpnLinkService();
//		CommonNote commonNote = new CommonNote();
//		int nearEndLoopDistance = 0;
//		int nearEndLoppDistanceOC = 0;
//		int nearEndLoppDistanceCustomer = 0;
//		int nearEndLoppDistanceBTCL = 0;
//
//		int farEndLoopDistance = 0;
//		int farEndLoopDistanceOC = 0;
//		int farEndLoopDistanceCustomer = 0;
//		int farEndLoopDistanceBTCL = 0;
//
//		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
//		commonNote.setEntityId( commonRequestDTO.getEntityID() );
//		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_VPN_LINK );
//		commonNote.setReqID( commonRequestDTO.getReqID() );
//
//		VpnLinkDTO vpnLinkDTO = (VpnLinkDTO) getObjectByID(VpnLinkDTO.class, commonNote.getEntityId(), databaseConnection);
//		VpnNearEndDTO nearEndDTO = vpnLinkService.getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
//		VpnFarEndDTO farEndDTO = vpnLinkService.getFarEndByFarEndID( vpnLinkDTO.getFarEndID() );
//
//		String conditionString = " where vresfrexEntityTypeID = " + EntityTypeConstant.VPN_LINK_NEAR_END + " and vresfrexNearOrFarEndpointID = " + nearEndDTO.getID() + " order by vresfrexID desc";
//		ArrayList<VpnFRResponseExternalDTO> nearEndResponseList = (ArrayList<VpnFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(VpnFRResponseExternalDTO.class, databaseConnection, conditionString);
//
//		if( nearEndResponseList != null && nearEndResponseList.size() != 0 ){
//
//			VpnFRResponseExternalDTO nearEndResponse = nearEndResponseList.get(0);
//			nearEndLoopDistance = nearEndResponse.getDistanceTotal();
//			nearEndLoppDistanceOC = nearEndResponse.getDistanceOC();
//			nearEndLoppDistanceCustomer = nearEndResponse.getDistanceCustomer();
//			nearEndLoppDistanceBTCL = nearEndResponse.getDistanceBTCL();
//		}
//		else
//		{
//			nearEndLoopDistance = nearEndDTO.getLoopDistanceTotal();
//			nearEndLoppDistanceOC = nearEndDTO.getLoopDistanceOC();
//			nearEndLoppDistanceCustomer = nearEndDTO.getLoopDistanceCustomer();
//			nearEndLoppDistanceBTCL = nearEndDTO.getLoopDistanceBTCL();
//		}
//
//		conditionString = " where vresfrexEntityTypeID = " + EntityTypeConstant.VPN_LINK_FAR_END + " and vresfrexNearOrFarEndpointID = " + farEndDTO.getID();
//		ArrayList<VpnFRResponseExternalDTO> farEndResponseList = (ArrayList<VpnFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(VpnFRResponseExternalDTO.class, databaseConnection, conditionString);
//
//		if( farEndResponseList != null && farEndResponseList.size() != 0 ){
//
//			VpnFRResponseExternalDTO farEndResponse = farEndResponseList.get(0);
//			farEndLoopDistance = farEndResponse.getDistanceTotal();
//			farEndLoopDistanceOC = farEndResponse.getDistanceOC();
//			farEndLoopDistanceBTCL = farEndResponse.getDistanceBTCL();
//			farEndLoopDistanceCustomer = farEndResponse.getDistanceCustomer();
//		}
//		else
//		{
//			farEndLoopDistance = farEndDTO.getLoopDistanceTotal();
//			farEndLoopDistanceOC = farEndDTO.getLoopDistanceOC();
//			farEndLoopDistanceBTCL = farEndDTO.getLoopDistanceBTCL();
//			farEndLoopDistanceCustomer = farEndDTO.getLoopDistanceCustomer();
//		}
//
//		VpnEndPointDetailsDTO nearEndDetailsDTO = vpn.link.LinkUtils.getEndPointDTODetails( nearEndDTO );
//		VpnEndPointDetailsDTO farEndDetailsDTO = vpn.link.LinkUtils.getEndPointDTODetails( farEndDTO );
//
//
//		commonNote.startDescription( "Description", "Value" );
//		commonNote.addRow( "Link Name" , vpnLinkDTO.getName() );
//		commonNote.addRow( "Description" , vpnLinkDTO.getLinkDescription() );
//
//		commonNote.addRow( "Bandwith", vpnLinkDTO.getVpnBandwidth() + " " + EntityTypeConstant.linkBandwidthTypeMap.get(vpnLinkDTO.getVpnBandwidthType()) );
//		if(((commonRequestDTO.getRequestTypeID() / 100) % 100) == 2 || ((commonRequestDTO.getRequestTypeID() / 100) % 100) == 3)
//		{
//			VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO = requestUtilDAO.getExtendedRequestByRootReqID(VpnBandWidthChangeRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
//			commonNote.addRow( "Requested Bandwidth" , vpnBandWidthChangeRequestDTO.getNewBandwidth() + " " + EntityTypeConstant.linkBandwidthTypeMap.get(vpnBandWidthChangeRequestDTO.getNewBandwidthType()));
//		}
//		commonNote.addRow( "Pop to Pop distance (km)", vpnLinkDTO.getPopToPopDistance() );
//
//		commonNote.endDescription();
//
//		String[] columns = {"Description"," Local End "," Remote End "};
//
//		commonNote.startDescription( columns );
//
//		commonNote.addRow( "District", nearEndDetailsDTO.getDistrictName() , farEndDetailsDTO.getDistrictName() );
//		commonNote.addRow( "Address", nearEndDetailsDTO.getAddress(), farEndDetailsDTO.getAddress() );
//		commonNote.addRow( "POP Name", nearEndDetailsDTO.getPopName(), farEndDetailsDTO.getPopName() );
//		commonNote.addRow( "Port Type", nearEndDetailsDTO.getPortCateogryTypeName(), farEndDetailsDTO.getPortCateogryTypeName() );
//		commonNote.addRow( "Port Name", nearEndDetailsDTO.getPortName(), farEndDetailsDTO.getPortName() );
//		commonNote.addRow( "Distance O/C", nearEndLoppDistanceOC, farEndLoopDistanceOC);
//		commonNote.addRow( "Distance Customer", nearEndLoppDistanceCustomer, farEndLoopDistanceCustomer);
//		commonNote.addRow( "Distance BTCL", nearEndLoppDistanceBTCL, farEndLoopDistanceBTCL);
//		commonNote.addRow( "Distance (Total)", nearEndLoopDistance, farEndLoopDistance );
//		commonNote.addRow( "Loop Provider", EndPointConstants.providerOfOFC.get( nearEndDetailsDTO.getOfcProviderID() ), EndPointConstants.providerOfOFC.get( farEndDetailsDTO.getOfcProviderID() ) );
//
//		commonNote.endDescription();
//
//		CommonNoteDAO.insert( commonNote, databaseConnection);
//
//		//logger.debug( commonRequestDTO );
//	}
//
//
//	/**
//	 * Enters an advice note entry for the document
//	 * @author Alam
//	 * @param commonRequestDTO
//	 * @param request
//	 * @param loginDTO
//	 * @param databaseConnection
//	 * @throws Exception
//	 */
	@SuppressWarnings("unchecked")
	/*public void generateAdviceNoteLLI(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		
		LliLinkService lliLinkService = new LliLinkService();
		CommonNote commonNote = new CommonNote();
		
		int farEndLoopDistance = 0;
		int farEndLoopDistanceOC = 0;
		int farEndLoopDistanceBTCL = 0;
		int farEndLoopDistanceCustomer = 0;
		
		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
		commonNote.setEntityId( commonRequestDTO.getEntityID() );
		commonNote.setNoteTypeId( CommonNoteConstants.FR_RESPONSE.get( commonRequestDTO.getRequestTypeID() ) );
		commonNote.setReqID( commonRequestDTO.getReqID() );
		
		LliLinkDTO lliLinkDTO = (LliLinkDTO) getObjectByID( LliLinkDTO.class, commonNote.getEntityId(), databaseConnection);
		LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
		
		String conditionString = " where entityTypeID = " + EntityTypeConstant.LLI_LINK_FAR_END + " and nearOrFarEndPointID = " + farEndDTO.getID() + " order by ID desc";
		ArrayList<LliFRResponseExternalDTO> farEndResponseList = (ArrayList<LliFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(LliFRResponseExternalDTO.class, databaseConnection, conditionString);
		
		if( farEndResponseList != null && farEndResponseList.size() != 0 ){
			
			LliFRResponseExternalDTO farEndResponse = farEndResponseList.get(0);
			farEndLoopDistance = farEndResponse.getDistanceTotal();
			farEndLoopDistanceOC = farEndResponse.getDistanceOC();
			farEndLoopDistanceBTCL = farEndResponse.getDistanceBTCL();
			farEndLoopDistanceCustomer = farEndResponse.getDistanceCustomer();
		}
		else
		{
			farEndLoopDistance = farEndDTO.getLoopDistanceTotal();
			farEndLoopDistanceOC = farEndDTO.getLoopDistanceOC();
			farEndLoopDistanceBTCL = farEndDTO.getLoopDistanceBTCL();
			farEndLoopDistanceCustomer = farEndDTO.getLoopDistanceCustomer();
		}
		
		LliEndPointDetailsDTO farEndDetailsDTO = lli.link.LinkUtils.getEndPointDTODetails( farEndDTO );
		
		commonNote.startDescription( "Description", "Value" );
		
		commonNote.addRow( "Link Name" , lliLinkDTO.getName() );
		commonNote.addRow( "Description" , lliLinkDTO.getLinkDescription() );
		commonNote.addRow( "Bandwith", lliLinkDTO.getLliBandwidth() + " " + EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) );
		if(((commonRequestDTO.getRequestTypeID() / 100) % 100) == 2 || ((commonRequestDTO.getRequestTypeID() / 100) % 100) == 3)
		{
			LliBandWidthChangeRequestDTO lliBandWidthChangeRequestDTO = requestUtilDAO.getExtendedRequestByRootReqID(LliBandWidthChangeRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
			commonNote.addRow( "Requested Bandwidth" , lliBandWidthChangeRequestDTO.getNewBandwidth() + " " + EntityTypeConstant.linkBandwidthTypeMap.get(lliBandWidthChangeRequestDTO.getNewBandwidthType()));
		}
		
		

		List<IpBlock> ipList = ServiceDAOFactory.getService(IpAddressService.class).getIPAddressByEntityID(lliLinkDTO.getID());
		String statusStr = "";
		String ipStringMandatory = "";
		String ipStringAdditional = "";
		if(ipList != null)
		{								
			int ipCount = 0;
			for(IpBlock ipBlock: ipList)
			{
				if(ipBlock.getUsageType() == InventoryConstants.USAGE_ESSENTIAL)
				{
					ipStringMandatory += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
				}
				else
				{
					ipStringAdditional += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
				}
				if(++ipCount == ipList.size())
				{
					break;
				}
				ipStringMandatory += ",";
			}
		}		
		commonNote.addRow( "Mandatory IP",  ipStringMandatory);
		commonNote.addRow( "Additional IP",  ipStringAdditional);		

		
		commonNote.endDescription();
		
		String[] columns = {"Description"," Connection End "};
		
		commonNote.startDescription( columns );

		commonNote.addRow( "District",  farEndDetailsDTO.getDistrictName() );
		commonNote.addRow( "Address",  farEndDetailsDTO.getAddress() );
		commonNote.addRow( "POP Name", farEndDetailsDTO.getPopName() );
		commonNote.addRow( "Port Type", farEndDetailsDTO.getPortCateogryTypeName() );
		commonNote.addRow( "Port Name", farEndDetailsDTO.getPortName() );
		commonNote.addRow( "Loop Provider", EndPointConstants.providerOfOFC.get( farEndDetailsDTO.getOfcProviderID() ) );
		commonNote.addRow( "Distance O/C",  farEndLoopDistanceOC);
		commonNote.addRow( "Distance Customer", farEndLoopDistanceCustomer);
		commonNote.addRow( "Distance BTCL", farEndLoopDistanceBTCL);
		commonNote.addRow( "Distance (Total)", farEndLoopDistance );
		
		commonNote.endDescription();
		
		CommonNoteDAO.insert( commonNote, databaseConnection);
		
		//logger.debug( commonRequestDTO );
	}*/
	
//	public void generateDemandNoteForLinkUpgradation(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
//			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//
//		VpnLinkSearchDAO vpnLinkSearchDAO = new VpnLinkSearchDAO();
//		vpnLinkSearchDAO.issueDemanNoteForLinkUpgrade(commonRequestDTO, databaseConnection, request);
//	}
	/*
	public void generateDemandNoteForLinkUpgradationLLI(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		
		LliLinkSearchDAO lliLinkSearchDAO = new LliLinkSearchDAO();
		lliLinkSearchDAO.issueDemanNoteForLinkUpgrade(commonRequestDTO, databaseConnection, request);		
	}

	public void generateDemandNoteForLinkDowngradeLLI(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		LliLinkSearchDAO lliLinkSearchDAO = new LliLinkSearchDAO();
		lliLinkSearchDAO.issueDemanNoteForLinkDowngrade(commonRequestDTO, databaseConnection, request);
	}
	*/
//	public void generateDemandNote(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
//			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		VpnLinkSearchDAO vpnLinkSearchDAO = new VpnLinkSearchDAO();
//		vpnLinkSearchDAO.issueDemanNoteForNewLink(commonRequestDTO, databaseConnection, request);
//	}
	/*
	public void generateDemandNoteLLI(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		LliLinkSearchDAO lliLinkSearchDAO = new LliLinkSearchDAO();
		lliLinkSearchDAO.issueDemanNoteForNewLink(commonRequestDTO, databaseConnection, request);		
	}
	*/
//	public void generateDemandNoteForLinkShift(CommonRequestDTO commonRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		VpnLinkSearchDAO vpnLinkSearchDAO = new VpnLinkSearchDAO();
//		vpnLinkSearchDAO.issueDemanNoteForLinkShift(commonRequestDTO, databaseConnection, request);
//	}
	

	
//	public void createDemandNoteForNewColocation(CommonRequestDTO commonRequestDTO, HttpServletRequest request,	LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		ColocationDAO colocationDAO = new ColocationDAO();
//		colocationDAO.issueDemandForNewColocation(commonRequestDTO, ColocationConstants.COLOCATION_REQUEST_TYPE_NEW, databaseConnection);
//	}
	
	
	

	
	public short[] getCombinedDistance(HttpServletRequest request)
	{
		int KM_TO_METER = 1000;
		String totalDistanceStr = request.getParameter("totalDistance");
		short totalDistance = (short)(Double.parseDouble((totalDistanceStr != null && totalDistanceStr.length() > 0)? totalDistanceStr : "0") * KM_TO_METER);		
		String btclDistanceStr = request.getParameter("btclDistance");
		short btclDistance = (short)(Double.parseDouble((btclDistanceStr != null && btclDistanceStr.length() > 0)? btclDistanceStr : "0") * KM_TO_METER);
		String ocDistanceStr = request.getParameter("ocDistance");
		short ocDistance = (short)(Double.parseDouble((ocDistanceStr != null && ocDistanceStr.length() > 0)? ocDistanceStr : "0") * KM_TO_METER);
		String cusDistanceStr = request.getParameter("cusDistance");
		short cusDistance = (short)(Double.parseDouble((cusDistanceStr != null && cusDistanceStr.length() > 0)? cusDistanceStr : "0") * KM_TO_METER);
		
		return new short[]{totalDistance, btclDistance, ocDistance, cusDistance};
		
	}
	
	public long fourShortsToLong(short a, short b, short c, short d)
	{
		long along = (a << 16);
		along = along | (b & 0xFFFF);
		along = along << 16;
		along = along | (c & 0xFFFF);
		along = along << 16;
		along = along | (d & 0xFFFF);
		System.out.println("field " + along);
		return along;
	}
	
//	/**
//	 * This method handles request of FR response
//	 * @author Alam
//	 * @param commonRequestDTO
//	 * @param sourceRequestDTO
//	 * @param databaseConnection
//	 * @param request
//	 * @param loginDTO
//	 * @return
//	 * @throws Exception
//	 */
//	public CommonRequestDTO processExternalFRResponse( CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, DatabaseConnection databaseConnection, HttpServletRequest request, LoginDTO loginDTO) throws Exception {
//
//		VpnDAO vpnDAO = new VpnDAO();
//		VpnFRResponseExternalDTO frResponseExtDTO = new VpnFRResponseExternalDTO();
//
//		//Set some initial value from common request dto provided. Like document type, document id etc.
//		frResponseExtDTO.setFromCommonRequestDTO( commonRequestDTO );
//		frResponseExtDTO.setVersionID(1);
//		if(commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END ||
//				commonRequestDTO.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END )
//		{
//			frResponseExtDTO.setVersionID(2);
//		}
//		long currentTime = System.currentTimeMillis();
////		VpnLinkDTO vpnLinkDTO = vpnDAO.getVpnLink(commonRequestDTO.getEntityID(), databaseConnection);
//		VpnLinkDTO vpnLinkDTO = null;
//		long linkID = commonRequestDTO.getRootEntityID();
//		VpnNearEndDTO vpnNearEndDTO = null;
//		VpnFarEndDTO vpnFarEndDTO = null;
//
//
//		logger.debug("sourceRequestDTO " + sourceRequestDTO);
//
//		Long rootReqID = frResponseExtDTO.getRootReqID();
//
//		if(sourceRequestDTO != null)
//		{
//			if(sourceRequestDTO.getRootReqID() != null && sourceRequestDTO.getRootReqID() != 0)
//			{
//				rootReqID = sourceRequestDTO.getRootReqID();
//			}
//			else
//			{
//				rootReqID = sourceRequestDTO.getReqID();
//			}
//			frResponseExtDTO.setRootReqID(rootReqID);
//			frResponseExtDTO.setClientID(sourceRequestDTO.getClientID());
//		}
//		frResponseExtDTO.setLastModificationTime(currentTime);
//		frResponseExtDTO.setRequestTime(currentTime);
//		frResponseExtDTO.setRootReqIDInExtentedTable(frResponseExtDTO.getRootReqID());
//		logger.debug("before adding VpnFRResponseExternalDTO " + frResponseExtDTO);
//
//		requestUtilDAO.updateRequestByRequestID(Long.parseLong(frResponseExtDTO.getSourceRequestID()), databaseConnection );
//		CommonNote frResponse = new CommonNote();
//
//		frResponseExtDTO.setDistanceTotal( Integer.parseInt( request.getParameter( "totalDistance" ) ) );
//		frResponseExtDTO.setDistanceBTCL( Integer.parseInt( request.getParameter( "btclDistance" ) ) );
//		frResponseExtDTO.setDistanceOC( Integer.parseInt( request.getParameter( "ocDistance" ) ) );
//		frResponseExtDTO.setDistanceCustomer( Integer.parseInt( request.getParameter( "cusDistance" ) ) );
//		frResponseExtDTO.setUpazilaIDSelected(Long.parseLong( request.getParameter( "upazilaID" )));
//		frResponseExtDTO.setUnionIDSelected(Long.parseLong( request.getParameter( "unionID" )));
//		frResponseExtDTO.setPopIDSelected(Long.parseLong( request.getParameter( "popID" )));
//		frResponseExtDTO.setNearOrFarEndPointID( frResponseExtDTO.getEntityID() );
//		frResponseExtDTO.setEntityTypeIDInExtentedTable( frResponseExtDTO.getEntityTypeID() );
//
//		SqlGenerator.insert( frResponseExtDTO, VpnFRResponseExternalDTO.class, databaseConnection, true );
//
//		if(commonRequestDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_NEAR_END)
//		{
////			linkID = vpnDAO.getVpnLinkIDByNearEndID(commonRequestDTO.getEntityID(), databaseConnection);
//			vpnLinkDTO = vpnDAO.getVpnLink(linkID, databaseConnection);
//			vpnNearEndDTO =  vpnDAO.getVpnNearEndByID(vpnLinkDTO.getNearEndID(), databaseConnection);
//			vpnNearEndDTO.setLoopDistanceBTCL(frResponseExtDTO.getDistanceBTCL());
//			vpnNearEndDTO.setLoopDistanceCustomer(frResponseExtDTO.getDistanceCustomer());
//			vpnNearEndDTO.setLoopDistanceOC(frResponseExtDTO.getDistanceOC());
//			vpnNearEndDTO.setLoopDistanceTotal(frResponseExtDTO.getDistanceTotal());
//
//			if(frResponseExtDTO.getVersionID() == 1)
//			{
//				vpnNearEndDTO.setPopID(frResponseExtDTO.getPopIDSelected());
//			}
//
//			updateEntity(vpnNearEndDTO, VpnNearEndDTO.class, databaseConnection, true, false);
//		}
//		else if(commonRequestDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END)
//		{
//			linkID = vpnDAO.getVpnLinkIDByFarEndID(commonRequestDTO.getEntityID(), databaseConnection);
//			vpnLinkDTO = vpnDAO.getVpnLink(linkID, databaseConnection);
//			vpnFarEndDTO = vpnDAO.getVpnFarEndByID(vpnLinkDTO.getFarEndID(), databaseConnection);
//
//			vpnFarEndDTO.setLoopDistanceBTCL(frResponseExtDTO.getDistanceBTCL());
//			vpnFarEndDTO.setLoopDistanceCustomer(frResponseExtDTO.getDistanceCustomer());
//			vpnFarEndDTO.setLoopDistanceOC(frResponseExtDTO.getDistanceOC());
//			vpnFarEndDTO.setLoopDistanceTotal(frResponseExtDTO.getDistanceTotal());
//
//			if(frResponseExtDTO.getVersionID() == 1)
//			{
//				vpnFarEndDTO.setPopID(frResponseExtDTO.getPopIDSelected());
//			}
//
//			updateEntity(vpnFarEndDTO, VpnFarEndDTO.class, databaseConnection, true, false);
//		}
//
//
//		//Common note to contain fr response report
//
//		/********Make common note*********/
//
//		frResponse.setEntityId( frResponseExtDTO.getEntityID() );
//		frResponse.setEntityTypeId( frResponseExtDTO.getEntityTypeID() );
//		frResponse.setNoteTypeId( CommonNoteConstants.FR_RESPONSE.get( frResponseExtDTO.getRequestTypeID() ) );
//
//		String frResponseStr = "";
//
//		frResponseStr ="Link Name: <strong>" +vpnLinkDTO.getName()+"</strong><br>";
//
//		frResponseStr+="From <strong>'" + UserRepository.getInstance().getUserDTOByUserID(-commonRequestDTO.getRequestByAccountID()).getUsername() + "' </strong>  " +
//					"To <strong>'System'</strong>  " +
//					" : <strong>FR Report ("+EntityTypeConstant.entityNameMap.get(commonRequestDTO.getEntityTypeID())+") </strong> <br/>";
//		frResponse.setNoteBody( frResponseStr );
//		frResponse.startDescription( "Area Type", "Name" );
//
//		InventoryService invtService= new InventoryService();
//
//		VpnEndPointDetailsDTO endPointDTO=null;
//		if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.VPN_LINK_NEAR_END){
//			endPointDTO= vpn.link.LinkUtils.getEndPointDTODetails(vpnDAO.getVpnNearEndByID(frResponseExtDTO.getEntityID(), databaseConnection));
//		}else if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.VPN_LINK_FAR_END){
//			endPointDTO = vpn.link.LinkUtils.getEndPointDTODetails(vpnDAO.getVpnFarEndByID(frResponseExtDTO.getEntityID(), databaseConnection));
//		}
//
//		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName(), endPointDTO.getDistrictName() );
//		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getUpazilaIDSelected()).getName() );
//		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getUnionIDSelected()).getName()  );
//		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getPopIDSelected()).getName() );
//		if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.VPN_LINK_NEAR_END){
//			frResponse.addRow( "Address", vpnNearEndDTO.getAddress());
//		}
//		else if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.VPN_LINK_FAR_END){
//			frResponse.addRow( "Address", vpnFarEndDTO.getAddress());
//		}
//
//
//
//		frResponse.endDescription();
//
//		//frResponse.setNoteBody("<br>");
//		frResponse.startDescription( "Distance Type ", "Meters" );
//
//		/*frResponse.addRow( "District ", request.getParameter( "btclDistance" ) );
//
//		frResponse.addRow( "Upazila", request.getParameter( "btclDistance" ) );
//
//		frResponse.addRow( "Union", request.getParameter( "btclDistance" ) );*/
//
//		frResponse.addRow( "Distance covered by BTCL", request.getParameter( "btclDistance" ) );
//		frResponse.addRow( "Distance covered by O/C", request.getParameter( "ocDistance" ) );
//		frResponse.addRow( "Distance covered by Customer", request.getParameter( "cusDistance" ) );
//		frResponse.addRow( "Total Distance", request.getParameter( "totalDistance" ) );
//
//		frResponse.endDescription();
//
//		/*********Common note made************/
//
//		frResponse.setReqID( frResponseExtDTO.getReqID() );
//		new CommonNoteService().insert( frResponse );
//
//		frResponseStr = frResponseExtDTO.getDescription();
//		//frResponseStr += "<a target='_blank' href='../../common/note/externalFrResponseReport.jsp?id=" + frResponse.getId() + "'>View External FR Response</a>";
//
//		frResponseExtDTO.setDescription( frResponseStr );
//		SqlGenerator.updateEntity( frResponseExtDTO, VpnFRResponseExternalDTO.class, databaseConnection, true, false );
//
//		return frResponseExtDTO;
//	}
//
	
//	/**
//	 * This method handles request of FR response
//	 * @author Alam
//	 * @param commonRequestDTO
//	 * @param
//	 * @param databaseConnection
//	 * @param request
//	 * @param loginDTO
//	 * @return
//	 * @throws Exception
//	 */
	/*public CommonRequestDTO processExternalFRResponseLLI( CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, DatabaseConnection databaseConnection, HttpServletRequest request, LoginDTO loginDTO) throws Exception {

		LliDAO lliDAO = new LliDAO();
		LliFRResponseExternalDTO frResponseExtDTO = new LliFRResponseExternalDTO();
		
		//Set some initial value from common request dto provided. Like document type, document id etc.
		frResponseExtDTO.setFromCommonRequestDTO( commonRequestDTO );
		frResponseExtDTO.setVersionID(1);
		if(commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END ||
				commonRequestDTO.getRequestTypeID() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END )
		{
			frResponseExtDTO.setVersionID(2);
		}
		long currentTime = System.currentTimeMillis();
//		LliLinkDTO lliLinkDTO = lliDAO.getLliLink(commonRequestDTO.getEntityID(), databaseConnection);
		LliLinkDTO lliLinkDTO = null;
		long linkID = -1;
		LliFarEndDTO lliFarEndDTO = null;
		
		
		logger.debug("sourceRequestDTO " + sourceRequestDTO);
		
		Long rootReqID = frResponseExtDTO.getRootReqID();
		
		if(sourceRequestDTO != null)
		{
			if(sourceRequestDTO.getRootReqID() != null && sourceRequestDTO.getRootReqID() != 0)
			{
				rootReqID = sourceRequestDTO.getRootReqID();
			}
			else
			{
				rootReqID = sourceRequestDTO.getReqID();
			}
			frResponseExtDTO.setRootReqID(rootReqID);
			frResponseExtDTO.setClientID(sourceRequestDTO.getClientID());
		}
		frResponseExtDTO.setLastModificationTime(currentTime);
		frResponseExtDTO.setRequestTime(currentTime);	
		frResponseExtDTO.setRootReqIDInExtentedTable(frResponseExtDTO.getRootReqID());
		logger.debug("before adding LliFRResponseExternalDTO " + frResponseExtDTO);
		
		requestUtilDAO.updateRequestByRequestID(Long.parseLong(frResponseExtDTO.getSourceRequestID()), databaseConnection );
		CommonNote frResponse = new CommonNote();
		
		frResponseExtDTO.setDistanceTotal( Integer.parseInt( request.getParameter( "totalDistance" ) ) );
		frResponseExtDTO.setDistanceBTCL( Integer.parseInt( request.getParameter( "btclDistance" ) ) );
		frResponseExtDTO.setDistanceOC( Integer.parseInt( request.getParameter( "ocDistance" ) ) );
		frResponseExtDTO.setDistanceCustomer( Integer.parseInt( request.getParameter( "cusDistance" ) ) );
		frResponseExtDTO.setUpazilaIDSelected(Long.parseLong( request.getParameter( "upazilaID" )));
		frResponseExtDTO.setUnionIDSelected(Long.parseLong( request.getParameter( "unionID" )));
		frResponseExtDTO.setPopIDSelected(Long.parseLong( request.getParameter( "popID" )));		
		frResponseExtDTO.setNearOrFarEndPointID( frResponseExtDTO.getEntityID() );
		frResponseExtDTO.setEntityTypeIDInExtentedTable( frResponseExtDTO.getEntityTypeID() );
		
		SqlGenerator.insert( frResponseExtDTO, LliFRResponseExternalDTO.class, databaseConnection, true );
		
		if(commonRequestDTO.getEntityTypeID() == EntityTypeConstant.LLI_LINK_FAR_END)
		{
			linkID = lliDAO.getLliLinkIDByFarEndID(commonRequestDTO.getEntityID(), databaseConnection);
			lliLinkDTO = lliDAO.getLliLink(linkID, databaseConnection);
			lliFarEndDTO = lliDAO.getLliFarEndByID(lliLinkDTO.getFarEndID(), databaseConnection);
			
			lliFarEndDTO.setLoopDistanceBTCL(frResponseExtDTO.getDistanceBTCL());
			lliFarEndDTO.setLoopDistanceCustomer(frResponseExtDTO.getDistanceCustomer());
			lliFarEndDTO.setLoopDistanceOC(frResponseExtDTO.getDistanceOC());
			lliFarEndDTO.setLoopDistanceTotal(frResponseExtDTO.getDistanceTotal());
			
			if(frResponseExtDTO.getVersionID() == 1)
			{
				lliFarEndDTO.setPopID(frResponseExtDTO.getPopIDSelected());	
			}
			
			updateEntity(lliFarEndDTO, LliFarEndDTO.class, databaseConnection, true, false);
		}		

		
		//Common note to contain fr response report
		
		/********Make common note*********/
		
		/*
		frResponse.setEntityId( frResponseExtDTO.getEntityID() );

		frResponse.setEntityTypeId( frResponseExtDTO.getEntityTypeID() );
		frResponse.setNoteTypeId( CommonNoteConstants.FR_RESPONSE.get( EntityTypeConstant.LLI_LINK_FAR_END ) );
		
		String frResponseStr = "";
		
		frResponseStr ="Connection Name: <strong>" +lliLinkDTO.getName()+"</strong><br/><hr>";

		frResponseStr+="From <strong>'" + UserRepository.getInstance().getUserDTOByUserID(-commonRequestDTO.getRequestByAccountID()).getUsername() + "' </strong>  " +
					"To <strong>'System'</strong>  " +
					" : <strong>FR Report ("+EntityTypeConstant.entityNameMap.get(commonRequestDTO.getEntityTypeID())+") </strong> <br/><hr>";
		frResponse.setNoteBody( frResponseStr );
		frResponse.startDescription( "Area Type", "Name" );
		
		InventoryService invtService= new InventoryService();
		
		LliEndPointDetailsDTO endPointDTO=null;

		if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.LLI_LINK_FAR_END){
			endPointDTO = lli.link.LinkUtils.getEndPointDTODetails(lliDAO.getLliFarEndByID(frResponseExtDTO.getEntityID(), databaseConnection));
		}
		
		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName(), endPointDTO.getDistrictName() );
		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getUpazilaIDSelected()).getName() );
		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getUnionIDSelected()).getName()  );
		frResponse.addRow( InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName(), invtService.getInventoryItemByItemID(frResponseExtDTO.getPopIDSelected()).getName() );
	
		if(frResponseExtDTO.getEntityTypeID()==EntityTypeConstant.LLI_LINK_FAR_END){
			frResponse.addRow( "Address", lliFarEndDTO.getAddress());
		}
			
		
		
		frResponse.endDescription();
		
		//frResponse.setNoteBody("<br>");
		frResponse.startDescription( "Distance Type ", "Meters" );
		
		/*frResponse.addRow( "District ", request.getParameter( "btclDistance" ) );
		
		frResponse.addRow( "Upazila", request.getParameter( "btclDistance" ) );
		
		frResponse.addRow( "Union", request.getParameter( "btclDistance" ) );
		
		frResponse.addRow( "Distance covered by btcl", request.getParameter( "btclDistance" ) );
		frResponse.addRow( "Distance covered by O/C", request.getParameter( "ocDistance" ) );
//		frResponse.addRow( "Distance covered by Customer", request.getParameter( "cusDistance" ) );
		frResponse.addRow( "Total Distance", request.getParameter( "totalDistance" ) );
		
		frResponse.endDescription();
		
		/*********Common note made***********
		
		frResponse.setReqID( frResponseExtDTO.getReqID() );
		new CommonNoteService().insert( frResponse );
		
		frResponseStr = frResponseExtDTO.getDescription();
		//frResponseStr += "<a target='_blank' href='../../common/note/externalFrResponseReport.jsp?id=" + frResponse.getId() + "'>View External FR Response</a>";
		
		frResponseExtDTO.setDescription( frResponseStr );
		SqlGenerator.updateEntity( frResponseExtDTO, LliFRResponseExternalDTO.class, databaseConnection, true, false );
		return frResponseExtDTO;
	}
	
	*/
//	public void connectionOwnershipChange(CommonRequestDTO commonRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		long currentTime = System.currentTimeMillis();
//		VpnConnectionOwnerChangeRequestDTO vpnConnectionOwnerChangeRequestDTO = new VpnConnectionOwnerChangeRequestDTO();
//		vpnConnectionOwnerChangeRequestDTO.setReqID(commonRequestDTO.getReqID());
//		vpnConnectionOwnerChangeRequestDTO.setVpnConnectionID(commonRequestDTO.getEntityID());
//		vpnConnectionOwnerChangeRequestDTO.setLastModificationTime(currentTime);
//		requestDAO.changeVpnConnectionOwner(vpnConnectionOwnerChangeRequestDTO, databaseConnection);
//
//	}
	

	
	public static void main(String args[])
	{
		CommonDAO comDAO = new CommonDAO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			EntityDTO entityDTO =  new CommonDAO().getEntityDTOByEntityIDAndEntityTypeID(604,81002, databaseConnection);
			logger.debug("entityDTO " + entityDTO);
		}
		catch(Exception ex)
		{
			logger.debug("ex ", ex);
		}
		finally
		{
			databaseConnection.dbClose();
		}
		
	}
	public Collection getDTOs(Collection recordIDs, DatabaseConnection databaseConnection) throws Exception {
    	String sql = "SELECT * from at_req ";
    	if (recordIDs.size() > 0)
    	{
    		sql += " where ";
    		for (int i = 0; i < recordIDs.size(); i++)
    		{
    			sql += " arID = " + ( (ArrayList) recordIDs).get(i);
    			if (i <= recordIDs.size() - 2)
    			{
    				sql += " or ";
    			}
    		}

    	}
    	sql += " order by arID desc";
    	Collection data = new ArrayList();
    	Statement stmt = databaseConnection.getNewStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	while (rs.next())
    	{
    		CommonRequestDTO dto = new CommonRequestDTO();

    	/*	dto.setReqID(rs.getLong("arID"));
    		dto.setRequestTypeID(rs.getInt("arRequestTypeID"));

    		    		

    		dto.setClientID(rs.getLong("arClientId"));
    		dto.setEntityTypeID(rs.getInt("arRequestTypeID"));
    		dto.setRequestTime(rs.getLong("arReqTime"));
    		dto.setRequestByAccountID(rs.getLong("arRequestedByAccountID"));
    		dto.setRequestToAccountID(rs.getLong("arRequestedToAccountID"));
    		dto.setPriority(rs.getInt("arPriority"));
    		dto.setExpireTime(rs.getLong("arExpireTime"));
    		dto.setLastModificationTime(rs.getLong("arLastModificationTime"));
    		dto.setDeleted(rs.getBoolean("arIsDeleted"));
    		dto.setCompletionStatus(rs.getInt("arCompletionStatus"));*/

    		SqlGenerator.populateObjectFromDB(dto, rs);
    		data.add(dto);
    	}


        return data;
	}

//
//	public void waitForPayment(CommonRequestDTO commonRequestDTO, CommonRequestDTO sourceRequestDTO, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
//		// TODO Auto-generated method stub
//		long currentTime = System.currentTimeMillis();
//		PaymentDAO paymentDAO = new PaymentDAO();
//		PaymentDTO paymentDTO = new PaymentDTO();
//		Long rootReqID = commonRequestDTO.getRootReqID();
//		if(rootReqID == null || rootReqID == 0)
//		{
//			rootReqID = commonRequestDTO.getReqID();
//		}
//		BillDTO billDTO = billDAO.getBillByReqID(rootReqID, databaseConnection);
//		logger.debug("billDTO " + billDTO);
//		//logger.debug("request.getParameter(paymentGatewayList) " + request.getParameter("paymentGatewayList"));
//
//		logger.debug("paymentDTO " + paymentDTO);
//
//		informTeletalkForPayment(paymentDTO, commonRequestDTO, billDTO, databaseConnection);
//	}
//

	
	

	public void rejectTeletalkForPayment(CommonRequestDTO commonRequestDTO, BillDTO billDTO, DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		String teletalkAcceptUrl = PaymentConstants.TELETALK.BTCL_TO_TELETALK_ACCEPT;
		int moduleID = commonRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
		teletalkAcceptUrl += "?user=btcldomain&password=btcldomain";		
		teletalkAcceptUrl += "&module=" + ModuleConstants.ModuleMap.get(moduleID);

		
		teletalkAcceptUrl += "&request_type=" + "reject";
		teletalkAcceptUrl += "&payment_token=" + billDTO.getID();

		logger.debug("teletalkRejectUrl " + teletalkAcceptUrl);
		logger.debug("encoded teletalkRejectUrl " + teletalkAcceptUrl);
		URL url = new URL(teletalkAcceptUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setConnectTimeout(10 * 1000);
		urlConnection.setReadTimeout(10 * 1000);
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
		logger.debug("m_responseMessage " + m_responseMessage);
		boolean successfull = false;
		String successMessage = "Success";
		if (m_responseMessage.contains(successMessage)) {

		}
		else{

		}       			
	}
	
//	private void informTeletalkForPayment(PaymentDTO paymentDTO, CommonRequestDTO commonRequestDTO, BillDTO billDTO, DatabaseConnection databaseConnection) throws Exception {
//		// TODO Auto-generated method stub
//		String teletalkAcceptUrl = PaymentConstants.TELETALK.BTCL_TO_TELETALK_ACCEPT;
////		public static String teletalkAcceptUrl = "http://114.130.64.36:9999/btcl_dom/btclsend2teletalk_new.php?&module=MODULE_NAME&entity=ENTITY_NAME&request_type=REQUEST_TYPE&payment_token=PAYMENT_TOKEN
//		int moduleID = commonRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
//		teletalkAcceptUrl += "?user=btcldomain&password=btcldomain";
//		teletalkAcceptUrl += "&module=" + ModuleConstants.ModuleMap.get(moduleID);
//
//
//		teletalkAcceptUrl += "&request_type=" + "accept";
//		teletalkAcceptUrl += "&payment_token=" + billDTO.getID();
//		teletalkAcceptUrl += "&customer_id=" + commonRequestDTO.getClientID();
//		logger.debug("commonRequestDTO.getClientID() " + commonRequestDTO.getClientID() + " moduleID " + moduleID);
//
//		ClientDAO clientDAO = new ClientDAO();
//		ClientDetailsDTO clientDetailsDTO = clientDAO.getDTO(commonRequestDTO.getClientID(), moduleID, databaseConnection);
//
//		logger.debug("clientDetailsDTO " + clientDetailsDTO);
//		ClientContactDetailsDTO clientContactDetailsDTO = clientDetailsDTO.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_BILLING);
//		teletalkAcceptUrl += "&customer_name=" + URLEncoder.encode(clientContactDetailsDTO.getRegistrantsName() + " " + clientContactDetailsDTO.getRegistrantsLastName(), "utf-8");
//		teletalkAcceptUrl += "&payment_type=annual";
//		double payment_amount_db = Math.ceil(((billDTO.getNetPayable())/( 1 - PaymentConstants.TELETALK.TELETALK_CHARGE)));
//		teletalkAcceptUrl += "&payment_amount=" + payment_amount_db;
//		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		teletalkAcceptUrl += "&end_date=" + URLEncoder.encode(dateformat.format(billDTO.getLastPaymentDate()), "utf-8");
//		teletalkAcceptUrl += "&mobileno=" + URLEncoder.encode(clientDetailsDTO.getVpnContactDetails().get(ClientConstants.DETAILS_TYPE_BILLING).getPhoneNumber(), "utf-8");
//
//		logger.debug("teletalkAcceptUrl " + teletalkAcceptUrl);
//
//
//		logger.debug("encoded teletalkAcceptUrl " + teletalkAcceptUrl);
//		URL url = new URL(teletalkAcceptUrl);
//		URLConnection urlConnection = url.openConnection();
//		urlConnection.setConnectTimeout(10 * 1000);
//		urlConnection.setReadTimeout(10 * 1000);
//		URLConnection urlconnection = url.openConnection();
//		BufferedReader br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
//		String m_responseMessage = new String();
//
//		while (true) {
//			String line = br.readLine();
//			if (line != null) {
//				m_responseMessage += line;
//			} else {
//				break;
//			}
//		}
//		logger.debug("m_responseMessage " + m_responseMessage);
//		boolean successfull = false;
//		String successMessage = "Success";
//		if (m_responseMessage.contains(successMessage)) {
//
//		}
//		else{
//
//		}
//
//
//	}
	public EntityDTO getEntityDTOByEntityIDAndEntityTypeID(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection) throws Exception{
		return getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(), databaseConnection);
	}
	public EntityDTO getEntityDTOByEntityIDAndEntityTypeID(int entityTypeID, long entityID, DatabaseConnection databaseConnection) throws Exception{
		if(entityTypeID % 100 == 51)
		{
			//client
			String conditionString = " where vclClientID = " + entityID + " and vclModuleID = " + (entityTypeID / EntityTypeConstant.MULTIPLIER2);
			List<? extends EntityDTO> entityList =  getAllObjectListFullyPopulated(ClientDetailsDTO.class, databaseConnection, conditionString);
			if(entityList == null || entityList.size() == 0)
			{
				return null;
			}
			else
			{
				return entityList.get(entityList.size()-1);
			}
		}
		/*if(EntityTypeConstant.hasSuperClassMap.get(entityTypeID) != null)
		{
			EntityDTO entityDTO = (EntityDTO)getObjectByID(EntityTypeConstant.entityClassMap.get(entityTypeID), entityID, databaseConnection);
			return (EntityDTO)getObjectByID(EntityTypeConstant.hasSuperClassMap.get(entityTypeID), EntityTypeConstant.entityClassMap.get(entityTypeID),  entityID, databaseConnection);
		}
		else*/
//			return (EntityDTO)getObjectByID(EntityTypeConstant.entityClassMap.get(entityTypeID), entityID, databaseConnection);
			return (EntityDTO)getObjectFullyPopulatedByID(EntityTypeConstant.entityClassMap.get(entityTypeID),  databaseConnection, entityID);
	}
	
	
	
	public  String getActivationStatusName(int status)
	{	
		StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(status);
		if(stateDTO == null){
			return null;
		}
		return EntityTypeConstant.statusMap.get(stateDTO.getActivationStatus());
	}
	
	public  static String getActivationStatusNameStatic(int status)
	{	StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(status);
		if(stateDTO == null){
			return null;
		}
		return EntityTypeConstant.statusMap.get(stateDTO.getActivationStatus());
	}
	public void updateStatusByEntityIDAndEntityTypeID(CommonRequestDTO commonRequestDTO, int statusType, DatabaseConnection databaseConnection) throws Exception{
		int stateID = 0;
		long currentTime = System.currentTimeMillis();
		if(commonRequestDTO.getState() != 0)
		{
			stateID = commonRequestDTO.getState();
		}
		else
		{
			logger.debug("commonRequestDTO.getRequestTypeID() " + commonRequestDTO.getRequestTypeID());
			stateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(commonRequestDTO.getRequestTypeID()).getNextStateID();
		}
						
		updateStatusByEntityIDAndEntityTypeID(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(),  stateID, statusType, currentTime, databaseConnection);		
	}
	
	private void updateStatusOfClient(int entityTypeID, long entityID, int status, int statusType, long currentTime,DatabaseConnection databaseConnection) throws Exception{
	
		ArrayList<Long> idlist = new ArrayList<Long>();
		int moduleID = entityTypeID / EntityTypeConstant.MULTIPLIER2;
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(entityID, moduleID);
		ClientDetailsDTO clientDetailsDTO2 = new ClientDetailsDTO();
		clientDetailsDTO2.setClientID(clientDetailsDTO.getClientID());
		clientDetailsDTO2.setId(clientDetailsDTO.getId());
		String statusTypeStr = null;
		if(statusType == EntityTypeConstant.STATUS_LATEST)
		{	 
			statusTypeStr = "latestStatus";
			clientDetailsDTO2.setLatestStatus(status);
		}
		else if(statusType == EntityTypeConstant.STATUS_CURRENT)
		{
			statusTypeStr = "currentStatus";
			clientDetailsDTO2.setCurrentStatus(status);
		}
		clientDetailsDTO2.setLastModificationTime(currentTime);		
		updateEntityByPropertyList(clientDetailsDTO2, ClientDetailsDTO.class, databaseConnection, false, false, new String[]{statusTypeStr,"lastModificationTime"});
	}
	
	public void updateStatusOfEntity(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO, int statusType, DatabaseConnection databaseConnection) throws Exception 
	{		
		if(statusType == EntityTypeConstant.STATUS_CURRENT_AND_LATEST)
		{
			updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_CURRENT,  databaseConnection);
			updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
		}
		else
		{
			updateStatusByEntityIDAndEntityTypeID(commonRequestDTO, statusType,  databaseConnection);			
		}
		
	}
	
	public void updateStatusByEntityIDAndEntityTypeID(int entityTypeID, long entityID, int status, int statusType, long currentTime,DatabaseConnection databaseConnection) throws Exception{
		
		if(entityTypeID % 100 == 51)
		{
			updateStatusOfClient(entityTypeID, entityID, status, statusType, currentTime, databaseConnection);
			return;
		}
		
		Class classObject = EntityTypeConstant.entityClassMap.get(entityTypeID);
		EntityDTO entityDTO = (EntityDTO)SqlGenerator.getObjectByID(classObject,entityID, databaseConnection);//(EntityDTO)classObject.getConstructors()[0].newInstance();
		String statusTypeStr = null;
		if(statusType == EntityTypeConstant.STATUS_LATEST)
		{	 
			statusTypeStr = "latestStatus";
			entityDTO.setLatestStatus(status);
		}
		else if(statusType == EntityTypeConstant.STATUS_CURRENT)
		{
			statusTypeStr = "currentStatus";
			entityDTO.setCurrentStatus(status);
		}
		else if(statusType == EntityTypeConstant.STATUS_CURRENT_AND_LATEST)
		{
			updateStatusByEntityIDAndEntityTypeID(entityTypeID, entityID, status, EntityTypeConstant.STATUS_CURRENT,  currentTime, databaseConnection);
			updateStatusByEntityIDAndEntityTypeID(entityTypeID, entityID, status,  EntityTypeConstant.STATUS_LATEST,  currentTime, databaseConnection);
		}
		entityDTO.setLastModificationTime(currentTime);
		updateEntityByPropertyList(entityDTO, classObject, databaseConnection, true, false, new String[]{statusTypeStr,"lastModificationTime"});
	}

	public void uploadDocument(String []documents, LoginDTO loginDTO, CommonRequestDTO commonRequestDTO, HttpServletRequest p_request, DatabaseConnection databaseConnection) throws Exception {
		FileService fileService = new FileService();
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
			fileService.insert(fileDTO, databaseConnection);
		}
	}
	
//	public VpnFRResponseInternalDTO getLatestInternalFR(long rootReqID, DatabaseConnection databaseConnection) throws Exception
//	{
//		String conditionString = " where arRootRequestID = " + rootReqID + " and arRequestTypeID = " + VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR + " and arIsDeleted = 0 order by arID desc limit 1";
//		ArrayList<CommonRequestDTO> commonRequestDTOList = (ArrayList<CommonRequestDTO>)SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, conditionString);
//		if(commonRequestDTOList == null || commonRequestDTOList.size() == 0)
//		{
//			return null;
//		}
//		conditionString = " where vresfrinReqID = " + commonRequestDTOList.get(0).getReqID();
//		ArrayList<VpnFRResponseInternalDTO> vpnFRResponseInternalDTOList = (ArrayList<VpnFRResponseInternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(VpnFRResponseInternalDTO.class, databaseConnection, conditionString);
//		return vpnFRResponseInternalDTOList.get(0);
//	}
	

	

	
	public ArrayList<CommonRequestDTO> getBranchRootRequestIDsByMainRoot(long mainRootReqID, DatabaseConnection databaseConnection) throws Exception
	{
		String conditionString = " where arParentRequestID = " + mainRootReqID;
		ArrayList<CommonRequestDTO> commonRequestDTOList = (ArrayList<CommonRequestDTO>)SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, conditionString);
		return commonRequestDTOList;
	}
	
//	public ArrayList<VpnFRResponseExternalDTO> getLatestExternalFR(long rootReqID, DatabaseConnection databaseConnection) throws Exception
//	{
//		ArrayList<CommonRequestDTO> branchRequestDTOList = getBranchRootRequestIDsByMainRoot(rootReqID, databaseConnection);
//		logger.debug("branchRequestDTOList " + branchRequestDTOList);
//		ArrayList<VpnFRResponseExternalDTO> vpnFRResponseExternalDTOList = new ArrayList<VpnFRResponseExternalDTO>();
//
//
//
//		for(CommonRequestDTO branchRequestDTO: branchRequestDTOList)
//		{
//			int requestTypeID = VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END;
//			if(branchRequestDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END)
//			{
//				requestTypeID = VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END;
//			}
//			String conditionString = " where arRootRequestID = " + branchRequestDTO.getReqID() + " and arRequestTypeID = " + requestTypeID + " and arIsDeleted = 0 order by arID desc limit 1";
//			ArrayList<CommonRequestDTO> commonRequestDTOList = (ArrayList<CommonRequestDTO>)SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, conditionString);
//			if(commonRequestDTOList == null || commonRequestDTOList.size() == 0)
//			{
//				continue;
//			}
//			conditionString = " where vresfrexReqID = " + commonRequestDTOList.get(0).getReqID();
//			ArrayList<VpnFRResponseExternalDTO> vpnFRResponseExternalDTOListLocal = (ArrayList<VpnFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(VpnFRResponseExternalDTO.class, databaseConnection, conditionString);
//			vpnFRResponseExternalDTOList.add(vpnFRResponseExternalDTOListLocal.get(0));
//		}
//
//		return vpnFRResponseExternalDTOList;
//	}
	/*
	public ArrayList<LliFRResponseExternalDTO> getLatestExternalFR_LLI(long rootReqID, DatabaseConnection databaseConnection) throws Exception
	{		   
		ArrayList<CommonRequestDTO> branchRequestDTOList = getBranchRootRequestIDsByMainRoot(rootReqID, databaseConnection);
		logger.debug("branchRequestDTOList " + branchRequestDTOList);
		ArrayList<LliFRResponseExternalDTO> lliFRResponseExternalDTOList = new ArrayList<LliFRResponseExternalDTO>(); 

		for(CommonRequestDTO branchRequestDTO: branchRequestDTOList)
		{
			int requestTypeID = VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END;
			if(branchRequestDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END)
			{
				requestTypeID = VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END;
			}
			String conditionString = " where arRootRequestID = " + branchRequestDTO.getReqID() + " and arRequestTypeID = " + requestTypeID + " and arIsDeleted = 0 order by arID desc limit 1";
			ArrayList<CommonRequestDTO> commonRequestDTOList = (ArrayList<CommonRequestDTO>)SqlGenerator.getAllObjectListFullyPopulated(CommonRequestDTO.class, databaseConnection, conditionString);
			if(commonRequestDTOList == null || commonRequestDTOList.size() == 0)
			{
				continue;
			}
			conditionString = " where vresfrexReqID = " + commonRequestDTOList.get(0).getReqID();
			ArrayList<LliFRResponseExternalDTO> vpnFRResponseExternalDTOListLocal = (ArrayList<LliFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(LliFRResponseExternalDTO.class, databaseConnection, conditionString);
			lliFRResponseExternalDTOList.add(vpnFRResponseExternalDTOListLocal.get(0));
		}

		return lliFRResponseExternalDTOList;
	}
	*/
	
	public Object getExtendedRequest(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection) throws Exception
	{		
		int remainder = commonRequestDTO.getRequestTypeID() % 100;
		int rootRequestType = commonRequestDTO.getRequestTypeID() - remainder + 1;
//		Class classObject = DomainRequestTypeConstants.requestTypeClassNameMap.get(rootRequestType);
//		String conditionString = " where "+SqlGenerator.getForeignKeyColumnName(classObject)  +" = " +(commonRequestDTO.getRootReqID()==null?commonRequestDTO.getReqID(): commonRequestDTO.getRootReqID());
//		ArrayList<Object> list = (ArrayList<Object>)SqlGenerator.getAllObjectListFullyPopulated(classObject, databaseConnection, conditionString);
//		if(list.size() == 0)
			return null;
//		return list.get(0);
	}
	
	public boolean isActiveAndNotInAFlow(int entityTypeID, long entityID, DatabaseConnection databaseConnection) throws Exception
	{
		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, entityID, databaseConnection);
		logger.debug("entityDTO " + entityDTO);
		logger.debug("entityDTO.getCurrentStatus() " + entityDTO.getCurrentStatus());
		logger.debug("entityDTO.getLatestStatus() " + entityDTO.getLatestStatus());
		StateDTO latestStateDTO = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getLatestStatus());
		StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getCurrentStatus());
		boolean latestStatusActive = (latestStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);		
		boolean currentStatusActive = (currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);

		boolean activeEntity = StateRepository.getInstance().getStateDTOByStateID(entityDTO.getCurrentStatus()).getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE;
		boolean inAFlow = (!currentStatusActive || (currentStatusActive && !latestStatusActive));
		return activeEntity && (!inAFlow);
	}
	
	public void rollBackBills(CommonRequestDTO sourceRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		BillDAO billDAO = new BillDAO();
		List<Long> billIDList = new ArrayList<Long>();
				
		ArrayList<BillDTO> billList = (ArrayList<BillDTO>)SqlGenerator.getAllObjectList(BillDTO.class, databaseConnection, " where blReqID = " + sourceRequestDTO.getReqID() + " and blIsDeleted = 0");

		logger.debug("billList " + billList);

		if(billList.size() > 0)
		{
			for( BillDTO tempBill : billList ){
				billIDList.add( tempBill.getID() );
			}
			billDAO.deleteBillDTOByIDList(billIDList, System.currentTimeMillis(), databaseConnection);
		}
		
	}
	public void updateSourceForRollback(CommonRequestDTO sourceRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception 
	{
		long currentTime = System.currentTimeMillis();
		CommonRequestDTO commonRequestDTOLocal = sourceRequestDTO;
		Class classObject = EntityTypeConstant.requestTypeExtentendedRequestClassMap.get(sourceRequestDTO.getRequestTypeID());
		logger.debug("classObject " + classObject);
		if(classObject != null)
		{
			String conditionString = " where " + SqlGenerator.getForeignKeyColumnName(classObject) + " = " + commonRequestDTOLocal.getReqID() + " and " + SqlGenerator.getColumnName(classObject, "isDeleted") + " = 0";
			List<Object> objectList = SqlGenerator.getAllObjectListFullyPopulated(classObject, databaseConnection, conditionString);
			logger.debug("objectList " + objectList);
			if(objectList != null && objectList.size() > 0)
			{
				commonRequestDTOLocal = (CommonRequestDTO) objectList.get(0);
				/*commonRequestDTOLocal.setDeleted(true);
				commonRequestDTOLocal.setLastModificationTime(currentTime);
				SqlGenerator.updateEntity(commonRequestDTOLocal, classObject, databaseConnection, true, false);
				*/
				SqlGenerator.deleteEntity(commonRequestDTOLocal, classObject, databaseConnection, false);
				SqlGenerator.deleteEntity(commonRequestDTOLocal, CommonRequestDTO.class, databaseConnection, false);
			}
			else
			{
				classObject = null;
			}
		}

		if(classObject == null)
		{
			classObject = CommonRequestDTO.class;
			requestUtilDAO.updateRequestByRequestID(commonRequestDTOLocal.getReqID(), true, databaseConnection);//making isDeleted too
		}		
		
	}
	/*public ArrayList<CommonRequestDTO> updateSourceOfSourceForRollbackOld(CommonRequestDTO sourceRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception 
	{
		long currentTime = System.currentTimeMillis();
		ArrayList<CommonRequestDTO> sourceOfSourceRequestDTOList = new ArrayList<>();
		String sourceOfSourceRequesIDs = sourceRequestDTO.getSourceRequestID();
		if( sourceOfSourceRequesIDs != null ){
			String[] sourceRequesIDarr = sourceOfSourceRequesIDs.split( "," );
				
			for( int i = 0; i < sourceRequesIDarr.length; i++ ){
				
				CommonRequestDTO sourceOfSourceRequestDTO = (CommonRequestDTO)SqlGenerator.getObjectByID(sourceRequestDTO.getClass(), Long.parseLong(sourceRequesIDarr[i]), databaseConnection);												
				
				sourceOfSourceRequestDTOList.add(sourceOfSourceRequestDTO);
				
				CommonRequestDTO commonRequestDTOLocal = sourceOfSourceRequestDTO;
				Class classObject = EntityTypeConstant.requestTypeExtentendedRequestClassMap.get(commonRequestDTOLocal.getRequestTypeID());
				logger.debug("classObject " + classObject);
				if(classObject != null)
				{
					String conditionString = " where " + SqlGenerator.getForeignKeyColumnName(classObject) + " = " + commonRequestDTOLocal.getReqID() + " and " + SqlGenerator.getColumnName(classObject, "isDeleted") + " = 0";
					List<Object> objectList = SqlGenerator.getAllObjectListFullyPopulated(classObject, databaseConnection, conditionString);
					logger.debug("objectList " + objectList);
					
					if(objectList != null && objectList.size() > 0)
					{
						commonRequestDTOLocal = (CommonRequestDTO) objectList.get(0);
						commonRequestDTOLocal.setDeleted(true);
						commonRequestDTOLocal.setLastModificationTime(currentTime);
						SqlGenerator.updateEntity(commonRequestDTOLocal, classObject, databaseConnection, true, false);
//						updateEntityByPropertyList(commonRequestDTOLocal, classObject, databaseConnection, true, false, new String[]{"isDeleted","lastModificationTime"});
					}
					else
					{
						classObject = null;
					}
				}
				
				if(classObject == null)
				{
					requestUtilDAO.updateRequestByRequestID(sourceOfSourceRequestDTO.getReqID(), true, databaseConnection);//making isDeleted too
					classObject = CommonRequestDTO.class;
				}
				
				if(sourceOfSourceRequestDTO.getRootReqID() == null)
				{
					commonRequestDTOLocal.setRootReqID( sourceRequestDTO.getRootReqID() );
					commonRequestDTOLocal.setSourceRequestID( "" + sourceRequestDTO.getReqID() );
				}
				commonRequestDTOLocal.setDeleted(false);
				commonRequestDTOLocal.setCompletionStatus( RequestStatus.PENDING );
				commonRequestDTOLocal.setLastModificationTime(currentTime);
//				commonRequestDTOLocal.setRequestTime(currentTime);
				
				requestDAO.addRequest(commonRequestDTOLocal, loginDTO.getLoginSourceIP(), classObject, databaseConnection);
				
				CommonNoteDAO commonNoteDAO = new CommonNoteDAO();
				CommonNote commonNote = commonNoteDAO.getByRequestId(sourceRequestDTO.getReqID(), databaseConnection);
				if(commonNote != null)
				{
					commonNote.setReqID(commonRequestDTOLocal.getReqID());
					commonNoteDAO.update(commonNote, databaseConnection);
				}
				
				
				int requestTypeID = sourceRequestDTO.getRequestTypeID();
				int result = requestTypeID / EntityTypeConstant.MULTIPLIER2;
				boolean isCreativeRequest = ((result % 100) == 1);
				if(isCreativeRequest)
				{
					updateStatusByEntityIDAndEntityTypeID(commonRequestDTOLocal, EntityTypeConstant.STATUS_CURRENT_AND_LATEST,  databaseConnection);
				}
				else
				{
					updateStatusByEntityIDAndEntityTypeID(commonRequestDTOLocal, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
				}
			}
		}
		return sourceOfSourceRequestDTOList;
	}*/
	
	public ArrayList<CommonRequestDTO> updateSourceOfSourceForRollback(CommonRequestDTO sourceRequestDTO, HttpServletRequest request, LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception 
	{
		long currentTime = System.currentTimeMillis();
		ArrayList<CommonRequestDTO> sourceOfSourceRequestDTOList = new ArrayList<>();
		String sourceOfSourceRequesIDs = sourceRequestDTO.getSourceRequestID();
		if( sourceOfSourceRequesIDs != null ){
			String[] sourceRequesIDarr = sourceOfSourceRequesIDs.split( "," );
				
			for( int i = 0; i < sourceRequesIDarr.length; i++ ){
				
				CommonRequestDTO sourceOfSourceRequestDTO = (CommonRequestDTO)SqlGenerator.getObjectByID(sourceRequestDTO.getClass(), Long.parseLong(sourceRequesIDarr[i]), databaseConnection);												
				
				sourceOfSourceRequestDTOList.add(sourceOfSourceRequestDTO);
				
				CommonRequestDTO commonRequestDTOLocal = sourceOfSourceRequestDTO;
				commonRequestDTOLocal.setCompletionStatus(RequestStatus.PENDING);
				commonRequestDTOLocal.setLastModificationTime(currentTime);
				SqlGenerator.updateEntity(commonRequestDTOLocal, CommonRequestDTO.class, databaseConnection, true, false);
				
				if(UtilService.isCreativeRequest(sourceRequestDTO.getRequestTypeID()))
				{
					updateStatusByEntityIDAndEntityTypeID(commonRequestDTOLocal, EntityTypeConstant.STATUS_CURRENT_AND_LATEST,  databaseConnection);
				}
				else
				{
					updateStatusByEntityIDAndEntityTypeID(commonRequestDTOLocal, EntityTypeConstant.STATUS_LATEST,  databaseConnection);
				}
			}
		}
		return sourceOfSourceRequestDTOList;
	}
	

	public CommonRequestDTO getTerminationDummyRequest( long entityID, int entityTypeID, CommonRequestDTO bottom, LoginDTO loginDTO, DatabaseConnection databaseConnection ){
		
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		
		commonRequestDTO.setEntityID( entityID );
		commonRequestDTO.setEntityTypeID(entityTypeID);
		commonRequestDTO.setRequestTypeID(-((Math.abs(bottom.getRequestTypeID()) / 100) * 100 + 3));
		commonRequestDTO.setRootReqID( bottom.getRootReqID() == null ? bottom.getReqID() : bottom.getRootReqID() );
		commonRequestDTO.setParentReqID( bottom.getParentReqID() );
		commonRequestDTO.setClientID( bottom.getClientID() );
		
		if( loginDTO != null )
			commonRequestDTO.setRequestByAccountID( loginDTO.getAccountID() );
		else
			commonRequestDTO.setRequestByAccountID( bottom.getRequestByAccountID() );
		
		commonRequestDTO.setRequestTime( System.currentTimeMillis() );
		commonRequestDTO.setSourceRequestID( bottom.getReqID().toString() );
		commonRequestDTO.setCompletionStatus( RequestStatus.ALL_PROCESSED );
		commonRequestDTO.setLastModificationTime( System.currentTimeMillis() );
		
		return commonRequestDTO;
	}




	/*
	public void generateAdviceNoteForRequestIPAddressLLI(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		LliLinkService lliLinkService = new LliLinkService();
		CommonNote commonNote = new CommonNote();
		
		int farEndLoopDistance = 0;
		int farEndLoopDistanceOC = 0;
		int farEndLoopDistanceBTCL = 0;
		int farEndLoopDistanceCustomer = 0;
		
		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
		commonNote.setEntityId( commonRequestDTO.getEntityID() );
		commonNote.setNoteTypeId( CommonNoteConstants.FR_RESPONSE.get( commonRequestDTO.getRequestTypeID() ) );
		commonNote.setReqID( commonRequestDTO.getReqID() );
		
		LliLinkDTO lliLinkDTO = (LliLinkDTO) getObjectByID( LliLinkDTO.class, commonNote.getEntityId(), databaseConnection);
		
		String conditionString = " where " + SqlGenerator.getColumnName(CommonRequestDTO.class, "requestTypeID") + " like '%442' and " + SqlGenerator.getColumnName(CommonRequestDTO.class, "rootReqID") + " = " + commonRequestDTO.getRootReqID() + " order by " + SqlGenerator.getPrimaryKeyColumnName(CommonRequestDTO.class) + " desc limit 1";
		List<CommonRequestDTO> internalFRRequestDTOList = SqlGenerator.getAllUndeletedObjectList(CommonRequestDTO.class, databaseConnection, conditionString);
		CommonRequestDTO requestDTO = internalFRRequestDTOList.get(0);
		LliFRResponseInternalDTO lliFRResponseInternalDTO = requestUtilDAO.getExtendedRequestByRootReqID(LliFRResponseInternalDTO.class, requestDTO.getReqID(), databaseConnection);
		String ipStringMandatory = "";
		String ipStringAdditional = "";
		String[] splittedStr = lliFRResponseInternalDTO.getAdditionalIPs().split(",");
		IpAddressService ipAddressService = ServiceDAOFactory.getService(IpAddressService.class);
		for(int i = 0; i < splittedStr.length ; i++)
		{
			if(splittedStr[i].length() == 0)continue;
			IpBlock ipBlock = ipAddressService.getIPBlockByBlockID(Long.parseLong(splittedStr[i]));
			ipStringAdditional += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
			if(i < splittedStr.length - 1)
			{
				ipStringAdditional += ",";
			}
		}
		
		
		
		LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
		
		conditionString = " where entityTypeID = " + EntityTypeConstant.LLI_LINK_FAR_END + " and nearOrFarEndPointID = " + farEndDTO.getID();
		ArrayList<LliFRResponseExternalDTO> farEndResponseList = (ArrayList<LliFRResponseExternalDTO>)SqlGenerator.getAllObjectListFullyPopulated(LliFRResponseExternalDTO.class, databaseConnection, conditionString);
		
		if( farEndResponseList != null && farEndResponseList.size() != 0 ){
			
			LliFRResponseExternalDTO farEndResponse = farEndResponseList.get(0);
			farEndLoopDistance = farEndResponse.getDistanceTotal();
			farEndLoopDistanceOC = farEndResponse.getDistanceOC();
			farEndLoopDistanceBTCL = farEndResponse.getDistanceBTCL();
			farEndLoopDistanceCustomer = farEndResponse.getDistanceCustomer();
		}
		else
		{
			farEndLoopDistance = farEndDTO.getLoopDistanceTotal();
			farEndLoopDistanceOC = farEndDTO.getLoopDistanceOC();
			farEndLoopDistanceBTCL = farEndDTO.getLoopDistanceBTCL();
			farEndLoopDistanceCustomer = farEndDTO.getLoopDistanceCustomer();
		}
		
		LliEndPointDetailsDTO farEndDetailsDTO = lli.link.LinkUtils.getEndPointDTODetails( farEndDTO );
		
		commonNote.startDescription( "Description", "Value" );
		
		commonNote.addRow( "Link Name" , lliLinkDTO.getName() );
		commonNote.addRowRed( "Requested Additional IPs" , ipStringAdditional);
		commonNote.addRow( "Description" , lliLinkDTO.getLinkDescription() );
		commonNote.addRow( "Bandwith", lliLinkDTO.getLliBandwidth() + " " + EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) );

		commonNote.endDescription();
		
		String[] columns = {"Description"," Connection End "};
		
		commonNote.startDescription( columns );

		commonNote.addRow( "District",  farEndDetailsDTO.getDistrictName() );
		commonNote.addRow( "Address",  farEndDetailsDTO.getAddress() );
		commonNote.addRow( "POP Name", farEndDetailsDTO.getPopName() );
		commonNote.addRow( "Port Type", farEndDetailsDTO.getPortCateogryTypeName() );
		commonNote.addRow( "Port Name", farEndDetailsDTO.getPortName() );
		commonNote.addRow( "Loop Provider", EndPointConstants.providerOfOFC.get( farEndDetailsDTO.getOfcProviderID() ) );
		commonNote.addRow( "Distance O/C",  farEndLoopDistanceOC);
		commonNote.addRow( "Distance Customer", farEndLoopDistanceCustomer);
		commonNote.addRow( "Distance BTCL", farEndLoopDistanceBTCL);
		commonNote.addRow( "Distance (Total)", lliLinkDTO.getLliLoopDistance() );
		
		commonNote.endDescription();
		
		CommonNoteDAO.insert( commonNote, databaseConnection);
		
		//logger.debug( commonRequestDTO );
	}

	*/



	public void generateAdviceNoteForTDEnable(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) {
		
	}



/*

	public void generateAdviceNoteForTDEnableLli(CommonRequestDTO commonRequestDTO, HttpServletRequest request,
			LoginDTO loginDTO, DatabaseConnection databaseConnection) throws Exception {
		CommonNote commonNote = new CommonNote();
		
		commonNote.setEntityTypeId( commonRequestDTO.getEntityTypeID() );
		commonNote.setEntityId( commonRequestDTO.getEntityID() );
		commonNote.setNoteTypeId( CommonNoteConstants.ADVICE_NOTE_FOR_LLI_LINK );
		commonNote.setReqID( commonRequestDTO.getReqID() );
		LliLinkDisableRequestDTO lliLinkDisableRequestDTO = new RequestUtilDAO().getExtendedRequestByRootReqID(LliLinkDisableRequestDTO.class, commonRequestDTO.getRootReqID(), databaseConnection);
		logger.debug(request.getParameterMap().toString());
		
		long clientID = Long.parseLong(request.getParameter("clientID"));
		EntityDTO entityDTO = getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO, databaseConnection);
		List<String> clientDetails = getClientDetails(clientID, ModuleConstants.Module_ID_LLI);
		
		commonNote.startDescription( "Description", "Value" );
		commonNote.addRow( "Connection Name" ,entityDTO.getName() );
		commonNote.addRow( "TD Start date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkDisableRequestDTO.getTdStartDate() ));
		commonNote.addRow( "Connection Enable Date" , TimeConverter.getDateTimeStringFromDateTime(lliLinkDisableRequestDTO.getExpirationDate()) );
		commonNote.addRow( "Description" , lliLinkDisableRequestDTO.getDescription());
		commonNote.endDescription();
		
		CommonNoteDAO.insert( commonNote, databaseConnection);
	}
*/




	


	
}