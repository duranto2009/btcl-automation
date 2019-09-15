<%@page import="common.ModuleConstants"%>
<% 
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","searchBankVPN");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lli");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","searchBankLLI");
}

%>


<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Bank" /> 
	<jsp:param name="body" value="../bank/general/searchBankBody.jsp" />
</jsp:include>