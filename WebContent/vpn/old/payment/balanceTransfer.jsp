<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../../includes/checkLogin.jsp" %>
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
	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
%>
<%
request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","vpnLink");
request.setAttribute("subMenu2","vpnBalanceTransfer");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Balance Transfer" /> 
	<jsp:param name="body" value="../vpn/payment/balanceTransferBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 