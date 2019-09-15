<%@page import="common.ModuleConstants"%>
<%
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));
switch (moduleID){
case ModuleConstants.Module_ID_DOMAIN:
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainClientMenu");
	request.setAttribute("subMenu2","addDomainClientMenu"); 
	break;
case ModuleConstants.Module_ID_WEBHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_DNSHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_COLOCATION:
	request.setAttribute("menu","colocationMenu");
	request.setAttribute("subMenu1","colocationClientMenu");
	request.setAttribute("subMenu2","searchColocationClientMenu");
	break;
case ModuleConstants.Module_ID_IPADDRESS:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_VPN:
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnClient");
	request.setAttribute("subMenu2","searchVpnClient");
	break;
case ModuleConstants.Module_ID_LLI:
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliClientMenu");
	request.setAttribute("subMenu2","searchLliClientMenu");
	break;
case ModuleConstants.Module_ID_IIG:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_ADSL:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu"); 
	break;
case ModuleConstants.Module_ID_NIX:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
default:
	response.sendRedirect("applicationroot");
}
%>


<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Client Search" /> 
	<jsp:param name="body" value="../client/client/clientSearchBody.jsp" />
	<jsp:param name="moduleID" value="<%=moduleID%>" />
</jsp:include>