<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Shift Request" /> 
	<jsp:param name="body" value="../vpn/link/vpnLinkShiftPreviewBody.jsp" />
</jsp:include> 
