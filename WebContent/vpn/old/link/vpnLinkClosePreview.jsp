<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","VpnLinkClose");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link TD Request" /> 
	<jsp:param name="body" value="../vpn/link/vpnLinkClosePreviewBody.jsp" />
</jsp:include> 
