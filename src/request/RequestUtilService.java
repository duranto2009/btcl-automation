package request;

import common.EntityTypeConstant;
import common.note.CommonNote;
import common.note.CommonNoteService;
import connection.DatabaseConnection;
import file.FileDAO;
import file.FileDTO;
import lli.constants.LliRequestTypeConstants;
import login.LoginDTO;
import org.apache.log4j.Logger;
import permission.StateActionDTO;
import util.SqlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestUtilService {
	Logger logger = Logger.getLogger(getClass());
	RequestUtilDAO requestUtilDAO = null;
	public RequestUtilService()
	{
		requestUtilDAO = new RequestUtilDAO();
		logger.debug("created");
	}
	public CommonRequestDTO getRequestDTOByReqID(long reqID)
	{
		logger.debug("getEntityByReqID reqID " + reqID);
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			CommonRequestDTO edto = (CommonRequestDTO) SqlGenerator.getObjectByID(CommonRequestDTO.class, reqID, databaseConnection);
			databaseConnection.dbTransationEnd();
			logger.debug("returning " + edto);
			return edto;
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
		return null;
	}
	public void updateRequestByRequestID(long requestID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			requestUtilDAO.updateRequestByRequestID(requestID, databaseConnection);
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
	}
	
	public ArrayList<StateActionDTO> getNextActionList(CommonRequestDTO commonRequestDTO, LoginDTO loginDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<StateActionDTO> stateActionList = null;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			stateActionList = requestUtilDAO.getNextActionList(commonRequestDTO, loginDTO, databaseConnection);
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
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return stateActionList;
	}
	
	public Set<CommonRequestDTO> getBottomRequestDTOsByEntity(CommonRequestDTO commonRequestDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Set<CommonRequestDTO> bottomRequestSet = null;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			bottomRequestSet = requestUtilDAO.getBottomRequestDTOsByEntity( commonRequestDTO, databaseConnection, true );
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
			throw ex;
		}
		finally
		{
			databaseConnection.dbClose();
		}
		return bottomRequestSet;
	}
	
	public static String getNoteLink( String contextPath, int requestTypeId, long requestId, int entityTypeId, long entityId ) throws Exception {
		
		String noteLink = null;
		CommonNote report = CommonNoteService.getNote( entityTypeId, entityId, requestId );
		
		switch( requestTypeId ) {
//
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_REQUEST_ADVICE_NOTE:
//			case VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE:
//			case VpnRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE:
//			case VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE:
//			case VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
//			case VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
//
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE:
			case LliRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_REQUEST_ADVICE_NOTE:
			case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE:
			case LliRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE:
			case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_REQUEST_ADVICE_NOTE:
			case LliRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE:
			case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
			case LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_REQUEST_ADVICE_NOTE:
	
				if( report != null )
					noteLink = "<a href='" + contextPath + "/common/note/noteView.jsp?id=" + report.getId() + "'> View Advice Note </a>";
				break;
				
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case VpnRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//
//			case -VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case -VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case -VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case -VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case -VpnRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
//			case -VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case -VpnRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case -VpnRequestTypeConstants.REQUEST_DOWNGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
//			case -VpnRequestTypeConstants.REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
			
			case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case LliRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case LliRequestTypeConstants.REQUEST_CONNECTION_OWNERSHIP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
			case LliRequestTypeConstants.REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
			case LliRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
				
			case -LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case -LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case -LliRequestTypeConstants.REQUEST_CONNECTION_CLOSE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case -LliRequestTypeConstants.REQUEST_CONNECTION_OWNERSHIP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case -LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR:
			case -LliRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
			case -LliRequestTypeConstants.REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
			case -LliRequestTypeConstants.REQUEST_UPGRADE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE:
				
				if( report != null )
					noteLink = "<a href='" + contextPath + "/common/reports/internalFrResponseReport.jsp?id=" + report.getId() + "'> View Internal FR </a>";
				break;
				
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END:
				
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
			case LliRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END:
			case LliRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END:
				
				List<FileDTO> files = new FileDAO().getFileByEntityType( EntityTypeConstant.REQUEST, requestId );
				noteLink = "<br/>";
				
				for( FileDTO file: files ) {
					
					noteLink += "<a href='" + contextPath + "/DownloadFile.do?documentID=" + file.getDocID() + "'> View Uploaded Document </a><br/>"; 
				}
				
				if( report != null )
					noteLink += "<a href='" + contextPath + "/common/note/externalFrResponseReport.jsp?id=" + report.getId() + "'> View External FR Response</a>";
				break;
				
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE:
//			case VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC:
//
//				return "<a href='" + contextPath + "/GetPdfBill.do?method=getPdf&reqID=" + requestId + "'> View Bill </a>";
				
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE:
			case LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE:
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE:
			case LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC:
			case LliRequestTypeConstants.REQUEST_IPADDRESS.SYSTEM_GENERATE_DEMAND_NOTE:
				
				return "<a href='" + contextPath + "/GetPdfBillLli.do?method=getPdf&reqID=" + requestId + "'> View Bill </a>";
		}
		
		
		return noteLink;
	}
	
	/**
	 * @author dhrubo
	 */
	public static CommonRequestDTO getRequestDTOByRequestID(long requestID) {
		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			commonRequestDTO = (CommonRequestDTO) SqlGenerator.getObjectByID(CommonRequestDTO.class, requestID, databaseConnection);
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally	{
			databaseConnection.dbClose();
		}
		return commonRequestDTO;
	}
}
