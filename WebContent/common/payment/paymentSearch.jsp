<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>

<%
if((ModuleConstants.Module_ID_DOMAIN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainBillAndPayment");
	request.setAttribute("subMenu2","domainPaymentSearch");
}else if((ModuleConstants.Module_ID_WEBHOSTING+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","webHostingMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_IPADDRESS+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","ipaddressMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_COLOCATION+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","colocationMenu");
	request.setAttribute("subMenu1","colocationBillPaymentSubmenu1");
	request.setAttribute("subMenu2","paymentSearchColocationBillPaymentSubmenu2");
}else if((ModuleConstants.Module_ID_IIG+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","iigMenu");
	request.setAttribute("subMenu1","toBeWritten");
	request.setAttribute("subMenu2","toBeWritten");
}else if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","vpnPaymentSearch");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","lliPaymentSearch");
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
<jsp:param name="title" value="Payment Search" /> 
	<jsp:param name="body" value="../common/payment/paymentSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 