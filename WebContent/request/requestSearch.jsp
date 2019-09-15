<%@page import="common.ModuleConstants"%>

<%
	if ((ModuleConstants.Module_ID_DOMAIN + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "domainMenu");
		request.setAttribute("subMenu1", "domRequestSubMenu");
		request.setAttribute("subMenu2", "domSearchRequestSubMenu");
	} else if ((ModuleConstants.Module_ID_VPN + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1", "vpnRequestSubMenu");
		request.setAttribute("subMenu2", "vpnSearchRequestSubMenu");
	} else if ((ModuleConstants.Module_ID_DNSHOSTING + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1", "vpnRequestSubMenu");
		request.setAttribute("subMenu2", "vpnSearchRequestSubMenu");
	} else if ((ModuleConstants.Module_ID_COLOCATION + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "colocationMenu");
		request.setAttribute("subMenu1", "colocationRequestSubMenu");
		request.setAttribute("subMenu2", "colocationSearchRequestSubMenu");
	}
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Request Search" />
	<jsp:param name="body" value="../request/requestSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include>
