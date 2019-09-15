<%@page import="vpn.constants.VpnRequestTypeConstants"%>

<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","shiftVpnLink");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Link Shift Details" /> 
	<jsp:param name="body" value="../vpn/link/linkShiftBodyCompact.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 