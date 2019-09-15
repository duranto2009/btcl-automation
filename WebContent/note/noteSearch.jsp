<%@page import="common.ModuleConstants"%>

<%
	if ((ModuleConstants.Module_ID_DOMAIN + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "domainMenu");
		request.setAttribute("subMenu1", "domNoteSubMenu");
		request.setAttribute("subMenu2", "domSearchNoteSubMenu");
	} else if ((ModuleConstants.Module_ID_VPN + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1", "vpnNoteSubMenu");
		request.setAttribute("subMenu2", "vpnSearchNoteSubMenu");
	} else if ((ModuleConstants.Module_ID_DNSHOSTING + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1", "vpnNoteSubMenu");
		request.setAttribute("subMenu2", "vpnSearchNoteSubMenu");
	} else if ((ModuleConstants.Module_ID_COLOCATION + "").equals(request.getParameter("moduleID"))) {
		request.setAttribute("menu", "colocationMenu");
		request.setAttribute("subMenu1", "colocationNoteSubMenu");
		request.setAttribute("subMenu2", "colocationSearchNoteSubMenu");
	}
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Note Search" />
	<jsp:param name="body" value="../note/noteSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include>
