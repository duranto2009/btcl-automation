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
	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}

%>

<%
if((ModuleConstants.Module_ID_DOMAIN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainBillAndPayment");
	request.setAttribute("subMenu2","domainBankPayment");
}else if((ModuleConstants.Module_ID_WEBHOSTING+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","webHostingMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_IPADDRESS+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","ipaddressMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_COLOCATION+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","colocMenu");
	request.setAttribute("subMenu1","colocationBillPaymentSubmenu1");
	request.setAttribute("subMenu2","bankPaymentColocationBillPaymentSubmenu2");
}else if((ModuleConstants.Module_ID_IIG+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","iigMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","vpnBankPayment");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lli");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","lliBankPayment");
}else if((ModuleConstants.Module_ID_ADSL+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_NIX+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","nixMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_DNSHOSTING+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","dnshostingMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}
%>



<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Bank Payment" /> 
	<jsp:param name="body" value="../common/payment/bankPaymentBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 