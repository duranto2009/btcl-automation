<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Shifting Request Preview" /> 
	<jsp:param name="body" value="../vpn/link/shiftVpnLinkPreviewBody.jsp" />
</jsp:include> 
