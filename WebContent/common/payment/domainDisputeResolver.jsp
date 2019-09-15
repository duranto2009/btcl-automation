<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

/*boolean hasPermission = false;
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
	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
*/
%>

<%
/*if((ModuleConstants.Module_ID_DOMAIN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainBillAndPayment");
	request.setAttribute("subMenu2","domainBankPayment");
}else if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","vpnBankPayment");
}*/
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Bank Dispute Resolver" /> 
	<jsp:param name="body" value="../common/payment/domainDisputeResolverBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 