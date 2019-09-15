<%@page import="common.ModuleConstants"%>
<% 
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","addBankVPN");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lli");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","addBankLLI");
}

%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Bank" /> 
	<jsp:param name="body" value="../bank/general/addBankBody.jsp" />
</jsp:include>