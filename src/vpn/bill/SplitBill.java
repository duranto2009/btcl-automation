package vpn.bill;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import common.CommonDAO;
import common.CommonService;
import common.EntityDTO;
import common.bill.BillDTO;
import common.bill.BillService;
import requestMapping.ApiResponse;

public class SplitBill extends DispatchAction {
	
	private static String SPLIT_BILL_VIEW_PAGE = "viewPage";
	private static Logger logger = Logger.getLogger( SplitBill.class );
	private static Gson gson = new Gson();
	private PrintWriter writer = null;
	private ApiResponse apiResponse = null;
	
	private ApiResponse readExceptionAndReturnApiResponse( Exception e ) {
		
		ApiResponse apiResponse = null;
		
		if( e instanceof NumberFormatException ) {
			
			apiResponse = ApiResponse.makeErrorResponse( "Wrong module id or client id given" );
		}
		else if( e instanceof IOException ) {
			
			logger.fatal( "Could not get prinwriter to write on response" );
		}
		else if( e instanceof JsonSyntaxException ) {
			
			apiResponse = ApiResponse.makeErrorResponse( "Json syntax error, bill id or installment data are wrongly given" );
		}
		else if( e instanceof ArrayIndexOutOfBoundsException ) {
			
			apiResponse = ApiResponse.makeErrorResponse( "No Bill is given to merge" );
		}
		else {
			
			apiResponse = ApiResponse.makeErrorResponse( "Could not merge bills. See error log for details" );
		}
		
		return apiResponse;
	}
	
	public ActionForward splitBill( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ){
		
		if ( request.getMethod().equalsIgnoreCase("get") ) {
			
			return mapping.findForward( SPLIT_BILL_VIEW_PAGE );
		}
		else{
			
			try {
				response.getWriter().write( request.getParameter("name" ) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward getUnpaidBills( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		String clientIDStr = request.getParameter( "clientID" );
		String moduleIDStr = request.getParameter( "moduleID" );
		String jsonString = "";
		CommonDAO commonDAO = new CommonDAO();
		CommonService commonService = new CommonService();
		
		try{
			
			writer = response.getWriter();
			int clientID = Integer.parseInt( clientIDStr );
			int moduleID = Integer.parseInt( moduleIDStr );
			
			List<BillDTO> bills = (List<BillDTO>) new BillService().getUnPaidBillDTOListByModuleIDAndClientID( moduleID, clientID );
			
			for( BillDTO bill: bills ) {
				
				EntityDTO entity = commonService.getEntityDTOByEntityIDAndEntityTypeID( bill.getEntityTypeID(), bill.getEntityID() );
				if( entity != null )
					bill.entityName = entity.getName();
			}
			
			apiResponse = new ApiResponse( ApiResponse.API_RESPONSE_SUCCESS, bills, "" );
		}
		catch( Exception e ) {
			
			apiResponse = readExceptionAndReturnApiResponse( e );
			logger.error( e.getMessage() );
		}
		
		if( apiResponse != null ) {
			
			jsonString = gson.toJson( apiResponse );
			writer.write( jsonString );
		}
		
		return null;
	}
	
	public ActionForward mergeBills( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ){
		
		String data = request.getParameter( "data" );
		String billIDs = request.getParameter( "billID" );
		BillService billService = new BillService();
		
		try{
			
			writer = response.getWriter();
			Installment[] installments = gson.fromJson(data, Installment[].class );
			Long[] ids = gson.fromJson( billIDs, Long[].class );
			
			if( ids.length != 0 )
				billService.mergeBillsAndInsertSplittedBills( ids, installments );
			else
				throw new ArrayIndexOutOfBoundsException();
			
			apiResponse = ApiResponse.makeSuccessResponse( "Bills are merged successfully" );
			
		}
		catch (Exception e) {
			
			apiResponse = readExceptionAndReturnApiResponse( e );
			logger.error( e.getMessage() );
		}
		
		if( apiResponse != null ) {
			writer.write( gson.toJson( apiResponse ) );
		}
		
		return null;
	}
}
