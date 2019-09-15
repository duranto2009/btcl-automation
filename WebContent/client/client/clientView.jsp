<%@page import="common.ModuleConstants"%>

<%
switch (Integer.parseInt(request.getParameter("moduleID"))){
case ModuleConstants.Module_ID_DOMAIN:
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainClient");
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
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_IPADDRESS:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_VPN:
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnClient");
	break;
case ModuleConstants.Module_ID_LLI:
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliClientMenu");
	break;
case ModuleConstants.Module_ID_IIG:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_ADSL:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_NIX:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
}
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Client View" /> 
	<jsp:param name="body" value="../client/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 