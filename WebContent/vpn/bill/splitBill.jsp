<%@page import="common.ModuleConstants"%>
<% 
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","splitBillVPN");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","splitBillLLI");
}

%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Vpn Bill Split" /> 
	<jsp:param name="body" value="../vpn/bill/splitBillBody.jsp" />
</jsp:include> 
