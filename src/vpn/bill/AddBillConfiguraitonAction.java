package vpn.bill;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;

import common.bill.BillService;
import requestMapping.ApiResponse;

public class AddBillConfiguraitonAction extends DispatchAction {

	public ActionForward insert( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException{
		
		String moduleIDStr = request.getParameter( "moduleID" );
		String billTypeIDStr = request.getParameter( "billTypeID" );
		
		if( moduleIDStr == null || billTypeIDStr == null )
		{
			//new CommonActionStatusDTO().setErrorMessage( "Bill Type ID or Module ID is not given", true, request );
			//response.getOutputStream().write(  );
		}
		
		int billTypeID = Integer.parseInt( billTypeIDStr );
		int moduleID = Integer.parseInt( moduleIDStr );
		
		try{
			
			BillService.insertBillConfigurationData( moduleID, billTypeID, request );
		}
		catch( Exception e ){
			
			ApiResponse apiResponse = new ApiResponse(2, null, "Can not insert, an error occured. " + e.getMessage() );
			response.getOutputStream().print( new Gson().toJson( apiResponse ) );
			return null;
		}
		
		ApiResponse apiResponse = new ApiResponse( 1, null, "Header and footer added successfuly" ); 
		response.getOutputStream().print( new Gson().toJson( apiResponse ) );
		
		return null;
	}
	
	public ActionForward get( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException{
		
		String moduleIDStr = request.getParameter( "moduleID" );
		String billTypeIDStr = request.getParameter( "billTypeID" );
		
		if( moduleIDStr == null || billTypeIDStr == null )
		{
			//new CommonActionStatusDTO().setErrorMessage( "Bill Type ID or Module ID is not given", true, request );
			//response.getOutputStream().write(  );
		}
		
		int billTypeID = Integer.parseInt( billTypeIDStr );
		int moduleID = Integer.parseInt( moduleIDStr );
		ApiResponse apiResponse = null;
		
		try{
			
			List<BillConfigurationDTO> billConfigurationDTOs = BillService.getBillConfiguration( moduleID, billTypeID );
			
			apiResponse = new ApiResponse( 1, billConfigurationDTOs, "Data sent successfully" );
		}
		catch( Exception e ){
			
			e.printStackTrace();
			apiResponse = new ApiResponse( 1, null, "An error occured " + e.getMessage() );
			return null;
		}
		
		response.setCharacterEncoding("UTF8");
		response.setContentType("application/json");
		
		String output = new Gson().toJson( apiResponse );
		
		response.getOutputStream().write( output.getBytes( "UTF8" ) );
		
		return null;
	}
}
