<%@page import="common.ModuleConstants"%>
<% 
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","billConfigurationVPN");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","billConfigurationLLI");
}

%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Vpn Bill Configuration" /> 
	<jsp:param name="body" value="../vpn/bill/billConfigurationBody.jsp" />
</jsp:include> 
