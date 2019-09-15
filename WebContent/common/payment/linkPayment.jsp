<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%
boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.BILL) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.BILL_BANK_PAYMENT ) >= PermissionConstants.PERMISSION_FULL ) ){
    	
        hasPermission=true;
    }
}

if( !hasPermission ){
	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();

	// todo: done by bony
	if(loginDTO.getUserID()<0){
		commonActionStatusDTO.setSuccessMessage( "Your request is taken for processing" , false, request );
		response.sendRedirect("lli/application/search.do");
		return;
	}else{

		commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
		response.sendRedirect("../");
		return;
	}

}


if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","vpnPaymentSearch");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","lliPaymentSearch");
}
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Bank Payment" /> 
	<jsp:param name="body" value="../common/payment/linkPaymentBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 