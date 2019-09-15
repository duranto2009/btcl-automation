<%@page import="common.ModuleConstants"%>
<%
if((ModuleConstants.Module_ID_DOMAIN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","complainMenu"+ModuleConstants.Module_ID_DOMAIN);
	request.setAttribute("subMenu2","complainSearchMenu"+ModuleConstants.Module_ID_DOMAIN);
}else if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","complainMenu"+ModuleConstants.Module_ID_VPN);
	request.setAttribute("subMenu2","complainSearchMenu"+ModuleConstants.Module_ID_VPN);
}
%>
<jsp:include page='../common/layout.jsp' flush="true">
<jsp:param name="title" value="View Complain" /> 
	<jsp:param name="body" value="../complain/viewComplainBody.jsp" />
</jsp:include>